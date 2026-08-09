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

