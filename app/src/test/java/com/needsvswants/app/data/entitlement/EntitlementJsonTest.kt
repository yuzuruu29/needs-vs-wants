package com.needsvswants.app.data.entitlement

import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EntitlementJsonTest {

    @Test
    fun parse_proPlan_withPaidUntil() {
        val json = """
            {
              "success": true,
              "data": {
                "is_pro": true,
                "plan": "pro",
                "trial_ends_at": null,
                "paid_until": "2030-01-01T00:00:00.000Z",
                "provider": "google_play",
                "source": "purchase",
                "status": "active"
              }
            }
        """.trimIndent()

        val ent = EntitlementJson.parseGetEntitlementResponse(json)
        assertNotNull(ent)
        assertEquals(EntitlementTier.PRO, ent!!.tier)
        assertEquals(EntitlementType.PAID, ent.type)
        assertEquals("google_play", ent.provider)
        assertTrue(ent.isProAt(System.currentTimeMillis()))
    }

    @Test
    fun parse_maxTier() {
        val json = """
            {"success":true,"data":{"is_pro":true,"plan":"max","tier":"max","paid_until":"2030-06-01T00:00:00.000Z","provider":"apple","source":null,"status":"active"}}
        """.trimIndent()

        val ent = EntitlementJson.parseGetEntitlementResponse(json)!!
        assertEquals(EntitlementTier.MAX, ent.tier)
        assertTrue(ent.hasMaxAccessAt(System.currentTimeMillis()))
    }

    @Test
    fun parse_paypalLiveShape_isProWithPaidUntilAndNullTrial() {
        // Live get_entitlement envelope after a PayPal webhook grant: is_pro +
        // paid_until present, trial fields null/absent (no trial started).
        val json = """
            {"success":true,"data":{
              "is_pro": true,
              "plan": "pro",
              "tier": "pro",
              "trial_started_at": null,
              "trial_ends_at": null,
              "paid_until": "2030-01-01T00:00:00.000Z",
              "provider": "paypal",
              "source": "paypal_webhook",
              "status": "active"
            }}
        """.trimIndent()

        val ent = EntitlementJson.parseGetEntitlementResponse(json)!!
        assertEquals(EntitlementTier.PRO, ent.tier)
        assertEquals(EntitlementType.PAID, ent.type)
        assertEquals("paypal", ent.provider)
        assertTrue(ent.isProAt(System.currentTimeMillis()))
        assertTrue(ent.hasProAccessAt(System.currentTimeMillis()))
    }

    @Test
    fun parse_freePlan_returnsDefaultFree() {
        val json = """{"success":true,"data":{"is_pro":false,"plan":"free","paid_until":null,"trial_ends_at":null}}"""
        val ent = EntitlementJson.parseGetEntitlementResponse(json)!!
        assertFalse(ent.isProAt(System.currentTimeMillis()))
        assertEquals(EntitlementTier.FREE, ent.tier)
    }

    @Test
    fun parse_blank_returnsNull() {
        assertNull(EntitlementJson.parseGetEntitlementResponse(""))
    }
}
