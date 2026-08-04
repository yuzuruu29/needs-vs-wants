# Node Description Batch 78 of 111

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

- "clang_include_avx512vlintrin_mm256_maskz_rolv_epi64": "_mm256_maskz_rolv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5061 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_rorv_epi32": "_mm256_maskz_rorv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5298 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_rorv_epi64": "_mm256_maskz_rorv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5358 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_rsqrt14_pd": "_mm256_maskz_rsqrt14_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7375 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_rsqrt14_ps": "_mm256_maskz_rsqrt14_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7427 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_scalef_pd": "_mm256_maskz_scalef_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3875 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_scalef_ps": "_mm256_maskz_scalef_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3928 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_set1_epi64": "_mm256_maskz_set1_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6001 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sll_epi32": "_mm256_maskz_sll_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5157 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sll_epi64": "_mm256_maskz_sll_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5217 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sllv_epi32": "_mm256_maskz_sllv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5438 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sllv_epi64": "_mm256_maskz_sllv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5398 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sqrt_pd": "_mm256_maskz_sqrt_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4119 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sqrt_ps": "_mm256_maskz_sqrt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4149 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sra_epi32": "_mm256_maskz_sra_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7017 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sra_epi64": "_mm256_maskz_sra_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7097 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_srav_epi32": "_mm256_maskz_srav_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5682 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_srav_epi64": "_mm256_maskz_srav_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5742 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_srl_epi32": "_mm256_maskz_srl_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5562 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_srl_epi64": "_mm256_maskz_srl_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5622 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_srlv_epi32": "_mm256_maskz_srlv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5520 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_srlv_epi64": "_mm256_maskz_srlv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5480 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sub_epi32": "_mm256_maskz_sub_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L668 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sub_epi64": "_mm256_maskz_sub_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L688 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sub_pd": "_mm256_maskz_sub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4183 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_sub_ps": "_mm256_maskz_sub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4217 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_unpackhi_epi32": "_mm256_maskz_unpackhi_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6883 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_unpackhi_epi64": "_mm256_maskz_unpackhi_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6915 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_unpackhi_pd": "_mm256_maskz_unpackhi_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6407 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_unpackhi_ps": "_mm256_maskz_unpackhi_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6439 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_unpacklo_epi32": "_mm256_maskz_unpacklo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6947 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_unpacklo_epi64": "_mm256_maskz_unpacklo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6979 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_unpacklo_pd": "_mm256_maskz_unpacklo_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6471 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_unpacklo_ps": "_mm256_maskz_unpacklo_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6503 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_permutex2var_epi32": "_mm256_permutex2var_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4338 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_permutex2var_epi64": "_mm256_permutex2var_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4523 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_permutex2var_pd": "_mm256_permutex2var_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4400 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_permutex2var_ps": "_mm256_permutex2var_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4462 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_permutexvar_epi32": "_mm256_permutexvar_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8853 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_permutexvar_epi64": "_mm256_permutexvar_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8787 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-077.json

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
