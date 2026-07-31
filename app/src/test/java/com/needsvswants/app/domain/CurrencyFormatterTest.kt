package com.needsvswants.app.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyFormatterTest {

    @Test
    fun filterAmountInput_keepsDigitsAndOneDot() {
        assertEquals("12.50", filterAmountInput("12.50"))
        assertEquals("12345", filterAmountInput("12345"))
        assertEquals("0.5", filterAmountInput("0.5"))
    }

    @Test
    fun filterAmountInput_removesNonNumericChars() {
        assertEquals("123.45", filterAmountInput("1,23.a4b5"))
        assertEquals("7", filterAmountInput("₱7"))
    }

    @Test
    fun filterAmountInput_allowsAtMostTwoDecimals() {
        assertEquals("12.34", filterAmountInput("12.345"))
        assertEquals("1.23", filterAmountInput("1.234567"))
    }

    @Test
    fun filterAmountInput_keepsOnlyFirstDot() {
        assertEquals("1.23", filterAmountInput("1.23.45"))
        assertEquals("0.", filterAmountInput("0.."))
    }

    @Test
    fun filterAmountInput_handlesEmptyAndGarbage() {
        assertEquals("", filterAmountInput(""))
        assertEquals("", filterAmountInput("abc"))
        assertEquals(".", filterAmountInput("."))
    }

    @Test
    fun toInputAmount_formatsWholeAndDecimalCents() {
        assertEquals("5000", 500_000L.toInputAmount())
        assertEquals("123.45", 12_345L.toInputAmount())
        assertEquals("5", 500L.toInputAmount())
        assertEquals("1.05", 105L.toInputAmount())
        assertEquals("0.09", 9L.toInputAmount())
    }
}
