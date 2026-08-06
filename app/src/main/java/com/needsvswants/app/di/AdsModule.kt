package com.needsvswants.app.di

import com.needsvswants.app.ads.AdMobRewardedAdGateway
import com.needsvswants.app.ads.NoOpRewardedAdGateway
import com.needsvswants.app.ads.RewardedAdGateway
import com.needsvswants.app.domain.AdsConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Ads wiring. The master kill switch (AdsConfig.ENABLED) picks the real
 * AdMob gateway or the NoOp one — flipping one constant disables
 * monetization without touching call sites.
 */
@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

    @Provides
    @Singleton
    fun provideRewardedAdGateway(
        adMob: AdMobRewardedAdGateway,
        noOp: NoOpRewardedAdGateway
    ): RewardedAdGateway = if (AdsConfig.ENABLED) adMob else noOp
}
