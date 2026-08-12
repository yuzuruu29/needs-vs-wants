// Needs vs Wants - unit tests for soft rate-limit helpers
// Run: deno test supabase/functions/_shared/rate_limit.test.ts
import {
  assertEquals,
  assertNotEquals,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  CHECKOUT_MAX_PER_HOUR,
  clientIpFrom,
  hashIpForRateLimit,
  NOTIFY_SIGNUP_MAX_PER_HOUR,
  RATE_LIMIT_WINDOW_MS,
  rateLimitKey,
  shouldRateLimit,
  windowStartIso,
} from "./rate_limit.ts";

Deno.test("rateLimitKey: scope prefixes keep buckets from colliding", () => {
  assertEquals(rateLimitKey("notify", "abc123"), "notify:abc123");
  assertEquals(rateLimitKey("checkout", "user-1"), "checkout:user-1");
  assertNotEquals(rateLimitKey("notify", "x"), rateLimitKey("checkout", "x"));
});

Deno.test("windowStartIso: subtracts the window from now", () => {
  const nowMs = Date.parse("2026-08-13T12:00:00.000Z");
  assertEquals(windowStartIso(nowMs), "2026-08-13T11:00:00.000Z");
  assertEquals(
    windowStartIso(nowMs, 15 * 60 * 1000),
    "2026-08-13T11:45:00.000Z",
  );
  assertEquals(RATE_LIMIT_WINDOW_MS, 60 * 60 * 1000);
});

Deno.test("shouldRateLimit: allows below max, refuses at and beyond max", () => {
  assertEquals(shouldRateLimit(0, 5), false);
  assertEquals(shouldRateLimit(4, 5), false);
  assertEquals(shouldRateLimit(5, 5), true);
  assertEquals(shouldRateLimit(6, 5), true);
  // Configured product limits.
  assertEquals(shouldRateLimit(NOTIFY_SIGNUP_MAX_PER_HOUR - 1, NOTIFY_SIGNUP_MAX_PER_HOUR), false);
  assertEquals(shouldRateLimit(NOTIFY_SIGNUP_MAX_PER_HOUR, NOTIFY_SIGNUP_MAX_PER_HOUR), true);
  assertEquals(shouldRateLimit(CHECKOUT_MAX_PER_HOUR, CHECKOUT_MAX_PER_HOUR), true);
});

Deno.test("clientIpFrom: first hop of x-forwarded-for, unknown fallback", () => {
  assertEquals(clientIpFrom("203.0.113.7"), "203.0.113.7");
  assertEquals(clientIpFrom("203.0.113.7, 10.0.0.1, 10.0.0.2"), "203.0.113.7");
  assertEquals(clientIpFrom(" 203.0.113.7 ,10.0.0.1"), "203.0.113.7");
  assertEquals(clientIpFrom(null), "unknown");
  assertEquals(clientIpFrom(""), "unknown");
  assertEquals(clientIpFrom(" , 10.0.0.1"), "unknown");
});

Deno.test("hashIpForRateLimit: stable, 32 hex chars, never the raw IP", async () => {
  const a1 = await hashIpForRateLimit("203.0.113.7");
  const a2 = await hashIpForRateLimit("203.0.113.7");
  const b = await hashIpForRateLimit("203.0.113.8");
  assertEquals(a1, a2);
  assertNotEquals(a1, b);
  assertEquals(/^[0-9a-f]{32}$/.test(a1), true);
  assertNotEquals(a1.includes("203"), true);
});
