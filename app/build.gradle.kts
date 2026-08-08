import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

// Load gitignored local.properties (sdk.dir + optional Supabase / billing keys).
// Never put real keys in this file or any tracked source — only local.properties.
val localProperties = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
fun localProp(name: String, default: String = ""): String =
    (localProperties.getProperty(name) ?: default)
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")

android {
    namespace = "com.needsvswants.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.needsvswants.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 11
        versionName = "2.0.3"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // --- Task 3: Pro / Supabase seams -----------------------------------
        // Empty by default (offline). Set SUPABASE_URL + SUPABASE_ANON_KEY in
        // root local.properties (gitignored). Anon key is a public client key;
        // never put the service_role key in the app.
        buildConfigField("String", "SUPABASE_URL", "\"${localProp("SUPABASE_URL")}\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${localProp("SUPABASE_ANON_KEY")}\"")
        buildConfigField(
            "String",
            "PRO_TRIAL_PRODUCT_ID",
            "\"${localProp("PRO_TRIAL_PRODUCT_ID", "pro_trial_3day")}\""
        )
        buildConfigField(
            "String",
            "PRO_MONTHLY_PRODUCT_ID",
            "\"${localProp("PRO_MONTHLY_PRODUCT_ID", "pro_monthly")}\""
        )
        buildConfigField(
            "String",
            "PRO_MAX_MONTHLY_PRODUCT_ID",
            "\"${localProp("PRO_MAX_MONTHLY_PRODUCT_ID", "max_monthly")}\""
        )
        // Web OAuth client ID — serverClientId for Credential Manager Google Sign-In.
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"${localProp("GOOGLE_WEB_CLIENT_ID")}\"")
        // ---------------------------------------------------------------------
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a")
        }
    }

    signingConfigs {
        create("release") {
            // Signing credentials live OUTSIDE the repo: ~/.gradle/gradle.properties
            // (user home) or environment variables (CI). Never commit the keystore
            // or passwords — the original release.keystore was public on GitHub
            // since 2026-07-29 and was rotated 2026-08-07 (D86); it is BURNED.
            val storeFileProp = (project.properties["RELEASE_STORE_FILE"] as String?)
                ?: System.getenv("RELEASE_STORE_FILE")
            val storePass = (project.properties["RELEASE_STORE_PASSWORD"] as String?)
                ?: System.getenv("RELEASE_STORE_PASSWORD")
            val aliasProp = (project.properties["RELEASE_KEY_ALIAS"] as String?)
                ?: System.getenv("RELEASE_KEY_ALIAS")
            val keyPass = (project.properties["RELEASE_KEY_PASSWORD"] as String?)
                ?: System.getenv("RELEASE_KEY_PASSWORD")
            storeFile = file(storeFileProp ?: "release.keystore")
            storePassword = storePass ?: ""
            keyAlias = aliasProp ?: ""
            keyPassword = keyPass ?: ""
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs["release"]
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/DebugProbesKt.bin"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)

    // --- Task 3: Google Play Billing (stub implementation; offline build) --
    // Uncomment `androidx-billing` in gradle/libs.versions.toml first, then enable:
    // implementation(libs.androidx.billing)
    // ------------------------------------------------------------------------

    // Google Sign-In via Credential Manager (native ID token → Supabase)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.googleid)

    // --- Phase 3: Rewarded ads (AdMob + UMP) — SDK STRIPPED (D87) ---
    // No AdMob account yet; the deps added ~1.5 MB to the release APK.
    // To restore: uncomment these two lines + the version/library entries in
    // gradle/libs.versions.toml, restore ads/AdMobRewardedAdGateway.kt +
    // ads/ConsentHelper.kt from git commit 5622b7e, set AdsConfig.ENABLED =
    // true and replace the test IDs with production values.
    // implementation(libs.google.play.services.ads)
    // implementation(libs.google.ump)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.ui.test.junit4)

    // Phase 2 WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.1")
    implementation("androidx.hilt:hilt-work:1.2.0")
    ksp("androidx.hilt:hilt-compiler:1.2.0")

    // Phase 6 Glance Widget
    implementation("androidx.glance:glance-appwidget:1.1.0")
}
