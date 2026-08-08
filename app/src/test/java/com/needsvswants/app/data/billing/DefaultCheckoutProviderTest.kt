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
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Availability + resolution behavior of the [CheckoutProvider] seam over the
 * two live web-checkout controllers.
 */
class DefaultCheckoutProviderTest {

    private fun provider(
        config: SupabaseConfig = enabledConfig(),
        payPal: PayPalBillingController? = null,
        payMongo: PayMongoBillingController? = null
    ): DefaultCheckoutProvider {
        val entitlements = EntitlementRepository(FakeLocal(), FakeRemote())
        val auth = AuthRepository(
            auth = NoopSupabaseAuth,
            store = FakeSessionStore(null),
            google = NoopGoogle,
            entitlements = entitlements,
            payPalReturn = FakePayPalReturnStore(),
            config = config
        )
        return DefaultCheckoutProvider(
            payPal = payPal ?: PayPalBillingController(config, auth, entitlements),
            payMongo = payMongo ?: PayMongoBillingController(config, auth, entitlements),
            config = config
        )
    }

    private fun enabledConfig(pro: String = "P-pro-plan", max: String = "P-max-plan"): SupabaseConfig =
        SupabaseConfig(
            url = "https://supabase.example",
            anonKey = "anon-key",
            proTrialProductId = "",
            proMonthlyProductId = pro,
            maxMonthlyProductId = max
        )

    @Test
    fun payPalAvailable_true_whenEnabledAndPlanIdsConfigured() {
        assertTrue(provider(enabledConfig()).payPalAvailable)
    }

    @Test
    fun payPalAvailable_false_whenSupabaseDisabled() {
        assertFalse(provider(SupabaseConfig.Disabled).payPalAvailable)
    }

    @Test
    fun payPalAvailable_false_whenPlanIdsBlank() {
        assertFalse(provider(enabledConfig(pro = "", max = "")).payPalAvailable)
    }

    @Test
    fun payMongoAvailable_true_whenEnabledAndPlanIdsConfigured() {
        assertTrue(provider(enabledConfig()).payMongoAvailable)
    }

    @Test
    fun payMongoAvailable_false_whenSupabaseDisabled() {
        assertFalse(provider(SupabaseConfig.Disabled).payMongoAvailable)
    }

    @Test
    fun payMongoAvailable_false_whenPlanIdsBlank() {
        assertFalse(provider(enabledConfig(pro = "", max = "")).payMongoAvailable)
    }

    @Test
    fun controllerFor_payPal_returnsPayPalController() {
        val payPal = PayPalBillingController(
            enabledConfig(),
            authFor(SupabaseConfig.Disabled),
            EntitlementRepository(FakeLocal(), FakeRemote())
        )
        val payMongo = PayMongoBillingController(
            enabledConfig(),
            authFor(SupabaseConfig.Disabled),
            EntitlementRepository(FakeLocal(), FakeRemote())
        )
        val seam = provider(payPal = payPal, payMongo = payMongo)

        assertSame(payPal, seam.controllerFor(PaymentProvider.PAYPAL))
    }

    @Test
    fun controllerFor_payMongo_returnsPayMongoController() {
        val payPal = PayPalBillingController(
            enabledConfig(),
            authFor(SupabaseConfig.Disabled),
            EntitlementRepository(FakeLocal(), FakeRemote())
        )
        val payMongo = PayMongoBillingController(
            enabledConfig(),
            authFor(SupabaseConfig.Disabled),
            EntitlementRepository(FakeLocal(), FakeRemote())
        )
        val seam = provider(payPal = payPal, payMongo = payMongo)

        assertSame(payMongo, seam.controllerFor(PaymentProvider.PAYMONGO))
    }

    @Test
    fun controllerFor_returnsDistinctLiveControllers() {
        val seam = provider(enabledConfig())
        // Both providers resolve to distinct live controllers; neither is the
        // other, and both are available when configured.
        assertTrue(seam.controllerFor(PaymentProvider.PAYPAL) is PayPalBillingController)
        assertTrue(seam.controllerFor(PaymentProvider.PAYMONGO) is PayMongoBillingController)
        assertTrue(seam.controllerFor(PaymentProvider.PAYPAL) !== seam.controllerFor(PaymentProvider.PAYMONGO))
    }

    private fun authFor(config: SupabaseConfig): AuthRepository = AuthRepository(
        auth = NoopSupabaseAuth,
        store = FakeSessionStore(null),
        google = NoopGoogle,
        entitlements = EntitlementRepository(FakeLocal(), FakeRemote()),
        payPalReturn = FakePayPalReturnStore(),
        config = config
    )

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

    private class FakeLocal : EntitlementLocalStore {
        private val state = MutableStateFlow(Entitlement())
        private val synced = MutableStateFlow(0L)
        override val entitlement: Flow<Entitlement> = state
        override val entitlementSyncedAtMillis: Flow<Long> = synced
        override suspend fun setEntitlement(entitlement: Entitlement) {
            state.value = entitlement
        }
        override suspend fun markEntitlementSynced(atMillis: Long) {
            synced.value = atMillis
        }
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
}
