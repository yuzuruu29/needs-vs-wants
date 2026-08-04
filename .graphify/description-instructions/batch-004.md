# Node Description Batch 5 of 111

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

- "clang_include_avx512fintrin_mm512_unpacklo_epi32": "_mm512_unpacklo_epi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4260 | neighbors=[avx512fintrin.h, _mm512_mask_unpacklo_epi32(), _mm512_maskz_unpacklo_epi32()] | lang=en
- "clang_include_avx512fintrin_mm512_unpacklo_epi64": "_mm512_unpacklo_epi64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4309 | neighbors=[avx512fintrin.h, _mm512_mask_unpacklo_epi64(), _mm512_maskz_unpacklo_epi64()] | lang=en
- "clang_include_avx512fintrin_mm512_unpacklo_pd": "_mm512_unpacklo_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4159 | neighbors=[avx512fintrin.h, _mm512_mask_unpacklo_pd(), _mm512_maskz_unpacklo_pd()] | lang=en
- "clang_include_avx512fintrin_mm512_unpacklo_ps": "_mm512_unpacklo_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L4208 | neighbors=[avx512fintrin.h, _mm512_mask_unpacklo_ps(), _mm512_maskz_unpacklo_ps()] | lang=en
- "clang_include_cpuid": "cpuid.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/cpuid.h:L1 | neighbors=[__get_cpuid(), __get_cpuid_max(), 5f770b1 chore: pre-reformat backup 2026…] | lang=en
- "clang_include_emmintrin_mm_store1_pd": "_mm_store1_pd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L605 | neighbors=[emmintrin.h, _mm_store_pd(), _mm_store_pd1()] | lang=en
- "clang_include_f16cintrin": "f16cintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/f16cintrin.h:L1 | neighbors=[_cvtsh_ss(), _mm_cvtph_ps(), 5f770b1 chore: pre-reformat backup 2026…] | lang=en
- "clang_include_mmintrin_mm_set_pi16": "_mm_set_pi16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mmintrin.h:L1314 | neighbors=[mmintrin.h, _mm_set1_pi16(), _mm_setr_pi16()] | lang=en
- "clang_include_mmintrin_mm_set_pi32": "_mm_set_pi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mmintrin.h:L1291 | neighbors=[mmintrin.h, _mm_set1_pi32(), _mm_setr_pi32()] | lang=en
- "clang_include_mmintrin_mm_set_pi8": "_mm_set_pi8()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mmintrin.h:L1345 | neighbors=[mmintrin.h, _mm_set1_pi8(), _mm_setr_pi8()] | lang=en
- "clang_include_mwaitxintrin": "mwaitxintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mwaitxintrin.h:L1 | neighbors=[_mm_monitorx(), _mm_mwaitx(), 5f770b1 chore: pre-reformat backup 2026…] | lang=en
- "clang_include_pkuintrin": "pkuintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/pkuintrin.h:L1 | neighbors=[void(), _wrpkru(), 5f770b1 chore: pre-reformat backup 2026…] | lang=en
- "clang_include_prfchwintrin": "prfchwintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/prfchwintrin.h:L1 | neighbors=[_m_prefetch(), _m_prefetchw(), 5f770b1 chore: pre-reformat backup 2026…] | lang=en
- "clang_include_unwind_unwind_getgr": "_Unwind_GetGR()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/unwind.h:L182 | neighbors=[unwind.h, _Unwind_GetIP(), _Unwind_SetIP()] | lang=en
- "clang_include_unwind_unwind_setip": "_Unwind_SetIP()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/unwind.h:L201 | neighbors=[unwind.h, _Unwind_GetGR(), _Unwind_SetGR()] | lang=en
- "clang_include_vecintrin_vec_abs": "vec_abs()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6373 | neighbors=[vecintrin.h, vec_cmplt(), vec_sel()] | lang=en
- "clang_include_vecintrin_vec_cmpgt": "vec_cmpgt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1923 | neighbors=[vecintrin.h, vec_max(), vec_min()] | lang=en
- "clang_include_vecintrin_vec_max": "vec_max()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6407 | neighbors=[vecintrin.h, vec_cmpgt(), vec_sel()] | lang=en
- "clang_include_vecintrin_vec_min": "vec_min()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L6550 | neighbors=[vecintrin.h, vec_cmpgt(), vec_sel()] | lang=en
- "clang_include_xmmintrin_mm_cvtpi32x2_ps": "_mm_cvtpi32x2_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2727 | neighbors=[xmmintrin.h, _mm_cvtpi32_ps(), _mm_movelh_ps()] | lang=en
- "clang_include_xmmintrin_mm_cvtps_pi32": "_mm_cvtps_pi32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1316 | neighbors=[xmmintrin.h, _mm_cvt_ps2pi(), _mm_cvtps_pi16()] | lang=en
- "clang_include_xmmintrin_mm_cvtpu16_ps": "_mm_cvtpu16_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2646 | neighbors=[xmmintrin.h, _mm_cvtpi32_ps(), _mm_movelh_ps()] | lang=en
- "clang_include_xmmintrin_mm_store_ps": "_mm_store_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1950 | neighbors=[xmmintrin.h, _mm_store1_ps(), _mm_storer_ps()] | lang=en
- "clang_include_xmmintrin_mm_store1_ps": "_mm_store1_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1969 | neighbors=[xmmintrin.h, _mm_store_ps(), _mm_store_ps1()] | lang=en
- "clang_include_xsavecintrin": "xsavecintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsavecintrin.h:L1 | neighbors=[_xsavec(), _xsavec64(), 5f770b1 chore: pre-reformat backup 2026…] | lang=en
- "clang_include_xsaveoptintrin": "xsaveoptintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsaveoptintrin.h:L1 | neighbors=[_xsaveopt(), _xsaveopt64(), 5f770b1 chore: pre-reformat backup 2026…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@1d5373cb287ee52f88405afc0f9402d10fea4887": "1d5373c feat: polish daily budget UX and share amount-input helpers" | kind=Commit | source=git | neighbors=[main, 5f770b1 chore: pre-reformat backup 2026…, 81b2279 chore: opt-in for ExperimentalC…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@2ed537afe5e12cd0a40f6756dbb8ba7fcb1775aa": "2ed537a checkpoint before checking out cursor/premium-surface-parity-45af" | kind=Commit | source=git | neighbors=[feat/ios-native-rewrite, 50878cf chore(website): include release…, 9374c35 feat(expo): add SDK 54 port wit…] | lang=pt
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@317b88249023d1a5cba02f1fac7a01c88b302caa": "317b882 feat: show daily budget meter on Summary Day" | kind=Commit | source=git | neighbors=[main, 524eae1 feat: confirm before sealing ov…, 85b4a42 feat: Settings UI to set and cl…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@3afdbde4cc89dd13b0139aae4e7aecac324e7950": "3afdbde feat: store optional daily_budget_cents in DataStore" | kind=Commit | source=git | neighbors=[main, 72b1659 feat: add DailyBudgetUseCase wi…, cecb87e test: add DailyBudgetMath helpe…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@50878cf22168a778adaf5b0483f5c01ca4fec8d3": "50878cf chore(website): include release APK in deployment" | kind=Commit | source=git | neighbors=[2ed537a checkpoint before checking out …, feat/ios-native-rewrite, b8cb239 checkpoint before checking out …] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@524eae1a0566a7ba38d9ad33a1a4458134468902": "524eae1 feat: confirm before sealing over daily budget" | kind=Commit | source=git | neighbors=[317b882 feat: show daily budget meter o…, main, 81b2279 chore: opt-in for ExperimentalC…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@54901a637da8a89eca9523ebb72696f33d8a8535": "54901a6 Restore supermarket premium surfaces on iOS; wire Android new-sheet" | kind=Commit | source=git | neighbors=[cursor/premium-surface-parity-45af, 5915fe5 checkpoint before checking out …, efd7a1f fix(ios-native): CI — use -sdk …] | lang=pt
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@72b1659c4712669b0bfcc209bdf29ac9af552e9a": "72b1659 feat: add DailyBudgetUseCase wired through Hilt" | kind=Commit | source=git | neighbors=[3afdbde feat: store optional daily_budg…, main, 85b4a42 feat: Settings UI to set and cl…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@81b2279a0691bc5e28ca08477826e5a86e461187": "81b2279 chore: opt-in for ExperimentalCoroutinesApi in SummaryViewModel" | kind=Commit | source=git | neighbors=[524eae1 feat: confirm before sealing ov…, main, 1d5373c feat: polish daily budget UX an…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@85b4a428c3e1e9448066e861989d3d9f06db5f5f": "85b4a42 feat: Settings UI to set and clear daily budget" | kind=Commit | source=git | neighbors=[72b1659 feat: add DailyBudgetUseCase wi…, main, 317b882 feat: show daily budget meter o…] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@9374c3586de0c614d0ac4874a232bde624a05741": "9374c35 feat(expo): add SDK 54 port with EAS Update for Expo Go" | kind=Commit | source=git | neighbors=[feat/ios-native-rewrite, 2ed537a checkpoint before checking out …, efd7a1f fix(ios-native): CI — use -sdk …] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@affab56fa59bf89f09eebd1c510f0189979c4409": "affab56 docs: daily budget design spec and implementation plan" | kind=Commit | source=git | neighbors=[main, cecb87e test: add DailyBudgetMath helpe…, d7cd41c feat: Needs vs Wants - Android …] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@b8cb239a912e5cd418dd537cc7e78187946afb01": "b8cb239 checkpoint before checking out main" | kind=Commit | source=git | neighbors=[50878cf chore(website): include release…, feat/ios-native-rewrite, f17aa71 fix(gitignore): ignore all APK …] | lang=en
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@cecb87ef605116b2b95876190176270d17913155": "cecb87e test: add DailyBudgetMath helpers and unit tests" | kind=Commit | source=git | neighbors=[affab56 docs: daily budget design spec …, main, 3afdbde feat: store optional daily_budg…] | lang=en

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-004.json

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
