package com.needsvswants.app.data.entitlement

/**
 * The retried checkout-return sync as seen by the paywall ViewModel.
 *
 * Separated from [EntitlementSync] (the @Singleton implementation bound to
 * Supabase/DataStore) so
 * [com.needsvswants.app.ui.screens.paywall.PaywallViewModel] can be unit-tested
 * against a fake without the Context-bound
 * [com.needsvswants.app.data.prefs.AppPreferences] dependency — the same seam
 * pattern as [PayPalReturnStore].
 */
interface CheckoutReturnSync {
    /**
     * Retried refresh after a PayPal checkout return; see
     * [EntitlementSync.syncAfterCheckoutReturn] for the full contract.
     *
     * @param onResult final outcome: true when Pro access was confirmed during
     *   the retry window, false after the schedule exhausted without a grant.
     * @return true when Pro access was confirmed during the retry window.
     */
    suspend fun syncAfterCheckoutReturn(onResult: (Boolean) -> Unit = {}): Boolean
}
