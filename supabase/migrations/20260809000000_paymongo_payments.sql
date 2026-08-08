-- Needs vs Wants - PayMongo one-time payment ledger (idempotency)
--
-- Manual-renewal Pro (₱199/mo) / Max (₱399/mo) checkout via PayMongo Hosted
-- Checkout Sessions. Each checkout is a ONE-TIME session; renewal is
-- user-initiated. There is no auto-subscription.
--
-- This table is the idempotency guard: the paymongo_webhook inserts a row
-- keyed on the PayMongo payment id (pay_xxx) BEFORE granting entitlement, so a
-- webhook retry for the same payment can never double-grant +30 days.
--
-- RLS is enabled with NO authenticated policies: the ledger is written and
-- read only by Edge Functions using the service-role key (which bypasses RLS).
-- Clients see nothing.

create table public.payment_events (
  id                    text        primary key,   -- PayMongo payment id pay_xxx
  user_id               uuid        not null references auth.users (id),
  tier                  text        not null check (tier in ('pro','max')),
  amount_centavos       int         not null,
  currency              text        not null default 'PHP',
  provider              text        not null default 'paymongo',
  checkout_session_id   text,
  status                text        not null,
  raw_reference         text,
  created_at            timestamptz not null default now()
);

comment on table public.payment_events is
  'One-time payment ledger for PayMongo checkout sessions. Idempotency key = payment id (pay_xxx).';

create index if not exists payment_events_user_created_idx
  on public.payment_events (user_id, created_at desc);

alter table public.payment_events enable row level security;

-- Intentionally NO policies for authenticated / anon. The ledger is written
-- and read exclusively by the service role (Edge Functions). RLS stays on with
-- no policies so clients see nothing.