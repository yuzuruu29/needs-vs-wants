package com.needsvswants.app.data.billing

/**
 * Parses `paypal_create_subscription` Edge Function JSON for the approval URL.
 *
 * Expected:
 * ```
 * { "success": true, "data": { "approval_url": "https://www.paypal.com/..." } }
 * ```
 * Also accepts a flat `approval_url` field.
 */
object PayPalCheckoutJson {

    fun parseApprovalUrl(json: String): String? {
        if (json.isBlank()) return null
        readUrlField(json, "approval_url")?.let { return it }
        // Nested data object
        val data = extractObject(json, "data") ?: return null
        return readUrlField(data, "approval_url")
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
