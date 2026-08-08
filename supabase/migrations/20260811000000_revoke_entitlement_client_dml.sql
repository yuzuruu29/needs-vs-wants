-- Needs vs Wants - revoke client DML on entitlement tables (defense in depth)
--
-- After D117 dropped entitlements_update_own, RLS already blocks authenticated
-- INSERT/UPDATE/DELETE (no matching policies). Table-level GRANTs still
-- exposed INSERT/UPDATE/DELETE/TRUNCATE to anon + authenticated, and
-- TRUNCATE is not subject to RLS. Revoke those privileges so clients can only
-- SELECT their own entitlements row (via entitlements_select_own) and cannot
-- touch payment_events at all. Edge Functions keep full access via service_role.

revoke insert, update, delete, truncate on public.entitlements from anon, authenticated;
revoke all on public.payment_events from anon, authenticated;

-- Keep authenticated SELECT on entitlements for the existing select policy.
grant select on public.entitlements to authenticated;
