# Plan: Clearer Paywall → PayPal checkout (post–Google sign-in)

## Problem

1. **Sign-in works**, but purchase often feels stuck: user expects a PayPal button; sticky CTA only says **“Start Pro 3-day free trial”**, so trial looks like the only path and PayPal never appears as the payment surface.
2. **Trial is a PayPal plan feature**, not a separate app product. The app should drive **Subscribe with PayPal** (browser approval). Trial (if any) happens on PayPal’s page, not as an exclusive in-app mode.
3. **Stale Play Store copy** on the trial timeline (“Play bills…”, “Cancel in Google Play”) contradicts PayPal.
4. **Post–sign-in handoff is fragile**: pending purchase must always continue into `paypal_create_subscription` → open approval URL; failures show generic “Payment didn’t go through” with no retry or reason.

## Goals

- Clearer UX: every paid step names **Google** then **PayPal (browser)**.
- One primary action for Pro and Max: **subscribe / continue with PayPal**, not “trial-only”.
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
| Timeline | “Play bills… / Google Play” | Wrong provider |
| Pending | `ProTrial` / `MaxUpgrade` | Naming implies trial-only for Pro |
| Errors | `BillingResult.Failed` with no message | User can’t tell sign-in vs Edge Function vs plan |
| After sign-in | `onSignedInForPurchase` then `runBilling` | Token race / silent fail / no retry CTA |

## Implementation

### 1. ViewModel: subscribe paths + durable pending + errors

**File:** `PaywallViewModel.kt`

- Rename (or alias) pending intents:
  - `ProTrial` → `ProSubscribe`
  - `MaxUpgrade` → `MaxSubscribe`
- Replace dual mental model with:
  - `subscribePro()` → pending if signed out, else `billing.purchase(proMonthlyProductId)` (or keep `startTrial` as alias that still creates the same PayPal subscription; plan trial stays on PayPal).
  - `subscribeMax()` → same with max product id.
- Prefer **`purchase(proMonthlyProductId)`** for Pro so product id is always `P-…` monthly plan (trial product id is the same plan today, but naming stops implying a separate trial SKU).
- After Google success:
  - Keep pending until checkout **starts** successfully (`OpenCheckout`), not clear pending before `runBilling` if that races with composition.
  - Optionally `delay(150–300ms)` then `ensureFreshAccessToken` before create subscription (mitigate post-login race).
- Extend result surface:
  - Add `BillingResult.Failed(val reason: String? = null)` **or** parallel `lastErrorMessage: StateFlow<String?>` to avoid breaking all `Failed` equality tests—prefer **optional message field** with default null and update tests that use `BillingResult.Failed`.
- Expose `retryCheckout()` when signed in and last result was Failed/Unavailable for a paid plan.

**File:** `BillingController.kt` / `PayPalBillingController.kt`

- On HTTP failure, parse Edge Function JSON `error` via existing `PayPalCheckoutJson.parseErrorMessage` and return `Failed(reason)`.
- On missing `P-` plan id: message like “PayPal plans not configured on this build.”
- On missing token: “Sign in required.”

### 2. Paywall UI: clearer UX

**File:** `PaywallScreen.kt`

Sticky CTA labels (when not already entitled):

| Selection | Label |
|-----------|--------|
| Free | Continue free |
| Pro | **Continue with PayPal · Pro** |
| Max | **Continue with PayPal · Max** |
| Signed out + paid selected | Still same label; subcopy: “First Google, then PayPal opens in your browser.” |

Footer / helper copy:

- Free: unchanged (no account).
- Pro/Max: “3-day free trial is on PayPal if enabled on the plan. You’ll approve in the browser. Cancel anytime in PayPal.”
- When `needsSignInForPurchase && !isSignedIn`: emphasize **Step 1: Google → Step 2: PayPal**.
- When `OpenCheckout`: keep “Opening PayPal…”.
- When `Failed` with reason: show reason + button **Try PayPal again** calling `retryCheckout()` / re-invoke subscribe for selected plan.
- When signed in after success path idle: “Signed in as … · Tap continue to open PayPal.”

Google strip copy:

- “Sign in with Google to start PayPal checkout. Free use never needs an account.”

### 3. Timeline card (PayPal, not Play)

**File:** `PlanCards.kt` → `TrialTimelineCard`

- Eyebrow: keep or soften to `TRIAL ON PAYPAL` / `HOW BILLING WORKS`.
- Day 3: “Trial ends. PayPal charges the monthly rate unless you cancel.”
- Anytime: “Cancel in your PayPal account / subscription settings.”
- Today: “After PayPal approval, Pro/Max unlocks on this device.”

### 4. Tests

**Files:**

- `PaywallViewModelTest.kt` — update enum names / `startTrial` → `subscribePro` if renamed; assert signed-out sets pending; signed-in invokes billing; after pending + sign-in, billing called; Failed reason optional.
- `PayPalCheckoutJsonTest.kt` — already has error parse; keep.
- Adjust any `BillingResult.Failed` equality if sealed class gains a parameter (use `is BillingResult.Failed` or data object with default).

### 5. Ship

- Bump **versionName 2.0.2** / **versionCode 10** (Google + checkout UX fix).
- `assembleRelease`, copy to `website/public/downloads/needs-vs-wants-2.0.2.apk`, update site version strings (keep `qrcode@1.5.1`).
- Deploy Vercel + alias.
- Verify DEX still has `paypal_create_subscription` + plan IDs + new Web client id.
- Second Brain: Tasks + short D107 decision.

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
- [ ] No “Play bills / Google Play cancel” copy on paywall.
- [ ] CTAs mention **PayPal**; free path still no account.
- [ ] Unit tests green; **2.0.2** APK on production site.
