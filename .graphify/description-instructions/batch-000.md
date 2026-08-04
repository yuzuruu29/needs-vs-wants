# Node Description Batch 1 of 111

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

- "clang_include_avx512vlintrin": "avx512vlintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L1 | neighbors=[__DEFAULT_FN_ATTRS(), _mm256_broadcast_f32x4(), _mm256_broadcast_i32x4(), _mm256_cmpeq_epi32_mask(), _mm256_cmpeq_epi64_mask(), _mm256_cmpeq_epu32_mask()]
- "clang_include_avx512fintrin": "avx512fintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512fintrin.h:L1 | neighbors=[__DEFAULT_FN_ATTRS(), _mm512_abs_epi32(), _mm512_abs_epi64(), _mm512_abs_pd(), _mm512_abs_ps(), _mm512_add_pd()]
- "clang_include_avx512vlbwintrin": "avx512vlbwintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlbwintrin.h:L1 | neighbors=[__DEFAULT_FN_ATTRS(), _mm256_cmpeq_epi16_mask(), _mm256_cmpeq_epi8_mask(), _mm256_cmpeq_epu16_mask(), _mm256_cmpeq_epu8_mask(), _mm256_cmpge_epi16_mask()]
- "clang_include_altivec": "altivec.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1 | neighbors=[__builtin_crypto_vcipher(), __builtin_crypto_vcipherlast(), __builtin_crypto_vncipher(), __builtin_crypto_vncipherlast(), __builtin_crypto_vpermxor(), __builtin_crypto_vpmsumb()]
- "clang_include_emmintrin": "emmintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/emmintrin.h:L1 | neighbors=[_mm_add_epi16(), _mm_add_epi32(), _mm_add_epi64(), _mm_add_epi8(), _mm_add_pd(), _mm_add_sd()]
- "clang_include_avx512bwintrin": "avx512bwintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512bwintrin.h:L1 | neighbors=[__DEFAULT_FN_ATTRS(), _mm512_broadcastb_epi8(), _mm512_broadcastw_epi16(), _mm512_cmpeq_epi16_mask(), _mm512_cmpeq_epi8_mask(), _mm512_cmpeq_epu16_mask()]
- "clang_include_vecintrin": "vecintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/vecintrin.h:L1 | neighbors=[__lcbb(), vec_abs(), vec_add_u128(), vec_addc(), vec_addc_u128(), vec_adde_u128()]
- "clang_include_avxintrin": "avxintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avxintrin.h:L1 | neighbors=[__DEFAULT_FN_ATTRS(), _mm256_add_pd(), _mm256_add_ps(), _mm256_addsub_pd(), _mm256_addsub_ps(), _mm256_and_pd()]
- "clang_include_avx2intrin": "avx2intrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx2intrin.h:L1 | neighbors=[_mm256_abs_epi16(), _mm256_abs_epi32(), _mm256_abs_epi8(), _mm256_add_epi16(), _mm256_add_epi32(), _mm256_add_epi64()]
- "clang_include_xmmintrin": "xmmintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1 | neighbors=[_mm_add_ps(), _mm_add_ss(), _mm_and_ps(), _mm_andnot_ps(), _mm_avg_pu16(), _mm_avg_pu8()]
- "clang_include_xopintrin": "xopintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xopintrin.h:L1 | neighbors=[_mm256_cmov_si256(), _mm256_frcz_pd(), _mm256_frcz_ps(), _mm_cmov_si128(), _mm_comeq_epi16(), _mm_comeq_epi32()]
- "clang_include_avx512vldqintrin": "avx512vldqintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vldqintrin.h:L1 | neighbors=[__DEFAULT_FN_ATTRS(), _mm256_broadcast_f32x2(), _mm256_broadcast_f64x2(), _mm256_broadcast_i32x2(), _mm256_broadcast_i64x2(), _mm256_cvtepi64_pd()]
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@5f770b1c103a5c10ff0659bf0033f610f1859676": "5f770b1 chore: pre-reformat backup 2026-08-03" | kind=Commit | source=git | neighbors=[1d5373c feat: polish daily budget UX an…, binder_auto_utils.h, binder_enums.h, binder_interface_utils.h, binder_internal_logging.h, binder_parcel_utils.h]
- "clang_include_intrin": "intrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/intrin.h:L1 | neighbors=[_BitScanForward(), _BitScanForward64(), _BitScanReverse(), _BitScanReverse64(), _bittest(), _bittest64()]
- "clang_include_avx512dqintrin": "avx512dqintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512dqintrin.h:L1 | neighbors=[__DEFAULT_FN_ATTRS(), _mm512_broadcast_f32x2(), _mm512_broadcast_f32x8(), _mm512_broadcast_f64x2(), _mm512_broadcast_i32x2(), _mm512_broadcast_i32x8()]
- "clang_include_mmintrin": "mmintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mmintrin.h:L1 | neighbors=[_mm_add_pi16(), _mm_add_pi32(), _mm_add_pi8(), _mm_adds_pi16(), _mm_adds_pi8(), _mm_adds_pu16()]
- "clang_include_tgmath": "tgmath.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tgmath.h:L1 | neighbors=[__tg_acos(), __tg_acosh(), __tg_asin(), __tg_asinh(), __tg_atan(), __tg_atan2()]
- "pad_parts_pad": "pad.js" | kind=code-symbol | source=website/_pad-parts/pad.js:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…, adapter, announce(), bindNav(), buildFlat(), buildFlipLeaves()]
- "android_binder_parcel_utils": "binder_parcel_utils.h" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L1 | neighbors=[AParcel_nullableStdArrayAllocator(), AParcel_nullableStdArrayExternalAllocat…, AParcel_nullableStdArraySetter(), AParcel_nullableStdArrayStringElementAl…, AParcel_nullableStdStringAllocator(), AParcel_nullableStdVectorAllocator()]
- "clang_include_clang_cuda_cmath": "__clang_cuda_cmath.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_cmath.h:L1 | neighbors=[abs(), acos(), asin(), atan(), atan2(), ceil()]
- "clang_include_arm_acle": "arm_acle.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/arm_acle.h:L1 | neighbors=[__clz(), __clzl(), __clzll(), __crc32b(), __crc32cb(), __crc32cd()]
- "clang_include_altivec_vec_perm": "vec_perm()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L5961 | neighbors=[altivec.h, vec_lvlx(), vec_lvlxl(), vec_lvrx(), vec_lvrxl(), vec_lvsl()]
- "clang_include_fma4intrin": "fma4intrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fma4intrin.h:L1 | neighbors=[_mm256_macc_pd(), _mm256_macc_ps(), _mm256_maddsub_pd(), _mm256_maddsub_ps(), _mm256_msub_pd(), _mm256_msub_ps()]
- "clang_include_fmaintrin": "fmaintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/fmaintrin.h:L1 | neighbors=[_mm256_fmadd_pd(), _mm256_fmadd_ps(), _mm256_fmaddsub_pd(), _mm256_fmaddsub_ps(), _mm256_fmsub_pd(), _mm256_fmsub_ps()]
- "clang_include_tmmintrin": "tmmintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tmmintrin.h:L1 | neighbors=[_mm_abs_epi16(), _mm_abs_epi32(), _mm_abs_epi8(), _mm_abs_pi16(), _mm_abs_pi32(), _mm_abs_pi8()]
- "clang_include_avx512vlcdintrin": "avx512vlcdintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlcdintrin.h:L1 | neighbors=[__DEFAULT_FN_ATTRS(), _mm256_broadcastmb_epi64(), _mm256_broadcastmw_epi32(), _mm256_conflict_epi32(), _mm256_conflict_epi64(), _mm256_lzcnt_epi32()]
- "clang_include_mm3dnow": "mm3dnow.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/mm3dnow.h:L1 | neighbors=[_m_pavgusb(), _m_pf2id(), _m_pf2iw(), _m_pfacc(), _m_pfadd(), _m_pfcmpeq()]
- "clang_include_smmintrin": "smmintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/smmintrin.h:L1 | neighbors=[__DEFAULT_FN_ATTRS(), _mm_cmpeq_epi64(), _mm_cmpgt_epi64(), _mm_crc32_u16(), _mm_crc32_u32(), _mm_crc32_u64()]
- "branch:repo:github.com/yuzuruu29/needs-vs-wants#feat/ios-native-rewrite": "feat/ios-native-rewrite" | kind=Branch | source=git | neighbors=[1795ac6 feat(ios-native): P0 scaffold —…, 2680d57 fix(ios-native): discover simul…, 2ed537a checkpoint before checking out …, 33294ab fix(ios-native): CI — erase+boo…, 3ed4802 fix(ios-native): CI — compile-o…, 50878cf chore(website): include release…]
- "clang_include_avx512vbmivlintrin": "avx512vbmivlintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vbmivlintrin.h:L1 | neighbors=[_mm256_mask2_permutex2var_epi8(), _mm256_mask_multishift_epi64_epi8(), _mm256_mask_permutex2var_epi8(), _mm256_mask_permutexvar_epi8(), _mm256_maskz_multishift_epi64_epi8(), _mm256_maskz_permutex2var_epi8()]
- "clang_include_htmxlintrin": "htmxlintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/htmxlintrin.h:L1 | neighbors=[__TM_abort(), __TM_begin(), __TM_end(), __TM_failure_address(), __TM_failure_code(), __TM_is_conflict()]
- "clang_include_tbmintrin": "tbmintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/tbmintrin.h:L1 | neighbors=[__blcfill_u32(), __blcfill_u64(), __blci_u32(), __blci_u64(), __blcic_u32(), __blcic_u64()]
- "branch:repo:github.com/yuzuruu29/needs-vs-wants#cursor/premium-surface-parity-45af": "cursor/premium-surface-parity-45af" | kind=Branch | source=git | neighbors=[1795ac6 feat(ios-native): P0 scaffold —…, 2680d57 fix(ios-native): discover simul…, 33294ab fix(ios-native): CI — erase+boo…, 3ed4802 fix(ios-native): CI — compile-o…, 5366128 feat(ios-native): P1 data layer…, 54901a6 Restore supermarket premium sur…]
- "clang_include_bmiintrin": "bmiintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/bmiintrin.h:L1 | neighbors=[__andn_u32(), __andn_u64(), __bextr_u32(), __bextr_u64(), __blsi_u32(), __blsi_u64()]
- "clang_include_avx512cdintrin": "avx512cdintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512cdintrin.h:L1 | neighbors=[_mm512_broadcastmb_epi64(), _mm512_broadcastmw_epi32(), _mm512_conflict_epi32(), _mm512_conflict_epi64(), _mm512_lzcnt_epi32(), _mm512_lzcnt_epi64()]
- "clang_include_immintrin": "immintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/immintrin.h:L1 | neighbors=[_bit_scan_forward(), _bit_scan_reverse(), _mm256_cvtph_ps(), _rdrand16_step(), _rdrand32_step(), _rdrand64_step()]
- "android_binder_interface_utils": "binder_interface_utils.h" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L1 | neighbors=[asBinder(), BnCInterface(), BpCInterface(), dump(), handleShellCommand(), internal()]
- "clang_include_avx512vlintrin_mm_setzero_di": "_mm_setzero_di()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512vlintrin.h:L34 | neighbors=[avx512vlintrin.h, __DEFAULT_FN_ATTRS(), _mm_maskz_load_epi64(), _mm_maskz_mov_epi64(), _mm_maskz_rolv_epi64(), _mm_maskz_rorv_epi64()]
- "clang_include_clang_cuda_runtime_wrapper": "__clang_cuda_runtime_wrapper.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/__clang_cuda_runtime_wrapper.h:L1 | neighbors=[__assert_fail(), __brkpt(), cospi(), erfcinv(), erfcx(), normcdf()]
- "clang_include_avx512ifmavlintrin": "avx512ifmavlintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/avx512ifmavlintrin.h:L1 | neighbors=[_mm256_madd52hi_epu64(), _mm256_madd52lo_epu64(), _mm256_mask_madd52hi_epu64(), _mm256_mask_madd52lo_epu64(), _mm256_maskz_madd52hi_epu64(), _mm256_maskz_madd52lo_epu64()]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-000.json

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
