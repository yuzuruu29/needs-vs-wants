package com.needsvswants.app.domain

object AdsConfig {
    /**
     * Master kill switch. TRUE (2026-08-09, D119): rewarded AdMob is live for
     * Free users only — the AdMob/UMP SDK is bundled, the real gateway is bound,
     * and the "Watch Ad" button + Settings panel are visible. Pro / Max stay
     * ad-less (the caller enforces that via `Entitlement.hasProAccessAt` before
     * the quota gate ever runs).
     *
     * Ad IDs are still Google TEST values (manifest APPLICATION_ID +
     * AdMobRewardedAdGateway.REWARDED_AD_UNIT_ID). Swap to production IDs when
     * the AdMob account exists — same two places as the D85 checklist.
     */
    const val ENABLED = true

    /** Free seals per local calendar day. Changed 10 → 5 (D119). */
    const val FREE_DAILY_LOGS = 5

    /** Bonus logs granted per rewarded ad. */
    const val EXTRA_LOGS_PER_REWARD = 8

    /** Max rewarded ads a Free user can watch per day (~+24 bonus). */
    const val MAX_REWARDED_ADS_PER_DAY = 3
}
