package com.needsvswants.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.needsvswants.app.data.model.DailyBudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyBudgetDao {
    @Query("SELECT * FROM daily_budgets WHERE dayKey = :dayKey")
    fun observeForDay(dayKey: String): Flow<DailyBudgetEntity?>

    @Query("SELECT * FROM daily_budgets WHERE dayKey = :dayKey")
    suspend fun getForDay(dayKey: String): DailyBudgetEntity?

    @Query("SELECT * FROM daily_budgets ORDER BY dayKey DESC")
    fun observeAll(): Flow<List<DailyBudgetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(budget: DailyBudgetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(budgets: List<DailyBudgetEntity>)

    @Query("DELETE FROM daily_budgets WHERE dayKey = :dayKey")
    suspend fun deleteForDay(dayKey: String)

    @Query("DELETE FROM daily_budgets")
    suspend fun deleteAll()

    /**
     * Free retention cleanup for budget-only days. A budget attached to a
     * ledger day is retained so a later verified paid entitlement can restore
     * the complete historical day without data loss.
     */
    @Query(
        "DELETE FROM daily_budgets " +
            "WHERE dayKey < :beforeDayKey " +
            "AND NOT EXISTS (SELECT 1 FROM entries WHERE entries.date = daily_budgets.dayKey)"
    )
    suspend fun deleteOrphanedBefore(beforeDayKey: String)
}
