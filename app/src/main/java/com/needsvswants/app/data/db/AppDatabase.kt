package com.needsvswants.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.needsvswants.app.data.model.DailyBudgetEntity
import com.needsvswants.app.data.model.Entry

/**
 * Schema changes MUST follow the migration policy in [Migrations.kt]:
 * bump the version, add a Migration to [ALL_MIGRATIONS], and commit the
 * exported schema JSON under `app/schemas/`. Never use destructive fallbacks.
 */
@Database(entities = [Entry::class, DailyBudgetEntity::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
    abstract fun dailyBudgetDao(): DailyBudgetDao
}
