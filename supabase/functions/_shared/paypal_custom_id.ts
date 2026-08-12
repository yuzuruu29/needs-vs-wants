// Needs vs Wants - signed PayPal custom_id helpers
//
// Closes the D46-era trust caveat: the subscription custom_id used to be the
// raw Supabase user id, which any PayPal-capable client could set to an
// arbitrary value at checkout. The webhook signature authenticates PayPal,
// not the buyer, so the user id inside the event was unauthenticated.
//
// New format (minted server-side by paypal_create_subscription):
//
//   v1.<user_id>.<issued_at_seconds>.<hex hmac>
//
// where the HMAC is HMAC-SHA256(PAYPAL_CUSTOM_ID_SECRET, "<user_id>.<issued_at_seconds>")
// in lowercase hex. PayPal's custom_id field allows up to 127 characters; a
// UUID user id yields 115 characters, safely inside the limit.
//
// Verification (paypal_webhook) rejects invalid signatures outright and
// treats tokens older than SIGNED_CUSTOM_ID_MAX_AGE_MS as "expired". Expiry
// only hard-blocks FIRST-TIME grants: subscription approval happens within
// minutes of minting, but the same custom_id is replayed on every renewal
// webhook for the life of the subscription, so renewal-class events accept an
// expired-but-valid signature when the subscription is already linked to the
// user (payment_events ledger row or an existing paypal entitlement).
//
// This module is intentionally dependency-free so it can be unit-tested with
// `deno test` (no supabase-js, no external imports).

export const SIGNED_CUSTOM_ID_VERSION = "v1";
export const SIGNED_CUSTOM_ID_MAX_AGE_MS = 24 * 60 * 60 * 1000; // 24h

// ---------------------------------------------------------------------------
// Minting
// ---------------------------------------------------------------------------

/**
 * Mint a signed custom_id for a Supabase user id.
 *
 * Throws when the user id or secret is unusable (empty, or a user id that
 * contains "." which would corrupt the dot-delimited format). The caller
 * (paypal_create_subscription) fails closed on a missing secret before ever
 * calling this.
 */
export async function mintSignedCustomId(
  userId: string,
  secret: string,
  nowMs: number = Date.now(),
): Promise<string> {
  if (!userId || userId.includes(".")) {
    throw new Error("mintSignedCustomId: invalid user id");
  }
  if (!secret) {
    throw new Error("mintSignedCustomId: missing secret");
  }
  const issuedAt = Math.floor(nowMs / 1000).toString();
  const sig = await hmacHex(secret, `${userId}.${issuedAt}`);
  return `${SIGNED_CUSTOM_ID_VERSION}.${userId}.${issuedAt}.${sig}`;
}

// ---------------------------------------------------------------------------
// Verification
// ---------------------------------------------------------------------------

/**
 * True when the value is structurally a signed custom_id (v1.<id>.<ts>.<sig>).
 * Legacy custom_ids (raw user uuids) never match: they contain no dots.
 */
export function isSignedCustomIdFormat(value: string | null | undefined): boolean {
  return parseSignedCustomId(value) !== null;
}

export type CustomIdVerification =
  /** Signature valid and token fresh. */
  | { ok: true; user_id: string; issued_at_ms: number }
  /** Signature valid but older than the max age (renewal replays land here). */
  | { ok: false; reason: "expired"; user_id: string; issued_at_ms: number }
  /** Structurally signed but the signature does not verify. NEVER grant. */
  | { ok: false; reason: "signature"; user_id: null }
  /** Not in signed format at all (legacy raw user id path). */
  | { ok: false; reason: "format"; user_id: null };

/**
 * Verify a signed custom_id. Returns a discriminated result so the webhook
 * can distinguish tamper ("signature": always reject) from staleness
 * ("expired": acceptable for renewal-class events of an already-linked
 * subscription) from legacy values ("format": raw-uuid fallback rules apply).
 */
export async function verifySignedCustomId(
  value: string | null | undefined,
  secret: string,
  nowMs: number = Date.now(),
  maxAgeMs: number = SIGNED_CUSTOM_ID_MAX_AGE_MS,
): Promise<CustomIdVerification> {
  const parsed = parseSignedCustomId(value);
  if (!parsed) return { ok: false, reason: "format", user_id: null };
  if (!secret) return { ok: false, reason: "signature", user_id: null };

  const expected = await hmacHex(secret, `${parsed.userId}.${parsed.issuedAt}`);
  if (!timingSafeEqual(expected, parsed.sig)) {
    return { ok: false, reason: "signature", user_id: null };
  }

  const issuedAtMs = Number(parsed.issuedAt) * 1000;
  if (nowMs - issuedAtMs > maxAgeMs) {
    return {
      ok: false,
      reason: "expired",
      user_id: parsed.userId,
      issued_at_ms: issuedAtMs,
    };
  }
  return { ok: true, user_id: parsed.userId, issued_at_ms: issuedAtMs };
}

interface ParsedCustomId {
  userId: string;
  issuedAt: string;
  sig: string;
}

function parseSignedCustomId(
  value: string | null | undefined,
): ParsedCustomId | null {
  if (typeof value !== "string") return null;
  const parts = value.split(".");
  if (parts.length !== 4) return null;
  const [version, userId, issuedAt, sig] = parts;
  if (version !== SIGNED_CUSTOM_ID_VERSION) return null;
  if (!userId) return null;
  if (!/^\d{1,17}$/.test(issuedAt)) return null;
  if (!/^[0-9a-f]{64}$/.test(sig)) return null;
  return { userId, issuedAt, sig };
}

async function hmacHex(secret: string, message: string): Promise<string> {
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
    new TextEncoder().encode(message),
  );
  return Array.from(new Uint8Array(sig))
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
// Grant acceptance policy (webhook trust decision)
// ---------------------------------------------------------------------------

/**
 * Trust classes of PayPal webhook event types:
 * - "initial": first-time grants (ACTIVATED / CREATED). Strictest rules.
 * - "extend":  renewals / lifecycle changes of an existing subscription
 *              (PAYMENT.SUCCEEDED / REACTIVATED / REVISED).
 * - "status":  metadata-only flags (CANCELLED / SUSPENDED / EXPIRED); these
 *              never grant access, only update provider/source/status.
 * - "other":   events the webhook ignores anyway.
 */
export type PayPalEventTrustClass = "initial" | "extend" | "status" | "other";

export function classifyPayPalEventForTrust(
  eventType: string,
): PayPalEventTrustClass {
  switch (eventType) {
    case "BILLING.SUBSCRIPTION.ACTIVATED":
    case "BILLING.SUBSCRIPTION.CREATED":
      return "initial";
    case "BILLING.SUBSCRIPTION.PAYMENT.SUCCEEDED":
    case "BILLING.SUBSCRIPTION.REACTIVATED":
    case "BILLING.SUBSCRIPTION.REVISED":
      return "extend";
    case "BILLING.SUBSCRIPTION.CANCELLED":
    case "BILLING.SUBSCRIPTION.SUSPENDED":
    case "BILLING.SUBSCRIPTION.EXPIRED":
      return "status";
    default:
      return "other";
  }
}

const UUID_RE =
  /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;

/**
 * Candidate user id the webhook should run its linkage lookups against
 * (payment_events ledger / entitlements row). Null when no candidate exists
 * (tampered signature, or a legacy value that is not even a uuid).
 */
export function candidateUserIdFor(
  verification: CustomIdVerification,
  rawCustomId: string,
): string | null {
  if (verification.ok) return verification.user_id;
  switch (verification.reason) {
    case "expired":
      return verification.user_id;
    case "format":
      return UUID_RE.test(rawCustomId) ? rawCustomId : null;
    case "signature":
      return null;
  }
}

export interface GrantAcceptance {
  accept: boolean;
  /** The authoritative user id to grant to (only meaningful when accept). */
  user_id: string | null;
  /** Machine-readable reason, logged by the webhook. */
  reason:
    | "signed_valid"
    | "signed_expired_linked"
    | "signed_expired_unlinked"
    | "signed_invalid"
    | "legacy_linked"
    | "legacy_initial_rejected"
    | "legacy_unlinked"
    | "no_candidate";
}

/**
 * Decide whether a verified-by-PayPal webhook event may grant/update the
 * entitlement for the user identified by its custom_id.
 *
 * - Fresh valid signature: always accept (all event classes).
 * - Valid-but-expired signature: accept only when the subscription is already
 *   linked to the same user (ledger row or paypal entitlement) - this is the
 *   renewal-replay path, since custom_id never changes after checkout.
 * - Tampered signature: never accept.
 * - Legacy raw uuid: accept only for extend/status events of a user that is
 *   already linked to PayPal; NEVER for first-time initial grants. This keeps
 *   pre-cutover subscribers renewing while denying forged first grants.
 */
export function decideGrantAcceptance(args: {
  verification: CustomIdVerification;
  rawCustomId: string;
  eventClass: PayPalEventTrustClass;
  hasPriorPayPalGrant: boolean;
}): GrantAcceptance {
  const { verification, rawCustomId, eventClass, hasPriorPayPalGrant } = args;
  const candidate = candidateUserIdFor(verification, rawCustomId);
  if (!candidate) {
    return {
      accept: false,
      user_id: null,
      reason: verification.ok === false && verification.reason === "signature"
        ? "signed_invalid"
        : "no_candidate",
    };
  }

  if (verification.ok) {
    return { accept: true, user_id: candidate, reason: "signed_valid" };
  }

  if (verification.reason === "expired") {
    return hasPriorPayPalGrant
      ? { accept: true, user_id: candidate, reason: "signed_expired_linked" }
      : {
        accept: false,
        user_id: null,
        reason: "signed_expired_unlinked",
      };
  }

  // Legacy raw-uuid custom_id (pre-cutover subscriptions).
  if (eventClass === "initial") {
    return {
      accept: false,
      user_id: null,
      reason: "legacy_initial_rejected",
    };
  }
  return hasPriorPayPalGrant
    ? { accept: true, user_id: candidate, reason: "legacy_linked" }
    : { accept: false, user_id: null, reason: "legacy_unlinked" };
}
