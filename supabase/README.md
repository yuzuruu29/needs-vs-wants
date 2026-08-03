# Needs vs Wants - Supabase Backend (Pro Subscription)

Task 2: backend scaffolding for the Pro entitlement. Provides the SQL
migration, four Edge Functions, and configuration for the Needs vs Wants
app's subscription gating.

Deploy from this directory only (`C:\Needs vs Wants\supabase`), never the
monorepo root.

## Architecture

- `migrations/20260803000000_create_entitlements.sql` - creates the
  `public.entitlements` table, RLS policies, and two SECURITY DEFINER RPCs
  (`my_entitlement()`, `apply_entitlement_grant(...)`).
- `functions/_shared/` - shared pure helpers:
  - `entitlements.ts` - expiry logic, PayPal webhook mapping, grant state
    builders. **No external imports** (unit-testable).
  - `http.ts` - CORS + JSON response helpers.
- `functions/get_entitlement/` - reads the caller's entitlement (client app
  calls this with the user's access token).
- `functions/paypal_webhook/` - verifies PayPal webhook signatures
  (mandatory, locked decision) and upserts grants.
- `functions/google_play_verify/` - verifies Play purchases/subscriptions on
  the Play Developer API and grants Pro.
- `functions/apple_verify/` - verifies App Store receipts via verifyReceipt
  and grants Pro.
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

### paypal_webhook
- Deployed with `--no-verify-jwt` (PayPal is not a Supabase client).
- Requires `PAYPAL_TRANSMISSION_*` headers; signature verified against
  `POST /v1/notifications/verify-webhook-signature` before any write.
- Accepts `BILLING.SUBSCRIPTION.ACTIVATED|CREATED|REACTIVATED|REVISED` (paid
  grant), `CANCELLED|SUSPENDED|EXPIRED` (status flag only; access continues
  until the paid period ends). Other events are acked and ignored.
- User id comes from the subscription's `custom_id` / `user_id` field that
  the client embeds at checkout - never trusted from the caller.
- Idempotent upsert keyed on `user_id`.
- Security caveat: `custom_id` is client-controlled at checkout. Production
  must verify `resource.user_id == auth.uid()` when the subscription links a
  Supabase user id, or sign the `custom_id` value. The current scaffold trusts
  the webhook signature (which authenticates PayPal, not the buyer).

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
| PAYPAL_ENVIRONMENT                    | yes      | paypal_webhook      | `live` or `sandbox`                      |
| PAYPAL_CLIENT_ID                      | yes      | paypal_webhook      | REST API app credential                  |
| PAYPAL_CLIENT_SECRET                  | yes      | paypal_webhook      | REST API app credential                  |
| PAYPAL_WEBHOOK_ID                     | yes      | paypal_webhook      | from the PayPal dashboard                |
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
supabase secrets set GOOGLE_PLAY_SERVICE_ACCOUNT_JSON='{"type":"service_account",...}'
supabase secrets set APPLE_SHARED_SECRET=...
supabase secrets set APPLE_BUNDLE_ID=com.example.needsvswants
supabase functions deploy get_entitlement
supabase functions deploy google_play_verify
supabase functions deploy apple_verify
supabase functions deploy paypal_webhook --no-verify-jwt
```

Never hardcode or log secrets. Keep `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` and all
credentials out of git.

## Local development

```bash
# unit tests for the pure entitlement helpers (no network needed)
deno test supabase/functions/_shared/entitlements.test.ts

# typecheck all functions (fetches pinned types from esm.sh)
deno check --allow-import=esm.sh supabase/functions/_shared/entitlements.ts \
  supabase/functions/_shared/http.ts \
  supabase/functions/get_entitlement/index.ts \
  supabase/functions/paypal_webhook/index.ts \
  supabase/functions/google_play_verify/index.ts \
  supabase/functions/apple_verify/index.ts

# start the local stack (migrations + functions + studio)
supabase start
```

## Client integration notes (Android)

- Call `get_entitlement` with the user's access token; treat `plan == "pro"`
  as unlocked, else free limits (20 sheets / 35-day retention).
- On purchase, send the provider's token/receipt to the matching verify
  function; the server confirms before the app upgrades the local state.
