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
 * Test IDs are used until a real AdMob app exists (locked decision):
 * replace [REWARDED_AD_UNIT_ID] and the manifest APPLICATION_ID
 * meta-data with production values before release.
 */
@Singleton
class AdMobRewardedAdGateway @Inject constructor(
    @ApplicationContext private val appContext: Context
) : RewardedAdGateway {

    private val consentHelper = ConsentHelper(appContext)
    private val sdkInitialized = AtomicBoolean(false)
    private var rewardedAd: RewardedAd? = null
    private var loading = false

    override fun isReady(): Boolean = rewardedAd != null

    override fun reset() {
        rewardedAd = null
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
        consentHelper.gatherConsent(activity) { consentOk ->
            if (!consentOk) {
                loading = false
                onClosed(false, "Ad consent is not available right now. Please try again later.")
                return@gatherConsent
            }
            initializeSdk()
            RewardedAd.load(
                activity,
                REWARDED_AD_UNIT_ID,
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        loading = false
                        rewardedAd = ad
                        present(activity, ad, onUserEarnedReward, onClosed)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        loading = false
                        rewardedAd = null
                        onClosed(false, "The ad could not be loaded. Check your connection and try again.")
                    }
                }
            )
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

    private fun initializeSdk() {
        if (sdkInitialized.getAndSet(true)) return
        MobileAds.initialize(appContext) {}
    }

    companion object {
        /** Google test rewarded ad unit. Replace with the production unit before release. */
        const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
    }
}
