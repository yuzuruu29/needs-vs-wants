# Node Description Batch 70 of 111

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

- "clang_include_avx512vlintrin_mm256_mask_compress_epi64": "_mm256_mask_compress_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2066 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_compress_pd": "_mm256_mask_compress_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2036 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_compress_ps": "_mm256_mask_compress_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2096 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_compressstoreu_epi32": "_mm256_mask_compressstoreu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2190 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_compressstoreu_epi64": "_mm256_mask_compressstoreu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2162 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_compressstoreu_pd": "_mm256_mask_compressstoreu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2148 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_compressstoreu_ps": "_mm256_mask_compressstoreu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2176 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi16_epi32": "_mm256_mask_cvtepi16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4672 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi16_epi64": "_mm256_mask_cvtepi16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4706 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi32_epi16": "_mm256_mask_cvtepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8279 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi32_epi64": "_mm256_mask_cvtepi32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4638 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi32_epi8": "_mm256_mask_cvtepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8221 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi32_pd": "_mm256_mask_cvtepi32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2212 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi32_ps": "_mm256_mask_cvtepi32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2242 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi32_storeu_epi16": "_mm256_mask_cvtepi32_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8294 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi32_storeu_epi8": "_mm256_mask_cvtepi32_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8236 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi64_epi16": "_mm256_mask_cvtepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8454 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi64_epi32": "_mm256_mask_cvtepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8395 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi64_epi8": "_mm256_mask_cvtepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8337 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi64_storeu_epi16": "_mm256_mask_cvtepi64_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8469 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi64_storeu_epi32": "_mm256_mask_cvtepi64_storeu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8410 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi64_storeu_epi8": "_mm256_mask_cvtepi64_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8352 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi8_epi32": "_mm256_mask_cvtepi8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4570 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepi8_epi64": "_mm256_mask_cvtepi8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4604 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepu16_epi32": "_mm256_mask_cvtepu16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4843 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepu16_epi64": "_mm256_mask_cvtepu16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4877 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepu32_epi64": "_mm256_mask_cvtepu32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4809 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepu32_pd": "_mm256_mask_cvtepu32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2652 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepu32_ps": "_mm256_mask_cvtepu32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2698 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepu8_epi32": "_mm256_mask_cvtepu8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4741 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtepu8_epi64": "_mm256_mask_cvtepu8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4775 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtpd_epi32": "_mm256_mask_cvtpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2272 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtpd_epu32": "_mm256_mask_cvtpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2348 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtpd_ps": "_mm256_mask_cvtpd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2302 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtph_ps": "_mm256_mask_cvtph_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L9099 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtps_epi32": "_mm256_mask_cvtps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2378 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtps_epu32": "_mm256_mask_cvtps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2454 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtps_pd": "_mm256_mask_cvtps_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2408 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtps_ph": "_mm256_mask_cvtps_ph()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L9142 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtsepi32_epi16": "_mm256_mask_cvtsepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7693 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-069.json

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
