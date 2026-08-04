package com.needsvswants.app

import android.app.Application
import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.entitlement.EntitlementRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class NeedsVsWantsApp : Application() {
    @Inject lateinit var entryDao: EntryDao
    @Inject lateinit var entitlementRepository: EntitlementRepository

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val cutoff = entitlementRepository.entitlement.first()
                .retentionCutoffAt(System.currentTimeMillis())
            if (cutoff != null) {
                entryDao.purgeBefore(cutoff)
            }
        }
    }
}
