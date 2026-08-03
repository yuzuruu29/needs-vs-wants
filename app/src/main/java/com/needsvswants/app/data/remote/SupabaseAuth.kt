package com.needsvswants.app.data.remote

import javax.inject.Inject

/**
 * Supabase auth seam (magic-link / OTP email + native Google ID token).
 *
 * Provider-driven: [isConfigured] is true only when [SupabaseConfig.enabled].
 * When disabled (the default offline state) every call returns a failure without
 * touching the network. When configured later, the HTTP bodies follow the
 * Supabase `/auth/v1` API shape.
 */
interface SupabaseAuth {
    val isConfigured: Boolean

    /** Requests a one-time-password login link to [email]. */
    suspend fun sendMagicLink(email: String): Result<Unit>

    /**
     * Verifies the emailed OTP and returns the resulting Supabase access token.
     * @param token the 6-digit OTP the user received
     */
    suspend fun verifyOtp(email: String, token: String): Result<String>

    /**
     * Exchanges a Google ID token for a Supabase session
     * (`POST /auth/v1/token?grant_type=id_token`).
     *
     * @param nonce raw (unhashed) nonce that was hashed for Google Credential Manager
     */
    suspend fun signInWithGoogleIdToken(idToken: String, nonce: String?): Result<AuthSession>

    /** Refreshes an expired access token using a refresh token. */
    suspend fun refreshSession(refreshToken: String): Result<AuthSession>

    /** Invalidates the server session. Local store is cleared by the repository. */
    suspend fun signOut(accessToken: String): Result<Unit>
}

class HttpSupabaseAuth @Inject constructor(private val config: SupabaseConfig) : SupabaseAuth {

    override val isConfigured: Boolean get() = config.enabled

    override suspend fun sendMagicLink(email: String): Result<Unit> {
        if (!config.enabled) return Result.failure(IllegalStateException("Supabase auth not configured"))
        val body = """{"email":"${email.escapeJson()}","create_user":true}"""
        return HttpJsonClient.request(
            url = authEndpoint("otp"),
            method = "POST",
            headers = headers(),
            body = body
        ).map { Unit }
    }

    override suspend fun verifyOtp(email: String, token: String): Result<String> {
        if (!config.enabled) return Result.failure(IllegalStateException("Supabase auth not configured"))
        val body = """{"type":"email","email":"${email.escapeJson()}","token":"${token.escapeJson()}"}"""
        return HttpJsonClient.request(
            url = authEndpoint("verify"),
            method = "POST",
            headers = headers(),
            body = body
        ).mapCatching { raw ->
            SupabaseJson.parseAccessToken(raw)
                ?: throw IllegalStateException("Missing access_token in verify response")
        }
    }

    override suspend fun signInWithGoogleIdToken(idToken: String, nonce: String?): Result<AuthSession> {
        if (!config.enabled) return Result.failure(IllegalStateException("Supabase auth not configured"))
        val noncePart = if (nonce.isNullOrBlank()) {
            ""
        } else {
            ""","nonce":"${nonce.escapeJson()}""""
        }
        val body = """{"provider":"google","id_token":"${idToken.escapeJson()}"$noncePart}"""
        val url = "${config.url.trimEnd('/')}/auth/v1/token?grant_type=id_token"
        val now = System.currentTimeMillis()
        return HttpJsonClient.request(
            url = url,
            method = "POST",
            headers = headers(),
            body = body
        ).mapCatching { raw ->
            SupabaseJson.parseAuthSession(raw, now)
                ?: throw IllegalStateException("Missing access_token in id_token response")
        }
    }

    override suspend fun refreshSession(refreshToken: String): Result<AuthSession> {
        if (!config.enabled) return Result.failure(IllegalStateException("Supabase auth not configured"))
        val body = """{"refresh_token":"${refreshToken.escapeJson()}"}"""
        val url = "${config.url.trimEnd('/')}/auth/v1/token?grant_type=refresh_token"
        val now = System.currentTimeMillis()
        return HttpJsonClient.request(
            url = url,
            method = "POST",
            headers = headers(),
            body = body
        ).mapCatching { raw ->
            SupabaseJson.parseAuthSession(raw, now)
                ?: throw IllegalStateException("Missing access_token in refresh response")
        }
    }

    override suspend fun signOut(accessToken: String): Result<Unit> {
        if (!config.enabled) return Result.failure(IllegalStateException("Supabase auth not configured"))
        // Best-effort: 2xx or 401 both mean the client should treat the session as gone.
        val result = HttpJsonClient.request(
            url = authEndpoint("logout"),
            method = "POST",
            headers = headers() + mapOf("Authorization" to "Bearer $accessToken"),
            body = "{}"
        )
        return result.fold(
            onSuccess = { Result.success(Unit) },
            onFailure = { err ->
                val msg = err.message.orEmpty()
                if (msg.contains("HTTP 401") || msg.contains("HTTP 403")) {
                    Result.success(Unit)
                } else {
                    Result.failure(err)
                }
            }
        )
    }

    private fun authEndpoint(path: String): String = "${config.url.trimEnd('/')}/auth/v1/$path"

    private fun headers(): Map<String, String> = mapOf(
        "apikey" to config.anonKey,
        "Authorization" to "Bearer ${config.anonKey}",
        "Accept" to "application/json"
    )

    private fun String.escapeJson(): String = this
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
}
