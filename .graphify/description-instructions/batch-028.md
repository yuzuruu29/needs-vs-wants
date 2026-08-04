# Node Description Batch 29 of 111

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

- "clang_include_avx512dqintrin_mm512_maskz_cvtepu64_pd": "_mm512_maskz_cvtepu64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L648 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_cvtepu64_ps": "_mm512_maskz_cvtepu64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L689 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_cvtpd_epi64": "_mm512_maskz_cvtpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L256 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_cvtpd_epu64": "_mm512_maskz_cvtpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L295 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_cvtps_epi64": "_mm512_maskz_cvtps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L334 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_cvtps_epu64": "_mm512_maskz_cvtps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L373 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_cvttpd_epi64": "_mm512_maskz_cvttpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L492 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_cvttpd_epu64": "_mm512_maskz_cvttpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L531 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_cvttps_epi64": "_mm512_maskz_cvttps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L570 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_cvttps_epu64": "_mm512_maskz_cvttps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L609 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_mullo_epi64": "_mm512_maskz_mullo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L47 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_or_pd": "_mm512_maskz_or_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L113 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_or_ps": "_mm512_maskz_or_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L135 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_xor_pd": "_mm512_maskz_xor_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L69 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_maskz_xor_ps": "_mm512_maskz_xor_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L91 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_movepi32_mask": "_mm512_movepi32_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L983 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_movepi64_mask": "_mm512_movepi64_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1001 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_mullo_epi64": "_mm512_mullo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L34 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512erintrin": "avx512erintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512erintrin.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_avx512fintrin_default_fn_attrs": "__DEFAULT_FN_ATTRS()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L344 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_cvtsd_u32": "_mm_cvtsd_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6324 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_cvtsd_u64": "_mm_cvtsd_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6335 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_cvtss_u32": "_mm_cvtss_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6358 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_cvtss_u64": "_mm_cvtss_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6369 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_cvttsd_i64": "_mm_cvttsd_i64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6396 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_cvttsd_u32": "_mm_cvttsd_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6406 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_cvttsd_u64": "_mm_cvttsd_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6417 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_cvttss_i64": "_mm_cvttss_i64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6444 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_cvttss_u32": "_mm_cvttss_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6454 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_cvttss_u64": "_mm_cvttss_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L6465 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_add_sd": "_mm_mask_add_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1943 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_add_ss": "_mm_mask_add_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1907 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_div_sd": "_mm_mask_div_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2376 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_div_ss": "_mm_mask_div_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L2340 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_fnmadd_sd": "_mm_mask_fnmadd_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8564 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_fnmadd_ss": "_mm_mask_fnmadd_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8372 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_fnmsub_sd": "_mm_mask_fnmsub_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8612 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_fnmsub_ss": "_mm_mask_fnmsub_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L8420 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_getexp_sd": "_mm_mask_getexp_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5733 | neighbors=[avx512fintrin.h]
- "clang_include_avx512fintrin_mm_mask_getexp_ss": "_mm_mask_getexp_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L5778 | neighbors=[avx512fintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-028.json

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
