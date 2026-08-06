package com.needsvswants.app.domain

object AdsConfig {
    /**
     * Master kill switch.
     *
     * FALSE (2026-08-07, D85): AdMob is on hold — no AdMob account yet and
     * the app is not deployed to the Play Store. The NoOp gateway is bound,
     * the "Watch Ad" button is hidden, the Settings panel is hidden, and
     * MobileAds/UMP are never initialized (no network, no test ads).
     * Flip to TRUE when the AdMob account + production App ID/unit exist
     * (and replace the test IDs in AndroidManifest.xml and
     * AdMobRewardedAdGateway.REWARDED_AD_UNIT_ID).
     */
    const val ENABLED = false

    const val FREE_DAILY_LOGS = 10
    const val EXTRA_LOGS_PER_REWARD = 8
    const val MAX_REWARDED_ADS_PER_DAY = 3
}
