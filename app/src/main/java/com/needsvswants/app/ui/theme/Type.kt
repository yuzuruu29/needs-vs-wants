package com.needsvswants.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.needsvswants.app.R

/**
 * Soft pencil display face (hybrid type B, D94).
 * Titles / stamp only — never money or dense UI.
 */
val DisplayFont = FontFamily(
    Font(R.font.caveat_regular, FontWeight.Normal),
    Font(R.font.caveat_bold, FontWeight.Bold)
)

/** Legacy Playfair SC kept bundled for any one-off call sites / seals that still need it. */
val DisplayFontSerif = FontFamily(
    Font(R.font.playfair_display_sc_bold, FontWeight.Bold)
)

/**
 * Warm humanist body (Source Sans 3). UI, ledger, eyebrows, money.
 * Tabular figures via fontFeatureSettings "tnum" on money styles.
 */
val BodyFont = FontFamily(
    Font(R.font.source_sans3_regular, FontWeight.Normal),
    Font(R.font.source_sans3_medium, FontWeight.Medium),
    Font(R.font.source_sans3_semibold, FontWeight.SemiBold),
    Font(R.font.source_sans3_bold, FontWeight.Bold)
)

/**
 * Hybrid type system: Caveat titles + Source Sans 3 body/money.
 *
 * Scale notes (2026-08-07 readability pass):
 * - Default step is ~1 step larger than the old Inter ladder (Source Sans reads smaller).
 * - lineHeight stays ≥ 1.35× fontSize on body, ≥ 1.2× on display (Caveat needs air).
 * - Spacing that must breathe with Large/Extra large uses [scaledSpacing].
 */
object AppType {
    /** Screen titles only — Caveat pencil. Max ~one per screen. */
    val screenTitle = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 44.sp
    )

    /** Slightly smaller screen title (paywall hero, two-line lockups). */
    val screenTitleSm = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 40.sp
    )

    /** Dialog / sheet titles. */
    val dialogTitle = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = 0.05.sp,
        lineHeight = 28.sp
    )

    /** Card / plan / section product names. */
    val sectionTitle = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        letterSpacing = 0.05.sp,
        lineHeight = 30.sp
    )

    val titleMd = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 24.sp
    )

    val titleSm = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 22.sp
    )

    val body = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 26.sp
    )

    val bodyMd = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 24.sp
    )

    val bodySm = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        letterSpacing = 0.08.sp,
        lineHeight = 22.sp
    )

    val bodySmEmph = bodySm.copy(fontWeight = FontWeight.SemiBold)

    val bodyXs = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 18.sp
    )

    /** Eyebrow / micro label — tracked; floor kept readable. */
    val eyebrow = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 1.4.sp,
        lineHeight = 16.sp
    )

    val eyebrowSm = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 1.2.sp,
        lineHeight = 15.sp
    )

    /** Primary button label. */
    val button = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        letterSpacing = 0.8.sp,
        lineHeight = 20.sp
    )

    val meta = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 18.sp
    )

    val caption = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 17.sp
    )

    val navLabel = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 14.sp
    )

    /** Ledger item name. */
    val ledgerItem = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = 0.02.sp,
        lineHeight = 22.sp
    )

    val ledgerMeta = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.05.sp,
        lineHeight = 16.sp,
        fontFeatureSettings = "tnum"
    )

    val ledgerHeader = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 0.9.sp,
        lineHeight = 15.sp
    )

    /**
     * Money / totals — Source Sans 3 tabular only (never Caveat on ₱ amounts).
     */
    val moneyLg = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = 0.05.sp,
        lineHeight = 32.sp,
        fontFeatureSettings = "tnum"
    )

    val moneyMd = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        letterSpacing = 0.05.sp,
        lineHeight = 24.sp,
        fontFeatureSettings = "tnum"
    )

    val moneySm = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        letterSpacing = 0.05.sp,
        lineHeight = 20.sp,
        fontFeatureSettings = "tnum"
    )

    val moneyHero = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        letterSpacing = 0.02.sp,
        lineHeight = 38.sp,
        fontFeatureSettings = "tnum"
    )

    val input = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        letterSpacing = 0.05.sp,
        lineHeight = 24.sp
    )

    /** Seal stamp wordmark — Caveat bold. */
    val stamp = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = 1.5.sp,
        lineHeight = 40.sp
    )
}

/**
 * Damped fontScale factor for chrome spacing — only ~55% of the delta is
 * applied to padding/heights so Large / Extra large adds air without
 * blowing the layout (text still leads). Clamped to 1.45 (system 1.3 ×
 * app Extra large). Pure so unit tests can pin the curve.
 *
 * Example: fontScale 1.22 → factor ≈ 1.12
 */
fun scaledSpacingFactor(fontScale: Float): Float {
    val fs = fontScale.coerceIn(0.85f, 1.45f)
    return 1f + (fs - 1f) * 0.55f
}

/**
 * Spacing that grows with the active font scale so Large / Extra large
 * does not crush fixed-height chrome. See [scaledSpacingFactor].
 */
@Composable
fun scaledSpacing(baseDp: Float): Dp {
    return (baseDp * scaledSpacingFactor(LocalDensity.current.fontScale)).dp
}

@Composable
fun scaledSpacing(baseDp: Int): Dp = scaledSpacing(baseDp.toFloat())

/**
 * Paywall aliases → [AppType] so membership screens stay on the same ladder.
 */
object PaywallType {
    val screenHero = AppType.screenTitleSm
    val screenLede = AppType.body
    val planTitle = AppType.sectionTitle
    val planPrice = AppType.moneyHero
    val planPriceSuffix = AppType.bodySm.copy(fontWeight = FontWeight.Medium)
    val planSub = AppType.bodySm
    val planFeature = AppType.bodySm
    val planFeatureEmph = AppType.bodySmEmph
    val meta = AppType.meta
    val stickyNote = AppType.caption
}

/**
 * Material 3 scale mapped to hybrid B (Caveat display + Source Sans 3 body).
 */
val AppTypography = Typography(
    displayLarge = AppType.screenTitle,
    displayMedium = AppType.screenTitleSm,
    displaySmall = AppType.screenTitleSm.copy(fontSize = 26.sp, lineHeight = 34.sp),
    headlineLarge = AppType.screenTitleSm.copy(fontSize = 26.sp, lineHeight = 34.sp),
    headlineMedium = AppType.dialogTitle,
    headlineSmall = AppType.dialogTitle.copy(fontSize = 18.sp, lineHeight = 26.sp),
    titleLarge = AppType.sectionTitle.copy(fontSize = 20.sp, lineHeight = 28.sp),
    titleMedium = AppType.titleMd,
    titleSmall = AppType.titleSm,
    bodyLarge = AppType.body,
    bodyMedium = AppType.bodyMd,
    bodySmall = AppType.bodyXs,
    labelLarge = AppType.button,
    labelMedium = AppType.meta,
    labelSmall = AppType.eyebrow
)
