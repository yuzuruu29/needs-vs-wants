# Node Description Batch 82 of 111

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

- "clang_include_avxintrin_mm256_stream_ps": "_mm256_stream_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2511 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_stream_si256": "_mm256_stream_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2499 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_sub_pd": "_mm256_sub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L104 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_sub_ps": "_mm256_sub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L122 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_testc_pd": "_mm256_testc_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2241 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_testc_ps": "_mm256_testc_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2259 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_testc_si256": "_mm256_testc_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2277 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_testnzc_pd": "_mm256_testnzc_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2247 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_testnzc_ps": "_mm256_testnzc_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2265 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_testnzc_si256": "_mm256_testnzc_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2283 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_testz_pd": "_mm256_testz_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2235 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_testz_ps": "_mm256_testz_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2253 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_testz_si256": "_mm256_testz_si256()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2271 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_unpackhi_pd": "_mm256_unpackhi_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2174 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_unpackhi_ps": "_mm256_unpackhi_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2186 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_unpacklo_pd": "_mm256_unpacklo_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2180 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_unpacklo_ps": "_mm256_unpacklo_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2192 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_xor_pd": "_mm256_xor_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L642 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_mm256_xor_ps": "_mm256_xor_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L660 | neighbors=[avxintrin.h]
- "clang_include_avxintrin_void": "void()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L2304 | neighbors=[avxintrin.h]
- "clang_include_bmi2intrin_bzhi_u32": "_bzhi_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmi2intrin.h:L34 | neighbors=[bmi2intrin.h]
- "clang_include_bmi2intrin_bzhi_u64": "_bzhi_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmi2intrin.h:L54 | neighbors=[bmi2intrin.h]
- "clang_include_bmi2intrin_default_fn_attrs": "__DEFAULT_FN_ATTRS()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmi2intrin.h:L83 | neighbors=[bmi2intrin.h]
- "clang_include_bmi2intrin_mulx_u64": "_mulx_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmi2intrin.h:L72 | neighbors=[bmi2intrin.h]
- "clang_include_bmi2intrin_pdep_u32": "_pdep_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmi2intrin.h:L40 | neighbors=[bmi2intrin.h]
- "clang_include_bmi2intrin_pdep_u64": "_pdep_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmi2intrin.h:L60 | neighbors=[bmi2intrin.h]
- "clang_include_bmi2intrin_pext_u32": "_pext_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmi2intrin.h:L46 | neighbors=[bmi2intrin.h]
- "clang_include_bmi2intrin_pext_u64": "_pext_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmi2intrin.h:L66 | neighbors=[bmi2intrin.h]
- "clang_include_bmiintrin_andn_u32": "__andn_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L171 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_andn_u64": "__andn_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L408 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_bextr_u32": "__bextr_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L193 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_bextr_u64": "__bextr_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L430 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_blsi_u32": "__blsi_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L234 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_blsi_u64": "__blsi_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L471 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_blsmsk_u32": "__blsmsk_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L251 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_blsmsk_u64": "__blsmsk_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L488 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_blsr_u32": "__blsr_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L268 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_blsr_u64": "__blsr_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L505 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_mm_tzcnt_32": "_mm_tzcnt_32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L300 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_mm_tzcnt_64": "_mm_tzcnt_64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L537 | neighbors=[bmiintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-081.json

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
