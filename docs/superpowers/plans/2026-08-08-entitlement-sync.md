# Plan: Pro trial feels free after successful PayPal checkout

## Verified diagnosis (evidence-backed, verified against code 2026-08-08)

**Server is fine. Client never keeps Pro.**

Production `public.entitlements` (project `xpwcrloarciomikfudln`) shows the grant exists:
`is_pro=true`, `tier=pro`, `paid_until=2026-08-10 10:00:00+00` (~3-day trial window), `provider=paypal`,
`status=billing.subscription.activated`, `updated_at=2026-08-07 21:49:53+00`. PayPal approval and the
`paypal_webhook` already granted Pro. What the device feels is **local FREE entitlement** driving the UI and caps.

Client sync is fragile (all verified in code):
1. **One-shot in-memory flag** — `PaywallViewModel.awaitingPaypalReturn` is set only when checkout URL opens. Lost on process death, ViewModel clear, or closing the paywall.
2. **Refresh only from the paywall** — `onReturnFromCheckout()` runs only while `PaywallScreen` is composed + RESUMED. Deep link `needsvswants://paypal/return` is declared in the manifest but MainActivity never handles it.
3. **No cold-start re-sync** — signed-in users never pull entitlement on app launch; `NeedsVsWantsApp.onCreate` reads the local snapshot for the purge cutoff but never refreshes. Miss the first refresh → stuck FREE until manual Restore.
4. **Webhook race** — the happy path may refresh *before* the row exists, then show "Account refreshed. If you just paid, wait a few seconds and tap Restore." with no automatic retry.
5. **UI still looks free even if Pro** — Log always renders `N / 20`; Settings never shows "You're on Pro"; Free plan card says "Active on this device" when `!isPro`.

Caps that only unlock when local `hasProAccessAt` is true: Log sheet full at 20 (`InputViewModel.isSheetFull`), 35-day retention purge on launch, Free plan "Active" badge on paywall.

## Goals

1. After a successful trial/subscribe, the device **reliably** becomes Pro without hunting for Restore.
2. Pro state is **visible** in Settings + Log + paywall.
3. No dependency on a single in-memory flag for checkout return.

## Non-goals

- Change PayPal plan pricing / trial length on PayPal dashboard.
- Re-work Max Advisor content.
- Website marketing changes.

## Task 1 — Durable sync core (cold-start + deep link + retries)

**Files:** `app/src/main/java/com/needsvswants/app/NeedsVsWantsApp.kt`, new `data/entitlement/EntitlementSync.kt` (or equivalent), `MainActivity.kt`, `data/prefs/AppPreferences.kt`.

**A. Refresh on app start when signed in** — in `NeedsVsWantsApp.onCreate` (appScope, IO): if a session exists (`authRepository.isSignedIn` / session non-null) and `SupabaseConfig.enabled`: `ensureFreshAccessToken` → `entitlementRepository.refreshFromRemote(token)`. Best-effort; offline keeps the last local snapshot. **Order matters: run the refresh BEFORE the purge-cutoff read** so a Pro user's history is not purged while local state is stale-free.

**B. Deep-link driven refresh** — `MainActivity`: handle `needsvswants://paypal/return` and `needsvswants://paypal/cancel` in `onCreate` / `onNewIntent` (check/adjust `launchMode` so `onNewIntent` actually fires, e.g. `singleTop`). On **return**: set a durable "checkout return pending" flag (DataStore) + kick the sync with retries. On **cancel**: clear the pending flag; no grant expected.

**C. Retry after checkout return** — a retry routine with attempts at ~0s, 2s, 5s, 10s; stop early once `hasProAccessAt` is true; if still free after retries, surface a clear message ("Payment recorded — tap Restore, or wait a moment") instead of silent free.

**D. Persist "awaiting PayPal return"** — move `awaitingPaypalReturn` from ViewModel memory → DataStore (survives process death while the browser is open).

**Tests:** pure retry helper (stop-on-pro / max attempts); `EntitlementJson` case matching the live shape (`is_pro` + `paid_until`, null trial).

## Task 2 — Paywall return path on the durable flag

**Files:** `PaywallViewModel.kt`, `PaywallScreen.kt`.

- Replace the in-memory `awaitingPaypalReturn` + `onReturnFromCheckout()` with the durable flag: after `OpenCheckout` the pending flag is set (persisted); the return deep link (Task 1B) or a paywall-resume check triggers the sync with retries; flag cleared when Pro is confirmed or cancel arrives.
- Keep `restore()` / Restore purchases as the explicit escape hatch.
- Success copy: prefer "Welcome to Pro" only when `isPro`; otherwise "Still unlocking — retrying…" during backoff (instead of the stale "Account refreshed. If you just paid…" being the only state).
- Keep the exactly-once auto-continue (`autoContinued`, D109) and `lastResult == null` gate semantics intact.

**Tests:** VM: OpenCheckout sets the durable pending flag; return path triggers sync; retry no-ops while busy; pending cleared on cancel.

## Task 3 — Settings → Membership section

**Files:** `SettingsScreen.kt` / `SettingsViewModel.kt`.

When signed in, show:
- Plan: Free | **Pro (active)** | Max
- Expiry: "Trial/access until …" from `expiresAtEpochMillis`
- Button: **Refresh membership** (same as restore)
- Free users keep the existing "View Pro & Max plans" row.

**Tests:** SettingsViewModel membership snapshot mapping (pure helper if extracted).

## Task 4 — Log sheet counter respects Pro

**Files:** `InputScreen.kt` / `InputViewModel.kt` (expose `hasProAccess` StateFlow).

- Free: keep `N / 20` and the full-sheet handoff.
- Pro/Max: show `N sealed` or `N · unlimited` (no `/ 20`), never force "Start new sheet" at 20.

**Tests:** `InputViewModel.isSheetFull` false under Pro with 20+ entries.

## Task 5 — Paywall status notes

**Files:** `PaywallScreen.kt`.

- When `isPro`: Free card must not say "Active on this device" (verify existing `if (!isPro)` logic covers it).
- Success copy: "Welcome to Pro" only when `isPro`; otherwise the "Still unlocking — retrying…" state during backoff (coordinate with Task 2).

## Task 6 — Ship

- Bump **versionName 2.0.3** / **versionCode 11** in `app/build.gradle.kts`.
- `:app:testDebugUnitTest` full suite green; `:app:assembleRelease` green; aapt versionCode/versionName; apksigner D86 cert `5fc43fb6…`; not debuggable.
- Copy the APK to `website/public/downloads/needs-vs-wants-2.0.3.apk` AND `website/downloads/needs-vs-wants-2.0.3.apk` (byte-identical); update version touchpoints 2.0.2 → 2.0.3 in both HTML copies (byte-identical); keep `qrcode@1.5.1` pinned (D22); keep all legacy APKs + D104 copy; `node _pad-parts/apply.js` + `check.js` pass.
- Deploy from `website/` (`vercel deploy --prod`) + re-alias `needs-vs-wants.vercel.app` explicitly (D19/D33/D55 alias-drift mode; correct arg order `vercel alias set <deployment> <alias>`).
- Live verify: homepage 200 with 2.0.3, APK 200 + sha256 matches local build.
- Commit on main (conventional style), NOT pushed (user policy).

## Task 7 — Second Brain closeout

- Append Tasks.md progress entry (2026-08-08, dated, verified outcomes).
- Append **D111** to Decisions.md (next free number after D110 — verified): durable entitlement sync (cold-start + deep-link return + retries), membership visible in Settings, Log counter respects Pro unlimited.
- Update Summary.md if platform status changed (versionName 2.0.3).
- Vault-write safety: temp file + `os.replace`, latin-1 round-trip, ASCII-only inserts, full-line anchors.

## Root-cause summary (one paragraph)

PayPal and the Supabase webhook already activated Pro (`is_pro=true`, `paid_until≈2026-08-10`). The Android client only pulls that row on sign-in or a fragile paywall resume/Restore path; it does not re-sync on cold start or deep-link return. Local DataStore stays FREE, so sheet caps and copy still look free. Fix by durable post-checkout sync + cold-start refresh, and surface membership in Settings/Log so Pro is obvious.

## Acceptance criteria

- [ ] Signed-in Pro user cold-starts the app → Pro without touching Restore.
- [ ] PayPal return deep link (`needsvswants://paypal/return`) triggers sync with retries → Pro.
- [ ] Settings shows Pro (active) + access-until date + Refresh membership.
- [ ] Log counter shows `N · unlimited` (no `/ 20`) for Pro/Max.
- [ ] Unit tests green; **2.0.3** APK on production site.
