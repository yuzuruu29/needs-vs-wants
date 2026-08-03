package com.needsvswants.app.data.entitlement

import javax.inject.Inject
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.data.remote.HttpJsonClient
import com.needsvswants.app.data.remote.SupabaseConfig

/**
 * Remote source for an entitlement, backed by the Supabase `get_entitlement`
 * Edge Function. Provider-driven via [SupabaseConfig]. When the config is
 * disabled (empty placeholders) [fetchEntitlement] returns null without any
 * network call, so the local FREE default remains authoritative offline.
 */
interface EntitlementRemote {
    /** Returns the current entitlement, or null when unavailable (offline/unconfigured). */
    suspend fun fetchEntitlement(accessToken: String?): Entitlement?
}

class SupabaseEntitlementRemote @Inject constructor(
    private val config: SupabaseConfig
) : EntitlementRemote {

    override suspend fun fetchEntitlement(accessToken: String?): Entitlement? {
        if (!config.enabled) return null
        if (accessToken.isNullOrBlank()) return null

        val url = "${config.url.trimEnd('/')}/functions/v1/get_entitlement"
        val result = HttpJsonClient.request(
            url = url,
            method = "POST",
            headers = mapOf(
                "apikey" to config.anonKey,
                "Authorization" to "Bearer $accessToken",
                "Accept" to "application/json"
            ),
            body = "{}"
        )
        return result.getOrNull()?.let { EntitlementJson.parseGetEntitlementResponse(it) }
    }
}