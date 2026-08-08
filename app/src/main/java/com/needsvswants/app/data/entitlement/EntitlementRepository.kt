package com.needsvswants.app.data.entitlement

import com.needsvswants.app.BuildConfig
import com.needsvswants.app.domain.Entitlement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single read path for the app's Pro entitlement.
 *
 * Combines the locally persisted [EntitlementLocalStore] snapshot with an
 * optional Supabase refresh. Consumers should read [isPro] / [entitlement];
 * no cap/purge policy lives here — that stays in [Entitlement] itself.
 *
 * Paid local snapshots are only honored when a successful remote sync stamped
 * [EntitlementLocalStore.entitlementSyncedAtMillis] within the offline grace
 * window (see [trustedLocalEntitlement]). That stops casual DataStore edits
 * from unlocking Pro/Max without a server refresh.
 *
 * The `plain` test flavor (BuildConfig.PLAIN_FREE) hard-wires Free: whatever a
 * stale DataStore snapshot or remote sync claims, it can never unlock Pro/Max.
 */
@Singleton
class EntitlementRepository @Inject constructor(
    private val local: EntitlementLocalStore,
    private val remote: EntitlementRemote
) {
    /** Trusted local snapshot; FREE when absent/untrusted. Always Free on plain. */
    val entitlement: Flow<Entitlement> =
        if (BuildConfig.PLAIN_FREE) {
            flowOf(Entitlement.Free)
        } else {
            combine(local.entitlement, local.entitlementSyncedAtMillis) { snapshot, syncedAt ->
                trustedLocalEntitlement(snapshot, syncedAt, System.currentTimeMillis())
            }
        }

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
     * A successful fetch stamps [EntitlementLocalStore.markEntitlementSynced].
     *
     * @param accessToken optional Supabase JWT from [com.needsvswants.app.data.remote.SupabaseAuth.verifyOtp]
     */
    suspend fun refreshFromRemote(accessToken: String? = null) {
        if (BuildConfig.PLAIN_FREE) return
        val fresh = remote.fetchEntitlement(accessToken = accessToken)
        if (fresh != null) {
            local.setEntitlement(fresh)
            local.markEntitlementSynced(System.currentTimeMillis())
        }
    }

    /**
     * Writes the snapshot without a remote sync stamp. Paid values will not
     * unlock until [refreshFromRemote] succeeds (or tests call
     * [EntitlementLocalStore.markEntitlementSynced] explicitly).
     */
    suspend fun saveLocal(entitlement: Entitlement) {
        if (BuildConfig.PLAIN_FREE) return
        local.setEntitlement(entitlement)
    }

    suspend fun clearLocal() {
        if (BuildConfig.PLAIN_FREE) return
        local.clearEntitlement()
    }
}
