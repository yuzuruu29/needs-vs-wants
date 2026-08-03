package com.needsvswants.app.data.remote

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SupabaseJsonSessionTest {

    @Test
    fun parseAuthSession_readsTokensAndUser() {
        val json = """
            {
              "access_token":"at-1",
              "refresh_token":"rt-1",
              "expires_in":3600,
              "token_type":"bearer",
              "user":{"id":"uid-9","email":"a@b.com"}
            }
        """.trimIndent()
        val s = SupabaseJson.parseAuthSession(json, nowEpochMillis = 1_000_000L)!!
        assertEquals("at-1", s.accessToken)
        assertEquals("rt-1", s.refreshToken)
        assertEquals("a@b.com", s.email)
        assertEquals("uid-9", s.userId)
        assertEquals(1_000_000L + 3600_000L, s.expiresAtEpochMillis)
    }

    @Test
    fun parseAuthSession_missingAccessToken_returnsNull() {
        assertNull(SupabaseJson.parseAuthSession("""{"refresh_token":"x"}""", 0L))
    }

    @Test
    fun authSession_isExpired_respectsSkew() {
        val s = AuthSession("at", null, null, null, expiresAtEpochMillis = 1_000_000L)
        // At exact expiry (no skew) → expired
        assertTrue(s.isExpired(nowEpochMillis = 1_000_000L, skewMillis = 0L))
        // Within 60s skew window before expiry → treated as expired
        assertTrue(s.isExpired(nowEpochMillis = 950_000L, skewMillis = 60_000L))
        // Well before expiry (outside skew) → not expired
        assertTrue(!s.isExpired(nowEpochMillis = 100_000L, skewMillis = 60_000L))
        // Null expiry → never expired by clock
        val lifetime = AuthSession("at", null, null, null, expiresAtEpochMillis = null)
        assertTrue(!lifetime.isExpired(nowEpochMillis = Long.MAX_VALUE))
    }
}
