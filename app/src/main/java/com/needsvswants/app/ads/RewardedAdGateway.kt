package com.needsvswants.app.ads

import android.app.Activity

/**
 * Rewarded-ad gateway seam. The only ads surface the app talks to.
 *
 * Since D87 the AdMob/UMP SDK is STRIPPED from the build (lean APK): the
 * only bound implementation is [NoOpRewardedAdGateway]. To re-enable ads:
 * restore `AdMobRewardedAdGateway.kt` + `ConsentHelper.kt` from git commit
 * `5622b7e`, uncomment the ads deps in `gradle/libs.versions.toml` +
 * `app/build.gradle.kts`, set `AdsConfig.ENABLED = true` with production
 * IDs, and restore the conditional binding in `di/AdsModule.kt`.
 *
 * To strip monetization entirely (already done as of D87): drop the deps,
 * delete this package + the AdsModule binding, and remove the quota prefs
 * keys (quota_day / quota_logs_created / quota_bonus_logs /
 * quota_ads_watched) from AppPreferences.
 */
interface RewardedAdGateway {

    /** True when a rewarded ad is already loaded and showable. */
    fun isReady(): Boolean

    /**
     * Consent (first "Watch ad" tap only) → lazy SDK init → load → show.
     *
     * [onUserEarnedReward] is invoked ONLY from the official
     * onUserEarnedReward callback — the sole path that may grant bonus logs.
     *
     * [onClosed] is invoked exactly once when the flow ends:
     * - (earned = false, error != null): consent/load/show failed — no grant
     * - (earned = false, error = null): ad dismissed without reward — no grant
     * - (earned = true,  error = null): reward granted, ad dismissed
     */
    fun loadAndShow(
        activity: Activity,
        onUserEarnedReward: () -> Unit,
        onClosed: (earned: Boolean, error: String?) -> Unit
    )

    /** Drop any loaded ad (e.g. dialog dismissed). Safe to call anytime. */
    fun reset()
}
