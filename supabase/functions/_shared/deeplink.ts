// Deep-link scheme forwarding for checkout return/cancel redirect pages.
//
// Audit gap (flagged 2026-08-09, still open at the 2026-08-13 audit): the
// `plain` test flavor declares the `needsvswantsplain` scheme, but the hosted
// redirect pages hardcoded `needsvswants://`, so plain builds never received
// checkout returns. The app now sends its scheme in the create-checkout body;
// we whitelist it and forward it to the redirect page as a `scheme` query
// param, which the page uses for both the custom-scheme URL and the
// package-targeted intent:// launch.

export const DEFAULT_SCHEME = "needsvswants";

export const ALLOWED_SCHEMES: readonly string[] = [
  DEFAULT_SCHEME,
  "needsvswantsplain",
];

/** Unknown/missing/invalid input falls back to the production scheme. */
export function sanitizeScheme(raw: unknown): string {
  return typeof raw === "string" && ALLOWED_SCHEMES.includes(raw)
    ? raw
    : DEFAULT_SCHEME;
}

/**
 * Appends `scheme=<value>` to a redirect-page URL. The default scheme is NOT
 * appended, so production full-flavor URLs stay byte-identical to before.
 */
export function withSchemeParam(url: string, scheme: string): string {
  const clean = sanitizeScheme(scheme);
  if (clean === DEFAULT_SCHEME) return url;
  const sep = url.includes("?") ? "&" : "?";
  return `${url}${sep}scheme=${encodeURIComponent(clean)}`;
}
