// Needs vs Wants - get_entitlement Edge Function
// Task 2: Pro subscription backend scaffolding.
//
// Returns the caller's current entitlement, computing Pro status from DB
// timestamps and SERVER time (never client time). Callers pass the user's
// Supabase access token as `Authorization: Bearer <token>`.
//
// Flow:
//   1. Decode `sub` from the caller's JWT (no signature verification here;
//      Supabase gateway already validated the token and only forwards
//      authenticated requests when the client uses the supabase client).
//   2. Load the entitlements row via the my_entitlement() RPC (SECURITY
//      DEFINER, RLS-scoped to the caller).
//   3. Compute is_active via the shared pure helper against server time.

import { error, handleOptions, jsonResponse } from "../_shared/http.ts";
import {
  isEntitlementActive,
  type EntitlementRow,
} from "../_shared/entitlements.ts";
import {
  createClient,
} from "https://esm.sh/@supabase/supabase-js@2.45.4";

interface DecodedJwt {
  sub?: string;
  [key: string]: unknown;
}

/** Base64url-decode the payload segment of a JWT (no signature verification). */
function decodeJwtPayload(token: string): DecodedJwt | null {
  const parts = token.split(".");
  if (parts.length !== 3) return null;
  const payload = parts[1];
  if (!payload) return null;
  let json: string;
  try {
    const normalized = payload.replace(/-/g, "+").replace(/_/g, "/");
    const padded = normalized.padEnd(
      normalized.length + ((4 - (normalized.length % 4)) % 4),
      "=",
    );
    json = new TextDecoder().decode(
      Uint8Array.from(atob(padded), (c) => c.charCodeAt(0)),
    );
  } catch {
    return null;
  }
  try {
    return JSON.parse(json) as DecodedJwt;
  } catch {
    return null;
  }
}

Deno.serve(async (req: Request) => {
  if (req.method === "OPTIONS") return handleOptions();

  try {
    const auth = req.headers.get("authorization") ?? "";
    const token = auth.startsWith("Bearer ") ? auth.slice(7) : null;
    if (!token) {
      return jsonResponse(
        { success: false, error: "Missing bearer token" },
        401,
      );
    }

    const claims = decodeJwtPayload(token);
    const userId = claims?.sub;
    if (!userId) {
      return jsonResponse(
        { success: false, error: "Invalid token" },
        401,
      );
    }

    const supabaseUrl = Deno.env.get("SUPABASE_URL");
    const anonKey = Deno.env.get("SUPABASE_ANON_KEY");
    if (!supabaseUrl || !anonKey) {
      return jsonResponse(
        { success: false, error: "Server not configured" },
        500,
      );
    }

    const supabase = createClient(supabaseUrl, anonKey, {
      global: { headers: { Authorization: `Bearer ${token}` } },
      auth: { persistSession: false },
    });

    const { data, error: rpcError } = await supabase.rpc("my_entitlement");

    if (rpcError) {
      return jsonResponse(
        { success: false, error: "Failed to load entitlement" },
        500,
      );
    }

    const row = (data ?? null) as EntitlementRow | null;
    const serverNow = new Date().toISOString();
    const isActive = isEntitlementActive(
      {
        is_pro: row?.is_pro ?? false,
        trial_ends_at: row?.trial_ends_at ?? null,
        paid_until: row?.paid_until ?? null,
      },
      serverNow,
    );

    return jsonResponse({
      success: true,
      data: {
        is_pro: isActive,
        plan: isActive ? "pro" : "free",
        trial_started_at: row?.trial_started_at ?? null,
        trial_ends_at: row?.trial_ends_at ?? null,
        paid_until: row?.paid_until ?? null,
        provider: row?.provider ?? null,
        source: row?.source ?? null,
        status: row?.status ?? null,
        server_time: serverNow,
      },
    });
  } catch (err) {
    console.error("get_entitlement error:", err);
    return jsonResponse(
      { success: false, error: "Internal error" },
      500,
    );
  }
});
