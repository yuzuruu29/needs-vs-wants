# Node Description Batch 13 of 111

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

- "clang_include_altivec_builtin_crypto_vpmsumb": "__builtin_crypto_vpmsumb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14402 | neighbors=[altivec.h]
- "clang_include_altivec_builtin_crypto_vsbox": "__builtin_crypto_vsbox()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14333 | neighbors=[altivec.h]
- "clang_include_altivec_vec_abs": "vec_abs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L105 | neighbors=[altivec.h]
- "clang_include_altivec_vec_abss": "vec_abss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L148 | neighbors=[altivec.h]
- "clang_include_altivec_vec_add": "vec_add()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L168 | neighbors=[altivec.h]
- "clang_include_altivec_vec_addc": "vec_addc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L437 | neighbors=[altivec.h]
- "clang_include_altivec_vec_adde": "vec_adde()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L295 | neighbors=[altivec.h]
- "clang_include_altivec_vec_addec": "vec_addec()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L311 | neighbors=[altivec.h]
- "clang_include_altivec_vec_adds": "vec_adds()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L470 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_eq": "vec_all_eq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L12033 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_ge": "vec_all_ge()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L12220 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_gt": "vec_all_gt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L12391 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_in": "vec_all_in()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L12562 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_le": "vec_all_le()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L12569 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_lt": "vec_all_lt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L12741 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_nan": "vec_all_nan()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L12913 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_ne": "vec_all_ne()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L12929 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_nge": "vec_all_nge()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L13117 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_ngt": "vec_all_ngt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L13135 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_nle": "vec_all_nle()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L13153 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_nlt": "vec_all_nlt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L13160 | neighbors=[altivec.h]
- "clang_include_altivec_vec_all_numeric": "vec_all_numeric()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L13167 | neighbors=[altivec.h]
- "clang_include_altivec_vec_and": "vec_and()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L720 | neighbors=[altivec.h]
- "clang_include_altivec_vec_andc": "vec_andc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1073 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_eq": "vec_any_eq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L13174 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_ge": "vec_any_ge()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L13362 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_gt": "vec_any_gt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L13541 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_le": "vec_any_le()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L13720 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_lt": "vec_any_lt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L13899 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_nan": "vec_any_nan()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14078 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_ne": "vec_any_ne()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14085 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_nge": "vec_any_nge()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14273 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_ngt": "vec_any_ngt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14280 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_nle": "vec_any_nle()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14287 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_nlt": "vec_any_nlt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14294 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_numeric": "vec_any_numeric()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14301 | neighbors=[altivec.h]
- "clang_include_altivec_vec_any_out": "vec_any_out()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14308 | neighbors=[altivec.h]
- "clang_include_altivec_vec_avg": "vec_avg()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1424 | neighbors=[altivec.h]
- "clang_include_altivec_vec_bperm": "vec_bperm()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14448 | neighbors=[altivec.h]
- "clang_include_altivec_vec_ceil": "vec_ceil()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1498 | neighbors=[altivec.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-012.json

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
