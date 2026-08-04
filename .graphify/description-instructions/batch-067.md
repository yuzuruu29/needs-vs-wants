# Node Description Batch 68 of 111

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

- "clang_include_avx512vlintrin_mm256_cmpge_epu32_mask": "_mm256_cmpge_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L174 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpge_epu64_mask": "_mm256_cmpge_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L222 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpgt_epi32_mask": "_mm256_cmpgt_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L258 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpgt_epi64_mask": "_mm256_cmpgt_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L306 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpgt_epu32_mask": "_mm256_cmpgt_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L270 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpgt_epu64_mask": "_mm256_cmpgt_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L318 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmple_epi32_mask": "_mm256_cmple_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L354 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmple_epi64_mask": "_mm256_cmple_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L402 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmple_epu32_mask": "_mm256_cmple_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L366 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmple_epu64_mask": "_mm256_cmple_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L414 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmplt_epi32_mask": "_mm256_cmplt_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L450 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmplt_epi64_mask": "_mm256_cmplt_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L498 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmplt_epu32_mask": "_mm256_cmplt_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L462 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmplt_epu64_mask": "_mm256_cmplt_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L510 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpneq_epi32_mask": "_mm256_cmpneq_epi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L546 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpneq_epi64_mask": "_mm256_cmpneq_epi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L594 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpneq_epu32_mask": "_mm256_cmpneq_epu32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L558 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cmpneq_epu64_mask": "_mm256_cmpneq_epu64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L606 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtepi32_epi16": "_mm256_cvtepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8271 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtepi32_epi8": "_mm256_cvtepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8213 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtepi64_epi16": "_mm256_cvtepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8446 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtepi64_epi32": "_mm256_cvtepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8387 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtepi64_epi8": "_mm256_cvtepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8329 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtepu32_pd": "_mm256_cvtepu32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2644 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtepu32_ps": "_mm256_cvtepu32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2690 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtpd_epu32": "_mm256_cvtpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2340 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtps_epu32": "_mm256_cvtps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2446 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtsepi32_epi16": "_mm256_cvtsepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7685 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtsepi32_epi8": "_mm256_cvtsepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7626 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtsepi64_epi16": "_mm256_cvtsepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7860 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtsepi64_epi32": "_mm256_cvtsepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7801 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtsepi64_epi8": "_mm256_cvtsepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7743 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvttpd_epu32": "_mm256_cvttpd_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2522 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvttps_epu32": "_mm256_cvttps_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L2598 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtusepi32_epi16": "_mm256_cvtusepi32_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7978 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtusepi32_epi8": "_mm256_cvtusepi32_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L7919 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtusepi64_epi16": "_mm256_cvtusepi64_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8154 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtusepi64_epi32": "_mm256_cvtusepi64_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8096 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_cvtusepi64_epi8": "_mm256_cvtusepi64_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L8037 | neighbors=[avx512vlintrin.h]
- "clang_include_avx512vlintrin_mm256_mask_abs_epi32": "_mm256_mask_abs_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L3351 | neighbors=[avx512vlintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-067.json

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
