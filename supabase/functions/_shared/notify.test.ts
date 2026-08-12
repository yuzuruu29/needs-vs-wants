// Needs vs Wants - unit tests for notify_signup helpers
// Run: deno test supabase/functions/_shared/notify.test.ts
import {
  assertEquals,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  DEFAULT_SIGNUP_SOURCE,
  EMAIL_MAX_LENGTH,
  isHoneypotTripped,
  isValidEmail,
  normalizeEmail,
  validateNotifySignup,
} from "./notify.ts";

// ---------------------------------------------------------------------------
// isValidEmail
// ---------------------------------------------------------------------------

Deno.test("isValidEmail: accepts ordinary addresses", () => {
  assertEquals(isValidEmail("user@example.com"), true);
  assertEquals(isValidEmail("first.last+tag@sub.domain.ph"), true);
  assertEquals(isValidEmail("a@b.co"), true);
});

Deno.test("isValidEmail: rejects junk shapes", () => {
  assertEquals(isValidEmail(""), false);
  assertEquals(isValidEmail("plainaddress"), false);
  assertEquals(isValidEmail("@no-local.com"), false);
  assertEquals(isValidEmail("no-at-sign.com"), false);
  assertEquals(isValidEmail("no-tld@domain"), false);
  assertEquals(isValidEmail("one-char-tld@domain.x"), false);
  assertEquals(isValidEmail("spaces in@local.com"), false);
  assertEquals(isValidEmail("user@do main.com"), false);
  assertEquals(isValidEmail("two@@ats.com"), false);
});

Deno.test("isValidEmail: rejects over-length addresses (RFC 5321 cap)", () => {
  const local = "a".repeat(EMAIL_MAX_LENGTH); // way past 254 once domain added
  assertEquals(isValidEmail(`${local}@example.com`), false);
});

// ---------------------------------------------------------------------------
// normalizeEmail
// ---------------------------------------------------------------------------

Deno.test("normalizeEmail: trims and lowercases for case-insensitive dedupe", () => {
  assertEquals(normalizeEmail("  User@Example.COM  "), "user@example.com");
  assertEquals(normalizeEmail("already@lower.com"), "already@lower.com");
});

// ---------------------------------------------------------------------------
// isHoneypotTripped
// ---------------------------------------------------------------------------

Deno.test("honeypot: empty / whitespace / absent values do NOT trip", () => {
  assertEquals(isHoneypotTripped(""), false);
  assertEquals(isHoneypotTripped("   "), false);
  assertEquals(isHoneypotTripped(undefined), false);
  assertEquals(isHoneypotTripped(null), false);
});

Deno.test("honeypot: any filled value trips (string or non-string)", () => {
  assertEquals(isHoneypotTripped("http://spam.example"), true);
  assertEquals(isHoneypotTripped("x"), true);
  assertEquals(isHoneypotTripped(42), true);
  assertEquals(isHoneypotTripped({ nested: true }), true);
});

// ---------------------------------------------------------------------------
// validateNotifySignup
// ---------------------------------------------------------------------------

Deno.test("validate: happy path normalizes email and defaults source", () => {
  const result = validateNotifySignup({
    email: " Reader@Example.COM ",
    website: "",
  });
  assertEquals(result, {
    ok: true,
    email: "reader@example.com",
    source: DEFAULT_SIGNUP_SOURCE,
  });
});

Deno.test("validate: custom source is honored when sane", () => {
  const result = validateNotifySignup({
    email: "reader@example.com",
    website: "",
    source: " hero ",
  });
  assertEquals(result.ok, true);
  if (result.ok) assertEquals(result.source, "hero");
});

Deno.test("validate: over-length or non-string source falls back to default", () => {
  const long = validateNotifySignup({
    email: "reader@example.com",
    website: "",
    source: "s".repeat(65),
  });
  assertEquals(long.ok, true);
  if (long.ok) assertEquals(long.source, DEFAULT_SIGNUP_SOURCE);

  const nonString = validateNotifySignup({
    email: "reader@example.com",
    website: "",
    source: 123,
  });
  assertEquals(nonString.ok, true);
  if (nonString.ok) assertEquals(nonString.source, DEFAULT_SIGNUP_SOURCE);
});

Deno.test("validate: filled honeypot rejects before email is even considered", () => {
  const result = validateNotifySignup({
    email: "not-even-an-email",
    website: "https://bot.example",
  });
  assertEquals(result, { ok: false, error: "honeypot" });
});

Deno.test("validate: invalid or missing email rejects", () => {
  assertEquals(
    validateNotifySignup({ email: "junk", website: "" }),
    { ok: false, error: "invalid_email" },
  );
  assertEquals(
    validateNotifySignup({ website: "" }),
    { ok: false, error: "invalid_email" },
  );
  assertEquals(
    validateNotifySignup({ email: 42, website: "" }),
    { ok: false, error: "invalid_email" },
  );
});

Deno.test("validate: non-object bodies reject as invalid_body", () => {
  assertEquals(validateNotifySignup(null), { ok: false, error: "invalid_body" });
  assertEquals(validateNotifySignup("json string"), {
    ok: false,
    error: "invalid_body",
  });
  assertEquals(validateNotifySignup([1, 2]), {
    ok: false,
    error: "invalid_body",
  });
});
