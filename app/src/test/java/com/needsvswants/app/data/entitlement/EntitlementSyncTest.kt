package com.needsvswants.app.data.entitlement

import android.content.Context
import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.auth.AuthSessionStore
import com.needsvswants.app.data.auth.GoogleIdTokenProvider
import com.needsvswants.app.data.auth.GoogleIdTokenResult
import com.needsvswants.app.data.remote.AuthSession
import com.needsvswants.app.data.remote.SupabaseAuth
import com.needsvswants.app.data.remote.SupabaseConfig
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pins the pure retry-decision logic of [EntitlementSync] without real delays:
 * the schedule, max attempts, and early stop once Pro access is confirmed —
 * plus the retry routine's concurrency contract (in-flight dedup) and its
 * durable-flag clearing on success.
 */
class EntitlementSyncTest {

    @Test
    fun checkoutRetryDelays_areImmediateThen2s5s10s() {
        assertEquals(
            listOf(0L, 2_000L, 5_000L, 10_000L),
            EntitlementSync.checkoutRetryDelaysMillis
        )
    }

    @Test
    fun shouldStop_keepsRetryingWhileFree_andAttemptsRemain() {
        // 4 scheduled attempts (indices 0..3): the first three done-and-free
        // still allow the next retry.
        assertFalse(EntitlementSync.shouldStop(0, hasProAccess = false))
        assertFalse(EntitlementSync.shouldStop(1, hasProAccess = false))
        assertFalse(EntitlementSync.shouldStop(2, hasProAccess = false))
    }

    @Test
    fun shouldStop_stopsAfterLastAttempt() {
        // Last scheduled attempt (index 3) always ends the loop — max attempts.
        assertTrue(EntitlementSync.shouldStop(3, hasProAccess = false))
    }

    @Test
    fun shouldStop_stopsEarlyOnceProConfirmed() {
        // Stop-on-pro at every index, including the first immediate attempt.
        for (index in EntitlementSync.checkoutRetryDelaysMillis.indices) {
            assertTrue("index $index should stop on pro", EntitlementSync.shouldStop(index, hasProAccess = true))
        }
    }

    @Test
    fun shouldStop_outOfRangeIndexStops() {
        assertTrue(EntitlementSync.shouldStop(4, hasProAccess = false))
        assertEquals(4, EntitlementSync.checkoutRetryDelaysMillis.size)
    }

    @Test
    fun syncAfterCheckoutReturn_concurrentSecondCall_returnsWithoutSecondRoutine() = runTest {
        // The warm deep-link return fires two entry points (MainActivity handler
        // + paywall RESUMED path). Only the FIRST routine runs — but the
        // deduped second caller must still receive the shared outcome on its
        // OWN callback (no lost onResult, no stranded caller).
        val gate = CompletableDeferred<Unit>()
        val remote = GatedRemote(gate)
        val store = FakePayPalReturnStore()
        val sync = buildSync(remote, store)

        val first = async { sync.syncAfterCheckoutReturn() }
        runCurrent()
        // First routine in flight: its first refresh is suspended on the gate.
        assertEquals(1, remote.fetchCalls)

        var forwarded: Boolean? = null
        val second = async { sync.syncAfterCheckoutReturn { forwarded = it } }
        runCurrent()
        // The second caller is deduped (no second refresh) and awaits the
        // winner's shared outcome instead of returning immediately.
        assertFalse(second.isCompleted)
        assertEquals(1, remote.fetchCalls)

        gate.complete(Unit)
        advanceUntilIdle()

        val firstResult = first.await()
        val secondResult = second.await()
        assertFalse(firstResult)
        // The losing caller observed the same outcome on its own callback.
        assertFalse(secondResult)
        assertEquals(firstResult, forwarded)
        // Only the first routine's own 4 scheduled attempts ran.
        assertEquals(4, remote.fetchCalls)
    }

    @Test
    fun syncAfterCheckoutReturn_confirmedPro_clearsPendingFlag() = runTest {
        val store = FakePayPalReturnStore().apply { setPaypalReturnPending(true) }
        val remote = FixedRemote(proEntitlement)
        val sync = buildSync(remote, store)

        val result = sync.syncAfterCheckoutReturn()

        // Pro confirmed during the retry window: the durable pending flag is
        // cleared so no later cold start re-runs the loop.
        assertTrue(result)
        assertFalse(store.paypalReturnPending.first())
    }

    @Test
    fun syncAfterCheckoutReturn_exhaustedFree_keepsPendingFlag() = runTest {
        val store = FakePayPalReturnStore().apply { setPaypalReturnPending(true) }
        val remote = FixedRemote(null)
        val sync = buildSync(remote, store)

        val result = sync.syncAfterCheckoutReturn()

        // Every attempt stayed free: the durable flag must survive so the next
        // cold start (or an explicit Restore) can self-heal.
        assertFalse(result)
        assertTrue(store.paypalReturnPending.first())
    }

    // --- fakes -----------------------------------------------------------

    private fun buildSync(remote: EntitlementRemote, store: PayPalReturnStore): EntitlementSync {
        val local = FakeLocal()
        val entitlements = EntitlementRepository(local, remote)
        val auth = AuthRepository(
            auth = NoopSupabaseAuth,
            store = FakeSessionStore(AuthSession("at", "rt", "u1", "user@example.com", null)),
            google = NoopGoogle,
            entitlements = entitlements,
            payPalReturn = store,
            config = ENABLED_CONFIG
        )
        return EntitlementSync(auth, entitlements, store, ENABLED_CONFIG)
    }

    private class FakeLocal(initial: Entitlement = Entitlement()) : EntitlementLocalStore {
        private val state = MutableStateFlow(initial)
        override val entitlement: Flow<Entitlement> = state
        override suspend fun setEntitlement(entitlement: Entitlement) {
            state.value = entitlement
        }
        override suspend fun clearEntitlement() {
            state.value = Entitlement()
        }
    }

    /** Returns a fixed entitlement (or null = remote has no grant) on every call. */
    private class FixedRemote(private val result: Entitlement?) : EntitlementRemote {
        var fetchCalls = 0
            private set
        override suspend fun fetchEntitlement(accessToken: String?): Entitlement? {
            fetchCalls++
            return result
        }
    }

    /** Suspends each fetch on [gate] until released; reports free (no grant). */
    private class GatedRemote(private val gate: CompletableDeferred<Unit>) : EntitlementRemote {
        var fetchCalls = 0
            private set
        override suspend fun fetchEntitlement(accessToken: String?): Entitlement? {
            fetchCalls++
            gate.await()
            return null
        }
    }

    private class FakeSessionStore(initial: AuthSession?) : AuthSessionStore {
        private val state = MutableStateFlow(initial)
        override val session: Flow<AuthSession?> = state
        override suspend fun save(session: AuthSession) {
            state.value = session
        }
        override suspend fun clear() {
            state.value = null
        }
    }

    private class FakePayPalReturnStore : PayPalReturnStore {
        private val state = MutableStateFlow(false)
        override val paypalReturnPending: Flow<Boolean> = state
        override suspend fun setPaypalReturnPending(pending: Boolean) {
            state.value = pending
        }
        override suspend fun clearPaypalReturnPending() {
            state.value = false
        }
    }

    private object NoopGoogle : GoogleIdTokenProvider {
        override val isAvailable: Boolean = false
        override suspend fun requestIdToken(activityContext: Context): Result<GoogleIdTokenResult> =
            Result.failure(IllegalStateException("noop"))
    }

    private object NoopSupabaseAuth : SupabaseAuth {
        override val isConfigured: Boolean = false
        override suspend fun sendMagicLink(email: String): Result<Unit> =
            Result.failure(IllegalStateException("noop"))
        override suspend fun verifyOtp(email: String, token: String): Result<String> =
            Result.failure(IllegalStateException("noop"))
        override suspend fun signInWithGoogleIdToken(idToken: String, nonce: String?): Result<AuthSession> =
            Result.failure(IllegalStateException("noop"))
        override suspend fun refreshSession(refreshToken: String): Result<AuthSession> =
            Result.failure(IllegalStateException("noop"))
        override suspend fun signOut(accessToken: String): Result<Unit> = Result.success(Unit)
    }

    companion object {
        private val ENABLED_CONFIG = SupabaseConfig(
            url = "https://supabase.example",
            anonKey = "anon-key",
            proTrialProductId = "trial",
            proMonthlyProductId = "pro_monthly",
            maxMonthlyProductId = "max_monthly"
        )
        private val proEntitlement = Entitlement(
            tier = EntitlementTier.PRO,
            type = EntitlementType.PAID,
            expiresAtEpochMillis = null
        )
    }
}
