// Needs vs Wants - shared soft rate-limit helpers
//
// Table-backed sliding-window counters shared by notify_signup (per-IP) and
// the checkout/subscription creation functions (per-user). The generic
// rate_limit_events table stores one row per attempt (key, created_at);
// callers COUNT rows for their key inside the window and refuse when the
// count has reached the max, then INSERT their own attempt row.
//
// This is SOFT abuse control (fail-open on storage errors - a broken counter
// must never block a paying customer), not a hard security boundary.
//
// Pure helpers only in this module (no supabase-js, no external imports) so
// they are unit-testable with `deno test`; DB wiring lives in the functions.

export const RATE_LIMIT_WINDOW_MS = 60 * 60 * 1000; // 1 hour

/** notify_signup: max signup attempts per IP hash per hour. */
export const NOTIFY_SIGNUP_MAX_PER_HOUR = 5;

/** checkout/subscription creation: max sessions per user per hour. */
export const CHECKOUT_MAX_PER_HOUR = 10;

export type RateLimitScope = "notify" | "checkout";

/**
 * Bucket key stored in rate_limit_events. Scope prefix keeps notify (IP hash)
 * and checkout (user id) counters from ever colliding.
 */
export function rateLimitKey(scope: RateLimitScope, id: string): string {
  return `${scope}:${id}`;
}

/** ISO lower bound of the sliding window for the COUNT query. */
export function windowStartIso(
  nowMs: number,
  windowMs: number = RATE_LIMIT_WINDOW_MS,
): string {
  return new Date(nowMs - windowMs).toISOString();
}

/**
 * Refuse when the attempts already recorded inside the window have reached
 * the max (the current attempt is only inserted AFTER passing this check, so
 * `recentCount` excludes it).
 */
export function shouldRateLimit(recentCount: number, max: number): boolean {
  return recentCount >= max;
}

/**
 * First client IP from an x-forwarded-for header ("client, proxy1, proxy2").
 * Falls back to "unknown" so header-less callers still share one soft bucket.
 */
export function clientIpFrom(forwardedFor: string | null): string {
  if (!forwardedFor) return "unknown";
  const first = forwardedFor.split(",")[0]?.trim() ?? "";
  return first.length > 0 ? first : "unknown";
}

/**
 * SHA-256 hash of the client IP, truncated to 32 hex chars. The raw IP is
 * never stored; the hash only needs to be stable within the rate window.
 */
export async function hashIpForRateLimit(ip: string): Promise<string> {
  const digest = await crypto.subtle.digest(
    "SHA-256",
    new TextEncoder().encode(ip),
  );
  return Array.from(new Uint8Array(digest))
    .map((b) => b.toString(16).padStart(2, "0"))
    .join("")
    .slice(0, 32);
}
