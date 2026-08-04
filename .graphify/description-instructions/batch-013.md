# Node Description Batch 14 of 111

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

- "clang_include_altivec_vec_cmpeq": "vec_cmpeq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1535 | neighbors=[altivec.h]
- "clang_include_altivec_vec_cntlz": "vec_cntlz()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1887 | neighbors=[altivec.h]
- "clang_include_altivec_vec_cpsgn": "vec_cpsgn()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1924 | neighbors=[altivec.h]
- "clang_include_altivec_vec_ctf": "vec_ctf()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1937 | neighbors=[altivec.h]
- "clang_include_altivec_vec_cts": "vec_cts()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1978 | neighbors=[altivec.h]
- "clang_include_altivec_vec_ctu": "vec_ctu()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1999 | neighbors=[altivec.h]
- "clang_include_altivec_vec_div": "vec_div()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2040 | neighbors=[altivec.h]
- "clang_include_altivec_vec_double": "vec_double()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2022 | neighbors=[altivec.h]
- "clang_include_altivec_vec_dss": "vec_dss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2094 | neighbors=[altivec.h]
- "clang_include_altivec_vec_dssall": "vec_dssall()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2100 | neighbors=[altivec.h]
- "clang_include_altivec_vec_eqv": "vec_eqv()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2127 | neighbors=[altivec.h]
- "clang_include_altivec_vec_extract": "vec_extract()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10868 | neighbors=[altivec.h]
- "clang_include_altivec_vec_floor": "vec_floor()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2227 | neighbors=[altivec.h]
- "clang_include_altivec_vec_insert": "vec_insert()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10940 | neighbors=[altivec.h]
- "clang_include_altivec_vec_lde": "vec_lde()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2428 | neighbors=[altivec.h]
- "clang_include_altivec_vec_lvebx": "vec_lvebx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2462 | neighbors=[altivec.h]
- "clang_include_altivec_vec_lvehx": "vec_lvehx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2474 | neighbors=[altivec.h]
- "clang_include_altivec_vec_lvewx": "vec_lvewx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2486 | neighbors=[altivec.h]
- "clang_include_altivec_vec_lvx": "vec_lvx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2339 | neighbors=[altivec.h]
- "clang_include_altivec_vec_lvxl": "vec_lvxl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2591 | neighbors=[altivec.h]
- "clang_include_altivec_vec_madds": "vec_madds()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2996 | neighbors=[altivec.h]
- "clang_include_altivec_vec_max": "vec_max()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3027 | neighbors=[altivec.h]
- "clang_include_altivec_vec_mfvscr": "vec_mfvscr()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3868 | neighbors=[altivec.h]
- "clang_include_altivec_vec_min": "vec_min()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3875 | neighbors=[altivec.h]
- "clang_include_altivec_vec_mradds": "vec_mradds()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4179 | neighbors=[altivec.h]
- "clang_include_altivec_vec_msub": "vec_msub()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3012 | neighbors=[altivec.h]
- "clang_include_altivec_vec_msum": "vec_msum()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4193 | neighbors=[altivec.h]
- "clang_include_altivec_vec_msums": "vec_msums()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4249 | neighbors=[altivec.h]
- "clang_include_altivec_vec_mtvscr": "vec_mtvscr()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4278 | neighbors=[altivec.h]
- "clang_include_altivec_vec_mul": "vec_mul()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4328 | neighbors=[altivec.h]
- "clang_include_altivec_vec_mule": "vec_mule()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4387 | neighbors=[altivec.h]
- "clang_include_altivec_vec_mulo": "vec_mulo()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4489 | neighbors=[altivec.h]
- "clang_include_altivec_vec_nand": "vec_nand()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4592 | neighbors=[altivec.h]
- "clang_include_altivec_vec_nearbyint": "vec_nearbyint()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6390 | neighbors=[altivec.h]
- "clang_include_altivec_vec_nmadd": "vec_nmadd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4732 | neighbors=[altivec.h]
- "clang_include_altivec_vec_nmsub": "vec_nmsub()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4747 | neighbors=[altivec.h]
- "clang_include_altivec_vec_nor": "vec_nor()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4776 | neighbors=[altivec.h]
- "clang_include_altivec_vec_or": "vec_or()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4912 | neighbors=[altivec.h]
- "clang_include_altivec_vec_orc": "vec_orc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L5093 | neighbors=[altivec.h]
- "clang_include_altivec_vec_packs": "vec_packs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L5714 | neighbors=[altivec.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-013.json

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
