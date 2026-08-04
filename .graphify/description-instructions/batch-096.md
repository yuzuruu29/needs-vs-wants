# Node Description Batch 97 of 111

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

- "clang_include_pmmintrin_mm_hsub_ps": "_mm_hsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/pmmintrin.h:L110 | neighbors=[pmmintrin.h]
- "clang_include_pmmintrin_mm_lddqu_si128": "_mm_lddqu_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/pmmintrin.h:L45 | neighbors=[pmmintrin.h]
- "clang_include_pmmintrin_mm_monitor": "_mm_monitor()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/pmmintrin.h:L284 | neighbors=[pmmintrin.h]
- "clang_include_pmmintrin_mm_movedup_pd": "_mm_movedup_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/pmmintrin.h:L255 | neighbors=[pmmintrin.h]
- "clang_include_pmmintrin_mm_movehdup_ps": "_mm_movehdup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/pmmintrin.h:L132 | neighbors=[pmmintrin.h]
- "clang_include_pmmintrin_mm_moveldup_ps": "_mm_moveldup_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/pmmintrin.h:L154 | neighbors=[pmmintrin.h]
- "clang_include_pmmintrin_mm_mwait": "_mm_mwait()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/pmmintrin.h:L303 | neighbors=[pmmintrin.h]
- "clang_include_popcntintrin_mm_popcnt_u32": "_mm_popcnt_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/popcntintrin.h:L40 | neighbors=[popcntintrin.h]
- "clang_include_popcntintrin_mm_popcnt_u64": "_mm_popcnt_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/popcntintrin.h:L73 | neighbors=[popcntintrin.h]
- "clang_include_popcntintrin_popcnt32": "_popcnt32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/popcntintrin.h:L56 | neighbors=[popcntintrin.h]
- "clang_include_popcntintrin_popcnt64": "_popcnt64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/popcntintrin.h:L89 | neighbors=[popcntintrin.h]
- "clang_include_prfchwintrin_m_prefetch": "_m_prefetch()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/prfchwintrin.h:L32 | neighbors=[prfchwintrin.h]
- "clang_include_prfchwintrin_m_prefetchw": "_m_prefetchw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/prfchwintrin.h:L38 | neighbors=[prfchwintrin.h]
- "clang_include_rdseedintrin_rdseed16_step": "_rdseed16_step()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/rdseedintrin.h:L34 | neighbors=[rdseedintrin.h]
- "clang_include_rdseedintrin_rdseed32_step": "_rdseed32_step()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/rdseedintrin.h:L40 | neighbors=[rdseedintrin.h]
- "clang_include_rdseedintrin_rdseed64_step": "_rdseed64_step()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/rdseedintrin.h:L47 | neighbors=[rdseedintrin.h]
- "clang_include_rtmintrin_void": "void()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/rtmintrin.h:L44 | neighbors=[rtmintrin.h]
- "clang_include_s390intrin": "s390intrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/s390intrin.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_shaintrin_mm_sha1msg1_epu32": "_mm_sha1msg1_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/shaintrin.h:L43 | neighbors=[shaintrin.h]
- "clang_include_shaintrin_mm_sha1msg2_epu32": "_mm_sha1msg2_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/shaintrin.h:L49 | neighbors=[shaintrin.h]
- "clang_include_shaintrin_mm_sha1nexte_epu32": "_mm_sha1nexte_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/shaintrin.h:L37 | neighbors=[shaintrin.h]
- "clang_include_shaintrin_mm_sha256msg1_epu32": "_mm_sha256msg1_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/shaintrin.h:L61 | neighbors=[shaintrin.h]
- "clang_include_shaintrin_mm_sha256msg2_epu32": "_mm_sha256msg2_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/shaintrin.h:L67 | neighbors=[shaintrin.h]
- "clang_include_shaintrin_mm_sha256rnds2_epu32": "_mm_sha256rnds2_epu32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/shaintrin.h:L55 | neighbors=[shaintrin.h]
- "clang_include_smmintrin_default_fn_attrs": "__DEFAULT_FN_ATTRS()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L87 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cmpeq_epi64": "_mm_cmpeq_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L274 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cmpgt_epi64": "_mm_cmpgt_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L468 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_crc32_u16": "_mm_crc32_u16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L481 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_crc32_u32": "_mm_crc32_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L487 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_crc32_u64": "_mm_crc32_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L494 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_crc32_u8": "_mm_crc32_u8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L475 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cvtepi16_epi32": "_mm_cvtepi16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L305 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cvtepi16_epi64": "_mm_cvtepi16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L311 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cvtepi32_epi64": "_mm_cvtepi32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L317 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cvtepi8_epi16": "_mm_cvtepi8_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L281 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cvtepi8_epi32": "_mm_cvtepi8_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L289 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cvtepi8_epi64": "_mm_cvtepi8_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L297 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cvtepu16_epi32": "_mm_cvtepu16_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L342 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cvtepu16_epi64": "_mm_cvtepu16_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L348 | neighbors=[smmintrin.h]
- "clang_include_smmintrin_mm_cvtepu32_epi64": "_mm_cvtepu32_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L354 | neighbors=[smmintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-096.json

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
