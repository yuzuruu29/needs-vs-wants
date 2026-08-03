-- Needs vs Wants - Pro entitlement data model
-- Task 2: Supabase migrations and backend scaffolding
--
-- Table + RLS + helper functions for the Pro subscription gate.
-- Grant flows run from Edge Functions with the service-role key (which
-- bypasses RLS) or through the SECURITY DEFINER apply_entitlement_grant()
-- RPC. Regular users may only SELECT/UPDATE their own row.
-- Server time is authoritative: Pro is always computed from DB timestamps.

-- ---------------------------------------------------------------------------
-- 1. Entitlements table
-- ---------------------------------------------------------------------------

create table if not exists public.entitlements (
  user_id          uuid primary key references auth.users (id) on delete cascade,
  is_pro           boolean     not null default false,
  trial_started_at timestamptz null,
  trial_ends_at    timestamptz null,
  paid_until       timestamptz null,
  provider         text        null,
  source           text        null,
  status           text        null,
  updated_at       timestamptz not null default now()
);

comment on table public.entitlements is
  'Per-user Pro subscription/trial state. Grants written by Edge Functions (service role).';

create index if not exists entitlements_updated_at_idx
  on public.entitlements (updated_at desc);

-- ---------------------------------------------------------------------------
-- 2. Row Level Security
-- ---------------------------------------------------------------------------

alter table public.entitlements enable row level security;

drop policy if exists "entitlements_select_own" on public.entitlements;
create policy "entitlements_select_own"
  on public.entitlements
  for select
  to authenticated
  using (auth.uid() = user_id);

drop policy if exists "entitlements_update_own" on public.entitlements;
create policy "entitlements_update_own"
  on public.entitlements
  for update
  to authenticated
  using (auth.uid() = user_id)
  with check (auth.uid() = user_id);

-- There is intentionally NO insert/delete policy for authenticated users.
-- Inserts/updates for grant flows come from Edge Functions using the
-- service-role key (bypasses RLS) or from SECURITY DEFINER functions.

-- ---------------------------------------------------------------------------
-- 3. Helper: is_entitlement_active(entitlements)
-- ---------------------------------------------------------------------------
-- Returns true when the row grants Pro AND neither the trial nor the paid
-- window has expired. A NULL trial_ends_at / paid_until on a Pro row means
-- lifetime access. Uses now() so it is STABLE, not immutable.

create or replace function public.is_entitlement_active(row_param public.entitlements)
returns boolean
language sql
stable
parallel safe
set search_path = ''
as $$
  select (
    row_param.is_pro = true
    and (row_param.trial_ends_at is null or row_param.trial_ends_at > now())
    and (row_param.paid_until is null or row_param.paid_until > now())
  );
$$;

-- ---------------------------------------------------------------------------
-- 4. RPC: my_entitlement()
-- ---------------------------------------------------------------------------
-- SECURITY DEFINER so the read Edge Function gets the row plus an
-- authoritative is_active boolean computed server-side. Returns a free-user
-- row when no entitlements row exists (free users have no row).

create or replace function public.my_entitlement()
returns table (
  is_pro           boolean,
  trial_started_at timestamptz,
  trial_ends_at    timestamptz,
  paid_until       timestamptz,
  provider         text,
  source           text,
  status           text,
  is_active        boolean
)
language plpgsql
security definer
set search_path = public
as $$
declare
  e public.entitlements;
begin
  select * into e
  from public.entitlements
  where user_id = auth.uid();

  if not found then
    return query
      select false,
             null::timestamptz, null::timestamptz, null::timestamptz,
             null::text, null::text, null::text,
             false::boolean;
    return;
  end if;

  return query
    select e.is_pro,
           e.trial_started_at,
           e.trial_ends_at,
           e.paid_until,
           e.provider,
           e.source,
           e.status,
           public.is_entitlement_active(e) as is_active;
end;
$$;

revoke execute on function public.my_entitlement() from public, anon;
grant execute on function public.my_entitlement() to authenticated, service_role;

-- ---------------------------------------------------------------------------
-- 5. Grant RPC: apply_entitlement_grant()
-- ---------------------------------------------------------------------------
-- SECURITY DEFINER upsert used by verifiers. Idempotent: keyed on the
-- user_id primary key, so repeated webhook/verification calls for the same
-- subscription/purchase cannot corrupt state.
--
-- Modes:
--   'paid'   -> is_pro = true, paid_until = p_paid_until (grant paid Pro)
--   'trial'  -> is_pro = true, trial_started_at = now(),
--               trial_ends_at = now() + p_trial_days (default 3)
--   'status' -> only provider/source/status update (no Pro change; e.g.
--               cancelled/suspended where paid_until is unknown)
--   'clear'  -> reset row to free defaults

create or replace function public.apply_entitlement_grant(
  p_user_id    uuid,
  p_mode       text,
  p_paid_until timestamptz default null,
  p_provider   text        default null,
  p_source     text        default null,
  p_status     text        default null,
  p_trial_days int         default 3
)
returns public.entitlements
language plpgsql
security definer
set search_path = public
as $$
declare
  v_now         timestamptz := now();
  v_trial       timestamptz;
  v_paid_until  timestamptz;
  v_row         public.entitlements;
begin
  if p_user_id is null then
    raise exception 'p_user_id is required';
  end if;

  if p_mode = 'paid' then
    -- NEVER write paid_until = NULL for a paid grant: in this model NULL means
    -- lifetime Pro. When no expiry is supplied we apply a bounded 30-day window
    -- instead of silently granting lifetime access.
    v_paid_until := coalesce(p_paid_until, v_now + interval '30 days');
    insert into public.entitlements (user_id, is_pro, paid_until, provider, source, status, updated_at)
    values (p_user_id, true, v_paid_until, p_provider, p_source, p_status, v_now)
    on conflict (user_id) do update
      set is_pro     = true,
          paid_until = excluded.paid_until,
          provider   = coalesce(excluded.provider, public.entitlements.provider),
          source     = coalesce(excluded.source, public.entitlements.source),
          status     = coalesce(excluded.status, public.entitlements.status),
          updated_at = excluded.updated_at;

  elsif p_mode = 'trial' then
    v_trial := v_now + make_interval(days => greatest(1, p_trial_days));
    insert into public.entitlements (user_id, is_pro, trial_started_at, trial_ends_at, provider, source, status, updated_at)
    values (p_user_id, true, v_now, v_trial, p_provider, p_source, p_status, v_now)
    on conflict (user_id) do update
      set is_pro           = true,
          trial_started_at = excluded.trial_started_at,
          trial_ends_at    = excluded.trial_ends_at,
          provider         = coalesce(excluded.provider, public.entitlements.provider),
          source           = coalesce(excluded.source, public.entitlements.source),
          status           = coalesce(excluded.status, public.entitlements.status),
          updated_at       = excluded.updated_at;

  elsif p_mode = 'status' then
    insert into public.entitlements (user_id, is_pro, provider, source, status, updated_at)
    values (p_user_id, false, p_provider, p_source, p_status, v_now)
    on conflict (user_id) do update
      set provider   = coalesce(excluded.provider, public.entitlements.provider),
          source     = coalesce(excluded.source, public.entitlements.source),
          status     = coalesce(excluded.status, public.entitlements.status),
          updated_at = excluded.updated_at;

  elsif p_mode = 'clear' then
    insert into public.entitlements (user_id, is_pro, provider, source, status, updated_at)
    values (p_user_id, false, null, null, null, v_now)
    on conflict (user_id) do update
      set is_pro           = false,
          trial_started_at = null,
          trial_ends_at    = null,
          paid_until       = null,
          provider         = null,
          source           = null,
          status           = null,
          updated_at       = excluded.updated_at;

  else
    raise exception 'unknown grant mode: %', p_mode;
  end if;

  select * into v_row from public.entitlements where user_id = p_user_id;
  return v_row;
end;
$$;

-- Service-role only: this RPC can grant (or clear) Pro for ANY user, so it
-- must not be callable by authenticated/anon clients (privilege escalation).
revoke execute on function public.apply_entitlement_grant(uuid, text, timestamptz, text, text, text, int)
  from public, anon, authenticated;
grant execute on function public.apply_entitlement_grant(uuid, text, timestamptz, text, text, text, int)
  to service_role;
