package com.needsvswants.app.ads

import android.app.Activity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * No-op gateway bound when the master kill switch is off
 * (AdsConfig.ENABLED = false): no SDK init, no network, no ads offered.
 * The UI additionally hides the "Watch Ad" button when the switch is off.
 */
@Singleton
class NoOpRewardedAdGateway @Inject constructor() : RewardedAdGateway {

    override fun isReady(): Boolean = false

    override fun loadAndShow(
        activity: Activity,
        onUserEarnedReward: () -> Unit,
        onClosed: (earned: Boolean, error: String?) -> Unit
    ) {
        onClosed(false, "Ads are not available in this build.")
    }

    override fun reset() = Unit
}
