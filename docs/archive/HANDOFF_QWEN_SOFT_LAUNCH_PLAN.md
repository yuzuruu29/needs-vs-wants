> ARCHIVED 2026-08-13 — historical snapshot, do not trust for current state.

# Handoff for Qwen 3.8 Max — Soft-Launch Site Plan

**To:** Qwen 3.8 Max (planner)  
**From:** Grok (implementer session, 2026-07-31)  
**Project:** Needs vs Wants  
**Repo:** `C:\Needs vs Wants`  
**Your job:** Produce a **concrete implementation plan** (not code). The user will send your plan back to Grok for execution.

### Note for Grok when implementing Qwen’s plan (STRICT)

**Before any code edit**, Grok must run the project pre-code gate (D25 / `AGENTS.md`):

1. **Obsidian** — `Projects/Needs vs Wants/` notes  
2. **Context7 (ctx7)** — docs for any library/API in the plan  
3. **Graphify** — scoped blast-radius / architecture orientation  

Do not start implementation until all three are done.

---

## 1. Mission for Qwen

Write a plan that Grok can implement in one focused session. The plan must:

1. Fix / redesign the **hero Log notepad page-flip** so it **actually looks like flipping paper** (current version is still not acceptable).
2. Preserve working soft-launch pieces (APK download, QR install, CTA roll, seal-to-log demo).
3. Stay within a **static single-file site** (`website/public/index.html` + APK asset) deployed on Vercel.
4. Match brand: supermarket-premium, Playfair + Inter, crimson/green/gold — **not** generic AI SaaS UI.

**Output format (required):**

```markdown
# Soft-Launch Notepad Flip — Implementation Plan

## Goals
## Non-goals
## Current-state diagnosis
## Recommended approach (pick ONE primary technique + why)
## Alternatives considered (and why rejected)
## UX specification (states, controls, animations)
## Technical design (DOM structure, CSS, JS, libs if any)
## Step-by-step implementation order
## Acceptance criteria (testable)
## Risks & fallbacks
## File touch list
## Out of scope
```

Do **not** write full production code. Pseudocode and structure sketches are fine. Prefer one clear recommendation over a menu of half-options.

---

## 2. Product context (short)

**Needs vs Wants** is an offline-first expense trainer: every purchase is sealed as **Need** or **Want**. Platforms: Android (Kotlin/Compose) + iOS (SwiftUI). Soft-launch marketing site ships the story + Android APK sideload.

| Item | Value |
|------|--------|
| Live site | https://needs-vs-wants.vercel.app |
| Site root for deploy | `website/` (Vercel project `needs-vs-wants`) |
| Canonical HTML | `website/public/index.html` (also sync `website/index.html`) |
| APK | `website/public/downloads/needs-vs-wants-1.0.0.apk` |
| Brand tokens | Surface `#FAFAF7`, crimson `#C8102E`, green `#0B6B3A`, gold `#E8A92A` |
| Type | Playfair Display SC (titles), Inter (UI) |

Second Brain (Obsidian): `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\`  
Key notes: `Summary.md`, `Tasks.md`, `Decisions.md`, `Log Sheet Paper Flip.md`, `Soft Launch CTA Motion.md`.

---

## 3. What already works (do not break)

### 3.1 APK download + QR install
- Download button: `#apkDownload` → `./downloads/needs-vs-wants-1.0.0.apk`
- QR library: **must** use `qrcode@1.5.1` browser build  
  (`1.5.4/build/qrcode.min.js` 404s on jsDelivr — D22)
- QR panel on the **right** of the red CTA; stamp top-right must not cover QR

### 3.2 Red CTA panel height animation (D23)
- **Only** `.cta-panel` rolls down/up on Scan QR toggle
- QR **hides immediately** on close; panel still rolls up
- `MIN_ROLL` forces visible height delta on desktop
- **Do not** reintroduce perimeter gold “border roll” or QR-card border animations

### 3.3 Interactive Log demo (seal flow)
- Item + cost + Need/Want → row seals (no save button)
- Max 20 rows per page; multi-page `pages: Row[][]`
- Currency switcher, delete row, live region announcements

### 3.4 Deploy rules
- Always deploy **from `website/`**, never monorepo root (uploads 100MB+ junk)
- After prod deploy, re-alias if needed:  
  `vercel alias set <deployment-url> needs-vs-wants.vercel.app`

---

## 4. What is broken / rejected (main planning target)

### 4.1 Hero Log “page flip” — user feedback timeline

| Version | What shipped | User reaction |
|---------|----------------|---------------|
| v1 | Decorative under-sheets + keyframe leaf | “Bad animation”; no flip button |
| v2–v3 | Book leaf + Flip button, rotateY, opacity fades | Stiff; awkward stop |
| v4 | Notepad chrome (rings, stack, dots, dog-ear), rotateX | “Flips in one direction” |
| v5 | Left-spine rotateY, opaque leaf, sheet hidden mid-flip | Still “doesn’t look like flipping at all” |

### 4.2 User’s current requirement (authoritative)

> Turn the Log demo into a **proper notepad** where you can **see** stacked pages and **interact** and **flip pages back and forth**. The flip must **look like a real page flip**, not a fade, scale, or dim.

Must-haves for the plan:

- [ ] Visible notepad / paper stack (not a flat card)
- [ ] Explicit controls: **Previous page** + **Next page** (and optionally dog-ear / page dots)
- [ ] Bidirectional flip that is **visually distinct** (next vs previous)
- [ ] Flip reads as **3D paper turning** to a non-designer in under 1 second of watching
- [ ] Active page remains **interactive** for logging after flip settles
- [ ] Works in modern Chromium/Safari/Firefox; respect `prefers-reduced-motion`
- [ ] No heavy build step required (static HTML preferred); CDN libs OK if justified

### 4.3 Current implementation sketch (as of handoff)

**Files:** `website/public/index.html` (CSS ~lines 145–290, HTML notepad block in hero, JS flip ~`flipPage`)

**DOM (approx):**
```
.notepad
  .notepad-bind          (fake metal rings)
  .notepad-stage
    .notepad-stack       (under-pages with peek labels)
    .notepad-shade
    .notepad-leaf        (front/back faces, WAAPI rotateY)
    .sheet               (interactive form + ledger; hidden while flipping)
  .notepad-nav
    Previous page | Next page | page dots | Page X of Y
```

**JS state:** `pages: Row[][]`, `pageIndex`, `flipPage(±1)` via Web Animations API.

**Likely failure modes to diagnose in the plan:**
- Leaf may still not read as thickness/curl (flat rotate only)
- Hiding entire `.sheet` may feel like a hard cut rather than a turn
- Under-stack may not feel connected to the turning page
- Perspective origin / origin edge may be wrong for “notepad” vs “book”
- Snapshot faces may look sparse compared to the live sheet (uncanny)

---

## 5. Constraints for the plan

### Hard
- Stay static-site friendly (no Next.js rewrite)
- Keep seal UX: item + cost + type → auto-seal
- Keep APK + QR + CTA behavior
- Brand palette / type as above
- Accessibility: keyboard buttons, aria labels, reduced motion path

### Soft
- Prefer zero or one small CDN dependency for flip quality
- Prefer ~800–1200ms flip duration
- Mobile: touch-friendly controls; swipe optional, not required

### Explicit non-goals
- Do not rebuild the Android/iOS apps
- Do not redesign the whole marketing site
- Do not add accounts/backend
- Do not “fix” by only changing easing numbers again without a structural redesign if needed

---

## 6. Research directions Qwen should evaluate

Compare and **pick one** primary approach with rationale:

| Approach | Pros | Cons |
|----------|------|------|
| **A. StPageFlip / page-flip (CDN)** | Proven realistic flip, shadows, hard/soft pages | Integrating live form may need “static pages + one live overlay” pattern |
| **B. Custom WAAPI/CSS 3D (improved)** | No dependency, full control | Easy to underdeliver (history of failures) |
| **C. Hybrid** | Live form always on top layer; flip is pure visual between page states | Must sync form state carefully |
| **D. 2.5D peel (clip-path + shadow)** | Often more “paper” than pure rotateY | Less “book”, more “sticky note” |

Also reference quality bars (study, don’t copy wholesale):
- StPageFlip demos (nodlik.github.io/StPageFlip)
- Classic CSS 3D page-flip / book demos (bending strips if proposing custom)
- motionsites.ai / 21st.dev motion craft: deliberate easing, spatial continuity, no purposeless opacity spam

**If recommending a library:** specify exact package, version, CDN URL, license, how the interactive Log form mounts (e.g. only current page is a live form; other pages are static HTML snapshots).

---

## 7. UX specification Qwen must fill in

Please specify in the plan:

1. **Notepad physical model** — top-bound pad vs left-bound book (justify)
2. **Page model** — max pages? auto-create blank on next? delete empty trailing pages?
3. **Controls layout** — placement of Previous/Next, dots, dog-ear; disabled states
4. **Flip timeline** — ms, key poses, what user sees at 0% / 50% / 100%
5. **During flip** — is form disabled? is under-page visible? audio? (audio: no)
6. **After flip** — focus management (return focus to Next/Prev or item field?)
7. **Empty / full page** — copy + CTA
8. **Mobile** — how stack + controls reflow
9. **Reduced motion** — exact fallback (crossfade? instant cut + page number change?)

---

## 8. Acceptance criteria (plan must make these testable)

Grok will treat these as the definition of done:

1. A stranger can tell within **one flip** that a **page is turning** (not fading).
2. **Next** and **Previous** both work and feel directionally correct.
3. User can flip to page 2, seal a new expense, flip back to page 1, and still see original sample rows.
4. Page indicators stay correct (`Page X of Y`, dots, sheet counter).
5. No console errors; no layout jump of the hero column > ~8px after flip settles.
6. `prefers-reduced-motion: reduce` still allows changing pages without 3D.
7. Vercel deploy from `website/` still serves homepage + APK (200).
8. QR Scan still works; CTA roll still works.

---

## 9. Suggested plan depth

- **Diagnosis:** ½ page on why current flip fails visually  
- **Chosen approach:** 1 page with architecture diagram (ASCII OK)  
- **Implementation steps:** numbered, ordered, each step ≤ 30 min of coding  
- **Verification:** checklist matching §8  
- **Optional stretch:** swipe-to-flip, soundless “paper weight” shadow only  

Estimate total implementation for Grok: aim for **plan that fits 1–2 hours of coding**, not a multi-day rewrite.

---

## 10. Files Grok expects to touch (plan may refine)

| Path | Role |
|------|------|
| `website/public/index.html` | Only source of truth for UI/CSS/JS |
| `website/index.html` | Keep in sync with public |
| `website/vercel.json` | Only if routing/headers needed |
| Obsidian `Projects/Needs vs Wants/*` | Grok will update after implement |

Do **not** plan changes under `app/` (Android) or `ios/` unless incidental.

---

## 11. Decisions already locked (cite, don’t reopen)

| ID | Decision |
|----|----------|
| D7 | Supermarket premium light theme |
| D17 | Soft-launch static site + APK |
| D22 | QR CDN `qrcode@1.5.1` only |
| D23 | Animate red CTA panel only; QR fixed; hide QR immediately on close |
| D24 | Notepad + flip buttons direction (implementation still open to redesign) |

---

## 12. Message to Qwen (copy tone)

You are planning for a product owner who has **already rejected multiple flip attempts**. Do not propose “tweak the easing.” Propose a **structurally convincing** notepad page-turn (library or custom) with clear controls and measurable acceptance tests. Prefer one bold correct approach over three weak ones.

When done, output **only the implementation plan** in the required format (§1), ready to paste back to Grok.

---

## 13. Handoff metadata

| Field | Value |
|-------|--------|
| Date | 2026-07-31 |
| Live URL | https://needs-vs-wants.vercel.app |
| Primary implementer after plan | Grok |
| Planner | Qwen 3.8 Max |
| Priority | P0 — hero Log notepad flip quality |
| Related handoff notes | `Projects/Needs vs Wants/Log Sheet Paper Flip.md` |

---

*End of handoff.*
