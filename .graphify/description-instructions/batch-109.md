# Node Description Batch 110 of 111

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

- "clang_include_xopintrin_mm_maddsd_epi16": "_mm_maddsd_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L96 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_perm_epi8": "_mm_perm_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L210 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_rot_epi16": "_mm_rot_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L222 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_rot_epi32": "_mm_rot_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L228 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_rot_epi64": "_mm_rot_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L234 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_rot_epi8": "_mm_rot_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L216 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_sha_epi16": "_mm_sha_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L282 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_sha_epi32": "_mm_sha_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L288 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_sha_epi64": "_mm_sha_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L294 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_sha_epi8": "_mm_sha_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L276 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_shl_epi16": "_mm_shl_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L258 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_shl_epi32": "_mm_shl_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L264 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_shl_epi64": "_mm_shl_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L270 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm_shl_epi8": "_mm_shl_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L252 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm256_cmov_si256": "_mm256_cmov_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L204 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm256_frcz_pd": "_mm256_frcz_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L774 | neighbors=[xopintrin.h]
- "clang_include_xopintrin_mm256_frcz_ps": "_mm256_frcz_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L768 | neighbors=[xopintrin.h]
- "clang_include_xsavecintrin_xsavec": "_xsavec()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsavecintrin.h:L34 | neighbors=[xsavecintrin.h]
- "clang_include_xsavecintrin_xsavec64": "_xsavec64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsavecintrin.h:L40 | neighbors=[xsavecintrin.h]
- "clang_include_xsaveintrin_xrstor": "_xrstor()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsaveintrin.h:L39 | neighbors=[xsaveintrin.h]
- "clang_include_xsaveintrin_xrstor64": "_xrstor64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsaveintrin.h:L50 | neighbors=[xsaveintrin.h]
- "clang_include_xsaveintrin_xsave": "_xsave()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsaveintrin.h:L34 | neighbors=[xsaveintrin.h]
- "clang_include_xsaveintrin_xsave64": "_xsave64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsaveintrin.h:L45 | neighbors=[xsaveintrin.h]
- "clang_include_xsaveoptintrin_xsaveopt": "_xsaveopt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsaveoptintrin.h:L34 | neighbors=[xsaveoptintrin.h]
- "clang_include_xsaveoptintrin_xsaveopt64": "_xsaveopt64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsaveoptintrin.h:L40 | neighbors=[xsaveoptintrin.h]
- "clang_include_xsavesintrin_xrstors": "_xrstors()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsavesintrin.h:L39 | neighbors=[xsavesintrin.h]
- "clang_include_xsavesintrin_xrstors64": "_xrstors64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsavesintrin.h:L45 | neighbors=[xsavesintrin.h]
- "clang_include_xsavesintrin_xsaves": "_xsaves()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsavesintrin.h:L34 | neighbors=[xsavesintrin.h]
- "clang_include_xsavesintrin_xsaves64": "_xsaves64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsavesintrin.h:L50 | neighbors=[xsavesintrin.h]
- "clang_include_xtestintrin_xtest": "_xtest()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xtestintrin.h:L35 | neighbors=[xtestintrin.h]
- "pad_parts_apply_css": "css" | kind=code-symbol | source=website/_pad-parts/apply.js:L8 | neighbors=[apply.js]
- "pad_parts_apply_file": "file" | kind=code-symbol | source=website/_pad-parts/apply.js:L4 | neighbors=[apply.js]
- "pad_parts_apply_fs": "fs" | kind=code-symbol | source=website/_pad-parts/apply.js:L1 | neighbors=[apply.js]
- "pad_parts_apply_html": "html" | kind=code-symbol | source=website/_pad-parts/apply.js:L7 | neighbors=[apply.js]
- "pad_parts_apply_padhtml": "padHtml" | kind=code-symbol | source=website/_pad-parts/apply.js:L9 | neighbors=[apply.js]
- "pad_parts_apply_padjs": "padJs" | kind=code-symbol | source=website/_pad-parts/apply.js:L10 | neighbors=[apply.js]
- "pad_parts_apply_path": "path" | kind=code-symbol | source=website/_pad-parts/apply.js:L2 | neighbors=[apply.js]
- "pad_parts_apply_root": "root" | kind=code-symbol | source=website/_pad-parts/apply.js:L3 | neighbors=[apply.js]
- "pad_parts_check_code": "code" | kind=code-symbol | source=website/_pad-parts/check.js:L6 | neighbors=[check.js]
- "pad_parts_check_fs": "fs" | kind=code-symbol | source=website/_pad-parts/check.js:L1 | neighbors=[check.js]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-109.json

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
