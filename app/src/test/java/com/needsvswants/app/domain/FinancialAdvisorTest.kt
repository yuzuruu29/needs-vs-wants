package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class FinancialAdvisorTest {

    private val dayMs = TimeUnit.DAYS.toMillis(1)

    /** Fixed reference "now" (any epoch; day windows are resolved in the local time zone). */
    private val NOW = 1786320000000L

    private fun dayStart(epochMs: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = epochMs
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun entry(
        nowEpochMs: Long = NOW,
        daysAgo: Int,
        item: String,
        costCents: Long,
        type: EntryType,
        time: String = "10:00"
    ): Entry {
        val dateUtc = dayStart(nowEpochMs) - daysAgo * dayMs
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(dateUtc))
        return Entry(
            id = daysAgo.toLong(),
            dateUtc = dateUtc,
            date = date,
            time = time,
            item = item,
            costCents = costCents,
            type = type
        )
    }

    private fun pack(
        entries: List<Entry>,
        dailyBudgetCents: Long? = null,
        spendingGoal: String = "track",
        nowEpochMs: Long = NOW
    ): AdvisorContextPack =
        AdvisorContextPack.build(entries, dailyBudgetCents, spendingGoal, nowEpochMs)

    // --- AdvisorContextPack math -------------------------------------------------

    @Test
    fun contextPack_computesPctAndTotals() {
        val ctx = pack(
            listOf(
                entry(daysAgo = 0, item = "Groceries", costCents = 150_00, type = EntryType.NEED),
                entry(daysAgo = 0, item = "Snack", costCents = 50_00, type = EntryType.WANT)
            )
        )

        assertEquals(200_00L, ctx.todayTotalCents)
        assertEquals(200_00L, ctx.weekTotalCents)
        assertEquals(150_00L, ctx.needsCents)
        assertEquals(50_00L, ctx.wantsCents)
        assertEquals(75.0, ctx.needsPct, 0.001)
        assertEquals(25.0, ctx.wantsPct, 0.001)
    }

    @Test
    fun contextPack_weekWindowIsLastSevenCalendarDays() {
        val ctx = pack(
            listOf(
                entry(daysAgo = 0, item = "Today", costCents = 100_00, type = EntryType.NEED),
                entry(daysAgo = 6, item = "Six days ago", costCents = 200_00, type = EntryType.NEED),
                entry(daysAgo = 7, item = "A week ago", costCents = 999_00, type = EntryType.WANT)
            )
        )

        assertEquals(100_00L, ctx.todayTotalCents)
        assertEquals(300_00L, ctx.weekTotalCents) // 7-days-ago entry excluded
        assertEquals(300_00L, ctx.needsCents)
        assertEquals(0L, ctx.wantsCents)
    }

    @Test
    fun contextPack_topWantItems_sortedDescMaxThreeNeedsExcluded() {
        val ctx = pack(
            listOf(
                entry(daysAgo = 0, item = "Need1", costCents = 999_00, type = EntryType.NEED),
                entry(daysAgo = 0, item = "WantSmall", costCents = 100_00, type = EntryType.WANT),
                entry(daysAgo = 0, item = "WantBig", costCents = 500_00, type = EntryType.WANT),
                entry(daysAgo = 0, item = "WantMid", costCents = 300_00, type = EntryType.WANT),
                entry(daysAgo = 0, item = "WantSecond", costCents = 200_00, type = EntryType.WANT)
            )
        )

        assertEquals(listOf("WantBig", "WantMid", "WantSecond"), ctx.topWantItems)
    }

    @Test
    fun contextPack_budgetOffWhenNull_orZero() {
        val noBudget = pack(listOf(entry(daysAgo = 0, item = "X", costCents = 100_00, type = EntryType.NEED)))
        assertFalse(noBudget.budgetOn)
        assertEquals(0L, noBudget.remainingCents)

        val zeroBudget = pack(
            listOf(entry(daysAgo = 0, item = "X", costCents = 100_00, type = EntryType.NEED)),
            dailyBudgetCents = 0
        )
        assertFalse(zeroBudget.budgetOn)
    }

    @Test
    fun contextPack_budgetRemaining_negativeWhenOver() {
        val over = pack(
            listOf(entry(daysAgo = 0, item = "X", costCents = 120_00, type = EntryType.WANT)),
            dailyBudgetCents = 100_00
        )
        assertTrue(over.budgetOn)
        assertEquals(-20_00L, over.remainingCents)

        val under = pack(
            listOf(entry(daysAgo = 0, item = "X", costCents = 80_00, type = EntryType.NEED)),
            dailyBudgetCents = 100_00
        )
        assertEquals(20_00L, under.remainingCents)
    }

    @Test
    fun contextPack_streakDays() {
        val threeDay = pack(
            (0..2).map { entry(daysAgo = it, item = "Log", costCents = 10_00, type = EntryType.NEED) }
        )
        assertEquals(3, threeDay.streakDays)

        val singleToday = pack(listOf(entry(daysAgo = 0, item = "Log", costCents = 10_00, type = EntryType.NEED)))
        assertEquals(1, singleToday.streakDays)
    }

    @Test
    fun contextPack_spendingGoalPassthrough() {
        assertEquals("budget", pack(emptyList(), spendingGoal = "budget").spendingGoal)
        assertEquals("track", pack(emptyList(), spendingGoal = "track").spendingGoal)
        assertEquals("analyze", pack(emptyList(), spendingGoal = "analyze").spendingGoal)
    }

    // --- generateInsight rules (each must carry a Section citation) ---------------

    @Test
    fun generateInsight_whenOverDailyBudget_returnsRecoveryWarning() {
        val ctx = pack(
            listOf(entry(daysAgo = 0, item = "Dinner", costCents = 600_00, type = EntryType.WANT)),
            dailyBudgetCents = 500_00
        )

        val insight = FinancialAdvisorEngine.generateInsight(ctx)

        assertTrue(insight.isWarning)
        assertEquals("Compensatory Budget Recovery Active", insight.headline)
        assertTrue(insight.advice.contains("exceeded today's budget limit"))
        assertEquals("Notebook #3: Impulse Recovery", insight.citation.title)
        assertTrue(insight.citation.section.contains("Section"))
    }

    @Test
    fun generateInsight_whenWantsExceedNeeds_returnsEquilibriumWarning() {
        val ctx = pack(
            listOf(
                entry(daysAgo = 0, item = "Groceries", costCents = 100_00, type = EntryType.NEED),
                entry(daysAgo = 0, item = "Gadget", costCents = 300_00, type = EntryType.WANT)
            ),
            dailyBudgetCents = 1000_00
        )

        val insight = FinancialAdvisorEngine.generateInsight(ctx)

        assertTrue(insight.isWarning)
        assertEquals("Discretionary Spending Exceeds Baseline Needs", insight.headline)
        assertEquals("Notebook #1: Budgetary Equilibrium", insight.citation.title)
        assertTrue(insight.citation.section.contains("Section"))
    }

    @Test
    fun generateInsight_whenStreakFive_returnsWeekendPlan() {
        val ctx = pack(
            (0..4).map { entry(daysAgo = it, item = "Staples", costCents = 100_00, type = EntryType.NEED) }
        )

        val insight = FinancialAdvisorEngine.generateInsight(ctx)

        assertFalse(insight.isWarning)
        assertEquals("Weekend Streak Plan", insight.headline)
        assertTrue(insight.advice.contains("5-day"))
        assertTrue(insight.citation.section.contains("Section"))
    }

    @Test
    fun generateInsight_whenStreakTwoToFour_returnsStreakMomentum() {
        val ctx = pack(
            (0..2).map { entry(daysAgo = it, item = "Staples", costCents = 100_00, type = EntryType.NEED) }
        )

        val insight = FinancialAdvisorEngine.generateInsight(ctx)

        assertFalse(insight.isWarning)
        assertEquals("Streak Momentum", insight.headline)
        assertTrue(insight.advice.contains("3-day"))
        assertTrue(insight.citation.section.contains("Section"))
    }

    @Test
    fun generateInsight_whenAnalyzeGoal_returnsWeeklyComparison() {
        val ctx = pack(
            listOf(entry(daysAgo = 0, item = "Groceries", costCents = 100_00, type = EntryType.NEED)),
            spendingGoal = "analyze"
        )

        val insight = FinancialAdvisorEngine.generateInsight(ctx)

        assertFalse(insight.isWarning)
        assertEquals("Analyze Goal: Weekly Comparison", insight.headline)
        assertTrue(insight.advice.contains("100"))
        assertTrue(insight.citation.section.contains("Section"))
    }

    @Test
    fun generateInsight_whenWithinBudget_returnsBudgetHealth() {
        val ctx = pack(
            listOf(
                entry(daysAgo = 0, item = "Groceries", costCents = 100_00, type = EntryType.NEED),
                entry(daysAgo = 0, item = "Snack", costCents = 50_00, type = EntryType.WANT)
            ),
            dailyBudgetCents = 1000_00
        )

        val insight = FinancialAdvisorEngine.generateInsight(ctx)

        assertFalse(insight.isWarning)
        assertEquals("Budget Health: Within Daily Limit", insight.headline)
        assertTrue(insight.advice.contains("remaining"))
        assertTrue(insight.citation.section.contains("Section"))
    }

    @Test
    fun generateInsight_whenBalanced_returnsStandardTargetInsight() {
        val ctx = pack(
            listOf(
                entry(daysAgo = 0, item = "Rent", costCents = 500_00, type = EntryType.NEED),
                entry(daysAgo = 0, item = "Coffee", costCents = 100_00, type = EntryType.WANT)
            )
        )

        val insight = FinancialAdvisorEngine.generateInsight(ctx)

        assertFalse(insight.isWarning)
        assertEquals("Spending Within Economic Study Targets", insight.headline)
        assertEquals("Notebook #2: Behavioral Friction", insight.citation.title)
        assertTrue(insight.citation.section.contains("Section"))
    }

    @Test
    fun generateInsight_everyRuleReturnsNonBlankSectionCitation() {
        val cases = listOf(
            // Rule 1: over-budget recovery
            pack(
                listOf(entry(daysAgo = 0, item = "Gadget", costCents = 600_00, type = EntryType.WANT)),
                dailyBudgetCents = 500_00
            ),
            // Rule 2: wants > needs
            pack(
                listOf(
                    entry(daysAgo = 0, item = "Food", costCents = 100_00, type = EntryType.NEED),
                    entry(daysAgo = 0, item = "Gadget", costCents = 300_00, type = EntryType.WANT)
                ),
                dailyBudgetCents = 1000_00
            ),
            // Rule 3: weekend plan (streak >= 5)
            pack((0..4).map { entry(daysAgo = it, item = "Staples", costCents = 100_00, type = EntryType.NEED) }),
            // Rule 4: streak momentum (streak 2..4)
            pack((0..2).map { entry(daysAgo = it, item = "Staples", costCents = 100_00, type = EntryType.NEED) }),
            // Rule 5: analyze-goal comparison
            pack(listOf(entry(daysAgo = 0, item = "Food", costCents = 100_00, type = EntryType.NEED)), spendingGoal = "analyze"),
            // Rule 6: budget health OK
            pack(
                listOf(
                    entry(daysAgo = 0, item = "Food", costCents = 100_00, type = EntryType.NEED),
                    entry(daysAgo = 0, item = "Snack", costCents = 50_00, type = EntryType.WANT)
                ),
                dailyBudgetCents = 1000_00
            ),
            // Rule 7: balanced fallback
            pack(
                listOf(
                    entry(daysAgo = 0, item = "Food", costCents = 100_00, type = EntryType.NEED),
                    entry(daysAgo = 0, item = "Snack", costCents = 50_00, type = EntryType.WANT)
                )
            )
        )

        cases.forEachIndexed { index, ctx ->
            val insight = FinancialAdvisorEngine.generateInsight(ctx)
            assertTrue("rule $index returned a blank citation", insight.citation.section.isNotBlank())
            assertTrue(
                "rule $index citation must reference a Section, was: ${insight.citation.section}",
                insight.citation.section.contains("Section")
            )
        }
    }

    // --- evaluateConversationalQuery branches (each must carry a Section citation) -

    @Test
    fun evaluateConversationalQuery_handlesOverspendQuestion() {
        val ctx = pack(
            listOf(entry(daysAgo = 0, item = "Shirt", costCents = 600_00, type = EntryType.WANT)),
            dailyBudgetCents = 500_00
        )

        val chatMsg = FinancialAdvisorEngine.evaluateConversationalQuery("Am I over budget today?", ctx)

        assertEquals(ChatSender.ADVISOR, chatMsg.sender)
        assertTrue(chatMsg.isWarning)
        assertTrue(chatMsg.text.contains("over budget"))
        assertNotNull(chatMsg.citation)
        assertTrue(chatMsg.citation!!.section.contains("Section"))
    }

    @Test
    fun evaluateConversationalQuery_handlesWantPurchaseQuestion() {
        val ctx = pack(
            listOf(entry(daysAgo = 0, item = "Food", costCents = 200_00, type = EntryType.NEED)),
            dailyBudgetCents = 1000_00
        )

        val chatMsg = FinancialAdvisorEngine.evaluateConversationalQuery("Can I buy a Want item?", ctx)

        assertEquals(ChatSender.ADVISOR, chatMsg.sender)
        assertFalse(chatMsg.isWarning)
        assertTrue(chatMsg.text.contains("capacity for a deliberate Want"))
        assertTrue(chatMsg.citation!!.section.contains("Section"))
    }

    @Test
    fun evaluateConversationalQuery_wantsHeavy_advisesHold() {
        val ctx = pack(
            listOf(
                entry(daysAgo = 0, item = "Food", costCents = 100_00, type = EntryType.NEED),
                entry(daysAgo = 0, item = "Gadget", costCents = 300_00, type = EntryType.WANT)
            )
        )

        val chatMsg = FinancialAdvisorEngine.evaluateConversationalQuery("Can I buy a Want item?", ctx)

        assertTrue(chatMsg.isWarning)
        assertTrue(chatMsg.text.contains("24 hours"))
        assertTrue(chatMsg.citation!!.section.contains("Section"))
    }

    @Test
    fun evaluateConversationalQuery_everyBranchReturnsNonBlankSectionCitation() {
        val base = pack(
            listOf(entry(daysAgo = 0, item = "Groceries", costCents = 100_00, type = EntryType.NEED)),
            dailyBudgetCents = 1000_00
        )
        val cases = listOf(
            "How is my Need to Want ratio?" to base,        // branch 1: ratio / share
            "Plan my weekend spending" to base,             // branch 2: weekend
            "What is my streak?" to base,                   // branch 3: streak
            "Is my budget health okay?" to base,            // branch 4: budget health
            "Analyze my week" to base,                      // branch 5: goal / analyze
            "Am I over budget today?" to base,              // branch 6: overspend / budget
            "Can I buy a Want item today?" to base,         // branch 7: buy / afford
            "hello there" to base                           // branch 8: default
        )

        cases.forEach { (query, ctx) ->
            val msg = FinancialAdvisorEngine.evaluateConversationalQuery(query, ctx)
            val citation = msg.citation
            assertNotNull("query [$query] must return a citation", citation)
            assertTrue("query [$query] returned a blank citation", citation!!.section.isNotBlank())
            assertTrue(
                "query [$query] citation must reference a Section, was: ${citation.section}",
                citation.section.contains("Section")
            )
        }
    }
}
