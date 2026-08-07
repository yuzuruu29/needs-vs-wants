package com.needsvswants.app.data.entitlement

import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.remote.SupabaseConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Durable entitlement sync for the "Pro feels free after PayPal checkout" path.
 *
 * Two entry points:
 * - [refreshOnce] — single best-effort refresh (app cold start when no checkout
 *   return is pending).
 * - [syncAfterCheckoutReturn] — retried refresh after a PayPal return deep link
 *   (`needsvswants://paypal/return`), stopping as soon as Pro access is
 *   confirmed and clearing the durable pending-return flag on success.
 *
 * Every call is safe offline: with [SupabaseConfig.enabled] false or no session,
 * the local snapshot is kept and the call reports `false`. Network failures are
 * caught and reported as `false` — they can never crash app start.
 */
@Singleton
class EntitlementSync @Inject constructor(
    private val auth: AuthRepository,
    private val entitlements: EntitlementRepository,
    private val preferences: AppPreferences,
    private val config: SupabaseConfig
) {
    companion object {
        /**
         * Retry schedule after a PayPal checkout return, in milliseconds.
         * First attempt is immediate; later attempts give the PayPal webhook
         * time to land before the app gives up.
         */
        val checkoutRetryDelaysMillis: List<Long> = listOf(0L, 2_000L, 5_000L, 10_000L)

        /**
         * Cap on cold-start sync work. Generous enough for the full retry
         * schedule when a checkout return is pending, but still bounds app
         * launch so a hung network can never stall startup indefinitely.
         */
        const val COLD_START_SYNC_TIMEOUT_MILLIS = 30_000L

        /**
         * Decides whether the retry loop stops after attempt [retryIndex]:
         * true once Pro access is confirmed (stop early) or after the last
         * scheduled attempt (max attempts). Pure so unit tests can pin both
         * stop-on-pro and max-attempts without real delays.
         */
        fun shouldStop(retryIndex: Int, hasProAccess: Boolean): Boolean =
            hasProAccess || retryIndex >= checkoutRetryDelaysMillis.lastIndex
    }

    /**
     * Single best-effort refresh from the remote entitlement source.
     *
     * @return true when the caller has active Pro access right now (remote
     *   confirmed, or the local snapshot already says Pro and the refresh was
     *   a no-op offline); false when unconfigured, signed out, offline, or the
     *   remote reports free.
     */
    suspend fun refreshOnce(): Boolean {
        if (!config.enabled) return false
        return try {
            val token = auth.ensureFreshAccessToken() ?: return false
            entitlements.refreshFromRemote(token)
            entitlements.entitlement.first().hasProAccessAt(System.currentTimeMillis())
        } catch (t: CancellationException) {
            throw t
        } catch (t: Throwable) {
            // Best-effort only: a network/parse failure never throws to the caller.
            false
        }
    }

    /**
     * Retried refresh after a PayPal checkout return.
     *
     * Attempts follow [checkoutRetryDelaysMillis] and stop early once
     * [Entitlement.hasProAccessAt] is true; when Pro is confirmed the durable
     * pending-return flag is cleared. After the last attempt [onResult] fires
     * with the final outcome so the UI can show "Payment recorded — tap
     * Restore, or wait a moment" instead of silent free.
     *
     * @return true when Pro access was confirmed during the retry window.
     */
    suspend fun syncAfterCheckoutReturn(onResult: (Boolean) -> Unit = {}): Boolean {
        if (!config.enabled) {
            // No remote to consult — a grant cannot arrive; do not spin the delays.
            onResult(false)
            return false
        }
        var confirmed = false
        checkoutRetryDelaysMillis.forEachIndexed { index, delayMillis ->
            if (delayMillis > 0L) delay(delayMillis)
            confirmed = refreshOnce()
            if (shouldStop(index, confirmed)) return@forEachIndexed
        }
        if (confirmed) {
            runCatching { preferences.setPaypalReturnPending(false) }
        }
        onResult(confirmed)
        return confirmed
    }

    /**
     * Cold-start entry point, bounded by [COLD_START_SYNC_TIMEOUT_MILLIS] so
     * app launch never blocks on the network. Runs the full retry routine when
     * a PayPal return is still pending (a previous return was missed), a single
     * refresh otherwise.
     */
    suspend fun syncAtAppStart() {
        val pendingReturn = preferences.paypalReturnPending.first()
        withTimeoutOrNull(COLD_START_SYNC_TIMEOUT_MILLIS) {
            if (pendingReturn) {
                syncAfterCheckoutReturn()
            } else {
                refreshOnce()
            }
        }
    }
}
