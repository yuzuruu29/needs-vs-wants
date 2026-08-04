# Node Description Batch 6 of 111

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

- "commit:repo:github.com/yuzuruu29/needs-vs-wants@f17aa7140b54a5d231be281b174b4c9321aa0b9d": "f17aa71 fix(gitignore): ignore all APK artifacts with *.apk" | kind=Commit | source=git | neighbors=[b8cb239 checkpoint before checking out …, feat/ios-native-rewrite, d889e73 checkpoint before checking out …]
- "pad_parts_pad_capturedraft": "captureDraft()" | kind=code-symbol | source=website/_pad-parts/pad.js:L135 | neighbors=[pad.js, freezeLiveSheet(), go()]
- "pad_parts_pad_poseallleaves": "poseAllLeaves()" | kind=code-symbol | source=website/_pad-parts/pad.js:L444 | neighbors=[pad.js, buildFlipLeaves(), settleFlip()]
- "pad_parts_pad_rows": "rows()" | kind=code-symbol | source=website/_pad-parts/pad.js:L46 | neighbors=[pad.js, trySeal(), updateChrome()]
- "pad_parts_pad_wireliveform": "wireLiveForm()" | kind=code-symbol | source=website/_pad-parts/pad.js:L145 | neighbors=[pad.js, buildFlat(), hydrateLiveSheet()]
- "android_binder_enums": "binder_enums.h" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_enums.h:L1 | neighbors=[ndk(), 5f770b1 chore: pre-reformat backup 2026…]
- "android_binder_interface_utils_ondump": "onDump()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L295 | neighbors=[binder_interface_utils.h, dump()]
- "android_binder_interface_utils_sharedrefbase": "SharedRefBase()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L123 | neighbors=[binder_interface_utils.h, asBinder()]
- "android_binder_parcel_utils_aparcel_nullablestdarraystringelementallocator": "AParcel_nullableStdArrayStringElementAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L582 | neighbors=[binder_parcel_utils.h, AParcel_nullableStdStringAllocator()]
- "android_binder_parcel_utils_aparcel_nullablestdvectorstringelementallocator": "AParcel_nullableStdVectorStringElementAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L449 | neighbors=[binder_parcel_utils.h, AParcel_nullableStdStringAllocator()]
- "android_binder_parcel_utils_aparcel_stdarraynullablestringelementallocator": "AParcel_stdArrayNullableStringElementAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L549 | neighbors=[binder_parcel_utils.h, AParcel_nullableStdStringAllocator()]
- "android_binder_parcel_utils_aparcel_stdarraystringelementallocator": "AParcel_stdArrayStringElementAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L523 | neighbors=[binder_parcel_utils.h, AParcel_stdStringAllocator()]
- "android_binder_parcel_utils_aparcel_stdvectorstringelementallocator": "AParcel_stdVectorStringElementAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L424 | neighbors=[binder_parcel_utils.h, AParcel_stdStringAllocator()]
- "android_binder_parcelable_utils": "binder_parcelable_utils.h" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcelable_utils.h:L1 | neighbors=[ndk(), 5f770b1 chore: pre-reformat backup 2026…]
- "android_binder_to_string": "binder_to_string.h" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_to_string.h:L1 | neighbors=[constexpr(), 5f770b1 chore: pre-reformat backup 2026…]
- "clang_include_altivec_vec_cmple": "vec_cmple()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1776 | neighbors=[altivec.h, vec_cmpge()]
- "clang_include_altivec_vec_cmplt": "vec_cmplt()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L1832 | neighbors=[altivec.h, vec_cmpgt()]
- "clang_include_altivec_vec_madd": "vec_madd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L2946 | neighbors=[altivec.h, vec_mladd()]
- "clang_include_altivec_vec_mergee": "vec_mergee()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3814 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_mergeh": "vec_mergeh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3280 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_mergel": "vec_mergel()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3550 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_mergeo": "vec_mergeo()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3840 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_mladd": "vec_mladd()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L4130 | neighbors=[altivec.h, vec_madd()]
- "clang_include_altivec_vec_pack": "vec_pack()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L5404 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_sld": "vec_sld()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6784 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_splat": "vec_splat()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L7665 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vmrghb": "vec_vmrghb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3452 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vmrghh": "vec_vmrghh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3480 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vmrghw": "vec_vmrghw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3516 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vmrglb": "vec_vmrglb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3715 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vmrglh": "vec_vmrglh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3743 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vmrglw": "vec_vmrglw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L3779 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vperm": "vec_vperm()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6183 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vpkudum": "vec_vpkudum()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L5644 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vpkuhum": "vec_vpkuhum()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L5545 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vpkuwum": "vec_vpkuwum()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L5594 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vslb": "vec_vslb()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6742 | neighbors=[altivec.h, vec_sl()]
- "clang_include_altivec_vec_vsldoi": "vec_vsldoi()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6989 | neighbors=[altivec.h, vec_perm()]
- "clang_include_altivec_vec_vslh": "vec_vslh()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6756 | neighbors=[altivec.h, vec_sl()]
- "clang_include_altivec_vec_vslw": "vec_vslw()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L6770 | neighbors=[altivec.h, vec_sl()]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-005.json

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
