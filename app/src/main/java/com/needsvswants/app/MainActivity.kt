package com.needsvswants.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.ui.AppAppearanceViewModel
import com.needsvswants.app.ui.navigation.AppNavigation
import com.needsvswants.app.ui.theme.NeedsVsWantsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appearanceVm: AppAppearanceViewModel = hiltViewModel()
            val themeId by appearanceVm.themeId.collectAsStateWithLifecycle()
            val fontScaleStep by appearanceVm.fontScaleStep.collectAsStateWithLifecycle()
            NeedsVsWantsTheme(
                themeId = themeId,
                fontScaleStep = fontScaleStep,
                systemDark = isSystemInDarkTheme()
            ) {
                AppNavigation()
            }
        }
    }
}
