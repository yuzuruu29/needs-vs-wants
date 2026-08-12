// Needs vs Wants - shared PayMongo helpers
//
// PayMongo manual-renewal checkout: Pro (₱49/mo or ₱490/yr) / Max (₱99/mo or
// ₱990/yr). Monthly grants 30 days; annual grants 365 days.
//
// This module is intentionally dependency-free so it can be unit-tested with
// `deno test` (no supabase-js, no external imports). Edge Functions import it
// alongside the Supabase client.
//
// Amounts are server-authoritative: Pro 4900 / Max 9900 centavos monthly,
// Pro 49000 / Max 99000 centavos annual. A client may never supply an amount.

import {
  isEntitlementActive,
  type EntitlementRow,
} from "./entitlements.ts";

// ---------------------------------------------------------------------------
// Amounts (cents / centavos, server-authoritative)
// ---------------------------------------------------------------------------

export const PAYMONGO_AMOUNT_PRO = 4900;
export const PAYMONGO_AMOUNT_MAX = 9900;
export const PAYMONGO_AMOUNT_PRO_ANNUAL = 49000;
export const PAYMONGO_AMOUNT_MAX_ANNUAL = 99000;
export const PAYMONGO_GRANT_DAYS = 30;
export const PAYMONGO_GRANT_DAYS_ANNUAL = 365;

/** Billing period for a manual-renewal checkout. */
export type BillingPeriod = "monthly" | "annual";

/** Server-authoritative checkout amount in centavos for a tier + period. */
export function expectedAmountCentavos(
  tier: "pro" | "max",
  period: BillingPeriod = "monthly",
): number {
  if (period === "annual") {
    return tier === "max"
      ? PAYMONGO_AMOUNT_MAX_ANNUAL
      : PAYMONGO_AMOUNT_PRO_ANNUAL;
  }
  return tier === "max" ? PAYMONGO_AMOUNT_MAX : PAYMONGO_AMOUNT_PRO;
}

/** Grant length in days for a billing period. */
export function grantDaysFor(period: BillingPeriod): number {
  return period === "annual" ? PAYMONGO_GRANT_DAYS_ANNUAL : PAYMONGO_GRANT_DAYS;
}

// ---------------------------------------------------------------------------
// Signature verification
// ---------------------------------------------------------------------------

/**
 * Verify a PayMongo `Paymongo-Signature` header against the raw request body.
 *
 * PayMongo signs the RAW request body with the webhook secret; the header is
 * the lowercase hex SHA-256 HMAC digest (`hmac = hex( HMAC_SHA256( secret,
 * rawBody ) )`). We compare using a constant-time / timing-safe approach.
 *
 * Returns false on missing header, wrong header format, or digest mismatch.
 */
export async function verifyPaymongoSignature(
  rawBody: string,
  header: string | null,
  secret: string,
): Promise<boolean> {
  if (!header || !secret) return false;
  const expected = header.trim().toLowerCase();
  if (!/^[0-9a-f]{64}$/.test(expected)) return false;

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
    new TextEncoder().encode(rawBody),
  );
  const actual = hexOf(sig);

  return timingSafeEqual(actual, expected);
}

function hexOf(buffer: ArrayBuffer): string {
  return Array.from(new Uint8Array(buffer))
    .map((b) => b.toString(16).padStart(2, "0"))
    .join("");
}

/** Length-preserving, timing-safe equality of two lowercase hex strings. */
function timingSafeEqual(a: string, b: string): boolean {
  if (a.length !== b.length) return false;
  let diff = 0;
  for (let i = 0; i < a.length; i++) {
    diff |= a.charCodeAt(i) ^ b.charCodeAt(i);
  }
  return diff === 0;
}

// ---------------------------------------------------------------------------
// paid_until stacking
// ---------------------------------------------------------------------------

/**
 * Compute the next paid_until for a manual-renewal grant.
 *
 * Returns `max(now, existingPaidUntil) + days * 24h` in ISO. If the existing
 * paid_until is null/undefined/expired, the base is just `now`, so timely
 * renewals STACK (+30 days from the current expiry) and late renewals simply
 * grant from now.
 */
export function nextPaidUntil(
  nowIso: string,
  existingPaidUntil: string | null | undefined,
  days: number = PAYMONGO_GRANT_DAYS,
): string {
  const now = Date.parse(nowIso);
  const base = Number.isNaN(now) ? Date.now() : now;

  let existing = 0;
  if (existingPaidUntil) {
    const parsed = Date.parse(existingPaidUntil);
    if (!Number.isNaN(parsed)) existing = parsed;
  }

  const ceiling = Math.max(base, existing);
  return new Date(ceiling + days * 24 * 60 * 60 * 1000).toISOString();
}

// ---------------------------------------------------------------------------
// Webhook payload mapping
// ---------------------------------------------------------------------------

export interface Grant {
  user_id: string;
  tier: "pro" | "max";
  period: BillingPeriod;
  payment_id: string;
  amount_centavos: number;
  checkout_session_id: string | null;
  paid_at: string | null;
}

/**
 * Map a PayMongo `checkout_session.payment.paid` webhook payload to a Grant.
 *
 * DEFENSIVE DEEP-FINDER for the resource attributes. PayMongo has shipped
 * multiple envelope shapes over time, and the checkout-session resource (the
 * object carrying `metadata` and `payments`) can sit at different depths:
 *
 *   A) { data: { type, id, attributes: <resource> } }            (wiki/guide)
 *   B) { data: { attributes: { data: <resource> } } }             (webhooks page)
 *   C) { data: { attributes: <resource> } }                       (common form)
 *   D) { data: <resource> }                                       (bare)
 *   E) <resource> wrapped in a `checkout_session` field
 *
 * We gather candidate objects from ALL plausible paths and pick the one that
 * is an object AND has a `metadata` object AND has a `payments` (or
 * `line_items`) array - that is the resource attributes. Extraction then reads
 * user_id/tier from its `metadata`, the paid payment from its `payments[]`
 * (first with `status === "paid"`), and the amount from the paid payment (or
 * line-items fallback).
 *
 * Returns null only when genuinely no paid payment / no user_id/tier is found,
 * or the payload is malformed.
 */
export function mapCheckoutPaidEvent(payload: unknown): Grant | null {
  const found = findResourceAttributes(payload);
  if (!found) return null;
  const resource = found.resource;

  const metadata = (resource.metadata ?? {}) as Record<string, unknown>;
  const user_id = typeof metadata.user_id === "string" && metadata.user_id
    ? metadata.user_id
    : null;
  const tierRaw = typeof metadata.tier === "string" ? metadata.tier : null;
  const tier: "pro" | "max" | null =
    tierRaw === "pro" || tierRaw === "max" ? tierRaw : null;
  if (!user_id || !tier) return null;
  // Billing period rides in checkout metadata; absent metadata means monthly.
  const period: BillingPeriod =
    metadata.period === "annual" ? "annual" : "monthly";

  const checkoutSessionId = found.checkoutSessionId;
  const paidAt = found.paidAt;

  const payments = Array.isArray(resource.payments)
    ? resource.payments
    : [];

  const paidPayment = payments
    .filter((p): p is Record<string, unknown> =>
      !!p && typeof p === "object")
    .find((p) => p.status === "paid");

  if (!paidPayment) return null;

  const paymentId = typeof paidPayment.id === "string" ? paidPayment.id : null;
  if (!paymentId) return null;

  // Amount: prefer the paid payment's own amount; fall back to the checkout
  // session line total. Always integer centavos.
  let amountCentavos: number | null =
    typeof paidPayment.amount === "number"
      ? paidPayment.amount
      : typeof paidPayment.amount === "string"
        ? Number(paidPayment.amount)
        : null;
  if (amountCentavos === null || Number.isNaN(amountCentavos)) {
    const lines = Array.isArray(resource.line_items)
      ? resource.line_items
      : [];
    const line = lines.find((l): l is Record<string, unknown> =>
      !!l && typeof l === "object");
    const lineAmount =
      typeof line?.amount === "number"
        ? line.amount
        : typeof line?.amount === "string"
          ? Number(line.amount)
          : null;
    amountCentavos = lineAmount !== null && !Number.isNaN(lineAmount)
      ? lineAmount
      : null;
  }
  if (amountCentavos === null) return null;

  return {
    user_id,
    tier,
    period,
    payment_id: paymentId,
    amount_centavos: amountCentavos,
    checkout_session_id: checkoutSessionId,
    paid_at: paidAt,
  };
}

/**
 * Deep-find the resource-attributes object (the one carrying `metadata` and
 * `payments`/`line_items`) from any PayMongo envelope shape. Also recovers the
 * checkout-session id and paid_at, which may live on wrapper objects (the
 * resource's parent `id`) or the event envelope rather than on the resource
 * attributes itself.
 */
interface ResourceFind {
  resource: Record<string, unknown>;
  checkoutSessionId: string | null;
  paidAt: string | null;
}

function findResourceAttributes(payload: unknown): ResourceFind | null {
  if (!payload || typeof payload !== "object") return null;

  interface Candidate {
    obj: Record<string, unknown>;
    checkoutSessionId: string | null;
    paidAt: string | null;
  }
  const candidates: Candidate[] = [];
  const seen = new Set<unknown>();

  const push = (
    value: unknown,
    checkoutSessionId: string | null = null,
    paidAt: string | null = null,
  ): void => {
    if (!value || typeof value !== "object" || seen.has(value)) return;
    seen.add(value);
    candidates.push({ obj: value as Record<string, unknown>, checkoutSessionId, paidAt });
  };

  const root = payload as Record<string, unknown>;
  const data = root.data;
  push(data);

  if (data && typeof data === "object") {
    const d = data as Record<string, unknown>;
    const dId = typeof d.id === "string" ? d.id : null;
    const dAttrs =
      d.attributes && typeof d.attributes === "object"
        ? d.attributes as Record<string, unknown>
        : null;
    const dPaidAt =
      dAttrs && typeof dAttrs.paid_at === "string"
        ? dAttrs.paid_at
        : typeof d.paid_at === "string"
          ? d.paid_at
          : null;

    push(d.attributes, dId, dPaidAt);
    push(d.data, dId, dPaidAt);

    // `data.attributes` may itself be a wrapper carrying `data`.
    const attrs = d.attributes;
    if (attrs && typeof attrs === "object") {
      const a = attrs as Record<string, unknown>;
      const aId = typeof a.id === "string" ? a.id : dId;
      const aPaidAt = typeof a.paid_at === "string" ? a.paid_at : dPaidAt;
      // `a.data` may be the resource wrapper; its `.attributes` is the resource.
      const aData = a.data;
      if (aData && typeof aData === "object") {
        const ad = aData as Record<string, unknown>;
        push(ad, aId, aPaidAt);
        const adAttrs =
          ad.attributes && typeof ad.attributes === "object"
            ? ad.attributes as Record<string, unknown>
            : null;
        if (adAttrs) {
          push(
            adAttrs,
            typeof ad.id === "string" ? ad.id : aId,
            typeof adAttrs.paid_at === "string" ? adAttrs.paid_at : aPaidAt,
          );
        }
      }
      push(a.attributes, aId, aPaidAt);
      // The wrapper's own `attributes` may be the resource (paid_at nested).
      const aNestedAttrs = a.attributes;
      if (aNestedAttrs && typeof aNestedAttrs === "object") {
        const na = aNestedAttrs as Record<string, unknown>;
        push(na, aId, typeof na.paid_at === "string" ? na.paid_at : aPaidAt);
      }
      const cs = a.checkout_session;
      if (cs && typeof cs === "object") {
        const csObj = cs as Record<string, unknown>;
        const csId = typeof csObj.id === "string" ? csObj.id : aId;
        push(csObj.attributes, csId, aPaidAt);
        push(csObj, csId, aPaidAt);
      }
    }
    // `data.data` may be the resource or a wrapper.
    const dData = d.data;
    if (dData && typeof dData === "object") {
      const dd = dData as Record<string, unknown>;
      const ddId = typeof dd.id === "string" ? dd.id : dId;
      const ddAttrs =
        dd.attributes && typeof dd.attributes === "object"
          ? dd.attributes as Record<string, unknown>
          : null;
      const ddPaidAt =
        ddAttrs && typeof ddAttrs.paid_at === "string"
          ? ddAttrs.paid_at
          : typeof dd.paid_at === "string"
            ? dd.paid_at
            : dPaidAt;
      // `data.data.attributes` is very commonly the resource itself.
      push(dd.attributes, ddId, ddPaidAt);
      if (ddAttrs) push(ddAttrs, ddId, ddPaidAt);
      const cs = dd.checkout_session;
      if (cs && typeof cs === "object") {
        const csObj = cs as Record<string, unknown>;
        const csId = typeof csObj.id === "string" ? csObj.id : ddId;
        push(csObj.attributes, csId, ddPaidAt);
        push(csObj, csId, ddPaidAt);
      }
    }
    const dCs = d.checkout_session;
    if (dCs && typeof dCs === "object") {
      const csObj = dCs as Record<string, unknown>;
      const csId = typeof csObj.id === "string" ? csObj.id : dId;
      push(csObj.attributes, csId, dPaidAt);
      push(csObj, csId, dPaidAt);
    }
  }

  // Qualify: the resource attributes has a `metadata` object AND a
  // `payments` or `line_items` array. Prefer the resource's own `id` /
  // `paid_at` when present, falling back to the wrapper's values.
  for (const c of candidates) {
    const obj = c.obj;
    const hasMetadata = !!obj.metadata &&
      typeof obj.metadata === "object" &&
      obj.metadata !== null;
    const hasPayments = Array.isArray(obj.payments);
    const hasLineItems = Array.isArray(obj.line_items);
    if (hasMetadata && (hasPayments || hasLineItems)) {
      return {
        resource: obj,
        checkoutSessionId:
          typeof obj.id === "string" ? obj.id : c.checkoutSessionId,
        paidAt:
          typeof obj.paid_at === "string" ? obj.paid_at : c.paidAt,
      };
    }
  }

  return null;
}

// ---------------------------------------------------------------------------
// Tier resolution (Max wins while active)
// ---------------------------------------------------------------------------

/**
 * Resolve the tier to write after a manual renewal grant.
 *
 * A Max payment always upgrades to max. A Pro renewal while the current Max
 * tier is still active keeps max (extending paid_until) rather than
 * downgrading. Otherwise the grant tier is used.
 */
export function resolveGrantTier(
  grantTier: "pro" | "max",
  current: Pick<
    EntitlementRow,
    "is_pro" | "tier" | "trial_ends_at" | "paid_until"
  > | null,
  nowIso: string,
): "pro" | "max" {
  if (grantTier === "max") return "max";
  // grantTier === "pro": keep max only if the current row is max AND still active.
  if (current && current.tier === "max" && isEntitlementActive(current, nowIso)) {
    return "max";
  }
  return "pro";
}