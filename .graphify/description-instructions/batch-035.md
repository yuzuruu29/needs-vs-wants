# Node Description Batch 36 of 111

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

- "clang_include_avx512fintrin_mm512_mask_cvtpd_ps": "_mm512_mask_cvtpd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3783 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtph_ps": "_mm512_mask_cvtph_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3857 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtps_epi32": "_mm512_mask_cvtps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3983 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtps_epu32": "_mm512_mask_cvtps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4071 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtps_pd": "_mm512_mask_cvtps_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9228 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtsepi32_epi16": "_mm512_mask_cvtsepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7434 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtsepi32_epi8": "_mm512_mask_cvtsepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7405 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtsepi32_storeu_epi16": "_mm512_mask_cvtsepi32_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7449 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtsepi32_storeu_epi8": "_mm512_mask_cvtsepi32_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7420 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtsepi64_epi16": "_mm512_mask_cvtsepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7522 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtsepi64_epi32": "_mm512_mask_cvtsepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7493 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtsepi64_epi8": "_mm512_mask_cvtsepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7463 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtsepi64_storeu_epi16": "_mm512_mask_cvtsepi64_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7537 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtsepi64_storeu_epi32": "_mm512_mask_cvtsepi64_storeu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7508 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtsepi64_storeu_epi8": "_mm512_mask_cvtsepi64_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7478 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvttpd_epi32": "_mm512_mask_cvttpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3899 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvttpd_epu32": "_mm512_mask_cvttpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6733 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvttps_epi32": "_mm512_mask_cvttps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3941 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvttps_epu32": "_mm512_mask_cvttps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3607 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtusepi32_epi16": "_mm512_mask_cvtusepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7581 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtusepi32_epi8": "_mm512_mask_cvtusepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7551 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtusepi32_storeu_epi16": "_mm512_mask_cvtusepi32_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7597 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtusepi32_storeu_epi8": "_mm512_mask_cvtusepi32_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7567 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtusepi64_epi16": "_mm512_mask_cvtusepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7670 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtusepi64_epi32": "_mm512_mask_cvtusepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7641 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtusepi64_epi8": "_mm512_mask_cvtusepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7611 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtusepi64_storeu_epi16": "_mm512_mask_cvtusepi64_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7685 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtusepi64_storeu_epi32": "_mm512_mask_cvtusepi64_storeu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7656 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtusepi64_storeu_epi8": "_mm512_mask_cvtusepi64_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7627 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_div_pd": "_mm512_mask_div_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2418 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_div_ps": "_mm512_mask_div_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2443 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_expand_epi32": "_mm512_mask_expand_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9187 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_expand_epi64": "_mm512_mask_expand_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9091 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_expand_pd": "_mm512_mask_expand_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9075 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_expand_ps": "_mm512_mask_expand_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9171 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_expandloadu_epi32": "_mm512_mask_expandloadu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9155 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_expandloadu_epi64": "_mm512_mask_expandloadu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9123 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_expandloadu_pd": "_mm512_mask_expandloadu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9107 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_expandloadu_ps": "_mm512_mask_expandloadu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9139 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_floor_pd": "_mm512_mask_floor_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1810 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-035.json

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
