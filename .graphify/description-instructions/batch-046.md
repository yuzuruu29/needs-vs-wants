# Node Description Batch 47 of 111

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

- "clang_include_avx512vlbwintrin_mm_mask_blend_epi16": "_mm_mask_blend_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L802 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_blend_epi8": "_mm_mask_blend_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L786 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_broadcastb_epi8": "_mm_mask_broadcastb_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3191 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_broadcastw_epi16": "_mm_mask_broadcastw_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3223 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpeq_epi16_mask": "_mm_mask_cmpeq_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L95 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpeq_epi8_mask": "_mm_mask_cmpeq_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L47 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpeq_epu16_mask": "_mm_mask_cmpeq_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L107 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpeq_epu8_mask": "_mm_mask_cmpeq_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L59 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpge_epi16_mask": "_mm_mask_cmpge_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L191 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpge_epi8_mask": "_mm_mask_cmpge_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L143 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpge_epu16_mask": "_mm_mask_cmpge_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L203 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpge_epu8_mask": "_mm_mask_cmpge_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L155 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpgt_epi16_mask": "_mm_mask_cmpgt_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L287 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpgt_epi8_mask": "_mm_mask_cmpgt_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L239 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpgt_epu16_mask": "_mm_mask_cmpgt_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L299 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpgt_epu8_mask": "_mm_mask_cmpgt_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L251 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmple_epi16_mask": "_mm_mask_cmple_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L383 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmple_epi8_mask": "_mm_mask_cmple_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L335 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmple_epu16_mask": "_mm_mask_cmple_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L395 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmple_epu8_mask": "_mm_mask_cmple_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L347 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmplt_epi16_mask": "_mm_mask_cmplt_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L479 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmplt_epi8_mask": "_mm_mask_cmplt_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L431 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmplt_epu16_mask": "_mm_mask_cmplt_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L491 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmplt_epu8_mask": "_mm_mask_cmplt_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L443 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpneq_epi16_mask": "_mm_mask_cmpneq_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L575 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpneq_epi8_mask": "_mm_mask_cmpneq_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L527 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpneq_epu16_mask": "_mm_mask_cmpneq_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L587 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cmpneq_epu8_mask": "_mm_mask_cmpneq_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L539 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cvtepi16_epi8": "_mm_mask_cvtepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1988 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cvtepi16_storeu_epi8": "_mm_mask_cvtepi16_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2002 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cvtepi8_epi16": "_mm_mask_cvtepi8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2271 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cvtepu8_epi16": "_mm_mask_cvtepu8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2306 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cvtsepi16_epi8": "_mm_mask_cvtsepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1903 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cvtsepi16_storeu_epi8": "_mm_mask_cvtsepi16_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2009 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cvtusepi16_epi8": "_mm_mask_cvtusepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1945 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_cvtusepi16_storeu_epi8": "_mm_mask_cvtusepi16_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2015 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_loadu_epi16": "_mm_mask_loadu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2924 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_loadu_epi8": "_mm_mask_loadu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2958 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_madd_epi16": "_mm_mask_madd_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1863 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_maddubs_epi16": "_mm_mask_maddubs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1830 | neighbors=[avx512vlbwintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-046.json

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
