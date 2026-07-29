package com.needsvswants.app

import android.app.Application
import com.needsvswants.app.data.db.EntryDao
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltAndroidApp
class NeedsVsWantsApp : Application() {
    @Inject lateinit var entryDao: EntryDao

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val cutoff = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -35)
            }.timeInMillis
            entryDao.purgeBefore(cutoff)
        }
    }
}
