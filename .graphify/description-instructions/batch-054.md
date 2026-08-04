# Node Description Batch 55 of 111

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

- "clang_include_avx512vlbwintrin_mm256_maskz_sub_epi8": "_mm256_maskz_sub_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L659 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_subs_epi16": "_mm256_maskz_subs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1667 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_subs_epi8": "_mm256_maskz_subs_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1629 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_subs_epu16": "_mm256_maskz_subs_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1743 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_subs_epu8": "_mm256_maskz_subs_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1705 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_unpackhi_epi16": "_mm256_maskz_unpackhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2208 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_unpackhi_epi8": "_mm256_maskz_unpackhi_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2180 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_unpacklo_epi16": "_mm256_maskz_unpacklo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2264 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_maskz_unpacklo_epi8": "_mm256_maskz_unpacklo_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L2236 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_movepi16_mask": "_mm256_movepi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3161 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_movepi8_mask": "_mm256_movepi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3149 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_permutex2var_epi16": "_mm256_permutex2var_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1801 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_permutexvar_epi16": "_mm256_permutexvar_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3315 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_test_epi16_mask": "_mm256_test_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3068 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_test_epi8_mask": "_mm256_test_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3038 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_testn_epi16_mask": "_mm256_testn_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3128 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_mm256_testn_epi8_mask": "_mm256_testn_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L3098 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlbwintrin_void": "void()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L35 | neighbors=[avx512vlbwintrin.h]
- "clang_include_avx512vlcdintrin_default_fn_attrs": "__DEFAULT_FN_ATTRS()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L157 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_broadcastmb_epi64": "_mm_broadcastmb_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L34 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_broadcastmw_epi32": "_mm_broadcastmw_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L46 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_conflict_epi32": "_mm_conflict_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L108 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_conflict_epi64": "_mm_conflict_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L59 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_mask_conflict_epi32": "_mm_mask_conflict_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L116 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_mask_conflict_epi64": "_mm_mask_conflict_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L67 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_mask_lzcnt_epi32": "_mm_mask_lzcnt_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L166 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_mask_lzcnt_epi64": "_mm_mask_lzcnt_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L218 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_maskz_conflict_epi32": "_mm_maskz_conflict_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L124 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_maskz_conflict_epi64": "_mm_maskz_conflict_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L75 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_maskz_lzcnt_epi32": "_mm_maskz_lzcnt_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L174 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm_maskz_lzcnt_epi64": "_mm_maskz_lzcnt_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L226 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_broadcastmb_epi64": "_mm256_broadcastmb_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L40 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_broadcastmw_epi32": "_mm256_broadcastmw_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L52 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_conflict_epi32": "_mm256_conflict_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L132 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_conflict_epi64": "_mm256_conflict_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L84 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_lzcnt_epi32": "_mm256_lzcnt_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L183 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_lzcnt_epi64": "_mm256_lzcnt_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L235 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_mask_conflict_epi32": "_mm256_mask_conflict_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L140 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_mask_conflict_epi64": "_mm256_mask_conflict_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L92 | neighbors=[avx512vlcdintrin.h]
- "clang_include_avx512vlcdintrin_mm256_mask_lzcnt_epi32": "_mm256_mask_lzcnt_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L192 | neighbors=[avx512vlcdintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-054.json

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
