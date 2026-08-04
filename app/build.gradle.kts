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
        versionCode = 5
        versionName = "1.4.0"
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
            storeFile = file(project.properties["RELEASE_STORE_FILE"] as String? ?: "release.keystore")
            storePassword = project.properties["RELEASE_STORE_PASSWORD"] as String? ?: ""
            keyAlias = project.properties["RELEASE_KEY_ALIAS"] as String? ?: ""
            keyPassword = project.properties["RELEASE_KEY_PASSWORD"] as String? ?: ""
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

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.ui.test.junit4)
}
