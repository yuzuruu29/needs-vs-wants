# Node Description Batch 84 of 111

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

- "clang_include_clang_cuda_intrinsics_funnelshift_rc": "__funnelshift_rc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_intrinsics.h:L311 | neighbors=[__clang_cuda_intrinsics.h]
- "clang_include_clang_cuda_intrinsics_ldg": "__ldg()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_intrinsics.h:L103 | neighbors=[__clang_cuda_intrinsics.h]
- "clang_include_clang_cuda_math_forward_declares_std": "std()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_math_forward_declares.h:L186 | neighbors=[__clang_cuda_math_forward_declares.h]
- "clang_include_clang_cuda_runtime_wrapper_assert_fail": "__assert_fail()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L251 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_brkpt": "__brkpt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L184 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_cospi": "cospi()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L173 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_erfcinv": "erfcinv()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L177 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_erfcx": "erfcx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L180 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_normcdf": "normcdf()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L179 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_normcdfinv": "normcdfinv()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L178 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_rcbrt": "rcbrt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L171 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_rsqrt": "rsqrt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L170 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_sincospi": "sincospi()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L174 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_sinpi": "sinpi()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L172 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_std": "std()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L263 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clang_cuda_runtime_wrapper_uint3": "uint3()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L273 | neighbors=[__clang_cuda_runtime_wrapper.h]
- "clang_include_clflushoptintrin_mm_clflushopt": "_mm_clflushopt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/clflushoptintrin.h:L34 | neighbors=[clflushoptintrin.h]
- "clang_include_cpuid_get_cpuid": "__get_cpuid()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/cpuid.h:L174 | neighbors=[cpuid.h]
- "clang_include_cpuid_get_cpuid_max": "__get_cpuid_max()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/cpuid.h:L181 | neighbors=[cpuid.h]
- "clang_include_cuda_builtin_vars": "cuda_builtin_vars.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/cuda_builtin_vars.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_emmintrin_mm_add_epi16": "_mm_add_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L658 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_add_epi32": "_mm_add_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L664 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_add_epi64": "_mm_add_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L676 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_add_epi8": "_mm_add_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L652 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_add_pd": "_mm_add_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L59 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_add_sd": "_mm_add_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L52 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_add_si64": "_mm_add_si64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L670 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_adds_epi16": "_mm_adds_epi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L688 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_adds_epi8": "_mm_adds_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L682 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_adds_epu16": "_mm_adds_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L700 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_adds_epu8": "_mm_adds_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L694 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_and_pd": "_mm_and_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L141 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_and_si128": "_mm_and_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1024 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_andnot_pd": "_mm_andnot_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L147 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_andnot_si128": "_mm_andnot_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1044 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_avg_epu16": "_mm_avg_epu16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L712 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_avg_epu8": "_mm_avg_epu8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L706 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_castpd_ps": "_mm_castpd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2412 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_castpd_si128": "_mm_castpd_si128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2418 | neighbors=[emmintrin.h]
- "clang_include_emmintrin_mm_castps_pd": "_mm_castps_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L2424 | neighbors=[emmintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-083.json

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
