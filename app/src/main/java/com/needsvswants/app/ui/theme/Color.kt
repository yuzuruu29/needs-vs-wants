package com.needsvswants.app.ui.theme

import androidx.compose.ui.graphics.Color

// Supermarket-inspired palette — Puregold crimson, Robinsons green, warm gold trim.
// Light surfaces for a clean, friendly, trustworthy shopping feel.

val Surface = Color(0xFFFAFAF7)            // app background — warm off-white
val SurfaceCard = Color(0xFFFFFFFF)       // cards, primary surface
val SurfaceRaised = Color(0xFFF3F1EA)     // chips, raised surfaces
val SurfaceSunken = Color(0xFFF7F4EC)      // inputs, sunken wells
val Divider = Color(0xFFE8E5DC)           // hairline divider
val DividerStrong = Color(0xFFD6D2C6)

// Brand accents
val Crimson = Color(0xFFC8102E)           // Puregold red — primary accent
val CrimsonDeep = Color(0xFFA40E25)       // pressed / deep red
val CrimsonSoft = Color(0xFFE25C6F)       // tints, badged

val MarketGreen = Color(0xFF0B6B3A)     // Robinsons green — secondary
val MarketGreenDeep = Color(0xFF084F2A)
val MarketGreenSoft = Color(0xFF3E9D6E)

val Gold = Color(0xFFE8A92A)             // warm gold — premium trim, totals
val GoldSoft = Color(0xFFF4C968)
val GoldDeep = Color(0xFFB9881E)

// Semantic tagging
val Need = MarketGreen                   // need = green (staple, "go")
val Want = Crimson                       // want = red (indulgence, "stop")
val Danger = Color(0xFFC8102E)

// Text on light surfaces — darkened a notch for AA contrast over translucent glass.
val TextPrimary = Color(0xFF1A1A1A)      // near-ink, ≥16:1 on white
val TextSecondary = Color(0xFF454545)
val TextMuted = Color(0xFF6E6E6E)

// Aliases for any screens still referencing the old Ink-era names during migration.
val Ink = Surface
val InkElevated = SurfaceCard
val InkRaised = SurfaceRaised
val InkDivider = Divider
val InkDividerStrong = DividerStrong
val Gilt = Gold
val GiltSoft = GoldSoft
val GiltDeep = GoldDeep
val PrimarySky = Need
val SecondaryRose = Want
val GoldTrim = Gold
val SurfaceNight = Surface
val SurfaceCardAlt = SurfaceRaised
val TextPrimary_Warm = TextPrimary
