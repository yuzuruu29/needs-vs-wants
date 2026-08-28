# Premium Redesign Plan — Needs vs Wants (Android)

Date: 2026-08-28
Status: PROPOSAL (not yet approved for implementation)
Scope: Android app UI only. Website is out of scope for this pass.
Agent: ZCode (Poteto Mode, Investigation playbook)

---

## 1. Verdict

The app is half premium already, and the half that is premium is the valuable half.

**What is genuinely premium (keep, protect, do not touch):**
- The paper-ledger material language. `rememberPaperSpec` desk shadows, matte paper
  surfaces, the gold hairline trim. This is a real, authored identity, not a template.
- The hero dial (`FloatingGeminiOrb.kt`). A static matte paper dial with butt-capped
  Need/Want arcs and a contact shadow. Deliberately anti-glow. The most premium object
  in the app.
- The motion and haptics choreography. `Motion.kt` tokens, the odometer money roll,
  the SEALED stamp, the paper page-flip tab transition, InkWave press indication.
- The state coverage. Loading skeletons matched to layout, honest empty states,
  overspend confirms, quota blocks, billing result machines. Best-in-class.
- The ledger metrics engineering. Font-scale-scaled columns, width-aware money fitting
  with an 11sp floor. The best XL-text handling in the codebase.

**What reads AI-generated (the redesign target):**
- The identical header formula on every screen. Crimson/gold `Eyebrow` then a 36sp
  pencil-script title then `GiltRule(40.dp)` then a subtitle. Repeated verbatim on
  Summary, Log, History, Settings, Advisor, and every `PremiumDialog`. One signature
  becomes a tic by the third screen.
- Pill and chip proliferation. At least six distinct chip styles (period, trend,
  legend, type, compact, spending-goal, protocol), each with its own radius and padding.
- Card-stacking. The Advisor screen is five stacked `PremiumSurface` cards. The paywall
  is three equal-weight tier cards with a "MOST POPULAR" ribbon, the most generic SaaS
  pricing pattern there is.
- A typography drift that contradicts the brand spec (see section 2).

**The one-line summary for the maintainer.** The craft layer is done. The composition
layer is where the app looks generated. This plan is mostly subtraction and
consolidation, with one high-leverage typography decision. It is not a repaint.

---

## 2. The headline finding: typography drifted from the spec

Verified in code, `app/src/main/java/com/needsvswants/app/ui/theme/Type.kt:19-38`:

- Display face is **Caveat** (a handwritten pencil script), used for every screen title
  at 36sp. Comment reads "Soft pencil display face (hybrid type B, D94)".
- Body face is **Source Sans 3** for UI, ledger, eyebrows, and money.
- **Playfair Display SC** is still bundled but demoted. Comment reads "Legacy Playfair
  SC kept bundled for any one-off call sites / seals".

The documented spec disagrees. `docs/UI_GUIDELINES.md:53` says Inter Tight + Playfair
Display SC. The vault `Summary.md` theme table says body Inter / Inter Tight, display
Playfair Display SC. Both are stale. The code drifted to Caveat + Source Sans 3 at D94
and the docs were never updated.

**Why this is the top lever.** A handwritten pencil font at 36sp as a screen title reads
as journal, craft, casual. It is distinctive, but it is the opposite of "premium". Every
screen title carries it, so the single change with the largest premium impact is fixing
the display voice. This is also the one genuine brand decision in the plan and needs a
human call (section 3).

---

## 3. The one decision fork: the display font

This is a brand-identity choice, not a fact an experiment can settle on its own, so it is
surfaced as a decision rather than silently made. Three viable options.

| Option | What it is | Premium read | Risk |
|---|---|---|---|
| A. Keep Caveat, re-scope it | Caveat stays but only for the seal, stamps, and handwritten annotations (where pen-on-receipt is authentic). Screen titles move to a confident display face. | High. Keeps the distinctive craft where it means something, removes it where it reads casual. | Low-medium. Two display faces to manage. |
| B. Promote Playfair Display SC | Restore the documented spec. Serif small-caps titles. | Medium-high. Editorial and premium, but serif-as-default is a known generic tell and it is the font every budget app reaches for. | Low. Already bundled. |
| C. Adopt a new premium display face | A confident sans display (PP Neue Montreal, GT Walsheim, Cabinet Grotesk class) for titles. | High and most modern, but furthest from the paper-ledger identity. | Medium. New font asset, new licensing check. |

**Recommendation: Option A.** Demote Caveat to seal/stamp/annotation duty and promote a
calm, confident display face for screen titles. This preserves the one truly distinctive
asset (handwritten marks on a ledger are authentic) and removes the casual read from the
places that need to feel premium. Option B is the safe fallback if no new font is wanted.

**Prototype before committing.** Render the Summary and Paywall headers in A and B
side by side (throwaway Compose preview or two static screenshots) and pick by eye. This
is the one step where looking at the result beats arguing about it.

Decision number to assign on approval: next free `D###` after D185.

---

## 4. Phased plan

Sequenced so each phase ends in a verifiable state. Phase 0 is prerequisite and is almost
pure deletion. Later phases build on it.

### Phase 0 — Foundation: subtract and consolidate

Goal: remove dead weight and collapse the token sprawl so later phases have a clean base.
This is `principle-subtract-before-you-add` and `principle-laziness-protocol` applied.
No visual intent changes yet.

Changes:
- Delete the 13 unused legacy color aliases in `Color.kt:39-52` (Ink, Gilt, PrimarySky,
  SurfaceNight, etc.). The instance aliases on `AppPalette` are used and stay.
- Remove `Danger` duplication of Crimson, or give it a distinct role. Add missing
  semantic tokens: `onGold`, `onCrimson`, success/warning. Replace the hardcoded
  `Color(0xFF1A1208)` on-gold ink in `PlanCards.kt:95,144` and `MembershipDesk.kt:56`.
- Delete dead decorative code: `glassBlobBackground`, `giltGlow`, `GiltCard` (contains
  the real bug `goldEdge = true || giltAccent` at `Components.kt:507`), `GoldUnderline`,
  `StreakLine`, `SummarySkeletonSparkline`, and the three unused illustrations.
- Delete orphaned motion tokens: `RecoilMs`, `TabGlideMs`, `ReceiptUnrollMs`,
  `PagerSettleMs`, `OrbFloatMs`, `OrbBreathMs`, `OrbBeamMs`, `EaseSpringRecoil`,
  `EaseTabGlide`.
- Make the shape scale real. Collapse the 18 ad-hoc radii onto `AppShapes`
  (6/8/16/20/28). Add 12 and 14 to the scale since they are de-facto steps. Replace
  literal `RoundedCornerShape` call sites screen by screen.
- Introduce a spacing scale. A `Spacing` object on a 4dp ladder (4/8/12/16/24/32/48)
  replacing ad-hoc padding literals.
- Fix the hardcoded light-only color in `Illustrations.kt:56` (breaks dark theme) and the
  widget palette drift in `NvwWidget.kt:102-107`.
- Rename `FloatingGeminiOrb` to something honest (`SpendDial`). The name leaks an old
  design era.

Acceptance:
- Existing suite green on both flavors (baseline 431/431).
- Zero references to deleted symbols (grep).
- No visual diff on the default theme except the radius/spacing normalization.

Risk: low. This is deletion plus token wiring. The only behavior change is the GiltCard
bug removal, and GiltCard has one caller.

### Phase 1 — Typography and hierarchy (the premium lever)

Goal: fix the display voice (section 3 decision) and kill the repeated header formula.
This is the highest-impact phase for "looks premium".

Changes:
- Apply the chosen display-font option from section 3.
- Break the identical header formula. Each screen gets a header sized to its actual
  hierarchy instead of eyebrow + 36sp script + gold rule. Concretely:
  - Summary: the dial is the hero. Shrink the header block so the dial moves up. The
    current header is three loud stacked elements before any data appears.
  - Log, History, Settings, Advisor: one quiet title treatment, no decorative gold rule
    under every title. Keep the gold rule as a rare accent, not a per-screen tic.
- Cut eyebrow overload. 37 `Eyebrow(` call sites today, 12 on Summary alone. Keep only
  the ones that label a real section. Remove the `Eyebrow(size = 10)` call sites that
  silently floor to 11sp.
- Restore the type ladder. Replace the six `.copy(fontSize = ...)` bypasses in
  `SummaryScreen.kt` (675, 693, 827, 843, 866, 994) with real tokens (add `moneyXl`,
  `moneyDisplay`). Fix the 9sp sparkline tooltip (`SparklineChart.kt:137`) to the 11sp
  floor. Replace `TrendPill` text glyphs (up/down/right arrows at `TrendPill.kt:29-31`)
  with proper icons.
- Remove the bundled-but-unused Playfair SC font if Option A or C is chosen (saves
  ~200KB APK).

Acceptance:
- No two screens share an identical header block.
- Every text size maps to a token (grep for `.copy(fontSize` returns none in screens).
- XL-text pass clean on Summary, Log, History, Paywall (user QA's at Extra large).

Risk: medium. Touches brand identity and every screen header. Gated on the section 3
decision and the A/B prototype.

### Phase 2 — Composition: fix the three most template-reading surfaces

Goal: rebuild the paywall, the advisor, and the filter/empty patterns so they stop
reading as stock components.

Changes:
- Paywall (`PaywallScreen.kt`, `PlanCards.kt`): stop treating Free/Pro/Max as three
  equal stacked cards with a "MOST POPULAR" ribbon. Make Max the flagship (larger,
  featured), Pro secondary, Free quiet. Reduce the up-to-9 stacked elements per
  `PlanTierCard`. Collapse the reassurance copy that currently appears three times
  (in-card lede + timeline card + two footer notes). Shorten CTA labels so they fit one
  line ("Continue with PayPal · Pro (3-day trial)" wraps today).
- Advisor (`FinancialAdvisorScreen.kt`): the chat lives inside a card inside a
  vertically scrolling page today, with the input scrolling away and bubbles capped at a
  fixed 280dp. Make the advisor conversation a real full-height chat surface. Cut the
  five-stacked-card layout. Remove the per-bubble "YOU"/"ADVISOR" eyebrows (labels louder
  than the message). Fix the green send button borrowing the "Need" semantic color and a
  hardcoded white icon.
- History filter bank (`HistoryScreen.kt:175-234`): two stacked rows of chips with "All"
  in both reads as a bug. Label the rows or merge them. The four gold period chips
  differentiate only by fill alpha, the weakest chip group in the app.
- Consolidate chips app-wide to two styles (a selectable filter chip and a semantic
  tag). `TypeChip` (InputScreen) and `EditTypeChip` (HistoryScreen) are near-duplicates
  and should become one shared component.
- Make the non-scrollable dialogs scroll: `InstructionsOverlay` and
  `SplitPercentagePortal` both clip at XL text. These are the highest-risk XL surfaces.
- Fix the visible bug: duplicate empty CURRENCY section header at
  `SettingsScreen.kt:331-333`.

Acceptance:
- Paywall reads as one flagship plus two supporting options, not three equal cards.
- Advisor chat input stays reachable while messages grow.
- Both previously-clipping dialogs scroll at Extra large text.
- Chip styles reduced from six to two (grep the chip composables).

Risk: medium. The paywall and advisor are revenue and retention surfaces. Ship behind the
existing state machines, which are already strong.

### Phase 3 — Engagement: peak-end loop and signature moments

Goal: make the app feel alive, per `principle-experience-first`. This targets "engaging"
where Phases 0-2 target "premium".

Changes:
- Design the peak moment. Sealing an entry is the core loop. The SEALED stamp and haptic
  exist. Make the first seal of the day and streak milestones land with a small,
  intentional celebration (the existing `SealStampOverlay` plus a streak beat). Keep it
  restrained to match the paper identity.
- Design the ending. The Summary screen already has the dial and insight strip. Add a
  gentle close-the-day affirmation and a reason to return, not a marketing nudge.
- Rebuild onboarding (`InstructionsOverlay`) as a guided narrative toward the first seal,
  not five stacked cards with goal picker. The first-run path should end in the user
  sealing one real entry.
- Surface the signature moments. The dial long-press portal, the odometer roll, and the
  activation seal are the premium set pieces. Ensure each has one clear entry point and a
  reduced-motion collapse (already required by D88/D184).

Acceptance:
- A new user reaches their first sealed entry within onboarding.
- First-seal-of-day and streak milestones have a visible, restrained celebration.
- All signature moments honor reduced motion.

Risk: low-medium. Builds on existing primitives. Do this last so it sits on the cleaned
token base from Phase 0.

---

## 5. What this plan deliberately does not do

- No new UI toolkit or component library. The project rule (UI_GUIDELINES.md) requires a
  decision-numbered change for that, and none is needed.
- No glassmorphism, no decorative gradients, no automatic cardification. These are the
  project's documented anti-slop list and the source of the current problem.
- No website changes this pass.
- No repaint of the paper identity. The material language is the asset.

---

## 6. Verification approach

- Baseline before Phase 0: record current suite count on both flavors (431/431) and take
  reference screenshots of all six screens at default and Extra large text.
- Each phase ends in a green suite plus a visual diff review against the reference set.
- XL-text pass is mandatory at the end of Phase 1 and Phase 2 (user QA's at Extra large).
- Reduced-motion pass at the end of Phase 3.
- Follow the repo pre-code gate (Obsidian + Context7 + Graphify) before any phase starts
  implementation. This document is the plan; implementation is a separate approval.

---

## 7. Open items for the user

1. Approve the display-font direction (section 3). Recommendation is Option A.
2. Confirm phase ordering. Recommendation is 0 then 1 then 2 then 3, with a shippable
   checkpoint after each.
3. Decide whether Phase 3 (engagement) is in scope now or deferred to a follow-up.
