# Max Tier (₱399) — Coach Implementation Spec (P0: M1–M4)

**Date:** 2026-08-10
**Source plan:** Grok product plan "Max Tier (₱399) — Feature & Capability Plan" (approved for implementation: Approach A + P0 set M1–M4, park P2, Pro stays diary-complete).
**Platform:** Android first. No iOS, no website deploy beyond Max-card copy sync in this spec's Task 5 (website HTML copy only, keep mirrors byte-identical via apply.js/check.js).
**Pre-code gate:** Obsidian loaded (Summary/Tasks/Decisions D44/D49/D53/D72/D104/D114/D116/D129); Context7 N/A (no new library/API); Graphify blast radius = `FinancialAdvisor*`, `InputScreen` (pre-seal), `SummaryScreen` (recovery progress), `PaywallScreen` Max bullets, website Max card copy.

---

## Locked product decisions (do not revisit)

1. **Approach A** — Max = "cited coach on top of the unlimited diary". No bank linking, no cloud sync, no human advisors.
2. **Local-only engine in P0** — NO cloud LLM. All coach output is deterministic from the offline rule library + the user's sealed ledger. The quota model (20–40 msgs/day) is reserved for a future cloud-polish layer; do NOT build quota UI now.
3. **NotebookLM = static study packs** — `app/src/main/assets/economic_studies_index.json` stays the source-of-truth content. Every Max answer MUST carry a citation `Notebook #N · Section X.Y` (or the existing `NotebookLM Section X.Y` format). Unit tests pin citation presence on every engine output.
4. **Pre-seal friction is non-blocking** — never blocks Needs; reduced-motion safe; Free/Pro seal speed unchanged.
5. **Pro integrity** — nothing in Pro moves to Max (no unlimited-history/Month-analytics moves).
6. **Honest language** — never claim "real-time NotebookLM API" or "AI" beyond "grounded in economic study notebooks with citations". Paywall Max card copy follows the approved bullets.
7. **₱399 unchanged.** No billing/entitlement changes (gate stays `hasMaxAccessAt`).

---

## Current state (verified)

- `FinancialAdvisor.kt` (domain): `generateInsight()` = 3 rules (over-budget → recover 33% Wants/3 days · Wants>Needs → 24h delay · else balanced); `evaluateConversationalQuery()` = keyword router (over/budget, buy-want, default). Citations already present (`AdvisorCitation`, e.g. "NotebookLM Section 4.5").
- `AdvisorChatSession.kt` (domain): pure chat state, `sendUserQuery(query, entries, currencySymbol, dailyBudgetCents)` → appends user msg + engine reply. Unit-tested.
- `FinancialAdvisorViewModel.kt`: `hasMaxAccess` gate (fail-closed twice: UI + sendUserQuery), `AdvisorUiState(isLoading)`, chat messages flow.
- `FinancialAdvisorScreen.kt`: Max lock gate vs unlocked chat (gold bubbles, chips, quick prompts), `MaxLockedGate` with `LockedBookIllustration`.
- `InputScreen.kt`: seal form + `LogDailyBudgetSection`; overspend "Log anyway?" confirm.
- `SummaryScreen.kt`: bento layout; `StreakBentoCard`, sparkline, insights strip.
- `economic_studies_index.json` (assets): studies with `citation`, `summary`, `advice` fields (NotebookLM Section 1.2/3.1/4.5 family).
- `PaywallScreen.kt` Max card features: "Everything in Pro", "AI Financial Advisor with citations", "Footnotes from study notebooks", "Overspend recovery coaching".
- Website Max card bullets in `website/public/index.html` (mirror `website/index.html` kept byte-identical via `node _pad-parts/apply.js`; verify `node _pad-parts/check.js`).

---

## Tasks

### Task 1 — Structured coach pipeline: ledger context pack + expanded rule library (M1 core)

**Files:**
- `app/src/main/java/com/needsvswants/app/domain/FinancialAdvisor.kt` (extend; keep public API compatible where callers exist)
- `app/src/main/assets/economic_studies_index.json` (add rule entries: each rule has `id`, `keywords`, `condition` description, `advice`, `citation` — keep existing entries, append `study_05..study_08`-style rules for: recovery protocol, want-hold, budget health, need/want ratio, weekend plan, streak)
- Tests: `app/src/test/java/com/needsvswants/app/domain/FinancialAdvisorTest.kt` (extend existing suite)

**Requirements:**
1. New pure builder `AdvisorContextPack` (data class): today total, week total, needsCents, wantsCents, needsPct, wantsPct, budget status (On/Off + remaining), streakDays, topWantItems (top 3 by cost, from entries), spendingGoal (String: track|budget|analyze). Built from `List<Entry>` + budget + goal. Pure, no Android types.
2. `FinancialAdvisorEngine` (or extend existing object) exposes:
   - `generateInsight(context)` — keep existing 3 rules, extend to ≥6 rules from the asset library (over-budget recovery, wants>needs hold, budget-health OK, streak encouragement, weekend plan when streak ≥5, analyze-goal period comparison). Every rule returns advice + citation.
   - `evaluateConversationalQuery(query, context)` — keep keyword routing but now sourced from the rule library (≥8 branches: overspend/budget, buy-want/afford, want-share/ratio, budget-health, streak, weekend, goal/analyze, default). Every branch returns advice + citation. No branch may return without a citation.
3. `AdvisorChatSession.sendUserQuery` must pass the context pack through (build context in the ViewModel from entries + prefs and pass to the session, OR build inside session from entries + budgetCents + goal — choose the cleaner seam; keep the session pure and unit-testable).
4. Citation guarantee: a unit test iterates every rule/branch and asserts `citation` is non-blank and matches `Notebook #\d+ · Section \d+\.\d+` OR the existing `NotebookLM Section X.Y` format.
5. Keep `AdvisorChatSession` constructor/snapshot API stable (Screen + VM depend on it).
6. Update `docs/advisor/financial_advisor_engine.py` ONLY if it breaks nothing (it is not production-wired; leave it or note stale — do not spend time syncing it).

**Acceptance:** all existing tests green + new tests: context-pack math (pct, top items, budget status), ≥6 insight rules each with citation, ≥8 chat branches each with citation, no-citation test fails on regression. `./gradlew :app:testFullDebugUnitTest --tests '*FinancialAdvisor*'` green.

### Task 2 — 3-day Compensatory Recovery Plan model + UI (M3)

**Files:**
- `app/src/main/java/com/needsvswants/app/domain/FinancialAdvisor.kt` (add `RecoveryPlan` model + pure builder)
- `app/src/main/java/com/needsvswants/app/ui/theme/RecoveryPlanCard.kt` (new composable) OR inside advisor screen — prefer a dedicated composable in the advisor package
- `app/src/main/java/com/needsvswants/app/ui/screens/advisor/FinancialAdvisorScreen.kt` (surface: recovery card after overspend; "Quick protocols" chip "Overspend" triggers it)
- `FinancialAdvisorViewModel.kt` (state + trigger)
- Tests: `RecoveryPlanTest.kt` (pure math)

**Requirements:**
1. Pure `RecoveryPlan(overByCents, dayCaps: List<Long> (3), needOnlyEvenings: Boolean, citation)` built from: today's overspend amount, daily budget, wants trend. Deterministic cents math:
   - D+1 cap = max(0, budget - wantsToday*0.33)  (recover 33% of today's Want spend)
   - D+2 cap = max(0, budget - wantsToday*0.2)
   - D+3 cap = budget
   - `needOnlyEvenings` = true when overBy > 25% of budget.
   - Citation: "Notebook #3 · Section 4.5 — Impulse Recovery" (match existing asset id).
2. UI `RecoveryPlanCard`: eyebrow "RECOVERY PLAN", title "3-day plan · over by ₱X", 3 rows D+1..D+3 with daily Want caps, "Need-only evenings" toggle line when true, citation footnote, "Log anyway" already handled elsewhere — this card is informational (no action buttons beyond dismiss).
3. Advisor screen: after a budget-exceeded seal (or when `overBy > 0` on Advisor open), show the recovery card above chat; "Overspend" quick-protocol chip also builds it from live context.
4. Reduced motion: card uses `Motion.entrance()`; static when motion off.

**Acceptance:** pure math tests (33%/20%/0 caps, needOnlyEvenings threshold, zero-over returns null plan); VM exposes `recoveryPlan: StateFlow<RecoveryPlan?>`; screen renders card when present.

### Task 3 — Pre-seal Want coach: chip + soft hold gate (M2)

**Files:**
- `app/src/main/java/com/needsvswants/app/ui/screens/input/InputScreen.kt` (chip + soft gate near the Want chip / seal area)
- `InputViewModel.kt` (Max-gated coach state: expose `hasMaxAccess`; `wantHoldSuggestion(costCents)` pure-ish helper or reuse domain)
- `app/src/main/java/com/needsvswants/app/domain/FinancialAdvisor.kt` (pure `wantHoldSuggestion(costCents, context)` → `WantHold(hold: Boolean, reason: String, citation)` or null when Max-less)
- Tests: `WantHoldTest.kt` (pure)

**Requirements:**
1. Pure `wantHoldSuggestion(costCents, context)`: returns `WantHold(hold=true, reason, citation)` when the draft Want is > 15% of remaining daily budget OR wants share > 55%; else `WantHold(hold=false, ...)`. Citation from rule library ("Notebook #3 · Section 1.2/4.5"). Returns null when budget is Off (no guardrail to measure against).
2. UI (Max only, `hasMaxAccess`): when the user marks a row **Want** with a cost, show a quiet **Coach chip** next to the seal area: "Ask Max before sealing" → opens the soft gate dialog; soft gate only when `hold == true`: "24h hold suggested" + reason + citation + buttons **Seal anyway** / **Hold** (Hold cancels the seal; row stays draft). When `hold == false`: chip shows "Max: looks okay" quietly, no dialog.
3. Non-blocking: Needs never gated; Free/Pro see nothing (zero UI change); reduced-motion safe; haptics tick on chip tap.
4. Respect the existing seal flow (`trySeal`, quota gate, overspend confirm) — the coach chip must not interfere with `isFull`/`trySeal` logic; soft gate only intercepts the Want seal when Max + hold.

**Acceptance:** pure tests (15% threshold, wants-share >55%, budget-off → null, citation present); InputScreen compiles with chip; existing InputViewModel tests stay green.

### Task 4 — Advisor home: dashboard, not empty chat (M4)

**Files:**
- `FinancialAdvisorScreen.kt` (dashboard layout)
- `FinancialAdvisorViewModel.kt` (today insight + protocols)
- (reuse Task 1 engine)

**Requirements:**
1. Unlocked Advisor opens to a dashboard: **Today's insight** card (from `generateInsight` on live context), **Quick protocols** chip row: `Overspend` · `Can I buy this?` · `Want share` · `Budget health` · `Weekend plan` — tapping a chip sends the corresponding query into chat (reuse `sendUserQuery`) and shows the reply; **Chat** below (existing surface, secondary).
2. Cold open always shows the insight card without typing. Chat input stays.
3. Keep the Max lock gate + `MaxLockedGate` untouched.
4. Reduced motion / existing motion tokens.

**Acceptance:** screen renders insight + chips + chat; tapping chip appends a user message + cited reply; VM tests for protocol mapping (chip → query text) if VM holds the mapping (else pure helper tested).

### Task 5 — Paywall + website Max copy sync

**Files:**
- `app/src/main/java/com/needsvswants/app/ui/screens/paywall/PaywallScreen.kt` (Max card `features` list)
- `website/public/index.html` + regenerate mirror via `node website/_pad-parts/apply.js` + `node website/_pad-parts/check.js`

**Requirements:**
1. Paywall Max card features → approved bullets (verbatim):
   - "Everything in Pro" (emphasize)
   - "Live insight card from your sealed ledger" (true)
   - "Pre-seal Want coach + hold suggestions" (true)
   - "3-day overspend recovery plans" (true)
   - "Grounded citations on every answer" (true)
2. Website Max card `.pri-list` bullets updated to match (same 5 lines, honest language, no em/en dashes — use middle dots or periods). Do NOT touch prices, ids (`#get`, `payPalSeamBtn`), QR, flip, or any locked item (D22/D23/D32/D55).
3. apply.js regenerates both HTML copies byte-identical; check.js ALL CHECKS PASSED. **Do NOT deploy** (deploy happens in the release flow, not this spec).

**Acceptance:** unit suite green; `node _pad-parts/check.js` ALL CHECKS PASSED; both mirrors byte-identical; no price/id changes.

---

## Global constraints (bind every task)

- **No cloud/network code.** No new dependencies. No new permissions.
- **No entitlement changes.** Gate = `Entitlement.hasMaxAccessAt` (unchanged). Free/Pro behavior identical.
- **Copy discipline:** no em/en dashes in new user-facing strings; use periods or middle dots. (Site `check.js` enforces on HTML.)
- **Reduced motion:** every new animation consumes `Motion.*` tokens; collapses when `Motion.enabled == false`.
- **Haptics/SFX:** reuse `AppHaptics` (`tick`/`seal`) and `rememberAppSfx`; no new assets.
- **Tests:** pure logic must be unit-tested (JUnit, `app/src/test`); UI stays compile-verified + existing suites green. Suite must stay ≥ 281 passing.
- **Citations:** every coach answer (insight, chat, recovery, hold) carries a citation string; tests pin presence.
- **Brand:** supermarket palette tokens only (`AppTheme.colors`), no new colors, no confetti (D71/D102).
- **Do not** modify `Entitlement.kt`, billing, webhooks, or the entitlement sync retry schedule.
- **Verification per task:** targeted test run + `./gradlew :app:compileFullDebugKotlin`. Final: `:app:testFullDebugUnitTest :app:lintFullDebug :app:assembleFullDebug` all green.

## Out of scope (explicit)

- Cloud LLM polish + message quota UI (Phase 3, parked).
- Weekly Max brief / share (M5), goal-aware protocols beyond goal passthrough (M6), afford-this-Want tool (M7), coach memory (M8), Taglish, widget line, Want clustering, scenario sims, coach-trail export.
- iOS parity, website deploy, version bump/release.
- `docs/advisor/financial_advisor_engine.py` sync (stale-but-harmless).
