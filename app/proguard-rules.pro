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

# Sentry 8.x references API 35 classes (app-start / profiling) that don't
# exist on compileSdk 34; usage is API-level guarded at runtime, so R8 only
# needs the warnings suppressed. Sentry ships its own consumer keep rules.
-dontwarn android.app.ApplicationStartInfo
-dontwarn android.os.ProfilingManager
-dontwarn android.os.ProfilingResult

