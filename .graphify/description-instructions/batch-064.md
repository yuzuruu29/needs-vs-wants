# Node Description Batch 65 of 111

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

- "clang_include_avx512vlintrin_mm_maskz_cvtepi64_epi32": "_mm_maskz_cvtepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8373 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepi64_epi8": "_mm_maskz_cvtepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8315 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepi8_epi32": "_mm_maskz_cvtepi8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4561 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepi8_epi64": "_mm_maskz_cvtepi8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4595 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepu16_epi32": "_mm_maskz_cvtepu16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4834 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepu16_epi64": "_mm_maskz_cvtepu16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4868 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepu32_epi64": "_mm_maskz_cvtepu32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4800 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepu32_pd": "_mm_maskz_cvtepu32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2636 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepu32_ps": "_mm_maskz_cvtepu32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2682 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepu8_epi32": "_mm_maskz_cvtepu8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4732 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtepu8_epi64": "_mm_maskz_cvtepu8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4766 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtpd_epi32": "_mm_maskz_cvtpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2264 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtpd_epu32": "_mm_maskz_cvtpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2332 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtpd_ps": "_mm_maskz_cvtpd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2294 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtph_ps": "_mm_maskz_cvtph_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L9090 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtps_epi32": "_mm_maskz_cvtps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2370 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtps_epu32": "_mm_maskz_cvtps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2438 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtps_pd": "_mm_maskz_cvtps_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2400 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtps_ph": "_mm_maskz_cvtps_ph()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L9124 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtsepi32_epi16": "_mm_maskz_cvtsepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7671 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtsepi32_epi8": "_mm_maskz_cvtsepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7612 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtsepi64_epi16": "_mm_maskz_cvtsepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7846 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtsepi64_epi32": "_mm_maskz_cvtsepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7787 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtsepi64_epi8": "_mm_maskz_cvtsepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7729 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvttpd_epi32": "_mm_maskz_cvttpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2476 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvttpd_epu32": "_mm_maskz_cvttpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2514 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvttps_epi32": "_mm_maskz_cvttps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2552 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvttps_epu32": "_mm_maskz_cvttps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2590 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtusepi32_epi16": "_mm_maskz_cvtusepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7964 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtusepi32_epi8": "_mm_maskz_cvtusepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7905 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtusepi64_epi16": "_mm_maskz_cvtusepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8140 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtusepi64_epi32": "_mm_maskz_cvtusepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8082 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_cvtusepi64_epi8": "_mm_maskz_cvtusepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8023 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_expand_epi32": "_mm_maskz_expand_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3014 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_expand_epi64": "_mm_maskz_expand_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2819 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_expand_pd": "_mm_maskz_expand_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2789 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_expand_ps": "_mm_maskz_expand_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2984 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_expandloadu_epi32": "_mm_maskz_expandloadu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2951 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_expandloadu_epi64": "_mm_maskz_expandloadu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2884 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_maskz_expandloadu_pd": "_mm_maskz_expandloadu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2850 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-064.json

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
