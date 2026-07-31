# Custom Daily Budget — Design Spec

**Date:** 2026-08-01  
**Status:** Approved (brainstorm)  
**Platform (v1):** Android `app/` only  
**Agent:** Cursor  

## Problem

Users want an optional, customizable daily spending limit (e.g. ₱5,000). They should see today’s spend against that limit and get a confirmation when a new Log entry would push them over. This is **not** Pledgr’s weekly-pledge “safe to spend today” engine — it is a fixed daily amount the user sets and can change anytime.

## Goals

- Let users set, edit, and clear a daily budget in Settings.
- When enabled, show spent / limit / remaining on Summary for the **Day** period.
- When a seal would exceed today’s budget, show a confirm dialog; user may still log.
- Count **all** sealed expenses (Need + Want).
- Stay offline-first; store amounts in cents (D2).

## Non-goals (v1)

- iOS / Expo / website
- Weekly pledge or derived daily allowance (Pledgr-style)
- Hard-blocking seals
- Early ~80% banner/toast
- Wants-only budget mode
- Per-day historical budget rows in Room
- Push notifications

## Approach

**Small `DailyBudgetUseCase` + DataStore preference** (Approach 2).

- Preference holds the current limit (or off).
- Domain layer computes `BudgetStatus` from limit + today’s sealed spend.
- Summary and Input consume that status; Settings writes the preference.

## Data

### Preference

| Key | Type | Semantics |
|-----|------|-----------|
| `daily_budget_cents` | `Long` in DataStore | Missing or `≤ 0` ⇒ budget **off**. Positive ⇒ on with that limit. |

- File: `app/src/main/java/com/needsvswants/app/data/prefs/AppPreferences.kt`
- API: `dailyBudgetCents: Flow<Long?>` (map missing/`≤0` → `null`), `setDailyBudgetCents(cents: Long)`, `clearDailyBudget()`.
- `wipeAll()` already clears DataStore → budget clears with wipe (no extra wipe logic).

### No Room changes

Budget is a preference, not a ledger entity. Today’s spend comes from existing `Entry` rows via the Day window (`dateUtc >= startOfToday()`), same as `SummaryUseCase` + `Period.DAY`.

## Domain

### Types

```kotlin
sealed class BudgetStatus {
    data object Off : BudgetStatus()
    data class On(
        val budgetCents: Long,
        val spentCents: Long,
        val remainingCents: Long, // may be negative when over
        val progress: Float       // spent / budget; may be > 1f
    ) : BudgetStatus()
}
```

### `DailyBudgetUseCase`

- Depends on `EntryDao` + `AppPreferences`.
- `observeStatus(): Flow<BudgetStatus>` — combine budget preference with sum of `costCents` for entries since local midnight (Need + Want).
- Pure helpers (testable without Android):
  - `status(budgetCents: Long?, spentCents: Long): BudgetStatus`
  - `wouldExceed(spentCents: Long, budgetCents: Long, newCostCents: Long): Boolean`  
    ⇒ `spentCents + newCostCents > budgetCents` (exact equality does **not** confirm).

Provide via `DomainModule` like `SummaryUseCase`.

## UI

### Settings

- New section **Daily budget** (between Currency and Data).
- Copy: optional; off until an amount is set.
- Amount field using existing cost filtering / `parseCents`.
- Actions: Save (persist cents), Turn off / clear when currently on.
- Show current formatted limit when on (`toMoney(symbol)`).

### Summary

- When `BudgetStatus.On` **and** `period == Day`: meter card under period rotor (or above donut) — spent / budget, remaining (or “Over by ₱X”), progress bar.
- Hidden when Off, or when period is Week / All.
- Overspend styling uses existing crimson / Danger tokens.

### Log (Input)

1. Existing validation (item, cost, type, sheet not full) runs first.
2. If budget Off → seal as today.
3. If On and `wouldExceed` → do **not** insert; emit pending confirm state with draft values.
4. Dialog: “This puts you over your daily budget of {formatted}. Log anyway?” — Cancel / Log anyway.
5. Cancel → dismiss dialog; keep draft fields so user can edit cost.
6. Log anyway → insert, clear draft, clear pending confirm.
7. Sheet-full block still takes precedence over budget confirm.

Reuse existing `AlertDialog` styling from delete-confirm on Input.

## Edge cases

| Case | Behavior |
|------|----------|
| Budget off | No meter, no confirm |
| Exact at limit | No confirm; seal OK |
| Already over | Meter shows over; further seals still confirm |
| Mid-day raise/lower | New limit applies immediately |
| Turn off mid-day | Meter/confirm gone; entries unchanged |
| Midnight | Spent resets via Day window; same limit until cleared |
| Currency switch | Cents unchanged; display reformats (D2) |
| Wipe | Entries + prefs including budget |
| Invalid Settings amount | Do not save |

## Testing

- Unit tests for pure `status` / `wouldExceed` (and optionally use-case mapping) under `app/src/test/`.
- No instrumentation UI tests required for v1.
- Test deps already declared in `app/build.gradle.kts`.

## Decision log (product)

| Decision | Choice |
|----------|--------|
| Model | Fixed daily amount (not Pledgr-derived) |
| Editable | Yes, anytime |
| Overspend | Confirm, then allow |
| Scope | Android first |
| What counts | Need + Want |
| Default | Optional / off until set |
| Awareness | Summary Day meter + confirm (no 80% banner) |

## Related

- Project: [[Summary]] / [[Decisions]] / [[Tasks]] (Obsidian)
- Contrast: Pledgr `calculateSafeToSpend` is weekly-derived; do not port that engine here.
