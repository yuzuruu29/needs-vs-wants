# Node Description Batch 99 of 111

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

- "clang_include_tgmath_tg_atan2": "__tg_atan2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L560 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_atanh": "__tg_atanh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L210 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_carg": "__tg_carg()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1227 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_cbrt": "__tg_cbrt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L578 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_ceil": "__tg_ceil()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L595 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_cimag": "__tg_cimag()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1256 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_conj": "__tg_conj()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1286 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_copysign": "__tg_copysign()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L612 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_cos": "__tg_cos()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L239 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_cosh": "__tg_cosh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L326 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_cproj": "__tg_cproj()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1315 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_creal": "__tg_creal()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1343 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_erf": "__tg_erf()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L630 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_erfc": "__tg_erfc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L647 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_exp": "__tg_exp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L413 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_exp2": "__tg_exp2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L664 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_expm1": "__tg_expm1()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L681 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_fabs": "__tg_fabs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L531 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_fdim": "__tg_fdim()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L698 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_floor": "__tg_floor()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L716 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_fma": "__tg_fma()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L733 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_fmax": "__tg_fmax()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L756 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_fmin": "__tg_fmin()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L774 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_fmod": "__tg_fmod()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L792 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_frexp": "__tg_frexp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L810 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_hypot": "__tg_hypot()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L827 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_ilogb": "__tg_ilogb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L845 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_ldexp": "__tg_ldexp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L862 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_lgamma": "__tg_lgamma()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L879 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_llrint": "__tg_llrint()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L896 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_llround": "__tg_llround()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L913 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_log": "__tg_log()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L442 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_log10": "__tg_log10()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L930 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_log1p": "__tg_log1p()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L947 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_log2": "__tg_log2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L964 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_logb": "__tg_logb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L981 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_lrint": "__tg_lrint()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L998 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_lround": "__tg_lround()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1015 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_nearbyint": "__tg_nearbyint()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1032 | neighbors=[tgmath.h]
- "clang_include_tgmath_tg_nextafter": "__tg_nextafter()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1049 | neighbors=[tgmath.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-098.json

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
