package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType

/**
 * Round-trip partner of [ExportUseCase]. Parses the machine-friendly CSV format
 * `date,time,item,cost_cents,type` back into [Entry]s.
 *
 * Tolerant parser: a header row is skipped, rows are matched by column position
 * (not by name), quotes are handled with the same RFC rules [ExportUseCase] emits,
 * and malformed rows are skipped rather than failing the whole import. The count
 * of skipped rows is surfaced so the UI can tell the user what was (and wasn't)
 * imported. Costs are integer cents (D2) — never formatted money.
 */
object ImportUseCase {

    data class Result(
        val entries: List<Entry>,
        val skippedCount: Int
    ) {
        val isEmpty: Boolean get() = entries.isEmpty()
    }

    private const val COL_COUNT = 5
    private const val HEADER_DATE = "date"

    fun parseCsv(text: String): Result {
        val entries = ArrayList<Entry>()
        var skipped = 0

        val lines = text.split("\n")
        val firstDataIndex = firstDataLineIndex(lines)
        for ((index, rawLine) in lines.withIndex()) {
            val line = rawLine.trimEnd('\r')
            if (line.isBlank()) continue

            val fields = splitCsvLine(line)
            if (fields.size < COL_COUNT) {
                skipped++
                continue
            }

            // First non-blank line: skip a header row (e.g. "date,time,item,...").
            // A header is not a malformed row — it must not count toward skippedCount.
            if (index == firstDataIndex && fields[0].equals(HEADER_DATE, ignoreCase = true)) {
                continue
            }

            val entry = buildEntry(fields)
            if (entry == null) {
                skipped++
            } else {
                entries.add(entry)
            }
        }

        return Result(entries, skipped)
    }

    private fun firstDataLineIndex(lines: List<String>): Int {
        for ((i, l) in lines.withIndex()) {
            if (!l.isBlank()) return i
        }
        return 0
    }

    private fun buildEntry(fields: List<String>): Entry? {
        val date = fields[0].trim()
        val time = fields[1].trim()
        val item = fields[2].trim()
        val costCents = fields[3].trim().toLongOrNull() ?: return null
        val type = when (fields[4].trim().uppercase()) {
            "NEED" -> EntryType.NEED
            "WANT" -> EntryType.WANT
            else -> return null
        }
        if (date.isEmpty() || item.isEmpty() || costCents < 0) return null

        // Rebuild dateUtc from the yyyy-MM-dd + HH:mm fields so the entry sorts
        // and retains correctly (ImportUseCase never claims knowledge of the
        // original epoch; the exported format only carries display strings).
        val dateUtc = parseDateTimeUtc(date, time) ?: return null

        return Entry(
            id = 0, // Room auto-generates; import is always a fresh insert.
            dateUtc = dateUtc,
            date = date,
            time = time,
            item = item,
            costCents = costCents,
            type = type
        )
    }

    /**
     * Best-effort rebuild of a UTC epoch from the exported `date` (yyyy-MM-dd) +
     * `time` (HH:mm) strings. Returns null when unparseable so the row is skipped.
     */
    internal fun parseDateTimeUtc(date: String, time: String): Long? {
        val dateParts = date.split("-")
        if (dateParts.size != 3) return null
        val year = dateParts[0].toIntOrNull() ?: return null
        val month = dateParts[1].toIntOrNull() ?: return null
        val day = dateParts[2].toIntOrNull() ?: return null

        val timeParts = time.split(":")
        val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: return null
        val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: return null

        if (year < 2000 || month !in 1..12 || day !in 1..31) return null
        if (hour !in 0..23 || minute !in 0..59) return null

        // Date-only arithmetic (no timezone/DST): interpret as UTC. The app renders
        // by display string, so parity is what matters for history grouping.
        val days = daysFromCivil(year, month, day)
        return days * 86_400_000L + hour * 3_600_000L + minute * 60_000L
    }

    /** Days since 1970-01-01 for a civil date (Howard Hinnant's algorithm). */
    internal fun daysFromCivil(year: Int, month: Int, day: Int): Long {
        val y = if (month <= 2) year - 1 else year
        val era = (if (y >= 0) y else y - 399) / 400
        val yoe = y - era * 400
        val mp = (month + 9) % 12
        val doy = (153 * mp + 2) / 5 + day - 1
        val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
        return era * 146097L + doe - 719468L
    }

    /**
     * Split one CSV line into fields, honoring double-quoted fields and doubled
     * quotes inside them (RFC 4180). Unquoted fields may still contain commas.
     */
    internal fun splitCsvLine(line: String): List<String> {
        val fields = ArrayList<String>(COL_COUNT)
        val sb = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val ch = line[i]
            when {
                ch == '"' -> {
                    if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                        sb.append('"')
                        i += 2
                        continue
                    }
                    inQuotes = !inQuotes
                    i++
                }
                ch == ',' && !inQuotes -> {
                    fields.add(sb.toString())
                    sb.setLength(0)
                    i++
                }
                else -> {
                    sb.append(ch)
                    i++
                }
            }
        }
        fields.add(sb.toString())
        return fields
    }
}