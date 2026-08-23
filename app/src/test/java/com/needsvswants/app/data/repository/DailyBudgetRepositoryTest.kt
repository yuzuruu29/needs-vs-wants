package com.needsvswants.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.needsvswants.app.data.db.DailyBudgetDao
import com.needsvswants.app.data.entitlement.EntitlementLocalStore
import com.needsvswants.app.data.entitlement.EntitlementRemote
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.DailyBudgetEntity
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import com.needsvswants.app.domain.LocalDayKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("DEPRECATION")
class DailyBudgetRepositoryTest {
    private lateinit var prefs: AppPreferences
    private lateinit var dao: FakeDailyBudgetDao
    private lateinit var local: FakeEntitlementLocal
    private lateinit var repository: DailyBudgetRepository
    private var dataStoreScope: CoroutineScope? = null
    private var dataStoreFile: File? = null

    @Before
    fun setUp() {
        val file = File.createTempFile("nvw-budget-repo", ".preferences_pb")
        dataStoreFile = file
        dataStoreScope = CoroutineScope(UnconfinedTestDispatcher() + Job())
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = dataStoreScope!!
        ) { file }
        prefs = AppPreferences(dataStore)
        dao = FakeDailyBudgetDao()
        local = FakeEntitlementLocal()
        repository = DailyBudgetRepository(
            dao = dao,
            preferences = prefs,
            entitlements = EntitlementRepository(local, NoOpEntitlementRemote)
        )
    }

    @After
    fun tearDown() {
        dataStoreScope?.cancel()
        dataStoreScope = null
        dataStoreFile?.let { runCatching { it.delete() } }
    }

    @Test
    fun legacyBudget_migratesToToday_andRepeatedMigrationDoesNotOverwriteCurrentEdit() = runTest {
        val now = System.currentTimeMillis()
        val today = LocalDayKey.today(now)
        prefs.setDailyBudgetCents(50_000L)

        repository.migrateLegacyBudgetIfNeeded(now)
        assertEquals(50_000L, dao.getForDay(today)!!.budgetCents)
        assertNull(prefs.legacyDailyBudgetCents.first())

        // A second legacy value cannot replace a current-day Room edit.
        prefs.setDailyBudgetCents(60_000L)
        repository.migrateLegacyBudgetIfNeeded(now)
        assertEquals(50_000L, dao.getForDay(today)!!.budgetCents)
        assertNull(prefs.legacyDailyBudgetCents.first())
    }

    @Test
    fun budgets_are_independent_by_localDayKey_andStoredAsCents() = runTest {
        val now = System.currentTimeMillis()
        val today = LocalDayKey.today(now)
        val yesterday = LocalDayKey.daysAgo(now, 1)

        repository.setForDay(today, 12_345L)
        repository.setForDay(yesterday, 67_890L)

        assertEquals(12_345L, repository.getForDay(today))
        assertEquals(67_890L, repository.getForDay(yesterday))
        assertTrue(dao.budgets.value.all { it.budgetCents > 0L })
    }

    @Test
    fun freeHistory_isThirtyCalendarDays_andPaidHistory_isLifetime() = runTest {
        val now = System.currentTimeMillis()
        val old = LocalDayKey.daysAgo(now, 35)
        val recent = LocalDayKey.daysAgo(now, 2)
        repository.setForDay(old, 10_000L)
        repository.setForDay(recent, 20_000L)

        assertEquals(setOf(recent), repository.observeVisibleBudgets().first().map { it.dayKey }.toSet())

        local.snapshot.value = Entitlement(
            tier = EntitlementTier.PRO,
            type = EntitlementType.PAID,
            expiresAtEpochMillis = null
        )
        local.syncedAt.value = now
        assertEquals(
            setOf(old, recent),
            repository.observeVisibleBudgets().first().map { it.dayKey }.toSet()
        )
    }

    @Test
    fun freePrune_removesOnlyExpiredOrphanedBudgets() = runTest {
        val now = System.currentTimeMillis()
        val old = LocalDayKey.daysAgo(now, 35)
        repository.setForDay(old, 10_000L)
        dao.linkedDays += old

        repository.pruneExpiredOrphanedBudgets(now)
        assertEquals(10_000L, repository.getForDay(old))

        dao.linkedDays.clear()
        repository.pruneExpiredOrphanedBudgets(now)
        assertNull(repository.getForDay(old))
    }

    private object NoOpEntitlementRemote : EntitlementRemote {
        override suspend fun fetchEntitlement(accessToken: String?): Entitlement? = null
    }

    private class FakeEntitlementLocal : EntitlementLocalStore {
        val snapshot = MutableStateFlow(Entitlement.Free)
        val syncedAt = MutableStateFlow(0L)

        override val entitlement: Flow<Entitlement> = snapshot
        override val entitlementSyncedAtMillis: Flow<Long> = syncedAt
        override suspend fun setEntitlement(entitlement: Entitlement) {
            snapshot.value = entitlement
        }
        override suspend fun markEntitlementSynced(atMillis: Long) {
            syncedAt.value = atMillis
        }
        override suspend fun clearEntitlement() {
            snapshot.value = Entitlement.Free
            syncedAt.value = 0L
        }
    }

    private class FakeDailyBudgetDao : DailyBudgetDao {
        val budgets = MutableStateFlow<List<DailyBudgetEntity>>(emptyList())
        val linkedDays = mutableSetOf<String>()

        override fun observeForDay(dayKey: String): Flow<DailyBudgetEntity?> =
            budgets.map { list -> list.firstOrNull { it.dayKey == dayKey } }

        override suspend fun getForDay(dayKey: String): DailyBudgetEntity? =
            budgets.value.firstOrNull { it.dayKey == dayKey }

        override fun observeAll(): Flow<List<DailyBudgetEntity>> = budgets

        override suspend fun upsert(budget: DailyBudgetEntity) {
            budgets.value = budgets.value.filterNot { it.dayKey == budget.dayKey } + budget
        }

        override suspend fun upsertAll(budgets: List<DailyBudgetEntity>) {
            val current = this.budgets.value
            this.budgets.value = current
                .filterNot { old -> budgets.any { it.dayKey == old.dayKey } } + budgets
        }

        override suspend fun deleteForDay(dayKey: String) {
            budgets.value = budgets.value.filterNot { it.dayKey == dayKey }
        }

        override suspend fun deleteAll() {
            budgets.value = emptyList()
        }

        override suspend fun deleteOrphanedBefore(beforeDayKey: String) {
            budgets.value = budgets.value.filter {
                it.dayKey >= beforeDayKey || it.dayKey in linkedDays
            }
        }
    }
}
