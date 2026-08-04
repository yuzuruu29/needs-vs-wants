# Node Description Batch 58 of 111

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

- "clang_include_avx512vldqintrin_mm256_maskz_andnot_pd": "_mm256_maskz_andnot_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L86 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_andnot_ps": "_mm256_maskz_andnot_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L120 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_broadcast_f32x2": "_mm256_maskz_broadcast_f32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1049 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_broadcast_f64x2": "_mm256_maskz_broadcast_f64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1073 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_broadcast_i32x2": "_mm256_maskz_broadcast_i32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1121 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_broadcast_i64x2": "_mm256_maskz_broadcast_i64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1145 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvtepi64_pd": "_mm256_maskz_cvtepi64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L554 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvtepi64_ps": "_mm256_maskz_cvtepi64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L596 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvtepu64_pd": "_mm256_maskz_cvtepu64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L806 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvtepu64_ps": "_mm256_maskz_cvtepu64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L848 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvtpd_epi64": "_mm256_maskz_cvtpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L386 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvtpd_epu64": "_mm256_maskz_cvtpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L428 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvtps_epi64": "_mm256_maskz_cvtps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L470 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvtps_epu64": "_mm256_maskz_cvtps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L512 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvttpd_epi64": "_mm256_maskz_cvttpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L638 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvttpd_epu64": "_mm256_maskz_cvttpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L680 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvttps_epi64": "_mm256_maskz_cvttps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L722 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_cvttps_epu64": "_mm256_maskz_cvttps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L764 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_mullo_epi64": "_mm256_maskz_mullo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L47 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_or_pd": "_mm256_maskz_or_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L291 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_or_ps": "_mm256_maskz_or_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L325 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_xor_pd": "_mm256_maskz_xor_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L223 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_maskz_xor_ps": "_mm256_maskz_xor_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L257 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_movepi32_mask": "_mm256_movepi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L991 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_movepi64_mask": "_mm256_movepi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1027 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vldqintrin_mm256_mullo_epi64": "_mm256_mullo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L34 | neighbors=[avx512vldqintrin.h]
- "clang_include_avx512vlintrin_mm_cmpeq_epi32_mask": "_mm_cmpeq_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L41 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpeq_epi64_mask": "_mm_cmpeq_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L89 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpeq_epu32_mask": "_mm_cmpeq_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L53 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpeq_epu64_mask": "_mm_cmpeq_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L101 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpge_epi32_mask": "_mm_cmpge_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L138 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpge_epi64_mask": "_mm_cmpge_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L186 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpge_epu32_mask": "_mm_cmpge_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L150 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpge_epu64_mask": "_mm_cmpge_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L198 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpgt_epi32_mask": "_mm_cmpgt_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L234 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpgt_epi64_mask": "_mm_cmpgt_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L282 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpgt_epu32_mask": "_mm_cmpgt_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L246 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpgt_epu64_mask": "_mm_cmpgt_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L294 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmple_epi32_mask": "_mm_cmple_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L330 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmple_epi64_mask": "_mm_cmple_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L378 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-057.json

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
