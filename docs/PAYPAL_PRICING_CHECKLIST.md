# PayPal Pricing Checklist (owner-run)

Closes the **D148 caveat**: after the price cut to PHP 49 (Pro) / 99 (Max)
monthly, PayMongo charges the new prices (server-authoritative), but **PayPal
charges whatever the dashboard plans say** — and those prices could not be
verified from this machine (the Supabase CLI masks secret values). Annual
PayPal plans (PHP 490 / 990) may not exist yet; until they do, the app's
PayPal annual path returns "Annual PayPal plan not configured on this build."

Everything below runs from `C:\Needs vs Wants` unless noted. Nothing here was
executed by the agent — each step is yours to run.

## Current state (2026-08-13)

| Plan | ID | Expected price | Status |
|------|----|----------------|--------|
| Pro monthly (3-day trial) | `P-701099249D7315939NJ3BQHQ` | PHP 49 / MONTH | **UNVERIFIED** |
| Max monthly | `P-2GK5612954300654GNJ3BSBQ` | PHP 99 / MONTH | **UNVERIFIED** |
| Pro annual | `P-10G97539L7699934CNJ6NFPA` (created 2026-08-13) | PHP 490 / YEAR | created — verify with the script (Step 4) |
| Max annual | `P-10K54734NX585203ANJ6NGBQ` (created 2026-08-13) | PHP 990 / YEAR | created — verify with the script (Step 4) |

## Step 1 — Verify the monthly plan prices

Option A, script (recommended — uses the read-only Billing Plans API):

```powershell
$env:PAYPAL_CLIENT_ID = "<live REST app client id>"
$env:PAYPAL_CLIENT_SECRET = "<live REST app secret>"
$env:PAYPAL_ENV = "live"
deno run --allow-net --allow-env supabase/tools/verify_paypal_plans.ts
```

It prints each plan's name/status/billing cycles/pricing and PASS/FAIL against
PHP 49/99 MONTH. Option B, dashboard: paypal.com → Pay & Get Paid →
Subscriptions → Subscription plans → open each plan → check the regular-cycle
price.

## Step 2 — If a monthly price is still the old rate (PHP 199/399)

Two ways to fix it; pick ONE per plan:

- **(a) Update pricing on the existing plan** (dashboard "Update pricing", or
  API `POST /v1/billing/plans/{id}/update-pricing-schemes`).
  ⚠ Per PayPal, a fixed-price change applies to **existing AND future
  subscriptions** (charges within 10 days of the change are unaffected).
  Since the price went *down*, existing subscribers would move to the cheaper
  rate — customer-friendly, but it changes the "existing subscribers keep old
  rates" stance.
- **(b) Create NEW plans at PHP 49/99** and swap the ids everywhere:
  `supabase secrets set PAYPAL_PLAN_PRO=... PAYPAL_PLAN_MAX=...` (run in
  `supabase/`), update `PRO_MONTHLY_PRODUCT_ID` / `PRO_TRIAL_PRODUCT_ID` /
  `PRO_MAX_MONTHLY_PRODUCT_ID` in `local.properties`, rebuild + release the
  APK. Existing subscribers stay on the old plans at their old rate.

Re-run Step 1 afterwards until both monthly plans PASS.

## Step 3 — Create the annual plans

Dashboard → Subscription plans → Create plan (reuse the existing Pro/Max
products):

- **Pro annual**: fixed price **PHP 490**, billing cycle **every 1 YEAR**,
  infinite cycles. Trial is your call (monthly Pro has a 3-day trial; the app
  copy for annual assumes plain "Billed yearly").
- **Max annual**: fixed price **PHP 990**, every 1 YEAR, infinite cycles, no
  trial.
- Activate both plans, note their `P-…` ids.

## Step 4 — Verify the annual plans

```powershell
deno run --allow-net --allow-env supabase/tools/verify_paypal_plans.ts `
  --pro-annual=P-XXXXXXXX --max-annual=P-YYYYYYYY
```

Expect PASS: PHP 490 / 990 on a YEAR interval, status ACTIVE.

## Step 5 — Set the Supabase secrets

```powershell
cd supabase
supabase secrets set PAYPAL_PLAN_PRO_ANNUAL=P-XXXXXXXX
supabase secrets set PAYPAL_PLAN_MAX_ANNUAL=P-YYYYYYYY
```

(Project ref `xpwcrloarciomikfudln`; also set `PAYPAL_CUSTOM_ID_SECRET` if not
done yet — see `supabase/README.md`, the redeploy in Step 7 requires it.)

## Step 6 — Fill local.properties + rebuild

In `local.properties` (gitignored):

```
PRO_ANNUAL_PRODUCT_ID=P-XXXXXXXX
PRO_MAX_ANNUAL_PRODUCT_ID=P-YYYYYYYY
```

The annual PayPal path in the app needs a rebuilt APK with these ids;
PayMongo annual (one-time PHP 490/990) works without an app change.

## Step 7 — Redeploy the PayPal edge functions

```powershell
cd supabase
supabase functions deploy paypal_create_subscription
supabase functions deploy paypal_webhook --no-verify-jwt
```

⚠ Deploy both together, and only after `PAYPAL_CUSTOM_ID_SECRET` is set —
the current code signs/verifies the subscription `custom_id`
(see `supabase/README.md` → paypal_webhook).

## Step 8 — Sandbox test

1. Create sandbox plans mirroring Step 3 in the sandbox dashboard.
2. `$env:PAYPAL_ENV = "sandbox"` and re-run the verify script with the
   sandbox ids (`--skip-monthly --pro-annual=… --max-annual=…`).
3. Point a debug build / sandbox secrets at the sandbox plans, subscribe with
   a sandbox buyer, approve, and confirm the entitlement lands
   (`get_entitlement` returns the tier with a ~1-year `paid_until`).

## Notes

- **Existing subscribers keep their old rates** unless you choose Step 2(a),
  which migrates them to the new (lower) price.
- The verify script is read-only; it never creates or edits plans.
- Keep client id/secret out of files and shell history where possible; they
  are already Supabase secrets (`PAYPAL_CLIENT_ID` / `PAYPAL_CLIENT_SECRET`).
