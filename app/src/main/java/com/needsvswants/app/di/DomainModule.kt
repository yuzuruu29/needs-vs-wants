package com.needsvswants.app.di

import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.domain.DailyBudgetUseCase
import com.needsvswants.app.domain.SummaryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideSummaryUseCase(dao: EntryDao): SummaryUseCase {
        return SummaryUseCase(dao)
    }

    @Provides
    @Singleton
    fun provideDailyBudgetUseCase(
        dao: EntryDao,
        preferences: AppPreferences
    ): DailyBudgetUseCase = DailyBudgetUseCase(dao, preferences)
}
