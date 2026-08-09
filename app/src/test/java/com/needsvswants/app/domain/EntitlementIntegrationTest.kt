package com.needsvswants.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EntitlementIntegrationTest {

    @Test
    fun `free plan restricts retention to 30 days`() {
        val now = InstantMillis
        val free = Entitlement()

        val cutoff = free.retentionCutoffAt(now)
        assertNotNull(cutoff)
        assertEquals(now - 30L * MILLIS_PER_DAY, cutoff)
        assertEquals(20, free.sheetLimitAt(now))
    }

    @Test
    fun `active pro plan has unlimited retention and sheet cap`() {
        val now = InstantMillis
        val pro = Entitlement(
            tier = EntitlementTier.PRO,
            type = EntitlementType.PAID,
            expiresAtEpochMillis = now + 30L * MILLIS_PER_DAY
        )

        assertTrue(pro.isProAt(now))
        assertTrue(pro.hasProAccessAt(now))
        assertFalse(pro.hasMaxAccessAt(now))
        assertNull(pro.retentionCutoffAt(now))
        assertNull(pro.sheetLimitAt(now))
    }

    @Test
    fun `active max plan unlocks max access and pro access`() {
        val now = InstantMillis
        val max = Entitlement(
            tier = EntitlementTier.MAX,
            type = EntitlementType.PAID,
            expiresAtEpochMillis = now + 30L * MILLIS_PER_DAY
        )

        assertTrue(max.isProAt(now))
        assertTrue(max.hasProAccessAt(now))
        assertTrue(max.hasMaxAccessAt(now))
        assertNull(max.retentionCutoffAt(now))
        assertNull(max.sheetLimitAt(now))
    }

    @Test
    fun `expired trial reverts to free plan constraints`() {
        val now = InstantMillis
        val expiredTrial = Entitlement(
            tier = EntitlementTier.PRO,
            type = EntitlementType.TRIAL,
            expiresAtEpochMillis = now - 1000L
        )

        assertFalse(expiredTrial.isProAt(now))
        assertFalse(expiredTrial.hasProAccessAt(now))
        assertFalse(expiredTrial.hasMaxAccessAt(now))
        assertEquals(20, expiredTrial.sheetLimitAt(now))
        assertNotNull(expiredTrial.retentionCutoffAt(now))
    }

    private companion object {
        const val InstantMillis = 1_784_088_000_000L
        const val MILLIS_PER_DAY = 24L * 60L * 60L * 1_000L
    }
}
