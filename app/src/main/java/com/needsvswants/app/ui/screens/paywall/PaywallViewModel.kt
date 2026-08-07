package com.needsvswants.app.ui.screens.paywall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.billing.BillingController
import com.needsvswants.app.data.billing.BillingResult
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.remote.SupabaseConfig
import dagger.hilt.android.lifecycle.HiltViewModel
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
 * Pause after Google sign-in before forcing a fresh access token, so the session
 * write settles before the subscription request (plan's 150-300ms window).
 */
private const val TOKEN_SETTLE_MILLIS = 200L

@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val billing: BillingController,
    private val repository: EntitlementRepository,
    private val authRepository: AuthRepository,
    private val config: SupabaseConfig
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

    val trialProductId: String = config.proTrialProductId.ifBlank { "pro_trial_3day" }
    val monthlyProductId: String = config.proMonthlyProductId.ifBlank { "pro_monthly" }
    val maxProductId: String = config.maxMonthlyProductId.ifBlank { "max_monthly" }

    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy.asStateFlow()

    private val _lastResult = MutableStateFlow<BillingResult?>(null)
    val lastResult: StateFlow<BillingResult?> = _lastResult.asStateFlow()

    private val _pendingPurchase = MutableStateFlow(PendingPurchase.None)
    val pendingPurchase: StateFlow<PendingPurchase> = _pendingPurchase.asStateFlow()

    /** Set when PayPal approval URL is opened; cleared after return refresh. */
    private var awaitingPaypalReturn: Boolean = false

    /**
     * True only after the user taps Start trial / Upgrade while signed out.
     * Drives the Google sign-in strip — never shown for free browsing of the paywall.
     */
    val needsSignInForPurchase: StateFlow<Boolean> = _pendingPurchase
        .map { it != PendingPurchase.None }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    /** Start a Pro subscription; defers to Google sign-in when signed out. */
    fun subscribePro() {
        viewModelScope.launch {
            if (!authRepository.isSignedIn.first()) {
                _pendingPurchase.value = PendingPurchase.ProSubscribe
                return@launch
            }
            runBilling { billing.purchase(monthlyProductId) }
        }
    }

    /** Start a Max subscription; defers to Google sign-in when signed out. */
    fun subscribeMax() {
        viewModelScope.launch {
            if (!authRepository.isSignedIn.first()) {
                _pendingPurchase.value = PendingPurchase.MaxSubscribe
                return@launch
            }
            runBilling { billing.purchase(maxProductId) }
        }
    }

    /**
     * Call after a successful Google sign-in triggered by a pending subscription.
     * Completes the subscription the user originally chose. Pending stays set
     * until checkout actually starts, so a failed attempt can be retried via
     * [retryCheckout].
     */
    fun onSignedInForPurchase() {
        viewModelScope.launch {
            if (!authRepository.isSignedIn.first()) return@launch
            runPendingSubscription()
        }
    }

    /**
     * Re-runs the deferred subscription after a checkout that never started.
     * No-op when nothing is pending or the user is signed out.
     */
    fun retryCheckout() {
        viewModelScope.launch {
            if (_pendingPurchase.value == PendingPurchase.None) return@launch
            if (!authRepository.isSignedIn.first()) return@launch
            runPendingSubscription()
        }
    }

    fun cancelPendingSignIn() {
        _pendingPurchase.value = PendingPurchase.None
    }

    fun restore() {
        viewModelScope.launch {
            _lastResult.value = billing.restorePurchases()
        }
    }

    /**
     * After returning from PayPal browser, re-pull entitlement.
     * No-op unless a checkout was just started (avoids spam on every resume).
     */
    fun onReturnFromCheckout() {
        if (!awaitingPaypalReturn) return
        awaitingPaypalReturn = false
        viewModelScope.launch {
            _busy.value = true
            try {
                _lastResult.value = billing.restorePurchases()
            } finally {
                _busy.value = false
            }
        }
    }

    fun consumeResult() {
        _lastResult.value = null
    }

    /**
     * Runs the pending subscription the user chose. A short settle delay plus a
     * forced token refresh mitigate the post-sign-in token race; pending is
     * cleared by [runBilling] only once checkout starts.
     */
    private suspend fun runPendingSubscription() {
        delay(TOKEN_SETTLE_MILLIS)
        // Force a fresh access token; the billing controller re-reads it.
        authRepository.ensureFreshAccessToken()
        when (_pendingPurchase.value) {
            PendingPurchase.ProSubscribe -> runBilling { billing.purchase(monthlyProductId) }
            PendingPurchase.MaxSubscribe -> runBilling { billing.purchase(maxProductId) }
            PendingPurchase.None -> Unit
        }
    }

    private suspend fun runBilling(block: suspend () -> BillingResult) {
        if (_busy.value) return
        _busy.value = true
        try {
            val result = block()
            if (result is BillingResult.OpenCheckout) {
                awaitingPaypalReturn = true
                // Checkout started: the deferred intent has been consumed.
                _pendingPurchase.value = PendingPurchase.None
            }
            _lastResult.value = result
        } finally {
            _busy.value = false
        }
    }
}
