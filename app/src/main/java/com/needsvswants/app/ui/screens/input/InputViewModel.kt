package com.needsvswants.app.ui.screens.input

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
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

@HiltViewModel
class InputViewModel @Inject constructor(
    private val entries: EntryRepository,
    private val preferences: AppPreferences,
    private val dailyBudgetUseCase: DailyBudgetUseCase,
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
    }

    fun onWatchAd() {
        // Phase 3: gateway.loadAndShow() + grant on onUserEarnedReward.
        // Phase 1: button is hidden; this is a no-op stub kept for the dialog to call.
    }

    fun retrySealAfterReward() {
        // Phase 3: grant bonus → retry pending seal.
        // Phase 1: no-op stub.
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
