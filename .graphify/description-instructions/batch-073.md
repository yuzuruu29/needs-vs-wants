# Node Description Batch 74 of 111

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

- "clang_include_avx512vlintrin_mm256_mask_storeu_pd": "_mm256_mask_storeu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6358 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_storeu_ps": "_mm256_mask_storeu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6374 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sub_epi32": "_mm256_mask_sub_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L658 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sub_epi64": "_mm256_mask_sub_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L678 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sub_pd": "_mm256_mask_sub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4174 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_sub_ps": "_mm256_mask_sub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4209 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_test_epi32_mask": "_mm256_mask_test_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6760 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_test_epi64_mask": "_mm256_mask_test_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6790 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_testn_epi32_mask": "_mm256_mask_testn_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6820 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_testn_epi64_mask": "_mm256_mask_testn_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6850 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_unpackhi_epi32": "_mm256_mask_unpackhi_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6875 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_unpackhi_epi64": "_mm256_mask_unpackhi_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6907 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_unpackhi_pd": "_mm256_mask_unpackhi_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6399 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_unpackhi_ps": "_mm256_mask_unpackhi_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6431 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_unpacklo_epi32": "_mm256_mask_unpacklo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6939 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_unpacklo_epi64": "_mm256_mask_unpacklo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6971 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_unpacklo_pd": "_mm256_mask_unpacklo_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6463 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_unpacklo_ps": "_mm256_mask_unpacklo_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6495 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask2_permutex2var_epi32": "_mm256_mask2_permutex2var_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4236 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask2_permutex2var_epi64": "_mm256_mask2_permutex2var_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4298 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask2_permutex2var_pd": "_mm256_mask2_permutex2var_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4257 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask2_permutex2var_ps": "_mm256_mask2_permutex2var_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4278 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fmadd_pd": "_mm256_mask3_fmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1321 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fmadd_ps": "_mm256_mask3_fmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1465 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fmaddsub_pd": "_mm256_mask3_fmaddsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1585 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fmaddsub_ps": "_mm256_mask3_fmaddsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1679 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fmsub_pd": "_mm256_mask3_fmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1724 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fmsub_ps": "_mm256_mask3_fmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1742 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fmsubadd_pd": "_mm256_mask3_fmsubadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1761 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fmsubadd_ps": "_mm256_mask3_fmsubadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1780 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fnmadd_pd": "_mm256_mask3_fnmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1357 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fnmadd_ps": "_mm256_mask3_fnmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1501 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fnmsub_pd": "_mm256_mask3_fnmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1852 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask3_fnmsub_ps": "_mm256_mask3_fnmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1888 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_abs_epi32": "_mm256_maskz_abs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3358 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_abs_epi64": "_mm256_maskz_abs_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3404 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_add_epi32": "_mm256_maskz_add_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L628 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_add_epi64": "_mm256_maskz_add_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L648 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_add_pd": "_mm256_maskz_add_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1922 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_add_ps": "_mm256_maskz_add_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1956 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-073.json

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
