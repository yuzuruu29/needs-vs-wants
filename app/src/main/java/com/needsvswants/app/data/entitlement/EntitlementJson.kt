package com.needsvswants.app.data.entitlement

import com.needsvswants.app.data.remote.SupabaseJson
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Maps `get_entitlement` Edge Function JSON into [Entitlement].
 *
 * Expected envelope (see `supabase/functions/get_entitlement`):
 * ```
 * { "success": true, "data": {
 *     "is_pro": bool, "plan": "free"|"pro"|"max",
 *     "tier": "free"|"pro"|"max" (optional),
 *     "trial_ends_at": iso|null, "paid_until": iso|null,
 *     "provider", "source", "status"
 * }}
 * ```
 */
object EntitlementJson {

    fun parseGetEntitlementResponse(json: String): Entitlement? {
        if (json.isBlank()) return null
        // Prefer nested data object when present; fall back to flat body.
        val dataSlice = extractObject(json, "data") ?: json

        val planOrTier = (
            SupabaseJson.readStringField(dataSlice, "tier")
                ?: SupabaseJson.readStringField(dataSlice, "plan")
                ?: "free"
            ).lowercase()

        val tier = when (planOrTier) {
            "max" -> EntitlementTier.MAX
            "pro" -> EntitlementTier.PRO
            else -> EntitlementTier.FREE
        }

        val isProFlag = SupabaseJson.readBooleanField(dataSlice, "is_pro") ?: (tier != EntitlementTier.FREE)
        if (!isProFlag || tier == EntitlementTier.FREE) {
            return Entitlement()
        }

        val trialEnds = parseIsoToEpochMillis(SupabaseJson.readNullableStringField(dataSlice, "trial_ends_at"))
        val paidUntil = parseIsoToEpochMillis(SupabaseJson.readNullableStringField(dataSlice, "paid_until"))

        val type = when {
            trialEnds != null && (paidUntil == null || trialEnds >= paidUntil) -> EntitlementType.TRIAL
            else -> EntitlementType.PAID
        }

        val expiresAt = listOfNotNull(trialEnds, paidUntil).maxOrNull()

        return Entitlement(
            tier = tier,
            type = type,
            expiresAtEpochMillis = expiresAt,
            provider = SupabaseJson.readNullableStringField(dataSlice, "provider"),
            source = SupabaseJson.readNullableStringField(dataSlice, "source"),
            status = SupabaseJson.readNullableStringField(dataSlice, "status")
        )
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

    private fun parseIsoToEpochMillis(iso: String?): Long? {
        if (iso.isNullOrBlank()) return null
        // The Edge Functions emit new Date().toISOString() → "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'".
        // java.time.Instant is API 26+; minSdk is 24, so parse with a UTC SimpleDateFormat.
        return runCatching {
            iso.let {
                val fmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                fmt.timeZone = TimeZone.getTimeZone("UTC")
                fmt.parse(it)?.time
            }
        }.getOrNull()
    }
}
