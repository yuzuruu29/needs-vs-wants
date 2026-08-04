# Node Description Batch 7 of 111

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

- "clang_include_altivec_vec_vspltb": "vec_vspltb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7795 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vsplth": "vec_vsplth()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7814 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vspltw": "vec_vspltw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7854 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_xor": "vec_xor()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10518 | neighbors=[altivec.h, vec_perm()]
- "clang_include_arm_acle_rbit": "__rbit()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L203 | neighbors=[arm_acle.h, __rbitl()]
- "clang_include_arm_acle_rbitll": "__rbitll()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L208 | neighbors=[arm_acle.h, __rbitl()]
- "clang_include_arm_acle_rev": "__rev()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L157 | neighbors=[arm_acle.h, __rev16()]
- "clang_include_arm_acle_rorll": "__rorll()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L122 | neighbors=[arm_acle.h, __rorl()]
- "clang_include_avx512bwintrin_mm512_mask_broadcastb_epi8": "_mm512_mask_broadcastb_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2277 | neighbors=[avx512bwintrin.h, _mm512_broadcastb_epi8()]
- "clang_include_avx512bwintrin_mm512_mask_broadcastw_epi16": "_mm512_mask_broadcastw_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2318 | neighbors=[avx512bwintrin.h, _mm512_broadcastw_epi16()]
- "clang_include_avx512bwintrin_mm512_mask_unpackhi_epi16": "_mm512_mask_unpackhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1463 | neighbors=[avx512bwintrin.h, _mm512_unpackhi_epi16()]
- "clang_include_avx512bwintrin_mm512_mask_unpackhi_epi8": "_mm512_mask_unpackhi_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1436 | neighbors=[avx512bwintrin.h, _mm512_unpackhi_epi8()]
- "clang_include_avx512bwintrin_mm512_mask_unpacklo_epi16": "_mm512_mask_unpacklo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1525 | neighbors=[avx512bwintrin.h, _mm512_unpacklo_epi16()]
- "clang_include_avx512bwintrin_mm512_mask_unpacklo_epi8": "_mm512_mask_unpacklo_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1498 | neighbors=[avx512bwintrin.h, _mm512_unpacklo_epi8()]
- "clang_include_avx512bwintrin_mm512_maskz_broadcastb_epi8": "_mm512_maskz_broadcastb_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2285 | neighbors=[avx512bwintrin.h, _mm512_broadcastb_epi8()]
- "clang_include_avx512bwintrin_mm512_maskz_broadcastw_epi16": "_mm512_maskz_broadcastw_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2326 | neighbors=[avx512bwintrin.h, _mm512_broadcastw_epi16()]
- "clang_include_avx512bwintrin_mm512_maskz_unpackhi_epi16": "_mm512_maskz_unpackhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1470 | neighbors=[avx512bwintrin.h, _mm512_unpackhi_epi16()]
- "clang_include_avx512bwintrin_mm512_maskz_unpackhi_epi8": "_mm512_maskz_unpackhi_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1443 | neighbors=[avx512bwintrin.h, _mm512_unpackhi_epi8()]
- "clang_include_avx512bwintrin_mm512_maskz_unpacklo_epi16": "_mm512_maskz_unpacklo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1532 | neighbors=[avx512bwintrin.h, _mm512_unpacklo_epi16()]
- "clang_include_avx512bwintrin_mm512_maskz_unpacklo_epi8": "_mm512_maskz_unpacklo_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1505 | neighbors=[avx512bwintrin.h, _mm512_unpacklo_epi8()]
- "clang_include_avx512fintrin_mm512_andnot_epi32": "_mm512_andnot_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L555 | neighbors=[avx512fintrin.h, _mm512_mask_andnot_epi32()]
- "clang_include_avx512fintrin_mm512_andnot_epi64": "_mm512_andnot_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L576 | neighbors=[avx512fintrin.h, _mm512_mask_andnot_epi64()]
- "clang_include_avx512fintrin_mm512_mask_broadcastd_epi32": "_mm512_mask_broadcastd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L207 | neighbors=[avx512fintrin.h, _mm512_broadcastd_epi32()]
- "clang_include_avx512fintrin_mm512_mask_broadcastq_epi64": "_mm512_mask_broadcastq_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L231 | neighbors=[avx512fintrin.h, _mm512_broadcastq_epi64()]
- "clang_include_avx512fintrin_mm512_mask_broadcastsd_pd": "_mm512_mask_broadcastsd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7365 | neighbors=[avx512fintrin.h, _mm512_broadcastsd_pd()]
- "clang_include_avx512fintrin_mm512_mask_broadcastss_ps": "_mm512_mask_broadcastss_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7381 | neighbors=[avx512fintrin.h, _mm512_broadcastss_ps()]
- "clang_include_avx512fintrin_mm512_mask_movehdup_ps": "_mm512_mask_movehdup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9006 | neighbors=[avx512fintrin.h, _mm512_movehdup_ps()]
- "clang_include_avx512fintrin_mm512_mask_moveldup_ps": "_mm512_mask_moveldup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9029 | neighbors=[avx512fintrin.h, _mm512_moveldup_ps()]
- "clang_include_avx512fintrin_mm512_mask_unpackhi_epi32": "_mm512_mask_unpackhi_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4244 | neighbors=[avx512fintrin.h, _mm512_unpackhi_epi32()]
- "clang_include_avx512fintrin_mm512_mask_unpackhi_epi64": "_mm512_mask_unpackhi_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4293 | neighbors=[avx512fintrin.h, _mm512_unpackhi_epi64()]
- "clang_include_avx512fintrin_mm512_mask_unpackhi_pd": "_mm512_mask_unpackhi_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4143 | neighbors=[avx512fintrin.h, _mm512_unpackhi_pd()]
- "clang_include_avx512fintrin_mm512_mask_unpackhi_ps": "_mm512_mask_unpackhi_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4192 | neighbors=[avx512fintrin.h, _mm512_unpackhi_ps()]
- "clang_include_avx512fintrin_mm512_mask_unpacklo_epi32": "_mm512_mask_unpacklo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4270 | neighbors=[avx512fintrin.h, _mm512_unpacklo_epi32()]
- "clang_include_avx512fintrin_mm512_mask_unpacklo_epi64": "_mm512_mask_unpacklo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4316 | neighbors=[avx512fintrin.h, _mm512_unpacklo_epi64()]
- "clang_include_avx512fintrin_mm512_mask_unpacklo_pd": "_mm512_mask_unpacklo_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4166 | neighbors=[avx512fintrin.h, _mm512_unpacklo_pd()]
- "clang_include_avx512fintrin_mm512_mask_unpacklo_ps": "_mm512_mask_unpacklo_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4218 | neighbors=[avx512fintrin.h, _mm512_unpacklo_ps()]
- "clang_include_avx512fintrin_mm512_maskz_and_epi32": "_mm512_maskz_and_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L521 | neighbors=[avx512fintrin.h, _mm512_mask_and_epi32()]
- "clang_include_avx512fintrin_mm512_maskz_and_epi64": "_mm512_maskz_and_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L542 | neighbors=[avx512fintrin.h, _mm512_mask_and_epi64()]
- "clang_include_avx512fintrin_mm512_maskz_andnot_epi32": "_mm512_maskz_andnot_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L569 | neighbors=[avx512fintrin.h, _mm512_mask_andnot_epi32()]
- "clang_include_avx512fintrin_mm512_maskz_andnot_epi64": "_mm512_maskz_andnot_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L590 | neighbors=[avx512fintrin.h, _mm512_mask_andnot_epi64()]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-006.json

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
