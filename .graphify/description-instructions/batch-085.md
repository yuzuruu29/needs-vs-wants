# Node Description Batch 86 of 111

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

- "clang_include_emmintrin_mm_cvtpd_pi32": "_mm_cvtpd_pi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L450 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtpd_ps": "_mm_cvtpd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L385 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtpi32_pd": "_mm_cvtpi32_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L462 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtps_epi32": "_mm_cvtps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1739 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtps_pd": "_mm_cvtps_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L391 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtsd_f64": "_mm_cvtsd_f64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L468 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtsd_si32": "_mm_cvtsd_si32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L411 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtsd_si64": "_mm_cvtsd_si64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1690 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtsd_ss": "_mm_cvtsd_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L417 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtsi128_si32": "_mm_cvtsi128_si32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1806 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtsi128_si64": "_mm_cvtsi128_si64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1825 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtsi32_sd": "_mm_cvtsi32_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L424 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtsi32_si128": "_mm_cvtsi32_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1771 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtsi64_sd": "_mm_cvtsi64_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1672 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtsi64_si128": "_mm_cvtsi64_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1788 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvtss_sd": "_mm_cvtss_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L431 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvttpd_epi32": "_mm_cvttpd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L438 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvttpd_pi32": "_mm_cvttpd_pi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L456 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvttps_epi32": "_mm_cvttps_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1755 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvttsd_si32": "_mm_cvttsd_si32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L444 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_cvttsd_si64": "_mm_cvttsd_si64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1707 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_div_pd": "_mm_div_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L98 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_div_sd": "_mm_div_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L91 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_extract_epi16": "_mm_extract_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2280 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_insert_epi16": "_mm_insert_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2287 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_load_pd": "_mm_load_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L474 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_load_sd": "_mm_load_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L518 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_load_si128": "_mm_load_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1842 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_load1_pd": "_mm_load1_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L480 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_loadh_pd": "_mm_loadh_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L528 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_loadl_epi64": "_mm_loadl_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1879 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_loadl_pd": "_mm_loadl_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L538 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_loadr_pd": "_mm_loadr_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L492 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_loadu_pd": "_mm_loadu_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L499 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_loadu_si128": "_mm_loadu_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1858 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_loadu_si64": "_mm_loadu_si64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L508 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_madd_epi16": "_mm_madd_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L718 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_maskmoveu_si128": "_mm_maskmoveu_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2203 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_max_epi16": "_mm_max_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L724 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_max_epu8": "_mm_max_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L730 | neighbors=[emmintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-085.json

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
