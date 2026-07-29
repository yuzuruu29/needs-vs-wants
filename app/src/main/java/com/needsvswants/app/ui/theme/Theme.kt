package com.needsvswants.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Crimson,
    onPrimary = SurfaceCard,
    primaryContainer = Crimson.copy(alpha = 0.12f),
    onPrimaryContainer = CrimsonDeep,
    secondary = MarketGreen,
    onSecondary = SurfaceCard,
    secondaryContainer = MarketGreen.copy(alpha = 0.12f),
    onSecondaryContainer = MarketGreenDeep,
    tertiary = GoldDeep,
    background = Surface,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    error = Danger,
    onError = SurfaceCard,
    outline = Divider,
    outlineVariant = DividerStrong
)

@Composable
fun NeedsVsWantsTheme(content: @Composable () -> Unit) {
    val colorScheme = LightColorScheme
    // Light surfaces → dark icons in the status & navigation bars.
    val activity = androidx.compose.ui.platform.LocalView.current.context as? android.app.Activity
    SideEffect {
        activity?.window?.let { w ->
            w.statusBarColor = Surface.toArgb()
            w.navigationBarColor = Surface.toArgb()
            WindowCompat.getInsetsController(w, w.decorView).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(w, w.decorView).isAppearanceLightNavigationBars = true
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
