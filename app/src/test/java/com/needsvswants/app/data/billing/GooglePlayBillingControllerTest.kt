package com.needsvswants.app.data.billing

import android.content.Context
import com.android.billingclient.api.BillingFlowParams
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
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [GooglePlayBillingController] initialization, product IDs,
 * configuration, subscription replacement parameters (BUG 1), and the honest
 * restore pipeline outcomes (BUG 2).
 *
 * JVM-safety note: the Play builders exercised here are stub-free on the JVM;
 * anything touching android.jar stubs (e.g. ProductDetailsParams.setOfferToken)
 * is intentionally NOT constructed in these tests.
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

    // --- BUG 1: subscription replacement parameters ---

    @Test
    fun subscriptionReplacement_absentWithoutActiveSubscription() {
        val c = controller()
        assertNull(c.subscriptionReplacementFor(null))
    }

    @Test
    fun subscriptionReplacement_presentWhenActiveSubscriptionExists() {
        val c = controller()
        val spec = c.subscriptionReplacementFor(
            ActivePlaySubscription(productId = "needsvswants_pro", purchaseToken = "old-token")
        )
        assertNotNull(spec)
        assertEquals("old-token", spec!!.oldPurchaseToken)
        assertEquals("needsvswants_pro", spec.oldProductId)
        assertEquals(
            BillingFlowParams.ProductDetailsParams.SubscriptionProductReplacementParams
                .ReplacementMode.CHARGE_PRORATED_PRICE,
            spec.replacementMode
        )

        val params = c.productReplacementParams(spec)
        assertEquals("needsvswants_pro", params.oldProductId)
        assertEquals(spec.replacementMode, params.replacementMode)
    }

    // --- BUG 2: honest restore pipeline ---

    @Test
    fun restorePipeline_zeroPurchases_isNoneFoundNotSuccess() = runTest {
        val c = controller()
        val outcome = c.processRestoredPurchases(
            purchases = emptyList(),
            verify = { true },
            acknowledge = { _, _ -> true }
        )
        assertTrue(outcome is RestoreOutcome.NoneFound)

        // None-found is explicitly NOT reported as success.
        val mapped = outcome.toBillingResult()
        assertTrue(mapped is BillingResult.Failed)
        assertEquals(
            "No active Google Play subscriptions found.",
            (mapped as BillingResult.Failed).reason
        )
    }

    @Test
    fun restorePipeline_verificationFails_isErrorNotSuccess() = runTest {
        val c = controller()
        val outcome = c.processRestoredPurchases(
            purchases = listOf(RestorablePurchase("tok-1", listOf("needsvswants_pro"), false)),
            verify = { false },
            acknowledge = { _, _ -> true }
        )
        assertTrue(outcome is RestoreOutcome.Error)
        assertTrue(outcome.toBillingResult() is BillingResult.Failed)
    }

    @Test
    fun restorePipeline_ackFailure_isSurfacedNotSwallowed() = runTest {
        val c = controller()
        val outcome = c.processRestoredPurchases(
            purchases = listOf(RestorablePurchase("tok-1", listOf("needsvswants_pro"), false)),
            verify = { true },
            acknowledge = { _, _ -> false }
        )
        assertTrue(outcome is RestoreOutcome.Restored)
        val restored = outcome as RestoreOutcome.Restored
        assertEquals(1, restored.verifiedCount)
        assertEquals(listOf("tok-1"), restored.ackFailedTokens)
    }

    @Test
    fun restorePipeline_verifiedPurchases_mapToSuccess() = runTest {
        val c = controller()
        val outcome = c.processRestoredPurchases(
            purchases = listOf(
                RestorablePurchase("tok-1", listOf("needsvswants_pro"), true),
                RestorablePurchase("tok-2", listOf("needsvswants_max"), false)
            ),
            verify = { true },
            acknowledge = { _, _ -> true }
        )
        assertTrue(outcome is RestoreOutcome.Restored)
        val restored = outcome as RestoreOutcome.Restored
        assertEquals(2, restored.verifiedCount)
        assertTrue(restored.ackFailedTokens.isEmpty())
        assertTrue(outcome.toBillingResult() is BillingResult.Success)
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
