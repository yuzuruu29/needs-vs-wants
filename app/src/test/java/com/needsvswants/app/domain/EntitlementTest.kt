package com.needsvswants.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EntitlementTest {

    @Test
    fun freeDefaults_keepSheetLimitAndRetention() {
        val entitlement = Entitlement()

        assertFalse(entitlement.isProAt(NOW))
        assertEquals(FREE_SHEET_LIMIT, entitlement.sheetLimitAt(NOW))
        assertEquals(NOW - FREE_RETENTION_MILLIS, entitlement.retentionCutoffAt(NOW))
    }

    @Test
    fun activePaidEntitlement_isPro() {
        val entitlement = Entitlement(
            type = EntitlementType.PAID,
            expiresAtEpochMillis = NOW + ONE_DAY_MILLIS
        )

        assertTrue(entitlement.isProAt(NOW))
    }

    @Test
    fun activeThreeDayTrial_isPro() {
        val entitlement = Entitlement(
            type = EntitlementType.TRIAL,
            expiresAtEpochMillis = NOW + THREE_DAYS_MILLIS
        )

        assertTrue(entitlement.isProAt(NOW))
    }

    @Test
    fun paidEntitlement_atExpiryBoundary_isNotPro() {
        val entitlement = Entitlement(
            type = EntitlementType.PAID,
            expiresAtEpochMillis = NOW
        )

        assertFalse(entitlement.isProAt(NOW))
    }

    @Test
    fun trialEntitlement_atExpiryBoundary_isNotPro() {
        val entitlement = Entitlement(
            type = EntitlementType.TRIAL,
            expiresAtEpochMillis = NOW + THREE_DAYS_MILLIS
        )

        assertFalse(entitlement.isProAt(NOW + THREE_DAYS_MILLIS))
    }

    @Test
    fun paidEntitlement_withoutExpiry_isPro() {
        val entitlement = Entitlement(
            type = EntitlementType.PAID,
            expiresAtEpochMillis = null
        )

        assertTrue(entitlement.isProAt(NOW + ONE_DAY_MILLIS))
    }

    @Test
    fun trialEntitlement_withoutExpiry_isPro() {
        val entitlement = Entitlement(
            type = EntitlementType.TRIAL,
            expiresAtEpochMillis = null
        )

        assertTrue(entitlement.isProAt(NOW + ONE_DAY_MILLIS))
    }

    @Test
    fun proEntitlement_hasUnlimitedSheetLimit() {
        val entitlement = activePaidEntitlement()

        assertNull(entitlement.sheetLimitAt(NOW))
    }

    @Test
    fun proEntitlement_hasUnlimitedRetention() {
        val entitlement = activePaidEntitlement()

        assertNull(entitlement.retentionCutoffAt(NOW))
    }

    @Test
    fun freeRetentionCutoff_usesExactThirtyDayWindow() {
        val entitlement = Entitlement()

        assertEquals(NOW - 30L * MILLIS_PER_DAY, entitlement.retentionCutoffAt(NOW))
    }

    private fun activePaidEntitlement(): Entitlement = Entitlement(
        type = EntitlementType.PAID,
        expiresAtEpochMillis = NOW + ONE_DAY_MILLIS
    )

    private companion object {
        const val NOW = 1_700_000_000_000L
        const val MILLIS_PER_DAY = 24L * 60L * 60L * 1_000L
        const val ONE_DAY_MILLIS = MILLIS_PER_DAY
        const val THREE_DAYS_MILLIS = 3L * MILLIS_PER_DAY
        const val FREE_RETENTION_MILLIS = 30L * MILLIS_PER_DAY
        const val FREE_SHEET_LIMIT = 20
    }
}
