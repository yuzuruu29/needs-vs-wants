package com.needsvswants.app.data.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupJsonTest {

    @Test
    fun `parses nested objects arrays and primitives`() {
        val parsed = BackupJson.parse(
            """{"a":1,"b":-2.5,"c":true,"d":null,"e":[1,2],"f":{"g":"h"}}"""
        ) as Map<*, *>
        assertEquals(1L, parsed["a"])
        assertEquals(-2.5, parsed["b"])
        assertEquals(true, parsed["c"])
        assertNull(parsed["d"])
        assertEquals(listOf(1L, 2L), parsed["e"])
        assertEquals(mapOf("g" to "h"), parsed["f"])
    }

    @Test
    fun `parses string escapes including unicode`() {
        val parsed = BackupJson.parse("""{"s":"line\nquote\" peso \u20b1 slash\/"}""") as Map<*, *>
        assertEquals("line\nquote\" peso ₱ slash/", parsed["s"])
    }

    @Test
    fun `escape and parse round-trip preserves content`() {
        val original = "Kape ₱150 \"promo\"\nsecond line\ttabbed \\ backslash"
        val json = "{\"v\":\"${BackupJson.escape(original)}\"}"
        val parsed = BackupJson.parse(json) as Map<*, *>
        assertEquals(original, parsed["v"])
    }

    @Test
    fun `rejects trailing garbage`() {
        val error = runCatching { BackupJson.parse("""{"a":1} extra""") }.exceptionOrNull()
        assertTrue(error is IllegalArgumentException)
    }

    @Test
    fun `rejects unterminated string`() {
        val error = runCatching { BackupJson.parse("""{"a":"unclosed}""") }.exceptionOrNull()
        assertTrue(error is IllegalArgumentException)
    }

    @Test
    fun `parses empty object and array`() {
        assertEquals(emptyMap<String, Any?>(), BackupJson.parse("{}"))
        assertEquals(emptyList<Any?>(), BackupJson.parse("[]"))
    }
}
