package com.needsvswants.app.data.auth

import android.content.Context
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.entitlement.PayPalReturnStore
import com.needsvswants.app.data.remote.AuthSession
import com.needsvswants.app.data.remote.SupabaseAuth
import com.needsvswants.app.data.remote.SupabaseConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Orchestrates Google Sign-In → Supabase session → local store → entitlement refresh.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val auth: SupabaseAuth,
    private val store: AuthSessionStore,
    private val google: GoogleIdTokenProvider,
    private val entitlements: EntitlementRepository,
    private val payPalReturn: PayPalReturnStore,
    private val config: SupabaseConfig
) {
    val session: Flow<AuthSession?> = store.session

    val isSignedIn: Flow<Boolean> = session.map { it?.accessToken?.isNotBlank() == true }

    val googleSignInAvailable: Boolean
        get() = google.isAvailable && config.googleSignInEnabled

    /**
     * Full Google sign-in path. Does not clear an existing session on cancel/failure.
     */
    suspend fun signInWithGoogle(activityContext: Context): Result<AuthSession> {
        val googleResult = google.requestIdToken(activityContext)
        return completeGoogleSignIn(googleResult)
    }

    /**
     * Completes sign-in after a Google ID token is obtained.
     * Extracted for unit tests that avoid Android [Context] / Credential Manager.
     */
    suspend fun completeGoogleSignIn(googleResult: Result<GoogleIdTokenResult>): Result<AuthSession> {
        val id = googleResult.getOrElse { return Result.failure(it) }
        val sessionResult = auth.signInWithGoogleIdToken(id.idToken, id.rawNonce)
        val session = sessionResult.getOrElse { return Result.failure(it) }
        store.save(session)
        entitlements.refreshFromRemote(session.accessToken)
        return Result.success(session)
    }

    /**
     * Returns a usable access token, refreshing if near expiry.
     * Returns null when signed out or refresh fails.
     */
    suspend fun ensureFreshAccessToken(nowEpochMillis: Long = System.currentTimeMillis()): String? {
        val current = store.session.first() ?: return null
        if (!current.isExpired(nowEpochMillis)) return current.accessToken
        val rt = current.refreshToken ?: return null
        val refreshed = auth.refreshSession(rt).getOrNull() ?: return null
        store.save(refreshed)
        return refreshed.accessToken
    }

    /**
     * Signs out remotely (best-effort) and always clears the local session.
     * Also drops any pending PayPal checkout return — a signed-out (or
     * different) account must never inherit the retry churn of the previous
     * user's late/never-landed webhook. Does not wipe diary data.
     */
    suspend fun signOut() {
        val token = store.session.first()?.accessToken
        if (!token.isNullOrBlank()) {
            auth.signOut(token)
        }
        store.clear()
        payPalReturn.clearPaypalReturnPending()
    }
}
