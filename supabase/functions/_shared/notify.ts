// Needs vs Wants - shared notify_signup helpers
//
// Email-capture validation for the soft-launch "get notified" form. The
// website POSTs { email, website } where `website` is a honeypot field that
// real users never fill (bots auto-complete every input). All decisions here
// are pure so they can be unit-tested with `deno test`; the notify_signup
// Edge Function wires them to the launch_notify table.
//
// This module is intentionally dependency-free (no supabase-js, no external
// imports), following the paymongo.ts factoring.

/** Hard cap from RFC 5321; longer strings are junk or abuse. */
export const EMAIL_MAX_LENGTH = 254;

/** Optional source tag cap (e.g. "hero", "footer"). */
export const SOURCE_MAX_LENGTH = 64;
export const DEFAULT_SIGNUP_SOURCE = "website";

/**
 * Pragmatic email shape check: one @, a non-empty local part, and a domain
 * with at least one dot and a 2+ char TLD. Not an RFC parser on purpose -
 * the goal is filtering junk/abuse, not adjudicating exotic addresses.
 */
export function isValidEmail(email: string): boolean {
  if (typeof email !== "string") return false;
  if (email.length === 0 || email.length > EMAIL_MAX_LENGTH) return false;
  return /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(email);
}

/** Canonical storage form: trimmed + lowercased (dedupe is case-insensitive). */
export function normalizeEmail(email: string): string {
  return email.trim().toLowerCase();
}

/** A honeypot trips when the hidden field arrives with ANY non-blank value. */
export function isHoneypotTripped(website: unknown): boolean {
  return typeof website === "string" ? website.trim().length > 0 : website != null;
}

export type NotifySignupValidation =
  | { ok: true; email: string; source: string }
  | { ok: false; error: "invalid_body" | "honeypot" | "invalid_email" };

/**
 * Validate a parsed notify_signup request body.
 *
 * Order matters: honeypot first (bots fail fast, and a bot's email is
 * worthless anyway), then email shape. The returned email is normalized and
 * ready to insert; `source` falls back to "website" when absent/invalid.
 */
export function validateNotifySignup(body: unknown): NotifySignupValidation {
  if (!body || typeof body !== "object" || Array.isArray(body)) {
    return { ok: false, error: "invalid_body" };
  }
  const record = body as Record<string, unknown>;

  if (isHoneypotTripped(record.website)) {
    return { ok: false, error: "honeypot" };
  }

  if (typeof record.email !== "string") {
    return { ok: false, error: "invalid_email" };
  }
  const email = normalizeEmail(record.email);
  if (!isValidEmail(email)) {
    return { ok: false, error: "invalid_email" };
  }

  const rawSource = record.source;
  const source =
    typeof rawSource === "string" &&
      rawSource.trim().length > 0 &&
      rawSource.trim().length <= SOURCE_MAX_LENGTH
      ? rawSource.trim()
      : DEFAULT_SIGNUP_SOURCE;

  return { ok: true, email, source };
}
