package com.needsvswants.app.ui.screens.paywall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.billing.BillingController
import com.needsvswants.app.data.billing.BillingResult
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.remote.SupabaseConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Purchase the user chose; sign-in is only offered after one of these is selected. */
enum class PendingPurchase {
    None,
    ProTrial,
    MaxUpgrade
}

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

    fun startTrial() {
        viewModelScope.launch {
            if (!authRepository.isSignedIn.first()) {
                _pendingPurchase.value = PendingPurchase.ProTrial
                return@launch
            }
            runBilling { billing.startTrial(trialProductId) }
        }
    }

    fun upgrade() {
        viewModelScope.launch {
            if (!authRepository.isSignedIn.first()) {
                _pendingPurchase.value = PendingPurchase.MaxUpgrade
                return@launch
            }
            runBilling { billing.purchase(maxProductId) }
        }
    }

    /**
     * Call after a successful Google sign-in triggered by a pending purchase.
     * Completes the trial/upgrade the user originally chose.
     */
    fun onSignedInForPurchase() {
        viewModelScope.launch {
            if (!authRepository.isSignedIn.first()) return@launch
            when (_pendingPurchase.value) {
                PendingPurchase.ProTrial -> {
                    _pendingPurchase.value = PendingPurchase.None
                    runBilling { billing.startTrial(trialProductId) }
                }
                PendingPurchase.MaxUpgrade -> {
                    _pendingPurchase.value = PendingPurchase.None
                    runBilling { billing.purchase(maxProductId) }
                }
                PendingPurchase.None -> Unit
            }
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

    private suspend fun runBilling(block: suspend () -> BillingResult) {
        if (_busy.value) return
        _busy.value = true
        try {
            val result = block()
            if (result is BillingResult.OpenCheckout) {
                awaitingPaypalReturn = true
            }
            _lastResult.value = result
        } finally {
            _busy.value = false
        }
    }
}
