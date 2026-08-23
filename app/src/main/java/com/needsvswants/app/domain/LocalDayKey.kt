package com.needsvswants.app.domain

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Canonical local-calendar day identity shared by entries, budgets, and History. */
object LocalDayKey {
    const val PATTERN = "yyyy-MM-dd"

    fun today(nowEpochMs: Long = System.currentTimeMillis()): String = fromEpoch(nowEpochMs)

    fun fromEpoch(epochMs: Long): String = keyFor(calendarAt(epochMs))

    fun daysAgo(nowEpochMs: Long, days: Int): String = addDays(today(nowEpochMs), -days)

    fun addDays(dayKey: String, days: Int): String {
        val calendar = parseCalendar(dayKey)
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return keyFor(calendar)
    }

    fun startOfDay(epochMs: Long): Long {
        val calendar = calendarAt(epochMs)
        clearTime(calendar)
        return calendar.timeInMillis
    }

    fun millisUntilNextMidnight(nowEpochMs: Long = System.currentTimeMillis()): Long {
        val next = calendarAt(nowEpochMs)
        clearTime(next)
        next.add(Calendar.DAY_OF_YEAR, 1)
        return (next.timeInMillis - nowEpochMs).coerceAtLeast(1_000L)
    }

    fun isInPeriod(dayKey: String, period: Period, nowEpochMs: Long): Boolean {
        if (period == Period.ALL) return true
        val since = PeriodWindow.sinceEpochMs(period, nowEpochMs)
        return dayKey >= fromEpoch(since) && dayKey <= today(nowEpochMs)
    }

    private fun calendarAt(epochMs: Long): Calendar = Calendar.getInstance().apply {
        timeInMillis = epochMs
    }

    private fun clearTime(calendar: Calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    private fun keyFor(calendar: Calendar): String =
        String.format(
            Locale.US,
            "%04d-%02d-%02d",
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )

    private fun parseCalendar(dayKey: String): Calendar {
        val formatter = SimpleDateFormat(PATTERN, Locale.US).apply {
            isLenient = false
        }
        val position = ParsePosition(0)
        val date = formatter.parse(dayKey, position)
        require(date != null && position.index == dayKey.length) {
            "Invalid local day key: $dayKey"
        }
        return Calendar.getInstance().apply {
            time = date
            clearTime(this)
        }
    }
}
