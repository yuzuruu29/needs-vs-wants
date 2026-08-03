package com.needsvswants.app.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.needsvswants.app.data.remote.SupabaseConfig
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Android Credential Manager implementation of [GoogleIdTokenProvider].
 *
 * Uses the Supabase-documented pattern: SHA-256 hash of a raw nonce is sent to
 * Google; the raw nonce is later sent to Supabase with the ID token.
 */
@Singleton
class CredentialManagerGoogleIdTokenProvider @Inject constructor(
    private val config: SupabaseConfig
) : GoogleIdTokenProvider {

    override val isAvailable: Boolean
        get() = config.googleSignInEnabled

    override suspend fun requestIdToken(activityContext: Context): Result<GoogleIdTokenResult> {
        if (!isAvailable) {
            return Result.failure(IllegalStateException("Google Sign-In not configured"))
        }
        val rawNonce = UUID.randomUUID().toString()
        val hashedNonce = sha256Hex(rawNonce)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(config.googleWebClientId)
            .setNonce(hashedNonce)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        return try {
            val credentialManager = CredentialManager.create(activityContext)
            val result = credentialManager.getCredential(
                context = activityContext,
                request = request
            )
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val idToken = googleIdTokenCredential.idToken
            if (idToken.isBlank()) {
                Result.failure(IllegalStateException("Google returned an empty ID token"))
            } else {
                Result.success(GoogleIdTokenResult(idToken = idToken, rawNonce = rawNonce))
            }
        } catch (e: GetCredentialCancellationException) {
            Result.failure(e)
        } catch (e: GoogleIdTokenParsingException) {
            Result.failure(e)
        } catch (e: GetCredentialException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        fun sha256Hex(value: String): String {
            val digest = MessageDigest.getInstance("SHA-256").digest(value.toByteArray(Charsets.UTF_8))
            return digest.joinToString("") { "%02x".format(it) }
        }
    }
}
