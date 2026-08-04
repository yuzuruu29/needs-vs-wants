# Node Description Batch 105 of 111

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

- "clang_include_vecintrin_vec_unpackl": "vec_unpackl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1762 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_xld2": "vec_xld2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L762 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_xlw4": "vec_xlw4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L809 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_xstd2": "vec_xstd2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L841 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_xstw4": "vec_xstw4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L891 | neighbors=[vecintrin.h]
- "clang_include_wmmintrin": "wmmintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/wmmintrin.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_wmmintrin_aes_mm_aesdec_si128": "_mm_aesdec_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__wmmintrin_aes.h:L85 | neighbors=[__wmmintrin_aes.h]
- "clang_include_wmmintrin_aes_mm_aesdeclast_si128": "_mm_aesdeclast_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__wmmintrin_aes.h:L105 | neighbors=[__wmmintrin_aes.h]
- "clang_include_wmmintrin_aes_mm_aesenc_si128": "_mm_aesenc_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__wmmintrin_aes.h:L45 | neighbors=[__wmmintrin_aes.h]
- "clang_include_wmmintrin_aes_mm_aesenclast_si128": "_mm_aesenclast_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__wmmintrin_aes.h:L65 | neighbors=[__wmmintrin_aes.h]
- "clang_include_wmmintrin_aes_mm_aesimc_si128": "_mm_aesimc_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__wmmintrin_aes.h:L122 | neighbors=[__wmmintrin_aes.h]
- "clang_include_wmmintrin_pclmul": "__wmmintrin_pclmul.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__wmmintrin_pclmul.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_x86intrin": "x86intrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/x86intrin.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_xmmintrin_mm_add_ps": "_mm_add_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L80 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_add_ss": "_mm_add_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L60 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_and_ps": "_mm_and_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L417 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_andnot_ps": "_mm_andnot_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L439 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_avg_pu16": "_mm_avg_pu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2349 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_avg_pu8": "_mm_avg_pu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2330 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpeq_ps": "_mm_cmpeq_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L516 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpeq_ss": "_mm_cmpeq_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L498 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpge_ps": "_mm_cmpge_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L690 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpge_ss": "_mm_cmpge_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L669 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpgt_ps": "_mm_cmpgt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L645 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpgt_ss": "_mm_cmpgt_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L624 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmple_ps": "_mm_cmple_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L601 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmple_ss": "_mm_cmple_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L582 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmplt_ps": "_mm_cmplt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L558 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmplt_ss": "_mm_cmplt_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L539 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpneq_ps": "_mm_cmpneq_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L730 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpneq_ss": "_mm_cmpneq_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L712 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpnge_ps": "_mm_cmpnge_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L905 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpnge_ss": "_mm_cmpnge_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L884 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpngt_ps": "_mm_cmpngt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L860 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpngt_ss": "_mm_cmpngt_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L839 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpnle_ps": "_mm_cmpnle_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L815 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpnle_ss": "_mm_cmpnle_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L796 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpnlt_ps": "_mm_cmpnlt_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L772 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpnlt_ss": "_mm_cmpnlt_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L753 | neighbors=[xmmintrin.h]
- "clang_include_xmmintrin_mm_cmpord_ps": "_mm_cmpord_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L948 | neighbors=[xmmintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-104.json

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
