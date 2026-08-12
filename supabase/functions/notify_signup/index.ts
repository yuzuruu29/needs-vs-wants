// Needs vs Wants - notify_signup Edge Function
//
// Email capture for the soft-launch website's "get notified" form (the form
// previously posted to "#" and discarded leads). Deployed with
// `--no-verify-jwt`: website visitors have no Supabase session.
//
// Contract (website JS):
//   POST JSON { "email": string, "website": string }
//     - `website` is a HONEYPOT and must be empty (bots fill every field).
//     - optional `source` tag (e.g. "hero"), defaults to "website".
//   200 { "ok": true }                       stored, or duplicate (idempotent)
//   400 { "ok": false, "error": ... }        invalid email / honeypot filled
//   429 { "ok": false, "error": ... }        per-IP rate limit (5/hour)
//   405 { "ok": false, "error": ... }        non-POST
//
// CORS allows only the production site origin. Abuse controls: email shape
// validation, honeypot, and a soft per-IP sliding-window rate limit keyed on
// a SHA-256 hash of the client IP (raw IPs are never stored). The rate limit
// FAILS OPEN on counter errors - it is abuse damping, not a security gate.

import {
  clientIpFrom,
  hashIpForRateLimit,
  NOTIFY_SIGNUP_MAX_PER_HOUR,
  rateLimitKey,
  shouldRateLimit,
  windowStartIso,
} from "../_shared/rate_limit.ts";
import { validateNotifySignup } from "../_shared/notify.ts";
import {
  createClient,
} from "https://esm.sh/@supabase/supabase-js@2.45.4";

// Only the production website may call this from a browser. (Non-browser
// callers ignore CORS entirely; the honeypot + rate limit handle those.)
const ALLOWED_ORIGIN = "https://needs-vs-wants.vercel.app";

const corsHeaders = {
  "Access-Control-Allow-Origin": ALLOWED_ORIGIN,
  "Access-Control-Allow-Methods": "POST, OPTIONS",
  "Access-Control-Allow-Headers": "content-type",
  "Access-Control-Max-Age": "86400",
};

function json(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { ...corsHeaders, "Content-Type": "application/json" },
  });
}

Deno.serve(async (req: Request) => {
  if (req.method === "OPTIONS") {
    return new Response(null, { status: 204, headers: corsHeaders });
  }
  if (req.method !== "POST") {
    return json({ ok: false, error: "Method not allowed" }, 405);
  }

  try {
    let raw: unknown = null;
    try {
      raw = await req.json();
    } catch {
      raw = null;
    }

    // Validation first: garbage is rejected without touching the database.
    const validated = validateNotifySignup(raw);
    if (!validated.ok) {
      return json(
        {
          ok: false,
          error: validated.error === "invalid_email"
            ? "Invalid email"
            : "Invalid request",
        },
        400,
      );
    }

    const supabaseUrl = Deno.env.get("SUPABASE_URL");
    const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
    if (!supabaseUrl || !serviceRoleKey) {
      return json({ ok: false, error: "Server not configured" }, 500);
    }
    const supabase = createClient(supabaseUrl, serviceRoleKey, {
      auth: { persistSession: false },
    });

    // Soft per-IP rate limit: count this hour's attempts, refuse at the max,
    // then record this attempt. Counter errors fail OPEN (log + continue).
    const ip = clientIpFrom(req.headers.get("x-forwarded-for"));
    const key = rateLimitKey("notify", await hashIpForRateLimit(ip));

    const { count, error: countErr } = await supabase
      .from("rate_limit_events")
      .select("id", { count: "exact", head: true })
      .eq("key", key)
      .gte("created_at", windowStartIso(Date.now()));

    if (countErr) {
      console.error("notify_signup rate-limit count error:", countErr);
    } else if (shouldRateLimit(count ?? 0, NOTIFY_SIGNUP_MAX_PER_HOUR)) {
      return json({ ok: false, error: "Too many requests" }, 429);
    }

    const { error: attemptErr } = await supabase
      .from("rate_limit_events")
      .insert({ key });
    if (attemptErr) {
      console.error("notify_signup rate-limit insert error:", attemptErr);
    }

    // Store the email. A unique violation means this address is already on
    // the list - that is success (idempotent), not an error.
    const { error: insertErr } = await supabase
      .from("launch_notify")
      .insert({ email: validated.email, source: validated.source });

    if (insertErr) {
      if (typeof insertErr.code === "string" && insertErr.code === "23505") {
        return json({ ok: true });
      }
      console.error("launch_notify insert error:", insertErr);
      return json({ ok: false, error: "Internal error" }, 500);
    }

    return json({ ok: true });
  } catch (err) {
    console.error("notify_signup error:", err);
    return json({ ok: false, error: "Internal error" }, 500);
  }
});
