# Node Description Batch 59 of 111

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

- "clang_include_avx512vlintrin_mm_cmple_epu32_mask": "_mm_cmple_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L342 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmple_epu64_mask": "_mm_cmple_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L390 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmplt_epi32_mask": "_mm_cmplt_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L426 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmplt_epi64_mask": "_mm_cmplt_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L474 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmplt_epu32_mask": "_mm_cmplt_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L438 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmplt_epu64_mask": "_mm_cmplt_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L486 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpneq_epi32_mask": "_mm_cmpneq_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L522 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpneq_epi64_mask": "_mm_cmpneq_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L570 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpneq_epu32_mask": "_mm_cmpneq_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L534 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cmpneq_epu64_mask": "_mm_cmpneq_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L582 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtepi32_epi16": "_mm_cvtepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8242 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtepi64_epi16": "_mm_cvtepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8416 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtepi64_epi32": "_mm_cvtepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8358 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtsepi32_epi16": "_mm_cvtsepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7655 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtsepi32_epi8": "_mm_cvtsepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7597 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtsepi64_epi16": "_mm_cvtsepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7831 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtsepi64_epi32": "_mm_cvtsepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7772 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtsepi64_epi8": "_mm_cvtsepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7714 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtusepi32_epi16": "_mm_cvtusepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7949 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtusepi32_epi8": "_mm_cvtusepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7889 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtusepi64_epi16": "_mm_cvtusepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8125 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtusepi64_epi32": "_mm_cvtusepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8067 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_cvtusepi64_epi8": "_mm_cvtusepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8007 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_abs_epi32": "_mm_mask_abs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3336 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_abs_epi64": "_mm_mask_abs_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3374 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_add_epi32": "_mm_mask_add_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L698 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_add_epi64": "_mm_mask_add_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L718 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_blend_epi32": "_mm_mask_blend_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1965 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_blend_epi64": "_mm_mask_blend_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2007 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_broadcastd_epi32": "_mm_mask_broadcastd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7533 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_broadcastq_epi64": "_mm_mask_broadcastq_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7565 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_broadcastss_ps": "_mm_mask_broadcastss_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7501 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpeq_epi32_mask": "_mm_mask_cmpeq_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L47 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpeq_epi64_mask": "_mm_mask_cmpeq_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L95 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpeq_epu32_mask": "_mm_mask_cmpeq_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L59 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpeq_epu64_mask": "_mm_mask_cmpeq_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L107 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpge_epi32_mask": "_mm_mask_cmpge_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L144 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpge_epi64_mask": "_mm_mask_cmpge_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L192 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpge_epu32_mask": "_mm_mask_cmpge_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L156 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpge_epu64_mask": "_mm_mask_cmpge_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L204 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-058.json

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
