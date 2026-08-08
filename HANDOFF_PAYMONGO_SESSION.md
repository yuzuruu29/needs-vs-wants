# Handoff: Finish + commit the PayMongo provider switch (for the parallel PayMongo session)

You are the PayMongo payment-provider session for **Needs vs Wants** (`C:\Needs vs Wants`). A parallel session (entitlement sync) has committed its work. Your PayMongo switch is mid-edit in the same shared working tree and must now be **finished to a green state and committed**, because the entitlement-sync session is blocked on your commit for the final whole-branch review and the 2.0.3 release.

## Context you must respect

**Already committed on main (the entitlement-sync session's work — DONE, do NOT revert, reimplement, or alter it):**
- `734966b6` docs(plan): entitlement sync
- `2d0bf3b4` feat(entitlement): durable sync — cold-start refresh, PayPal deep-link return with retries (`EntitlementSync`, `PayPalReturnStore`, `PayPalReturnHandler`, `paypal_return_pending` DataStore flag)
- `ef01493a` fix(entitlement): clear paypal return pending on sign-out
- `21ffa8f6` feat(paywall): durable checkout-return sync with retrying state ("Still unlocking — retrying…", "Payment recorded — tap Restore, or wait a moment", `CheckoutReturnSync` seam)

Your PayMongo work must **build on** that: `PayMongoBillingController` implements the same `BillingController` seam; `MainActivity.handleCheckoutDeepLink` routes both hosts (`paypal` + `paymongo`) to the shared handler — keep that integration intact. Do not regress the exactly-once auto-continue flag or the `lastResult == null` gate in `PaywallViewModel`.

> **`git status` count note:** you'll see **19** untracked/modified entries, not 18 — the extra one is `HANDOFF_PAYMONGO_SESSION.md` (this file, still untracked). It is NOT part of your PayMongo work. Do NOT `git add` it (a bare `git add -A` WOULD stage it). Stage your 18 PayMongo files explicitly, or `git add -A` then `git restore --staged HANDOFF_PAYMONGO_SESSION.md` before committing.

**Your uncommitted work in the working tree (18 files — verify with `git status` before committing):**
- New: `app/.../data/billing/PayMongoBillingController.kt`, `PayMongoCheckoutJson.kt`, `app/src/test/.../PayMongoCheckoutJsonTest.kt`, `supabase/functions/_shared/paymongo.ts`, `paymongo.test.ts`, `supabase/functions/paymongo_create_checkout/`, `supabase/functions/paymongo_webhook/`, `supabase/migrations/20260809000000_paymongo_payments.sql`
- Modified: `AndroidManifest.xml`, `MainActivity.kt`, `EntitlementModule.kt`, `InputScreen.kt`, `PaywallScreen.kt`, `SettingsScreen.kt`, `SettingsViewModel.kt`, `PlanCards.kt`, `supabase/README.md`, `supabase/config.toml`

**Vault:** your **D114** decision is recorded in `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\Decisions.md` (sequence now D110–D114, all taken). Do not renumber or edit D111/D112/D113 — they belong to the entitlement-sync session's records.

## Tasks

1. **Finish your implementation to a stable, green state.** Pay special attention to the shared files where your copy meets the other session's committed code:
   - `PaywallScreen.kt` / `PlanCards.kt`: your "Continue with PayMongo · Pro", "One-time payment via GCash or card. You pay each month when ready — access ends on your expiry date. No auto-charge.", "WHEN YOU NEED IT" timeline, and Pro card tag changes must compile and read coherently WITH the other session's committed strings (retrying state, Free-card badge, Welcome-to-Pro copy). If your uncommitted edits conflict with their committed strings, resolve so the merged result is coherent and on-brand — do not delete their sync states.
   - `PaywallViewModel.kt`: if your D114 plan needs changes there (listed as modified in D114), apply them now and keep the durable-flag return path + exactly-once semantics working.
2. **Full verification (all must pass before commit):**
   - `cd "C:/Needs vs Wants/supabase" && deno test functions/_shared/paymongo.test.ts` (24/24)
   - `deno check` on `paymongo_create_checkout` and `paymongo_webhook`
   - `cd "C:/Needs vs Wants" && ./gradlew :app:testDebugUnitTest --rerun-tasks` (committed baseline is 209 tests; report the new total — target 215+; known repo quirk: re-run with `--rerun-tasks` on phantom compile errors)
   - `./gradlew :app:assembleDebug` — BUILD SUCCESSFUL
3. **Commit all your work on main** in logical commits (conventional style, e.g. `feat(paymongo): hosted-checkout provider — GCash/card manual renewal` for Android, `feat(paymongo): edge functions + migration` for supabase). `git add -A` is fine — the tree contains only your uncommitted files; verify with `git status` first and do NOT stage anything you didn't write. Do NOT touch `.superpowers/` (other sessions' scratch). **Staging note:** `HANDOFF_PAYMONGO_SESSION.md` at the repo root is this very document — it is tracked/committed separately by the orchestrator, so do NOT include it in your commits; if `git add -A` staged it anyway, `git restore --staged HANDOFF_PAYMONGO_SESSION.md` before committing.
4. **Do NOT** push, deploy the site, or bump the app version (the entitlement-sync session owns the 2.0.3 ship).
5. **Report back:** commit hash(es), test totals (deno + android), any conflicts you resolved against the entitlement-sync session's committed code.
