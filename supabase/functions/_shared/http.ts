// Needs vs Wants - shared HTTP helpers for Edge Functions
// Zero-dependency: CORS + JSON response helpers used by all four functions.

export const corsHeaders = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Methods": "POST, GET, OPTIONS",
  "Access-Control-Allow-Headers":
    "authorization, x-client-info, apikey, content-type",
  "Access-Control-Max-Age": "86400",
};

export function jsonResponse(
  body: unknown,
  status = 200,
): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { ...corsHeaders, "Content-Type": "application/json" },
  });
}

export function handleOptions(): Response {
  return new Response("ok", { status: 204, headers: corsHeaders });
}

export function ok(data: unknown): Response {
  return jsonResponse({ success: true, data });
}

export function error(message: string, status = 400): Response {
  return jsonResponse({ success: false, error: message }, status);
}

/** Safe env accessor: throws with a clear message when a required secret is missing. */
export function requireEnv(name: string): string {
  const value = Deno.env.get(name);
  if (!value) {
    throw new Error(`Missing required env var: ${name}`);
  }
  return value;
}
