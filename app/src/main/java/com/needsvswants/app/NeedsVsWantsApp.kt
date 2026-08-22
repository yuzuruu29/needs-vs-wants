package com.needsvswants.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.needsvswants.app.data.backup.BackupScheduler
import com.needsvswants.app.data.entitlement.EntitlementSync
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.update.UpdateChecker
import com.needsvswants.app.diagnostics.CrashReporting
import com.needsvswants.app.notification.ReminderScheduler
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class NeedsVsWantsApp : Application(), Configuration.Provider {
    @Inject lateinit var entitlementSync: EntitlementSync
    @Inject lateinit var preferences: AppPreferences
    @Inject lateinit var updateChecker: UpdateChecker
    @Inject lateinit var workerFactory: HiltWorkerFactory

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        appScope.launch {
            // Crash reporting first so early startup crashes are captured.
            // No-op in debug builds / blank DSN / user opt-out.
            runCatching {
                CrashReporting.applyState(
                    this@NeedsVsWantsApp,
                    preferences.crashReportsEnabled.first()
                )
            }
            // Best-effort remote entitlement refresh at cold start so a Pro
            // user whose local snapshot went stale (offline > 7 days, backend
            // outage) regains paid access as soon as a connection exists.
            // Bounded and exception-safe; offline keeps the local snapshot.
            runCatching { entitlementSync.syncAtAppStart() }
            // NOTE: startup must NEVER delete entries based on entitlement
            // state. The old purgeBefore(cutoff) call here physically deleted
            // lifetime history when a stale paid snapshot degraded to Free.
            // Retention is now a visibility boundary inside EntryRepository:
            // Free-tier reads return only the last 30 days while every older
            // row stays stored and reappears once Pro access is verified.

            // Re-arm reminder if user had it enabled (e.g. after process death / reinstall of work).
            if (preferences.reminderEnabled.first()) {
                val hour = preferences.reminderHour.first()
                ReminderScheduler.schedule(this@NeedsVsWantsApp, hour)
            }
            // Re-arm daily auto-backup the same way.
            if (preferences.autoBackupEnabled.first()) {
                BackupScheduler.schedule(this@NeedsVsWantsApp)
            }
            // Throttled sideload update check (once a day; silent offline).
            runCatching { updateChecker.checkOnce() }
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
