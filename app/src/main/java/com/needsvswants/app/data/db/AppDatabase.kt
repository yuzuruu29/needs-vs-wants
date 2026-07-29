package com.needsvswants.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.needsvswants.app.data.model.Entry

@Database(entities = [Entry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
}
