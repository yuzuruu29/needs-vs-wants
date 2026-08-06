package com.needsvswants.app.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * Pure consecutive-day logging streak math.
 * Dates are yyyy-MM-dd strings matching Entry.date (local calendar).
 *
 * SimpleDateFormat is created per call — the class is not thread-safe and
 * streak is read from UI, widget, and WorkManager workers concurrently.
 */
object StreakMath {
    private val dayMs = TimeUnit.DAYS.toMillis(1)

    private fun dayFormatter(): SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            isLenient = false
            timeZone = TimeZone.getDefault()
        }

    /**
     * @param distinctDates unique log dates (any order)
     * @param todayEpochMs reference "now" (injectable for tests)
     * @return consecutive days ending on today, or yesterday if today has no entry yet.
     *         If the tip is older than yesterday, streak is 0.
     */
    fun currentStreak(
        distinctDates: Collection<String>,
        todayEpochMs: Long = System.currentTimeMillis()
    ): Int {
        if (distinctDates.isEmpty()) return 0
        val daySet = distinctDates.mapNotNull { parseDayStart(it) }.toSortedSet()
        if (daySet.isEmpty()) return 0

        val todayStart = startOfDay(todayEpochMs)
        val yesterdayStart = todayStart - dayMs

        val tip = when {
            todayStart in daySet -> todayStart
            yesterdayStart in daySet -> yesterdayStart
            else -> return 0
        }

        var streak = 0
        var cursor = tip
        while (cursor in daySet) {
            streak++
            cursor -= dayMs
        }
        return streak
    }

    /**
     * @param distinctDates unique log dates (any order)
     * @return longest consecutive run in the entire history
     */
    fun bestStreak(distinctDates: Collection<String>): Int {
        if (distinctDates.isEmpty()) return 0
        val daySet = distinctDates.mapNotNull { parseDayStart(it) }.toSortedSet()
        if (daySet.isEmpty()) return 0

        var best = 0
        var current = 0
        var prev: Long? = null

        for (day in daySet) {
            if (prev == null || day == prev + dayMs) {
                current++
            } else {
                current = 1
            }
            if (current > best) {
                best = current
            }
            prev = day
        }
        return best
    }

    private fun parseDayStart(date: String): Long? {
        return try {
            val parsed = dayFormatter().parse(date) ?: return null
            startOfDay(parsed.time)
        } catch (_: Exception) {
            null
        }
    }

    private fun startOfDay(epochMs: Long): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = epochMs
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    /** Test helper: format an epoch as yyyy-MM-dd. */
    fun formatDay(epochMs: Long): String = dayFormatter().format(Date(epochMs))
}
