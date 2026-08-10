// Needs vs Wants - paypal_create_subscription Edge Function
//
// Authenticated: creates a PayPal subscription for the caller and returns the
// PayPal approval URL so the Android app (or website) can open checkout.
//
// Body: { "tier": "pro" | "max" }  OR  { "plan_id": "P-..." }
// custom_id on the subscription is always the Supabase user id (for webhooks).

import { error, handleOptions, ok } from "../_shared/http.ts";
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
  body: { tier?: string; plan_id?: string },
): { planId: string; tier: "pro" | "max" } | null {
  const proPlan = Deno.env.get("PAYPAL_PLAN_PRO") ?? "";
  const maxPlan = Deno.env.get("PAYPAL_PLAN_MAX") ?? "";

  if (typeof body.plan_id === "string" && body.plan_id.startsWith("P-")) {
    const planId = body.plan_id;
    if (maxPlan && planId === maxPlan) return { planId, tier: "max" };
    if (proPlan && planId === proPlan) return { planId, tier: "pro" };
    if (/max/i.test(planId)) return { planId, tier: "max" };
    return { planId, tier: "pro" };
  }

  const tier = body.tier === "max" ? "max" : body.tier === "pro" ? "pro" : null;
  if (!tier) return null;
  const planId = tier === "max" ? maxPlan : proPlan;
  if (!planId) return null;
  return { planId, tier };
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

    let body: { tier?: string; plan_id?: string } = {};
    try {
      body = await req.json();
    } catch {
      body = {};
    }

    const resolved = resolvePlanId(body);
    if (!resolved) {
      return error(
        "Unknown plan. Set PAYPAL_PLAN_PRO / PAYPAL_PLAN_MAX secrets and pass tier pro|max.",
        400,
      );
    }

    const returnUrl = Deno.env.get("PAYPAL_RETURN_URL") ??
      "https://needs-vs-wants.vercel.app/paypal-return.html";
    const cancelUrl = Deno.env.get("PAYPAL_CANCEL_URL") ??
      "https://needs-vs-wants.vercel.app/paypal-cancel.html";

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
        custom_id: userId,
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
      plan_id: resolved.planId,
      approval_url: approve,
    });
  } catch (err) {
    const detail = err instanceof Error
      ? err.message + (err.cause ? ` (cause: ${String(err.cause)})` : "")
      : String(err);
    console.error("paypal_create_subscription error:", err);
    return error(`Internal error: ${detail}`, 500);
  }
});
