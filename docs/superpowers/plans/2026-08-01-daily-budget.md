# Custom Daily Budget Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add an optional, user-editable fixed daily budget on Android: Settings to set/clear it, Summary Day meter for spent vs limit, and a Log confirm dialog when a seal would exceed today’s limit.

**Architecture:** Store `daily_budget_cents` in DataStore (`AppPreferences`). `DailyBudgetUseCase` combines that preference with today’s sealed Need+Want spend into `BudgetStatus`. Summary shows a Day-only meter; Input gates `trySeal` with a soft confirm. Pure math helpers are unit-tested first (TDD).

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, Hilt, Room (entries only), DataStore Preferences, JUnit + coroutines-test.

**Spec:** [docs/superpowers/specs/2026-08-01-daily-budget-design.md](../specs/2026-08-01-daily-budget-design.md)

**Pre-code gate (AGENTS.md):** Before first code edit, load Obsidian `Projects/Needs vs Wants/{Summary,Tasks,Decisions}`, Context7 N/A for this feature (no new library), Graphify scoped to `app/` if available.

---

## File map

| File | Responsibility |
|------|----------------|
| Create `app/src/main/java/.../domain/DailyBudget.kt` | `BudgetStatus` + pure `status` / `wouldExceed` |
| Create `app/src/main/java/.../domain/DailyBudgetUseCase.kt` | Observe today’s spend + preference → `BudgetStatus` |
| Create `app/src/test/java/.../domain/DailyBudgetTest.kt` | Unit tests for pure helpers |
| Modify `.../data/prefs/AppPreferences.kt` | Budget key + get/set/clear |
| Modify `.../di/DomainModule.kt` | Provide `DailyBudgetUseCase` |
| Modify `.../settings/SettingsViewModel.kt` | Expose budget state + save/clear |
| Modify `.../settings/SettingsScreen.kt` | Daily budget UI section |
| Modify `.../summary/SummaryViewModel.kt` | Expose `budgetStatus` |
| Modify `.../summary/SummaryScreen.kt` | Day meter when On |
| Modify `.../input/InputViewModel.kt` | Overspend confirm gate |
| Modify `.../input/InputScreen.kt` | Overspend `AlertDialog` |
| Update Obsidian `Tasks.md` / `Decisions.md` | Record D26+ and progress |

---

### Task 1: Pure domain helpers + failing tests (TDD)

**Files:**
- Create: `app/src/test/java/com/needsvswants/app/domain/DailyBudgetTest.kt`
- Create: `app/src/main/java/com/needsvswants/app/domain/DailyBudget.kt`

- [ ] **Step 1: Write the failing test**

```kotlin
package com.needsvswants.app.domain

import org.junit.Assert.*
import org.junit.Test

class DailyBudgetTest {

    @Test
    fun status_nullOrNonPositive_isOff() {
        assertEquals(BudgetStatus.Off, DailyBudgetMath.status(null, 1_000))
        assertEquals(BudgetStatus.Off, DailyBudgetMath.status(0L, 1_000))
        assertEquals(BudgetStatus.Off, DailyBudgetMath.status(-100L, 0))
    }

    @Test
    fun status_on_computesRemainingAndProgress() {
        val s = DailyBudgetMath.status(500_000L, 420_000L) as BudgetStatus.On
        assertEquals(500_000L, s.budgetCents)
        assertEquals(420_000L, s.spentCents)
        assertEquals(80_000L, s.remainingCents)
        assertEquals(0.84f, s.progress, 0.001f)
    }

    @Test
    fun status_over_allowsNegativeRemaining() {
        val s = DailyBudgetMath.status(100_000L, 150_000L) as BudgetStatus.On
        assertEquals(-50_000L, s.remainingCents)
        assertTrue(s.progress > 1f)
    }

    @Test
    fun wouldExceed_onlyWhenStrictlyOver() {
        assertFalse(DailyBudgetMath.wouldExceed(400_000, 500_000, 100_000)) // exact
        assertTrue(DailyBudgetMath.wouldExceed(400_000, 500_000, 100_001))
        assertTrue(DailyBudgetMath.wouldExceed(500_000, 500_000, 1))
        assertFalse(DailyBudgetMath.wouldExceed(0, 500_000, 500_000))
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run (from repo root):

```bat
gradlew.bat :app:testDebugUnitTest --tests com.needsvswants.app.domain.DailyBudgetTest
```

Expected: FAIL (classes/symbols not found).

- [ ] **Step 3: Write minimal implementation**

Create `app/src/main/java/com/needsvswants/app/domain/DailyBudget.kt`:

```kotlin
package com.needsvswants.app.domain

sealed class BudgetStatus {
    data object Off : BudgetStatus()
    data class On(
        val budgetCents: Long,
        val spentCents: Long,
        val remainingCents: Long,
        val progress: Float
    ) : BudgetStatus()
}

object DailyBudgetMath {
    fun status(budgetCents: Long?, spentCents: Long): BudgetStatus {
        if (budgetCents == null || budgetCents <= 0L) return BudgetStatus.Off
        val remaining = budgetCents - spentCents
        val progress = spentCents.toFloat() / budgetCents.toFloat()
        return BudgetStatus.On(
            budgetCents = budgetCents,
            spentCents = spentCents,
            remainingCents = remaining,
            progress = progress
        )
    }

    fun wouldExceed(spentCents: Long, budgetCents: Long, newCostCents: Long): Boolean =
        spentCents + newCostCents > budgetCents
}
```

- [ ] **Step 4: Run tests and verify they pass**

```bat
gradlew.bat :app:testDebugUnitTest --tests com.needsvswants.app.domain.DailyBudgetTest
```

Expected: PASS (4 tests).

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/needsvswants/app/domain/DailyBudget.kt app/src/test/java/com/needsvswants/app/domain/DailyBudgetTest.kt
git commit -m "$(cat <<'EOF'
test: add DailyBudgetMath helpers and unit tests

EOF
)"
```

---

### Task 2: AppPreferences daily budget key

**Files:**
- Modify: `app/src/main/java/com/needsvswants/app/data/prefs/AppPreferences.kt`

- [ ] **Step 1: Add preference key and API**

In `companion object` add:

```kotlin
private val DAILY_BUDGET_CENTS = longPreferencesKey("daily_budget_cents")
```

Add:

```kotlin
/** null means budget is off (missing or ≤ 0). */
val dailyBudgetCents: Flow<Long?> = context.dataStore.data.map { prefs ->
    val v = prefs[DAILY_BUDGET_CENTS] ?: return@map null
    if (v <= 0L) null else v
}

suspend fun setDailyBudgetCents(cents: Long) {
    require(cents > 0L) { "daily budget must be positive cents" }
    context.dataStore.edit { it[DAILY_BUDGET_CENTS] = cents }
}

suspend fun clearDailyBudget() {
    context.dataStore.edit { it.remove(DAILY_BUDGET_CENTS) }
}
```

Keep existing `wipeAll()` — it clears all keys including budget.

- [ ] **Step 2: Commit**

```bash
git add app/src/main/java/com/needsvswants/app/data/prefs/AppPreferences.kt
git commit -m "$(cat <<'EOF'
feat: store optional daily_budget_cents in DataStore

EOF
)"
```

---

### Task 3: DailyBudgetUseCase + Hilt

**Files:**
- Create: `app/src/main/java/com/needsvswants/app/domain/DailyBudgetUseCase.kt`
- Modify: `app/src/main/java/com/needsvswants/app/di/DomainModule.kt`

- [ ] **Step 1: Implement use case**

```kotlin
package com.needsvswants.app.domain

import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.prefs.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar

class DailyBudgetUseCase(
    private val dao: EntryDao,
    private val preferences: AppPreferences
) {
    fun observeStatus(): Flow<BudgetStatus> {
        val since = startOfToday()
        return combine(
            preferences.dailyBudgetCents,
            dao.observeSince(since)
        ) { budgetCents, entries ->
            val spent = entries.sumOf { it.costCents }
            DailyBudgetMath.status(budgetCents, spent)
        }
    }

    private fun startOfToday(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
```

Note: `startOfToday()` is evaluated once per `observeStatus()` subscription. That matches `SummaryUseCase.getStats(Period.DAY)` (also computes `since` once per call). Acceptable for v1; do not invent a ticker.

- [ ] **Step 2: Register in DomainModule**

```kotlin
@Provides
@Singleton
fun provideDailyBudgetUseCase(
    dao: EntryDao,
    preferences: AppPreferences
): DailyBudgetUseCase = DailyBudgetUseCase(dao, preferences)
```

Add imports for `AppPreferences` and `DailyBudgetUseCase`.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/needsvswants/app/domain/DailyBudgetUseCase.kt app/src/main/java/com/needsvswants/app/di/DomainModule.kt
git commit -m "$(cat <<'EOF'
feat: add DailyBudgetUseCase wired through Hilt

EOF
)"
```

---

### Task 4: Settings — ViewModel + UI

**Files:**
- Modify: `app/src/main/java/com/needsvswants/app/ui/screens/settings/SettingsViewModel.kt`
- Modify: `app/src/main/java/com/needsvswants/app/ui/screens/settings/SettingsScreen.kt`

- [ ] **Step 1: Extend SettingsViewModel**

Inject nothing new beyond existing `preferences`. Add:

```kotlin
val dailyBudgetCents: StateFlow<Long?> = preferences.dailyBudgetCents
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

fun saveDailyBudget(rawAmount: String): Boolean {
    val cents = parseCents(rawAmount) ?: return false
    if (cents <= 0L) return false
    viewModelScope.launch { preferences.setDailyBudgetCents(cents) }
    return true
}

fun clearDailyBudget() {
    viewModelScope.launch { preferences.clearDailyBudget() }
}
```

Import `com.needsvswants.app.domain.parseCents`.

- [ ] **Step 2: Add Settings UI section**

Between Currency block and `SectionLabel("DATA")`, insert a **DAILY BUDGET** section:

- `SectionLabel("DAILY BUDGET")`
- Surface card with:
  - Helper text: “Optional. Off until you set an amount.”
  - When `dailyBudgetCents != null`: show `dailyBudgetCents!!.toMoney(currentSymbol)` and a “Turn off” text button calling `viewModel.clearDailyBudget()`
  - `OutlinedTextField` (or project-styled field) for amount; filter digits/decimal like Input (`filter` inline or local)
  - “Save” button: call `saveDailyBudget`; on false, set local `error` string (“Enter a valid amount”)
- Reuse `InkElevated`, `BorderStroke`, `Gilt`/`Crimson` patterns from Currency card
- Import `toMoney`

- [ ] **Step 3: Manual smoke (optional on device)**

Set 5000, confirm it shows formatted; Turn off; Wipe still resets.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/needsvswants/app/ui/screens/settings/SettingsViewModel.kt app/src/main/java/com/needsvswants/app/ui/screens/settings/SettingsScreen.kt
git commit -m "$(cat <<'EOF'
feat: Settings UI to set and clear daily budget

EOF
)"
```

---

### Task 5: Summary Day meter

**Files:**
- Modify: `app/src/main/java/com/needsvswants/app/ui/screens/summary/SummaryViewModel.kt`
- Modify: `app/src/main/java/com/needsvswants/app/ui/screens/summary/SummaryScreen.kt`

- [ ] **Step 1: Expose budget status on SummaryViewModel**

```kotlin
@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val summaryUseCase: SummaryUseCase,
    private val dailyBudgetUseCase: DailyBudgetUseCase,
    private val preferences: AppPreferences
) : ViewModel() {
    // existing period/stats/currency/firstLaunch ...

    val budgetStatus: StateFlow<BudgetStatus> = dailyBudgetUseCase.observeStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetStatus.Off)
}
```

- [ ] **Step 2: Render meter on SummaryScreen**

Collect `budgetStatus`. After the period selector `Spacer(Modifier.height(28.dp))` (before the donut), when `period == Period.DAY && budgetStatus is BudgetStatus.On`:

```kotlin
val on = budgetStatus as BudgetStatus.On
val over = on.remainingCents < 0
Surface(
    shape = RoundedCornerShape(16.dp),
    color = SurfaceCard,
    border = BorderStroke(1.dp, if (over) Crimson.copy(alpha = 0.45f) else Divider),
    modifier = Modifier.fillMaxWidth()
) {
    Column(Modifier = Modifier.padding(16.dp)) {
        Eyebrow("DAILY BUDGET", color = if (over) Crimson else Gilt)
        Spacer(Modifier.height(8.dp))
        Text(
            "${on.spentCents.toMoney(symbol)} / ${on.budgetCents.toMoney(symbol)}",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { on.progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = if (over) Crimson else MarketGreen,
            trackColor = SurfaceRaised
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (over) "Over by ${(-on.remainingCents).toMoney(symbol)}"
            else "Remaining ${on.remainingCents.toMoney(symbol)}",
            style = MaterialTheme.typography.bodyMedium,
            color = if (over) Crimson else TextSecondary
        )
    }
}
Spacer(Modifier.height(20.dp))
```

Adjust token names (`Raised`, `MarketGreen`, `Divider`, `SurfaceCard`) to match `ui/theme/Color.kt` aliases actually used in SummaryScreen — prefer existing imports over inventing new colors.

Hide entirely for Week/All and `BudgetStatus.Off`.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/needsvswants/app/ui/screens/summary/SummaryViewModel.kt app/src/main/java/com/needsvswants/app/ui/screens/summary/SummaryScreen.kt
git commit -m "$(cat <<'EOF'
feat: show daily budget meter on Summary Day

EOF
)"
```

---

### Task 6: Log overspend confirm

**Files:**
- Modify: `app/src/main/java/com/needsvswants/app/ui/screens/input/InputViewModel.kt`
- Modify: `app/src/main/java/com/needsvswants/app/ui/screens/input/InputScreen.kt`

- [ ] **Step 1: Gate trySeal in InputViewModel**

```kotlin
@HiltViewModel
class InputViewModel @Inject constructor(
    private val dao: EntryDao,
    private val preferences: AppPreferences,
    private val dailyBudgetUseCase: DailyBudgetUseCase
) : ViewModel() {
    // existing sheetEntries, currencySymbol, active fields...

    val budgetStatus: StateFlow<BudgetStatus> = dailyBudgetUseCase.observeStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetStatus.Off)

    private val _overspendConfirm = MutableStateFlow<Long?>(null)
    /** Non-null = pending new cost cents awaiting user confirm. */
    val overspendConfirmCostCents: StateFlow<Long?> = _overspendConfirm.asStateFlow()

    fun trySeal() {
        if (isSealing) return
        if (_overspendConfirm.value != null) return
        val item = activeItem.value.trim()
        val costCents = parseCents(activeCost.value)
        val type = activeType.value
        if (item.isEmpty() || costCents == null || type == null || isSheetFull) return

        val status = budgetStatus.value
        if (status is BudgetStatus.On &&
            DailyBudgetMath.wouldExceed(status.spentCents, status.budgetCents, costCents)
        ) {
            _overspendConfirm.value = costCents
            return
        }
        sealNow(item, costCents, type)
    }

    fun confirmOverspendSeal() {
        val costCents = _overspendConfirm.value ?: return
        val item = activeItem.value.trim()
        val type = activeType.value
        _overspendConfirm.value = null
        if (item.isEmpty() || type == null) return
        sealNow(item, costCents, type)
    }

    fun dismissOverspendConfirm() {
        _overspendConfirm.value = null
        // Keep draft item/cost/type so user can edit.
    }

    private fun sealNow(item: String, costCents: Long, type: EntryType) {
        if (isSealing) return
        isSealing = true
        val now = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        viewModelScope.launch {
            dao.insert(
                Entry(
                    dateUtc = now,
                    date = dateFormat.format(Date(now)),
                    time = timeFormat.format(Date(now)),
                    item = item,
                    costCents = costCents,
                    type = type
                )
            )
            activeItem.value = ""
            activeCost.value = ""
            activeType.value = null
            isSealing = false
        }
    }
}
```

Import `BudgetStatus`, `DailyBudgetMath`, `DailyBudgetUseCase`.

- [ ] **Step 2: AlertDialog on InputScreen**

Collect `overspendConfirmCostCents` and `budgetStatus`. When confirm cost non-null and status is On:

```kotlin
val pendingCost by viewModel.overspendConfirmCostCents.collectAsStateWithLifecycle()
val budgetStatus by viewModel.budgetStatus.collectAsStateWithLifecycle()

pendingCost?.let { _ ->
    val on = budgetStatus as? BudgetStatus.On
    if (on != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissOverspendConfirm() },
            containerColor = InkElevated,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(20.dp),
            title = {
                Column {
                    Eyebrow("DAILY BUDGET", color = Danger)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Over budget?",
                        color = TextPrimary,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            },
            text = {
                Text(
                    "This puts you over your daily budget of ${on.budgetCents.toMoney(symbol)}. Log anyway?",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmOverspendSeal() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Crimson.copy(alpha = 0.18f),
                        contentColor = Crimson
                    ),
                    border = BorderStroke(1.dp, Crimson)
                ) { Text("Log anyway", fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissOverspendConfirm() }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}
```

Place beside the existing delete `AlertDialog` block. Match token names already imported in InputScreen.

- [ ] **Step 3: Manual QA checklist**

1. No budget → seal works, no dialog.
2. Budget ₱100; spend ₱80; seal ₱20 → no dialog.
3. Seal ₱21 → dialog; Cancel keeps draft; Log anyway inserts; Summary meter updates.
4. Already over → every new seal shows dialog.
5. Sheet at 20 → sheet-full still blocks (no budget dialog if full).

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/needsvswants/app/ui/screens/input/InputViewModel.kt app/src/main/java/com/needsvswants/app/ui/screens/input/InputScreen.kt
git commit -m "$(cat <<'EOF'
feat: confirm before sealing over daily budget

EOF
)"
```

---

### Task 7: Docs / Second Brain

**Files:**
- Modify: `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\Tasks.md`
- Modify: `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\Decisions.md`
- Modify: `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\Summary.md` (brief mention under Key Surfaces / Settings)
- Modify: `.gitignore` — add `.superpowers/`

- [ ] **Step 1: Append decision**

```markdown
## D27 — Optional custom fixed daily budget (Android)
**Context:** Users want awareness of a personal daily spending limit (e.g. ₱5,000), inspired by Pledgr’s “aware of spend” feel but not its weekly-derived STS engine.
**Decision:** Optional DataStore `daily_budget_cents`; `DailyBudgetUseCase`; Summary Day meter; Log confirm when seal would exceed (allow override). All Need+Want count. Android-only v1.
**Spec:** `docs/superpowers/specs/2026-08-01-daily-budget-design.md`
**Agent:** Cursor
**Date:** 2026-08-01
```

(Use next free D-number if D27 taken.)

- [ ] **Step 2: Tasks progress**

Add checkbox items under a new “Daily budget” section and mark done when shipped.

- [ ] **Step 3: gitignore**

```
.superpowers/
```

- [ ] **Step 4: Commit docs**

```bash
git add docs/superpowers/specs/2026-08-01-daily-budget-design.md docs/superpowers/plans/2026-08-01-daily-budget.md .gitignore
git commit -m "$(cat <<'EOF'
docs: daily budget design spec and implementation plan

EOF
)"
```

(Obsidian vault may live outside this git repo — update vault files on disk; only commit repo docs here.)

---

## Spec coverage checklist

| Spec requirement | Task |
|------------------|------|
| Optional DataStore budget | 2 |
| Pure status / wouldExceed | 1 |
| DailyBudgetUseCase observe today | 3 |
| Settings set/clear | 4 |
| Summary Day meter | 5 |
| Log confirm overspend | 6 |
| Exact limit no confirm | 1 + 6 |
| Wipe clears budget | 2 (existing wipeAll) |
| Android unit tests | 1 |
| Obsidian / gitignore | 7 |
| No iOS / hard block / 80% banner | Explicit non-goals — no tasks |

---

## Execution handoff

Plan complete and saved to `docs/superpowers/plans/2026-08-01-daily-budget.md`. Two execution options:

**1. Subagent-Driven (recommended)** — fresh subagent per task, review between tasks  

**2. Inline Execution** — execute tasks in this session with executing-plans checkpoints  

Which approach?
