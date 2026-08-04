# Node Description Batch 32 of 111

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

- "clang_include_avx512fintrin_mm512_ceil_pd": "_mm512_ceil_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1837 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_ceil_ps": "_mm512_ceil_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1828 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpeq_epi32_mask": "_mm512_cmpeq_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4633 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpeq_epi64_mask": "_mm512_cmpeq_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4663 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpeq_epu32_mask": "_mm512_cmpeq_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4645 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpeq_epu64_mask": "_mm512_cmpeq_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4669 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpge_epi32_mask": "_mm512_cmpge_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4681 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpge_epi64_mask": "_mm512_cmpge_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4705 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpge_epu32_mask": "_mm512_cmpge_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4693 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpge_epu64_mask": "_mm512_cmpge_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4717 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpgt_epi32_mask": "_mm512_cmpgt_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4729 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpgt_epi64_mask": "_mm512_cmpgt_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4759 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpgt_epu32_mask": "_mm512_cmpgt_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4741 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpgt_epu64_mask": "_mm512_cmpgt_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4765 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmple_epi32_mask": "_mm512_cmple_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4777 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmple_epi64_mask": "_mm512_cmple_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4801 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmple_epu32_mask": "_mm512_cmple_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4789 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmple_epu64_mask": "_mm512_cmple_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4813 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmplt_epi32_mask": "_mm512_cmplt_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4825 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmplt_epi64_mask": "_mm512_cmplt_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4849 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmplt_epu32_mask": "_mm512_cmplt_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4837 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmplt_epu64_mask": "_mm512_cmplt_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4861 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpneq_epi32_mask": "_mm512_cmpneq_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4873 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpneq_epi64_mask": "_mm512_cmpneq_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4897 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpneq_epu32_mask": "_mm512_cmpneq_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4885 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cmpneq_epu64_mask": "_mm512_cmpneq_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4909 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi16_epi32": "_mm512_cvtepi16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4999 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi16_epi64": "_mm512_cvtepi16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5025 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi32_epi16": "_mm512_cvtepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7720 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi32_epi64": "_mm512_cvtepi32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4973 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi32_epi8": "_mm512_cvtepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7691 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi32_pd": "_mm512_cvtepi32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3682 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi32_ps": "_mm512_cvtepi32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3707 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi64_epi16": "_mm512_cvtepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7807 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi64_epi32": "_mm512_cvtepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7778 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi64_epi8": "_mm512_cvtepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7749 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi8_epi32": "_mm512_cvtepi8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4921 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepi8_epi64": "_mm512_cvtepi8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4947 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepu16_epi32": "_mm512_cvtepu16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5129 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_cvtepu16_epi64": "_mm512_cvtepu16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5155 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-031.json

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
