# Node Description Batch 91 of 111

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

- "clang_include_fmaintrin_mm256_fmaddsub_pd": "_mm256_fmaddsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L208 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm256_fmaddsub_ps": "_mm256_fmaddsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L202 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm256_fmsub_pd": "_mm256_fmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L172 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm256_fmsub_ps": "_mm256_fmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L166 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm256_fmsubadd_pd": "_mm256_fmsubadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L220 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm256_fmsubadd_ps": "_mm256_fmsubadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L214 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm256_fnmadd_pd": "_mm256_fnmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L184 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm256_fnmadd_ps": "_mm256_fnmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L178 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm256_fnmsub_pd": "_mm256_fnmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L196 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm256_fnmsub_ps": "_mm256_fnmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L190 | neighbors=[fmaintrin.h]
- "clang_include_fxsrintrin_fxrstor": "_fxrstor()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fxsrintrin.h:L43 | neighbors=[fxsrintrin.h]
- "clang_include_fxsrintrin_fxrstor64": "_fxrstor64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fxsrintrin.h:L48 | neighbors=[fxsrintrin.h]
- "clang_include_fxsrintrin_fxsave": "_fxsave()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fxsrintrin.h:L33 | neighbors=[fxsrintrin.h]
- "clang_include_fxsrintrin_fxsave64": "_fxsave64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fxsrintrin.h:L38 | neighbors=[fxsrintrin.h]
- "clang_include_htmintrin_builtin_tbegin_retry_nofloat_null": "__builtin_tbegin_retry_nofloat_null()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmintrin.h:L195 | neighbors=[htmintrin.h]
- "clang_include_htmintrin_builtin_tbegin_retry_nofloat_tdb": "__builtin_tbegin_retry_nofloat_tdb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmintrin.h:L207 | neighbors=[htmintrin.h]
- "clang_include_htmintrin_builtin_tbegin_retry_null": "__builtin_tbegin_retry_null()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmintrin.h:L166 | neighbors=[htmintrin.h]
- "clang_include_htmintrin_builtin_tbegin_retry_tdb": "__builtin_tbegin_retry_tdb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmintrin.h:L178 | neighbors=[htmintrin.h]
- "clang_include_htmxlintrin_tm_abort": "__TM_abort()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L89 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_begin": "__TM_begin()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L63 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_end": "__TM_end()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L80 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_failure_address": "__TM_failure_address()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L195 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_failure_code": "__TM_failure_code()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L202 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_is_conflict": "__TM_is_conflict()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L177 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_is_failure_persistent": "__TM_is_failure_persistent()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L187 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_is_footprint_exceeded": "__TM_is_footprint_exceeded()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L143 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_is_illegal": "__TM_is_illegal()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L135 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_is_named_user_abort": "__TM_is_named_user_abort()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L125 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_is_nested_too_deep": "__TM_is_nested_too_deep()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L169 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_is_user_abort": "__TM_is_user_abort()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L117 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_named_abort": "__TM_named_abort()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L96 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_nesting_depth": "__TM_nesting_depth()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L151 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_non_transactional_store": "__TM_non_transactional_store()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L253 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_resume": "__TM_resume()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L103 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_simple_begin": "__TM_simple_begin()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L54 | neighbors=[htmxlintrin.h]
- "clang_include_htmxlintrin_tm_suspend": "__TM_suspend()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L110 | neighbors=[htmxlintrin.h]
- "clang_include_ia32intrin_rdpmc": "__rdpmc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/ia32intrin.h:L58 | neighbors=[ia32intrin.h]
- "clang_include_ia32intrin_rdtsc": "__rdtsc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/ia32intrin.h:L64 | neighbors=[ia32intrin.h]
- "clang_include_ia32intrin_rdtscp": "__rdtscp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/ia32intrin.h:L70 | neighbors=[ia32intrin.h]
- "clang_include_ia32intrin_readeflags": "__readeflags()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/ia32intrin.h:L32 | neighbors=[ia32intrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-090.json

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
