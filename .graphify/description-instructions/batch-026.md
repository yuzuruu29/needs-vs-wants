# Node Description Batch 27 of 111

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

- "clang_include_avx512bwintrin_mm512_packus_epi16": "_mm512_packus_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L600 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_packus_epi32": "_mm512_packus_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L572 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_permutex2var_epi16": "_mm512_permutex2var_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1170 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_permutexvar_epi16": "_mm512_permutexvar_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2334 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_shuffle_epi8": "_mm512_shuffle_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1020 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_test_epi16_mask": "_mm512_test_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2197 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_test_epi8_mask": "_mm512_test_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2182 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_testn_epi16_mask": "_mm512_testn_epi16_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2227 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_mm512_testn_epi8_mask": "_mm512_testn_epi8_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L2212 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512bwintrin_void": "void()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L38 | neighbors=[avx512bwintrin.h]
- "clang_include_avx512cdintrin_mm512_broadcastmb_epi64": "_mm512_broadcastmb_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L130 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_broadcastmw_epi32": "_mm512_broadcastmw_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L136 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_conflict_epi32": "_mm512_conflict_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L58 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_conflict_epi64": "_mm512_conflict_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L34 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_lzcnt_epi32": "_mm512_lzcnt_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L82 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_lzcnt_epi64": "_mm512_lzcnt_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L106 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_mask_conflict_epi32": "_mm512_mask_conflict_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L66 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_mask_conflict_epi64": "_mm512_mask_conflict_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L42 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_mask_lzcnt_epi32": "_mm512_mask_lzcnt_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L90 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_mask_lzcnt_epi64": "_mm512_mask_lzcnt_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L114 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_maskz_conflict_epi32": "_mm512_maskz_conflict_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L74 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_maskz_conflict_epi64": "_mm512_maskz_conflict_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L50 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_maskz_lzcnt_epi32": "_mm512_maskz_lzcnt_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L98 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512cdintrin_mm512_maskz_lzcnt_epi64": "_mm512_maskz_lzcnt_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L122 | neighbors=[avx512cdintrin.h]
- "clang_include_avx512dqintrin_default_fn_attrs": "__DEFAULT_FN_ATTRS()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L56 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_broadcast_f32x2": "_mm512_broadcast_f32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1008 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_broadcast_f32x8": "_mm512_broadcast_f32x8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1032 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_broadcast_f64x2": "_mm512_broadcast_f64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1056 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_broadcast_i32x2": "_mm512_broadcast_i32x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1080 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_broadcast_i32x8": "_mm512_broadcast_i32x8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1104 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_broadcast_i64x2": "_mm512_broadcast_i64x2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1129 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_cvtepi64_pd": "_mm512_cvtepi64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L397 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_cvtepi64_ps": "_mm512_cvtepi64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L436 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_cvtepu64_pd": "_mm512_cvtepu64_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L632 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_cvtepu64_ps": "_mm512_cvtepu64_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L673 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_cvtpd_epi64": "_mm512_cvtpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L240 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_cvtpd_epu64": "_mm512_cvtpd_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L279 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_cvtps_epi64": "_mm512_cvtps_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L318 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_cvtps_epu64": "_mm512_cvtps_epu64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L357 | neighbors=[avx512dqintrin.h]
- "clang_include_avx512dqintrin_mm512_cvttpd_epi64": "_mm512_cvttpd_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L476 | neighbors=[avx512dqintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-026.json

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
