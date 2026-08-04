# Node Description Batch 92 of 111

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

- "clang_include_ia32intrin_writeeflags": "__writeeflags()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/ia32intrin.h:L38 | neighbors=[ia32intrin.h]
- "clang_include_immintrin_bit_scan_forward": "_bit_scan_forward()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L177 | neighbors=[immintrin.h]
- "clang_include_immintrin_bit_scan_reverse": "_bit_scan_reverse()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L183 | neighbors=[immintrin.h]
- "clang_include_immintrin_mm256_cvtph_ps": "_mm256_cvtph_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L75 | neighbors=[immintrin.h]
- "clang_include_immintrin_rdrand16_step": "_rdrand16_step()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L164 | neighbors=[immintrin.h]
- "clang_include_immintrin_rdrand32_step": "_rdrand32_step()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L170 | neighbors=[immintrin.h]
- "clang_include_immintrin_rdrand64_step": "_rdrand64_step()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L189 | neighbors=[immintrin.h]
- "clang_include_immintrin_readfsbase_u32": "_readfsbase_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L199 | neighbors=[immintrin.h]
- "clang_include_immintrin_readfsbase_u64": "_readfsbase_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L205 | neighbors=[immintrin.h]
- "clang_include_immintrin_readgsbase_u32": "_readgsbase_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L211 | neighbors=[immintrin.h]
- "clang_include_immintrin_readgsbase_u64": "_readgsbase_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L217 | neighbors=[immintrin.h]
- "clang_include_immintrin_writefsbase_u32": "_writefsbase_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L223 | neighbors=[immintrin.h]
- "clang_include_immintrin_writefsbase_u64": "_writefsbase_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L229 | neighbors=[immintrin.h]
- "clang_include_immintrin_writegsbase_u32": "_writegsbase_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L235 | neighbors=[immintrin.h]
- "clang_include_immintrin_writegsbase_u64": "_writegsbase_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L241 | neighbors=[immintrin.h]
- "clang_include_intrin_bitscanforward": "_BitScanForward()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L502 | neighbors=[intrin.h]
- "clang_include_intrin_bitscanforward64": "_BitScanForward64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L552 | neighbors=[intrin.h]
- "clang_include_intrin_bitscanreverse": "_BitScanReverse()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L509 | neighbors=[intrin.h]
- "clang_include_intrin_bitscanreverse64": "_BitScanReverse64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L559 | neighbors=[intrin.h]
- "clang_include_intrin_bittest": "_bittest()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L524 | neighbors=[intrin.h]
- "clang_include_intrin_bittest64": "_bittest64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L571 | neighbors=[intrin.h]
- "clang_include_intrin_bittestandcomplement": "_bittestandcomplement()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L528 | neighbors=[intrin.h]
- "clang_include_intrin_bittestandcomplement64": "_bittestandcomplement64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L575 | neighbors=[intrin.h]
- "clang_include_intrin_bittestandreset": "_bittestandreset()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L534 | neighbors=[intrin.h]
- "clang_include_intrin_bittestandreset64": "_bittestandreset64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L581 | neighbors=[intrin.h]
- "clang_include_intrin_bittestandset": "_bittestandset()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L540 | neighbors=[intrin.h]
- "clang_include_intrin_bittestandset64": "_bittestandset64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L587 | neighbors=[intrin.h]
- "clang_include_intrin_cpuid": "__cpuid()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L897 | neighbors=[intrin.h]
- "clang_include_intrin_cpuidex": "__cpuidex()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L902 | neighbors=[intrin.h]
- "clang_include_intrin_emul": "__emul()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L436 | neighbors=[intrin.h]
- "clang_include_intrin_emulu": "__emulu()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L440 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedand": "_InterlockedAnd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L675 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedand16": "_InterlockedAnd16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L671 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedand64": "_InterlockedAnd64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L680 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedand8": "_InterlockedAnd8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L667 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedbittestandset": "_interlockedbittestandset()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L546 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedbittestandset64": "_interlockedbittestandset64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L593 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedcompareexchange16": "_InterlockedCompareExchange16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L757 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedcompareexchange64": "_InterlockedCompareExchange64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L764 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedcompareexchange8": "_InterlockedCompareExchange8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L750 | neighbors=[intrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-091.json

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
