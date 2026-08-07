// Needs vs Wants - shared entitlement helpers
// Task 2: Supabase Edge Functions backend scaffolding
//
// This module is intentionally dependency-free so it can be unit-tested with
// `deno test` (no supabase-js, no external imports). Edge Functions import it
// alongside the Supabase client. All Pro computation is done from DB
// timestamps and server time; never trust client-supplied timestamps.

export const TRIAL_DURATION_DAYS = 3;
// Bounded fallback for a PAID grant that cannot derive an expiry (e.g. PayPal
// webhook with no next_billing_time). We must NEVER write paid_until = NULL for
// a paid subscription, because NULL means lifetime Pro in the model.
export const PAID_GRANT_FALLBACK_DAYS = 30;

export interface EntitlementRow {
  user_id?: string;
  is_pro: boolean;
  tier?: "free" | "pro" | "max";
  trial_started_at: string | null;
  trial_ends_at: string | null;
  paid_until: string | null;
  provider: string | null;
  source: string | null;
  status: string | null;
  updated_at?: string;
}

export interface GrantState {
  mode: "paid" | "trial" | "status" | "clear";
  tier?: "pro" | "max";
  paid_until?: string | null;
  provider?: string | null;
  source?: string | null;
  status?: string | null;
  trial_days?: number;
}

/**
 * Pure expiry logic mirroring the SQL is_entitlement_active().
 * Server time (the injected `now`) is authoritative. A NULL paid_until /
 * trial_ends_at on a Pro row is treated as lifetime (active).
 */
export function isEntitlementActive(
  row: Pick<EntitlementRow, "is_pro" | "trial_ends_at" | "paid_until">,
  nowIso: string,
): boolean {
  if (!row.is_pro) return false;
  const now = Date.parse(nowIso);
  if (Number.isNaN(now)) return false;
  if (row.paid_until !== null && row.paid_until !== undefined && Date.parse(row.paid_until) <= now) {
    return false;
  }
  if (row.trial_ends_at !== null && row.trial_ends_at !== undefined && Date.parse(row.trial_ends_at) <= now) {
    return false;
  }
  return true;
}

/** Evaluates whether an entitlement row has active Pro access (Pro or Max tier). */
export function hasProAccess(
  row: Pick<EntitlementRow, "is_pro" | "tier" | "trial_ends_at" | "paid_until">,
  nowIso: string,
): boolean {
  if (!isEntitlementActive(row, nowIso)) return false;
  const tier = row.tier ?? "pro";
  return tier === "pro" || tier === "max";
}

/** Evaluates whether an entitlement row has active Max access (Max tier only). */
export function hasMaxAccess(
  row: Pick<EntitlementRow, "is_pro" | "tier" | "trial_ends_at" | "paid_until">,
  nowIso: string,
): boolean {
  if (!isEntitlementActive(row, nowIso)) return false;
  const tier = row.tier ?? "pro";
  return tier === "max";
}

/** Build a 3-day trial grant state from server time. */
export function buildTrialGrant(
  nowIso: string,
  days: number = TRIAL_DURATION_DAYS,
): GrantState {
  const start = new Date(nowIso);
  const end = new Date(start.getTime() + days * 24 * 60 * 60 * 1000);
  return {
    mode: "trial",
    paid_until: null,
    trial_days: days,
  };
}

/**
 * Resolve paid tier from a PayPal plan id using env plan ids.
 * Defaults to "pro" when plan is unknown (legacy / single-plan setups).
 */
export function tierFromPayPalPlanId(
  planId: string | null | undefined,
  proPlanId?: string | null,
  maxPlanId?: string | null,
): "pro" | "max" {
  if (!planId) return "pro";
  if (maxPlanId && planId === maxPlanId) return "max";
  if (proPlanId && planId === proPlanId) return "pro";
  // Heuristic: plan id / name often contains "max"
  if (/max/i.test(planId)) return "max";
  return "pro";
}

/**
 * Map a PayPal BILLING.SUBSCRIPTION.* webhook event to an entitlement grant.
 * Returns null when the event is not one we grant/flag from.
 *
 * The user id is taken from the subscription's custom_id (the value the
 * client embedded when creating the subscription). We never trust the caller's
 * identity in the webhook; the mapping is derived from verified event data.
 *
 * Optional env plan ids (PAYPAL_PLAN_PRO / PAYPAL_PLAN_MAX) map plan_id → tier.
 */
export function mapPayPalWebhookEvent(
  payload: unknown,
  proPlanId?: string | null,
  maxPlanId?: string | null,
): {
  user_id: string;
  grant: GrantState;
} | null {
  if (!payload || typeof payload !== "object") return null;
  const event = payload as Record<string, unknown>;
  const resource = (event.resource ?? {}) as Record<string, unknown>;
  const eventType = typeof event.event_type === "string" ? event.event_type : "";

  const user_id =
    typeof resource.user_id === "string"
      ? resource.user_id
      : typeof resource.custom_id === "string"
        ? resource.custom_id
        : null;

  if (!user_id) return null;

  const planId =
    typeof resource.plan_id === "string"
      ? resource.plan_id
      : null;
  const tier = tierFromPayPalPlanId(planId, proPlanId, maxPlanId);

  // next_billing_time may be absent when PayPal cannot determine a next cycle.
  // Leave paid_until UNSET (undefined) in that case so grantToRowFields applies
  // a bounded fallback window. We must never emit a plain null here: for a paid
  // grant, null would mean lifetime Pro in the model.
  const hasPaidUntil =
    typeof resource.billing_info === "object" &&
    resource.billing_info !== null &&
    typeof (resource.billing_info as Record<string, unknown>).next_billing_time === "string";
  const paidUntil = hasPaidUntil
    ? (resource.billing_info as Record<string, unknown>).next_billing_time as string
    : undefined;

  switch (eventType) {
    case "BILLING.SUBSCRIPTION.ACTIVATED":
    case "BILLING.SUBSCRIPTION.CREATED":
    case "BILLING.SUBSCRIPTION.REACTIVATED":
    case "BILLING.SUBSCRIPTION.REVISED":
      return {
        user_id,
        grant: {
          mode: "paid",
          tier,
          ...(hasPaidUntil ? { paid_until: paidUntil as string } : {}),
          provider: "paypal",
          source: "paypal",
          status: eventType.toLowerCase(),
        },
      };
    case "BILLING.SUBSCRIPTION.CANCELLED":
    case "BILLING.SUBSCRIPTION.SUSPENDED":
    case "BILLING.SUBSCRIPTION.EXPIRED":
      // Access continues until the paid period ends; only flag status. A
      // partial metadata update (mode: "status") preserves is_pro / paid_until
      // / trial_* so cancellation never wipes already-paid access.
      return {
        user_id,
        grant: {
          mode: "status",
          provider: "paypal",
          source: "paypal",
          status: eventType.toLowerCase(),
        },
      };
    default:
      return null;
  }
}

/** Apply a mapped grant to the entitlements row (idempotent upsert fields). */
export function grantToRowFields(
  grant: GrantState,
): Partial<
  Pick<
    EntitlementRow,
    | "is_pro"
    | "tier"
    | "trial_started_at"
    | "trial_ends_at"
    | "paid_until"
    | "provider"
    | "source"
    | "status"
  >
> {
  const provider = grant.provider ?? null;
  const source = grant.source ?? null;
  const status = grant.status ?? null;
  const tier = grant.tier ?? "pro";

  switch (grant.mode) {
    case "paid":
      // paid_until === undefined: no expiry available -> apply a bounded
      // fallback window. Never write NULL for a paid subscription.
      // paid_until === null: intentionally lifetime (e.g. Google Play one_time).
      const paidUntil = grant.paid_until === undefined
        ? new Date(
            Date.now() + PAID_GRANT_FALLBACK_DAYS * 24 * 60 * 60 * 1000,
          ).toISOString()
        : grant.paid_until;
      return {
        is_pro: true,
        tier,
        trial_started_at: null,
        trial_ends_at: null,
        paid_until: paidUntil,
        provider,
        source,
        status,
      };
    case "trial":
      const trialStart = new Date();
      const trialEnd = new Date(
        trialStart.getTime() + (grant.trial_days ?? TRIAL_DURATION_DAYS) * 24 * 60 * 60 * 1000,
      );
      return {
        is_pro: true,
        tier: grant.tier ?? "pro",
        trial_started_at: trialStart.toISOString(),
        trial_ends_at: trialEnd.toISOString(),
        paid_until: null,
        provider,
        source,
        status,
      };
    case "status":
      // Partial update only: metadata (provider/source/status). is_pro,
      // paid_until and trial_* must be left untouched so a cancellation or
      // suspension does not revoke already-paid or already-trialing access.
      return {
        provider,
        source,
        status,
      };
    case "clear":
      return {
        is_pro: false,
        tier: "free",
        trial_started_at: null,
        trial_ends_at: null,
        paid_until: null,
        provider: null,
        source: null,
        status: null,
      };
  }
}
