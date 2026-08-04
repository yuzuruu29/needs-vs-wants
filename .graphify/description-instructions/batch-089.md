# Node Description Batch 90 of 111

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

- "clang_include_fma4intrin_mm_nmacc_sd": "_mm_nmacc_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L102 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_nmacc_ss": "_mm_nmacc_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L96 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_nmsub_pd": "_mm_nmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L114 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_nmsub_ps": "_mm_nmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L108 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_nmsub_sd": "_mm_nmsub_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L126 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm_nmsub_ss": "_mm_nmsub_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L120 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_macc_pd": "_mm256_macc_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L162 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_macc_ps": "_mm256_macc_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L156 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_maddsub_pd": "_mm256_maddsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L210 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_maddsub_ps": "_mm256_maddsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L204 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_msub_pd": "_mm256_msub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L174 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_msub_ps": "_mm256_msub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L168 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_msubadd_pd": "_mm256_msubadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L222 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_msubadd_ps": "_mm256_msubadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L216 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_nmacc_pd": "_mm256_nmacc_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L186 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_nmacc_ps": "_mm256_nmacc_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L180 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_nmsub_pd": "_mm256_nmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L198 | neighbors=[fma4intrin.h]
- "clang_include_fma4intrin_mm256_nmsub_ps": "_mm256_nmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L192 | neighbors=[fma4intrin.h]
- "clang_include_fmaintrin_mm_fmadd_pd": "_mm_fmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L40 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fmadd_ps": "_mm_fmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L34 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fmadd_sd": "_mm_fmadd_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L52 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fmadd_ss": "_mm_fmadd_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L46 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fmaddsub_pd": "_mm_fmaddsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L136 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fmaddsub_ps": "_mm_fmaddsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L130 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fmsub_pd": "_mm_fmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L64 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fmsub_ps": "_mm_fmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L58 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fmsub_sd": "_mm_fmsub_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L76 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fmsub_ss": "_mm_fmsub_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L70 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fmsubadd_pd": "_mm_fmsubadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L148 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fmsubadd_ps": "_mm_fmsubadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L142 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fnmadd_pd": "_mm_fnmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L88 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fnmadd_ps": "_mm_fnmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L82 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fnmadd_sd": "_mm_fnmadd_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L100 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fnmadd_ss": "_mm_fnmadd_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L94 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fnmsub_pd": "_mm_fnmsub_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L112 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fnmsub_ps": "_mm_fnmsub_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L106 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fnmsub_sd": "_mm_fnmsub_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L124 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm_fnmsub_ss": "_mm_fnmsub_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L118 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm256_fmadd_pd": "_mm256_fmadd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L160 | neighbors=[fmaintrin.h]
- "clang_include_fmaintrin_mm256_fmadd_ps": "_mm256_fmadd_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L154 | neighbors=[fmaintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-089.json

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
