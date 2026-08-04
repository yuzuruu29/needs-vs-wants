# Node Description Batch 81 of 111

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

- "clang_include_avxintrin_mm256_movedup_pd": "_mm256_movedup_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2167 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_movehdup_ps": "_mm256_movehdup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2155 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_moveldup_ps": "_mm256_moveldup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2161 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_movemask_pd": "_mm256_movemask_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2290 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_movemask_ps": "_mm256_movemask_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2296 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_mul_pd": "_mm256_mul_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L290 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_mul_ps": "_mm256_mul_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L308 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_or_pd": "_mm256_or_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L606 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_or_ps": "_mm256_or_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L624 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_permutevar_pd": "_mm256_permutevar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L822 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_permutevar_ps": "_mm256_permutevar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L968 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_rcp_ps": "_mm256_rcp_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L376 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_rsqrt_ps": "_mm256_rsqrt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L359 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set_epi16": "_mm256_set_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2556 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set_epi32": "_mm256_set_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2549 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set_epi64x": "_mm256_set_epi64x()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2584 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set_epi8": "_mm256_set_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2566 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set_pd": "_mm256_set_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2536 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set_ps": "_mm256_set_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2542 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set1_epi16": "_mm256_set1_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2663 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set1_epi32": "_mm256_set1_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2657 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set1_epi64x": "_mm256_set1_epi64x()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2678 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set1_epi8": "_mm256_set1_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2670 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set1_pd": "_mm256_set1_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2645 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_set1_ps": "_mm256_set1_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2651 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_setr_epi16": "_mm256_setr_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2611 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_setr_epi32": "_mm256_setr_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2604 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_setr_epi64x": "_mm256_setr_epi64x()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2638 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_setr_epi8": "_mm256_setr_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2621 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_setr_pd": "_mm256_setr_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2591 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_setr_ps": "_mm256_setr_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2597 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_sqrt_pd": "_mm256_sqrt_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L325 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_sqrt_ps": "_mm256_sqrt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L342 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_store_pd": "_mm256_store_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2402 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_store_ps": "_mm256_store_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2408 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_store_si256": "_mm256_store_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2432 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_storeu_pd": "_mm256_storeu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2414 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_storeu_ps": "_mm256_storeu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2423 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_storeu_si256": "_mm256_storeu_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2438 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_stream_pd": "_mm256_stream_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2505 | neighbors=[avxintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-080.json

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
