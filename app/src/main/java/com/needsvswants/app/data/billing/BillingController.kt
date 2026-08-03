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
}

/**
 * Injected seam between the paywall UI and Google Play Billing.
 *
 * The app depends only on this interface; the concrete [PlayBillingController]
 * is a stub until Play Billing is fully wired (product ids, listener wiring,
 * purchase flow) — it reports [BillingResult.Unavailable] today.
 */
interface BillingController {
    /** False until a real BillingClient is connected. */
    val isPlayAvailable: Boolean

    suspend fun startTrial(productId: String): BillingResult
    suspend fun purchase(productId: String): BillingResult
    suspend fun restorePurchases(): BillingResult
}