package com.needsvswants.app.data.auth

import com.needsvswants.app.data.remote.AuthSession
import kotlinx.coroutines.flow.Flow

/**
 * Local persistence for the Supabase auth session.
 * Production impl: [com.needsvswants.app.data.prefs.AppPreferences].
 */
interface AuthSessionStore {
    val session: Flow<AuthSession?>
    suspend fun save(session: AuthSession)
    suspend fun clear()
}
