# Node Description Batch 9 of 111

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

- "clang_include_avx512vlintrin_mm_maskz_rorv_epi64": "_mm_maskz_rorv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5328 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_maskz_sll_epi64": "_mm_maskz_sll_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5197 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_maskz_sllv_epi64": "_mm_maskz_sllv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5378 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_maskz_sra_epi64": "_mm_maskz_sra_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7067 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_maskz_srav_epi64": "_mm_maskz_srav_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5712 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_maskz_srl_epi64": "_mm_maskz_srl_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5602 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_maskz_srlv_epi64": "_mm_maskz_srlv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5460 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_maskz_unpackhi_epi64": "_mm_maskz_unpackhi_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6899 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_maskz_unpacklo_epi64": "_mm_maskz_unpacklo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6963 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_maskz_xor_epi32": "_mm_maskz_xor_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1000 | neighbors=[avx512vlintrin.h, _mm_mask_xor_epi32()]
- "clang_include_avx512vlintrin_mm_maskz_xor_epi64": "_mm_maskz_xor_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1114 | neighbors=[avx512vlintrin.h, _mm_mask_xor_epi64()]
- "clang_include_avx512vlintrin_mm256_mask_and_epi32": "_mm256_mask_and_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L892 | neighbors=[avx512vlintrin.h, _mm256_maskz_and_epi32()]
- "clang_include_avx512vlintrin_mm256_mask_and_epi64": "_mm256_mask_and_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1006 | neighbors=[avx512vlintrin.h, _mm256_maskz_and_epi64()]
- "clang_include_avx512vlintrin_mm256_mask_andnot_epi32": "_mm256_mask_andnot_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L920 | neighbors=[avx512vlintrin.h, _mm256_maskz_andnot_epi32()]
- "clang_include_avx512vlintrin_mm256_mask_andnot_epi64": "_mm256_mask_andnot_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1034 | neighbors=[avx512vlintrin.h, _mm256_maskz_andnot_epi64()]
- "clang_include_avx512vlintrin_mm256_mask_or_epi32": "_mm256_mask_or_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L949 | neighbors=[avx512vlintrin.h, _mm256_maskz_or_epi32()]
- "clang_include_avx512vlintrin_mm256_mask_or_epi64": "_mm256_mask_or_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1063 | neighbors=[avx512vlintrin.h, _mm256_maskz_or_epi64()]
- "clang_include_avx512vlintrin_mm256_mask_xor_epi32": "_mm256_mask_xor_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L977 | neighbors=[avx512vlintrin.h, _mm256_maskz_xor_epi32()]
- "clang_include_avx512vlintrin_mm256_mask_xor_epi64": "_mm256_mask_xor_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1091 | neighbors=[avx512vlintrin.h, _mm256_maskz_xor_epi64()]
- "clang_include_avx512vlintrin_mm256_maskz_and_epi32": "_mm256_maskz_and_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L900 | neighbors=[avx512vlintrin.h, _mm256_mask_and_epi32()]
- "clang_include_avx512vlintrin_mm256_maskz_and_epi64": "_mm256_maskz_and_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1014 | neighbors=[avx512vlintrin.h, _mm256_mask_and_epi64()]
- "clang_include_avx512vlintrin_mm256_maskz_andnot_epi32": "_mm256_maskz_andnot_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L928 | neighbors=[avx512vlintrin.h, _mm256_mask_andnot_epi32()]
- "clang_include_avx512vlintrin_mm256_maskz_andnot_epi64": "_mm256_maskz_andnot_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1042 | neighbors=[avx512vlintrin.h, _mm256_mask_andnot_epi64()]
- "clang_include_avx512vlintrin_mm256_maskz_or_epi32": "_mm256_maskz_or_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L957 | neighbors=[avx512vlintrin.h, _mm256_mask_or_epi32()]
- "clang_include_avx512vlintrin_mm256_maskz_or_epi64": "_mm256_maskz_or_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1071 | neighbors=[avx512vlintrin.h, _mm256_mask_or_epi64()]
- "clang_include_avx512vlintrin_mm256_maskz_xor_epi32": "_mm256_maskz_xor_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L985 | neighbors=[avx512vlintrin.h, _mm256_mask_xor_epi32()]
- "clang_include_avx512vlintrin_mm256_maskz_xor_epi64": "_mm256_maskz_xor_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1099 | neighbors=[avx512vlintrin.h, _mm256_mask_xor_epi64()]
- "clang_include_avxintrin_mm256_castpd128_pd256": "_mm256_castpd128_pd256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2758 | neighbors=[avxintrin.h, _mm256_loadu2_m128d()]
- "clang_include_avxintrin_mm256_castpd256_pd128": "_mm256_castpd256_pd128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2740 | neighbors=[avxintrin.h, _mm256_storeu2_m128d()]
- "clang_include_avxintrin_mm256_castps128_ps256": "_mm256_castps128_ps256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2764 | neighbors=[avxintrin.h, _mm256_loadu2_m128()]
- "clang_include_avxintrin_mm256_castps256_ps128": "_mm256_castps256_ps128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2746 | neighbors=[avxintrin.h, _mm256_storeu2_m128()]
- "clang_include_avxintrin_mm256_castsi128_si256": "_mm256_castsi128_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2770 | neighbors=[avxintrin.h, _mm256_loadu2_m128i()]
- "clang_include_avxintrin_mm256_castsi256_si128": "_mm256_castsi256_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2752 | neighbors=[avxintrin.h, _mm256_storeu2_m128i()]
- "clang_include_avxintrin_mm256_loadu2_m128": "_mm256_loadu2_m128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2841 | neighbors=[avxintrin.h, _mm256_castps128_ps256()]
- "clang_include_avxintrin_mm256_loadu2_m128d": "_mm256_loadu2_m128d()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2848 | neighbors=[avxintrin.h, _mm256_castpd128_pd256()]
- "clang_include_avxintrin_mm256_loadu2_m128i": "_mm256_loadu2_m128i()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2855 | neighbors=[avxintrin.h, _mm256_castsi128_si256()]
- "clang_include_avxintrin_mm256_storeu2_m128": "_mm256_storeu2_m128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2863 | neighbors=[avxintrin.h, _mm256_castps256_ps128()]
- "clang_include_avxintrin_mm256_storeu2_m128d": "_mm256_storeu2_m128d()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2874 | neighbors=[avxintrin.h, _mm256_castpd256_pd128()]
- "clang_include_avxintrin_mm256_storeu2_m128i": "_mm256_storeu2_m128i()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2885 | neighbors=[avxintrin.h, _mm256_castsi256_si128()]
- "clang_include_clang_cuda_cmath_abs": "abs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L47 | neighbors=[__clang_cuda_cmath.h, fabs()]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-008.json

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
