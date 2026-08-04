# Node Description Batch 111 of 111

Graphify is running in assistant/skill mode (no API key). You are the host
assistant (Claude Code / Codex / Gemini CLI). Read the prompt below and write
your JSON answer to the answer file.

## Prompt

You are documenting nodes in a knowledge graph.
For each entry below, write ONE concise factual plain-language sentence
describing what it is or does. Use only the provided context.
For a code symbol (kind=code-symbol — a function, class, or constant),
describe what the function/symbol does based on its name, source location
and neighbors — e.g. "Resolves the configured ontology profile from graphify.yaml.".
Write every description in English (en). Do not switch languages.
No marketing language.
Respond ONLY with a JSON object mapping each node id (as a string) to its
one-sentence description — no prose, no markdown fences.

- "pad_parts_check_html": "html" | kind=code-symbol | source=website/_pad-parts/check.js:L3 | neighbors=[check.js]
- "pad_parts_check_i": "i" | kind=code-symbol | source=website/_pad-parts/check.js:L4 | neighbors=[check.js]
- "pad_parts_check_j": "j" | kind=code-symbol | source=website/_pad-parts/check.js:L5 | neighbors=[check.js]
- "pad_parts_check_path": "path" | kind=code-symbol | source=website/_pad-parts/check.js:L2 | neighbors=[check.js]
- "pad_parts_debug_mime": "mime" | kind=code-symbol | source=website/_pad-parts/debug.mjs:L8 | neighbors=[debug.mjs]
- "pad_parts_debug_root": "root" | kind=code-symbol | source=website/_pad-parts/debug.mjs:L7 | neighbors=[debug.mjs]
- "pad_parts_debug_server": "server" | kind=code-symbol | source=website/_pad-parts/debug.mjs:L9 | neighbors=[debug.mjs]
- "pad_parts_pad_adapter": "adapter" | kind=code-symbol | source=website/_pad-parts/pad.js:L20 | neighbors=[pad.js]
- "pad_parts_pad_flipleaves": "flipLeaves" | kind=code-symbol | source=website/_pad-parts/pad.js:L37 | neighbors=[pad.js]
- "pad_parts_pad_focusdemo": "focusDemo()" | kind=code-symbol | source=website/_pad-parts/pad.js:L660 | neighbors=[pad.js]
- "pad_parts_pad_formdraft": "formDraft" | kind=code-symbol | source=website/_pad-parts/pad.js:L41 | neighbors=[pad.js]
- "pad_parts_pad_notepad": "notepad" | kind=code-symbol | source=website/_pad-parts/pad.js:L9 | neighbors=[pad.js]
- "pad_parts_pad_padstage": "padStage" | kind=code-symbol | source=website/_pad-parts/pad.js:L10 | neighbors=[pad.js]
- "pad_parts_pad_pagedots": "pageDots" | kind=code-symbol | source=website/_pad-parts/pad.js:L11 | neighbors=[pad.js]
- "pad_parts_pad_pagenext": "pageNext" | kind=code-symbol | source=website/_pad-parts/pad.js:L13 | neighbors=[pad.js]
- "pad_parts_pad_pagenexttab": "pageNextTab" | kind=code-symbol | source=website/_pad-parts/pad.js:L15 | neighbors=[pad.js]
- "pad_parts_pad_pageprev": "pagePrev" | kind=code-symbol | source=website/_pad-parts/pad.js:L12 | neighbors=[pad.js]
- "pad_parts_pad_pageprevtab": "pagePrevTab" | kind=code-symbol | source=website/_pad-parts/pad.js:L14 | neighbors=[pad.js]
- "pad_parts_pad_pages": "pages" | kind=code-symbol | source=website/_pad-parts/pad.js:L25 | neighbors=[pad.js]
- "pad_parts_smoke_errs": "errs" | kind=code-symbol | source=website/_pad-parts/smoke.mjs:L150 | neighbors=[smoke.mjs]
- "pad_parts_smoke_logs": "logs" | kind=code-symbol | source=website/_pad-parts/smoke.mjs:L30 | neighbors=[smoke.mjs]
- "pad_parts_smoke_mime": "mime" | kind=code-symbol | source=website/_pad-parts/smoke.mjs:L8 | neighbors=[smoke.mjs]
- "pad_parts_smoke_root": "root" | kind=code-symbol | source=website/_pad-parts/smoke.mjs:L7 | neighbors=[smoke.mjs]
- "pad_parts_smoke_server": "server" | kind=code-symbol | source=website/_pad-parts/smoke.mjs:L10 | neighbors=[smoke.mjs]
- "pad_parts_smoke2_logs": "logs" | kind=code-symbol | source=website/_pad-parts/smoke2.mjs:L22 | neighbors=[smoke2.mjs]
- "pad_parts_smoke2_mime": "mime" | kind=code-symbol | source=website/_pad-parts/smoke2.mjs:L8 | neighbors=[smoke2.mjs]
- "pad_parts_smoke2_root": "root" | kind=code-symbol | source=website/_pad-parts/smoke2.mjs:L7 | neighbors=[smoke2.mjs]
- "pad_parts_smoke2_server": "server" | kind=code-symbol | source=website/_pad-parts/smoke2.mjs:L9 | neighbors=[smoke2.mjs]
- "pad_parts_smoke3_mime": "mime" | kind=code-symbol | source=website/_pad-parts/smoke3.mjs:L8 | neighbors=[smoke3.mjs]
- "pad_parts_smoke3_root": "root" | kind=code-symbol | source=website/_pad-parts/smoke3.mjs:L7 | neighbors=[smoke3.mjs]
- "pad_parts_smoke3_server": "server" | kind=code-symbol | source=website/_pad-parts/smoke3.mjs:L9 | neighbors=[smoke3.mjs]
- "website_probe2_current_main": "main()" | kind=code-symbol | source=website/probe2_current.py:L10 | neighbors=[probe2_current.py]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-110.json

Keep each description factual and concise (one sentence). No markdown, no prose
outside the JSON object. It is acceptable to omit a node if context is
insufficient — but include every node you can ground confidently.

Example answer format:
```json
{
  "node_id_1": "Resolves the configured ontology profile from graphify.yaml.",
  "node_id_2": "Colonel James Barclay, an antagonist in The Crooked Man."
}
```
