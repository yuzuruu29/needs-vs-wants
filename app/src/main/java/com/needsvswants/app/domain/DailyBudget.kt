package com.needsvswants.app.domain

sealed class BudgetStatus {
    data object Off : BudgetStatus()
    data class On(
        val budgetCents: Long,
        val spentCents: Long,
        val remainingCents: Long,
        val progress: Float
    ) : BudgetStatus()
}

object DailyBudgetMath {
    fun status(budgetCents: Long?, spentCents: Long): BudgetStatus {
        if (budgetCents == null || budgetCents <= 0L) return BudgetStatus.Off
        val remaining = budgetCents - spentCents
        val progress = spentCents.toFloat() / budgetCents.toFloat()
        return BudgetStatus.On(
            budgetCents = budgetCents,
            spentCents = spentCents,
            remainingCents = remaining,
            progress = progress
        )
    }

    fun wouldExceed(spentCents: Long, budgetCents: Long, newCostCents: Long): Boolean =
        spentCents + newCostCents > budgetCents
}