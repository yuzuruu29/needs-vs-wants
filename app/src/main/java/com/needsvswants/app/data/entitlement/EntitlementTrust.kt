package com.needsvswants.app.data.entitlement

import com.needsvswants.app.domain.Entitlement

/**
 * Pure trust gate for the locally cached entitlement snapshot.
 *
 * A rooted device can edit DataStore prefs and fake Pro/Max. We only honor a
 * paid local snapshot when a successful *remote* sync stamped [syncedAtMillis]
 * within [graceMillis]. Unstamped or stale paid snapshots degrade to Free
 * until the next successful [EntitlementRepository.refreshFromRemote].
 */
fun localPaidEntitlementTrusted(
    syncedAtMillis: Long,
    nowMillis: Long,
    graceMillis: Long = ENTITLEMENT_SYNC_GRACE_MILLIS
): Boolean = syncedAtMillis > 0L && nowMillis - syncedAtMillis < graceMillis

/**
 * Returns [snapshot] when free/expired, or when paid *and* recently synced.
 * Otherwise returns [Entitlement.Free].
 */
fun trustedLocalEntitlement(
    snapshot: Entitlement,
    syncedAtMillis: Long,
    nowMillis: Long,
    graceMillis: Long = ENTITLEMENT_SYNC_GRACE_MILLIS
): Entitlement {
    if (!snapshot.hasProAccessAt(nowMillis)) return snapshot
    return if (localPaidEntitlementTrusted(syncedAtMillis, nowMillis, graceMillis)) {
        snapshot
    } else {
        Entitlement.Free
    }
}

/** 7-day offline grace after a successful remote entitlement sync. */
const val ENTITLEMENT_SYNC_GRACE_MILLIS: Long = 7L * 24L * 60L * 60L * 1_000L
