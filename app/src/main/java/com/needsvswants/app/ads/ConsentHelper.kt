package com.needsvswants.app.ads

import android.app.Activity
import android.content.Context
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.needsvswants.app.BuildConfig

/**
 * Thin UMP (User Messaging Platform) wrapper.
 *
 * Locked product decision: consent is gathered ONLY on the first "Watch ad"
 * tap — never on cold start, never in the seal path. All UMP calls must run
 * on the main thread (the SDK enforces this internally).
 *
 * Debug builds (BuildConfig.DEBUG) force the EEA consent form and register a
 * known test device, so QA can exercise the consent + ad path on any machine
 * regardless of physical geography. Release builds use real geography.
 */
class ConsentHelper(private val context: Context) {

    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

    val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()

    /**
     * Debug-only consent parameters. Force EEA so the consent form always
     * appears (default geography suppresses it on non-EEA devices), and mark
     * this device as a test device so it is never treated as a real user.
     * [testDeviceHashedId] is the desktop/emulator's Advertising ID hash; the
     * value here is the standard Google UMP sample device. Replace with your
     * QA device's hash (see UMP docs) when needed.
     */
    private fun debugParameters(): ConsentRequestParameters {
        val settings = ConsentDebugSettings.Builder(context)
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .addTestDeviceHashedId(TEST_DEVICE_HASHED_ID)
            .build()
        return ConsentRequestParameters.Builder()
            .setConsentDebugSettings(settings)
            .build()
    }

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
        val params = if (BuildConfig.DEBUG) debugParameters() else ConsentRequestParameters.Builder().build()
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

    companion object {
        /**
         * Standard Google UMP sample test device (Advertising ID hashed). In
         * debug builds this device is exempted from real-user consent; swap in
         * your QA device's hash when you need that specific machine.
         */
        private const val TEST_DEVICE_HASHED_ID = "TEST-DEVICE-HASHED-ID"
    }
}
