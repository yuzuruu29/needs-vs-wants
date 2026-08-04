# Node Description Batch 75 of 111

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

- "clang_include_avx512vlintrin_mm256_maskz_broadcast_f32x4": "_mm256_maskz_broadcast_f32x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7452 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_broadcast_i32x4": "_mm256_maskz_broadcast_i32x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7476 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_broadcastd_epi32": "_mm256_maskz_broadcastd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7557 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_broadcastq_epi64": "_mm256_maskz_broadcastq_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7589 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_broadcastsd_pd": "_mm256_maskz_broadcastsd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7493 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_broadcastss_ps": "_mm256_maskz_broadcastss_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7525 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_compress_epi32": "_mm256_maskz_compress_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2133 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_compress_epi64": "_mm256_maskz_compress_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2073 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_compress_pd": "_mm256_maskz_compress_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2043 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_compress_ps": "_mm256_maskz_compress_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2103 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi16_epi32": "_mm256_maskz_cvtepi16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4680 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi16_epi64": "_mm256_maskz_cvtepi16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4714 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi32_epi16": "_mm256_maskz_cvtepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8286 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi32_epi64": "_mm256_maskz_cvtepi32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4646 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi32_epi8": "_mm256_maskz_cvtepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8228 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi32_pd": "_mm256_maskz_cvtepi32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2219 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi32_ps": "_mm256_maskz_cvtepi32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2249 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi64_epi16": "_mm256_maskz_cvtepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8461 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi64_epi32": "_mm256_maskz_cvtepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8402 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi64_epi8": "_mm256_maskz_cvtepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8344 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi8_epi32": "_mm256_maskz_cvtepi8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4578 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepi8_epi64": "_mm256_maskz_cvtepi8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4612 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepu16_epi32": "_mm256_maskz_cvtepu16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4851 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepu16_epi64": "_mm256_maskz_cvtepu16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4885 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepu32_epi64": "_mm256_maskz_cvtepu32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4817 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepu32_pd": "_mm256_maskz_cvtepu32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2659 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepu32_ps": "_mm256_maskz_cvtepu32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2705 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepu8_epi32": "_mm256_maskz_cvtepu8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4749 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtepu8_epi64": "_mm256_maskz_cvtepu8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4783 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtpd_epi32": "_mm256_maskz_cvtpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2279 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtpd_epu32": "_mm256_maskz_cvtpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2355 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtpd_ps": "_mm256_maskz_cvtpd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2309 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtph_ps": "_mm256_maskz_cvtph_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L9107 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtps_epi32": "_mm256_maskz_cvtps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2385 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtps_epu32": "_mm256_maskz_cvtps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2461 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtps_pd": "_mm256_maskz_cvtps_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2415 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtps_ph": "_mm256_maskz_cvtps_ph()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L9150 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtsepi32_epi16": "_mm256_maskz_cvtsepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7700 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtsepi32_epi8": "_mm256_maskz_cvtsepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7641 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_maskz_cvtsepi64_epi16": "_mm256_maskz_cvtsepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7875 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-074.json

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
