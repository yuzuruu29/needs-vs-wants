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
        versionCode = 23
        versionName = "2.0.15"
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
        buildConfigField(
            "String",
            "PRO_ANNUAL_PRODUCT_ID",
            "\"${localProp("PRO_ANNUAL_PRODUCT_ID")}\""
        )
        buildConfigField(
            "String",
            "PRO_MAX_ANNUAL_PRODUCT_ID",
            "\"${localProp("PRO_MAX_ANNUAL_PRODUCT_ID")}\""
        )
        // Web OAuth client ID — serverClientId for Credential Manager Google Sign-In.
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"${localProp("GOOGLE_WEB_CLIENT_ID")}\"")
        // Sentry crash reporting (privacy-lean; release builds only). Empty DSN
        // keeps the SDK fully disabled — set SENTRY_DSN in local.properties.
        buildConfigField("String", "SENTRY_DSN", "\"${localProp("SENTRY_DSN")}\"")
        // ---------------------------------------------------------------------
        // Plain-free test flavor: the deep-link scheme is overridden per flavor so
        // a plain APK installed next to production never steals its checkout return URLs.
        manifestPlaceholders["deepLinkScheme"] = "needsvswants"
        // Mirrored into BuildConfig so checkout requests can tell the server
        // which scheme the redirect pages should bounce back to (plain-flavor
        // deep-link fix — the pages hardcoded needsvswants:// before).
        buildConfigField("String", "DEEP_LINK_SCHEME", "\"needsvswants\"")
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a")
        }
    }

    // `experience` flavor: `full` is the current production app (unchanged);
    // `plain` is a Free-only side-by-side test APK (FORCED Free, no Advisor /
    // paywall / membership UI). Gate flag is BuildConfig.PLAIN_FREE.
    flavorDimensions += "experience"
    productFlavors {
        create("full") {
            dimension = "experience"
            buildConfigField("boolean", "PLAIN_FREE", "false")
        }
        create("plain") {
            dimension = "experience"
            applicationIdSuffix = ".plain"
            versionNameSuffix = "-plain"
            buildConfigField("boolean", "PLAIN_FREE", "true")
            // Distinct launcher label + deep-link scheme so a plain install sits
            // clearly beside production and never collides with its checkout URLs.
            resValue("string", "app_name", "Needs vs Wants (Free Test)")
            manifestPlaceholders["deepLinkScheme"] = "needsvswantsplain"
            buildConfigField("String", "DEEP_LINK_SCHEME", "\"needsvswantsplain\"")
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

    testOptions {
        managedDevices {
            devices {
                // Gradle-managed emulator for instrumented tests (CI nightly):
                //   ./gradlew pixel2api33FullDebugAndroidTest
                maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel2api33").apply {
                    device = "Pixel 2"
                    apiLevel = 33
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }

    }

// Room schema snapshots (migration policy — see data/db/Migrations.kt).
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
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

    // Google Sign-In via Credential Manager (native ID token → Supabase)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.googleid)

    // Crash reporting — manual init only (diagnostics/CrashReporting.kt);
    // disabled in debug builds and whenever SENTRY_DSN is blank.
    implementation(libs.sentry.android)
    // SAF tree access for local auto-backup (data/backup/)
    implementation(libs.androidx.documentfile)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.ui.test.junit4)

    // Instrumented tests (app/src/androidTest — paywall, deep links, backup round-trip)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // Phase 2 WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.1")
    implementation("androidx.hilt:hilt-work:1.2.0")
    ksp("androidx.hilt:hilt-compiler:1.2.0")

    // Phase 6 Glance Widget
    implementation("androidx.glance:glance-appwidget:1.1.0")
}
