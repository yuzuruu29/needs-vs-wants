-- Needs vs Wants - launch_notify email capture (notify_signup Edge Function)
--
-- The soft-launch website's "get notified" form previously discarded emails
-- (posted to "#"). notify_signup now stores them here. Dedupe is the UNIQUE
-- email constraint: the function normalizes (trim + lowercase) before insert
-- and treats a unique violation as success, so re-submitting is idempotent.
--
-- RLS is enabled with NO client policies: the table is written and read only
-- by Edge Functions using the service-role key (which bypasses RLS). The
-- notify_signup function itself is deployed with --no-verify-jwt because the
-- website caller has no Supabase session; abuse control lives in the function
-- (honeypot, email validation, per-IP rate limit via rate_limit_events).

create table public.launch_notify (
  id          uuid        primary key default gen_random_uuid(),
  email       text        not null unique,
  created_at  timestamptz not null default now(),
  source      text
);

comment on table public.launch_notify is
  'Soft-launch notify-me email list. Written only by the notify_signup Edge Function (service role).';
comment on column public.launch_notify.email is
  'Normalized (trimmed, lowercased) email. UNIQUE = idempotent dedupe.';
comment on column public.launch_notify.source is
  'Optional client tag for where the signup came from (defaults to "website").';

alter table public.launch_notify enable row level security;

-- Intentionally NO policies for authenticated / anon, and no table-level
-- privileges either (defense in depth, mirrors payment_events posture).
revoke all on public.launch_notify from anon, authenticated;
