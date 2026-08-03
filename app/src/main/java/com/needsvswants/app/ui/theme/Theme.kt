package com.needsvswants.app.ui.theme

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

    CompositionLocalProvider(
        LocalAppPalette provides palette,
        LocalDensity provides scaledDensity
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}
