package com.needsvswants.app.data.entitlement

import com.needsvswants.app.BuildConfig
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementTier
import com.needsvswants.app.domain.EntitlementType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EntitlementRepositoryTest {

    @Test
    fun saveLocal_without_sync_stamp_does_not_grant_pro() = runTest {
        val local = FakeLocalStore()
        val remote = FakeRemote(null)
        val repo = EntitlementRepository(local, remote)

        assertFalse(repo.isPro.first())

        val paid = Entitlement(
            tier = EntitlementTier.PRO,
            type = EntitlementType.PAID,
            expiresAtEpochMillis = null
        )
        repo.saveLocal(paid)

        // Unstamped local Pro must not unlock (DataStore spoof / saveLocal alone).
        assertFalse(repo.isPro.first())
        assertEquals(Entitlement.Free, repo.entitlement.first())
    }

    @Test
    fun clearLocal_revertsToFree() = runTest {
        val local = FakeLocalStore()
        val repo = EntitlementRepository(local, FakeRemote(null))
        local.setEntitlement(
            Entitlement(type = EntitlementType.PAID, expiresAtEpochMillis = null)
        )
        local.markEntitlementSynced(System.currentTimeMillis())
        assertTrue(repo.isPro.first())
        repo.clearLocal()
        assertFalse(repo.isPro.first())
        assertEquals(Entitlement(), repo.entitlement.first())
    }

    @Test
    fun refreshFromRemote_whenNull_keepsTrustedLocal() = runTest {
        val local = FakeLocalStore()
        val paid = Entitlement(type = EntitlementType.PAID, expiresAtEpochMillis = null)
        local.setEntitlement(paid)
        local.markEntitlementSynced(System.currentTimeMillis())
        val repo = EntitlementRepository(local, FakeRemote(null))

        repo.refreshFromRemote(accessToken = "tok")
        assertEquals(paid, repo.entitlement.first())
    }

    @Test
    fun refreshFromRemote_whenFresh_persists_and_stamps_sync() = runTest {
        val local = FakeLocalStore()
        val fresh = Entitlement(
            tier = EntitlementTier.MAX,
            type = EntitlementType.PAID,
            expiresAtEpochMillis = null,
            provider = "supabase"
        )
        val repo = EntitlementRepository(local, FakeRemote(fresh))

        repo.refreshFromRemote(accessToken = "tok")
        assertEquals(fresh, repo.entitlement.first())
        assertTrue(repo.hasMaxAccess.first())
        assertTrue(local.entitlementSyncedAtMillis.first() > 0L)
    }

    @Test
    fun stalePaidSnapshot_without_sync_is_free_on_full_flavor() = runTest {
        // BuildConfig.PLAIN_FREE is compiled per flavor:
        //  - full: unstamped paid DataStore snapshot degrades to Free (P2 trust).
        //  - plain: forced Free regardless.
        val local = FakeLocalStore()
        val paid = Entitlement(
            tier = EntitlementTier.MAX,
            type = EntitlementType.PAID,
            expiresAtEpochMillis = null
        )
        local.setEntitlement(paid)
        val repo = EntitlementRepository(local, FakeRemote(null))

        val exposed = repo.entitlement.first()
        assertEquals(Entitlement.Free, exposed)
        assertFalse(repo.hasMaxAccess.first())
        if (BuildConfig.PLAIN_FREE) {
            assertTrue(BuildConfig.PLAIN_FREE)
        }
    }

    private class FakeLocalStore : EntitlementLocalStore {
        private val state = MutableStateFlow(Entitlement())
        private val synced = MutableStateFlow(0L)
        override val entitlement: Flow<Entitlement> = state
        override val entitlementSyncedAtMillis: Flow<Long> = synced
        override suspend fun setEntitlement(entitlement: Entitlement) {
            state.value = entitlement
        }
        override suspend fun markEntitlementSynced(atMillis: Long) {
            synced.value = atMillis
        }
        override suspend fun clearEntitlement() {
            state.value = Entitlement()
            synced.value = 0L
        }
    }

    private class FakeRemote(private val value: Entitlement?) : EntitlementRemote {
        override suspend fun fetchEntitlement(accessToken: String?): Entitlement? = value
    }
}
