# Node Description Batch 83 of 111

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

- "clang_include_bmiintrin_tzcnt_u16": "__tzcnt_u16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L152 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_tzcnt_u32": "__tzcnt_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L284 | neighbors=[bmiintrin.h]
- "clang_include_bmiintrin_tzcnt_u64": "__tzcnt_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L521 | neighbors=[bmiintrin.h]
- "clang_include_clang_cuda_cmath_acos": "acos()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L51 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_asin": "asin()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L52 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_atan": "atan()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L53 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_atan2": "atan2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L54 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_ceil": "ceil()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L55 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_cos": "cos()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L56 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_cosh": "cosh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L57 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_exp": "exp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L58 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_floor": "floor()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L60 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_fmod": "fmod()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L61 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_fpclassify": "fpclassify()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L62 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_frexp": "frexp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L70 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_isfinite": "isfinite()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L75 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_isgreater": "isgreater()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L77 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_isgreaterequal": "isgreaterequal()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L83 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_isinf": "isinf()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L73 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_isless": "isless()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L89 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_islessequal": "islessequal()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L95 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_islessgreater": "islessgreater()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L101 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_isnan": "isnan()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L107 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_isnormal": "isnormal()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L109 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_isunordered": "isunordered()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L111 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_ldexp": "ldexp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L117 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_log": "log()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L120 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_log10": "log10()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L121 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_modf": "modf()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L122 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_nexttoward": "nexttoward()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L123 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_pow": "pow()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L129 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_signbit": "signbit()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L138 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_sin": "sin()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L140 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_sinh": "sinh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L141 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_sqrt": "sqrt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L142 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_tan": "tan()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L143 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_cmath_tanh": "tanh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L144 | neighbors=[__clang_cuda_cmath.h]
- "clang_include_clang_cuda_intrinsics_funnelshift_l": "__funnelshift_l()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_intrinsics.h:L287 | neighbors=[__clang_cuda_intrinsics.h]
- "clang_include_clang_cuda_intrinsics_funnelshift_lc": "__funnelshift_lc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_intrinsics.h:L295 | neighbors=[__clang_cuda_intrinsics.h]
- "clang_include_clang_cuda_intrinsics_funnelshift_r": "__funnelshift_r()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_intrinsics.h:L303 | neighbors=[__clang_cuda_intrinsics.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-082.json

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
