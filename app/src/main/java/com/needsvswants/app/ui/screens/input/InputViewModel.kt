package com.needsvswants.app.ui.screens.input

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.ads.RewardedAdGateway
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.AdsConfig
import com.needsvswants.app.domain.AdvisorContextPack
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.DailyBudgetMath
import com.needsvswants.app.domain.DailyBudgetUseCase
import com.needsvswants.app.domain.DailyLogQuota
import com.needsvswants.app.domain.FinancialAdvisorEngine
import com.needsvswants.app.domain.QuotaState
import com.needsvswants.app.domain.ReceiptOcrProcessor
import com.needsvswants.app.domain.ReceiptScanResult
import com.needsvswants.app.domain.ScannedLineItem
import com.needsvswants.app.domain.StreakMath
import com.needsvswants.app.domain.WantHold
import com.needsvswants.app.domain.filterAmountInput
import com.needsvswants.app.domain.parseCents
import com.needsvswants.app.domain.toInputAmount
import com.needsvswants.app.widget.NvwWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

sealed class SealEvent {
    /**
     * Peak-moment payload for the Log seal choreography (D191). [firstEver]
     * marks the diary's very first entry; [firstOfDay] the first seal of the
     * local day, the instant a logging streak extends, with [streakDay] as the
     * streak length that seal creates. 0 when not applicable.
     */
    data class Sealed(
        val sheetComplete: Boolean,
        val firstEver: Boolean = false,
        val firstOfDay: Boolean = false,
        val streakDay: Int = 0
    ) : SealEvent()
}

data class QuotaBlocked(val item: String, val costCents: Long, val type: EntryType)

/** Rewarded-ad dialog state (optional Free-tier unlock). */
sealed class AdState {
    data object Idle : AdState()
    data object Loading : AdState()
    data class Failed(val message: String) : AdState()
}

/** State machine for the Pro/Max Receipt Scanner. */
sealed class ReceiptScanUiState {
    data object Idle : ReceiptScanUiState()
    data object Scanning : ReceiptScanUiState()
    data class Ready(val result: ReceiptScanResult) : ReceiptScanUiState()
    data class Error(val message: String) : ReceiptScanUiState()
}

/**
 * Pending pre-seal Want hold consult for Max users (Task 3). Non-null only
 * while a draft Want row is waiting for the coach's hold verdict; the seal is
 * held until the user chooses "Seal anyway" (via [InputViewModel.confirmCoachSeal])
 * or edits the row away. All-clear verdicts, Needs, and Free/Pro never produce
 * this state.
 */
data class CoachHold(
    val hold: Boolean,
    val reason: String,
    val citation: String
)

@HiltViewModel
class InputViewModel @Inject constructor(
    private val entries: EntryRepository,
    private val preferences: AppPreferences,
    private val dailyBudgetUseCase: DailyBudgetUseCase,
    private val rewardedAds: RewardedAdGateway,
    @ApplicationContext private val appContext: Context?,
    private val receiptOcrProcessor: ReceiptOcrProcessor
) : ViewModel() {
    // Same rationale as entitlement/quotaState: isSheetFull and sealNow read
    // .value, and an uncollected WhileSubscribed StateFlow would freeze it.
    val sheetEntries: StateFlow<List<Entry>> = entries.observeAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val currencySymbol: StateFlow<String> = preferences.currencySymbol
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₱")

    val entitlement = preferences.entitlement
        .stateIn(viewModelScope, SharingStarted.Eagerly, com.needsvswants.app.domain.Entitlement.Free)

    /**
     * Daily free quota for today's logs. Eagerly subscribed (not WhileSubscribed):
     * [trySeal] reads `.value` with no collector attached, and an uncollected
     * WhileSubscribed StateFlow never starts its upstream — `.value` would stay
     * frozen at the initial state and the quota gate would silently pass forever.
     */
    val quotaState: StateFlow<QuotaState> = preferences.quotaState
        .map { DailyLogQuota.rollDayIfNeeded(it, todayString()) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, QuotaState("", 0, 0))

    /** Onboarding spending goal; the coach consults it (not the default). */
    val spendingGoal: StateFlow<String> = preferences.spendingGoal
        .stateIn(viewModelScope, SharingStarted.Eagerly, AdvisorContextPack.DEFAULT_SPENDING_GOAL)

    /** True while the free user may watch another rewarded ad today (cap 3). */
    val canWatchAdToday: StateFlow<Boolean> = quotaState
        .map { AdsConfig.ENABLED && DailyLogQuota.canWatchAd(it, todayString()) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _adState = MutableStateFlow<AdState>(AdState.Idle)
    val adState: StateFlow<AdState> = _adState.asStateFlow()

    val isSheetFull: Boolean get() {
        val now = System.currentTimeMillis()
        if (entitlement.value.hasProAccessAt(now)) return false
        return sheetEntries.value.size >= 20
    }

    var activeItem = MutableStateFlow("")
    var activeCost = MutableStateFlow("")
    var activeType = MutableStateFlow<EntryType?>(null)
    private var isSealing = false
    private var receiptScanJob: Job? = null

    /**
     * Optional "seal as earlier today" hour (0-23, top of hour). Null = stamp
     * with the real clock (Now). Cleared after the next successful seal.
     */
    var sealHourOverride = MutableStateFlow<Int?>(null)

    fun setSealHour(hour: Int?) {
        sealHourOverride.value = hour
    }

    /** Up to 3 unique most-recent (item, type) pairs for one-tap replay. */
    val lastItemChips: StateFlow<List<Pair<String, EntryType>>> = sheetEntries
        .map { entries ->
            entries.asReversed()
                .distinctBy { it.item to it.type }
                .take(3)
                .map { it.item to it.type }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Fill the form from a replay chip; the cost stays empty on purpose. */
    fun replayLastItem(item: String, type: EntryType) {
        activeItem.value = item
        activeType.value = type
    }

    /** One-shot backup nudge: shown once after 5 sealed entries with no folder. */
    val backupNudgeVisible: StateFlow<Boolean> = combine(
        sheetEntries.map { it.size >= 5 },
        preferences.backupFolderUri.map { it.isNullOrBlank() },
        preferences.backupNudgeSeen.map { !it }
    ) { enough, noFolder, notSeen -> enough && noFolder && notSeen }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun dismissBackupNudge() {
        viewModelScope.launch { preferences.setBackupNudgeSeen(true) }
    }

    val budgetStatus: StateFlow<BudgetStatus> = dailyBudgetUseCase.observeStatus()
        .stateIn(viewModelScope, SharingStarted.Eagerly, BudgetStatus.Off)

    val dailyBudgetCents: StateFlow<Long?> = dailyBudgetUseCase.observeCurrentBudget()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val currentDayKey: StateFlow<String> = dailyBudgetUseCase.observeCurrentDayKey()
        .stateIn(viewModelScope, SharingStarted.Eagerly, com.needsvswants.app.domain.LocalDayKey.today())

    /** One-shot onboarding nudge: pre-open the set-budget form on Log (design audit #9 follow-up). */
    val budgetNudgePending: StateFlow<Boolean> = preferences.budgetNudgePending
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun consumeBudgetNudge() {
        viewModelScope.launch { preferences.setBudgetNudgePending(false) }
    }

    private val _overspendConfirm = MutableStateFlow<Long?>(null)
    val overspendConfirmCostCents: StateFlow<Long?> = _overspendConfirm.asStateFlow()

    private val _quotaBlocked = MutableStateFlow<QuotaBlocked?>(null)
    val quotaBlocked: StateFlow<QuotaBlocked?> = _quotaBlocked.asStateFlow()

    /** Pending pre-seal Want hold consult (Max only); null for Free/Pro and Needs. */
    private val _coachHold = MutableStateFlow<CoachHold?>(null)
    val coachHold: StateFlow<CoachHold?> = _coachHold.asStateFlow()

    /** True only for the seal the user explicitly confirmed through the coach dialog. */
    private var coachSealOverride = false

    private val _sealEvents = MutableSharedFlow<SealEvent>(extraBufferCapacity = 1)
    val sealEvents: SharedFlow<SealEvent> = _sealEvents.asSharedFlow()

    private fun todayString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun filterItem(input: String): String =
        input.filter { it.isLetterOrDigit() || it.isWhitespace() || it == '-' || it == '.' || it == '\'' || it == ',' }

    fun filterCost(input: String): String = filterAmountInput(input)

    fun filterBudgetAmount(input: String): String = filterAmountInput(input)

    fun saveDailyBudget(rawAmount: String): Boolean {
        val cents = parseCents(rawAmount) ?: return false
        if (cents <= 0L) return false
        viewModelScope.launch { dailyBudgetUseCase.setCurrentBudget(cents) }
        return true
    }

    fun clearDailyBudget() {
        viewModelScope.launch { dailyBudgetUseCase.clearCurrentBudget() }
    }

    fun trySeal() {
        if (isSealing) return
        if (_overspendConfirm.value != null) return
        if (_quotaBlocked.value != null) return
        val item = activeItem.value.trim()
        val costCents = parseCents(activeCost.value)
        val type = activeType.value
        if (item.isEmpty() || costCents == null || type == null || isSheetFull) {
            // Row is no longer sealable; drop any stale pending consult.
            _coachHold.value = null
            return
        }

        val now = System.currentTimeMillis()
        val hasMax = entitlement.value.hasMaxAccessAt(now)
        // Max coach gate (Task 3): intercept ONLY a Max Want whose verdict is
        // a hold. All-clear verdicts, Needs, Free/Pro, and a budget Off fall
        // through to the untouched seal path (quota, overspend, seal).
        val coachSuggestion = if (!coachSealOverride && hasMax && type == EntryType.WANT) {
            wantHoldSuggestion(costCents)
        } else {
            null
        }
        if (!coachSealOverride &&
            FinancialAdvisorEngine.shouldInterceptCoach(hasMax, type, coachSuggestion)
        ) {
            val verdict = coachSuggestion ?: return
            _coachHold.value = CoachHold(
                hold = verdict.hold,
                reason = verdict.reason,
                citation = verdict.citation
            )
            return
        }
        coachSealOverride = false

        if (!entitlement.value.hasProAccessAt(now)) {
            val rolled = DailyLogQuota.rollDayIfNeeded(quotaState.value, todayString())
            if (!DailyLogQuota.canLog(rolled, todayString())) {
                _quotaBlocked.value = QuotaBlocked(item, costCents, type)
                return
            }
        }

        val status = budgetStatus.value
        if (status is BudgetStatus.On &&
            DailyBudgetMath.wouldExceed(status.spentCents, status.budgetCents, costCents)
        ) {
            _overspendConfirm.value = costCents
            return
        }
        sealNow(item, costCents, type, now)
    }

    /**
     * Pure-ish coach verdict for a draft Want, backed by the domain engine
     * over the live ledger + daily budget + the user's spending goal. Null
     * when the budget is off.
     */
    fun wantHoldSuggestion(costCents: Long): WantHold? {
        val context = AdvisorContextPack.build(
            entries = sheetEntries.value,
            dailyBudgetCents = dailyBudgetCents.value,
            spendingGoal = spendingGoal.value,
            currencySymbol = currencySymbol.value
        )
        return FinancialAdvisorEngine.wantHoldSuggestion(costCents, context)
    }

    /**
     * "Seal anyway" from the coach dialog: drops the pending consult and
     * re-enters the untouched seal path (quota gate, overspend confirm, seal).
     */
    fun confirmCoachSeal() {
        if (_coachHold.value == null) return
        _coachHold.value = null
        coachSealOverride = true
        trySeal()
    }

    fun confirmOverspendSeal() {
        val costCents = _overspendConfirm.value ?: return
        val item = activeItem.value.trim()
        val type = activeType.value
        _overspendConfirm.value = null
        if (item.isEmpty() || type == null) return
        sealNow(item, costCents, type, System.currentTimeMillis())
    }

    fun dismissOverspendConfirm() {
        _overspendConfirm.value = null
    }

    fun dismissQuotaBlocked() {
        _quotaBlocked.value = null
        _adState.value = AdState.Idle
        // Cancel any in-flight consent/load so no ad pops up after dismissal.
        rewardedAds.reset()
    }

    /**
     * Watch a rewarded ad to unlock more logs today. Called from the
     * QuotaBlocked dialog only (explicit user opt-in). Bonus logs are granted
     * ONLY in the gateway's onUserEarnedReward callback — never on close or
     * failure.
     */
    fun onWatchAd(activity: Activity) {
        if (!AdsConfig.ENABLED) return
        if (!canWatchAdToday.value) return
        if (_adState.value is AdState.Loading) return
        _adState.value = AdState.Loading
        rewardedAds.loadAndShow(
            activity = activity,
            onUserEarnedReward = { grantAndRetrySeal() },
            onClosed = { earned, error ->
                if (error != null) {
                    _adState.value = AdState.Failed(error)
                } else if (!earned) {
                    _adState.value = AdState.Idle
                }
                // earned → grantAndRetrySeal already cleared the pending state.
            }
        )
    }

    /**
     * Grant the ad bonus and immediately retry the pending seal with the
     * draft snapshot held in [QuotaBlocked]. The retry goes through the
     * normal pipeline (coach gate, quota gate, overspend confirm, seal).
     */
    private fun grantAndRetrySeal() {
        viewModelScope.launch {
            val current = preferences.quotaState.first()
            val granted = DailyLogQuota.grantBonus(current, todayString())
            preferences.setQuotaState(granted)
            _adState.value = AdState.Idle
            val pending = _quotaBlocked.value
            _quotaBlocked.value = null
            if (pending != null) {
                activeItem.value = pending.item
                activeCost.value = pending.costCents.toInputAmount()
                activeType.value = pending.type
                // quotaState (in-memory StateFlow) lags the DataStore write by
                // one emission — let it deliver the granted state so the quota
                // gate sees the fresh bonus. Bounded: on a midnight day-roll the
                // match never occurs, and the gate re-evaluates anyway.
                withTimeoutOrNull(2_000) { quotaState.first { it == granted } }
                trySeal()
            }
        }
    }

    private fun sealNow(item: String, costCents: Long, type: EntryType, now: Long) {
        if (isSealing) return
        isSealing = true
        _coachHold.value = null
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        // Optional "earlier today" stamp (top of the chosen hour); default Now.
        val stamp = sealHourOverride.value?.let { hour ->
            Calendar.getInstance().apply {
                timeInMillis = now
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        } ?: now
        sealHourOverride.value = null
        val existing = sheetEntries.value
        val willComplete = !entitlement.value.hasProAccessAt(now) && existing.size + 1 >= 20
        val firstEver = existing.isEmpty()
        val firstOfDay = existing.none { it.date == todayString() }
        val streakDay = if (firstOfDay) {
            StreakMath.currentStreak(
                existing.map { it.date }.distinct() + dateFormat.format(Date(stamp)),
                now
            )
        } else 0
        viewModelScope.launch {
            entries.insert(
                Entry(
                    dateUtc = stamp,
                    date = dateFormat.format(Date(stamp)),
                    time = timeFormat.format(Date(stamp)),
                    item = item,
                    costCents = costCents,
                    type = type
                )
            )
            if (!entitlement.value.hasProAccessAt(now)) {
                val current = preferences.quotaState.first()
                val rolled = DailyLogQuota.rollDayIfNeeded(current, todayString())
                preferences.setQuotaState(DailyLogQuota.incrementCreated(rolled, todayString()))
            }
            activeItem.value = ""
            activeCost.value = ""
            activeType.value = null
            isSealing = false
            _sealEvents.tryEmit(
                SealEvent.Sealed(
                    sheetComplete = willComplete,
                    firstEver = firstEver,
                    firstOfDay = firstOfDay,
                    streakDay = streakDay
                )
            )
            appContext?.let { ctx -> runCatching { NvwWidget.refreshAll(ctx) } }
        }
    }

    private val _receiptScanState = MutableStateFlow<ReceiptScanUiState>(ReceiptScanUiState.Idle)
    val receiptScanState: StateFlow<ReceiptScanUiState> = _receiptScanState.asStateFlow()

    fun scanReceipt(bitmap: Bitmap?) {
        receiptScanJob?.cancel()
        val now = System.currentTimeMillis()
        if (!entitlement.value.hasProAccessAt(now)) {
            _receiptScanState.value = ReceiptScanUiState.Error("Receipt scanning is exclusive to Pro and Max members.")
            return
        }
        if (bitmap == null) {
            _receiptScanState.value = ReceiptScanUiState.Error("The selected image could not be decoded.")
            return
        }
        _receiptScanState.value = ReceiptScanUiState.Scanning
        receiptScanJob = viewModelScope.launch {
            val result = receiptOcrProcessor.recognizeReceipt(bitmap)
            result.onSuccess { scanResult ->
                if (scanResult.items.isEmpty()) {
                    _receiptScanState.value = ReceiptScanUiState.Error("No purchase line items could be detected. Please ensure the receipt is well-lit and clear.")
                } else {
                    _receiptScanState.value = ReceiptScanUiState.Ready(scanResult)
                }
            }.onFailure { error ->
                _receiptScanState.value = ReceiptScanUiState.Error(error.message ?: "Failed to process receipt.")
            }
        }
    }

    fun dismissReceiptScan() {
        receiptScanJob?.cancel()
        receiptScanJob = null
        _receiptScanState.value = ReceiptScanUiState.Idle
    }

    fun sealScannedBatch(
        items: List<ScannedLineItem>,
        dateUtcOverride: Long? = null
    ) {
        if (isSealing) return
        val validItems = items.filter { it.type != null && it.costCents > 0L && it.name.isNotBlank() }
        if (validItems.size != items.size || validItems.isEmpty()) return

        val now = System.currentTimeMillis()
        if (!entitlement.value.hasProAccessAt(now)) {
            _receiptScanState.value = ReceiptScanUiState.Error("Your Pro or Max membership is required to seal receipt items.")
            return
        }
        val stamp = dateUtcOverride ?: now
        val oldestAllowed = now - 30L * 24L * 60L * 60L * 1000L
        if (stamp > now || stamp < oldestAllowed) {
            _receiptScanState.value = ReceiptScanUiState.Error("Receipt date must be within the last 30 days.")
            return
        }
        val budget = budgetStatus.value
        val totalCents = validItems.sumOf { it.costCents }
        if (budget is BudgetStatus.On && DailyBudgetMath.wouldExceed(budget.spentCents, budget.budgetCents, totalCents)) {
            _receiptScanState.value = ReceiptScanUiState.Error("This receipt exceeds today's budget. Review the items in Log before sealing.")
            return
        }
        if (entitlement.value.hasMaxAccessAt(now) && validItems.any { it.type == EntryType.WANT && wantHoldSuggestion(it.costCents)?.hold == true }) {
            _receiptScanState.value = ReceiptScanUiState.Error("Max coach review is required for a Want on this receipt. Log it from the sheet to review the hold.")
            return
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date(stamp))
        val timeStr = timeFormat.format(Date(stamp))

        val entriesToInsert = validItems.mapIndexed { index, item ->
            Entry(
                dateUtc = stamp + index,
                date = dateStr,
                time = timeStr,
                item = item.name.trim(),
                costCents = item.costCents,
                type = item.type!!
            )
        }

        val existing = sheetEntries.value
        val firstEver = existing.isEmpty()
        val firstOfDay = existing.none { it.date == dateStr }
        val streakDay = if (firstOfDay) {
            StreakMath.currentStreak(existing.map { it.date }.distinct() + dateStr, now)
        } else 0

        isSealing = true
        viewModelScope.launch {
            try {
                entries.insertAll(entriesToInsert)
                _receiptScanState.value = ReceiptScanUiState.Idle
                _sealEvents.tryEmit(
                    SealEvent.Sealed(
                        sheetComplete = false,
                        firstEver = firstEver,
                        firstOfDay = firstOfDay,
                        streakDay = streakDay
                    )
                )
                appContext?.let { ctx -> runCatching { NvwWidget.refreshAll(ctx) } }
            } finally {
                isSealing = false
            }
        }
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch {
            entries.delete(entry)
            appContext?.let { ctx -> runCatching { NvwWidget.refreshAll(ctx) } }
        }
    }

    fun startNewSheet() {
        viewModelScope.launch {
            entries.deleteAll()
            appContext?.let { ctx -> runCatching { NvwWidget.refreshAll(ctx) } }
        }
    }
}
