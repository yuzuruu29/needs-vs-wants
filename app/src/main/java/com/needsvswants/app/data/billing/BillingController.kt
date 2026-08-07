package com.needsvswants.app.data.billing

/**
 * Outcome of a billing action surfaced to the UI. Kept intentionally small;
 * richer Play Billing states can be added when the real client lands.
 */
sealed interface BillingResult {
    data object Success : BillingResult
    data object Pending : BillingResult
    data object Failed : BillingResult
    data object Unavailable : BillingResult
    /** Open PayPal (or other web) approval in a browser / Custom Tab. */
    data class OpenCheckout(val approvalUrl: String) : BillingResult
}

/**
 * Injected seam between the paywall UI and billing providers.
 *
 * Website soft-launch path: [PayPalBillingController] creates a PayPal
 * subscription via Supabase and returns [BillingResult.OpenCheckout].
 * Play Billing remains available as a stub for a future store build.
 */
interface BillingController {
    /** True when Google Play BillingClient is connected (store builds). */
    val isPlayAvailable: Boolean

    /** True when PayPal checkout can be started (Supabase + plans configured). */
    val isPayPalAvailable: Boolean
        get() = false

    suspend fun startTrial(productId: String): BillingResult
    suspend fun purchase(productId: String): BillingResult
    suspend fun restorePurchases(): BillingResult
}