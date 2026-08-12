package com.needsvswants.app.data.billing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PayMongoCheckoutJsonTest {

    @Test
    fun parseCheckoutUrl_nestedData() {
        val json = """
            {"success":true,"data":{"checkout_url":"https://checkout.paymongo.com/checkout/abc123","checkout_session_id":"cs_123","tier":"pro","amount_centavos":4900}}
        """.trimIndent()
        assertEquals(
            "https://checkout.paymongo.com/checkout/abc123",
            PayMongoCheckoutJson.parseCheckoutUrl(json)
        )
    }

    @Test
    fun parseCheckoutUrl_flat() {
        val json = """{"checkout_url":"https://checkout.paymongo.com/px/xyz"}"""
        assertEquals(
            "https://checkout.paymongo.com/px/xyz",
            PayMongoCheckoutJson.parseCheckoutUrl(json)
        )
    }

    @Test
    fun parseCheckoutUrl_rejectsNonHttp() {
        assertNull(PayMongoCheckoutJson.parseCheckoutUrl("""{"checkout_url":"javascript:alert(1)"}"""))
    }

    @Test
    fun parseCheckoutUrl_blankReturnsNull() {
        assertNull(PayMongoCheckoutJson.parseCheckoutUrl(""))
    }

    @Test
    fun parseErrorMessage_readsError() {
        assertEquals(
            "declined",
            PayMongoCheckoutJson.parseErrorMessage("""{"success":false,"error":"declined"}""")
        )
    }

    @Test
    fun parseErrorMessage_fallsBackToMessage() {
        assertEquals(
            "missing tier",
            PayMongoCheckoutJson.parseErrorMessage("""{"success":false,"message":"missing tier"}""")
        )
    }
}