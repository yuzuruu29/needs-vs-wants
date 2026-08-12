-- Needs vs Wants - generic soft rate-limit counter table
--
-- One row per counted attempt. Keys are scope-prefixed buckets:
--   notify:<sha256(ip) 32-hex>   notify_signup, max 5 per hour per IP
--   checkout:<user_id>           paymongo_create_checkout +
--                                paypal_create_subscription, max 10 per hour
--
-- Functions COUNT rows for their key newer than the window start and refuse
-- with 429 when the max is reached, then INSERT their own attempt row. This
-- is SOFT abuse control: functions fail open on counter errors so a broken
-- counter can never block a paying customer.
--
-- Rows older than the 1-hour window are dead weight. Volume is tiny at this
-- scale; an optional periodic cleanup is documented in supabase/README.md.

create table public.rate_limit_events (
  id          bigint      generated always as identity primary key,
  key         text        not null,
  created_at  timestamptz not null default now()
);

comment on table public.rate_limit_events is
  'Sliding-window rate-limit attempts (scope-prefixed keys). Service-role only; safe to purge rows older than a day.';

create index rate_limit_events_key_created_idx
  on public.rate_limit_events (key, created_at desc);

alter table public.rate_limit_events enable row level security;

-- Intentionally NO policies for authenticated / anon, and no table-level
-- privileges either. Only Edge Functions (service role) touch this table.
revoke all on public.rate_limit_events from anon, authenticated;
