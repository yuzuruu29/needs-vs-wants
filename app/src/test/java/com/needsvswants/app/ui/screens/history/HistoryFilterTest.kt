package com.needsvswants.app.ui.screens.history

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.domain.Period
import com.needsvswants.app.domain.PeriodWindow
import org.junit.Assert.assertEquals
import org.junit.Test

class HistoryFilterTest {

    private val entries = listOf(
        Entry(1, 300L, "2026-08-13", "07:00 PM", "Kape latte", 15_000L, EntryType.WANT),
        Entry(2, 200L, "2026-08-12", "12:00 PM", "Rice 5kg", 120_000L, EntryType.NEED),
        Entry(3, 100L, "2026-08-01", "09:00 AM", "Jeep fare", 2_400L, EntryType.NEED)
    )

    @Test
    fun `blank query and no type returns list unchanged`() {
        assertEquals(entries, filterHistoryEntries(entries, "", null))
        assertEquals(entries, filterHistoryEntries(entries, "   ", null))
    }

    @Test
    fun `item search is case-insensitive`() {
        assertEquals(listOf(entries[0]), filterHistoryEntries(entries, "KAPE", null))
        assertEquals(listOf(entries[1]), filterHistoryEntries(entries, "rice", null))
    }

    @Test
    fun `date substring matches`() {
        assertEquals(listOf(entries[0]), filterHistoryEntries(entries, "08-13", null))
        assertEquals(entries, filterHistoryEntries(entries, "2026", null))
    }

    @Test
    fun `type filter narrows and combines with query`() {
        assertEquals(
            listOf(entries[1], entries[2]),
            filterHistoryEntries(entries, "", EntryType.NEED)
        )
        assertEquals(
            listOf(entries[2]),
            filterHistoryEntries(entries, "fare", EntryType.NEED)
        )
        assertEquals(
            emptyList<Entry>(),
            filterHistoryEntries(entries, "fare", EntryType.WANT)
        )
    }

    // --- Period windows (Day / Week / Month / All) ----------------------------

    private val dayMs = 24L * 60L * 60L * 1_000L
    private val now = 1_760_000_000_000L
    private val todayStart = PeriodWindow.startOfDay(now)

    /** Entry stamped [daysAgo] days before today's local midnight, plus an hour. */
    private fun entryDaysAgo(id: Long, daysAgo: Int): Entry =
        Entry(
            id = id,
            dateUtc = todayStart - daysAgo * dayMs + 60L * 60L * 1_000L,
            date = "2026-01-01",
            time = "01:00 PM",
            item = "Item $id",
            costCents = 1_000L,
            type = EntryType.NEED
        )

    @Test
    fun `day window keeps only today`() {
        val list = listOf(entryDaysAgo(1, 0), entryDaysAgo(2, 1))
        assertEquals(listOf(list[0]), withinPeriod(list, Period.DAY, now))
    }

    @Test
    fun `week window keeps 7 calendar days including today`() {
        val list = listOf(
            entryDaysAgo(1, 0),
            entryDaysAgo(2, 6),
            entryDaysAgo(3, 7),
            entryDaysAgo(4, 8)
        )
        // WEEK starts at local midnight 6 days ago → 7 calendar days including
        // today; anything stamped before that midnight falls out.
        assertEquals(listOf(list[0], list[1]), withinPeriod(list, Period.WEEK, now))
    }

    @Test
    fun `month window covers the full 30-day ledger`() {
        val list = listOf(
            entryDaysAgo(1, 0),
            entryDaysAgo(2, 29),
            entryDaysAgo(3, 30),
            entryDaysAgo(4, 40)
        )
        assertEquals(listOf(list[0], list[1]), withinPeriod(list, Period.MONTH, now))
    }

    @Test
    fun `all window passes entries through unchanged`() {
        assertEquals(entries, withinPeriod(entries, Period.ALL, now))
    }
}
