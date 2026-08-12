// Needs vs Wants - unit tests for signed PayPal custom_id helpers
// Run: deno test supabase/functions/_shared/paypal_custom_id.test.ts
import {
  assertEquals,
  assertRejects,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  candidateUserIdFor,
  classifyPayPalEventForTrust,
  decideGrantAcceptance,
  isSignedCustomIdFormat,
  mintSignedCustomId,
  SIGNED_CUSTOM_ID_MAX_AGE_MS,
  verifySignedCustomId,
} from "./paypal_custom_id.ts";

const SECRET = "paypal_custom_id_test_secret";
const USER_ID = "a1b2c3d4-1111-2222-3333-444455556666";
const NOW_MS = Date.parse("2026-08-13T00:00:00.000Z");

// ---------------------------------------------------------------------------
// mint + verify round trip
// ---------------------------------------------------------------------------

Deno.test("mint/verify: round trip accepts a fresh token", async () => {
  const token = await mintSignedCustomId(USER_ID, SECRET, NOW_MS);
  const result = await verifySignedCustomId(token, SECRET, NOW_MS);
  assertEquals(result.ok, true);
  if (result.ok) {
    assertEquals(result.user_id, USER_ID);
    assertEquals(result.issued_at_ms, Math.floor(NOW_MS / 1000) * 1000);
  }
});

Deno.test("mint: token shape is v1.<user>.<seconds>.<hex64> and fits PayPal's 127-char custom_id", async () => {
  const token = await mintSignedCustomId(USER_ID, SECRET, NOW_MS);
  const parts = token.split(".");
  assertEquals(parts.length, 4);
  assertEquals(parts[0], "v1");
  assertEquals(parts[1], USER_ID);
  assertEquals(/^\d+$/.test(parts[2]), true);
  assertEquals(/^[0-9a-f]{64}$/.test(parts[3]), true);
  assertEquals(token.length <= 127, true);
});

Deno.test("mint: rejects empty user id, dotted user id, and empty secret", async () => {
  await assertRejects(() => mintSignedCustomId("", SECRET));
  await assertRejects(() => mintSignedCustomId("user.with.dots", SECRET));
  await assertRejects(() => mintSignedCustomId(USER_ID, ""));
});

// ---------------------------------------------------------------------------
// verify: tamper rejection
// ---------------------------------------------------------------------------

Deno.test("verify: tampered user id fails with reason signature", async () => {
  const token = await mintSignedCustomId(USER_ID, SECRET, NOW_MS);
  const other = "ffffffff-0000-0000-0000-000000000000";
  const tampered = token.replace(USER_ID, other);
  const result = await verifySignedCustomId(tampered, SECRET, NOW_MS);
  assertEquals(result, { ok: false, reason: "signature", user_id: null });
});

Deno.test("verify: tampered issued_at fails with reason signature", async () => {
  const token = await mintSignedCustomId(USER_ID, SECRET, NOW_MS);
  const parts = token.split(".");
  parts[2] = String(Number(parts[2]) + 999999); // forge a fresher timestamp
  const result = await verifySignedCustomId(parts.join("."), SECRET, NOW_MS);
  assertEquals(result, { ok: false, reason: "signature", user_id: null });
});

Deno.test("verify: wrong secret fails with reason signature", async () => {
  const token = await mintSignedCustomId(USER_ID, SECRET, NOW_MS);
  const result = await verifySignedCustomId(token, "other_secret", NOW_MS);
  assertEquals(result, { ok: false, reason: "signature", user_id: null });
});

Deno.test("verify: empty verifier secret fails closed with reason signature", async () => {
  const token = await mintSignedCustomId(USER_ID, SECRET, NOW_MS);
  const result = await verifySignedCustomId(token, "", NOW_MS);
  assertEquals(result, { ok: false, reason: "signature", user_id: null });
});

// ---------------------------------------------------------------------------
// verify: expiry
// ---------------------------------------------------------------------------

Deno.test("verify: token just inside the 24h window is ok", async () => {
  const token = await mintSignedCustomId(USER_ID, SECRET, NOW_MS);
  const later = NOW_MS + SIGNED_CUSTOM_ID_MAX_AGE_MS - 1000;
  const result = await verifySignedCustomId(token, SECRET, later);
  assertEquals(result.ok, true);
});

Deno.test("verify: token older than 24h is expired but keeps the user id", async () => {
  const token = await mintSignedCustomId(USER_ID, SECRET, NOW_MS);
  const later = NOW_MS + SIGNED_CUSTOM_ID_MAX_AGE_MS + 60 * 1000;
  const result = await verifySignedCustomId(token, SECRET, later);
  assertEquals(result.ok, false);
  if (!result.ok && result.reason === "expired") {
    assertEquals(result.user_id, USER_ID);
  } else {
    throw new Error(`expected expired, got ${JSON.stringify(result)}`);
  }
});

// ---------------------------------------------------------------------------
// verify: legacy / malformed formats
// ---------------------------------------------------------------------------

Deno.test("verify: raw legacy uuid is reason format (not signed)", async () => {
  const result = await verifySignedCustomId(USER_ID, SECRET, NOW_MS);
  assertEquals(result, { ok: false, reason: "format", user_id: null });
});

async function reasonOf(value: string | null): Promise<string | null> {
  const result = await verifySignedCustomId(value, SECRET, NOW_MS);
  return result.ok ? null : result.reason;
}

Deno.test("verify: null / empty / junk values are reason format", async () => {
  assertEquals(await reasonOf(null), "format");
  assertEquals(await reasonOf(""), "format");
  assertEquals(await reasonOf("v2.a.1.deadbeef"), "format");
  assertEquals(await reasonOf("v1.user.notdigits." + "a".repeat(64)), "format");
  assertEquals(await reasonOf("v1.user.123.shortsig"), "format");
});

Deno.test("isSignedCustomIdFormat: signed tokens yes, legacy uuids no", async () => {
  const token = await mintSignedCustomId(USER_ID, SECRET, NOW_MS);
  assertEquals(isSignedCustomIdFormat(token), true);
  assertEquals(isSignedCustomIdFormat(USER_ID), false);
  assertEquals(isSignedCustomIdFormat(null), false);
  assertEquals(isSignedCustomIdFormat(""), false);
});

// ---------------------------------------------------------------------------
// classifyPayPalEventForTrust
// ---------------------------------------------------------------------------

Deno.test("classify: initial / extend / status / other buckets", () => {
  assertEquals(
    classifyPayPalEventForTrust("BILLING.SUBSCRIPTION.ACTIVATED"),
    "initial",
  );
  assertEquals(
    classifyPayPalEventForTrust("BILLING.SUBSCRIPTION.CREATED"),
    "initial",
  );
  assertEquals(
    classifyPayPalEventForTrust("BILLING.SUBSCRIPTION.PAYMENT.SUCCEEDED"),
    "extend",
  );
  assertEquals(
    classifyPayPalEventForTrust("BILLING.SUBSCRIPTION.REACTIVATED"),
    "extend",
  );
  assertEquals(
    classifyPayPalEventForTrust("BILLING.SUBSCRIPTION.REVISED"),
    "extend",
  );
  assertEquals(
    classifyPayPalEventForTrust("BILLING.SUBSCRIPTION.CANCELLED"),
    "status",
  );
  assertEquals(
    classifyPayPalEventForTrust("BILLING.SUBSCRIPTION.SUSPENDED"),
    "status",
  );
  assertEquals(
    classifyPayPalEventForTrust("BILLING.SUBSCRIPTION.EXPIRED"),
    "status",
  );
  assertEquals(classifyPayPalEventForTrust("PAYMENT.SALE.COMPLETED"), "other");
});

// ---------------------------------------------------------------------------
// candidateUserIdFor
// ---------------------------------------------------------------------------

Deno.test("candidate: ok and expired use the signed user id", () => {
  assertEquals(
    candidateUserIdFor(
      { ok: true, user_id: USER_ID, issued_at_ms: NOW_MS },
      "ignored",
    ),
    USER_ID,
  );
  assertEquals(
    candidateUserIdFor(
      { ok: false, reason: "expired", user_id: USER_ID, issued_at_ms: NOW_MS },
      "ignored",
    ),
    USER_ID,
  );
});

Deno.test("candidate: legacy format uses the raw custom_id only when uuid-shaped", () => {
  assertEquals(
    candidateUserIdFor({ ok: false, reason: "format", user_id: null }, USER_ID),
    USER_ID,
  );
  assertEquals(
    candidateUserIdFor(
      { ok: false, reason: "format", user_id: null },
      "not-a-uuid",
    ),
    null,
  );
});

Deno.test("candidate: tampered signature never yields a candidate", () => {
  assertEquals(
    candidateUserIdFor(
      { ok: false, reason: "signature", user_id: null },
      USER_ID,
    ),
    null,
  );
});

// ---------------------------------------------------------------------------
// decideGrantAcceptance
// ---------------------------------------------------------------------------

const OK = { ok: true as const, user_id: USER_ID, issued_at_ms: NOW_MS };
const EXPIRED = {
  ok: false as const,
  reason: "expired" as const,
  user_id: USER_ID,
  issued_at_ms: NOW_MS,
};
const TAMPERED = {
  ok: false as const,
  reason: "signature" as const,
  user_id: null,
};
const LEGACY = { ok: false as const, reason: "format" as const, user_id: null };

Deno.test("decide: fresh valid signature accepts all event classes", () => {
  for (const eventClass of ["initial", "extend", "status"] as const) {
    const d = decideGrantAcceptance({
      verification: OK,
      rawCustomId: "ignored",
      eventClass,
      hasPriorPayPalGrant: false,
    });
    assertEquals(d, { accept: true, user_id: USER_ID, reason: "signed_valid" });
  }
});

Deno.test("decide: expired signature accepts only when subscription already linked", () => {
  const linked = decideGrantAcceptance({
    verification: EXPIRED,
    rawCustomId: "ignored",
    eventClass: "extend",
    hasPriorPayPalGrant: true,
  });
  assertEquals(linked, {
    accept: true,
    user_id: USER_ID,
    reason: "signed_expired_linked",
  });

  const unlinked = decideGrantAcceptance({
    verification: EXPIRED,
    rawCustomId: "ignored",
    eventClass: "extend",
    hasPriorPayPalGrant: false,
  });
  assertEquals(unlinked.accept, false);
  assertEquals(unlinked.reason, "signed_expired_unlinked");
});

Deno.test("decide: tampered signature never accepts, even when linked", () => {
  const d = decideGrantAcceptance({
    verification: TAMPERED,
    rawCustomId: USER_ID,
    eventClass: "extend",
    hasPriorPayPalGrant: true,
  });
  assertEquals(d, { accept: false, user_id: null, reason: "signed_invalid" });
});

Deno.test("decide: legacy uuid renewal/status accepted only for linked users", () => {
  for (const eventClass of ["extend", "status"] as const) {
    const linked = decideGrantAcceptance({
      verification: LEGACY,
      rawCustomId: USER_ID,
      eventClass,
      hasPriorPayPalGrant: true,
    });
    assertEquals(linked, {
      accept: true,
      user_id: USER_ID,
      reason: "legacy_linked",
    });

    const unlinked = decideGrantAcceptance({
      verification: LEGACY,
      rawCustomId: USER_ID,
      eventClass,
      hasPriorPayPalGrant: false,
    });
    assertEquals(unlinked.accept, false);
    assertEquals(unlinked.reason, "legacy_unlinked");
  }
});

Deno.test("decide: legacy uuid NEVER accepted for first-time initial grants", () => {
  const d = decideGrantAcceptance({
    verification: LEGACY,
    rawCustomId: USER_ID,
    eventClass: "initial",
    hasPriorPayPalGrant: true, // even a linked user cannot legacy-activate
  });
  assertEquals(d, {
    accept: false,
    user_id: null,
    reason: "legacy_initial_rejected",
  });
});

Deno.test("decide: legacy non-uuid custom_id has no candidate", () => {
  const d = decideGrantAcceptance({
    verification: LEGACY,
    rawCustomId: "totally-not-a-uuid",
    eventClass: "extend",
    hasPriorPayPalGrant: true,
  });
  assertEquals(d, { accept: false, user_id: null, reason: "no_candidate" });
});
