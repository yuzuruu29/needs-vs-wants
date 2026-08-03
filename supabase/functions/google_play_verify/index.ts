// Needs vs Wants - google_play_verify Edge Function
// Task 2: Pro subscription backend scaffolding.
//
// Verifies a Google Play purchase/subscription on the Play Developer API,
// then grants/extends the caller's Pro entitlement.
//
// Trust model:
//   - Client sends {package_name, product_id, purchase_token, kind} where
//     kind is "subscription" | "one_time".
//   - We mint a service-account RS256 JWT (GOOGLE_PLAY_SERVICE_ACCOUNT_JSON),
//     exchange it for an OAuth access token, and call the real Play API.
//   - Only a valid, non-cancelled, non-expired purchase produces a grant.
//   - Server never trusts the client's own claim of purchase validity.
//
// Idempotent: purchase_token is a natural idempotency key; re-verifying the
// same token re-applies the same grant.

import { error, handleOptions, jsonResponse } from "../_shared/http.ts";
import { grantToRowFields } from "../_shared/entitlements.ts";
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

async function verifySubscription(
  token: string,
  packageName: string,
  productId: string,
  purchaseToken: string,
): Promise<{ expiry: string; entitlementState: "active" | "on_hold" } | null> {
  const url =
    `https://androidpublisher.googleapis.com/androidpublisher/v3/applications/${encodeURIComponent(packageName)}/purchases/subscriptions/${encodeURIComponent(productId)}/tokens/${encodeURIComponent(purchaseToken)}`;
  const res = await fetch(url, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (res.status === 404) return null;
  if (!res.ok) {
    throw new Error(`Play API subscription error: ${res.status}`);
  }
  const data = await res.json() as {
    expiryTimeMillis?: string;
    paymentState?: number;
    cancelReason?: number;
  };
  const paymentState = data.paymentState;
  // 0 = payment pending, 1 = received, 2 = free trial, 3 = pending deferral.
  // Only proceed if payment received or free trial.
  if (paymentState !== undefined && paymentState !== 1 && paymentState !== 2) {
    return null;
  }
  // 0 = user cancelled, 1 = system cancelled, 2 = replaced, 3 = developer.
  // ANY present cancelReason means the subscription is no longer active, so
  // every value (0..3) is rejected.
  if (data.cancelReason !== undefined && data.cancelReason !== null) {
    return null;
  }
  if (!data.expiryTimeMillis) return null;
  // An expiry in the past means the subscription has lapsed; never re-grant.
  if (Number(data.expiryTimeMillis) <= Date.now()) return null;
  const expiry = new Date(Number(data.expiryTimeMillis)).toISOString();
  return { expiry, entitlementState: "active" };
}

async function verifyOneTimeProduct(
  token: string,
  packageName: string,
  productId: string,
  purchaseToken: string,
): Promise<{ expiry: string | null } | null> {
  const url =
    `https://androidpublisher.googleapis.com/androidpublisher/v3/applications/${encodeURIComponent(packageName)}/purchases/products/${encodeURIComponent(productId)}/tokens/${encodeURIComponent(purchaseToken)}`;
  const res = await fetch(url, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (res.status === 404) return null;
  if (!res.ok) {
    throw new Error(`Play API product error: ${res.status}`);
  }
  const data = await res.json() as {
    purchaseState?: number;
    purchaseTimeMillis?: string;
  };
  // purchaseState 0 = purchased.
  if (data.purchaseState !== undefined && data.purchaseState !== 0) return null;
  // One-time products never expire, so they grant lifetime Pro. We encode that
  // as expiry: null -> paid_until: NULL, which the model treats as lifetime.
  // This is the one intentional exception to "never write paid_until = NULL
  // for a paid grant" (a one-time product is not a recurring subscription).
  return { expiry: null };
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

    const body = await req.json() as {
      package_name?: string;
      product_id?: string;
      purchase_token?: string;
      kind?: "subscription" | "one_time";
    };
    const packageName = body.package_name;
    const productId = body.product_id;
    const purchaseToken = body.purchase_token;
    const kind = body.kind;
    if (!packageName || !productId || !purchaseToken || !kind) {
      return error("Missing purchase params", 400);
    }

    const accessToken = await getAccessToken(sa);

    let grantExpiry: string | null;
    if (kind === "subscription") {
      const result = await verifySubscription(
        accessToken,
        packageName,
        productId,
        purchaseToken,
      );
      if (!result) {
        return jsonResponse({ success: true, valid: false, reason: "not_active" });
      }
      grantExpiry = result.expiry;
    } else {
      const result = await verifyOneTimeProduct(
        accessToken,
        packageName,
        productId,
        purchaseToken,
      );
      if (!result) {
        return jsonResponse({ success: true, valid: false, reason: "not_active" });
      }
      grantExpiry = result.expiry;
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

    const fields = grantToRowFields({
      mode: "paid",
      paid_until: grantExpiry,
      provider: "google",
      source: kind,
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

    return jsonResponse({ success: true, valid: true, paid_until: grantExpiry });
  } catch (err) {
    console.error("google_play_verify error:", err);
    return error("Internal error", 500);
  }
});
