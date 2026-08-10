# Antigravity Handoff — Paywall + PayPal Return Debugging

**Priority:** P0 — production blockers reported by the user on version 2.0.9 (`3bba6ecb`).
**Date:** 2026-08-10
**Pre-code gate (D25/AGENTS.md):** Complete Obsidian + Context7 + Graphify before touching any code.

---

## Issue 1 — Paywall shows infinite loading skeleton

**Symptom:** When the paywall opens (soft-launch cold start or Go Pro/Max tap), the plan cards (Free/Pro/Max) never appear — only shimmer skeleton cards show indefinitely.

**Prime suspect:** `PaywallViewModel.loading` introduced in D133 (`cf7a9b03`), commit `HANDOFF_ANTIGRAVITY_PAYWALL_BILLING_DEBUG.md:331`.

### Root-cause hypothesis (HIGH confidence)

`PaywallViewModel.init`:
```kotlin
repository.entitlement.drop(1).first()
_loading.value = false
```

`EntitlementRepository.entitlement` is a **cold flow**:
```kotlin
// EntitlementRepository.kt:37
combine(local.entitlement, local.entitlementSyncedAtMillis) { snapshot, syncedAt ->
    trustedLocalEntitlement(snapshot, syncedAt, System.currentTimeMillis())
}
```

Both sources are `dataStore.data.map{...}` — DataStore flows emit the **current value once** on subscription and only re-emit on actual writes. So:

1. `drop(1).first()` subscribes → combine emits current snapshot (emission #1)
2. `drop(1)` discards it
3. `first()` waits for emission #2 → **never arrives** (no DataStore write → no re-emit)
4. `loading` stays `true` forever → `PaywallSkeletonCard` shows indefinitely

**Contrast with SummaryViewModel/HistoryViewModel** (which work): those use `stats`/`entries` which are `stateIn(StateFlow)` backed by Room. Room flows emit the initial query result on collect → `drop(1).first()` sees the second emission → loading flips false correctly. The PaywallViewModel uses a **raw cold Flow** not a `stateIn` — that's the critical difference.

### Fix direction

Remove the `drop(1).first()` loading pattern from PaywallViewModel. Options:
1. **Delete the loading flag entirely** — paywall renders from local DataStore (fast); the skeleton is a brief flash at best, not worth the risk.
2. **Use `entitlement` directly as a StateFlow** — if `PaywallViewModel` observes `entitlement` as a `stateIn` (which `isPro`/`hasMaxAccess` already derive from), the `first()` approach would work. But this adds complexity for marginal UX benefit.
3. **Guard the first() with a timeout** — `withTimeoutOrNull(3000) { ... } ?: _loading.value = false` — but this is a bandaid, not a fix.

**Recommendation:** Delete the loading flag (option 1). Paywall data is locally sourced; a 0-200ms flash is invisible. This also means the `PaywallSkeletonCard` composable and the `paywallLoading` state in `PaywallScreen` become dead code — clean those up too.

### Files to change

| File | Change |
|------|--------|
| `app/src/main/java/com/needsvswants/app/ui/screens/paywall/PaywallViewModel.kt` | Remove `_loading`, `loading`, and the `init` block with `drop(1).first()` |
| `app/src/main/java/com/needsvswants/app/ui/screens/paywall/PaywallScreen.kt` | Remove `paywallLoading` state + the `if (paywallLoading) { PaywallSkeletonCard ... } else {` branch — show plan cards unconditionally |

### Verification

- `testFullDebugUnitTest` must stay 277/277 (or slightly fewer if any PaywallViewModelTest tested the loading state)
- `assembleDebug` green
- Manual: open paywall → plan cards render immediately with no skeleton delay

---

## Issue 2 — PayPal approval → return to merchant shows errors (no auto deep-link)

**Symptom:** After approving payment on PayPal in the browser, "Return to merchant" redirects to a page that errors out. The user had to manually reconnect to the app tab to continue.

### Root-cause analysis

The PayPal `return_url` is configured as a **custom URI scheme**:

```typescript
// supabase/functions/paypal_create_subscription/index.ts:140-141
const returnUrl = Deno.env.get("PAYPAL_RETURN_URL") ??
    "needsvswants://paypal/return";
```

When PayPal completes approval and redirects the browser to `needsvswants://paypal/return`:
- Browsers **cannot resolve custom URI schemes** like `needsvswants://` from an HTTPS context
- The browser shows an error page (e.g., "This site can't be reached" or "Page not found")
- The app's `MainActivity.handleCheckoutDeepLink` is never triggered because the browser never launched the app — it just shows the broken redirect

This is the standard PayPal custom-scheme pitfall: the approval flow happens entirely in the browser, and custom schemes don't work from there without a web intermediary.

### Fix direction

Replace the raw custom scheme with a **web-based redirect page** hosted at a real HTTPS URL:

1. **Host a redirect page** at `https://needs-vs-wants.vercel.app/paypal-return` (or `paypal-return-200.html` to avoid conflicts with apply.js)
2. That page executes a JavaScript `location.href = "needsvswants://paypal/return"` (or uses an `intent://` scheme for Android)
3. PayPal product's `return_url` is updated to the HTTPS URL in the Supabase Edge Function
4. `cancel_url` follows the same pattern (`needsvswants://paypal/cancel` → HTTPS redirect page → deep link)

**Also update the `cancel_url`** the same way — same issue applies.

### Edge function to change

| File | Change |
|------|--------|
| `supabase/functions/paypal_create_subscription/index.ts` | `PAYPAL_RETURN_URL` env var default → `https://needs-vs-wants.vercel.app/paypal-return` |

### New file to create

| File | Content |
|------|---------|
| `website/public/paypal-return.html` | Minimal page that reads the query params PayPal adds (token, payer_id, etc.), redirects via `location.href = "needsvswants://paypal/return?" + originalQuery`, plus a fallback link "Tap here to open the app" if JS doesn't run. Mirror the same page as `paypal-cancel.html` for the cancel path. |

### Verification

- Update the Edge Function and redeploy
- Trigger a PayPal checkout: approve → "Return to merchant" should open the HTTPS redirect page → page auto-redirects via `location.href` into the app → `MainActivity.handleCheckoutDeepLink` fires → `PayPalReturnHandler.onCheckoutReturned()` → entitlement refresh
- Manual fallback: if auto-redirect fails (e.g., iOS Safari blocks custom scheme), the page shows a tappable link
- `cancel_url` path: tap "Cancel" on PayPal → HTTPS cancel page → redirect into app → `onCheckoutCancelled()`
- `testFullDebugUnitTest` green + `assembleDebug` green

---

## Testing instructions

1. **Full pre-code gate:** Obsidian Second Brain (`Projects/Needs vs Wants/`), Context7 (Supabase docs, Android billing), Graphify (blast radius on billing/paywall/edge functions).
2. **Issue 1:** `./gradlew :app:assembleFullDebug` → install on emulator → open paywall → confirm plan cards appear immediately (no skeleton).
3. **Issue 2:** Requires the Supabase Edge Function to be redeployed. If secrets are needed (`PAYPAL_RETURN_URL` env var), ask the user for the Supabase CLI auth token. Otherwise, create the redirect page first and note the Edge Function update as a pending deploy.
4. **Regression guard:** Run `./gradlew :app:testFullDebugUnitTest` — ensure 277/277 (or adjusted) still pass. The `PaywallViewModelTest` should be updated to remove any loading-related assertions.

---

## Constraints

- **No breakage of locked billing flow:** D105-D114 (PayPal checkout), D116 (dual provider), D86 (release keystore) — do not alter the subscription creation logic, only the return/cancel URLs and the loading flag.
- **APK signing:** If a new APK is built, it must use D86 cert (`5fc43fb6...`), not the burned original key.
- **Plain flavor:** `BuildConfig.PLAIN_FREE` skips billing entirely — loading flag removal has no effect on the plain build.

---

## User's screenshot

The user attached a screenshot (2026-08-10 14:34) of the error. This model cannot display images — if you need the exact error text or page shown, ask the user for it. The error is likely one of:
- PayPal error page ("Something went wrong" / "Return to merchant failed")
- Blank page at `needsvswants://paypal/return` (scheme not handled)
- The "Payment recorded — tap Restore, or wait a moment" state stuck with no progress (entitlement sync failed)
