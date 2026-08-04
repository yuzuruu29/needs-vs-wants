# Node Description Batch 28 of 111

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

- "clang_include_avx512dqintrin_mm512_cvttpd_epu64": "_mm512_cvttpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L515 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_cvttps_epi64": "_mm512_cvttps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L554 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_cvttps_epu64": "_mm512_cvttps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L593 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_and_pd": "_mm512_mask_and_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L149 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_and_ps": "_mm512_mask_and_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L171 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_andnot_pd": "_mm512_mask_andnot_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L197 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_andnot_ps": "_mm512_mask_andnot_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L223 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_broadcast_f32x2": "_mm512_mask_broadcast_f32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1016 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_broadcast_f32x8": "_mm512_mask_broadcast_f32x8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1040 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_broadcast_f64x2": "_mm512_mask_broadcast_f64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1064 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_broadcast_i32x2": "_mm512_mask_broadcast_i32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1088 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_broadcast_i32x8": "_mm512_mask_broadcast_i32x8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1112 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_broadcast_i64x2": "_mm512_mask_broadcast_i64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1137 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvtepi64_pd": "_mm512_mask_cvtepi64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L405 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvtepi64_ps": "_mm512_mask_cvtepi64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L444 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvtepu64_pd": "_mm512_mask_cvtepu64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L640 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvtepu64_ps": "_mm512_mask_cvtepu64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L681 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvtpd_epi64": "_mm512_mask_cvtpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L248 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvtpd_epu64": "_mm512_mask_cvtpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L287 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvtps_epi64": "_mm512_mask_cvtps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L326 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvtps_epu64": "_mm512_mask_cvtps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L365 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvttpd_epi64": "_mm512_mask_cvttpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L484 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvttpd_epu64": "_mm512_mask_cvttpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L523 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvttps_epi64": "_mm512_mask_cvttps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L562 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_cvttps_epu64": "_mm512_mask_cvttps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L601 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_mullo_epi64": "_mm512_mask_mullo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L39 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_xor_pd": "_mm512_mask_xor_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L61 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mask_xor_ps": "_mm512_mask_xor_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L83 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_and_pd": "_mm512_maskz_and_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L157 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_and_ps": "_mm512_maskz_and_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L179 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_andnot_pd": "_mm512_maskz_andnot_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L205 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_andnot_ps": "_mm512_maskz_andnot_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L231 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_broadcast_f32x2": "_mm512_maskz_broadcast_f32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1024 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_broadcast_f32x8": "_mm512_maskz_broadcast_f32x8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1048 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_broadcast_f64x2": "_mm512_maskz_broadcast_f64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1072 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_broadcast_i32x2": "_mm512_maskz_broadcast_i32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1096 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_broadcast_i32x8": "_mm512_maskz_broadcast_i32x8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1120 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_broadcast_i64x2": "_mm512_maskz_broadcast_i64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1145 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_cvtepi64_pd": "_mm512_maskz_cvtepi64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L413 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_cvtepi64_ps": "_mm512_maskz_cvtepi64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L452 | neighbors=[avx512dqintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-027.json

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
