# Node Description Batch 43 of 111

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

- "clang_include_avx512fintrin_mm512_maskz_scalef_ps": "_mm512_maskz_scalef_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6925 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_set1_epi32": "_mm512_maskz_set1_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L248 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_set1_epi64": "_mm512_maskz_set1_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L257 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sll_epi32": "_mm512_maskz_sll_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5947 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sll_epi64": "_mm512_maskz_sll_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5976 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sllv_epi32": "_mm512_maskz_sllv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6005 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sllv_epi64": "_mm512_maskz_sllv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6034 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sqrt_pd": "_mm512_maskz_sqrt_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1516 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sqrt_ps": "_mm512_maskz_sqrt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1559 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sra_epi32": "_mm512_maskz_sra_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6063 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sra_epi64": "_mm512_maskz_sra_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6092 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_srav_epi32": "_mm512_maskz_srav_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6121 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_srav_epi64": "_mm512_maskz_srav_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6150 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_srl_epi32": "_mm512_maskz_srl_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6179 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_srl_epi64": "_mm512_maskz_srl_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6208 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_srlv_epi32": "_mm512_maskz_srlv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6237 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_srlv_epi64": "_mm512_maskz_srlv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6266 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sub_epi32": "_mm512_maskz_sub_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L823 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sub_epi64": "_mm512_maskz_sub_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L773 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sub_pd": "_mm512_maskz_sub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2130 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_maskz_sub_ps": "_mm512_maskz_sub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2149 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_max_epi32": "_mm512_max_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1005 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_max_epi64": "_mm512_max_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1062 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_max_epu32": "_mm512_max_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1034 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_max_epu64": "_mm512_max_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1090 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_max_pd": "_mm512_max_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L851 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_max_ps": "_mm512_max_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L901 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_min_epi32": "_mm512_min_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1290 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_min_epi64": "_mm512_min_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1347 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_min_epu32": "_mm512_min_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1319 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_min_epu64": "_mm512_min_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1375 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_min_pd": "_mm512_min_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1136 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_min_ps": "_mm512_min_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1186 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mul_epi32": "_mm512_mul_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1403 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mul_epu32": "_mm512_mul_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1431 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mul_pd": "_mm512_mul_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L709 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mul_ps": "_mm512_mul_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L715 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mullo_epi32": "_mm512_mullo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1459 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_or_si512": "_mm512_or_si512()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L683 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_permutevar_pd": "_mm512_permutevar_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6558 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-042.json

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
