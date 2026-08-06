package com.needsvswants.app.domain

object AdsConfig {
    /**
     * Master kill switch.
     *
     * FALSE (2026-08-07, D85 + D87): AdMob is on hold — no AdMob account yet
     * and the app is not deployed to the Play Store. Since D87 the AdMob/UMP
     * SDK is STRIPPED from the build (lean 1.5.0-sized APK): the NoOp gateway
     * is bound, the "Watch Ad" button is hidden, the Settings panel is hidden,
     * and there is no SDK to initialize (no network, no test ads).
     * To re-enable: restore ads/AdMobRewardedAdGateway.kt + ConsentHelper.kt
     * from git commit 5622b7e, uncomment the ads deps in libs.versions.toml +
     * app/build.gradle.kts, set this to TRUE, and replace the test IDs
     * (AndroidManifest.xml APPLICATION_ID + REWARDED_AD_UNIT_ID).
     */
    const val ENABLED = false

    const val FREE_DAILY_LOGS = 10
    const val EXTRA_LOGS_PER_REWARD = 8
    const val MAX_REWARDED_ADS_PER_DAY = 3
}
