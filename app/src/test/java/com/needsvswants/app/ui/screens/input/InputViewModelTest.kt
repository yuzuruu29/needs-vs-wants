package com.needsvswants.app.ui.screens.input

import android.app.Activity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.needsvswants.app.ads.NoOpRewardedAdGateway
import com.needsvswants.app.ads.RewardedAdGateway
import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.DailyBudgetUseCase
import com.needsvswants.app.domain.DailyLogQuota
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import com.needsvswants.app.domain.QuotaState
import com.needsvswants.app.domain.ReceiptOcrProcessor
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
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
import kotlinx.coroutines.test.TestScope
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

    private fun buildViewModel(
        rewardedAds: RewardedAdGateway = NoOpRewardedAdGateway()
    ): InputViewModel = InputViewModel(
        entries = repository,
        preferences = prefs,
        dailyBudgetUseCase = DailyBudgetUseCase(dao, prefs),
        rewardedAds = rewardedAds,
        appContext = null,
        receiptOcrProcessor = ReceiptOcrProcessor { Result.success(com.needsvswants.app.domain.ReceiptScanResult()) }
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
        prefs.setQuotaState(QuotaState(today(), logsCreated = 5, carriedLogs = 0))
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
        prefs.setQuotaState(QuotaState(today(), logsCreated = 4, carriedLogs = 0))
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
        prefs.setQuotaState(QuotaState(today(), logsCreated = 5, carriedLogs = 0))
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
        prefs.setQuotaState(QuotaState(today(), logsCreated = 2, carriedLogs = 0))
        advanceUntilIdle()

        assertEquals(2, vm.quotaState.value.logsCreated)
    }

    // --- Max coach gate (Task 3): intercept ONLY Max + WANT + hold -------------

    private suspend fun TestScope.buildMaxViewModel(): InputViewModel {
        prefs.setEntitlement(
            Entitlement(
                tier = EntitlementTier.MAX,
                type = EntitlementType.PAID,
                expiresAtEpochMillis = System.currentTimeMillis() + 86_400_000L
            )
        )
        val vm = buildViewModel()
        backgroundScope.launch { vm.sheetEntries.collect {} }
        // dailyBudgetCents is WhileSubscribed; without a collector it freezes at
        // null and the coach would see budget Off. Collect it like the screen does.
        backgroundScope.launch { vm.dailyBudgetCents.collect {} }
        vm.saveDailyBudget("100")
        advanceUntilIdle()
        assertEquals(100_00L, vm.dailyBudgetCents.value)
        return vm
    }

    @Test
    fun max_wantOver15PercentOfRemaining_holdsSeal_andSetsCoachHold() = runTest(dispatcher) {
        val vm = buildMaxViewModel()

        vm.activeItem.value = "Sneakers"
        vm.activeCost.value = "3000"
        vm.activeType.value = EntryType.WANT
        vm.trySeal()
        advanceUntilIdle()

        assertTrue(dao.entries.value.isEmpty())
        assertNotNull(vm.coachHold.value)
        assertTrue(vm.coachHold.value!!.hold)
        assertTrue(vm.coachHold.value!!.reason.isNotBlank())
        assertTrue(vm.coachHold.value!!.citation.contains("Section"))
    }

    @Test
    fun max_wantWithin15PercentOfRemaining_sealsImmediately_withoutCoachHold() = runTest(dispatcher) {
        val vm = buildMaxViewModel()

        // ₱10 = 1000 cents, under the ₱15 (15% of ₱100 remaining) threshold.
        vm.activeItem.value = "Coffee"
        vm.activeCost.value = "10"
        vm.activeType.value = EntryType.WANT
        vm.trySeal()
        advanceUntilIdle()

        val sealed = dao.entries.value.single()
        assertEquals("Coffee", sealed.item)
        assertEquals(1000L, sealed.costCents)
        assertNull(vm.coachHold.value)
    }

    @Test
    fun max_needRow_neverIntercepted_evenWhenCostWouldHold() = runTest(dispatcher) {
        val vm = buildMaxViewModel()

        vm.activeItem.value = "Groceries"
        vm.activeCost.value = "9000"
        vm.activeType.value = EntryType.NEED
        vm.trySeal()
        advanceUntilIdle()

        assertEquals(1, dao.entries.value.size)
        assertEquals(EntryType.NEED, dao.entries.value.single().type)
        assertNull(vm.coachHold.value)
    }

    @Test
    fun max_confirmCoachSeal_sealsThroughNormalPipeline_andClearsCoachHold() = runTest(dispatcher) {
        val vm = buildMaxViewModel()

        vm.activeItem.value = "Sneakers"
        vm.activeCost.value = "3000"
        vm.activeType.value = EntryType.WANT
        vm.trySeal()
        advanceUntilIdle()
        assertNotNull(vm.coachHold.value)

        vm.confirmCoachSeal()
        advanceUntilIdle()

        assertNull(vm.coachHold.value)
        val sealed = dao.entries.value.single()
        assertEquals("Sneakers", sealed.item)
        assertEquals(300_000L, sealed.costCents)
        assertEquals(EntryType.WANT, sealed.type)
    }

    // --- Rewarded ad bonus (Wave A) -------------------------------------------

    private class FakeRewardedAdGateway : RewardedAdGateway {
        var loadAndShowCalls = 0
        var resetCalls = 0
        private var onUserEarnedReward: (() -> Unit)? = null
        private var onClosed: ((earned: Boolean, error: String?) -> Unit)? = null

        override fun isReady(): Boolean = false

        override fun loadAndShow(
            activity: Activity,
            onUserEarnedReward: () -> Unit,
            onClosed: (earned: Boolean, error: String?) -> Unit
        ) {
            loadAndShowCalls++
            this.onUserEarnedReward = onUserEarnedReward
            this.onClosed = onClosed
        }

        override fun reset() {
            resetCalls++
        }

        /** Simulates the ad being watched to completion (reward granted). */
        fun earnReward() {
            onUserEarnedReward?.invoke()
        }

        /** Simulates the ad closing without a reward (e.g. dismissed). */
        fun closeWithoutReward() {
            onClosed?.invoke(false, null)
        }
    }

    @Test
    fun watchAd_grantsBonus_andRetriesBlockedSeal() = runTest(dispatcher) {
        val gateway = FakeRewardedAdGateway()
        prefs.setQuotaState(QuotaState(today(), logsCreated = 5, carriedLogs = 0))
        val vm = buildViewModel(rewardedAds = gateway)
        advanceUntilIdle()

        fillForm(vm)
        vm.trySeal()
        assertNotNull(vm.quotaBlocked.value)
        assertTrue(dao.entries.value.isEmpty())

        vm.onWatchAd(Activity())
        assertEquals(AdState.Loading, vm.adState.value)
        assertEquals(1, gateway.loadAndShowCalls)

        gateway.earnReward()
        advanceUntilIdle()

        // Bonus granted (5 base used + 8 bonus), pending seal re-ran through
        // the normal pipeline and inserted the row (now 6 of 13 used).
        assertEquals(7, DailyLogQuota.remaining(vm.quotaState.value, today()))
        assertEquals(8, vm.quotaState.value.bonusLogs)
        assertEquals(1, vm.quotaState.value.adsWatched)
        assertEquals(6, vm.quotaState.value.logsCreated)
        assertEquals(1, dao.entries.value.size)
        assertEquals("Coffee", dao.entries.value.single().item)
        assertNull(vm.quotaBlocked.value)
        assertEquals(AdState.Idle, vm.adState.value)
    }

    @Test
    fun dismissQuotaBlocked_resetsGateway_withoutGranting() = runTest(dispatcher) {
        val gateway = FakeRewardedAdGateway()
        prefs.setQuotaState(QuotaState(today(), logsCreated = 5, carriedLogs = 0))
        val vm = buildViewModel(rewardedAds = gateway)
        advanceUntilIdle()

        fillForm(vm)
        vm.trySeal()
        assertNotNull(vm.quotaBlocked.value)

        vm.dismissQuotaBlocked()

        assertEquals(1, gateway.resetCalls)
        assertNull(vm.quotaBlocked.value)
        assertEquals(0, vm.quotaState.value.bonusLogs)
        assertEquals(0, vm.quotaState.value.adsWatched)
        assertTrue(dao.entries.value.isEmpty())
    }

    @Test
    fun watchAd_capReached_doesNotLoadAd() = runTest(dispatcher) {
        val gateway = FakeRewardedAdGateway()
        // All 5 base + all 24 bonus used, and all 3 ads watched → quota gate
        // blocks, but the Watch-ad affordance must not load another ad.
        prefs.setQuotaState(
            QuotaState(today(), logsCreated = 29, carriedLogs = 0, bonusLogs = 24, adsWatched = 3)
        )
        val vm = buildViewModel(rewardedAds = gateway)
        advanceUntilIdle()

        fillForm(vm)
        vm.trySeal()
        assertNotNull(vm.quotaBlocked.value)

        vm.onWatchAd(Activity())

        assertEquals(0, gateway.loadAndShowCalls)
        assertEquals(AdState.Idle, vm.adState.value)
    }

    // --- Wave C: earlier-today stamp, last-item replay, backup nudge ----------

    @Test
    fun sealEarlierToday_stampsTopOfChosenHour() = runTest(dispatcher) {
        val vm = buildViewModel()
        advanceUntilIdle()

        // The UI offers hours 06:00..now; pick the current hour (always offered).
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        vm.setSealHour(hour)
        fillForm(vm)
        vm.trySeal()
        advanceUntilIdle()

        val sealed = dao.entries.value.single()
        assertEquals(String.format("%02d:00", hour), sealed.time)
        assertEquals(today(), sealed.date)
        assertTrue(sealed.dateUtc <= System.currentTimeMillis())
        // One-shot: the override clears after the seal.
        assertNull(vm.sealHourOverride.value)
    }

    @Test
    fun sealNow_withoutOverride_usesRealClock() = runTest(dispatcher) {
        val vm = buildViewModel()
        advanceUntilIdle()

        val before = System.currentTimeMillis()
        fillForm(vm)
        vm.trySeal()
        advanceUntilIdle()
        val after = System.currentTimeMillis()

        val sealed = dao.entries.value.single()
        assertTrue(sealed.dateUtc in before..after)
        assertEquals(today(), sealed.date)
    }

    @Test
    fun replayLastItem_fillsItemAndType_keepsCostEmpty() = runTest(dispatcher) {
        dao.entries.value = listOf(
            seedEntry(dateUtc = System.currentTimeMillis() - 3000, item = "Rice", type = EntryType.NEED),
            seedEntry(dateUtc = System.currentTimeMillis() - 2000, item = "Milk tea", type = EntryType.WANT),
            seedEntry(dateUtc = System.currentTimeMillis() - 1000, item = "Rice", type = EntryType.WANT)
        )
        val vm = buildViewModel()
        backgroundScope.launch { vm.sheetEntries.collect {} }
        backgroundScope.launch { vm.lastItemChips.collect {} }
        advanceUntilIdle()

        assertEquals(
            listOf("Rice" to EntryType.WANT, "Milk tea" to EntryType.WANT, "Rice" to EntryType.NEED),
            vm.lastItemChips.value
        )

        vm.replayLastItem("Milk tea", EntryType.WANT)

        assertEquals("Milk tea", vm.activeItem.value)
        assertEquals(EntryType.WANT, vm.activeType.value)
        assertEquals("", vm.activeCost.value)
    }

    @Test
    fun backupNudgeVisible_hidesAfterDismiss() = runTest(dispatcher) {
        dao.entries.value = List(5) { seedEntry(dateUtc = System.currentTimeMillis() - it) }
        val vm = buildViewModel()
        backgroundScope.launch { vm.sheetEntries.collect {} }
        backgroundScope.launch { vm.backupNudgeVisible.collect {} }
        advanceUntilIdle()

        // 5 sealed entries + no backup folder + nudge never seen.
        assertTrue(vm.backupNudgeVisible.value)

        vm.dismissBackupNudge()
        advanceUntilIdle()

        assertFalse(vm.backupNudgeVisible.value)
    }

    @Test
    fun sealScannedBatch_insertsAllCategorizedItems_andEmitsSealed() = runTest(dispatcher) {
        prefs.setEntitlement(
            Entitlement(
                tier = EntitlementTier.PRO,
                type = EntitlementType.PAID,
                expiresAtEpochMillis = System.currentTimeMillis() + 86_400_000L
            )
        )
        val vm = buildViewModel()
        advanceUntilIdle()
        val scanned = listOf(
            com.needsvswants.app.domain.ScannedLineItem(name = "Bread", costCents = 8250L, type = EntryType.NEED),
            com.needsvswants.app.domain.ScannedLineItem(name = "Chips", costCents = 4500L, type = EntryType.WANT)
        )

        vm.sealScannedBatch(scanned)
        advanceUntilIdle()

        assertEquals(2, dao.entries.value.size)
        assertEquals("Bread", dao.entries.value[0].item)
        assertEquals(EntryType.NEED, dao.entries.value[0].type)
        assertEquals(8250L, dao.entries.value[0].costCents)
        assertEquals("Chips", dao.entries.value[1].item)
        assertEquals(EntryType.WANT, dao.entries.value[1].type)
        assertEquals(4500L, dao.entries.value[1].costCents)
        assertEquals(ReceiptScanUiState.Idle, vm.receiptScanState.value)
    }

    @Test
    fun scanReceipt_onFreeTier_setsError() = runTest(dispatcher) {
        val vm = buildViewModel()
        vm.scanReceipt(null)
        advanceUntilIdle()

        assertTrue(vm.receiptScanState.value is ReceiptScanUiState.Error)
        assertEquals(
            "Receipt scanning is exclusive to Pro and Max members.",
            (vm.receiptScanState.value as ReceiptScanUiState.Error).message
        )
    }

    @Test
    fun sealScannedBatch_rejectsUnclassifiedItems_withoutPartialInsert() = runTest(dispatcher) {
        prefs.setEntitlement(
            Entitlement(
                tier = EntitlementTier.PRO,
                type = EntitlementType.PAID,
                expiresAtEpochMillis = System.currentTimeMillis() + 86_400_000L
            )
        )
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.sealScannedBatch(
            listOf(
                com.needsvswants.app.domain.ScannedLineItem(name = "Bread", costCents = 8250L, type = EntryType.NEED),
                com.needsvswants.app.domain.ScannedLineItem(name = "Unknown", costCents = 4500L, type = null)
            )
        )
        advanceUntilIdle()

        assertTrue(dao.entries.value.isEmpty())
        assertTrue(vm.receiptScanState.value is ReceiptScanUiState.Ready || vm.receiptScanState.value is ReceiptScanUiState.Idle)
    }

    private class FakeEntryDao : EntryDao {
        val entries = MutableStateFlow<List<Entry>>(emptyList())

        override suspend fun insert(entry: Entry): Long {
            entries.value = entries.value + entry
            return entries.value.size.toLong()
        }

        override suspend fun insertAll(entries: List<Entry>): List<Long> {
            this.entries.value = this.entries.value + entries
            return entries.map { it.id }
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

        override suspend fun update(entry: Entry) {
            entries.value = entries.value.map { if (it.id == entry.id) entry else it }
        }

        override suspend fun restore(entry: Entry): Long {
            entries.value = entries.value + entry
            return entry.id
        }
    }
}
