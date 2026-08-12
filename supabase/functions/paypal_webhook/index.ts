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
// custom_id trust (closes the D46 caveat): the webhook signature only
// authenticates PayPal, not the buyer, and custom_id is client-controlled at
// checkout. paypal_create_subscription now mints an HMAC-signed custom_id
// (`v1.<user_id>.<issued_at>.<sig>`, secret PAYPAL_CUSTOM_ID_SECRET) and this
// webhook verifies it before granting:
//   - fresh valid signature        -> grant (any event type)
//   - valid but >24h old           -> grant only when the subscription is
//                                     already linked to the same user (ledger
//                                     row or paypal entitlement) - renewals
//                                     replay the same custom_id for the life
//                                     of the subscription
//   - tampered signature           -> never grant
//   - legacy raw uuid (pre-cutover)-> renew/extend/status events only, and
//                                     only for users already linked to
//                                     PayPal; NEVER first-time ACTIVATED /
//                                     CREATED grants
//
// Idempotency: processed events are recorded in the payment_events ledger
// keyed on the PayPal event id (provider 'paypal'; checkout_session_id
// stores the PayPal subscription id). A replayed delivery is acked as
// already_applied without re-granting. Unlike the PayMongo webhook (which
// must ledger-first because its grants STACK +30/+365 days), PayPal grants
// are absolute (paid_until = next_billing_time), so we grant first and
// ledger after: a transient grant failure stays retryable and a rare
// double-apply writes identical values.
//
// NOTE: deployed with `--no-verify-jwt` (PayPal is not an authenticated
// Supabase client). Signature verification is the only auth gate.

import { error, handleOptions, jsonResponse } from "../_shared/http.ts";
import {
  grantToRowFields,
  mapPayPalWebhookEvent,
} from "../_shared/entitlements.ts";
import {
  candidateUserIdFor,
  classifyPayPalEventForTrust,
  decideGrantAcceptance,
  verifySignedCustomId,
} from "../_shared/paypal_custom_id.ts";
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

    const envelope = event as Record<string, unknown>;
    const resource = (envelope.resource ?? {}) as Record<string, unknown>;
    const eventId = typeof envelope.id === "string" && envelope.id
      ? envelope.id
      : null;
    const eventType = typeof envelope.event_type === "string"
      ? envelope.event_type
      : "";
    const subscriptionId = typeof resource.id === "string" && resource.id
      ? resource.id
      : null;
    // Same precedence as mapPayPalWebhookEvent (real events only carry
    // custom_id; user_id is scaffold-era leniency).
    const rawCustomId = typeof resource.user_id === "string"
      ? resource.user_id
      : typeof resource.custom_id === "string"
        ? resource.custom_id
        : "";

    const supabaseUrl = Deno.env.get("SUPABASE_URL");
    const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
    if (!supabaseUrl || !serviceRoleKey) {
      return error("Server not configured", 500);
    }

    const supabase = createClient(supabaseUrl, serviceRoleKey, {
      auth: { persistSession: false },
    });

    // Step 1 - Idempotency. Replayed deliveries of an already-processed
    // event are logged in the ledger under the PayPal event id; ack them
    // without re-granting.
    if (eventId) {
      const { data: existingEvent, error: ledgerReadErr } = await supabase
        .from("payment_events")
        .select("id")
        .eq("id", eventId)
        .maybeSingle();

      if (ledgerReadErr) {
        console.error("payment_events read error:", ledgerReadErr);
        return error("Failed to verify payment ledger", 500);
      }
      if (existingEvent) {
        return jsonResponse({ success: true, already_applied: true });
      }
    }

    // Step 2 - custom_id trust decision. Missing PAYPAL_CUSTOM_ID_SECRET is
    // a degraded mode: signed tokens fail verification (rejected) while
    // legacy uuids still follow the legacy fallback rules. Log loudly.
    const customIdSecret = Deno.env.get("PAYPAL_CUSTOM_ID_SECRET") ?? "";
    if (!customIdSecret) {
      console.error("paypal_webhook: PAYPAL_CUSTOM_ID_SECRET is not set");
    }
    const verification = await verifySignedCustomId(
      rawCustomId,
      customIdSecret,
    );

    // Linkage lookup, only needed for the expired / legacy paths: is this
    // subscription (or at least this user) already linked to PayPal?
    const candidate = candidateUserIdFor(verification, rawCustomId);
    let hasPriorPayPalGrant = false;
    if (!verification.ok && candidate) {
      if (subscriptionId) {
        const { data: ledgerLink, error: linkErr } = await supabase
          .from("payment_events")
          .select("id")
          .eq("provider", "paypal")
          .eq("checkout_session_id", subscriptionId)
          .eq("user_id", candidate)
          .limit(1)
          .maybeSingle();
        if (linkErr) {
          console.error("payment_events link read error:", linkErr);
          return error("Failed to verify payment ledger", 500);
        }
        hasPriorPayPalGrant = !!ledgerLink;
      }
      if (!hasPriorPayPalGrant) {
        // Pre-cutover subscribers have no ledger rows yet; their entitlement
        // row (provider 'paypal') is the linkage that keeps renewals working.
        const { data: entRow, error: entErr } = await supabase
          .from("entitlements")
          .select("provider")
          .eq("user_id", candidate)
          .maybeSingle();
        if (entErr) {
          console.error("entitlements link read error:", entErr);
          return error("Failed to load entitlement", 500);
        }
        hasPriorPayPalGrant = entRow?.provider === "paypal";
      }
    }

    const decision = decideGrantAcceptance({
      verification,
      rawCustomId,
      eventClass: classifyPayPalEventForTrust(eventType),
      hasPriorPayPalGrant,
    });

    if (!decision.accept || !decision.user_id) {
      // Ack with 200: PayPal retries cannot change this outcome, and the
      // event is fully logged server-side for reconciliation.
      console.error(
        `paypal_webhook: grant refused (${decision.reason})`,
        { eventId, eventType, subscriptionId },
      );
      return jsonResponse({
        success: true,
        ignored: true,
        reason: decision.reason,
      });
    }

    // Step 3 - Apply the grant to the VERIFIED user (never the raw
    // custom_id string, which for signed tokens is the whole token).
    const fields = grantToRowFields(mapped.grant);
    const { error: upsertError } = await supabase
      .from("entitlements")
      .upsert(
        {
          user_id: decision.user_id,
          ...fields,
          updated_at: new Date().toISOString(),
        },
        { onConflict: "user_id" },
      );

    if (upsertError) {
      console.error("entitlements upsert error:", upsertError);
      return error("Failed to apply grant", 500);
    }

    // Step 4 - Record the processed event in the ledger (replay guard +
    // subscription-to-user linkage for future legacy/expired decisions).
    // PayPal rows: id = event id, checkout_session_id = subscription id,
    // amount_centavos = 0 (amounts live in PayPal reports, not events).
    if (eventId) {
      const { error: ledgerError } = await supabase
        .from("payment_events")
        .insert({
          id: eventId,
          user_id: decision.user_id,
          tier: mapped.grant.tier ?? "pro",
          amount_centavos: 0,
          currency: "PHP",
          provider: "paypal",
          checkout_session_id: subscriptionId,
          status: eventType.toLowerCase(),
          raw_reference: typeof envelope.create_time === "string"
            ? envelope.create_time
            : null,
        });
      if (ledgerError) {
        // 23505 = a concurrent delivery raced us; both wrote identical
        // absolute grant values, so this is safe to ignore.
        if (
          !(typeof ledgerError.code === "string" &&
            ledgerError.code === "23505")
        ) {
          console.error("payment_events insert error:", ledgerError);
        }
      }
    } else {
      console.error("paypal_webhook: event without id; ledger row skipped", {
        eventType,
        subscriptionId,
      });
    }

    return jsonResponse({ success: true, applied: mapped.grant.mode });
  } catch (err) {
    console.error("paypal_webhook error:", err);
    return error("Internal error", 500);
  }
});
