# Node Description Batch 2 of 111

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
Write every description in English (en). Do not switch languages.
No marketing language.
Respond ONLY with a JSON object mapping each node id (as a string) to its
one-sentence description — no prose, no markdown fences.

- "clang_include_pmmintrin": "pmmintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/pmmintrin.h:L1 | neighbors=[_mm_addsub_pd(), _mm_addsub_ps(), _mm_hadd_pd(), _mm_hadd_ps(), _mm_hsub_pd(), _mm_hsub_ps()]
- "android_binder_auto_utils": "binder_auto_utils.h" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L1 | neighbors=[fromExceptionCode(), fromExceptionCodeWithMessage(), fromServiceSpecificError(), fromServiceSpecificErrorWithMessage(), fromStatus(), getExceptionCode()]
- "pad_parts_pad_hydratelivesheet": "hydrateLiveSheet()" | kind=code-symbol | source=website/_pad-parts/pad.js:L264 | neighbors=[pad.js, buildFlipLeaves(), go(), buildFlat(), liveOverlay(), renderSheetMarkup()]
- "branch:repo:github.com/yuzuruu29/needs-vs-wants#main": "main" | kind=Branch | source=git | neighbors=[1d5373c feat: polish daily budget UX an…, 317b882 feat: show daily budget meter o…, 3afdbde feat: store optional daily_budg…, 524eae1 feat: confirm before sealing ov…, 5f770b1 chore: pre-reformat backup 2026…, 72b1659 feat: add DailyBudgetUseCase wi…]
- "clang_include_avx512vbmiintrin": "avx512vbmiintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmiintrin.h:L1 | neighbors=[_mm512_mask2_permutex2var_epi8(), _mm512_mask_multishift_epi64_epi8(), _mm512_mask_permutex2var_epi8(), _mm512_mask_permutexvar_epi8(), _mm512_maskz_multishift_epi64_epi8(), _mm512_maskz_permutex2var_epi8()]
- "pad_parts_pad_go": "go()" | kind=code-symbol | source=website/_pad-parts/pad.js:L499 | neighbors=[pad.js, announce(), buildFlat(), buildFlipLeaves(), captureDraft(), clearFlipSafety()]
- "clang_include_bmi2intrin": "bmi2intrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmi2intrin.h:L1 | neighbors=[_bzhi_u32(), _bzhi_u64(), __DEFAULT_FN_ATTRS(), _mulx_u64(), _pdep_u32(), _pdep_u64()]
- "pad_parts_apply": "apply.js" | kind=code-symbol | source=website/_pad-parts/apply.js:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…, css, file, fs, html, padHtml]
- "pad_parts_pad_jumpto": "jumpTo()" | kind=code-symbol | source=website/_pad-parts/pad.js:L557 | neighbors=[pad.js, announce(), clearFlipSafety(), flipTo(), freezeLiveSheet(), hydrateLiveSheet()]
- "pad_parts_pad_settleflip": "settleFlip()" | kind=code-symbol | source=website/_pad-parts/pad.js:L317 | neighbors=[pad.js, flipTo(), announce(), clearFlipSafety(), hydrateLiveSheet(), poseAllLeaves()]
- "clang_include_adxintrin": "adxintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/adxintrin.h:L1 | neighbors=[_addcarry_u32(), _addcarry_u64(), _addcarryx_u32(), _addcarryx_u64(), _subborrow_u32(), _subborrow_u64()]
- "clang_include_avx512ifmaintrin": "avx512ifmaintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmaintrin.h:L1 | neighbors=[_mm512_madd52hi_epu64(), _mm512_madd52lo_epu64(), _mm512_mask_madd52hi_epu64(), _mm512_mask_madd52lo_epu64(), _mm512_maskz_madd52hi_epu64(), _mm512_maskz_madd52lo_epu64()]
- "clang_include_shaintrin": "shaintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/shaintrin.h:L1 | neighbors=[_mm_sha1msg1_epu32(), _mm_sha1msg2_epu32(), _mm_sha1nexte_epu32(), _mm_sha256msg1_epu32(), _mm_sha256msg2_epu32(), _mm_sha256rnds2_epu32()]
- "pad_parts_check": "check.js" | kind=code-symbol | source=website/_pad-parts/check.js:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…, code, fs, html, i, j]
- "pad_parts_pad_updatechrome": "updateChrome()" | kind=code-symbol | source=website/_pad-parts/pad.js:L190 | neighbors=[pad.js, buildFlat(), go(), hydrateLiveSheet(), initPad(), jumpTo()]
- "clang_include_altivec_vec_lvlx": "vec_lvlx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L11031 | neighbors=[altivec.h, vec_ld(), vec_lvsl(), vec_perm(), vec_stvrx(), vec_stvrxl()]
- "clang_include_altivec_vec_lvrx": "vec_lvrx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L11243 | neighbors=[altivec.h, vec_ld(), vec_lvsl(), vec_perm(), vec_stvlx(), vec_stvlxl()]
- "clang_include_altivec_vec_lvsl": "vec_lvsl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2697 | neighbors=[altivec.h, vec_lvlx(), vec_lvlxl(), vec_lvrx(), vec_lvrxl(), vec_perm()]
- "clang_include_altivec_vec_lvsr": "vec_lvsr()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2818 | neighbors=[altivec.h, vec_perm(), vec_stvlx(), vec_stvlxl(), vec_stvrx(), vec_stvrxl()]
- "clang_include_clang_cuda_intrinsics": "__clang_cuda_intrinsics.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_intrinsics.h:L1 | neighbors=[__funnelshift_l(), __funnelshift_lc(), __funnelshift_r(), __funnelshift_rc(), __ldg(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_ia32intrin": "ia32intrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/ia32intrin.h:L1 | neighbors=[__rdpmc(), __rdtsc(), __rdtscp(), __readeflags(), __writeeflags(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_lzcntintrin": "lzcntintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/lzcntintrin.h:L1 | neighbors=[__lzcnt16(), __lzcnt32(), __lzcnt64(), _lzcnt_u32(), _lzcnt_u64(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_wmmintrin_aes": "__wmmintrin_aes.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__wmmintrin_aes.h:L1 | neighbors=[_mm_aesdec_si128(), _mm_aesdeclast_si128(), _mm_aesenc_si128(), _mm_aesenclast_si128(), _mm_aesimc_si128(), 5f770b1 chore: pre-reformat backup 2026…]
- "pad_parts_pad_buildflat": "buildFlat()" | kind=code-symbol | source=website/_pad-parts/pad.js:L468 | neighbors=[pad.js, updateChrome(), wireLiveForm(), go(), hydrateLiveSheet(), initPad()]
- "pad_parts_pad_buildflipleaves": "buildFlipLeaves()" | kind=code-symbol | source=website/_pad-parts/pad.js:L415 | neighbors=[pad.js, ensureStage(), hydrateLiveSheet(), poseAllLeaves(), go(), initPad()]
- "pad_parts_smoke": "smoke.mjs" | kind=code-symbol | source=website/_pad-parts/smoke.mjs:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…, errs, logs, mime, root, server]
- "clang_include_altivec_vec_stvlx": "vec_stvlx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L11455 | neighbors=[altivec.h, vec_lvrx(), vec_lvsr(), vec_perm(), vec_st()]
- "clang_include_altivec_vec_stvlxl": "vec_stvlxl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L11571 | neighbors=[altivec.h, vec_lvrx(), vec_lvsr(), vec_perm(), vec_stl()]
- "clang_include_altivec_vec_stvrx": "vec_stvrx()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L11688 | neighbors=[altivec.h, vec_lvlx(), vec_lvsr(), vec_perm(), vec_st()]
- "clang_include_altivec_vec_stvrxl": "vec_stvrxl()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L11804 | neighbors=[altivec.h, vec_lvlx(), vec_lvsr(), vec_perm(), vec_stl()]
- "clang_include_ammintrin": "ammintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/ammintrin.h:L1 | neighbors=[_mm_extract_si64(), _mm_insert_si64(), _mm_stream_sd(), _mm_stream_ss(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_arm_acle_rev16": "__rev16()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L177 | neighbors=[arm_acle.h, __rev(), __ror(), __rev16l(), __rev16ll()]
- "clang_include_fxsrintrin": "fxsrintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fxsrintrin.h:L1 | neighbors=[_fxrstor(), _fxrstor64(), _fxsave(), _fxsave64(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_htmintrin": "htmintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmintrin.h:L1 | neighbors=[__builtin_tbegin_retry_nofloat_null(), __builtin_tbegin_retry_nofloat_tdb(), __builtin_tbegin_retry_null(), __builtin_tbegin_retry_tdb(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_popcntintrin": "popcntintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/popcntintrin.h:L1 | neighbors=[_mm_popcnt_u32(), _mm_popcnt_u64(), _popcnt32(), _popcnt64(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_unwind": "unwind.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/unwind.h:L1 | neighbors=[_Unwind_GetGR(), _Unwind_GetIP(), _Unwind_SetGR(), _Unwind_SetIP(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_xmmintrin_mm_cvtpi16_ps": "_mm_cvtpi16_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2616 | neighbors=[xmmintrin.h, _mm_cvtpi32_ps(), _mm_movelh_ps(), _mm_cvtpi8_ps(), _mm_cvtpu8_ps()]
- "clang_include_xmmintrin_mm_cvtpi32_ps": "_mm_cvtpi32_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1515 | neighbors=[xmmintrin.h, _mm_cvt_pi2ps(), _mm_cvtpi16_ps(), _mm_cvtpi32x2_ps(), _mm_cvtpu16_ps()]
- "clang_include_xsaveintrin": "xsaveintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsaveintrin.h:L1 | neighbors=[_xrstor(), _xrstor64(), _xsave(), _xsave64(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_xsavesintrin": "xsavesintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xsavesintrin.h:L1 | neighbors=[_xrstors(), _xrstors64(), _xsaves(), _xsaves64(), 5f770b1 chore: pre-reformat backup 2026…]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-001.json

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
