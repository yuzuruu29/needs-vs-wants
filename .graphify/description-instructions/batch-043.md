# Node Description Batch 44 of 111

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

- "clang_include_avx512fintrin_mm512_permutevar_ps": "_mm512_permutevar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6587 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_permutex2var_epi32": "_mm512_permutex2var_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3380 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_permutex2var_epi64": "_mm512_permutex2var_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3412 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_permutex2var_pd": "_mm512_permutex2var_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6616 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_permutex2var_ps": "_mm512_permutex2var_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6647 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_permutexvar_epi32": "_mm512_permutexvar_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8796 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_permutexvar_epi64": "_mm512_permutexvar_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8741 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_permutexvar_pd": "_mm512_permutexvar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8705 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_permutexvar_ps": "_mm512_permutexvar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8760 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_rcp14_pd": "_mm512_rcp14_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1675 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_rcp14_ps": "_mm512_rcp14_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1701 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_rsqrt14_pd": "_mm512_rsqrt14_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1568 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_rsqrt14_ps": "_mm512_rsqrt14_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1593 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_set1_epi16": "_mm512_set1_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L314 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_set1_epi8": "_mm512_set1_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L301 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_set1_pd": "_mm512_set1_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L295 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_set1_ps": "_mm512_set1_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L288 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_sqrt_pd": "_mm512_sqrt_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1498 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_sqrt_ps": "_mm512_sqrt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1541 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_store_epi32": "_mm512_store_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4611 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_store_epi64": "_mm512_store_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4617 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_store_pd": "_mm512_store_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4586 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_store_ps": "_mm512_store_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4599 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_store_si512": "_mm512_store_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4605 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_storeu_pd": "_mm512_storeu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4561 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_storeu_ps": "_mm512_storeu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4574 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_storeu_si512": "_mm512_storeu_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4541 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_stream_load_si512": "_mm512_stream_load_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8869 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_stream_si512": "_mm512_stream_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8863 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_sub_pd": "_mm512_sub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L721 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_sub_ps": "_mm512_sub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L727 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_test_epi32_mask": "_mm512_test_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4334 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_test_epi64_mask": "_mm512_test_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4349 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_testn_epi32_mask": "_mm512_testn_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6678 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_testn_epi64_mask": "_mm512_testn_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6693 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_xor_si512": "_mm512_xor_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L689 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_void": "void()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L168 | neighbors=[avx512fintrin.h]
- "clang_include_avx512ifmaintrin_mm512_madd52hi_epu64": "_mm512_madd52hi_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmaintrin.h:L34 | neighbors=[avx512ifmaintrin.h]
- "clang_include_avx512ifmaintrin_mm512_madd52lo_epu64": "_mm512_madd52lo_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmaintrin.h:L62 | neighbors=[avx512ifmaintrin.h]
- "clang_include_avx512ifmaintrin_mm512_mask_madd52hi_epu64": "_mm512_mask_madd52hi_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmaintrin.h:L43 | neighbors=[avx512ifmaintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-043.json

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
