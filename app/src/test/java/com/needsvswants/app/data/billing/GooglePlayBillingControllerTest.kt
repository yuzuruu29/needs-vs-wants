package com.needsvswants.app.data.billing

import android.content.Context
import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.auth.AuthSessionStore
import com.needsvswants.app.data.auth.GoogleIdTokenProvider
import com.needsvswants.app.data.auth.GoogleIdTokenResult
import com.needsvswants.app.data.entitlement.EntitlementLocalStore
import com.needsvswants.app.data.entitlement.EntitlementRemote
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.entitlement.PayPalReturnStore
import com.needsvswants.app.data.remote.AuthSession
import com.needsvswants.app.data.remote.SupabaseAuth
import com.needsvswants.app.data.remote.SupabaseConfig
import com.needsvswants.app.domain.Entitlement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Unit tests for [GooglePlayBillingController] initialization, product IDs, and configuration.
 */
class GooglePlayBillingControllerTest {

    private fun controller(
        config: SupabaseConfig = enabledConfig()
    ): GooglePlayBillingController {
        val entitlements = EntitlementRepository(FakeLocal(), FakeRemote())
        val auth = AuthRepository(
            auth = NoopSupabaseAuth,
            store = FakeSessionStore(null),
            google = NoopGoogle,
            entitlements = entitlements,
            payPalReturn = FakePayPalReturnStore(),
            config = config
        )
        return GooglePlayBillingController(
            context = android.content.ContextWrapper(null),
            config = config,
            auth = auth,
            entitlements = entitlements
        )
    }

    private fun enabledConfig(): SupabaseConfig =
        SupabaseConfig(
            url = "https://supabase.example",
            anonKey = "anon-key",
            proTrialProductId = "",
            proMonthlyProductId = "pro_monthly",
            maxMonthlyProductId = "max_monthly"
        )

    @Test
    fun isPayPalAvailable_alwaysFalse() {
        val c = controller()
        assertFalse(c.isPayPalAvailable)
    }

    @Test
    fun defaultProductIds_areConfigured() {
        val c = controller()
        assertNotNull(c.proProductId)
        assertNotNull(c.maxProductId)
        assertEquals("needsvswants_pro", c.proProductId)
        assertEquals("needsvswants_max", c.maxProductId)
    }

    private class FakeSessionStore(initial: AuthSession?) : AuthSessionStore {
        private val state = MutableStateFlow(initial)
        override val session: Flow<AuthSession?> = state
        override suspend fun save(session: AuthSession) { state.value = session }
        override suspend fun clear() { state.value = null }
    }

    private class FakeLocal : EntitlementLocalStore {
        private val state = MutableStateFlow(Entitlement())
        private val synced = MutableStateFlow(0L)
        override val entitlement: Flow<Entitlement> = state
        override val entitlementSyncedAtMillis: Flow<Long> = synced
        override suspend fun setEntitlement(entitlement: Entitlement) { state.value = entitlement }
        override suspend fun markEntitlementSynced(atMillis: Long) { synced.value = atMillis }
        override suspend fun clearEntitlement() {
            state.value = Entitlement()
            synced.value = 0L
        }
    }

    private class FakeRemote : EntitlementRemote {
        override suspend fun fetchEntitlement(accessToken: String?): Entitlement? = null
    }

    private class FakePayPalReturnStore : PayPalReturnStore {
        private val state = MutableStateFlow(false)
        override val paypalReturnPending: Flow<Boolean> = state
        override suspend fun setPaypalReturnPending(pending: Boolean) { state.value = pending }
        override suspend fun clearPaypalReturnPending() { state.value = false }
    }

    private object NoopGoogle : GoogleIdTokenProvider {
        override val isAvailable: Boolean = false
        override suspend fun requestIdToken(activityContext: Context): Result<GoogleIdTokenResult> =
            Result.failure(IllegalStateException("noop"))
    }

    private object NoopSupabaseAuth : SupabaseAuth {
        override val isConfigured: Boolean = false
        override suspend fun sendMagicLink(email: String): Result<Unit> = Result.failure(IllegalStateException("noop"))
        override suspend fun verifyOtp(email: String, token: String): Result<AuthSession> = Result.failure(IllegalStateException("noop"))
        override suspend fun signInWithGoogleIdToken(idToken: String, nonce: String?): Result<AuthSession> = Result.failure(IllegalStateException("noop"))
        override suspend fun refreshSession(refreshToken: String): Result<AuthSession> = Result.failure(IllegalStateException("noop"))
        override suspend fun signOut(accessToken: String): Result<Unit> = Result.success(Unit)
    }
}
