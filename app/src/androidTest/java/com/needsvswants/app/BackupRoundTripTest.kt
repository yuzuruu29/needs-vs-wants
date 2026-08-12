package com.needsvswants.app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.needsvswants.app.data.backup.BackupEntry
import com.needsvswants.app.data.backup.BackupEnvelope
import com.needsvswants.app.data.backup.BackupEnvelopeCodec
import com.needsvswants.app.data.db.AppDatabase
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * End-to-end backup round-trip through real SQLite: seed a Room DB, export
 * the envelope, restore into a second DB (with a duplicate already present),
 * and verify content + dedupe.
 */
@RunWith(AndroidJUnit4::class)
class BackupRoundTripTest {

    @Test
    fun envelope_roundTrips_throughRoom_withDedupe() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val source = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val target = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        try {
            val a = Entry(dateUtc = 1_000L, date = "2026-08-12", time = "07:30 PM", item = "Kape ₱ \"promo\"", costCents = 15_000L, type = EntryType.WANT)
            val b = Entry(dateUtc = 2_000L, date = "2026-08-13", time = "12:00 PM", item = "Rice 5kg", costCents = 120_000L, type = EntryType.NEED)
            source.entryDao().insert(a)
            source.entryDao().insert(b)

            val exported = source.entryDao().observeAll().first()
            val json = BackupEnvelopeCodec.toJson(
                BackupEnvelope(
                    schemaVersion = BackupEnvelopeCodec.SCHEMA_VERSION,
                    appVersionName = "test",
                    appVersionCode = 0,
                    exportedAtEpochMillis = 0L,
                    prefs = null,
                    entries = exported.map { BackupEntry.fromEntry(it) }
                )
            )

            // The target already holds a content-duplicate of `a`.
            target.entryDao().insert(a.copy(id = 0))

            val parsed = BackupEnvelopeCodec.fromJson(json)
            val existing = target.entryDao().observeAll().first()
            val fresh = BackupEnvelopeCodec.newEntriesOnly(parsed.entries, existing)
            fresh.forEach { target.entryDao().insert(it.toEntry()) }

            val restored = target.entryDao().observeAll().first()
            assertEquals(2, restored.size)
            assertEquals(
                setOf("Kape ₱ \"promo\"" to 15_000L, "Rice 5kg" to 120_000L),
                restored.map { it.item to it.costCents }.toSet()
            )
        } finally {
            source.close()
            target.close()
        }
    }
}
