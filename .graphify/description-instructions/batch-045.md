# Node Description Batch 46 of 111

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

- "clang_include_avx512vbmivlintrin_mm256_maskz_multishift_epi64_epi8": "_mm256_maskz_multishift_epi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L224 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm256_maskz_permutex2var_epi8": "_mm256_maskz_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L117 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm256_maskz_permutexvar_epi8": "_mm256_maskz_permutexvar_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L166 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm256_multishift_epi64_epi8": "_mm256_multishift_epi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L234 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm256_permutex2var_epi8": "_mm256_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L94 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm256_permutexvar_epi8": "_mm256_permutexvar_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L157 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vlbwintrin_default_fn_attrs": "__DEFAULT_FN_ATTRS()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L684 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpeq_epi16_mask": "_mm_cmpeq_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L89 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpeq_epi8_mask": "_mm_cmpeq_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L41 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpeq_epu16_mask": "_mm_cmpeq_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L101 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpeq_epu8_mask": "_mm_cmpeq_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L53 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpge_epi16_mask": "_mm_cmpge_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L185 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpge_epi8_mask": "_mm_cmpge_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L137 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpge_epu16_mask": "_mm_cmpge_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L197 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpge_epu8_mask": "_mm_cmpge_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L149 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpgt_epi16_mask": "_mm_cmpgt_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L281 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpgt_epi8_mask": "_mm_cmpgt_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L233 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpgt_epu16_mask": "_mm_cmpgt_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L293 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpgt_epu8_mask": "_mm_cmpgt_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L245 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmple_epi16_mask": "_mm_cmple_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L377 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmple_epi8_mask": "_mm_cmple_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L329 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmple_epu16_mask": "_mm_cmple_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L389 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmple_epu8_mask": "_mm_cmple_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L341 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmplt_epi16_mask": "_mm_cmplt_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L473 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmplt_epi8_mask": "_mm_cmplt_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L425 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmplt_epu16_mask": "_mm_cmplt_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L485 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmplt_epu8_mask": "_mm_cmplt_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L437 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpneq_epi16_mask": "_mm_cmpneq_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L569 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpneq_epi8_mask": "_mm_cmpneq_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L521 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpneq_epu16_mask": "_mm_cmpneq_epu16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L581 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cmpneq_epu8_mask": "_mm_cmpneq_epu8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L533 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cvtsepi16_epi8": "_mm_cvtsepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1896 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_cvtusepi16_epi8": "_mm_cvtusepi16_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1938 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_abs_epi16": "_mm_mask_abs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L850 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_add_epi16": "_mm_mask_add_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L701 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_adds_epi16": "_mm_mask_adds_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1068 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_adds_epi8": "_mm_mask_adds_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1030 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_adds_epu16": "_mm_mask_adds_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1144 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_adds_epu8": "_mm_mask_adds_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1106 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm_mask_avg_epu16": "_mm_mask_avg_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1220 | neighbors=[avx512vlbwintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-045.json

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
