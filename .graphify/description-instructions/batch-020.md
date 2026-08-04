# Node Description Batch 21 of 111

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

- "clang_include_avx2intrin_mm256_mul_epu32": "_mm256_mul_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L474 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_mulhi_epi16": "_mm256_mulhi_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L456 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_mulhi_epu16": "_mm256_mulhi_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L450 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_mulhrs_epi16": "_mm256_mulhrs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L444 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_mullo_epi16": "_mm256_mullo_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L462 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_mullo_epi32": "_mm256_mullo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L468 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_or_si256": "_mm256_or_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L480 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_packs_epi16": "_mm256_packs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L57 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_packs_epi32": "_mm256_packs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L63 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_packus_epi16": "_mm256_packus_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L69 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_packus_epi32": "_mm256_packus_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L75 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_permutevar8x32_epi32": "_mm256_permutevar8x32_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L937 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_permutevar8x32_ps": "_mm256_permutevar8x32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L951 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_sad_epu8": "_mm256_sad_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L486 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_shuffle_epi8": "_mm256_shuffle_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L492 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_sign_epi16": "_mm256_sign_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L544 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_sign_epi32": "_mm256_sign_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L550 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_sign_epi8": "_mm256_sign_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L538 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_sll_epi16": "_mm256_sll_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L601 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_sll_epi32": "_mm256_sll_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L613 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_sll_epi64": "_mm256_sll_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L625 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_slli_epi16": "_mm256_slli_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L595 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_slli_epi32": "_mm256_slli_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L607 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_slli_epi64": "_mm256_slli_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L619 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_sllv_epi32": "_mm256_sllv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1030 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_sllv_epi64": "_mm256_sllv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1042 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_sra_epi16": "_mm256_sra_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L637 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_sra_epi32": "_mm256_sra_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L649 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_srai_epi16": "_mm256_srai_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L631 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_srai_epi32": "_mm256_srai_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L643 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_srav_epi32": "_mm256_srav_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1054 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_srl_epi16": "_mm256_srl_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L700 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_srl_epi32": "_mm256_srl_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L712 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_srl_epi64": "_mm256_srl_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L724 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_srli_epi16": "_mm256_srli_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L694 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_srli_epi32": "_mm256_srli_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L706 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_srli_epi64": "_mm256_srli_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L718 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_srlv_epi32": "_mm256_srlv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1066 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_srlv_epi64": "_mm256_srlv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1078 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_stream_load_si256": "_mm256_stream_load_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L832 | neighbors=[avx2intrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-020.json

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
