package com.needsvswants.app.data.backup

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType

/**
 * Versioned local-backup envelope (audit gap: "lifetime history" had no
 * backup story). Pure JVM code — all format logic is unit-tested; the SAF /
 * Android side lives in [BackupService].
 *
 * Format v2 adds a `dailyBudgets` list keyed by local calendar day. The v1
 * `prefs.dailyBudgetCents` field remains readable as a current-day fallback.
 * ```json
 * {
 *   "format": "nvw-backup",
 *   "schemaVersion": 2,
 *   "appVersionName": "2.0.14",
 *   "appVersionCode": 22,
 *   "exportedAtEpochMillis": 1765584000000,
 *   "prefs": { "currencySymbol": "₱", "currencyCode": "PHP",
 *              "dailyBudgetCents": 50000, "reminderEnabled": true,
 *              "reminderHour": 20, "spendingGoal": "track" },
 *   "dailyBudgets": [ { "dayKey": "2026-08-13", "budgetCents": 50000 } ],
 *   "entries": [ { "dateUtc": 1765584000000, "date": "2026-08-13",
 *                  "time": "07:30 PM", "item": "Coffee",
 *                  "costCents": 15000, "type": "WANT" } ]
 * }
 * ```
 * Entry ids are intentionally NOT exported — restore inserts fresh rows and
 * dedupes by content ([BackupEntry.contentKey]).
 */
data class BackupEnvelope(
    val schemaVersion: Int,
    val appVersionName: String,
    val appVersionCode: Int,
    val exportedAtEpochMillis: Long,
    val prefs: BackupPrefs?,
    val entries: List<BackupEntry>,
    val dailyBudgets: List<BackupDailyBudget> = emptyList()
)

data class BackupPrefs(
    val currencySymbol: String,
    val currencyCode: String,
    val dailyBudgetCents: Long?,
    val reminderEnabled: Boolean,
    val reminderHour: Int,
    val spendingGoal: String
)

data class BackupDailyBudget(
    val dayKey: String,
    val budgetCents: Long
)

data class BackupEntry(
    val dateUtc: Long,
    val date: String,
    val time: String,
    val item: String,
    val costCents: Long,
    val type: EntryType
) {
    /** Content identity used for restore dedupe (ids are not portable). */
    fun contentKey(): String = "$dateUtc|$item|$costCents|${type.name}"

    fun toEntry(): Entry = Entry(
        dateUtc = dateUtc, date = date, time = time,
        item = item, costCents = costCents, type = type
    )

    companion object {
        fun fromEntry(e: Entry): BackupEntry = BackupEntry(
            dateUtc = e.dateUtc, date = e.date, time = e.time,
            item = e.item, costCents = e.costCents, type = e.type
        )
    }
}

object BackupEnvelopeCodec {
    const val FORMAT = "nvw-backup"
    const val SCHEMA_VERSION = 2

    fun toJson(envelope: BackupEnvelope): String = buildString(envelope.entries.size * 96 + 512) {
        append("{\"format\":\"").append(FORMAT).append("\",")
        append("\"schemaVersion\":").append(envelope.schemaVersion).append(',')
        append("\"appVersionName\":\"").append(BackupJson.escape(envelope.appVersionName)).append("\",")
        append("\"appVersionCode\":").append(envelope.appVersionCode).append(',')
        append("\"exportedAtEpochMillis\":").append(envelope.exportedAtEpochMillis).append(',')
        envelope.prefs?.let { p ->
            append("\"prefs\":{")
            append("\"currencySymbol\":\"").append(BackupJson.escape(p.currencySymbol)).append("\",")
            append("\"currencyCode\":\"").append(BackupJson.escape(p.currencyCode)).append("\",")
            p.dailyBudgetCents?.let { append("\"dailyBudgetCents\":").append(it).append(',') }
            append("\"reminderEnabled\":").append(p.reminderEnabled).append(',')
            append("\"reminderHour\":").append(p.reminderHour).append(',')
            append("\"spendingGoal\":\"").append(BackupJson.escape(p.spendingGoal)).append("\"},")
        }
        append("\"dailyBudgets\":[")
        envelope.dailyBudgets.forEachIndexed { i, budget ->
            if (i > 0) append(',')
            append("{\"dayKey\":\"")
                .append(BackupJson.escape(budget.dayKey))
                .append("\",\"budgetCents\":")
                .append(budget.budgetCents)
                .append('}')
        }
        append("],\"entries\":[")
        envelope.entries.forEachIndexed { i, e ->
            if (i > 0) append(',')
            append("{\"dateUtc\":").append(e.dateUtc).append(',')
            append("\"date\":\"").append(BackupJson.escape(e.date)).append("\",")
            append("\"time\":\"").append(BackupJson.escape(e.time)).append("\",")
            append("\"item\":\"").append(BackupJson.escape(e.item)).append("\",")
            append("\"costCents\":").append(e.costCents).append(',')
            append("\"type\":\"").append(e.type.name).append("\"}")
        }
        append("]}")
    }

    /** Throws [IllegalArgumentException] with a user-presentable reason on any format problem. */
    fun fromJson(text: String): BackupEnvelope {
        val root = BackupJson.parse(text) as? Map<*, *>
            ?: throw IllegalArgumentException("Not a Needs vs Wants backup file")
        require(root["format"] == FORMAT) { "Not a Needs vs Wants backup file" }
        val schemaVersion = (root["schemaVersion"] as? Long)?.toInt()
            ?: throw IllegalArgumentException("Backup file has no schema version")
        require(schemaVersion in 1..SCHEMA_VERSION) {
            "Backup was made by a newer app version — update the app first"
        }
        val entriesRaw = root["entries"] as? List<*>
            ?: throw IllegalArgumentException("Backup file has no entries list")
        val entries = entriesRaw.mapIndexed { i, raw ->
            val m = raw as? Map<*, *>
                ?: throw IllegalArgumentException("Entry $i is malformed")
            BackupEntry(
                dateUtc = m.longField("dateUtc", i),
                date = m.stringField("date", i),
                time = m.stringField("time", i),
                item = m.stringField("item", i),
                costCents = m.longField("costCents", i),
                type = when (val t = m["type"]) {
                    EntryType.NEED.name -> EntryType.NEED
                    EntryType.WANT.name -> EntryType.WANT
                    else -> throw IllegalArgumentException("Entry $i has unknown type '$t'")
                }
            )
        }
        val dailyBudgets = (root["dailyBudgets"] as? List<*>).orEmpty().mapIndexed { i, raw ->
            val m = raw as? Map<*, *>
                ?: throw IllegalArgumentException("Daily budget $i is malformed")
            val budgetCents = m["budgetCents"] as? Long
                ?: throw IllegalArgumentException("Daily budget $i is missing 'budgetCents'")
            require(budgetCents > 0L) { "Daily budget $i must be positive" }
            BackupDailyBudget(
                dayKey = m["dayKey"] as? String
                    ?: throw IllegalArgumentException("Daily budget $i is missing 'dayKey'"),
                budgetCents = budgetCents
            )
        }
        val prefs = (root["prefs"] as? Map<*, *>)?.let { p ->
            BackupPrefs(
                currencySymbol = p["currencySymbol"] as? String ?: "₱",
                currencyCode = p["currencyCode"] as? String ?: "PHP",
                dailyBudgetCents = (p["dailyBudgetCents"] as? Long)?.takeIf { it > 0L },
                reminderEnabled = p["reminderEnabled"] as? Boolean ?: false,
                reminderHour = (p["reminderHour"] as? Long)?.toInt()?.coerceIn(0, 23) ?: 20,
                spendingGoal = p["spendingGoal"] as? String ?: "track"
            )
        }
        return BackupEnvelope(
            schemaVersion = schemaVersion,
            appVersionName = root["appVersionName"] as? String ?: "",
            appVersionCode = (root["appVersionCode"] as? Long)?.toInt() ?: 0,
            exportedAtEpochMillis = root["exportedAtEpochMillis"] as? Long ?: 0L,
            prefs = prefs,
            entries = entries,
            dailyBudgets = dailyBudgets
        )
    }

    /**
     * Entries from [imported] whose content does not already exist in
     * [existing]; intra-file duplicates are also collapsed (the set grows as
     * it filters).
     */
    fun newEntriesOnly(imported: List<BackupEntry>, existing: List<Entry>): List<BackupEntry> {
        val keys = existing.mapTo(HashSet()) { BackupEntry.fromEntry(it).contentKey() }
        return imported.filter { keys.add(it.contentKey()) }
    }

    private fun Map<*, *>.longField(key: String, index: Int): Long =
        this[key] as? Long ?: throw IllegalArgumentException("Entry $index is missing '$key'")

    private fun Map<*, *>.stringField(key: String, index: Int): String =
        this[key] as? String ?: throw IllegalArgumentException("Entry $index is missing '$key'")
}
