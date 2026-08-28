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
// Precedence mirrors the signingConfigs block: local.properties property first,
// then an environment variable of the same name, then the default. The env
// fallback is what lets CI secrets feed BuildConfig without a local.properties.
fun localProp(name: String, default: String = ""): String =
    (localProperties.getProperty(name) ?: System.getenv(name) ?: default)
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")

android {
    namespace = "com.needsvswants.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.needsvswants.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 32
        versionName = "2.0.24"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // --- Google Play Billing Subscriptions -----------------------------
        // Official product IDs registered in Google Play Console.
        buildConfigField(
            "String",
            "PLAY_SUB_PRO",
            "\"${localProp("PLAY_SUB_PRO", "needsvswants_pro")}\""
        )
        buildConfigField(
            "String",
            "PLAY_SUB_MAX",
            "\"${localProp("PLAY_SUB_MAX", "needsvswants_max")}\""
        )

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
        // --- Rewarded ads (ads/ package) ------------------------------------
        // Google TEST ids by default; override ADMOB_APP_ID / ADMOB_REWARDED_UNIT_ID
        // in local.properties with production values before a real release.
        buildConfigField(
            "String",
            "ADMOB_APP_ID",
            "\"${localProp("ADMOB_APP_ID", "ca-app-pub-3940256099942544~3347511713")}\""
        )
        buildConfigField(
            "String",
            "ADMOB_REWARDED_UNIT_ID",
            "\"${localProp("ADMOB_REWARDED_UNIT_ID", "ca-app-pub-3940256099942544/5224354917")}\""
        )
        manifestPlaceholders["admobAppId"] = localProp(
            "ADMOB_APP_ID",
            "ca-app-pub-3940256099942544~3347511713"
        )
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

    // Dual distribution: `direct` (sideload APK + PayPal/PayMongo) vs
    // `play` (Google Play Store AAB + Google Play Billing).
    // `experience`: `full` (production app) vs `plain` (free-only side-by-side test build).
    flavorDimensions += listOf("distribution", "experience")
    productFlavors {
        create("direct") {
            dimension = "distribution"
            buildConfigField("boolean", "PLAY_STORE_BUILD", "false")
        }
        create("play") {
            dimension = "distribution"
            buildConfigField("boolean", "PLAY_STORE_BUILD", "true")
        }
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

    variantFilter {
        // Play Store distribution only needs the full production app, not the plain-free test flavor.
        if (flavors.any { it.name == "play" } && flavors.any { it.name == "plain" }) {
            ignore = true
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
        freeCompilerArgs += listOf("-Xskip-metadata-version-check")
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
                // Gradle-managed emulator for instrumented tests (CI nightly).
                // With flavors the task is <device><Flavors><BuildType>AndroidTest:
                //   ./gradlew pixel2api33DirectFullDebugAndroidTest
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

    // PBL 9 / Kotlin 2.x metadata compatibility for Hilt annotation processing.
    // The Play Billing 9 KTX jars carry newer Kotlin metadata than the
    // kotlinx-metadata-jvm version bundled with Hilt 2.51.x. Force a newer
    // metadata library so hiltJavaCompile can read those class files.
    constraints {
        implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.6.3")
    }

    // Google Sign-In via Credential Manager (native ID token → Supabase)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.googleid)

    // Crash reporting — manual init only (diagnostics/CrashReporting.kt);
    // disabled in debug builds and whenever SENTRY_DSN is blank.
    implementation(libs.sentry.android)
    // SAF tree access for local auto-backup (data/backup/)
    implementation(libs.androidx.documentfile)
    implementation(libs.androidx.exifinterface)

    // Rewarded ads for the Free tier (ads/ package). Kill switch is
    // AdsConfig.ENABLED — a disabled config compiles the SDK in but never
    // inits it and never requests ads.
    implementation(libs.play.services.ads)
    implementation(libs.user.messaging.platform)

    // ML Kit on-device Text Recognition (Pro/Max Receipt Sorter)
    implementation(libs.play.services.mlkit.text.recognition)

    // Google Play In-App Billing (Play Store subscriptions)
    implementation(libs.play.billing)

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

// PBL 9 / Kotlin 2.x metadata compatibility for Hilt annotation processing.
// The Play Billing 9 KTX jars carry newer Kotlin metadata than the
// kotlinx-metadata-jvm version bundled with Hilt 2.51.x. Force a newer
// metadata library so hiltJavaCompile can read those class files.
configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.6.3")
    }
}

// ---------------------------------------------------------------------------
// Release runtime-config gate.
// A signed release with blank SUPABASE_URL / SUPABASE_ANON_KEY /
// GOOGLE_WEB_CLIENT_ID cannot sign in or sync — fail that build here instead of
// shipping a dead artifact. Google TEST AdMob ids and a blank SENTRY_DSN are
// the deliberate soft-launch posture (D159-D161) until the owner creates real
// accounts: warned loudly, never failed. Each check task is wired only as a
// dependency of its release variant's assemble/bundle tasks, so debug builds,
// unit tests and PR jobs never trigger it (CI PR runs have no secrets).
// ---------------------------------------------------------------------------
val releaseGateRequiredKeys = listOf("SUPABASE_URL", "SUPABASE_ANON_KEY", "GOOGLE_WEB_CLIENT_ID")
val releaseGateTestAdIds = mapOf(
    "ADMOB_APP_ID" to "ca-app-pub-3940256099942544~3347511713",
    "ADMOB_REWARDED_UNIT_ID" to "ca-app-pub-3940256099942544/5224354917",
)
val releaseVariantNames = mutableListOf<String>()
androidComponents {
    onVariants(selector().withBuildType("release")) { variant ->
        releaseVariantNames += variant.name
    }
}
afterEvaluate {
    // Runs after AGP has created the variant tasks, so the assemble/bundle
    // tasks can be wired below without ordering races.
    for (variantName in releaseVariantNames) {
        val capitalized = variantName.replaceFirstChar { it.uppercaseChar() }
        // Snapshot exactly what BuildConfig will embed (same localProp precedence).
        val requiredValues = releaseGateRequiredKeys.associateWith { key -> localProp(key) }
        val admobAppId = localProp("ADMOB_APP_ID")
        val admobRewardedId = localProp("ADMOB_REWARDED_UNIT_ID")
        val sentryDsn = localProp("SENTRY_DSN")
        val checkTask = tasks.register("check${capitalized}Config") {
            group = "verification"
            description =
                "Fails $variantName if sign-in/sync runtime config would be blank in the artifact."
            doLast {
                val blankKeys = requiredValues.filterValues { it.isBlank() }.keys
                if (blankKeys.isNotEmpty()) {
                    throw GradleException(
                        "$variantName would ship with blank runtime config: " +
                            "${blankKeys.joinToString(", ")}. Sign-in/sync would be broken. " +
                            "Set them in local.properties, or as environment variables " +
                            "(GitHub repo secrets for CI)."
                    )
                }
                if (admobAppId.isBlank() || admobAppId == releaseGateTestAdIds["ADMOB_APP_ID"]) {
                    println(
                        "WARNING [$variantName]: ADMOB_APP_ID is blank or a Google TEST id — " +
                            "real ads will not serve (deliberate soft-launch posture, D159)."
                    )
                }
                if (admobRewardedId.isBlank() || admobRewardedId == releaseGateTestAdIds["ADMOB_REWARDED_UNIT_ID"]) {
                    println(
                        "WARNING [$variantName]: ADMOB_REWARDED_UNIT_ID is blank or a Google TEST id — " +
                            "rewarded ads grant nothing on live accounts (deliberate soft-launch posture, D159)."
                    )
                }
                if (sentryDsn.isBlank()) {
                    println(
                        "WARNING [$variantName]: SENTRY_DSN is blank — crash reporting stays disabled " +
                            "(deliberate soft-launch posture, D160)."
                    )
                }
                println("$variantName runtime config OK: sign-in/sync keys present.")
            }
        }
        listOf("assemble", "bundle").forEach { verb ->
            tasks.matching { it.name == "$verb$capitalized" }.configureEach {
                dependsOn(checkTask)
            }
        }
    }
}
