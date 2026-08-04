# Node Description Batch 15 of 111

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

- "clang_include_altivec_vec_packsu": "vec_packsu()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L5842 | neighbors=[altivec.h]
- "clang_include_altivec_vec_promote": "vec_promote()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L11921 | neighbors=[altivec.h]
- "clang_include_altivec_vec_re": "vec_re()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6262 | neighbors=[altivec.h]
- "clang_include_altivec_vec_rint": "vec_rint()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6380 | neighbors=[altivec.h]
- "clang_include_altivec_vec_rl": "vec_rl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6285 | neighbors=[altivec.h]
- "clang_include_altivec_vec_round": "vec_round()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6365 | neighbors=[altivec.h]
- "clang_include_altivec_vec_rsqrte": "vec_rsqrte()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6420 | neighbors=[altivec.h]
- "clang_include_altivec_vec_sel": "vec_sel()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6445 | neighbors=[altivec.h]
- "clang_include_altivec_vec_sll": "vec_sll()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7139 | neighbors=[altivec.h]
- "clang_include_altivec_vec_slo": "vec_slo()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7485 | neighbors=[altivec.h]
- "clang_include_altivec_vec_splat_s16": "vec_splat_s16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7913 | neighbors=[altivec.h]
- "clang_include_altivec_vec_splat_s32": "vec_splat_s32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7929 | neighbors=[altivec.h]
- "clang_include_altivec_vec_splat_s8": "vec_splat_s8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7895 | neighbors=[altivec.h]
- "clang_include_altivec_vec_splat_u16": "vec_splat_u16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7951 | neighbors=[altivec.h]
- "clang_include_altivec_vec_splat_u32": "vec_splat_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7959 | neighbors=[altivec.h]
- "clang_include_altivec_vec_splat_u8": "vec_splat_u8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7943 | neighbors=[altivec.h]
- "clang_include_altivec_vec_splats": "vec_splats()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L11969 | neighbors=[altivec.h]
- "clang_include_altivec_vec_sqrt": "vec_sqrt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6409 | neighbors=[altivec.h]
- "clang_include_altivec_vec_sr": "vec_sr()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7966 | neighbors=[altivec.h]
- "clang_include_altivec_vec_sra": "vec_sra()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L8056 | neighbors=[altivec.h]
- "clang_include_altivec_vec_srl": "vec_srl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L8136 | neighbors=[altivec.h]
- "clang_include_altivec_vec_sro": "vec_sro()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L8482 | neighbors=[altivec.h]
- "clang_include_altivec_vec_ste": "vec_ste()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L8925 | neighbors=[altivec.h]
- "clang_include_altivec_vec_stvebx": "vec_stvebx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L9001 | neighbors=[altivec.h]
- "clang_include_altivec_vec_stvehx": "vec_stvehx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L9023 | neighbors=[altivec.h]
- "clang_include_altivec_vec_stvewx": "vec_stvewx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L9055 | neighbors=[altivec.h]
- "clang_include_altivec_vec_stvx": "vec_stvx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L8793 | neighbors=[altivec.h]
- "clang_include_altivec_vec_stvxl": "vec_stvxl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L9213 | neighbors=[altivec.h]
- "clang_include_altivec_vec_sub": "vec_sub()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L9346 | neighbors=[altivec.h]
- "clang_include_altivec_vec_subc": "vec_subc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L9583 | neighbors=[altivec.h]
- "clang_include_altivec_vec_subs": "vec_subs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L9609 | neighbors=[altivec.h]
- "clang_include_altivec_vec_sum4s": "vec_sum4s()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L9857 | neighbors=[altivec.h]
- "clang_include_altivec_vec_trunc": "vec_trunc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L9967 | neighbors=[altivec.h]
- "clang_include_altivec_vec_unpackh": "vec_unpackh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L9993 | neighbors=[altivec.h]
- "clang_include_altivec_vec_unpackl": "vec_unpackl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10127 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vaddcuq": "vec_vaddcuq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L691 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vaddecuq": "vec_vaddecuq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L703 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vaddeuqm": "vec_vaddeuqm()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L677 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vaddsbs": "vec_vaddsbs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L562 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vaddshs": "vec_vaddshs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L596 | neighbors=[altivec.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-014.json

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
