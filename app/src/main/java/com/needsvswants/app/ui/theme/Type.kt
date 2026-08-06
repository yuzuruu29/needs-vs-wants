package com.needsvswants.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.needsvswants.app.R

val DisplayFont = FontFamily(
    Font(R.font.playfair_display_sc_bold, FontWeight.Bold)
)

val BodyFont = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold)
)

/**
 * Option A type system (D78): Playfair Display SC + Inter.
 *
 * Rules (whole app):
 * 1. Playfair SC only for screen titles (LOG, LEDGER, SETTINGS, paywall hero, etc.)
 * 2. Inter for body, UI, plan names, ledger, settings, money
 * 3. Money always Inter + tabular nums (never Playfair for ₱ amounts)
 * 4. Eyebrows are Inter tracked micro-labels — rationed, not on every block
 * 5. Prefer these tokens over ad-hoc typography.copy() in screens
 */
object AppType {
    /** Screen titles only — Playfair SC. Max ~one per screen. */
    val screenTitle = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        letterSpacing = 0.5.sp,
        lineHeight = 36.sp
    )

    /** Slightly smaller screen title (paywall hero, two-line lockups). */
    val screenTitleSm = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = 0.4.sp,
        lineHeight = 34.sp
    )

    /** Dialog / sheet titles — Inter, not Playfair. */
    val dialogTitle = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 24.sp
    )

    /** Card / plan / section product names. */
    val sectionTitle = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 26.sp
    )

    val titleMd = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 20.sp
    )

    val titleSm = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 18.sp
    )

    val body = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 22.sp
    )

    val bodyMd = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 20.sp
    )

    val bodySm = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 18.sp
    )

    val bodySmEmph = bodySm.copy(fontWeight = FontWeight.SemiBold)

    val bodyXs = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 16.sp
    )

    /** Eyebrow / micro label — Inter, tracked. */
    val eyebrow = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 1.6.sp,
        lineHeight = 14.sp
    )

    val eyebrowSm = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        letterSpacing = 1.4.sp,
        lineHeight = 13.sp
    )

    /** Primary button label. */
    val button = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        letterSpacing = 1.0.sp,
        lineHeight = 16.sp
    )

    val meta = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 16.sp
    )

    val caption = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 15.sp
    )

    val navLabel = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 12.sp
    )

    /** Ledger item name. */
    val ledgerItem = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        letterSpacing = 0.05.sp,
        lineHeight = 20.sp
    )

    val ledgerMeta = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 14.sp,
        fontFeatureSettings = "tnum"
    )

    val ledgerHeader = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        letterSpacing = 1.0.sp,
        lineHeight = 13.sp
    )

    /**
     * Money / totals — Inter tabular only (Option A: no Playfair on ₱ amounts).
     */
    val moneyLg = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 28.sp,
        fontFeatureSettings = "tnum"
    )

    val moneyMd = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 20.sp,
        fontFeatureSettings = "tnum"
    )

    val moneySm = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 18.sp,
        fontFeatureSettings = "tnum"
    )

    val moneyHero = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = 0.05.sp,
        lineHeight = 32.sp,
        fontFeatureSettings = "tnum"
    )

    val input = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 22.sp
    )

    val stamp = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = 3.sp,
        lineHeight = 34.sp
    )
}

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
 * Material 3 scale mapped to Option A.
 * display* = Playfair SC screen titles; everything else = Inter.
 */
val AppTypography = Typography(
    displayLarge = AppType.screenTitle,
    displayMedium = AppType.screenTitleSm,
    displaySmall = AppType.screenTitleSm.copy(fontSize = 24.sp, lineHeight = 30.sp),
    headlineLarge = AppType.screenTitleSm.copy(fontSize = 24.sp, lineHeight = 30.sp),
    headlineMedium = AppType.dialogTitle.copy(fontSize = 20.sp, lineHeight = 26.sp),
    headlineSmall = AppType.dialogTitle,
    titleLarge = AppType.sectionTitle.copy(fontSize = 18.sp, lineHeight = 24.sp),
    titleMedium = AppType.titleMd,
    titleSmall = AppType.titleSm,
    bodyLarge = AppType.body,
    bodyMedium = AppType.bodyMd,
    bodySmall = AppType.bodyXs,
    labelLarge = AppType.button,
    labelMedium = AppType.meta,
    labelSmall = AppType.eyebrow
)
