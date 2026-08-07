package com.needsvswants.app.data.billing

import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.remote.HttpJsonClient
import com.needsvswants.app.data.remote.SupabaseConfig
import javax.inject.Inject
import javax.inject.Singleton

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

    override suspend fun startTrial(productId: String): BillingResult {
        // Pro trial is configured on the PayPal plan; same create path as Pro.
        return createSubscription(tier = "pro", planIdHint = productId.ifBlank { config.proMonthlyProductId })
    }

    override suspend fun purchase(productId: String): BillingResult {
        val hint = productId.ifBlank { config.maxMonthlyProductId }
        val tier = when {
            hint == config.maxMonthlyProductId -> "max"
            hint == config.proMonthlyProductId -> "pro"
            hint.contains("max", ignoreCase = true) -> "max"
            else -> "max" // paywall upgrade() is Max
        }
        return createSubscription(tier = tier, planIdHint = hint)
    }

    override suspend fun restorePurchases(): BillingResult {
        if (!config.enabled) return BillingResult.Unavailable
        val token = auth.ensureFreshAccessToken()
            ?: return BillingResult.Failed
        entitlements.refreshFromRemote(token)
        // UI reads tier from entitlement flows after refresh.
        return BillingResult.Success
    }

    private suspend fun createSubscription(tier: String, planIdHint: String): BillingResult {
        if (!config.enabled) return BillingResult.Unavailable
        val planPro = config.proMonthlyProductId
        val planMax = config.maxMonthlyProductId
        val planId = when (tier) {
            "max" -> planMax.ifBlank { planIdHint }
            else -> planPro.ifBlank { planIdHint }
        }
        if (planId.isBlank() || !planId.startsWith("P-")) {
            return BillingResult.Unavailable
        }

        val accessToken = auth.ensureFreshAccessToken()
            ?: return BillingResult.Failed

        val url = "${config.url.trimEnd('/')}/functions/v1/paypal_create_subscription"
        val body = """{"tier":"${tier.escapeJson()}","plan_id":"${planId.escapeJson()}"}"""
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

        val json = result.getOrElse { return BillingResult.Failed }
        val approval = PayPalCheckoutJson.parseApprovalUrl(json)
            ?: return BillingResult.Failed
        return BillingResult.OpenCheckout(approval)
    }

    private fun String.escapeJson(): String =
        replace("\\", "\\\\").replace("\"", "\\\"")
}
