# Node Description Batch 41 of 111

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

- "clang_include_avx512fintrin_mm512_maskz_cvttps_epi32": "_mm512_maskz_cvttps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3950 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvttps_epu32": "_mm512_maskz_cvttps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3616 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtusepi32_epi16": "_mm512_maskz_cvtusepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7589 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtusepi32_epi8": "_mm512_maskz_cvtusepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7559 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtusepi64_epi16": "_mm512_maskz_cvtusepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7677 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtusepi64_epi32": "_mm512_maskz_cvtusepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7648 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtusepi64_epi8": "_mm512_maskz_cvtusepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7619 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_div_pd": "_mm512_maskz_div_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2427 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_div_ps": "_mm512_maskz_div_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2452 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_expand_epi32": "_mm512_maskz_expand_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9195 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_expand_epi64": "_mm512_maskz_expand_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9099 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_expand_pd": "_mm512_maskz_expand_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9083 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_expand_ps": "_mm512_maskz_expand_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9179 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_expandloadu_epi32": "_mm512_maskz_expandloadu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9163 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_expandloadu_epi64": "_mm512_maskz_expandloadu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9131 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_expandloadu_pd": "_mm512_maskz_expandloadu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9115 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_expandloadu_ps": "_mm512_maskz_expandloadu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9147 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fmadd_pd": "_mm512_maskz_fmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2674 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fmadd_ps": "_mm512_maskz_fmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2878 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fmaddsub_pd": "_mm512_maskz_fmaddsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3047 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fmaddsub_ps": "_mm512_maskz_fmaddsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3166 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fmsub_pd": "_mm512_maskz_fmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2704 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fmsub_ps": "_mm512_maskz_fmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2908 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fmsubadd_pd": "_mm512_maskz_fmsubadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3077 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fmsubadd_ps": "_mm512_maskz_fmsubadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3196 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fnmadd_pd": "_mm512_maskz_fnmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2734 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fnmadd_ps": "_mm512_maskz_fnmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2938 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fnmsub_pd": "_mm512_maskz_fnmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2754 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_fnmsub_ps": "_mm512_maskz_fnmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2958 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_getexp_pd": "_mm512_maskz_getexp_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8049 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_getexp_ps": "_mm512_maskz_getexp_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8091 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_load_epi32": "_mm512_maskz_load_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5466 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_load_epi64": "_mm512_maskz_load_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5522 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_load_pd": "_mm512_maskz_load_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4505 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_load_ps": "_mm512_maskz_load_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4479 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_loadu_epi32": "_mm512_maskz_loadu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4384 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_loadu_epi64": "_mm512_maskz_loadu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4401 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_loadu_pd": "_mm512_maskz_loadu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4435 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_loadu_ps": "_mm512_maskz_loadu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4418 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_max_epi32": "_mm512_maskz_max_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1024 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-040.json

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
