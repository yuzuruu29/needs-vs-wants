# Node Description Batch 10 of 111

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

- "clang_include_clang_cuda_cmath_fabs": "fabs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L59 | neighbors=[__clang_cuda_cmath.h, abs()]
- "clang_include_clang_cuda_math_forward_declares": "__clang_cuda_math_forward_declares.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_math_forward_declares.h:L1 | neighbors=[std(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_clflushoptintrin": "clflushoptintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/clflushoptintrin.h:L1 | neighbors=[_mm_clflushopt(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_emmintrin_mm_cmpgt_epi16": "_mm_cmpgt_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1568 | neighbors=[emmintrin.h, _mm_cmplt_epi16()]
- "clang_include_emmintrin_mm_cmpgt_epi32": "_mm_cmpgt_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1588 | neighbors=[emmintrin.h, _mm_cmplt_epi32()]
- "clang_include_emmintrin_mm_cmpgt_epi8": "_mm_cmpgt_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1546 | neighbors=[emmintrin.h, _mm_cmplt_epi8()]
- "clang_include_emmintrin_mm_cmplt_epi16": "_mm_cmplt_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1628 | neighbors=[emmintrin.h, _mm_cmpgt_epi16()]
- "clang_include_emmintrin_mm_cmplt_epi32": "_mm_cmplt_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1648 | neighbors=[emmintrin.h, _mm_cmpgt_epi32()]
- "clang_include_emmintrin_mm_cmplt_epi8": "_mm_cmplt_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1608 | neighbors=[emmintrin.h, _mm_cmpgt_epi8()]
- "clang_include_emmintrin_mm_store_pd": "_mm_store_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L599 | neighbors=[emmintrin.h, _mm_store1_pd()]
- "clang_include_emmintrin_mm_store_pd1": "_mm_store_pd1()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L612 | neighbors=[emmintrin.h, _mm_store1_pd()]
- "clang_include_mm_malloc": "mm_malloc.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm_malloc.h:L1 | neighbors=[_mm_free(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_mmintrin_mm_set1_pi16": "_mm_set1_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mmintrin.h:L1383 | neighbors=[mmintrin.h, _mm_set_pi16()]
- "clang_include_mmintrin_mm_set1_pi32": "_mm_set1_pi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mmintrin.h:L1365 | neighbors=[mmintrin.h, _mm_set_pi32()]
- "clang_include_mmintrin_mm_set1_pi8": "_mm_set1_pi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mmintrin.h:L1401 | neighbors=[mmintrin.h, _mm_set_pi8()]
- "clang_include_mmintrin_mm_setr_pi16": "_mm_setr_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mmintrin.h:L1445 | neighbors=[mmintrin.h, _mm_set_pi16()]
- "clang_include_mmintrin_mm_setr_pi32": "_mm_setr_pi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mmintrin.h:L1422 | neighbors=[mmintrin.h, _mm_set_pi32()]
- "clang_include_mmintrin_mm_setr_pi8": "_mm_setr_pi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mmintrin.h:L1476 | neighbors=[mmintrin.h, _mm_set_pi8()]
- "clang_include_rtmintrin": "rtmintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/rtmintrin.h:L1 | neighbors=[void(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_stddef": "stddef.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/stddef.h:L1 | neighbors=[std(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_unwind_unwind_getip": "_Unwind_GetIP()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/unwind.h:L195 | neighbors=[unwind.h, _Unwind_GetGR()]
- "clang_include_unwind_unwind_setgr": "_Unwind_SetGR()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/unwind.h:L189 | neighbors=[unwind.h, _Unwind_SetIP()]
- "clang_include_vecintrin_vec_cmplt": "vec_cmplt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L2017 | neighbors=[vecintrin.h, vec_abs()]
- "clang_include_xmmintrin_mm_cvt_pi2ps": "_mm_cvt_pi2ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1538 | neighbors=[xmmintrin.h, _mm_cvtpi32_ps()]
- "clang_include_xmmintrin_mm_cvt_ps2pi": "_mm_cvt_ps2pi()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1332 | neighbors=[xmmintrin.h, _mm_cvtps_pi32()]
- "clang_include_xmmintrin_mm_cvt_si2ss": "_mm_cvt_si2ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1465 | neighbors=[xmmintrin.h, _mm_cvtsi32_ss()]
- "clang_include_xmmintrin_mm_cvt_ss2si": "_mm_cvt_ss2si()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1279 | neighbors=[xmmintrin.h, _mm_cvtss_si32()]
- "clang_include_xmmintrin_mm_cvtpi8_ps": "_mm_cvtpi8_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2675 | neighbors=[xmmintrin.h, _mm_cvtpi16_ps()]
- "clang_include_xmmintrin_mm_cvtps_pi8": "_mm_cvtps_pi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2784 | neighbors=[xmmintrin.h, _mm_cvtps_pi16()]
- "clang_include_xmmintrin_mm_cvtpu8_ps": "_mm_cvtpu8_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2700 | neighbors=[xmmintrin.h, _mm_cvtpi16_ps()]
- "clang_include_xmmintrin_mm_cvtsi32_ss": "_mm_cvtsi32_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1442 | neighbors=[xmmintrin.h, _mm_cvt_si2ss()]
- "clang_include_xmmintrin_mm_cvtss_si32": "_mm_cvtss_si32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1262 | neighbors=[xmmintrin.h, _mm_cvt_ss2si()]
- "clang_include_xmmintrin_mm_cvtt_ps2pi": "_mm_cvtt_ps2pi()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1420 | neighbors=[xmmintrin.h, _mm_cvttps_pi32()]
- "clang_include_xmmintrin_mm_cvtt_ss2si": "_mm_cvtt_ss2si()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1368 | neighbors=[xmmintrin.h, _mm_cvttss_si32()]
- "clang_include_xmmintrin_mm_cvttps_pi32": "_mm_cvttps_pi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1403 | neighbors=[xmmintrin.h, _mm_cvtt_ps2pi()]
- "clang_include_xmmintrin_mm_cvttss_si32": "_mm_cvttss_si32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1350 | neighbors=[xmmintrin.h, _mm_cvtt_ss2si()]
- "clang_include_xmmintrin_mm_load_ps": "_mm_load_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1675 | neighbors=[xmmintrin.h, _mm_loadr_ps()]
- "clang_include_xmmintrin_mm_loadr_ps": "_mm_loadr_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1714 | neighbors=[xmmintrin.h, _mm_load_ps()]
- "clang_include_xmmintrin_mm_movehl_ps": "_mm_movehl_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2577 | neighbors=[xmmintrin.h, _mm_cvtps_pi16()]
- "clang_include_xmmintrin_mm_set_ps1": "_mm_set_ps1()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1786 | neighbors=[xmmintrin.h, _mm_set1_ps()]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-009.json

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
