# Node Description Batch 57 of 111

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

- "clang_include_avx512vldqintrin_mm256_broadcast_f64x2": "_mm256_broadcast_f64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1057 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_broadcast_i32x2": "_mm256_broadcast_i32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1105 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_broadcast_i64x2": "_mm256_broadcast_i64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1129 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvtepi64_pd": "_mm256_cvtepi64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L540 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvtepi64_ps": "_mm256_cvtepi64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L582 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvtepu64_pd": "_mm256_cvtepu64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L792 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvtepu64_ps": "_mm256_cvtepu64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L834 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvtpd_epi64": "_mm256_cvtpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L372 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvtpd_epu64": "_mm256_cvtpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L414 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvtps_epi64": "_mm256_cvtps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L456 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvtps_epu64": "_mm256_cvtps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L498 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvttpd_epi64": "_mm256_cvttpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L624 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvttpd_epu64": "_mm256_cvttpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L666 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvttps_epi64": "_mm256_cvttps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L708 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_cvttps_epu64": "_mm256_cvttps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L750 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_and_pd": "_mm256_mask_and_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L146 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_and_ps": "_mm256_mask_and_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L180 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_andnot_pd": "_mm256_mask_andnot_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L78 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_andnot_ps": "_mm256_mask_andnot_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L112 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_broadcast_f32x2": "_mm256_mask_broadcast_f32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1041 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_broadcast_f64x2": "_mm256_mask_broadcast_f64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1065 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_broadcast_i32x2": "_mm256_mask_broadcast_i32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1113 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_broadcast_i64x2": "_mm256_mask_broadcast_i64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1137 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvtepi64_pd": "_mm256_mask_cvtepi64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L547 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvtepi64_ps": "_mm256_mask_cvtepi64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L589 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvtepu64_pd": "_mm256_mask_cvtepu64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L799 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvtepu64_ps": "_mm256_mask_cvtepu64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L841 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvtpd_epi64": "_mm256_mask_cvtpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L379 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvtpd_epu64": "_mm256_mask_cvtpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L421 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvtps_epi64": "_mm256_mask_cvtps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L463 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvtps_epu64": "_mm256_mask_cvtps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L505 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvttpd_epi64": "_mm256_mask_cvttpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L631 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvttpd_epu64": "_mm256_mask_cvttpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L673 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvttps_epi64": "_mm256_mask_cvttps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L715 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_cvttps_epu64": "_mm256_mask_cvttps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L757 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_mullo_epi64": "_mm256_mask_mullo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L39 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_xor_pd": "_mm256_mask_xor_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L214 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mask_xor_ps": "_mm256_mask_xor_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L249 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_and_pd": "_mm256_maskz_and_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L154 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_and_ps": "_mm256_maskz_and_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L188 | neighbors=[avx512vldqintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-056.json

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
