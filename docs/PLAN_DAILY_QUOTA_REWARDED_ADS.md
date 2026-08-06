# Plan: Daily free log quota + optional rewarded ads

**Status:** Approved and implemented (Phases 1-4, D80-D84). **AdMob on hold + SDK STRIPPED from the build (D85, D87, 2026-08-07):** no AdMob account yet, app not on Play Store — deps commented out, ads/ AdMob impl removed (restore from git 5622b7e), `AdsConfig.ENABLED = false`, version stays 1.5.0, lean 5.34 MB release APK. Enable when the AdMob account + production App ID/unit exist.

**Related product:** Needs vs Wants expense trainer  
**Scope (recommended):** Android native APK only (`app/`)  
**Date:** 2026-08-07

---

## Verdict on the idea

**Solid fit** for an offline-first free trainer, *if* it is layered cleanly on top of the existing Free / Pro / Max model and never touches the seal path with network code.

What works well:

- Rewarded-only, opt-in, after free logs → respects privacy and frictionless first seals
- Local daily quota → offline free experience stays real
- Lazy AdMob init → matches non-negotiable rules

What needs sharpening (below) is mainly product vocabulary, interaction with the **existing** free caps, and Pro bypass.

---

## Product context

Needs vs Wants is an offline-first, privacy-focused 35-day expense trainer. Core philosophy must be protected:

- Frictionless logging at the moment of spending
- Offline-first (no network required for the free experience)
- Minimal, honest UI
- Data stays on device

### Monetization model (v1)

- Free users get a daily free allowance of logs (default: **10** logs per calendar day)
- When the daily free quota is reached, the user can watch a rewarded ad to unlock extra logs for that same day only (**+8** extra logs recommended)
- The limit resets every new calendar day (based on local device date)
- No lifetime hard limit
- Ads are never forced on the first logs of the day
- Core logging flow must remain clean and fast

### Non-negotiable rules

1. Offline must still work fully — free daily quota is enforced locally even without internet.
2. Never put ad initialization or network calls inside the main logging / sealing path.
3. Only rewarded ads. No banners, interstitials, or app-open ads.
4. AdMob SDK initializes only when the user is about to watch an ad (and device is online).
5. Keep the implementation minimal and easy to remove or adjust later.
6. Do not change the 35-day auto-delete behavior or existing data model unless necessary for the daily counter.

---

## What the codebase actually is

| Surface | Reality |
|--------|---------|
| **Shipped Android APK** | Native **Kotlin + Jetpack Compose + Material 3**, Hilt, Room, DataStore — `com.needsvswants.app`, `versionName 1.4.0` |
| **Not primary for this** | `expo/` and `ios/` exist; ads plan should be **Android-only** unless parity is requested |
| **Logging** | Log tab auto-seals when Item + Cost + Type are valid (`InputViewModel.trySeal` → `sealNow` → Room insert) |
| **Prefs** | `AppPreferences` (DataStore `settings`) — currency, budget, entitlement, auth, etc. |
| **Design system** | Supermarket premium: `PremiumDialog`, `PremiumSurface`, `GiltButton`, `Eyebrow`, gold hairlines |
| **Existing free monetization** | Free: **20 entries per sheet** then “Start new sheet” (currently **wipes all entries**), **35-day** purge. Pro/Max: unlimited sheets + retention |
| **Ads today** | None. `INTERNET` already in manifest (for Supabase seam). No AdMob deps |
| **Useful existing API** | `EntryDao.countForDate(date)` exists, but **prefer a creation counter in DataStore** for quota (see decisions) |

### Pre-code gate (for when implementation starts)

- **Obsidian:** Memory Index + Summary / Tasks / Decisions
- **Context7:** Google Mobile Ads examples (rewarded + UMP consent pattern)
- **Graphify:** blast radius is Log + Settings + prefs + new `ads/` package (root graph may be stale/web-heavy)

---

## Critical product clarifications (lock before coding)

### 1. Unit of account: **logs**, not sheets

Title language may say “extra sheets”; the model body means **+8 logs**. In this app:

- **Log / entry** = one sealed purchase
- **Sheet** = 20-row Log UI batch (existing free UX)

**Recommendation:** Count **sealed entries per local calendar day**. Keep “sheet” language out of the quota dialog so it doesn’t collide with D4’s 20-row sheet.

### 2. Free caps: **stack**, don’t replace (v1)

Today free already has:

1. 20 entries visible on the sheet (then wipe-all “new sheet”)
2. 35-day retention

Daily quota is a **third** free constraint:

| Cap | Applies when |
|-----|----------------|
| Daily free logs (10 + ad bonuses) | Free only, per local day |
| 20-row sheet full | Free only (unchanged) |
| 35-day purge | Free only (unchanged) |

**Pro / Max:** no daily quota, no ads. That’s the conversion story and matches paywall copy (“Pro lifts the caps”).

### 3. Pending seal, not silent drop

Log auto-seals. When quota is hit, mirror **overspend**:

- Hold the draft in memory
- Show `PremiumDialog`
- On reward success → grant capacity → **retry seal of the same draft**
- On “Come back tomorrow” → dismiss; draft can remain so they can edit/cancel

Never silently no-op `trySeal`.

### 4. Creation counter, not “rows still in Room”

Prefer DataStore:

- `quota_day` = `yyyy-MM-dd`
- `quota_logs_created`
- `quota_bonus_logs`
- `quota_ads_watched`

On day change → reset all.

**Why not Room `countForDate` alone?**  
Delete entry / “start new sheet” wipe would re-open free slots and invite gaming. “Logs **created** today” matches the product wording and stays independent of 35-day data.

### 5. Frequency safety (concrete defaults)

| Constant | Default | Notes |
|----------|---------|--------|
| `FREE_DAILY_LOGS` | 10 | Free only |
| `EXTRA_LOGS_PER_REWARD` | 8 | Per successful reward |
| `MAX_REWARDED_ADS_PER_DAY` | **3** | Cap = +24 bonus logs/day max |
| Master kill switch | `AdsConfig.ENABLED = true` | One-line disable |

When ads/day cap is hit: dialog offers only “Come back tomorrow” (or soft Pro CTA — optional).

### 6. Optional soft Pro CTA (suggested, not required)

Third line in the exhausted dialog (ghost/text, not forced):

> “Or go Pro — unlimited logs, no ads”

Keeps honesty and uses existing paywall. Skip if zero subscription pressure is preferred on this surface.

---

## Suggested improvements over the raw brief

| # | Improvement | Why |
|---|-------------|-----|
| A | **Pro/Max bypass** explicit | Avoid punishing paid users; clear conversion |
| B | **Isolated `ads/` package + interface** | Easy remove; unit-test domain without AdMob |
| C | **`AdsConfig` single source of constants** | Tweaking 10 / 8 / 3 without hunting call sites |
| D | **Creation counter in DataStore** | Offline, anti-delete-farm, day-keyed |
| E | **Pending-seal state** like overspend | Clean auto-seal UX |
| F | **No SDK init in `Application.onCreate`** | Meets non-negotiable; only on “Watch ad” |
| G | **Test ad unit + test App ID in debug** | Safe sideload/dev |
| H | **UMP only when user first taps Watch ad** | Not on cold start |
| I | **Offline / no-fill friendly errors** | Never crash; never grant without reward callback |
| J | **Domain unit tests first** | Pure quota math; matches existing domain-test style |
| K | **Android-only v1** | Expo/iOS don’t ship this APK path |
| L | **Do not change Entry schema / 35-day purge** | Zero retention risk |

**Sideload caveat:** soft-launch APK (not Play) may get weaker AdMob fill. Fine for test IDs; production needs a real AdMob app + (eventually) Play listing. Privacy policy link will eventually be required for UMP/AdMob policy — follow-up, not a block for Phase 1–2.

---

## Architecture (minimal, removable)

```
domain/
  AdsConfig.kt              // ENABLED, FREE_DAILY_LOGS, EXTRA_PER_AD, MAX_ADS_PER_DAY
  DailyLogQuota.kt          // pure: remaining, canLog, grantBonus, rollDayIfNeeded
  DailyLogQuotaTest.kt

data/prefs/
  AppPreferences.kt         // + quota keys / flows / mutators

ads/                        // entire package deletable
  RewardedAdGateway.kt      // interface: ensureReady / show / results
  AdMobRewardedAdGateway.kt // UMP → lazy MobileAds.initialize → load → show
  NoOpRewardedAdGateway.kt  // ENABLED=false or missing Activity
  ConsentHelper.kt          // UMP wrapper (first request only)

di/
  AdsModule.kt              // bind gateway by build/flag

ui/screens/input/
  InputViewModel.kt         // free-tier gate before sealNow; grant + retry
  InputScreen.kt            // PremiumDialog for quota / ad failure

ui/screens/settings/
  SettingsViewModel.kt      // expose remaining / allowance
  SettingsScreen.kt         // read-only “Daily free logs” panel
```

### Seal path contract (non-negotiable)

```
trySeal()
  → validate fields
  → if Free && !quota.canLogToday → emit QuotaBlocked (no network)
  → else existing overspend check
  → sealNow()  // Room only; then increment quota_logs_created
```

### Ads only after explicit button

```
onWatchAd()
  → if offline → friendly message
  → if ads_watched >= MAX → friendly message
  → gateway.ensureConsentAndInit()  // first time only
  → gateway.loadAndShow()
  → onUserEarnedReward → grantBonus() → retry pending seal
```

---

## Phase plan (file-level)

### Phase 1 — Daily quota (fully offline)

| File | Action |
|------|--------|
| `domain/AdsConfig.kt` | **New** — constants + `ENABLED` |
| `domain/DailyLogQuota.kt` | **New** — pure state machine |
| `domain/DailyLogQuotaTest.kt` | **New** — day rollover, free limit, bonus, max ads, Pro unlimited |
| `data/prefs/AppPreferences.kt` | **Edit** — keys + read/write for day / created / bonus / ads |
| `ui/screens/input/InputViewModel.kt` | **Edit** — check quota for Free only; pending quota dialog state; increment on seal |
| `ui/screens/input/InputScreen.kt` | **Edit** — `PremiumDialog`: “Watch a short ad for +8 more logs today” / “Come back tomorrow” |

**Done when:** With airplane mode, free user can seal 10 times; 11th shows dialog; next calendar day resets. Pro user never sees it.

### Phase 2 — Settings

| File | Action |
|------|--------|
| `ui/screens/settings/SettingsViewModel.kt` | **Edit** — observe quota remaining / allowance |
| `ui/screens/settings/SettingsScreen.kt` | **Edit** — section e.g. “DAILY FREE LOGS”: “10 free · X left today · Watch ads for more” |

No ad SDK here — read-only local state.

### Phase 3 — Rewarded AdMob

| File | Action |
|------|--------|
| `gradle/libs.versions.toml` | **Edit** — `play-services-ads` + UMP |
| `app/build.gradle.kts` | **Edit** — deps |
| `AndroidManifest.xml` | **Edit** — `APPLICATION_ID` meta-data (test App ID in debug) |
| `ads/*` + `di/AdsModule.kt` | **New** — isolated gateway |
| `InputViewModel` / `InputScreen` | **Edit** — wire Watch ad → gateway → grant |
| `MainActivity` or Activity provider | **Edit** — only as Activity handle for show/consent (no init on launch) |

- Dev ad unit: `ca-app-pub-3940256099942544/5224354917`
- Grant **only** on `onUserEarnedReward`
- Load failure / offline: Snackbar or dialog body, no crash, no grant

**SDK:** `com.google.android.gms:play-services-ads` (current GMA Android path). “Next-gen” here means modern GMA + UMP, not banners/app-open.

### Phase 4 — Safety & polish

| Item | Approach |
|------|----------|
| UMP | First “Watch ad” only; continue gracefully if form fails (policy-safe fallback per Google samples) |
| Max 3 ads/day | Enforced in `DailyLogQuota` before load |
| Kill switch | `AdsConfig.ENABLED = false` → `NoOpRewardedAdGateway` + no UI offer (or “ads unavailable”) |
| Comments | Package header: “Remove ads/ + AdsModule + prefs keys to strip monetization” |
| ProGuard | Keep AdMob rules when minify on |
| Tests | Domain green; optional fake gateway for VM if cheap |

---

## What we will **not** do in v1

- Banners / interstitials / app-open
- Init AdMob in `NeedsVsWantsApp.onCreate`
- Network inside `sealNow`
- Change Entry / Room schema / 35-day purge logic
- Count “sheets” as the quota unit
- Apply quota to Pro/Max
- iOS / Expo ads
- Preload ads on app launch

---

## Done when

- Daily free quota works completely offline
- User can log freely until the daily limit, then optionally watch one rewarded ad for more logs that day
- Core Log / sealing experience is unchanged for free logs
- Ads code is isolated and only runs on explicit user action
- Every file changed is listed with a reason (at implementation closeout)
- The daily limit and extra-logs amount are easy to tweak (`AdsConfig`)

---

## Risks / open decisions

1. **Stacking free caps** — Keep 20-sheet **and** daily 10, or eventually soften sheet wipe once daily quota exists?  
   - *Recommendation:* stack for v1; revisit sheet wipe later (current wipe-all is harsh).

2. **Dialog after 10th log** — Also offer Pro CTA?  
   - *Recommendation:* yes, one ghost line.

3. **MAX_REWARDED_ADS_PER_DAY = 3** — OK, or stricter (1–2)?

4. **Production AdMob App ID** — Use Google sample App ID until a real AdMob app is created?  
   - *Recommendation:* test IDs until real app + privacy policy exist.

5. **Scope** — Android only?

---

## Approval checklist

Before implementation:

- [ ] Unit = **daily sealed logs** (not sheets)
- [ ] Free only; **Pro/Max unlimited**
- [ ] Defaults **10 / +8 / max 3 ads**
- [ ] DataStore creation counter
- [ ] Optional soft Pro CTA on exhausted dialog
- [ ] Android only

Implementation order after approval: **Phase 1 → 2 → 3 → 4**, domain tests first, then UI, then AdMob behind the interface.

---

## References (in-repo)

- Entitlement free caps: `app/src/main/java/com/needsvswants/app/domain/Entitlement.kt` (`FREE_SHEET_LIMIT = 20`)
- Seal path: `app/src/main/java/com/needsvswants/app/ui/screens/input/InputViewModel.kt`
- Log UI + `PremiumDialog` patterns: `InputScreen.kt`, `ui/theme/Components.kt`
- Prefs: `app/src/main/java/com/needsvswants/app/data/prefs/AppPreferences.kt`
- Paywall free copy: `ui/screens/paywall/PaywallScreen.kt`
- Second Brain: `Projects/Needs vs Wants/` (`Summary`, `Tasks`, `Decisions`)
- Agent gate: `AGENTS.md` (Obsidian + Context7 + Graphify before code)
`)
