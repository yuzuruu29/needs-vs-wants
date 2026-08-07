-- Needs vs Wants — Pro vs Max tier column + RPC surface
-- Adds tier to entitlements so Max (Advisor) can be granted separately from Pro.

alter table public.entitlements
  add column if not exists tier text not null default 'free';

comment on column public.entitlements.tier is
  'free | pro | max — Max is a paid tier that includes Pro benefits';

-- Replace my_entitlement() to include tier (drop first: return shape changes)
drop function if exists public.my_entitlement();

create or replace function public.my_entitlement()
returns table (
  is_pro           boolean,
  trial_started_at timestamptz,
  trial_ends_at    timestamptz,
  paid_until       timestamptz,
  provider         text,
  source           text,
  status           text,
  is_active        boolean,
  tier             text
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
             false::boolean,
             'free'::text;
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
           public.is_entitlement_active(e) as is_active,
           coalesce(e.tier, 'free') as tier;
end;
$$;

revoke execute on function public.my_entitlement() from public, anon;
grant execute on function public.my_entitlement() to authenticated, service_role;
