package com.needsvswants.app.domain

import com.needsvswants.app.data.model.EntryType
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID
import java.util.regex.Pattern

data class ScannedLineItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val costCents: Long,
    val type: EntryType? = null
)

data class ReceiptScanResult(
    val storeName: String? = null,
    val dateUtc: Long? = null,
    val items: List<ScannedLineItem> = emptyList(),
    val rawText: String = ""
)

/**
 * Pure heuristic parser for Philippine store receipts (Puregold, SM, 7-Eleven, Robinsons,
 * Mercury Drug, GCash transaction screenshots, generic POS thermal receipts).
 */
object ReceiptParser {

    private val IGNORE_KEYWORDS = listOf(
        "TIN", "VAT", "MIN ", "SERIAL", "PERMIT", "CASHIER", "TERMINAL", "MACHINE",
        "INVOICE", "OR #", "OR#", "SI #", "SI#", "TRANS #", "TXN", "STORE", "BRANCH",
        "TEL ", "PHONE", "ADDRESS", "WELCOME", "THANK YOU", "COME AGAIN", "POWERED BY",
        "CUSTOMER COPY", "MERCHANT COPY", "OFFICIAL RECEIPT", "SALES INVOICE",
        "TOTAL", "SUBTOTAL", "SUB-TOTAL", "NET AMOUNT", "AMOUNT DUE", "TOTAL DUE",
        "CASH", "CHANGE", "TENDERED", "ROUNDING", "PAYMENT", "MASTERCARD", "VISA",
        "GCASH", "PAYMAYA", "MAYA", "DEBIT", "CREDIT", "APPROVAL", "AUTH",
        "VATABLE", "VAT-EXEMPT", "ZERO RATED", "12%", "TAX", "DISCOUNT", "LESS",
        "ITEMS COUNT", "QTY TOTAL", "ITEMS:", "BAG CHARGE", "REUSABLE BAG"
    )

    // Matches prices like: 125.50, 1,250.00, P45.00, ₱80.00, PHP 99.00, 15.00
    private val PRICE_REGEX = Pattern.compile(
        """(?:PHP|PHP\.|P|₱)?\s*([0-9]{1,3}(?:,[0-9]{3})*(?:\.[0-9]{2})|[0-9]+(?:\.[0-9]{2}))\s*$"""
    )

    // Matches standalone price lines
    private val STANDALONE_PRICE_REGEX = Pattern.compile(
        """^(?:PHP|PHP\.|P|₱)?\s*([0-9]{1,3}(?:,[0-9]{3})*(?:\.[0-9]{2})|[0-9]+(?:\.[0-9]{2}))\s*$"""
    )

    // Matches date patterns: 2026-08-16, 16/08/2026, 16-Aug-2026, etc.
    // Order is load-bearing: dd/MM before MM/dd, so an ambiguous 04/09/2026 on a
    // Philippine receipt reads as 4 September. isLenient = false below rejects 16
    // as a month, so a US-printed 08/16/2026 still falls through to MM/dd/yyyy.
    private val DATE_PATTERNS = listOf(
        "yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy", "yyyy/MM/dd",
        "dd-MMM-yyyy", "MMM dd, yyyy"
    )

    fun parse(rawOcrText: String): ReceiptScanResult {
        if (rawOcrText.isBlank()) {
            return ReceiptScanResult(rawText = rawOcrText)
        }

        val lines = rawOcrText.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        var detectedStore: String? = null
        var detectedDate: Long? = null
        val candidateItems = mutableListOf<ScannedLineItem>()

        // Look for store name in top 4 lines
        for (i in 0 until minOf(4, lines.size)) {
            val line = lines[i]
            if (!isNoiseLine(line) && line.length in 3..40 && !containsPrice(line)) {
                detectedStore = cleanStoreName(line)
                break
            }
        }

        // Look for date in lines
        for (line in lines) {
            val date = tryParseDate(line)
            if (date != null) {
                detectedDate = date
                break
            }
        }

        // Parse line items
        var i = 0
        while (i < lines.size) {
            val currentLine = lines[i]

            if (isNoiseLine(currentLine)) {
                i++
                continue
            }

            // Case 1: Item and Price on the SAME line (e.g. "GARDENIA WHEAT 82.50")
            val sameLineMatch = matchSameLineItem(currentLine)
            if (sameLineMatch != null) {
                if (isValidItem(sameLineMatch)) {
                    candidateItems.add(sameLineMatch)
                }
                i++
                continue
            }

            // Case 2: Item on current line, Price on NEXT line (e.g. "SELECTA ICE CREAM" \n "185.00")
            if (i + 1 < lines.size) {
                val nextLine = lines[i + 1]
                val standalonePrice = matchStandalonePrice(nextLine)
                if (standalonePrice != null && !isNoiseLine(currentLine)) {
                    val cleanedName = cleanItemName(currentLine)
                    if (cleanedName.length >= 2 && !containsPrice(cleanedName)) {
                        val item = ScannedLineItem(
                            name = cleanedName,
                            costCents = standalonePrice
                        )
                        if (isValidItem(item)) {
                            candidateItems.add(item)
                            i += 2
                            continue
                        }
                    }
                }
            }

            i++
        }

        return ReceiptScanResult(
            storeName = detectedStore,
            dateUtc = detectedDate,
            items = candidateItems,
            rawText = rawOcrText
        )
    }

    private fun matchSameLineItem(line: String): ScannedLineItem? {
        val matcher = PRICE_REGEX.matcher(line)
        if (matcher.find()) {
            val priceStr = matcher.group(1) ?: return null
            val rawName = line.substring(0, matcher.start()).trim()
            val cleanedName = cleanItemName(rawName)
            val costCents = parsePriceToCents(priceStr) ?: return null

            if (cleanedName.length >= 2 && costCents > 0L) {
                return ScannedLineItem(
                    name = cleanedName,
                    costCents = costCents
                )
            }
        }
        return null
    }

    private fun matchStandalonePrice(line: String): Long? {
        val matcher = STANDALONE_PRICE_REGEX.matcher(line)
        if (matcher.matches()) {
            val priceStr = matcher.group(1) ?: return null
            return parsePriceToCents(priceStr)
        }
        return null
    }

    private fun parsePriceToCents(priceString: String): Long? {
        val normalized = priceString.replace(",", "").trim()
        val parts = normalized.split(".")
        val whole = parts[0].toLongOrNull() ?: return null
        val frac = if (parts.size > 1) {
            val f = parts[1].padEnd(2, '0').take(2)
            f.toIntOrNull() ?: 0
        } else {
            0
        }
        return whole * 100L + frac
    }

    private fun cleanItemName(rawName: String): String {
        var name = rawName
        // Strip leading item barcodes or long digits
        name = name.replace(Regex("""^\d{6,}\s*"""), "")
        // Strip leading quantity like "1x ", "2 ", "3 @ "
        name = name.replace(Regex("""^\d+\s*[xX@]\s*"""), "")
        name = name.replace(Regex("""^\d+\s+"""), "")
        // Strip trailing quantity / multiplier tokens
        name = name.replace(Regex("""\s+\d+\s*[xX@].*$"""), "")
        // Strip non-alphanumeric noise except common symbols
        name = name.replace(Regex("""[^\p{L}\p{N}\s\-.,'&/]"""), "")
        // Normalize whitespace
        name = name.replace(Regex("""\s+"""), " ").trim()
        return name
    }

    private fun cleanStoreName(raw: String): String {
        return raw.replace(Regex("""[^\p{L}\p{N}\s\-.,'&]"""), "").trim().trimEnd('.', ',', '-', ':')
    }

    private fun isNoiseLine(line: String): Boolean {
        val upper = line.uppercase(Locale.US)
        if (IGNORE_KEYWORDS.any { upper.contains(it) }) {
            return true
        }
        // Pure symbols or very short lines
        if (line.replace(Regex("""[^a-zA-Z0-9]"""), "").length < 2) {
            return true
        }
        return false
    }

    private fun containsPrice(line: String): Boolean {
        return PRICE_REGEX.matcher(line).find()
    }

    private fun isValidItem(item: ScannedLineItem): Boolean {
        if (item.name.isBlank() || item.name.length < 2) return false
        if (item.costCents <= 0L) return false
        val upper = item.name.uppercase(Locale.US)
        if (IGNORE_KEYWORDS.any { upper == it || upper.startsWith("$it ") || upper.endsWith(" $it") }) {
            return false
        }
        return true
    }

    private fun tryParseDate(line: String): Long? {
        val dateMatch = Regex("""\b(\d{4}[-/]\d{1,2}[-/]\d{1,2}|\d{1,2}[-/]\d{1,2}[-/]\d{4}|\d{1,2}-[A-Za-z]{3}-\d{4})\b""").find(line)
        if (dateMatch != null) {
            val dateStr = dateMatch.value
            for (pattern in DATE_PATTERNS) {
                try {
                    val format = SimpleDateFormat(pattern, Locale.US)
                    format.isLenient = false
                    val parsed = format.parse(dateStr)
                    if (parsed != null) {
                        return parsed.time
                    }
                } catch (_: Exception) {
                }
            }
        }
        return null
    }
}
