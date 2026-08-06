package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.ui.screens.summary.SummaryViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class PeriodWindowTest {

    /** Fixed now = 2026-08-06 12:00 local. */
    private val nowMs: Long = Calendar.getInstance().apply {
        set(2026, Calendar.AUGUST, 6, 12, 0, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    private fun startOf(y: Int, m: Int, d: Int): Long =
        Calendar.getInstance().apply {
            set(y, m, d, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    @Test
    fun day_is_local_midnight_today() {
        val since = PeriodWindow.sinceEpochMs(Period.DAY, nowMs)
        assertEquals(startOf(2026, Calendar.AUGUST, 6), since)
    }

    @Test
    fun week_is_inclusive_seven_calendar_days() {
        // Label: Jul 31 – Aug 6 → start midnight Jul 31 (today − 6).
        val since = PeriodWindow.sinceEpochMs(Period.WEEK, nowMs)
        assertEquals(startOf(2026, Calendar.JULY, 31), since)
        val spanDays = (PeriodWindow.startOfDay(nowMs) - since) / (24L * 60 * 60 * 1000)
        assertEquals(6L, spanDays)
    }

    @Test
    fun all_uses_retention_cutoff_when_present() {
        val cutoff = startOf(2026, Calendar.JULY, 2)
        val since = PeriodWindow.sinceEpochMs(Period.ALL, nowMs, retentionCutoffAt = cutoff)
        assertEquals(cutoff, since)
    }

    @Test
    fun all_unlimited_starts_at_zero() {
        assertEquals(0L, PeriodWindow.sinceEpochMs(Period.ALL, nowMs, retentionCutoffAt = null))
    }

    @Test
    fun filterToPeriod_week_excludes_day_before_window() {
        val inWindow = Entry(
            id = 1,
            dateUtc = startOf(2026, Calendar.JULY, 31) + 1000,
            date = "2026-07-31",
            time = "10:00",
            item = "In",
            costCents = 100,
            type = EntryType.NEED
        )
        val before = Entry(
            id = 2,
            dateUtc = startOf(2026, Calendar.JULY, 30) + 1000,
            date = "2026-07-30",
            time = "10:00",
            item = "Out",
            costCents = 200,
            type = EntryType.WANT
        )
        val filtered = SummaryViewModel.filterToPeriod(
            listOf(inWindow, before),
            Period.WEEK,
            nowMs = nowMs
        )
        assertEquals(1, filtered.size)
        assertEquals("In", filtered[0].item)
    }

    @Test
    fun filterToPeriod_day_only_today() {
        val today = Entry(
            id = 1,
            dateUtc = startOf(2026, Calendar.AUGUST, 6) + 5000,
            date = "2026-08-06",
            time = "09:00",
            item = "Today",
            costCents = 50,
            type = EntryType.NEED
        )
        val yesterday = Entry(
            id = 2,
            dateUtc = startOf(2026, Calendar.AUGUST, 5) + 5000,
            date = "2026-08-05",
            time = "09:00",
            item = "Yday",
            costCents = 50,
            type = EntryType.NEED
        )
        val filtered = SummaryViewModel.filterToPeriod(
            listOf(today, yesterday),
            Period.DAY,
            nowMs = nowMs
        )
        assertEquals(1, filtered.size)
        assertTrue(filtered[0].item == "Today")
    }
}
