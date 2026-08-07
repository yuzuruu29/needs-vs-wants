package com.needsvswants.app.data.billing

import com.needsvswants.app.data.remote.SupabaseConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Google Play Billing controller.
 *
 * **Without the BillingClient AAR** (dependency intentionally commented in
 * `libs.versions.toml` for offline builds) every action returns
 * [BillingResult.Unavailable]. Product IDs and the connection state machine
 * are ready so enabling the dependency is mostly uncomment + Activity host.
 *
 * When network allows:
 * 1. Uncomment `androidx-billing` in `gradle/libs.versions.toml`
 * 2. Uncomment `implementation(libs.androidx.billing)` in `app/build.gradle.kts`
 * 3. Replace the body of [connectIfNeeded] / launch helpers with BillingClient APIs
 */
@Singleton
class PlayBillingController @Inject constructor(
    private val config: SupabaseConfig
) : BillingController {

    /** Set true only after a real BillingClient reaches BILLING_RESPONSE_CODE_OK. */
    @Volatile
    private var clientConnected: Boolean = false

    /** Product catalog from BuildConfig / [SupabaseConfig] placeholders. */
    val trialProductId: String
        get() = config.proTrialProductId.ifBlank { DEFAULT_TRIAL_ID }

    val monthlyProductId: String
        get() = config.proMonthlyProductId.ifBlank { DEFAULT_MONTHLY_ID }

    override val isPlayAvailable: Boolean
        get() = clientConnected && isBillingLibraryOnClasspath()

    override suspend fun startTrial(productId: String): BillingResult {
        connectIfNeeded()
        if (!isPlayAvailable) return BillingResult.Unavailable
        return launchPurchase(productId.ifBlank { trialProductId })
    }

    override suspend fun purchase(productId: String): BillingResult {
        connectIfNeeded()
        if (!isPlayAvailable) return BillingResult.Unavailable
        return launchPurchase(productId.ifBlank { monthlyProductId })
    }

    override suspend fun restorePurchases(): BillingResult {
        connectIfNeeded()
        if (!isPlayAvailable) return BillingResult.Unavailable
        // TODO(Task 3 live): queryPurchasesAsync + acknowledge + sync EntitlementRepository
        return BillingResult.Unavailable
    }

    /**
     * Attempts to start a BillingClient connection. No-op while the Play Billing
     * library is not on the classpath (offline stub mode).
     */
    private fun connectIfNeeded() {
        if (clientConnected) return
        if (!isBillingLibraryOnClasspath()) {
            clientConnected = false
            return
        }
        // Live path (requires billing AAR + Application context):
        // billingClient = BillingClient.newBuilder(context)
        //     .setListener { purchases, result -> ... }
        //     .enablePendingPurchases()
        //     .build()
        // billingClient.startConnection(...)
        clientConnected = false
    }

    private fun launchPurchase(productId: String): BillingResult {
        // Live path: queryProductDetails + BillingFlowParams + launchBillingFlow(activity)
        // Product id is validated here so UI product placeholders never NPE.
        if (productId.isBlank()) return BillingResult.Failed()
        return BillingResult.Unavailable
    }

    companion object {
        const val DEFAULT_TRIAL_ID = "pro_trial_3day"
        const val DEFAULT_MONTHLY_ID = "pro_monthly"
        const val DEFAULT_MAX_MONTHLY_ID = "max_monthly"

        /**
         * Reflective probe so we never hard-depend on BillingClient at compile
         * time while the artifact stays optional.
         */
        fun isBillingLibraryOnClasspath(): Boolean =
            runCatching {
                Class.forName("com.android.billingclient.api.BillingClient")
                true
            }.getOrDefault(false)
    }
}
