# Graph Report - .  (2026-08-03)

## Corpus Check
- Large corpus: 243 files · ~1,132,841 words. Semantic extraction will be expensive (many Claude tokens). Consider running on a subfolder, or use --no-semantic to run AST-only.

## Summary
- 4432 nodes · 4742 edges · 86 communities detected
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS
- Token cost: 0 input · 0 output
- Edge kinds: contains: 4296 · calls: 265 · MODIFIES: 100 · ON_BRANCH: 49 · PARENT_OF: 32


## Input Scope
- Requested: auto
- Resolved: committed (source: default-auto)
- Included files: 243 · Candidates: 5101
- Excluded: 40 untracked · 14333 ignored · 19 sensitive · 2 missing committed
- Recommendation: Use --scope all or graphify.yaml inputs.corpus for a knowledge-base folder.

## Graph Freshness
- Built from Git commit: `5f770b1`
- Compare this hash to `git rev-parse HEAD` before trusting freshness-sensitive graph output.
## God Nodes (most connected - your core abstractions)
1. `vec_perm()` - 33 edges
2. `_mm_setzero_di()` - 14 edges
3. `hydrateLiveSheet()` - 12 edges
4. `go()` - 11 edges
5. `settleFlip()` - 8 edges
6. `jumpTo()` - 8 edges
7. `updateChrome()` - 7 edges
8. `vec_lvsl()` - 6 edges
9. `vec_lvsr()` - 6 edges
10. `vec_lvlx()` - 6 edges

## Surprising Connections (you probably didn't know these)
- `1d5373c feat: polish daily budget UX and share amount-input helpers` --PARENT_OF--> `5f770b1 chore: pre-reformat backup 2026-08-03`  [EXTRACTED]
  git → git  _Bridges community 21 → community 16_

## Communities

### Community 16 - "Community 16"
Cohesion: 0.04
Nodes (1): 5f770b1 chore: pre-reformat backup 2026-08-03

### Community 17 - "Community 17"
Cohesion: 0.11
Nodes (38): adapter, announce(), bindNav(), buildFlat(), buildFlipLeaves(), captureDraft(), clearFlipSafety(), dateLabel() (+30 more)

### Community 18 - "Community 18"
Cohesion: 0.06
Nodes (7): AParcel_nullableStdArrayStringElementAllocator(), AParcel_nullableStdStringAllocator(), AParcel_nullableStdVectorStringElementAllocator(), AParcel_stdArrayNullableStringElementAllocator(), AParcel_stdArrayStringElementAllocator(), AParcel_stdStringAllocator(), AParcel_stdVectorStringElementAllocator()

### Community 19 - "Community 19"
Cohesion: 0.08
Nodes (37): vec_ld(), vec_ldl(), vec_lvlx(), vec_lvlxl(), vec_lvrx(), vec_lvrxl(), vec_lvsl(), vec_lvsr() (+29 more)

### Community 20 - "Community 20"
Cohesion: 0.06
Nodes (2): abs(), fabs()

### Community 21 - "Community 21"
Cohesion: 0.13
Nodes (35): cursor/premium-surface-parity-45af, feat/ios-native-rewrite, main, 1795ac6 feat(ios-native): P0 scaffold — XcodeGen project, CI workflow, fonts, build docs, 1d5373c feat: polish daily budget UX and share amount-input helpers, 2680d57 fix(ios-native): discover simulator UDID, boot it, build+test against it, 2ed537a checkpoint before checking out cursor/premium-surface-parity-45af, 317b882 feat: show daily budget meter on Summary Day (+27 more)

### Community 22 - "Community 22"
Cohesion: 0.07
Nodes (10): __rbit(), __rbitl(), __rbitll(), __rev(), __rev16(), __rev16l(), __rev16ll(), __ror() (+2 more)

### Community 35 - "Community 35"
Cohesion: 0.18
Nodes (4): asBinder(), dump(), onDump(), SharedRefBase()

### Community 36 - "Community 36"
Cohesion: 0.14
Nodes (14): __DEFAULT_FN_ATTRS(), _mm_maskz_load_epi64(), _mm_maskz_mov_epi64(), _mm_maskz_rolv_epi64(), _mm_maskz_rorv_epi64(), _mm_maskz_sll_epi64(), _mm_maskz_sllv_epi64(), _mm_maskz_sra_epi64() (+6 more)

### Community 43 - "Community 43"
Cohesion: 0.22
Nodes (8): css, file, fs, html, padHtml, padJs, path, root

### Community 44 - "Community 44"
Cohesion: 0.32
Nodes (8): _mm_cvt_pi2ps(), _mm_cvtpi16_ps(), _mm_cvtpi32_ps(), _mm_cvtpi32x2_ps(), _mm_cvtpi8_ps(), _mm_cvtpu16_ps(), _mm_cvtpu8_ps(), _mm_movelh_ps()

### Community 48 - "Community 48"
Cohesion: 0.29
Nodes (6): code, fs, html, i, j, path

### Community 49 - "Community 49"
Cohesion: 0.40
Nodes (6): _mm512_abs_pd(), _mm512_and_epi64(), _mm512_mask_abs_pd(), _mm512_mask_and_epi64(), _mm512_maskz_and_epi64(), _mm512_set1_epi64()

### Community 50 - "Community 50"
Cohesion: 0.40
Nodes (6): _mm512_abs_ps(), _mm512_and_epi32(), _mm512_mask_abs_ps(), _mm512_mask_and_epi32(), _mm512_maskz_and_epi32(), _mm512_set1_epi32()

### Community 54 - "Community 54"
Cohesion: 0.40
Nodes (6): vec_abs(), vec_cmpgt(), vec_cmplt(), vec_max(), vec_min(), vec_sel()

### Community 56 - "Community 56"
Cohesion: 0.33
Nodes (5): errs, logs, mime, root, server

### Community 61 - "Community 61"
Cohesion: 0.70
Nodes (4): _Unwind_GetGR(), _Unwind_GetIP(), _Unwind_SetGR(), _Unwind_SetIP()

### Community 62 - "Community 62"
Cohesion: 0.40
Nodes (5): _mm_cvt_ps2pi(), _mm_cvtps_pi16(), _mm_cvtps_pi32(), _mm_cvtps_pi8(), _mm_movehl_ps()

### Community 65 - "Community 65"
Cohesion: 0.40
Nodes (4): logs, mime, root, server

### Community 66 - "Community 66"
Cohesion: 0.50
Nodes (4): vec_cmpge(), vec_cmpgt(), vec_cmple(), vec_cmplt()

### Community 67 - "Community 67"
Cohesion: 0.50
Nodes (4): vec_sl(), vec_vslb(), vec_vslh(), vec_vslw()

### Community 69 - "Community 69"
Cohesion: 0.50
Nodes (4): _mm_store1_ps(), _mm_store_ps(), _mm_store_ps1(), _mm_storer_ps()

### Community 70 - "Community 70"
Cohesion: 0.50
Nodes (3): mime, root, server

### Community 71 - "Community 71"
Cohesion: 0.50
Nodes (3): mime, root, server

### Community 72 - "Community 72"
Cohesion: 0.67
Nodes (3): _mm512_broadcastb_epi8(), _mm512_mask_broadcastb_epi8(), _mm512_maskz_broadcastb_epi8()

### Community 73 - "Community 73"
Cohesion: 0.67
Nodes (3): _mm512_broadcastw_epi16(), _mm512_mask_broadcastw_epi16(), _mm512_maskz_broadcastw_epi16()

### Community 74 - "Community 74"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpackhi_epi16(), _mm512_maskz_unpackhi_epi16(), _mm512_unpackhi_epi16()

### Community 75 - "Community 75"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpackhi_epi8(), _mm512_maskz_unpackhi_epi8(), _mm512_unpackhi_epi8()

### Community 76 - "Community 76"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpacklo_epi16(), _mm512_maskz_unpacklo_epi16(), _mm512_unpacklo_epi16()

### Community 77 - "Community 77"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpacklo_epi8(), _mm512_maskz_unpacklo_epi8(), _mm512_unpacklo_epi8()

### Community 78 - "Community 78"
Cohesion: 0.67
Nodes (3): _mm512_andnot_epi32(), _mm512_mask_andnot_epi32(), _mm512_maskz_andnot_epi32()

### Community 79 - "Community 79"
Cohesion: 0.67
Nodes (3): _mm512_andnot_epi64(), _mm512_mask_andnot_epi64(), _mm512_maskz_andnot_epi64()

### Community 80 - "Community 80"
Cohesion: 0.67
Nodes (3): _mm512_broadcastd_epi32(), _mm512_mask_broadcastd_epi32(), _mm512_maskz_broadcastd_epi32()

### Community 81 - "Community 81"
Cohesion: 0.67
Nodes (3): _mm512_broadcastq_epi64(), _mm512_mask_broadcastq_epi64(), _mm512_maskz_broadcastq_epi64()

### Community 82 - "Community 82"
Cohesion: 0.67
Nodes (3): _mm512_broadcastsd_pd(), _mm512_mask_broadcastsd_pd(), _mm512_maskz_broadcastsd_pd()

### Community 83 - "Community 83"
Cohesion: 0.67
Nodes (3): _mm512_broadcastss_ps(), _mm512_mask_broadcastss_ps(), _mm512_maskz_broadcastss_ps()

### Community 84 - "Community 84"
Cohesion: 0.67
Nodes (3): _mm512_mask_movehdup_ps(), _mm512_maskz_movehdup_ps(), _mm512_movehdup_ps()

### Community 85 - "Community 85"
Cohesion: 0.67
Nodes (3): _mm512_mask_moveldup_ps(), _mm512_maskz_moveldup_ps(), _mm512_moveldup_ps()

### Community 86 - "Community 86"
Cohesion: 0.67
Nodes (3): _mm512_mask_or_epi32(), _mm512_maskz_or_epi32(), _mm512_or_epi32()

### Community 87 - "Community 87"
Cohesion: 0.67
Nodes (3): _mm512_mask_or_epi64(), _mm512_maskz_or_epi64(), _mm512_or_epi64()

### Community 88 - "Community 88"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpackhi_epi32(), _mm512_maskz_unpackhi_epi32(), _mm512_unpackhi_epi32()

### Community 89 - "Community 89"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpackhi_epi64(), _mm512_maskz_unpackhi_epi64(), _mm512_unpackhi_epi64()

### Community 90 - "Community 90"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpackhi_pd(), _mm512_maskz_unpackhi_pd(), _mm512_unpackhi_pd()

### Community 91 - "Community 91"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpackhi_ps(), _mm512_maskz_unpackhi_ps(), _mm512_unpackhi_ps()

### Community 92 - "Community 92"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpacklo_epi32(), _mm512_maskz_unpacklo_epi32(), _mm512_unpacklo_epi32()

### Community 93 - "Community 93"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpacklo_epi64(), _mm512_maskz_unpacklo_epi64(), _mm512_unpacklo_epi64()

### Community 94 - "Community 94"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpacklo_pd(), _mm512_maskz_unpacklo_pd(), _mm512_unpacklo_pd()

### Community 95 - "Community 95"
Cohesion: 0.67
Nodes (3): _mm512_mask_unpacklo_ps(), _mm512_maskz_unpacklo_ps(), _mm512_unpacklo_ps()

### Community 96 - "Community 96"
Cohesion: 0.67
Nodes (3): _mm512_mask_xor_epi32(), _mm512_maskz_xor_epi32(), _mm512_xor_epi32()

### Community 97 - "Community 97"
Cohesion: 0.67
Nodes (3): _mm512_mask_xor_epi64(), _mm512_maskz_xor_epi64(), _mm512_xor_epi64()

### Community 98 - "Community 98"
Cohesion: 0.67
Nodes (3): _mm_store1_pd(), _mm_store_pd(), _mm_store_pd1()

### Community 100 - "Community 100"
Cohesion: 0.67
Nodes (3): _mm_set1_pi16(), _mm_set_pi16(), _mm_setr_pi16()

### Community 101 - "Community 101"
Cohesion: 0.67
Nodes (3): _mm_set1_pi32(), _mm_set_pi32(), _mm_setr_pi32()

### Community 102 - "Community 102"
Cohesion: 0.67
Nodes (3): _mm_set1_pi8(), _mm_set_pi8(), _mm_setr_pi8()

### Community 108 - "Community 108"
Cohesion: 1.00
Nodes (2): vec_madd(), vec_mladd()

### Community 109 - "Community 109"
Cohesion: 1.00
Nodes (2): _mm_mask_and_epi32(), _mm_maskz_and_epi32()

### Community 110 - "Community 110"
Cohesion: 1.00
Nodes (2): _mm_mask_and_epi64(), _mm_maskz_and_epi64()

### Community 111 - "Community 111"
Cohesion: 1.00
Nodes (2): _mm_mask_andnot_epi32(), _mm_maskz_andnot_epi32()

### Community 112 - "Community 112"
Cohesion: 1.00
Nodes (2): _mm_mask_andnot_epi64(), _mm_maskz_andnot_epi64()

### Community 113 - "Community 113"
Cohesion: 1.00
Nodes (2): _mm_mask_or_epi32(), _mm_maskz_or_epi32()

### Community 114 - "Community 114"
Cohesion: 1.00
Nodes (2): _mm_mask_or_epi64(), _mm_maskz_or_epi64()

### Community 115 - "Community 115"
Cohesion: 1.00
Nodes (2): _mm_mask_xor_epi32(), _mm_maskz_xor_epi32()

### Community 116 - "Community 116"
Cohesion: 1.00
Nodes (2): _mm_mask_xor_epi64(), _mm_maskz_xor_epi64()

### Community 117 - "Community 117"
Cohesion: 1.00
Nodes (2): _mm256_mask_and_epi32(), _mm256_maskz_and_epi32()

### Community 118 - "Community 118"
Cohesion: 1.00
Nodes (2): _mm256_mask_and_epi64(), _mm256_maskz_and_epi64()

### Community 119 - "Community 119"
Cohesion: 1.00
Nodes (2): _mm256_mask_andnot_epi32(), _mm256_maskz_andnot_epi32()

### Community 120 - "Community 120"
Cohesion: 1.00
Nodes (2): _mm256_mask_andnot_epi64(), _mm256_maskz_andnot_epi64()

### Community 121 - "Community 121"
Cohesion: 1.00
Nodes (2): _mm256_mask_or_epi32(), _mm256_maskz_or_epi32()

### Community 122 - "Community 122"
Cohesion: 1.00
Nodes (2): _mm256_mask_or_epi64(), _mm256_maskz_or_epi64()

### Community 123 - "Community 123"
Cohesion: 1.00
Nodes (2): _mm256_mask_xor_epi32(), _mm256_maskz_xor_epi32()

### Community 124 - "Community 124"
Cohesion: 1.00
Nodes (2): _mm256_mask_xor_epi64(), _mm256_maskz_xor_epi64()

### Community 125 - "Community 125"
Cohesion: 1.00
Nodes (2): _mm256_castpd128_pd256(), _mm256_loadu2_m128d()

### Community 126 - "Community 126"
Cohesion: 1.00
Nodes (2): _mm256_castpd256_pd128(), _mm256_storeu2_m128d()

### Community 127 - "Community 127"
Cohesion: 1.00
Nodes (2): _mm256_castps128_ps256(), _mm256_loadu2_m128()

### Community 128 - "Community 128"
Cohesion: 1.00
Nodes (2): _mm256_castps256_ps128(), _mm256_storeu2_m128()

### Community 129 - "Community 129"
Cohesion: 1.00
Nodes (2): _mm256_castsi128_si256(), _mm256_loadu2_m128i()

### Community 130 - "Community 130"
Cohesion: 1.00
Nodes (2): _mm256_castsi256_si128(), _mm256_storeu2_m128i()

### Community 131 - "Community 131"
Cohesion: 1.00
Nodes (2): _mm_cmpgt_epi16(), _mm_cmplt_epi16()

### Community 132 - "Community 132"
Cohesion: 1.00
Nodes (2): _mm_cmpgt_epi32(), _mm_cmplt_epi32()

### Community 133 - "Community 133"
Cohesion: 1.00
Nodes (2): _mm_cmpgt_epi8(), _mm_cmplt_epi8()

### Community 134 - "Community 134"
Cohesion: 1.00
Nodes (2): _mm_cvt_si2ss(), _mm_cvtsi32_ss()

### Community 135 - "Community 135"
Cohesion: 1.00
Nodes (2): _mm_cvt_ss2si(), _mm_cvtss_si32()

### Community 136 - "Community 136"
Cohesion: 1.00
Nodes (2): _mm_cvtt_ps2pi(), _mm_cvttps_pi32()

### Community 137 - "Community 137"
Cohesion: 1.00
Nodes (2): _mm_cvtt_ss2si(), _mm_cvttss_si32()

### Community 138 - "Community 138"
Cohesion: 1.00
Nodes (2): _mm_load_ps(), _mm_loadr_ps()

### Community 139 - "Community 139"
Cohesion: 1.00
Nodes (2): _mm_set1_ps(), _mm_set_ps1()

## Knowledge Gaps
- **40 isolated node(s):** `fs`, `path`, `root`, `file`, `html` (+35 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **Thin community `Community 16`** (1 nodes): `5f770b1 chore: pre-reformat backup 2026-08-03`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 20`** (2 nodes): `abs()`, `fabs()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 108`** (2 nodes): `vec_madd()`, `vec_mladd()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 109`** (2 nodes): `_mm_mask_and_epi32()`, `_mm_maskz_and_epi32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 110`** (2 nodes): `_mm_mask_and_epi64()`, `_mm_maskz_and_epi64()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 111`** (2 nodes): `_mm_mask_andnot_epi32()`, `_mm_maskz_andnot_epi32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 112`** (2 nodes): `_mm_mask_andnot_epi64()`, `_mm_maskz_andnot_epi64()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 113`** (2 nodes): `_mm_mask_or_epi32()`, `_mm_maskz_or_epi32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 114`** (2 nodes): `_mm_mask_or_epi64()`, `_mm_maskz_or_epi64()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 115`** (2 nodes): `_mm_mask_xor_epi32()`, `_mm_maskz_xor_epi32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 116`** (2 nodes): `_mm_mask_xor_epi64()`, `_mm_maskz_xor_epi64()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 117`** (2 nodes): `_mm256_mask_and_epi32()`, `_mm256_maskz_and_epi32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 118`** (2 nodes): `_mm256_mask_and_epi64()`, `_mm256_maskz_and_epi64()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 119`** (2 nodes): `_mm256_mask_andnot_epi32()`, `_mm256_maskz_andnot_epi32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 120`** (2 nodes): `_mm256_mask_andnot_epi64()`, `_mm256_maskz_andnot_epi64()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 121`** (2 nodes): `_mm256_mask_or_epi32()`, `_mm256_maskz_or_epi32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 122`** (2 nodes): `_mm256_mask_or_epi64()`, `_mm256_maskz_or_epi64()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 123`** (2 nodes): `_mm256_mask_xor_epi32()`, `_mm256_maskz_xor_epi32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 124`** (2 nodes): `_mm256_mask_xor_epi64()`, `_mm256_maskz_xor_epi64()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 125`** (2 nodes): `_mm256_castpd128_pd256()`, `_mm256_loadu2_m128d()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 126`** (2 nodes): `_mm256_castpd256_pd128()`, `_mm256_storeu2_m128d()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 127`** (2 nodes): `_mm256_castps128_ps256()`, `_mm256_loadu2_m128()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 128`** (2 nodes): `_mm256_castps256_ps128()`, `_mm256_storeu2_m128()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 129`** (2 nodes): `_mm256_castsi128_si256()`, `_mm256_loadu2_m128i()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 130`** (2 nodes): `_mm256_castsi256_si128()`, `_mm256_storeu2_m128i()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 131`** (2 nodes): `_mm_cmpgt_epi16()`, `_mm_cmplt_epi16()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 132`** (2 nodes): `_mm_cmpgt_epi32()`, `_mm_cmplt_epi32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 133`** (2 nodes): `_mm_cmpgt_epi8()`, `_mm_cmplt_epi8()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 134`** (2 nodes): `_mm_cvt_si2ss()`, `_mm_cvtsi32_ss()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 135`** (2 nodes): `_mm_cvt_ss2si()`, `_mm_cvtss_si32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 136`** (2 nodes): `_mm_cvtt_ps2pi()`, `_mm_cvttps_pi32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 137`** (2 nodes): `_mm_cvtt_ss2si()`, `_mm_cvttss_si32()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 138`** (2 nodes): `_mm_load_ps()`, `_mm_loadr_ps()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 139`** (2 nodes): `_mm_set1_ps()`, `_mm_set_ps1()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `vec_perm()` connect `Community 19` to `Community 3`?**
  _High betweenness centrality (0.000) - this node is a cross-community bridge._
- **Why does `_mm_setzero_di()` connect `Community 36` to `Community 0`?**
  _High betweenness centrality (0.000) - this node is a cross-community bridge._
- **What connects `fs`, `path`, `root` to the rest of the system?**
  _40 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.00243605359317905 - nodes in this community are weakly interconnected._
- **Should `Community 1` be split into smaller, more focused modules?**
  _Cohesion score 0.0032310177705977385 - nodes in this community are weakly interconnected._
- **Should `Community 2` be split into smaller, more focused modules?**
  _Cohesion score 0.005361930294906166 - nodes in this community are weakly interconnected._
- **Should `Community 3` be split into smaller, more focused modules?**
  _Cohesion score 0.009345794392523364 - nodes in this community are weakly interconnected._