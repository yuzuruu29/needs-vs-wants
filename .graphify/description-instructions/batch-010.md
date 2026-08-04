# Node Description Batch 11 of 111

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

- "clang_include_xmmintrin_mm_set1_ps": "_mm_set1_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1767 | neighbors=[xmmintrin.h, _mm_set_ps1()]
- "clang_include_xmmintrin_mm_store_ps1": "_mm_store_ps1()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L1988 | neighbors=[xmmintrin.h, _mm_store1_ps()]
- "clang_include_xmmintrin_mm_storer_ps": "_mm_storer_ps()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xmmintrin.h:L2007 | neighbors=[xmmintrin.h, _mm_store_ps()]
- "clang_include_xtestintrin": "xtestintrin.h" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/xtestintrin.h:L1 | neighbors=[_xtest(), 5f770b1 chore: pre-reformat backup 2026…]
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@5915fe5cb3fb89309bccb01ec54e23561830d0e6": "5915fe5 checkpoint before checking out feat/ios-native-rewrite" | kind=Commit | source=git | neighbors=[54901a6 Restore supermarket premium sur…, cursor/premium-surface-parity-45af]
- "commit:repo:github.com/yuzuruu29/needs-vs-wants@d889e73375374e078035823a82bd99fb0c9fdc52": "d889e73 checkpoint before checking out main" | kind=Commit | source=git | neighbors=[feat/ios-native-rewrite, f17aa71 fix(gitignore): ignore all APK …]
- "pad_parts_pad_bindnav": "bindNav()" | kind=code-symbol | source=website/_pad-parts/pad.js:L615 | neighbors=[pad.js, initPad()]
- "pad_parts_pad_datelabel": "dateLabel()" | kind=code-symbol | source=website/_pad-parts/pad.js:L49 | neighbors=[pad.js, renderSheetMarkup()]
- "pad_parts_pad_ensurestage": "ensureStage()" | kind=code-symbol | source=website/_pad-parts/pad.js:L367 | neighbors=[pad.js, buildFlipLeaves()]
- "pad_parts_pad_esc": "esc()" | kind=code-symbol | source=website/_pad-parts/pad.js:L17 | neighbors=[pad.js, renderSheetMarkup()]
- "pad_parts_pad_liveoverlay": "liveOverlay()" | kind=code-symbol | source=website/_pad-parts/pad.js:L239 | neighbors=[pad.js, hydrateLiveSheet()]
- "pad_parts_pad_renderrows": "renderRows()" | kind=code-symbol | source=website/_pad-parts/pad.js:L657 | neighbors=[pad.js, hydrateLiveSheet()]
- "pad_parts_pad_setrows": "setRows()" | kind=code-symbol | source=website/_pad-parts/pad.js:L47 | neighbors=[pad.js, trySeal()]
- "pad_parts_pad_syncstaticflippagesquiet": "syncStaticFlipPagesQuiet()" | kind=code-symbol | source=website/_pad-parts/pad.js:L454 | neighbors=[pad.js, settleFlip()]
- "website_probe2_current": "probe2_current.py" | kind=code-symbol | source=website/probe2_current.py:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…, main()]
- "android_binder_auto_utils_fromexceptioncode": "fromExceptionCode()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L304 | neighbors=[binder_auto_utils.h]
- "android_binder_auto_utils_fromexceptioncodewithmessage": "fromExceptionCodeWithMessage()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L307 | neighbors=[binder_auto_utils.h]
- "android_binder_auto_utils_fromservicespecificerror": "fromServiceSpecificError()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L311 | neighbors=[binder_auto_utils.h]
- "android_binder_auto_utils_fromservicespecificerrorwithmessage": "fromServiceSpecificErrorWithMessage()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L314 | neighbors=[binder_auto_utils.h]
- "android_binder_auto_utils_fromstatus": "fromStatus()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L318 | neighbors=[binder_auto_utils.h]
- "android_binder_auto_utils_getexceptioncode": "getExceptionCode()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L253 | neighbors=[binder_auto_utils.h]
- "android_binder_auto_utils_getstatus": "getStatus()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L263 | neighbors=[binder_auto_utils.h]
- "android_binder_auto_utils_impl": "impl()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L129 | neighbors=[binder_auto_utils.h]
- "android_binder_auto_utils_ndk": "ndk()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L40 | neighbors=[binder_auto_utils.h]
- "android_binder_auto_utils_ok": "ok()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L303 | neighbors=[binder_auto_utils.h]
- "android_binder_auto_utils_promote": "promote()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_auto_utils.h:L362 | neighbors=[binder_auto_utils.h]
- "android_binder_enums_ndk": "ndk()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_enums.h:L32 | neighbors=[binder_enums.h]
- "android_binder_interface_utils_bncinterface": "BnCInterface()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L193 | neighbors=[binder_interface_utils.h]
- "android_binder_interface_utils_bpcinterface": "BpCInterface()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L222 | neighbors=[binder_interface_utils.h]
- "android_binder_interface_utils_handleshellcommand": "handleShellCommand()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L243 | neighbors=[binder_interface_utils.h]
- "android_binder_interface_utils_internal": "internal()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L342 | neighbors=[binder_interface_utils.h]
- "android_binder_interface_utils_makeservicename": "makeServiceName()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L199 | neighbors=[binder_interface_utils.h]
- "android_binder_interface_utils_ndk": "ndk()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L43 | neighbors=[binder_interface_utils.h]
- "android_binder_interface_utils_oncreate": "onCreate()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L285 | neighbors=[binder_interface_utils.h]
- "android_binder_interface_utils_ondestroy": "onDestroy()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L291 | neighbors=[binder_interface_utils.h]
- "android_binder_interface_utils_std": "std()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_interface_utils.h:L350 | neighbors=[binder_interface_utils.h]
- "android_binder_internal_logging": "binder_internal_logging.h" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_internal_logging.h:L1 | neighbors=[5f770b1 chore: pre-reformat backup 2026…]
- "android_binder_parcel_utils_aparcel_nullablestdarrayallocator": "AParcel_nullableStdArrayAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L124 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_nullablestdarrayexternalallocator": "AParcel_nullableStdArrayExternalAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L154 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_nullablestdarraysetter": "AParcel_nullableStdArraySetter()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L514 | neighbors=[binder_parcel_utils.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-010.json

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
