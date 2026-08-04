# Node Description Batch 54 of 111

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

- "clang_include_avx512vlbwintrin_mm256_maskz_broadcastw_epi16": "_mm256_maskz_broadcastw_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3247 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_cvtepi16_epi8": "_mm256_maskz_cvtepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2035 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_cvtepi8_epi16": "_mm256_maskz_cvtepi8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2296 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_cvtepu8_epi16": "_mm256_maskz_cvtepu8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2331 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_cvtsepi16_epi8": "_mm256_maskz_cvtsepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1931 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_cvtusepi16_epi8": "_mm256_maskz_cvtusepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1973 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_loadu_epi16": "_mm256_maskz_loadu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2949 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_loadu_epi8": "_mm256_maskz_loadu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2983 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_madd_epi16": "_mm256_maskz_madd_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1888 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_maddubs_epi16": "_mm256_maskz_maddubs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1855 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_max_epi16": "_mm256_maskz_max_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1315 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_max_epi8": "_mm256_maskz_max_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1277 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_max_epu16": "_mm256_maskz_max_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1391 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_max_epu8": "_mm256_maskz_max_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1353 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_min_epi16": "_mm256_maskz_min_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1467 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_min_epi8": "_mm256_maskz_min_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1429 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_min_epu16": "_mm256_maskz_min_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1543 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_min_epu8": "_mm256_maskz_min_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1505 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_mov_epi16": "_mm256_maskz_mov_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2849 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_mov_epi8": "_mm256_maskz_mov_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2881 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_mulhi_epi16": "_mm256_maskz_mulhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2151 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_mulhi_epu16": "_mm256_maskz_mulhi_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2117 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_mulhrs_epi16": "_mm256_maskz_mulhrs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2083 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_mullo_epi16": "_mm256_maskz_mullo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L760 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_packs_epi16": "_mm256_maskz_packs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L936 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_packs_epi32": "_mm256_maskz_packs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L899 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_packus_epi16": "_mm256_maskz_packus_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1011 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_packus_epi32": "_mm256_maskz_packus_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L973 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_permutex2var_epi16": "_mm256_maskz_permutex2var_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1820 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_permutexvar_epi16": "_mm256_maskz_permutexvar_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3324 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_set1_epi16": "_mm256_maskz_set1_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3263 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_set1_epi8": "_mm256_maskz_set1_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2915 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_shuffle_epi8": "_mm256_maskz_shuffle_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1591 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_sll_epi16": "_mm256_maskz_sll_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2553 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_sllv_epi16": "_mm256_maskz_sllv_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2483 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_sra_epi16": "_mm256_maskz_sra_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2735 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_srav_epi16": "_mm256_maskz_srav_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2665 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_srl_epi16": "_mm256_maskz_srl_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2795 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_srlv_epi16": "_mm256_maskz_srlv_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2605 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_sub_epi16": "_mm256_maskz_sub_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L676 | neighbors=[avx512vlbwintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-053.json

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
