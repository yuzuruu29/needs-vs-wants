package com.needsvswants.app.data.entitlement

import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.remote.SupabaseConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.yield
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
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
    private val preferences: PayPalReturnStore,
    private val config: SupabaseConfig
) : CheckoutReturnSync {
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
         * Cooperative per-attempt cap for a single refresh inside the retry
         * routine, matching [com.needsvswants.app.data.remote.HttpJsonClient]'s
         * worst case (10s connect + 10s read). Each attempt is individually
         * bounded, so one hung request can never stall the whole schedule.
         */
        const val ATTEMPT_TIMEOUT_MILLIS = 20_000L

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
     * In-flight guard so concurrent entry points collapse into a single
     * routine; the owner publishes its outcome on [retryOutcome] so deduped
     * callers can await and forward it.
     */
    private val retryInFlight = AtomicBoolean(false)

    /**
     * Shared outcome of the routine that owns [retryInFlight], published right
     * after claiming so a deduped concurrent caller can await the final result
     * and forward it to its own callback. Replaced when a new routine claims;
     * a completed deferred is left in place until then so a late loser can
     * still capture the outcome it was deduped against.
     */
    private val retryOutcome = AtomicReference<CompletableDeferred<Boolean>?>(null)

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
            // Guard: if the user signed out while this fetch was in flight, the
            // snapshot written above belongs to an account that is no longer on
            // this device — clear it again so a stale Pro write can never
            // survive sign-out (see AuthRepository.signOut).
            if (!auth.isSignedIn.first()) {
                entitlements.clearLocal()
                return false
            }
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
     * Each attempt is bounded by [ATTEMPT_TIMEOUT_MILLIS]; worst case when Pro
     * never confirms is 4 × 20s + 17s of delays ≈ 97s (a cold-start caller is
     * still capped overall by [COLD_START_SYNC_TIMEOUT_MILLIS]). Concurrent
     * callers collapse into one routine: the first wins the claim, deduped
     * callers await its shared outcome and forward it to their own [onResult]
     * before returning — every caller observes the final result, so no
     * caller's callback is ever lost (e.g. the paywall's Syncing strip
     * resolves even when the deep-link handler's routine won the claim).
     *
     * @return true when Pro access was confirmed during the retry window.
     */
    override suspend fun syncAfterCheckoutReturn(onResult: (Boolean) -> Unit): Boolean {
        val outcome = CompletableDeferred<Boolean>()
        if (!retryInFlight.compareAndSet(false, true)) {
            // Deduped: another routine owns the retry window. Await its shared
            // outcome and forward it to this caller's OWN callback — the
            // per-caller contract holds even for the losing caller.
            val result = awaitSharedOutcome()
            onResult(result)
            return result
        }
        retryOutcome.set(outcome)
        var confirmed = false
        try {
            if (config.enabled) {
                checkoutRetryDelaysMillis.forEachIndexed { index, delayMillis ->
                    if (delayMillis > 0L) delay(delayMillis)
                    confirmed = withTimeoutOrNull(ATTEMPT_TIMEOUT_MILLIS) { refreshOnce() } ?: false
                    if (shouldStop(index, confirmed)) return@forEachIndexed
                }
                if (confirmed) {
                    runCatching { preferences.setPaypalReturnPending(false) }
                }
            } else {
                // No remote to consult — a grant cannot arrive; do not spin the delays.
                confirmed = false
            }
            onResult(confirmed)
            return confirmed
        } finally {
            // Always release waiting callers — even when this routine is
            // cancelled mid-flight — so no deduped caller can strand.
            outcome.complete(confirmed)
            retryInFlight.set(false)
        }
    }

    /**
     * Awaits the outcome of the in-flight routine. In practice the owner
     * publishes [retryOutcome] before its first suspension, so the retry loop
     * only covers the tiny publish window when a caller lands on another
     * thread between the owner's claim and its write.
     */
    private suspend fun awaitSharedOutcome(): Boolean {
        while (true) {
            val shared = retryOutcome.get()
            if (shared != null) return shared.await()
            if (!retryInFlight.get()) return false
            yield()
        }
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
