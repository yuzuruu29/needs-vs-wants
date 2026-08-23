package com.needsvswants.app.data.repository

import com.needsvswants.app.data.db.DailyBudgetDao
import com.needsvswants.app.data.model.DailyBudgetEntity
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.domain.LocalDayKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyBudgetRepository @Inject constructor(
    private val dao: DailyBudgetDao,
    private val preferences: AppPreferences,
    private val entitlements: EntitlementRepository
) {
    private val migrationMutex = Mutex()

    /** Raw value for one day; the flow also makes the legacy migration safe for cold starts. */
    fun observeForDay(dayKey: String): Flow<Long?> = flow {
        migrateLegacyBudgetIfNeeded()
        emitAll(dao.observeForDay(dayKey).map { it?.budgetCents?.takeIf { cents -> cents > 0L } })
    }

    suspend fun getForDay(dayKey: String): Long? {
        migrateLegacyBudgetIfNeeded()
        return dao.getForDay(dayKey)?.budgetCents?.takeIf { it > 0L }
    }

    suspend fun setForDay(dayKey: String, budgetCents: Long) {
        require(budgetCents > 0L) { "daily budget must be positive cents" }
        migrationMutex.withLock {
            migrateLegacyBudgetLocked(LocalDayKey.today())
            dao.upsert(DailyBudgetEntity(dayKey = dayKey, budgetCents = budgetCents))
        }
    }

    suspend fun clearForDay(dayKey: String) {
        migrationMutex.withLock {
            migrateLegacyBudgetLocked(LocalDayKey.today())
            dao.deleteForDay(dayKey)
        }
    }

    /** Visible budget history follows the same Free 30-day / paid lifetime boundary as entries. */
    fun observeVisibleBudgets(): Flow<List<DailyBudgetEntity>> = flow {
        migrateLegacyBudgetIfNeeded()
        emitAll(
            combine(dao.observeAll(), entitlements.entitlement) { budgets, entitlement ->
                val cutoff = if (entitlement.hasProAccessAt(System.currentTimeMillis())) {
                    null
                } else {
                    LocalDayKey.daysAgo(System.currentTimeMillis(), 29)
                }
                if (cutoff == null) budgets else budgets.filter { it.dayKey >= cutoff }
            }
        )
    }

    /** Tier-blind read for backup; hidden paid history must remain restorable. */
    suspend fun allStoredBudgets(): List<DailyBudgetEntity> {
        migrateLegacyBudgetIfNeeded()
        return dao.observeAll().first()
    }

    suspend fun restoreBudgets(budgets: List<DailyBudgetEntity>) {
        if (budgets.isEmpty()) return
        dao.upsertAll(budgets.filter { it.budgetCents > 0L })
    }

    suspend fun deleteAll() = dao.deleteAll()

    /**
     * Removes expired budget-only rows for Free users. Rows that share a day
     * with an entry remain stored, preserving the D175 visibility-boundary
     * guarantee if paid access is later verified.
     */
    suspend fun pruneExpiredOrphanedBudgets(nowEpochMs: Long = System.currentTimeMillis()) {
        migrateLegacyBudgetIfNeeded(nowEpochMs)
        val entitlement = entitlements.entitlement.first()
        if (!entitlement.hasProAccessAt(nowEpochMs)) {
            dao.deleteOrphanedBefore(LocalDayKey.daysAgo(nowEpochMs, 29))
        }
    }

    suspend fun migrateLegacyBudgetIfNeeded(
        nowEpochMs: Long = System.currentTimeMillis()
    ) {
        migrateLegacyBudgetIfNeeded(LocalDayKey.today(nowEpochMs))
    }

    private suspend fun migrateLegacyBudgetIfNeeded(todayKey: String) {
        migrationMutex.withLock { migrateLegacyBudgetLocked(todayKey) }
    }

    private suspend fun migrateLegacyBudgetLocked(todayKey: String) {
        val legacy = preferences.legacyDailyBudgetCents.first() ?: run {
            return
        }
        if (legacy > 0L && dao.getForDay(todayKey)?.budgetCents?.takeIf { it > 0L } == null) {
            dao.upsert(DailyBudgetEntity(dayKey = todayKey, budgetCents = legacy))
        }
        // Clearing is deliberately part of the same mutex-protected operation:
        // repeated launches cannot duplicate or overwrite a current-day edit.
        preferences.clearLegacyDailyBudget()
    }
}
