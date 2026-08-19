package com.needsvswants.app.domain

import org.junit.Assert.*
import org.junit.Test

class ReceiptParserTest {

    @Test
    fun parse_emptyString_returnsEmptyResult() {
        val result = ReceiptParser.parse("")
        assertTrue(result.items.isEmpty())
        assertNull(result.storeName)
    }

    @Test
    fun parse_puregoldReceipt_extractsItemsAndPrices() {
        val ocr = """
            PUREGOLD PRICE CLUB INC.
            TIN: 004-444-222-000 VAT REG
            CASHIER: 102 - MARIA
            DATE: 2026-08-16 14:32

            GARDENIA WHEAT BREAD 82.50
            SAN MIG LIGHT 6 CAN 285.00
            DUTCH MILL YOGURT 45.75
            ALASKA EVAP 370ML 38.00

            SUBTOTAL 451.25
            12% VAT 48.35
            TOTAL AMOUNT DUE 451.25
            CASH 500.00
            CHANGE 48.75
            THANK YOU FOR SHOPPING!
        """.trimIndent()

        val result = ReceiptParser.parse(ocr)

        assertEquals("PUREGOLD PRICE CLUB INC", result.storeName)
        assertEquals(4, result.items.size)

        assertEquals("GARDENIA WHEAT BREAD", result.items[0].name)
        assertEquals(8250L, result.items[0].costCents)

        assertEquals("SAN MIG LIGHT 6 CAN", result.items[1].name)
        assertEquals(28500L, result.items[1].costCents)

        assertEquals("DUTCH MILL YOGURT", result.items[2].name)
        assertEquals(4575L, result.items[2].costCents)

        assertEquals("ALASKA EVAP 370ML", result.items[3].name)
        assertEquals(3800L, result.items[3].costCents)

        assertNotNull(result.dateUtc)
    }

    @Test
    fun parse_smSupermarketTwoLineItems_extractsCorrectly() {
        val ocr = """
            SM SUPERMARKET - MEGAMALL
            POS 042  TRANS 88921

            SELECTA ICE CREAM 1.5L
            ₱185.00
            LAYS SOUR CREAM 170G
            ₱135.50
            COCA COLA 1.5L
            ₱75.00

            TOTAL 395.50
            CASH TENDERED 500.00
            CHANGE 104.50
        """.trimIndent()

        val result = ReceiptParser.parse(ocr)

        assertEquals("SM SUPERMARKET - MEGAMALL", result.storeName)
        assertEquals(3, result.items.size)

        assertEquals("SELECTA ICE CREAM 1.5L", result.items[0].name)
        assertEquals(18500L, result.items[0].costCents)

        assertEquals("LAYS SOUR CREAM 170G", result.items[1].name)
        assertEquals(13550L, result.items[1].costCents)

        assertEquals("COCA COLA 1.5L", result.items[2].name)
        assertEquals(7500L, result.items[2].costCents)
    }

    @Test
    fun parse_sevenElevenReceiptWithBarcodes_stripsBarcodes() {
        val ocr = """
            7-ELEVEN STORE 3120
            OR# 4920199

            4800016644211 NESCAFE 3IN1 14.00
            4800092110291 C2 GREEN TEA 500ML 28.00
            1x CORNETTO CHOCO 35.00

            TOTAL AMOUNT: 77.00
            GCASH: 77.00
            APPROVAL: 994021
            THANK YOU
        """.trimIndent()

        val result = ReceiptParser.parse(ocr)

        assertEquals(3, result.items.size)
        assertEquals("NESCAFE 3IN1", result.items[0].name)
        assertEquals(1400L, result.items[0].costCents)

        assertEquals("C2 GREEN TEA 500ML", result.items[1].name)
        assertEquals(2800L, result.items[1].costCents)

        assertEquals("CORNETTO CHOCO", result.items[2].name)
        assertEquals(3500L, result.items[2].costCents)
    }

    @Test
    fun parse_ignoresAllSummaryAndPaymentNoise() {
        val ocr = """
            TIN: 123-456-789
            CASHIER: JANE
            VATABLE SALES: 100.00
            VAT AMOUNT: 12.00
            TOTAL: 112.00
            CASH: 200.00
            CHANGE: 88.00
            CUSTOMER COPY
        """.trimIndent()

        val result = ReceiptParser.parse(ocr)
        assertTrue(result.items.isEmpty())
    }
}
