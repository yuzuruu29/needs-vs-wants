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

@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val billing: BillingController,
    private val repository: EntitlementRepository,
    private val authRepository: AuthRepository,
    private val config: SupabaseConfig
) : ViewModel() {

    val isPro: StateFlow<Boolean> = repository.isPro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // Eager so purchase gates see the current session without a UI subscriber first.
    val isSignedIn: StateFlow<Boolean> = authRepository.isSignedIn
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val signedInEmail: StateFlow<String?> = authRepository.session
        .map { it?.email }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /** Trial product id resolved from BuildConfig, with a harmless offline default. */
    val trialProductId: String = config.proTrialProductId.ifBlank { "pro_trial_3day" }

    /** Monthly product id resolved from BuildConfig, with a harmless offline default. */
    val monthlyProductId: String = config.proMonthlyProductId.ifBlank { "pro_monthly" }

    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy.asStateFlow()

    private val _lastResult = MutableStateFlow<BillingResult?>(null)
    val lastResult: StateFlow<BillingResult?> = _lastResult.asStateFlow()

    private val _needsSignIn = MutableStateFlow(false)
    val needsSignIn: StateFlow<Boolean> = _needsSignIn.asStateFlow()

    fun startTrial() {
        viewModelScope.launch {
            if (!authRepository.isSignedIn.first()) {
                _needsSignIn.value = true
                return@launch
            }
            if (_busy.value) return@launch
            _busy.value = true
            _lastResult.value = billing.startTrial(trialProductId)
            _busy.value = false
        }
    }

    fun upgrade() {
        viewModelScope.launch {
            if (!authRepository.isSignedIn.first()) {
                _needsSignIn.value = true
                return@launch
            }
            if (_busy.value) return@launch
            _busy.value = true
            _lastResult.value = billing.purchase(monthlyProductId)
            _busy.value = false
        }
    }

    fun restore() {
        // NOTE: restorePurchases is in the BillingController seam; not surfaced as a
        // first-class CTA on the paywall for now, but exposed for future tying to Supabase.
        viewModelScope.launch {
            _lastResult.value = billing.restorePurchases()
        }
    }

    fun consumeResult() {
        _lastResult.value = null
    }

    fun consumeNeedsSignIn() {
        _needsSignIn.value = false
    }

    private fun runAction(block: suspend () -> BillingResult) {
        if (_busy.value) return
        viewModelScope.launch {
            _busy.value = true
            _lastResult.value = block()
            _busy.value = false
        }
    }
}
