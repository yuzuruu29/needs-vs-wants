package com.needsvswants.app.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat
import com.needsvswants.app.domain.FontScaleStep
import com.needsvswants.app.domain.ThemeId

@Composable
fun NeedsVsWantsTheme(
    themeId: ThemeId = ThemeId.MARKET_LIGHT,
    fontScaleStep: FontScaleStep = FontScaleStep.DEFAULT,
    systemDark: Boolean = isSystemInDarkTheme(),
    /** In-app reduced motion (Settings). Also off when system animator scale is 0. */
    userReducedMotion: Boolean = false,
    content: @Composable () -> Unit
) {
    val palette = AppPalette.forTheme(themeId, systemDark)
    val colorScheme = palette.toMaterialColorScheme()
    val baseDensity = LocalDensity.current
    val scaledDensity = Density(
        density = baseDensity.density,
        fontScale = baseDensity.fontScale * fontScaleStep.multiplier
    )

    val view = LocalView.current
    val activity = view.context as? android.app.Activity
    SideEffect {
        activity?.window?.let { w ->
            w.statusBarColor = palette.background.toArgb()
            w.navigationBarColor = palette.background.toArgb()
            val insets = WindowCompat.getInsetsController(w, w.decorView)
            insets.isAppearanceLightStatusBars = palette.isLightStatusBars
            insets.isAppearanceLightNavigationBars = palette.isLightStatusBars
        }
    }

    RememberMotionGate(userReducedMotion = userReducedMotion)

    CompositionLocalProvider(
        LocalAppPalette provides palette,
        LocalDensity provides scaledDensity
    ) {
        // One ink wave for the whole app (D99/D101) — replaces stock Material
        // ripple. When reduced motion is on, InkWave durations collapse via Motion.
        val inkWave = rememberInkWaveIndication()
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes.asMaterialShapes(),
            content = {
                CompositionLocalProvider(LocalIndication provides inkWave) {
                    content()
                }
            }
        )
    }
}
