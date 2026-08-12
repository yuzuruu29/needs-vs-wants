> ARCHIVED 2026-08-13 — historical snapshot, do not trust for current state.

# Needs Vs. Wants — Implementation Plan

> Android APK. Single-purpose trainer that forces every purchase into **Need** or **Want**.
> Target: looks intentional, premium, and period-evocative — never AI-generated, never generic.

> **Historical note (2026-08-09):** This document is a stale early plan. Trust
> `Decisions.md` + the source for ground truth. In particular, the Free-tier
> retention below was **35 days** at writing but is now **30 days**
> (`Entitlement.FREE_RETENTION_MILLIS`), and the Free tier is a **5 logs/day
> local allowance with streak carry-forward** (no ads). The paywall/entitlement
> model has evolved substantially since this plan (see Decisions D45–D122).

---

## 0. Non-Negotiables

1. **Kotlin + Jetpack Compose + Material 3.** No React Native, no Flutter.
2. **Offline first.** No backend, no accounts, no analytics phoning home.
3. **Currency-immune storage.** All costs stored as `Long` cents; the UI formats.
4. **Retention hard cap.** Auto-purge older entries on launch (Free 30 days; Pro/Max keep all).
5. **20-item input cap per sheet** with a confirm-first "Start new sheet" handoff — the grid is **never cleared silently**.
6. **Hand-rolled charts** (Canvas `PieChart` / `BarChart`). No chart libraries.
7. **Friendster 2003 premium palette** (see §1). No gradient meshes, no purple-to-pink SaaS look.
8. **Dark theme only in v1.** No light mode, no theme toggle — one authored look.
9. **No data loss, ever.** A sealed row is a saved row. Killing the app mid-sheet loses nothing.

---

## 1. Design System — "Friendster 2003, Premium"

### 1.1 Palette (dark is default — a budgeting diary is best read low-light)

| Token | Hex | Role |
|------|------|------|
| `surface_night` | `#0B1220` | App background |
| `surface_card` | `#111A2B` | Cards, grid rows |
| `surface_card_alt` | `#16223A` | Alternating row, hover |
| `primary_sky` | `#7BD1FF` | Friendster link blue; "Need" tag |
| `secondary_rose` | `#FF8FB1` | Friendster smile pink; "Want" tag |
| `gold_trim` | `#E6B864` | Hairlines, title underline, splash trim |
| `text_primary` | `#F2F5FB` | Headings, body |
| `text_secondary` | `#9FB0CC` | Captions, timestamps |
| `danger` | `#FF6B6B` | Delete, "Start new sheet" warning |
| `divider` | `#1F2C45` | Subtle hairline |

**Why not light mode default:** Budgeting is often done at night. The night-blue ground makes the gold trim and cyan/rose accents *pop* like glossy Friendster buttons against a dark profile — strictly premium, strictly period.

### 1.2 Typography

| Role | Family | Weight | Notes |
|------|--------|--------|-------|
| Display titles | Playfair Display SC | 700 | Embossed feel, all small-caps |
| Body, UI | Inter | 400 / 500 / 600 | Tight, modern, legible |
| Numbers, cost | Inter Tight (mono-feel) | 600 | Tabular figures for the ledger |
| Timestamps | Inter | 500 | 12sp, `text_secondary` |

### 1.3 Shape & Material

- **Corner radius:** 6dp on cards & inputs, 4dp on chips. No 24dp "iOS bubble" corners.
- **Elevation:** Single soft shadow under cards: `0,2,16,#00000033`. No Material 3 floating tonal lifts — they read "modern Android", not "premium diary".
- **Dividers:** 1dp, `divider` colour, full-width hairlines. Gold hairline only on section headers.
- **Buttons:** Solid `surface_card` with 1.5dp `primary_sky` or `gold_trim` border. Pressed state fills the accent colour at 12%.
- **Micro-typography trick for premium feel:** every section header gets a 2dp gold underline 28dp wide, offset 8dp below the label.

### 1.4 Motion

- Tab transitions: 200ms ease-out slide.
- Row stamps: a 160ms gold hairline sweep across the Date/Time cells (the row "gets sealed").
- No bounce. No spring. Budgeting deserves calm.

### 1.5 Anti-AI-Slop rules (literal)

- ❌ No purple-to-pink gradient meshes.
- ❌ No `rounded-3xl` on everything.
- ❌ No emoji as primary icons. Use Material Symbols outlined at 24dp.
- ❌ No "AI-generated" placeholder copy ("Welcome to your dashboard 🚀").
- ❌ No glassmorphism blurs on dark cards.
- ✅ Real hairlines, real small-caps display type, real contrast between fill and border.

---

## 2. Tech Stack & Build

### 2.1 Toolchain
- Kotlin 1.9.x, JDK 17
- Android Gradle Plugin 8.5.x
- `minSdk 24`, `targetSdk 34`, `compileSdk 34`
- Kotlin DSL (`build.gradle.kts`)
- Version catalog (`libs.versions.toml`)

### 2.2 Key libraries (only these — nothing else)
- `androidx.compose:compose-bom:2024.09.x`
- `androidx.compose.material3:material3`
- `androidx.lifecycle:lifecycle-viewmodel-compose`
- `androidx.datastore:datastore-preferences`
- `androidx.room:room-runtime`, `room-ktx`, `room-compiler` (KSP)
- `com.google.dagger:hilt-android`, `hilt-compiler`
- `androidx.navigation:navigation-compose`
- Fonts: `androidx.compose.ui:ui-text` with bundled Playfair Display SC + Inter resources

### 2.3 Build commands
```bash
./gradlew assembleDebug       # dev build
./gradlew test                 # unit + room tests
./gradlew lint                 # Android Lint
./gradlew assembleRelease      # signed APK
```

---

## 3. Information Architecture

```
BottomNav (4 tabs):
  Summary    Input    History    Settings

Instructions overlay (not a route, not a FAB):
  - auto-shows on first launch
  - "?" icon in the Summary top app bar
  - "How it works" row in Settings
```

### Screen map
```
Summary (intro) ─┐
                 ├─[Log a Purchase / FAB]→ Input
Input ───────────┤
                 ├─[View History]→ History
History ─────────┤
                 └─30-day ledger, swipe-delete
Settings ─ currency switch, data wipe, about
```

---

## 4. Screen Specs

### 4.1 Summary / Intro Screen (default launch)

**Header**
- Embossed title `NEEDS vs WANTS` in Playfair Display SC, gold underline.
- Subtitle: current period label ("Today" / "This Week" / "All 30 Days"). Date/Time auto-stamped.

**Body — Period Rotor**
- Pill row: `Day | Week | All (30d)`. Default: **Day**.
- **Honest labels:** the third pill is *not* called "Month" — the retained window is 30 days and users read "Month" as a calendar month. `All (30d)` says exactly what it shows.
- Directly under the rotor, a 12sp `text_secondary` caption shows the resolved range, e.g. `Jul 27` / `Jul 21 – Jul 27` / `Jun 23 – Jul 27`. The numbers always explain themselves.
- The rotor slices the same dataset; no new fetches.

**Body — Chart**
- Canvas `PieChart`. Two slices: Need (cyan) / Want (pink).
- Center: total spend, formatted with active currency.
- Below pie: small legend with percentages.
- When period has 0 entries: instead of empty pie, show a single hairline-gold ring and the text *"Log your first purchase to start the diary."* with a CTA.

**Body — Stat strip**
- 3 mini cards: `Needs total` / `Wants total` / `Need %`
- Each with a tiny bar showing relative share (cyan vs pink).

**Footer**
- Primary button: **Log a purchase** → navigates to Input.
- Secondary text button: **View history** → History.

### 4.2 Input Screen — one active entry, a ledger of sealed rows

> Design premise: people log purchases **one at a time**, seconds after buying. The screen is built
> around a single fast entry; the ledger aesthetic lives in the sealed rows below it — not in a
> 5-column spreadsheet that fights a 360dp phone.

**Layout**
- **Active entry card** (top, always exactly one): three comfortable fields —
  `Item` (full width), `Cost` + `[Need] [Want]` chips on the second line.
- **Sealed ledger** below: a sticky small-caps header `Time | Item | Cost | Type`, then compact
  read-only rows (`14:08  Coffee   420.00  [N]`) for this sheet, newest last. Date is implied —
  the sheet header shows today's date once, Playfair SC with gold underline. No Date column
  burning 60dp per row.
- Sheet counter chip in the header: `Sheet · 7 / 20`.

**Entry behaviour**
1. User types **Item** — letters in any script, digits, spaces, basic punctuation. Disallowed
   characters (control chars, newlines) are **silently stripped** — no rejection shake, no error
   state. Input friction teaches nothing here.
2. User types **Cost** — numeric, max 2 decimal places, enforced by the field's filter (again:
   filtered, not shaken). No currency symbol in the cell; the symbol lives **only** in the sticky
   ledger header.
3. User picks **Type** — `[Need] [Want]` chips. Active chip solid accent; inactive hairline-bordered.
4. The instant item+cost+type are all valid, the entry **seals**: it auto-stamps with `now()`
   (local TZ, 24h), **saves to Room immediately**, and drops into the sealed ledger with the 160ms
   gold hairline sweep. A fresh empty active card appears. **There is no "Log" button** — sealing
   *is* saving. One mechanic, no ambiguity, no lost work if the app dies.
5. Sealed rows are read-only. **Long-press to "unseal"** (gentle confirm; the row is re-opened in
   the active card and re-stamps on re-seal). Because long-press is invisible, the **first time a
   user taps a sealed row**, a one-shot tooltip appears: *"Hold to unseal."*
6. When the sheet holds 20 sealed rows, the active card is replaced by a **Start new sheet**
   prompt — shown *before* any 21st entry is typed, never after. Confirming archives the sheet
   (rows are already saved; this only resets the visible sheet and counter). Nothing is cleared
   without the user's explicit confirm, so no "Undo" race timer is needed.

**Footer**
- Sticky bottom toolbar: settings gear, history icon, and the currency symbol (tap → Settings).
  No save button (see 4), no "?" (instructions live on Summary + Settings, §4.5).

### 4.3 History Screen

**Header**
- Embossed `LEDGER`, small-caps subtitle showing the range present, e.g. `Jun 23 – Jul 27, 2026`.

**Body**
- Grouped by day, newest first.
- Each day is a card with: date header (Playfair SC) gold underline, sum chip (cyan/pink split), then rows.
- Each row: `hh:mm  Item            ₱ 420  [N]` (or `[W]`). Swipe-left = delete with confirm.
- Empty-state: hairline-gold ring + *"Your diary is empty. The page waits for ink."*

**Footer**
- CTA back to Input: **Log a purchase**.

### 4.4 Settings Screen

- Currency selection (radio): `₱ PHP (default)`, `$ USD`, `€ EUR`, `¥ JPY`, `S$ SGD`, `Custom symbol`.
- Data controls: `Wipe diary` (double confirm).
- About: app version, single line: *"A 30-day trainer to help you tell needs from wants."*

### 4.5 Instructions Overlay

Three cards, swipeable, dismissible. **Auto-shows on first launch**; afterwards reachable from the
"?" icon in the Summary top app bar and a "How it works" row in Settings. (No floating "?" FAB —
it crowded the bottom bar and read as a fifth tab.)

1. **"Every purchase is either a Need or a Want."** — explanation of the binary force, with two example rows shown.
2. **"Your diary keeps 30 days."** — auto-purge explained, with a tiny timeline graphic.
3. **"Rows seal themselves — hold one to unseal."** — explains the auto-stamp, that sealing = saving, and demonstrates the long-press-to-edit gesture explicitly.

Type-spec: 16sp body, generous leading, Playfair SC card titles, Material Symbols outlined illustrations (not emoji), real contrast on `surface_card`.

---

## 5. Data Model

### 5.1 Entity

```kotlin
@Entity(tableName = "entries")
data class Entry(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val dateUtc: Long,          // epoch millis
  val date: String,           // "2026-07-27"  (for grouping)
  val time: String,           // "14:08"       (24h local)
  val item: String,           // alphanumeric
  val costCents: Long,        // currency-immune
  val type: EntryType         // NEED | WANT
)

enum class EntryType { NEED, WANT }
```

### 5.2 DAO
```kotlin
@Dao
interface EntryDao {
  @Insert suspend fun insert(entry: Entry): Long
  @Query("SELECT * FROM entries WHERE dateUtc >= :since ORDER BY dateUtc DESC")
  fun observeSince(since: Long): Flow<List<Entry>>
  @Query("DELETE FROM entries WHERE dateUtc < :before")
  suspend fun purgeBefore(before: Long): Int
  @Query("SELECT COUNT(*) FROM entries WHERE date= :date")
  suspend fun countForDate(date: String): Int
  @Delete suspend fun delete(entry: Entry)
}
```

### 5.3 30-day cap
On `Application.onCreate`, fire `dao.purgeBefore(now - 30 days)`. No user prompt.

### 5.4 20-item cap
Counted in-memory on the Input VM. Storage has no such cap.

---

## 6. Summary Engine

`SummaryUseCase.entriesFor(period: Period): Flow<SummaryStats>` where:

```kotlin
data class SummaryStats(
  val needsTotalCents: Long,
  val wantsTotalCents: Long,
  val needsCount: Int,
  val wantsCount: Int,
  val totalCents: Long,
  val needsPct: Int,
  val wantsPct: Int
)

enum class Period { DAY, WEEK, ALL }   // UI labels: Day / Week / All (30d)
```

- **DAY**: today's date only.
- **WEEK**: last 7 days including today.
- **ALL**: the full 30-day retained window. (Formerly labelled "Month" — renamed because users read
  "Month" as a calendar month and would distrust totals that silently span 30 days. The resolved
  date range is always captioned under the rotor, §4.1.)

### Charts (hand-rolled)
- `PieChart(state)` — Canvas `drawArc`, two slices, gold center hairline.
- `ShareBar` — 1dp hairline rectangle split cyan/pink by ratio.

Both take `SummaryStats` and render deterministically; no animation on initial draw, only on period change (200ms cross-dissolve).

---

## 7. Currency

Stored as `costCents: Long`. Display layer uses `CurrencyFormatter(symbol, code)`:

```kotlin
fun Long.toMoney(symbol: String): String {
  val whole = this / 100
  val cents = (abs(this) % 100).toString().padStart(2, '0')
  return "$symbol $whole.$cents"
}
```

Settings persists the chosen `symbol` in DataStore. Default `₱`.

---

## 8. Navigation

`NavHost` with 4 routes: `summary`, `input`, `history`, `settings`.
- `summary` is start destination.
- Bottom bar visible on all four.
- Instructions overlay: a Box-level Compose `Dialog` (not a route) toggled by a VM boolean.
  Triggers: first-launch flag in DataStore, "?" icon in the Summary top app bar, "How it works"
  row in Settings. No FAB.

---

## 9. TDD Plan (minimum 80% coverage)

Per `tdd-workflow` skill: write failing test → minimal impl → refactor → commit checkpoint.

### 9.1 Unit tests
- `EntryDaoTest` — insert, observeSince, purgeBefore round-trip (Room in-memory DB).
- `SummaryUseCaseTest` — DAY/WEEK/ALL ranges, totals, percentages, empty case.
- `CurrencyFormatterTest` — PHP, USD, negative, zero, custom symbol.
- `InputValidatorTest` — item filter strips control chars but keeps any-script letters, cost decimal filter, row-completion logic.
- `PurgeOnLaunchTest` — entries older than 30 days are removed at app start.

### 9.2 Compose UI tests
- `InputScreenTest` — disallowed chars silently stripped, seal auto-stamps **and persists to Room**, 20th seal shows Start-new-sheet prompt before a 21st entry is possible, tap-on-sealed-row shows one-shot "Hold to unseal" tooltip, long-press unseals into the active card.
- `SummaryScreenTest` — period rotor swaps dataset, range caption matches selected period, empty state renders ring + CTA.
- `HistoryScreenTest` — grouping by day, swipe-delete.
- `InstructionsOverlayTest` — three cards swipe, dismiss persists.

### 9.3 E2E (instrumented)
- Install, tap Instructions, dismiss.
- Log 3 purchases (2 Needs, 1 Want).
- Kill the app mid-sheet, relaunch → sealed rows are still there (seal = save).
- Open Summary, tap **Day** → 3 entries; tap **All (30d)** → caption shows the 30-day range.
- Change currency → Summary totals reformat.
- Wait (or fake clock) → 36th day entry auto-purges on next launch.

---

## 10. Phased Delivery (with git checkpoints)

| Phase | Scope | Commit |
|------|-------|--------|
| P0 | Project init, theme, fonts, nav shell | `feat: scaffold NeedsVsWants APP shell` |
| P1 | Room + DAO (TDD) | `feat: add entry DAO with retention tests` |
| P2 | Input screen (TDD) | `feat: add 20-cap input ledger with auto-stamp` |
| P3 | Summary engine + charts (TDD) | `feat: add summary engine and hand-rolled pie chart` |
| P4 | History screen | `feat: add 30-day ledger view` |
| P5 | Settings + currency | `feat: add currency switching and data controls` |
| P6 | Instructions overlay | `feat: add first-time-user instructions overlay` |
| P7 | Polish pass (motion, hairlines, splash) | `polish: friendster-2003-premium chrome` |
| P8 | Build & sign APK | `chore: sign release APK` |

---

## 11. Build & Ship

```bash
# 1. Dev build
./gradlew assembleDebug

# 2. Full QA + tests
./gradlew test lint

# 3. Release APK (signed with keystore)
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/needs-vs-wants.apk`

ProGuard rules: keep Room-generated, keep Hilt entry points, keep `EntryType` enum for Room.

---

## 12. Quality Gates (Definition of Done)

- [ ] All screens render at 360dp / 412dp / 480dp / 600dp widths without overflow.
- [ ] Dark theme passes WCAG AA contrast for body text on `surface_card`.
- [ ] No `console.log` equivalent (no stray `Log.d` outside a thin Logger wrapper).
- [ ] No third-party analytics, ads, or crash SDKs.
- [ ] APK ≤ 6 MB.
- [ ] First-time user can: open → dismiss instructions → log first purchase → see it on Summary, in < 90 seconds unaided.
- [ ] Force-stopping the app after sealing a row never loses that row.
- [ ] Lint passes with 0 errors. Tests pass with ≥ 80% line coverage on the data + summary packages.

---

## 13. Out of Scope (explicitly)

- No iCloud/Drive sync, no export-to-CSV (v2 conversation).
- No multi-user or family sharing.
- No budget goals — the *binary choice* is the lesson.
- No ML insights ("you spend 40% on coffee") — feels AI-generated, refused.
- No web version.
