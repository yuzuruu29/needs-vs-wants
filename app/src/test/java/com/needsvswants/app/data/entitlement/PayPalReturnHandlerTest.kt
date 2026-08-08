package com.needsvswants.app.data.entitlement

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.jvm.Volatile

/**
 * Pins the ordering contract of [PayPalReturnHandler]: the durable pending flag
 * is persisted BEFORE the checkout-return sync starts (so the flag never misses
 * a grant window), and the cancel path clears the flag without touching the sync.
 */
class PayPalReturnHandlerTest {

    @Test
    fun onCheckoutReturned_setsPendingFlag_beforeFirstSyncAttempt() = runTest {
        val store = FakeStore()
        val sync = RecordingSync(store)
        val handler = PayPalReturnHandler(store, sync)

        handler.onCheckoutReturned()

        // The handler runs on a real IO scope: wait (bounded) for the sync seam
        // to be invoked, then verify the flag was ALREADY set at that moment.
        val flagAtInvocation = awaitSyncInvocation(sync)
        assertTrue("pending flag must be set before the first sync attempt", flagAtInvocation)
        assertTrue(store.paypalReturnPending.first())
    }

    @Test
    fun onCheckoutCancelled_clearsPendingFlag_withoutStartingSync() = runTest {
        val store = FakeStore(initial = true)
        val sync = RecordingSync(store)
        val handler = PayPalReturnHandler(store, sync)

        handler.onCheckoutCancelled()

        // Wait (bounded) for the durable flag to clear; the sync seam must
        // never be invoked on the cancel path.
        assertFalse(store.paypalReturnPending.first { !it })
        assertFalse(store.paypalReturnPending.first())
        assertEquals(0, sync.syncCalls)
    }

    private suspend fun awaitSyncInvocation(sync: RecordingSync): Boolean {
        withTimeoutOrNull(5_000) {
            while (sync.flagAtFirstRefresh == null) kotlinx.coroutines.delay(1)
        } ?: error("checkout-return sync seam was never invoked")
        return sync.flagAtFirstRefresh ?: error("checkout-return sync seam was never invoked")
    }

    private class FakeStore(initial: Boolean = false) : PayPalReturnStore {
        val state = MutableStateFlow(initial)
        override val paypalReturnPending: Flow<Boolean> = state
        override suspend fun setPaypalReturnPending(pending: Boolean) {
            state.value = pending
        }
        override suspend fun clearPaypalReturnPending() {
            state.value = false
        }
    }

    /**
     * Records the pending-flag value at the moment the sync seam is invoked —
     * true proves the handler persisted the flag before starting the sync.
     */
    private class RecordingSync(private val store: FakeStore) : CheckoutReturnSync {
        @Volatile
        var flagAtFirstRefresh: Boolean? = null
            private set
        @Volatile
        var syncCalls = 0
            private set

        override suspend fun syncAfterCheckoutReturn(onResult: (Boolean) -> Unit): Boolean {
            syncCalls++
            flagAtFirstRefresh = store.state.value
            onResult(false)
            return false
        }
    }
}
