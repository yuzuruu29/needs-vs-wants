package com.needsvswants.app.diagnostics

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CrashReportingTest {

    @Test
    fun `enabled only for opted-in release builds with a dsn`() {
        assertTrue(CrashReporting.shouldEnable(userOptedIn = true, isDebugBuild = false, dsn = "https://k@sentry.io/1"))
    }

    @Test
    fun `user opt-out wins`() {
        assertFalse(CrashReporting.shouldEnable(userOptedIn = false, isDebugBuild = false, dsn = "https://k@sentry.io/1"))
    }

    @Test
    fun `debug builds never report`() {
        assertFalse(CrashReporting.shouldEnable(userOptedIn = true, isDebugBuild = true, dsn = "https://k@sentry.io/1"))
    }

    @Test
    fun `blank dsn keeps the sdk off`() {
        assertFalse(CrashReporting.shouldEnable(userOptedIn = true, isDebugBuild = false, dsn = ""))
        assertFalse(CrashReporting.shouldEnable(userOptedIn = true, isDebugBuild = false, dsn = "   "))
    }
}
