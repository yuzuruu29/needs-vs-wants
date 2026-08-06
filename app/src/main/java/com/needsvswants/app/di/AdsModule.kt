package com.needsvswants.app.di

import com.needsvswants.app.ads.NoOpRewardedAdGateway
import com.needsvswants.app.ads.RewardedAdGateway
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Ads wiring — SDK STRIPPED from the build (D87, lean 1.5.0-sized APK).
 * The NoOp gateway is always bound; nothing ad-related can run or initialize.
 *
 * Re-enable when an AdMob account exists:
 * 1. Restore `ads/AdMobRewardedAdGateway.kt` + `ads/ConsentHelper.kt` from
 *    git commit `5622b7e` (or `8969364` for the QA-fixed versions).
 * 2. Uncomment the play-services-ads + UMP entries in
 *    `gradle/libs.versions.toml` and `app/build.gradle.kts`.
 * 3. Set `AdsConfig.ENABLED = true` and replace the test IDs
 *    (AndroidManifest.xml APPLICATION_ID + REWARDED_AD_UNIT_ID).
 * 4. Restore the conditional binding: `if (AdsConfig.ENABLED) adMob else noOp`.
 */
@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

    @Provides
    @Singleton
    fun provideRewardedAdGateway(noOp: NoOpRewardedAdGateway): RewardedAdGateway = noOp
}
