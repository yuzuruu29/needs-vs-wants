package com.needsvswants.app.ui.screens.history

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
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
}
