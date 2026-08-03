package com.needsvswants.app.data.entitlement

import com.needsvswants.app.domain.Entitlement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single read path for the app's Pro entitlement.
 *
 * Combines the locally persisted [EntitlementLocalStore] snapshot with an
 * optional Supabase refresh. Consumers should read [isPro] / [entitlement];
 * no cap/purge policy lives here — that stays in [Entitlement] itself.
 */
@Singleton
class EntitlementRepository @Inject constructor(
    private val local: EntitlementLocalStore,
    private val remote: EntitlementRemote
) {
    /** Persisted local snapshot; FREE when absent. */
    val entitlement: Flow<Entitlement> = local.entitlement

    /** Live pro/not-pro decision derived from the snapshot's own expiry policy. */
    val isPro: Flow<Boolean> = entitlement.map { it.isProAt(System.currentTimeMillis()) }

    /** Reactive decision for active Pro access (Pro or Max tier). */
    val hasProAccess: Flow<Boolean> = entitlement.map { it.hasProAccessAt(System.currentTimeMillis()) }

    /** Reactive decision for active Max access (Max tier only). */
    val hasMaxAccess: Flow<Boolean> = entitlement.map { it.hasMaxAccessAt(System.currentTimeMillis()) }

    /**
     * Pulls the latest entitlement from Supabase and persists it locally.
     * No-op (keeps the local snapshot) while the remote is unconfigured or
     * returns null (offline / no token).
     *
     * @param accessToken optional Supabase JWT from [com.needsvswants.app.data.remote.SupabaseAuth.verifyOtp]
     */
    suspend fun refreshFromRemote(accessToken: String? = null) {
        val fresh = remote.fetchEntitlement(accessToken = accessToken)
        if (fresh != null) local.setEntitlement(fresh)
    }

    suspend fun saveLocal(entitlement: Entitlement) {
        local.setEntitlement(entitlement)
    }

    suspend fun clearLocal() {
        local.clearEntitlement()
    }
}