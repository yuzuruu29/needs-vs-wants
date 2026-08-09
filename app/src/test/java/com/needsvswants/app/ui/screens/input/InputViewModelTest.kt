package com.needsvswants.app.ui.screens.input

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.needsvswants.app.ads.NoOpRewardedAdGateway
import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.DailyBudgetUseCase
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import com.needsvswants.app.domain.QuotaState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Regression tests for the InputViewModel quota-gate wiring (D120).
 *
 * `quotaState` and `entitlement` are read via `.value` with no collector
 * attached, so they must be built with `SharingStarted.Eagerly`: an
 * uncollected `WhileSubscribed` StateFlow never starts its upstream and stays
 * frozen at the initial value, which silently disabled the daily quota gate
 * for Free users and treated Pro/Max as Free. These tests pin that wiring
 * with a real in-memory PreferenceDataStore (plain JVM, no Robolectric).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class InputViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var prefs: AppPreferences
    private lateinit var dao: FakeEntryDao
    private lateinit var repository: EntryRepository
    private var dataStoreScope: CoroutineScope? = null
    private var dataStoreFile: File? = null

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        val file = File.createTempFile("nvw-input-vm", ".preferences_pb")
        dataStoreFile = file
        dataStoreScope = CoroutineScope(UnconfinedTestDispatcher(dispatcher.scheduler) + Job())
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = dataStoreScope!!
        ) { file }
        prefs = AppPreferences(dataStore)
        dao = FakeEntryDao()
        repository = EntryRepository(dao)
    }

    @After
    fun tearDown() {
        // Cancel the DataStore actor first and drain the scheduler while Main is
        // still installed: its final write resumes collector continuations on
        // Dispatchers.Main, and resetMain() first would throw "Main dispatcher
        // failed to initialize" from inside the cancellation path.
        dataStoreScope?.cancel()
        dataStoreScope = null
        dispatcher.scheduler.advanceUntilIdle()
        Dispatchers.resetMain()
        dataStoreFile?.let { runCatching { it.delete() } }
    }

    private fun buildViewModel(): InputViewModel = InputViewModel(
        entries = repository,
        preferences = prefs,
        dailyBudgetUseCase = DailyBudgetUseCase(dao, prefs),
        rewardedAdGateway = NoOpRewardedAdGateway(),
        appContext = null
    )

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private fun seedEntry(
        dateUtc: Long = System.currentTimeMillis(),
        item: String = "Item",
        costCents: Long = 100L,
        type: EntryType = EntryType.NEED
    ): Entry = Entry(
        dateUtc = dateUtc,
        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(dateUtc)),
        time = "12:00",
        item = item,
        costCents = costCents,
        type = type
    )

    private fun fillForm(vm: InputViewModel) {
        vm.activeItem.value = "Coffee"
        vm.activeCost.value = "180"
        vm.activeType.value = EntryType.WANT
    }

    @Test
    fun quotaBlocked_at5Used_doesNotInsert() = runTest(dispatcher) {
        prefs.setQuotaState(QuotaState(today(), logsCreated = 5, bonusLogs = 0, adsWatched = 0))
        val vm = buildViewModel()
        advanceUntilIdle()

        fillForm(vm)
        vm.trySeal()

        assertNotNull(vm.quotaBlocked.value)
        assertTrue(dao.entries.value.isEmpty())
        assertEquals(5, vm.quotaState.value.logsCreated)
    }

    @Test
    fun seals_andIncrements_at4Used() = runTest(dispatcher) {
        prefs.setQuotaState(QuotaState(today(), logsCreated = 4, bonusLogs = 0, adsWatched = 0))
        val vm = buildViewModel()
        advanceUntilIdle()

        fillForm(vm)
        vm.trySeal()
        advanceUntilIdle()

        val sealed = dao.entries.value.single()
        assertEquals("Coffee", sealed.item)
        assertEquals(18000L, sealed.costCents)
        assertEquals(EntryType.WANT, sealed.type)
        assertEquals(5, vm.quotaState.value.logsCreated)
        assertNull(vm.quotaBlocked.value)
    }

    @Test
    fun pro_bypassesQuota_andSheetLimit() = runTest(dispatcher) {
        prefs.setQuotaState(QuotaState(today(), logsCreated = 5, bonusLogs = 0, adsWatched = 0))
        prefs.setEntitlement(
            Entitlement(
                tier = EntitlementTier.PRO,
                type = EntitlementType.PAID,
                expiresAtEpochMillis = System.currentTimeMillis() + 86_400_000L
            )
        )
        dao.entries.value = List(21) { seedEntry(dateUtc = System.currentTimeMillis() - it) }
        val vm = buildViewModel()
        backgroundScope.launch { vm.sheetEntries.collect {} }
        advanceUntilIdle()

        fillForm(vm)
        vm.trySeal()
        advanceUntilIdle()

        assertEquals(22, dao.entries.value.size)
        assertFalse(vm.isSheetFull)
        assertEquals(5, vm.quotaState.value.logsCreated)
    }

    @Test
    fun free_at20Entries_cannotSeal() = runTest(dispatcher) {
        dao.entries.value = List(20) { seedEntry(dateUtc = System.currentTimeMillis() - it) }
        val vm = buildViewModel()
        backgroundScope.launch { vm.sheetEntries.collect {} }
        advanceUntilIdle()
        assertEquals(20, vm.sheetEntries.value.size)

        fillForm(vm)
        vm.trySeal()
        advanceUntilIdle()

        assertEquals(20, dao.entries.value.size)
        assertTrue(vm.isSheetFull)
        assertNull(vm.quotaBlocked.value)
        assertEquals(0, vm.quotaState.value.logsCreated)
    }

    @Test
    fun quotaState_reflectsDatastoreWrite_withoutCollectors() = runTest(dispatcher) {
        val vm = buildViewModel()
        advanceUntilIdle()
        assertEquals(0, vm.quotaState.value.logsCreated)

        // Never collect vm.quotaState — the exact regression this bug class
        // represents: an uncollected WhileSubscribed StateFlow stays frozen at
        // the initial value and never reflects DataStore writes.
        prefs.setQuotaState(QuotaState(today(), logsCreated = 2, bonusLogs = 0, adsWatched = 0))
        advanceUntilIdle()

        assertEquals(2, vm.quotaState.value.logsCreated)
    }

    private class FakeEntryDao : EntryDao {
        val entries = MutableStateFlow<List<Entry>>(emptyList())

        override suspend fun insert(entry: Entry): Long {
            entries.value = entries.value + entry
            return entries.value.size.toLong()
        }

        override fun observeSince(since: Long): Flow<List<Entry>> =
            entries.map { list -> list.filter { it.dateUtc >= since } }

        override fun observeAll(): Flow<List<Entry>> = entries

        override suspend fun purgeBefore(before: Long): Int {
            val (kept, removed) = entries.value.partition { it.dateUtc >= before }
            entries.value = kept
            return removed.size
        }

        override suspend fun countForDate(date: String): Int =
            entries.value.count { it.date == date }

        override suspend fun deleteAll() {
            entries.value = emptyList()
        }

        override suspend fun delete(entry: Entry) {
            entries.value = entries.value.filterNot { it == entry }
        }
    }
}
