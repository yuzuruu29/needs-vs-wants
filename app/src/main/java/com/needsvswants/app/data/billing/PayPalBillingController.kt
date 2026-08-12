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
 * Live PayPal subscription checkout for website / sideload distribution.
 *
 * Flow:
 * 1. Ensure Supabase session (caller signs in first)
 * 2. POST Edge Function `paypal_create_subscription` with tier pro|max
 * 3. Return [BillingResult.OpenCheckout] so the UI opens the approval URL
 * 4. PayPal webhook grants entitlement; [restorePurchases] refreshes local state
 *
 * Plan ids live in BuildConfig / local.properties (`PRO_MONTHLY_PRODUCT_ID`,
 * `PRO_MAX_MONTHLY_PRODUCT_ID`) and must match Supabase secrets
 * `PAYPAL_PLAN_PRO` / `PAYPAL_PLAN_MAX`.
 */
@Singleton
class PayPalBillingController @Inject constructor(
    private val config: SupabaseConfig,
    private val auth: AuthRepository,
    private val entitlements: EntitlementRepository
) : BillingController {

    override val isPlayAvailable: Boolean = false

    override val isPayPalAvailable: Boolean
        get() = config.enabled &&
            (config.proMonthlyProductId.isNotBlank() || config.maxMonthlyProductId.isNotBlank())

    override suspend fun startTrial(productId: String, period: BillingPeriod): BillingResult {
        // Pro trial is configured on the PayPal plan; same create path as Pro.
        return createSubscription(
            tier = "pro",
            planIdHint = productId.ifBlank { planIdFor("pro", period) },
            period = period
        )
    }

    override suspend fun purchase(productId: String, period: BillingPeriod): BillingResult {
        val hint = productId.ifBlank { planIdFor("max", period) }
        val tier = when {
            hint == config.maxMonthlyProductId || hint == config.maxAnnualProductId -> "max"
            hint == config.proMonthlyProductId || hint == config.proAnnualProductId -> "pro"
            hint.contains("max", ignoreCase = true) -> "max"
            else -> "max" // paywall upgrade() is Max
        }
        return createSubscription(tier = tier, planIdHint = hint, period = period)
    }

    override suspend fun restorePurchases(): BillingResult {
        if (!config.enabled) return BillingResult.Unavailable
        val token = auth.ensureFreshAccessToken()
            ?: return BillingResult.Failed("Sign in required.")
        entitlements.refreshFromRemote(token)
        // UI reads tier from entitlement flows after refresh.
        return BillingResult.Success
    }

    private suspend fun createSubscription(
        tier: String,
        planIdHint: String,
        period: BillingPeriod
    ): BillingResult {
        if (!config.enabled) return BillingResult.Unavailable
        val planPro = planIdFor("pro", period)
        val planMax = planIdFor("max", period)
        val planId = when (tier) {
            "max" -> planMax.ifBlank { planIdHint }
            else -> planPro.ifBlank { planIdHint }
        }
        if (planId.isBlank() || !planId.startsWith("P-")) {
            return BillingResult.Failed(
                if (period == BillingPeriod.ANNUAL) {
                    "Annual PayPal plan not configured on this build."
                } else {
                    "PayPal plans not configured on this build."
                }
            )
        }

        val accessToken = auth.ensureFreshAccessToken()
            ?: return BillingResult.Failed("Sign in required.")

        val url = "${config.url.trimEnd('/')}/functions/v1/paypal_create_subscription"
        val periodName = if (period == BillingPeriod.ANNUAL) "annual" else "monthly"
        val body = """{"tier":"${tier.escapeJson()}","plan_id":"${planId.escapeJson()}","period":"$periodName"}"""
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
        val approval = PayPalCheckoutJson.parseApprovalUrl(json)
            ?: return BillingResult.Failed("PayPal checkout returned an unexpected response. Please try again.")
        return BillingResult.OpenCheckout(approval)
    }

    /**
     * Turns an [HttpJsonClient] failure into a human-readable reason. Non-2xx
     * responses arrive as `HTTP <code>: <body>`; the body's `error`/`message`
     * JSON field (via [PayPalCheckoutJson.parseErrorMessage]) becomes the reason,
     * falling back to the raw HTTP status when the body carries no message.
     */
    private fun httpFailureReason(t: Throwable): String {
        val match = t.message?.let { HTTP_FAILURE.matchEntire(it) }
            ?: return "PayPal checkout failed. Please try again."
        val body = match.groupValues[2]
        val apiError = PayPalCheckoutJson.parseErrorMessage(body)
        if (!apiError.isNullOrBlank()) return apiError
        return "PayPal checkout failed (HTTP ${match.groupValues[1]}). Please try again."
    }

    private fun planIdFor(tier: String, period: BillingPeriod): String = when {
        period == BillingPeriod.ANNUAL && tier == "max" -> config.maxAnnualProductId
        period == BillingPeriod.ANNUAL -> config.proAnnualProductId
        tier == "max" -> config.maxMonthlyProductId
        else -> config.proMonthlyProductId
    }

    private fun String.escapeJson(): String =
        replace("\\", "\\\\").replace("\"", "\\\"")
}
