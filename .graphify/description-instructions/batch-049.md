# Node Description Batch 50 of 111

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

- "clang_include_avx512vlbwintrin_mm_maskz_permutexvar_epi16": "_mm_maskz_permutexvar_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3296 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_set1_epi16": "_mm_maskz_set1_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3279 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_set1_epi8": "_mm_maskz_set1_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2898 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_shuffle_epi8": "_mm_maskz_shuffle_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1572 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_sll_epi16": "_mm_maskz_sll_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2533 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_sllv_epi16": "_mm_maskz_sllv_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2513 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_sra_epi16": "_mm_maskz_sra_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2715 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_srav_epi16": "_mm_maskz_srav_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2695 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_srl_epi16": "_mm_maskz_srl_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2775 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_srlv_epi16": "_mm_maskz_srlv_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2635 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_sub_epi16": "_mm_maskz_sub_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L743 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_sub_epi8": "_mm_maskz_sub_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L726 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_subs_epi16": "_mm_maskz_subs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1648 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_subs_epi8": "_mm_maskz_subs_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1610 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_subs_epu16": "_mm_maskz_subs_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1724 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_subs_epu8": "_mm_maskz_subs_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1686 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_unpackhi_epi16": "_mm_maskz_unpackhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2194 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_unpackhi_epi8": "_mm_maskz_unpackhi_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2166 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_unpacklo_epi16": "_mm_maskz_unpacklo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2250 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_maskz_unpacklo_epi8": "_mm_maskz_unpacklo_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2222 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_permutex2var_epi16": "_mm_permutex2var_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1772 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_permutexvar_epi16": "_mm_permutexvar_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3287 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_test_epi16_mask": "_mm_test_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3053 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_test_epi8_mask": "_mm_test_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3023 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_testn_epi16_mask": "_mm_testn_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3113 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_testn_epi8_mask": "_mm_testn_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3083 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpeq_epi16_mask": "_mm256_cmpeq_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L113 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpeq_epi8_mask": "_mm256_cmpeq_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L65 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpeq_epu16_mask": "_mm256_cmpeq_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L125 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpeq_epu8_mask": "_mm256_cmpeq_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L77 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpge_epi16_mask": "_mm256_cmpge_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L209 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpge_epi8_mask": "_mm256_cmpge_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L161 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpge_epu16_mask": "_mm256_cmpge_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L221 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpge_epu8_mask": "_mm256_cmpge_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L173 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpgt_epi16_mask": "_mm256_cmpgt_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L305 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpgt_epi8_mask": "_mm256_cmpgt_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L257 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpgt_epu16_mask": "_mm256_cmpgt_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L317 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmpgt_epu8_mask": "_mm256_cmpgt_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L269 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmple_epi16_mask": "_mm256_cmple_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L401 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_cmple_epi8_mask": "_mm256_cmple_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L353 | neighbors=[avx512vlbwintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-049.json

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
