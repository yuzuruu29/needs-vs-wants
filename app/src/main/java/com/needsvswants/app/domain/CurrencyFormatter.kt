package com.needsvswants.app.domain

import kotlin.math.abs

fun Long.toMoney(symbol: String): String {
    val whole = this / 100
    val cents = (abs(this) % 100).toString().padStart(2, '0')
    return "$symbol $whole.$cents"
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
