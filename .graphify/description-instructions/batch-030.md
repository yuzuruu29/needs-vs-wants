# Node Description Batch 31 of 111

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

- "clang_include_avx512fintrin_mm_maskz_mul_sd": "_mm_maskz_mul_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2239 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_mul_ss": "_mm_maskz_mul_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2204 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_rcp14_sd": "_mm_maskz_rcp14_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1774 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_rcp14_ss": "_mm_maskz_rcp14_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1746 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_rsqrt14_sd": "_mm_maskz_rsqrt14_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1666 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_rsqrt14_ss": "_mm_maskz_rsqrt14_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1638 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_scalef_sd": "_mm_maskz_scalef_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6967 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_scalef_ss": "_mm_maskz_scalef_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7014 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_sub_sd": "_mm_maskz_sub_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2094 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_sub_ss": "_mm_maskz_sub_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2059 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_rcp14_sd": "_mm_rcp14_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1755 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_rcp14_ss": "_mm_rcp14_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1727 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_rsqrt14_sd": "_mm_rsqrt14_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1647 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_rsqrt14_ss": "_mm_rsqrt14_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1619 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_abs_epi32": "_mm512_abs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1881 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_abs_epi64": "_mm512_abs_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1855 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_add_pd": "_mm512_add_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L697 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_add_ps": "_mm512_add_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L703 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_and_si512": "_mm512_and_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L677 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_andnot_si512": "_mm512_andnot_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L549 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_broadcast_f32x4": "_mm512_broadcast_f32x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7261 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_broadcast_f64x4": "_mm512_broadcast_f64x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7287 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_broadcast_i32x4": "_mm512_broadcast_i32x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7313 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_broadcast_i64x4": "_mm512_broadcast_i64x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7339 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castpd_si512": "_mm512_castpd_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L440 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castpd128_pd512": "_mm512_castpd128_pd512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L446 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castpd256_pd512": "_mm512_castpd256_pd512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L397 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castpd512_pd128": "_mm512_castpd512_pd128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L410 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castpd512_pd256": "_mm512_castpd512_pd256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L416 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castps_si512": "_mm512_castps_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L458 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castps128_ps512": "_mm512_castps128_ps512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L464 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castps256_ps512": "_mm512_castps256_ps512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L403 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castps512_ps128": "_mm512_castps512_ps128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L422 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castps512_ps256": "_mm512_castps512_ps256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L428 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castsi128_si512": "_mm512_castsi128_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L470 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castsi256_si512": "_mm512_castsi256_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L476 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castsi512_pd": "_mm512_castsi512_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L488 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castsi512_ps": "_mm512_castsi512_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L482 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castsi512_si128": "_mm512_castsi512_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L494 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_castsi512_si256": "_mm512_castsi512_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L500 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-030.json

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
