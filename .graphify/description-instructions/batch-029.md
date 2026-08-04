# Node Description Batch 30 of 111

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

- "clang_include_avx512fintrin_mm_mask_max_sd": "_mm_mask_max_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L969 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_max_ss": "_mm_mask_max_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L933 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_min_sd": "_mm_mask_min_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1254 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_min_ss": "_mm_mask_min_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1218 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_mul_sd": "_mm_mask_mul_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2230 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_mul_ss": "_mm_mask_mul_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2195 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_rsqrt14_sd": "_mm_mask_rsqrt14_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1657 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_rsqrt14_ss": "_mm_mask_rsqrt14_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1629 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_scalef_sd": "_mm_mask_scalef_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6951 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_scalef_ss": "_mm_mask_scalef_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6998 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_sub_sd": "_mm_mask_sub_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2085 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_sub_ss": "_mm_mask_sub_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2050 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask3_fmadd_sd": "_mm_mask3_fmadd_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8500 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask3_fmadd_ss": "_mm_mask3_fmadd_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8308 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask3_fmsub_sd": "_mm_mask3_fmsub_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8548 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask3_fmsub_ss": "_mm_mask3_fmsub_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8356 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask3_fnmadd_sd": "_mm_mask3_fnmadd_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8596 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask3_fnmadd_ss": "_mm_mask3_fnmadd_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8404 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask3_fnmsub_sd": "_mm_mask3_fnmsub_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8645 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask3_fnmsub_ss": "_mm_mask3_fnmsub_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8452 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_add_sd": "_mm_maskz_add_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1952 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_add_ss": "_mm_maskz_add_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1916 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_cvtsd_ss": "_mm_maskz_cvtsd_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9334 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_cvtss_sd": "_mm_maskz_cvtss_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9401 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_div_sd": "_mm_maskz_div_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2385 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_div_ss": "_mm_maskz_div_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2349 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_fmadd_sd": "_mm_maskz_fmadd_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8484 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_fmadd_ss": "_mm_maskz_fmadd_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8292 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_fmsub_sd": "_mm_maskz_fmsub_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8532 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_fmsub_ss": "_mm_maskz_fmsub_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8340 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_fnmadd_sd": "_mm_maskz_fnmadd_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8580 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_fnmadd_ss": "_mm_maskz_fnmadd_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8388 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_fnmsub_sd": "_mm_maskz_fnmsub_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8628 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_fnmsub_ss": "_mm_maskz_fnmsub_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8436 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_getexp_sd": "_mm_maskz_getexp_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5749 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_getexp_ss": "_mm_maskz_getexp_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5794 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_max_sd": "_mm_maskz_max_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L978 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_max_ss": "_mm_maskz_max_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L942 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_min_sd": "_mm_maskz_min_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1263 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_maskz_min_ss": "_mm_maskz_min_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1227 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-029.json

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
