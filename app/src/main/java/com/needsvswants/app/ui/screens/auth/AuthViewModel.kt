package com.needsvswants.app.ui.screens.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val signedIn: Boolean = false,
    val email: String? = null,
    val busy: Boolean = false,
    val error: String? = null,
    val googleAvailable: Boolean = false,
    val needsSignInForPurchase: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _busy = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _needsSignInForPurchase = MutableStateFlow(false)

    val uiState: StateFlow<AuthUiState> = combine(
        authRepository.session,
        _busy,
        _error,
        _needsSignInForPurchase
    ) { session, busy, error, needsSignIn ->
        AuthUiState(
            signedIn = session?.accessToken?.isNotBlank() == true,
            email = session?.email,
            busy = busy,
            error = error,
            googleAvailable = authRepository.googleSignInAvailable,
            needsSignInForPurchase = needsSignIn
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        AuthUiState(googleAvailable = authRepository.googleSignInAvailable)
    )

    fun signInWithGoogle(context: Context) {
        if (_busy.value) return
        val activity = context.findActivity()
        if (activity == null) {
            _error.value = "Unable to open Google Sign-In here."
            return
        }
        viewModelScope.launch {
            _busy.value = true
            _error.value = null
            val result = authRepository.signInWithGoogle(activity)
            _busy.value = false
            result.fold(
                onSuccess = {
                    _needsSignInForPurchase.value = false
                },
                onFailure = { err ->
                    if (err is GetCredentialCancellationException) {
                        // User dismissed the picker — no error banner.
                        _error.value = null
                    } else {
                        _error.value = humanMessage(err)
                    }
                }
            )
        }
    }

    fun signOut() {
        if (_busy.value) return
        viewModelScope.launch {
            _busy.value = true
            _error.value = null
            authRepository.signOut()
            _busy.value = false
        }
    }

    fun requireSignInForPurchase() {
        _needsSignInForPurchase.value = true
        if (!authRepository.googleSignInAvailable) {
            _error.value = "Google Sign-In is not configured on this build."
        }
    }

    fun consumeError() {
        _error.value = null
    }

    fun consumePurchasePrompt() {
        _needsSignInForPurchase.value = false
    }

    private fun humanMessage(err: Throwable): String {
        val raw = err.message.orEmpty()
        return when {
            raw.contains("not configured", ignoreCase = true) ->
                "Sign-in is not configured yet. Add Supabase + Google client IDs."
            raw.contains("HTTP 4") ->
                "Google account could not be verified. Check Supabase Google provider settings."
            raw.isBlank() -> "Sign-in failed. Try again."
            else -> "Sign-in failed. Try again."
        }
    }
}

/** Walk Context wrappers to find the host Activity (required by Credential Manager). */
fun Context.findActivity(): Activity? {
    var ctx: Context? = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
