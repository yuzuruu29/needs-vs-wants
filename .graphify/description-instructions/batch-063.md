# Node Description Batch 64 of 111

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

- "clang_include_avx512vlintrin_mm_mask_unpackhi_ps": "_mm_mask_unpackhi_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6415 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_unpacklo_epi32": "_mm_mask_unpacklo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6923 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_unpacklo_epi64": "_mm_mask_unpacklo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6955 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_unpacklo_pd": "_mm_mask_unpacklo_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6447 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_unpacklo_ps": "_mm_mask_unpacklo_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6479 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask2_permutex2var_epi32": "_mm_mask2_permutex2var_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4226 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask2_permutex2var_epi64": "_mm_mask2_permutex2var_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4288 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask2_permutex2var_pd": "_mm_mask2_permutex2var_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4246 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask2_permutex2var_ps": "_mm_mask2_permutex2var_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4268 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fmadd_pd": "_mm_mask3_fmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1249 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fmadd_ps": "_mm_mask3_fmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1393 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fmaddsub_pd": "_mm_mask3_fmaddsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1537 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fmaddsub_ps": "_mm_mask3_fmaddsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1633 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fmsub_pd": "_mm_mask3_fmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1715 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fmsub_ps": "_mm_mask3_fmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1733 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fmsubadd_pd": "_mm_mask3_fmsubadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1751 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fmsubadd_ps": "_mm_mask3_fmsubadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1771 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fnmadd_pd": "_mm_mask3_fnmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1285 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fnmadd_ps": "_mm_mask3_fnmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1429 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fnmsub_pd": "_mm_mask3_fnmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1834 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask3_fnmsub_ps": "_mm_mask3_fnmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1870 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_abs_epi32": "_mm_maskz_abs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3343 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_abs_epi64": "_mm_maskz_abs_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3381 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_add_epi32": "_mm_maskz_add_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L708 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_add_epi64": "_mm_maskz_add_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L728 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_broadcastd_epi32": "_mm_maskz_broadcastd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7541 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_broadcastq_epi64": "_mm_maskz_broadcastq_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7573 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_broadcastss_ps": "_mm_maskz_broadcastss_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7509 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_compress_epi32": "_mm_maskz_compress_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2118 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_compress_epi64": "_mm_maskz_compress_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2058 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_compress_pd": "_mm_maskz_compress_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2028 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_compress_ps": "_mm_maskz_compress_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2088 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepi16_epi32": "_mm_maskz_cvtepi16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4663 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepi16_epi64": "_mm_maskz_cvtepi16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4697 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepi32_epi16": "_mm_maskz_cvtepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8257 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepi32_epi64": "_mm_maskz_cvtepi32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4629 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepi32_epi8": "_mm_maskz_cvtepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8198 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepi32_pd": "_mm_maskz_cvtepi32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2204 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepi32_ps": "_mm_maskz_cvtepi32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2234 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepi64_epi16": "_mm_maskz_cvtepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8432 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-063.json

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
