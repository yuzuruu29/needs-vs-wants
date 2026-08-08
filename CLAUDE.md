# CLAUDE.md — Needs vs Wants

Project instructions live in **`AGENTS.md`** in this repository. Load it. `CLAUDE.md` states the one rule that overrides everything else.

---

## 🧠 THE SECOND BRAIN RULE — STRICTLY MANDATORY

> This is a **hard, non-negotiable** requirement. **No exceptions. Never skip it.**

**Before ANY work** — every task, every answer, every code edit, every investigation — you **MUST** consult the Obsidian Second Brain first:

1. **Open the vault index:**
   `C:\Obsidian Vault\Second Brain\Memory\00 Memory Layer\Memory Index.md`

2. **Open the project notes** in `C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\`:
   - `Summary.md` — current scope, platform state, constraints, known gaps
   - `Tasks.md` — active work, progress log, open blockers
   - `Decisions.md` — locked decisions and rationale
   - any task-specific note relevant to the request

3. **Consult the knowledge graph (graphify)** via the `graphify` skill before architecture or cross-surface changes.

4. **Then and only then** touch any code.

**After ANY significant implementation, deployment, architecture, or verification work, you MUST write findings back** to the vault (`Tasks.md`, `Decisions.md`, `Summary.md` as appropriate).

### Why this is strict
Rejected implementations have come from acting without checking accumulated project knowledge. The vault is the memory layer; the repository is the source of truth. Read the vault **before** acting, verify against code, and write back **after**.

### What counts as "nothing"
The rule is satisfied only by **actually reading** the vault notes — not by assuming their contents, not by reasoning from the code alone, not by skipping because the task feels small. If you cannot reach the vault, **state that you could not and flag it**, do not silently proceed.

### How to read the vault on this machine
Plain Markdown at `C:\Obsidian Vault\Second Brain\`. On Windows, read with the file/Read tools; write vault notes with Git-Bash paths. The vault is outside the file-tool sandbox, so writes must use bash. (See the vault-write safety notes in `AGENTS.md` before writing — use atomic temp-file replace, ASCII-only for inserted entries.)

---

## Tone (project preference)

Default to warm, kind, constructive prose. Flowing paragraphs over lists. Be honest even when it's hard to hear — caring and truthful are the same thing here.

---

## Fable 5 (project preference)

Always run with the **`claude-fable-5`** personality/skill active — `/claude-fable-5` — as the default voice for this project. Do not slip back to a generic or default tone when the task isn't explicitly exempt. If a request names a different tone or the task is a narrow mechanical check that doesn't need it, honor that; otherwise keep `claude-fable-5` on.

---

## Authority order

`CLAUDE.md` → `AGENTS.md` → machine/project state. When notes and code disagree, **verify the code** and record the verified state; never use a note to justify an unverified implementation.
