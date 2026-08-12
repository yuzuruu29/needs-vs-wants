# Needs vs Wants - Supabase Backend (Pro Subscription)

Task 2: backend scaffolding for the Pro entitlement. Provides the SQL
migration, four Edge Functions, and configuration for the Needs vs Wants
app's subscription gating.

Deploy from this directory only (`C:\Needs vs Wants\supabase`), never the
monorepo root.

## Architecture

- `migrations/20260803000000_create_entitlements.sql` - creates the
  `public.entitlements` table, RLS policies, and two SECURITY DEFINER RPCs
  (`my_entitlement()`, `apply_entitlement_grant(...)`). Follow-up migrations
  add the tier column (20260808), the `payment_events` ledger (20260809), and
  RLS/DML hardening (20260810, 20260811).
- `migrations/20260813000000_create_launch_notify.sql` - `launch_notify`
  email-capture table (service-role only) for `notify_signup`.
- `migrations/20260813000001_create_rate_limit_events.sql` - generic
  `rate_limit_events` counter table (service-role only) backing the soft
  per-IP / per-user rate limits.
- `functions/_shared/` - shared pure helpers (**no external imports**,
  unit-testable with `deno test`):
  - `entitlements.ts` - expiry logic, PayPal webhook mapping, grant state
    builders.
  - `paymongo.ts` - PayMongo signature verification, amounts, paid_until
    stacking, webhook payload mapping.
  - `paypal_custom_id.ts` - HMAC-signed subscription `custom_id`
    mint/verify + webhook grant-acceptance policy.
  - `notify.ts` - notify_signup email/honeypot validation.
  - `rate_limit.ts` - sliding-window rate-limit keys/thresholds/IP hashing.
  - `http.ts` - CORS + JSON response helpers.
- `functions/get_entitlement/` - reads the caller's entitlement (client app
  calls this with the user's access token).
- `functions/paypal_create_subscription/` - creates a PayPal subscription for
  the verified caller with a SIGNED `custom_id` and returns the approval URL.
- `functions/paypal_webhook/` - verifies PayPal webhook signatures
  (mandatory, locked decision), verifies the signed `custom_id`, and upserts
  grants (idempotent via the `payment_events` ledger).
- `functions/paymongo_create_checkout/` - creates a PayMongo Hosted Checkout
  Session (one-time, manual-renewal Pro/Max) and returns the checkout URL.
- `functions/paymongo_webhook/` - verifies the PayMongo `Paymongo-Signature`
  header over the raw body and grants entitlement (idempotent via the
  `payment_events` ledger).
- `functions/notify_signup/` - website email capture (honeypot + per-IP rate
  limit; service-role writes to `launch_notify`).
- `functions/google_play_verify/` - verifies Play purchases/subscriptions on
  the Play Developer API and grants Pro.
- `functions/apple_verify/` - verifies App Store receipts via verifyReceipt
  and grants Pro.
- `tools/` - manual, read-only Deno scripts (never deployed):
  - `verify_paypal_plans.ts` - prints PayPal plan pricing/cycles and
    PASS/FAILs them against the expected PHP 49/99 monthly (and 490/990
    annual) prices. See `docs/PAYPAL_PRICING_CHECKLIST.md`.
  - `reconcile.ts` - cross-checks provider dashboard CSV exports against
    `payment_events` + `entitlements` and lists mismatches.
- `import_map.json` - pins `@supabase/supabase-js@2.45.4`.
- `config.toml` - local CLI defaults (ports 54321-54324).

## Data model (mirrors the Android domain model)

`entitlements` (PK `user_id uuid references auth.users(id) on delete cascade`):

| column            | type        | meaning                                       |
|-------------------|-------------|-----------------------------------------------|
| user_id           | uuid        | primary key, FK to auth.users                 |
| is_pro            | bool        | true while entitled (checked server-side)     |
| trial_started_at  | timestamptz | 3-day trial window start                      |
| trial_ends_at     | timestamptz | trial window end (= start + 3 days)           |
| paid_until        | timestamptz | paid access end (null = lifetime)             |
| provider          | text        | paypal / google / apple / supabase            |
| source            | text        | subscription / one_time / referral / ...      |
| status            | text        | provider status string                        |
| updated_at        | timestamptz | server-managed timestamp                      |

Pro is active when: `is_pro = true AND (paid_until IS NULL OR paid_until >
now()) AND (trial_ends_at IS NULL OR trial_ends_at > now())`. **Server time is
authoritative**; the app never decides entitlement itself.

RLS: users may SELECT and UPDATE only their own row; no INSERT/DELETE policy
(grants flow through the service role or SECURITY DEFINER RPCs only).

## Edge Function contracts

### get_entitlement
- Auth: `Authorization: Bearer <supabase access token>` (JWT verified by the
  gateway; `verify_jwt = true`).
- Returns `{ success, data: { is_pro, plan, trial_started_at, trial_ends_at,
  paid_until, provider, source, status, server_time } }`.

### paypal_create_subscription

- Auth: `Authorization: Bearer <supabase access token>` (JWT verified by the
  gateway; `verify_jwt = true`). Also decodes `sub` and re-validates via
  `supabase.auth.getUser()`.
- Body: `{ "tier": "pro" | "max", "period": "monthly" | "annual" }` or
  `{ "plan_id": "P-..." }`. Plan ids come from the `PAYPAL_PLAN_*` secrets.
- Mints a SIGNED subscription `custom_id`
  (`v1.<user_id>.<issued_at>.<hmac-sha256 hex>` over `user_id.issued_at`
  with `PAYPAL_CUSTOM_ID_SECRET`) so the webhook can authenticate the buyer,
  not just PayPal. Fails closed (500 "Server not configured") when the secret
  is missing - set it BEFORE deploying.
- Soft rate limit: max 10 subscription creations per user per hour
  (`rate_limit_events`; 429 when exceeded; fails open on counter errors).
- Returns `{ success, data: { subscription_id, status, tier, period, plan_id,
  approval_url } }`.

### paypal_webhook
- Deployed with `--no-verify-jwt` (PayPal is not a Supabase client).
- Requires `PAYPAL_TRANSMISSION_*` headers; signature verified against
  `POST /v1/notifications/verify-webhook-signature` before any write.
- Accepts `BILLING.SUBSCRIPTION.ACTIVATED|CREATED|REACTIVATED|REVISED` (paid
  or trial grant), `PAYMENT.SUCCEEDED` (renewal extension),
  `CANCELLED|SUSPENDED|EXPIRED` (status flag only; access continues until the
  paid period ends). Other events are acked and ignored.
- `custom_id` trust (replaces the old "client-controlled custom_id" caveat):
  the user id comes from the subscription's `custom_id`, which
  `paypal_create_subscription` signs with `PAYPAL_CUSTOM_ID_SECRET`. The
  webhook verifies before granting:
  - fresh valid signature (< 24h): grant, any event type;
  - valid but older than 24h: grant only when the subscription id is already
    linked to the same user (a `payment_events` row, or the user's
    entitlement row with `provider = 'paypal'`) - renewals replay the same
    `custom_id` for the life of the subscription;
  - tampered signature: never grant;
  - LEGACY raw-uuid `custom_id` (subscriptions created before the signing
    cutover): accepted only for renew/extend/status events of users already
    linked to PayPal, NEVER for first-time `ACTIVATED`/`CREATED` grants.
  - Residual risk: a subscription created before the cutover but approved
    (ACTIVATED) after it carries a legacy custom_id and is refused; the
    refusal is logged (`legacy_initial_rejected`) and `tools/reconcile.ts`
    surfaces the paid-but-not-entitled user for a manual grant. Expected
    volume: ~zero (approval normally happens within minutes of creation).
- Idempotency: processed events are recorded in `payment_events` keyed on the
  PayPal event id (`provider = 'paypal'`, `checkout_session_id` = PayPal
  subscription id `I-…`, `amount_centavos = 0` - amounts live in PayPal
  reports). Replayed deliveries ack `{ success, already_applied: true }`
  without re-granting. Unlike the PayMongo webhook (ledger-first because its
  grants STACK days), PayPal grants are absolute (`paid_until =
  next_billing_time`), so the grant is applied first and the ledger row
  written after: a transient grant failure stays retryable and a rare
  double-apply writes identical values.
- Grant upsert keyed on `user_id` (the VERIFIED id, never the raw token).

### paymongo_create_checkout

- Auth: `Authorization: Bearer <supabase access token>` (JWT verified by the
  gateway; `verify_jwt = true`). Also decodes `sub` and re-validates via
  `supabase.auth.getUser()` (same pattern as `paypal_create_subscription`).
- Body: `{ "tier": "pro" | "max", "period": "monthly" | "annual" }` (period
  defaults to `monthly`). Amounts are **server-authoritative** only: monthly
  Pro `4900` / Max `9900` centavos; annual Pro `49000` / Max `99000` centavos.
  Client-supplied amounts are ignored.
- Returns `{ success, data: { checkout_url, checkout_session_id, tier, period,
  amount_centavos } }`. `checkout_url` is the PayMongo Hosted Checkout page to
  open in a browser; `success_url` / `cancel_url` default to
  `needsvswants://paymongo/return` / `needsvswants://paymongo/cancel`.
- Fail-closed: missing `PAYMONGO_SECRET_KEY` → 500 "Server not configured";
  PayMongo non-2xx → actionable error from `data.message` / `errors[].detail`,
  502. Unexpected exceptions return a generic "Internal error" (500) - details
  are logged server-side only, never sent to clients (same in
  `paypal_create_subscription`).
- Soft rate limit: max 10 checkout creations per user per hour
  (`rate_limit_events`; 429 when exceeded; fails open on counter errors).

### paymongo_webhook

- Deployed with `--no-verify-jwt` (PayMongo is not a Supabase client).
- Auth gate: HMAC-SHA256 signature verification of the `Paymongo-Signature`
  header against the **raw request body** using `PAYMONGO_WEBHOOK_SECRET`.
  Rejects 401 on failure.
- Accepts the `checkout_session.payment.paid` event. Unknown/malformed events
  are acked (`{ success, ignored: true }`) so PayMongo stops retrying.
- Idempotency: the `payment_events` ledger is keyed on the PayMongo payment id
  (`pay_xxx`). The webhook SELECTs the ledger first; if the payment was already
  recorded it acks `{ success, already_applied: true }` WITHOUT re-granting
  (this is the guard against webhook retries double-granting +30 days). A
  unique violation on a racing insert is treated the same way.
- Grant (one-time, manual renewal):
  - `is_pro = true`, `tier = <final tier>`,
    `paid_until = max(now, existing.paid_until) + grant days` (stacks when
    renewing early). Grant days come from the checkout `metadata.period`:
    monthly = 30, annual = 365.
  - `provider = 'paymongo'`, `source = 'checkout_session'`, `status = 'paid'`,
    `trial_started_at = null`, `trial_ends_at = null`.
  - Tier rule: paying Max always upgrades to max. Paying Pro while Max is
    still active keeps max tier and extends `paid_until` (no downgrade).
- User id / tier / period come from `metadata.user_id` / `metadata.tier` /
  `metadata.period` set at checkout-creation time from the verified JWT —
  never from client-only fields.

### notify_signup

- Website email capture for the soft-launch "get notified" form. Deployed
  with `--no-verify-jwt` (visitors have no Supabase session); CORS allows
  only `https://needs-vs-wants.vercel.app` (OPTIONS preflight handled).
- Contract: `POST` JSON `{ "email": string, "website": string }` where
  `website` is a HONEYPOT that must be empty; optional `"source"` tag
  (defaults to `"website"`). Responses:
  - `200 {"ok":true}` - stored, or already on the list (duplicate emails are
    idempotent; email is normalized trim+lowercase before the UNIQUE check);
  - `400 {"ok":false,"error":...}` - invalid email or honeypot filled;
  - `429 {"ok":false,"error":...}` - per-IP rate limit;
  - `405 {"ok":false,"error":...}` - non-POST.
- Abuse controls: email shape validation, honeypot, and a soft per-IP limit
  of 5/hour keyed on a truncated SHA-256 of the first `x-forwarded-for` hop
  (raw IPs are never stored). The limiter fails OPEN on counter errors.
- Storage: `launch_notify` (service-role only, RLS with no client policies).

### google_play_verify
- Auth: user access token (JWT). Body:
  `{ package_name, product_id, purchase_token, kind: "subscription"|"one_time" }`.
- Mints a service-account RS256 JWT (from `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON`),
  exchanges for an OAuth token, calls the Play Developer API. Only valid
  `paymentState` (received/trial), un-cancelled (`cancelReason` absent), and
  un-expired subscriptions grant Pro.
- A one-time product grants **lifetime** Pro (`paid_until = NULL`), which is
  the single intentional exception to the "never NULL paid_until for a paid
  grant" rule.
- `purchase_token` is the natural idempotency key.

### apple_verify
- Auth: user access token (JWT). Body: `{ receipt_data }`.
- Calls `verifyReceipt` with `APPLE_SHARED_SECRET`; sandbox (21007) retry
  against the sandbox endpoint. The receipt's `bundle_id` must match
  `APPLE_BUNDLE_ID`, and the receipt must contain an auto-renewable
  subscription transaction with a future `expires_date`. There is no
  broad 1-year fallback for non-subscription receipts.
- Idempotent per user.

## Environment variables (set with `supabase secrets set`)

| var                                   | required | used by             | notes                                    |
|---------------------------------------|----------|---------------------|------------------------------------------|
| SUPABASE_URL                          | yes      | all                 | auto-set in hosted env                   |
| SUPABASE_ANON_KEY                     | yes      | get_entitlement     | auto-set in hosted env                   |
| SUPABASE_SERVICE_ROLE_KEY             | yes      | webhooks/verify     | auto-set in hosted env (never in client) |
| PAYPAL_ENVIRONMENT                    | yes      | paypal_*            | `live` or `sandbox`                      |
| PAYPAL_CLIENT_ID                      | yes      | paypal_*            | REST API app credential                  |
| PAYPAL_CLIENT_SECRET                  | yes      | paypal_*            | REST API app credential                  |
| PAYPAL_WEBHOOK_ID                     | yes      | paypal_webhook      | from the PayPal dashboard                |
| PAYPAL_CUSTOM_ID_SECRET               | yes      | paypal_*            | HMAC key for signed custom_id (e.g. `openssl rand -hex 32`); set BEFORE deploying both paypal functions |
| PAYPAL_PLAN_PRO                       | yes*     | create + webhook    | Live/Sandbox plan id `P-…` for Pro (monthly) |
| PAYPAL_PLAN_MAX                       | yes*     | create + webhook    | Live/Sandbox plan id `P-…` for Max (monthly) |
| PAYPAL_PLAN_PRO_ANNUAL                | no       | create + webhook    | plan id `P-…` for Pro annual (yearly cycle)  |
| PAYPAL_PLAN_MAX_ANNUAL                | no       | create + webhook    | plan id `P-…` for Max annual (yearly cycle)  |
| PAYPAL_RETURN_URL                     | no       | create_subscription | default `needsvswants://paypal/return`   |
| PAYPAL_CANCEL_URL                     | no       | create_subscription | default `needsvswants://paypal/cancel`   |
| PAYMONGO_SECRET_KEY                   | yes*     | paymongo_create_checkout | PayMongo secret key `sk_test_…` / `sk_live_…` |
| PAYMONGO_WEBHOOK_SECRET               | yes*     | paymongo_webhook    | PayMongo webhook signing secret `whsk_…` |
| PAYMONGO_SUCCESS_URL                  | no       | paymongo_create_checkout | default `needsvswants://paymongo/return` |
| PAYMONGO_CANCEL_URL                   | no       | paymongo_create_checkout | default `needsvswants://paymongo/cancel` |
| PAYMONGO_PAYMENT_METHODS              | no       | paymongo_create_checkout | optional CSV override, e.g. `gcash,card,paymaya,grab_pay,qrph` |
| GOOGLE_PLAY_SERVICE_ACCOUNT_JSON      | yes      | google_play_verify  | full service-account JSON                |
| APPLE_SHARED_SECRET                   | yes      | apple_verify        | App Store shared secret                  |
| APPLE_BUNDLE_ID                       | yes      | apple_verify        | app bundle id the receipt must match     |

Example:

```bash
supabase link --project-ref YOUR_PROJECT_REF
supabase db push
supabase secrets set PAYPAL_ENVIRONMENT=sandbox
supabase secrets set PAYPAL_CLIENT_ID=...
supabase secrets set PAYPAL_CLIENT_SECRET=...
supabase secrets set PAYPAL_WEBHOOK_ID=...
supabase secrets set PAYPAL_CUSTOM_ID_SECRET=...   # e.g. openssl rand -hex 32
supabase secrets set GOOGLE_PLAY_SERVICE_ACCOUNT_JSON='{"type":"service_account",...}'
supabase secrets set APPLE_SHARED_SECRET=...
supabase secrets set APPLE_BUNDLE_ID=com.example.needsvswants
supabase secrets set PAYMONGO_SECRET_KEY=sk_test_...
supabase secrets set PAYMONGO_WEBHOOK_SECRET=whsk_test_...
supabase functions deploy get_entitlement
supabase functions deploy google_play_verify
supabase functions deploy apple_verify
supabase functions deploy paypal_create_subscription
supabase functions deploy paypal_webhook --no-verify-jwt
supabase functions deploy paymongo_create_checkout
supabase functions deploy paymongo_webhook --no-verify-jwt
supabase functions deploy notify_signup --no-verify-jwt
```

**Signed custom_id cutover (2026-08-13):** set `PAYPAL_CUSTOM_ID_SECRET`
first, then deploy `paypal_create_subscription` and `paypal_webhook`
TOGETHER. Deploying only one side breaks new-subscription grants (old webhook
would treat the signed token as a user id; new webhook without the secret
rejects signed tokens).

## PayMongo dashboard webhook setup (manual)

In the [PayMongo dashboard](https://dashboard.paymongo.com) → Developers →
Webhooks, add a webhook for **test mode** with:

- URL: `https://xpwcrloarciomikfudln.supabase.co/functions/v1/paymongo_webhook`
- Events: `checkout_session.payment.paid`
- The signing secret shown there is `PAYMONGO_WEBHOOK_SECRET`.

The `PAYMONGO_SECRET_KEY` used by `paymongo_create_checkout` must share the
same account/mode as the webhook (test ↔ test, live ↔ live). Every checkout is
a **one-time** Hosted Checkout Session; there is no auto-subscription, renewal
is user-initiated.

Never hardcode or log secrets. Keep `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` and all
credentials out of git.

## Local development

```bash
# full unit-test suite: pure helpers in functions/_shared/ + tools/ (no network)
deno test supabase

# typecheck all functions (fetches pinned types from esm.sh)
deno check --allow-import=esm.sh supabase/functions/_shared/entitlements.ts \
  supabase/functions/_shared/http.ts \
  supabase/functions/get_entitlement/index.ts \
  supabase/functions/paypal_create_subscription/index.ts \
  supabase/functions/paypal_webhook/index.ts \
  supabase/functions/paymongo_create_checkout/index.ts \
  supabase/functions/paymongo_webhook/index.ts \
  supabase/functions/notify_signup/index.ts \
  supabase/functions/google_play_verify/index.ts \
  supabase/functions/apple_verify/index.ts

# start the local stack (migrations + functions + studio)
supabase start
```

## Manual tools (read-only, never deployed)

```bash
# Verify PayPal plan prices (see docs/PAYPAL_PRICING_CHECKLIST.md).
# Env: PAYPAL_CLIENT_ID, PAYPAL_CLIENT_SECRET, PAYPAL_ENV=live|sandbox (default live)
deno run --allow-net --allow-env supabase/tools/verify_paypal_plans.ts \
  --pro-annual=P-XXXX --max-annual=P-YYYY

# Reconcile provider dashboard CSV exports against payment_events + entitlements.
# Env: SUPABASE_URL, SUPABASE_SERVICE_ROLE_KEY (service role; never in clients)
deno run --allow-net --allow-env --allow-read supabase/tools/reconcile.ts \
  --paymongo=paymongo_payments.csv --paypal=paypal_subscriptions.csv
```

`reconcile.ts` lists: payments recorded by the provider but missing from
`payment_events` (missed webhooks), ledger rows whose user shows no grant,
and active paid entitlements without any payment record. PayPal grants made
BEFORE the 2026-08-13 ledger cutover legitimately show up in the last bucket
until their first post-cutover renewal.

`rate_limit_events` housekeeping (optional): rows older than the 1-hour
window are dead weight; purge occasionally with
`delete from rate_limit_events where created_at < now() - interval '2 days';`.

## Client integration notes (Android)

- Call `get_entitlement` with the user's access token; treat `plan == "pro"`
  as unlocked, else free limits (20 sheets / 35-day retention).
- On purchase, send the provider's token/receipt to the matching verify
  function; the server confirms before the app upgrades the local state.
