# Node Description Batch 80 of 111

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

- "clang_include_avxintrin_mm256_cvtepi32_pd": "_mm256_cvtepi32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2059 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_cvtepi32_ps": "_mm256_cvtepi32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2074 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_cvtpd_epi32": "_mm256_cvtpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2123 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_cvtpd_ps": "_mm256_cvtpd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2090 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_cvtps_epi32": "_mm256_cvtps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2105 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_cvtps_pd": "_mm256_cvtps_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2111 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_cvtsd_f64": "_mm256_cvtsd_f64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2135 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_cvtsi256_si32": "_mm256_cvtsi256_si32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2141 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_cvtss_f32": "_mm256_cvtss_f32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2148 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_cvttpd_epi32": "_mm256_cvttpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2117 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_cvttps_epi32": "_mm256_cvttps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2129 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_div_pd": "_mm256_div_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L178 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_div_ps": "_mm256_div_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L196 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_extract_epi16": "_mm256_extract_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L1889 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_extract_epi32": "_mm256_extract_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L1867 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_extract_epi64": "_mm256_extract_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L1934 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_extract_epi8": "_mm256_extract_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L1911 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_hadd_pd": "_mm256_hadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L684 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_hadd_ps": "_mm256_hadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L707 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_hsub_pd": "_mm256_hsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L730 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_hsub_ps": "_mm256_hsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L753 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_insert_epi16": "_mm256_insert_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L1987 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_insert_epi32": "_mm256_insert_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L1960 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_insert_epi64": "_mm256_insert_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2040 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_insert_epi8": "_mm256_insert_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2013 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_lddqu_si256": "_mm256_lddqu_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2395 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_load_pd": "_mm256_load_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2350 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_load_ps": "_mm256_load_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2356 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_load_si256": "_mm256_load_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2380 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_loadu_pd": "_mm256_loadu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2362 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_loadu_ps": "_mm256_loadu_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2371 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_loadu_si256": "_mm256_loadu_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2386 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_maskload_pd": "_mm256_maskload_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2454 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_maskload_ps": "_mm256_maskload_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2467 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_maskstore_pd": "_mm256_maskstore_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2486 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_maskstore_ps": "_mm256_maskstore_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2474 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_max_pd": "_mm256_max_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L215 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_max_ps": "_mm256_max_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L234 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_min_pd": "_mm256_min_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L253 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_min_ps": "_mm256_min_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L272 | neighbors=[avxintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-079.json

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
