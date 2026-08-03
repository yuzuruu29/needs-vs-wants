package com.needsvswants.app.data.remote

/**
 * Pure JSON helpers for Supabase auth / edge responses.
 * No Android types — fully unit-testable on the JVM.
 */
object SupabaseJson {

    /**
     * Extracts `access_token` from a Supabase `/auth/v1/verify` response body.
     * Returns null when the field is missing, blank, or the body is not a JSON object.
     */
    fun parseAccessToken(json: String): String? {
        val token = readStringField(json, "access_token") ?: return null
        return token.takeIf { it.isNotBlank() }
    }

    /**
     * Parses a Supabase token response (`grant_type=id_token` or `refresh_token`).
     * Returns null when `access_token` is missing or blank.
     */
    fun parseAuthSession(json: String, nowEpochMillis: Long): AuthSession? {
        val access = parseAccessToken(json) ?: return null
        val refresh = readStringField(json, "refresh_token")?.takeIf { it.isNotBlank() }
        val expiresIn = readLongField(json, "expires_in")
        val userId = readUserField(json, "id")
        val email = readUserField(json, "email")
        val expiresAt = expiresIn?.let { nowEpochMillis + it * 1000L }
        return AuthSession(
            accessToken = access,
            refreshToken = refresh,
            userId = userId,
            email = email,
            expiresAtEpochMillis = expiresAt
        )
    }

    /**
     * Minimal string-field reader for flat JSON objects.
     * Handles escaped quotes and ignores nested objects for the target key.
     */
    fun readStringField(json: String, key: String): String? {
        val pattern = Regex(""""$key"\s*:\s*"((?:\\.|[^"\\])*)"""")
        val match = pattern.find(json) ?: return null
        return match.groupValues[1]
            .replace("\\\"", "\"")
            .replace("\\\\", "\\")
            .replace("\\n", "\n")
            .replace("\\t", "\t")
    }

    fun readLongField(json: String, key: String): Long? {
        val pattern = Regex(""""$key"\s*:\s*(-?\d+)""")
        val match = pattern.find(json) ?: return null
        return match.groupValues[1].toLongOrNull()
    }

    fun readBooleanField(json: String, key: String): Boolean? {
        val pattern = Regex(""""$key"\s*:\s*(true|false)""")
        val match = pattern.find(json) ?: return null
        return match.groupValues[1] == "true"
    }

    fun readNullableStringField(json: String, key: String): String? {
        val nullPattern = Regex(""""$key"\s*:\s*null""")
        if (nullPattern.containsMatchIn(json)) return null
        return readStringField(json, key)
    }

    /**
     * Reads a string field from the top-level `"user": { ... }` object only.
     * Avoids matching the same key at the outer level.
     */
    fun readUserField(json: String, key: String): String? {
        val userBlock = Regex(""""user"\s*:\s*\{([^}]*)\}""")
            .find(json)
            ?.groupValues
            ?.getOrNull(1)
            ?: return null
        return readStringField("{$userBlock}", key)?.takeIf { it.isNotBlank() }
    }
}
