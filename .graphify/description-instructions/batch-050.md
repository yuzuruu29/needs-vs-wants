# Node Description Batch 51 of 111

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

- "clang_include_avx512vlbwintrin_mm256_cmple_epu16_mask": "_mm256_cmple_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L413 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmple_epu8_mask": "_mm256_cmple_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L365 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmplt_epi16_mask": "_mm256_cmplt_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L497 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmplt_epi8_mask": "_mm256_cmplt_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L449 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmplt_epu16_mask": "_mm256_cmplt_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L509 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmplt_epu8_mask": "_mm256_cmplt_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L461 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpneq_epi16_mask": "_mm256_cmpneq_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L593 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpneq_epi8_mask": "_mm256_cmpneq_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L545 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpneq_epu16_mask": "_mm256_cmpneq_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L605 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpneq_epu8_mask": "_mm256_cmpneq_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L557 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cvtepi16_epi8": "_mm256_cvtepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2021 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cvtsepi16_epi8": "_mm256_cvtsepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1917 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cvtusepi16_epi8": "_mm256_cvtusepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1959 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_abs_epi16": "_mm256_mask_abs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L866 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_abs_epi8": "_mm256_mask_abs_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L834 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_add_epi16": "_mm256_mask_add_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L634 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_add_epi8": "_mm256_mask_add_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L617 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_adds_epi16": "_mm256_mask_adds_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1087 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_adds_epi8": "_mm256_mask_adds_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1049 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_adds_epu16": "_mm256_mask_adds_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1163 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_adds_epu8": "_mm256_mask_adds_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1125 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_avg_epu16": "_mm256_mask_avg_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1239 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_avg_epu8": "_mm256_mask_avg_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1201 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_blend_epi16": "_mm256_mask_blend_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L810 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_blend_epi8": "_mm256_mask_blend_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L794 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_broadcastb_epi8": "_mm256_mask_broadcastb_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3207 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_broadcastw_epi16": "_mm256_mask_broadcastw_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3239 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpeq_epi16_mask": "_mm256_mask_cmpeq_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L119 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpeq_epi8_mask": "_mm256_mask_cmpeq_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L71 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpeq_epu16_mask": "_mm256_mask_cmpeq_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L131 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpeq_epu8_mask": "_mm256_mask_cmpeq_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L83 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpge_epi16_mask": "_mm256_mask_cmpge_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L215 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpge_epi8_mask": "_mm256_mask_cmpge_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L167 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpge_epu16_mask": "_mm256_mask_cmpge_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L227 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpge_epu8_mask": "_mm256_mask_cmpge_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L179 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpgt_epi16_mask": "_mm256_mask_cmpgt_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L311 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpgt_epi8_mask": "_mm256_mask_cmpgt_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L263 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpgt_epu16_mask": "_mm256_mask_cmpgt_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L323 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpgt_epu8_mask": "_mm256_mask_cmpgt_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L275 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmple_epi16_mask": "_mm256_mask_cmple_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L407 | neighbors=[avx512vlbwintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-050.json

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
