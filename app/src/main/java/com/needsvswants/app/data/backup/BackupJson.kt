package com.needsvswants.app.data.backup

/**
 * Minimal JSON reader/writer used only by the backup envelope codec.
 *
 * Hand-rolled on purpose: `org.json` is a throwing stub in JVM unit tests and
 * the project avoids new third-party deps (offline-build rule) — the same
 * trade-off `PayMongoCheckoutJson` makes, but the envelope needs real
 * array/object parsing rather than regex field reads. Supports the full JSON
 * grammar; integral numbers surface as [Long], others as [Double].
 */
internal object BackupJson {

    /** Parses [text] into Map<String, Any?> / List<Any?> / String / Long / Double / Boolean / null. */
    fun parse(text: String): Any? {
        val parser = Parser(text)
        val value = parser.parseValue()
        parser.skipWhitespace()
        require(parser.atEnd) { "Unexpected trailing JSON at offset ${parser.pos}" }
        return value
    }

    fun escape(value: String): String = buildString(value.length + 8) {
        for (c in value) {
            when {
                c == '"' -> append("\\\"")
                c == '\\' -> append("\\\\")
                c == '\n' -> append("\\n")
                c == '\r' -> append("\\r")
                c == '\t' -> append("\\t")
                c < ' ' -> append("\\u").append(c.code.toString(16).padStart(4, '0'))
                else -> append(c)
            }
        }
    }

    private class Parser(private val s: String) {
        var pos = 0
        val atEnd: Boolean get() = pos >= s.length

        fun skipWhitespace() {
            while (pos < s.length && s[pos].isWhitespace()) pos++
        }

        fun parseValue(): Any? {
            skipWhitespace()
            require(pos < s.length) { "Unexpected end of JSON" }
            return when (s[pos]) {
                '{' -> parseObject()
                '[' -> parseArray()
                '"' -> parseString()
                't' -> literal("true", true)
                'f' -> literal("false", false)
                'n' -> literal("null", null)
                else -> parseNumber()
            }
        }

        private fun <T> literal(text: String, value: T): T {
            require(s.startsWith(text, pos)) { "Invalid JSON literal at offset $pos" }
            pos += text.length
            return value
        }

        private fun parseObject(): Map<String, Any?> {
            expect('{')
            val out = LinkedHashMap<String, Any?>()
            skipWhitespace()
            if (peek() == '}') { pos++; return out }
            while (true) {
                skipWhitespace()
                val key = parseString()
                skipWhitespace()
                expect(':')
                out[key] = parseValue()
                skipWhitespace()
                when (peek()) {
                    ',' -> pos++
                    '}' -> { pos++; return out }
                    else -> throw IllegalArgumentException("Expected ',' or '}' at offset $pos")
                }
            }
        }

        private fun parseArray(): List<Any?> {
            expect('[')
            val out = ArrayList<Any?>()
            skipWhitespace()
            if (peek() == ']') { pos++; return out }
            while (true) {
                out.add(parseValue())
                skipWhitespace()
                when (peek()) {
                    ',' -> pos++
                    ']' -> { pos++; return out }
                    else -> throw IllegalArgumentException("Expected ',' or ']' at offset $pos")
                }
            }
        }

        private fun parseString(): String {
            expect('"')
            val sb = StringBuilder()
            while (true) {
                require(pos < s.length) { "Unterminated JSON string" }
                when (val c = s[pos++]) {
                    '"' -> return sb.toString()
                    '\\' -> {
                        require(pos < s.length) { "Unterminated escape" }
                        when (val e = s[pos++]) {
                            '"' -> sb.append('"')
                            '\\' -> sb.append('\\')
                            '/' -> sb.append('/')
                            'b' -> sb.append('\b')
                            'f' -> sb.append('\u000C')
                            'n' -> sb.append('\n')
                            'r' -> sb.append('\r')
                            't' -> sb.append('\t')
                            'u' -> {
                                require(pos + 4 <= s.length) { "Bad \\u escape" }
                                sb.append(s.substring(pos, pos + 4).toInt(16).toChar())
                                pos += 4
                            }
                            else -> throw IllegalArgumentException("Bad escape '\\$e' at offset $pos")
                        }
                    }
                    else -> sb.append(c)
                }
            }
        }

        private fun parseNumber(): Any {
            val start = pos
            if (peek() == '-') pos++
            while (pos < s.length && (s[pos].isDigit() || s[pos] in ".eE+-")) pos++
            val raw = s.substring(start, pos)
            require(raw.isNotEmpty()) { "Invalid JSON number at offset $start" }
            return if (raw.none { it in ".eE" }) {
                raw.toLongOrNull() ?: throw IllegalArgumentException("Bad integer '$raw'")
            } else {
                raw.toDoubleOrNull() ?: throw IllegalArgumentException("Bad number '$raw'")
            }
        }

        private fun peek(): Char {
            require(pos < s.length) { "Unexpected end of JSON" }
            return s[pos]
        }

        private fun expect(c: Char) {
            require(pos < s.length && s[pos] == c) { "Expected '$c' at offset $pos" }
            pos++
        }
    }
}
