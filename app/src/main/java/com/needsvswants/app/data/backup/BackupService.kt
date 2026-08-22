package com.needsvswants.app.data.backup

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.needsvswants.app.BuildConfig
import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.notification.ReminderScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SAF-backed local backup + restore (audit gap: Pro's "lifetime history" had
 * no backup story). The user picks a folder once (persistable tree URI — a
 * Google Drive folder works too); backups are plain JSON envelopes written by
 * [BackupEnvelopeCodec]. Keeps the newest [KEEP_BACKUPS] files, prunes older.
 *
 * Reads go through the raw [EntryDao], NOT the retention-bounded
 * EntryRepository: backups are tier-blind. Exporting only the tier-visible
 * window would silently drop hidden-but-stored rows from a Free/stale user's
 * backup, and restore dedupe must see every stored row or hidden entries get
 * re-inserted as duplicates.
 */
@Singleton
class BackupService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: EntryDao,
    private val preferences: AppPreferences
) {
    sealed class BackupResult {
        data class Success(val fileName: String, val entryCount: Int) : BackupResult()
        object NoFolder : BackupResult()
        data class Failed(val reason: String) : BackupResult()
    }

    sealed class RestoreResult {
        data class Success(val imported: Int, val duplicatesSkipped: Int) : RestoreResult()
        data class Failed(val reason: String) : RestoreResult()
    }

    suspend fun backupNow(): BackupResult = withContext(Dispatchers.IO) {
        val treeUri = preferences.backupFolderUri.first() ?: return@withContext BackupResult.NoFolder
        try {
            val dir = DocumentFile.fromTreeUri(context, Uri.parse(treeUri))
                ?: return@withContext BackupResult.Failed("Backup folder is unavailable")
            if (!dir.canWrite()) {
                return@withContext BackupResult.Failed("Backup folder is not writable — pick it again")
            }
            val entries = dao.observeAll().first()
            val envelope = BackupEnvelope(
                schemaVersion = BackupEnvelopeCodec.SCHEMA_VERSION,
                appVersionName = BuildConfig.VERSION_NAME,
                appVersionCode = BuildConfig.VERSION_CODE,
                exportedAtEpochMillis = System.currentTimeMillis(),
                prefs = BackupPrefs(
                    currencySymbol = preferences.currencySymbol.first(),
                    currencyCode = preferences.currencyCode.first(),
                    dailyBudgetCents = preferences.dailyBudgetCents.first(),
                    reminderEnabled = preferences.reminderEnabled.first(),
                    reminderHour = preferences.reminderHour.first(),
                    spendingGoal = preferences.spendingGoal.first()
                ),
                entries = entries.map { BackupEntry.fromEntry(it) }
            )
            val json = BackupEnvelopeCodec.toJson(envelope)
            val name = "$FILE_PREFIX${FILE_STAMP.format(Date())}.json"
            val file = dir.createFile("application/json", name)
                ?: return@withContext BackupResult.Failed("Could not create the backup file")
            context.contentResolver.openOutputStream(file.uri)?.use { out ->
                out.write(json.toByteArray(Charsets.UTF_8))
            } ?: return@withContext BackupResult.Failed("Could not write the backup file")
            pruneOldBackups(dir)
            preferences.setLastBackupAt(System.currentTimeMillis())
            BackupResult.Success(file.name ?: name, envelope.entries.size)
        } catch (t: Throwable) {
            BackupResult.Failed(t.message ?: "Backup failed")
        }
    }

    suspend fun restoreFrom(fileUri: Uri): RestoreResult = withContext(Dispatchers.IO) {
        try {
            val text = context.contentResolver.openInputStream(fileUri)?.use { input ->
                val bytes = input.readBytes()
                if (bytes.size > MAX_RESTORE_BYTES) {
                    return@withContext RestoreResult.Failed("File is too large to be a backup")
                }
                String(bytes, Charsets.UTF_8)
            } ?: return@withContext RestoreResult.Failed("Could not read the selected file")

            val envelope = try {
                BackupEnvelopeCodec.fromJson(text)
            } catch (e: IllegalArgumentException) {
                return@withContext RestoreResult.Failed(e.message ?: "Invalid backup file")
            }

            val existing = dao.observeAll().first()
            val fresh = BackupEnvelopeCodec.newEntriesOnly(envelope.entries, existing)
            fresh.forEach { dao.insert(it.toEntry()) }

            envelope.prefs?.let { p ->
                preferences.setCurrency(p.currencySymbol, p.currencyCode)
                p.dailyBudgetCents?.let { preferences.setDailyBudgetCents(it) }
                preferences.setSpendingGoal(p.spendingGoal)
                preferences.setReminderHour(p.reminderHour)
                preferences.setReminderEnabled(p.reminderEnabled)
                if (p.reminderEnabled) {
                    ReminderScheduler.schedule(context, p.reminderHour)
                }
            }
            RestoreResult.Success(
                imported = fresh.size,
                duplicatesSkipped = envelope.entries.size - fresh.size
            )
        } catch (t: Throwable) {
            RestoreResult.Failed(t.message ?: "Restore failed")
        }
    }

    private fun pruneOldBackups(dir: DocumentFile) {
        val backups = dir.listFiles()
            .filter { (it.name ?: "").startsWith(FILE_PREFIX) && (it.name ?: "").endsWith(".json") }
            .sortedByDescending { it.name }
        backups.drop(KEEP_BACKUPS).forEach { runCatching { it.delete() } }
    }

    private companion object {
        const val FILE_PREFIX = "needs-vs-wants-backup-"
        const val KEEP_BACKUPS = 10
        const val MAX_RESTORE_BYTES = 32 * 1024 * 1024
        val FILE_STAMP = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US)
    }
}
