# Node Description Batch 4 of 111

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

- "clang_include_altivec_vec_cmpgt": "vec_cmpgt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1600 | neighbors=[altivec.h, vec_cmpge(), vec_cmplt()]
- "clang_include_altivec_vec_ld": "vec_ld()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2250 | neighbors=[altivec.h, vec_lvlx(), vec_lvrx()]
- "clang_include_altivec_vec_ldl": "vec_ldl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2502 | neighbors=[altivec.h, vec_lvlxl(), vec_lvrxl()]
- "clang_include_altivec_vec_st": "vec_st()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L8662 | neighbors=[altivec.h, vec_stvlx(), vec_stvrx()]
- "clang_include_altivec_vec_stl": "vec_stl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L9082 | neighbors=[altivec.h, vec_stvlxl(), vec_stvrxl()]
- "clang_include_arm_acle_rbitl": "__rbitl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L218 | neighbors=[arm_acle.h, __rbit(), __rbitll()]
- "clang_include_arm_acle_rev16l": "__rev16l()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L187 | neighbors=[arm_acle.h, __rev16(), __rev16ll()]
- "clang_include_arm_acle_rev16ll": "__rev16ll()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L182 | neighbors=[arm_acle.h, __rev16l(), __rev16()]
- "clang_include_arm_acle_ror": "__ror()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L114 | neighbors=[arm_acle.h, __rev16(), __rorl()]
- "clang_include_arm_acle_rorl": "__rorl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L130 | neighbors=[arm_acle.h, __ror(), __rorll()]
- "clang_include_avx512bwintrin_mm512_broadcastb_epi8": "_mm512_broadcastb_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2266 | neighbors=[avx512bwintrin.h, _mm512_mask_broadcastb_epi8(), _mm512_maskz_broadcastb_epi8()]
- "clang_include_avx512bwintrin_mm512_broadcastw_epi16": "_mm512_broadcastw_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2309 | neighbors=[avx512bwintrin.h, _mm512_mask_broadcastw_epi16(), _mm512_maskz_broadcastw_epi16()]
- "clang_include_avx512bwintrin_mm512_unpackhi_epi16": "_mm512_unpackhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1450 | neighbors=[avx512bwintrin.h, _mm512_mask_unpackhi_epi16(), _mm512_maskz_unpackhi_epi16()]
- "clang_include_avx512bwintrin_mm512_unpackhi_epi8": "_mm512_unpackhi_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1415 | neighbors=[avx512bwintrin.h, _mm512_mask_unpackhi_epi8(), _mm512_maskz_unpackhi_epi8()]
- "clang_include_avx512bwintrin_mm512_unpacklo_epi16": "_mm512_unpacklo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1512 | neighbors=[avx512bwintrin.h, _mm512_mask_unpacklo_epi16(), _mm512_maskz_unpacklo_epi16()]
- "clang_include_avx512bwintrin_mm512_unpacklo_epi8": "_mm512_unpacklo_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1477 | neighbors=[avx512bwintrin.h, _mm512_mask_unpacklo_epi8(), _mm512_maskz_unpacklo_epi8()]
- "clang_include_avx512fintrin_mm512_abs_pd": "_mm512_abs_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9529 | neighbors=[avx512fintrin.h, _mm512_and_epi64(), _mm512_set1_epi64()]
- "clang_include_avx512fintrin_mm512_abs_ps": "_mm512_abs_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9517 | neighbors=[avx512fintrin.h, _mm512_and_epi32(), _mm512_set1_epi32()]
- "clang_include_avx512fintrin_mm512_and_epi32": "_mm512_and_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L507 | neighbors=[avx512fintrin.h, _mm512_abs_ps(), _mm512_mask_and_epi32()]
- "clang_include_avx512fintrin_mm512_and_epi64": "_mm512_and_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L528 | neighbors=[avx512fintrin.h, _mm512_abs_pd(), _mm512_mask_and_epi64()]
- "clang_include_avx512fintrin_mm512_broadcastd_epi32": "_mm512_broadcastd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L199 | neighbors=[avx512fintrin.h, _mm512_mask_broadcastd_epi32(), _mm512_maskz_broadcastd_epi32()]
- "clang_include_avx512fintrin_mm512_broadcastq_epi64": "_mm512_broadcastq_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L223 | neighbors=[avx512fintrin.h, _mm512_mask_broadcastq_epi64(), _mm512_maskz_broadcastq_epi64()]
- "clang_include_avx512fintrin_mm512_broadcastsd_pd": "_mm512_broadcastsd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L387 | neighbors=[avx512fintrin.h, _mm512_mask_broadcastsd_pd(), _mm512_maskz_broadcastsd_pd()]
- "clang_include_avx512fintrin_mm512_broadcastss_ps": "_mm512_broadcastss_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L336 | neighbors=[avx512fintrin.h, _mm512_mask_broadcastss_ps(), _mm512_maskz_broadcastss_ps()]
- "clang_include_avx512fintrin_mm512_mask_abs_pd": "_mm512_mask_abs_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9535 | neighbors=[avx512fintrin.h, _mm512_mask_and_epi64(), _mm512_set1_epi64()]
- "clang_include_avx512fintrin_mm512_mask_abs_ps": "_mm512_mask_abs_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9523 | neighbors=[avx512fintrin.h, _mm512_mask_and_epi32(), _mm512_set1_epi32()]
- "clang_include_avx512fintrin_mm512_mask_andnot_epi32": "_mm512_mask_andnot_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L561 | neighbors=[avx512fintrin.h, _mm512_andnot_epi32(), _mm512_maskz_andnot_epi32()]
- "clang_include_avx512fintrin_mm512_mask_andnot_epi64": "_mm512_mask_andnot_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L582 | neighbors=[avx512fintrin.h, _mm512_andnot_epi64(), _mm512_maskz_andnot_epi64()]
- "clang_include_avx512fintrin_mm512_mask_or_epi32": "_mm512_mask_or_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L603 | neighbors=[avx512fintrin.h, _mm512_or_epi32(), _mm512_maskz_or_epi32()]
- "clang_include_avx512fintrin_mm512_mask_or_epi64": "_mm512_mask_or_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L623 | neighbors=[avx512fintrin.h, _mm512_or_epi64(), _mm512_maskz_or_epi64()]
- "clang_include_avx512fintrin_mm512_mask_xor_epi32": "_mm512_mask_xor_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L643 | neighbors=[avx512fintrin.h, _mm512_xor_epi32(), _mm512_maskz_xor_epi32()]
- "clang_include_avx512fintrin_mm512_mask_xor_epi64": "_mm512_mask_xor_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L663 | neighbors=[avx512fintrin.h, _mm512_xor_epi64(), _mm512_maskz_xor_epi64()]
- "clang_include_avx512fintrin_mm512_movehdup_ps": "_mm512_movehdup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8999 | neighbors=[avx512fintrin.h, _mm512_mask_movehdup_ps(), _mm512_maskz_movehdup_ps()]
- "clang_include_avx512fintrin_mm512_moveldup_ps": "_mm512_moveldup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9022 | neighbors=[avx512fintrin.h, _mm512_mask_moveldup_ps(), _mm512_maskz_moveldup_ps()]
- "clang_include_avx512fintrin_mm512_set1_epi32": "_mm512_set1_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L323 | neighbors=[avx512fintrin.h, _mm512_abs_ps(), _mm512_mask_abs_ps()]
- "clang_include_avx512fintrin_mm512_set1_epi64": "_mm512_set1_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L330 | neighbors=[avx512fintrin.h, _mm512_abs_pd(), _mm512_mask_abs_pd()]
- "clang_include_avx512fintrin_mm512_unpackhi_epi32": "_mm512_unpackhi_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4234 | neighbors=[avx512fintrin.h, _mm512_mask_unpackhi_epi32(), _mm512_maskz_unpackhi_epi32()]
- "clang_include_avx512fintrin_mm512_unpackhi_epi64": "_mm512_unpackhi_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4286 | neighbors=[avx512fintrin.h, _mm512_mask_unpackhi_epi64(), _mm512_maskz_unpackhi_epi64()]
- "clang_include_avx512fintrin_mm512_unpackhi_pd": "_mm512_unpackhi_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4136 | neighbors=[avx512fintrin.h, _mm512_mask_unpackhi_pd(), _mm512_maskz_unpackhi_pd()]
- "clang_include_avx512fintrin_mm512_unpackhi_ps": "_mm512_unpackhi_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4182 | neighbors=[avx512fintrin.h, _mm512_mask_unpackhi_ps(), _mm512_maskz_unpackhi_ps()]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-003.json

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
