# Needs vs Wants — Agent Instructions

**Project root:** `C:\Needs vs Wants`  
**Second Brain:** `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\`

---

## This machine (verified 2026-08-05)

Agent instructions below are adapted to what is actually available on this PC:

| Item | State on this PC | How to use it here |
|------|------------------|--------------------|
| **OS / shell** | Windows, Git Bash (`bash`) | Use forward-slash paths in shell: `/c/Obsidian Vault/Second Brain/...` (spaces OK quoted); `C:\...` in config/file docs |
| **Obsidian vault** | Plain Markdown at `C:\Obsidian Vault\Second Brain\` | **Read** with file tools (`read_file`/`grep`); **write** via `bash` (unconfined on Windows) with Git-Bash paths — vault is outside the file-tool sandbox root, so `edit_file`/`write_file` cannot touch it. No Obsidian REST API/plugin required — see below for the global-key note |
| **Reasonix** | This workspace runs under the Reasonix agent | Follow the gate below; Reasonix memory/skills are optional extras, they do not replace the vault as the memory layer |
| **Context7 (ctx7)** | MCP server **installed globally** (Streamable HTTP, `https://mcp.context7.com/mcp`) — connects automatically at session start | Use `resolve-library-id` → `query-docs` for current docs; if the server is unavailable in a session, fall back to current official docs and *record the fallback* in the gate checklist |
| **Graphify** | Existing graphs: repo `.graphify/` (2026-08-03, incl. `GRAPH_REPORT.md`) and vault `graphify-out/` (2026-07-28) | Prefer these cached graphs + `graphify` query/summary first; rebuild/update only for a stale or missing scope |
| **Global protocol** | `~/.grok/Agents.md` + `~/.zcode/AGENTS.md` already enforce the Second Brain before/after-every-implementation mandate | This file is the project-specific gate (D25); it **adds** to the global rule, it does not replace it |

---

## STRICT: Pre-code gate (mandatory)

**Before touching any implementation code** (editing app sources, `website/**` HTML/CSS/JS, build configs for a feature, etc.), the agent **MUST** complete **all three** steps below. Skipping any step is a protocol violation.

This applies to: features, bug fixes, redesigns, “just a small tweak,” and plan execution after Qwen (or any planner).

**Exempt only:** pure documentation/memory updates the user asked for *without* code changes (e.g. this file, Obsidian notes only). If the task will eventually change code in the same turn, run the gate **before** the first code edit.

### 1. Obsidian Second Brain

The Second Brain is the project memory layer for this PC. Load it before investigating or changing implementation code (Git Bash paths: `/c/Obsidian Vault/Second Brain/...`):

1. Start with `C:\Obsidian Vault\Second Brain\Memory\00 Memory Layer\Memory Index.md`.
2. Open the matching project dashboard or index when one exists. For this project, use `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\` directly.
3. Always load these project notes:
   - `Summary.md` — current product scope, platform state, constraints, and known gaps
   - `Tasks.md` — active work, progress log, and open blockers
   - `Decisions.md` — locked decisions and historical rationale
4. Load task-specific notes before touching that surface, such as `Agent Pre Code Gate.md`, `Log Sheet Paper Flip.md`, `Soft Launch CTA Motion.md`, and `Qwen Handoff Soft Launch Plan.md`.
5. Follow `[[wikilinks]]` to source notes rather than relying on summaries alone.
6. Treat the repository at `C:\Needs vs Wants` as the ultimate source of truth for what actually exists. Treat vault notes as project context and a historical snapshot; if they disagree, verify the code and report the discrepancy.
7. Do not infer missing behavior from notes. State when a claim is unsupported, stale, or still planned.
8. After significant implementation, deployment, architecture, or verification work, update the relevant vault notes:
   - append the outcome to `Tasks.md`;
   - add a dated entry to `Decisions.md` when a meaningful decision or correction was made;
   - update `Summary.md` when scope, platform status, architecture, or known gaps changed.
9. Preserve existing note structure, frontmatter, decision numbering, and wikilink style. Do not rewrite historical entries.

**On this PC the vault is plain Markdown and lives outside the file-tool sandbox.** Read it with the file tools (`read_file`, `grep`); write vault notes via `bash` using Git-Bash paths (`/c/Obsidian Vault/Second Brain/...`) — on Windows bash is unconfined, while `edit_file`/`write_file` are confined to the workspace and **cannot** write the vault. No Obsidian REST API/plugin is required for this project's notes.

> **Global-key heads-up:** the global `~/.zcode/AGENTS.md` still carries a hardcoded Obsidian Local REST API `Bearer` key and mandates obsidian-skill vault I/O. This project's vault notes are plain files, so the file-tools/bash path above works without that key. If both sets of instructions load, the plain-Markdown path takes precedence **for the Needs vs Wants vault** — do not use an invented or unrelated API key.

**Documentation-only exception:** If the task only changes project documentation or vault memory and does not include code changes in the same turn, the Context7 and Graphify implementation gates are not required. Keep the Second Brain links and status accurate nevertheless.

### Second Brain conflict protocol

When repository state, project notes, and generated artifacts disagree:

1. Verify the relevant source files, tests, build output, or deployed surface.
2. Record the verified state, not the intended state.
3. Identify stale notes or generated files explicitly.
4. Update the vault only after verification; never use the vault to justify an unverified implementation.
5. Never delete or overwrite unfamiliar memory, project, or generated files without checking their purpose first.

### 2. Context7 (ctx7)

When the task involves a **library, framework, SDK, API, CDN package, or web platform API** (examples: `qrcode`, Vercel, Web Animations API, Compose, SwiftUI):

1. Use the **Context7 MCP** (`resolve-library-id` → `query-docs`) for current docs — it is installed globally on this PC and connects automatically at session start.
2. Do **not** rely only on training data for API shapes, CDN paths, or versions.
3. If the server is unavailable in a session, fall back to current official docs directly (official docs sites / latest stable release notes) and record the fallback in the gate checklist (`Context7: N/A — official docs used`). A fallback doc check is still a doc check.
4. If the task is pure product/layout with **no** external library surface, note `N/A` and move on.

**For this project’s soft-launch site, always Context7 (or equivalent current docs) before changing:** QR generation, flip/page-turn libraries, Vercel config, or non-trivial browser animation APIs.

### 3. Graphify

Use the **graphify** skill (`graphify` / `graphify-windows`) for architecture orientation before coding:

1. **Prefer the graphs already on this PC** before rebuilding anything:
   - Repo: `.graphify/` at the project root (graph dated 2026-08-03, incl. `GRAPH_REPORT.md`)
   - Vault: `graphify-out/` under `C:\Obsidian Vault\Second Brain\` (2026-07-28)
2. Scoped paths first: `website/`, `app/`, or `Projects/Needs vs Wants/` — not the whole monorepo with `node_modules`  
3. Prefer query mode (`graphify query`, `summary`, `path`, `explain`) or `--update` over a full rebuild when a graph already exists
4. Use results to avoid breaking linked surfaces (CTA, QR, notepad, deploy)

If no graph exists yet for the relevant scope, run a **scoped** graphify build (e.g. `website/` only) before large website changes—not a full `expo/node_modules` crawl.

### Gate checklist (paste mentally before first code edit)

```
[ ] Obsidian: Memory Index + Summary + Tasks + Decisions (+ task notes) loaded
[ ] Context7: docs pulled for any library/API involved (or N/A / official-docs fallback noted)
[ ] Graphify: existing graph queried (or scoped update) for blast radius
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

1. Update `Projects/Needs vs Wants/Tasks.md` progress (also tick during long sessions, not only at the end)
2. Append significant decisions to `Decisions.md` (dated entry, agent ID, rationale — next free number after the latest `## D##`)
3. Update `Summary.md` if scope/structure changed
4. Record **verified** outcomes (tests/builds/code), not intentions

Global mirror (same mandate, all projects): `C:\Users\uzzie\.grok\Agents.md` and `C:\Users\uzzie\.zcode\AGENTS.md`.
