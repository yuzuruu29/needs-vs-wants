package com.needsvswants.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.entitlement.EntitlementSync
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
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
    @Inject lateinit var entryRepository: EntryRepository
    @Inject lateinit var entitlementRepository: EntitlementRepository
    @Inject lateinit var entitlementSync: EntitlementSync
    @Inject lateinit var preferences: AppPreferences
    @Inject lateinit var workerFactory: HiltWorkerFactory

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        appScope.launch {
            // Best-effort remote entitlement refresh BEFORE the purge-cutoff
            // read, so a Pro user whose local snapshot is stale (e.g. right
            // after PayPal approval) is not purged as free. Bounded and
            // exception-safe; offline keeps the local snapshot.
            runCatching { entitlementSync.syncAtAppStart() }
            val cutoff = entitlementRepository.entitlement.first()
                .retentionCutoffAt(System.currentTimeMillis())
            if (cutoff != null) {
                entryRepository.purgeBefore(cutoff)
            }
            // Re-arm reminder if user had it enabled (e.g. after process death / reinstall of work).
            if (preferences.reminderEnabled.first()) {
                val hour = preferences.reminderHour.first()
                ReminderScheduler.schedule(this@NeedsVsWantsApp, hour)
            }
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
