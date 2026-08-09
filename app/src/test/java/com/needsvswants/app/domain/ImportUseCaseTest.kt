package com.needsvswants.app.domain

import com.needsvswants.app.data.model.EntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportUseCaseTest {

    @Test
    fun parseCsv_roundTripsExport_and_assigns_idZero() {
        val csv = "date,time,item,cost_cents,type\n" +
            "2026-08-05,10:00,Groceries,150000,NEED\n" +
            "2026-08-05,14:30,\"Espresso, large\",18000,WANT\n"

        val result = ImportUseCase.parseCsv(csv)

        assertEquals(2, result.entries.size)
        assertEquals(0, result.skippedCount)
        // Fresh imports always get id 0 so Room auto-generates a new row.
        assertTrue(result.entries.all { it.id == 0L })
        assertEquals(EntryType.NEED, result.entries[0].type)
        assertEquals("Espresso, large", result.entries[1].item)
        assertEquals(18000L, result.entries[1].costCents)
    }

    @Test
    fun parseCsv_skipsHeader_and_blankLines() {
        val csv = "date,time,item,cost_cents,type\n\n2026-08-05,09:00,Coffee,12000,WANT\n"
        val result = ImportUseCase.parseCsv(csv)
        assertEquals(1, result.entries.size)
        assertEquals(0, result.skippedCount)
    }

    @Test
    fun parseCsv_skips_malformedRows_and_countsThem() {
        val csv = "2026-08-05,10:00,Ok,1000,NEED\n" +
            "not,a,row\n" +
            "2026-08-05,11:00,BadType,1000,FOO\n" +
            "2026-08-05,99:99,BadTime,1000,NEED\n"
        val result = ImportUseCase.parseCsv(csv)
        assertEquals(1, result.entries.size)
        assertEquals(3, result.skippedCount)
    }

    @Test
    fun parseCsv_emptyOrAllInvalid_isEmpty() {
        assertTrue(ImportUseCase.parseCsv("").isEmpty)
        assertTrue(ImportUseCase.parseCsv("date,time,item,cost_cents,type\n").isEmpty)
        assertTrue(ImportUseCase.parseCsv("garbage\n").isEmpty)
    }

    @Test
    fun parseDateTimeUtc_rebuildsEpoch_utcInterpretation() {
        // 2026-08-05 10:00 UTC
        val utc = ImportUseCase.parseDateTimeUtc("2026-08-05", "10:00")
        assertEquals(1785924000000L, utc)
    }

    @Test
    fun parseDateTimeUtc_rejectsOutOfRange() {
        assertEquals(null, ImportUseCase.parseDateTimeUtc("1999-01-01", "10:00"))
        assertEquals(null, ImportUseCase.parseDateTimeUtc("2026-13-01", "10:00"))
        assertEquals(null, ImportUseCase.parseDateTimeUtc("2026-08-05", "24:00"))
        assertEquals(null, ImportUseCase.parseDateTimeUtc("2026-08-05", "10:60"))
    }

    @Test
    fun daysFromCivil_matchesEpochFloor_andLeapBoundary() {
        // 1970-01-01 is day 0; 2000-02-29 is a leap day after the epoch.
        assertEquals(0L, ImportUseCase.daysFromCivil(1970, 1, 1))
        assertEquals(11016L, ImportUseCase.daysFromCivil(2000, 2, 29))
    }

    @Test
    fun splitCsvLine_handlesQuotedCommaAndDoubledQuote() {
        assertEquals(
            listOf("2026-08-05", "10:00", "Coffee, large", "12000", "WANT"),
            ImportUseCase.splitCsvLine("2026-08-05,10:00,\"Coffee, large\",12000,WANT")
        )
        assertEquals(
            listOf("say \"hi\""),
            ImportUseCase.splitCsvLine("\"say \"\"hi\"\"\"")
        )
    }
}