package com.needsvswants.app.domain

/**
 * Quiet ledger milestones — typographic marks, not emoji badges.
 * Thresholds match the 35-day trainer cycle.
 */
enum class StreakMilestone(val days: Int, val label: String) {
    SPARK(3, "3-day mark"),
    WEEKLY(7, "Week solid"),
    FORTNIGHT(14, "Fortnight"),
    VETERAN(21, "Three weeks"),
    MASTER(35, "Full cycle");

    companion object {
        fun forStreak(days: Int): StreakMilestone? =
            entries.lastOrNull { days >= it.days }

        fun nextAfter(days: Int): StreakMilestone? =
            entries.firstOrNull { days < it.days }

        fun allEarned(days: Int): List<StreakMilestone> =
            entries.filter { days >= it.days }
    }
}
