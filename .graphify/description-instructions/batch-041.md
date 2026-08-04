# Node Description Batch 42 of 111

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

- "clang_include_avx512fintrin_mm512_maskz_max_epi64": "_mm512_maskz_max_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1080 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_max_epu32": "_mm512_maskz_max_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1052 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_max_epu64": "_mm512_maskz_max_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1108 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_max_pd": "_mm512_maskz_max_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L872 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_max_ps": "_mm512_maskz_max_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L922 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_min_epi32": "_mm512_maskz_min_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1309 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_min_epi64": "_mm512_maskz_min_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1365 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_min_epu32": "_mm512_maskz_min_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1337 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_min_epu64": "_mm512_maskz_min_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1393 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_min_pd": "_mm512_maskz_min_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1175 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_min_ps": "_mm512_maskz_min_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1207 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_mov_epi32": "_mm512_maskz_mov_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5490 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_mov_epi64": "_mm512_maskz_mov_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5506 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_mov_pd": "_mm512_maskz_mov_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9255 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_mov_ps": "_mm512_maskz_mov_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9271 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_movedup_pd": "_mm512_maskz_movedup_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5553 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_mul_epi32": "_mm512_maskz_mul_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1421 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_mul_epu32": "_mm512_maskz_mul_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1449 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_mul_pd": "_mm512_maskz_mul_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2275 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_mul_ps": "_mm512_maskz_mul_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2294 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_mullo_epi32": "_mm512_maskz_mullo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1465 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_permutevar_pd": "_mm512_maskz_permutevar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6577 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_permutevar_ps": "_mm512_maskz_permutevar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6606 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_permutex2var_epi32": "_mm512_maskz_permutex2var_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3401 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_permutex2var_epi64": "_mm512_maskz_permutex2var_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3434 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_permutex2var_pd": "_mm512_maskz_permutex2var_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6636 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_permutex2var_ps": "_mm512_maskz_permutex2var_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6667 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_permutexvar_epi32": "_mm512_maskz_permutexvar_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8787 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_permutexvar_epi64": "_mm512_maskz_permutexvar_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8732 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_permutexvar_pd": "_mm512_maskz_permutexvar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8723 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_permutexvar_ps": "_mm512_maskz_permutexvar_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8778 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_rcp14_pd": "_mm512_maskz_rcp14_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1692 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_rcp14_ps": "_mm512_maskz_rcp14_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1718 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_rolv_epi32": "_mm512_maskz_rolv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5328 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_rolv_epi64": "_mm512_maskz_rolv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5357 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_rorv_epi32": "_mm512_maskz_rorv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5200 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_rorv_epi64": "_mm512_maskz_rorv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5229 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_rsqrt14_pd": "_mm512_maskz_rsqrt14_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1584 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_rsqrt14_ps": "_mm512_maskz_rsqrt14_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1610 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_scalef_pd": "_mm512_maskz_scalef_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6875 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-041.json

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
