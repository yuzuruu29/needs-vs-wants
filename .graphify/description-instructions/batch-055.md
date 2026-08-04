# Node Description Batch 56 of 111

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

- "clang_include_avx512vlcdintrin_mm256_mask_lzcnt_epi64": "_mm256_mask_lzcnt_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L244 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_maskz_conflict_epi32": "_mm256_maskz_conflict_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L148 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_maskz_conflict_epi64": "_mm256_maskz_conflict_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L100 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_maskz_lzcnt_epi32": "_mm256_maskz_lzcnt_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L200 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_maskz_lzcnt_epi64": "_mm256_maskz_lzcnt_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L252 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vldqintrin_default_fn_attrs": "__DEFAULT_FN_ATTRS()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L56 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_broadcast_i32x2": "_mm_broadcast_i32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1081 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_andnot_pd": "_mm_mask_andnot_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L95 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_andnot_ps": "_mm_mask_andnot_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L129 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_broadcast_i32x2": "_mm_mask_broadcast_i32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1089 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvtepi64_pd": "_mm_mask_cvtepi64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L526 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvtepi64_ps": "_mm_mask_cvtepi64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L568 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvtepu64_pd": "_mm_mask_cvtepu64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L778 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvtepu64_ps": "_mm_mask_cvtepu64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L820 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvtpd_epi64": "_mm_mask_cvtpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L358 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvtpd_epu64": "_mm_mask_cvtpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L400 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvtps_epi64": "_mm_mask_cvtps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L442 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvtps_epu64": "_mm_mask_cvtps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L484 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvttpd_epi64": "_mm_mask_cvttpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L610 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvttpd_epu64": "_mm_mask_cvttpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L652 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvttps_epi64": "_mm_mask_cvttps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L694 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_cvttps_epu64": "_mm_mask_cvttps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L736 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_mask_mullo_epi64": "_mm_mask_mullo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L61 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_andnot_pd": "_mm_maskz_andnot_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L103 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_andnot_ps": "_mm_maskz_andnot_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L137 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_broadcast_i32x2": "_mm_maskz_broadcast_i32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1097 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvtepi64_pd": "_mm_maskz_cvtepi64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L533 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvtepi64_ps": "_mm_maskz_cvtepi64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L575 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvtepu64_pd": "_mm_maskz_cvtepu64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L785 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvtepu64_ps": "_mm_maskz_cvtepu64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L827 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvtpd_epi64": "_mm_maskz_cvtpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L365 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvtpd_epu64": "_mm_maskz_cvtpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L407 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvtps_epi64": "_mm_maskz_cvtps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L449 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvtps_epu64": "_mm_maskz_cvtps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L491 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvttpd_epi64": "_mm_maskz_cvttpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L617 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvttpd_epu64": "_mm_maskz_cvttpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L659 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvttps_epi64": "_mm_maskz_cvttps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L701 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_cvttps_epu64": "_mm_maskz_cvttps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L743 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm_maskz_mullo_epi64": "_mm_maskz_mullo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L69 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_broadcast_f32x2": "_mm256_broadcast_f32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1033 | neighbors=[avx512vldqintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-055.json

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
