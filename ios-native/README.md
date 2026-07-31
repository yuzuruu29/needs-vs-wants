# Needs vs. Wants — iOS (Native Rewrite)

A behaviour-changing spending trainer. Every purchase is classified as a **Need** or a **Want** in real time — not reconciled at month-end. Offline-first, no accounts, no analytics.

> **This is the native iOS rewrite.** The earlier `ios/` port was a faithful but translated mirror of the Android app and carried Android-shaped problems (god store object, `try?`-swallowed errors, denormalized date strings, opacity-toggled `ZStack`, `NotificationCenter` tab switching, zero haptics, zero tests). This rewrite is architected natively for iOS. See `docs/superpowers/plans/2026-07-29-ios-native-rewrite.md` for the full plan and `Decisions.md` D12 for the strategy rationale.

## Architecture

**MVVM + Repository + SwiftData** (iOS 17.0+)

```
App/
  NeedsVsWantsApp.swift     — @main, ModelContainer, throttled 35-day purge
  AppModel.swift             — @Observable navigation coordinator

Data/
  Entry.swift                — @Model: single dateUtc source of truth (no denormalized strings)
  EntryRepository.swift      — Result-based CRUD, purge, sheetCount (no try?)
  StatsEngine.swift          — Pure period aggregation (testable without SwiftData)
  CurrencyFormatter.swift    — NumberFormatter(.currency), JPY-minor-unit correct

Design/
  AppColors.swift            — Adaptive light/dark semantic tokens (D7 brand preserved)
  AppTypography.swift        — Hybrid: Playfair Display SC display + SF Pro body
  Haptics.swift              — Impact on seal, notification on delete/20-cap/wipe

Components/
  DonutChart.swift           — Hand-rolled Canvas (D6: no chart libraries)
  LedgerHeader/Row.swift     — Shared column geometry (D8)
  CurrencyText, NeedWantBadge, ShareBar, PrimaryButton, Eyebrow

Features/
  Summary/   — period rotor, donut, stat cards, CTA
  Log/       — auto-seal state machine, 20-cap, keyboard toolbar
  History/   — @Query sectioned by day, delete confirm
  Settings/  — native Form, currency Picker, wipe confirm
  Onboarding/— .page TabView, swipeable, first-launch
```

## Key improvements over the old `ios/` port

| Area | Old port | This rewrite |
|---|---|---|
| **Navigation** | `ZStack` + opacity toggle, `NotificationCenter`, all 4 screens mounted | Native `TabView` + `@Observable` coordinator, lazy loading |
| **Data** | God `EntryStore`, `try?` everywhere, denormalized `date`/`time` strings | `EntryRepository` with `Result` errors, single `dateUtc`, computed display |
| **Charts** | Hand-rolled Canvas | Same (D6 preserved), with accessibility labels |
| **Haptics** | None | Impact on seal, notification on delete/20-cap/wipe |
| **Onboarding** | Manual Next/Skip, no swipe | `.tabViewStyle(.page)` — native swipe + page dots |
| **Accessibility** | Rectangles + tap gestures, no VoiceOver tab semantics | Native `Button`s, `TabView` tab semantics, `@FocusState`, labels/hints |
| **Currency** | Hand-rolled `\(symbol) \(whole).\(c2)` — wrong for JPY | `NumberFormatter(.currency)` per-locale — JPY correct |
| **Tests** | Zero | 5 test files: CurrencyFormatter, StatsEngine, EntryRepository, LogViewModel, SummaryViewModel |
| **Palette** | Light-only D7 | D7 brand as adaptive light/dark tokens |

## Build

**GitHub Actions is the compile target** (authored on a non-Mac host).

```sh
cd ios-native
xcodegen generate
xcodebuild -project NeedsVsWants.xcodeproj \
  -scheme NeedsVsWants \
  -destination 'generic/platform=iOS Simulator' \
  build
```

CI: `.github/workflows/ios-native.yml` — `macos-15`, Xcode 16, `xcodegen` + `xcodebuild build test`.

## Fonts

Playfair Display SC (OFL license) bundled for display titles. Body uses SF Pro system fonts. See `Resources/OFL.txt`.

## Feature parity with Android

| Feature | Status |
|---|---|
| Summary (period rotor, donut, stat cards, CTA) | ✅ |
| Log (auto-seal, 20-cap, sealed ledger) | ✅ |
| History (grouped by day, delete confirm) | ✅ |
| Settings (currency, wipe, about) | ✅ |
| Onboarding (3 swipeable cards) | ✅ |
| 35-day purge | ✅ (throttled once per launch) |
| Currency (PHP/USD/EUR/JPY/SGD) | ✅ (JPY-correct) |

## Constraints (carried from Android)

- 35-day hard cap, silent auto-purge (D3)
- 20-entry sheet cap, total-count parity (D4)
- Cost stored as `Int64` cents, currency-immune (D2)
- Hand-rolled charts, no third-party libs (D6)
- Need = green, Want = red (D7)
- Offline-first, no network/accounts/analytics
