package com.needsvswants.app.data.billing

/**
 * Parses `paymongo_create_checkout` Edge Function JSON for the checkout URL.
 *
 * Expected:
 * ```
 * { "success": true, "data": { "checkout_url": "https://checkout.paymongo.com/..." } }
 * ```
 * Also accepts a flat `checkout_url` field.
 */
object PayMongoCheckoutJson {

    fun parseCheckoutUrl(json: String): String? {
        if (json.isBlank()) return null
        readUrlField(json, "checkout_url")?.let { return it }
        // Nested data object
        val data = extractObject(json, "data") ?: return null
        return readUrlField(data, "checkout_url")
    }

    fun parseErrorMessage(json: String): String? {
        if (json.isBlank()) return null
        return readStringField(json, "error")
            ?: readStringField(json, "message")
    }

    private fun readUrlField(json: String, key: String): String? {
        val v = readStringField(json, key) ?: return null
        return if (v.startsWith("https://") || v.startsWith("http://")) v else null
    }

    private fun readStringField(json: String, key: String): String? {
        val re = Regex(""""$key"\s*:\s*"((?:\\.|[^"\\])*)"""")
        val m = re.find(json) ?: return null
        return m.groupValues[1]
            .replace("\\/", "/")
            .replace("\\u0026", "&")
            .replace("\\\"", "\"")
    }

    private fun extractObject(json: String, key: String): String? {
        val startPattern = Regex(""""$key"\s*:\s*\{""")
        val match = startPattern.find(json) ?: return null
        val openBrace = match.range.last
        var depth = 0
        for (i in openBrace until json.length) {
            when (json[i]) {
                '{' -> depth++
                '}' -> {
                    depth--
                    if (depth == 0) return json.substring(openBrace, i + 1)
                }
            }
        }
        return null
    }
}