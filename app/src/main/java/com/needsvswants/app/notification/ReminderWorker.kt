package com.needsvswants.app.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.needsvswants.app.MainActivity
import com.needsvswants.app.R
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.StreakMath
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val entryRepository: EntryRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val entries = entryRepository.observeAll().first()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val loggedToday = entries.any { it.date == today }

        if (loggedToday) {
            return Result.success()
        }

        val streak = StreakMath.currentStreak(entries.map { it.date }.distinct())
        val message = if (streak > 0) {
            "Day $streak streak — log today's expenses to keep it."
        } else {
            "What did you spend today? Need or Want?"
        }

        NotificationChannelSetup.setupChannel(context)

        val openLog = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(MainActivity.EXTRA_OPEN_TAB, MainActivity.TAB_LOG)
        }
        val pending = PendingIntent.getActivity(
            context,
            0,
            openLog,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NotificationChannelSetup.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Needs vs Wants")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentIntent(pending)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        return try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
            Result.success()
        } catch (_: SecurityException) {
            // Permission not granted on Android 13+ — keep work success so we don't thrash.
            Result.success()
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1001
    }
}
