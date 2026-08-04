# Node Description Batch 101 of 111

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

- "clang_include_tmmintrin_mm_sign_epi32": "_mm_sign_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L690 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_sign_epi8": "_mm_sign_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L640 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_sign_pi16": "_mm_sign_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L740 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_sign_pi32": "_mm_sign_pi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L765 | neighbors=[tmmintrin.h]
- "clang_include_tmmintrin_mm_sign_pi8": "_mm_sign_pi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L715 | neighbors=[tmmintrin.h]
- "clang_include_vadefs": "vadefs.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vadefs.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_varargs": "varargs.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/varargs.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_vecintrin_lcbb": "__lcbb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L43 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_add_u128": "vec_add_u128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6693 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_addc": "vec_addc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6700 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_addc_u128": "vec_addc_u128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6722 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_adde_u128": "vec_adde_u128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6729 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_addec_u128": "vec_addec_u128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6737 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_eq": "vec_all_eq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L2064 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_ge": "vec_all_ge()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L2506 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_gt": "vec_all_gt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L2715 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_le": "vec_all_le()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L2924 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_lt": "vec_all_lt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L3133 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_nan": "vec_all_nan()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L3378 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_ne": "vec_all_ne()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L2285 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_nge": "vec_all_nge()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L3342 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_ngt": "vec_all_ngt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L3351 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_nle": "vec_all_nle()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L3360 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_nlt": "vec_all_nlt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L3369 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_all_numeric": "vec_all_numeric()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L3387 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_andc": "vec_andc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L4728 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_eq": "vec_any_eq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L3396 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_ge": "vec_any_ge()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L3838 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_gt": "vec_any_gt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L4047 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_le": "vec_any_le()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L4256 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_lt": "vec_any_lt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L4465 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_nan": "vec_any_nan()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L4710 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_ne": "vec_any_ne()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L3617 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_nge": "vec_any_nge()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L4674 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_ngt": "vec_any_ngt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L4683 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_nle": "vec_any_nle()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L4692 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_nlt": "vec_any_nlt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L4701 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_any_numeric": "vec_any_numeric()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L4719 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_avg": "vec_avg()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6745 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_ceil": "vec_ceil()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7354 | neighbors=[vecintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-100.json

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
