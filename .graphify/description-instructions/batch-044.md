# Node Description Batch 45 of 111

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

- "clang_include_avx512ifmaintrin_mm512_mask_madd52lo_epu64": "_mm512_mask_madd52lo_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmaintrin.h:L71 | neighbors=[avx512ifmaintrin.h]
- "clang_include_avx512ifmaintrin_mm512_maskz_madd52hi_epu64": "_mm512_maskz_madd52hi_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmaintrin.h:L53 | neighbors=[avx512ifmaintrin.h]
- "clang_include_avx512ifmaintrin_mm512_maskz_madd52lo_epu64": "_mm512_maskz_madd52lo_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmaintrin.h:L81 | neighbors=[avx512ifmaintrin.h]
- "clang_include_avx512ifmavlintrin_mm_madd52hi_epu64": "_mm_madd52hi_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L36 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512ifmavlintrin_mm_madd52lo_epu64": "_mm_madd52lo_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L91 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512ifmavlintrin_mm_mask_madd52hi_epu64": "_mm_mask_madd52hi_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L45 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512ifmavlintrin_mm_mask_madd52lo_epu64": "_mm_mask_madd52lo_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L100 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512ifmavlintrin_mm_maskz_madd52hi_epu64": "_mm_maskz_madd52hi_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L54 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512ifmavlintrin_mm_maskz_madd52lo_epu64": "_mm_maskz_madd52lo_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L109 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512ifmavlintrin_mm256_madd52hi_epu64": "_mm256_madd52hi_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L63 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512ifmavlintrin_mm256_madd52lo_epu64": "_mm256_madd52lo_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L118 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512ifmavlintrin_mm256_mask_madd52hi_epu64": "_mm256_mask_madd52hi_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L72 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512ifmavlintrin_mm256_mask_madd52lo_epu64": "_mm256_mask_madd52lo_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L127 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512ifmavlintrin_mm256_maskz_madd52hi_epu64": "_mm256_maskz_madd52hi_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L82 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512ifmavlintrin_mm256_maskz_madd52lo_epu64": "_mm256_maskz_madd52lo_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L137 | neighbors=[avx512ifmavlintrin.h]
- "clang_include_avx512pfintrin": "avx512pfintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512pfintrin.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_avx512vbmiintrin_mm512_mask_multishift_epi64_epi8": "_mm512_mask_multishift_epi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmiintrin.h:L107 | neighbors=[avx512vbmiintrin.h]
- "clang_include_avx512vbmiintrin_mm512_mask_permutex2var_epi8": "_mm512_mask_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmiintrin.h:L56 | neighbors=[avx512vbmiintrin.h]
- "clang_include_avx512vbmiintrin_mm512_mask_permutexvar_epi8": "_mm512_mask_permutexvar_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmiintrin.h:L97 | neighbors=[avx512vbmiintrin.h]
- "clang_include_avx512vbmiintrin_mm512_mask2_permutex2var_epi8": "_mm512_mask2_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmiintrin.h:L35 | neighbors=[avx512vbmiintrin.h]
- "clang_include_avx512vbmiintrin_mm512_maskz_multishift_epi64_epi8": "_mm512_maskz_multishift_epi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmiintrin.h:L116 | neighbors=[avx512vbmiintrin.h]
- "clang_include_avx512vbmiintrin_mm512_maskz_permutex2var_epi8": "_mm512_maskz_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmiintrin.h:L67 | neighbors=[avx512vbmiintrin.h]
- "clang_include_avx512vbmiintrin_mm512_maskz_permutexvar_epi8": "_mm512_maskz_permutexvar_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmiintrin.h:L87 | neighbors=[avx512vbmiintrin.h]
- "clang_include_avx512vbmiintrin_mm512_multishift_epi64_epi8": "_mm512_multishift_epi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmiintrin.h:L125 | neighbors=[avx512vbmiintrin.h]
- "clang_include_avx512vbmiintrin_mm512_permutex2var_epi8": "_mm512_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmiintrin.h:L46 | neighbors=[avx512vbmiintrin.h]
- "clang_include_avx512vbmiintrin_mm512_permutexvar_epi8": "_mm512_permutexvar_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmiintrin.h:L78 | neighbors=[avx512vbmiintrin.h]
- "clang_include_avx512vbmivlintrin_mm_mask_multishift_epi64_epi8": "_mm_mask_multishift_epi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L186 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm_mask_permutex2var_epi8": "_mm_mask_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L70 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm_mask_permutexvar_epi8": "_mm_mask_permutexvar_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L147 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm_mask2_permutex2var_epi8": "_mm_mask2_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L35 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm_maskz_multishift_epi64_epi8": "_mm_maskz_multishift_epi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L195 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm_maskz_permutex2var_epi8": "_mm_maskz_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L82 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm_maskz_permutexvar_epi8": "_mm_maskz_permutexvar_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L138 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm_multishift_epi64_epi8": "_mm_multishift_epi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L205 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm_permutex2var_epi8": "_mm_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L59 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm_permutexvar_epi8": "_mm_permutexvar_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L129 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm256_mask_multishift_epi64_epi8": "_mm256_mask_multishift_epi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L215 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm256_mask_permutex2var_epi8": "_mm256_mask_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L105 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm256_mask_permutexvar_epi8": "_mm256_mask_permutexvar_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L176 | neighbors=[avx512vbmivlintrin.h]
- "clang_include_avx512vbmivlintrin_mm256_mask2_permutex2var_epi8": "_mm256_mask2_permutex2var_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L47 | neighbors=[avx512vbmivlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-044.json

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
