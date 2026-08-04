# Node Description Batch 23 of 111

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

- "clang_include_avx512bwintrin_mm512_cmpneq_epu16_mask": "_mm512_cmpneq_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L335 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_cmpneq_epu8_mask": "_mm512_cmpneq_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L311 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_cvtepi16_epi8": "_mm512_cvtepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1376 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_cvtepi8_epi16": "_mm512_cvtepi8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1539 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_cvtepu8_epi16": "_mm512_cvtepu8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1565 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_cvtsepi16_epi8": "_mm512_cvtsepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1334 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_cvtusepi16_epi8": "_mm512_cvtusepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1355 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_maddubs_epi16": "_mm512_maddubs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1284 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_abs_epi16": "_mm512_mask_abs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L500 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_abs_epi8": "_mm512_mask_abs_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L476 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_add_epi16": "_mm512_mask_add_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L394 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_add_epi8": "_mm512_mask_add_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L352 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_adds_epi16": "_mm512_mask_adds_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L665 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_adds_epi8": "_mm512_mask_adds_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L637 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_adds_epu16": "_mm512_mask_adds_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L721 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_adds_epu8": "_mm512_mask_adds_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L693 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_avg_epu16": "_mm512_mask_avg_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L777 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_avg_epu8": "_mm512_mask_avg_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L749 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_blend_epi16": "_mm512_mask_blend_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L460 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_blend_epi8": "_mm512_mask_blend_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L452 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpeq_epi16_mask": "_mm512_mask_cmpeq_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L89 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpeq_epi8_mask": "_mm512_mask_cmpeq_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L65 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpeq_epu16_mask": "_mm512_mask_cmpeq_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L101 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpeq_epu8_mask": "_mm512_mask_cmpeq_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L77 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpge_epi16_mask": "_mm512_mask_cmpge_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L137 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpge_epi8_mask": "_mm512_mask_cmpge_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L113 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpge_epu16_mask": "_mm512_mask_cmpge_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L149 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpge_epu8_mask": "_mm512_mask_cmpge_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L125 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpgt_epi16_mask": "_mm512_mask_cmpgt_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L185 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpgt_epi8_mask": "_mm512_mask_cmpgt_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L161 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpgt_epu16_mask": "_mm512_mask_cmpgt_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L197 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpgt_epu8_mask": "_mm512_mask_cmpgt_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L173 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmple_epi16_mask": "_mm512_mask_cmple_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L233 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmple_epi8_mask": "_mm512_mask_cmple_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L209 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmple_epu16_mask": "_mm512_mask_cmple_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L245 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmple_epu8_mask": "_mm512_mask_cmple_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L221 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmplt_epi16_mask": "_mm512_mask_cmplt_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L281 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmplt_epi8_mask": "_mm512_mask_cmplt_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L257 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmplt_epu16_mask": "_mm512_mask_cmplt_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L293 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmplt_epu8_mask": "_mm512_mask_cmplt_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L269 | neighbors=[avx512bwintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-022.json

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
