package com.needsvswants.app.data.backup

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupEnvelopeCodecTest {

    private fun sampleEnvelope() = BackupEnvelope(
        schemaVersion = BackupEnvelopeCodec.SCHEMA_VERSION,
        appVersionName = "2.0.14",
        appVersionCode = 22,
        exportedAtEpochMillis = 1_765_584_000_000L,
        prefs = BackupPrefs(
            currencySymbol = "₱",
            currencyCode = "PHP",
            dailyBudgetCents = 50_000L,
            reminderEnabled = true,
            reminderHour = 20,
            spendingGoal = "budget"
        ),
        entries = listOf(
            BackupEntry(1_765_500_000_000L, "2026-08-12", "07:30 PM", "Kape \"promo\"", 15_000L, EntryType.WANT),
            BackupEntry(1_765_400_000_000L, "2026-08-11", "12:00 PM", "Groceries", 120_000L, EntryType.NEED)
        ),
        dailyBudgets = listOf(
            BackupDailyBudget("2026-08-12", 50_000L),
            BackupDailyBudget("2026-08-11", 75_000L)
        )
    )

    @Test
    fun `round-trip preserves everything`() {
        val original = sampleEnvelope()
        val restored = BackupEnvelopeCodec.fromJson(BackupEnvelopeCodec.toJson(original))
        assertEquals(original, restored)
    }

    @Test
    fun `round-trip without prefs and with empty entries`() {
        val original = sampleEnvelope().copy(prefs = null, entries = emptyList())
        val restored = BackupEnvelopeCodec.fromJson(BackupEnvelopeCodec.toJson(original))
        assertNull(restored.prefs)
        assertTrue(restored.entries.isEmpty())
    }

    @Test
    fun `budget off round-trips as null`() {
        val original = sampleEnvelope().copy(prefs = sampleEnvelope().prefs!!.copy(dailyBudgetCents = null))
        val restored = BackupEnvelopeCodec.fromJson(BackupEnvelopeCodec.toJson(original))
        assertNull(restored.prefs!!.dailyBudgetCents)
    }

    @Test
    fun `rejects non-backup json`() {
        val error = runCatching { BackupEnvelopeCodec.fromJson("""{"hello":"world"}""") }.exceptionOrNull()
        assertTrue(error is IllegalArgumentException)
        assertEquals("Not a Needs vs Wants backup file", error!!.message)
    }

    @Test
    fun `rejects newer schema version with upgrade message`() {
        val json = BackupEnvelopeCodec.toJson(sampleEnvelope())
            .replace("\"schemaVersion\":${BackupEnvelopeCodec.SCHEMA_VERSION}", "\"schemaVersion\":99")
        val error = runCatching { BackupEnvelopeCodec.fromJson(json) }.exceptionOrNull()
        assertTrue(error is IllegalArgumentException)
        assertTrue(error!!.message!!.contains("newer app version"))
    }

    @Test
    fun `rejects entry with unknown type`() {
        val json = BackupEnvelopeCodec.toJson(sampleEnvelope()).replace("\"WANT\"", "\"SPLURGE\"")
        val error = runCatching { BackupEnvelopeCodec.fromJson(json) }.exceptionOrNull()
        assertTrue(error is IllegalArgumentException)
        assertTrue(error!!.message!!.contains("unknown type"))
    }

    @Test
    fun `rejects entry missing a required field`() {
        val json = """{"format":"nvw-backup","schemaVersion":1,"appVersionName":"x",
            "appVersionCode":1,"exportedAtEpochMillis":0,
            "entries":[{"date":"2026-08-12","time":"1 PM","item":"x","costCents":1,"type":"NEED"}]}"""
        val error = runCatching { BackupEnvelopeCodec.fromJson(json) }.exceptionOrNull()
        assertTrue(error is IllegalArgumentException)
        assertTrue(error!!.message!!.contains("dateUtc"))
    }

    @Test
    fun `v1 backup without daily budget list remains readable`() {
        val json = """
            {"format":"nvw-backup","schemaVersion":1,"appVersionName":"old",
             "appVersionCode":1,"exportedAtEpochMillis":0,
             "prefs":{"currencySymbol":"₱","currencyCode":"PHP",
             "dailyBudgetCents":42000,"reminderEnabled":false,"reminderHour":20,
             "spendingGoal":"track"},"entries":[]}
        """.trimIndent()
        val restored = BackupEnvelopeCodec.fromJson(json)
        assertEquals(42_000L, restored.prefs!!.dailyBudgetCents)
        assertTrue(restored.dailyBudgets.isEmpty())
    }

    @Test
    fun `newEntriesOnly skips content duplicates from the db and inside the file`() {
        val existing = listOf(
            Entry(id = 7, dateUtc = 100L, date = "d", time = "t", item = "Kape", costCents = 5L, type = EntryType.WANT)
        )
        val imported = listOf(
            BackupEntry(100L, "d", "t", "Kape", 5L, EntryType.WANT),        // dup of db row (id ignored)
            BackupEntry(200L, "d", "t", "Rice", 9L, EntryType.NEED),        // new
            BackupEntry(200L, "d2", "t2", "Rice", 9L, EntryType.NEED),      // dup content of previous (date fields differ but key fields match)
            BackupEntry(300L, "d", "t", "Rice", 9L, EntryType.NEED)         // new (different dateUtc)
        )
        val fresh = BackupEnvelopeCodec.newEntriesOnly(imported, existing)
        assertEquals(listOf(200L, 300L), fresh.map { it.dateUtc })
    }
}
