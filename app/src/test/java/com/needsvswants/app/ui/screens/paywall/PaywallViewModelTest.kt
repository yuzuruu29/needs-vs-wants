package com.needsvswants.app.ui.screens.paywall

import android.content.Context
import com.needsvswants.app.data.auth.AuthRepository
import com.needsvswants.app.data.auth.AuthSessionStore
import com.needsvswants.app.data.auth.GoogleIdTokenProvider
import com.needsvswants.app.data.auth.GoogleIdTokenResult
import com.needsvswants.app.data.billing.BillingController
import com.needsvswants.app.data.billing.BillingPeriod
import com.needsvswants.app.data.billing.BillingResult
import com.needsvswants.app.data.billing.CheckoutProvider
import com.needsvswants.app.data.billing.PaymentProvider
import com.needsvswants.app.data.entitlement.CheckoutReturnSync
import com.needsvswants.app.data.entitlement.EntitlementLocalStore
import com.needsvswants.app.data.entitlement.EntitlementRemote
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.entitlement.EntitlementSync
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
            entitlementSync = FakeEntitlementSync(),
            checkoutProvider = FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

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
            entitlementSync = FakeEntitlementSync(),
            checkoutProvider = FakeCheckoutProvider()
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
            entitlementSync = FakeEntitlementSync(),
            checkoutProvider = FakeCheckoutProvider()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

        vm.subscribePro()
        advanceUntilIdle()

        // The subscribe path must use the MONTHLY product id, never the trial id.
        assertEquals(listOf("monthly_x"), billing.purchaseIds)
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
            entitlementSync = FakeEntitlementSync(),
            checkoutProvider = FakeCheckoutProvider()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )

        vm.subscribeMax()
        advanceUntilIdle()

        assertFalse(vm.busy.first())
    }

    @Test
    fun runBilling_billingThrow_surfacesFailedResult_notCrash() = runTest(dispatcher) {
        // "The app stops when going for the free trial": an unexpected throw
        // inside a billing controller used to escape the ViewModel coroutine
        // and force-close the app. The pipeline's exception boundary must
        // surface the paywall's error contract instead.
        val billing = ThrowingBilling()
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )

        vm.subscribePro()
        advanceUntilIdle()

        assertFalse(vm.busy.first())
        assertTrue("billing throw must surface as Failed, not crash", vm.lastResult.first() is BillingResult.Failed)
    }

    @Test
    fun restore_billingThrow_surfacesFailedResult_notCrash() = runTest(dispatcher) {
        val billing = ThrowingBilling()
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )

        vm.restore()
        advanceUntilIdle()

        assertTrue("restore throw must surface as Failed, not crash", vm.lastResult.first() is BillingResult.Failed)
    }

    @Test
    fun reportCheckoutOpenFailure_clearsPendingFlag_surfacesFailed() = runTest(dispatcher) {
        // The browser launch failed (no browser / malformed approval URL): no
        // checkout actually started, so the durable pending-return flag must
        // not survive (no phantom cold-start retry loop) and the CTA must show
        // a retryable error instead of a silent no-op.
        val payPalReturn = FakePayPalReturnStore().apply { setPaypalReturnPending(true) }
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            payPalReturn,
            FakeEntitlementSync(),
            FakeCheckoutProvider()
        )

        vm.reportCheckoutOpenFailure()
        advanceUntilIdle()

        assertFalse(payPalReturn.paypalReturnPending.first())
        assertTrue(vm.lastResult.first() is BillingResult.Failed)
        assertEquals(CheckoutSyncState.Idle, vm.checkoutSyncState.first())
    }

    @Test
    fun subscribePro_sessionReadThrows_setsPendingInsteadOfCrashing() = runTest(dispatcher) {
        // A DataStore session-read failure at the CTA tap must behave like
        // signed-out (deferred intent), never crash the app.
        val auth = AuthRepository(
            auth = NoopSupabaseAuth,
            store = ThrowingSessionStore(),
            google = NoopGoogle,
            entitlements = EntitlementRepository(FakeLocal(), FakeRemote()),
            payPalReturn = FakePayPalReturnStore(),
            config = SupabaseConfig.Disabled
        )
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            auth,
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider()
        )

        vm.subscribePro()
        advanceUntilIdle()

        // No crash: the failed session read behaves like signed-out (deferred
        // intent) and the pipeline never engaged (busy stays false).
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
        assertFalse(vm.busy.first())
    }

    @Test
    fun onSignedInForPurchase_tokenRefreshStoreThrow_doesNotCrash() = runTest(dispatcher) {
        // The auto-continue path force-refreshes the access token; a storage
        // write failure there must not escape the ViewModel coroutine — the
        // billing controller re-checks the session and reports its own result.
        val auth = AuthRepository(
            auth = RefreshOkSupabaseAuth,
            store = SaveThrowingStore(),
            google = NoopGoogle,
            entitlements = EntitlementRepository(FakeLocal(), FakeRemote()),
            payPalReturn = FakePayPalReturnStore(),
            config = SupabaseConfig.Disabled
        )
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            auth,
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider()
        )

        vm.onSignedInForPurchase()
        advanceUntilIdle()

        // No crash: the pending intent ran (or aborted) through the guarded
        // pipeline; the screen's own gates decide the outcome.
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
            FakeEntitlementSync(),
            FakeCheckoutProvider()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

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
            FakeEntitlementSync(),
            FakeCheckoutProvider()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
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
        val billing = FakeBilling(BillingResult.OpenCheckout("https://paypal.test/approve"))
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            payPalReturn,
            sync,
            FakeCheckoutProvider(billing)
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
            sync,
            FakeCheckoutProvider()
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
            sync,
            FakeCheckoutProvider()
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
            sync,
            FakeCheckoutProvider()
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
            sync,
            FakeCheckoutProvider()
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
            sync,
            FakeCheckoutProvider()
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
    fun onReturnFromCheckout_vmIsSecondCaller_receivesSharedOutcome() = runTest(dispatcher) {
        // Warm return: the deep-link handler's routine (first caller, ahead of
        // the paywall RESUMED effect) wins the in-flight claim. The VM's
        // RESUMED path is the deduped second caller, but its callback must
        // still fire with the shared outcome — "Welcome to Pro." must appear,
        // never a stranded "Still unlocking — retrying…" strip.
        val gate = CompletableDeferred<Unit>()
        val remote = GatedProRemote(gate)
        val store = FakePayPalReturnStore().apply { setPaypalReturnPending(true) }
        val sync = EntitlementSync(
            auth = authForStore(FakeSessionStore(AuthSession("at", "rt", "u1", "user@example.com", null))),
            entitlements = EntitlementRepository(FakeLocal(), remote),
            preferences = store,
            config = enabledSyncConfig()
        )
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), remote),
            signedInAuth(),
            SupabaseConfig.Disabled,
            store,
            sync,
            FakeCheckoutProvider()
        )

        // First caller: the deep-link handler routine (in flight, gated).
        launch { sync.syncAfterCheckoutReturn() }
        runCurrent()
        assertEquals(1, remote.fetchCalls)

        // Second caller: the VM's RESUMED path — deduped, so it surfaces
        // Syncing while the shared routine is still in flight.
        vm.onReturnFromCheckout()
        runCurrent()
        assertEquals(CheckoutSyncState.Syncing, vm.checkoutSyncState.first())

        gate.complete(Unit)
        advanceUntilIdle()

        // The deduped VM call forwarded the winner's outcome: Pro confirmed →
        // Idle + Success (not stranded in Syncing, not wrongly Exhausted).
        assertEquals(CheckoutSyncState.Idle, vm.checkoutSyncState.first())
        assertEquals(BillingResult.Success, vm.lastResult.first())
        assertFalse(store.paypalReturnPending.first())
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
            sync,
            FakeCheckoutProvider(billing)
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
            sync,
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
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
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

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
            FakeEntitlementSync(),
            FakeCheckoutProvider()
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
            sync,
            FakeCheckoutProvider()
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
    fun onReturnFromCheckout_restoreSuccessDuringLoop_notOverwrittenByExhaustedTail() = runTest(dispatcher) {
        // A Restore-success reported while the retry loop is still in flight
        // must survive the loop's exhausted tail: the paywall already shows
        // "Welcome to Pro." and must not regress to the payment-recorded state.
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
            sync,
            FakeCheckoutProvider()
        )

        vm.onReturnFromCheckout()
        runCurrent()
        assertEquals(CheckoutSyncState.Syncing, vm.checkoutSyncState.first())

        // While the loop is gated, the user taps Restore purchases and it succeeds.
        vm.restore()
        runCurrent()
        assertEquals(BillingResult.Success, vm.lastResult.first())
        assertEquals(CheckoutSyncState.Idle, vm.checkoutSyncState.first())

        // The loop tail reports false (still free) — Exhausted must NOT
        // overwrite the restore success.
        gate.complete(Unit)
        advanceUntilIdle()

        assertEquals(CheckoutSyncState.Idle, vm.checkoutSyncState.first())
        assertEquals(BillingResult.Success, vm.lastResult.first())
    }

    @Test
    fun onSignedInForPurchase_cancelMidSettleWindow_abortsBeforeBilling() = runTest(dispatcher) {
        // The user cancels inside the 200ms token-settle window: the delayed
        // path re-reads pending after the delay and must abort without billing.
        val billing = FakeBilling(BillingResult.Success)
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceTimeBy(100)
        assertTrue(vm.busy.first())

        vm.cancelPendingSignIn()
        advanceTimeBy(300)
        advanceUntilIdle()

        assertEquals(PendingPurchase.None, vm.pendingPurchase.first())
        assertEquals(0, billing.purchaseIds.size)
        assertFalse(vm.busy.first())
    }

    @Test
    fun failedReason_roundTrips() {
        val withReason: BillingResult = BillingResult.Failed("paypal declined: 10486")
        assertEquals("paypal declined: 10486", (withReason as BillingResult.Failed).reason)
        // Default argument is null; equality is preserved between the two forms.
        assertEquals(BillingResult.Failed(null), BillingResult.Failed())
    }

    @Test
    fun selectedProvider_defaultsToPayPal() {
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Unavailable),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedOutAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider()
        )
        assertEquals(PaymentProvider.PAYPAL, vm.selectedProvider.value)
    }

    @Test
    fun selectedProvider_defaultsToPayMongo_whenPayPalUnavailable() {
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Unavailable),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedOutAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(
                FakeBilling(BillingResult.Success),
                FakeBilling(BillingResult.Success),
                payPalAvailable = false
            )
        )
        assertEquals(PaymentProvider.PAYMONGO, vm.selectedProvider.value)
        assertFalse(vm.payPalAvailable)
        assertTrue(vm.payMongoAvailable)
    }

    @Test
    fun subscribePro_payPalRoutesToStartTrial_withMonthlyProductId() = runTest(dispatcher) {
        // PayPal Pro: the plan carries the 3-day trial — the route is startTrial,
        // never a plain purchase, and it uses the monthly (plan) product id.
        val payPal = RecordingBilling()
        val payMongo = RecordingBilling()
        val config = providerRoutingConfig()
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            config,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(payPal = payPal, payMongo = payMongo)
        )

        vm.subscribePro()
        advanceUntilIdle()

        assertEquals(listOf("startTrial" to "monthly_x"), payPal.calls)
        assertEquals(emptyList<Pair<String, String>>(), payMongo.calls)
    }

    @Test
    fun subscribeMax_payPalRoutesToPurchase_withMaxProductId() = runTest(dispatcher) {
        // PayPal Max: no trial on the max plan — a direct subscription purchase.
        val payPal = RecordingBilling()
        val payMongo = RecordingBilling()
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            providerRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(payPal = payPal, payMongo = payMongo)
        )

        vm.subscribeMax()
        advanceUntilIdle()

        assertEquals(listOf("purchase" to "max_x"), payPal.calls)
        assertEquals(emptyList<Pair<String, String>>(), payMongo.calls)
    }

    @Test
    fun subscribePro_payMongoRoutesToPurchase_withMonthlyProductId() = runTest(dispatcher) {
        // PayMongo Pro: one-time monthly checkout, never the PayPal trial path.
        val payPal = RecordingBilling()
        val payMongo = RecordingBilling()
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            providerRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(payPal = payPal, payMongo = payMongo)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

        vm.subscribePro()
        advanceUntilIdle()

        assertEquals(emptyList<Pair<String, String>>(), payPal.calls)
        assertEquals(listOf("purchase" to "monthly_x"), payMongo.calls)
    }

    @Test
    fun subscribeMax_payMongoRoutesToPurchase_withMaxProductId() = runTest(dispatcher) {
        val payPal = RecordingBilling()
        val payMongo = RecordingBilling()
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            providerRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(payPal = payPal, payMongo = payMongo)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

        vm.subscribeMax()
        advanceUntilIdle()

        assertEquals(emptyList<Pair<String, String>>(), payPal.calls)
        assertEquals(listOf("purchase" to "max_x"), payMongo.calls)
    }

    @Test
    fun subscribePro_annualRoutesToStartTrial_withAnnualProductId() = runTest(dispatcher) {
        // Annual PayPal Pro: the plan carries the trial — startTrial with the
        // annual plan id and the ANNUAL period riding through.
        val payPal = RecordingBilling()
        val payMongo = RecordingBilling()
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            providerRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(payPal = payPal, payMongo = payMongo)
        )
        vm.selectPeriod(BillingPeriod.ANNUAL)
        advanceUntilIdle()

        vm.subscribePro()
        advanceUntilIdle()

        assertEquals(listOf("startTrial" to "annual_x"), payPal.calls)
        assertEquals(listOf("startTrial" to BillingPeriod.ANNUAL), payPal.periods)
        assertEquals(emptyList<Pair<String, String>>(), payMongo.calls)
    }

    @Test
    fun subscribeMax_annualRoutesToPurchase_withMaxAnnualProductId() = runTest(dispatcher) {
        val payPal = RecordingBilling()
        val payMongo = RecordingBilling()
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            providerRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(payPal = payPal, payMongo = payMongo)
        )
        vm.selectPeriod(BillingPeriod.ANNUAL)
        advanceUntilIdle()

        vm.subscribeMax()
        advanceUntilIdle()

        assertEquals(listOf("purchase" to "max_annual_x"), payPal.calls)
        assertEquals(listOf("purchase" to BillingPeriod.ANNUAL), payPal.periods)
        assertEquals(emptyList<Pair<String, String>>(), payMongo.calls)
    }

    @Test
    fun subscribePro_annualPayMongoRoutesToPurchase_withAnnualPeriod() = runTest(dispatcher) {
        // PayMongo annual: one-time checkout with the annual period; the VM
        // passes the annual product-id hint (PayMongo uses it for tier mapping).
        val payPal = RecordingBilling()
        val payMongo = RecordingBilling()
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            providerRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(payPal = payPal, payMongo = payMongo)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
        vm.selectPeriod(BillingPeriod.ANNUAL)
        advanceUntilIdle()

        vm.subscribePro()
        advanceUntilIdle()

        assertEquals(emptyList<Pair<String, String>>(), payPal.calls)
        assertEquals(listOf("purchase" to "annual_x"), payMongo.calls)
        assertEquals(listOf("purchase" to BillingPeriod.ANNUAL), payMongo.periods)
    }

    @Test
    fun selectPeriod_whileSignedInPending_clearsPending() = runTest(dispatcher) {
        // Mirror of selectProvider (D108): a signed-in deferred intent must
        // never cross a period change — it is cleared and the user re-taps
        // the CTA to start fresh with the new period.
        // providerRoutingConfig (annual ids set): PAYPAL, the default provider,
        // supports ANNUAL here - this test pins pending semantics, not the
        // annual-availability containment.
        val billing = FakeBilling(BillingResult.Failed("timeout"))
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            providerRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.subscribePro()
        advanceUntilIdle()
        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        vm.selectPeriod(BillingPeriod.ANNUAL)
        advanceUntilIdle()

        assertEquals(BillingPeriod.ANNUAL, vm.selectedPeriod.first())
        assertEquals(PendingPurchase.None, vm.pendingPurchase.first())
        assertFalse(vm.needsSignInForPurchase.first())
        assertNull(vm.lastResult.first())
    }

    @Test
    fun selectPeriod_whileSignedOutPending_reassertsForCurrentPlan() = runTest(dispatcher) {
        // Signed out: the deferred intent is re-asserted so sign-in completes
        // the purchase with the period just picked.
        // providerRoutingConfig (annual ids set): PAYPAL, the default provider,
        // supports ANNUAL here - this test pins pending semantics, not the
        // annual-availability containment.
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            providerRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(FakeBilling(BillingResult.Success))
        )
        vm.subscribeMax()
        advanceUntilIdle()
        assertEquals(PendingPurchase.MaxSubscribe, vm.pendingPurchase.first())

        vm.selectPeriod(BillingPeriod.ANNUAL)
        advanceUntilIdle()

        assertEquals(BillingPeriod.ANNUAL, vm.selectedPeriod.first())
        assertEquals(PendingPurchase.MaxSubscribe, vm.pendingPurchase.first())
        assertTrue(vm.needsSignInForPurchase.first())
    }

    @Test
    fun isAnnualAvailable_payPalFalse_whenAnnualPlanIdsBlank() = runTest(dispatcher) {
        // P1 containment: without configured annual PayPal plan ids the
        // controller would fail with "Annual PayPal plan not configured" -
        // PayPal exposes no ANNUAL, while PayMongo (server-authoritative
        // amounts) keeps annual available. A partial config (only one tier's
        // annual id set) must count as unavailable too: the selector drives
        // both Pro and Max.
        val partialConfig = SupabaseConfig(
            url = "",
            anonKey = "",
            proTrialProductId = "trial_x",
            proMonthlyProductId = "monthly_x",
            maxMonthlyProductId = "max_x",
            proAnnualProductId = "annual_x"
        )
        val blankVm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            monthlyOnlyRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider()
        )
        val partialVm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            partialConfig,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider()
        )

        assertFalse(blankVm.isAnnualAvailable(PaymentProvider.PAYPAL))
        assertFalse(partialVm.isAnnualAvailable(PaymentProvider.PAYPAL))
        assertTrue(blankVm.isAnnualAvailable(PaymentProvider.PAYMONGO))
        assertTrue(partialVm.isAnnualAvailable(PaymentProvider.PAYMONGO))
    }

    @Test
    fun isAnnualAvailable_payPalTrue_whenBothAnnualPlanIdsSet() = runTest(dispatcher) {
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            providerRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider()
        )

        assertTrue(vm.isAnnualAvailable(PaymentProvider.PAYPAL))
    }

    @Test
    fun selectPeriod_annualRejected_forPayPalWithoutAnnualPlans() = runTest(dispatcher) {
        // Default provider is PAYPAL (the fake reports both available): Annual
        // must stay rejected so the unconfigured yearly plan is unreachable.
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            monthlyOnlyRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(FakeBilling(BillingResult.Success))
        )

        vm.selectPeriod(BillingPeriod.ANNUAL)
        advanceUntilIdle()

        assertEquals(BillingPeriod.MONTHLY, vm.selectedPeriod.first())
    }

    @Test
    fun selectPeriod_annualSelectable_whenPayPalAnnualPlansSet() = runTest(dispatcher) {
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            providerRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(FakeBilling(BillingResult.Success))
        )

        vm.selectPeriod(BillingPeriod.ANNUAL)
        advanceUntilIdle()

        assertEquals(BillingPeriod.ANNUAL, vm.selectedPeriod.first())
    }

    @Test
    fun selectPeriod_annualUnaffected_forPayMongoWithoutPayPalAnnualPlans() = runTest(dispatcher) {
        // PayMongo annual is server-authoritative: selectable and routable
        // even when both PayPal annual plan ids are blank.
        val payMongo = RecordingBilling()
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            monthlyOnlyRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(payPal = RecordingBilling(), payMongo = payMongo)
        )
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

        vm.selectPeriod(BillingPeriod.ANNUAL)
        advanceUntilIdle()
        vm.subscribePro()
        advanceUntilIdle()

        assertEquals(BillingPeriod.ANNUAL, vm.selectedPeriod.first())
        assertEquals(listOf("purchase" to "pro_annual"), payMongo.calls)
        assertEquals(listOf("purchase" to BillingPeriod.ANNUAL), payMongo.periods)
    }

    @Test
    fun selectProvider_toPayPal_resetsAnnualToMonthly_whenAnnualUnavailable() = runTest(dispatcher) {
        // Switching back to PayPal without annual plans must fall back to
        // MONTHLY - also covering the signed-out deferred intent, whose
        // re-assert then routes the monthly plan instead of the unconfigured
        // annual one. No controller may be reached.
        val payPal = RecordingBilling()
        val payMongo = RecordingBilling()
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedOutAuth(),
            monthlyOnlyRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(payPal = payPal, payMongo = payMongo)
        )
        vm.subscribePro()
        advanceUntilIdle()
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
        vm.selectPeriod(BillingPeriod.ANNUAL)
        advanceUntilIdle()
        assertEquals(BillingPeriod.ANNUAL, vm.selectedPeriod.first())

        vm.selectProvider(PaymentProvider.PAYPAL)
        advanceUntilIdle()

        assertEquals(BillingPeriod.MONTHLY, vm.selectedPeriod.first())
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
        assertEquals(emptyList<Pair<String, String>>(), payPal.calls)
        assertEquals(emptyList<Pair<String, String>>(), payMongo.calls)
    }

    @Test
    fun onSignedInForPurchase_pendingUsesCurrentProvider() = runTest(dispatcher) {
        // The deferred intent is provider-agnostic in storage: after a provider
        // switch the sign-in auto-continue routes via the CURRENT provider.
        val payPal = RecordingBilling()
        val payMongo = RecordingBilling()
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            FakeBilling(BillingResult.Success),
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            providerRoutingConfig(),
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(payPal = payPal, payMongo = payMongo)
        )
        // User taps Start Pro while signed out (PAYPAL default): deferred.
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        // Switch to PayMongo before signing in: the intent re-asserts and the
        // auto-continue must run on the PayMongo one-time checkout.
        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()
        assertEquals(PaymentProvider.PAYMONGO, vm.selectedProvider.first())
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()

        assertEquals(emptyList<Pair<String, String>>(), payPal.calls)
        assertEquals(listOf("purchase" to "monthly_x"), payMongo.calls)
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
    }

    @Test
    fun selectProvider_whileSignedOutPending_reassertsPending() = runTest(dispatcher) {
        // Mirror of the plan-switch semantics (D108): signed out + deferred
        // intent → the intent re-asserts for the current plan so sign-in
        // completes the purchase with the provider just picked.
        val billing = FakeBilling(BillingResult.Success)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedOutAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.subscribePro()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())

        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

        assertEquals(PaymentProvider.PAYMONGO, vm.selectedProvider.first())
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
        assertTrue(vm.needsSignInForPurchase.first())
        assertEquals(0, billing.purchaseIds.size)
    }

    @Test
    fun selectProvider_whileSignedInPending_clearsPending() = runTest(dispatcher) {
        // Mirror of the plan-switch semantics (D108): a signed-in deferred
        // intent must never cross a provider change — it is cleared and the
        // user re-taps the CTA to start fresh with the new provider.
        val billing = FakeBilling(BillingResult.Failed("timeout"))
        val store = FakeSessionStore(null)
        val vm = PaywallViewModel(
            billing,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            authForStore(store),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(billing)
        )
        vm.subscribePro()
        advanceUntilIdle()
        store.save(AuthSession("at", "rt", "u1", "user@example.com", null))
        advanceUntilIdle()
        vm.onSignedInForPurchase()
        advanceUntilIdle()
        assertEquals(PendingPurchase.ProSubscribe, vm.pendingPurchase.first())
        assertEquals(BillingResult.Failed("timeout"), vm.lastResult.first())

        vm.selectProvider(PaymentProvider.PAYMONGO)
        advanceUntilIdle()

        assertEquals(PaymentProvider.PAYMONGO, vm.selectedProvider.first())
        assertEquals(PendingPurchase.None, vm.pendingPurchase.first())
        assertFalse(vm.needsSignInForPurchase.first())
        assertNull(vm.lastResult.first())
    }

    @Test
    fun restore_usesDefaultBilling_notProviderControllers() = runTest(dispatcher) {
        // restore() is provider-agnostic: it always goes through the default
        // BillingController binding, never the provider-resolved controllers.
        val defaultBilling = FakeBilling(BillingResult.Pending)
        val payPal = RecordingBilling()
        val payMongo = RecordingBilling()
        val vm = PaywallViewModel(
            defaultBilling,
            EntitlementRepository(FakeLocal(), FakeRemote()),
            signedInAuth(),
            SupabaseConfig.Disabled,
            FakePayPalReturnStore(),
            FakeEntitlementSync(),
            FakeCheckoutProvider(payPal = payPal, payMongo = payMongo)
        )

        vm.restore()
        advanceUntilIdle()

        assertEquals(1, defaultBilling.restoreCalls)
        assertEquals(0, payPal.restoreCalls)
        assertEquals(0, payMongo.restoreCalls)
        assertEquals(BillingResult.Pending, vm.lastResult.first())
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

    /** Enabled Supabase config for a REAL [EntitlementSync] (refresh path live). */
    private fun enabledSyncConfig(): SupabaseConfig = SupabaseConfig(
        url = "https://supabase.example",
        anonKey = "anon-key",
        proTrialProductId = "",
        proMonthlyProductId = "",
        maxMonthlyProductId = ""
    )

    /** Plan ids distinct from the VM fallbacks, for provider-routing assertions. */
    private fun providerRoutingConfig(): SupabaseConfig = SupabaseConfig(
        url = "",
        anonKey = "",
        proTrialProductId = "trial_x",
        proMonthlyProductId = "monthly_x",
        maxMonthlyProductId = "max_x",
        proAnnualProductId = "annual_x",
        maxAnnualProductId = "max_annual_x"
    )

    /** Plan ids with the ANNUAL ids left blank (PayPal yearly unconfigured). */
    private fun monthlyOnlyRoutingConfig(): SupabaseConfig = SupabaseConfig(
        url = "",
        anonKey = "",
        proTrialProductId = "trial_x",
        proMonthlyProductId = "monthly_x",
        maxMonthlyProductId = "max_x"
    )

    private class FakeBilling(private val result: BillingResult) : BillingController {
        var restoreCalls = 0
        val purchaseIds = mutableListOf<String>()
        override val isPlayAvailable: Boolean = false
        override suspend fun startTrial(productId: String, period: BillingPeriod): BillingResult = result
        override suspend fun purchase(productId: String, period: BillingPeriod): BillingResult {
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
        override suspend fun startTrial(productId: String, period: BillingPeriod): BillingResult = throw RuntimeException("boom")
        override suspend fun purchase(productId: String, period: BillingPeriod): BillingResult = throw RuntimeException("boom")
        override suspend fun restorePurchases(): BillingResult = throw RuntimeException("boom")
    }

    /** Records every checkout call as (method, productId); restore tracked separately. */
    private class RecordingBilling(
        private val result: BillingResult = BillingResult.Success
    ) : BillingController {
        val calls = mutableListOf<Pair<String, String>>()
        val periods = mutableListOf<Pair<String, BillingPeriod>>()
        var restoreCalls = 0
            private set
        override val isPlayAvailable: Boolean = false
        override suspend fun startTrial(productId: String, period: BillingPeriod): BillingResult {
            calls.add("startTrial" to productId)
            periods.add("startTrial" to period)
            return result
        }
        override suspend fun purchase(productId: String, period: BillingPeriod): BillingResult {
            calls.add("purchase" to productId)
            periods.add("purchase" to period)
            return result
        }
        override suspend fun restorePurchases(): BillingResult {
            restoreCalls++
            return result
        }
    }

    /**
     * Routes a [PaymentProvider] to the matching fake controller. When built
     * with a single controller ([constructor] overload) it is used for BOTH
     * providers, for tests that only care that the billing fake was called.
     */
    private class FakeCheckoutProvider(
        private val payPal: BillingController,
        private val payMongo: BillingController,
        private val googlePlay: BillingController = payMongo,
        override val payPalAvailable: Boolean = true,
        override val payMongoAvailable: Boolean = true,
        override val isPlayStoreBuild: Boolean = false
    ) : CheckoutProvider {
        constructor(billing: BillingController) : this(billing, billing, billing)
        constructor() : this(FakeBilling(BillingResult.Success), FakeBilling(BillingResult.Success), FakeBilling(BillingResult.Success))

        override fun controllerFor(provider: PaymentProvider): BillingController = when (provider) {
            PaymentProvider.PAYPAL -> payPal
            PaymentProvider.PAYMONGO -> payMongo
            PaymentProvider.GOOGLE_PLAY -> googlePlay
        }
    }

    private class FakeLocal(initial: Entitlement = Entitlement()) : EntitlementLocalStore {
        private val state = MutableStateFlow(initial)
        private val synced = MutableStateFlow(
            if (initial.hasProAccessAt(System.currentTimeMillis())) System.currentTimeMillis() else 0L
        )
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

    /** Suspends each fetch on [gate], then confirms Pro access (a landed grant). */
    private class GatedProRemote(private val gate: CompletableDeferred<Unit>) : EntitlementRemote {
        var fetchCalls = 0
            private set
        override suspend fun fetchEntitlement(accessToken: String?): Entitlement? {
            fetchCalls++
            gate.await()
            return proEntitlement
        }
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

    /** Session read always throws — simulates a DataStore read failure. */
    private class ThrowingSessionStore : AuthSessionStore {
        override val session: Flow<AuthSession?> = flow { throw RuntimeException("disk read failed") }
        override suspend fun save(session: AuthSession) = Unit
        override suspend fun clear() = Unit
    }

    /** Expired session + refresh succeeds + save throws — the token-refresh storage failure. */
    private class SaveThrowingStore : AuthSessionStore {
        private val state = MutableStateFlow(AuthSession("at", "rt", "u1", "user@example.com", 0L))
        override val session: Flow<AuthSession?> = state
        override suspend fun save(session: AuthSession) {
            throw RuntimeException("disk write failed")
        }
        override suspend fun clear() = Unit
    }

    private object RefreshOkSupabaseAuth : SupabaseAuth {
        override val isConfigured: Boolean = true
        override suspend fun sendMagicLink(email: String): Result<Unit> =
            Result.success(Unit)
        override suspend fun verifyOtp(email: String, token: String): Result<AuthSession> =
            Result.success(AuthSession(token, null, null, email, null))
        override suspend fun signInWithGoogleIdToken(idToken: String, nonce: String?): Result<AuthSession> =
            Result.success(AuthSession("at", "rt", "u1", "user@example.com", null))
        override suspend fun refreshSession(refreshToken: String): Result<AuthSession> =
            Result.success(AuthSession("new-at", "new-rt", "u1", "user@example.com", null))
        override suspend fun signOut(accessToken: String): Result<Unit> = Result.success(Unit)
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
        override suspend fun startTrial(productId: String, period: BillingPeriod): BillingResult = BillingResult.Pending
        override suspend fun purchase(productId: String, period: BillingPeriod): BillingResult {
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
        override suspend fun verifyOtp(email: String, token: String): Result<AuthSession> =
            Result.failure(IllegalStateException("noop"))
        override suspend fun signInWithGoogleIdToken(idToken: String, nonce: String?): Result<AuthSession> =
            Result.failure(IllegalStateException("noop"))
        override suspend fun refreshSession(refreshToken: String): Result<AuthSession> =
            Result.failure(IllegalStateException("noop"))
        override suspend fun signOut(accessToken: String): Result<Unit> = Result.success(Unit)
    }
}
