// Needs vs Wants - google_play_verify Edge Function
// Task 2: Pro subscription backend scaffolding.
//
// Verifies a Google Play purchase/subscription on the Play Developer API
// via subscriptionsv2, then grants/extends the caller's Pro/Max entitlement.
//
// Trust model:
//   - Client sends {package_name, purchase_token, kind}.
//   - package_name must equal GOOGLE_PLAY_PACKAGE_NAME exactly; kind must be a
//     supported value; one_time requires an allowlisted Play one-time product
//     (none exists today, so every one_time request is rejected).
//   - We mint a service-account RS256 JWT (GOOGLE_PLAY_SERVICE_ACCOUNT_JSON),
//     exchange it for an OAuth access token, and call the real Play API.
//   - Only a valid, non-cancelled, non-expired purchase produces a grant, and
//     the verified productId must be allowlisted (needsvswants_pro/max);
//     unknown or missing product ids fail closed instead of mapping to Pro.
//   - Server never trusts the client's own claim of purchase validity or tier.
//
// Idempotent: purchase_token is a natural idempotency key; re-verifying the
// same token re-applies the same grant.
//
// Merging: grants are applied via mergePaidEntitlements so duplicate,
// out-of-order, or overlapping verifications (e.g. Pro->Max upgrades where both
// subscriptions briefly coexist) can never downgrade the user: highest tier
// wins, lifetime outranks dated expiries, and the latest dated expiry wins.

import { error, handleOptions, jsonResponse } from "../_shared/http.ts";
import {
  grantToRowFields,
  tierFromGooglePlayProductId,
  parseSubscriptionV2Response,
  parseOneTimeProductV2Response,
  validateGooglePlayVerifyRequest,
  mergePaidEntitlements,
  paidPurchaseFromRow,
  type GooglePlayVerifyRequestBody,
  type PaidEntitlementPurchase,
  type SubscriptionPurchaseV2Response,
  type OneTimeProductV2Response,
} from "../_shared/entitlements.ts";
import {
  createClient,
} from "https://esm.sh/@supabase/supabase-js@2.45.4";

interface ServiceAccount {
  client_email: string;
  private_key: string;
  token_uri?: string;
  project_id?: string;
}

function b64url(input: ArrayBuffer | Uint8Array): string {
  const bytes = input instanceof Uint8Array
    ? input
    : new Uint8Array(input);
  let binary = "";
  for (const b of bytes) binary += String.fromCharCode(b);
  return btoa(binary).replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/, "");
}

async function rs256Sign(
  data: string,
  privateKeyPem: string,
): Promise<string> {
  const pemBody = privateKeyPem
    .replace(/-----BEGIN PRIVATE KEY-----/g, "")
    .replace(/-----END PRIVATE KEY-----/g, "")
    .replace(/\s+/g, "");
  const binaryKey = atob(pemBody);
  const keyBytes = new Uint8Array(binaryKey.length);
  for (let i = 0; i < binaryKey.length; i++) {
    keyBytes[i] = binaryKey.charCodeAt(i);
  }
  const key = await crypto.subtle.importKey(
    "pkcs8",
    keyBytes.buffer as ArrayBuffer,
    { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" },
    false,
    ["sign"],
  );
  const sig = await crypto.subtle.sign(
    { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" },
    key,
    new TextEncoder().encode(data),
  );
  return b64url(sig);
}

async function getAccessToken(sa: ServiceAccount): Promise<string> {
  const now = Math.floor(Date.now() / 1000);
  const header = b64url(new TextEncoder().encode(JSON.stringify({
    alg: "RS256",
    typ: "JWT",
  })));
  const claims = b64url(new TextEncoder().encode(JSON.stringify({
    iss: sa.client_email,
    scope: "https://www.googleapis.com/auth/androidpublisher",
    aud: "https://oauth2.googleapis.com/token",
    iat: now,
    exp: now + 3600,
  })));
  const signature = await rs256Sign(`${header}.${claims}`, sa.private_key);
  const assertion = `${header}.${claims}.${signature}`;

  const res = await fetch(
    sa.token_uri ?? "https://oauth2.googleapis.com/token",
    {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams({
        grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
        assertion,
      }),
    },
  );
  if (!res.ok) {
    throw new Error(`Google token exchange failed: ${res.status}`);
  }
  const data = await res.json() as { access_token?: string };
  if (!data.access_token) throw new Error("Google: no access_token");
  return data.access_token;
}

interface SubscriptionPurchaseV2 {
  subscriptionState?: string;
  lineItems?: Array<{
    productId?: string;
    expiryTime?: string;
    acknowledgementState?: string;
  }>;
  externalAccountIdentifiers?: Record<string, unknown>;
}

async function verifySubscriptionV2(
  token: string,
  packageName: string,
  purchaseToken: string,
): Promise<{ expiry: string; productId: string } | null> {
  const url =
    `https://androidpublisher.googleapis.com/androidpublisher/v3/applications/${encodeURIComponent(packageName)}/purchases/subscriptionsv2/tokens/${encodeURIComponent(purchaseToken)}`;
  const res = await fetch(url, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (res.status === 404) return null;
  if (!res.ok) {
    throw new Error(`Play API subscriptionsv2 error: ${res.status}`);
  }
  const data = await res.json() as SubscriptionPurchaseV2Response;
  return parseSubscriptionV2Response(data);
}

async function verifyOneTimeProductV2(
  token: string,
  packageName: string,
  productId: string,
  purchaseToken: string,
): Promise<{ expiry: string | null; productId: string } | null> {
  const url =
    `https://androidpublisher.googleapis.com/androidpublisher/v3/applications/${encodeURIComponent(packageName)}/purchases/products/${encodeURIComponent(productId)}/tokens/${encodeURIComponent(purchaseToken)}`;
  const res = await fetch(url, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (res.status === 404) return null;
  if (!res.ok) {
    throw new Error(`Play API product error: ${res.status}`);
  }
  const data = await res.json() as OneTimeProductV2Response;
  return parseOneTimeProductV2Response(data, productId);
}

Deno.serve(async (req: Request) => {
  if (req.method === "OPTIONS") return handleOptions();
  if (req.method !== "POST") {
    return error("Method not allowed", 405);
  }

  try {
    const saJson = Deno.env.get("GOOGLE_PLAY_SERVICE_ACCOUNT_JSON");
    if (!saJson) return error("Server not configured", 500);

    const sa: ServiceAccount = JSON.parse(saJson);
    if (!sa.client_email || !sa.private_key) {
      return error("Invalid service account config", 500);
    }

    const body = await req.json() as GooglePlayVerifyRequestBody;
    // Fail-closed request validation: exact package allowlist, supported kinds
    // only, and no grantable Play one-time product exists today.
    const validated = validateGooglePlayVerifyRequest(body);
    if (!validated.ok) {
      return error(validated.error, validated.status);
    }

    const accessToken = await getAccessToken(sa);

    let result: { expiry: string; productId: string } | null;
    if (validated.kind === "subscription") {
      result = await verifySubscriptionV2(
        accessToken,
        validated.packageName,
        validated.purchaseToken,
      );
    } else {
      const oneTime = await verifyOneTimeProductV2(
        accessToken,
        validated.packageName,
        validated.productId,
        validated.purchaseToken,
      );
      result = oneTime ? { expiry: oneTime.expiry ?? "", productId: oneTime.productId } : null;
    }

    if (!result) {
      return jsonResponse({ success: true, valid: false, reason: "not_active" });
    }

    const auth = req.headers.get("authorization") ?? "";
    const token = auth.startsWith("Bearer ") ? auth.slice(7) : null;
    if (!token) {
      return error("Missing bearer token", 401);
    }

    // Decode user id from the caller JWT (Supabase gateway already verified it).
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

    // Tier is derived from Google's verified response, never from the client's
    // request. Unknown product ids fail closed instead of mapping to Pro.
    const tier = tierFromGooglePlayProductId(result.productId);
    if (!tier) {
      return error("Unknown product_id", 400);
    }

    // Load the current entitlement so the new grant MERGES order-independently
    // (highest tier wins; lifetime outranks dated; latest dated expiry wins).
    // A duplicate or out-of-order callback must never shorten a newer grant.
    const { data: currentRow, error: currentError } = await supabase
      .from("entitlements")
      .select("is_pro,tier,trial_ends_at,paid_until")
      .eq("user_id", userId)
      .maybeSingle();
    if (currentError) {
      console.error("entitlements read error:", currentError);
      return error("Failed to load entitlement", 500);
    }

    const merged = mergePaidEntitlements(
      [
        paidPurchaseFromRow(currentRow),
        { tier, paid_until: result.expiry || null },
      ].filter((p): p is PaidEntitlementPurchase => p !== null),
      new Date().toISOString(),
    );

    const fields = grantToRowFields({
      mode: "paid",
      tier: merged.tier,
      paid_until: merged.paid_until,
      provider: "google",
      source: validated.kind,
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

    // Response schema unchanged for the Android client; paid_until now reflects
    // the merged entitlement rather than blindly the last-arriving purchase.
    return jsonResponse({ success: true, valid: true, paid_until: merged.paid_until, product_id: result.productId });
  } catch (err) {
    console.error("google_play_verify error:", err);
    return error("Internal error", 500);
  }
});
