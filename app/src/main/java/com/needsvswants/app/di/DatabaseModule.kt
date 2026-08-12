package com.needsvswants.app.di

import android.content.Context
import androidx.room.Room
import com.needsvswants.app.data.db.ALL_MIGRATIONS
import com.needsvswants.app.data.db.AppDatabase
import com.needsvswants.app.data.db.EntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "needs_vs_wants.db")
            // Migration policy lives in data/db/Migrations.kt — no destructive fallbacks.
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @Provides
    fun provideEntryDao(db: AppDatabase): EntryDao = db.entryDao()
}
