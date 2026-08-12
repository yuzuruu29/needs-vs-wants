// Needs vs Wants - unit tests for shared entitlement helpers
// Run: deno test supabase/functions/_shared/entitlements.test.ts
import {
  assertEquals,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  isEntitlementActive,
  hasProAccess,
  hasMaxAccess,
  mapPayPalWebhookEvent,
  grantToRowFields,
  buildTrialGrant,
  TRIAL_DURATION_DAYS,
} from "./entitlements.ts";

// ---------------------------------------------------------------------------
// isEntitlementActive - expiry logic (server time is authoritative)
// ---------------------------------------------------------------------------

const now = "2026-08-03T12:00:00.000Z";

Deno.test("free (is_pro=false) is not active", () => {
  assertEquals(
    isEntitlementActive(
      { is_pro: false, trial_ends_at: null, paid_until: null },
      now,
    ),
    false,
  );
});

Deno.test("pro with null end dates is lifetime-active", () => {
  assertEquals(
    isEntitlementActive(
      { is_pro: true, trial_ends_at: null, paid_until: null },
      now,
    ),
    true,
  );
});

Deno.test("pro with paid_until in the future is active", () => {
  assertEquals(
    isEntitlementActive(
      {
        is_pro: true,
        trial_ends_at: null,
        paid_until: "2026-09-01T00:00:00.000Z",
      },
      now,
    ),
    true,
  );
});

Deno.test("pro with expired paid_until is NOT active (exact expiry boundary)", () => {
  assertEquals(
    isEntitlementActive(
      {
        is_pro: true,
        trial_ends_at: null,
        paid_until: "2026-08-03T12:00:00.000Z", // == now
      },
      now,
    ),
    false,
  );
});

Deno.test("pro with active trial is active", () => {
  assertEquals(
    isEntitlementActive(
      {
        is_pro: true,
        trial_ends_at: "2026-08-06T12:00:00.000Z",
        paid_until: null,
      },
      now,
    ),
    true,
  );
});

Deno.test("pro with expired trial is not active", () => {
  assertEquals(
    isEntitlementActive(
      {
        is_pro: true,
        trial_ends_at: "2026-08-02T12:00:00.000Z",
        paid_until: null,
      },
      now,
    ),
    false,
  );
});

Deno.test("invalid now timestamp is treated as not active", () => {
  assertEquals(
    isEntitlementActive(
      { is_pro: true, trial_ends_at: null, paid_until: null },
      "not-a-date",
    ),
    false,
  );
});

Deno.test("hasProAccess returns true for pro and max tiers, false for free or expired", () => {
  assertEquals(hasProAccess({ is_pro: true, tier: "pro", trial_ends_at: null, paid_until: null }, now), true);
  assertEquals(hasProAccess({ is_pro: true, tier: "max", trial_ends_at: null, paid_until: null }, now), true);
  assertEquals(hasProAccess({ is_pro: false, tier: "free", trial_ends_at: null, paid_until: null }, now), false);
  assertEquals(hasProAccess({ is_pro: true, tier: "pro", trial_ends_at: "2026-08-01T00:00:00.000Z", paid_until: null }, now), false);
});

Deno.test("hasMaxAccess returns true ONLY for max tier, false for pro or free", () => {
  assertEquals(hasMaxAccess({ is_pro: true, tier: "max", trial_ends_at: null, paid_until: null }, now), true);
  assertEquals(hasMaxAccess({ is_pro: true, tier: "pro", trial_ends_at: null, paid_until: null }, now), false);
  assertEquals(hasMaxAccess({ is_pro: false, tier: "free", trial_ends_at: null, paid_until: null }, now), false);
});

// ---------------------------------------------------------------------------
// mapPayPalWebhookEvent - webhook event to entitlement mapping
// ---------------------------------------------------------------------------

Deno.test("null / non-object payload maps to null", () => {
  assertEquals(mapPayPalWebhookEvent(null), null);
  assertEquals(mapPayPalWebhookEvent("junk"), null);
});

Deno.test("activated event grants paid pro with paid_until", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.ACTIVATED",
    resource: {
      custom_id: "user-abc",
      billing_info: { next_billing_time: "2026-09-01T00:00:00Z" },
    },
  });
  assertEquals(mapped, {
    user_id: "user-abc",
    grant: {
      mode: "paid",
      tier: "pro",
      paid_until: "2026-09-01T00:00:00Z",
      provider: "paypal",
      source: "paypal",
      status: "billing.subscription.activated",
    },
  });
});

Deno.test("cancelled event flags status without clearing pro", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.CANCELLED",
    resource: { custom_id: "user-abc" },
  });
  assertEquals(mapped?.grant.mode, "status");
  assertEquals(mapped?.grant.status, "billing.subscription.cancelled");
});

Deno.test("expired event maps to status (does not clear paid access)", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.EXPIRED",
    resource: { custom_id: "user-abc" },
  });
  assertEquals(mapped?.grant.mode, "status");
  assertEquals(mapped?.grant.status, "billing.subscription.expired");
});

Deno.test("activated event without next_billing_time omits paid_until", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.ACTIVATED",
    resource: { custom_id: "user-abc" },
  });
  assertEquals(mapped?.grant.mode, "paid");
  // No next_billing_time -> paid_until stays unset so the shared mapper applies
  // a bounded fallback instead of interpreting it as lifetime Pro.
  assertEquals(
    (mapped?.grant as unknown as Record<string, unknown>).paid_until,
    undefined,
  );
});

Deno.test("unknown event maps to null", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "CUSTOM.PIN_SUBJECT",
    resource: { custom_id: "user-abc" },
  });
  assertEquals(mapped, null);
});

Deno.test("event without a user id maps to null", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.ACTIVATED",
    resource: { subscription_id: "I-123" },
  });
  assertEquals(mapped, null);
});

// ---------------------------------------------------------------------------
// Trial tenure detection + PAYMENT.SUCCEEDED (D142: trial-aware grants)
// ---------------------------------------------------------------------------

Deno.test("activated event with TRIAL tenure grants trial mode with trial_ends_at", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.ACTIVATED",
    resource: {
      custom_id: "user-abc",
      plan_id: "P-PRO",
      billing_info: {
        next_billing_time: "2026-08-06T10:00:00Z",
        cycle_executions: [
          { tenure_type: "TRIAL", sequence: 1, cycles_completed: 0, cycles_remaining: 1 },
        ],
      },
    },
  }, "P-PRO", "P-MAX");
  assertEquals(mapped, {
    user_id: "user-abc",
    grant: {
      mode: "trial",
      tier: "pro",
      trial_ends_at: "2026-08-06T10:00:00Z",
      provider: "paypal",
      source: "paypal",
      status: "billing.subscription.activated",
    },
  });
});

Deno.test("activated event with REGULAR tenure still grants paid (no trial)", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.ACTIVATED",
    resource: {
      custom_id: "user-abc",
      plan_id: "P-MAX",
      billing_info: {
        next_billing_time: "2026-09-01T00:00:00Z",
        cycle_executions: [
          { tenure_type: "REGULAR", sequence: 1, cycles_completed: 0, cycles_remaining: 12 },
        ],
      },
    },
  }, "P-PRO", "P-MAX");
  assertEquals(mapped?.grant.mode, "paid");
  assertEquals(mapped?.grant.paid_until, "2026-09-01T00:00:00Z");
  assertEquals(mapped?.grant.tier, "max");
});

// Annual plan ids (D147: annual billing). Annual ids are checked BEFORE the
// /max/i heuristic so an opaque annual Max plan id never degrades to pro.
Deno.test("annual Pro plan id maps to pro paid grant", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.ACTIVATED",
    resource: {
      custom_id: "user-abc",
      plan_id: "P-PRO-ANNUAL",
      billing_info: {
        next_billing_time: "2027-08-12T00:00:00Z",
        cycle_executions: [
          { tenure_type: "REGULAR", sequence: 1, cycles_completed: 0, cycles_remaining: 1 },
        ],
      },
    },
  }, "P-PRO", "P-MAX", "P-PRO-ANNUAL", "P-MAX-ANNUAL");
  assertEquals(mapped?.grant.mode, "paid");
  assertEquals(mapped?.grant.tier, "pro");
  assertEquals(mapped?.grant.paid_until, "2027-08-12T00:00:00Z");
});

Deno.test("annual Max plan id maps to max paid grant (no heuristic fallback)", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.ACTIVATED",
    resource: {
      custom_id: "user-abc",
      plan_id: "P-MAX-ANNUAL",
      billing_info: {
        next_billing_time: "2027-08-12T00:00:00Z",
        cycle_executions: [
          { tenure_type: "REGULAR", sequence: 1, cycles_completed: 0, cycles_remaining: 1 },
        ],
      },
    },
  }, "P-PRO", "P-MAX", "P-PRO-ANNUAL", "P-MAX-ANNUAL");
  assertEquals(mapped?.grant.mode, "paid");
  assertEquals(mapped?.grant.tier, "max");
  assertEquals(mapped?.grant.paid_until, "2027-08-12T00:00:00Z");
});

Deno.test("annual plan id without annual env ids falls back to heuristic", () => {
  // No annual secrets configured: an unknown annual Pro id defaults to pro
  // (legacy behavior).
  const mappedPro = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.ACTIVATED",
    resource: {
      custom_id: "user-abc",
      plan_id: "P-PRO-ANNUAL",
      billing_info: {
        next_billing_time: "2027-08-12T00:00:00Z",
        cycle_executions: [
          { tenure_type: "REGULAR", sequence: 1, cycles_completed: 0, cycles_remaining: 1 },
        ],
      },
    },
  }, "P-PRO", "P-MAX");
  assertEquals(mappedPro?.grant.tier, "pro");
});

Deno.test("activated event with a completed trial (cycles_remaining 0) grants paid", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.ACTIVATED",
    resource: {
      custom_id: "user-abc",
      billing_info: {
        next_billing_time: "2026-09-01T00:00:00Z",
        cycle_executions: [
          { tenure_type: "TRIAL", sequence: 1, cycles_completed: 1, cycles_remaining: 0 },
        ],
      },
    },
  });
  assertEquals(mapped?.grant.mode, "paid");
});

Deno.test("activated trial without next_billing_time omits trial_ends_at (bounded fallback)", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.ACTIVATED",
    resource: {
      custom_id: "user-abc",
      billing_info: {
        cycle_executions: [
          { tenure_type: "TRIAL", sequence: 1, cycles_completed: 0, cycles_remaining: 1 },
        ],
      },
    },
  });
  assertEquals(mapped?.grant.mode, "trial");
  assertEquals(
    (mapped?.grant as unknown as Record<string, unknown>).trial_ends_at,
    undefined,
  );
});

Deno.test("payment succeeded extends paid_until (mode paid, not ignored)", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.PAYMENT.SUCCEEDED",
    resource: {
      custom_id: "user-abc",
      plan_id: "P-PRO",
      billing_info: {
        next_billing_time: "2026-09-10T10:00:00Z",
        last_payment: { amount: { currency_code: "PHP", value: "199.00" }, time: "2026-08-06T10:00:00Z" },
      },
    },
  }, "P-PRO", "P-MAX");
  assertEquals(mapped, {
    user_id: "user-abc",
    grant: {
      mode: "paid",
      tier: "pro",
      paid_until: "2026-09-10T10:00:00Z",
      provider: "paypal",
      source: "paypal",
      status: "billing.subscription.payment.succeeded",
    },
  });
});

Deno.test("payment succeeded without next_billing_time omits paid_until (bounded fallback)", () => {
  const mapped = mapPayPalWebhookEvent({
    event_type: "BILLING.SUBSCRIPTION.PAYMENT.SUCCEEDED",
    resource: { custom_id: "user-abc" },
  });
  assertEquals(mapped?.grant.mode, "paid");
  assertEquals(
    (mapped?.grant as unknown as Record<string, unknown>).paid_until,
    undefined,
  );
});

// ---------------------------------------------------------------------------
// grantToRowFields / buildTrialGrant
// ---------------------------------------------------------------------------

Deno.test("paid grant sets is_pro true and propagates paid_until", () => {
  const fields = grantToRowFields({
    mode: "paid",
    paid_until: "2026-09-01T00:00:00.000Z",
    provider: "apple",
    source: "apple",
    status: "activated",
  });
  assertEquals(fields.is_pro, true);
  assertEquals(fields.paid_until, "2026-09-01T00:00:00.000Z");
});

Deno.test("paid grant with missing expiry uses bounded fallback (never null)", () => {
  const fields = grantToRowFields({
    mode: "paid",
    provider: "paypal",
    source: "paypal",
    status: "activated",
  });
  assertEquals(fields.is_pro, true);
  // paid_until must NOT be null (null means lifetime Pro in the model).
  assertEquals(fields.paid_until === null, false);
  const fallback = Date.parse(fields.paid_until!);
  const floor = Date.now() + 29 * 24 * 60 * 60 * 1000;
  const ceil = Date.now() + 31 * 24 * 60 * 60 * 1000;
  assertEquals(fallback >= floor, true);
  assertEquals(fallback <= ceil, true);
});

Deno.test("paid grant with explicit null paid_until is lifetime (google one-time)", () => {
  const fields = grantToRowFields({
    mode: "paid",
    paid_until: null,
    provider: "google",
    source: "one_time",
    status: "purchased",
  });
  assertEquals(fields.is_pro, true);
  assertEquals(fields.paid_until, null);
});

Deno.test("status grant returns only metadata, preserving pro fields", () => {
  const fields = grantToRowFields({
    mode: "status",
    provider: "paypal",
    source: "paypal",
    status: "billing.subscription.cancelled",
  });
  assertEquals(fields.provider, "paypal");
  assertEquals(fields.source, "paypal");
  assertEquals(fields.status, "billing.subscription.cancelled");
  // Critical: status must NOT clear pro / paid / trial fields.
  assertEquals(fields.is_pro, undefined);
  assertEquals(fields.paid_until, undefined);
  assertEquals(fields.trial_started_at, undefined);
  assertEquals(fields.trial_ends_at, undefined);
});

Deno.test("trial grant sets is_pro true and a +TRIAL_DURATION_DAYS trial_ends_at", () => {
  const trial = grantToRowFields({
    mode: "trial",
    provider: "supabase",
    source: "referral",
  });
  assertEquals(trial.is_pro, true);
  const start = Date.parse(trial.trial_started_at!);
  const end = Date.parse(trial.trial_ends_at!);
  assertEquals((end - start) / (24 * 60 * 60 * 1000), TRIAL_DURATION_DAYS);
});

Deno.test("trial grant with explicit trial_ends_at writes it through, paid_until stays null", () => {
  const trial = grantToRowFields({
    mode: "trial",
    trial_ends_at: "2026-08-06T10:00:00Z",
    provider: "paypal",
    source: "paypal",
    status: "billing.subscription.activated",
  });
  assertEquals(trial.is_pro, true);
  // Round-tripped through Date -> toISOString() (canonical .000Z form).
  assertEquals(trial.trial_ends_at, "2026-08-06T10:00:00.000Z");
  assertEquals(trial.paid_until, null);
});

Deno.test("trial grant with malformed trial_ends_at falls back to trial_days (no Invalid Date)", () => {
  const trial = grantToRowFields({
    mode: "trial",
    trial_ends_at: "not-a-date",
    provider: "paypal",
    source: "paypal",
  });
  const start = Date.parse(trial.trial_started_at!);
  const end = Date.parse(trial.trial_ends_at!);
  assertEquals(Number.isNaN(end), false);
  assertEquals((end - start) / (24 * 60 * 60 * 1000), TRIAL_DURATION_DAYS);
});

Deno.test("clear grant resets row to free defaults", () => {
  const fields = grantToRowFields({ mode: "clear" });
  assertEquals(fields, {
    is_pro: false,
    tier: "free",
    trial_started_at: null,
    trial_ends_at: null,
    paid_until: null,
    provider: null,
    source: null,
    status: null,
  });
});

Deno.test("buildTrialGrant produces a 3-day window", () => {
  const build = buildTrialGrant("2026-08-03T00:00:00.000Z", 3);
  assertEquals(build.mode, "trial");
  const start = Date.parse("2026-08-03T00:00:00.000Z");
  const end = start + build.trial_days! * 24 * 60 * 60 * 1000;
  assertEquals(build.trial_days, 3);
  assertEquals(end - start, 3 * 24 * 60 * 60 * 1000);
});