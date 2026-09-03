package com.needsvswants.app.ui.screens.summary

import com.needsvswants.app.domain.Period
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class SummaryPeriodRangeTest {

    private fun fixedCalendar(): Calendar =
        Calendar.getInstance().apply {
            set(2026, Calendar.AUGUST, 6, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

    @Test
    fun day_returns_today_only() {
        val range = getPeriodRange(Period.DAY, paid = false, baseDate = fixedCalendar())
        assertEquals("Aug 6", range)
    }

    @Test
    fun week_returns_inclusive_seven_days() {
        val range = getPeriodRange(Period.WEEK, paid = false, baseDate = fixedCalendar())
        assertEquals("Jul 31–Aug 6", range)
    }

    @Test
    fun month_returns_inclusive_thirty_days() {
        val range = getPeriodRange(Period.MONTH, paid = false, baseDate = fixedCalendar())
        assertEquals("Jul 8–Aug 6", range)
    }

    @Test
    fun all_paid_returns_first_entry_copy() {
        val range = getPeriodRange(Period.ALL, paid = true, baseDate = fixedCalendar())
        assertEquals("Since your first entry", range)
    }

    @Test
    fun all_free_returns_thirty_day_retention_range() {
        val range = getPeriodRange(Period.ALL, paid = false, baseDate = fixedCalendar())
        assertEquals("Jul 7–Aug 6", range)
    }
}
