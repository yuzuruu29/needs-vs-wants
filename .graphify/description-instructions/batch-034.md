# Node Description Batch 35 of 111

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

- "clang_include_avx512fintrin_mm512_mask_cmplt_epu32_mask": "_mm512_mask_cmplt_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4843 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmplt_epu64_mask": "_mm512_mask_cmplt_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4867 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpneq_epi32_mask": "_mm512_mask_cmpneq_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4879 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpneq_epi64_mask": "_mm512_mask_cmpneq_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4903 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpneq_epu32_mask": "_mm512_mask_cmpneq_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4891 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cmpneq_epu64_mask": "_mm512_mask_cmpneq_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4915 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_compress_epi32": "_mm512_mask_compress_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8938 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_compress_epi64": "_mm512_mask_compress_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8904 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_compress_pd": "_mm512_mask_compress_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8887 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_compress_ps": "_mm512_mask_compress_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8921 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_compressstoreu_epi32": "_mm512_mask_compressstoreu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9300 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_compressstoreu_epi64": "_mm512_mask_compressstoreu_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9286 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_compressstoreu_pd": "_mm512_mask_compressstoreu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9279 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_compressstoreu_ps": "_mm512_mask_compressstoreu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L9293 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi16_epi32": "_mm512_mask_cvtepi16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5008 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi16_epi64": "_mm512_mask_cvtepi16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5034 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi32_epi16": "_mm512_mask_cvtepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7728 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi32_epi64": "_mm512_mask_cvtepi32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4982 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi32_epi8": "_mm512_mask_cvtepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7699 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi32_pd": "_mm512_mask_cvtepi32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3691 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi32_ps": "_mm512_mask_cvtepi32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3716 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi32_storeu_epi16": "_mm512_mask_cvtepi32_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7743 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi32_storeu_epi8": "_mm512_mask_cvtepi32_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7714 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi64_epi16": "_mm512_mask_cvtepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7815 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi64_epi32": "_mm512_mask_cvtepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7786 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi64_epi8": "_mm512_mask_cvtepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7757 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi64_storeu_epi16": "_mm512_mask_cvtepi64_storeu_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7830 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi64_storeu_epi32": "_mm512_mask_cvtepi64_storeu_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7801 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi64_storeu_epi8": "_mm512_mask_cvtepi64_storeu_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L7772 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi8_epi32": "_mm512_mask_cvtepi8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4930 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepi8_epi64": "_mm512_mask_cvtepi8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4956 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepu16_epi32": "_mm512_mask_cvtepu16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5138 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepu16_epi64": "_mm512_mask_cvtepu16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5164 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepu32_epi64": "_mm512_mask_cvtepu32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5112 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepu32_pd": "_mm512_mask_cvtepu32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3743 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepu32_ps": "_mm512_mask_cvtepu32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L3664 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepu8_epi32": "_mm512_mask_cvtepu8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5060 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtepu8_epi64": "_mm512_mask_cvtepu8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5086 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtpd_epi32": "_mm512_mask_cvtpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4027 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm512_mask_cvtpd_epu32": "_mm512_mask_cvtpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4115 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-034.json

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
