// Needs vs Wants - paymongo_create_checkout Edge Function
//
// Authenticated: creates a PayMongo Hosted Checkout Session for the caller and
// returns the checkout_url so the Android app can open it in a browser.
//
// Body: { "tier": "pro" | "max", "period": "monthly" | "annual" }
//
// Amounts are SERVER-AUTHORITATIVE only: monthly Pro 4900 / Max 9900 centavos;
// annual Pro 49000 / Max 99000 centavos. Any client-supplied amount is ignored.
//
// Renewal is manual and one-time: each call creates a fresh Hosted Checkout
// Session. There is no auto-subscription. The grant (is_pro, tier, +30 days)
// is applied by paymongo_webhook when PayMongo reports
// `checkout_session.payment.paid`.

import { error, handleOptions, ok, requireEnv } from "../_shared/http.ts";
import { expectedAmountCentavos } from "../_shared/paymongo.ts";
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

    // Confirm the JWT is still valid with Supabase Auth (same as
    // paypal_create_subscription).
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

    // Fail closed if the PayMongo secret is not configured.
    let secretKey: string;
    try {
      secretKey = requireEnv("PAYMONGO_SECRET_KEY");
    } catch (e) {
      console.error("paymongo_create_checkout:", e);
      return error("Server not configured", 500);
    }

    let body: { tier?: string; period?: string } = {};
    try {
      body = await req.json();
    } catch {
      body = {};
    }

    const tier: "pro" | "max" | null =
      body.tier === "max" ? "max" : body.tier === "pro" ? "pro" : null;
    if (!tier) return error("Invalid tier. Must be 'pro' or 'max'.", 400);

    const period = body.period === "annual" ? "annual" : "monthly";

    // Server-authoritative amount. Client-supplied amounts are ignored.
    const amountCentavos = expectedAmountCentavos(tier, period);

    const successUrl = Deno.env.get("PAYMONGO_SUCCESS_URL") ??
      "https://needs-vs-wants.vercel.app/paymongo-return.html";
    const cancelUrl = Deno.env.get("PAYMONGO_CANCEL_URL") ??
      "https://needs-vs-wants.vercel.app/paymongo-cancel.html";

    const paymentMethods = (
      Deno.env.get("PAYMONGO_PAYMENT_METHODS") ??
      "gcash,card,paymaya,grab_pay,qrph"
    ).split(",").map((s) => s.trim()).filter(Boolean);

    const referenceNumber =
      `nvw_${userId.slice(0, 8)}_${tier}_${period}_${Date.now()}`;

    const productName = period === "annual"
      ? (tier === "max"
          ? "Needs vs Wants Max — 12 months"
          : "Needs vs Wants Pro — 12 months")
      : (tier === "max"
          ? "Needs vs Wants Max — 1 month"
          : "Needs vs Wants Pro — 1 month");
    const description = period === "annual"
      ? "Manual renewal · 365 days access"
      : "Manual renewal · 30 days access";

    const secretBase64 = btoa(`${secretKey}:`);

    const createRes = await fetch(
      "https://api.paymongo.com/v2/checkout_sessions",
      {
        method: "POST",
        headers: {
          Authorization: `Basic ${secretBase64}`,
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify({
          data: {
            attributes: {
              line_items: [
                {
                  name: productName,
                  amount: amountCentavos,
                  currency: "PHP",
                  quantity: 1,
                  description,
                },
              ],
              payment_method_types: paymentMethods,
              success_url: successUrl,
              cancel_url: cancelUrl,
              reference_number: referenceNumber,
              send_email_receipt: true,
              metadata: {
                user_id: userId,
                tier,
                period,
                product: period === "annual" ? "nvw_manual_annual" : "nvw_manual_month",
                app: "needs-vs-wants",
              },
            },
          },
        }),
      },
    );

    const createJson = await createRes.json() as {
      data?: {
        id?: string;
        attributes?: {
          checkout_url?: string;
          payments?: Array<Record<string, unknown>>;
        };
      };
      checkout_url?: string;
      message?: string;
      errors?: Array<{ detail?: string }>;
    };

    if (!createRes.ok) {
      console.error("PayMongo create checkout failed", createRes.status, createJson);
      const detail =
        typeof createJson.message === "string"
          ? createJson.message
          : Array.isArray(createJson.errors) &&
              typeof createJson.errors[0]?.detail === "string"
            ? createJson.errors[0].detail
            : "PayMongo create checkout failed";
      return error(detail, 502);
    }

    // Accept both the nested `data.attributes.checkout_url` and a flat
    // top-level `checkout_url`.
    const checkoutUrl =
      createJson.data?.attributes?.checkout_url ??
      createJson.checkout_url ??
      null;
    if (!checkoutUrl) {
      return error("PayMongo response missing checkout_url", 502);
    }

    const checkoutSessionId = createJson.data?.id ?? null;

    return ok({
      checkout_url: checkoutUrl,
      checkout_session_id: checkoutSessionId,
      tier,
      period,
      amount_centavos: amountCentavos,
    });
  } catch (err) {
    const detail = err instanceof Error
      ? err.message + (err.cause ? ` (cause: ${String(err.cause)})` : "")
      : String(err);
    console.error("paymongo_create_checkout error:", err);
    return error(`Internal error: ${detail}`, 500);
  }
});