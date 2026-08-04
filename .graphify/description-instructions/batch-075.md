# Node Description Batch 76 of 111

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

- "clang_include_avx512vlintrin_mm256_maskz_cvtsepi64_epi32": "_mm256_maskz_cvtsepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7817 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtsepi64_epi8": "_mm256_maskz_cvtsepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7758 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvttpd_epi32": "_mm256_maskz_cvttpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2491 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvttpd_epu32": "_mm256_maskz_cvttpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2537 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvttps_epi32": "_mm256_maskz_cvttps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2567 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvttps_epu32": "_mm256_maskz_cvttps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2613 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtusepi32_epi16": "_mm256_maskz_cvtusepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7993 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtusepi32_epi8": "_mm256_maskz_cvtusepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7935 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtusepi64_epi16": "_mm256_maskz_cvtusepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8169 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtusepi64_epi32": "_mm256_maskz_cvtusepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8111 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtusepi64_epi8": "_mm256_maskz_cvtusepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8053 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_div_pd": "_mm256_maskz_div_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2739 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_div_ps": "_mm256_maskz_div_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2773 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_expand_epi32": "_mm256_maskz_expand_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3029 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_expand_epi64": "_mm256_maskz_expand_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2834 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_expand_pd": "_mm256_maskz_expand_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2804 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_expand_ps": "_mm256_maskz_expand_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2999 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_expandloadu_epi32": "_mm256_maskz_expandloadu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2968 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_expandloadu_epi64": "_mm256_maskz_expandloadu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2902 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_expandloadu_pd": "_mm256_maskz_expandloadu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2867 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_expandloadu_ps": "_mm256_maskz_expandloadu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2934 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fmadd_pd": "_mm256_maskz_fmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1330 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fmadd_ps": "_mm256_maskz_fmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1474 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fmaddsub_pd": "_mm256_maskz_fmaddsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1595 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fmaddsub_ps": "_mm256_maskz_fmaddsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1688 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fmsub_pd": "_mm256_maskz_fmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1348 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fmsub_ps": "_mm256_maskz_fmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1492 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fmsubadd_pd": "_mm256_maskz_fmsubadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1614 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fmsubadd_ps": "_mm256_maskz_fmsubadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1706 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fnmadd_pd": "_mm256_maskz_fnmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1366 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fnmadd_ps": "_mm256_maskz_fnmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1510 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fnmsub_pd": "_mm256_maskz_fnmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1375 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_fnmsub_ps": "_mm256_maskz_fnmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1519 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_getexp_pd": "_mm256_maskz_getexp_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3075 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_getexp_ps": "_mm256_maskz_getexp_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3121 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_load_epi32": "_mm256_maskz_load_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5813 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_load_epi64": "_mm256_maskz_load_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L5899 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_load_pd": "_mm256_maskz_load_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6107 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_load_ps": "_mm256_maskz_load_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6141 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_loadu_epi32": "_mm256_maskz_loadu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L6209 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-075.json

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
