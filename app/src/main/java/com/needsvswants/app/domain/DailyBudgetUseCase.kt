package com.needsvswants.app.domain

import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.prefs.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar

class DailyBudgetUseCase(
    private val dao: EntryDao,
    private val preferences: AppPreferences
) {
    fun observeStatus(): Flow<BudgetStatus> {
        val since = startOfToday()
        return combine(
            preferences.dailyBudgetCents,
            dao.observeSince(since)
        ) { budgetCents, entries ->
            val spent = entries.sumOf { it.costCents }
            DailyBudgetMath.status(budgetCents, spent)
        }
    }

    private fun startOfToday(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}