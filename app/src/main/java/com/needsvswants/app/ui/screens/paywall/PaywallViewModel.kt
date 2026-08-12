package com.needsvswants.app.ui.screens.paywall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.billing.BillingController
import com.needsvswants.app.data.billing.BillingPeriod
import com.needsvswants.app.data.billing.BillingResult
import com.needsvswants.app.data.billing.CheckoutProvider
import com.needsvswants.app.data.billing.PaymentProvider
import com.needsvswants.app.data.entitlement.CheckoutReturnSync
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.entitlement.PayPalReturnStore
import com.needsvswants.app.data.remote.SupabaseConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Subscription the user chose; sign-in is only offered after one of these is selected. */
enum class PendingPurchase {
    None,
    ProSubscribe,
    MaxSubscribe
}

/**
 * Lifecycle of the durable checkout-return sync, surfaced to the paywall status
 * strip:
 * - [Idle] — no return sync running or just completed;
 * - [Syncing] — retry loop in flight after a PayPal return (not yet Pro);
 * - [Exhausted] — retry schedule finished without a grant; the durable pending
 *   flag stays set (self-heal on next cold start / Restore).
 */
enum class CheckoutSyncState {
    Idle,
    Syncing,
    Exhausted
}

/**
 * Pause after Google sign-in before forcing a fresh access token, so the session
 * write settles before the subscription request (plan's 150-300ms window).
 */
private const val TOKEN_SETTLE_MILLIS = 200L

@HiltViewModel
class PaywallViewModel @Inject constructor(
    /** Default billing controller — used only for provider-agnostic [restore]. */
    private val billing: BillingController,
    private val repository: EntitlementRepository,
    private val authRepository: AuthRepository,
    private val config: SupabaseConfig,
    private val payPalReturn: PayPalReturnStore,
    private val entitlementSync: CheckoutReturnSync,
    /** Resolves the concrete controller for the user-selected [PaymentProvider]. */
    private val checkoutProvider: CheckoutProvider
) : ViewModel() {

    val isPro: StateFlow<Boolean> = repository.isPro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val hasMaxAccess: StateFlow<Boolean> = repository.hasMaxAccess
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isSignedIn: StateFlow<Boolean> = authRepository.isSignedIn
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val signedInEmail: StateFlow<String?> = authRepository.session
        .map { it?.email }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val monthlyProductId: String = config.proMonthlyProductId.ifBlank { "pro_monthly" }
    val maxProductId: String = config.maxMonthlyProductId.ifBlank { "max_monthly" }
    val annualProductId: String = config.proAnnualProductId.ifBlank { "pro_annual" }
    val maxAnnualProductId: String = config.maxAnnualProductId.ifBlank { "max_annual" }

    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy.asStateFlow()

    private val _lastResult = MutableStateFlow<BillingResult?>(null)
    val lastResult: StateFlow<BillingResult?> = _lastResult.asStateFlow()

    private val _pendingPurchase = MutableStateFlow(PendingPurchase.None)
    val pendingPurchase: StateFlow<PendingPurchase> = _pendingPurchase.asStateFlow()

    /** Provider used for the next checkout; PayPal trial-first, PayMongo as the one-time alternative. */
    private val _selectedProvider = MutableStateFlow(
        if (checkoutProvider.payPalAvailable) PaymentProvider.PAYPAL else PaymentProvider.PAYMONGO
    )
    val selectedProvider: StateFlow<PaymentProvider> = _selectedProvider.asStateFlow()

    /** Billing cycle used for the next checkout (monthly 30d / annual 365d). */
    private val _selectedPeriod = MutableStateFlow(BillingPeriod.MONTHLY)
    val selectedPeriod: StateFlow<BillingPeriod> = _selectedPeriod.asStateFlow()

    /** Static per-build availability of each checkout provider (config-driven). */
    val payPalAvailable: Boolean = checkoutProvider.payPalAvailable
    val payMongoAvailable: Boolean = checkoutProvider.payMongoAvailable

    /**
     * Checkout-return sync lifecycle ([CheckoutSyncState]). Drives the paywall
     * status strip: "Still unlocking — retrying…" while [CheckoutSyncState.Syncing]
     * and "Payment recorded — tap Restore, or wait a moment." while
     * [CheckoutSyncState.Exhausted].
     */
    private val _checkoutSyncState = MutableStateFlow(CheckoutSyncState.Idle)
    val checkoutSyncState: StateFlow<CheckoutSyncState> = _checkoutSyncState.asStateFlow()

    /**
     * The pending intent that has already been auto-continued by
     * [onSignedInForPurchase]. Guards exactly-once auto-continue: the same
     * pending intent must never run the subscription pipeline twice, even if
     * the screen's fresh-result gate re-opens (e.g. restore() reports an
     * unconditional Success, the screen consumes it, then rotation re-enters
     * the paywall with lastResult == null and the intent still pending).
     * Cleared whenever a fresh user intent can be established: a signed-out
     * subscribe, [cancelPendingSignIn], or checkout actually starting.
     */
    private var autoContinued: PendingPurchase? = null

    /**
     * Once-per-return guard for the durable checkout-return sync: the retried
     * sync runs at most once per pending-flag instance per ViewModel lifetime,
     * so a paywall resume that follows the deep-link handler's routine (or a
     * later background/foreground cycle) does not restart the 0/2/5/10s loop.
     * Re-armed on every OpenCheckout so a NEW checkout retriggers the loop.
     */
    private var checkoutReturnSyncLaunched = false

    /**
     * True only after the user taps Start trial / Upgrade while signed out.
     * Drives the Google sign-in strip — never shown for free browsing of the paywall.
     */
    val needsSignInForPurchase: StateFlow<Boolean> = _pendingPurchase
        .map { it != PendingPurchase.None }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    /**
     * Reads the signed-in flag defensively: a storage read failure behaves as
     * signed-out instead of escaping the ViewModel coroutine (an uncaught
     * exception there force-closes the app). The billing controllers re-check
     * the session and report their own "Sign in required." when it matters.
     */
    private suspend fun signedInSafe(): Boolean =
        runCatching { authRepository.isSignedIn.first() }.getOrDefault(false)

    /**
     * Switches the payment provider used for the next checkout. Mirrors the
     * plan-switch semantics (D108): while a deferred intent is pending, a
     * signed-in user's intent is cleared (fresh CTA with the new provider);
     * a signed-out user's intent is re-asserted for the CURRENT plan so
     * sign-in completes the purchase with the provider just picked. The
     * pending intent is provider-agnostic in storage — the auto-continue path
     * reads the current provider at run time.
     */
    fun selectProvider(provider: PaymentProvider) {
        if (provider == _selectedProvider.value) return
        _selectedProvider.value = provider
        consumeResult()
        viewModelScope.launch {
            when (_pendingPurchase.value) {
                PendingPurchase.None -> Unit
                else -> {
                    if (signedInSafe()) {
                        // A deferred purchase must never cross a provider change:
                        // the user re-taps the CTA to start fresh.
                        cancelPendingSignIn()
                    } else {
                        // Signed out: re-assert the deferred intent for the NEW
                        // selection so sign-in completes the purchase just picked.
                        when (_pendingPurchase.value) {
                            PendingPurchase.ProSubscribe -> subscribePro()
                            PendingPurchase.MaxSubscribe -> subscribeMax()
                            PendingPurchase.None -> Unit
                        }
                    }
                }
            }
        }
    }

    /**
     * Switches the billing period (monthly / annual) used for the next checkout.
     * Mirrors [selectProvider]: a deferred intent is cleared for a signed-in
     * user (fresh CTA) or re-asserted for a signed-out user so sign-in
     * completes the purchase with the period just picked.
     */
    fun selectPeriod(period: BillingPeriod) {
        if (period == _selectedPeriod.value) return
        _selectedPeriod.value = period
        consumeResult()
        viewModelScope.launch {
            when (_pendingPurchase.value) {
                PendingPurchase.None -> Unit
                else -> {
                    if (signedInSafe()) {
                        cancelPendingSignIn()
                    } else {
                        when (_pendingPurchase.value) {
                            PendingPurchase.ProSubscribe -> subscribePro()
                            PendingPurchase.MaxSubscribe -> subscribeMax()
                            PendingPurchase.None -> Unit
                        }
                    }
                }
            }
        }
    }

    /** Start a Pro subscription; defers to Google sign-in when signed out. Ignored while a billing pipeline is in flight. */
    fun subscribePro() {
        viewModelScope.launch {
            if (_busy.value) return@launch
            if (!signedInSafe()) {
                _pendingPurchase.value = PendingPurchase.ProSubscribe
                autoContinued = null
                return@launch
            }
            runBilling { routeProCheckout() }
        }
    }

    /** Start a Max subscription; defers to Google sign-in when signed out. Ignored while a billing pipeline is in flight. */
    fun subscribeMax() {
        viewModelScope.launch {
            if (_busy.value) return@launch
            if (!signedInSafe()) {
                _pendingPurchase.value = PendingPurchase.MaxSubscribe
                autoContinued = null
                return@launch
            }
            runBilling { routeMaxCheckout() }
        }
    }

    /**
     * Call after a successful Google sign-in triggered by a pending subscription.
     * Completes the subscription the user originally chose. Pending stays set
     * until checkout actually starts, so a failed attempt can be retried via
     * [retryCheckout].
     *
     * Exactly-once: if the current pending intent was already auto-continued
     * (see [autoContinued]) this is a no-op — the same intent must never run
     * the pipeline twice, regardless of how lastResult evolves on screen.
     * Explicit retry paths ([retryCheckout], the CTA) are unaffected.
     */
    fun onSignedInForPurchase() {
        viewModelScope.launch {
            if (!signedInSafe()) return@launch
            if (autoContinued == _pendingPurchase.value) return@launch
            runPendingSubscription()
        }
    }

    /**
     * Re-runs the deferred subscription after a checkout that never started.
     *
     * Gating: [PendingPurchase] is the durable proxy — it survives every result
     * except [BillingResult.OpenCheckout] (which clears it) — so additionally
     * no-oping on a Success/OpenCheckout last result covers the window where
     * the result was consumed or reported success while pending was still set.
     * No-op when nothing is pending, busy, signed out, or last result was
     * Success/OpenCheckout.
     */
    fun retryCheckout() {
        viewModelScope.launch {
            if (_pendingPurchase.value == PendingPurchase.None) return@launch
            if (_busy.value) return@launch
            if (!signedInSafe()) return@launch
            val last = _lastResult.value
            if (last is BillingResult.Success || last is BillingResult.OpenCheckout) return@launch
            runPendingSubscription()
        }
    }

    fun cancelPendingSignIn() {
        _pendingPurchase.value = PendingPurchase.None
        autoContinued = null
    }

    fun restore() {
        viewModelScope.launch {
            val result = try {
                billing.restorePurchases()
            } catch (t: CancellationException) {
                throw t
            } catch (t: Throwable) {
                // Exception boundary: a restore failure must surface as the
                // paywall's error contract, never escape the ViewModel and
                // force-close the app.
                BillingResult.Failed("Restore didn't go through. Try again.")
            }
            // A successful restore proves the subscription is live: the stale
            // "Payment recorded — tap Restore, or wait a moment." (Exhausted)
            // state must not survive it and contradict the Welcome-to-Pro result.
            if (result is BillingResult.Success) {
                _checkoutSyncState.value = CheckoutSyncState.Idle
            }
            _lastResult.value = result
        }
    }

    /**
     * After returning from PayPal browser (deep link or paywall resume), run
     * the retried entitlement sync exactly once per pending return.
     *
     * No-op while the durable pending flag is unset (no checkout started, or
     * the cancel deep link already cleared it). When set and this session has
     * not yet started the retry loop for it, runs
     * [CheckoutReturnSync.syncAfterCheckoutReturn]:
     * - Pro confirmed → clear the durable flag and surface Success (the sync
     *   has refreshed the local snapshot, so the paywall shows "Welcome to
     *   Pro." / "Welcome to Max.");
     * - still free after the retry schedule → leave the flag set (self-heals
     *   on the next cold start, or via the explicit Restore escape hatch) and
     *   surface [CheckoutSyncState.Exhausted].
     *
     * The once-per-return guard means the RESUMED effect can keep calling this
     * on every resume without re-firing the retry routine.
     */
    fun onReturnFromCheckout() {
        viewModelScope.launch {
            val pendingReturn = runCatching { payPalReturn.paypalReturnPending.first() }.getOrDefault(false)
            if (!pendingReturn) return@launch
            if (checkoutReturnSyncLaunched) return@launch
            checkoutReturnSyncLaunched = true
            _checkoutSyncState.value = CheckoutSyncState.Syncing
            entitlementSync.syncAfterCheckoutReturn { proConfirmed ->
                if (proConfirmed) {
                    // onResult is a plain (non-suspend) callback: hop back onto
                    // the VM scope for the durable-flag clear (idempotent — the
                    // sync already cleared it on its own success path).
                    viewModelScope.launch { runCatching { payPalReturn.clearPaypalReturnPending() } }
                    _checkoutSyncState.value = CheckoutSyncState.Idle
                    _lastResult.value = BillingResult.Success
                } else {
                    // A Restore-success reported while the loop ran (user tapped
                    // Restore and it confirmed Pro) must not be overwritten by
                    // the exhausted loop tail — the paywall is already showing
                    // the activation seal / Success state from that restore.
                    if (_lastResult.value !is BillingResult.Success) {
                        _checkoutSyncState.value = CheckoutSyncState.Exhausted
                    }
                }
            }
        }
    }

    fun consumeResult() {
        _lastResult.value = null
    }

    /**
     * The checkout browser could not be opened (no browser on the device, or a
     * malformed approval URL). The durable pending-return flag must not survive
     * — no checkout actually started, so a later cold start must not re-run a
     * phantom retry loop — and the failure must be surfaced so the CTA shows a
     * retryable error instead of silently doing nothing.
     */
    fun reportCheckoutOpenFailure() {
        viewModelScope.launch {
            runCatching { payPalReturn.clearPaypalReturnPending() }
            _checkoutSyncState.value = CheckoutSyncState.Idle
            _lastResult.value =
                BillingResult.Failed("Couldn't open the payment page. Please try again.")
        }
    }

    /**
     * Runs the pending subscription the user chose. Holds the in-flight guard
     * over the WHOLE pipeline (settle delay + token refresh + billing) so no
     * second tap can reach [BillingController.purchase] while this runs. A
     * short settle delay plus a forced token refresh mitigate the post-sign-in
     * token race; pending is re-read after the delay (aborts if the user
     * cancelled) and cleared only once checkout starts.
     */
    private suspend fun runPendingSubscription() {
        if (_busy.value) return
        _busy.value = true
        try {
            delay(TOKEN_SETTLE_MILLIS)
            // Force a fresh access token; the billing controller re-reads it.
            // Best-effort: a storage/token-refresh throw must never kill the
            // app — the controller re-checks the session and reports its own
            // Failed("Sign in required.") if the token is really unusable.
            runCatching { authRepository.ensureFreshAccessToken() }
            when (_pendingPurchase.value) {
                PendingPurchase.ProSubscribe -> {
                    autoContinued = PendingPurchase.ProSubscribe
                    executeBilling { routeProCheckout() }
                }
                PendingPurchase.MaxSubscribe -> {
                    autoContinued = PendingPurchase.MaxSubscribe
                    executeBilling { routeMaxCheckout() }
                }
                PendingPurchase.None -> Unit
            }
        } finally {
            _busy.value = false
        }
    }

    /**
     * Routes a Pro intent to the currently selected provider's controller:
     * PayPal starts the plan-carrying 3-day trial; PayMongo charges the
     * one-time checkout. Both honor the selected billing period.
     */
    private suspend fun routeProCheckout(): BillingResult {
        val period = _selectedPeriod.value
        return when (_selectedProvider.value) {
            PaymentProvider.PAYPAL ->
                checkoutProvider.controllerFor(PaymentProvider.PAYPAL)
                    .startTrial(productIdFor("pro", period), period)
            PaymentProvider.PAYMONGO ->
                checkoutProvider.controllerFor(PaymentProvider.PAYMONGO)
                    .purchase(productIdFor("pro", period), period)
        }
    }

    /**
     * Routes a Max intent to the currently selected provider's controller
     * (no trial on Max — both providers charge the checkout directly).
     */
    private suspend fun routeMaxCheckout(): BillingResult {
        val period = _selectedPeriod.value
        return when (_selectedProvider.value) {
            PaymentProvider.PAYPAL ->
                checkoutProvider.controllerFor(PaymentProvider.PAYPAL)
                    .purchase(productIdFor("max", period), period)
            PaymentProvider.PAYMONGO ->
                checkoutProvider.controllerFor(PaymentProvider.PAYMONGO)
                    .purchase(productIdFor("max", period), period)
        }
    }

    /** Product-id hint for a tier + period; PayPal annual uses the annual ids. */
    private fun productIdFor(tier: String, period: BillingPeriod): String = when {
        period == BillingPeriod.ANNUAL && tier == "max" -> maxAnnualProductId
        period == BillingPeriod.ANNUAL -> annualProductId
        tier == "max" -> maxProductId
        else -> monthlyProductId
    }

    private suspend fun runBilling(block: suspend () -> BillingResult) {
        if (_busy.value) return
        _busy.value = true
        try {
            executeBilling(block)
        } finally {
            _busy.value = false
        }
    }

    /**
     * Executes a billing block while the caller already holds the busy guard.
     * Once checkout actually starts the deferred intent is consumed; every
     * other result leaves [PendingPurchase] set so the attempt can be retried.
     *
     * Exception boundary: an unexpected provider/parse/storage throw during
     * checkout start must surface as [BillingResult.Failed] (the paywall's
     * error contract) instead of escaping the ViewModel coroutine — an
     * uncaught exception there force-closes the app mid-trial ("the app stops
     * when going for the free trial").
     */
    private suspend fun executeBilling(block: suspend () -> BillingResult) {
        val result = try {
            block()
        } catch (t: CancellationException) {
            throw t
        } catch (t: Throwable) {
            BillingResult.Failed("Payment couldn't be started. Please try again.")
        }
        if (result is BillingResult.OpenCheckout) {
            // Checkout started: persist the durable pending-return flag (survives
            // process death while the browser is open) and re-arm the
            // once-per-return sync guard so the next return retriggers the loop.
            runCatching { payPalReturn.setPaypalReturnPending(true) }
            checkoutReturnSyncLaunched = false
            _checkoutSyncState.value = CheckoutSyncState.Idle
            // Checkout started: the deferred intent has been consumed.
            _pendingPurchase.value = PendingPurchase.None
            autoContinued = null
        }
        _lastResult.value = result
    }
}
