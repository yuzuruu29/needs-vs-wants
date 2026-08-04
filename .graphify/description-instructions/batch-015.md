# Node Description Batch 16 of 111

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

- "clang_include_altivec_vec_vaddsws": "vec_vaddsws()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L630 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vaddubm": "vec_vaddubm()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L328 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vaddubs": "vec_vaddubs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L579 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vadduhm": "vec_vadduhm()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L362 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vadduhs": "vec_vadduhs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L613 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vadduqm": "vec_vadduqm()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L665 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vadduwm": "vec_vadduwm()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L396 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vadduws": "vec_vadduws()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L647 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vand": "vec_vand()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L906 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vandc": "vec_vandc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1259 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vavgsh": "vec_vavgsh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1470 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vavguh": "vec_vavguh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1477 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vbpermq": "vec_vbpermq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14436 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vcmpgefp": "vec_vcmpgefp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1720 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vcmpgtfp": "vec_vcmpgtfp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1769 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vcmpgtsb": "vec_vcmpgtsb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1727 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vcmpgtsh": "vec_vcmpgtsh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1741 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vcmpgtsw": "vec_vcmpgtsw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1755 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vcmpgtub": "vec_vcmpgtub()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1734 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vcmpgtuh": "vec_vcmpgtuh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1748 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vcmpgtuw": "vec_vcmpgtuw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1762 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vgbbd": "vec_vgbbd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14423 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmaxsb": "vec_vmaxsb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3167 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmaxsh": "vec_vmaxsh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3201 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmaxsw": "vec_vmaxsw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3235 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmaxub": "vec_vmaxub()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3184 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmaxuh": "vec_vmaxuh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3218 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmaxuw": "vec_vmaxuw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3252 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmhaddshs": "vec_vmhaddshs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3003 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmhraddshs": "vec_vmhraddshs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4186 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vminsb": "vec_vminsb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4015 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vminsh": "vec_vminsh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4049 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vminsw": "vec_vminsw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4083 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vminub": "vec_vminub()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4032 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vminuh": "vec_vminuh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4066 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vminuw": "vec_vminuw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4100 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmladduhm": "vec_vmladduhm()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4155 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmulesb": "vec_vmulesb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4445 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmuleub": "vec_vmuleub()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4456 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vmulosb": "vec_vmulosb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4547 | neighbors=[altivec.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-015.json

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
