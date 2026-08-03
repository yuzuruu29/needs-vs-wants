package com.needsvswants.app.data.auth

import com.needsvswants.app.data.entitlement.EntitlementLocalStore
import com.needsvswants.app.data.entitlement.EntitlementRemote
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.remote.AuthSession
import com.needsvswants.app.data.remote.SupabaseAuth
import com.needsvswants.app.data.remote.SupabaseConfig
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthRepositoryTest {

    @Test
    fun completeGoogleSignIn_savesSession_andRefreshesEntitlement() = runTest {
        val store = FakeSessionStore()
        val session = AuthSession("at", "rt", "u1", "a@b.com", null)
        val auth = FakeSupabaseAuth(session = session)
        val remote = RecordingRemote()
        val entitlements = EntitlementRepository(FakeEntitlementLocal(), remote)
        val google = UnusedGoogle
        val repo = AuthRepository(auth, store, google, entitlements, configEnabled())

        val result = repo.completeGoogleSignIn(
            Result.success(GoogleIdTokenResult("idtok", "nonce"))
        )
        assertTrue(result.isSuccess)
        assertEquals("at", store.session.first()?.accessToken)
        assertEquals("at", remote.lastToken)
        assertEquals("idtok", auth.lastIdToken)
        assertEquals("nonce", auth.lastNonce)
    }

    @Test
    fun completeGoogleSignIn_googleCancel_doesNotClearExistingSession() = runTest {
        val existing = AuthSession("old-at", "rt", "u1", "old@b.com", null)
        val store = FakeSessionStore(existing)
        val auth = FakeSupabaseAuth(session = AuthSession("new", null, null, null, null))
        val entitlements = EntitlementRepository(FakeEntitlementLocal(), RecordingRemote())
        val repo = AuthRepository(auth, store, UnusedGoogle, entitlements, configEnabled())

        val result = repo.completeGoogleSignIn(Result.failure(RuntimeException("cancelled")))
        assertTrue(result.isFailure)
        assertEquals("old-at", store.session.first()?.accessToken)
    }

    @Test
    fun signOut_clearsLocalEvenIfRemoteFails() = runTest {
        val store = FakeSessionStore(AuthSession("at", "rt", "u1", "a@b.com", null))
        val auth = FakeSupabaseAuth(
            session = null,
            signOutResult = Result.failure(RuntimeException("network"))
        )
        val entitlements = EntitlementRepository(FakeEntitlementLocal(), RecordingRemote())
        val repo = AuthRepository(auth, store, UnusedGoogle, entitlements, configEnabled())

        repo.signOut()
        assertNull(store.session.first())
    }

    @Test
    fun ensureFreshAccessToken_refreshesWhenExpired() = runTest {
        val expired = AuthSession("old", "rt", "u1", "a@b.com", expiresAtEpochMillis = 1_000L)
        val store = FakeSessionStore(expired)
        val refreshed = AuthSession("new-at", "rt2", "u1", "a@b.com", expiresAtEpochMillis = 9_999_999L)
        val auth = FakeSupabaseAuth(session = refreshed)
        val entitlements = EntitlementRepository(FakeEntitlementLocal(), RecordingRemote())
        val repo = AuthRepository(auth, store, UnusedGoogle, entitlements, configEnabled())

        val token = repo.ensureFreshAccessToken(nowEpochMillis = 5_000L)
        assertEquals("new-at", token)
        assertEquals("new-at", store.session.first()?.accessToken)
    }

    private fun configEnabled() = SupabaseConfig(
        url = "https://example.supabase.co",
        anonKey = "anon",
        proTrialProductId = "t",
        proMonthlyProductId = "m",
        googleWebClientId = "web.apps.googleusercontent.com"
    )
}

// --- Fakes ------------------------------------------------------------------

private object UnusedGoogle : GoogleIdTokenProvider {
    override val isAvailable: Boolean = true
    override suspend fun requestIdToken(activityContext: android.content.Context): Result<GoogleIdTokenResult> =
        Result.failure(UnsupportedOperationException("use completeGoogleSignIn in unit tests"))
}

private class FakeSessionStore(
    initial: AuthSession? = null
) : AuthSessionStore {
    private val state = MutableStateFlow(initial)
    override val session: Flow<AuthSession?> = state
    override suspend fun save(session: AuthSession) {
        state.value = session
    }
    override suspend fun clear() {
        state.value = null
    }
}

private class FakeSupabaseAuth(
    private val session: AuthSession?,
    private val signOutResult: Result<Unit> = Result.success(Unit)
) : SupabaseAuth {
    var lastIdToken: String? = null
    var lastNonce: String? = null
    override val isConfigured: Boolean = true
    override suspend fun sendMagicLink(email: String) = Result.success(Unit)
    override suspend fun verifyOtp(email: String, token: String) = Result.success("tok")
    override suspend fun signInWithGoogleIdToken(idToken: String, nonce: String?): Result<AuthSession> {
        lastIdToken = idToken
        lastNonce = nonce
        return session?.let { Result.success(it) }
            ?: Result.failure(IllegalStateException("no session"))
    }
    override suspend fun refreshSession(refreshToken: String): Result<AuthSession> {
        return session?.let { Result.success(it) }
            ?: Result.failure(IllegalStateException("no session"))
    }
    override suspend fun signOut(accessToken: String): Result<Unit> = signOutResult
}

private class RecordingRemote : EntitlementRemote {
    var lastToken: String? = null
    override suspend fun fetchEntitlement(accessToken: String?): Entitlement? {
        lastToken = accessToken
        return Entitlement.Free
    }
}

private class FakeEntitlementLocal : EntitlementLocalStore {
    private val state = MutableStateFlow(Entitlement.Free)
    override val entitlement: Flow<Entitlement> = state
    override suspend fun setEntitlement(entitlement: Entitlement) {
        state.value = entitlement
    }
    override suspend fun clearEntitlement() {
        state.value = Entitlement.Free
    }
}
