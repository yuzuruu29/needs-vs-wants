# Node Description Batch 102 of 111

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

- "clang_include_vecintrin_vec_checksum": "vec_checksum()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6787 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpeq": "vec_cmpeq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1809 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpeq_idx": "vec_cmpeq_idx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7511 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpeq_idx_cc": "vec_cmpeq_idx_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7567 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpeq_or_0_idx": "vec_cmpeq_or_0_idx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7625 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpeq_or_0_idx_cc": "vec_cmpeq_or_0_idx_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7681 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpge": "vec_cmpge()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1876 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmple": "vec_cmple()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1970 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpne_idx": "vec_cmpne_idx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7742 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpne_idx_cc": "vec_cmpne_idx_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7798 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpne_or_0_idx": "vec_cmpne_or_0_idx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7856 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpne_or_0_idx_cc": "vec_cmpne_or_0_idx_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7912 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpnrg": "vec_cmpnrg()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8093 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpnrg_cc": "vec_cmpnrg_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8113 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpnrg_idx": "vec_cmpnrg_idx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8133 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpnrg_idx_cc": "vec_cmpnrg_idx_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8153 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpnrg_or_0_idx": "vec_cmpnrg_or_0_idx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8173 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmpnrg_or_0_idx_cc": "vec_cmpnrg_or_0_idx_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8193 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmprg": "vec_cmprg()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7973 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmprg_cc": "vec_cmprg_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7993 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmprg_idx": "vec_cmprg_idx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8013 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmprg_idx_cc": "vec_cmprg_idx_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8033 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmprg_or_0_idx": "vec_cmprg_or_0_idx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8053 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cmprg_or_0_idx_cc": "vec_cmprg_or_0_idx_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8073 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cntlz": "vec_cntlz()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L5048 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cnttz": "vec_cnttz()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L5090 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cp_until_zero": "vec_cp_until_zero()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7411 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_cp_until_zero_cc": "vec_cp_until_zero_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7458 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_ctd": "vec_ctd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7311 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_ctsl": "vec_ctsl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7329 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_ctul": "vec_ctul()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7338 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_extend_s64": "vec_extend_s64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1342 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_extract": "vec_extract()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L64 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_find_any_eq": "vec_find_any_eq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8213 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_find_any_eq_cc": "vec_find_any_eq_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8272 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_find_any_eq_idx": "vec_find_any_eq_idx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8335 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_find_any_eq_idx_cc": "vec_find_any_eq_idx_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8391 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_find_any_eq_or_0_idx": "vec_find_any_eq_or_0_idx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8454 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_find_any_eq_or_0_idx_cc": "vec_find_any_eq_or_0_idx_cc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8510 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_find_any_ne": "vec_find_any_ne()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L8575 | neighbors=[vecintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-101.json

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
