package com.needsvswants.app.data.remote

/**
 * Environmental Supabase configuration. Populated from BuildConfig placeholders.
 *
 * When [url] or [anonKey] are blank the client is considered **disabled** and all
 * network operations short-circuit to an unavailable/failure result — this is the
 * default offline behavior until real credentials are configured.
 */
data class SupabaseConfig(
    val url: String,
    val anonKey: String,
    val proTrialProductId: String,
    val proMonthlyProductId: String,
    val maxMonthlyProductId: String = "",
    val googleWebClientId: String = "",
    val proAnnualProductId: String = "",
    val maxAnnualProductId: String = ""
) {
    val enabled: Boolean get() = url.isNotBlank() && anonKey.isNotBlank()

    /** True when Supabase is configured and a Google Web client ID is present. */
    val googleSignInEnabled: Boolean get() = enabled && googleWebClientId.isNotBlank()

    companion object {
        val Disabled = SupabaseConfig(
            url = "",
            anonKey = "",
            proTrialProductId = "",
            proMonthlyProductId = "",
            maxMonthlyProductId = "",
            googleWebClientId = "",
            proAnnualProductId = "",
            maxAnnualProductId = ""
        )
    }
}