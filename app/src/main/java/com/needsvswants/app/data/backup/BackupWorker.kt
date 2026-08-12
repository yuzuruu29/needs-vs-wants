package com.needsvswants.app.data.backup

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.needsvswants.app.data.prefs.AppPreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

/** Daily auto-backup into the user's chosen SAF folder (no-op while disabled). */
@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val backupService: BackupService,
    private val preferences: AppPreferences
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        if (!preferences.autoBackupEnabled.first()) return Result.success()
        return when (backupService.backupNow()) {
            is BackupService.BackupResult.Success -> Result.success()
            // No folder chosen — nothing to retry until the user picks one.
            BackupService.BackupResult.NoFolder -> Result.success()
            is BackupService.BackupResult.Failed ->
                if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}

object BackupScheduler {
    private const val WORK_NAME = "nvw_auto_backup"

    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<BackupWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(6, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request)
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}
