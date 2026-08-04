# Node Description Batch 19 of 111

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

- "clang_include_avx2intrin_mm_broadcastss_ps": "_mm_broadcastss_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L838 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_broadcastw_epi16": "_mm_broadcastw_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L918 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_maskload_epi32": "_mm_maskload_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L994 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_maskload_epi64": "_mm_maskload_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1000 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_maskstore_epi32": "_mm_maskstore_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1018 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_maskstore_epi64": "_mm_maskstore_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1024 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_sllv_epi32": "_mm_sllv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1036 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_sllv_epi64": "_mm_sllv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1048 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_srav_epi32": "_mm_srav_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1060 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_srlv_epi32": "_mm_srlv_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1072 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_srlv_epi64": "_mm_srlv_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1084 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_abs_epi16": "_mm256_abs_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L45 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_abs_epi32": "_mm256_abs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L51 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_abs_epi8": "_mm256_abs_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L39 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_add_epi16": "_mm256_add_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L87 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_add_epi32": "_mm256_add_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L93 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_add_epi64": "_mm256_add_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L99 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_add_epi8": "_mm256_add_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L81 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_adds_epi16": "_mm256_adds_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L111 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_adds_epi8": "_mm256_adds_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L105 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_adds_epu16": "_mm256_adds_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L123 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_adds_epu8": "_mm256_adds_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L117 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_and_si256": "_mm256_and_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L133 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_andnot_si256": "_mm256_andnot_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L139 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_avg_epu16": "_mm256_avg_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L151 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_avg_epu8": "_mm256_avg_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L145 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_blendv_epi8": "_mm256_blendv_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L157 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_broadcastb_epi8": "_mm256_broadcastb_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L888 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_broadcastd_epi32": "_mm256_broadcastd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L900 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_broadcastq_epi64": "_mm256_broadcastq_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L906 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_broadcastsd_pd": "_mm256_broadcastsd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L856 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_broadcastsi128_si256": "_mm256_broadcastsi128_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L862 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_broadcastss_ps": "_mm256_broadcastss_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L850 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_broadcastw_epi16": "_mm256_broadcastw_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L894 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cmpeq_epi16": "_mm256_cmpeq_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L190 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cmpeq_epi32": "_mm256_cmpeq_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L196 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cmpeq_epi64": "_mm256_cmpeq_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L202 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cmpeq_epi8": "_mm256_cmpeq_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L184 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cmpgt_epi16": "_mm256_cmpgt_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L216 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm256_cmpgt_epi32": "_mm256_cmpgt_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L222 | neighbors=[avx2intrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-018.json

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
