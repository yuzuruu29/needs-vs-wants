# Node Description Batch 94 of 111

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

- "clang_include_intrin_rotl": "_rotl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L467 | neighbors=[intrin.h]
- "clang_include_intrin_rotl16": "_rotl16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L457 | neighbors=[intrin.h]
- "clang_include_intrin_rotl64": "_rotl64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L487 | neighbors=[intrin.h]
- "clang_include_intrin_rotl8": "_rotl8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L447 | neighbors=[intrin.h]
- "clang_include_intrin_rotr": "_rotr()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L472 | neighbors=[intrin.h]
- "clang_include_intrin_rotr16": "_rotr16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L462 | neighbors=[intrin.h]
- "clang_include_intrin_rotr64": "_rotr64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L493 | neighbors=[intrin.h]
- "clang_include_intrin_rotr8": "_rotr8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L452 | neighbors=[intrin.h]
- "clang_include_intrin_stosb": "__stosb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L856 | neighbors=[intrin.h]
- "clang_include_intrin_stosd": "__stosd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L861 | neighbors=[intrin.h]
- "clang_include_intrin_stosq": "__stosq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L878 | neighbors=[intrin.h]
- "clang_include_intrin_stosw": "__stosw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L866 | neighbors=[intrin.h]
- "clang_include_intrin_umul128": "_umul128()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L416 | neighbors=[intrin.h]
- "clang_include_intrin_umulh": "__umulh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L424 | neighbors=[intrin.h]
- "clang_include_intrin_void": "void()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L775 | neighbors=[intrin.h]
- "clang_include_intrin_writecr3": "__writecr3()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L944 | neighbors=[intrin.h]
- "clang_include_intrin_xgetbv": "_xgetbv()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L907 | neighbors=[intrin.h]
- "clang_include_inttypes": "inttypes.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/inttypes.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_iso646": "iso646.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/iso646.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_limits": "limits.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/limits.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_lzcntintrin_lzcnt_u32": "_lzcnt_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/lzcntintrin.h:L46 | neighbors=[lzcntintrin.h]
- "clang_include_lzcntintrin_lzcnt_u64": "_lzcnt_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/lzcntintrin.h:L59 | neighbors=[lzcntintrin.h]
- "clang_include_lzcntintrin_lzcnt16": "__lzcnt16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/lzcntintrin.h:L34 | neighbors=[lzcntintrin.h]
- "clang_include_lzcntintrin_lzcnt32": "__lzcnt32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/lzcntintrin.h:L40 | neighbors=[lzcntintrin.h]
- "clang_include_lzcntintrin_lzcnt64": "__lzcnt64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/lzcntintrin.h:L53 | neighbors=[lzcntintrin.h]
- "clang_include_mm_malloc_mm_free": "_mm_free()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm_malloc.h:L68 | neighbors=[mm_malloc.h]
- "clang_include_mm3dnow_m_pavgusb": "_m_pavgusb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L40 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pf2id": "_m_pf2id()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L45 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pf2iw": "_m_pf2iw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L139 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pfacc": "_m_pfacc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L50 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pfadd": "_m_pfadd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L55 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pfcmpeq": "_m_pfcmpeq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L60 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pfcmpge": "_m_pfcmpge()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L65 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pfcmpgt": "_m_pfcmpgt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L70 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pfmax": "_m_pfmax()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L75 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pfmin": "_m_pfmin()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L80 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pfmul": "_m_pfmul()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L85 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pfnacc": "_m_pfnacc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L144 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pfpnacc": "_m_pfpnacc()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L149 | neighbors=[mm3dnow.h]
- "clang_include_mm3dnow_m_pfrcp": "_m_pfrcp()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L90 | neighbors=[mm3dnow.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-093.json

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
