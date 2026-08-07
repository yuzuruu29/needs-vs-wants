package com.needsvswants.app.data.billing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PayPalCheckoutJsonTest {

    @Test
    fun parseApprovalUrl_nestedData() {
        val json = """
            {"success":true,"data":{"approval_url":"https://www.paypal.com/webapps/billing/subscriptions?ba_token=x"}}
        """.trimIndent()
        assertEquals(
            "https://www.paypal.com/webapps/billing/subscriptions?ba_token=x",
            PayPalCheckoutJson.parseApprovalUrl(json)
        )
    }

    @Test
    fun parseApprovalUrl_flat() {
        val json = """{"approval_url":"https://www.sandbox.paypal.com/checkoutnow?token=1"}"""
        assertEquals(
            "https://www.sandbox.paypal.com/checkoutnow?token=1",
            PayPalCheckoutJson.parseApprovalUrl(json)
        )
    }

    @Test
    fun parseApprovalUrl_rejectsNonHttp() {
        assertNull(PayPalCheckoutJson.parseApprovalUrl("""{"approval_url":"javascript:alert(1)"}"""))
    }

    @Test
    fun parseErrorMessage() {
        assertEquals("nope", PayPalCheckoutJson.parseErrorMessage("""{"success":false,"error":"nope"}"""))
    }
}
