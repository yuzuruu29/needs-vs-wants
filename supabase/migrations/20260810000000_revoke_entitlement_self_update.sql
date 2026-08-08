-- Needs vs Wants - revoke client self-update on entitlements
--
-- Security hardening (P0, 2026-08-08): the `entitlements_update_own` RLS
-- policy let ANY authenticated user UPDATE their own entitlements row via a
-- direct PostgREST call (PATCH /rest/v1/entitlements), setting is_pro=true,
-- paid_until in the future, and tier='max' - self-granting paid access without
-- any billing. The client NEVER legitimately writes to this table; all reads
-- go through the SECURITY DEFINER my_entitlement() RPC, and all grants are
-- written by Edge Functions using the service-role key (which bypasses RLS).
--
-- Dropping the UPDATE policy closes the self-grant path while preserving:
--   - reads: my_entitlement() RPC (separately granted to authenticated)
--   - writes: Edge Functions via service-role (bypasses RLS)
-- This matches the original migration's intent: "There is intentionally NO
-- insert/delete policy for authenticated users" - the UPDATE policy was an
-- oversight of the same privilege-escalation class.

drop policy if exists "entitlements_update_own" on public.entitlements;