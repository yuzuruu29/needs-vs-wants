package com.needsvswants.app.ui.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.DailyBudgetUseCase
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.Insight
import com.needsvswants.app.domain.InsightEngine
import com.needsvswants.app.domain.Period
import com.needsvswants.app.domain.PeriodWindow
import com.needsvswants.app.domain.StreakMath
import com.needsvswants.app.domain.StreakMilestone
import com.needsvswants.app.domain.SummaryStats
import com.needsvswants.app.domain.SummaryUseCase
import com.needsvswants.app.domain.toMoney
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val summaryUseCase: SummaryUseCase,
    dailyBudgetUseCase: DailyBudgetUseCase,
    private val preferences: AppPreferences,
    entryRepository: EntryRepository,
    entitlementRepository: EntitlementRepository
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

    /** Default false so we do not flash How It Works before DataStore resolves (D75). */
    val isFirstLaunch: StateFlow<Boolean> = preferences.isFirstLaunch
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val allEntries: StateFlow<List<Entry>> = entryRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Live entitlement snapshot — drives tier-aware Summary chrome (eyebrow, pills, lifetime copy). */
    val entitlement: StateFlow<Entitlement> = entitlementRepository.entitlement
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Entitlement.Free)

    /** True when any history exists (even if the active period is empty). */
    val hasHistory: StateFlow<Boolean> = allEntries
        .map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val streakDays: StateFlow<Int> = allEntries
        .map { list -> StreakMath.currentStreak(list.map { it.date }.distinct()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val computedBest: StateFlow<Int> = allEntries
        .map { list -> StreakMath.bestStreak(list.map { it.date }.distinct()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val bestStreak: StateFlow<Int> = combine(
        computedBest,
        preferences.bestStreakEver
    ) { computed, stored -> maxOf(computed, stored) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val insights: StateFlow<List<Insight>> = combine(
        allEntries,
        _period,
        currencySymbol,
        budgetStatus,
        entitlement
    ) { list, p, sym, budget, ent ->
        val periodSlice = filterToPeriod(list, p, retentionCutoffAt = ent.retentionCutoffAt(System.currentTimeMillis()))
        InsightEngine.generateInsights(
            periodEntries = periodSlice,
            period = p,
            currencySymbol = sym,
            budgetStatus = budget,
            maxInsights = 1
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _newMilestone = MutableSharedFlow<StreakMilestone>(extraBufferCapacity = 1)
    val newMilestone: SharedFlow<StreakMilestone> = _newMilestone.asSharedFlow()

    init {
        viewModelScope.launch {
            var lastPersisted = -1
            computedBest.collect { best ->
                if (best > 0 && best != lastPersisted) {
                    lastPersisted = best
                    preferences.updateBestStreak(best)
                }
            }
        }

        viewModelScope.launch {
            var lastEmittedDays = -1
            combine(streakDays, preferences.lastMilestoneShown) { current, lastShown ->
                current to lastShown
            }.collect { (current, lastShown) ->
                // Emit the lowest unshown milestone so multi-day gaps don't skip marks.
                val nextMark = StreakMilestone.entries.firstOrNull { it.days > lastShown && current >= it.days }
                if (nextMark != null && nextMark.days != lastEmittedDays) {
                    lastEmittedDays = nextMark.days
                    _newMilestone.tryEmit(nextMark)
                }
            }
        }
    }

    fun acknowledgeMilestone(milestone: StreakMilestone) {
        viewModelScope.launch {
            preferences.setLastMilestoneShown(milestone.days)
        }
    }

    fun setPeriod(period: Period) {
        _period.value = period
    }

    /**
     * When access drops (Pro/Max → Free), a previously selected paid-only period
     * (MONTH) is no longer offered. Reset to a free period so the pill bar never
     * shows a selection a free user cannot pick.
     */
    init {
        viewModelScope.launch {
            entitlement.collect { ent ->
                val paid = ent.hasProAccessAt(System.currentTimeMillis())
                if (!paid && _period.value == Period.MONTH) {
                    _period.value = Period.ALL
                }
            }
        }
    }

    fun dismissFirstLaunch() {
        viewModelScope.launch { preferences.setFirstLaunchComplete() }
    }

    fun shareSummaryText(): String {
        val s = stats.value
        val sym = currencySymbol.value
        val streak = streakDays.value
        val paid = entitlement.value.hasProAccessAt(System.currentTimeMillis())
        val periodLabel = when (_period.value) {
            Period.DAY -> "Today"
            Period.WEEK -> "This week"
            Period.MONTH -> "This month"
            Period.ALL -> if (paid) "All time" else "Last 30 days"
        }
        val streakLine = if (streak > 0) "\nLogging streak: day $streak" else ""
        return buildString {
            append("Needs vs Wants · $periodLabel\n")
            append("Needs: ${s.needsTotalCents.toMoney(sym)} (${s.needsPct}%)\n")
            append("Wants: ${s.wantsTotalCents.toMoney(sym)} (${s.wantsPct}%)\n")
            append("Total: ${s.totalCents.toMoney(sym)}")
            append(streakLine)
        }
    }

    companion object {
        internal fun filterToPeriod(
            entries: List<Entry>,
            period: Period,
            nowMs: Long = System.currentTimeMillis(),
            retentionCutoffAt: Long? = null
        ): List<Entry> {
            val since = PeriodWindow.sinceEpochMs(period, nowMs, retentionCutoffAt)
            return entries.filter { it.dateUtc >= since }
        }
    }
}
