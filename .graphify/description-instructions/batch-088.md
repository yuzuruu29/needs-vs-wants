# Node Description Batch 89 of 111

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

- "clang_include_emmintrin_mm_subs_epi16": "_mm_subs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L966 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_subs_epi8": "_mm_subs_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L945 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_subs_epu16": "_mm_subs_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1006 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_subs_epu8": "_mm_subs_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L986 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_ucomieq_sd": "_mm_ucomieq_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L349 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_ucomige_sd": "_mm_ucomige_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L373 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_ucomigt_sd": "_mm_ucomigt_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L367 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_ucomile_sd": "_mm_ucomile_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L361 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_ucomilt_sd": "_mm_ucomilt_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L355 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_ucomineq_sd": "_mm_ucomineq_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L379 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_unpackhi_epi16": "_mm_unpackhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2329 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_unpackhi_epi32": "_mm_unpackhi_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2335 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_unpackhi_epi64": "_mm_unpackhi_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2341 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_unpackhi_epi8": "_mm_unpackhi_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2323 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_unpackhi_pd": "_mm_unpackhi_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2389 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_unpacklo_epi16": "_mm_unpacklo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2353 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_unpacklo_epi32": "_mm_unpacklo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2359 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_unpacklo_epi64": "_mm_unpacklo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2365 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_unpacklo_epi8": "_mm_unpacklo_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2347 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_unpacklo_pd": "_mm_unpacklo_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2395 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_xor_pd": "_mm_xor_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L159 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_xor_si128": "_mm_xor_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1079 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_void": "void()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L549 | neighbors=[emmintrin.h]
- "clang_include_f16cintrin_cvtsh_ss": "_cvtsh_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/f16cintrin.h:L45 | neighbors=[f16cintrin.h]
- "clang_include_f16cintrin_mm_cvtph_ps": "_mm_cvtph_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/f16cintrin.h:L116 | neighbors=[f16cintrin.h]
- "clang_include_float": "float.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/float.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_fma4intrin_mm_macc_pd": "_mm_macc_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L42 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_macc_ps": "_mm_macc_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L36 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_macc_sd": "_mm_macc_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L54 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_macc_ss": "_mm_macc_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L48 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_maddsub_pd": "_mm_maddsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L138 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_maddsub_ps": "_mm_maddsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L132 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_msub_pd": "_mm_msub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L66 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_msub_ps": "_mm_msub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L60 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_msub_sd": "_mm_msub_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L78 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_msub_ss": "_mm_msub_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L72 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_msubadd_pd": "_mm_msubadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L150 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_msubadd_ps": "_mm_msubadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L144 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_nmacc_pd": "_mm_nmacc_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L90 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_nmacc_ps": "_mm_nmacc_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L84 | neighbors=[fma4intrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-088.json

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
