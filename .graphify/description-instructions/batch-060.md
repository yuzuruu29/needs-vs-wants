# Node Description Batch 61 of 111

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

- "clang_include_avx512vlintrin_mm_mask_cvtepi8_epi64": "_mm_mask_cvtepi8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4587 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepu16_epi32": "_mm_mask_cvtepu16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4826 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepu16_epi64": "_mm_mask_cvtepu16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4860 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepu32_epi64": "_mm_mask_cvtepu32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4792 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepu32_pd": "_mm_mask_cvtepu32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2629 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepu32_ps": "_mm_mask_cvtepu32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2675 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepu8_epi32": "_mm_mask_cvtepu8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4724 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepu8_epi64": "_mm_mask_cvtepu8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4758 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtpd_epi32": "_mm_mask_cvtpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2257 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtpd_epu32": "_mm_mask_cvtpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2325 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtps_epi32": "_mm_mask_cvtps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2363 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtps_epu32": "_mm_mask_cvtps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2431 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtsepi32_epi16": "_mm_mask_cvtsepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7663 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtsepi32_epi8": "_mm_mask_cvtsepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7605 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtsepi32_storeu_epi16": "_mm_mask_cvtsepi32_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7679 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtsepi32_storeu_epi8": "_mm_mask_cvtsepi32_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7620 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtsepi64_epi16": "_mm_mask_cvtsepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7839 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtsepi64_epi32": "_mm_mask_cvtsepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7780 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtsepi64_epi8": "_mm_mask_cvtsepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7722 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtsepi64_storeu_epi16": "_mm_mask_cvtsepi64_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7854 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtsepi64_storeu_epi32": "_mm_mask_cvtsepi64_storeu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7795 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtsepi64_storeu_epi8": "_mm_mask_cvtsepi64_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7737 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvttpd_epi32": "_mm_mask_cvttpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2469 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvttpd_epu32": "_mm_mask_cvttpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2507 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvttps_epi32": "_mm_mask_cvttps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2545 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvttps_epu32": "_mm_mask_cvttps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2583 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtusepi32_epi16": "_mm_mask_cvtusepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7957 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtusepi32_epi8": "_mm_mask_cvtusepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7897 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtusepi32_storeu_epi16": "_mm_mask_cvtusepi32_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7972 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtusepi32_storeu_epi8": "_mm_mask_cvtusepi32_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7913 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtusepi64_epi16": "_mm_mask_cvtusepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8133 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtusepi64_epi32": "_mm_mask_cvtusepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8075 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtusepi64_epi8": "_mm_mask_cvtusepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8015 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtusepi64_storeu_epi16": "_mm_mask_cvtusepi64_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8148 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtusepi64_storeu_epi32": "_mm_mask_cvtusepi64_storeu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8090 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtusepi64_storeu_epi8": "_mm_mask_cvtusepi64_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8031 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_expand_epi32": "_mm_mask_expand_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3007 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_expand_epi64": "_mm_mask_expand_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2812 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_expand_pd": "_mm_mask_expand_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2782 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_expand_ps": "_mm_mask_expand_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2977 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-060.json

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
