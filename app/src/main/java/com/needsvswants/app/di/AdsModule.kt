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
 * Ads wiring — rewarded AdMob live for Free users (D119).
 *
 * Binds the real [AdMobRewardedAdGateway] when [AdsConfig.ENABLED] is true,
 * otherwise the [NoOpRewardedAdGateway] (kill switch). The "Watch Ad" button
 * and Settings panel additionally gate on `AdsConfig.ENABLED`, so a disabled
 * config means no SDK init, no network, no test ads — the lean build.
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
