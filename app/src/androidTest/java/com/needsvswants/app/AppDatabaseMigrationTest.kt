package com.needsvswants.app

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.needsvswants.app.data.db.AppDatabase
import com.needsvswants.app.data.db.MIGRATION_1_2
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {
    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrate1To2_preservesEntries_andCreatesDailyBudgetTable() {
        val name = "migration-1-2"
        helper.createDatabase(name, 1).apply {
            execSQL(
                "INSERT INTO entries " +
                    "(dateUtc, date, time, item, costCents, type) " +
                    "VALUES (1000, '2026-08-23', '12:00', 'Rice', 12000, 'NEED')"
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate(name, 2, true, MIGRATION_1_2)
        migrated.query("SELECT COUNT(*) FROM entries").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(1, cursor.getInt(0))
        }
        migrated.query("SELECT COUNT(*) FROM daily_budgets").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(0, cursor.getInt(0))
        }
        migrated.close()
        InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase(name)
    }
}
