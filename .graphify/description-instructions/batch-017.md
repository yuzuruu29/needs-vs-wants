# Node Description Batch 18 of 111

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

- "clang_include_altivec_vec_vsx_st": "vec_vsx_st()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10374 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vupkhsb": "vec_vupkhsb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10058 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vupkhsh": "vec_vupkhsh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10078 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vupkhsw": "vec_vupkhsw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10107 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vupklsb": "vec_vupklsb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10192 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vupklsh": "vec_vupklsh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10212 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vupklsw": "vec_vupklsw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10241 | neighbors=[altivec.h]
- "clang_include_altivec_vec_vxor": "vec_vxor()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L10701 | neighbors=[altivec.h]
- "clang_include_ammintrin_mm_extract_si64": "_mm_extract_si64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/ammintrin.h:L78 | neighbors=[ammintrin.h]
- "clang_include_ammintrin_mm_insert_si64": "_mm_insert_si64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/ammintrin.h:L150 | neighbors=[ammintrin.h]
- "clang_include_ammintrin_mm_stream_sd": "_mm_stream_sd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/ammintrin.h:L168 | neighbors=[ammintrin.h]
- "clang_include_ammintrin_mm_stream_ss": "_mm_stream_ss()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/ammintrin.h:L186 | neighbors=[ammintrin.h]
- "clang_include_arm_acle_clz": "__clz()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L141 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_clzl": "__clzl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L146 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_clzll": "__clzll()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L151 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_crc32b": "__crc32b()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L259 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_crc32cb": "__crc32cb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L279 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_crc32cd": "__crc32cd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L294 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_crc32ch": "__crc32ch()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L284 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_crc32cw": "__crc32cw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L289 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_crc32d": "__crc32d()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L274 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_crc32h": "__crc32h()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L264 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_crc32w": "__crc32w()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L269 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_nop": "__nop()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L107 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_qadd": "__qadd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L241 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_qdbl": "__qdbl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L251 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_qsub": "__qsub()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L246 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_revl": "__revl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L162 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_revll": "__revll()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L171 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_revsh": "__revsh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L197 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_sev": "__sev()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L56 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_sevl": "__sevl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L60 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_swp": "__swp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L74 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_wfe": "__wfe()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L52 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_wfi": "__wfi()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L48 | neighbors=[arm_acle.h]
- "clang_include_arm_acle_yield": "__yield()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L64 | neighbors=[arm_acle.h]
- "clang_include_avx2intrin_mm_broadcastb_epi8": "_mm_broadcastb_epi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L912 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_broadcastd_epi32": "_mm_broadcastd_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L925 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_broadcastq_epi64": "_mm_broadcastq_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L931 | neighbors=[avx2intrin.h]
- "clang_include_avx2intrin_mm_broadcastsd_pd": "_mm_broadcastsd_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L844 | neighbors=[avx2intrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-017.json

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
