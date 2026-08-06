package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType

/**
 * Machine-friendly CSV export. Costs are integer cents (D2) — not formatted money.
 */
object ExportUseCase {

    fun exportCsv(entries: List<Entry>): String {
        val sb = StringBuilder()
        sb.append("date,time,item,cost_cents,type\n")
        for (entry in entries) {
            val typeStr = if (entry.type == EntryType.NEED) "NEED" else "WANT"
            sb.append(csvField(entry.date)).append(',')
            sb.append(csvField(entry.time)).append(',')
            sb.append(csvField(entry.item)).append(',')
            sb.append(entry.costCents).append(',')
            sb.append(typeStr).append('\n')
        }
        return sb.toString()
    }

    /** RFC-style CSV field: quote when needed; double internal quotes. */
    internal fun csvField(raw: String): String {
        val needsQuote = raw.any { it == ',' || it == '"' || it == '\n' || it == '\r' }
        if (!needsQuote) return raw
        return "\"" + raw.replace("\"", "\"\"") + "\""
    }
}
