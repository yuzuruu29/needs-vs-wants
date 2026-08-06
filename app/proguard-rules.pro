-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.**
-keepclassmembers class * {
    @androidx.room.* <methods>;
    @androidx.room.* <fields>;
}
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keepclassmembers enum com.needsvswants.app.data.model.EntryType { *; }
-keep class com.needsvswants.app.NeedsVsWantsApp { *; }

# Google Credential Manager / Identity
-keep class androidx.credentials.** { *; }
-keep class com.google.android.libraries.identity.googleid.** { *; }
-dontwarn com.google.android.gms.**

# Google Mobile Ads SDK (Phase 3) — play-services-ads + UMP ship their own
# consumer ProGuard rules inside the AARs; nothing extra is required for
# 23.x / UMP 2.x. The app-side gateway (com.needsvswants.app.ads.*) is
# referenced from code and kept by R8 automatically.
# Before release: replace the test AdMob App ID in AndroidManifest.xml and
# AdMobRewardedAdGateway.REWARDED_AD_UNIT_ID with production values.

