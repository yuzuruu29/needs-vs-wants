// Needs vs Wants - unit tests for shared entitlement helpers
// Run: deno test supabase/functions/_shared/entitlements.test.ts
import {
  assertThrows,
  assertEquals,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  isEntitlementActive,
  hasProAccess,
  hasMaxAccess,
  tierFromGooglePlayProductId,
  mapPayPalWebhookEvent,
  grantToRowFields,
  buildTrialGrant,
  TRIAL_DURATION_DAYS,
  parseSubscriptionV2Response,
  parseOneTimeProductV2Response,
  validateGooglePlayVerifyRequest,
  mergePaidEntitlements,
  paidPurchaseFromRow,
  type PaidEntitlementPurchase,
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

Deno.test("tierFromGooglePlayProductId maps allowlisted product IDs to tiers", () => {
  assertEquals(tierFromGooglePlayProductId("needsvswants_pro"), "pro");
  assertEquals(tierFromGooglePlayProductId("needsvswants_max"), "max");
});

Deno.test("tierFromGooglePlayProductId rejects unknown product IDs (fail closed, never maps to Pro)", () => {
  assertEquals(tierFromGooglePlayProductId("pro_monthly"), null);
  assertEquals(tierFromGooglePlayProductId("max_annual"), null);
  assertEquals(tierFromGooglePlayProductId("needsvswants_pro "), null); // trailing space
  assertEquals(tierFromGooglePlayProductId("NEEDSVSWANTS_PRO"), null); // case must match exactly
  assertEquals(tierFromGooglePlayProductId(""), null);
});

Deno.test("tierFromGooglePlayProductId rejects missing product IDs", () => {
  assertEquals(tierFromGooglePlayProductId(null), null);
  assertEquals(tierFromGooglePlayProductId(undefined), null);
});

// ---------------------------------------------------------------------------
// validateGooglePlayVerifyRequest - fail-closed request authorization
// ---------------------------------------------------------------------------

const VALID_BODY = {
  package_name: "com.needsvswants.app",
  purchase_token: "token-abc",
};

Deno.test("validateGooglePlayVerifyRequest accepts a valid subscription request", () => {
  assertEquals(validateGooglePlayVerifyRequest(VALID_BODY), {
    ok: true,
    packageName: "com.needsvswants.app",
    purchaseToken: "token-abc",
    kind: "subscription",
  });
});

Deno.test("validateGooglePlayVerifyRequest rejects a wrong package name", () => {
  const result = validateGooglePlayVerifyRequest({
    package_name: "com.evil.repackage",
    purchase_token: "token-abc",
  });
  assertEquals(result.ok, false);
  if (!result.ok) {
    assertEquals(result.status, 400);
    assertEquals(result.error, "Unknown package_name");
  }
});

Deno.test("validateGooglePlayVerifyRequest rejects a missing package name or purchase token", () => {
  assertEquals(validateGooglePlayVerifyRequest({ purchase_token: "t" }).ok, false);
  assertEquals(
    validateGooglePlayVerifyRequest({ package_name: "com.needsvswants.app" }).ok,
    false,
  );
});

Deno.test("validateGooglePlayVerifyRequest rejects one_time purchases (no allowlisted Play one-time product)", () => {
  const withoutId = validateGooglePlayVerifyRequest({
    ...VALID_BODY,
    kind: "one_time",
  });
  assertEquals(withoutId.ok, false);

  const withUnknownId = validateGooglePlayVerifyRequest({
    ...VALID_BODY,
    kind: "one_time",
    product_id: "needsvswants_pro",
  });
  assertEquals(withUnknownId.ok, false);
  if (!withUnknownId.ok) {
    assertEquals(
      withUnknownId.error,
      "Unsupported product_id for one_time purchase",
    );
  }
});

Deno.test("validateGooglePlayVerifyRequest rejects an unknown purchase kind", () => {
  const result = validateGooglePlayVerifyRequest({
    ...VALID_BODY,
    kind: "junk",
  });
  assertEquals(result.ok, false);
  if (!result.ok) {
    assertEquals(result.error, "Unknown purchase kind");
  }
});

// ---------------------------------------------------------------------------
// paidPurchaseFromRow - row -> mergeable purchase
// ---------------------------------------------------------------------------

Deno.test("paidPurchaseFromRow maps an active paid row to a dated purchase", () => {
  assertEquals(
    paidPurchaseFromRow({
      is_pro: true,
      tier: "max",
      trial_ends_at: null,
      paid_until: "2026-10-01T00:00:00.000Z",
    }),
    { tier: "max", paid_until: "2026-10-01T00:00:00.000Z" },
  );
});

Deno.test("paidPurchaseFromRow maps a genuine lifetime row to null expiry", () => {
  // is_pro + paid_until NULL + NO trial window = lifetime paid.
  assertEquals(
    paidPurchaseFromRow({
      is_pro: true,
      tier: "pro",
      trial_ends_at: null,
      paid_until: null,
    }),
    { tier: "pro", paid_until: null },
  );
});

Deno.test("paidPurchaseFromRow does NOT treat a trial row as lifetime paid", () => {
  // Trial rows carry is_pro=true + paid_until=null but are gated by
  // trial_ends_at; merging them as lifetime would mint free lifetime Pro.
  assertEquals(
    paidPurchaseFromRow({
      is_pro: true,
      tier: "pro",
      trial_ends_at: "2026-08-25T00:00:00.000Z",
      paid_until: null,
    }),
    null,
  );
});

Deno.test("paidPurchaseFromRow returns null for free or missing rows", () => {
  assertEquals(
    paidPurchaseFromRow({
      is_pro: false,
      tier: "free",
      trial_ends_at: null,
      paid_until: null,
    }),
    null,
  );
  assertEquals(paidPurchaseFromRow(null), null);
  assertEquals(paidPurchaseFromRow(undefined), null);
});

// ---------------------------------------------------------------------------
// mergePaidEntitlements - order-independent entitlement merging
// ---------------------------------------------------------------------------

const MERGE_NOW = "2026-08-22T12:00:00.000Z";

function pro(untilIso: string | null): PaidEntitlementPurchase {
  return { tier: "pro", paid_until: untilIso };
}

function max(untilIso: string | null): PaidEntitlementPurchase {
  return { tier: "max", paid_until: untilIso };
}

Deno.test("mergePaidEntitlements: Pro + Max coexisting resolves to Max with Max expiry", () => {
  assertEquals(
    mergePaidEntitlements([pro("2026-09-01T00:00:00.000Z"), max("2026-10-15T00:00:00.000Z")], MERGE_NOW),
    max("2026-10-15T00:00:00.000Z"),
  );
  // Same set, reversed arrival order.
  assertEquals(
    mergePaidEntitlements([max("2026-10-15T00:00:00.000Z"), pro("2026-09-01T00:00:00.000Z")], MERGE_NOW),
    max("2026-10-15T00:00:00.000Z"),
  );
});

Deno.test("mergePaidEntitlements: late Pro callback cannot downgrade an existing Max grant", () => {
  // Pro->Max upgrade where the stale Pro verification finishes LAST.
  assertEquals(
    mergePaidEntitlements([max("2026-11-01T00:00:00.000Z"), pro("2026-09-01T00:00:00.000Z")], MERGE_NOW),
    max("2026-11-01T00:00:00.000Z"),
  );
});

Deno.test("mergePaidEntitlements: Max keeps its own earlier expiry (no cross-tier extension)", () => {
  // The winning tier never inherits a lower tier's later expiry, so a grant
  // cannot outlive its own verified coverage.
  assertEquals(
    mergePaidEntitlements([pro("2026-12-31T00:00:00.000Z"), max("2026-09-01T00:00:00.000Z")], MERGE_NOW),
    max("2026-09-01T00:00:00.000Z"),
  );
});

Deno.test("mergePaidEntitlements: latest expiry wins within a tier regardless of arrival order", () => {
  assertEquals(
    mergePaidEntitlements([pro("2026-09-01T00:00:00.000Z"), pro("2027-03-01T00:00:00.000Z")], MERGE_NOW),
    pro("2027-03-01T00:00:00.000Z"),
  );
  assertEquals(
    mergePaidEntitlements([pro("2027-03-01T00:00:00.000Z"), pro("2026-09-01T00:00:00.000Z")], MERGE_NOW),
    pro("2027-03-01T00:00:00.000Z"),
  );
});

Deno.test("mergePaidEntitlements: lifetime (null paid_until) outranks any dated expiry within the tier", () => {
  assertEquals(
    mergePaidEntitlements([pro("2027-03-01T00:00:00.000Z"), pro(null)], MERGE_NOW),
    pro(null),
  );
  // Lifetime in a LOWER tier does not outrank a higher tier's dated expiry.
  assertEquals(
    mergePaidEntitlements([max("2026-09-15T00:00:00.000Z"), pro(null)], MERGE_NOW),
    max("2026-09-15T00:00:00.000Z"),
  );
});

Deno.test("mergePaidEntitlements: duplicate callbacks produce identical entitlement in both orders", () => {
  const first = max("2026-10-01T00:00:00.000Z");
  const duplicate = max("2026-10-01T00:00:00.000Z");
  const otherProvider = pro("2026-11-01T00:00:00.000Z");

  const forward = mergePaidEntitlements([first, otherProvider, duplicate], MERGE_NOW);
  const backward = mergePaidEntitlements([duplicate, otherProvider, first], MERGE_NOW);
  assertEquals(forward, backward);
  // Max wins; the lower Pro tier's later expiry must NOT extend it.
  assertEquals(forward, max("2026-10-01T00:00:00.000Z"));
});

Deno.test("mergePaidEntitlements: earlier expiry cannot overwrite a later expiry", () => {
  const newer = pro("2026-12-01T00:00:00.000Z");
  const stale = pro("2026-08-25T00:00:00.000Z"); // older renewal arriving late
  assertEquals(mergePaidEntitlements([newer, stale], MERGE_NOW).paid_until, "2026-12-01T00:00:00.000Z");
  assertEquals(mergePaidEntitlements([stale, newer], MERGE_NOW).paid_until, "2026-12-01T00:00:00.000Z");
});

Deno.test("mergePaidEntitlements: expired purchases are dropped before ranking (fresh Pro beats stale Max)", () => {
  assertEquals(
    mergePaidEntitlements([max("2026-08-21T00:00:00.000Z"), pro("2026-09-30T00:00:00.000Z")], MERGE_NOW),
    pro("2026-09-30T00:00:00.000Z"),
  );
});

Deno.test("mergePaidEntitlements: throws when no purchase survives expiry filtering", () => {
  assertThrows(
    () => mergePaidEntitlements([pro("2026-01-01T00:00:00.000Z")], MERGE_NOW),
  );
  assertThrows(() => mergePaidEntitlements([], MERGE_NOW));
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

// ---------------------------------------------------------------------------
// subscriptionsv2 parsing helpers
// ---------------------------------------------------------------------------

const testNow = "2026-08-20T12:00:00.000Z";

Deno.test("parseSubscriptionV2Response maps ACTIVE state with future expiry", () => {
  const result = parseSubscriptionV2Response({
    subscriptionState: "ACTIVE",
    lineItems: [{ productId: "needsvswants_pro", expiryTime: "2026-09-01T00:00:00.000Z" }],
  }, testNow);
  assertEquals(result?.productId, "needsvswants_pro");
  assertEquals(result?.expiry, "2026-09-01T00:00:00.000Z");
});

Deno.test("parseSubscriptionV2Response maps SUBSCRIPTION_STATE_ACTIVE with future expiry", () => {
  const result = parseSubscriptionV2Response({
    subscriptionState: "SUBSCRIPTION_STATE_ACTIVE",
    lineItems: [{ productId: "needsvswants_max", expiryTime: "2026-09-01T00:00:00.000Z" }],
  }, testNow);
  assertEquals(result?.productId, "needsvswants_max");
  assertEquals(result?.expiry, "2026-09-01T00:00:00.000Z");
});

Deno.test("parseSubscriptionV2Response maps SUBSCRIPTION_STATE_IN_GRACE_PERIOD with future expiry", () => {
  const result = parseSubscriptionV2Response({
    subscriptionState: "SUBSCRIPTION_STATE_IN_GRACE_PERIOD",
    lineItems: [{ productId: "needsvswants_pro", expiryTime: "2026-08-25T00:00:00.000Z" }],
  }, testNow);
  assertEquals(result?.productId, "needsvswants_pro");
  assertEquals(result?.expiry, "2026-08-25T00:00:00.000Z");
});

Deno.test("parseSubscriptionV2Response maps IN_GRACE_PERIOD with future expiry", () => {
  const result = parseSubscriptionV2Response({
    subscriptionState: "IN_GRACE_PERIOD",
    lineItems: [{ productId: "needsvswants_pro", expiryTime: "2026-08-25T00:00:00.000Z" }],
  }, testNow);
  assertEquals(result?.productId, "needsvswants_pro");
  assertEquals(result?.expiry, "2026-08-25T00:00:00.000Z");
});

Deno.test("parseSubscriptionV2Response rejects expired state (SUBSCRIPTION_STATE_EXPIRED)", () => {
  assertEquals(
    parseSubscriptionV2Response({
      subscriptionState: "SUBSCRIPTION_STATE_EXPIRED",
      lineItems: [{ productId: "needsvswants_pro", expiryTime: "2026-09-01T00:00:00.000Z" }],
    }, testNow),
    null
  );
});

Deno.test("parseSubscriptionV2Response rejects canceled state (SUBSCRIPTION_STATE_CANCELED)", () => {
  assertEquals(
    parseSubscriptionV2Response({
      subscriptionState: "SUBSCRIPTION_STATE_CANCELED",
      lineItems: [{ productId: "needsvswants_pro", expiryTime: "2026-09-01T00:00:00.000Z" }],
    }, testNow),
    null
  );
});

Deno.test("parseSubscriptionV2Response rejects pending state (SUBSCRIPTION_STATE_PENDING)", () => {
  assertEquals(
    parseSubscriptionV2Response({
      subscriptionState: "SUBSCRIPTION_STATE_PENDING",
      lineItems: [{ productId: "needsvswants_pro", expiryTime: "2026-09-01T00:00:00.000Z" }],
    }, testNow),
    null
  );
});

Deno.test("parseSubscriptionV2Response rejects paused state (SUBSCRIPTION_STATE_PAUSED)", () => {
  assertEquals(
    parseSubscriptionV2Response({
      subscriptionState: "SUBSCRIPTION_STATE_PAUSED",
      lineItems: [{ productId: "needsvswants_pro", expiryTime: "2026-09-01T00:00:00.000Z" }],
    }, testNow),
    null
  );
});

Deno.test("parseSubscriptionV2Response rejects on hold state (SUBSCRIPTION_STATE_ON_HOLD)", () => {
  assertEquals(
    parseSubscriptionV2Response({
      subscriptionState: "SUBSCRIPTION_STATE_ON_HOLD",
      lineItems: [{ productId: "needsvswants_pro", expiryTime: "2026-09-01T00:00:00.000Z" }],
    }, testNow),
    null
  );
});

Deno.test("parseSubscriptionV2Response rejects unspecified state (SUBSCRIPTION_STATE_UNSPECIFIED)", () => {
  assertEquals(
    parseSubscriptionV2Response({
      subscriptionState: "SUBSCRIPTION_STATE_UNSPECIFIED",
      lineItems: [{ productId: "needsvswants_pro", expiryTime: "2026-09-01T00:00:00.000Z" }],
    }, testNow),
    null
  );
});

Deno.test("parseSubscriptionV2Response rejects unknown state", () => {
  assertEquals(
    parseSubscriptionV2Response({
      subscriptionState: "SOME_UNKNOWN_STATE",
      lineItems: [{ productId: "needsvswants_pro", expiryTime: "2026-09-01T00:00:00.000Z" }],
    }, testNow),
    null
  );
});

Deno.test("parseSubscriptionV2Response rejects active state with past expiry timestamp", () => {
  assertEquals(
    parseSubscriptionV2Response({
      subscriptionState: "SUBSCRIPTION_STATE_ACTIVE",
      lineItems: [{ productId: "needsvswants_pro", expiryTime: "2026-08-19T00:00:00.000Z" }],
    }, testNow),
    null
  );
});

Deno.test("parseSubscriptionV2Response rejects active state with exact expiry boundary (expiry == now)", () => {
  assertEquals(
    parseSubscriptionV2Response({
      subscriptionState: "SUBSCRIPTION_STATE_ACTIVE",
      lineItems: [{ productId: "needsvswants_pro", expiryTime: testNow }],
    }, testNow),
    null
  );
});

Deno.test("parseSubscriptionV2Response rejects missing line items", () => {
  assertEquals(parseSubscriptionV2Response({ subscriptionState: "ACTIVE" }, testNow), null);
});

Deno.test("parseSubscriptionV2Response rejects missing productId", () => {
  assertEquals(
    parseSubscriptionV2Response({
      subscriptionState: "ACTIVE",
      lineItems: [{ expiryTime: "2026-09-01T00:00:00.000Z" }],
    }, testNow),
    null
  );
});

Deno.test("parseSubscriptionV2Response rejects missing expiryTime", () => {
  assertEquals(
    parseSubscriptionV2Response({
      subscriptionState: "ACTIVE",
      lineItems: [{ productId: "needsvswants_pro" }],
    }, testNow),
    null
  );
});

Deno.test("parseOneTimeProductV2Response accepts purchased state with data.productId", () => {
  const result = parseOneTimeProductV2Response({
    productId: "needsvswants_pro",
    purchaseState: 0,
  });
  assertEquals(result?.productId, "needsvswants_pro");
  assertEquals(result?.expiry, null);
});

Deno.test("parseOneTimeProductV2Response accepts purchased state with fallbackProductId", () => {
  const result = parseOneTimeProductV2Response({
    purchaseState: 0,
  }, "needsvswants_max");
  assertEquals(result?.productId, "needsvswants_max");
  assertEquals(result?.expiry, null);
});

Deno.test("parseOneTimeProductV2Response rejects non-purchased state", () => {
  assertEquals(parseOneTimeProductV2Response({ productId: "needsvswants_pro", purchaseState: 1 }), null);
});

Deno.test("parseOneTimeProductV2Response rejects missing productId", () => {
  assertEquals(parseOneTimeProductV2Response({ purchaseState: 0 }), null);
});