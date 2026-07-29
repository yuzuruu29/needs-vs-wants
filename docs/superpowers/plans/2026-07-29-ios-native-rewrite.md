# Needs vs. Wants — Native iOS Rewrite Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a brand-new, native-iOS-architected SwiftUI + SwiftData app under `ios-native/` that replaces the translated `ios/` port, compiled and tested via GitHub Actions (no local Mac required).

**Architecture:** MVVM + Repository + SwiftData. `@Observable` ViewModels own state and delegate to a `Result`-returning `EntryRepository`; views use `@Query` for reads. Native `TabView` + `NavigationStack` + an `@Observable` `AppModel` coordinator replace the old opacity-toggled `ZStack`. Single `dateUtc` source of truth with computed display strings. Supermarket brand palette as adaptive light/dark semantic tokens.

**Tech Stack:** Swift 5.9+, SwiftUI, SwiftData, `@Observable`/`@Query` (iOS 17.0+), XcodeGen, GitHub Actions (macOS-14), XCTest.

---

## Locked decisions (from user, 2026-07-29)

| # | Decision | Choice |
|---|---|---|
| Loc | Where the app lives | `ios-native/` folder in this repo; old `ios/` port kept as reference |
| Palette | Visual theme | **Supermarket brand, adaptive** — D7 colors (crimson `#C8102E`, market green `#0B6B3A`, gold `#E8A92A`, warm cream `#FAFAF7`) as semantic tokens with dark-mode variants |
| 20-cap | Sheet cap semantics | **Total entries (Android parity)** — `isSheetFull = entries.count >= 20` (kept as conscious parity choice, recorded) |
| Type | Fonts | **Hybrid** — Playfair Display SC (bundled) for display titles; SF Pro system for body/UI |

**Vault record:** D12 in `Projects/Needs vs Wants/Decisions.md` records the mirror→native strategy reversal of D9.

---

## Non-negotiables (carried from Android + D2/D3/D4/D6)

1. SwiftUI + SwiftData only. No UIKit storyboards, no third-party chart/DB libs.
2. Offline first. No network, accounts, or analytics.
3. Currency-immune storage — `costCents: Int64`; UI formats via `NumberFormatter(.currency)`.
4. 35-day hard cap — auto-purge on launch (throttled to once per launch).
5. 20-entry input cap (total-count parity) → "Start new sheet" handoff; grid never cleared silently.
6. Hand-rolled `Canvas` charts. No chart libraries.
7. A sealed row is a saved row — no data loss on kill mid-sheet.
8. Need = green, Want = red (D7 semantic mapping).

---

## File structure

```
ios-native/
├── project.yml                              — XcodeGen spec (app + test targets)
├── Info.plist
├── BUILD.md
├── NeedsVsWants/
│   ├── App/
│   │   ├── NeedsVsWantsApp.swift            — @main, ModelContainer, throttled scenePhase purge
│   │   └── AppModel.swift                    — @Observable nav coordinator (Tab enum, selectedTab, routes)
│   ├── Data/
│   │   ├── Entry.swift                       — @Model: id, dateUtc, item, costCents, typeRaw; computed display
│   │   ├── EntryType.swift                   — enum {need, want}, Codable
│   │   ├── Period.swift                      — enum {day, week, all}
│   │   ├── SummaryStats.swift                — value type: needsTotalCents, wantsTotalCents, counts, needPct
│   │   ├── CurrencyOption.swift              — enum of PHP/USD/EUR/JPY/SGD with code+symbol+locale
│   │   ├── EntryRepository.swift             — Result-based CRUD, purge, sheetCount, stats delegation
│   │   ├── StatsEngine.swift                 — period windowing + aggregation (pure)
│   │   └── CurrencyFormatter.swift           — NumberFormatter(.currency), JPY no-minor-unit aware
│   ├── Design/
│   │   ├── AppColors.swift                   — adaptive semantic tokens (light+dark)
│   │   ├── AppTypography.swift               — Playfair display + SF body, Dynamic Type ramps
│   │   └── Haptics.swift                     — impact(.medium) on seal; notification(.success/.warning) on delete/20-cap/wipe
│   ├── Components/
│   │   ├── CurrencyText.swift                — monospacedDigit, adaptive size
│   │   ├── NeedWantBadge.swift               — native chip/tag, accessibilityLabel
│   │   ├── DonutChart.swift                  — Canvas, dynamic-type-sized, legend %
│   │   ├── ShareBar.swift                    — hairline ratio bar
│   │   ├── LedgerHeader.swift                — shared column geometry (D8)
│   │   ├── LedgerRow.swift                   — shared column geometry (D8), Button delete
│   │   ├── PrimaryButton.swift               — crimson filled, accessibility
│   │   └── Eyebrow.swift                     — small-caps label
│   ├── Features/
│   │   ├── Summary/{SummaryView, SummaryViewModel}.swift
│   │   ├── Log/{LogView, LogViewModel}.swift
│   │   ├── History/{HistoryView, HistoryViewModel}.swift
│   │   ├── Settings/{SettingsView, SettingsViewModel}.swift
│   │   └── Onboarding/OnboardingView.swift
│   └── Resources/
│       └── PlayfairDisplaySC.ttf             — bundled display font
└── NeedsVsWantsTests/
    ├── EntryRepositoryTests.swift
    ├── StatsEngineTests.swift
    ├── CurrencyFormatterTests.swift
    ├── LogViewModelTests.swift
    └── SummaryViewModelTests.swift
```

CI: `.github/workflows/ios-native.yml`.

---

## Task P0 — Scaffold + CI

**Files:** Create `ios-native/project.yml`, `ios-native/Info.plist`, `ios-native/BUILD.md`, `.github/workflows/ios-native.yml`, `ios-native/NeedsVsWants/Resources/PlayfairDisplaySC.ttf` (placeholder note).

- [ ] **P0.1: Write `project.yml`** — XcodeGen spec: iOS 17.0, app target `NeedsVsWants` + test target `NeedsVsWantsTests`, SwiftData framework, bundled font.
- [ ] **P0.2: Write `Info.plist`** — launch screen, font registration (`ATSApplicationFontsPath`/`UIAppFonts`).
- [ ] **P0.3: Write CI workflow** — macOS-14, install xcodegen, `xcodegen generate`, `xcodebuild -scheme NeedsVsWants -destination 'platform=iOS Simulator,name=iPhone 15' build test`, upload build/test logs as artifact.
- [ ] **P0.4: Commit.** `git add ios-native .github/workflows/ios-native.yml && git commit -m "feat(ios-native): scaffold project + CI"`

---

## Task P1 — Data layer (TDD)

### P1.1: Entry model (single source of truth)

**Files:** Create `ios-native/NeedsVsWants/Data/Entry.swift`, `EntryType.swift`.

- [ ] **Write `EntryType.swift`:**
```swift
import Foundation

enum EntryType: String, Codable, CaseIterable {
    case need = "NEED"
    case want = "WANT"
}
```

- [ ] **Write `Entry.swift`** — denormalized strings REMOVED; display strings computed:
```swift
import Foundation
import SwiftData

@Model
final class Entry {
    var id: UUID
    var dateUtc: Date
    var item: String
    var costCents: Int64
    var typeRaw: String

    init(id: UUID = UUID(), dateUtc: Date, item: String, costCents: Int64, type: EntryType) {
        self.id = id
        self.dateUtc = dateUtc
        self.item = item
        self.costCents = costCents
        self.typeRaw = type.rawValue
    }

    var type: EntryType { EntryType(rawValue: typeRaw) ?? .need }

    // Computed display strings — single source of truth is dateUtc.
    var dayKey: String { Self.dayFormatter.string(from: dateUtc) }       // "yyyy-MM-dd"
    var timeLabel: String { Self.timeFormatter.string(from: dateUtc) }   // "HH:mm"

    static let dayFormatter: DateFormatter = {
        let f = DateFormatter(); f.dateFormat = "yyyy-MM-dd"; return f
    }()
    static let timeFormatter: DateFormatter = {
        let f = DateFormatter(); f.dateFormat = "HH:mm"; return f
    }()
}
```

- [ ] **Commit.** `feat(ios-native): Entry model with single dateUtc source of truth`

### P1.2: CurrencyFormatter (JPY-correct — fixes old port bug)

**Files:** Create `CurrencyFormatter.swift`, `CurrencyOption.swift`. Test: `CurrencyFormatterTests.swift`.

- [ ] **Write failing test** — JPY must not show decimals; PHP/USD show 2:
```swift
import XCTest
@testable import NeedsVsWants

final class CurrencyFormatterTests: XCTestCase {
    func test_php_shows_two_decimals() {
        let s = CurrencyFormatter.shared.format(cents: 12345, currency: .php)
        XCTAssertTrue(s.contains("123.45"), "got \(s)")
    }
    func test_jpy_no_minor_units() {
        // 1000 cents → ¥10 (JPY stored whole-yen as cents; 1000 cents = 10 yen)
        let s = CurrencyFormatter.shared.format(cents: 1000, currency: .jpy)
        XCTAssertTrue(s.contains("10") && !s.contains("."))
    }
}
```

- [ ] **Run test (CI) → expect FAIL** (no impl).

- [ ] **Write impl:**
```swift
import Foundation

enum CurrencyOption: String, CaseIterable, Codable {
    case php, usd, eur, jpy, sgd
    var code: String { rawValue.uppercased() }
    var symbol: String {
        switch self { case .php: "₱"; case .usd: "$"; case .eur: "€"; case .jpy: "¥"; case .sgd: "S$" }
    }
    var locale: Locale {
        switch self {
        case .php: Locale(identifier: "en_PH")
        case .usd: Locale(identifier: "en_US")
        case .eur: Locale(identifier: "en_IE")
        case .jpy: Locale(identifier: "ja_JP")
        case .sgd: Locale(identifier: "en_SG")
        }
    }
}

final class CurrencyFormatter {
    static let shared = CurrencyFormatter()
    private init() {}

    func format(cents: Int64, currency: CurrencyOption) -> String {
        let nf = NumberFormatter()
        nf.numberStyle = .currency
        nf.locale = currency.locale
        nf.currencyCode = currency.code
        // JPY (and any zero-minor currency) — convert cents to whole units, no decimals.
        if currency == .jpy {
            nf.maximumFractionDigits = 0
            nf.minimumFractionDigits = 0
            return nf.string(from: NSNumber(value: cents / 100)) ?? ""
        }
        return nf.string(from: NSNumber(value: Double(cents) / 100.0)) ?? ""
    }
}
```

- [ ] **Run test → PASS. Commit.**

### P1.3: StatsEngine (pure, testable)

**Files:** `Period.swift`, `SummaryStats.swift`, `StatsEngine.swift`. Test: `StatsEngineTests.swift`.

- [ ] **Write `Period` + `SummaryStats`:**
```swift
import Foundation

enum Period: String, CaseIterable, Hashable {
    case day, week, all
    var label: String { switch self { case .day: "Day"; case .week: "Week"; case .all: "All (35d)" } }
}

struct SummaryStats: Equatable {
    var needsTotalCents: Int64 = 0
    var wantsTotalCents: Int64 = 0
    var needsCount: Int = 0
    var wantsCount: Int = 0
    var totalCents: Int64 { needsTotalCents + wantsTotalCents }
    var needPct: Double {
        totalCents == 0 ? 0 : Double(needsTotalCents) / Double(totalCents)
    }
}
```

- [ ] **Write failing test** — week window is last 7 days inclusive; all is 35 days:
```swift
func test_week_includes_today_and_prior_six() {
    let cal = Calendar.current
    let now = cal.date(from: DateComponents(year: 2026, month: 7, day: 29))!
    let engine = StatsEngine(calendar: cal, now: now)
    let entries = [
        Entry(dateUtc: now, item: "a", costCents: 100, type: .need),
        Entry(dateUtc: cal.date(byAdding: .day, value: -6, to: now)!, item: "b", costCents: 200, type: .want),
        Entry(dateUtc: cal.date(byAdding: .day, value: -7, to: now)!, item: "c", costCents: 999, type: .need),
    ]
    let s = engine.stats(for: .week, entries: entries)
    XCTAssertEqual(s.totalCents, 300)            // -7 day excluded
    XCTAssertEqual(s.needsCount, 1); XCTAssertEqual(s.wantsCount, 1)
}
```

- [ ] **Run → FAIL. Write impl:**
```swift
import Foundation

struct StatsEngine {
    let calendar: Calendar
    let now: Date
    init(calendar: Calendar = .current, now: Date = Date()) { self.calendar = calendar; self.now = now }

    func startOf(_ period: Period) -> Date {
        let today = calendar.startOfDay(for: now)
        switch period {
        case .day:  return today
        case .week: return calendar.date(byAdding: .day, value: -6, to: today)!
        case .all:  return calendar.date(byAdding: .day, value: -34, to: today)!
        }
    }

    func stats(for period: Period, entries: [Entry]) -> SummaryStats {
        let since = startOf(period)
        var s = SummaryStats()
        for e in entries where e.dateUtc >= since {
            if e.type == .need { s.needsTotalCents += e.costCents; s.needsCount += 1 }
            else { s.wantsTotalCents += e.costCents; s.wantsCount += 1 }
        }
        return s
    }

    func rangeCaption(_ period: Period) -> String {
        let f = DateFormatter(); f.dateFormat = "MMM d"
        switch period {
        case .day:  return f.string(from: now)
        case .week, .all:
            return "\(f.string(from: startOf(period))) – \(f.string(from: now))"
        }
    }
}
```

- [ ] **Run → PASS. Commit.**

### P1.4: EntryRepository (Result-based — no `try?`)

**Files:** `EntryRepository.swift`. Test: `EntryRepositoryTests.swift` (in-memory ModelContainer).

- [ ] **Write failing test** — insert round-trip + purge + sheetCount (total parity):
```swift
@MainActor
final class EntryRepositoryTests: XCTestCase {
    func makeRepo() throws -> EntryRepository {
        let config = ModelConfiguration(isStoredInMemoryOnly: true)
        let container = try ModelContainer(for: Entry.self, configurations: config)
        return EntryRepository(context: ModelContext(container))
    }
    func test_insert_and_count() throws {
        let repo = try makeRepo()
        try repo.insert(item: "Coffee", costCents: 250, type: .want).get()
        XCTAssertEqual(repo.sheetCount, 1)
    }
    func test_purge_older_than_35_days() throws {
        let repo = try makeRepo()
        let old = Calendar.current.date(byAdding: .day, value: -40, to: Date())!
        try repo.insertAt(date: old, item: "stale", costCents: 100, type: .need).get()
        repo.purgeOlderThan(days: 35)
        XCTAssertEqual(repo.sheetCount, 0)
    }
    func test_sheet_full_at_20_total() throws {
        let repo = try makeRepo()
        for i in 0..<20 { try repo.insert(item: "x\(i)", costCents: 100, type: .need).get() }
        XCTAssertTrue(repo.isSheetFull)
    }
}
```

- [ ] **Run → FAIL. Write impl:**
```swift
import Foundation
import SwiftData

enum RepositoryError: Error { case insertFailed, deleteFailed, saveFailed, notFound }

@MainActor
final class EntryRepository {
    private let context: ModelContext
    init(context: ModelContext) { self.context = context }

    func insert(item: String, costCents: Int64, type: EntryType) -> Result<Entry, RepositoryError> {
        insertAt(date: Date(), item: item, costCents: costCents, type: type)
    }

    func insertAt(date: Date, item: String, costCents: Int64, type: EntryType) -> Result<Entry, RepositoryError> {
        let e = Entry(dateUtc: date, item: item, costCents: costCents, type: type)
        context.insert(e)
        do { try context.save(); return .success(e) }
        catch { return .failure(.saveFailed) }
    }

    func delete(_ entry: Entry) -> Result<Void, RepositoryError> {
        context.delete(entry)
        do { try context.save(); return .success(()) }
        catch { return .failure(.deleteFailed) }
    }

    func deleteAll() -> Result<Void, RepositoryError> {
        let all = (try? context.fetch(FetchDescriptor<Entry>())) ?? []
        all.forEach { context.delete($0) }
        do { try context.save(); return .success(()) }
        catch { return .failure(.saveFailed) }
    }

    func purgeOlderThan(days: Int) {
        guard let cutoff = Calendar.current.date(byAdding: .day, value: -days, to: Date()) else { return }
        let pred = #Predicate<Entry> { $0.dateUtc < cutoff }
        if let stale = try? context.fetch(FetchDescriptor<Entry>(predicate: pred)) {
            stale.forEach { context.delete($0) }
            try? context.save()
        }
    }

    // Total-count parity (D4/Android). Conscious choice.
    var sheetCount: Int { (try? context.fetch(FetchDescriptor<Entry>()))?.count ?? 0 }
    var isSheetFull: Bool { sheetCount >= 20 }

    func stats(for period: Period, engine: StatsEngine) -> SummaryStats {
        let entries = (try? context.fetch(FetchDescriptor<Entry>())) ?? []
        return engine.stats(for: period, entries: entries)
    }
}
```

- [ ] **Run → PASS. Commit.** `feat(ios-native): repository + stats engine + currency (TDD)`

---

## Task P2 — Design system (adaptive brand)

### P2.1: AppColors — adaptive semantic tokens

**Files:** `Design/AppColors.swift`.

- [ ] **Write adaptive tokens** (light = D7 values; dark = tuned variants):
```swift
import SwiftUI

enum AppColors {
    static let surface       = Color("Surface")        // or adaptive:
    static let surfaceCard   = Color(.systemBackground)
    static let surfaceRaised = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark ?
            UIColor(red: 0.16, green: 0.16, blue: 0.16, alpha: 1) :
            UIColor(red: 0.953, green: 0.945, blue: 0.918, alpha: 1) // #F3F1EA
    })
    static let crimson       = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark ?
            UIColor(red: 0.85, green: 0.20, blue: 0.30, alpha: 1) :
            UIColor(red: 0.784, green: 0.063, blue: 0.180, alpha: 1) // #C8102E
    })
    static let marketGreen   = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark ?
            UIColor(red: 0.20, green: 0.70, blue: 0.45, alpha: 1) :
            UIColor(red: 0.043, green: 0.420, blue: 0.227, alpha: 1) // #0B6B3A
    })
    static let gold          = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark ?
            UIColor(red: 0.90, green: 0.72, blue: 0.30, alpha: 1) :
            UIColor(red: 0.910, green: 0.662, blue: 0.165, alpha: 1) // #E8A92A
    })
    static let divider       = Color(uiColor: UIColor.separator)
    static let textPrimary   = Color.primary
    static let textSecondary = Color.secondary
    // Semantic
    static let need  = marketGreen
    static let want  = crimson
    static let accent = crimson
}
```

### P2.2: AppTypography, Haptics

- [ ] **AppTypography** — Playfair for display, SF for body, Dynamic Type ramps:
```swift
import SwiftUI
enum AppTypography {
    static func display(_ size: CGFloat, _ weight: Font.Weight = .bold) -> Font {
        .custom("PlayfairDisplaySC", size: size).weight(weight)
    }
    static let eyebrow = Font.system(size: 11, weight: .semibold).smallCaps()
    static let body    = Font.body
    static let money   = Font.body.monospacedDigit()
}
```
- [ ] **Haptics:**
```swift
import UIKit
enum Haptics {
    static func seal()  { UIImpactFeedbackGenerator(style: .medium).impactOccurred() }
    static func warn()  { UINotificationFeedbackGenerator().notificationOccurred(.warning) }
    static func success(){ UINotificationFeedbackGenerator().notificationOccurred(.success) }
}
```
- [ ] **Commit.** `feat(ios-native): adaptive design system + haptics`

### P2.3: Shared ledger geometry (D8) + components

**Files:** `LedgerHeader.swift`, `LedgerRow.swift`, `CurrencyText.swift`, `NeedWantBadge.swift`, `DonutChart.swift`, `ShareBar.swift`, `PrimaryButton.swift`, `Eyebrow.swift`.

- [ ] **LedgerRow** — shared column widths (Time 48 / Cost 88 / Type 42 / Delete 32), `Button` delete (accessibility), `monospacedDigit` cost:
```swift
import SwiftUI
struct LedgerRow: View {
    let entry: Entry; let currency: CurrencyOption; let onDelete: () -> Void
    var body: some View {
        HStack(spacing: 0) {
            Text(entry.timeLabel).frame(width: 48, alignment: .leading).font(.system(size: 11)).foregroundStyle(AppColors.textSecondary)
            Text(entry.item).lineLimit(1).frame(maxWidth: .infinity, alignment: .leading)
            CurrencyText(cents: entry.costCents, currency: currency).frame(width: 88, alignment: .trailing)
            NeedWantBadge(type: entry.type).frame(width: 42)
            Button(action: onDelete) { Image(systemName: "trash") }
                .frame(width: 32).tint(AppColors.crimson)
                .accessibilityLabel("Delete \(entry.item)")
        }
    }
}
```
- [ ] **DonutChart** — `Canvas`, dynamic-type-sized ring, two slices (need green / want red), center total:
```swift
import SwiftUI
struct DonutChart: View {
    let stats: SummaryStats; let currency: CurrencyOption
    var body: some View {
        Canvas { ctx, size in
            let r = min(size.width, size.height) / 2
            let center = CGPoint(x: size.width/2, y: size.height/2)
            let total = CGFloat(stats.totalCents)
            guard total > 0 else { return }   // empty state handled by parent
            var start = Angle.degrees(-90)
            for (frac, color) in [(CGFloat(stats.needsTotalCents)/total, AppColors.need),
                                  (CGFloat(stats.wantsTotalCents)/total, AppColors.want)] {
                let end = start + .degrees(360 * frac)
                let path = Path { p in
                    p.addArc(center: center, radius: r, startAngle: start, endAngle: end, clockwise: false)
                    p.addArc(center: center, radius: r*0.62, startAngle: end, endAngle: start, clockwise: true)
                }
                ctx.fill(path, with: .color(color))
                start = end
            }
        }
        .accessibilityLabel("Need \(Int(stats.needPct*100)) percent")
    }
}
```
- [ ] **Commit.** `feat(ios-native): shared ledger geometry + donut + components`

---

## Task P3 — Summary screen

**Files:** `Features/Summary/SummaryView.swift`, `SummaryViewModel.swift`. Test: `SummaryViewModelTests.swift`.

- [ ] **SummaryViewModel** — `@Observable`, owns `period`, delegates stats to repo+engine:
```swift
import SwiftUI
@Observable @MainActor
final class SummaryViewModel {
    var period: Period = .day
    let repo: EntryRepository; let engine: StatsEngine
    init(repo: EntryRepository, engine: StatsEngine = StatsEngine()) { self.repo = repo; self.engine = engine }
    var stats: SummaryStats { repo.stats(for: period, engine: engine) }
    var rangeCaption: String { engine.rangeCaption(period) }
}
```
- [ ] **SummaryView** — editorial header (Playfair), `Picker(.segmented)` for period, `DonutChart`, 3 stat cards (NEEDS/WANTS/NEED%), empty state, "Log an expense" CTA → `AppModel.selectedTab = .log`. Range caption under rotor.
- [ ] **Test:** period switch changes stats window (inject repo with seeded entries).
- [ ] **Commit.** `feat(ios-native): Summary screen`

---

## Task P4 — Log screen (auto-seal state machine)

**Files:** `Features/Log/LogView.swift`, `LogViewModel.swift`. Test: `LogViewModelTests.swift`.

- [ ] **LogViewModel** — `@Observable`, `@FocusState`-friendly, auto-seal the instant item+cost+type valid:
```swift
import SwiftUI
@Observable @MainActor
final class LogViewModel {
    var item = ""; var costText = ""; var type: EntryType? = nil
    let repo: EntryRepository; let currency: CurrencyOption
    init(repo: EntryRepository, currency: CurrencyOption) { self.repo = repo; self.currency = currency }

    var costCents: Int64? {
        guard let val = Double(costText.replacingOccurrences(of: ",", with: ".")) else { return nil }
        return Int64((val * 100).rounded())
    }
    var canSeal: Bool { !item.trimmingCharacters(in: .whitespaces).isEmpty && costCents != nil && type != nil }
    var isSheetFull: Bool { repo.isSheetFull }

    func sealIfPossible() -> Bool {
        guard canSeal, !isSheetFull, let cents = costCents, let t = type else { return false }
        if case .success = repo.insert(item: item.trimmingCharacters(in: .whitespaces), costCents: cents, type: t) {
            Haptics.seal(); reset(); return true
        }
        return false
    }
    func reset() { item = ""; costText = ""; type = nil }
    func startNewSheet() { try? repo.deleteAll().get(); Haptics.success() }  // "Start new sheet" handoff
}
```
- [ ] **Test:** valid input seals + resets; invalid input does not seal; 20-cap blocks seal.
- [ ] **LogView** — header `TODAY · <date>` + `SHEET n/20`, active entry card (Item field, Cost field, Need/Want chips), auto-seal via `.onChange(of: canSeal)`, `@FocusState` on Item→Cost flow, `.toolbar { ToolbarItem(placement: .keyboard) { "Done" } }` to dismiss, sealed ledger (`@Query` sorted desc), 20/20 → "Start new sheet" prompt.
- [ ] **Commit.** `feat(ios-native): Log screen with auto-seal + 20-cap`

---

## Task P5 — History screen (sectioned @Query)

**Files:** `Features/History/HistoryView.swift`, `HistoryViewModel.swift`.

- [ ] **HistoryView** — `@Query(sort: \Entry.dateUtc, order: .reverse)`; group by `dayKey` in `Dictionary`; `List` with `Section` per day (date header, n entries, day totals); `LedgerRow` rows; `.swipeActions` delete with confirm alert; empty state "The page waits for ink."; CTA → Log.
- [ ] **Commit.** `feat(ios-native): History screen`

---

## Task P6 — Settings + Onboarding

**Files:** `Settings/{SettingsView, SettingsViewModel}.swift`, `Onboarding/OnboardingView.swift`.

- [ ] **SettingsView** — native `Form`: currency `Picker`, "Wipe diary" with confirm alert (`UINotificationFeedbackGenerator(.warning)`), About row (`v1.0.0`), "How it works" → onboarding. Currency via `@AppStorage("currency")`.
- [ ] **OnboardingView** — `TabView { ... }.tabViewStyle(.page(indexDisplayMode: .always))`, 3 swipeable cards, `.fullScreenCover` on first launch (`@AppStorage("hasOnboarded")`).
- [ ] **Commit.** `feat(ios-native): Settings + Onboarding`

---

## Task P7 — App shell + native navigation

**Files:** `App/NeedsVsWantsApp.swift`, `AppModel.swift`.

- [ ] **AppModel** — `@Observable` coordinator:
```swift
import SwiftUI
@Observable @MainActor
final class AppModel {
    enum Tab: Hashable, CaseIterable { case summary, log, history, settings }
    var selectedTab: Tab = .summary
    var showOnboarding = false
}
```
- [ ] **NeedsVsWantsApp** — `@main`, `ModelContainer`, throttled purge (once per launch via `@State` flag), native `TabView`:
```swift
import SwiftUI
import SwiftData

@main
struct NeedsVsWantsApp: App {
    @State private var appModel = AppModel()
    @AppStorage("hasOnboarded") private var hasOnboarded = false
    let container: ModelContainer
    @State private var didPurge = false

    init() {
        do { container = try ModelContainer(for: Entry.self) }
        catch { fatalError("ModelContainer: \(error)") }
    }

    var body: some Scene {
        WindowGroup {
            TabView(selection: $appModel.selectedTab) {
                SummaryView().tag(AppModel.Tab.summary).tabItem { Label("Summary", systemImage: "house") }
                LogView().tag(AppModel.Tab.log).tabItem { Label("Log", systemImage: "cart") }
                HistoryView().tag(AppModel.Tab.history).tabItem { Label("History", systemImage: "clock") }
                SettingsView().tag(AppModel.Tab.settings).tabItem { Label("Settings", systemImage: "gearshape") }
            }
            .fullScreenCover(isPresented: $appModel.showOnboarding) { OnboardingView() }
            .onAppear {
                if !didPurge { EntryRepository(context: container.mainContext).purgeOlderThan(days: 35); didPurge = true }
                if !hasOnboarded { appModel.showOnboarding = true }
            }
        }
        .modelContainer(container)
    }
}
```
- [ ] **Commit.** `feat(ios-native): app shell + native TabView + throttled purge`

---

## Task P8 — Polish + CI verification

- [ ] **Accessibility:** `accessibilityLabel`/`hint` on DonutChart, LedgerRow, NeedWantBadge, PrimaryButton; verify `TabView` exposes tab semantics to VoiceOver.
- [ ] **Keyboard:** `.toolbar { ToolbarItem(placement: .keyboard) }` Done button; `@FocusState` Item→Cost→chips.
- [ ] **QA matrix (CI + manual):** iPhone SE / 15 Pro Max; light+dark; Dynamic Type XL; 20-cap; 35-day purge; currency switch (incl. JPY); relaunch persistence; first-launch onboarding.
- [ ] **Push → trigger CI → fix compile errors from logs → re-run.**
- [ ] **BUILD.md** + root `README.md` update.
- [ ] **Final commit.** `feat(ios-native): polish + CI green`

---

## CI strategy (compile target = GitHub Actions)

- macOS-14 runner, Xcode 15+, `xcodegen generate` → `xcodebuild build test` on iPhone 15 simulator.
- Public repo → free unlimited macOS minutes (no billing).
- Iterate via CI logs since no local Mac. Push small commits; read failing build log; fix; repeat.

## Risks & mitigations
| Risk | Mitigation |
|---|---|
| No local compile (Windows host) | CI is compile target; commit small, read logs |
| SwiftData `@Query` sectioning edge cases | Fall back to manual fetch + Dictionary grouping (P5) |
| Bundled font name mismatch | Verify PostScript name `PlayfairDisplaySC` in CI log; adjust `AppTypography` |
| `@Observable` + `@Query` interaction | ViewModels hold repo ref; views own `@Query` for lists |

## Self-review
- **Spec coverage:** Summary/Log/History/Settings/Onboarding ✓; 35-day purge ✓ (throttled); 20-cap total ✓; currency JPY ✓; adaptive palette ✓; shared ledger geometry ✓ (D8); haptics ✓; native nav ✓; tests ✓ (P1/P3/P4).
- **Type consistency:** `EntryRepository`/`StatsEngine`/`CurrencyFormatter` signatures match across P1→P7. `AppModel.Tab` tags match `TabView`.
- **Placeholders:** none in critical paths; view bodies specify contract + key code.
