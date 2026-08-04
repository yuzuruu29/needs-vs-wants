# Node Description Batch 67 of 111

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

- "clang_include_avx512vlintrin_mm_maskz_permutex2var_ps": "_mm_maskz_permutex2var_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4451 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_rcp14_pd": "_mm_maskz_rcp14_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6528 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_rcp14_ps": "_mm_maskz_rcp14_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6580 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_rolv_epi32": "_mm_maskz_rolv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4971 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_rorv_epi32": "_mm_maskz_rorv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5268 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_rsqrt14_pd": "_mm_maskz_rsqrt14_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7349 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_rsqrt14_ps": "_mm_maskz_rsqrt14_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7401 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_scalef_pd": "_mm_maskz_scalef_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3848 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_scalef_ps": "_mm_maskz_scalef_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3901 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_set1_epi64": "_mm_maskz_set1_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5985 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_sll_epi32": "_mm_maskz_sll_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5137 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_sllv_epi32": "_mm_maskz_sllv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5418 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_sra_epi32": "_mm_maskz_sra_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6997 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_srav_epi32": "_mm_maskz_srav_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5662 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_srl_epi32": "_mm_maskz_srl_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5542 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_srlv_epi32": "_mm_maskz_srlv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5500 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_sub_epi32": "_mm_maskz_sub_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L748 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_sub_epi64": "_mm_maskz_sub_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L768 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_unpackhi_epi32": "_mm_maskz_unpackhi_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6867 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_unpackhi_pd": "_mm_maskz_unpackhi_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6391 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_unpackhi_ps": "_mm_maskz_unpackhi_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6423 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_unpacklo_epi32": "_mm_maskz_unpacklo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6931 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_unpacklo_pd": "_mm_maskz_unpacklo_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6455 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_unpacklo_ps": "_mm_maskz_unpacklo_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6487 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_permutex2var_epi32": "_mm_permutex2var_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4308 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_permutex2var_epi64": "_mm_permutex2var_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4492 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_permutex2var_pd": "_mm_permutex2var_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4368 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_permutex2var_ps": "_mm_permutex2var_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4432 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_test_epi32_mask": "_mm_test_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6737 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_test_epi64_mask": "_mm_test_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6767 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_testn_epi32_mask": "_mm_testn_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6797 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_testn_epi64_mask": "_mm_testn_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6827 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_broadcast_f32x4": "_mm256_broadcast_f32x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7436 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_broadcast_i32x4": "_mm256_broadcast_i32x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7460 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpeq_epi32_mask": "_mm256_cmpeq_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L65 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpeq_epi64_mask": "_mm256_cmpeq_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L113 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpeq_epu32_mask": "_mm256_cmpeq_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L77 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpeq_epu64_mask": "_mm256_cmpeq_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L125 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpge_epi32_mask": "_mm256_cmpge_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L162 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpge_epi64_mask": "_mm256_cmpge_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L210 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-066.json

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
