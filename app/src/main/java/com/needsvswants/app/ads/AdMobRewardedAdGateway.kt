package com.needsvswants.app.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.needsvswants.app.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Real AdMob rewarded-ad gateway.
 *
 * - SDK init is lazy: it happens only after the user taps "Watch ad"
 *   (never on app start, never inside the seal path).
 * - UMP consent runs on the first tap only (see [ConsentHelper]).
 * - Bonus logs are granted ONLY in onUserEarnedReward.
 * - Failures are reported as friendly strings; nothing is granted.
 *
 * The rewarded unit id comes from BuildConfig (local.properties override);
 * the default is Google's TEST unit.
 */
@Singleton
class AdMobRewardedAdGateway @Inject constructor(
    @ApplicationContext private val appContext: Context
) : RewardedAdGateway {

    private val consentHelper = ConsentHelper(appContext)
    private val sdkInitialized = AtomicBoolean(false)
    private var rewardedAd: RewardedAd? = null
    private var loading = false

    /** Bumped by [reset] to invalidate any in-flight consent/load flow. */
    private var loadGeneration = 0

    override fun isReady(): Boolean = rewardedAd != null

    override fun reset() {
        loadGeneration++
        rewardedAd = null
        loading = false
    }

    override fun loadAndShow(
        activity: Activity,
        onUserEarnedReward: () -> Unit,
        onClosed: (earned: Boolean, error: String?) -> Unit
    ) {
        if (loading) return
        rewardedAd?.let { ad ->
            present(activity, ad, onUserEarnedReward, onClosed)
            return
        }
        loading = true
        val gen = loadGeneration
        consentHelper.gatherConsent(activity) { consentOk ->
            if (gen != loadGeneration) return@gatherConsent // cancelled while gathering
            if (!consentOk) {
                loading = false
                onClosed(false, "Ad consent is not available right now. Please try again later.")
                return@gatherConsent
            }
            initializeSdk {
                if (gen != loadGeneration) return@initializeSdk // cancelled during init
                activity.runOnUiThread {
                    RewardedAd.load(
                        activity,
                        BuildConfig.ADMOB_REWARDED_UNIT_ID,
                        AdRequest.Builder().build(),
                        object : RewardedAdLoadCallback() {
                            override fun onAdLoaded(ad: RewardedAd) {
                                // Cancelled generations must not touch state or show.
                                if (gen != loadGeneration) return
                                loading = false
                                rewardedAd = ad
                                present(activity, ad, onUserEarnedReward, onClosed)
                            }

                            override fun onAdFailedToLoad(error: LoadAdError) {
                                // Cancelled generations must not touch state or report.
                                if (gen != loadGeneration) return
                                loading = false
                                rewardedAd = null
                                onClosed(false, "The ad could not be loaded. Check your connection and try again.")
                            }
                        }
                    )
                }
            }
        }
    }

    private fun present(
        activity: Activity,
        ad: RewardedAd,
        onUserEarnedReward: () -> Unit,
        onClosed: (earned: Boolean, error: String?) -> Unit
    ) {
        var earned = false
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                onClosed(earned, null)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                rewardedAd = null
                onClosed(false, "The ad could not be shown right now.")
            }
        }
        ad.show(activity) {
            earned = true
            onUserEarnedReward()
        }
    }

    /**
     * Initializes the SDK once (lazy), then runs [onReady]. Ad loads must be
     * requested on the main thread — callers wrap with activity.runOnUiThread.
     */
    private fun initializeSdk(onReady: () -> Unit) {
        if (sdkInitialized.getAndSet(true)) {
            onReady()
            return
        }
        MobileAds.initialize(appContext) { onReady() }
    }
}
