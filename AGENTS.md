# Needs vs Wants — Agent Instructions

**Project root:** `C:\Needs vs Wants`  
**Second Brain:** `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\`

---

## STRICT: Pre-code gate (mandatory)

**Before touching any implementation code** (editing app sources, `website/**` HTML/CSS/JS, build configs for a feature, etc.), the agent **MUST** complete **all three** steps below. Skipping any step is a protocol violation.

This applies to: features, bug fixes, redesigns, “just a small tweak,” and plan execution after Qwen (or any planner).

**Exempt only:** pure documentation/memory updates the user asked for *without* code changes (e.g. this file, Obsidian notes only). If the task will eventually change code in the same turn, run the gate **before** the first code edit.

### 1. Obsidian project memory

Load Second Brain context first (cheaper than rediscovering the repo):

1. `Memory/00 Memory Layer/Memory Index.md` (vault root: `C:\Obsidian Vault\Second Brain\`)
2. Project notes under `Projects/Needs vs Wants/`:
   - `Summary.md`
   - `Tasks.md`
   - `Decisions.md`
   - Any task-relevant notes (e.g. `Log Sheet Paper Flip.md`, `Soft Launch CTA Motion.md`, `Qwen Handoff Soft Launch Plan.md`)
3. Prefer `[[wikilinks]]` and keep notes updated after significant work

### 2. Context7 (ctx7)

When the task involves a **library, framework, SDK, API, CDN package, or web platform API** (examples: `qrcode`, Vercel, Web Animations API, page-flip libraries, Compose, SwiftUI):

1. Use the **Context7 MCP** (`resolve-library-id` → `query-docs`) for current docs
2. Do **not** rely only on training data for API shapes, CDN paths, or versions
3. If the task is pure product/layout with **no** external library surface, still run Context7 for any API you plan to use (e.g. WAAPI, CSS `transform` edge cases) when unsure—or when the user has mandated Context7 for the session

**For this project’s soft-launch site, always Context7 (or equivalent current docs) before changing:** QR generation, flip/page-turn libraries, Vercel config, or non-trivial browser animation APIs.

### 3. Graphify

Use the **graphify** skill (`graphify` / `graphify-windows`) for architecture orientation before coding:

1. Prefer an existing graph: repo `.graphify/` or vault `graphify-out/` if present  
2. Scoped paths first: `website/`, `app/`, or `Projects/Needs vs Wants/` — not the whole monorepo with `node_modules`  
3. Prefer `--update` / query (`graphify query`, `summary`, `path`, `explain`) over full rebuild when a graph already exists  
4. Use results to avoid breaking linked surfaces (CTA, QR, notepad, deploy)

If no graph exists yet for the relevant scope, run a **scoped** graphify build (e.g. `website/` only) before large website changes—not a full `expo/node_modules` crawl.

### Gate checklist (paste mentally before first code edit)

```
[ ] Obsidian: Summary + Tasks + Decisions (+ task notes) loaded
[ ] Context7: docs pulled for any library/API involved (or N/A documented)
[ ] Graphify: query/summary (or scoped update) run for blast radius
[ ] Only then: edit code
```

---

## Soft-launch website notes

- Canonical HTML: `website/public/index.html` (keep `website/index.html` in sync)
- Deploy **from `website/` only** (never monorepo root)
- Production: https://needs-vs-wants.vercel.app — re-alias if needed after deploy
- Locked decisions: D22 (QR CDN 1.5.1), D23 (CTA red panel only), D32 (notepad flip = native CSS 3D two-face turn, NO `page-flip` library)

Handoff for external planner: `HANDOFF_QWEN_SOFT_LAUNCH_PLAN.md`

---

## After code changes

1. Update `Projects/Needs vs Wants/Tasks.md` progress  
2. Append significant decisions to `Decisions.md`  
3. Update `Summary.md` if scope/structure changed  
