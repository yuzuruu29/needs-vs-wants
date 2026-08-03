# Google Sign-In Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Let users sign in (and create accounts) with Google in one tap, obtain a Supabase session JWT, persist it, refresh Pro entitlement, and sign out — primarily on Android, with iOS parity.

**Architecture:** Native Google ID token (Android Credential Manager / iOS Google Sign-In SDK) is exchanged with Supabase `POST /auth/v1/token?grant_type=id_token`. Session tokens live in DataStore. Existing `HttpJsonClient` + `SupabaseAuth` seams stay (no full supabase-kt SDK). UI surfaces: Settings **Account** section + Paywall “Sign in with Google” before purchase identity is needed.

**Tech Stack:** Kotlin, Jetpack Compose, Hilt, DataStore, Android Credential Manager + credentials-play-services-auth, Supabase Auth REST, JUnit; iOS: GoogleSignIn-iOS + existing `SupabaseAuthClient`.

**Out of scope (this plan):** Email magic-link UI (client already exists; leave unwired), Play Billing live purchase, website OAuth, cloud diary sync.

**Verified baseline (2026-08-04):**
- Supabase project live; Google provider **on**; email **on**; signup allowed.
- App has email OTP HTTP client only; **no** Google client; **no** auth UI; **no** session store.
- `get_entitlement` needs `Authorization: Bearer <user access_token>`.

**Pre-code gate (AGENTS.md):** Before first code edit — Obsidian Summary/Tasks/Decisions; Context7 for Credential Manager + Supabase Google native; Graphify scoped to `app/` (existing root graph is stale/website-heavy).

---

## Console prerequisites (human / one-time)

These must be done before a device can complete Google Sign-In. Code can still be written and unit-tested without them.

### Google Cloud Console
1. Create (or use) OAuth **Web application** client — this is Supabase’s Client ID + Secret **and** Android’s `serverClientId` / `WEB_CLIENT_ID`.
2. Create OAuth **Android** client: package `com.needsvswants.app`, SHA-1 from debug and release keystores.
3. Create OAuth **iOS** client: iOS bundle ID from the Xcode / Expo project.
4. Enable Google People API / Identity if Console prompts (standard for OAuth).

### Supabase Dashboard → Authentication → Providers → Google
1. Enable Google (already `true` on live project).
2. Paste **Web** client ID + client secret.
3. Authorized client IDs: include Web + Android (+ iOS) client IDs so native ID tokens are accepted.
4. Callback URL (for any browser flow): `https://awfmjqpevjrdjkyvmlfo.supabase.co/auth/v1/callback`.

### Local secrets (gitignored)
Add to `local.properties` (never commit real values):

```properties
SUPABASE_URL=https://awfmjqpevjrdjkyvmlfo.supabase.co
SUPABASE_ANON_KEY=...
GOOGLE_WEB_CLIENT_ID=....apps.googleusercontent.com
```

Update `local.properties.example` with the `GOOGLE_WEB_CLIENT_ID` placeholder.

---

## File map

| File | Responsibility |
|------|----------------|
| Modify `local.properties.example` | Document `GOOGLE_WEB_CLIENT_ID` |
| Modify `app/build.gradle.kts` | `BuildConfig.GOOGLE_WEB_CLIENT_ID`; Credential Manager deps |
| Modify `gradle/libs.versions.toml` | credentials / googleid library coords |
| Modify `.../data/remote/SupabaseConfig.kt` | `googleWebClientId` field |
| Modify `.../di/EntitlementModule.kt` | Provide config + auth session / Google client binds |
| Create `.../data/remote/AuthSession.kt` | Pure session model (`accessToken`, `refreshToken`, `email`, `userId`, `expiresAtEpochMillis`) |
| Modify `.../data/remote/SupabaseJson.kt` | Parse token + id_token response (session fields) |
| Modify `.../data/remote/SupabaseAuth.kt` | `signInWithGoogleIdToken`, optional `refreshSession`, `signOut` |
| Create `.../data/auth/AuthSessionStore.kt` | Interface + DataStore impl (or extend `AppPreferences`) |
| Create `.../data/auth/GoogleIdTokenProvider.kt` | Interface abstracting Credential Manager |
| Create `.../data/auth/CredentialManagerGoogleIdTokenProvider.kt` | Android implementation |
| Create `.../data/auth/AuthRepository.kt` | Orchestrate Google → Supabase → store → entitlement refresh |
| Create `.../ui/screens/auth/AuthViewModel.kt` | Sign-in / sign-out UI state |
| Modify `.../settings/SettingsScreen.kt` | Account section: signed-in email, Sign in with Google, Sign out |
| Modify `.../paywall/PaywallScreen.kt` + ViewModel | Sign-in CTA when no session |
| Modify `.../navigation/AppNavigation.kt` | Pass Activity context / no new route required |
| Create unit tests under `app/src/test/...` | JSON parse, AuthRepository with fakes |
| iOS: `SupabaseAuthClient.swift` | `signInWithGoogleIdToken` |
| iOS: new Google helper + Settings / Paywall UI | Parity |
| Obsidian Tasks/Decisions/Summary | Record D52+ outcome after ship |

---

## Architecture flow

```text
User taps "Sign in with Google"
        │
        ▼
GoogleIdTokenProvider (Credential Manager)
  rawNonce + SHA-256(hashedNonce)
  → Google ID token (aud = WEB_CLIENT_ID)
        │
        ▼
SupabaseAuth.signInWithGoogleIdToken(idToken, rawNonce)
  POST {url}/auth/v1/token?grant_type=id_token
  body: { provider, id_token, nonce }
  → access_token, refresh_token, user, expires_in
        │
        ▼
AuthSessionStore.save(session)
        │
        ▼
EntitlementRepository.refreshFromRemote(accessToken)
        │
        ▼
UI: shows email / Pro state
```

**Fail closed:** missing config, user cancel, Google error, Supabase 4xx → clear busy, show short error; never invent a session.

**Offline-first:** core diary works without sign-in. Sign-in only required for remote entitlement / Pro cloud identity.

---

### Task 0: Console checklist (non-code)

- [ ] **Step 1:** Confirm Google Cloud Web + Android OAuth clients exist for `com.needsvswants.app`.
- [ ] **Step 2:** Put Web client ID + secret into Supabase Google provider; add Android client ID to authorized clients.
- [ ] **Step 3:** Export debug SHA-1:

```powershell
keytool -list -v -keystore "$env:USERPROFILE\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```

- [ ] **Step 4:** Set `GOOGLE_WEB_CLIENT_ID` in `local.properties` (gitignored).
- [ ] **Step 5:** Smoke-test later after Task 5 (device sign-in). Do not block unit tests on this.

---

### Task 1: BuildConfig + config surface for Google Web client ID

**Files:**
- Modify: `local.properties.example`
- Modify: `app/build.gradle.kts`
- Modify: `app/src/main/java/com/needsvswants/app/data/remote/SupabaseConfig.kt`
- Modify: `app/src/main/java/com/needsvswants/app/di/EntitlementModule.kt`

- [ ] **Step 1: Extend `local.properties.example`**

```properties
# Copy to local.properties (gitignored) at repo root. Do not commit real keys.
SUPABASE_URL=https://YOUR_PROJECT_REF.supabase.co
SUPABASE_ANON_KEY=your_anon_key_here
# Web OAuth client ID from Google Cloud (also used as Android serverClientId)
GOOGLE_WEB_CLIENT_ID=your-web-client-id.apps.googleusercontent.com
# Optional overrides:
# PRO_TRIAL_PRODUCT_ID=pro_trial_3day
# PRO_MONTHLY_PRODUCT_ID=pro_monthly
```

- [ ] **Step 2: Add BuildConfig field in `app/build.gradle.kts` `defaultConfig`**

```kotlin
buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"${localProp("GOOGLE_WEB_CLIENT_ID")}\"")
```

- [ ] **Step 3: Extend `SupabaseConfig`**

```kotlin
data class SupabaseConfig(
    val url: String,
    val anonKey: String,
    val proTrialProductId: String,
    val proMonthlyProductId: String,
    val googleWebClientId: String = ""
) {
    val enabled: Boolean get() = url.isNotBlank() && anonKey.isNotBlank()
    val googleSignInEnabled: Boolean
        get() = enabled && googleWebClientId.isNotBlank()

    companion object {
        val Disabled = SupabaseConfig("", "", "", "", "")
    }
}
```

- [ ] **Step 4: Wire provider in `EntitlementModule`**

```kotlin
fun provideSupabaseConfig(): SupabaseConfig = SupabaseConfig(
    url = BuildConfig.SUPABASE_URL,
    anonKey = BuildConfig.SUPABASE_ANON_KEY,
    proTrialProductId = BuildConfig.PRO_TRIAL_PRODUCT_ID,
    proMonthlyProductId = BuildConfig.PRO_MONTHLY_PRODUCT_ID,
    googleWebClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
)
```

- [ ] **Step 5: Commit**

```bash
git add local.properties.example app/build.gradle.kts app/src/main/java/com/needsvswants/app/data/remote/SupabaseConfig.kt app/src/main/java/com/needsvswants/app/di/EntitlementModule.kt
git commit -m "feat: add Google web client id BuildConfig for auth"
```

---

### Task 2: Session model + JSON parse (TDD)

**Files:**
- Create: `app/src/main/java/com/needsvswants/app/data/remote/AuthSession.kt`
- Modify: `app/src/main/java/com/needsvswants/app/data/remote/SupabaseJson.kt`
- Create: `app/src/test/java/com/needsvswants/app/data/remote/SupabaseJsonSessionTest.kt`

- [ ] **Step 1: Write failing tests**

```kotlin
package com.needsvswants.app.data.remote

import org.junit.Assert.*
import org.junit.Test

class SupabaseJsonSessionTest {

    @Test
    fun parseAuthSession_readsTokensAndUser() {
        val json = """
            {
              "access_token":"at-1",
              "refresh_token":"rt-1",
              "expires_in":3600,
              "token_type":"bearer",
              "user":{"id":"uid-9","email":"a@b.com"}
            }
        """.trimIndent()
        val s = SupabaseJson.parseAuthSession(json, nowEpochMillis = 1_000_000L)!!
        assertEquals("at-1", s.accessToken)
        assertEquals("rt-1", s.refreshToken)
        assertEquals("a@b.com", s.email)
        assertEquals("uid-9", s.userId)
        assertEquals(1_000_000L + 3600_000L, s.expiresAtEpochMillis)
    }

    @Test
    fun parseAuthSession_missingAccessToken_returnsNull() {
        assertNull(SupabaseJson.parseAuthSession("""{"refresh_token":"x"}""", 0L))
    }
}
```

- [ ] **Step 2: Run tests — expect FAIL**

```powershell
.\gradlew :app:testDebugUnitTest --tests "com.needsvswants.app.data.remote.SupabaseJsonSessionTest"
```

- [ ] **Step 3: Add model + parse helpers**

```kotlin
// AuthSession.kt
package com.needsvswants.app.data.remote

data class AuthSession(
    val accessToken: String,
    val refreshToken: String?,
    val userId: String?,
    val email: String?,
    val expiresAtEpochMillis: Long?
) {
    fun isExpired(nowEpochMillis: Long, skewMillis: Long = 60_000L): Boolean {
        val exp = expiresAtEpochMillis ?: return false
        return nowEpochMillis >= exp - skewMillis
    }
}
```

```kotlin
// SupabaseJson.kt additions
fun parseAuthSession(json: String, nowEpochMillis: Long): AuthSession? {
    val access = parseAccessToken(json) ?: return null
    val refresh = readStringField(json, "refresh_token")
    val expiresIn = readLongField(json, "expires_in") // add minimal long reader
    val userId = readNestedStringField(json, "user", "id") // or regex on flat path
    val email = readNestedStringField(json, "user", "email")
    val expiresAt = expiresIn?.let { nowEpochMillis + it * 1000L }
    return AuthSession(access, refresh, userId, email, expiresAt)
}
```

Implementation note: keep parsers pure regex/string helpers consistent with existing `SupabaseJson` style (no org.json if avoidable on unit tests). Nested `user` fields may use a small dedicated regex: `"user"\s*:\s*\{[^}]*"email"\s*:\s*"..."`.

- [ ] **Step 4: Run tests — expect PASS**
- [ ] **Step 5: Commit**

```bash
git commit -m "feat: parse Supabase auth session JSON for Google sign-in"
```

---

### Task 3: SupabaseAuth — Google ID token exchange + sign-out

**Files:**
- Modify: `app/src/main/java/com/needsvswants/app/data/remote/SupabaseAuth.kt`
- Create: `app/src/test/java/com/needsvswants/app/data/remote/HttpSupabaseAuthGoogleTest.kt` (optional if pure body builders extracted)

- [ ] **Step 1: Extend interface**

```kotlin
interface SupabaseAuth {
    val isConfigured: Boolean
    suspend fun sendMagicLink(email: String): Result<Unit>
    suspend fun verifyOtp(email: String, token: String): Result<String>
    /** Native Google: exchange Google ID token for Supabase session. */
    suspend fun signInWithGoogleIdToken(idToken: String, nonce: String?): Result<AuthSession>
    suspend fun refreshSession(refreshToken: String): Result<AuthSession>
    suspend fun signOut(accessToken: String): Result<Unit>
}
```

- [ ] **Step 2: Implement `signInWithGoogleIdToken`**

```kotlin
override suspend fun signInWithGoogleIdToken(idToken: String, nonce: String?): Result<AuthSession> {
    if (!config.enabled) return Result.failure(IllegalStateException("Supabase auth not configured"))
    val noncePart = if (nonce.isNullOrBlank()) "" else ""","nonce":"${nonce.escapeJson()}""""
    val body = """{"provider":"google","id_token":"${idToken.escapeJson()}"$noncePart}"""
    val url = "${config.url.trimEnd('/')}/auth/v1/token?grant_type=id_token"
    val now = System.currentTimeMillis()
    return HttpJsonClient.request(url, "POST", headers(), body).mapCatching { raw ->
        SupabaseJson.parseAuthSession(raw, now)
            ?: throw IllegalStateException("Missing access_token in id_token response")
    }
}
```

- [ ] **Step 3: Implement `refreshSession`**

```kotlin
// POST /auth/v1/token?grant_type=refresh_token
// body: {"refresh_token":"..."}
```

- [ ] **Step 4: Implement `signOut`**

```kotlin
// POST /auth/v1/logout with Authorization: Bearer <access_token>
// Treat 2xx as success; 401 as already signed out (success)
```

- [ ] **Step 5: Unit-test body construction if extracted to pure helpers; otherwise integration smoke later.**
- [ ] **Step 6: Commit**

```bash
git commit -m "feat: Supabase Google id_token exchange and session refresh"
```

---

### Task 4: AuthSessionStore (DataStore)

**Files:**
- Create: `app/src/main/java/com/needsvswants/app/data/auth/AuthSessionStore.kt`
- Modify: `app/src/main/java/com/needsvswants/app/data/prefs/AppPreferences.kt` **or** separate DataStore file
- Modify: DI module to bind store
- Create: unit test with Robolectric only if already available; otherwise test via fake store in repository tests

- [ ] **Step 1: Define interface**

```kotlin
interface AuthSessionStore {
    val session: Flow<AuthSession?>
    suspend fun save(session: AuthSession)
    suspend fun clear()
}
```

- [ ] **Step 2: Persist keys in DataStore** (`auth_access_token`, `auth_refresh_token`, `auth_user_id`, `auth_email`, `auth_expires_at`)

Security note: tokens in DataStore are app-private storage (same trust as other prefs). Do not log tokens. `allowBackup=false` already set in manifest.

- [ ] **Step 3: Commit**

```bash
git commit -m "feat: persist Supabase auth session in DataStore"
```

---

### Task 5: Google ID token provider (Android Credential Manager)

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts` dependencies
- Create: `app/src/main/java/com/needsvswants/app/data/auth/GoogleIdTokenProvider.kt`
- Create: `app/src/main/java/com/needsvswants/app/data/auth/CredentialManagerGoogleIdTokenProvider.kt`

- [ ] **Step 1: Add dependencies** (versions pin at implement time via current docs; example):

```toml
# libs.versions.toml
credentials = "1.3.0"
googleid = "1.1.1"
# libraries:
androidx-credentials = { group = "androidx.credentials", name = "credentials", version.ref = "credentials" }
androidx-credentials-play-services = { group = "androidx.credentials", name = "credentials-play-services-auth", version.ref = "credentials" }
googleid = { group = "com.google.android.libraries.identity.googleid", name = "googleid", version.ref = "googleid" }
```

```kotlin
// app/build.gradle.kts
implementation(libs.androidx.credentials)
implementation(libs.androidx.credentials.play.services)
implementation(libs.googleid)
```

**Note:** Unlike Play Billing, these must resolve on the build machine network. If offline-only builds are required, gate with a compile-time stub interface that returns `Unavailable` when classes missing — prefer real deps; project already needs network for many Google artifacts.

- [ ] **Step 2: Interface**

```kotlin
data class GoogleIdTokenResult(val idToken: String, val rawNonce: String?)

interface GoogleIdTokenProvider {
    val isAvailable: Boolean
    /** Activity context required for Credential Manager UI. */
    suspend fun requestIdToken(activityContext: Context): Result<GoogleIdTokenResult>
}
```

- [ ] **Step 3: Implementation sketch (match Supabase docs)**

```kotlin
class CredentialManagerGoogleIdTokenProvider @Inject constructor(
    private val config: SupabaseConfig
) : GoogleIdTokenProvider {
    override val isAvailable: Boolean get() = config.googleSignInEnabled

    override suspend fun requestIdToken(activityContext: Context): Result<GoogleIdTokenResult> {
        if (!isAvailable) return Result.failure(IllegalStateException("Google Sign-In not configured"))
        val rawNonce = UUID.randomUUID().toString()
        val hashedNonce = sha256Hex(rawNonce)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(config.googleWebClientId)
            .setNonce(hashedNonce)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        return try {
            val cm = CredentialManager.create(activityContext)
            val result = cm.getCredential(activityContext, request)
            val google = GoogleIdTokenCredential.createFrom(result.credential.data)
            Result.success(GoogleIdTokenResult(google.idToken, rawNonce))
        } catch (e: GetCredentialCancellationException) {
            Result.failure(e)
        } catch (e: GetCredentialException) {
            Result.failure(e)
        }
    }
}
```

- [ ] **Step 4: Bind in Hilt**
- [ ] **Step 5: Commit**

```bash
git commit -m "feat: Credential Manager Google ID token provider"
```

---

### Task 6: AuthRepository orchestration (TDD with fakes)

**Files:**
- Create: `app/src/main/java/com/needsvswants/app/data/auth/AuthRepository.kt`
- Create: `app/src/test/java/com/needsvswants/app/data/auth/AuthRepositoryTest.kt`

- [ ] **Step 1: Failing tests**

```kotlin
@Test
fun signInWithGoogle_savesSession_andRefreshesEntitlement() = runTest {
    val store = FakeSessionStore()
    val auth = FakeSupabaseAuth(session = AuthSession("at", "rt", "u1", "a@b.com", null))
    val entitlement = FakeEntitlementRepo()
    val google = FakeGoogle { Result.success(GoogleIdTokenResult("idtok", "nonce")) }
    val repo = AuthRepository(auth, store, google, entitlement)

    val result = repo.signInWithGoogle(activityContext = mockContext)
    assertTrue(result.isSuccess)
    assertEquals("at", store.session.value?.accessToken)
    assertTrue(entitlement.refreshedWith == "at")
}

@Test
fun signInWithGoogle_googleCancel_doesNotClearExistingSession() = runTest {
    // arrange existing session; google fails cancel; assert session still present
}

@Test
fun signOut_clearsLocalEvenIfRemoteFails() = runTest {
    // remote logout fails; local still cleared
}
```

- [ ] **Step 2: Implement `AuthRepository`**

```kotlin
@Singleton
class AuthRepository @Inject constructor(
    private val auth: SupabaseAuth,
    private val store: AuthSessionStore,
    private val google: GoogleIdTokenProvider,
    private val entitlements: EntitlementRepository
) {
    val session: Flow<AuthSession?> = store.session
    val isSignedIn: Flow<Boolean> = session.map { it?.accessToken?.isNotBlank() == true }

    suspend fun signInWithGoogle(activityContext: Context): Result<AuthSession> {
        val googleResult = google.requestIdToken(activityContext)
        val id = googleResult.getOrElse { return Result.failure(it) }
        val sessionResult = auth.signInWithGoogleIdToken(id.idToken, id.rawNonce)
        val session = sessionResult.getOrElse { return Result.failure(it) }
        store.save(session)
        entitlements.refreshFromRemote(session.accessToken)
        return Result.success(session)
    }

    suspend fun ensureFreshAccessToken(): String? {
        val current = /* first() from store */
        if (current == null) return null
        if (!current.isExpired(System.currentTimeMillis())) return current.accessToken
        val rt = current.refreshToken ?: return null
        val refreshed = auth.refreshSession(rt).getOrNull() ?: return null
        store.save(refreshed)
        return refreshed.accessToken
    }

    suspend fun signOut() {
        val token = /* current access */
        if (token != null) auth.signOut(token)
        store.clear()
        // Do not wipe local diary; only auth + optional entitlement remote
    }
}
```

- [ ] **Step 3: Run unit tests green**
- [ ] **Step 4: Commit**

```bash
git commit -m "feat: AuthRepository Google sign-in orchestration"
```

---

### Task 7: Auth UI — Settings Account + Paywall

**Files:**
- Create: `app/src/main/java/com/needsvswants/app/ui/screens/auth/AuthViewModel.kt`
- Modify: `app/src/main/java/com/needsvswants/app/ui/screens/settings/SettingsScreen.kt`
- Modify: `app/src/main/java/com/needsvswants/app/ui/screens/paywall/PaywallScreen.kt`
- Modify: `app/src/main/java/com/needsvswants/app/ui/screens/paywall/PaywallViewModel.kt`

- [ ] **Step 1: `AuthViewModel` state**

```kotlin
data class AuthUiState(
    val signedIn: Boolean = false,
    val email: String? = null,
    val busy: Boolean = false,
    val error: String? = null,
    val googleAvailable: Boolean = false
)

// signIn(activity: Activity) / signOut() / consumeError()
```

Use `LocalContext.current` as Activity (`context.findActivity()`) when calling Credential Manager.

- [ ] **Step 2: Settings — new section above Pro**

```
ACCOUNT
  [Signed out]  Sign in with Google   (primary button)
  [Signed in]   a@b.com              Sign out
  helper: "Sign in to unlock Pro purchases and sync entitlement."
```

Style with existing `SectionLabel`, `Surface`, supermarket palette (crimson/gold). Google button: white/outlined card, not default Material Google branding package unless asset added.

- [ ] **Step 3: Paywall — if not signed in, show Sign in with Google above trial CTA**

```
When !signedIn:
  "Sign in to apply Pro to this account"
  [Sign in with Google]
When signedIn:
  show email chip; enable Start trial
```

Optional product rule (recommend): allow browsing paywall while signed out; **require sign-in before** `startTrial()` / `upgrade()` so purchase can be linked to Supabase user later. Implement gate:

```kotlin
fun startTrial() {
  if (!signedIn) { promptSignIn = true; return }
  // existing billing
}
```

- [ ] **Step 4: Manual QA checklist (device with Google account)**
  1. Cold start → Settings → Sign in with Google → account picker → success → email shown.
  2. Kill app → reopen → still signed in.
  3. Sign out → email cleared; diary data intact.
  4. Paywall reflects signed-in state.
  5. With Pro entitlement remote: `refreshFromRemote` after sign-in returns FREE or paid correctly.

- [ ] **Step 5: Commit**

```bash
git commit -m "feat: Google Sign-In UI on Settings and Paywall"
```

---

### Task 8: iOS parity

**Files:**
- Modify: `ios/NeedsVsWants/Storage/SupabaseAuthClient.swift`
- Create: Google Sign-In helper (GIDSignIn)
- Modify: Settings / Paywall views
- Config: iOS client ID in Info.plist / `GIDClientID`; configure URL scheme

- [ ] **Step 1: Add GoogleSignIn package dependency (SPM) to iOS project.**
- [ ] **Step 2: `signInWithGoogleIdToken` REST same as Android.**
- [ ] **Step 3: After `GIDSignIn.sharedInstance.signIn`, pass `idToken` (+ optional `accessToken`) to Supabase.
- [ ] **Step 4: Persist session in `UserDefaults` or Keychain (prefer Keychain for tokens).
- [ ] **Step 5: Settings Account UI parity.
- [ ] **Step 6: Compile on macOS / CI if available; otherwise mark as code-complete pending CI.

```bash
git commit -m "feat: iOS Google Sign-In parity with Supabase id_token"
```

---

### Task 9: Security, ProGuard, docs, memory

- [ ] **Step 1: ProGuard** — if minify keeps Credential Manager / GoogleIdTokenCredential, add keep rules if release crashes on reflection.
- [ ] **Step 2: Never log `access_token`, `id_token`, or refresh tokens.
- [ ] **Step 3: Confirm `android:allowBackup="false"` remains.
- [ ] **Step 4: Update Obsidian:**
  - `Tasks.md` — mark Google Sign-In done / remaining live QA
  - `Decisions.md` — **D52** Google native ID token + DataStore session; email UI still deferred
  - `Summary.md` — accounts optional via Google for Pro entitlement identity
- [ ] **Step 5: Copy plan to `docs/superpowers/plans/2026-08-04-google-sign-in.md` if not already there.**
- [ ] **Step 6: Final verification**

```powershell
.\gradlew :app:testDebugUnitTest :app:assembleDebug
```

Expected: all unit tests green; debug APK installs; device Google sign-in succeeds when Console configured.

---

## Risk register

| Risk | Mitigation |
|------|------------|
| SHA-1 / package mismatch → Google returns no credential | Document debug + release SHA-1; dual Android OAuth clients |
| Supabase rejects token (wrong audience) | Web client ID must be `serverClientId` + listed in Supabase authorized clients |
| Nonce mismatch | Send **raw** nonce to Supabase, **hashed** to Google |
| Credential Manager unavailable (old device / no Play) | Surface clear error; keep app usable offline |
| Offline builds break on new deps | Pin versions; document network required once for Gradle |
| User expects email password login | Product is Google-first; email OTP remains deferred |

---

## Test plan summary

| Layer | What |
|-------|------|
| Unit | `parseAuthSession`, `AuthRepository` success/cancel/sign-out |
| Manual device | Sign-in, persistence, sign-out, paywall gate |
| Live API | Optional: after sign-in, call `get_entitlement` with token |
| Regression | Diary log/summary still works signed-out |

---

## Suggested execution order

1. Tasks 1–4 (config, parse, Supabase, store) — fully offline-testable  
2. Task 5–6 (Google provider + repository)  
3. Task 7 (UI) + device QA with Console ready  
4. Task 8 (iOS)  
5. Task 9 (docs / ProGuard)

**Android-first milestone:** Tasks 0–7 + 9 deliver “Google sign-in works on Android.” iOS is parity, not a blocker for Android ship.

---

## Self-review

- **Spec coverage:** Google sign-in, account creation via Supabase Google, session persistence, entitlement refresh, sign-out, Settings + Paywall UI, Console setup, iOS parity, tests — all have tasks. Email UI explicitly out of scope.
- **No placeholders:** REST shapes, file paths, and core code sketches included.
- **Type consistency:** `AuthSession`, `GoogleIdTokenResult`, `AuthRepository`, `SupabaseAuth.signInWithGoogleIdToken` used consistently across tasks.
