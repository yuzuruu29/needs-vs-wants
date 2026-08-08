package com.needsvswants.app.data.entitlement

import com.needsvswants.app.domain.Entitlement
import kotlinx.coroutines.flow.Flow

/**
 * Local persistence seam for entitlement. [AppPreferences] is the production
 * implementation; tests inject an in-memory fake.
 */
interface EntitlementLocalStore {
    val entitlement: Flow<Entitlement>
    /**
     * Epoch millis of the last successful remote entitlement sync.
     * `0` means never synced (paid local snapshots must not be trusted).
     */
    val entitlementSyncedAtMillis: Flow<Long>
    suspend fun setEntitlement(entitlement: Entitlement)
    /** Stamp a successful remote sync; required for paid local snapshots to unlock. */
    suspend fun markEntitlementSynced(atMillis: Long)
    suspend fun clearEntitlement()
}
