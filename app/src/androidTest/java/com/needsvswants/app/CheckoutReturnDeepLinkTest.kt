package com.needsvswants.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Money-path smoke tests (audit gap: zero instrumented coverage): the
 * checkout-return deep links and a plain cold start must all reach RESUMED
 * without crashing. The 2.0.11 production crash (D141) lived exactly on this
 * path, so keeping these green is the cheap regression net.
 */
@RunWith(AndroidJUnit4::class)
class CheckoutReturnDeepLinkTest {

    private fun launch(uri: String?): ActivityScenario<MainActivity> {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = if (uri == null) {
            Intent(context, MainActivity::class.java)
        } else {
            Intent(Intent.ACTION_VIEW, Uri.parse(uri)).setClass(context, MainActivity::class.java)
        }
        return ActivityScenario.launch(intent)
    }

    @Test
    fun coldStart_reachesResumed() {
        launch(null).use { scenario ->
            assertEquals(Lifecycle.State.RESUMED, scenario.state)
        }
    }

    @Test
    fun paypalReturn_deepLink_reachesResumed() {
        launch("needsvswants://paypal/return?subscription_id=I-TEST123").use { scenario ->
            assertEquals(Lifecycle.State.RESUMED, scenario.state)
        }
    }

    @Test
    fun paypalCancel_deepLink_reachesResumed() {
        launch("needsvswants://paypal/cancel").use { scenario ->
            assertEquals(Lifecycle.State.RESUMED, scenario.state)
        }
    }

    @Test
    fun paymongoReturn_deepLink_reachesResumed() {
        launch("needsvswants://paymongo/return?session=cs_test").use { scenario ->
            assertEquals(Lifecycle.State.RESUMED, scenario.state)
        }
    }

    @Test
    fun paymongoCancel_deepLink_reachesResumed() {
        launch("needsvswants://paymongo/cancel").use { scenario ->
            assertEquals(Lifecycle.State.RESUMED, scenario.state)
        }
    }
}
