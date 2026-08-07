# Plan: Clearer Paywall → PayPal checkout (post–Google sign-in)

## Problem

1. **Sign-in works**, but purchase often feels stuck: user expects a PayPal button; sticky CTA only says **"Start Pro 3-day free trial"**, so trial looks like the only path and PayPal never appears as the payment surface.
2. **Trial is a PayPal plan feature**, not a separate app product. The app should drive **Subscribe with PayPal** (browser approval). Trial (if any) happens on PayPal's page, not as an exclusive in-app mode.
3. **Stale Play Store copy** on the trial timeline ("Play bills…", "Cancel in Google Play") contradicts PayPal.
4. **Post–sign-in handoff is fragile**: pending purchase must always continue into `paypal_create_subscription` → open approval URL; failures show generic "Payment didn't go through" with no retry or reason.

## Goals

- Clearer UX: every paid step names **Google** then **PayPal (browser)**.
- One primary action for Pro and Max: **subscribe / continue with PayPal**, not "trial-only".
- Reliable auto-continue after Google success.
- Actionable errors + **Open PayPal again** when signed in.
- No free-path account change (Google still only after Pro/Max intent).

## Non-goals

- Embedding PayPal JS buttons in Compose (browser approval remains).
- Changing plan prices or PayPal dashboard plan trial settings.
- Play Billing.

## Root cause (code)

| Area | Current | Issue |
|------|---------|--------|
| Pro CTA | `Start Pro 3-day free trial` → `startTrial()` | Sounds trial-only; no PayPal in label |
| Max CTA | `Start Max plan` | No PayPal in label |
| Timeline | "Play bills… / Google Play" | Wrong provider |
| Pending | `ProTrial` / `MaxUpgrade` | Naming implies trial-only for Pro |
| Errors | `BillingResult.Failed` with no message | User can't tell sign-in vs Edge Function vs plan |
| After sign-in | `onSignedInForPurchase` then `runBilling` | Token race / silent fail / no retry CTA |

## Task 1 — ViewModel + billing: subscribe paths, error reasons, retry

**Controller resolutions (pre-flight scan):** clean rename, not alias, for pending intents; `BillingResult.Failed(val reason: String? = null)` optional message field (NOT a parallel StateFlow); include the 150–300ms delay + `ensureFreshAccessToken` before creating the subscription (plan's Risks table mandates it as the token-race mitigation).

**File:** `app/src/main/java/com/needsvswants/app/ui/screens/paywall/PaywallViewModel.kt`

- Rename pending intents:
  - `ProTrial` → `ProSubscribe`
  - `MaxUpgrade` → `MaxSubscribe`
- Replace dual mental model with:
  - `subscribePro()` → pending if signed out, else `billing.purchase(proMonthlyProductId)`.
  - `subscribeMax()` → same with max product id.
- Prefer **`purchase(proMonthlyProductId)`** for Pro so product id is always `P-…` monthly plan (trial product id is the same plan today, but naming stops implying a separate trial SKU).
- After Google success:
  - Keep pending until checkout **starts** successfully (`OpenCheckout`), not clear pending before `runBilling` if that races with composition.
  - `delay(150–300ms)` then `ensureFreshAccessToken` before create subscription (mitigate post-login race).
- Extend result surface:
  - Add `BillingResult.Failed(val reason: String? = null)` (optional message field with default null) and update tests that use `BillingResult.Failed` equality.
- Expose `retryCheckout()` when signed in and last result was Failed/Unavailable for a paid plan.

**File:** `app/src/main/java/com/needsvswants/app/data/billing/BillingController.kt` / `PayPalBillingController.kt`

- On HTTP failure, parse Edge Function JSON `error` via existing `PayPalCheckoutJson.parseErrorMessage` and return `Failed(reason)`.
- On missing `P-` plan id: message "PayPal plans not configured on this build."
- On missing token: "Sign in required."

## Task 2 — Paywall UI: clearer UX (PayPal labels, steps, error + retry)

**File:** `app/src/main/java/com/needsvswants/app/ui/screens/paywall/PaywallScreen.kt`

Sticky CTA labels (when not already entitled) — exact strings:

| Selection | Label |
|-----------|--------|
| Free | Continue free |
| Pro | **Continue with PayPal · Pro** |
| Max | **Continue with PayPal · Max** |
| Signed out + paid selected | Still same label; subcopy: "First Google, then PayPal opens in your browser." |

Footer / helper copy — exact strings:

- Free: unchanged (no account).
- Pro/Max: "3-day free trial is on PayPal if enabled on the plan. You'll approve in the browser. Cancel anytime in PayPal."
- When `needsSignInForPurchase && !isSignedIn`: emphasize **Step 1: Google → Step 2: PayPal**.
- When `OpenCheckout`: keep "Opening PayPal…".
- When `Failed` with reason: show reason + button **Try PayPal again** calling `retryCheckout()` / re-invoke subscribe for selected plan.
- When signed in after success path idle: "Signed in as … · Tap continue to open PayPal."

Google strip copy — exact string:

- "Sign in with Google to start PayPal checkout. Free use never needs an account."

Match the existing PaywallScreen copy conventions (curly quotes where the file uses them, font/typography tokens already used on that screen). Keep all locked behavior: Google sign-in still appears only after Pro/Max intent (D54), free path no account.

## Task 3 — Timeline card (PayPal, not Play)

**File:** `app/src/main/java/com/needsvswants/app/ui/theme/PlanCards.kt` → `TrialTimelineCard`

- Eyebrow: use `TRIAL ON PAYPAL` (softer alternative allowed: `HOW BILLING WORKS`).
- Day 3: "Trial ends. PayPal charges the monthly rate unless you cancel."
- Anytime: "Cancel in your PayPal account / subscription settings."
- Today: "After PayPal approval, Pro/Max unlocks on this device."

Remove every "Play bills… / Google Play" string from the paywall timeline. Acceptance: no "Play bills / Google Play cancel" copy on paywall.

## Task 4 — Tests

**Files:**

- `app/src/test/java/com/needsvswants/app/ui/screens/paywall/PaywallViewModelTest.kt` — update enum names / `startTrial` → `subscribePro`; assert signed-out sets pending; signed-in invokes billing; after pending + sign-in, billing called; Failed reason optional.
- `app/src/test/java/com/needsvswants/app/data/billing/PayPalCheckoutJsonTest.kt` — already has error parse; keep.
- Adjust any `BillingResult.Failed` equality if sealed interface member gains a parameter (use `is BillingResult.Failed` or data class with default null).

Run the full `:app:testDebugUnitTest` suite green at the end.

## Task 5 — Ship release 2.0.2

- Bump **versionName 2.0.2** / **versionCode 10** in `app/build.gradle.kts` (Google + checkout UX fix).
- `:app:assembleRelease`, copy the APK to `website/public/downloads/needs-vs-wants-2.0.2.apk` AND `website/downloads/needs-vs-wants-2.0.2.apk` (site convention keeps both copies byte-identical, cf. D33/D39), update site version strings in both `website/index.html` and `website/public/index.html` (keep `qrcode@1.5.1` pinned; both HTML copies byte-identical; `apply.js`/`check.js` checks pass).
- Deploy Vercel from `website/` + re-alias `needs-vs-wants.vercel.app` (production deploy is explicitly part of this plan; precedent D18/D19/D33/D79/D104).
- Verify DEX still has `paypal_create_subscription` + plan IDs + new Web client id (strings on the APK, e.g. `apkanalyzer` or `unzip -p classes.dex | strings`; previous sessions verified this way).
- Do NOT change the D104 "try-out honesty" marketing copy (Pro/Max stay roadmap pricing on the site; this ship is APK + version strings only).

## Task 6 — Second Brain vault update

- Append the outcome to `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\Tasks.md` (dated 2026-08-08 entry, verified outcomes only).
- Add a short **D107** decision entry to `Decisions.md` (latest existing D# is D106 — verified; next free is D107). Dated entry, agent ID, rationale.
- Update `Summary.md` version/status lines if scope or platform status changed (2.0.2 / versionCode 10, clearer PayPal checkout).
- Vault-write safety (AGENTS.md): vault is outside the file-tool sandbox — write via bash with Git-Bash paths; NEVER open the target in `"w"` mode before the payload is known-good (truncation incident 2026-08-07); write to a temp file then `os.replace(tmp, p)` (atomic); preserve non-UTF-8 bytes by round-tripping `encoding="latin-1"`; prefer ASCII-only inserted text; full-line anchors in `replace()`.

## Flow after fix

```text
Select Pro or Max
    → Sticky: "Continue with PayPal · Pro/Max"
    → If signed out: Google strip (step 1)
    → On Google success: auto create subscription + open browser (step 2)
    → If signed in already: open PayPal immediately
    → On failure: show reason + "Try PayPal again"
    → Return to app → restore entitlement
```

## Risks

| Risk | Mitigation |
|------|------------|
| Sealed `Failed` change breaks tests | Default null message; update asserts to type check |
| Token race after Google | Short delay + ensureFreshAccessToken; retry CTA |
| User still thinks trial-only | CTA + footer never say trial is the only product; trial is PayPal-side |
| Edge Function still errors | Surface API error string so we can debug live |

## Out of scope unless needed in same pass

- Website PayPal JS buttons with `custom_id` (still app-first).
- Changing PayPal plan trial length in dashboard.

## Acceptance criteria

- [ ] Signed-in user taps Pro/Max → browser opens PayPal approval (or clear error + retry).
- [ ] Signed-out user: Google → then PayPal without second guess.
- [ ] No "Play bills / Google Play cancel" copy on paywall.
- [ ] CTAs mention **PayPal**; free path still no account.
- [ ] Unit tests green; **2.0.2** APK on production site.
