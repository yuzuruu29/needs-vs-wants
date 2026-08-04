# Node Description Batch 100 of 111

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

- "clang_include_tgmath_tg_nexttoward": "__tg_nexttoward()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1067 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_pow": "__tg_pow()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L471 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_remainder": "__tg_remainder()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1084 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_remquo": "__tg_remquo()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1102 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_rint": "__tg_rint()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1125 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_round": "__tg_round()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1142 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_scalbln": "__tg_scalbln()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1176 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_scalbn": "__tg_scalbn()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1159 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_sin": "__tg_sin()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L268 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_sinh": "__tg_sinh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L355 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_sqrt": "__tg_sqrt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L502 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_tan": "__tg_tan()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L297 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_tanh": "__tg_tanh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L384 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_tgamma": "__tg_tgamma()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1193 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_trunc": "__tg_trunc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1210 | neighbors=[tgmath.h]
- "clang_include_tmmintrin_mm_abs_epi16": "_mm_abs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L98 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_abs_epi32": "_mm_abs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L134 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_abs_epi8": "_mm_abs_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L62 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_abs_pi16": "_mm_abs_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L80 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_abs_pi32": "_mm_abs_pi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L116 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_abs_pi8": "_mm_abs_pi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L44 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hadd_epi16": "_mm_hadd_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L203 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hadd_epi32": "_mm_hadd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L226 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hadd_pi16": "_mm_hadd_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L249 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hadd_pi32": "_mm_hadd_pi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L272 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hadds_epi16": "_mm_hadds_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L296 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hadds_pi16": "_mm_hadds_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L320 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hsub_epi16": "_mm_hsub_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L343 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hsub_epi32": "_mm_hsub_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L366 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hsub_pi16": "_mm_hsub_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L389 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hsub_pi32": "_mm_hsub_pi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L412 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hsubs_epi16": "_mm_hsubs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L437 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_hsubs_pi16": "_mm_hsubs_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L462 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_maddubs_epi16": "_mm_maddubs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L495 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_maddubs_pi16": "_mm_maddubs_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L524 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_mulhrs_epi16": "_mm_mulhrs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L544 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_mulhrs_pi16": "_mm_mulhrs_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L564 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_shuffle_epi8": "_mm_shuffle_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L590 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_shuffle_pi8": "_mm_shuffle_pi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L615 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_sign_epi16": "_mm_sign_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L665 | neighbors=[tmmintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-099.json

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
