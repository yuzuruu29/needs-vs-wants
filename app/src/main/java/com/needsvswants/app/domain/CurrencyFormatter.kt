package com.needsvswants.app.domain

import kotlin.math.abs

fun Long.toMoney(symbol: String): String {
    val whole = this / 100
    val cents = (abs(this) % 100).toString().padStart(2, '0')
    return "$symbol $whole.$cents"
}

/**
 * Whole major units only (no decimals) — for cramped hero totals on the
 * Summary orb where ".00" eats width and throws alignment off.
 */
fun Long.toMoneyWhole(symbol: String): String {
    val whole = this / 100
    return "$symbol $whole"
}

fun parseCents(input: String): Long? {
    val cleaned = input.replace(",", "").trim()
    if (cleaned.isEmpty()) return null
    val regex = Regex("^\\d+(\\.\\d{0,2})?$")
    if (!regex.matches(cleaned)) return null
    val parts = cleaned.split(".")
    val whole = parts[0].toLongOrNull() ?: return null
    val cents = if (parts.size > 1) {
        val c = parts[1].padEnd(2, '0').take(2)
        c.toIntOrNull() ?: return null
    } else 0
    return whole * 100 + cents
}

/** Keep digits and one decimal point; cap at two decimal places. Shared by Log cost + budget inputs. */
fun filterAmountInput(input: String): String {
    val cleaned = input.filter { it.isDigit() || it == '.' }
    val parts = cleaned.split(".")
    return if (parts.size <= 1) cleaned else parts[0] + "." + parts[1].take(2)
}

/** Render cents back into the amount-input format ("5000", "123.45") for prefill. */
fun Long.toInputAmount(): String {
    val whole = this / 100
    val cents = (abs(this) % 100).toString().padStart(2, '0')
    return if (cents == "00") whole.toString() else "$whole.$cents"
}
