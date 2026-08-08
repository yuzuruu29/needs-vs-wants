package com.needsvswants.app.data.entitlement

import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EntitlementTrustTest {

    private val now = 1_700_000_000_000L
    private val paid = Entitlement(
        tier = EntitlementTier.PRO,
        type = EntitlementType.PAID,
        expiresAtEpochMillis = null
    )

    @Test
    fun unsynced_paid_snapshot_is_not_trusted() {
        assertFalse(localPaidEntitlementTrusted(syncedAtMillis = 0L, nowMillis = now))
        assertEquals(Entitlement.Free, trustedLocalEntitlement(paid, syncedAtMillis = 0L, nowMillis = now))
    }

    @Test
    fun recently_synced_paid_snapshot_is_trusted() {
        val synced = now - 60_000L
        assertTrue(localPaidEntitlementTrusted(synced, now))
        assertEquals(paid, trustedLocalEntitlement(paid, synced, now))
    }

    @Test
    fun grace_boundary_is_strict() {
        val exactlyGrace = now - ENTITLEMENT_SYNC_GRACE_MILLIS
        assertFalse(localPaidEntitlementTrusted(exactlyGrace, now))
        assertTrue(localPaidEntitlementTrusted(exactlyGrace + 1L, now))
    }

    @Test
    fun free_snapshot_passes_through_without_sync() {
        assertEquals(
            Entitlement.Free,
            trustedLocalEntitlement(Entitlement.Free, syncedAtMillis = 0L, nowMillis = now)
        )
    }

    @Test
    fun expired_paid_passes_through_as_expired_not_forced_free_object() {
        val expired = paid.copy(expiresAtEpochMillis = now - 1L)
        val result = trustedLocalEntitlement(expired, syncedAtMillis = 0L, nowMillis = now)
        assertEquals(expired, result)
        assertFalse(result.hasProAccessAt(now))
    }
}
