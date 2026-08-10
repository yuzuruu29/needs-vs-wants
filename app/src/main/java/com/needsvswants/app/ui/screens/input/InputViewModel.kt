package com.needsvswants.app.ui.screens.input

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.AdvisorContextPack
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.DailyBudgetMath
import com.needsvswants.app.domain.DailyBudgetUseCase
import com.needsvswants.app.domain.DailyLogQuota
import com.needsvswants.app.domain.FinancialAdvisorEngine
import com.needsvswants.app.domain.QuotaState
import com.needsvswants.app.domain.WantHold
import com.needsvswants.app.domain.filterAmountInput
import com.needsvswants.app.domain.parseCents
import com.needsvswants.app.widget.NvwWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

sealed class SealEvent {
    data class Sealed(val sheetComplete: Boolean) : SealEvent()
}

data class QuotaBlocked(val item: String, val costCents: Long, val type: EntryType)

/**
 * Pending pre-seal Want consult for Max users (Task 3). Non-null while a
 * draft Want row is waiting for the coach verdict; the seal is held until the
 * user chooses "Seal anyway" (via [InputViewModel.confirmCoachSeal]) or edits
 * the row away. Needs and Free/Pro never produce this state.
 */
data class CoachHold(
    val item: String,
    val costCents: Long,
    val hold: Boolean,
    val reason: String,
    val citation: String
)

@HiltViewModel
class InputViewModel @Inject constructor(
    private val entries: EntryRepository,
    private val preferences: AppPreferences,
    private val dailyBudgetUseCase: DailyBudgetUseCase,
    @ApplicationContext private val appContext: Context?
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

    val isSheetFull: Boolean get() {
        val now = System.currentTimeMillis()
        if (entitlement.value.hasProAccessAt(now)) return false
        return sheetEntries.value.size >= 20
    }

    var activeItem = MutableStateFlow("")
    var activeCost = MutableStateFlow("")
    var activeType = MutableStateFlow<EntryType?>(null)
    private var isSealing = false

    val budgetStatus: StateFlow<BudgetStatus> = dailyBudgetUseCase.observeStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetStatus.Off)

    val dailyBudgetCents: StateFlow<Long?> = preferences.dailyBudgetCents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

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

    /** Pending pre-seal Want consult (Max only); null for Free/Pro and Needs. */
    private val _coachHold = MutableStateFlow<CoachHold?>(null)
    val coachHold: StateFlow<CoachHold?> = _coachHold.asStateFlow()

    /** True when the current entitlement grants Max at this moment (coach gate). */
    val hasMaxAccess: Boolean
        get() = entitlement.value.hasMaxAccessAt(System.currentTimeMillis())

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
        viewModelScope.launch { preferences.setDailyBudgetCents(cents) }
        return true
    }

    fun clearDailyBudget() {
        viewModelScope.launch { preferences.clearDailyBudget() }
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
        // Max coach gate (Task 3): a draft Want with a budget On gets a quiet
        // pre-seal consult instead of an instant seal. Needs are never gated,
        // Free/Pro are untouched, and a budget Off yields no guardrail at all.
        if (!coachSealOverride &&
            entitlement.value.hasMaxAccessAt(now) && type == EntryType.WANT
        ) {
            val suggestion = wantHoldSuggestion(costCents)
            if (suggestion != null) {
                _coachHold.value = CoachHold(
                    item = item,
                    costCents = costCents,
                    hold = suggestion.hold,
                    reason = suggestion.reason,
                    citation = suggestion.citation
                )
                return
            }
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
     * over the live ledger + daily budget. Null when the budget is off.
     */
    fun wantHoldSuggestion(costCents: Long): WantHold? {
        val context = AdvisorContextPack.build(
            entries = sheetEntries.value,
            dailyBudgetCents = dailyBudgetCents.value,
            spendingGoal = AdvisorContextPack.DEFAULT_SPENDING_GOAL
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
    }

    private fun sealNow(item: String, costCents: Long, type: EntryType, now: Long) {
        if (isSealing) return
        isSealing = true
        _coachHold.value = null
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val willComplete = !entitlement.value.hasProAccessAt(now) && sheetEntries.value.size + 1 >= 20
        viewModelScope.launch {
            entries.insert(
                Entry(
                    dateUtc = now,
                    date = dateFormat.format(Date(now)),
                    time = timeFormat.format(Date(now)),
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
            _sealEvents.tryEmit(SealEvent.Sealed(sheetComplete = willComplete))
            appContext?.let { ctx -> runCatching { NvwWidget.refreshAll(ctx) } }
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
