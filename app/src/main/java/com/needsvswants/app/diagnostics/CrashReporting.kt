package com.needsvswants.app.diagnostics

import android.content.Context
import com.needsvswants.app.BuildConfig
import io.sentry.Sentry
import io.sentry.android.core.SentryAndroid

/**
 * Central switch for crash reporting (Sentry), privacy-lean by design:
 *
 *  - Release builds only, and only when a DSN is configured (SENTRY_DSN in
 *    local.properties → BuildConfig). A blank DSN keeps the SDK fully off.
 *  - Respects the user's "Send crash reports" toggle in Settings.
 *  - No PII: `sendDefaultPii` off, no screenshots, no view hierarchy, no
 *    performance tracing, no user-interaction breadcrumbs. Ledger contents
 *    never leave the device — only stack traces, device model and OS version.
 *
 * Manual init only: the manifest sets `io.sentry.auto-init=false`, so nothing
 * starts unless [applyState] decides it should.
 */
object CrashReporting {

    /** Pure decision gate (unit-tested in CrashReportingTest). */
    fun shouldEnable(userOptedIn: Boolean, isDebugBuild: Boolean, dsn: String): Boolean =
        userOptedIn && !isDebugBuild && dsn.isNotBlank()

    /** Idempotently starts or stops the SDK to match the desired state. */
    fun applyState(context: Context, userOptedIn: Boolean) {
        val enable = shouldEnable(userOptedIn, BuildConfig.DEBUG, BuildConfig.SENTRY_DSN)
        if (enable) {
            if (Sentry.isEnabled()) return
            SentryAndroid.init(context) { options ->
                options.dsn = BuildConfig.SENTRY_DSN
                options.release =
                    "com.needsvswants.app@${BuildConfig.VERSION_NAME}+${BuildConfig.VERSION_CODE}"
                options.environment = if (BuildConfig.PLAIN_FREE) "plain" else "production"
                options.isSendDefaultPii = false
                options.isAttachScreenshot = false
                options.isAttachViewHierarchy = false
                options.tracesSampleRate = null
                options.isEnableUserInteractionBreadcrumbs = false
            }
        } else if (Sentry.isEnabled()) {
            Sentry.close()
        }
    }
}
