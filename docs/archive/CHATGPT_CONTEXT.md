> ARCHIVED 2026-08-13 — historical snapshot, do not trust for current state.

# Needs vs Wants — Project Context for ChatGPT

> **Snapshot taken:** 2026-08-08 (verified against the repo, Obsidian Second Brain, and git).
> **Purpose:** Give you (ChatGPT) a complete, current orientation on this project so you can work without re-deriving everything.
> **Source of truth order:** Repo code > these notes > `IMPLEMENTATION_PLAN.md` (which is stale in places and documented as such below).

---

## 1. What the product is

**Needs vs Wants** is a personal **spending trainer**, not a ledger. Every purchase is entered as a single forced binary choice — **Need or Want** — so the user confronts impulse spending in real time instead of at month-end. It is **offline-first**: no accounts required, no cloud phoning home for core use, no analytics.

- **Android** (primary, shipped) — Kotlin + Jetpack Compose + Material 3, Hilt, Room, DataStore.
- **iOS** — native SwiftUI rewrite under `ios-native/` (MVVM + Repository + SwiftData) + a legacy SwiftUI port under `ios/` that's kept as reference only.
- Built for both **PH (₱)** and **USD ($)** display; cost stored as `Long` cents (currency-immune, decision **D2**).

**Brand / theme — "Supermarket premium" (decision D7)** — do not regress to generic or glassy:
- Surface background `#FAFAF7` warm off-white · Card `#FFFFFF` · Raised `#F3F1EA`
- Primary / Want accent crimson `#C8102E` · Secondary / Need accent green `#0B6B3A` · Gold trim `#E8A92A`
- Inter (Inter Tight) body/money · Playfair Display SC **only** for screen titles
- **Need = green, Want = red.** Light color scheme, dark status-bar icons.
- Stripped all glassmorphism (decision **D93**): surfaces are now **ruled costing-ledger paper** (`Paper.kt`), paper page-turn for tab switches (D92), paper-ink wave press feedback (D98/D101). Must NOT look AI-generated; real typography, real palette, real texture.

**Core hard constraints:**
- 30 days of history retention hard cap (free tier auto-purges; Pro keeps everything)
- 20 items max per input screen, then "Start new sheet"
- No accounts/cloud/analytics for the free product path
- Cost stored as `Long` cents (D2); display layer formats per currency

---

## 2. Platform / version state (verified)

- **Current Android release:** `versionCode 13`, `versionName 2.0.5`, applicationId `com.needsvswants.app`, `minSdk 24`, `targetSdk 34`.
- **Dual payment provider shipped in 2.0.5:** a payment-method selector on Pro & Max —
  - **PayPal:** Pro = 3-day free trial then ₱199/mo auto-charge; Max = ₱399/mo (no trial)
  - **PayMongo:** one-time ₱199 (Pro) / ₱399 (Max), 30 days, manual renewal, **no auto-charge**; supports GCash + card + PayMaya + GrabPay + QR PH (the PH-market-native path)
- **Build flavors:** `full` (default, = release candidate) and `plain` (Free-only QA APK, `com.needsvswants.app.plain`, label "Needs vs Wants (Free Test)", strips paywall/membership/Advisor).
- **Git:** branch `main`, remote `origin` → `https://github.com/yuzuruu29/needs-vs-wants.git`. **There are ~84 unpushed commits on top of `origin/main`** (2.0.x release + entitlement-sync + coordination work). Push requires explicit user approval (the "keep local" policy was changed back to "pushed" on 2026-08-08, but the latest wave is still unpushed).
- **Deployed website (Vercel):** https://needs-vs-wants.vercel.app — soft-launch page + downloadable APK. Deploy from `website/` only (never repo root); re-alias the canonical URL after each deploy (it drifts).

---

## 3. How billing / entitlement works (the most intricate part — read carefully)

**Model:** Free tier = 20 entries per sheet + 30-day retention. Pro/Max = unlimited sheets + no retention cutoff. Tiers: `FREE`, `PRO`, `MAX`. Max adds the AI Financial Advisor (hard-locked without Max).

**Providers (both live):**
- `PayPalBillingController` — subscription path (`paypal_create_subscription` edge function → approval URL → deep link `needsvswants://paypal/return|cancel`).
- `PayMongoBillingController` — one-time Hosted Checkout (`paymongo_create_checkout` → checkout URL → `needsvswants://paymongo/return|cancel`).
- Both sit behind a `CheckoutProvider` seam in DI. `PayMongo` is the default bind for provider-agnostic restore. Payment provider is read at run time; deferred sign-in/retry uses the current selection.

**Durable entitlement sync (D111–D113 — the "Pro feels free" fix):** The webhook always granted Pro server-side, but the device often stayed FREE because the client only refreshed when re-entering the paywall. Now:
- Cold-start remote refresh **before** the retention purge-cutoff read (so a stale-local Pro user is never purged as free), bounded to 30s, fully exception-guarded.
- A **durable `paypal_return_pending` / `paymongo_return_pending` DataStore flag** is set on the checkout-return deep link; survives process death and re-runs on next cold start. Cleared on cancel, on confirmed Pro, and on sign-out (D113).
- Retried restore at 0s/2s/5s/10s with stop-on-Pro. Settings has a Membership panel (plan + expiry + "Refresh membership"); Log counter shows unlimited for Pro/Max.

**Backend (Supabase, project ref `xpwcrloarciomikfudln`, org ACEU):**
- Edge Functions: `get_entitlement`, `paypal_create_subscription`, `paypal_webhook`, `paymongo_create_checkout`, `paymongo_webhook`, `google_play_verify`, `apple_verify`.
- `entitlements` table (user-scoped RLS, service_role-only grant RPC), `payment_events` idempotency ledger keyed on `pay_xxx` (SELECT-then-INSERT, exactly-once to prevent double-grant), `_shared/paymongo.ts`.
- **Webhook gotcha (verified):** `paymongo_webhook` HMAC-verifies the **raw body** and the grant maps the envelope — must handle both `data.data.attributes` and `data.attributes.data` shapes.
- Supabase project URL/anon key live in gitignored `local.properties`; secrets (PayPal client secret, PayMongo secret key/webhook secret) live in `Deno.env` on Supabase. **The PayMongo deployment to production is still pending**: `supabase db push`, set `PAYMONGO_SECRET_KEY`/`PAYMONGO_WEBHOOK_SECRET`, deploy both functions, create a test-mode dashboard webhook for `checkout_session.payment.paid`.

**Known billing gotchas to preserve:**
- Exactly-once auto-continue (D108/D109): a still-pending intent after a failed POST must never re-fire the create call (no server-side dedupe → double billing). Gated by `autoContinued` flag + `lastResult == null`.
- Deep-link returns must be handled on BOTH `needsvswants://paypal/*` and `needsvswants://paymongo/*`, and on `plain` builds the scheme is `needsvswantsplain` so a side-by-side install never steals the prod return URLs.

---

## 4. Android codebase (core surfaces)

Location: `app/src/main/java/com/needsvswants/app/` — data/db (Room `EntryDao`, `AppDatabase`), data/model, data/prefs (DataStore `AppPreferences`), di (Hilt modules), domain, ui/screens/{input,summary,history,settings}, ui/theme, ui/navigation.

**Five screens / tabs** (order locked: Home → Log → Advisor → History → Settings):
1. **Summary** — hand-rolled Canvas ring/donut chart (Need/Want split) + "Log a Purchase" CTA. Period selector **Day / Week / All / Month** (Month period added 2026-08-08). Daily budget meter on Day.
2. **Log (Input)** — 5-column ledger grid (Date, Time, Item, Cost, Type); auto date/time on row completion; 20-row cap with "Start new sheet"; optional daily budget set/update/clear lives here; daily free-log quota gate (5 logs/day free, unused logs carry to the next consecutive active day) with "Come back tomorrow" + soft Pro CTA. Row delete = **long-press** (D97).
3. **Advisor (Max only)** — AI spending advisor grounded in Google NotebookLM economic-study notebooks, RAG with inline footnote citations, offline asset index. Hard-locked without Max.
4. **History** — full ledger grouped by day, day totals + Need/Want split chips, CSV export (cents), long-press delete.
5. **Settings** — currency; text size (Default/Large/Extra large); appearance (Market light/dark/System/High contrast); Feedback (sound/vibration/reduced motion); daily free logs panel; Membership panel; data wipe; About.

**UI/UX system to preserve:** paper ledger surfaces (`Paper.kt`), paper page-turn tab transitions (`PaperPageFlip`), paper-ink wave as the app-wide default press indication (`InkWave.kt`), FluidGadget replacement `FloatingGeminiOrb` is a matte ledger ring, `Motion.*` tokens with reduced-motion collapse, Kenney CC0 SFX (SoundPool), type system = Playfair for titles only + Inter body/money.

**Tests:** `app/src/test/` — 26 test classes, ~253 tests green on the `full` flavor (`:app:testFullDebugUnitTest`). The `plain` flavor intentionally fails paid-grant tests (tier disabled by design). No instrumented (`androidTest`) tests yet.

---

## 5. iOS codebase

- **`ios-native/`** is the real, current iOS app — 29 Swift sources + 5 XCTest files. MVVM + Repository + SwiftData. All screens + 5th Advisor tab parity. TDD-driven.
- **`ios/`** is the old SwiftUI port — **reference only**, do not treat as current.
- **CI:** `.github/workflows/ios-native.yml` — macos-15, Xcode 16, XcodeGen → `xcodebuild build -sdk iphonesimulator` (compile-only; no simulator runtime on CI).
- **CI:** `.github/workflows/android.yml` exists (added D64/D81).
- iOS is **not buildable on this Windows machine** (needs Xcode).

---

## 6. Website / marketing

- **Soft-launch marketing (D104):** the site markets a **free Android APK try-out** only. Pro/Max are shown as planned prices (Pro ₱249 / Max ₱499) but labeled **"Not for sale yet"** — paid checkout is driven from inside the app, not the site.
- Canonical HTML: `website/public/index.html`, kept **byte-identical** with `website/index.html` via `_pad-parts/apply.js`.
- Locked (do not touch): QR CDN pinned `qrcode@1.5.1` (D22), CTA animates the red `.cta-panel` only (D23), notepad flip is a native CSS 3D two-face turn, **NO** `page-flip` library (D32).
- Site checks: `node _pad-parts/check.js` ALL CHECKS PASSED is required before deploy.
- Assets: the site + downloadable APK (`downloads/needs-vs-wants-2.0.5.apk` in BOTH `website/downloads/` and `website/public/downloads/`), favicon (vector-traced brand mark), PDF how-to promos + marketing videos live under `video/` (Remotion project) and `video/out/`.

---

## 7. Project memory / process

**The Second Brain rule is STRICT and non-negotiable:** before ANY work, read the Obsidian vault:
- `C:\Obsidian Vault\Second Brain\Memory\00 Memory Layer\Memory Index.md`
- `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\{Summary, Tasks, Decisions}.md`
- Consult the graphify knowledge graph for architecture/blast-radius changes.

After significant work, **write findings back** to the vault. Vault writes must go through Git-Bash (safe atomic pattern: write a temp file, `os.replace`, prefer ASCII; the vault has legacy non-UTF-8 bytes — never open in `"w"` before the payload is known-good).

**Decisions are tracked as D-notes** in `Decisions.md`, currently through ~D116 (the latest entries are dated 2026-08-08). Next free decision number when you log a new one: find the latest `## D##` and go one higher.

**Coordination (new):** the repo has a `.claude/` coordination layer — a cross-session mailbox + heartbeat/status view + cron setup (`feat(coordination)` commits on top of main, unpushed). `.claude/hooks/` holds a `vault-gate.js` hook; `.claude/.vault-gate` records last gate run (2026-08-08).

**Release/signing (security, D86):** the ORIGINAL `release.keystore` was public on GitHub and is BURNED. Current signing key is `.local/release.keystore` (gitignored), RSA-4096, cert SHA-256 `5fc43fb6…`, passwords in `~/.gradle/gradle.properties` + `.local/keystore.properties`. Never commit the keystore or passwords. If lost, the app cannot be updated in place — back up `.local/` off-machine.

---

## 8. Known gaps / what's next

- **PayMongo production deploy is pending** (the backend munitions are written and tested locally but not deployed to Supabase prod, and the webhook isn't set up).
- **~84 commits unpushed** to `origin/main` (needs user approval to push; existing sessions were told to pull before editing).
- **No Android CI workflow until recently** — `android.yml` exists (D64/D81).
- **`IMPLEMENTATION_PLAN.md` is partly stale** — §1 describes the overridden Friendster dark theme, §8 pre-rewrite nav, §9 test plan unimplemented. Trust `Decisions.md` + code over it.
- ProGuard rules are minimal (auto-generated file); a final security/lint pass on the newest payment code is worthwhile.
- Manual first-time-user QA on a real device is still listed as open.
- Ads: **AdMob/UMP removed from the build entirely** (2026-08-09). The `ads/` package, `AdsModule`, `AdsConfig`, and rewarded-quota fields (`bonusLogs`/`adsWatched`) are deleted. Free tier = 5 logs/day with streak carry-forward (`FreeQuotaConfig` + `DailyLogQuota`). No ad SDK, no ad UI, no manifest App ID.

---

## 9. Recent commit history (top of `main`)

```
54c2a027 feat(coordination): heartbeat + status view, cron setup docs
a7608b45 feat(coordination): cross-session mailbox protocol
5e5eb82d release: refresh 2.0.5 APK (Month period included — settled tree rebuild)
3ee7b690 feat(summary): Month summary period
036ed439 docs: corrected coordination note for Month-period session
f00f1708 release: refresh 2.0.5 APK (final-review P1+P2: MembershipDesk copy, fallback)
4d9757a9 fix(paywall): final-review P1+P2 — MembershipDesk no auto-charge claims removed, fallback prefers PayMongo
d61223ef release: 2.0.5 (versionCode 13) — dual payment provider, site + APK
e8bda234 feat(settings): MembershipDesk UI (parallel session's file)
03d658a2 release: 2.0.5 (versionCode 13) — dual payment provider
d4e6421d feat(flavor): add plain Free-only test APK (side-by-side experience flavor)
869e01e5 fix(paywall): review notes — neutral sticky subcopy, site 5th PayPal claim
d2de6bdf fix(site): payment-provider claims — PayPal or PayMongo
b2a7e47a feat(paywall): payment-method selector — PayPal trial + PayMongo one-time
4677cdc5 feat(paywall): payment-provider selection and routing
fb53759b docs(plan): dual payment provider — PayPal trial + PayMongo one-time selector
```

---

*End of context.* If you need the raw `Summary.md`, `Tasks.md`, or `Decisions.md`, they live in the Obsidian vault and the repo `AGENTS.md` documents how to read/write them safely (Git-Bash paths, atomic replace, ASCII-only inserts).
