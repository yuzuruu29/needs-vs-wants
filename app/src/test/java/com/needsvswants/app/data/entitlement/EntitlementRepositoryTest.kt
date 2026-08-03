package com.needsvswants.app.data.entitlement

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
    fun saveLocal_updatesFlow() = runTest {
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

        assertTrue(repo.isPro.first())
        assertEquals(paid, repo.entitlement.first())
    }

    @Test
    fun clearLocal_revertsToFree() = runTest {
        val local = FakeLocalStore()
        val repo = EntitlementRepository(local, FakeRemote(null))
        repo.saveLocal(
            Entitlement(type = EntitlementType.PAID, expiresAtEpochMillis = null)
        )
        repo.clearLocal()
        assertFalse(repo.isPro.first())
        assertEquals(Entitlement(), repo.entitlement.first())
    }

    @Test
    fun refreshFromRemote_whenNull_keepsLocal() = runTest {
        val local = FakeLocalStore()
        val paid = Entitlement(type = EntitlementType.PAID, expiresAtEpochMillis = null)
        local.setEntitlement(paid)
        val repo = EntitlementRepository(local, FakeRemote(null))

        repo.refreshFromRemote(accessToken = "tok")
        assertEquals(paid, repo.entitlement.first())
    }

    @Test
    fun refreshFromRemote_whenFresh_persists() = runTest {
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
    }

    private class FakeLocalStore : EntitlementLocalStore {
        private val state = MutableStateFlow(Entitlement())
        override val entitlement: Flow<Entitlement> = state
        override suspend fun setEntitlement(entitlement: Entitlement) {
            state.value = entitlement
        }
        override suspend fun clearEntitlement() {
            state.value = Entitlement()
        }
    }

    private class FakeRemote(private val value: Entitlement?) : EntitlementRemote {
        override suspend fun fetchEntitlement(accessToken: String?): Entitlement? = value
    }
}
