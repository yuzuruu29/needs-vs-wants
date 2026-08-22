package com.needsvswants.app.data.repository

import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.entitlement.EntitlementLocalStore
import com.needsvswants.app.data.entitlement.EntitlementRemote
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pins the P0 data-loss contract: Free-tier retention (D122) is a QUERY
 * VISIBILITY BOUNDARY inside [EntryRepository], never a physical deletion.
 *
 * A paid snapshot older than the 7-day sync grace ([com.needsvswants.app.data.entitlement.trustedLocalEntitlement])
 * degrades to Free. That must hide entries older than 30 days from every
 * repository read while every row stays physically stored, so a later
 * successful verification makes lifetime history reappear. The old startup
 * purgeBefore path destroyed those rows instead; these tests must fail if any
 * entitlement-derived deletion ever comes back.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class EntryRepositoryRetentionTest {

    private val now = System.currentTimeMillis()
    private val millisPerDay = 24L * 60 * 60 * 1000

    @Test
    fun stalePaidSnapshot_hidesEntriesOlderThan30Days_butEveryRowStaysStored() = runTest {
        // Paid snapshot last verified 8 days ago -> past the 7-day grace ->
        // degrades to Free. Exactly the state that used to trigger the startup
        // purge and destroy lifetime history.
        val dao = RecordingFakeEntryDao(
            listOf(entry("recent", daysAgo = 10), entry("old", daysAgo = 40))
        )
        val repo = EntryRepository(dao, entitlements(syncedDaysAgo = 8))

        val visible = repo.observeAll().first()

        assertEquals(listOf("recent"), visible.map { it.item })
        assertEquals(2, dao.store.value.size)
        assertTrue(dao.destructiveCalls.isEmpty())
    }

    @Test
    fun upgradingToPaid_makesHiddenEntriesVisibleAgain() = runTest {
        val dao = RecordingFakeEntryDao(
            listOf(entry("recent", daysAgo = 10), entry("old", daysAgo = 40))
        )
        val local = FakeLocalStore(paidProSnapshot(), syncedAtMillis = now - 8 * millisPerDay)
        val repo = EntryRepository(dao, EntitlementRepository(local, FakeRemote()))

        assertEquals(listOf("recent"), repo.observeAll().first().map { it.item })

        // Successful remote verification stamps the sync clock; nothing was
        // deleted meanwhile, so the full ledger reappears untouched.
        local.markEntitlementSynced(now)

        assertEquals(
            setOf("recent", "old"),
            repo.observeAll().first().map { it.item }.toSet()
        )
        assertEquals(2, dao.store.value.size)
        assertTrue(dao.destructiveCalls.isEmpty())
    }

    @Test
    fun confirmedFree_hidesOldEntries_andStoreRowCountIsUnchanged() = runTest {
        val old = entry("old", daysAgo = 40)
        val dao = RecordingFakeEntryDao(
            listOf(entry("recent", daysAgo = 10), old)
        )
        val repo = EntryRepository(dao, entitlements(syncedDaysAgo = 0, snapshot = Entitlement.Free))

        val visible = repo.observeAll().first()

        assertEquals(listOf("recent"), visible.map { it.item })
        // Hidden does not mean gone: both rows are still physically stored.
        assertEquals(2, dao.store.value.size)
        assertEquals(old, dao.store.value[1])
        assertTrue(dao.destructiveCalls.isEmpty())
    }

    @Test
    fun observeSince_respectsTheSameVisibilityBoundary() = runTest {
        val dao = RecordingFakeEntryDao(
            listOf(entry("recent", daysAgo = 10), entry("old", daysAgo = 40))
        )
        val repo = EntryRepository(dao, entitlements(syncedDaysAgo = 8))

        // Even an unbounded DAO window comes back bounded for Free.
        assertEquals(listOf("recent"), repo.observeSince(0L).first().map { it.item })

        // A paid tier gets the full requested window back.
        val paidRepo = EntryRepository(dao, entitlements(syncedDaysAgo = 0))
        assertEquals(
            setOf("recent", "old"),
            paidRepo.observeSince(0L).first().map { it.item }.toSet()
        )
    }

    @Test
    fun startupReadSequence_withStaleSnapshot_performsNoDeletion() = runTest {
        // Replays the exact reads the old NeedsVsWantsApp.onCreate performed
        // around its purgeBefore call (entitlement read + ledger reads) under
        // the stale-snapshot condition that used to wipe the ledger. Startup
        // must be side-effect free on stored rows.
        val dao = RecordingFakeEntryDao(
            listOf(entry("recent", daysAgo = 10), entry("old", daysAgo = 40))
        )
        val entitlements = EntitlementRepository(
            FakeLocalStore(paidProSnapshot(), syncedAtMillis = now - 8 * millisPerDay),
            FakeRemote()
        )
        val repo = EntryRepository(dao, entitlements)

        val effective = entitlements.entitlement.first()
        assertFalse(effective.hasProAccessAt(now))
        repo.observeAll().first()
        repo.observeSince(now - 60 * millisPerDay).first()
        repo.countForDate("d")

        assertEquals(2, dao.store.value.size)
        assertTrue(dao.destructiveCalls.isEmpty())
    }

    // --- helpers ---------------------------------------------------------------

    private fun paidProSnapshot(): Entitlement = Entitlement(
        tier = EntitlementTier.PRO,
        type = EntitlementType.PAID,
        expiresAtEpochMillis = null
    )

    /** Repository over a local store whose paid snapshot was synced [syncedDaysAgo] ago. */
    private fun entitlements(
        syncedDaysAgo: Long,
        snapshot: Entitlement = paidProSnapshot()
    ): EntitlementRepository = EntitlementRepository(
        FakeLocalStore(snapshot, syncedAtMillis = now - syncedDaysAgo * millisPerDay),
        FakeRemote()
    )

    private fun entry(item: String, daysAgo: Int): Entry = Entry(
        dateUtc = now - daysAgo * millisPerDay,
        date = "d",
        time = "t",
        item = item,
        costCents = 100L,
        type = EntryType.NEED
    )

    /**
     * DAO fake that mirrors Room's storage contract closely enough for the
     * retention tests: rows persist until explicitly destroyed, and every
     * destructive call is recorded so tests can prove none happened.
     */
    private class RecordingFakeEntryDao(seed: List<Entry>) : EntryDao {
        val store = MutableStateFlow(seed)
        val destructiveCalls = mutableListOf<String>()

        override suspend fun insert(entry: Entry): Long {
            store.value = store.value + entry
            return store.value.size.toLong()
        }

        override suspend fun insertAll(entries: List<Entry>): List<Long> {
            store.value = store.value + entries
            return entries.map { it.id }
        }

        override fun observeSince(since: Long): Flow<List<Entry>> =
            store.map { list -> list.filter { it.dateUtc >= since } }

        override fun observeAll(): Flow<List<Entry>> = store

        override suspend fun countForDate(date: String): Int =
            store.value.count { it.date == date }

        override suspend fun deleteAll() {
            destructiveCalls.add("deleteAll")
            store.value = emptyList()
        }

        override suspend fun delete(entry: Entry) {
            destructiveCalls.add("delete")
            store.value = store.value.filterNot { it.id == entry.id }
        }

        override suspend fun update(entry: Entry) {
            store.value = store.value.map { if (it.id == entry.id) entry else it }
        }

        override suspend fun restore(entry: Entry): Long {
            store.value = store.value + entry
            return entry.id
        }
    }

    private class FakeLocalStore(
        initial: Entitlement,
        syncedAtMillis: Long
    ) : EntitlementLocalStore {
        private val state = MutableStateFlow(initial)
        private val synced = MutableStateFlow(syncedAtMillis)

        override val entitlement: Flow<Entitlement> = state
        override val entitlementSyncedAtMillis: Flow<Long> = synced

        override suspend fun setEntitlement(entitlement: Entitlement) {
            state.value = entitlement
        }

        override suspend fun markEntitlementSynced(atMillis: Long) {
            synced.value = atMillis
        }

        override suspend fun clearEntitlement() {
            state.value = Entitlement()
            synced.value = 0L
        }
    }

    private class FakeRemote(private val value: Entitlement? = null) : EntitlementRemote {
        override suspend fun fetchEntitlement(accessToken: String?): Entitlement? = value
    }
}
