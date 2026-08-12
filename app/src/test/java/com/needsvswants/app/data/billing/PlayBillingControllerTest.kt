package com.needsvswants.app.data.billing

import com.needsvswants.app.data.remote.SupabaseConfig
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class PlayBillingControllerTest {

    @Test
    fun withoutBillingLibrary_allActionsUnavailable() = runTest {
        val controller = PlayBillingController(SupabaseConfig.Disabled)
        assertFalse(controller.isPlayAvailable)
        assertEquals(BillingResult.Unavailable, controller.startTrial("pro_trial_3day", BillingPeriod.MONTHLY))
        assertEquals(BillingResult.Unavailable, controller.purchase("pro_monthly", BillingPeriod.MONTHLY))
        assertEquals(BillingResult.Unavailable, controller.restorePurchases())
    }

    @Test
    fun productIds_fallbackToDefaults_whenConfigBlank() {
        val controller = PlayBillingController(SupabaseConfig.Disabled)
        assertEquals(PlayBillingController.DEFAULT_TRIAL_ID, controller.trialProductId)
        assertEquals(PlayBillingController.DEFAULT_MONTHLY_ID, controller.monthlyProductId)
    }

    @Test
    fun productIds_useConfigWhenSet() {
        val config = SupabaseConfig(
            url = "",
            anonKey = "",
            proTrialProductId = "cfg_trial",
            proMonthlyProductId = "cfg_month"
        )
        val controller = PlayBillingController(config)
        assertEquals("cfg_trial", controller.trialProductId)
        assertEquals("cfg_month", controller.monthlyProductId)
    }
}
