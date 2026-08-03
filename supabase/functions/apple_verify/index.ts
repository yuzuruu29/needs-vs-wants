// Needs vs Wants - apple_verify Edge Function
// Task 2: Pro subscription backend scaffolding.
//
// Verifies an App Store receipt via the App Store's verifyReceipt endpoint
// and grants Pro for the client's auto-renewing subscription expiry. A valid
// receipt that does not contain a matching in-date auto-renewable subscription
// transaction is refused (no broad fallback window).
//
// Flow:
//   1. Client posts {receipt_data, bundle_id}.
//   2. We call verifyReceipt with APPLE_SHARED_SECRET (production first,
//      sandbox on status 21007 as Apple prescribes).
//   3. Only status 0 (valid) or 21007 (sandbox retry) produce a grant.
//   4. The client's identity is taken from the caller JWT; the receipt is
//      never used as identity.
//
// Server-time authoritative; idempotent per user.

import { error, handleOptions, jsonResponse } from "../_shared/http.ts";
import { grantToRowFields } from "../_shared/entitlements.ts";
import {
  createClient,
} from "https://esm.sh/@supabase/supabase-js@2.45.4";

const PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";
const SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
// SANDBOX (21007) tells us to retry with the sandbox endpoint.
const STATUS_SANDBOX = 21007;

async function callVerifyReceipt(
  url: string,
  receiptData: string,
  sharedSecret: string,
): Promise<{ status: number; bundleId?: string; latestReceiptInfo?: Array<Record<string, string>> }> {
  const res = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      "receipt-data": receiptData,
      password: sharedSecret,
      "exclude-old-transactions": true,
    }),
  });
  if (!res.ok) {
    throw new Error(`verifyReceipt HTTP ${res.status}`);
  }
  const data = await res.json() as {
    status: number;
    receipt?: { bundle_id?: string };
    latest_receipt_info?: Array<Record<string, string>>;
  };
  return {
    status: data.status,
    bundleId: data.receipt?.bundle_id,
    latestReceiptInfo: data.latest_receipt_info,
  };
}

// Returns the latest expiry among the receipt's auto-renewable subscription
// transactions (those that carry an expires_date). Returns null when the
// receipt contains no auto-renewable subscription entry, so the grant is
// refused instead of inventing a fallback window.
function latestExpiresAt(
  latestReceiptInfo?: Array<Record<string, string>>,
): Date | null {
  let latest: Date | null = null;
  if (latestReceiptInfo) {
    for (const entry of latestReceiptInfo) {
      const exp = entry.expires_date || entry.expires_date_ms;
      if (!exp) continue;
      const parsed = new Date(/^\d{10,}$/.test(exp) ? Number(exp) : exp);
      if (isNaN(parsed.getTime())) continue;
      if (!latest || parsed.getTime() > latest.getTime()) latest = parsed;
    }
  }
  return latest;
}

Deno.serve(async (req: Request) => {
  if (req.method === "OPTIONS") return handleOptions();
  if (req.method !== "POST") {
    return error("Method not allowed", 405);
  }

  try {
    const sharedSecret = Deno.env.get("APPLE_SHARED_SECRET");
    const expectedBundleId = Deno.env.get("APPLE_BUNDLE_ID");
    if (!sharedSecret || !expectedBundleId) {
      return error("Server not configured", 500);
    }

    const body = await req.json() as { receipt_data?: string };
    const receiptData = body.receipt_data;
    if (!receiptData) {
      return error("Missing receipt_data", 400);
    }

    let result = await callVerifyReceipt(
      PRODUCTION_URL,
      receiptData,
      sharedSecret,
    );

    if (result.status === STATUS_SANDBOX) {
      result = await callVerifyReceipt(
        SANDBOX_URL,
        receiptData,
        sharedSecret,
      );
    }

    if (result.status !== 0) {
      return jsonResponse({ success: true, valid: false, status: result.status });
    }

    if (result.bundleId !== expectedBundleId) {
      return jsonResponse({ success: true, valid: false, reason: "bundle_mismatch" });
    }

    const now = new Date();
    const expiresAt = latestExpiresAt(result.latestReceiptInfo);
    if (!expiresAt || expiresAt.getTime() <= now.getTime()) {
      return jsonResponse({
        success: true,
        valid: false,
        reason: expiresAt ? "expired" : "no_subscription",
      });
    }
    const paidUntil = expiresAt.toISOString();

    const auth = req.headers.get("authorization") ?? "";
    const token = auth.startsWith("Bearer ") ? auth.slice(7) : null;
    if (!token) return error("Missing bearer token", 401);

    const claimsPart = token.split(".")[1];
    if (!claimsPart) return error("Invalid token", 401);
    const payload = JSON.parse(
      new TextDecoder().decode(
        Uint8Array.from(
          atob(claimsPart.replace(/-/g, "+").replace(/_/g, "/")),
          (c) => c.charCodeAt(0),
        ),
      ),
    ) as { sub?: string };
    const userId = payload.sub;
    if (!userId) return error("Invalid token", 401);

    const supabaseUrl = Deno.env.get("SUPABASE_URL");
    const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
    if (!supabaseUrl || !serviceRoleKey) {
      return error("Server not configured", 500);
    }

    const supabase = createClient(supabaseUrl, serviceRoleKey, {
      auth: { persistSession: false },
    });

    const fields = grantToRowFields({
      mode: "paid",
      paid_until: paidUntil,
      provider: "apple",
      source: "app_store",
      status: "purchased",
    });

    const { error: upsertError } = await supabase
      .from("entitlements")
      .upsert(
        { user_id: userId, ...fields, updated_at: new Date().toISOString() },
        { onConflict: "user_id" },
      );

    if (upsertError) {
      console.error("entitlements upsert error:", upsertError);
      return error("Failed to apply grant", 500);
    }

    return jsonResponse({ success: true, valid: true, paid_until: paidUntil });
  } catch (err) {
    console.error("apple_verify error:", err);
    return error("Internal error", 500);
  }
});