# Node Description Batch 38 of 111

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

- "clang_include_avx512fintrin_mm512_mask_mul_epi32": "_mm512_mask_mul_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1413 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_mul_epu32": "_mm512_mask_mul_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1441 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_mul_pd": "_mm512_mask_mul_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2266 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_mul_ps": "_mm512_mask_mul_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2285 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_mullo_epi32": "_mm512_mask_mullo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1475 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_permutevar_pd": "_mm512_mask_permutevar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6568 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_permutevar_ps": "_mm512_mask_permutevar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6597 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_permutex2var_epi32": "_mm512_mask_permutex2var_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3390 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_permutex2var_epi64": "_mm512_mask_permutex2var_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3422 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_permutex2var_pd": "_mm512_mask_permutex2var_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6626 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_permutex2var_ps": "_mm512_mask_permutex2var_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6657 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_permutexvar_epi32": "_mm512_mask_permutexvar_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8805 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_permutexvar_epi64": "_mm512_mask_permutexvar_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8750 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_permutexvar_pd": "_mm512_mask_permutexvar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8714 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_permutexvar_ps": "_mm512_mask_permutexvar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8769 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_rcp14_pd": "_mm512_mask_rcp14_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1684 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_rcp14_ps": "_mm512_mask_rcp14_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1710 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_rolv_epi32": "_mm512_mask_rolv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5319 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_rolv_epi64": "_mm512_mask_rolv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5348 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_rorv_epi32": "_mm512_mask_rorv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5191 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_rorv_epi64": "_mm512_mask_rorv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5220 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_rsqrt14_pd": "_mm512_mask_rsqrt14_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1576 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_rsqrt14_ps": "_mm512_mask_rsqrt14_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1602 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_scalef_pd": "_mm512_mask_scalef_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6865 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_scalef_ps": "_mm512_mask_scalef_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6915 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_set1_epi32": "_mm512_mask_set1_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9449 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_set1_epi64": "_mm512_mask_set1_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9456 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_sll_epi32": "_mm512_mask_sll_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5938 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_sll_epi64": "_mm512_mask_sll_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5967 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_sllv_epi32": "_mm512_mask_sllv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5996 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_sllv_epi64": "_mm512_mask_sllv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6025 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_sqrt_pd": "_mm512_mask_sqrt_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1507 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_sqrt_ps": "_mm512_mask_sqrt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1550 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_sra_epi32": "_mm512_mask_sra_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6054 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_sra_epi64": "_mm512_mask_sra_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6083 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_srav_epi32": "_mm512_mask_srav_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6112 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_srav_epi64": "_mm512_mask_srav_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6141 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_srl_epi32": "_mm512_mask_srl_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6170 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_srl_epi64": "_mm512_mask_srl_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6199 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_srlv_epi32": "_mm512_mask_srlv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6228 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-037.json

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
