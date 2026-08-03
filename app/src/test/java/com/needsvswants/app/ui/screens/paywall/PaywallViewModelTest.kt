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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
class PaywallViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
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

        vm.startTrial()
        advanceUntilIdle()

        assertEquals(BillingResult.Unavailable, vm.lastResult.first())
        assertFalse(vm.busy.first())
        assertEquals(1, billing.trialCalls)
    }

    @Test
    fun startTrial_requiresSignIn_whenSignedOut() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val vm = PaywallViewModel(
            billing = billing,
            repository = EntitlementRepository(FakeLocal(), FakeRemote()),
            authRepository = signedOutAuth(),
            config = SupabaseConfig.Disabled
        )

        vm.startTrial()
        advanceUntilIdle()

        assertEquals(0, billing.trialCalls)
        assertTrue(vm.needsSignIn.first())
        assertNull(vm.lastResult.first())
    }

    @Test
    fun upgrade_invokesPurchase_withMonthlyProductId() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val config = SupabaseConfig(
            url = "",
            anonKey = "",
            proTrialProductId = "trial_x",
            proMonthlyProductId = "monthly_x"
        )
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            config
        )

        vm.upgrade()
        advanceUntilIdle()

        assertEquals(listOf("monthly_x"), billing.purchaseIds)
        assertEquals(BillingResult.Success, vm.lastResult.first())
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

    private class FakeLocal : EntitlementLocalStore {
        private val state = MutableStateFlow(Entitlement())
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
