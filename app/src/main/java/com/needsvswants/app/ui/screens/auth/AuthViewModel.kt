package com.needsvswants.app.ui.screens.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
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
    val emailAvailable: Boolean = false,
    val needsSignInForPurchase: Boolean = false
)

/**
 * Email-code sign-in fallback (account recovery — audit gap: Google-only).
 * A tiny two-step machine: enter email → code sent → enter 6-digit code.
 */
data class EmailOtpState(
    val visible: Boolean = false,
    val email: String = "",
    val codeSent: Boolean = false,
    val busy: Boolean = false,
    val error: String? = null
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
            emailAvailable = authRepository.emailSignInAvailable,
            needsSignInForPurchase = needsSignIn
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        AuthUiState(
            googleAvailable = authRepository.googleSignInAvailable,
            emailAvailable = authRepository.emailSignInAvailable
        )
    )

    private val _emailOtp = MutableStateFlow(EmailOtpState())
    val emailOtp: StateFlow<EmailOtpState> = _emailOtp.asStateFlow()

    fun openEmailOtp() {
        _emailOtp.value = EmailOtpState(visible = true)
    }

    fun dismissEmailOtp() {
        _emailOtp.value = EmailOtpState()
    }

    fun sendEmailCode(email: String) {
        val trimmed = email.trim()
        if (trimmed.length < 5 || !trimmed.contains("@") || !trimmed.contains(".")) {
            _emailOtp.value = _emailOtp.value.copy(error = "Enter a valid email address.")
            return
        }
        if (_emailOtp.value.busy) return
        viewModelScope.launch {
            _emailOtp.value = _emailOtp.value.copy(busy = true, error = null, email = trimmed)
            val result = try {
                authRepository.requestEmailCode(trimmed)
            } catch (t: CancellationException) {
                throw t
            } catch (t: Throwable) {
                Result.failure(t)
            }
            _emailOtp.value = result.fold(
                onSuccess = { _emailOtp.value.copy(busy = false, codeSent = true, error = null) },
                onFailure = { _emailOtp.value.copy(busy = false, error = "Couldn't send the code. Check the address and try again.") }
            )
        }
    }

    fun verifyEmailCode(code: String) {
        val state = _emailOtp.value
        val trimmedCode = code.trim()
        if (trimmedCode.length < 6) {
            _emailOtp.value = state.copy(error = "Enter the 6-digit code from the email.")
            return
        }
        if (state.busy) return
        viewModelScope.launch {
            _emailOtp.value = state.copy(busy = true, error = null)
            val result = try {
                authRepository.signInWithEmailOtp(state.email, trimmedCode)
            } catch (t: CancellationException) {
                throw t
            } catch (t: Throwable) {
                Result.failure(t)
            }
            result.fold(
                onSuccess = {
                    _needsSignInForPurchase.value = false
                    _emailOtp.value = EmailOtpState() // signed in — close the sheet
                },
                onFailure = {
                    _emailOtp.value = _emailOtp.value.copy(
                        busy = false,
                        error = "That code didn't work. Check it or request a new one."
                    )
                }
            )
        }
    }

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
            // Exception boundary: an unexpected storage/session throw during
            // sign-in must surface as an error message — never escape the
            // ViewModel coroutine and force-close the app at the paywall's
            // "Continue with Google" step.
            val result = try {
                authRepository.signInWithGoogle(activity)
            } catch (t: CancellationException) {
                throw t
            } catch (t: Throwable) {
                Result.failure(t)
            }
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
