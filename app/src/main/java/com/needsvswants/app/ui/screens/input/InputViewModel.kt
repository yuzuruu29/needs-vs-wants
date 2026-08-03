package com.needsvswants.app.ui.screens.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.DailyBudgetMath
import com.needsvswants.app.domain.DailyBudgetUseCase
import com.needsvswants.app.domain.filterAmountInput
import com.needsvswants.app.domain.parseCents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InputViewModel @Inject constructor(
    private val dao: EntryDao,
    private val preferences: AppPreferences,
    private val dailyBudgetUseCase: DailyBudgetUseCase
) : ViewModel() {
    val sheetEntries: StateFlow<List<Entry>> = dao.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currencySymbol: StateFlow<String> = preferences.currencySymbol
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₱")

    val entitlement = preferences.entitlement
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), com.needsvswants.app.domain.Entitlement.Free)

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

    /** Raw limit cents (null = off). Used for set/edit prefill on Log. */
    val dailyBudgetCents: StateFlow<Long?> = preferences.dailyBudgetCents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _overspendConfirm = MutableStateFlow<Long?>(null)
    /** Non-null = pending new cost cents awaiting user confirm. */
    val overspendConfirmCostCents: StateFlow<Long?> = _overspendConfirm.asStateFlow()

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
        val item = activeItem.value.trim()
        val costCents = parseCents(activeCost.value)
        val type = activeType.value
        if (item.isEmpty() || costCents == null || type == null || isSheetFull) return

        val status = budgetStatus.value
        if (status is BudgetStatus.On &&
            DailyBudgetMath.wouldExceed(status.spentCents, status.budgetCents, costCents)
        ) {
            _overspendConfirm.value = costCents
            return
        }
        sealNow(item, costCents, type)
    }

    fun confirmOverspendSeal() {
        val costCents = _overspendConfirm.value ?: return
        val item = activeItem.value.trim()
        val type = activeType.value
        _overspendConfirm.value = null
        if (item.isEmpty() || type == null) return
        sealNow(item, costCents, type)
    }

    fun dismissOverspendConfirm() {
        _overspendConfirm.value = null
        // Keep draft item/cost/type so user can edit.
    }

    private fun sealNow(item: String, costCents: Long, type: EntryType) {
        if (isSealing) return
        isSealing = true
        val now = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        viewModelScope.launch {
            dao.insert(Entry(
                dateUtc = now,
                date = dateFormat.format(Date(now)),
                time = timeFormat.format(Date(now)),
                item = item,
                costCents = costCents,
                type = type
            ))
            activeItem.value = ""
            activeCost.value = ""
            activeType.value = null
            isSealing = false
        }
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch { dao.delete(entry) }
    }
}
