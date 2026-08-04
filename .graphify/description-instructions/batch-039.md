# Node Description Batch 40 of 111

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

- "clang_include_avx512fintrin_mm512_maskz_broadcast_f64x4": "_mm512_maskz_broadcast_f64x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7304 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_broadcast_i32x4": "_mm512_maskz_broadcast_i32x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7330 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_broadcast_i64x4": "_mm512_maskz_broadcast_i64x4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7356 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_compress_epi32": "_mm512_maskz_compress_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8946 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_compress_epi64": "_mm512_maskz_compress_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8912 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_compress_pd": "_mm512_maskz_compress_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8895 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_compress_ps": "_mm512_maskz_compress_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8929 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi16_epi32": "_mm512_maskz_cvtepi16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5016 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi16_epi64": "_mm512_maskz_cvtepi16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5042 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi32_epi16": "_mm512_maskz_cvtepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7735 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi32_epi64": "_mm512_maskz_cvtepi32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4990 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi32_epi8": "_mm512_maskz_cvtepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7706 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi32_pd": "_mm512_maskz_cvtepi32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3699 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi32_ps": "_mm512_maskz_cvtepi32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3725 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi64_epi16": "_mm512_maskz_cvtepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7822 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi64_epi32": "_mm512_maskz_cvtepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7793 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi64_epi8": "_mm512_maskz_cvtepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7764 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi8_epi32": "_mm512_maskz_cvtepi8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4938 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepi8_epi64": "_mm512_maskz_cvtepi8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4964 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepu16_epi32": "_mm512_maskz_cvtepu16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5146 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepu16_epi64": "_mm512_maskz_cvtepu16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5172 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepu32_epi64": "_mm512_maskz_cvtepu32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5120 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepu32_pd": "_mm512_maskz_cvtepu32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3751 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepu32_ps": "_mm512_maskz_cvtepu32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3673 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepu8_epi32": "_mm512_maskz_cvtepu8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5068 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtepu8_epi64": "_mm512_maskz_cvtepu8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5094 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtpd_epi32": "_mm512_maskz_cvtpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4036 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtpd_epu32": "_mm512_maskz_cvtpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4124 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtpd_ps": "_mm512_maskz_cvtpd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3792 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtph_ps": "_mm512_maskz_cvtph_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3866 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtps_epi32": "_mm512_maskz_cvtps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3992 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtps_epu32": "_mm512_maskz_cvtps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4080 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtps_pd": "_mm512_maskz_cvtps_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9237 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtsepi32_epi16": "_mm512_maskz_cvtsepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7441 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtsepi32_epi8": "_mm512_maskz_cvtsepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7412 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtsepi64_epi16": "_mm512_maskz_cvtsepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7529 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtsepi64_epi32": "_mm512_maskz_cvtsepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7500 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvtsepi64_epi8": "_mm512_maskz_cvtsepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7470 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvttpd_epi32": "_mm512_maskz_cvttpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3908 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_cvttpd_epu32": "_mm512_maskz_cvttpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6742 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-039.json

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
