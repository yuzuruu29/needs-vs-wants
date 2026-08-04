# Node Description Batch 62 of 111

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

- "clang_include_avx512vlintrin_mm_mask_expandloadu_epi32": "_mm_mask_expandloadu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2943 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_expandloadu_epi64": "_mm_mask_expandloadu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2876 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_expandloadu_pd": "_mm_mask_expandloadu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2842 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_expandloadu_ps": "_mm_mask_expandloadu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2911 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fmadd_pd": "_mm_mask_fmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1240 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fmadd_ps": "_mm_mask_fmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1384 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fmaddsub_pd": "_mm_mask_fmaddsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1528 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fmaddsub_ps": "_mm_mask_fmaddsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1624 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fmsub_pd": "_mm_mask_fmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1267 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fmsub_ps": "_mm_mask_fmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1411 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fmsubadd_pd": "_mm_mask_fmsubadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1557 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fmsubadd_ps": "_mm_mask_fmsubadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1651 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fnmadd_pd": "_mm_mask_fnmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1789 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fnmadd_ps": "_mm_mask_fnmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1807 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fnmsub_pd": "_mm_mask_fnmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1825 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_fnmsub_ps": "_mm_mask_fnmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1861 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_getexp_pd": "_mm_mask_getexp_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3045 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_getexp_ps": "_mm_mask_getexp_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3091 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_load_epi32": "_mm_mask_load_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5785 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_load_epi64": "_mm_mask_load_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5871 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_loadu_epi32": "_mm_mask_loadu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6184 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_loadu_epi64": "_mm_mask_loadu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6150 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_max_epi32": "_mm_mask_max_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3421 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_max_epi64": "_mm_mask_max_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3455 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_max_epu32": "_mm_mask_max_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3507 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_max_epu64": "_mm_mask_max_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3550 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_min_epi32": "_mm_mask_min_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3593 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_min_epi64": "_mm_mask_min_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3627 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_min_epu32": "_mm_mask_min_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3679 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_min_epu64": "_mm_mask_min_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3713 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_mov_epi32": "_mm_mask_mov_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5752 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_mov_epi64": "_mm_mask_mov_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5839 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_movedup_pd": "_mm_mask_movedup_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5925 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_movehdup_ps": "_mm_mask_movehdup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8934 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_moveldup_ps": "_mm_mask_moveldup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8966 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_mul_epi32": "_mm_mask_mul_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L797 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_mul_epu32": "_mm_mask_mul_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L835 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_mullo_epi32": "_mm_mask_mullo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L883 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_permutevar_pd": "_mm_mask_permutevar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6655 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_permutevar_ps": "_mm_mask_permutevar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6697 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-061.json

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
