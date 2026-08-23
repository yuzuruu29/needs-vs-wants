package com.needsvswants.app.di

import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.repository.DailyBudgetRepository
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.DailyBudgetUseCase
import com.needsvswants.app.domain.SummaryUseCase
import com.needsvswants.app.domain.ReceiptOcrEngine
import com.needsvswants.app.domain.ReceiptOcrProcessor
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
    fun provideReceiptOcrProcessor(engine: ReceiptOcrEngine): ReceiptOcrProcessor = engine

    @Provides
    @Singleton
    fun provideSummaryUseCase(
        entries: EntryRepository,
        entitlementRepository: EntitlementRepository
    ): SummaryUseCase {
        return SummaryUseCase(entries, entitlementRepository)
    }

    @Provides
    @Singleton
    fun provideDailyBudgetUseCase(
        entries: EntryRepository,
        budgets: DailyBudgetRepository
    ): DailyBudgetUseCase = DailyBudgetUseCase(entries, budgets)
}
