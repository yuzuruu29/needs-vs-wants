package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExportUseCaseTest {

    @Test
    fun exportCsv_header_and_cents_not_formatted_money() {
        val entries = listOf(
            Entry(
                id = 1,
                dateUtc = 1000L,
                date = "2026-08-05",
                time = "10:00",
                item = "Groceries",
                costCents = 150000L,
                type = EntryType.NEED
            ),
            Entry(
                id = 2,
                dateUtc = 2000L,
                date = "2026-08-05",
                time = "14:30",
                item = "Espresso",
                costCents = 18000L,
                type = EntryType.WANT
            )
        )

        val csv = ExportUseCase.exportCsv(entries)

        assertTrue(csv.startsWith("date,time,item,cost_cents,type\n"))
        assertTrue(csv.contains("150000"))
        assertTrue(csv.contains("18000"))
        assertTrue(csv.contains("NEED"))
        assertTrue(csv.contains("WANT"))
        // No currency decoration (D2: cents are the export unit).
        assertFalse(csv.contains("₱"))
        assertFalse(csv.contains("Grand Total"))
    }

    @Test
    fun csvField_quotes_commas_and_doubles_quotes() {
        assertEquals("plain", ExportUseCase.csvField("plain"))
        assertEquals("\"a,b\"", ExportUseCase.csvField("a,b"))
        assertEquals("\"say \"\"hi\"\"\"", ExportUseCase.csvField("say \"hi\""))
    }

    @Test
    fun exportCsv_escapes_item_with_comma() {
        val entries = listOf(
            Entry(
                id = 1,
                dateUtc = 1000L,
                date = "2026-08-05",
                time = "10:00",
                item = "Coffee, large",
                costCents = 12000L,
                type = EntryType.WANT
            )
        )
        val csv = ExportUseCase.exportCsv(entries)
        assertTrue(csv.contains("\"Coffee, large\""))
    }

    @Test
    fun empty_list_is_header_only() {
        val csv = ExportUseCase.exportCsv(emptyList())
        assertEquals("date,time,item,cost_cents,type\n", csv)
    }
}
