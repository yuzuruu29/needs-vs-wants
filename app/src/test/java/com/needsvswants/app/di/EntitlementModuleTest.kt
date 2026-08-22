package com.needsvswants.app.di

import com.needsvswants.app.BuildConfig
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pins the DI mapping from BuildConfig fields into [SupabaseConfig].
 *
 * Regression guard (P1): the annual PayPal product ids must reach
 * [SupabaseConfig] - omitting them blanks the ids at runtime and PayPal annual
 * checkout fails with "Annual PayPal plan not configured on this build." even
 * when local.properties carries PRO_ANNUAL_PRODUCT_ID /
 * PRO_MAX_ANNUAL_PRODUCT_ID.
 */
class EntitlementModuleTest {

    @Test
    fun provideSupabaseConfig_mapsEveryBillingField_fromBuildConfig() {
        val config = EntitlementModule.provideSupabaseConfig()

        assertEquals(BuildConfig.SUPABASE_URL, config.url)
        assertEquals(BuildConfig.SUPABASE_ANON_KEY, config.anonKey)
        assertEquals(BuildConfig.PRO_TRIAL_PRODUCT_ID, config.proTrialProductId)
        assertEquals(BuildConfig.PRO_MONTHLY_PRODUCT_ID, config.proMonthlyProductId)
        assertEquals(BuildConfig.PRO_MAX_MONTHLY_PRODUCT_ID, config.maxMonthlyProductId)
        assertEquals(BuildConfig.GOOGLE_WEB_CLIENT_ID, config.googleWebClientId)
        // The dropped fields behind the P1 defect:
        assertEquals(BuildConfig.PRO_ANNUAL_PRODUCT_ID, config.proAnnualProductId)
        assertEquals(BuildConfig.PRO_MAX_ANNUAL_PRODUCT_ID, config.maxAnnualProductId)
    }
}
