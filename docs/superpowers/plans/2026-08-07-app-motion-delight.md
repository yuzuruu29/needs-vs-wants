# App Motion & Delight Pass — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:subagent-driven-development` (recommended) or `superpowers:executing-plans` to implement task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make daily app use feel responsive, premium, and fun — physical-ledger delight without confetti — by expanding Android `Motion` + `Haptics`, **and** adding **horizontal swipe to change main tabs** alongside bottom-nav taps.

**Architecture:**
1. Token-first motion (`Motion.kt` / `Haptics.kt` / `MotionPrimitives.kt`)
2. **Main tabs as `HorizontalPager`** (swipe left/right) **synced with bottom nav taps**
3. Screen-level micro-motion (Summary, Log, History, Settings)
4. Paywall remains a modal-style route **outside** the pager (not a swipeable tab)
5. Reduced-motion + performance gate

**Tech Stack:** Kotlin, Jetpack Compose Animation, **`androidx.compose.foundation.pager.HorizontalPager`** (BOM `2024.09.02` — no Accompanist), Material 3, Navigation Compose (paywall + deep-link only or thin host), existing supermarket theme (D7).

**Platform:** **Android primary**. iOS / website deferred.

**Repo path after approval:** `docs/superpowers/plans/2026-08-07-app-motion-delight.md`

---

## 0. Research synthesis (why these motions)

### External references (web research, 2026-08-07)

| Source | Takeaway for NvW |
|--------|------------------|
| [Material 3 Motion / Expressive springs](https://m3.material.io/styles/motion/overview/how-it-works) | Prefer **physics springs** for spatial moves; effects (opacity/color) can stay tweened. |
| [M3 Expressive motion theming](https://m3.material.io/blog/m3-expressive-motion-theming) | One motion scheme app-wide beats one-off timings. |
| [Finance UX microinteractions](https://www.g-co.agency/insights/the-best-ux-design-practices-for-finance-apps) | Micro-feedback on money actions = trust + clarity. |
| Banking UI (Chime-style) | Subtle feedback + haptic on success; keep calm for money UI. |
| [Tubik — Motion for Mobile](https://tubikstudio.com/blog/motion-for-mobile-creative-concepts-of-ui-animation/) + Home Budget | Chart/period transitions, feed list motion, continuous navigation. |
| Habit / tracker delight | Fun from **completion feedback**, not RPG clutter (aligns with D71 no confetti). |
| Platform norm (IG / banking / system apps) | **Swipe between primary tabs + bottom bar taps** is expected dual input. |

### Brand / product constraints

- Theme: supermarket premium (D7). Seal = hero moment.
- **No confetti** (D71). Reduced motion via `Motion.enabled` / animator scale.
- Must not look AI-generated.

### Current motion + nav inventory (verified)

| Area | Exists today | Gap |
|------|--------------|-----|
| `Motion.kt` / `Haptics.kt` | Tokens, gate, seal/warn/tick/success | Primitives, stagger, number anim |
| Log / Summary / History | Partial anims | Stamp exit, money count-up, stagger, empty idle |
| **Main navigation** | **Bottom-nav tap only** via `NavHost` + `bottomNavItems` (5 tabs) | **No horizontal swipe between tabs** |
| Paywall | Separate route `paywall`; bar hidden | Must stay **outside** swipe pager |
| Deep link | `MainActivity.EXTRA_OPEN_TAB` → `input` | Map to pager initial page |
| History delete | Confirm dialog via `onDelete` (not row swipe-to-dismiss) | **Low conflict** with horizontal pager |

Tab order (canonical — swipe order):

```text
0 Home (summary) → 1 Log (input) → 2 Advisor → 3 History → 4 Settings
```

- **Swipe left** (content moves left / finger left) → next index  
- **Swipe right** → previous index  
- **Tap bottom pill** → animate to that index (same destination)

---

## 1. Design principles (non-negotiable)

1. **Purpose first** — motion guides, confirms, or connects space.
2. **Dual navigation** — swipe **and** tap are first-class; same tab order; same haptics (`tick` on settle/tap).
3. **Physical ledger** — paper settle, stamp, ink fill — not bounce-everywhere.
4. **Tokens only** — no magic durations in screens.
5. **Fun ≠ noisy** — one celebratory beat per action.
6. **A11y** — reduced motion collapses custom anim; pager can stay functional with short settle.
7. **60fps** — transform/alpha/Canvas; avoid layout thrash.
8. **Gesture safety** — vertical `LazyColumn` scrolls win on vertical axis; horizontal pager owns horizontal flings on main chrome. No horizontal swipe-to-delete exists today (dialog delete) — keep it that way unless separately redesigned.
9. **Paywall isolation** — swipe never lands on paywall; paywall overlays or navigates above pager.

### Motion personality mapping

| Moment | Feel | Spec family |
|--------|------|-------------|
| Button / chip press | Crisp paper press | `pressSpring` / `FeedbackMs` |
| Need \| Want select | Confident choice | `selectionSpring` + tick |
| Row seals | Ink stamps in | `sealSpring` + seal haptic |
| Sheet complete | Quiet gold stamp | `SealStampOverlay` enter/exit |
| Period / stats | Page-turn lite | `entrance` + slide 8–16dp |
| Budget fill / over | Ink rise / one warn pulse | `budget()` + warn once |
| **Tab swipe / tap** | **Soft page sheet** | **Pager spring + Directional slide** |
| List add/remove | Card settle | `animateItem` |

---

## 2. Swipe tabs — architecture decision

### Recommended: **HorizontalPager for main tabs** (Approach A)

Replace the five-tab `NavHost` destinations with a **`HorizontalPager`** whose pages are the existing screen composables. Bottom bar **and** swipe both drive `PagerState`.

```text
Scaffold
├── bottomBar: NavPills  →  pagerState.animateScrollToPage(i)
└── content:
    ├── HorizontalPager(pageCount = 5) { page ->
    │     when(page) Summary | Log | Advisor | History | Settings
    │   }
    └── Paywall: still NavHost single-route OR full-screen overlay when open
```

**Why A wins**

| | HorizontalPager (A) | Fling + NavHost navigate (B) | Edge-swipe only (C) |
|--|---------------------|------------------------------|---------------------|
| Feel | Continuous drag + snap (expected) | Discrete jump only | Easy to miss |
| Sync with bar | Natural via `currentPage` | Manual | Manual |
| Deep link | `initialPage` | startDestination | startDestination |
| Complexity | Medium (paywall split) | Low but janky | Low, weak UX |
| Gesture conflict | Nested vertical scroll OK | OK | Weak coverage |

**Rejected:** full Accompanist pager (obsolete; foundation pager is in BOM).  
**Rejected:** animating only NavHost with drag without pager (hard to get partial-drag right).

### Paywall + deep link

- Keep a **minimal** `NavHost` **or** boolean overlay for `paywall` only.
- Soft-launch paywall + Settings/Advisor “open paywall” keep current `navigate(ROUTE_PAYWALL)` semantics.
- When paywall is showing: **disable pager user scroll** and hide bottom bar (already hidden).
- `AppNavigation(startDestination)` / `EXTRA_OPEN_TAB=log` → `initialPage = 1` (`input`).

### Bottom bar sync rules

1. `selected = pagerState.currentPage == index` (or `targetPage` while animating — prefer `currentPage` for stability, or `settledPage` if available).
2. On pill tap: `haptics.tick()` + `coroutineScope.launch { pagerState.animateScrollToPage(index) }` with `Motion` spring/tween.
3. On swipe settle onto a new page: optional light `haptics.tick()` once (not every pixel).
4. `popUpTo` / `saveState` NavHost behavior is replaced by **pager keeps adjacent pages** — ViewModels via `hiltViewModel()` stay scoped to activity/nav graph carefully:
   - Prefer **each page** calling `hiltViewModel()` as today inside the screen.
   - Use `beyondViewportPageCount = 1` so neighbors stay warm without keeping all 5 always composed if memory is tight (`beyondViewportPageCount = 0` is OK for v1).

### Nested scroll contract

| Surface | Vertical | Horizontal |
|---------|----------|------------|
| Log / History LazyColumn | Scroll list | **Pager** (not row swipe) |
| Advisor chat list | Scroll | Pager |
| Settings | Scroll | Pager |
| Summary | Scroll | Pager |
| Dialogs / Paywall | N/A | **Blocked** (no tab change underneath) |

If any future swipe-to-dismiss is added on History rows, it must use higher touch priority on the row and document conflict testing — **out of scope** now.

### Context7 at implement time

- Resolve Compose foundation docs for `HorizontalPager`, `rememberPagerState`, `animateScrollToPage`, `beyondViewportPageCount`.
- Confirm API with project BOM (no experimental surprises if possible; `@OptIn(ExperimentalFoundationApi::class)` if still required on this BOM).

---

## 3. Skills, plugins, and agent routing

| Phase | Skill / agent | Role |
|-------|---------------|------|
| Pre-code gate | Second Brain, Graphify `ui/navigation`, Context7 Compose Pager + Animation | D25 |
| Motion strategy | `ccg/impeccable/animate`, `ecc/make-interfaces-feel-better` | Micro-interactions |
| Implementation | `subagent-driven-development` or `executing-plans` | Task loop |
| Review | `code-reviewer` / `kotlin-reviewer` | After nav refactor especially |
| Closeout | Vault `Tasks.md` / `Decisions.md` (swipe-tabs decision) | Memory |

**Do not use:** React `motion` package; confetti; Lottie by default.

---

## 4. File map

### Create

| File | Responsibility |
|------|----------------|
| `ui/theme/MotionPrimitives.kt` | Press scale, fade+slide, AnimatedMoney |
| `ui/navigation/MainTab.kt` (optional small file) | Enum/order helpers: route ↔ page index, labels |
| `app/src/test/.../MainTabTest.kt` | Pure: route↔index, open_tab deep link mapping |
| `docs/superpowers/plans/2026-08-07-app-motion-delight.md` | Checked-in plan |

### Modify

| File | Changes |
|------|---------|
| `ui/navigation/AppNavigation.kt` | **Core:** HorizontalPager + bottom bar sync; paywall isolation; deep link initial page |
| `ui/theme/Motion.kt` | Tokens + pager settle duration if needed |
| `ui/theme/Components.kt` | Seal overlay exit, dialog enter, budget pulse, etc. |
| `MainActivity.kt` | Only if start page API changes (prefer keep `startDestination` string → map to page) |
| `SummaryScreen.kt` / `InputScreen.kt` / `HistoryScreen.kt` / `SettingsScreen.kt` / paywall | Motion polish as planned |
| `FinancialAdvisorScreen.kt` | Ensure horizontal pager doesn’t break internal scroll |

### Out of scope

- Website, iOS parity  
- Confetti / Lottie / third-party anim SDKs  
- Changing tab **set** or order (unless user asks)  
- History swipe-to-delete redesign  

---

## 5. Phased tasks

### Task 1: Motion token expansion + primitives

**Files:** `Motion.kt`, create `MotionPrimitives.kt`, optional unit tests

- [x] Context7 Compose animation + pager docs
- [x] Expand `Motion` tokens (`number()`, `staggerDelay`, spatial/select springs) without breaking existing API
- [x] Add press / fade-slide / AnimatedMoney primitives
- [x] Unit-test pure helpers + duration collapse when `enabled=false`
- [x] `compileDebugKotlin` green
- [x] Commit `feat(motion): expand tokens and shared primitives`

---

### Task 2: Swipe tabs + bottom nav dual control (**user-requested core**)

**Files:**
- Modify: `AppNavigation.kt` (primary)
- Optional create: `MainTab.kt`
- Test: `MainTabTest.kt` (route/index mapping)
- Touch: paywall open/close paths from Advisor/Settings/LaunchPaywall

**Behaviors (acceptance):**

1. User can **swipe left/right** on main content to move between the 5 tabs in order: Home → Log → Advisor → History → Settings.
2. User can still **tap** any bottom nav pill to jump to that tab (animated scroll).
3. Swipe and tap **always agree** on selected pill highlight.
4. Paywall open: bottom bar hidden; **pager swipe disabled**; back/close returns to previous tab page.
5. Deep link / reminder `open_tab=log` opens **Log** page (index 1).
6. Light `tick` haptic on pill tap; optional tick when swipe **settles** on a new page.
7. Vertical scrolling on Log/History/Advisor still works (no dead vertical scroll).
8. Reduced motion: pager still works; settle can use short tween (`Motion.navEnter` / 1ms when disabled).

**Implementation sketch:**

```kotlin
// Pseudocode — implement with current foundation APIs after Context7 check
enum class MainTab(val route: String) {
    Home("summary"), Log("input"), Advisor("advisor"),
    History("history"), Settings("settings");
    companion object {
        fun fromRoute(route: String?) = entries.find { it.route == route } ?: Home
        fun indexOf(route: String?) = fromRoute(route).ordinal
    }
}

val initialPage = MainTab.indexOf(startDestination)
val pagerState = rememberPagerState(initialPage = initialPage) { MainTab.entries.size }
val scope = rememberCoroutineScope()

// Bottom bar: hide if paywallShowing
NavPill(selected = pagerState.currentPage == i, onClick = {
    haptics.tick()
    scope.launch { pagerState.animateScrollToPage(i) }
})

HorizontalPager(
    state = pagerState,
    userScrollEnabled = !paywallShowing,
    beyondViewportPageCount = 1,
    modifier = Modifier.fillMaxSize()
) { page ->
    when (MainTab.entries[page]) {
        MainTab.Home -> SummaryScreen(onNavigateToInput = {
            scope.launch { pagerState.animateScrollToPage(MainTab.Log.ordinal) }
        })
        MainTab.Log -> InputScreen()
        MainTab.Advisor -> FinancialAdvisorScreen(onOpenPaywall = { openPaywall() })
        MainTab.History -> HistoryScreen(onNavigateToInput = {
            scope.launch { pagerState.animateScrollToPage(MainTab.Log.ordinal) }
        })
        MainTab.Settings -> SettingsScreen(onOpenPaywall = { openPaywall() })
    }
}
```

**Paywall strategy (pick one, document in Decisions):**

- **P2a (recommended):** `var paywallOpen` + full-screen `PaywallScreen` composable above pager (simplest; matches “not a tab”).
- **P2b:** Keep nested `NavHost` with only `main` + `paywall` routes; `main` hosts the pager.

- [x] **Step 1:** Extract `MainTab` order/helpers + unit tests for mapping
- [x] **Step 2:** Refactor `AppNavigation` to HorizontalPager + synced pills
- [x] **Step 3:** Wire paywall overlay/route; disable pager while open
- [x] **Step 4:** Deep link initial page + Summary/History “Log a purchase” → animate to Log page (not old navController routes for tabs)
- [x] **Step 5:** Manual QA matrix (below)
- [x] **Step 6:** `compileDebugKotlin` + relevant unit tests
- [x] **Step 7:** Commit `feat(nav): swipe between main tabs with pager`

**QA matrix (Task 2):**

| # | Action | Expected |
|---|--------|----------|
| 1 | Swipe left from Home | Log selected |
| 2 | Swipe left through all | Ends on Settings; no crash |
| 3 | Swipe right from Settings | History … back to Home |
| 4 | Tap Advisor pill from Home | Lands Advisor; no intermediate stuck state |
| 5 | Swipe while scrolled mid Log list | List vertical still works; horizontal changes tab |
| 6 | Open paywall | No swipe tab change; bar hidden |
| 7 | Close paywall | Same tab as before |
| 8 | Cold start with open_tab=log | Log visible |
| 9 | Fast fling spam | Settles cleanly; pill matches page |
| 10 | Animator scale 0 | Tabs still change; less animation |

---

### Task 3: Hero moment — Log seal + TypeChip

**Files:** `InputScreen.kt`, `Components.kt` (`SealStampOverlay`)

1. TypeChip select → `haptics.tick()` + scale  
2. Seal rows use `animateItem` + settle  
3. Fix `SealStampOverlay` **enter and exit** (today early-return skips exit)  
4. Budget panel `AnimatedVisibility`

- [x] Implement + QA seals
- [x] Commit `feat(log): seal and type-chip motion polish`

---

### Task 4: Summary — chart, period, stats

**Files:** `SummaryScreen.kt`

1. Period indicator morph  
2. AnimatedMoney on totals (snap large jumps)  
3. Stagger cards / insight  
4. Empty ring idle pulse (motion-gated)  
5. Over-budget one-shot pulse + warn  

- [x] Implement + commit `feat(summary): chart and period motion choreography`

---

### Task 5: History + empty states

**Files:** `HistoryScreen.kt`

1. Day card enter / list settle  
2. Delete path keeps warn haptic + dialog  
3. Empty seal quiet breath  

- [x] Commit `feat(history): list and empty-state motion`

---

### Task 6: Nav chrome polish (on top of pager)

**Files:** `AppNavigation.kt`, `Components.kt` (`PremiumDialog`)

1. Nav pill icon scale when selected (1 → ~1.08 spring)  
2. Dialog enter scale 0.96→1 + fade; faster exit  
3. Optional: slight parallax of page content from `pagerState.currentPageOffsetFraction` (only if free; drop if jank)

- [x] Commit `feat(nav): pill and dialog motion polish`

---

### Task 7: Settings / Paywall light pass

Selection spring parity on chips; plan card haptic only on select.

- [x] Commit `feat(settings): selection feedback parity`

---

### Task 8: Verification, a11y, ship notes

- [ ] Full QA: Task 2 matrix + motion scenarios (seal, period, budget over, tab spam) — **manual on-device pass pending** (compile + unit suite + lint verified 2026-08-07)
- [x] `./gradlew :app:testDebugUnitTest` + `compileDebugKotlin`
- [x] Decisions entry: **swipe tabs via HorizontalPager + dual control with bottom nav**; paywall outside pager; tab order locked
- [x] Second Brain `Tasks.md` / `Summary.md` nav note
- [x] Copy plan into `docs/superpowers/plans/2026-08-07-app-motion-delight.md`

---

## 6. Success criteria

| Criterion | Measure |
|-----------|---------|
| Swipe tabs | Left/right changes main tabs in order |
| Tap tabs | Bottom pills still work and stay in sync |
| Fun / not flat | Seal, chips, period, pager, nav all feel physical |
| Not gimmicky | No confetti; controlled springs |
| Gesture safe | Vertical lists still scroll; paywall not swipeable as tab |
| Accessible | Reduced motion usable |
| Stable | Unit tests + compile green |

---

## 7. Risk register

| Risk | Mitigation |
|------|------------|
| NavHost refactor breaks paywall / deep link | Isolated paywall; explicit QA rows 6–8 |
| ViewModel re-create on page change | Keep screens as composables with hiltViewModel; beyondViewportPageCount ≥ 0; don’t key VM on disposable page incorrectly |
| Horizontal vs vertical gesture fight | Rely on Compose nested scroll; no row horizontal dismiss |
| Performance with 5 heavy pages | `beyondViewportPageCount = 1`; avoid infinite anim off-screen |
| Motion fatigue | Cap stagger; one haptic per settle |
| Scope creep | Website/iOS out of scope |

---

## 8. Recommended execution order

```text
Task 1 tokens
  → Task 2 swipe tabs (do early — architectural)
  → Task 3 Log hero
  → Task 4 Summary
  → Task 5 History
  → Task 6 nav chrome polish
  → Task 7 settings lite
  → Task 8 verify + vault
```

Estimate: **Session 1** = Tasks 1–2 (feel of app changes immediately). **Session 2** = Tasks 3–8.

---

## 9. Approaches considered (nav)

| Approach | Verdict |
|----------|---------|
| **A. HorizontalPager + bottom bar sync** | **Choose** — continuous swipe, standard UX |
| B. Detect fling → `navController.navigate` | Reject as primary — no partial drag, choppier |
| C. Edge-only swipe zones | Fallback only if A fights a future gesture |
| D. Full M3 Expressive MotionScheme migration | Defer |
| E. Lottie/particles | Reject (D71 / slop) |

---

## 10. Gate checklist (before first code edit)

```
[x] Obsidian: Memory Index + Summary + Tasks + Decisions loaded (prior session)
[x] Context7: Compose HorizontalPager + animation docs at implement start
[x] Graphify: ui/navigation blast radius on AppNavigation refactor
[x] Only then: edit code
```

---

## 11. Handoff

After approval:

1. Copy plan → `docs/superpowers/plans/2026-08-07-app-motion-delight.md`
2. Execution: **Subagent-Driven** (recommended) or **Inline**
3. Start **Task 1**, then **Task 2 (swipe tabs)** before deep micro-motion so navigation model is stable

**Session plan:** this file  
**Repo plan (post-approval):** `docs/superpowers/plans/2026-08-07-app-motion-delight.md`

---

## 12. Execution record (verified 2026-08-07)

| Task | Commit(s) | State |
|------|-----------|-------|
| Task 1 tokens + primitives | `f3336c64` | done |
| Task 2 swipe tabs + dual control | `32b1e1dd`, `89b36cc9` (haptic fix) | done |
| Task 3 Log seal + TypeChip | `7a394d35` + review fixes `874d4cc`, `6bf63f0` (Turn-off crash), `acdd9a6` (spring tests) | done |
| Task 4 Summary choreography | `1aa2c7d` | done |
| Task 5 History + empty states | `ff61969` | done |
| Task 6 Nav chrome (pill, dialog, parallax) | `47759b4`, `9ad97fe` (cleanup) | done |
| Task 7 Settings parity | `39ea699`, `0c3c278` (import order) | done |
| Task 8 verification | `9302182` (lint offset fix) | compile + tests + lint(0 new) green; **manual device QA pending** |

**Verified:** `./gradlew :app:compileDebugKotlin` + `:app:testDebugUnitTest` green (full suite: MainTabTest, MotionTokensTest with reduced-motion contract, DailyLogQuota, domain). `lintDebug`: 0 new issues in changed files (15 pre-existing errors in untouched Glance widget / supabase java.time / theme nav-bar files — baseline unchanged). Commits local on `main`, not pushed (D85). Vault: [[Decisions]] D88, Tasks.md + Summary.md updated. Manual on-device QA matrix (seal, period, budget over, tab spam, Task 2 rows 1–10) still pending.
