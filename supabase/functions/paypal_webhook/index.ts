// Needs vs Wants - paypal_webhook Edge Function
// Task 2: Pro subscription backend scaffolding.
//
// Receives PayPal webhook events, VERIFIES their signature via the PayPal
// `verify-webhook-signature` API, then upserts the caller's entitlements row.
//
// Mandatory verification (locked decision, never bypass):
//   - PayPal sends PAYPAL-TRANSMISSION-* headers.
//   - We POST {auth_algo, cert_url, transmission_id, transmission_sig,
//     transmission_time, webhook_id, webhook_event} to
//     /v1/notifications/verify-webhook-signature with an OAuth bearer token.
//   - Only a verification_status === "SUCCESS" is accepted.
//
// Idempotent: each webhook event maps to an idempotent upsert keyed by
// user_id (with on_conflict), so replays do not double-grant.
//
// NOTE: deployed with `--no-verify-jwt` (PayPal is not an authenticated
// Supabase client). Signature verification is the only auth gate.

import { error, handleOptions, jsonResponse } from "../_shared/http.ts";
import {
  grantToRowFields,
  mapPayPalWebhookEvent,
} from "../_shared/entitlements.ts";
import {
  createClient,
} from "https://esm.sh/@supabase/supabase-js@2.45.4";

interface PayPalVerificationResponse {
  verification_status: "SUCCESS" | "FAILURE";
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
    throw new Error(`PayPal OAuth failed: ${res.status}`);
  }
  const data = await res.json() as { access_token?: string };
  if (!data.access_token) throw new Error("PayPal OAuth: no access_token");
  return data.access_token;
}

async function verifyWebhook(
  req: Request,
  event: unknown,
  clientId: string,
  clientSecret: string,
  webhookId: string,
): Promise<boolean> {
  const authAlgo = req.headers.get("paypal-auth-algo");
  const certUrl = req.headers.get("paypal-cert-url");
  const transmissionId = req.headers.get("paypal-transmission-id");
  const transmissionSig = req.headers.get("paypal-transmission-sig");
  const transmissionTime = req.headers.get("paypal-transmission-time");

  if (
    !authAlgo || !certUrl || !transmissionId || !transmissionSig ||
    !transmissionTime
  ) {
    return false;
  }

  const token = await getOAuthToken(clientId, clientSecret);
  const body = {
    auth_algo: authAlgo,
    cert_url: certUrl,
    transmission_id: transmissionId,
    transmission_sig: transmissionSig,
    transmission_time: transmissionTime,
    webhook_id: webhookId,
    webhook_event: event,
  };

  const res = await fetch(
    `${apiBase()}/v1/notifications/verify-webhook-signature`,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    },
  );
  if (!res.ok) {
    console.error("verify-webhook-signature HTTP", res.status);
    return false;
  }
  const result = await res.json() as PayPalVerificationResponse;
  return result.verification_status === "SUCCESS";
}

Deno.serve(async (req: Request) => {
  if (req.method === "OPTIONS") return handleOptions();
  if (req.method !== "POST") {
    return error("Method not allowed", 405);
  }

  try {
    const clientId = Deno.env.get("PAYPAL_CLIENT_ID");
    const clientSecret = Deno.env.get("PAYPAL_CLIENT_SECRET");
    const webhookId = Deno.env.get("PAYPAL_WEBHOOK_ID");
    if (!clientId || !clientSecret || !webhookId) {
      return error("Server not configured", 500);
    }

    const event: unknown = await req.json();

    const verified = await verifyWebhook(
      req,
      event,
      clientId,
      clientSecret,
      webhookId,
    );
    if (!verified) {
      return error("Webhook signature verification failed", 401);
    }

    const mapped = mapPayPalWebhookEvent(
      event,
      Deno.env.get("PAYPAL_PLAN_PRO"),
      Deno.env.get("PAYPAL_PLAN_MAX"),
      Deno.env.get("PAYPAL_PLAN_PRO_ANNUAL"),
      Deno.env.get("PAYPAL_PLAN_MAX_ANNUAL"),
    );
    if (!mapped) {
      // Known event types we ignore (payment, auth, etc.) - ack so PayPal
      // stops retrying; nothing to grant.
      return jsonResponse({ success: true, ignored: true });
    }

    const supabaseUrl = Deno.env.get("SUPABASE_URL");
    const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
    if (!supabaseUrl || !serviceRoleKey) {
      return error("Server not configured", 500);
    }

    const supabase = createClient(supabaseUrl, serviceRoleKey, {
      auth: { persistSession: false },
    });

    const fields = grantToRowFields(mapped.grant);
    const { error: upsertError } = await supabase
      .from("entitlements")
      .upsert(
        {
          user_id: mapped.user_id,
          ...fields,
          updated_at: new Date().toISOString(),
        },
        { onConflict: "user_id" },
      );

    if (upsertError) {
      console.error("entitlements upsert error:", upsertError);
      return error("Failed to apply grant", 500);
    }

    return jsonResponse({ success: true, applied: mapped.grant.mode });
  } catch (err) {
    console.error("paypal_webhook error:", err);
    return error("Internal error", 500);
  }
});
