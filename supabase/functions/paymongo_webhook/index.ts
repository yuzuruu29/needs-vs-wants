// Needs vs Wants - paymongo_webhook Edge Function
//
// Receives PayMongo `checkout_session.payment.paid` webhook events, verifies
// the PayMongo-Signature header over the RAW body, then grants entitlement.
//
// Mandatory auth gate (PayMongo is not a Supabase client - deployed with
// `--no-verify-jwt`): HMAC-SHA256 signature verification only.
//
// Idempotency: the payment_events ledger is keyed on the PayMongo payment id
// (pay_xxx). We INSERT the payment_events row FIRST; if the insert conflicts
// the payment has already been applied and we do NOT re-grant. This prevents
// webhook retries from double-granting access.
//
// Grant: one-time, manual-renewal. Grant length comes from the checkout
// metadata period (monthly = 30 days, annual = 365 days). paid_until =
// max(now, existing.paid_until) + grant days (stacks when renewing early).
// Max always upgrades to max; a Pro renewal while Max is still active keeps
// max tier (extends paid_until).

import {
  error,
  handleOptions,
  jsonResponse,
  requireEnv,
} from "../_shared/http.ts";
import {
  grantDaysFor,
  mapCheckoutPaidEvent,
  nextPaidUntil,
  resolveGrantTier,
} from "../_shared/paymongo.ts";
import { verifyPaymongoSignature } from "../_shared/paymongo.ts";
import type { EntitlementRow } from "../_shared/entitlements.ts";
import {
  createClient,
} from "https://esm.sh/@supabase/supabase-js@2.45.4";

Deno.serve(async (req: Request) => {
  if (req.method === "OPTIONS") return handleOptions();
  if (req.method !== "POST") {
    return error("Method not allowed", 405);
  }

  try {
    const secret = requireEnv("PAYMONGO_WEBHOOK_SECRET");

    // Sign the RAW body (PayMongo signs the raw request body, not the parsed JSON).
    const rawBody = await req.text();
    const signature = req.headers.get("Paymongo-Signature");

    const verified = await verifyPaymongoSignature(rawBody, signature, secret);
    if (!verified) {
      return error("Webhook signature verification failed", 401);
    }

    let payload: unknown;
    try {
      payload = JSON.parse(rawBody);
    } catch {
      return error("Malformed JSON body", 400);
    }

    const grant = mapCheckoutPaidEvent(payload);
    if (!grant) {
      // A known, signed event we don't act on (e.g. not a paid payment, or a
      // different event type). Ack so PayMongo stops retrying.
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

    // Step 1 - Idempotency ledger. The payment_events row is the exactly-once
    // guard keyed on the PayMongo payment id (pay_xxx). We SELECT first: if the
    // row already exists, this payment was already granted by a prior webhook
    // delivery - ack WITHOUT re-granting. Otherwise INSERT it, then grant.
    const { data: existingEvent, error: ledgerReadErr } = await supabase
      .from("payment_events")
      .select("id")
      .eq("id", grant.payment_id)
      .maybeSingle();

    if (ledgerReadErr) {
      console.error("payment_events read error:", ledgerReadErr);
      return error("Failed to verify payment ledger", 500);
    }

    if (existingEvent) {
      // Already applied - do not re-grant (prevents webhook retries from
      // double-granting +30 days).
      return jsonResponse({ success: true, already_applied: true });
    }

    const { error: ledgerError } = await supabase
      .from("payment_events")
      .insert({
        id: grant.payment_id,
        user_id: grant.user_id,
        tier: grant.tier,
        amount_centavos: grant.amount_centavos,
        currency: "PHP",
        provider: "paymongo",
        checkout_session_id: grant.checkout_session_id,
        status: "paid",
        raw_reference: grant.paid_at,
      });

    if (ledgerError) {
      // A unique violation (code 23505) means a concurrent webhook delivery
      // inserted the row between our SELECT and INSERT - already applied.
      if (typeof ledgerError.code === "string" && ledgerError.code === "23505") {
        return jsonResponse({ success: true, already_applied: true });
      }
      console.error("payment_events insert error:", ledgerError);
      return error("Failed to record payment", 500);
    }

    // Step 2 - Load current entitlement to compute stacking.
    const { data: currentRow, error: currentErr } = await supabase
      .from("entitlements")
      .select(
        "user_id,is_pro,tier,trial_started_at,trial_ends_at,paid_until,provider,source,status,updated_at",
      )
      .eq("user_id", grant.user_id)
      .maybeSingle();

    if (currentErr) {
      console.error("entitlements read error:", currentErr);
      return error("Failed to load entitlement", 500);
    }

    const serverNow = new Date().toISOString();
    const finalTier = resolveGrantTier(
      grant.tier,
      (currentRow ?? null) as EntitlementRow | null,
      serverNow,
    );
    const paidUntil = nextPaidUntil(
      serverNow,
      currentRow?.paid_until ?? null,
      grantDaysFor(grant.period),
    );

    // Step 3 - Apply the grant (service role bypasses RLS). We compute the
    // stacked paid_until ourselves (grantToRowFields alone does NOT stack).
    const { error: upsertError } = await supabase
      .from("entitlements")
      .upsert(
        {
          user_id: grant.user_id,
          is_pro: true,
          tier: finalTier,
          trial_started_at: null,
          trial_ends_at: null,
          paid_until: paidUntil,
          provider: "paymongo",
          source: "checkout_session",
          status: "paid",
          updated_at: serverNow,
        },
        { onConflict: "user_id" },
      );

    if (upsertError) {
      console.error("entitlements upsert error:", upsertError);
      return error("Failed to apply grant", 500);
    }

    return jsonResponse({
      success: true,
      applied: true,
      data: {
        user_id: grant.user_id,
        tier: finalTier,
        payment_id: grant.payment_id,
        paid_until: paidUntil,
      },
    });
  } catch (err) {
    console.error("paymongo_webhook error:", err);
    return error("Internal error", 500);
  }
});