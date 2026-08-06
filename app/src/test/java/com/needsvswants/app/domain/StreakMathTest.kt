package com.needsvswants.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class StreakMathTest {

    /** Fixed "today" = 2026-08-05 local midnight. */
    private val todayMs: Long = Calendar.getInstance().apply {
        set(2026, Calendar.AUGUST, 5, 12, 0, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    @Test
    fun empty_is_zero() {
        assertEquals(0, StreakMath.currentStreak(emptyList(), todayMs))
    }

    @Test
    fun today_only_is_one() {
        assertEquals(1, StreakMath.currentStreak(listOf("2026-08-05"), todayMs))
    }

    @Test
    fun consecutive_ending_today() {
        assertEquals(
            4,
            StreakMath.currentStreak(
                listOf("2026-08-02", "2026-08-03", "2026-08-04", "2026-08-05"),
                todayMs
            )
        )
    }

    @Test
    fun gap_breaks_streak() {
        assertEquals(
            2,
            StreakMath.currentStreak(
                listOf("2026-08-01", "2026-08-04", "2026-08-05"),
                todayMs
            )
        )
    }

    @Test
    fun yesterday_tip_still_counts_when_today_empty() {
        assertEquals(
            3,
            StreakMath.currentStreak(
                listOf("2026-08-02", "2026-08-03", "2026-08-04"),
                todayMs
            )
        )
    }

    @Test
    fun older_than_yesterday_is_zero() {
        assertEquals(
            0,
            StreakMath.currentStreak(listOf("2026-08-01", "2026-08-02"), todayMs)
        )
    }

    @Test
    fun bestStreak_empty_is_zero() {
        assertEquals(0, StreakMath.bestStreak(emptyList()))
    }

    @Test
    fun bestStreak_single_is_one() {
        assertEquals(1, StreakMath.bestStreak(listOf("2026-08-05")))
    }

    @Test
    fun bestStreak_consecutive() {
        assertEquals(3, StreakMath.bestStreak(listOf("2026-08-02", "2026-08-03", "2026-08-04")))
    }

    @Test
    fun bestStreak_gap_in_middle() {
        assertEquals(
            4,
            StreakMath.bestStreak(
                listOf("2026-08-01", "2026-08-04", "2026-08-05", "2026-08-06", "2026-08-07")
            )
        )
    }

    @Test
    fun bestStreak_all_35_consecutive() {
        val fmt = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val cal = Calendar.getInstance().apply { set(2026, Calendar.JULY, 1) }
        val days = (1..35).map {
            val d = fmt.format(cal.time)
            cal.add(Calendar.DAY_OF_YEAR, 1)
            d
        }
        assertEquals(35, StreakMath.bestStreak(days))
    }

    @Test
    fun test_StreakMilestone() {
        assertEquals(null, StreakMilestone.forStreak(0))
        assertEquals(null, StreakMilestone.forStreak(1))
        assertEquals(StreakMilestone.SPARK, StreakMilestone.forStreak(3))
        assertEquals(StreakMilestone.WEEKLY, StreakMilestone.forStreak(7))
        assertEquals(StreakMilestone.FORTNIGHT, StreakMilestone.forStreak(14))
        assertEquals(StreakMilestone.VETERAN, StreakMilestone.forStreak(21))
        assertEquals(StreakMilestone.MASTER, StreakMilestone.forStreak(35))
        assertEquals(StreakMilestone.MASTER, StreakMilestone.forStreak(50))

        assertEquals(StreakMilestone.SPARK, StreakMilestone.nextAfter(0))
        assertEquals(StreakMilestone.SPARK, StreakMilestone.nextAfter(1))
        assertEquals(StreakMilestone.WEEKLY, StreakMilestone.nextAfter(3))
        assertEquals(StreakMilestone.FORTNIGHT, StreakMilestone.nextAfter(7))
        assertEquals(StreakMilestone.VETERAN, StreakMilestone.nextAfter(14))
        assertEquals(StreakMilestone.MASTER, StreakMilestone.nextAfter(21))
        assertEquals(null, StreakMilestone.nextAfter(35))
        assertEquals(null, StreakMilestone.nextAfter(50))
    }

    @Test
    fun milestones_are_typography_not_emoji() {
        StreakMilestone.entries.forEach { ms ->
            assertTrue(ms.label.isNotBlank())
            assertTrue(
                "Milestone label should not be emoji: ${ms.label}",
                ms.label.none { it.code > 0x1F300 }
            )
        }
    }
}
