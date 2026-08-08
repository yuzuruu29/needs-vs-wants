package com.needsvswants.app.data.entitlement

import com.needsvswants.app.BuildConfig
import com.needsvswants.app.domain.Entitlement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single read path for the app's Pro entitlement.
 *
 * Combines the locally persisted [EntitlementLocalStore] snapshot with an
 * optional Supabase refresh. Consumers should read [isPro] / [entitlement];
 * no cap/purge policy lives here — that stays in [Entitlement] itself.
 *
 * The `plain` test flavor (BuildConfig.PLAIN_FREE) hard-wires Free: whatever a
 * stale DataStore snapshot or remote sync claims, it can never unlock Pro/Max.
 */
@Singleton
class EntitlementRepository @Inject constructor(
    private val local: EntitlementLocalStore,
    private val remote: EntitlementRemote
) {
    /** Persisted local snapshot; FREE when absent. Always Free on the plain flavor. */
    val entitlement: Flow<Entitlement> =
        if (BuildConfig.PLAIN_FREE) flowOf(Entitlement.Free) else local.entitlement

    /** Live pro/not-pro decision derived from the snapshot's own expiry policy. */
    val isPro: Flow<Boolean> = entitlement.map { it.isProAt(System.currentTimeMillis()) }

    /** Reactive decision for active Pro access (Pro or Max tier). */
    val hasProAccess: Flow<Boolean> = entitlement.map { it.hasProAccessAt(System.currentTimeMillis()) }

    /** Reactive decision for active Max access (Max tier only). */
    val hasMaxAccess: Flow<Boolean> = entitlement.map { it.hasMaxAccessAt(System.currentTimeMillis()) }

    /**
     * Pulls the latest entitlement from Supabase and persists it locally.
     * No-op (keeps the local snapshot) while the remote is unconfigured or
     * returns null (offline / no token). No-op entirely on the plain flavor.
     *
     * @param accessToken optional Supabase JWT from [com.needsvswants.app.data.remote.SupabaseAuth.verifyOtp]
     */
    suspend fun refreshFromRemote(accessToken: String? = null) {
        if (BuildConfig.PLAIN_FREE) return
        val fresh = remote.fetchEntitlement(accessToken = accessToken)
        if (fresh != null) local.setEntitlement(fresh)
    }

    suspend fun saveLocal(entitlement: Entitlement) {
        if (BuildConfig.PLAIN_FREE) return
        local.setEntitlement(entitlement)
    }

    suspend fun clearLocal() {
        if (BuildConfig.PLAIN_FREE) return
        local.clearEntitlement()
    }
}