package com.needsvswants.app.data.entitlement

import com.needsvswants.app.data.prefs.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Deep-link entry point for PayPal checkout returns
 * (`needsvswants://paypal/return` / `needsvswants://paypal/cancel`), called
 * from [com.needsvswants.app.MainActivity] on both cold start and `onNewIntent`.
 *
 * Return: persists the durable pending-return flag (survives process death
 * while the browser is open) and kicks the retried sync; the sync clears the
 * flag itself once Pro is confirmed.
 *
 * Cancel: no grant is expected — clears the durable flag.
 *
 * All work is fire-and-forget in an app-lifetime scope and guarded with
 * runCatching, so a deep link can never crash the activity.
 */
@Singleton
class PayPalReturnHandler @Inject constructor(
    private val preferences: AppPreferences,
    private val sync: EntitlementSync
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /** PayPal approved: record the durable pending flag and start the retried sync. */
    fun onCheckoutReturned() {
        scope.launch {
            runCatching {
                preferences.setPaypalReturnPending(true)
                sync.syncAfterCheckoutReturn()
            }
        }
    }

    /** PayPal cancelled: no grant expected — clear the durable pending flag. */
    fun onCheckoutCancelled() {
        scope.launch {
            runCatching { preferences.setPaypalReturnPending(false) }
        }
    }
}
