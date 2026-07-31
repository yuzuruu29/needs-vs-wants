package com.needsvswants.app.ui.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.DailyBudgetUseCase
import com.needsvswants.app.domain.Period
import com.needsvswants.app.domain.SummaryStats
import com.needsvswants.app.domain.SummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val summaryUseCase: SummaryUseCase,
    private val dailyBudgetUseCase: DailyBudgetUseCase,
    private val preferences: AppPreferences
) : ViewModel() {
    val budgetStatus: StateFlow<BudgetStatus> = dailyBudgetUseCase.observeStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetStatus.Off)
    private val _period = MutableStateFlow(Period.DAY)
    val period: StateFlow<Period> = _period.asStateFlow()

    val stats: StateFlow<SummaryStats> = _period.flatMapLatest { p ->
        summaryUseCase.getStats(p)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SummaryStats())

    val currencySymbol: StateFlow<String> = preferences.currencySymbol
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₱")

    val isFirstLaunch: StateFlow<Boolean> = preferences.isFirstLaunch
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setPeriod(period: Period) { _period.value = period }

    fun dismissFirstLaunch() {
        viewModelScope.launch { preferences.setFirstLaunchComplete() }
    }
}
