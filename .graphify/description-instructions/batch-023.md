# Node Description Batch 24 of 111

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

- "clang_include_avx512bwintrin_mm512_mask_cmpneq_epi16_mask": "_mm512_mask_cmpneq_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L329 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpneq_epi8_mask": "_mm512_mask_cmpneq_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L305 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpneq_epu16_mask": "_mm512_mask_cmpneq_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L341 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cmpneq_epu8_mask": "_mm512_mask_cmpneq_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L317 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cvtepi16_epi8": "_mm512_mask_cvtepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1383 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cvtepi16_storeu_epi8": "_mm512_mask_cvtepi16_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1397 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cvtepi8_epi16": "_mm512_mask_cvtepi8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1548 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cvtepu8_epi16": "_mm512_mask_cvtepu8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1574 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cvtsepi16_epi8": "_mm512_mask_cvtsepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1341 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cvtsepi16_storeu_epi8": "_mm512_mask_cvtsepi16_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1403 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cvtusepi16_epi8": "_mm512_mask_cvtusepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1362 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_cvtusepi16_storeu_epi8": "_mm512_mask_cvtusepi16_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1409 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_loadu_epi16": "_mm512_mask_loadu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2133 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_loadu_epi8": "_mm512_mask_loadu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2150 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_madd_epi16": "_mm512_mask_madd_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1317 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_maddubs_epi16": "_mm512_mask_maddubs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1292 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_max_epi16": "_mm512_mask_max_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L842 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_max_epi8": "_mm512_mask_max_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L814 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_max_epu16": "_mm512_mask_max_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L898 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_max_epu8": "_mm512_mask_max_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L870 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_min_epi16": "_mm512_mask_min_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L954 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_min_epi8": "_mm512_mask_min_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L926 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_min_epu16": "_mm512_mask_min_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1010 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_min_epu8": "_mm512_mask_min_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L982 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_mov_epi16": "_mm512_mask_mov_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2070 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_mov_epi8": "_mm512_mask_mov_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2086 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_mulhi_epi16": "_mm512_mask_mulhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1237 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_mulhi_epu16": "_mm512_mask_mulhi_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1265 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_mulhrs_epi16": "_mm512_mask_mulhrs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1209 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_mullo_epi16": "_mm512_mask_mullo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L436 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_packs_epi16": "_mm512_mask_packs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L553 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_packs_epi32": "_mm512_mask_packs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L534 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_packus_epi16": "_mm512_mask_packus_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L609 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_packus_epi32": "_mm512_mask_packus_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L590 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_permutex2var_epi16": "_mm512_mask_permutex2var_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1179 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_permutexvar_epi16": "_mm512_mask_permutexvar_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2353 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_set1_epi16": "_mm512_mask_set1_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2293 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_set1_epi8": "_mm512_mask_set1_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2102 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_shuffle_epi8": "_mm512_mask_shuffle_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1029 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_mask_sll_epi16": "_mm512_mask_sll_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1746 | neighbors=[avx512bwintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-023.json

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
