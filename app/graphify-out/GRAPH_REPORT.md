# Graph Report - C:\Needs vs Wants\app  (2026-08-01)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 164 nodes · 231 edges · 14 communities (13 shown, 1 thin omitted)
- Extraction: 85% EXTRACTED · 15% INFERRED · 0% AMBIGUOUS · INFERRED: 35 edges (avg confidence: 0.8)
- Token cost: 585 input · 35 output

## Graph Freshness
- Built from commit: `81b2279a`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- UI Screens and Components
- Entry Data Access
- Input Logic and Formatting
- SummaryViewModel
- AppPreferences
- BudgetStatus
- SettingsViewModel
- AppNavigation
- AppDatabase
- DailyBudgetTest

## God Nodes (most connected - your core abstractions)
1. `EntryDao` - 13 edges
2. `InputViewModel` - 13 edges
3. `SummaryScreen()` - 13 edges
4. `Entry` - 11 edges
5. `Eyebrow()` - 11 edges
6. `InputScreen()` - 10 edges
7. `GiltRule()` - 10 edges
8. `AppPreferences` - 9 edges
9. `HistoryScreen()` - 9 edges
10. `SettingsViewModel` - 9 edges

## Surprising Connections (you probably didn't know these)
- `AppNavigation()` --calls--> `HistoryScreen()`  [INFERRED]
  src/main/java/com/needsvswants/app/ui/navigation/AppNavigation.kt → src/main/java/com/needsvswants/app/ui/screens/history/HistoryScreen.kt
- `AppNavigation()` --calls--> `InputScreen()`  [INFERRED]
  src/main/java/com/needsvswants/app/ui/navigation/AppNavigation.kt → src/main/java/com/needsvswants/app/ui/screens/input/InputScreen.kt
- `AppNavigation()` --calls--> `SettingsScreen()`  [INFERRED]
  src/main/java/com/needsvswants/app/ui/navigation/AppNavigation.kt → src/main/java/com/needsvswants/app/ui/screens/settings/SettingsScreen.kt
- `AppNavigation()` --calls--> `SummaryScreen()`  [INFERRED]
  src/main/java/com/needsvswants/app/ui/navigation/AppNavigation.kt → src/main/java/com/needsvswants/app/ui/screens/summary/SummaryScreen.kt
- `GoldUnderline()` --calls--> `GiltRule()`  [INFERRED]
  src/main/java/com/needsvswants/app/ui/screens/input/InputScreen.kt → src/main/java/com/needsvswants/app/ui/theme/Components.kt

## Import Cycles
- None detected.

## Communities (14 total, 1 thin omitted)

### Community 0 - "UI Screens and Components"
Cohesion: 0.10
Nodes (33): Dp, Shape, DayTotal(), HistoryScreen(), Color, GoldUnderline(), InputScreen(), Color (+25 more)

### Community 1 - "Entry Data Access"
Cohesion: 0.10
Nodes (9): Application, EntryDao, Flow, Entry, DomainModule, NeedsVsWantsApp, HistoryViewModel, StateFlow (+1 more)

### Community 2 - "Input Logic and Formatting"
Cohesion: 0.12
Nodes (7): EntryType, NEED, WANT, parseCents(), InputViewModel, StateFlow, ViewModel

### Community 3 - "SummaryViewModel"
Cohesion: 0.14
Nodes (10): Flow, Period, ALL, DAY, WEEK, SummaryStats, SummaryUseCase, StateFlow (+2 more)

### Community 4 - "AppPreferences"
Cohesion: 0.17
Nodes (4): AppPreferences, Flow, Context, PreferencesModule

### Community 5 - "BudgetStatus"
Cohesion: 0.21
Nodes (6): BudgetStatus, DailyBudgetMath, Off, On, DailyBudgetUseCase, Flow

### Community 6 - "SettingsViewModel"
Cohesion: 0.18
Nodes (6): SectionLabel(), SettingsScreen(), CurrencyOption, StateFlow, ViewModel, SettingsViewModel

### Community 7 - "AppNavigation"
Cohesion: 0.22
Nodes (7): Bundle, ComponentActivity, MainActivity, AppNavigation(), BottomNavItem, NavPill(), NeedsVsWantsTheme()

### Community 8 - "AppDatabase"
Cohesion: 0.25
Nodes (4): RoomDatabase, AppDatabase, DatabaseModule, Context

## Knowledge Gaps
- **8 isolated node(s):** `NEED`, `WANT`, `Off`, `DAY`, `WEEK` (+3 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **1 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `EntryDao` connect `Entry Data Access` to `AppDatabase`?**
  _High betweenness centrality (0.298) - this node is a cross-community bridge._
- **Why does `Entry` connect `Entry Data Access` to `UI Screens and Components`, `Input Logic and Formatting`?**
  _High betweenness centrality (0.297) - this node is a cross-community bridge._
- **Why does `InputViewModel` connect `Input Logic and Formatting` to `UI Screens and Components`, `Entry Data Access`, `BudgetStatus`?**
  _High betweenness centrality (0.213) - this node is a cross-community bridge._
- **Are the 6 inferred relationships involving `SummaryScreen()` (e.g. with `AppNavigation()` and `adaptiveMoneySize()`) actually correct?**
  _`SummaryScreen()` has 6 INFERRED edges - model-reasoned connections that need verification._
- **Are the 7 inferred relationships involving `Eyebrow()` (e.g. with `HistoryScreen()` and `InputScreen()`) actually correct?**
  _`Eyebrow()` has 7 INFERRED edges - model-reasoned connections that need verification._
- **What connects `NEED`, `WANT`, `Off` to the rest of the system?**
  _8 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `UI Screens and Components` be split into smaller, more focused modules?**
  _Cohesion score 0.1039136302294197 - nodes in this community are weakly interconnected._