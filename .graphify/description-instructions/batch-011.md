# Node Description Batch 12 of 111

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

- "android_binder_parcel_utils_aparcel_nullablestdvectorallocator": "AParcel_nullableStdVectorAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L189 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_nullablestdvectorexternalallocator": "AParcel_nullableStdVectorExternalAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L241 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_nullablestdvectorsetter": "AParcel_nullableStdVectorSetter()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L282 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_nullablestdvectorstringelementgetter": "AParcel_nullableStdVectorStringElementGetter()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L462 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_readnullableparcelfiledescriptor": "AParcel_readNullableParcelFileDescriptor()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L348 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_readnullablestrongbinder": "AParcel_readNullableStrongBinder()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L298 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_readrequiredparcelfiledescriptor": "AParcel_readRequiredParcelFileDescriptor()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L373 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_readrequiredstrongbinder": "AParcel_readRequiredStrongBinder()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L323 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_readstring": "AParcel_readString()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L600 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_readvector": "AParcel_readVector()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L639 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_resizevector": "AParcel_resizeVector()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L1432 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_stdarrayallocator": "AParcel_stdArrayAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L108 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_stdarrayexternalallocator": "AParcel_stdArrayExternalAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L145 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_stdarraynullablestringelementgetter": "AParcel_stdArrayNullableStringElementGetter()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L562 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_stdarraysetter": "AParcel_stdArraySetter()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L493 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_stdarraystringelementgetter": "AParcel_stdArrayStringElementGetter()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L535 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_stdvectorallocator": "AParcel_stdVectorAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L174 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_stdvectorexternalallocator": "AParcel_stdVectorExternalAllocator()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L219 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_stdvectorsetter": "AParcel_stdVectorSetter()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L272 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_stdvectorstringelementgetter": "AParcel_stdVectorStringElementGetter()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L435 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_writenullableparcelfiledescriptor": "AParcel_writeNullableParcelFileDescriptor()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L340 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_writenullablestrongbinder": "AParcel_writeNullableStrongBinder()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L290 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_writerequiredparcelfiledescriptor": "AParcel_writeRequiredParcelFileDescriptor()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L361 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_writerequiredstrongbinder": "AParcel_writeRequiredStrongBinder()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L311 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_writestring": "AParcel_writeString()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L593 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_aparcel_writevector": "AParcel_writeVector()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L629 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcel_utils_ndk": "ndk()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcel_utils.h:L40 | neighbors=[binder_parcel_utils.h]
- "android_binder_parcelable_utils_ndk": "ndk()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_parcelable_utils.h:L31 | neighbors=[binder_parcelable_utils.h]
- "android_binder_to_string_constexpr": "constexpr()" | kind=code-symbol | source=.android-sdk/platforms/android-34/optional/libbinder_ndk_cpp/android/binder_to_string.h:L161 | neighbors=[binder_to_string.h]
- "clang_include_adxintrin_addcarry_u32": "_addcarry_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/adxintrin.h:L52 | neighbors=[adxintrin.h]
- "clang_include_adxintrin_addcarry_u64": "_addcarry_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/adxintrin.h:L60 | neighbors=[adxintrin.h]
- "clang_include_adxintrin_addcarryx_u32": "_addcarryx_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/adxintrin.h:L35 | neighbors=[adxintrin.h]
- "clang_include_adxintrin_addcarryx_u64": "_addcarryx_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/adxintrin.h:L43 | neighbors=[adxintrin.h]
- "clang_include_adxintrin_subborrow_u32": "_subborrow_u32()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/adxintrin.h:L68 | neighbors=[adxintrin.h]
- "clang_include_adxintrin_subborrow_u64": "_subborrow_u64()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/adxintrin.h:L76 | neighbors=[adxintrin.h]
- "clang_include_altivec_builtin_crypto_vcipher": "__builtin_crypto_vcipher()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14338 | neighbors=[altivec.h]
- "clang_include_altivec_builtin_crypto_vcipherlast": "__builtin_crypto_vcipherlast()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14344 | neighbors=[altivec.h]
- "clang_include_altivec_builtin_crypto_vncipher": "__builtin_crypto_vncipher()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14350 | neighbors=[altivec.h]
- "clang_include_altivec_builtin_crypto_vncipherlast": "__builtin_crypto_vncipherlast()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14356 | neighbors=[altivec.h]
- "clang_include_altivec_builtin_crypto_vpermxor": "__builtin_crypto_vpermxor()" | kind=code-symbol | source=.android-sdk/build-tools/34.0.0/renderscript/clang-include/altivec.h:L14372 | neighbors=[altivec.h]

## Instructions

Write a single JSON object mapping each node id to a one-sentence description
to: C:\Needs vs Wants\.graphify\description-instructions\batch-011.json

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
