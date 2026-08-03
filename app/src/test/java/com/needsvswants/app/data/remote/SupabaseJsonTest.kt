package com.needsvswants.app.data.remote

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SupabaseJsonTest {

    @Test
    fun parseAccessToken_extractsToken() {
        val json = """{"access_token":"eyJhbGciOi.jwt","refresh_token":"r1","token_type":"bearer"}"""
        assertEquals("eyJhbGciOi.jwt", SupabaseJson.parseAccessToken(json))
    }

    @Test
    fun parseAccessToken_missingField_returnsNull() {
        val json = """{"refresh_token":"r1","token_type":"bearer"}"""
        assertNull(SupabaseJson.parseAccessToken(json))
    }

    @Test
    fun parseAccessToken_blankToken_returnsNull() {
        val json = """{"access_token":""}"""
        assertNull(SupabaseJson.parseAccessToken(json))
    }

    @Test
    fun parseAccessToken_invalidJson_returnsNull() {
        assertNull(SupabaseJson.parseAccessToken("not-json"))
    }
}
