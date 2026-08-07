package com.needsvswants.app.data.entitlement

import kotlinx.coroutines.flow.Flow

/**
 * Durable "PayPal checkout return pending" state, backed by DataStore via
 * [com.needsvswants.app.data.prefs.AppPreferences]. Survives process death so
 * a missed deep-link return re-syncs on the next cold start, and is cleared on
 * cancel, on confirmed Pro, and on sign-out.
 */
interface PayPalReturnStore {
    /** True while a PayPal checkout return is pending but not yet confirmed. */
    val paypalReturnPending: Flow<Boolean>

    suspend fun setPaypalReturnPending(pending: Boolean)

    suspend fun clearPaypalReturnPending()
}
