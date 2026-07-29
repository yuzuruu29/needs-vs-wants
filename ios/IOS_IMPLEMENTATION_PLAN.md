# Needs vs. Wants — iOS Implementation Plan (reconciled)

> Native iPhone port of the Android reference (`com.needsvswants.app`).
> SwiftUI + SwiftData, iOS 17.0+. Offline-first, no backend, no analytics.
> This document reconciles the proposed iPhone plan with the *actual* Android
> source so the port is faithful rather than aspirational.

---

## 0. Source-of-truth reconciliation

The Android repo contained a stale `IMPLEMENTATION_PLAN.md` describing a "Friendster
2003 dark" palette. The **live** Android code (`ui/theme/Color.kt`) was later migrated
to a **light supermarket palette** (crimson/green/gold on warm cream). The iPhone
plan's Phase 2 colors match the live code exactly, so this port uses the **light**
palette. The dark text is obsolete and ignored.

| Token | Hex | Role |
|------|------|------|
| `surface` | `#FAFAF7` | App background (warm off-white) |
| `surfaceCard` | `#FFFFFF` | Cards, primary surface |
| `surfaceRaised` | `#F3F1EA` | Chips, raised surfaces |
| `surfaceSunken` | `#F7F4EC` | Inputs |
| `divider` | `#E8E5DC` | Hairline divider |
| `dividerStrong` | `#D6D2C6` | Stronger hairline |
| `crimson` | `#C8102E` | Primary accent; **Want** tag |
| `crimsonDeep` | `#A40E25` | Pressed/deep red |
| `marketGreen` | `#0B6B3A` | Secondary; **Need** tag |
| `gold` | `#E8A92A` | Premium trim, totals |
| `textPrimary` | `#1A1A1A` | Headings/body |
| `textSecondary` | `#5A5A5A` | Captions |
| `textMuted` | `#8A8A8A` | Timestamps/labels |

Type semantics (from Android `Color.kt`): **Need = green**, **Want = red**. This is
carried into the donut, badges, and day totals.

---

## 1. Non-negotiables (ported from Android)

1. SwiftUI + SwiftData. No UIKit storyboards, no third-party chart/DB libs.
2. Offline first. No network, no accounts, no analytics.
3. Currency-immune storage — all costs stored as `Int64` cents; UI formats only.
4. 35-day hard cap — auto-purge entries older than 35 days on launch.
5. 20-entry input cap with a "Start new sheet" handoff (grid never cleared silently).
6. Hand-rolled charts (SwiftUI `Canvas`). No chart libraries.
7. Light supermarket palette (§0). One authored look — no theme toggle in v1.
8. No data loss: a sealed row is a saved row. Killing the app mid-sheet loses nothing.

---

## 2. Data model

```swift
@Model final class Entry {
    var id: UUID            // default UUID()
    var dateUtc: Date       // epoch-equivalent Date
    var date: String        // "yyyy-MM-dd"  (grouping)
    var time: String        // "HH:mm" (24h local)
    var item: String
    var costCents: Int64    // currency-immune
    var typeRaw: String     // "NEED" | "WANT"
    // computed `type: EntryType` derived from typeRaw
}
enum EntryType: String { case need = "NEED"; case want = "WANT" }
```

Storage behavior (SwiftData `EntryStore`, `@MainActor @Observable`):
- `insert(_:)`, `delete(_:)`, `deleteAll()`
- `purgeOlderThan(days: 35)` — called once on `App.init`
- `stats(for: Period)` → `SummaryStats`
- `entries` — published, sorted `dateUtc` desc
- `sheetCount` / `isSheetFull` — see parity note below

**Retention:** `purgeOlderThan(days: 35)` deletes `dateUtc < now-35d`. No prompt.

**PARITY NOTE (carried from Android):** In the Android `InputViewModel`, the sheet
counter uses `dao.observeAll().size` — i.e. the 20-cap counts **total** entries in the
database, not entries for "today". This port replicates that exactly (`isSheetFull =
entries.count >= 20`). If you intended a per-day 20 cap, change `isSheetFull` to count
today's entries only. Flagged so the behavior is a conscious choice, not a bug.

---

## 3. Currency

Stored as `costCents: Int64`. Display via `CurrencyFormatter.format(_:symbol:)`.
`AppSettings` persists `currencySymbol` + `currencyCode` in `UserDefaults` (default `₱` /
`PHP`). Switching currency reformats all displays; stored cents are never mutated.

```
func format(_ cents: Int64, symbol: String) -> String {
    let whole = cents / 100
    let c = abs(cents) % 100
    return "\(symbol) \(whole).\(String(format: "%02d", c))"
}
```

---

## 4. Information architecture

```
Tab bar (4, floating pill — matches Android nav):
  Summary   Log   History   Settings

Instructions overlay (not a route):
  - auto-shows on first launch (AppSettings.firstLaunch)
  - "?" in Summary header
  - "How it works" row in Settings
```

### Screen map
```
Summary (default) ─┐
                    ├─[Log an expense]→ Log
Log ───────────────┤
                    ├─[history CTA]→ History
History ───────────┤
                    └─35-day ledger, swipe/delete confirm
Settings ─ currency switch, data wipe, about
```

---

## 5. Screen specs (behavioral parity with Android)

### 5.1 Summary
- Editorial header `NEEDS / vs / WANTS` (serif) + `Expense Tracker` + period label.
- Period rotor pills: `Day | Week | All (35d)`, default **Day**.
- Range caption under rotor: `Jul 27` / `Jul 21 – Jul 27` / `Jun 23 – Jul 27`.
- Hand-built `Canvas` donut: Need (green) / Want (red), total centered, legend %.
  Empty state: hairline ring + "Log your first expense to start the diary."
- 3 stat cards: NEEDS / WANTS / NEED %.
- Primary CTA: **Log an expense** → Log tab.

### 5.2 Log
- Header `TODAY · <date>` + `LOG` + `SHEET n / 20`.
- Active entry card: `Item` (full width), `Cost` + `[Need] [Want]` chips.
- Auto-seal the instant item+cost+type are valid → stamps `now()`, saves immediately,
  resets the active card. No "Log" button (sealing *is* saving).
- Sealed ledger below: sticky `TIME | ITEM | COST | TYPE` header, compact read-only rows.
- Delete → confirm alert.
- At 20/20: active card replaced by **Start new sheet** prompt (before any 21st entry).
- Layout: fixed column widths (Time 48 / Cost 88 / Type 42 / Delete 32) so TYPE never
  wraps; cost uses `.monospacedDigit()`. Stable at iPhone SE width.

### 5.3 History
- Header `LEDGER` + date range present.
- Grouped by day (newest first). Each day: date header, `n entries`, Need/Want day totals,
  then rows. Delete confirm.
- Empty state: ring + "The page waits for ink."
- CTA: **Log an expense** → Log tab.

### 5.4 Settings
- Currency radio: `₱ PHP` (default), `$ USD`, `€ EUR`, `¥ JPY`, `S$ SGD`.
- Data: **Wipe diary** (confirm alert) → deletes all entries + resets settings.
- About: `v1.0.0` + one-line description.

### 5.5 Instructions overlay
- 3 swipeable cards: (1) every expense is Need or Want, (2) diary keeps 35 days,
  (3) rows seal themselves. Auto on first launch; also from Summary "?" and Settings.

---

## 6. Project structure

```
ios/
  IOS_IMPLEMENTATION_PLAN.md     (this file)
  BUILD.md                       (Mac build + TestFlight steps)
  project.yml                    (XcodeGen — regenerates .xcodeproj)
  NeedsVsWants/
    Info.plist
    App/NeedsVsWantsApp.swift
    Models/{Entry,EntryType,CurrencyOption,Period,SummaryStats}.swift
    Storage/{CurrencyFormatter,AppSettings,EntryStore}.swift
    Screens/Summary/SummaryView.swift
    Screens/Log/{LogView,LogLogic}.swift
    Screens/History/HistoryView.swift
    Screens/Settings/SettingsView.swift
    Screens/Onboarding/InstructionsView.swift
    Components/{Eyebrow,GiltRule,PremiumCard,PrimaryButton,TypeBadge,LedgerHeader,LedgerRow}.swift
    Theme/{Colors,Typography,Spacing}.swift
    Navigation/TabContainer.swift
```

---

## 7. Build caveat (important)

This codebase was authored on a Windows host without macOS/Xcode, so it has **not** been
compiled here. All Swift is written to iOS 17 SwiftUI/SwiftData conventions and should
compile in Xcode 15+. Build steps are in `BUILD.md` (XcodeGen then `xcodegen generate`,
open, run on simulator). QA matrix from the original plan (iPhone SE / 13 / 15 Pro Max,
light mode, Dynamic Type, 20-cap, 35-day retention, currency switch, relaunch
persistence) is the acceptance checklist.

---

## 8. Suggested timeline (per original plan)

- Day 1: scaffold + theme + models + persistence
- Day 2: Log + History
- Day 3: Summary + Settings + onboarding
- Day 4: polish + size QA + TestFlight prep

## 9. Decisions locked

- **Light supermarket palette** (not the stale dark text).
- **SwiftData** over Core Data (iOS 17 target).
- **TabView-style custom floating pill nav** matching Android.
- **Total-entry 20-cap** replicated for parity (see §2 note).
- **Hand-rolled Canvas charts** — no third-party dependency.
