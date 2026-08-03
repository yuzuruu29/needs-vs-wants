package com.needsvswants.app.data.entitlement

import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType

/**
 * Pure, DataStore-friendly projection of [Entitlement] into its scalar fields.
 *
 * Kept free of Android types so the type/expiry mapping can be unit-tested in
 * isolation ([EntitlementSnapshotTest]). DataStore persistence maps these scalars
 * to preference keys; the identity (FREE when nothing is persisted) is preserved
 * via [orFree].
 */
data class EntitlementSnapshot(
    val tier: EntitlementTier = EntitlementTier.FREE,
    val type: EntitlementType = EntitlementType.FREE,
    val expiresAtEpochMillis: Long? = null,
    val provider: String? = null,
    val source: String? = null,
    val status: String? = null
) {
    fun toEntitlement(): Entitlement = Entitlement(
        tier = tier,
        type = type,
        expiresAtEpochMillis = expiresAtEpochMillis,
        provider = provider,
        source = source,
        status = status
    )

    companion object {
        fun fromEntitlement(entitlement: Entitlement): EntitlementSnapshot = EntitlementSnapshot(
            tier = entitlement.tier,
            type = entitlement.type,
            expiresAtEpochMillis = entitlement.expiresAtEpochMillis,
            provider = entitlement.provider,
            source = entitlement.source,
            status = entitlement.status
        )

        /** Absent/empty snapshot maps back to the default FREE entitlement. */
        fun orFree(snapshot: EntitlementSnapshot?): Entitlement =
            snapshot?.toEntitlement() ?: Entitlement()
    }
}