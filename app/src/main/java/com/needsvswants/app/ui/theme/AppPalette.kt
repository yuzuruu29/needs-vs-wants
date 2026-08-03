package com.needsvswants.app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.needsvswants.app.domain.ThemeId

/**
 * Semantic colors for the whole UI. Screens read [AppTheme.colors] so light/dark/high-contrast
 * all repaint correctly (top-level Color.kt vals alone cannot switch themes).
 */
data class AppPalette(
    val background: Color,
    val surfaceCard: Color,
    val surfaceRaised: Color,
    val surfaceSunken: Color,
    val divider: Color,
    val dividerStrong: Color,
    val crimson: Color,
    val crimsonDeep: Color,
    val marketGreen: Color,
    val marketGreenDeep: Color,
    val gold: Color,
    val goldSoft: Color,
    val need: Color,
    val want: Color,
    val danger: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val isLightStatusBars: Boolean,
) {
    /** Ink-era aliases used across screens. */
    val ink: Color get() = background
    val inkElevated: Color get() = surfaceCard
    val inkRaised: Color get() = surfaceRaised
    val inkDivider: Color get() = divider
    val inkDividerStrong: Color get() = dividerStrong
    val gilt: Color get() = gold
    val giltSoft: Color get() = goldSoft

    fun toMaterialColorScheme(): ColorScheme {
        val scheme = if (isLightStatusBars) {
            lightColorScheme(
                primary = crimson,
                onPrimary = surfaceCard,
                primaryContainer = crimson.copy(alpha = 0.12f),
                onPrimaryContainer = crimsonDeep,
                secondary = marketGreen,
                onSecondary = surfaceCard,
                secondaryContainer = marketGreen.copy(alpha = 0.12f),
                onSecondaryContainer = marketGreenDeep,
                tertiary = gold,
                background = background,
                onBackground = textPrimary,
                surface = surfaceCard,
                onSurface = textPrimary,
                surfaceVariant = surfaceRaised,
                onSurfaceVariant = textSecondary,
                error = danger,
                onError = surfaceCard,
                outline = divider,
                outlineVariant = dividerStrong
            )
        } else {
            darkColorScheme(
                primary = crimson,
                onPrimary = Color(0xFF1A0A0C),
                primaryContainer = crimson.copy(alpha = 0.22f),
                onPrimaryContainer = Color(0xFFFFDAD9),
                secondary = marketGreen,
                onSecondary = Color(0xFF041A0E),
                secondaryContainer = marketGreen.copy(alpha = 0.22f),
                onSecondaryContainer = Color(0xFFC8F0D8),
                tertiary = gold,
                background = background,
                onBackground = textPrimary,
                surface = surfaceCard,
                onSurface = textPrimary,
                surfaceVariant = surfaceRaised,
                onSurfaceVariant = textSecondary,
                error = danger,
                onError = Color(0xFF1A0A0C),
                outline = divider,
                outlineVariant = dividerStrong
            )
        }
        return scheme
    }

    companion object {
        /** Current D7 supermarket light (default). */
        fun marketLight(): AppPalette = AppPalette(
            background = Color(0xFFFAFAF7),
            surfaceCard = Color(0xFFFFFFFF),
            surfaceRaised = Color(0xFFF3F1EA),
            surfaceSunken = Color(0xFFF7F4EC),
            divider = Color(0xFFE8E5DC),
            dividerStrong = Color(0xFFD6D2C6),
            crimson = Color(0xFFC8102E),
            crimsonDeep = Color(0xFFA40E25),
            marketGreen = Color(0xFF0B6B3A),
            marketGreenDeep = Color(0xFF084F2A),
            gold = Color(0xFFE8A92A),
            goldSoft = Color(0xFFF4C968),
            need = Color(0xFF0B6B3A),
            want = Color(0xFFC8102E),
            danger = Color(0xFFC8102E),
            textPrimary = Color(0xFF1A1A1A),
            textSecondary = Color(0xFF5A5A5A),
            textMuted = Color(0xFF8A8A8A),
            isLightStatusBars = true
        )

        fun marketDark(): AppPalette = AppPalette(
            background = Color(0xFF121410),
            surfaceCard = Color(0xFF1C1E1A),
            surfaceRaised = Color(0xFF262822),
            surfaceSunken = Color(0xFF1A1C18),
            divider = Color(0xFF33362F),
            dividerStrong = Color(0xFF454940),
            crimson = Color(0xFFE25C6F),
            crimsonDeep = Color(0xFFC8102E),
            marketGreen = Color(0xFF3E9D6E),
            marketGreenDeep = Color(0xFF2A7A52),
            gold = Color(0xFFF4C968),
            goldSoft = Color(0xFFE8A92A),
            need = Color(0xFF3E9D6E),
            want = Color(0xFFE25C6F),
            danger = Color(0xFFE25C6F),
            textPrimary = Color(0xFFF4F2EA),
            textSecondary = Color(0xFFB8B6AE),
            textMuted = Color(0xFF8A8880),
            isLightStatusBars = false
        )

        fun highContrast(): AppPalette = AppPalette(
            background = Color(0xFFFFFFFF),
            surfaceCard = Color(0xFFFFFFFF),
            surfaceRaised = Color(0xFFF0F0F0),
            surfaceSunken = Color(0xFFE8E8E8),
            divider = Color(0xFF1A1A1A),
            dividerStrong = Color(0xFF000000),
            crimson = Color(0xFFC8102E),
            crimsonDeep = Color(0xFF9B0000),
            marketGreen = Color(0xFF006B2E),
            marketGreenDeep = Color(0xFF004D21),
            gold = Color(0xFFB9881E),
            goldSoft = Color(0xFFE8A92A),
            need = Color(0xFF006B2E),
            want = Color(0xFF9B0000),
            danger = Color(0xFF9B0000),
            textPrimary = Color(0xFF000000),
            textSecondary = Color(0xFF1A1A1A),
            textMuted = Color(0xFF333333),
            isLightStatusBars = true
        )

        fun forTheme(themeId: ThemeId, systemDark: Boolean): AppPalette {
            if (themeId == ThemeId.HIGH_CONTRAST) return highContrast()
            return if (themeId.resolveIsDark(systemDark)) marketDark() else marketLight()
        }
    }
}

val LocalAppPalette = staticCompositionLocalOf { AppPalette.marketLight() }

object AppTheme {
    val colors: AppPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalAppPalette.current
}
