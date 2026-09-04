package com.needsvswants.app.ui.screens.history

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.needsvswants.app.data.db.DailyBudgetDao
import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.entitlement.EntitlementRemote
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.DailyBudgetEntity
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.DailyBudgetRepository
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.Entitlement
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var prefs: AppPreferences
    private lateinit var dao: FakeEntryDao
    private var dataStoreScope: CoroutineScope? = null
    private var dataStoreFile: File? = null

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        val file = File.createTempFile("nvw-hist-vm", ".preferences_pb")
        dataStoreFile = file
        dataStoreScope = CoroutineScope(UnconfinedTestDispatcher(dispatcher.scheduler) + Job())
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = dataStoreScope!!
        ) { file }
        prefs = AppPreferences(dataStore)
        dao = FakeEntryDao()
    }

    @After
    fun tearDown() {
        dataStoreScope?.coroutineContext?.get(Job)?.cancel()
        dataStoreFile?.delete()
        Dispatchers.resetMain()
    }

    @Test
    fun emptyDiary_dismissesSkeleton() = runTest(dispatcher) {
        val entitlements = EntitlementRepository(prefs, FakeEntitlementRemote())
        val vm = HistoryViewModel(
            entryRepository = EntryRepository(dao, entitlements),
            dailyBudgetRepository = DailyBudgetRepository(FakeDailyBudgetDao(), prefs, entitlements),
            preferences = prefs,
            entitlementRepository = entitlements,
            appContext = null
        )
        backgroundScope.launch { vm.entries.collect {} }
        advanceUntilIdle()
        assertFalse(vm.loading.value)
    }

    private class FakeEntitlementRemote : EntitlementRemote {
        override suspend fun fetchEntitlement(accessToken: String?): Entitlement? = null
    }

    private class FakeEntryDao : EntryDao {
        val entries = MutableStateFlow<List<Entry>>(emptyList())
        override suspend fun insert(entry: Entry): Long = 0
        override suspend fun insertAll(entries: List<Entry>): List<Long> = emptyList()
        override fun observeSince(since: Long): Flow<List<Entry>> =
            entries.map { list -> list.filter { it.dateUtc >= since } }
        override fun observeAll(): Flow<List<Entry>> = entries
        override fun observeForDate(date: String): Flow<List<Entry>> =
            entries.map { list -> list.filter { it.date == date } }
        override suspend fun countForDate(date: String): Int = 0
        override suspend fun deleteAll() {}
        override suspend fun delete(entry: Entry) {}
        override suspend fun update(entry: Entry) {}
        override suspend fun restore(entry: Entry): Long = 0
    }

    private class FakeDailyBudgetDao : DailyBudgetDao {
        override fun observeForDay(dayKey: String): Flow<DailyBudgetEntity?> =
            MutableStateFlow(null)
        override suspend fun getForDay(dayKey: String): DailyBudgetEntity? = null
        override fun observeAll(): Flow<List<DailyBudgetEntity>> = MutableStateFlow(emptyList())
        override suspend fun upsert(budget: DailyBudgetEntity) {}
        override suspend fun upsertAll(budgets: List<DailyBudgetEntity>) {}
        override suspend fun deleteForDay(dayKey: String) {}
        override suspend fun deleteAll() {}
        override suspend fun deleteOrphanedBefore(beforeDayKey: String) {}
    }
}
