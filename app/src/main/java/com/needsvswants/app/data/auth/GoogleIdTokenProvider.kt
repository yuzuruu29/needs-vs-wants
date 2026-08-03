package com.needsvswants.app.data.auth

import android.content.Context

/**
 * Result of a successful native Google Sign-In that yields an ID token
 * suitable for Supabase `grant_type=id_token`.
 *
 * @param idToken Google OpenID ID token
 * @param rawNonce unhashed nonce that was SHA-256'd for Credential Manager
 */
data class GoogleIdTokenResult(
    val idToken: String,
    val rawNonce: String?
)

/**
 * Platform seam for obtaining a Google ID token.
 * Production: [CredentialManagerGoogleIdTokenProvider].
 */
interface GoogleIdTokenProvider {
    val isAvailable: Boolean

    /**
     * Launches the Google account picker / Credential Manager UI.
     * [activityContext] must be an Activity (or Activity-backed Context).
     */
    suspend fun requestIdToken(activityContext: Context): Result<GoogleIdTokenResult>
}
