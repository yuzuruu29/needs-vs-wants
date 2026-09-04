package com.needsvswants.app.data.update

import com.needsvswants.app.data.prefs.AvailableUpdate
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

    @Test
    fun `user tap does not claim latest when the check failed`() {
        assertEquals(
            "Couldn't check right now. Try again.",
            UpdateChecker.feedbackAfterCheck(succeeded = false, available = null)
        )
    }

    @Test
    fun `user tap stays quiet when an update row will show`() {
        val update = AvailableUpdate("2.0.30", 38, "https://needs-vs-wants.vercel.app/downloads/a.apk")
        assertNull(UpdateChecker.feedbackAfterCheck(succeeded = true, available = update))
    }

    @Test
    fun `user tap says latest only after a successful empty check`() {
        assertEquals(
            "You're on the latest version.",
            UpdateChecker.feedbackAfterCheck(succeeded = true, available = null)
        )
    }
}
