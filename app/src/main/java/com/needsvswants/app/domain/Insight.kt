package com.needsvswants.app.domain

/**
 * Only types the engine actually produces.
 * Streak is owned by Summary [StreakLine] — not duplicated as an insight.
 */
enum class InsightType {
    BUDGET_OVER,
    HIGH_WANT_RATIO,
    TOP_WANT,
    STRONG_NEED_RATIO,
    WANT_FREE_DAYS,
    AVG_DAILY_SPEND
}

enum class InsightAccent {
    POSITIVE,
    NEUTRAL,
    ALERT
}

data class Insight(
    val type: InsightType,
    val title: String,
    val body: String,
    val accent: InsightAccent,
    /** Higher wins when selecting which insight to show. */
    val priority: Int
)
