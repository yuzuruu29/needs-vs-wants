# Node Description Batch 60 of 111

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

- "clang_include_avx512vlintrin_mm_mask_cmpgt_epi32_mask": "_mm_mask_cmpgt_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L240 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpgt_epi64_mask": "_mm_mask_cmpgt_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L288 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpgt_epu32_mask": "_mm_mask_cmpgt_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L252 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpgt_epu64_mask": "_mm_mask_cmpgt_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L300 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmple_epi32_mask": "_mm_mask_cmple_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L336 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmple_epi64_mask": "_mm_mask_cmple_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L384 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmple_epu32_mask": "_mm_mask_cmple_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L348 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmple_epu64_mask": "_mm_mask_cmple_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L396 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmplt_epi32_mask": "_mm_mask_cmplt_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L432 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmplt_epi64_mask": "_mm_mask_cmplt_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L480 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmplt_epu32_mask": "_mm_mask_cmplt_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L444 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmplt_epu64_mask": "_mm_mask_cmplt_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L492 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpneq_epi32_mask": "_mm_mask_cmpneq_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L528 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpneq_epi64_mask": "_mm_mask_cmpneq_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L576 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpneq_epu32_mask": "_mm_mask_cmpneq_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L540 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cmpneq_epu64_mask": "_mm_mask_cmpneq_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L588 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_compress_epi32": "_mm_mask_compress_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2111 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_compress_epi64": "_mm_mask_compress_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2051 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_compress_pd": "_mm_mask_compress_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2021 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_compress_ps": "_mm_mask_compress_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2081 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_compressstoreu_epi32": "_mm_mask_compressstoreu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2183 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_compressstoreu_epi64": "_mm_mask_compressstoreu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2155 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_compressstoreu_pd": "_mm_mask_compressstoreu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2141 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_compressstoreu_ps": "_mm_mask_compressstoreu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2169 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi16_epi32": "_mm_mask_cvtepi16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4655 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi16_epi64": "_mm_mask_cvtepi16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4689 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi32_epi16": "_mm_mask_cvtepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8250 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi32_epi64": "_mm_mask_cvtepi32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4621 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi32_epi8": "_mm_mask_cvtepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8191 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi32_pd": "_mm_mask_cvtepi32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2197 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi32_ps": "_mm_mask_cvtepi32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2227 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi32_storeu_epi16": "_mm_mask_cvtepi32_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8265 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi32_storeu_epi8": "_mm_mask_cvtepi32_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8207 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi64_epi16": "_mm_mask_cvtepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8424 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi64_epi32": "_mm_mask_cvtepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8366 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi64_epi8": "_mm_mask_cvtepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8308 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi64_storeu_epi16": "_mm_mask_cvtepi64_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8440 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi64_storeu_epi32": "_mm_mask_cvtepi64_storeu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8381 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi64_storeu_epi8": "_mm_mask_cvtepi64_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8323 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm_mask_cvtepi8_epi32": "_mm_mask_cvtepi8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L4553 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-059.json

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
