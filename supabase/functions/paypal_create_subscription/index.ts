// Needs vs Wants - paypal_create_subscription Edge Function
//
// Authenticated: creates a PayPal subscription for the caller and returns the
// PayPal approval URL so the Android app (or website) can open checkout.
//
// Body: { "tier": "pro" | "max", "period": "monthly" | "annual" }  OR
//       { "plan_id": "P-..." }
//
// custom_id on the subscription is an HMAC-SIGNED token minted from the
// verified caller's user id (`v1.<user_id>.<issued_at>.<sig>`, secret
// PAYPAL_CUSTOM_ID_SECRET). paypal_webhook verifies it before granting, so a
// client can no longer bind an arbitrary user id to a subscription. Fails
// closed (500) when the secret is not configured.
//
// Soft per-user rate limit: max 10 subscription creations per user per hour
// (rate_limit_events counter, shared with paymongo_create_checkout).
//
// Plan ids come from secrets: monthly PAYPAL_PLAN_PRO / PAYPAL_PLAN_MAX;
// annual PAYPAL_PLAN_PRO_ANNUAL / PAYPAL_PLAN_MAX_ANNUAL (optional). The
// billing interval itself lives on the PayPal plan in the dashboard.

import { sanitizeScheme, withSchemeParam } from "../_shared/deeplink.ts";
import { error, handleOptions, ok } from "../_shared/http.ts";
import { mintSignedCustomId } from "../_shared/paypal_custom_id.ts";
import {
  CHECKOUT_MAX_PER_HOUR,
  rateLimitKey,
  shouldRateLimit,
  windowStartIso,
} from "../_shared/rate_limit.ts";
import {
  createClient,
} from "https://esm.sh/@supabase/supabase-js@2.45.4";

interface DecodedJwt {
  sub?: string;
  [key: string]: unknown;
}

function decodeJwtPayload(token: string): DecodedJwt | null {
  const parts = token.split(".");
  if (parts.length !== 3) return null;
  const payload = parts[1];
  if (!payload) return null;
  try {
    const normalized = payload.replace(/-/g, "+").replace(/_/g, "/");
    const padded = normalized.padEnd(
      normalized.length + ((4 - (normalized.length % 4)) % 4),
      "=",
    );
    return JSON.parse(
      new TextDecoder().decode(
        Uint8Array.from(atob(padded), (c) => c.charCodeAt(0)),
      ),
    ) as DecodedJwt;
  } catch {
    return null;
  }
}

function apiBase(): string {
  return Deno.env.get("PAYPAL_ENVIRONMENT") === "live"
    ? "https://api-m.paypal.com"
    : "https://api-m.sandbox.paypal.com";
}

async function getOAuthToken(
  clientId: string,
  clientSecret: string,
): Promise<string> {
  const creds = btoa(`${clientId}:${clientSecret}`);
  const res = await fetch(`${apiBase()}/v1/oauth2/token`, {
    method: "POST",
    headers: {
      Authorization: `Basic ${creds}`,
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: "grant_type=client_credentials",
  });
  if (!res.ok) {
    const body = await res.text().catch(() => "");
    throw new Error(
      `PayPal OAuth failed: HTTP ${res.status} ${body.slice(0, 200)}`,
    );
  }
  const json = await res.json() as { access_token?: string };
  if (!json.access_token) throw new Error("PayPal OAuth missing access_token");
  return json.access_token;
}

function resolvePlanId(
  body: { tier?: string; plan_id?: string; period?: string },
): { planId: string; tier: "pro" | "max"; period: "monthly" | "annual" } | null {
  const period = body.period === "annual" ? "annual" : "monthly";
  const proPlan = Deno.env.get("PAYPAL_PLAN_PRO") ?? "";
  const maxPlan = Deno.env.get("PAYPAL_PLAN_MAX") ?? "";
  const proAnnualPlan = Deno.env.get("PAYPAL_PLAN_PRO_ANNUAL") ?? "";
  const maxAnnualPlan = Deno.env.get("PAYPAL_PLAN_MAX_ANNUAL") ?? "";

  if (typeof body.plan_id === "string" && body.plan_id.startsWith("P-")) {
    const planId = body.plan_id;
    if (maxAnnualPlan && planId === maxAnnualPlan) {
      return { planId, tier: "max", period: "annual" };
    }
    if (proAnnualPlan && planId === proAnnualPlan) {
      return { planId, tier: "pro", period: "annual" };
    }
    if (maxPlan && planId === maxPlan) return { planId, tier: "max", period };
    if (proPlan && planId === proPlan) return { planId, tier: "pro", period };
    if (/max/i.test(planId)) return { planId, tier: "max", period };
    return { planId, tier: "pro", period };
  }

  const tier = body.tier === "max" ? "max" : body.tier === "pro" ? "pro" : null;
  if (!tier) return null;
  const planId = period === "annual"
    ? (tier === "max" ? maxAnnualPlan : proAnnualPlan)
    : (tier === "max" ? maxPlan : proPlan);
  if (!planId) return null;
  return { planId, tier, period };
}

Deno.serve(async (req: Request) => {
  if (req.method === "OPTIONS") return handleOptions();
  if (req.method !== "POST") return error("Method not allowed", 405);

  try {
    const auth = req.headers.get("authorization") ?? "";
    const token = auth.startsWith("Bearer ") ? auth.slice(7) : null;
    if (!token) return error("Missing bearer token", 401);

    const claims = decodeJwtPayload(token);
    const userId = claims?.sub;
    if (!userId) return error("Invalid token", 401);

    // Confirm the JWT is still valid with Supabase Auth.
    const supabaseUrl = Deno.env.get("SUPABASE_URL");
    const anonKey = Deno.env.get("SUPABASE_ANON_KEY");
    if (!supabaseUrl || !anonKey) return error("Server not configured", 500);

    const supabase = createClient(supabaseUrl, anonKey, {
      global: { headers: { Authorization: `Bearer ${token}` } },
      auth: { persistSession: false },
    });
    const { data: userData, error: userErr } = await supabase.auth.getUser();
    if (userErr || !userData?.user?.id) {
      return error("Unauthorized", 401);
    }
    if (userData.user.id !== userId) {
      return error("Token subject mismatch", 401);
    }

    const clientId = Deno.env.get("PAYPAL_CLIENT_ID");
    const clientSecret = Deno.env.get("PAYPAL_CLIENT_SECRET");
    if (!clientId || !clientSecret) return error("PayPal not configured", 500);

    // Fail closed: without the signing secret we must not fall back to a raw
    // (spoofable) custom_id. Set PAYPAL_CUSTOM_ID_SECRET before deploying.
    const customIdSecret = Deno.env.get("PAYPAL_CUSTOM_ID_SECRET");
    if (!customIdSecret) {
      console.error(
        "paypal_create_subscription: PAYPAL_CUSTOM_ID_SECRET is not set",
      );
      return error("Server not configured", 500);
    }

    // Soft per-user rate limit (service role: rate_limit_events has no client
    // policies). Fails OPEN - a broken counter must never block checkout.
    const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
    if (serviceRoleKey) {
      const admin = createClient(supabaseUrl, serviceRoleKey, {
        auth: { persistSession: false },
      });
      const key = rateLimitKey("checkout", userId);
      const { count, error: countErr } = await admin
        .from("rate_limit_events")
        .select("id", { count: "exact", head: true })
        .eq("key", key)
        .gte("created_at", windowStartIso(Date.now()));
      if (countErr) {
        console.error(
          "paypal_create_subscription rate-limit count error:",
          countErr,
        );
      } else if (shouldRateLimit(count ?? 0, CHECKOUT_MAX_PER_HOUR)) {
        return error("Too many checkout attempts. Try again later.", 429);
      }
      const { error: attemptErr } = await admin
        .from("rate_limit_events")
        .insert({ key });
      if (attemptErr) {
        console.error(
          "paypal_create_subscription rate-limit insert error:",
          attemptErr,
        );
      }
    } else {
      console.error(
        "paypal_create_subscription: SUPABASE_SERVICE_ROLE_KEY missing; rate limit skipped",
      );
    }

    let body: { tier?: string; plan_id?: string; period?: string; scheme?: string } = {};
    try {
      body = await req.json();
    } catch {
      body = {};
    }

    const resolved = resolvePlanId(body);
    if (!resolved) {
      const wantsAnnual = body.period === "annual";
      return error(
        wantsAnnual
          ? "Annual PayPal plan not configured. Set PAYPAL_PLAN_PRO_ANNUAL / PAYPAL_PLAN_MAX_ANNUAL secrets."
          : "Unknown plan. Set PAYPAL_PLAN_PRO / PAYPAL_PLAN_MAX secrets and pass tier pro|max.",
        400,
      );
    }

    // Whitelisted deep-link scheme forwarded to the redirect pages so the
    // plain test flavor receives its checkout returns (see _shared/deeplink.ts).
    const scheme = sanitizeScheme(body.scheme);

    const returnUrl = withSchemeParam(
      Deno.env.get("PAYPAL_RETURN_URL") ??
        "https://needs-vs-wants.vercel.app/paypal-return.html",
      scheme,
    );
    const cancelUrl = withSchemeParam(
      Deno.env.get("PAYPAL_CANCEL_URL") ??
        "https://needs-vs-wants.vercel.app/paypal-cancel.html",
      scheme,
    );

    const accessToken = await getOAuthToken(clientId, clientSecret);
    const createRes = await fetch(`${apiBase()}/v1/billing/subscriptions`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
        Prefer: "return=representation",
      },
      body: JSON.stringify({
        plan_id: resolved.planId,
        custom_id: await mintSignedCustomId(userId, customIdSecret),
        application_context: {
          brand_name: "Needs vs Wants",
          locale: "en-US",
          shipping_preference: "NO_SHIPPING",
          user_action: "SUBSCRIBE_NOW",
          return_url: returnUrl,
          cancel_url: cancelUrl,
        },
      }),
    });

    const createJson = await createRes.json() as {
      id?: string;
      status?: string;
      links?: Array<{ rel?: string; href?: string }>;
      message?: string;
      details?: unknown;
    };

    if (!createRes.ok) {
      console.error("PayPal create subscription failed", createRes.status, createJson);
      return error(
        typeof createJson.message === "string"
          ? createJson.message
          : "PayPal create subscription failed",
        502,
      );
    }

    const approve = createJson.links?.find((l) => l.rel === "approve")?.href;
    if (!approve) {
      return error("PayPal response missing approval link", 502);
    }

    return ok({
      subscription_id: createJson.id ?? null,
      status: createJson.status ?? null,
      tier: resolved.tier,
      period: resolved.period,
      plan_id: resolved.planId,
      approval_url: approve,
    });
  } catch (err) {
    // Detail stays server-side only: internal messages can leak config or
    // upstream response fragments to clients.
    console.error("paypal_create_subscription error:", err);
    return error("Internal error", 500);
  }
});
