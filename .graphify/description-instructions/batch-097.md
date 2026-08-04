# Node Description Batch 98 of 111

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

- "clang_include_smmintrin_mm_cvtepu8_epi16": "_mm_cvtepu8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L324 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cvtepu8_epi32": "_mm_cvtepu8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L330 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cvtepu8_epi64": "_mm_cvtepu8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L336 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_minpos_epu16": "_mm_minpos_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L372 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_packus_epi32": "_mm_packus_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L361 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_stream_load_si128": "_mm_stream_load_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L143 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_testc_si128": "_mm_testc_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L257 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_testnzc_si128": "_mm_testnzc_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L263 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_testz_si128": "_mm_testz_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L251 | neighbors=[smmintrin.h]
- "clang_include_stdalign": "stdalign.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/stdalign.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_stdarg": "stdarg.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/stdarg.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_stdatomic": "stdatomic.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/stdatomic.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_stdbool": "stdbool.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/stdbool.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_stddef_max_align_t": "__stddef_max_align_t.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__stddef_max_align_t.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_stddef_std": "std()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/stddef.h:L109 | neighbors=[stddef.h]
- "clang_include_stdint": "stdint.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/stdint.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_stdnoreturn": "stdnoreturn.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/stdnoreturn.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_tbmintrin_blcfill_u32": "__blcfill_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L38 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blcfill_u64": "__blcfill_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L97 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blci_u32": "__blci_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L44 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blci_u64": "__blci_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L103 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blcic_u32": "__blcic_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L50 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blcic_u64": "__blcic_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L109 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blcmsk_u32": "__blcmsk_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L56 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blcmsk_u64": "__blcmsk_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L115 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blcs_u32": "__blcs_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L62 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blcs_u64": "__blcs_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L121 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blsfill_u32": "__blsfill_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L68 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blsfill_u64": "__blsfill_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L127 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blsic_u32": "__blsic_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L74 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_blsic_u64": "__blsic_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L133 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_t1mskc_u32": "__t1mskc_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L80 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_t1mskc_u64": "__t1mskc_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L139 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_tzmsk_u32": "__tzmsk_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L86 | neighbors=[tbmintrin.h]
- "clang_include_tbmintrin_tzmsk_u64": "__tzmsk_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L145 | neighbors=[tbmintrin.h]
- "clang_include_tgmath_tg_acos": "__tg_acos()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L65 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_acosh": "__tg_acosh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L152 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_asin": "__tg_asin()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L94 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_asinh": "__tg_asinh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L181 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_atan": "__tg_atan()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L123 | neighbors=[tgmath.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-097.json

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
