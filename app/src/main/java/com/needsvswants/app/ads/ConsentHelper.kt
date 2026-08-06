package com.needsvswants.app.ads

import android.app.Activity
import android.content.Context
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

/**
 * Thin UMP (User Messaging Platform) wrapper.
 *
 * Locked product decision: consent is gathered ONLY on the first "Watch ad"
 * tap — never on cold start, never in the seal path. All UMP calls must run
 * on the main thread (the SDK enforces this internally).
 */
class ConsentHelper(context: Context) {

    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

    val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()

    /**
     * Gathers consent if not already decided. [onResult] receives true only
     * when ads may be requested afterwards. Error paths fall back to
     * canRequestAds() — the Google-sample "continue gracefully" behavior:
     * no crash, no blocking, and if consent is genuinely missing the app
     * simply never requests ads.
     */
    fun gatherConsent(activity: Activity, onResult: (Boolean) -> Unit) {
        if (canRequestAds) {
            onResult(true)
            return
        }
        val params = ConsentRequestParameters.Builder().build()
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    onResult(canRequestAds)
                }
            },
            { onResult(canRequestAds) }
        )
    }
}
