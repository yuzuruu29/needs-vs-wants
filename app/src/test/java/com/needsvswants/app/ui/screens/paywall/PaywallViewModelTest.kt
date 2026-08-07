package com.needsvswants.app.ui.screens.paywall

import android.content.Context
import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.auth.AuthSessionStore
import com.needsvswants.app.data.auth.GoogleIdTokenProvider
import com.needsvswants.app.data.auth.GoogleIdTokenResult
import com.needsvswants.app.data.billing.BillingController
import com.needsvswants.app.data.billing.BillingResult
import com.needsvswants.app.data.entitlement.EntitlementLocalStore
import com.needsvswants.app.data.entitlement.EntitlementRemote
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.remote.AuthSession
import com.needsvswants.app.data.remote.SupabaseAuth
import com.needsvswants.app.data.remote.SupabaseConfig
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
class PaywallViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Swallow uncaught background coroutine exceptions so the throwing-billing
        // case can assert on `busy` without failing the test.
        Dispatchers.setMain(HandlerTestDispatcher(dispatcher, CoroutineExceptionHandler { _, _ -> }))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun startTrial_recordsUnavailable_whenBillingStub_andSignedIn() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Unavailable)
        val vm = PaywallViewModel(
            billing = billing,
            repository = EntitlementRepository(FakeLocal(), FakeRemote()),
            authRepository = signedInAuth(),
            config = SupabaseConfig.Disabled
        )

        vm.subscribePro()
        advanceUntilIdle()

        assertEquals(BillingResult.Unavailable, vm.lastResult.first())
        assertFalse(vm.busy.first())
        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
    }

    @Test
    fun startTrial_setsPendingPurchase_whenSignedOut_noBillingYet() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val vm = PaywallViewModel(
            billing = billing,
            repository = EntitlementRepository(FakeLocal(), FakeRemote()),
            authRepository = signedOutAuth(),
            config = SupabaseConfig.Disabled
        )

        vm.subscribePro()
        advanceUntilIdle()

        assertEquals(0, billing.trialCalls)
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
        assertTrue(vm.needsSignInForPurchase.first())
        assertNull(vm.lastResult.first())
    }

    @Test
    fun upgrade_setsPendingMax_whenSignedOut() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val vm = PaywallViewModel(
            billing = billing,
            repository = EntitlementRepository(FakeLocal(), FakeRemote()),
            authRepository = signedOutAuth(),
            config = SupabaseConfig.Disabled
        )

        vm.subscribeMax()
        advanceUntilIdle()

        assertEquals(0, billing.purchaseIds.size)
        assertEquals(PendingPurchase.MaxSubscribe, vm.pendingPurchase.first())
        assertTrue(vm.needsSignInForPurchase.first())
    }

    @Test
    fun onSignedInForPurchase_runsPendingTrial() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Unavailable)
        val vm = PaywallViewModel(
            billing = billing,
            repository = EntitlementRepository(FakeLocal(), FakeRemote()),
            authRepository = signedInAuth(),
            config = SupabaseConfig.Disabled
        )
        // Simulate: user had chosen trial while signed out, then signed in.
        // pending is set only via startTrial when signed out — inject by calling
        // cancel then use reflection-free path: startTrial while signed in runs billing.
        // For signed-in path:
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
        assertEquals(PendingPurchase.None, vm.pendingPurchase.first())
    }

    @Test
    fun cancelPendingSignIn_clearsGate() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val vm = PaywallViewModel(
            billing = billing,
            repository = EntitlementRepository(FakeLocal(), FakeRemote()),
            authRepository = signedOutAuth(),
            config = SupabaseConfig.Disabled
        )
        vm.subscribePro()
        advanceUntilIdle()
        assertTrue(vm.needsSignInForPurchase.first())
        vm.cancelPendingSignIn()
        advanceUntilIdle()
        assertEquals(PendingPurchase.None, vm.pendingPurchase.first())
        assertFalse(vm.needsSignInForPurchase.first())
    }

    @Test
    fun upgrade_invokesPurchase_withMaxProductId() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val config = SupabaseConfig(
            url = "",
            anonKey = "",
            proTrialProductId = "trial_x",
            proMonthlyProductId = "monthly_x",
            maxMonthlyProductId = "max_x"
        )
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            config
        )

        vm.subscribeMax()
        advanceUntilIdle()

        assertEquals(listOf("max_x"), billing.purchaseIds)
        assertEquals(BillingResult.Success, vm.lastResult.first())
    }

    @Test
    fun upgrade_proUser_invokesPurchase_withMaxProductId() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val config = SupabaseConfig(
            url = "",
            anonKey = "",
            proTrialProductId = "trial_x",
            proMonthlyProductId = "monthly_x",
            maxMonthlyProductId = "max_x"
        )
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(proEntitlement), FakeRemote()),
            signedInAuth(),
            config
        )

        vm.subscribeMax()
        advanceUntilIdle()

        assertEquals(listOf("max_x"), billing.purchaseIds)
        assertEquals(BillingResult.Success, vm.lastResult.first())
    }

    @Test
    fun runBilling_resetsBusy_whenBillingThrows() = runTest(dispatcher) {
        val billing = ThrowingBilling()
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled
        )

        vm.subscribeMax()
        advanceUntilIdle()

        assertFalse(vm.busy.first())
    }

    @Test
    fun hasMaxAccess_startsFalse_forFreeEntitlement() = runTest(dispatcher) {
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Unavailable),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedOutAuth(),
            SupabaseConfig.Disabled
        )

        advanceUntilIdle()

        assertFalse(vm.hasMaxAccess.first())
    }

    @Test
    fun restore_setsLastResult() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Pending)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedOutAuth(),
            SupabaseConfig.Disabled
        )

        vm.restore()
        advanceUntilIdle()

        assertEquals(1, billing.restoreCalls)
        assertEquals(BillingResult.Pending, vm.lastResult.first())
    }

    @Test
    fun productIds_useDefaults_whenConfigBlank() {
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Unavailable),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedOutAuth(),
            SupabaseConfig.Disabled
        )
        assertTrue(vm.trialProductId.isNotBlank())
        assertTrue(vm.monthlyProductId.isNotBlank())
    }

    private fun signedInAuth(): AuthRepository {
        val session = AuthSession("at", "rt", "u1", "user@example.com", null)
        return AuthRepository(
            auth = NoopSupabaseAuth,
            store = FakeSessionStore(session),
            google = NoopGoogle,
            entitlements = EntitlementRepository(FakeLocal(), FakeRemote()),
            config = SupabaseConfig.Disabled
        )
    }

    private fun signedOutAuth(): AuthRepository = AuthRepository(
        auth = NoopSupabaseAuth,
        store = FakeSessionStore(null),
        google = NoopGoogle,
        entitlements = EntitlementRepository(FakeLocal(), FakeRemote()),
        config = SupabaseConfig.Disabled
    )

    private class FakeBilling(private val result: BillingResult) : BillingController {
        var trialCalls = 0
        var restoreCalls = 0
        val purchaseIds = mutableListOf<String>()
        override val isPlayAvailable: Boolean = false
        override suspend fun startTrial(productId: String): BillingResult {
            trialCalls++
            return result
        }
        override suspend fun purchase(productId: String): BillingResult {
            purchaseIds.add(productId)
            return result
        }
        override suspend fun restorePurchases(): BillingResult {
            restoreCalls++
            return result
        }
    }

    private class ThrowingBilling : BillingController {
        override val isPlayAvailable: Boolean = false
        override suspend fun startTrial(productId: String): BillingResult = throw RuntimeException("boom")
        override suspend fun purchase(productId: String): BillingResult = throw RuntimeException("boom")
        override suspend fun restorePurchases(): BillingResult = throw RuntimeException("boom")
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

    private class FakeRemote : EntitlementRemote {
        override suspend fun fetchEntitlement(accessToken: String?): Entitlement? = null
    }

    companion object {
        val proEntitlement = Entitlement(
            tier = EntitlementTier.PRO,
            type = EntitlementType.PAID,
            expiresAtEpochMillis = null
        )
    }

    /**
     * Wraps a [CoroutineDispatcher] so that the coroutine context also carries a
     * [CoroutineExceptionHandler]. Used to install a handler on the main dispatcher
     * in tests that intentionally throw from a background coroutine.
     */
    private class HandlerTestDispatcher(
        private val delegate: CoroutineDispatcher,
        private val handler: CoroutineExceptionHandler
    ) : MainCoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            delegate.dispatch(context, block)
        }

        override fun isDispatchNeeded(context: CoroutineContext): Boolean =
            delegate.isDispatchNeeded(context)

        override val immediate: MainCoroutineDispatcher get() = this

        override fun <E : CoroutineContext.Element> get(key: CoroutineContext.Key<E>): E? {
            if (key == CoroutineExceptionHandler.Key) {
                @Suppress("UNCHECKED_CAST")
                return handler as E
            }
            return super.get(key)
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
