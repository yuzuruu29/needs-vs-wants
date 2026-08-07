package com.needsvswants.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.data.entitlement.PayPalReturnHandler
import com.needsvswants.app.ui.AppAppearanceViewModel
import com.needsvswants.app.ui.navigation.AppNavigation
import com.needsvswants.app.ui.theme.LocalAppSfx
import com.needsvswants.app.ui.theme.LocalHapticsEnabled
import com.needsvswants.app.ui.theme.NeedsVsWantsTheme
import com.needsvswants.app.ui.theme.rememberBoundAppSfx
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var payPalReturnHandler: PayPalReturnHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // Fresh process launched by a PayPal deep link.
        handlePayPalDeepLink(intent?.data)
        val openTab = intent?.getStringExtra(EXTRA_OPEN_TAB)
        setContent {
            val appearanceVm: AppAppearanceViewModel = hiltViewModel()
            val themeId by appearanceVm.themeId.collectAsStateWithLifecycle()
            val fontScaleStep by appearanceVm.fontScaleStep.collectAsStateWithLifecycle()
            val sfxEnabled by appearanceVm.sfxEnabled.collectAsStateWithLifecycle()
            val hapticsEnabled by appearanceVm.hapticsEnabled.collectAsStateWithLifecycle()
            val reducedMotion by appearanceVm.reducedMotion.collectAsStateWithLifecycle()
            val sfx = rememberBoundAppSfx(enabled = sfxEnabled)
            val startRoute = remember(openTab) {
                when (openTab) {
                    TAB_LOG -> "input"
                    else -> null
                }
            }
            CompositionLocalProvider(
                LocalAppSfx provides sfx,
                LocalHapticsEnabled provides hapticsEnabled,
            ) {
                NeedsVsWantsTheme(
                    themeId = themeId,
                    fontScaleStep = fontScaleStep,
                    systemDark = isSystemInDarkTheme(),
                    userReducedMotion = reducedMotion,
                ) {
                    AppNavigation(startDestination = startRoute ?: "summary")
                }
            }
        }
    }

    /**
     * singleTop: a PayPal deep link while this activity is on top re-delivers
     * the intent here instead of creating a second instance.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handlePayPalDeepLink(intent.data)
    }

    /** Routes `needsvswants://paypal/return|cancel` into [PayPalReturnHandler]. */
    private fun handlePayPalDeepLink(uri: Uri?) {
        if (uri == null) return
        if (uri.scheme?.equals("needsvswants", ignoreCase = true) != true) return
        if (uri.host != "paypal") return
        when (uri.path?.trimEnd('/')) {
            "/return" -> payPalReturnHandler.onCheckoutReturned()
            "/cancel" -> payPalReturnHandler.onCheckoutCancelled()
            else -> Unit
        }
    }

    companion object {
        const val EXTRA_OPEN_TAB = "open_tab"
        const val TAB_LOG = "log"
    }
}
