# Node Description Batch 72 of 111

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

- "clang_include_avx512vlintrin_mm256_mask_fmsubadd_ps": "_mm256_mask_fmsubadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1697 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_fnmadd_pd": "_mm256_mask_fnmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1798 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_fnmadd_ps": "_mm256_mask_fnmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1816 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_fnmsub_pd": "_mm256_mask_fnmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1843 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_fnmsub_ps": "_mm256_mask_fnmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1879 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_getexp_pd": "_mm256_mask_getexp_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3068 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_getexp_ps": "_mm256_mask_getexp_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3114 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_load_epi32": "_mm256_mask_load_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5804 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_load_epi64": "_mm256_mask_load_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5890 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_load_pd": "_mm256_mask_load_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6099 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_load_ps": "_mm256_mask_load_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6133 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_loadu_epi32": "_mm256_mask_loadu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6201 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_loadu_epi64": "_mm256_mask_loadu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6167 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_loadu_pd": "_mm256_mask_loadu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6235 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_loadu_ps": "_mm256_mask_loadu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6269 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_max_epi32": "_mm256_mask_max_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3438 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_max_epi64": "_mm256_mask_max_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3481 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_max_epu32": "_mm256_mask_max_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3524 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_max_epu64": "_mm256_mask_max_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3576 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_max_pd": "_mm256_mask_max_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3146 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_max_ps": "_mm256_mask_max_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3181 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_min_epi32": "_mm256_mask_min_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3610 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_min_epi64": "_mm256_mask_min_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3653 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_min_epu32": "_mm256_mask_min_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3696 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_min_epu64": "_mm256_mask_min_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3739 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_min_pd": "_mm256_mask_min_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3215 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_min_ps": "_mm256_mask_min_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3250 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_mov_epi32": "_mm256_mask_mov_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5769 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_mov_epi64": "_mm256_mask_mov_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5855 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_mov_pd": "_mm256_mask_mov_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L9034 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_mov_ps": "_mm256_mask_mov_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L9066 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_movedup_pd": "_mm256_mask_movedup_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5941 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_movehdup_ps": "_mm256_mask_movehdup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8950 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_moveldup_ps": "_mm256_mask_moveldup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8982 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_mul_epi32": "_mm256_mask_mul_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L778 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_mul_epu32": "_mm256_mask_mul_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L816 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_mul_pd": "_mm256_mask_mul_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3284 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_mul_ps": "_mm256_mask_mul_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3319 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_mullo_epi32": "_mm256_mask_mullo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L864 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_permutevar_pd": "_mm256_mask_permutevar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6675 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-071.json

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
