package com.needsvswants.app.domain

import com.needsvswants.app.data.repository.DailyBudgetRepository
import com.needsvswants.app.data.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class DailyBudgetUseCase(
    private val entries: EntryRepository,
    private val budgets: DailyBudgetRepository
) {
    fun observeCurrentDayKey(): Flow<String> = currentDayKeyFlow()

    fun observeCurrentBudget(): Flow<Long?> = observeCurrentDayKey()
        .flatMapLatest { dayKey -> budgets.observeForDay(dayKey) }

    fun observeStatus(): Flow<BudgetStatus> = observeCurrentDayKey()
        .flatMapLatest { dayKey ->
            combine(
                budgets.observeForDay(dayKey),
                entries.observeForDate(dayKey)
            ) { budgetCents, dayEntries ->
                DailyBudgetMath.status(budgetCents, dayEntries.sumOf { it.costCents })
            }
        }

    suspend fun currentBudget(): Long? = budgets.getForDay(LocalDayKey.today())

    suspend fun setCurrentBudget(cents: Long) {
        budgets.setForDay(LocalDayKey.today(), cents)
    }

    suspend fun clearCurrentBudget() {
        budgets.clearForDay(LocalDayKey.today())
    }

    private fun currentDayKeyFlow(): Flow<String> = flow {
        var lastKey: String? = null
        while (currentCoroutineContext().isActive) {
            val now = System.currentTimeMillis()
            val key = LocalDayKey.fromEpoch(now)
            if (key != lastKey) {
                emit(key)
                lastKey = key
            }
            delay(LocalDayKey.millisUntilNextMidnight(now))
            // Coroutine test schedulers can advance the delay without moving
            // the wall clock. Do not spin forever in that case; a real device
            // reaches the next local date and continues with the new key.
            if (LocalDayKey.fromEpoch(System.currentTimeMillis()) == lastKey) break
        }
    }.distinctUntilChanged()
}
