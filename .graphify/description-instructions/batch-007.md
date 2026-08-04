# Node Description Batch 8 of 111

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

- "clang_include_avx512fintrin_mm512_maskz_broadcastd_epi32": "_mm512_maskz_broadcastd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L215 | neighbors=[avx512fintrin.h, _mm512_broadcastd_epi32()]
- "clang_include_avx512fintrin_mm512_maskz_broadcastq_epi64": "_mm512_maskz_broadcastq_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L240 | neighbors=[avx512fintrin.h, _mm512_broadcastq_epi64()]
- "clang_include_avx512fintrin_mm512_maskz_broadcastsd_pd": "_mm512_maskz_broadcastsd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7373 | neighbors=[avx512fintrin.h, _mm512_broadcastsd_pd()]
- "clang_include_avx512fintrin_mm512_maskz_broadcastss_ps": "_mm512_maskz_broadcastss_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7389 | neighbors=[avx512fintrin.h, _mm512_broadcastss_ps()]
- "clang_include_avx512fintrin_mm512_maskz_movehdup_ps": "_mm512_maskz_movehdup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9014 | neighbors=[avx512fintrin.h, _mm512_movehdup_ps()]
- "clang_include_avx512fintrin_mm512_maskz_moveldup_ps": "_mm512_maskz_moveldup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9037 | neighbors=[avx512fintrin.h, _mm512_moveldup_ps()]
- "clang_include_avx512fintrin_mm512_maskz_or_epi32": "_mm512_maskz_or_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L611 | neighbors=[avx512fintrin.h, _mm512_mask_or_epi32()]
- "clang_include_avx512fintrin_mm512_maskz_or_epi64": "_mm512_maskz_or_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L631 | neighbors=[avx512fintrin.h, _mm512_mask_or_epi64()]
- "clang_include_avx512fintrin_mm512_maskz_unpackhi_epi32": "_mm512_maskz_unpackhi_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4252 | neighbors=[avx512fintrin.h, _mm512_unpackhi_epi32()]
- "clang_include_avx512fintrin_mm512_maskz_unpackhi_epi64": "_mm512_maskz_unpackhi_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4301 | neighbors=[avx512fintrin.h, _mm512_unpackhi_epi64()]
- "clang_include_avx512fintrin_mm512_maskz_unpackhi_pd": "_mm512_maskz_unpackhi_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4151 | neighbors=[avx512fintrin.h, _mm512_unpackhi_pd()]
- "clang_include_avx512fintrin_mm512_maskz_unpackhi_ps": "_mm512_maskz_unpackhi_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4200 | neighbors=[avx512fintrin.h, _mm512_unpackhi_ps()]
- "clang_include_avx512fintrin_mm512_maskz_unpacklo_epi32": "_mm512_maskz_unpacklo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4278 | neighbors=[avx512fintrin.h, _mm512_unpacklo_epi32()]
- "clang_include_avx512fintrin_mm512_maskz_unpacklo_epi64": "_mm512_maskz_unpacklo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4324 | neighbors=[avx512fintrin.h, _mm512_unpacklo_epi64()]
- "clang_include_avx512fintrin_mm512_maskz_unpacklo_pd": "_mm512_maskz_unpacklo_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4174 | neighbors=[avx512fintrin.h, _mm512_unpacklo_pd()]
- "clang_include_avx512fintrin_mm512_maskz_unpacklo_ps": "_mm512_maskz_unpacklo_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4226 | neighbors=[avx512fintrin.h, _mm512_unpacklo_ps()]
- "clang_include_avx512fintrin_mm512_maskz_xor_epi32": "_mm512_maskz_xor_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L651 | neighbors=[avx512fintrin.h, _mm512_mask_xor_epi32()]
- "clang_include_avx512fintrin_mm512_maskz_xor_epi64": "_mm512_maskz_xor_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L671 | neighbors=[avx512fintrin.h, _mm512_mask_xor_epi64()]
- "clang_include_avx512fintrin_mm512_or_epi32": "_mm512_or_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L597 | neighbors=[avx512fintrin.h, _mm512_mask_or_epi32()]
- "clang_include_avx512fintrin_mm512_or_epi64": "_mm512_or_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L617 | neighbors=[avx512fintrin.h, _mm512_mask_or_epi64()]
- "clang_include_avx512fintrin_mm512_xor_epi32": "_mm512_xor_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L637 | neighbors=[avx512fintrin.h, _mm512_mask_xor_epi32()]
- "clang_include_avx512fintrin_mm512_xor_epi64": "_mm512_xor_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L657 | neighbors=[avx512fintrin.h, _mm512_mask_xor_epi64()]
- "clang_include_avx512vlintrin_default_fn_attrs": "__DEFAULT_FN_ATTRS()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1897 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_mask_and_epi32": "_mm_mask_and_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L906 | neighbors=[avx512vlintrin.h, _mm_maskz_and_epi32()]
- "clang_include_avx512vlintrin_mm_mask_and_epi64": "_mm_mask_and_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1020 | neighbors=[avx512vlintrin.h, _mm_maskz_and_epi64()]
- "clang_include_avx512vlintrin_mm_mask_andnot_epi32": "_mm_mask_andnot_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L935 | neighbors=[avx512vlintrin.h, _mm_maskz_andnot_epi32()]
- "clang_include_avx512vlintrin_mm_mask_andnot_epi64": "_mm_mask_andnot_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1049 | neighbors=[avx512vlintrin.h, _mm_maskz_andnot_epi64()]
- "clang_include_avx512vlintrin_mm_mask_or_epi32": "_mm_mask_or_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L963 | neighbors=[avx512vlintrin.h, _mm_maskz_or_epi32()]
- "clang_include_avx512vlintrin_mm_mask_or_epi64": "_mm_mask_or_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1077 | neighbors=[avx512vlintrin.h, _mm_maskz_or_epi64()]
- "clang_include_avx512vlintrin_mm_mask_xor_epi32": "_mm_mask_xor_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L991 | neighbors=[avx512vlintrin.h, _mm_maskz_xor_epi32()]
- "clang_include_avx512vlintrin_mm_mask_xor_epi64": "_mm_mask_xor_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1105 | neighbors=[avx512vlintrin.h, _mm_maskz_xor_epi64()]
- "clang_include_avx512vlintrin_mm_maskz_and_epi32": "_mm_maskz_and_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L914 | neighbors=[avx512vlintrin.h, _mm_mask_and_epi32()]
- "clang_include_avx512vlintrin_mm_maskz_and_epi64": "_mm_maskz_and_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1028 | neighbors=[avx512vlintrin.h, _mm_mask_and_epi64()]
- "clang_include_avx512vlintrin_mm_maskz_andnot_epi32": "_mm_maskz_andnot_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L943 | neighbors=[avx512vlintrin.h, _mm_mask_andnot_epi32()]
- "clang_include_avx512vlintrin_mm_maskz_andnot_epi64": "_mm_maskz_andnot_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1057 | neighbors=[avx512vlintrin.h, _mm_mask_andnot_epi64()]
- "clang_include_avx512vlintrin_mm_maskz_load_epi64": "_mm_maskz_load_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5880 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_maskz_mov_epi64": "_mm_maskz_mov_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5847 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]
- "clang_include_avx512vlintrin_mm_maskz_or_epi32": "_mm_maskz_or_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L971 | neighbors=[avx512vlintrin.h, _mm_mask_or_epi32()]
- "clang_include_avx512vlintrin_mm_maskz_or_epi64": "_mm_maskz_or_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1085 | neighbors=[avx512vlintrin.h, _mm_mask_or_epi64()]
- "clang_include_avx512vlintrin_mm_maskz_rolv_epi64": "_mm_maskz_rolv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5031 | neighbors=[avx512vlintrin.h, _mm_setzero_di()]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-007.json

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
