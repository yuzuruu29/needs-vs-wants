package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType

/**
 * Period-scoped spending insights in ledger voice.
 * Callers must pass [periodEntries] already filtered to the active period.
 * Streak UI is owned by Summary — this engine never emits streak copy.
 */
object InsightEngine {

    fun generateInsights(
        periodEntries: List<Entry>,
        period: Period,
        currencySymbol: String,
        budgetStatus: BudgetStatus = BudgetStatus.Off,
        maxInsights: Int = 1
    ): List<Insight> {
        if (periodEntries.isEmpty()) return emptyList()

        val candidates = mutableListOf<Insight>()

        val needs = periodEntries.filter { it.type == EntryType.NEED }
        val wants = periodEntries.filter { it.type == EntryType.WANT }
        val totalCents = periodEntries.sumOf { it.costCents }
        val needsCents = needs.sumOf { it.costCents }
        val wantsCents = wants.sumOf { it.costCents }
        val allDates = periodEntries.map { it.date }.distinct()

        if (period == Period.DAY && budgetStatus is BudgetStatus.On && budgetStatus.remainingCents < 0) {
            val over = (-budgetStatus.remainingCents).toMoney(currencySymbol)
            candidates += Insight(
                type = InsightType.BUDGET_OVER,
                title = "Over daily limit",
                body = "Today's spend is $over past your guardrail.",
                accent = InsightAccent.ALERT,
                priority = 100
            )
        }

        if (totalCents > 0) {
            val needPct = ((needsCents * 100) / totalCents).toInt()
            when {
                needPct < 40 -> candidates += Insight(
                    type = InsightType.HIGH_WANT_RATIO,
                    title = "Want-heavy period",
                    body = "Only $needPct% went to Needs (${needsCents.toMoney(currencySymbol)} of ${totalCents.toMoney(currencySymbol)}).",
                    accent = InsightAccent.ALERT,
                    priority = 90
                )
                needPct >= 70 -> candidates += Insight(
                    type = InsightType.STRONG_NEED_RATIO,
                    title = "Needs led",
                    body = "$needPct% of spend was Needs (${needsCents.toMoney(currencySymbol)}).",
                    accent = InsightAccent.POSITIVE,
                    priority = 70
                )
            }
        }

        val topWant = wants.maxByOrNull { it.costCents }
        if (topWant != null && wantsCents > 0) {
            candidates += Insight(
                type = InsightType.TOP_WANT,
                title = "Largest Want",
                body = "${topWant.item} · ${topWant.costCents.toMoney(currencySymbol)}",
                accent = InsightAccent.NEUTRAL,
                priority = 80
            )
        }

        val datesWithWants = wants.map { it.date }.toSet()
        val wantFreeCount = allDates.count { it !in datesWithWants }
        if (wantFreeCount > 0 && allDates.size >= 2) {
            candidates += Insight(
                type = InsightType.WANT_FREE_DAYS,
                title = "Want-free days",
                body = "$wantFreeCount of ${allDates.size} logged days had zero Want spend.",
                accent = InsightAccent.POSITIVE,
                priority = 60
            )
        }

        if (allDates.isNotEmpty() && period != Period.DAY) {
            val avgCents = totalCents / allDates.size.coerceAtLeast(1)
            candidates += Insight(
                type = InsightType.AVG_DAILY_SPEND,
                title = "Average active day",
                body = "${avgCents.toMoney(currencySymbol)} across ${allDates.size} logged days.",
                accent = InsightAccent.NEUTRAL,
                priority = 50
            )
        }

        return candidates
            .sortedByDescending { it.priority }
            .distinctBy { it.type }
            .take(maxInsights.coerceAtLeast(0))
    }
}
