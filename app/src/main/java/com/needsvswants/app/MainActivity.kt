package com.needsvswants.app

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
import com.needsvswants.app.ui.AppAppearanceViewModel
import com.needsvswants.app.ui.navigation.AppNavigation
import com.needsvswants.app.ui.theme.LocalAppSfx
import com.needsvswants.app.ui.theme.LocalHapticsEnabled
import com.needsvswants.app.ui.theme.NeedsVsWantsTheme
import com.needsvswants.app.ui.theme.rememberBoundAppSfx
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
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

    companion object {
        const val EXTRA_OPEN_TAB = "open_tab"
        const val TAB_LOG = "log"
    }
}
