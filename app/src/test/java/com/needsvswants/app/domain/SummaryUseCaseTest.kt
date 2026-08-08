package com.needsvswants.app.domain

import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.entitlement.EntitlementLocalStore
import com.needsvswants.app.data.entitlement.EntitlementRemote
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SummaryUseCaseTest {

    private val now = System.currentTimeMillis()
    private val millisPerDay = 24L * 60 * 60 * 1000

    @Test
    fun allPeriod_freeEntitlement_filtersEntriesOlderThan35Days() = runTest {
        val recent = entry("recent", cost = 100, dateUtc = now - 10 * millisPerDay)
        val old = entry("old", cost = 200, dateUtc = now - 40 * millisPerDay)
        val dao = FakeEntryDao(listOf(recent, old))
        val repo = EntitlementRepository(FakeLocal(Entitlement.Free), FakeRemote())
        val useCase = SummaryUseCase(dao, repo)

        val stats = useCase.getStats(Period.ALL).first()

        assertEquals(100, stats.totalCents)
        assertEquals(1, stats.needsCount + stats.wantsCount)
    }

    @Test
    fun allPeriod_proEntitlement_includesAllEntries() = runTest {
        val recent = entry("recent", cost = 100, dateUtc = now - 10 * millisPerDay)
        val old = entry("old", cost = 200, dateUtc = now - 40 * millisPerDay)
        val dao = FakeEntryDao(listOf(recent, old))
        val pro = Entitlement(
            tier = EntitlementTier.PRO,
            type = EntitlementType.PAID,
            expiresAtEpochMillis = null
        )
        val repo = EntitlementRepository(FakeLocal(pro), FakeRemote())
        val useCase = SummaryUseCase(dao, repo)

        val stats = useCase.getStats(Period.ALL).first()

        assertEquals(300, stats.totalCents)
        assertEquals(2, stats.needsCount + stats.wantsCount)
    }

    @Test
    fun monthPeriod_includesLastWhenWithin30Days_butNotOlder() = runTest {
        val withinMonth = entry("within", cost = 100, dateUtc = now - 20 * millisPerDay)
        val olderThanMonth = entry("old", cost = 200, dateUtc = now - 40 * millisPerDay)
        val dao = FakeEntryDao(listOf(withinMonth, olderThanMonth))
        val repo = EntitlementRepository(FakeLocal(Entitlement.Free), FakeRemote())
        val useCase = SummaryUseCase(dao, repo)

        val stats = useCase.getStats(Period.MONTH).first()

        assertEquals(100, stats.totalCents)
        assertEquals(1, stats.needsCount + stats.wantsCount)
    }

    private fun entry(item: String, cost: Long, dateUtc: Long): Entry = Entry(
        dateUtc = dateUtc,
        date = "d",
        time = "t",
        item = item,
        costCents = cost,
        type = EntryType.NEED
    )

    private class FakeEntryDao(private val entries: List<Entry>) : EntryDao {
        override suspend fun insert(entry: Entry): Long = 0L
        override fun observeSince(since: Long): Flow<List<Entry>> =
            flowOf(entries.filter { it.dateUtc >= since })
        override fun observeAll(): Flow<List<Entry>> = flowOf(entries)
        override suspend fun purgeBefore(before: Long): Int = 0
        override suspend fun countForDate(date: String): Int = 0
        override suspend fun deleteAll() {}
        override suspend fun delete(entry: Entry) {}
    }

    private class FakeLocal(initial: Entitlement) : EntitlementLocalStore {
        private val state = MutableStateFlow(initial)
        private val synced = MutableStateFlow(
            if (initial.hasProAccessAt(System.currentTimeMillis())) System.currentTimeMillis() else 0L
        )
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

    private class FakeRemote : EntitlementRemote {
        override suspend fun fetchEntitlement(accessToken: String?): Entitlement? = null
    }
}