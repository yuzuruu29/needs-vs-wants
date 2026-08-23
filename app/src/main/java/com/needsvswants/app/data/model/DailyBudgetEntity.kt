package com.needsvswants.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/** One positive daily budget, stored as integer cents for one local day. */
@Entity(tableName = "daily_budgets")
data class DailyBudgetEntity(
    @PrimaryKey val dayKey: String,
    val budgetCents: Long
)
