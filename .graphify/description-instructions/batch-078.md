# Node Description Batch 79 of 111

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

- "clang_include_avx512vlintrin_mm256_permutexvar_pd": "_mm256_permutexvar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8750 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_permutexvar_ps": "_mm256_permutexvar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8825 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_test_epi32_mask": "_mm256_test_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6752 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_test_epi64_mask": "_mm256_test_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6782 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_testn_epi32_mask": "_mm256_testn_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6812 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_testn_epi64_mask": "_mm256_testn_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6842 | neighbors=[avx512vlintrin.h]
- "clang_include_avxintrin_default_fn_attrs": "__DEFAULT_FN_ATTRS()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2896 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_broadcast_ss": "_mm_broadcast_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2316 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_maskload_pd": "_mm_maskload_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2448 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_maskload_ps": "_mm_maskload_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2461 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_maskstore_pd": "_mm_maskstore_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2480 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_maskstore_ps": "_mm_maskstore_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2492 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_permutevar_pd": "_mm_permutevar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L783 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_permutevar_ps": "_mm_permutevar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L877 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_testc_pd": "_mm_testc_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2205 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_testc_ps": "_mm_testc_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2223 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_testnzc_pd": "_mm_testnzc_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2211 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_testnzc_ps": "_mm_testnzc_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2229 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_testz_pd": "_mm_testz_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2199 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm_testz_ps": "_mm_testz_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2217 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_add_pd": "_mm256_add_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L68 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_add_ps": "_mm256_add_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L86 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_addsub_pd": "_mm256_addsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L141 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_addsub_ps": "_mm256_addsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L160 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_and_pd": "_mm256_and_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L528 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_and_ps": "_mm256_and_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L546 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_andnot_pd": "_mm256_andnot_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L567 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_andnot_ps": "_mm256_andnot_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L588 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_blendv_pd": "_mm256_blendv_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L1419 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_blendv_ps": "_mm256_blendv_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L1447 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_broadcast_pd": "_mm256_broadcast_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2337 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_broadcast_ps": "_mm256_broadcast_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2343 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_broadcast_sd": "_mm256_broadcast_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2323 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_broadcast_ss": "_mm256_broadcast_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2330 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_castpd_ps": "_mm256_castpd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2704 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_castpd_si256": "_mm256_castpd_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2710 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_castps_pd": "_mm256_castps_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2716 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_castps_si256": "_mm256_castps_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2722 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_castsi256_pd": "_mm256_castsi256_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2734 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_castsi256_ps": "_mm256_castsi256_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2728 | neighbors=[avxintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-078.json

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
