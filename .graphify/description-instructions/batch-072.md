# Node Description Batch 73 of 111

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

- "clang_include_avx512vlintrin_mm256_mask_permutevar_ps": "_mm256_mask_permutevar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6717 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_permutex2var_epi32": "_mm256_mask_permutex2var_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4347 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_permutex2var_epi64": "_mm256_mask_permutex2var_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4532 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_permutex2var_pd": "_mm256_mask_permutex2var_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4410 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_permutex2var_ps": "_mm256_mask_permutex2var_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4471 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_permutexvar_epi32": "_mm256_mask_permutexvar_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8843 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_permutexvar_epi64": "_mm256_mask_permutexvar_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8796 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_permutexvar_pd": "_mm256_mask_permutexvar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8759 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_permutexvar_ps": "_mm256_mask_permutexvar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8806 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_rcp14_pd": "_mm256_mask_rcp14_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6546 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_rcp14_ps": "_mm256_mask_rcp14_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6598 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_rolv_epi32": "_mm256_mask_rolv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4991 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_rolv_epi64": "_mm256_mask_rolv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5051 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_rorv_epi32": "_mm256_mask_rorv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5288 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_rorv_epi64": "_mm256_mask_rorv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5348 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_rsqrt14_pd": "_mm256_mask_rsqrt14_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7367 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_rsqrt14_ps": "_mm256_mask_rsqrt14_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7419 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_scalef_pd": "_mm256_mask_scalef_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3866 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_scalef_ps": "_mm256_mask_scalef_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3919 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_set1_epi64": "_mm256_mask_set1_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5994 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sll_epi32": "_mm256_mask_sll_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5147 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sll_epi64": "_mm256_mask_sll_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5207 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sllv_epi32": "_mm256_mask_sllv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5428 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sllv_epi64": "_mm256_mask_sllv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5388 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sqrt_pd": "_mm256_mask_sqrt_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4112 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sqrt_ps": "_mm256_mask_sqrt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4142 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sra_epi32": "_mm256_mask_sra_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7007 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sra_epi64": "_mm256_mask_sra_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7087 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_srav_epi32": "_mm256_mask_srav_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5672 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_srav_epi64": "_mm256_mask_srav_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5732 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_srl_epi32": "_mm256_mask_srl_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5552 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_srl_epi64": "_mm256_mask_srl_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5612 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_srlv_epi32": "_mm256_mask_srlv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5510 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_srlv_epi64": "_mm256_mask_srlv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5470 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_store_epi32": "_mm256_mask_store_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5831 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_store_epi64": "_mm256_mask_store_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5917 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_store_pd": "_mm256_mask_store_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6294 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_store_ps": "_mm256_mask_store_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6310 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_storeu_epi32": "_mm256_mask_storeu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6342 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_storeu_epi64": "_mm256_mask_storeu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6326 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-072.json

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
