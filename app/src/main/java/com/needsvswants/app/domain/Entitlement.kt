package com.needsvswants.app.domain

enum class EntitlementTier {
    FREE,
    PRO,
    MAX
}

enum class EntitlementType {
    FREE,
    PAID,
    TRIAL
}

data class Entitlement(
    val tier: EntitlementTier = EntitlementTier.FREE,
    val type: EntitlementType = EntitlementType.FREE,
    val expiresAtEpochMillis: Long? = null,
    val provider: String? = null,
    val source: String? = null,
    val status: String? = null
) {
    fun isProAt(nowEpochMillis: Long): Boolean =
        (type == EntitlementType.PAID || type == EntitlementType.TRIAL) &&
            (expiresAtEpochMillis == null || nowEpochMillis < expiresAtEpochMillis)

    fun hasProAccessAt(nowEpochMillis: Long): Boolean =
        isProAt(nowEpochMillis)

    fun hasMaxAccessAt(nowEpochMillis: Long): Boolean =
        isProAt(nowEpochMillis) && tier == EntitlementTier.MAX

    fun sheetLimitAt(nowEpochMillis: Long): Int? =
        if (hasProAccessAt(nowEpochMillis)) null else FREE_SHEET_LIMIT

    fun retentionCutoffAt(nowEpochMillis: Long): Long? =
        if (hasProAccessAt(nowEpochMillis)) null else nowEpochMillis - FREE_RETENTION_MILLIS

    companion object {
        val Free = Entitlement()
        const val FREE_SHEET_LIMIT = 20
        const val MILLIS_PER_DAY = 24L * 60L * 60L * 1_000L
        const val FREE_RETENTION_MILLIS = 30L * MILLIS_PER_DAY
    }
}
