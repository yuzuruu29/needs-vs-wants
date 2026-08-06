# Phase 1 Plan — Daily free log quota + rewarded ads (offline slice)

**Status:** Approved. Ready for implementation.  
**Scope:** Android native APK only (`app/`)  
**Date:** 2026-08-07  
**Source plan:** `docs/PLAN_DAILY_QUOTA_REWARDED_ADS.md`

---

## Locked product decisions (from brief)

- Free daily logs: **10**
- Extra logs per successful reward: **+8**
- Max rewarded ads per day: **3**
- Soft Pro CTA (ghost line) in the quota dialog: **Yes**
- Stack with existing 20-entry sheet limit: **Yes**
- Scope: **Android only**
- Use Google test AdMob IDs until a real AdMob app exists

## AdMob + UMP policy requirements (non-negotiable — Phase 3)

- Reward is non-monetary and usable only inside the app
- Clear, conspicuous disclosure before every ad using exact copy (see B3 below)
- Explicit user opt-in only (button tap)
- Grant extra logs ONLY on the official `onUserEarnedReward` callback
- Never encourage users to click the ad
- UMP consent triggered only on the first "Watch ad" action (never on cold start)
- Force test ad unit ID in all debug builds: `ca-app-pub-3940256099942544/5224354917`
- No AdMob initialization or network calls inside the seal path
- Max 3 rewarded ads per day enforced

---

## Pre-code gate (verified)

- [x] **Obsidian:** `Summary.md`, `Tasks.md`, `Decisions.md` loaded — latest decision = **D39**; next free = **D40** (for post-implementation entry). No prior quota/ads decisions exist — greenfield.
- [x] **Context7:** N/A for Phase 1 — no external library surface. AdMob/UMP docs pulled fresh in Phase 3.
- [x] **Graphify:** cached repo graph at `.graphify/`. Blast radius for Phase 1 verified by direct reads: `domain/` (new), `data/prefs/AppPreferences.kt` (additive), `ui/screens/input/InputViewModel.kt` + `InputScreen.kt` (one new dialog branch). No Entry/Room/35-day-retention surface touched.
- [x] Verified: no `ads/` package and no `DailyLogQuota*` exist. Pro/Max bypass hook = `Entitlement.hasProAccessAt(now)` (already used at `InputViewModel.kt:46`).

---

## Confirmed implementation choices (user-approved)

1. **Phase 1 "Watch Ad" button is hidden** — only "Come back tomorrow" renders in the quota dialog. Phase 3 re-enables the button when the real AdMob gateway lands. Rationale: never offer an ad that can't load yet (policy-honesty).
2. **Day-rollover is lazy VM-flow map + write-on-mutation** — `quotaState` StateFlow maps through `rollDayIfNeeded(state, today)` so readers always see the rolled state without a write. DataStore only gets the reset write the first time a Free mutation happens on the new day. No eager write on screen mount, no WorkManager.

---

## New files (3)

### 1. `app/src/main/java/com/needsvswants/app/domain/AdsConfig.kt`

Single source of truth for all quota/ad constants. Pure `object`, no Android imports.

```kotlin
package com.needsvswants.app.domain

object AdsConfig {
    const val ENABLED = true                 // master kill switch (Phase 4 wires NoOp gateway when false)
    const val FREE_DAILY_LOGS = 10            // free-tier allowance per local calendar day
    const val EXTRA_LOGS_PER_REWARD = 8       // bonus logs granted per successful reward callback
    const val MAX_REWARDED_ADS_PER_DAY = 3    // hard ceiling → +24 bonus logs/day max
    // Dev ad unit + App ID are Phase 3 concerns (AdMob SDK not on classpath in Phase 1).
}
```

### 2. `app/src/main/java/com/needsvswants/app/domain/DailyLogQuota.kt`

Pure state machine. No Android, no Hilt, no DataStore. Takes an immutable snapshot + "today" string and returns the next snapshot.

```kotlin
package com.needsvswants.app.domain

data class QuotaState(
    val day: String,            // "yyyy-MM-dd" (local)
    val logsCreated: Int,       // sealed logs counted today
    val bonusLogs: Int,         // granted via rewards today
    val adsWatched: Int         // successful onUserEarnedReward callbacks today
)

object DailyLogQuota {
    /** Total logs the free user may still seal "today". */
    fun remaining(state: QuotaState, today: String): Int =
        (AdsConfig.FREE_DAILY_LOGS + state.bonusLogs - state.logCreated).coerceAtLeast(0)

    /** Pro/Max callers must short-circuit to true before calling this. */
    fun canLog(state: QuotaState, today: String): Boolean =
        remaining(rollDayIfNeeded(state, today), today) > 0

    /** Called by VM after a Room insert succeeds. Returns the bumped state. */
    fun incrementCreated(state: QuotaState, today: String): QuotaState {
        val rolled = rollDayIfNeeded(state, today)
        return rolled.copy(logsCreated = rolled.logsCreated + 1)
    }

    /** Can the user even request another rewarded ad today? */
    fun canWatchAd(state: QuotaState, today: String): Boolean {
        val rolled = rollDayIfNeeded(state, today)
        return rolled.adsWatched < AdsConfig.MAX_REWARDED_ADS_PER_DAY
    }

    /** Called ONLY from the onUserEarnedReward callback path (Phase 3). Returns bumped state. */
    fun grantBonus(state: QuotaState, today: String): QuotaState {
        val rolled = rollDayIfNeeded(state, today)
        if (rolled.adsWatched >= AdsConfig.MAX_REWARDED_ADS_PER_DAY) return rolled
        return rolled.copy(
            adsWatched = rolled.adsWatched + 1,
            bonusLogs = rolled.bonusLogs + AdsConfig.EXTRA_LOGS_PER_REWARD
        )
    }

    /** New calendar day → reset all counters. Idempotent if already on today. */
    fun rollDayIfNeeded(state: QuotaState, today: String): QuotaState =
        if (state.day == today) state else QuotaState(day = today, logsCreated = 0, bonusLogs = 0, adsWatched = 0)
}
```

### 3. `app/src/test/java/com/needsvswants/app/domain/DailyLogQuotaTest.kt`

JUnit4 only (matches `StreakMathTest.kt` style — `junit` 4.13.2 already in `libs.versions.toml`). No coroutines, no Android. Written RED first.

Test cases (locked before implementation):

| # | Test | Assertion |
|---|------|-----------|
| 1 | `fresh_state_on_new_day_can_log_free_count` | `QuotaState("2026-08-07",0,0,0)` → `remaining=10`, `canLog=true` |
| 2 | `after_free_count_canLog_false` | `logsCreated=10,0,0` → `canLog=false`, `remaining=0` |
| 3 | `grantBonus_adds_extra_logs_and_increments_ads` | `(0,0,0)` → grantBonus → `(0,8,1)`, `remaining=18` |
| 4 | `grantBonus_after_max_ads_is_noop` | `(0,16,3)` → grantBonus → state unchanged |
| 5 | `canWatchAd_false_after_three` | `adsWatched=3` → `canWatchAd=false` |
| 6 | `incrementCreated_advances_counter` | `(5,0,0)` → `(6,0,0)` |
| 7 | `rollDay_resets_when_day_changes` | `("2026-08-06", 18, 24, 3)` today=`"2026-08-07"` → `(2026-08-07, 0, 0, 0)`, `remaining=10` |
| 8 | `rollDay_idempotent_when_same_day` | state unchanged when `state.day == today` |
| 9 | `remaining_floored_at_zero_when_over_count` | `logsCreated=15,0,0` → `remaining=0` (never negative) |
| 10 | `pro_unlimited_handled_by_caller_not_domain` | domain is unaware of entitlement; caller bypasses (documented via test comment) |

---

## Edited files (3, additive only)

### 4. `app/src/main/java/com/needsvswants/app/data/prefs/AppPreferences.kt`

Additive DataStore keys. Companion block additions:

```kotlin
private val QUOTA_DAY = stringPreferencesKey("quota_day")
private val QUOTA_LOGS_CREATED = intPreferencesKey("quota_logs_created")
private val QUOTA_BONUS_LOGS = intPreferencesKey("quota_bonus_logs")
private val QUOTA_ADS_WATCHED = intPreferencesKey("quota_ads_watched")
```

New members (additive, no renaming):

```kotlin
val quotaState: Flow<QuotaState> = context.dataStore.data.map { prefs ->
    QuotaState(
        day = prefs[QUOTA_DAY] ?: "",
        logsCreated = prefs[QUOTA_LOGS_CREATED] ?: 0,
        bonusLogs = prefs[QUOTA_BONUS_LOGS] ?: 0,
        adsWatched = prefs[QUOTA_ADS_WATCHED] ?: 0
    )
}

suspend fun setQuotaState(state: QuotaState) {
    context.dataStore.edit {
        it[QUOTA_DAY] = state.day
        it[QUOTA_LOGS_CREATED] = state.logsCreated
        it[QUOTA_BONUS_LOGS] = state.bonusLogs
        it[QUOTA_ADS_WATCHED] = state.adsWatched
    }
}

suspend fun resetQuotaForDay(today: String) {
    setQuotaState(QuotaState(day = today, logsCreated = 0, bonusLogs = 0, adsWatched = 0))
}
```

No other surface changes. KSP/Hilt unaffected.

### 5. `app/src/main/java/com/needsvswants/app/ui/screens/input/InputViewModel.kt`

Touch points (exact, minimal):

**(a) Inject `QuotaState` flow + roll over on first read.** Add near `entitlement` (line 41):

```kotlin
val quotaState: StateFlow<QuotaState> = preferences.quotaState
    .map { DailyLogQuota.rollDayIfNeeded(it, todayString()) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuotaState("", 0, 0, 0))
```

`todayString()` = private helper using the same `SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())` already used in `sealNow` (line 120).

**(b) Gate `trySeal()` (line 85-101).** Insert the quota check between the validation block and the overspend block, scoped to Free only:

```kotlin
fun trySeal() {
    if (isSealing) return
    if (_overspendConfirm.value != null) return
    if (_quotaBlocked.value != null) return
    val item = activeItem.value.trim()
    val costCents = parseCents(activeCost.value)
    val type = activeType.value
    if (item.isEmpty() || costCents == null || type == null || isSheetFull) return

    // Quota gate — Free only; Pro/Max bypass entirely (Entitlement.hasProAccessAt).
    val now = System.currentTimeMillis()
    if (!entitlement.value.hasProAccessAt(now)) {
        val rolled = DailyLogQuota.rollDayIfNeeded(quotaState.value, todayString())
        if (!DailyLogQuota.canLog(rolled, todayString())) {
            _quotaBlocked.value = QuotaBlocked(item, costCents, type)
            return
        }
    }

    // Existing overspend check (unchanged) …
    val status = budgetStatus.value
    if (status is BudgetStatus.On &&
        DailyBudgetMath.wouldExceed(status.spentCents, status.budgetCents, costCents)
    ) {
        _overspendConfirm.value = costCents
        return
    }
    sealNow(item, costCents, type)
}
```

`QuotaBlocked` data class + state:

```kotlin
data class QuotaBlocked(val item: String, val costCents: Long, val type: EntryType)

private val _quotaBlocked = MutableStateFlow<QuotaBlocked?>(null)
val quotaBlocked: StateFlow<QuotaBlocked?> = _quotaBlocked.asStateFlow()
```

**(c) Increment `logsCreated` after `sealNow()` Room insert succeeds.** Inside the existing `viewModelScope.launch` in `sealNow` (line 123-140), after `entries.insert(...)` and before the field clear, add for Free only:

```kotlin
if (!entitlement.value.hasProAccessAt(now)) {
    val current = preferences.quotaState.first()
    val rolled = DailyLogQuota.rollDayIfNeeded(current, todayString())
    preferences.setQuotaState(DailyLogQuota.incrementCreated(rolled, todayString()))
}
```

**(d) Pending-seal dialog controls:**

```kotlin
fun dismissQuotaBlocked() { _quotaBlocked.value = null }

fun onWatchAd() {
    // Phase 3: gateway.loadAndShow() + grant on onUserEarnedReward.
    // Phase 1: button is hidden; this is a no-op stub kept for the dialog to call.
}

fun retrySealAfterReward() {
    // Phase 3: grant bonus → retry pending seal.
    // Phase 1: no-op stub.
}
```

No network, no AdMob, no SDK imports added. `sealNow` and the Entry/Room path are byte-for-byte unchanged otherwise.

### 6. `app/src/main/java/com/needsvswants/app/ui/screens/input/InputScreen.kt`

Add a `collectAsStateWithLifecycle` for `quotaBlocked` (mirror the `pendingOverspendCost` pattern at line 85) and a new `pendingOverspendCost?.let { … }`-style block (mirror lines 367-397) rendering a `PremiumDialog` with the exact locked copy:

```kotlin
quotaBlocked?.let { blocked ->
    PremiumDialog(
        onDismissRequest = { viewModel.dismissQuotaBlocked() },
        eyebrow = "DAILY QUOTA",
        eyebrowColor = palette.gilt,
        title = "You've used your free logs for today.",
        bodyContent = {
            Column {
                Text(
                    "Watch a short ad to unlock +8 more logs today?",
                    color = palette.textSecondary,
                    style = AppType.body
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Phase 1: "Watch Ad" button hidden (no AdMob SDK yet).
                    // Phase 3: re-enable this GiltButton and wire viewModel.onWatchAd().
                    TextButton(
                        onClick = { viewModel.dismissQuotaBlocked() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Come back tomorrow", color = palette.textMuted)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Or go Pro — unlimited logs, no ads",
                    style = AppType.bodySm,
                    color = palette.textMuted   // ghost line, soft CTA per locked decision
                )
            }
        },
        confirmLabel = "Come back tomorrow",   // unused — bodyContent drives the layout
        onConfirm = { viewModel.dismissQuotaBlocked() },
        dismissLabel = "Come back tomorrow"
    )
}
```

No new component created — reuses existing `PremiumDialog`, `GiltButton`, `TextButton`, `Eyebrow`, `AppType`, `Column`, `Row`, `Spacer`, `palette`.

---

## File-level change summary

| File | Action | New lines (est.) | Reason |
|------|--------|------------------|--------|
| `domain/AdsConfig.kt` | **New** | ~10 | Single source for `10 / 8 / 3 / ENABLED` constants |
| `domain/DailyLogQuota.kt` | **New** | ~60 | Pure state machine; no Android imports |
| `app/src/test/.../DailyLogQuotaTest.kt` | **New** | ~120 | TDD domain cover (10 cases) |
| `data/prefs/AppPreferences.kt` | Edit | +~25 | 4 additive DataStore keys + `quotaState` flow + `setQuotaState`/`resetQuotaForDay` |
| `ui/screens/input/InputViewModel.kt` | Edit | +~40 | Quota gate in `trySeal`, increment in `sealNow`, pending-seal state + stub handlers |
| `ui/screens/input/InputScreen.kt` | Edit | +~35 | New `QuotaBlocked` `PremiumDialog` with exact locked copy + soft Pro CTA |

**Total:** 3 new files (~190 LOC), 2 edits (~65 LOC added). No deletions, no renames, no schema/migration, no new gradle deps, no manifest change, no SDK.

---

## What Phase 1 deliberately does NOT touch

- ❌ `ads/` package (Phase 3 creates it)
- ❌ `gradle/libs.versions.toml`, `app/build.gradle.kts`, `AndroidManifest.xml` — no AdMob/UMP until Phase 3
- ❌ `Entry.kt`, `EntryDao`, `EntryRepository`, Room schema, migrations, 35-day purge — zero retention risk
- ❌ Settings screen (Phase 2)
- ❌ Pro/Max flow — already bypasses via `hasProAccessAt(now)` before the gate evaluates
- ❌ Network calls inside `sealNow` — the only new `preferences.*` call is a local DataStore write, same thread as the existing `entries.insert`. AdMob init strictly deferred to Phase 3 (and only on "Watch ad" tap, per the locked non-negotiables)

---

## Acceptance gates (Phase 1)

1. **Domain tests green** — `DailyLogQuotaTest` (10 cases) compiles and passes under `./gradlew :app:testDebugUnitTest` with airplane mode on (pure JVM, no device).
2. **Offline free path works** — free user can seal 10 logs in one calendar day; the 11th `trySeal` opens the `PremiumDialog` with the exact locked disclosure copy.
3. **No silent no-op** — `trySeal` at quota hit always routes to `_quotaBlocked` (pending-seal) and never just returns; mirrors the overspend pattern.
4. **Pro/Max never sees the dialog** — `Entitlement.hasProAccessAt(now)` true short-circuits the gate; 11th seal of the day for Pro proceeds normally.
5. **Stacking preserved** — 20-row `isSheetFull` fires first when applicable; daily quota is a third free cap, not a replacement.
6. **Day rollover resets** — next local calendar day, `DailyLogQuota.rollDayIfNeeded` clears all four counters via the lazy VM-flow map on the next Free seal; `remaining` returns 10.
7. **Soft Pro CTA present** — ghost line "Or go Pro — unlimited logs, no ads" rendered in `palette.textMuted` (locked decision).
8. **Build & lint clean** — `./gradlew :app:assembleDebug` and `./gradlew :app:lintDebug` pass; verify no new warnings introduced in edited files.

---

## Risks flagged for Phase 3 (not blocking Phase 1)

- **Stale stored prefs on a no-seal day.** Lazy rollover means `quota_day` in DataStore may lag the real date until the first Free seal/grant. Harmless because every read rolls; but Phase 2 Settings must use the `quotaState` **mapped flow** (`rollDayIfNeeded` applied), not the raw prefs read. Noted for Phase 2 plan.
- **Phase 3 InputScreen edit:** the new `PremiumDialog` block will need to gain the conditional "Watch Ad" button. Phase 1 code is structured so that change is a small in-place add, not a refactor.
- **No ProGuard impact** in Phase 1 (no new SDK).

---

## Implementation order

1. `domain/AdsConfig.kt` → `domain/DailyLogQuota.kt` → `DailyLogQuotaTest.kt` (TDD: write test RED, then domain GREEN)
2. `data/prefs/AppPreferences.kt` (additive keys + flows)
3. `ui/screens/input/InputViewModel.kt` (gate + increment + pending state)
4. `ui/screens/input/InputScreen.kt` (dialog)
5. Run `:app:testDebugUnitTest` + `:app:assembleDebug` + `:app:lintDebug`
6. Update `Tasks.md` progress + append decision **D40** to `Decisions.md` (dated, agent ID OpenCode, rationale)
