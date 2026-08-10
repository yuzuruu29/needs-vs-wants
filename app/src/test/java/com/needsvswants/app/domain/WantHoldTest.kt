package com.needsvswants.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure tests for the pre-seal Want consult (Task 3, M2): 15%-of-remaining
 * threshold boundaries, wants-share > 55% rule, budget-off null, and the
 * citation guarantee on every verdict.
 */
class WantHoldTest {

    /** Citation format pin per spec: every coach answer cites "Section X.Y". */
    private val sectionCitation = Regex("Section \\d+\\.\\d+")

    private fun pack(
        budgetOn: Boolean = true,
        remainingCents: Long = 100_00,
        wantsPct: Double = 40.0,
        weekTotalCents: Long = 100_00
    ): AdvisorContextPack {
        val wantsCents = (weekTotalCents * wantsPct / 100.0).toLong()
        return AdvisorContextPack(
            todayTotalCents = 0,
            weekTotalCents = weekTotalCents,
            needsCents = weekTotalCents - wantsCents,
            wantsCents = wantsCents,
            wantsTodayCents = 0,
            needsPct = 100.0 - wantsPct,
            wantsPct = wantsPct,
            budgetOn = budgetOn,
            remainingCents = remainingCents,
            streakDays = 0,
            topWantItems = emptyList(),
            spendingGoal = "track"
        )
    }

    // --- 15% of remaining budget threshold (strictly greater) --------------

    @Test
    fun wantHold_atExactly15PercentOfRemaining_doesNotHold() {
        // remaining 10000 cents; 15% = 1500 cents (integer math).
        val suggestion = FinancialAdvisorEngine.wantHoldSuggestion(
            costCents = 15_00,
            context = pack(remainingCents = 100_00)
        )

        assertNotNull(suggestion)
        assertFalse(suggestion!!.hold)
    }

    @Test
    fun wantHold_oneCentOver15PercentOfRemaining_holds() {
        val suggestion = FinancialAdvisorEngine.wantHoldSuggestion(
            costCents = 15_01,
            context = pack(remainingCents = 100_00)
        )

        assertNotNull(suggestion)
        assertTrue(suggestion!!.hold)
    }

    @Test
    fun wantHold_wellOver15PercentOfRemaining_holds() {
        val suggestion = FinancialAdvisorEngine.wantHoldSuggestion(
            costCents = 30_00,
            context = pack(remainingCents = 100_00)
        )

        assertNotNull(suggestion)
        assertTrue(suggestion!!.hold)
    }

    @Test
    fun wantHold_noRemainingBudget_anyPositiveCostHolds() {
        // remaining 0: 15% of 0 is 0, so any positive cost exceeds it.
        val suggestion = FinancialAdvisorEngine.wantHoldSuggestion(
            costCents = 1,
            context = pack(remainingCents = 0)
        )

        assertNotNull(suggestion)
        assertTrue(suggestion!!.hold)
    }

    @Test
    fun wantHold_alreadyOverBudget_holds() {
        // Negative remaining (today over the daily limit) holds any Want.
        val suggestion = FinancialAdvisorEngine.wantHoldSuggestion(
            costCents = 5_00,
            context = pack(remainingCents = -10_00)
        )

        assertNotNull(suggestion)
        assertTrue(suggestion!!.hold)
    }

    // --- Wants share > 55% rule (strictly greater) --------------------------

    @Test
    fun wantHold_wantsShareAbove55_holdsEvenWhenCostIsTiny() {
        val suggestion = FinancialAdvisorEngine.wantHoldSuggestion(
            costCents = 100,
            context = pack(remainingCents = 100_00, wantsPct = 55.5)
        )

        assertNotNull(suggestion)
        assertTrue(suggestion!!.hold)
    }

    @Test
    fun wantHold_wantsShareExactly55_doesNotHold_whenCostIsSmall() {
        val suggestion = FinancialAdvisorEngine.wantHoldSuggestion(
            costCents = 100,
            context = pack(remainingCents = 100_00, wantsPct = 55.0)
        )

        assertNotNull(suggestion)
        assertFalse(suggestion!!.hold)
    }

    @Test
    fun wantHold_wantsShareBelow55_doesNotHold_whenCostIsSmall() {
        val suggestion = FinancialAdvisorEngine.wantHoldSuggestion(
            costCents = 100,
            context = pack(remainingCents = 100_00, wantsPct = 40.0)
        )

        assertNotNull(suggestion)
        assertFalse(suggestion!!.hold)
    }

    // --- Budget off: no guardrail, no verdict ---------------------------------

    @Test
    fun wantHold_budgetOff_returnsNull_evenWhenCostOrShareWouldHold() {
        assertNull(
            FinancialAdvisorEngine.wantHoldSuggestion(
                costCents = 90_00,
                context = pack(budgetOn = false, remainingCents = 100_00, wantsPct = 40.0)
            )
        )
        assertNull(
            FinancialAdvisorEngine.wantHoldSuggestion(
                costCents = 1,
                context = pack(budgetOn = false, remainingCents = 100_00, wantsPct = 90.0)
            )
        )
    }

    // --- Verdict content: reasons short + citations present -------------------

    @Test
    fun wantHold_holdVerdict_reasonAndCitationPresent() {
        val suggestion = FinancialAdvisorEngine.wantHoldSuggestion(
            costCents = 30_00,
            context = pack(remainingCents = 100_00)
        )

        assertTrue(suggestion!!.hold)
        assertTrue(suggestion.reason.isNotBlank())
        assertTrue(suggestion.reason.length <= 80)
        assertEquals(WantHold.CITATION_HOLD, suggestion.citation)
        assertTrue(sectionCitation.containsMatchIn(suggestion.citation))
    }

    @Test
    fun wantHold_okVerdict_reasonAndCitationPresent() {
        val suggestion = FinancialAdvisorEngine.wantHoldSuggestion(
            costCents = 100,
            context = pack(remainingCents = 100_00)
        )

        assertFalse(suggestion!!.hold)
        assertTrue(suggestion.reason.isNotBlank())
        assertEquals(WantHold.CITATION_OK, suggestion.citation)
        assertTrue(sectionCitation.containsMatchIn(suggestion.citation))
    }

    @Test
    fun wantHold_wantsShareReason_whenOnlyShareRuleTriggers() {
        val suggestion = FinancialAdvisorEngine.wantHoldSuggestion(
            costCents = 100,
            context = pack(remainingCents = 100_00, wantsPct = 70.0)
        )

        assertTrue(suggestion!!.hold)
        assertEquals("Wants are more than 55% of this week's spending.", suggestion.reason)
    }
}
