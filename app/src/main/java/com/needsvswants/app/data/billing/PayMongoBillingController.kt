package com.needsvswants.app.data.billing

import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.remote.HttpJsonClient
import com.needsvswants.app.data.remote.SupabaseConfig
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

/** Matches [HttpJsonClient]'s non-2xx error shape: `HTTP <code>: <body>`. */
private val HTTP_FAILURE = Regex("""^HTTP (\d+): (.*)$""", RegexOption.DOT_MATCHES_ALL)

/**
 * Live PayMongo manual-renewal checkout for website / sideload distribution.
 *
 * Soft-launch default: the paywall opens a one-time PayMongo checkout (GCash /
 * card) instead of a PayPal auto-charging subscription. The user pays each month
 * when ready; the PayMongo webhook grants entitlement server-side and the app
 * refreshes via [restorePurchases] / [com.needsvswants.app.data.entitlement.EntitlementSync].
 *
 * Flow:
 * 1. Ensure Supabase session (caller signs in first)
 * 2. POST Edge Function `paymongo_create_checkout` with tier pro|max
 * 3. Return [BillingResult.OpenCheckout] so the UI opens the checkout URL
 * 4. PayMongo webhook grants entitlement; [restorePurchases] refreshes local state
 *
 * The PayMongo secret key lives server-side; the client never holds it.
 */
@Singleton
class PayMongoBillingController @Inject constructor(
    private val config: SupabaseConfig,
    private val auth: AuthRepository,
    private val entitlements: EntitlementRepository
) : BillingController {

    override val isPlayAvailable: Boolean = false

    override val isPayPalAvailable: Boolean
        get() = config.enabled

    override suspend fun startTrial(productId: String, period: BillingPeriod): BillingResult {
        // Trial is a product choice here; the same pro checkout path is used.
        return createCheckout(tier = "pro", period = period)
    }

    override suspend fun purchase(productId: String, period: BillingPeriod): BillingResult {
        // A blank product id must never fall through to a checkout: mapping it
        // to Max would silently bill on a misconfigured build.
        if (productId.isBlank()) return BillingResult.Failed("Checkout not configured on this build.")
        // productId is config.maxMonthlyProductId for Max, anything else is Pro.
        val hint = productId.ifBlank { config.maxMonthlyProductId }
        val tier = when {
            hint == config.maxMonthlyProductId -> "max"
            hint == config.proMonthlyProductId -> "pro"
            hint.contains("max", ignoreCase = true) -> "max"
            else -> "pro"
        }
        return createCheckout(tier = tier, period = period)
    }

    override suspend fun restorePurchases(): BillingResult {
        if (!config.enabled) return BillingResult.Unavailable
        val token = auth.ensureFreshAccessToken()
            ?: return BillingResult.Failed("Sign in required.")
        entitlements.refreshFromRemote(token)
        // UI reads tier from entitlement flows after refresh.
        return BillingResult.Success
    }

    private suspend fun createCheckout(tier: String, period: BillingPeriod): BillingResult {
        if (!config.enabled) return BillingResult.Unavailable

        val accessToken = auth.ensureFreshAccessToken()
            ?: return BillingResult.Failed("Sign in required.")

        val url = "${config.url.trimEnd('/')}/functions/v1/paymongo_create_checkout"
        val periodName = if (period == BillingPeriod.ANNUAL) "annual" else "monthly"
        val body = """{"tier":"${tier.escapeJson()}","period":"$periodName"}"""
        val result = HttpJsonClient.request(
            url = url,
            method = "POST",
            headers = mapOf(
                "apikey" to config.anonKey,
                "Authorization" to "Bearer $accessToken",
                "Content-Type" to "application/json"
            ),
            body = body
        )

        val json = result.getOrElse { throwable ->
            if (throwable is CancellationException) throw throwable
            return BillingResult.Failed(httpFailureReason(throwable))
        }
        val checkoutUrl = PayMongoCheckoutJson.parseCheckoutUrl(json)
            ?: return BillingResult.Failed("PayMongo checkout returned an unexpected response. Please try again.")
        return BillingResult.OpenCheckout(checkoutUrl)
    }

    /**
     * Turns an [HttpJsonClient] failure into a human-readable reason. Non-2xx
     * responses arrive as `HTTP <code>: <body>`; the body's `error`/`message`
     * JSON field (via [PayMongoCheckoutJson.parseErrorMessage]) becomes the reason,
     * falling back to the raw HTTP status when the body carries no message.
     */
    private fun httpFailureReason(t: Throwable): String {
        val match = t.message?.let { HTTP_FAILURE.matchEntire(it) }
            ?: return "PayMongo checkout failed. Please try again."
        val body = match.groupValues[2]
        val apiError = PayMongoCheckoutJson.parseErrorMessage(body)
        if (!apiError.isNullOrBlank()) return apiError
        return "PayMongo checkout failed (HTTP ${match.groupValues[1]}). Please try again."
    }

    private fun String.escapeJson(): String =
        replace("\\", "\\\\").replace("\"", "\\\"")
}