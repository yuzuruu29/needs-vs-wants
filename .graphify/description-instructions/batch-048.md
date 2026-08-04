# Node Description Batch 49 of 111

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

- "clang_include_avx512vlbwintrin_mm_maskz_abs_epi16": "_mm_maskz_abs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L858 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_abs_epi8": "_mm_maskz_abs_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L826 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_add_epi16": "_mm_maskz_add_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L709 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_add_epi8": "_mm_maskz_add_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L692 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_adds_epi16": "_mm_maskz_adds_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1078 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_adds_epi8": "_mm_maskz_adds_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1040 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_adds_epu16": "_mm_maskz_adds_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1154 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_adds_epu8": "_mm_maskz_adds_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1116 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_avg_epu16": "_mm_maskz_avg_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1230 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_avg_epu8": "_mm_maskz_avg_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1192 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_broadcastb_epi8": "_mm_maskz_broadcastb_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3199 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_broadcastw_epi16": "_mm_maskz_broadcastw_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3231 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_cvtepi16_epi8": "_mm_maskz_cvtepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1995 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_cvtepi8_epi16": "_mm_maskz_cvtepi8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2279 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_cvtepu8_epi16": "_mm_maskz_cvtepu8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2314 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_cvtsepi16_epi8": "_mm_maskz_cvtsepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1910 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_cvtusepi16_epi8": "_mm_maskz_cvtusepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1952 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_loadu_epi16": "_mm_maskz_loadu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2932 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_loadu_epi8": "_mm_maskz_loadu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2966 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_madd_epi16": "_mm_maskz_madd_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1872 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_maddubs_epi16": "_mm_maskz_maddubs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1838 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_max_epi16": "_mm_maskz_max_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1296 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_max_epi8": "_mm_maskz_max_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1258 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_max_epu16": "_mm_maskz_max_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1372 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_max_epu8": "_mm_maskz_max_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1334 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_min_epi16": "_mm_maskz_min_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1448 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_min_epi8": "_mm_maskz_min_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1410 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_min_epu16": "_mm_maskz_min_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1524 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_min_epu8": "_mm_maskz_min_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1486 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_mov_epi16": "_mm_maskz_mov_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2833 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_mov_epi8": "_mm_maskz_mov_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2865 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_mulhi_epi16": "_mm_maskz_mulhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2134 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_mulhi_epu16": "_mm_maskz_mulhi_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2100 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_mulhrs_epi16": "_mm_maskz_mulhrs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2067 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_mullo_epi16": "_mm_maskz_mullo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L777 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_packs_epi16": "_mm_maskz_packs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L917 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_packs_epi32": "_mm_maskz_packs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L882 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_packus_epi16": "_mm_maskz_packus_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L992 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_packus_epi32": "_mm_maskz_packus_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L955 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_permutex2var_epi16": "_mm_maskz_permutex2var_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1791 | neighbors=[avx512vlbwintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-048.json

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
