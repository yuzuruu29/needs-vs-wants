# Node Description Batch 3 of 111

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
For an entity node (any other kind — e.g. a person, place, event, object),
describe what the entity is and its role, grounded in its type, its
relations (neighbors) and the provided citations/evidence — e.g.
"Lady Carfax, a wealthy heiress who disappears en route to Lausanne.".
Ground entity descriptions in the citations/evidence when present; do not
speculate beyond the context, so a node with no supporting context may be
left out of the reply.
LANGUAGE: each entry has a `lang=` marker giving the language of its source.
Write that entry's description in EXACTLY that language. Do not translate to
a single common language — match each node's source language individually.
No marketing language.
Respond ONLY with a JSON object mapping each node id (as a string) to its
one-sentence description — no prose, no markdown fences.

- "commit:repo:github.com/yuzuruu29/needs-vs-wants@d7cd41cd5fc42d8de9d0df49e7ce82d19395c506": "d7cd41c feat: Needs vs Wants - Android + iOS source with GitHub Actions CI" | kind=Commit | source=git | neighbors=[cursor/premium-surface-parity-45af, feat/ios-native-rewrite, main, 1795ac6 feat(ios-native): P0 scaffold —…, affab56 docs: daily budget design spec …] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@efd7a1f63d2489a0f83687099c0b30b57844a97c": "efd7a1f fix(ios-native): CI — use -sdk iphonesimulator (no destination needed)" | kind=Commit | source=git | neighbors=[3ed4802 fix(ios-native): CI — compile-o…, cursor/premium-surface-parity-45af, feat/ios-native-rewrite, 54901a6 Restore supermarket premium sur…, 9374c35 feat(expo): add SDK 54 port wit…] | lang=pt
- "pad_parts_pad_announce": "announce()" | kind=code-symbol | source=website/_pad-parts/pad.js:L54 | neighbors=[pad.js, go(), jumpTo(), settleFlip(), trySeal()] | lang=en
- "pad_parts_pad_initpad": "initPad()" | kind=code-symbol | source=website/_pad-parts/pad.js:L637 | neighbors=[pad.js, bindNav(), buildFlat(), buildFlipLeaves(), updateChrome()] | lang=en
- "pad_parts_pad_tryseal": "trySeal()" | kind=code-symbol | source=website/_pad-parts/pad.js:L343 | neighbors=[pad.js, announce(), hydrateLiveSheet(), rows(), setRows()] | lang=en
- "pad_parts_smoke2": "smoke2.mjs" | kind=code-symbol | source=website/_pad-parts/smoke2.mjs:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…, logs, mime, root, server] | lang=en
- "android_binder_parcel_utils_aparcel_nullablestdstringallocator": "AParcel_nullableStdStringAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L404 | neighbors=[binder_parcel_utils.h, AParcel_nullableStdArrayStringElementAl…, AParcel_nullableStdVectorStringElementA…, AParcel_stdArrayNullableStringElementAl…] | lang=en
- "clang_include_altivec_vec_lvlxl": "vec_lvlxl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L11137 | neighbors=[altivec.h, vec_ldl(), vec_lvsl(), vec_perm()] | lang=en
- "clang_include_altivec_vec_lvrxl": "vec_lvrxl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L11349 | neighbors=[altivec.h, vec_ldl(), vec_lvsl(), vec_perm()] | lang=en
- "clang_include_altivec_vec_sl": "vec_sl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6696 | neighbors=[altivec.h, vec_vslb(), vec_vslh(), vec_vslw()] | lang=en
- "clang_include_avx512fintrin_mm512_mask_and_epi32": "_mm512_mask_and_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L513 | neighbors=[avx512fintrin.h, _mm512_mask_abs_ps(), _mm512_and_epi32(), _mm512_maskz_and_epi32()] | lang=en
- "clang_include_avx512fintrin_mm512_mask_and_epi64": "_mm512_mask_and_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L534 | neighbors=[avx512fintrin.h, _mm512_mask_abs_pd(), _mm512_and_epi64(), _mm512_maskz_and_epi64()] | lang=en
- "clang_include_rdseedintrin": "rdseedintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/rdseedintrin.h:L1 | neighbors=[_rdseed16_step(), _rdseed32_step(), _rdseed64_step(), 5f770b1 chore: pre-reformat backup 2026…] | lang=en
- "clang_include_vecintrin_vec_sel": "vec_sel()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L476 | neighbors=[vecintrin.h, vec_abs(), vec_max(), vec_min()] | lang=en
- "clang_include_xmmintrin_mm_cvtps_pi16": "_mm_cvtps_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2755 | neighbors=[xmmintrin.h, _mm_cvtps_pi32(), _mm_movehl_ps(), _mm_cvtps_pi8()] | lang=en
- "clang_include_xmmintrin_mm_movelh_ps": "_mm_movelh_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2598 | neighbors=[xmmintrin.h, _mm_cvtpi16_ps(), _mm_cvtpi32x2_ps(), _mm_cvtpu16_ps()] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@1795ac6456b01f744cec9187d265f9a5cdb9a49d": "1795ac6 feat(ios-native): P0 scaffold — XcodeGen project, CI workflow, fonts, b…" | kind=Commit | source=git | neighbors=[cursor/premium-surface-parity-45af, feat/ios-native-rewrite, 5366128 feat(ios-native): P1 data layer…, d7cd41c feat: Needs vs Wants - Android …] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@2680d57a339dfff7885b64b4cab6edb33c22b0fa": "2680d57 fix(ios-native): discover simulator UDID, boot it, build+test against it" | kind=Commit | source=git | neighbors=[cursor/premium-surface-parity-45af, feat/ios-native-rewrite, 7d31a08 fix(ios-native): 3 compile erro…, dd6d095 fix(ios-native): build against …] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@33294ab9d3166e4d50911c5288162a2da630e39b": "33294ab fix(ios-native): CI — erase+boot+bootstatus sim, fallback to name-based…" | kind=Commit | source=git | neighbors=[cursor/premium-surface-parity-45af, feat/ios-native-rewrite, 3ed4802 fix(ios-native): CI — compile-o…, 6411eec fix(ios-native): view VM initia…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@3ed4802f86e4f027a606d8f2451f3c9c4942a80f": "3ed4802 fix(ios-native): CI — compile-only with Any iOS Simulator Device (no ru…" | kind=Commit | source=git | neighbors=[33294ab fix(ios-native): CI — erase+boo…, cursor/premium-surface-parity-45af, feat/ios-native-rewrite, efd7a1f fix(ios-native): CI — use -sdk …] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@53661287efe4294414bcecd67d1e59d6adc8d9c0": "5366128 feat(ios-native): P1 data layer — Entry, Repository, StatsEngine, Curre…" | kind=Commit | source=git | neighbors=[1795ac6 feat(ios-native): P0 scaffold —…, cursor/premium-surface-parity-45af, feat/ios-native-rewrite, f90f9d2 fix(ios-native): use macos-15 r…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@60807a9e284d112da3b2a46dd49d6edcb59a3e1d": "60807a9 fix(ios-native): CI — target iOS 18.x simulators only, create if missing" | kind=Commit | source=git | neighbors=[cursor/premium-surface-parity-45af, feat/ios-native-rewrite, 6411eec fix(ios-native): view VM initia…, 7d31a08 fix(ios-native): 3 compile erro…] | lang=pt
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@6411eec7c9071cda0d3ca4c4296fc79d0941f3ea": "6411eec fix(ios-native): view VM initialization — no optional @State, use repo …" | kind=Commit | source=git | neighbors=[60807a9 fix(ios-native): CI — target iO…, cursor/premium-surface-parity-45af, feat/ios-native-rewrite, 33294ab fix(ios-native): CI — erase+boo…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@6819d5d0e1aa70fb5ea131d6f83234f096a37f26": "6819d5d feat(ios-native): P2 design system + components" | kind=Commit | source=git | neighbors=[cursor/premium-surface-parity-45af, feat/ios-native-rewrite, ba60e4c fix(ios-native): robust CI — ge…, f90f9d2 fix(ios-native): use macos-15 r…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@7d31a08b9271a8115c18f51afff3fb1aae14669d": "7d31a08 fix(ios-native): 3 compile errors from CI" | kind=Commit | source=git | neighbors=[2680d57 fix(ios-native): discover simul…, cursor/premium-surface-parity-45af, feat/ios-native-rewrite, 60807a9 fix(ios-native): CI — target iO…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@ba60e4ca108fda54d9e37e146fd8501eb8ff08d5": "ba60e4c fix(ios-native): robust CI — generic build dest + auto-discover simulat…" | kind=Commit | source=git | neighbors=[6819d5d feat(ios-native): P2 design sys…, cursor/premium-surface-parity-45af, feat/ios-native-rewrite, bab2025 feat(ios-native): P3-P7 screens…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@bab2025f8030e3e249eb203dea600bbc860e4474": "bab2025 feat(ios-native): P3-P7 screens, app shell, ViewModel tests; fix simctl…" | kind=Commit | source=git | neighbors=[ba60e4c fix(ios-native): robust CI — ge…, cursor/premium-surface-parity-45af, feat/ios-native-rewrite, dd6d095 fix(ios-native): build against …] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@dd6d095f056427dfe828dbdd2aaa758a41db07e8": "dd6d095 fix(ios-native): build against iphoneos SDK (no simulator runtime neede…" | kind=Commit | source=git | neighbors=[bab2025 feat(ios-native): P3-P7 screens…, cursor/premium-surface-parity-45af, feat/ios-native-rewrite, 2680d57 fix(ios-native): discover simul…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@f90f9d20ef102b973d94dde46548c7d61bbe4059": "f90f9d2 fix(ios-native): use macos-15 runner + Xcode 16 for project format 77" | kind=Commit | source=git | neighbors=[5366128 feat(ios-native): P1 data layer…, cursor/premium-surface-parity-45af, feat/ios-native-rewrite, 6819d5d feat(ios-native): P2 design sys…] | lang=en
- "pad_parts_debug": "debug.mjs" | kind=code-symbol | source=website/_pad-parts/debug.mjs:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…, mime, root, server] | lang=en
- "pad_parts_pad_clearflipsafety": "clearFlipSafety()" | kind=code-symbol | source=website/_pad-parts/pad.js:L241 | neighbors=[pad.js, go(), jumpTo(), settleFlip()] | lang=en
- "pad_parts_pad_flipto": "flipTo()" | kind=code-symbol | source=website/_pad-parts/pad.js:L584 | neighbors=[pad.js, settleFlip(), go(), jumpTo()] | lang=en
- "pad_parts_pad_freezelivesheet": "freezeLiveSheet()" | kind=code-symbol | source=website/_pad-parts/pad.js:L247 | neighbors=[pad.js, captureDraft(), go(), jumpTo()] | lang=en
- "pad_parts_pad_prunetrailingempty": "pruneTrailingEmpty()" | kind=code-symbol | source=website/_pad-parts/pad.js:L230 | neighbors=[pad.js, go(), jumpTo(), settleFlip()] | lang=en
- "pad_parts_pad_rendersheetmarkup": "renderSheetMarkup()" | kind=code-symbol | source=website/_pad-parts/pad.js:L60 | neighbors=[pad.js, hydrateLiveSheet(), dateLabel(), esc()] | lang=en
- "pad_parts_smoke3": "smoke3.mjs" | kind=code-symbol | source=website/_pad-parts/smoke3.mjs:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…, mime, root, server] | lang=en
- "android_binder_interface_utils_asbinder": "asBinder()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L195 | neighbors=[binder_interface_utils.h, dump(), SharedRefBase()] | lang=en
- "android_binder_interface_utils_dump": "dump()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L228 | neighbors=[binder_interface_utils.h, asBinder(), onDump()] | lang=en
- "android_binder_parcel_utils_aparcel_stdstringallocator": "AParcel_stdStringAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L390 | neighbors=[binder_parcel_utils.h, AParcel_stdArrayStringElementAllocator(), AParcel_stdVectorStringElementAllocator…] | lang=en
- "clang_include_altivec_vec_cmpge": "vec_cmpge()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1660 | neighbors=[altivec.h, vec_cmpgt(), vec_cmple()] | lang=en

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-002.json

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
