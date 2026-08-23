package com.needsvswants.app.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Room migration registry — the app's schema-change policy (audit gap fix).
 *
 * Policy (do not deviate):
 *  1. Any change to an @Entity or the database shape bumps [AppDatabase]'s
 *     `version` by exactly 1.
 *  2. Every bump ships a hand-written [Migration] added to [ALL_MIGRATIONS]
 *     in the same change. Destructive fallbacks
 *     (`fallbackToDestructiveMigration*`) are FORBIDDEN — user ledgers must
 *     survive every upgrade.
 *  3. `exportSchema = true` keeps a JSON snapshot per version under
 *     `app/schemas/` (committed). Never edit or delete old snapshots; they are
 *     the source for migration tests.
 *  4. New migrations get a round-trip test (insert on version N, migrate,
 *     read on N+1) before release.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `daily_budgets` " +
                "(`dayKey` TEXT NOT NULL, `budgetCents` INTEGER NOT NULL, " +
                "PRIMARY KEY(`dayKey`))"
        )
    }
}

val ALL_MIGRATIONS: Array<Migration> = arrayOf(MIGRATION_1_2)
