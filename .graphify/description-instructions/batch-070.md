# Node Description Batch 71 of 111

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

- "clang_include_avx512vlintrin_mm256_mask_cvtsepi32_epi8": "_mm256_mask_cvtsepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7634 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtsepi32_storeu_epi16": "_mm256_mask_cvtsepi32_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7708 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtsepi32_storeu_epi8": "_mm256_mask_cvtsepi32_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7649 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtsepi64_epi16": "_mm256_mask_cvtsepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7868 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtsepi64_epi32": "_mm256_mask_cvtsepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7809 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtsepi64_epi8": "_mm256_mask_cvtsepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7751 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtsepi64_storeu_epi16": "_mm256_mask_cvtsepi64_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7883 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtsepi64_storeu_epi32": "_mm256_mask_cvtsepi64_storeu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7825 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtsepi64_storeu_epi8": "_mm256_mask_cvtsepi64_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7766 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvttpd_epi32": "_mm256_mask_cvttpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2484 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvttpd_epu32": "_mm256_mask_cvttpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2530 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvttps_epi32": "_mm256_mask_cvttps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2560 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvttps_epu32": "_mm256_mask_cvttps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2606 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtusepi32_epi16": "_mm256_mask_cvtusepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7986 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtusepi32_epi8": "_mm256_mask_cvtusepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7927 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtusepi32_storeu_epi16": "_mm256_mask_cvtusepi32_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8001 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtusepi32_storeu_epi8": "_mm256_mask_cvtusepi32_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7943 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtusepi64_epi16": "_mm256_mask_cvtusepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8162 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtusepi64_epi32": "_mm256_mask_cvtusepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8104 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtusepi64_epi8": "_mm256_mask_cvtusepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8045 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtusepi64_storeu_epi16": "_mm256_mask_cvtusepi64_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8177 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtusepi64_storeu_epi32": "_mm256_mask_cvtusepi64_storeu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8119 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_cvtusepi64_storeu_epi8": "_mm256_mask_cvtusepi64_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8061 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_div_pd": "_mm256_mask_div_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2730 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_div_ps": "_mm256_mask_div_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2765 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_expand_epi32": "_mm256_mask_expand_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3022 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_expand_epi64": "_mm256_mask_expand_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2827 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_expand_pd": "_mm256_mask_expand_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2797 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_expand_ps": "_mm256_mask_expand_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2992 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_expandloadu_epi32": "_mm256_mask_expandloadu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2959 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_expandloadu_epi64": "_mm256_mask_expandloadu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2893 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_expandloadu_pd": "_mm256_mask_expandloadu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2859 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_expandloadu_ps": "_mm256_mask_expandloadu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2927 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_fmadd_pd": "_mm256_mask_fmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1312 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_fmadd_ps": "_mm256_mask_fmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1456 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_fmaddsub_pd": "_mm256_mask_fmaddsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1576 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_fmaddsub_ps": "_mm256_mask_fmaddsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1669 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_fmsub_pd": "_mm256_mask_fmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1339 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_fmsub_ps": "_mm256_mask_fmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1483 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_fmsubadd_pd": "_mm256_mask_fmsubadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1605 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-070.json

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
