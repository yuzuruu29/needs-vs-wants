# Node Description Batch 20 of 111

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

- "clang_include_avx2intrin_mm256_cmpgt_epi64": "_mm256_cmpgt_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L228 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cmpgt_epi8": "_mm256_cmpgt_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L208 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepi16_epi32": "_mm256_cvtepi16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L384 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepi16_epi64": "_mm256_cvtepi16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L390 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepi32_epi64": "_mm256_cvtepi32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L396 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepi8_epi16": "_mm256_cvtepi8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L360 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepi8_epi32": "_mm256_cvtepi8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L368 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepi8_epi64": "_mm256_cvtepi8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L376 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepu16_epi32": "_mm256_cvtepu16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L420 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepu16_epi64": "_mm256_cvtepu16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L426 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepu32_epi64": "_mm256_cvtepu32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L432 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepu8_epi16": "_mm256_cvtepu8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L402 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepu8_epi32": "_mm256_cvtepu8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L408 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cvtepu8_epi64": "_mm256_cvtepu8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L414 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_hadd_epi16": "_mm256_hadd_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L234 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_hadd_epi32": "_mm256_hadd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L240 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_hadds_epi16": "_mm256_hadds_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L246 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_hsub_epi16": "_mm256_hsub_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L252 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_hsub_epi32": "_mm256_hsub_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L258 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_hsubs_epi16": "_mm256_hsubs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L264 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_madd_epi16": "_mm256_madd_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L276 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_maddubs_epi16": "_mm256_maddubs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L270 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_maskload_epi32": "_mm256_maskload_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L982 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_maskload_epi64": "_mm256_maskload_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L988 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_maskstore_epi32": "_mm256_maskstore_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1006 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_maskstore_epi64": "_mm256_maskstore_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1012 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_max_epi16": "_mm256_max_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L288 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_max_epi32": "_mm256_max_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L294 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_max_epi8": "_mm256_max_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L282 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_max_epu16": "_mm256_max_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L306 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_max_epu32": "_mm256_max_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L312 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_max_epu8": "_mm256_max_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L300 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_min_epi16": "_mm256_min_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L324 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_min_epi32": "_mm256_min_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L330 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_min_epi8": "_mm256_min_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L318 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_min_epu16": "_mm256_min_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L342 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_min_epu32": "_mm256_min_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L348 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_min_epu8": "_mm256_min_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L336 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_movemask_epi8": "_mm256_movemask_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L354 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_mul_epi32": "_mm256_mul_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L438 | neighbors=[avx2intrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-019.json

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
