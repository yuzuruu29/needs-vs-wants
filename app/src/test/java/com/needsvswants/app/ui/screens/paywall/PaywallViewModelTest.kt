package com.needsvswants.app.ui.screens.paywall

import android.content.Context
import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.auth.AuthSessionStore
import com.needsvswants.app.data.auth.GoogleIdTokenProvider
import com.needsvswants.app.data.auth.GoogleIdTokenResult
import com.needsvswants.app.data.billing.BillingController
import com.needsvswants.app.data.billing.BillingResult
import com.needsvswants.app.data.entitlement.CheckoutReturnSync
import com.needsvswants.app.data.entitlement.EntitlementLocalStore
import com.needsvswants.app.data.entitlement.EntitlementRemote
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.entitlement.PayPalReturnStore
import com.needsvswants.app.data.remote.AuthSession
import com.needsvswants.app.data.remote.SupabaseAuth
import com.needsvswants.app.data.remote.SupabaseConfig
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
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
    fun subscribePro_recordsUnavailable_whenBillingStub_andSignedIn() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Unavailable)
        val vm = PaywallViewModel(
            billing = billing,
            repository = EntitlementRepository(FakeLocal(), FakeRemote()),
            authRepository = signedInAuth(),
            config = SupabaseConfig.Disabled,
            payPalReturn = FakePayPalReturnStore(),
            entitlementSync = FakeEntitlementSync()
        )

        vm.subscribePro()
        advanceUntilIdle()

        assertEquals(BillingResult.Unavailable, vm.lastResult.first())
        assertFalse(vm.busy.first())
        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
    }

    @Test
    fun subscribePro_setsPendingPurchase_whenSignedOut_noBillingYet() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val vm = PaywallViewModel(
            billing = billing,
            repository = EntitlementRepository(FakeLocal(), FakeRemote()),
            authRepository = signedOutAuth(),
            config = SupabaseConfig.Disabled,
            payPalReturn = FakePayPalReturnStore(),
            entitlementSync = FakeEntitlementSync()
        )

        vm.subscribePro()
        advanceUntilIdle()

        assertEquals(0, billing.purchaseIds.size)
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
        assertTrue(vm.needsSignInForPurchase.first())
        assertNull(vm.lastResult.first())
    }

    @Test
    fun subscribeMax_setsPendingMax_whenSignedOut() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val vm = PaywallViewModel(
            billing = billing,
            repository = EntitlementRepository(FakeLocal(), FakeRemote()),
            authRepository = signedOutAuth(),
            config = SupabaseConfig.Disabled,
            payPalReturn = FakePayPalReturnStore(),
            entitlementSync = FakeEntitlementSync()
        )

        vm.subscribeMax()
        advanceUntilIdle()

        assertEquals(0, billing.purchaseIds.size)
        assertEquals(PendingPurchase.MaxSubscribe, vm.pendingPurchase.first())
        assertTrue(vm.needsSignInForPurchase.first())
    }

    @Test
    fun subscribePro_signedIn_purchasesMonthlyProductId_notTrial() = runTest(dispatcher) {
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
            config,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )

        vm.subscribePro()
        advanceUntilIdle()

        // The subscribe path must use the MONTHLY product id, never the trial id.
        assertEquals(listOf("monthly_x"), billing.purchaseIds)
        assertFalse(billing.purchaseIds.contains("trial_x"))
        assertEquals(BillingResult.Success, vm.lastResult.first())
        assertEquals(PendingPurchase.None, vm.pendingPurchase.first())
    }

    @Test
    fun cancelPendingSignIn_clearsGate() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val vm = PaywallViewModel(
            billing = billing,
            repository = EntitlementRepository(FakeLocal(), FakeRemote()),
            authRepository = signedOutAuth(),
            config = SupabaseConfig.Disabled,
            payPalReturn = FakePayPalReturnStore(),
            entitlementSync = FakeEntitlementSync()
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
    fun subscribeMax_invokesPurchase_withMaxProductId() = runTest(dispatcher) {
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
            config,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )

        vm.subscribeMax()
        advanceUntilIdle()

        assertEquals(listOf("max_x"), billing.purchaseIds)
        assertEquals(BillingResult.Success, vm.lastResult.first())
    }

    @Test
    fun subscribeMax_proUser_invokesPurchase_withMaxProductId() = runTest(dispatcher) {
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
            config,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
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
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
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
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
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
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
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
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        assertEquals("pro_monthly", vm.monthlyProductId)
        assertEquals("max_monthly", vm.maxProductId)
    }

    @Test
    fun onSignedInForPurchase_runsPendingProSubscribe_withMonthlyProductId() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Unavailable)
        val store = FakeSessionStore(null)
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
            authForStore(store),
            config,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        // User taps Start Pro while signed out: deferred, no billing yet.
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
        assertEquals(0, billing.purchaseIds.size)

        // Google sign-in completes, then the app resumes the deferred purchase.
        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()

        assertEquals(listOf("monthly_x"), billing.purchaseIds)
        // Non-checkout result: pending survives so a failed attempt can be retried.
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
        assertEquals(BillingResult.Unavailable, vm.lastResult.first())
        assertFalse(vm.busy.first())
    }

    @Test
    fun onSignedInForPurchase_runsPendingMaxSubscribe_withMaxProductId() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Unavailable)
        val store = FakeSessionStore(null)
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
            authForStore(store),
            config,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        vm.subscribeMax()
        advanceUntilIdle()
        assertEquals(PendingPurchase.MaxSubscribe, vm.pendingPurchase.first())

        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()

        assertEquals(listOf("max_x"), billing.purchaseIds)
        assertEquals(PendingPurchase.MaxSubscribe, vm.pendingPurchase.first())
        assertEquals(BillingResult.Unavailable, vm.lastResult.first())
    }

    @Test
    fun onSignedInForPurchase_pendingSurvives_failedResult() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Failed("paypal declined"))
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()

        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
        // Failed: pending must stay set so retryCheckout can re-attempt.
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
        assertEquals(BillingResult.Failed("paypal declined"), vm.lastResult.first())
    }

    @Test
    fun onSignedInForPurchase_pendingCleared_openCheckout() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.OpenCheckout("https://paypal.test/approve"))
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()

        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
        // Checkout started: the deferred intent has been consumed.
        assertEquals(PendingPurchase.None, vm.pendingPurchase.first())
        assertEquals(BillingResult.OpenCheckout("https://paypal.test/approve"), vm.lastResult.first())
    }

    @Test
    fun onSignedInForPurchase_twiceWithSamePending_afterFailed_runsBillingExactlyOnce() = runTest(dispatcher) {
        // Exactly-once auto-continue: after a failed attempt the intent stays
        // pending; a re-entering composition (rotation) must NOT re-create the
        // subscription. The second call must no-op on the autoContinued flag.
        val billing = FakeBilling(BillingResult.Failed("timeout"))
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()
        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        // Same pending intent, auto-continue invoked again (rotation/re-entry):
        // must NOT issue a second paypal_create_subscription POST.
        vm.onSignedInForPurchase()
        advanceUntilIdle()

        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
    }

    @Test
    fun onSignedInForPurchase_runsAgain_afterCancelAndFreshSubscribe() = runTest(dispatcher) {
        // A NEW user intent must auto-continue exactly once: cancel + fresh
        // signed-out subscribe resets the autoContinued flag.
        val billing = FakeBilling(BillingResult.Failed("timeout"))
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        // First intent: continues once, fails, intent stays pending.
        vm.subscribePro()
        advanceUntilIdle()
        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()
        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        // Cancel the intent, sign out, then tap Pro again: fresh intent.
        vm.cancelPendingSignIn()
        advanceUntilIdle()
        store.clear()
        advanceUntilIdle()
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
        assertEquals(1, billing.purchaseIds.size)

        // Sign back in: the FRESH intent must auto-continue once again.
        store.save(AuthSession("at2", "rt2", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()

        assertEquals(listOf("pro_monthly", "pro_monthly"), billing.purchaseIds)
        assertFalse(vm.busy.first())
    }

    @Test
    fun retryCheckout_retries_whenLastResultFailed() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Failed("paypal declined"))
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        vm.subscribePro()
        advanceUntilIdle()
        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()
        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        vm.retryCheckout()
        advanceUntilIdle()

        assertEquals(listOf("pro_monthly", "pro_monthly"), billing.purchaseIds)
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
    }

    @Test
    fun retryCheckout_noOp_whenLastResultSuccess() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        vm.subscribePro()
        advanceUntilIdle()
        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()
        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
        assertEquals(BillingResult.Success, vm.lastResult.first())

        vm.retryCheckout()
        advanceUntilIdle()

        // A reported-success last result must not re-run the purchase.
        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
    }

    @Test
    fun retryCheckout_noOp_whenNothingPending() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Failed("nope"))
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )

        vm.retryCheckout()
        advanceUntilIdle()

        assertEquals(0, billing.purchaseIds.size)
    }

    @Test
    fun retryCheckout_noOp_whenSignedOut() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Failed("nope"))
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedOutAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        vm.retryCheckout()
        advanceUntilIdle()

        assertEquals(0, billing.purchaseIds.size)
    }

    @Test
    fun consumeResult_clearsLastResult() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Failed("boom"))
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(BillingResult.Failed("boom"), vm.lastResult.first())

        vm.consumeResult()
        advanceUntilIdle()

        assertNull(vm.lastResult.first())
    }

    @Test
    fun subscribePro_afterFailedResult_rerunsBilling_whenSignedIn() = runTest(dispatcher) {
        // Explicit user retry (CTA re-tap / "Try PayPal again") must re-run the
        // pipeline even though lastResult is non-null — the auto-continue gate
        // must not leak into explicit retry paths.
        val billing = FakeBilling(BillingResult.Failed("timeout"))
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )

        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
        assertEquals(BillingResult.Failed("timeout"), vm.lastResult.first())

        vm.subscribePro()
        advanceUntilIdle()

        assertEquals(listOf("pro_monthly", "pro_monthly"), billing.purchaseIds)
        assertFalse(vm.busy.first())
    }

    @Test
    fun subscribeMax_afterPendingPro_whenSignedOut_reassertsMax() = runTest(dispatcher) {
        // Plan switch while signed out with a deferred intent: pending must
        // follow the NEW selection so sign-in completes the plan just picked.
        val billing = FakeBilling(BillingResult.Success)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedOutAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        vm.subscribeMax()
        advanceUntilIdle()

        assertEquals(0, billing.purchaseIds.size)
        assertEquals(PendingPurchase.MaxSubscribe, vm.pendingPurchase.first())
        assertTrue(vm.needsSignInForPurchase.first())
    }

    @Test
    fun cancelPendingSignIn_clearsPending_afterFailedResult() = runTest(dispatcher) {
        // Plan switch while signed in with a deferred intent that already
        // failed: the intent must die; the user re-taps the CTA for the new plan.
        val billing = FakeBilling(BillingResult.Failed("timeout"))
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        vm.subscribePro()
        advanceUntilIdle()
        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
        assertEquals(BillingResult.Failed("timeout"), vm.lastResult.first())

        vm.cancelPendingSignIn()
        advanceUntilIdle()

        assertEquals(PendingPurchase.None, vm.pendingPurchase.first())
        assertFalse(vm.needsSignInForPurchase.first())
    }

    @Test
    fun openCheckout_setsDurablePendingFlag() = runTest(dispatcher) {
        val payPalReturn = FakePayPalReturnStore()
        val sync = FakeEntitlementSync(outcome = true)
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.OpenCheckout("https://paypal.test/approve")),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            payPalReturn,
            sync
        )

        vm.subscribePro()
        advanceUntilIdle()

        // Task-1 P3-4 carry: the durable flag is persisted when checkout starts —
        // before any return sync attempt — and only a confirmation/cancel clears it.
        assertTrue(payPalReturn.paypalReturnPending.first())
        assertEquals(BillingResult.OpenCheckout("https://paypal.test/approve"), vm.lastResult.first())
        assertEquals(0, sync.syncCalls)
    }

    @Test
    fun onReturnFromCheckout_flagSet_runsSync_clearsFlagOnPro() = runTest(dispatcher) {
        val payPalReturn = FakePayPalReturnStore().apply { setPaypalReturnPending(true) }
        val sync = FakeEntitlementSync(outcome = true)
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            payPalReturn,
            sync
        )

        vm.onReturnFromCheckout()
        advanceUntilIdle()

        assertEquals(1, sync.syncCalls)
        assertFalse(payPalReturn.paypalReturnPending.first())
        assertEquals(BillingResult.Success, vm.lastResult.first())
        assertEquals(CheckoutSyncState.Idle, vm.checkoutSyncState.first())
    }

    @Test
    fun onReturnFromCheckout_exhausted_leavesFlag_andSurfacesExhausted() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val payPalReturn = FakePayPalReturnStore().apply { setPaypalReturnPending(true) }
        val sync = FakeEntitlementSync(outcome = false, gate = gate)
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            payPalReturn,
            sync
        )

        vm.onReturnFromCheckout()
        runCurrent()

        // Backoff in flight: the retrying state is surfaced while the loop runs.
        assertEquals(CheckoutSyncState.Syncing, vm.checkoutSyncState.first())
        assertEquals(1, sync.syncCalls)

        gate.complete(Unit)
        advanceUntilIdle()

        // Exhausted: the flag stays set (self-heal on next cold start / Restore)
        // and the paywall surfaces the payment-recorded message instead of silence.
        assertTrue(payPalReturn.paypalReturnPending.first())
        assertEquals(CheckoutSyncState.Exhausted, vm.checkoutSyncState.first())
        assertNull(vm.lastResult.first())
    }

    @Test
    fun onReturnFromCheckout_flagNotSet_noSync() = runTest(dispatcher) {
        val payPalReturn = FakePayPalReturnStore()
        val sync = FakeEntitlementSync(outcome = true)
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            payPalReturn,
            sync
        )

        vm.onReturnFromCheckout()
        advanceUntilIdle()

        assertEquals(0, sync.syncCalls)
        assertEquals(CheckoutSyncState.Idle, vm.checkoutSyncState.first())
    }

    @Test
    fun onReturnFromCheckout_afterCancelClearedFlag_noSync() = runTest(dispatcher) {
        // The cancel deep link (Task 1) clears the durable flag; the paywall
        // resume that follows must not start a sync for a cancelled checkout.
        val payPalReturn = FakePayPalReturnStore().apply { setPaypalReturnPending(true) }
        payPalReturn.clearPaypalReturnPending()
        val sync = FakeEntitlementSync(outcome = true)
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            payPalReturn,
            sync
        )

        vm.onReturnFromCheckout()
        advanceUntilIdle()

        assertEquals(0, sync.syncCalls)
        assertFalse(payPalReturn.paypalReturnPending.first())
    }

    @Test
    fun onReturnFromCheckout_secondResume_doesNotRerunLoop() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val payPalReturn = FakePayPalReturnStore().apply { setPaypalReturnPending(true) }
        val sync = FakeEntitlementSync(outcome = false, gate = gate)
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            payPalReturn,
            sync
        )

        vm.onReturnFromCheckout()
        runCurrent()
        // Background/foreground cycle: the RESUMED effect fires again, but the
        // once-per-return guard must not restart the 0/2/5/10s loop.
        vm.onReturnFromCheckout()
        runCurrent()
        assertEquals(1, sync.syncCalls)

        gate.complete(Unit)
        advanceUntilIdle()
        assertEquals(1, sync.syncCalls)
    }

    @Test
    fun onReturnFromCheckout_newOpenCheckout_rearmsLoop() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val payPalReturn = FakePayPalReturnStore().apply { setPaypalReturnPending(true) }
        val sync = FakeEntitlementSync(outcome = false, gate = gate)
        val billing = FakeBilling(BillingResult.OpenCheckout("https://paypal.test/approve"))
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            payPalReturn,
            sync
        )

        vm.onReturnFromCheckout()
        runCurrent()
        assertEquals(1, sync.syncCalls)
        gate.complete(Unit)
        advanceUntilIdle()
        assertEquals(CheckoutSyncState.Exhausted, vm.checkoutSyncState.first())

        // A NEW checkout re-persists the flag, resets the exhausted state, and
        // re-arms the once-per-return guard so the next return retriggers it.
        vm.subscribePro()
        advanceUntilIdle()
        assertTrue(payPalReturn.paypalReturnPending.first())
        assertEquals(CheckoutSyncState.Idle, vm.checkoutSyncState.first())

        vm.onReturnFromCheckout()
        advanceUntilIdle()
        assertEquals(2, sync.syncCalls)
        assertEquals(CheckoutSyncState.Exhausted, vm.checkoutSyncState.first())
    }

    @Test
    fun onReturnFromCheckout_syncNotBlockedByBusy() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val billing = GatedBilling(gate)
        val payPalReturn = FakePayPalReturnStore().apply { setPaypalReturnPending(true) }
        val sync = FakeEntitlementSync(outcome = true)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            payPalReturn,
            sync
        )

        vm.subscribePro()
        runCurrent()
        assertTrue(vm.busy.first())

        // The checkout-return sync is independent of the billing pipeline: it
        // runs even while a purchase is in flight (per design, not busy-gated).
        vm.onReturnFromCheckout()
        advanceUntilIdle()
        assertEquals(1, sync.syncCalls)

        gate.complete(Unit)
        advanceUntilIdle()
        assertEquals(BillingResult.OpenCheckout("https://paypal.test/approve"), vm.lastResult.first())
    }

    @Test
    fun retryCheckout_noOp_whileBusy() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val billing = GatedBilling(gate)
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        runCurrent()
        assertTrue(vm.busy.first())

        vm.retryCheckout()
        advanceUntilIdle()

        // Busy guard: no second purchase while the first is in flight.
        assertEquals(1, billing.purchaseIds.size)

        gate.complete(Unit)
        advanceUntilIdle()
        assertEquals(1, billing.purchaseIds.size)
        assertFalse(vm.busy.first())
    }

    @Test
    fun subscribePro_signedIn_surfacesOpenCheckout() = runTest(dispatcher) {
        // PayMongo (or any web checkout) returns OpenCheckout; the VM must pass
        // it through from billing.purchase so the UI can open the checkout URL.
        val billing = FakeBilling(BillingResult.OpenCheckout("https://checkout.paymongo.com/px/abc"))
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )

        vm.subscribePro()
        advanceUntilIdle()

        assertEquals(listOf("pro_monthly"), billing.purchaseIds)
        assertEquals(
            BillingResult.OpenCheckout("https://checkout.paymongo.com/px/abc"),
            vm.lastResult.first()
        )
        assertFalse(vm.busy.first())
    }

    @Test
    fun restore_surfacesSuccess() = runTest(dispatcher) {
        val billing = FakeBilling(BillingResult.Success)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync()
        )

        vm.restore()
        advanceUntilIdle()

        assertEquals(1, billing.restoreCalls)
        assertEquals(BillingResult.Success, vm.lastResult.first())
    }

    @Test
    fun restore_success_resetsExhaustedCheckoutSyncState() = runTest(dispatcher) {
        // P2: the exhausted "Payment recorded — tap Restore…" state must not
        // survive a successful restore — the paywall would show it next to
        // "Welcome to Pro." A Success restore resets the sync state to Idle.
        val gate = CompletableDeferred<Unit>()
        val payPalReturn = FakePayPalReturnStore().apply { setPaypalReturnPending(true) }
        val sync = FakeEntitlementSync(outcome = false, gate = gate)
        val billing = FakeBilling(BillingResult.Success)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            payPalReturn,
            sync
        )

        // Retry schedule exhausts: surface the payment-recorded message.
        vm.onReturnFromCheckout()
        runCurrent()
        gate.complete(Unit)
        advanceUntilIdle()
        assertEquals(CheckoutSyncState.Exhausted, vm.checkoutSyncState.first())

        // User taps Restore purchases and the restore reports Success.
        vm.restore()
        advanceUntilIdle()

        assertEquals(1, billing.restoreCalls)
        assertEquals(BillingResult.Success, vm.lastResult.first())
        assertEquals(CheckoutSyncState.Idle, vm.checkoutSyncState.first())
    }

    @Test
    fun failedReason_roundTrips() {
        val withReason: BillingResult = BillingResult.Failed("paypal declined: 10486")
        assertTrue(withReason is BillingResult.Failed)
        assertEquals("paypal declined: 10486", (withReason as BillingResult.Failed).reason)
        // Default argument is null; equality is preserved between the two forms.
        assertEquals(BillingResult.Failed(null), BillingResult.Failed())
    }

    private fun signedInAuth(): AuthRepository {
        val session = AuthSession("at", "rt", "u1", "user@example.com", null)
        return authForStore(FakeSessionStore(session))
    }

    private fun signedOutAuth(): AuthRepository = authForStore(FakeSessionStore(null))

    private fun authForStore(store: FakeSessionStore): AuthRepository = AuthRepository(
        auth = NoopSupabaseAuth,
        store = store,
        google = NoopGoogle,
        entitlements = EntitlementRepository(FakeLocal(), FakeRemote()),
        payPalReturn = FakePayPalReturnStore(),
        config = SupabaseConfig.Disabled
    )

    private class FakeBilling(private val result: BillingResult) : BillingController {
        var restoreCalls = 0
        val purchaseIds = mutableListOf<String>()
        override val isPlayAvailable: Boolean = false
        override suspend fun startTrial(productId: String): BillingResult = result
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
     * Wraps a [TestDispatcher] so that the coroutine context also carries a
     * [CoroutineExceptionHandler]. Used to install a handler on the main dispatcher
     * in tests that intentionally throw from a background coroutine.
     *
     * Implements [Delay] by delegating to the wrapped dispatcher so `delay(...)`
     * calls inside the ViewModel (e.g. the token-settle pause) are scheduled on
     * the test scheduler and advance with virtual time instead of real time.
     */
    @OptIn(InternalCoroutinesApi::class)
    private class HandlerTestDispatcher(
        private val delegate: TestDispatcher,
        private val handler: CoroutineExceptionHandler
    ) : MainCoroutineDispatcher(), Delay {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            delegate.dispatch(context, block)
        }

        override fun isDispatchNeeded(context: CoroutineContext): Boolean =
            delegate.isDispatchNeeded(context)

        override val immediate: MainCoroutineDispatcher get() = this

        override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
            delegate.scheduleResumeAfterDelay(timeMillis, continuation)
        }

        override fun invokeOnTimeout(timeMillis: Long, block: Runnable, context: CoroutineContext): DisposableHandle =
            delegate.invokeOnTimeout(timeMillis, block, context)

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

    /**
     * Records calls to the checkout-return sync seam. [outcome] is reported to
     * the onResult callback; when [gate] is provided the call suspends on it
     * first so tests can observe the in-flight (Syncing) state.
     */
    private class FakeEntitlementSync(
        private val outcome: Boolean? = null,
        private val gate: CompletableDeferred<Unit>? = null
    ) : CheckoutReturnSync {
        var syncCalls = 0
            private set

        override suspend fun syncAfterCheckoutReturn(onResult: (Boolean) -> Unit): Boolean {
            syncCalls++
            gate?.await()
            val result = outcome ?: error("FakeEntitlementSync needs an outcome for this call")
            onResult(result)
            return result
        }
    }

    /** Billing whose purchase suspends on [gate] before returning OpenCheckout. */
    private class GatedBilling(private val gate: CompletableDeferred<Unit>) : BillingController {
        val purchaseIds = mutableListOf<String>()
        override val isPlayAvailable: Boolean = false
        override suspend fun startTrial(productId: String): BillingResult = BillingResult.Pending
        override suspend fun purchase(productId: String): BillingResult {
            purchaseIds.add(productId)
            gate.await()
            return BillingResult.OpenCheckout("https://paypal.test/approve")
        }
        override suspend fun restorePurchases(): BillingResult = BillingResult.Success
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
