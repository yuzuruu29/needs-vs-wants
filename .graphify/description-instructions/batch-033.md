# Node Description Batch 34 of 111

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

- "clang_include_avx512fintrin_mm512_knot": "_mm512_knot()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4625 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_load_pd": "_mm512_load_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4488 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_load_ps": "_mm512_load_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4462 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_loadu_pd": "_mm512_loadu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4444 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_loadu_ps": "_mm512_loadu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4453 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_loadu_si512": "_mm512_loadu_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4366 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_abs_epi32": "_mm512_mask_abs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1890 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_abs_epi64": "_mm512_mask_abs_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1864 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_add_epi32": "_mm512_mask_add_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L789 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_add_epi64": "_mm512_mask_add_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L739 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_add_pd": "_mm512_mask_add_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1978 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_add_ps": "_mm512_mask_add_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1996 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_blend_epi32": "_mm512_mask_blend_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3537 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_blend_epi64": "_mm512_mask_blend_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3529 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_blend_pd": "_mm512_mask_blend_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3513 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_blend_ps": "_mm512_mask_blend_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3521 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_broadcast_f32x4": "_mm512_mask_broadcast_f32x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7270 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_broadcast_f64x4": "_mm512_mask_broadcast_f64x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7296 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_broadcast_i32x4": "_mm512_mask_broadcast_i32x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7322 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_broadcast_i64x4": "_mm512_mask_broadcast_i64x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7348 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_ceil_pd": "_mm512_mask_ceil_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1846 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_ceil_ps": "_mm512_mask_ceil_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1819 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpeq_epi32_mask": "_mm512_mask_cmpeq_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4639 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpeq_epi64_mask": "_mm512_mask_cmpeq_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4657 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpeq_epu32_mask": "_mm512_mask_cmpeq_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4651 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpeq_epu64_mask": "_mm512_mask_cmpeq_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4675 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpge_epi32_mask": "_mm512_mask_cmpge_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4687 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpge_epi64_mask": "_mm512_mask_cmpge_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4711 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpge_epu32_mask": "_mm512_mask_cmpge_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4699 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpge_epu64_mask": "_mm512_mask_cmpge_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4723 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpgt_epi32_mask": "_mm512_mask_cmpgt_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4735 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpgt_epi64_mask": "_mm512_mask_cmpgt_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4753 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpgt_epu32_mask": "_mm512_mask_cmpgt_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4747 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpgt_epu64_mask": "_mm512_mask_cmpgt_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4771 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmple_epi32_mask": "_mm512_mask_cmple_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4783 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmple_epi64_mask": "_mm512_mask_cmple_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4807 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmple_epu32_mask": "_mm512_mask_cmple_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4795 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmple_epu64_mask": "_mm512_mask_cmple_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4819 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmplt_epi32_mask": "_mm512_mask_cmplt_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4831 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmplt_epi64_mask": "_mm512_mask_cmplt_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4855 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-033.json

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
