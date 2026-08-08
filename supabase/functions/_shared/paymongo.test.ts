// Needs vs Wants - unit tests for shared PayMongo helpers
// Run: deno test supabase/functions/_shared/paymongo.test.ts
import {
  assertEquals,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  expectedAmountCentavos,
  mapCheckoutPaidEvent,
  nextPaidUntil,
  resolveGrantTier,
  verifyPaymongoSignature,
} from "./paymongo.ts";

// ---------------------------------------------------------------------------
// verifyPaymongoSignature
// ---------------------------------------------------------------------------

async function hmacHex(secret: string, body: string): Promise<string> {
  const key = await crypto.subtle.importKey(
    "raw",
    new TextEncoder().encode(secret),
    { name: "HMAC", hash: "SHA-256" },
    false,
    ["sign"],
  );
  const sig = await crypto.subtle.sign(
    "HMAC",
    key,
    new TextEncoder().encode(body),
  );
  return Array.from(new Uint8Array(sig))
    .map((b) => b.toString(16).padStart(2, "0"))
    .join("");
}

const SECRET = "whsk_test_mysecret";
const BODY = '{"data":{"type":"checkout_session.payment.paid"}}';

Deno.test("verifyPaymongoSignature: valid signature passes", async () => {
  const header = await hmacHex(SECRET, BODY);
  assertEquals(await verifyPaymongoSignature(BODY, header, SECRET), true);
});

Deno.test("verifyPaymongoSignature: tampered body fails", async () => {
  const header = await hmacHex(SECRET, BODY);
  assertEquals(
    await verifyPaymongoSignature(BODY + "tampered", header, SECRET),
    false,
  );
});

Deno.test("verifyPaymongoSignature: wrong header value fails", async () => {
  const header = await hmacHex("other_secret", BODY);
  assertEquals(await verifyPaymongoSignature(BODY, header, SECRET), false);
});

Deno.test("verifyPaymongoSignature: empty/missing header fails", async () => {
  assertEquals(await verifyPaymongoSignature(BODY, null, SECRET), false);
  assertEquals(await verifyPaymongoSignature(BODY, "", SECRET), false);
});

Deno.test("verifyPaymongoSignature: malformed (non-hex / wrong length) header fails", async () => {
  assertEquals(
    await verifyPaymongoSignature(BODY, "not-a-hex-digest", SECRET),
    false,
  );
  assertEquals(
    await verifyPaymongoSignature(BODY, "abc", SECRET),
    false,
  );
});

// ---------------------------------------------------------------------------
// expectedAmountCentavos
// ---------------------------------------------------------------------------

Deno.test("expectedAmountCentavos: pro 19900, max 39900 (server-authoritative)", () => {
  assertEquals(expectedAmountCentavos("pro"), 19900);
  assertEquals(expectedAmountCentavos("max"), 39900);
});

// ---------------------------------------------------------------------------
// nextPaidUntil (stacking)
// ---------------------------------------------------------------------------

const NOW = "2026-08-08T12:00:00.000Z";
const NOW_MS = Date.parse(NOW);
const DAY_MS = 24 * 60 * 60 * 1000;

Deno.test("nextPaidUntil: existing in the future stacks (existing + 30d)", () => {
  const future = new Date(NOW_MS + 5 * DAY_MS).toISOString(); // 5 days out
  const result = nextPaidUntil(NOW, future);
  assertEquals(Date.parse(result), Date.parse(future) + 30 * DAY_MS);
});

Deno.test("nextPaidUntil: existing null bases on now (now + 30d)", () => {
  const result = nextPaidUntil(NOW, null);
  assertEquals(Date.parse(result), NOW_MS + 30 * DAY_MS);
});

Deno.test("nextPaidUntil: existing undefined bases on now", () => {
  const result = nextPaidUntil(NOW, undefined);
  assertEquals(Date.parse(result), NOW_MS + 30 * DAY_MS);
});

Deno.test("nextPaidUntil: expired existing bases on now (now + 30d)", () => {
  const expired = new Date(NOW_MS - 10 * DAY_MS).toISOString();
  const result = nextPaidUntil(NOW, expired);
  assertEquals(Date.parse(result), NOW_MS + 30 * DAY_MS);
});

Deno.test("nextPaidUntil: custom days honored", () => {
  const result = nextPaidUntil(NOW, null, 7);
  assertEquals(Date.parse(result), NOW_MS + 7 * DAY_MS);
});

// ---------------------------------------------------------------------------
// mapCheckoutPaidEvent
// ---------------------------------------------------------------------------

const FULL_PAID_EVENT = {
  data: {
    type: "checkout_session.payment.paid",
    id: "evt_123",
    attributes: {
      amount: 19900,
      billing: { address: {} },
      checkout_session: {
        id: "cs_abc",
        metadata: {
          user_id: "a1b2c3d4-1111-2222-3333-444455556666",
          tier: "pro",
          product: "nvw_manual_month",
          app: "needs-vs-wants",
        },
        payments: [
          {
            id: "pay_ignore",
            status: "pending",
            amount: 19900,
          },
          {
            id: "pay_granted",
            status: "paid",
            amount: 19900,
          },
        ],
      },
      payments: [],
      paid_at: "2026-08-08T12:00:00.000Z",
    },
  },
};

Deno.test("mapCheckoutPaidEvent: full paid event maps to a Grant", () => {
  const grant = mapCheckoutPaidEvent(FULL_PAID_EVENT);
  assertEquals(grant, {
    user_id: "a1b2c3d4-1111-2222-3333-444455556666",
    tier: "pro",
    payment_id: "pay_granted",
    amount_centavos: 19900,
    checkout_session_id: "cs_abc",
    paid_at: "2026-08-08T12:00:00.000Z",
  });
});

Deno.test("mapCheckoutPaidEvent: ignores non-paid payments (no paid found -> null)", () => {
  const payload = {
    data: {
      type: "checkout_session.payment.paid",
      attributes: {
        checkout_session: {
          id: "cs_abc",
          metadata: { user_id: "user-1", tier: "max" },
          payments: [
            { id: "pay_1", status: "pending", amount: 39900 },
            { id: "pay_2", status: "failed", amount: 39900 },
          ],
        },
        payments: [],
      },
    },
  };
  assertEquals(mapCheckoutPaidEvent(payload), null);
});

Deno.test("mapCheckoutPaidEvent: missing user_id -> null", () => {
  const payload = {
    data: {
      type: "checkout_session.payment.paid",
      attributes: {
        checkout_session: {
          metadata: { tier: "pro" },
          payments: [{ id: "pay_1", status: "paid", amount: 19900 }],
        },
      },
    },
  };
  assertEquals(mapCheckoutPaidEvent(payload), null);
});

Deno.test("mapCheckoutPaidEvent: missing tier -> null", () => {
  const payload = {
    data: {
      type: "checkout_session.payment.paid",
      attributes: {
        checkout_session: {
          metadata: { user_id: "user-1" },
          payments: [{ id: "pay_1", status: "paid", amount: 19900 }],
        },
      },
    },
  };
  assertEquals(mapCheckoutPaidEvent(payload), null);
});

Deno.test("mapCheckoutPaidEvent: invalid tier -> null", () => {
  const payload = {
    data: {
      type: "checkout_session.payment.paid",
      attributes: {
        checkout_session: {
          metadata: { user_id: "user-1", tier: "gold" },
          payments: [{ id: "pay_1", status: "paid", amount: 19900 }],
        },
      },
    },
  };
  assertEquals(mapCheckoutPaidEvent(payload), null);
});

Deno.test("mapCheckoutPaidEvent: null / non-object / malformed -> null", () => {
  assertEquals(mapCheckoutPaidEvent(null), null);
  assertEquals(mapCheckoutPaidEvent("junk"), null);
  assertEquals(mapCheckoutPaidEvent({}), null);
  assertEquals(mapCheckoutPaidEvent({ data: { attributes: null } }), null);
});

Deno.test("mapCheckoutPaidEvent: REAL shape - resource at data.data (outer envelope)", () => {
  // Hosted-checkout guide: outer `data` is the event envelope (type/id), the
  // checkout-session resource is at `data.data` (its `.attributes` holds
  // metadata + payments).
  const payload = {
    data: {
      type: "checkout_session.payment.paid",
      id: "evt_777",
      data: {
        id: "cs_777",
        type: "checkout_session",
        attributes: {
          metadata: { user_id: "user-resource-data", tier: "pro" },
          payments: [
            { id: "pay_777", status: "paid", amount: 19900 },
          ],
          line_items: [{ name: "Pro", amount: 19900 }],
          paid_at: "2026-08-08T00:00:00.000Z",
        },
      },
    },
  };
  const grant = mapCheckoutPaidEvent(payload);
  assertEquals(grant, {
    user_id: "user-resource-data",
    tier: "pro",
    payment_id: "pay_777",
    amount_centavos: 19900,
    checkout_session_id: "cs_777",
    paid_at: "2026-08-08T00:00:00.000Z",
  });
});

Deno.test("mapCheckoutPaidEvent: REAL shape - resource at data.attributes.data", () => {
  // Webhooks-events page: event envelope `data` has `attributes`, and the
  // checkout-session resource sits at `data.attributes.data`.
  const payload = {
    data: {
      type: "checkout_session.payment.paid",
      id: "evt_888",
      attributes: {
        data: {
          id: "cs_888",
          type: "checkout_session",
          attributes: {
            metadata: { user_id: "user-attr-data", tier: "max" },
            payments: [
              { id: "pay_888", status: "paid", amount: 39900 },
            ],
            line_items: [{ name: "Max", amount: 39900 }],
            paid_at: "2026-08-08T00:00:00.000Z",
          },
        },
      },
    },
  };
  const grant = mapCheckoutPaidEvent(payload);
  assertEquals(grant, {
    user_id: "user-attr-data",
    tier: "max",
    payment_id: "pay_888",
    amount_centavos: 39900,
    checkout_session_id: "cs_888",
    paid_at: "2026-08-08T00:00:00.000Z",
  });
});

Deno.test("mapCheckoutPaidEvent: payments at envelope level (no nested checkout_session)", () => {
  // Variant where attributes IS the checkout session (payments at top level).
  const payload = {
    data: {
      type: "checkout_session.payment.paid",
      attributes: {
        id: "cs_xyz",
        metadata: { user_id: "user-9", tier: "max" },
        payments: [{ id: "pay_99", status: "paid", amount: 39900 }],
        paid_at: "2026-08-08T00:00:00.000Z",
      },
    },
  };
  const grant = mapCheckoutPaidEvent(payload);
  assertEquals(grant?.user_id, "user-9");
  assertEquals(grant?.tier, "max");
  assertEquals(grant?.payment_id, "pay_99");
  assertEquals(grant?.amount_centavos, 39900);
  assertEquals(grant?.checkout_session_id, "cs_xyz");
});

// ---------------------------------------------------------------------------
// resolveGrantTier
// ---------------------------------------------------------------------------

const NOW_FOR_TIER = "2026-08-08T12:00:00.000Z";

Deno.test("resolveGrantTier: max payment always upgrades to max", () => {
  assertEquals(resolveGrantTier("max", null, NOW_FOR_TIER), "max");
  assertEquals(
    resolveGrantTier("max", { is_pro: true, tier: "pro" as const, trial_ends_at: null, paid_until: null }, NOW_FOR_TIER),
    "max",
  );
});

Deno.test("resolveGrantTier: pro renewal keeps active max tier", () => {
  const activeMax = { is_pro: true, tier: "max" as const, trial_ends_at: null, paid_until: "2026-09-01T00:00:00.000Z" };
  assertEquals(resolveGrantTier("pro", activeMax, NOW_FOR_TIER), "max");
});

Deno.test("resolveGrantTier: pro renewal while max inventoried but expired -> pro", () => {
  const expiredMax = { is_pro: true, tier: "max" as const, trial_ends_at: null, paid_until: "2026-08-01T00:00:00.000Z" };
  assertEquals(resolveGrantTier("pro", expiredMax, NOW_FOR_TIER), "pro");
});

Deno.test("resolveGrantTier: pro renewal on pro / free / no row -> pro", () => {
  assertEquals(resolveGrantTier("pro", null, NOW_FOR_TIER), "pro");
  assertEquals(
    resolveGrantTier("pro", { is_pro: true, tier: "pro", trial_ends_at: null, paid_until: null }, NOW_FOR_TIER),
    "pro",
  );
});