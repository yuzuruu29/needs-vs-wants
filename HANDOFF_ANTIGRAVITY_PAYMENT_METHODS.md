# Handoff: Payment-methods copy fix + 2.0.4 release (for the Antigravity session)

You are implementing the payment-methods copy fix and the 2.0.4 release for **Needs vs Wants** (`C:\Needs vs Wants` — Kotlin + Jetpack Compose Android app, Supabase Edge Functions, static Vercel site). The working tree is CLEAN at HEAD `5157a130` (release 2.0.3 shipped earlier; 228 unit tests green).

## Verified finding (orchestrator's evidence — build on it, don't re-derive)

The released 2.0.3 APK **under-reports the payment methods**: its paywall copy says "One-time payment via GCash or card…" while the checkout actually enables all five methods.

- `supabase/functions/paymongo_create_checkout/index.ts:107-110` — `PAYMONGO_PAYMENT_METHODS` defaults to `gcash,card,paymaya,grab_pay,qrph`; the secret is NOT set on Supabase (verify with `supabase secrets list` — only PAYMONGO_ENVIRONMENT/PAYMONGO_SECRET_KEY/PAYMONGO_WEBHOOK_SECRET exist).
- DEX scan of the released APK: `GCash` ×1, `PayMaya` ×0, `GrabPay` ×0 — only the "GCash or card" copy string exists.
- The site has NO payment-method claims (D104 "Not for sale yet") — do not touch it.

## Task 1 — Copy fix (exact string)

`app/src/main/java/com/needsvswants/app/ui/screens/paywall/PaywallScreen.kt:206` — replace:

`"One-time payment via GCash or card. You pay each month when ready — access ends on your expiry date. No auto-charge."`

with:

`"One-time payment via GCash, card, PayMaya, GrabPay, or QR PH. You pay each month when ready — access ends on your expiry date. No auto-charge."`

Character-exact (straight apostrophes, em dashes as the file uses them).

## Task 2 — Release 2.0.4 (versionCode 12)

1. **Bump** `app/build.gradle.kts`: `versionCode = 12`, `versionName = "2.0.4"`.
2. **IMPORTANT context:** the tree already contains the remaining-issues wave (`0d8ab98b` TTL/dedup/timeouts/copy/tests, `5157a130` dedup outcome fix) — do NOT re-implement or revert any of it; the 2.0.4 build simply includes it. This is why 2.0.4 ships the wave fixes too.
3. **Build + verify:** `cd "C:/Needs vs Wants" && ./gradlew :app:testDebugUnitTest --rerun-tasks` (must stay 228 green; phantom incremental-compiler errors are a known repo quirk — re-run with `--rerun-tasks` before assuming it's yours) + `./gradlew :app:assembleRelease` BUILD SUCCESSFUL. Then: aapt shows versionCode 12 / versionName 2.0.4, not debuggable; apksigner cert SHA-256 `5fc43fb6…` (D86 key — credentials in `~/.gradle/gradle.properties` + `.local/keystore.properties`, env-var fallback in build.gradle.kts).
4. **DEX verify** (Python ASCII-run on `classes.dex` — `strings` is absent in Git Bash): must contain `paymongo_create_checkout`, the new method names `GCash`/`PayMaya`/`GrabPay` (from the new copy), the Google web client id, and `needsvswants` scheme fragments.
5. **Site update:** copy the APK to BOTH `website/public/downloads/needs-vs-wants-2.0.4.apk` and `website/downloads/needs-vs-wants-2.0.4.apk` (byte-identical). Update all "2.0.3" touchpoints → "2.0.4" in both HTML copies (mirrors byte-identical). `node _pad-parts/apply.js` + `node _pad-parts/check.js` — ALL CHECKS PASSED. Keep `qrcode@1.5.1` (D22), all legacy APKs (1.0.0–2.0.3), and ALL D104 marketing copy untouched.
6. **Deploy:** `cd website && vercel whoami` (auth is present; if missing, STOP and report NEEDS_CONTEXT — do not invent credentials) → `vercel deploy --prod` → `vercel alias set <new-deployment-url> needs-vs-wants.vercel.app` (deployment URL FIRST — alias drift is the known failure mode, D19/D33/D55).
7. **Live verify:** homepage 200 + contains "2.0.4"; `https://needs-vs-wants.vercel.app/downloads/needs-vs-wants-2.0.4.apk` 200 + sha256 matches the local build exactly (download and hash); legacy 2.0.3 APK still 200.
8. **Commit** on main, conventional style: `feat(paywall): payment-methods copy reflects all five PayMongo methods` (Task 1) and `release: 2.0.4 (versionCode 12) — payment-methods copy + remaining-issues wave` (Task 2; includes the two APK binaries — website downloads are un-ignored per D33/D39). Do NOT push.

## Task 3 — Vault closeout (AGENTS.md mandate)

- **Pre-code gate:** load `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\` (Summary, Tasks, Decisions — latest D# is D114; **D115 is next free**); Context7: N/A (no new external library surface); Graphify: existing `.graphify/` is stale (2026-08-03) — blast radius is paywall copy + release only, no rebuild needed.
- **After ship:** prepend a dated 2026-08-08 Tasks.md entry (verified outcomes only: tests 228, new sha, deployment URL, live verification); append **D115** to Decisions.md (context: 2.0.3 APK copy under-reported the methods — checkout enabled `gcash,card,paymaya,grab_pay,qrph` by default; decision: copy now names all five, shipped with 2.0.4/versionCode 12 which also delivers the remaining-issues wave; validation: aapt/apksigner/DEX + live sha); update Summary.md versionName 2.0.4.
- **Vault-write safety (AGENTS.md):** vault is plain Markdown outside the file-tool sandbox — write via bash/python with Git-Bash paths; NEVER open the target in `"w"` mode before the payload is known-good; write to a temp file then `os.replace(tmp, p)` (atomic); round-trip `encoding="latin-1"` to preserve legacy bytes; ASCII-only inserted text; full-line anchors in `replace()`.

## Constraints

- Do NOT touch: `.superpowers/` (other sessions' scratch), supabase function logic, payment-method config, the D104 site marketing copy, the `qrcode@1.5.1` pin.
- Do NOT push. Do NOT modify anything outside the listed files + release artifacts.
- If the copy string, version bump, or any step conflicts with something you find (e.g. a newer commit landed), STOP and report — do not resolve by guessing.

## Report back (in chat)

Commit hash(es), test totals, DEX strings found, deployment URL, live sha256, D115 confirmation, any conflicts or deviations.
