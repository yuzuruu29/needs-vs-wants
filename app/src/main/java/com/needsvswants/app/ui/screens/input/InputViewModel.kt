package com.needsvswants.app.ui.screens.input

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.ads.RewardedAdGateway
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.AdsConfig
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.DailyBudgetMath
import com.needsvswants.app.domain.DailyBudgetUseCase
import com.needsvswants.app.domain.DailyLogQuota
import com.needsvswants.app.domain.QuotaState
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

/** Rewarded-ad dialog state (Phase 3). */
sealed class AdState {
    object Idle : AdState()
    object Loading : AdState()
    data class Failed(val message: String) : AdState()
}

@HiltViewModel
class InputViewModel @Inject constructor(
    private val entries: EntryRepository,
    private val preferences: AppPreferences,
    private val dailyBudgetUseCase: DailyBudgetUseCase,
    private val rewardedAdGateway: RewardedAdGateway,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    val sheetEntries: StateFlow<List<Entry>> = entries.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currencySymbol: StateFlow<String> = preferences.currencySymbol
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₱")

    val entitlement = preferences.entitlement
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), com.needsvswants.app.domain.Entitlement.Free)

    val quotaState: StateFlow<QuotaState> = preferences.quotaState
        .map { DailyLogQuota.rollDayIfNeeded(it, todayString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuotaState("", 0, 0, 0))

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

    private val _overspendConfirm = MutableStateFlow<Long?>(null)
    val overspendConfirmCostCents: StateFlow<Long?> = _overspendConfirm.asStateFlow()

    private val _quotaBlocked = MutableStateFlow<QuotaBlocked?>(null)
    val quotaBlocked: StateFlow<QuotaBlocked?> = _quotaBlocked.asStateFlow()

    private val _adState = MutableStateFlow<AdState>(AdState.Idle)
    val adState: StateFlow<AdState> = _adState.asStateFlow()

    /** Can the free user watch another rewarded ad today (max-3 cap)? */
    val canWatchAdToday: StateFlow<Boolean> = preferences.quotaState
        .map { DailyLogQuota.canWatchAd(it, todayString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

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
        if (item.isEmpty() || costCents == null || type == null || isSheetFull) return

        val now = System.currentTimeMillis()
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
        rewardedAdGateway.reset()
    }

    /**
     * Phase 3: watch a rewarded ad to unlock more logs today.
     * Called from the QuotaBlocked dialog only (explicit user opt-in).
     * Grant happens ONLY in the gateway's onUserEarnedReward callback.
     */
    fun onWatchAd(activity: Activity) {
        if (_adState.value is AdState.Loading) return
        val blocked = _quotaBlocked.value ?: return
        if (!AdsConfig.ENABLED) return
        val today = todayString()
        val rolled = DailyLogQuota.rollDayIfNeeded(quotaState.value, today)
        if (!DailyLogQuota.canWatchAd(rolled, today)) {
            // Cap reached: the dialog body already shows the limit copy and
            // hides the button — nothing else to say (avoids duplicate text).
            return
        }
        _adState.value = AdState.Loading
        rewardedAdGateway.loadAndShow(
            activity = activity,
            onUserEarnedReward = { grantAndRetrySeal(blocked) },
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
     * Grant +8 bonus logs (adsWatched capped at 3/day) and immediately retry
     * the pending seal with the draft snapshot held in QuotaBlocked.
     */
    private fun grantAndRetrySeal(blocked: QuotaBlocked) {
        viewModelScope.launch {
            val today = todayString()
            val current = preferences.quotaState.first()
            val rolled = DailyLogQuota.rollDayIfNeeded(current, today)
            preferences.setQuotaState(DailyLogQuota.grantBonus(rolled, today))
            _quotaBlocked.value = null
            _adState.value = AdState.Idle
            sealNow(blocked.item, blocked.costCents, blocked.type, System.currentTimeMillis())
        }
    }

    private fun sealNow(item: String, costCents: Long, type: EntryType, now: Long) {
        if (isSealing) return
        isSealing = true
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
            runCatching { NvwWidget.refreshAll(appContext) }
        }
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch {
            entries.delete(entry)
            runCatching { NvwWidget.refreshAll(appContext) }
        }
    }

    fun startNewSheet() {
        viewModelScope.launch {
            entries.deleteAll()
            runCatching { NvwWidget.refreshAll(appContext) }
        }
    }
}
