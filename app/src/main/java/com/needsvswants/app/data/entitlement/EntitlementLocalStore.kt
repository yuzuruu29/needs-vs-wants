package com.needsvswants.app.data.entitlement

import com.needsvswants.app.domain.Entitlement
import kotlinx.coroutines.flow.Flow

/**
 * Local persistence seam for entitlement. [AppPreferences] is the production
 * implementation; tests inject an in-memory fake.
 */
interface EntitlementLocalStore {
    val entitlement: Flow<Entitlement>
    suspend fun setEntitlement(entitlement: Entitlement)
    suspend fun clearEntitlement()
}
