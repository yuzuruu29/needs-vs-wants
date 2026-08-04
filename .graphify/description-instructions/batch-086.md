# Node Description Batch 87 of 111

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

- "clang_include_emmintrin_mm_max_pd": "_mm_max_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L135 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_max_sd": "_mm_max_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L129 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_min_epi16": "_mm_min_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L736 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_min_epu8": "_mm_min_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L742 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_min_pd": "_mm_min_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L123 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_min_sd": "_mm_min_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L117 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_move_epi64": "_mm_move_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2383 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_move_sd": "_mm_move_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L584 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_movemask_epi8": "_mm_movemask_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2295 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_movemask_pd": "_mm_movemask_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2401 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_movepi64_pi64": "_mm_movepi64_pi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2371 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_movpi64_epi64": "_mm_movpi64_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2377 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_mul_epu32": "_mm_mul_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L811 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_mul_pd": "_mm_mul_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L85 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_mul_sd": "_mm_mul_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L78 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_mul_su32": "_mm_mul_su32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L792 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_mulhi_epi16": "_mm_mulhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L748 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_mulhi_epu16": "_mm_mulhi_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L754 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_mullo_epi16": "_mm_mullo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L773 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_or_pd": "_mm_or_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L153 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_or_si128": "_mm_or_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1061 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_packs_epi16": "_mm_packs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2262 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_packs_epi32": "_mm_packs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2268 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_packus_epi16": "_mm_packus_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2274 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_sad_epu8": "_mm_sad_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L833 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set_epi16": "_mm_set_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2009 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set_epi32": "_mm_set_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1969 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set_epi64": "_mm_set_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1941 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set_epi64x": "_mm_set_epi64x()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1919 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set_epi8": "_mm_set_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2057 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set_pd": "_mm_set_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L566 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set_sd": "_mm_set_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L554 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set1_epi16": "_mm_set1_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2133 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set1_epi32": "_mm_set1_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2114 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set1_epi64": "_mm_set1_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2095 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set1_epi64x": "_mm_set1_epi64x()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2076 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set1_epi8": "_mm_set1_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2152 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_set1_pd": "_mm_set1_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L560 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_setr_epi16": "_mm_setr_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2170 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_setr_epi32": "_mm_setr_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2164 | neighbors=[emmintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-086.json

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
