# Node Description Batch 52 of 111

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

- "clang_include_avx512vlbwintrin_mm256_mask_cmple_epi8_mask": "_mm256_mask_cmple_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L359 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmple_epu16_mask": "_mm256_mask_cmple_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L419 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmple_epu8_mask": "_mm256_mask_cmple_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L371 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmplt_epi16_mask": "_mm256_mask_cmplt_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L503 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmplt_epi8_mask": "_mm256_mask_cmplt_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L455 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmplt_epu16_mask": "_mm256_mask_cmplt_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L515 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmplt_epu8_mask": "_mm256_mask_cmplt_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L467 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpneq_epi16_mask": "_mm256_mask_cmpneq_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L599 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpneq_epi8_mask": "_mm256_mask_cmpneq_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L551 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpneq_epu16_mask": "_mm256_mask_cmpneq_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L611 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cmpneq_epu8_mask": "_mm256_mask_cmpneq_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L563 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cvtepi16_epi8": "_mm256_mask_cvtepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2028 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cvtepi16_storeu_epi8": "_mm256_mask_cvtepi16_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2042 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cvtepi8_epi16": "_mm256_mask_cvtepi8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2288 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cvtepu8_epi16": "_mm256_mask_cvtepu8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2323 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cvtsepi16_epi8": "_mm256_mask_cvtsepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1924 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cvtsepi16_storeu_epi8": "_mm256_mask_cvtsepi16_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2048 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cvtusepi16_epi8": "_mm256_mask_cvtusepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1966 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_cvtusepi16_storeu_epi8": "_mm256_mask_cvtusepi16_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2054 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_loadu_epi16": "_mm256_mask_loadu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2941 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_loadu_epi8": "_mm256_mask_loadu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2975 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_madd_epi16": "_mm256_mask_madd_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1880 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_maddubs_epi16": "_mm256_mask_maddubs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1846 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_max_epi16": "_mm256_mask_max_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1324 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_max_epi8": "_mm256_mask_max_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1286 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_max_epu16": "_mm256_mask_max_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1400 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_max_epu8": "_mm256_mask_max_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1362 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_min_epi16": "_mm256_mask_min_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1476 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_min_epi8": "_mm256_mask_min_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1438 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_min_epu16": "_mm256_mask_min_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1552 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_min_epu8": "_mm256_mask_min_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1514 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_mov_epi16": "_mm256_mask_mov_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2841 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_mov_epi8": "_mm256_mask_mov_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2873 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_mulhi_epi16": "_mm256_mask_mulhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2142 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_mulhi_epu16": "_mm256_mask_mulhi_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2108 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_mulhrs_epi16": "_mm256_mask_mulhrs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2075 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_mullo_epi16": "_mm256_mask_mullo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L752 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_packs_epi16": "_mm256_mask_packs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L945 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_packs_epi32": "_mm256_mask_packs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L908 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_packus_epi16": "_mm256_mask_packus_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1020 | neighbors=[avx512vlbwintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-051.json

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
