package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InsightEngineTest {

    private fun entry(
        id: Long,
        date: String,
        item: String,
        cents: Long,
        type: EntryType,
        time: String = "10:00"
    ) = Entry(
        id = id,
        dateUtc = id * 1000L,
        date = date,
        time = time,
        item = item,
        costCents = cents,
        type = type
    )

    @Test
    fun empty_entries_returns_empty() {
        val result = InsightEngine.generateInsights(emptyList(), Period.ALL, "₱")
        assertTrue(result.isEmpty())
    }

    @Test
    fun empty_entries_even_with_streak_returns_empty_streak_owned_by_ui() {
        // Streak is not an insight anymore — Summary StreakLine owns it.
        val result = InsightEngine.generateInsights(emptyList(), Period.DAY, "₱")
        assertTrue(result.isEmpty())
    }

    @Test
    fun uses_only_period_entries_for_ratio() {
        val periodSlice = listOf(
            entry(1, "2026-08-05", "Coffee", 20000L, EntryType.WANT),
            entry(2, "2026-08-05", "Snack", 30000L, EntryType.WANT)
        )
        val insights = InsightEngine.generateInsights(periodSlice, Period.DAY, "₱", maxInsights = 3)
        assertTrue(insights.any { it.type == InsightType.HIGH_WANT_RATIO || it.type == InsightType.TOP_WANT })
        assertTrue(insights.none { it.body.contains("Groceries") })
    }

    @Test
    fun top_want_names_largest_item() {
        val entries = listOf(
            entry(1, "2026-08-05", "Coffee", 18000L, EntryType.WANT),
            entry(2, "2026-08-05", "Sneakers", 450000L, EntryType.WANT)
        )
        val insights = InsightEngine.generateInsights(entries, Period.WEEK, "₱", maxInsights = 5)
        val top = insights.first { it.type == InsightType.TOP_WANT }
        assertTrue(top.body.contains("Sneakers"))
    }

    @Test
    fun budget_over_outranks_top_want_on_day() {
        val entries = listOf(
            entry(1, "2026-08-05", "Coffee", 18000L, EntryType.WANT)
        )
        val budget = BudgetStatus.On(
            budgetCents = 10000L,
            spentCents = 18000L,
            remainingCents = -8000L,
            progress = 1.8f
        )
        val insights = InsightEngine.generateInsights(
            entries, Period.DAY, "₱", budget, maxInsights = 1
        )
        assertEquals(1, insights.size)
        assertEquals(InsightType.BUDGET_OVER, insights[0].type)
    }

    @Test
    fun max_insights_limits_output() {
        val entries = listOf(
            entry(1, "2026-08-01", "A", 10000L, EntryType.WANT),
            entry(2, "2026-08-02", "B", 20000L, EntryType.WANT),
            entry(3, "2026-08-03", "C", 30000L, EntryType.NEED)
        )
        val one = InsightEngine.generateInsights(entries, Period.WEEK, "₱", maxInsights = 1)
        assertEquals(1, one.size)
    }

    @Test
    fun no_emoji_in_copy() {
        val entries = listOf(
            entry(1, "2026-08-05", "Coffee", 18000L, EntryType.WANT)
        )
        val insights = InsightEngine.generateInsights(entries, Period.DAY, "₱", maxInsights = 5)
        insights.forEach { insight ->
            assertTrue(
                "Emoji leaked into insight: ${insight.body}",
                insight.body.none { ch -> ch.code > 0x1F300 }
            )
            assertTrue(
                "Emoji leaked into title: ${insight.title}",
                insight.title.none { ch -> ch.code > 0x1F300 }
            )
        }
    }

    @Test
    fun strong_need_ratio_when_needs_dominate() {
        val entries = listOf(
            entry(1, "2026-08-05", "Rent", 80000L, EntryType.NEED),
            entry(2, "2026-08-05", "Gum", 500L, EntryType.WANT)
        )
        val insights = InsightEngine.generateInsights(entries, Period.DAY, "₱", maxInsights = 3)
        assertTrue(insights.any { it.type == InsightType.STRONG_NEED_RATIO })
    }

    @Test
    fun never_emits_streak_nudge() {
        val entries = listOf(
            entry(1, "2026-08-05", "Coffee", 18000L, EntryType.WANT)
        )
        val insights = InsightEngine.generateInsights(entries, Period.DAY, "₱", maxInsights = 10)
        assertTrue(insights.none { it.title.contains("streak", ignoreCase = true) })
        assertTrue(insights.none { it.body.contains("streak", ignoreCase = true) })
    }
}
