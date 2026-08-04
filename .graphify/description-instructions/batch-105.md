# Node Description Batch 106 of 111

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

- "clang_include_xmmintrin_mm_cmpord_ss": "_mm_cmpord_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L929 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpunord_ps": "_mm_cmpunord_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L991 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpunord_ss": "_mm_cmpunord_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L972 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_comieq_ss": "_mm_comieq_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1011 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_comige_ss": "_mm_comige_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1095 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_comigt_ss": "_mm_comigt_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1074 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_comile_ss": "_mm_comile_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1053 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_comilt_ss": "_mm_comilt_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1032 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_comineq_ss": "_mm_comineq_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1116 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cvtsi64_ss": "_mm_cvtsi64_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1489 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cvtss_f32": "_mm_cvtss_f32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1555 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cvtss_si64": "_mm_cvtss_si64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1298 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cvttss_si64": "_mm_cvttss_si64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1386 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_div_ps": "_mm_div_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L206 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_div_ss": "_mm_div_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L187 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_load_ss": "_mm_load_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1630 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_load1_ps": "_mm_load1_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1652 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_loadh_pi": "_mm_loadh_pi()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1576 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_loadl_pi": "_mm_loadl_pi()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1603 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_loadu_ps": "_mm_loadu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1692 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_maskmove_si64": "_mm_maskmove_si64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2311 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_max_pi16": "_mm_max_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2160 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_max_ps": "_mm_max_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L399 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_max_pu8": "_mm_max_pu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2179 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_max_ss": "_mm_max_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L380 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_min_pi16": "_mm_min_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2198 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_min_ps": "_mm_min_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L357 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_min_pu8": "_mm_min_pu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2217 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_min_ss": "_mm_min_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L338 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_move_ss": "_mm_move_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2556 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_movemask_pi8": "_mm_movemask_pi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2235 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_movemask_ps": "_mm_movemask_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2809 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_mul_ps": "_mm_mul_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L165 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_mul_ss": "_mm_mul_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L145 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_mulhi_pu16": "_mm_mulhi_pu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2254 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_or_ps": "_mm_or_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L457 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_rcp_ps": "_mm_rcp_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L278 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_rcp_ss": "_mm_rcp_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L260 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_rsqrt_ps": "_mm_rsqrt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L315 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_rsqrt_ss": "_mm_rsqrt_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L297 | neighbors=[xmmintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-105.json

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
