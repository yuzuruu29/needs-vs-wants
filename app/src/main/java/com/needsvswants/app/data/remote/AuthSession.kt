package com.needsvswants.app.data.remote

/**
 * Supabase Auth session obtained via Google ID token (or OTP) exchange.
 * Pure data — no Android types.
 */
data class AuthSession(
    val accessToken: String,
    val refreshToken: String?,
    val userId: String?,
    val email: String?,
    val expiresAtEpochMillis: Long?
) {
    /**
     * Returns true when the access token should be treated as expired.
     * [skewMillis] refreshes slightly early to avoid edge-of-expiry failures.
     * When [expiresAtEpochMillis] is null, the session is treated as non-expiring
     * (caller should still re-auth on 401).
     */
    fun isExpired(nowEpochMillis: Long, skewMillis: Long = 60_000L): Boolean {
        val exp = expiresAtEpochMillis ?: return false
        return nowEpochMillis >= exp - skewMillis
    }
}
