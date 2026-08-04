# Node Description Batch 104 of 111

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

- "clang_include_vecintrin_vec_popcnt": "vec_popcnt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L5132 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_promote": "vec_promote()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L213 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_rl": "vec_rl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L5174 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_rli": "vec_rli()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L5220 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_round": "vec_round()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7399 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_roundc": "vec_roundc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7392 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_roundm": "vec_roundm()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7362 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_roundp": "vec_roundp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7347 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_roundz": "vec_roundz()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7377 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_scatter_element": "vec_scatter_element()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L701 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_slb": "vec_slb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L5535 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_sll": "vec_sll()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L5320 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_splat": "vec_splat()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1163 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_splat_s16": "vec_splat_s16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1249 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_splat_s32": "vec_splat_s32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1255 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_splat_s64": "vec_splat_s64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1261 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_splat_s8": "vec_splat_s8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1243 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_splat_u16": "vec_splat_u16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1275 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_splat_u32": "vec_splat_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1281 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_splat_u64": "vec_splat_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1287 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_splat_u8": "vec_splat_u8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1269 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_splats": "vec_splats()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1295 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_sqrt": "vec_sqrt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7288 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_srab": "vec_srab()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L5942 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_sral": "vec_sral()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L5768 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_srb": "vec_srb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6265 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_srl": "vec_srl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6050 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_st2f": "vec_st2f()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7303 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_store_len": "vec_store_len()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1017 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_sub_u128": "vec_sub_u128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7131 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_subc": "vec_subc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7138 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_subc_u128": "vec_subc_u128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7160 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_sube_u128": "vec_sube_u128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7167 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_subec_u128": "vec_subec_u128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7175 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_sum_u128": "vec_sum_u128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7195 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_sum2": "vec_sum2()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7183 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_sum4": "vec_sum4()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7207 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_test_mask": "vec_test_mask()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7219 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_trunc": "vec_trunc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L7384 | neighbors=[vecintrin.h]
- "clang_include_vecintrin_vec_unpackh": "vec_unpackh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1715 | neighbors=[vecintrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-103.json

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
