# Settings: Text Size + Appearance — Design Spec

**Date:** 2026-08-02  
**Status:** Approved (plan)  
**Surface:** Android app (`Settings`)  
**Agent:** Grok  

## Problem

The app is light-only with a fixed type scale. Elderly and low-vision users cannot enlarge text or switch to dark / high-contrast appearance.

## Goals

- **Text size** in Settings: Default / Large / Extra large (`1.00` / `1.15` / `1.30` multipliers on top of system font scale).
- **Appearance** in Settings: Market light, Market dark, System, High contrast.
- Keep **Need = green**, **Want = crimson**, gold trim (D7 brand).
- Persist in DataStore; wipe diary resets appearance prefs.

## Non-goals

- Soft-launch website preview (optional later)
- iOS parity (v1)
- Arbitrary rainbow themes / Material You wallpaper colors
- Free-form font sliders

## Architecture

1. `ThemeId` + `FontScaleStep` domain enums (pure, unit-tested).
2. `AppPreferences` keys `theme_id`, `font_scale_step`.
3. Semantic `AppPalette` + `CompositionLocal` (`AppTheme.colors`).
4. `NeedsVsWantsTheme(themeId, fontScaleStep, systemDark)` applies Density fontScale + Material 3 scheme + status bars.
5. Migrate screens off light-only top-level color vals.

## Settings UI

After **CURRENCY**:

1. **TEXT SIZE** — three radio rows + live sample line.
2. **APPEARANCE** — four radio rows with mini swatches (bg / need / want).

Then DATA / ABOUT as today.

## Invariants

- All themes keep Need/Want semantic mapping.
- Body text targets WCAG AA on its surface.
- Default launch matches current Market light + Default text (no visual regression).
