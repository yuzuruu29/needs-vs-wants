# Node Description Batch 93 of 111

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

- "clang_include_intrin_interlockeddecrement16": "_InterlockedDecrement16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L654 | neighbors=[intrin.h]
- "clang_include_intrin_interlockeddecrement64": "_InterlockedDecrement64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L659 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedexchange16": "_InterlockedExchange16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L735 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedexchange64": "_InterlockedExchange64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L741 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedexchange8": "_InterlockedExchange8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L730 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedexchangeadd16": "_InterlockedExchangeAdd16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L607 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedexchangeadd64": "_InterlockedExchangeAdd64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L612 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedexchangeadd8": "_InterlockedExchangeAdd8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L603 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedexchangesub": "_InterlockedExchangeSub()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L628 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedexchangesub16": "_InterlockedExchangeSub16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L624 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedexchangesub64": "_InterlockedExchangeSub64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L633 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedexchangesub8": "_InterlockedExchangeSub8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L620 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedincrement16": "_InterlockedIncrement16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L641 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedincrement64": "_InterlockedIncrement64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L646 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedor": "_InterlockedOr()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L696 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedor16": "_InterlockedOr16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L692 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedor64": "_InterlockedOr64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L701 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedor8": "_InterlockedOr8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L688 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedxor": "_InterlockedXor()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L717 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedxor16": "_InterlockedXor16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L713 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedxor64": "_InterlockedXor64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L722 | neighbors=[intrin.h]
- "clang_include_intrin_interlockedxor8": "_InterlockedXor8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L709 | neighbors=[intrin.h]
- "clang_include_intrin_lrotl": "_lrotl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L477 | neighbors=[intrin.h]
- "clang_include_intrin_lrotr": "_lrotr()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L482 | neighbors=[intrin.h]
- "clang_include_intrin_movsb": "__movsb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L841 | neighbors=[intrin.h]
- "clang_include_intrin_movsd": "__movsd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L846 | neighbors=[intrin.h]
- "clang_include_intrin_movsq": "__movsq()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L873 | neighbors=[intrin.h]
- "clang_include_intrin_movsw": "__movsw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L851 | neighbors=[intrin.h]
- "clang_include_intrin_popcnt": "__popcnt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L520 | neighbors=[intrin.h]
- "clang_include_intrin_popcnt16": "__popcnt16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L516 | neighbors=[intrin.h]
- "clang_include_intrin_popcnt64": "__popcnt64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L566 | neighbors=[intrin.h]
- "clang_include_intrin_readcr3": "__readcr3()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L937 | neighbors=[intrin.h]
- "clang_include_intrin_readfsbyte": "__readfsbyte()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L805 | neighbors=[intrin.h]
- "clang_include_intrin_readfsqword": "__readfsqword()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L813 | neighbors=[intrin.h]
- "clang_include_intrin_readfsword": "__readfsword()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L809 | neighbors=[intrin.h]
- "clang_include_intrin_readgsbyte": "__readgsbyte()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L819 | neighbors=[intrin.h]
- "clang_include_intrin_readgsdword": "__readgsdword()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L827 | neighbors=[intrin.h]
- "clang_include_intrin_readgsqword": "__readgsqword()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L831 | neighbors=[intrin.h]
- "clang_include_intrin_readgsword": "__readgsword()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L823 | neighbors=[intrin.h]
- "clang_include_intrin_readmsr": "__readmsr()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L923 | neighbors=[intrin.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-092.json

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
