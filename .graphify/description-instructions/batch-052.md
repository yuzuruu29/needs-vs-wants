# Node Description Batch 53 of 111

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

- "clang_include_avx512vlbwintrin_mm256_mask_packus_epi32": "_mm256_mask_packus_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L982 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_permutex2var_epi16": "_mm256_mask_permutex2var_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1810 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_permutexvar_epi16": "_mm256_mask_permutexvar_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3334 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_set1_epi16": "_mm256_mask_set1_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3255 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_set1_epi8": "_mm256_mask_set1_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2907 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_shuffle_epi8": "_mm256_mask_shuffle_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1581 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_sll_epi16": "_mm256_mask_sll_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2543 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_sllv_epi16": "_mm256_mask_sllv_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2473 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_sra_epi16": "_mm256_mask_sra_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2725 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_srav_epi16": "_mm256_mask_srav_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2655 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_srl_epi16": "_mm256_mask_srl_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2785 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_srlv_epi16": "_mm256_mask_srlv_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2595 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_storeu_epi16": "_mm256_mask_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2999 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_storeu_epi8": "_mm256_mask_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3015 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_sub_epi16": "_mm256_mask_sub_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L668 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_sub_epi8": "_mm256_mask_sub_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L651 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_subs_epi16": "_mm256_mask_subs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1657 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_subs_epi8": "_mm256_mask_subs_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1619 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_subs_epu16": "_mm256_mask_subs_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1733 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_subs_epu8": "_mm256_mask_subs_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1695 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_test_epi16_mask": "_mm256_mask_test_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3076 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_test_epi8_mask": "_mm256_mask_test_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3046 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_testn_epi16_mask": "_mm256_mask_testn_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3136 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_testn_epi8_mask": "_mm256_mask_testn_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3106 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_unpackhi_epi16": "_mm256_mask_unpackhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2201 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_unpackhi_epi8": "_mm256_mask_unpackhi_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2173 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_unpacklo_epi16": "_mm256_mask_unpacklo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2257 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask_unpacklo_epi8": "_mm256_mask_unpacklo_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2229 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_mask2_permutex2var_epi16": "_mm256_mask2_permutex2var_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1762 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_abs_epi16": "_mm256_maskz_abs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L874 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_abs_epi8": "_mm256_maskz_abs_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L842 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_add_epi16": "_mm256_maskz_add_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L642 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_add_epi8": "_mm256_maskz_add_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L625 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_adds_epi16": "_mm256_maskz_adds_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1097 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_adds_epi8": "_mm256_maskz_adds_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1059 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_adds_epu16": "_mm256_maskz_adds_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1173 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_adds_epu8": "_mm256_maskz_adds_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1135 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_avg_epu16": "_mm256_maskz_avg_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1249 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_avg_epu8": "_mm256_maskz_avg_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1211 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_broadcastb_epi8": "_mm256_maskz_broadcastb_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3215 | neighbors=[avx512vlbwintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-052.json

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
