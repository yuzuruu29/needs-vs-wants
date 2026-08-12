package com.needsvswants.app.data.update

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateCheckerTest {

    @Test
    fun `parses a valid version json`() {
        val update = UpdateChecker.parseVersionJson(
            """{"versionName":"2.1.0","versionCode":23,
               "apkUrl":"https://needs-vs-wants.vercel.app/downloads/needs-vs-wants-2.1.0.apk",
               "notes":"Backup + updates"}"""
        )
        assertEquals("2.1.0", update!!.versionName)
        assertEquals(23, update.versionCode)
        assertTrue(update.apkUrl.endsWith("2.1.0.apk"))
    }

    @Test
    fun `returns null when any field is missing`() {
        assertNull(UpdateChecker.parseVersionJson("""{"versionCode":23,"apkUrl":"https://x/a.apk"}"""))
        assertNull(UpdateChecker.parseVersionJson("""{"versionName":"2.1.0","apkUrl":"https://x/a.apk"}"""))
        assertNull(UpdateChecker.parseVersionJson("""{"versionName":"2.1.0","versionCode":23}"""))
        assertNull(UpdateChecker.parseVersionJson("not json at all"))
    }

    @Test
    fun `rejects non-https apk url`() {
        assertNull(
            UpdateChecker.parseVersionJson(
                """{"versionName":"2.1.0","versionCode":23,"apkUrl":"http://insecure/a.apk"}"""
            )
        )
    }

    @Test
    fun `throttle allows first check and blocks within interval`() {
        // Realistic clock: "never checked" (0) is always older than the interval.
        val now = 1_765_584_000_000L
        assertTrue(UpdateChecker.shouldCheck(lastCheckAt = 0L, nowMillis = now))
        assertFalse(UpdateChecker.shouldCheck(now - 1, now))
        assertFalse(UpdateChecker.shouldCheck(now - UpdateChecker.CHECK_INTERVAL_MS + 1, now))
        assertTrue(UpdateChecker.shouldCheck(now - UpdateChecker.CHECK_INTERVAL_MS, now))
    }
}
