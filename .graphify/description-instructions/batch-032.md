# Node Description Batch 33 of 111

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

- "clang_include_avx512fintrin_mm512_cvtepu32_epi64": "_mm512_cvtepu32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5103 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepu32_pd": "_mm512_cvtepu32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3734 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepu32_ps": "_mm512_cvtepu32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3655 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepu8_epi32": "_mm512_cvtepu8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5051 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepu8_epi64": "_mm512_cvtepu8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5077 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtpd_epi32": "_mm512_cvtpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4017 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtpd_epu32": "_mm512_cvtpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4105 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtph_ps": "_mm512_cvtph_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3847 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtps_epi32": "_mm512_cvtps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3974 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtps_epu32": "_mm512_cvtps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4061 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtsepi32_epi16": "_mm512_cvtsepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7426 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtsepi32_epi8": "_mm512_cvtsepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7397 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtsepi64_epi16": "_mm512_cvtsepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7514 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtsepi64_epi32": "_mm512_cvtsepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7484 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtsepi64_epi8": "_mm512_cvtsepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7455 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvttpd_epi32": "_mm512_cvttpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3890 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvttpd_epu32": "_mm512_cvttpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6723 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvttps_epi32": "_mm512_cvttps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3932 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvttps_epu32": "_mm512_cvttps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3597 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtusepi32_epi16": "_mm512_cvtusepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7573 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtusepi32_epi8": "_mm512_cvtusepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7543 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtusepi64_epi16": "_mm512_cvtusepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7662 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtusepi64_epi32": "_mm512_cvtusepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7633 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtusepi64_epi8": "_mm512_cvtusepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7603 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_div_pd": "_mm512_div_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2412 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_div_ps": "_mm512_div_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2437 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_floor_pd": "_mm512_floor_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1801 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_floor_ps": "_mm512_floor_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1783 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fmadd_pd": "_mm512_fmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2644 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fmadd_ps": "_mm512_fmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2848 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fmaddsub_pd": "_mm512_fmaddsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3017 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fmaddsub_ps": "_mm512_fmaddsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3136 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fmsub_pd": "_mm512_fmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2684 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fmsub_ps": "_mm512_fmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2888 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fmsubadd_pd": "_mm512_fmsubadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3057 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fmsubadd_ps": "_mm512_fmsubadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3176 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fnmadd_pd": "_mm512_fnmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2714 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fnmadd_ps": "_mm512_fnmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2918 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fnmsub_pd": "_mm512_fnmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2744 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_fnmsub_ps": "_mm512_fnmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2948 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-032.json

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
