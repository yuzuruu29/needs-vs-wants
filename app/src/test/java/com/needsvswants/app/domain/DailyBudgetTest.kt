package com.needsvswants.app.domain

import org.junit.Assert.*
import org.junit.Test

class DailyBudgetTest {

    @Test
    fun status_nullOrNonPositive_isOff() {
        assertEquals(BudgetStatus.Off, DailyBudgetMath.status(null, 1_000))
        assertEquals(BudgetStatus.Off, DailyBudgetMath.status(0L, 1_000))
        assertEquals(BudgetStatus.Off, DailyBudgetMath.status(-100L, 0))
    }

    @Test
    fun status_on_computesRemainingAndProgress() {
        val s = DailyBudgetMath.status(500_000L, 420_000L) as BudgetStatus.On
        assertEquals(500_000L, s.budgetCents)
        assertEquals(420_000L, s.spentCents)
        assertEquals(80_000L, s.remainingCents)
        assertEquals(0.84f, s.progress, 0.001f)
    }

    @Test
    fun status_over_allowsNegativeRemaining() {
        val s = DailyBudgetMath.status(100_000L, 150_000L) as BudgetStatus.On
        assertEquals(-50_000L, s.remainingCents)
        assertTrue(s.progress > 1f)
    }

    @Test
    fun wouldExceed_onlyWhenStrictlyOver() {
        assertFalse(DailyBudgetMath.wouldExceed(400_000, 500_000, 100_000)) // exact
        assertTrue(DailyBudgetMath.wouldExceed(400_000, 500_000, 100_001))
        assertTrue(DailyBudgetMath.wouldExceed(500_000, 500_000, 1))
        assertFalse(DailyBudgetMath.wouldExceed(0, 500_000, 500_000))
    }
}