# AdMob + Trainer Gaps Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:subagent-driven-development` (recommended) or `superpowers:executing-plans` to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Restore rewarded AdMob on the Free daily quota, keep D122 streak carry, make the site honest about ads, then close the trainer-faithful hygiene and product holes from the 2026-08-15 diagnosis.

**Architecture:** Restore the isolated `ads/` package (D84 cancel-safe gateway + UMP on first Watch-ad tap). Merge bonus/ads-watched into the existing carry-forward `QuotaState` without letting unused ad bonus roll overnight. Keep Pro/Max ad-less. Then a second wave of small, local UI/copy fixes that do not change the Need/Want method.

**Tech Stack:** Kotlin, Jetpack Compose, Hilt, DataStore, Google Mobile Ads `play-services-ads` **23.6.0**, UMP **2.2.0** (compileSdk 34), existing `PremiumDialog` / `GiltButton`, static `website/` + `check.js`.

**Baseline (do not regress):** Android **2.0.15 / versionCode 23**, live site and APK sha `b3b4085e…`, 5 free seals/day + carry, 30-day Free retention, D86 signing, D120 Eagerly quota, D126 Go Pro/Max CTA. Git `main` is **7 commits ahead of origin** — do not push unless asked. Do **not** bump the version or deploy unless asked.

**On disk after approval:** also copy this plan to `docs/superpowers/plans/2026-08-15-admob-hygiene-trainer.md`.

---

## What this plan is (and is not)

This is the diagnosis plan and the AdMob restore, as one sequence.

| Wave | Ships |
|---|---|
| **A — AdMob** | Rewarded extra seals for Free, UMP, Settings bonus rows, site/privacy honesty |
| **B — Hygiene** | Dead crash-report toggle, ACCOUNT+PLAN merge, How it works in Settings, sign-out confirm, spending-goal Settings row, coach uses real goal, Advisor money formatted, stale vault/docs |
| **C — Trainer holes** | Log “earlier today” time, last-3 replay chips, first-backup nudge |

**Do not add:** banners, interstitials, app-open ads, bank sync, categories, income accounts, PIN lock, Filipino locale, Play Store listing, D121 `admobFailFast` release block, weekly/payday budget (ads already tell that story; keep C small).

---

## Gate (before first code edit)

```
[x] Obsidian: Memory Index + Summary + Tasks + Decisions D82–D87, D119–D126, D149–D158
[x] Context7: /googleads/googleads-mobile-android-examples (RewardedAd.load / show / onUserEarnedReward)
[x] Context7: /websites/developers_google_ad-manager_mobile-ads-sdk_android (UMP canRequestAds + loadAndShowConsentFormIfRequired)
[x] Graphify: existing .graphify is stale (2026-08-03); use app/ ads blast radius from code, not a rebuild
```

SDK versions stay **23.6.0 / 2.2.0**. Do not jump to a line that needs compileSdk 35.

---

## File map

**Create**

- `app/src/main/java/com/needsvswants/app/ads/RewardedAdGateway.kt`
- `app/src/main/java/com/needsvswants/app/ads/AdMobRewardedAdGateway.kt`
- `app/src/main/java/com/needsvswants/app/ads/ConsentHelper.kt`
- `app/src/main/java/com/needsvswants/app/ads/NoOpRewardedAdGateway.kt`
- `app/src/main/java/com/needsvswants/app/di/AdsModule.kt`
- `app/src/main/java/com/needsvswants/app/domain/AdsConfig.kt`
- `app/src/test/java/com/needsvswants/app/ads/FakeRewardedAdGateway.kt` (if not inlined in InputViewModelTest)

**Restore source of truth:** `git show 82aa6bcc:<path>` then overlay D84 cancel-safe bits from `8969364` on `AdMobRewardedAdGateway.kt` and `ConsentHelper.kt`. Do not invent a third gateway.

**Modify**

- `app/src/main/java/com/needsvswants/app/domain/DailyLogQuota.kt`
- `app/src/main/java/com/needsvswants/app/domain/FreeQuotaConfig.kt` — delete after AdsConfig owns `FREE_DAILY_LOGS`
- `app/src/main/java/com/needsvswants/app/data/prefs/AppPreferences.kt`
- `app/src/main/java/com/needsvswants/app/ui/screens/input/InputViewModel.kt`
- `app/src/main/java/com/needsvswants/app/ui/screens/input/InputScreen.kt`
- `app/src/main/java/com/needsvswants/app/ui/screens/settings/SettingsViewModel.kt`
- `app/src/main/java/com/needsvswants/app/ui/screens/settings/SettingsScreen.kt`
- `app/src/main/java/com/needsvswants/app/ui/screens/settings/MembershipDesk.kt` (sign-out confirm)
- `app/src/main/java/com/needsvswants/app/domain/FinancialAdvisor.kt` (cents → `toMoney`)
- `app/src/main/AndroidManifest.xml`
- `app/build.gradle.kts`
- `gradle/libs.versions.toml`
- `app/proguard-rules.pro`
- `website/public/index.html`, `website/index.html` (via apply.js), `website/public/privacy.html`
- `website/_pad-parts/check.js`
- Tests: `DailyLogQuotaTest.kt`, `AppPreferencesTest.kt`, `InputViewModelTest.kt`, advisor tests that pin raw-cents strings

**Do not touch:** paywall pricing, entitlement math, Room `Entry` schema (Wave C time override is a VM stamp, not a new column), website notepad flip (D32).

---

## Locked product numbers

```kotlin
object AdsConfig {
    const val ENABLED = true
    const val FREE_DAILY_LOGS = 5
    const val EXTRA_LOGS_PER_REWARD = 8
    const val MAX_REWARDED_ADS_PER_DAY = 3
}
```

Google **TEST** IDs (defaults in `local.properties` / BuildConfig):

- App ID: `ca-app-pub-3940256099942544~3347511713`
- Rewarded unit: `ca-app-pub-3940256099942544/5224354917`

Quota remaining today: `5 + carriedLogs + bonusLogs − logsCreated` (floor 0).

Overnight carry (consecutive active day only): unused of **`5 + carried − created`**. Unused bonus does **not** carry. New day: `bonusLogs = 0`, `adsWatched = 0`.

Grant **only** in `onUserEarnedReward`. Closed-without-reward = no bonus.

---

## Wave A — AdMob

### Task A1: Quota domain + tests first (TDD)

**Files:** `DailyLogQuota.kt`, `AdsConfig.kt`, `FreeQuotaConfig.kt`, `DailyLogQuotaTest.kt`

- [ ] **Step 1: Add AdsConfig and extend QuotaState with defaults** so existing 3-arg tests still compile.

```kotlin
package com.needsvswants.app.domain

object AdsConfig {
    /**
     * Master kill switch. true = AdMob gateway bound, Watch-ad button shown.
     * Pro/Max never hit the quota gate (Entitlement.hasProAccessAt).
     * IDs are Google TEST values until a real AdMob app exists.
     */
    const val ENABLED = true
    const val FREE_DAILY_LOGS = 5
    const val EXTRA_LOGS_PER_REWARD = 8
    const val MAX_REWARDED_ADS_PER_DAY = 3
}

data class QuotaState(
    val day: String,
    val logsCreated: Int,
    val carriedLogs: Int,
    val bonusLogs: Int = 0,
    val adsWatched: Int = 0
)
```

- [ ] **Step 2: Write the new failing tests** at the bottom of `DailyLogQuotaTest.kt`. Replace every `FreeQuotaConfig.FREE_DAILY_LOGS` with `AdsConfig.FREE_DAILY_LOGS`.

```kotlin
@Test
fun grantBonus_addsEightAndCountsAnAd() {
    val state = QuotaState("2026-08-15", logsCreated = 5, carriedLogs = 0)
    val granted = DailyLogQuota.grantBonus(state, "2026-08-15")
    assertEquals(8, granted.bonusLogs)
    assertEquals(1, granted.adsWatched)
    assertEquals(8, DailyLogQuota.remaining(granted, "2026-08-15"))
    assertTrue(DailyLogQuota.canLog(granted, "2026-08-15"))
}

@Test
fun grantBonus_capsAtThreeAds() {
    var state = QuotaState("2026-08-15", 0, 0)
    repeat(3) { state = DailyLogQuota.grantBonus(state, "2026-08-15") }
    assertEquals(3, state.adsWatched)
    assertEquals(24, state.bonusLogs)
    val blocked = DailyLogQuota.grantBonus(state, "2026-08-15")
    assertEquals(3, blocked.adsWatched)
    assertEquals(24, blocked.bonusLogs)
    assertFalse(DailyLogQuota.canWatchAd(blocked, "2026-08-15"))
}

@Test
fun unused_bonus_does_not_carry() {
    // 5 created + 8 bonus → remaining 8 same day; next day carry is only unused BASE (0).
    val day1 = QuotaState("2026-08-15", logsCreated = 5, carriedLogs = 0, bonusLogs = 8, adsWatched = 1)
    val rolled = DailyLogQuota.rollDayIfNeeded(day1, "2026-08-16")
    assertEquals(0, rolled.carriedLogs)
    assertEquals(0, rolled.bonusLogs)
    assertEquals(0, rolled.adsWatched)
    assertEquals(AdsConfig.FREE_DAILY_LOGS, DailyLogQuota.remaining(rolled, "2026-08-16"))
}

@Test
fun remaining_includes_bonus_and_carry() {
    val state = QuotaState("2026-08-15", logsCreated = 2, carriedLogs = 3, bonusLogs = 8, adsWatched = 1)
    assertEquals(5 + 3 + 8 - 2, DailyLogQuota.remaining(state, "2026-08-15"))
}
```

- [ ] **Step 3: Run tests — expect fail** on `grantBonus` / `canWatchAd` missing.

```
./gradlew :app:testFullDebugUnitTest --tests com.needsvswants.app.domain.DailyLogQuotaTest
```

- [ ] **Step 4: Implement math**

```kotlin
fun remaining(state: QuotaState, today: String): Int {
    val rolled = rollDayIfNeeded(state, today)
    return (AdsConfig.FREE_DAILY_LOGS + rolled.carriedLogs + rolled.bonusLogs - rolled.logsCreated)
        .coerceAtLeast(0)
}

fun canWatchAd(state: QuotaState, today: String): Boolean {
    val rolled = rollDayIfNeeded(state, today)
    return rolled.adsWatched < AdsConfig.MAX_REWARDED_ADS_PER_DAY
}

fun grantBonus(state: QuotaState, today: String): QuotaState {
    val rolled = rollDayIfNeeded(state, today)
    if (rolled.adsWatched >= AdsConfig.MAX_REWARDED_ADS_PER_DAY) return rolled
    return rolled.copy(
        adsWatched = rolled.adsWatched + 1,
        bonusLogs = rolled.bonusLogs + AdsConfig.EXTRA_LOGS_PER_REWARD
    )
}

fun rollDayIfNeeded(state: QuotaState, today: String): QuotaState {
    if (state.day == today) return state
    val carried = if (isConsecutiveActive(state, today)) {
        (AdsConfig.FREE_DAILY_LOGS + state.carriedLogs - state.logsCreated).coerceAtLeast(0)
    } else 0
    return QuotaState(day = today, logsCreated = 0, carriedLogs = carried, bonusLogs = 0, adsWatched = 0)
}
```

- [ ] **Step 5: Delete `FreeQuotaConfig.kt`.** Point Settings + tests at `AdsConfig.FREE_DAILY_LOGS`.
- [ ] **Step 6: Re-run DailyLogQuotaTest — all green.** Commit `test(quota): restore ad bonus on carry-forward remaining`.

### Task A2: Persist bonus + ads watched

**Files:** `AppPreferences.kt`, `AppPreferencesTest.kt`

- [ ] Add keys (D122 left these unused on purpose):

```kotlin
private val QUOTA_BONUS_LOGS = intPreferencesKey("quota_bonus_logs")
private val QUOTA_ADS_WATCHED = intPreferencesKey("quota_ads_watched")
```

- [ ] Read/write them in `quotaState` / `setQuotaState` / `resetQuotaForDay` (reset zeroes all five fields).
- [ ] Extend `quotaState_roundTripsDay_logsCreated_andCarriedLogs` to assert `bonusLogs` and `adsWatched`.
- [ ] Run `AppPreferencesTest`. Commit `feat(prefs): persist quota bonus and ads watched`.

### Task A3: Restore ads package + Gradle + manifest

**Files:** ads/*, `AdsModule.kt`, `libs.versions.toml`, `app/build.gradle.kts`, `AndroidManifest.xml`, `proguard-rules.pro`

- [ ] Restore the four ads files + AdsModule from `82aa6bcc`, then overlay `8969364` on the gateway + ConsentHelper (generation token, init-before-load, `runOnUiThread`, `reset()` on dismiss).
- [ ] Wire rewarded unit from `BuildConfig.ADMOB_REWARDED_UNIT_ID`, not a hardcoded string in two places. App ID via manifest placeholder.

`gradle/libs.versions.toml` add:

```toml
play-services-ads = "23.6.0"
ump = "2.2.0"

# under [libraries]
play-services-ads = { group = "com.google.android.gms", name = "play-services-ads", version.ref = "play-services-ads" }
user-messaging-platform = { group = "com.google.android.ump", name = "user-messaging-platform", version.ref = "ump" }
```

`app/build.gradle.kts` `defaultConfig`:

```kotlin
buildConfigField(
    "String",
    "ADMOB_APP_ID",
    "\"${localProp("ADMOB_APP_ID", "ca-app-pub-3940256099942544~3347511713")}\""
)
buildConfigField(
    "String",
    "ADMOB_REWARDED_UNIT_ID",
    "\"${localProp("ADMOB_REWARDED_UNIT_ID", "ca-app-pub-3940256099942544/5224354917")}\""
)
manifestPlaceholders["admobAppId"] = localProp(
    "ADMOB_APP_ID",
    "ca-app-pub-3940256099942544~3347511713"
)
```

Dependencies: `implementation(libs.play.services.ads)` and `implementation(libs.user.messaging.platform)`.

Manifest inside `<application>`:

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="${admobAppId}" />
```

`AdsModule`:

```kotlin
@Binds @Singleton
abstract fun bindGateway(impl: AdMobRewardedAdGateway): RewardedAdGateway
```

If `!AdsConfig.ENABLED`, bind `NoOpRewardedAdGateway` instead (same `@Binds` with a provider `@Provides` that branches — match D119).

Do **not** add `admobFailFast`.

`proguard-rules.pro` one-line note: ads AARs ship consumer rules; keep test IDs out of marketing copy.

- [ ] `./gradlew :app:assembleFullDebug` must resolve deps. Commit `feat(ads): restore AdMob rewarded gateway and UMP`.

### Task A4: InputViewModel watch + grant

**Files:** `InputViewModel.kt`, `InputViewModelTest.kt`

Constructor gains `private val rewardedAds: RewardedAdGateway`. `buildViewModel()` in tests passes `NoOpRewardedAdGateway()` (or a fake that records `loadAndShow` / `reset`).

Keep `SharingStarted.Eagerly` on `quotaState` and `entitlement`.

```kotlin
sealed class AdState {
    data object Idle : AdState()
    data object Loading : AdState()
    data class Failed(val message: String) : AdState()
}

val canWatchAdToday: StateFlow<Boolean> = quotaState
    .map { AdsConfig.ENABLED && DailyLogQuota.canWatchAd(it, todayString()) }
    .stateIn(viewModelScope, SharingStarted.Eagerly, false)

private val _adState = MutableStateFlow<AdState>(AdState.Idle)
val adState: StateFlow<AdState> = _adState.asStateFlow()

fun onWatchAd(activity: android.app.Activity) {
    if (!AdsConfig.ENABLED) return
    if (!canWatchAdToday.value) return
    if (_adState.value is AdState.Loading) return
    _adState.value = AdState.Loading
    rewardedAds.loadAndShow(
        activity = activity,
        onUserEarnedReward = { grantAndRetrySeal() },
        onClosed = { /* leave Failed or Idle; do not grant */ },
        onFailed = { msg -> _adState.value = AdState.Failed(msg) }
    )
}

fun dismissQuotaBlocked() {
    rewardedAds.reset()
    _adState.value = AdState.Idle
    _quotaBlocked.value = null
}

private fun grantAndRetrySeal() {
    viewModelScope.launch {
        val current = preferences.quotaState.first()
        preferences.setQuotaState(DailyLogQuota.grantBonus(current, todayString()))
        _adState.value = AdState.Idle
        val pending = _quotaBlocked.value
        _quotaBlocked.value = null
        if (pending != null) {
            activeItem.value = pending.item
            activeCost.value = (pending.costCents / 100.0).toString() // use existing toInputAmount
            activeType.value = pending.type
            trySeal()
        }
    }
}
```

Use `toInputAmount()` for the cost replay, not float division.

Tests to add:

- After 5 used, `trySeal` sets `quotaBlocked`; fake gateway `onUserEarnedReward` → `logsCreated` still 5 until retry inserts; remaining becomes 8; insert happens.
- `dismissQuotaBlocked` calls `reset` on the fake (no grant).
- Pro user never sets `quotaBlocked` (existing test stays).

`quotaState` initial value becomes `QuotaState("", 0, 0)` (defaults cover bonus/ads).

- [ ] Run `InputViewModelTest`. Commit `feat(log): watch-ad grant retries the blocked seal`.

### Task A5: Quota dialog + Settings rows

**Files:** `InputScreen.kt`, `SettingsViewModel.kt`, `SettingsScreen.kt`

Quota dialog (keep D126 primary CTA):

- Title/body unchanged (carry copy stays).
- Extra body: if `AdsConfig.ENABLED && canWatchAdToday`, show a `GiltButton("Watch ad")` that calls `viewModel.onWatchAd(activity)` after `context as? Activity`.
- While `AdState.Loading`: button disabled, label `Loading ad…`.
- `AdState.Failed`: danger-colored message from the gateway (friendly, never a stack trace).
- If ads-watched cap hit: no Watch button (body already has tomorrow + Pro).

Settings `DailyFreeLogsInfo` add `bonusLogs: Int`, `adsWatched: Int`. Panel rows after carried:

- `Bonus from ads` → `+N` (hidden when 0)
- `Ads watched today` → `N / 3`

Section still null for Pro/Max. Hidden entirely when `!AdsConfig.ENABLED` is false? **Show the panel whenever Free** — bonus rows only if ENABLED.

- [ ] `assembleFullDebug` + `lintFullDebug`. Commit `feat(ui): Watch ad on quota dialog and Settings bonus rows`.

### Task A6: Website + privacy honesty

**Files:** both HTML mirrors (edit `website/public/index.html` then `node website/_pad-parts/apply.js`), `privacy.html`, `check.js`

Replace every user-facing “No ads” / “no ads, no analytics” with honest copy:

- Hero lede: drop “no ads”. Keep “No account needed” and “no analytics”.
- Meta description: same.
- Trust note: `The diary itself stays on your device. Free may offer an optional rewarded ad to unlock extra seals today. Pro and Max have no ads. Internet is used only for optional sign-in, checkout, entitlement checks, and that optional ad.`
- Footer chip line: `No account needed · Optional rewarded ads on Free · No analytics · …`
- Free plan bullet: add `Watch an ad for extra seals (up to 3 per day)`.
- Privacy short version: replace “No ads, no analytics” with the same optional-rewarded / no tracking distinction.

`check.js`:

- Ban the old claim `'No ads, no tracking'` and `'No ads, no analytics'` (exact strings currently on the page).
- Add lock: `/optional rewarded ad/`.
- Keep existing banned `no network calls` / `100% offline` / `NotebookLM`.

```
node website/_pad-parts/apply.js
node website/_pad-parts/check.js
```

Expect `ALL CHECKS PASSED` and byte-identical mirrors.

- [ ] Commit `docs(site): honest copy for optional Free rewarded ads`.

---

## Wave B — Hygiene (diagnosis)

Do these after Wave A is green so Settings does not churn twice.

### Task B1: Crash-reports toggle is honest

**Files:** `SettingsScreen.kt` Privacy section, `CrashReporting.kt`

If `BuildConfig.SENTRY_DSN.isBlank()`, do not show a working toggle. Show muted caption: `Crash reports are off in this build.` Do not persist a lie.

### Task B2: One membership CTA

**Files:** `SettingsScreen.kt`

Free users: keep ACCOUNT card + `View Pro & Max plans`. **Delete the PLAN section** (same destination). Paid users: MembershipDesk already has renew/upgrade — delete PLAN there too.

### Task B3: How it works + spending goal in Settings

**Files:** `SettingsScreen.kt`, `SettingsViewModel.kt`, `AppPreferences.kt`, `AppNavigation.kt` if needed

- Add a Settings row `How it works` that sets a one-shot `showInstructions` (same overlay as Summary `?`). Easiest path: callback `onShowInstructions` from `AppNavigation` into Settings, same as Summary.
- Add a small `SPENDING GOAL` panel: Track / Budget / Analyze, writes `preferences.setSpendingGoal`. Default `track`.

### Task B4: Sign-out confirm

**Files:** `MembershipDesk.kt`

Wrap `onSignOut` in `PremiumDialog` (eyebrow ACCOUNT, title `Sign out?`, body `This device goes back to Free until you sign in again.`, confirm `Sign out`, danger).

### Task B5: Coach uses real spending goal

**Files:** `InputViewModel.kt`

`wantHoldSuggestion` currently hardcodes `AdvisorContextPack.DEFAULT_SPENDING_GOAL`. Collect `preferences.spendingGoal` (Eagerly) and pass it into `AdvisorContextPack.build`.

### Task B6: Advisor money is money, not cents

**Files:** `FinancialAdvisor.kt` + tests that pin the advice strings

Replace raw `${context.todayTotalCents}` / `${context.remainingCents} cents` with formatted amounts. Domain has no symbol — pass `symbol` into the pack **or** format as whole pesos via existing `toMoney` if tests already use a symbol. Prefer adding `currencySymbol` to `AdvisorContextPack` from the VM (it already has prefs). Update any regex-pinned tests.

Do not re-NotebookLM the copy (D150 honesty).

### Task B7: Stale memory

**Files:** vault via Git-Bash atomic write (`latin-1`, temp + `os.replace`):

- `Projects/Needs vs Wants/Tasks.md` — prepend 2026-08-15 progress; tick the stale 2.0.9 paywall item as superseded by D135/D137.
- `Decisions.md` — **D159** AdMob restore + carry merge + no fail-fast + site honesty.
- `Decisions.md` — **D160** hygiene (toggle, PLAN removed, How it works, goal, sign-out, coach goal, money format) when Wave B lands.
- `Summary.md` — Free tier now 5/day + optional rewarded ads; crash reporting still off without DSN.
- Dashboard: HEAD is `main` 2.0.15, tests exist, iOS archived.

Do **not** rewrite historical Task Phase boxes in the middle of the file; prepend a “current truth” note if needed.

`docs/KURT_MARKETING_ASSISTANT.md`: current public version **2.0.15**, and Free may offer a rewarded ad.

---

## Wave C — Trainer holes

### Task C1: Seal as earlier today

**Files:** `InputViewModel.kt`, `InputScreen.kt`, tests

Optional control under the seal form: `Now` (default) or a compact hour chip row for **today only** (`HH:00` back to 06:00). `sealNow` uses that timestamp for `dateUtc` / `date` / `time` instead of `Date()`. No new Room column. History edit already fixes item/cost/type.

Not a full calendar. Forgotten yesterday stays a History problem.

### Task C2: Last-3 replay chips

**Files:** `InputScreen.kt`, `InputViewModel.kt`

When the form is empty, show up to 3 unique most-recent `(item, type)` chips from `sheetEntries`. Tap fills item + type; cost stays empty so the user still types the amount (honest seal).

### Task C3: First-backup nudge

**Files:** `SettingsViewModel` / `InputViewModel` or a small one-shot pref `backup_nudge_seen`

After the user has **≥ 5 sealed entries** and `backupFolderUri == null`, show a single `PremiumDialog` (eyebrow DIARY, `Keep a copy?`, body points at Settings → Backup). Confirm opens Settings (nav callback) or the folder picker. Dismiss sets the seen flag forever.

---

## Verification (after each wave)

```
./gradlew :app:testFullDebugUnitTest
./gradlew :app:lintFullDebug
./gradlew :app:assembleFullDebug
```

Wave A extra:

- Merged manifest contains `com.google.android.gms.ads.APPLICATION_ID` with the test app id.
- DEX / mapping contains `5224354917` and `gms.ads`.
- `assembleFullRelease` still allowed (no fail-fast).
- `node website/_pad-parts/check.js` → ALL CHECKS PASSED.

Do not claim TalkBack or device ad-fill. Test IDs on a sideload often fail to fill — Failed copy + Go Pro/Max is the fallback.

---

## Suggested commits (keep scoped)

1. `test(quota): restore ad bonus on carry-forward remaining`
2. `feat(prefs): persist quota bonus and ads watched`
3. `feat(ads): restore AdMob rewarded gateway and UMP`
4. `feat(log): watch-ad grant retries the blocked seal`
5. `feat(ui): Watch ad on quota dialog and Settings bonus rows`
6. `docs(site): honest copy for optional Free rewarded ads`
7. `fix(settings): honest crash-reports, one plan CTA, how-it-works, goal, sign-out`
8. `fix(advisor): format money and honor spending goal`
9. `feat(log): earlier-today stamp and last-item chips`
10. `feat(backup): first-backup nudge after five seals`

Do not push. Do not tag. Do not bump 2.0.16 unless asked.

---

## Decision numbers to write after implementation

- **D159** — Rewarded AdMob restored for Free; carry kept; bonus does not carry; test IDs; no fail-fast; site honesty.
- **D160** — Hygiene from the 2026-08-15 diagnosis.
- **D161** — Earlier-today stamp, last-3 chips, first-backup nudge (if Wave C ships in the same pass).

---

## Execution after you approve

Plan will also be copied to `docs/superpowers/plans/2026-08-15-admob-hygiene-trainer.md`.

Two ways to run it:

1. **Subagent-driven** — one fresh implementer per task, review between tasks.
2. **Inline** — this session, wave by wave, with a checkpoint after Wave A (ads must be green before hygiene).

Start at Task A1. Do not skip the failing tests.
