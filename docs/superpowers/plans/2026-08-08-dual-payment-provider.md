# Plan: Dual payment provider — PayPal (trial) + PayMongo (one-time) with selector

## Product decision (user, 2026-08-08)

The paywall offers a **payment-method selector** on both paid plans:

| Plan | PayPal path | PayMongo path |
|------|-------------|---------------|
| **Pro** | 3-day free trial (PayPal plan feature), then ₱199/mo auto-charge; cancel in PayPal | One-time ₱199 → 30 days; manual renewal, no auto-charge |
| **Max** | ₱399/mo subscription, **no trial** | One-time ₱399 → 30 days |

Both providers write the same Supabase entitlements row (tier pro/max, paid_until = max(now, existing) + 30d); the durable sync machinery (D111-D113) is provider-agnostic and unchanged.

## Verified feasibility (controller evidence, 2026-08-08)

- `PayPalBillingController.kt` still in the tree (was R8-stripped as unreferenced — restore wiring, not the file).
- `PayMongoBillingController.kt` is the current default binding (`EntitlementModule.kt:42`).
- Manifest has BOTH deep-link hosts (`needsvswants://paypal/*` and `needsvswants://paymongo/*`) routed to the shared `PayPalReturnHandler` (name is legacy).
- `paypal_create_subscription` (v10, instrumented error surfacing) + `paypal_webhook` (v14) ACTIVE; PayPal live secrets set (D110 fix). `paymongo_create_checkout` + `paymongo_webhook` ACTIVE.
- Known tradeoff (D114 rationale): PayPal auto-subscription needs the annual merchant-agreement renewal and serves card/QR only in PH — the user re-introduces it knowingly for the trial path.

## Task 1 — VM + DI: provider selection and routing

**Files:** `PaywallViewModel.kt`, `di/EntitlementModule.kt`, new small domain seam (e.g. `CheckoutProvider` / `PaymentProvider` enum).

- `enum class PaymentProvider { PAYPAL, PAYMONGO }`.
- `CheckoutProvider` (@Inject, wraps the two concrete controllers — both already @Singleton @Inject): `controllerFor(provider)`, `payPalAvailable` (from `PayPalBillingController.isPayPalAvailable`), `payMongoAvailable` (config.enabled + plan ids).
- `PaywallViewModel`: `selectedProvider: StateFlow<PaymentProvider>` (default PAYPAL), `selectProvider(provider)`; `subscribePro()`/`subscribeMax()` route by provider:
  - PAYPAL + Pro → `payPal.startTrial(proPlanId)` (trial is configured on the PayPal plan)
  - PAYPAL + Max → `payPal.purchase(maxProductId)` (no trial)
  - PAYMONGO + Pro → `payMongo.purchase(monthlyProductId)`
  - PAYMONGO + Max → `payMongo.purchase(maxProductId)`
- Pending intent semantics unchanged: provider is read at run time (post-Google / retry uses the CURRENT provider); provider switch while pending re-asserts like plan switch (controller resolution — decide: re-assert pending on provider change, mirroring selectPlan).
- `restore()` keeps using the default `BillingController` binding (PayMongo) — restore is provider-agnostic (refreshFromRemote). SettingsViewModel unchanged.
- Keep: exactly-once `autoContinued`, `lastResult == null` gate, busy guards, retryCheckout.
- Tests: routing per provider×plan (4 cases, fake controllers record calls + product ids), availability gating, provider switch mid-pending re-asserts, restore unchanged.

## Task 2 — Paywall UI: selector, CTAs, footer, timeline variants

**Files:** `PaywallScreen.kt`, `PlanCards.kt` (TrialTimelineCard variants).

- Selector (visible when Pro or Max selected): two options — PayPal card ("3-day free trial" tag on Pro; "₱399/mo" on Max) vs PayMongo card ("One-time · no auto-charge"). Hide PayPal option when `payPalAvailable` false; hide PayMongo when unavailable (selector collapses to the single available).
- CTA labels: Pro → `Continue with PayPal · Pro (3-day trial)` / `Continue with PayMongo · Pro`; Max → `Continue with PayPal · Max` / `Continue with PayMongo · Max` (per selected provider).
- Footer: PayPal → "3-day free trial on PayPal if enabled on the plan, then ₱199/mo. Cancel anytime in PayPal." (Pro) / "₱399/mo via PayPal. Cancel anytime in PayPal." (Max); PayMongo → the existing "One-time payment via GCash, card, PayMaya, GrabPay, or QR PH. You pay each month when ready — access ends on your expiry date. No auto-charge."
- TrialTimelineCard: provider-aware — PayPal shows the trial timeline (Today: "After PayPal approval, Pro unlocks on this device."; Day 3: "Trial ends. PayPal charges the monthly rate unless you cancel."; Anytime: "Cancel in your PayPal account / subscription settings."; eyebrow "TRIAL ON PAYPAL" for Pro; for Max the "no trial" variant: eyebrow "BILLED MONTHLY", rows "Today: Max unlocks after PayPal approval." / "Monthly: PayPal charges ₱399 each month until you cancel." / "Anytime: Cancel in PayPal."); PayMongo keeps "WHEN YOU NEED IT" (existing).
- Google strip + "Signed in as … · Tap continue…" copy stay provider-neutral.
- Keep all D108/D109 gate semantics; selected plan + provider both drive the CTA.

## Task 3 — Site copy correction (stale "PayPal-only" claims)

**Files:** `website/index.html`, `website/public/index.html` (byte-identical mirrors), `_pad-parts/check.js` locks if needed.

- Replace the 4 stale PayPal references with both-provider wording, e.g. "Pro and Max via PayPal or PayMongo in the app." Keep D104 framing ("Not for sale yet / Coming soon" for the site's own checkout) — only the provider-name claims get corrected.
- `node _pad-parts/apply.js` + `check.js` ALL CHECKS PASSED; `qrcode@1.5.1` pinned.

## Task 4 — Ship 2.0.5

- `versionCode 13` / `versionName "2.0.5"`; full suite green (228 baseline + new); `assembleRelease`; aapt + apksigner D86 `5fc43fb6…`; not debuggable.
- DEX verify: BOTH `paypal_create_subscription` AND `paymongo_create_checkout` present (the restoration proof), both deep-link hosts, Google client id.
- Site: APK to both download dirs; version touchpoints 2.0.4 → 2.0.5; legacy APKs kept; deploy + re-alias (D19/D33/D55 alias-drift mode, `vercel alias set <deployment> <alias>`); live verify (homepage 200 + 2.0.5, APK sha match).
- Commit conventional style; NOT pushed unless the user says so.

## Task 5 — Vault closeout

- Tasks.md entry (dated, verified outcomes); **D116** decision (dual provider: PayPal trial path + PayMongo one-time with selector; Max no trial; restores the PayPal backend alongside PayMongo); Summary versionName 2.0.5.
- Vault-write safety: temp file + os.replace, latin-1 round-trip, ASCII-only, full-line anchors.

## Acceptance criteria

- [ ] Pro paywall offers PayPal (3-day trial) and PayMongo (one-time) — selector visible and switchable.
- [ ] Max offers both, no trial copy anywhere on the Max path.
- [ ] CTA/footer/timeline copy matches the selected provider.
- [ ] PayPal checkout works end-to-end (deep link → approval; webhook grants; sync picks it up).
- [ ] Unit tests green; **2.0.5** APK on the site; site copy no longer claims PayPal-only.
