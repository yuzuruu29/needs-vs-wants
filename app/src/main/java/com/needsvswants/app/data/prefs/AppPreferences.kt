package com.needsvswants.app.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.needsvswants.app.data.auth.AuthSessionStore
import com.needsvswants.app.data.entitlement.EntitlementLocalStore
import com.needsvswants.app.data.entitlement.EntitlementSnapshot
import com.needsvswants.app.data.remote.AuthSession
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementType
import com.needsvswants.app.domain.FontScaleStep
import com.needsvswants.app.domain.ThemeId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppPreferences(private val context: Context) : EntitlementLocalStore, AuthSessionStore {
    companion object {
        private val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")
        private val CURRENCY_CODE = stringPreferencesKey("currency_code")
        private val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        private val DAILY_BUDGET_CENTS = longPreferencesKey("daily_budget_cents")
        private val THEME_ID = stringPreferencesKey("theme_id")
        private val FONT_SCALE_STEP = stringPreferencesKey("font_scale_step")
        private val ENTITLEMENT_TIER = stringPreferencesKey("entitlement_tier")
        private val ENTITLEMENT_TYPE = stringPreferencesKey("entitlement_type")
        private val ENTITLEMENT_EXPIRES_AT = longPreferencesKey("entitlement_expires_at")
        private val ENTITLEMENT_PROVIDER = stringPreferencesKey("entitlement_provider")
        private val ENTITLEMENT_SOURCE = stringPreferencesKey("entitlement_source")
        private val ENTITLEMENT_STATUS = stringPreferencesKey("entitlement_status")
        private val ENTITLEMENT_KEYS = listOf(
            ENTITLEMENT_TIER,
            ENTITLEMENT_TYPE,
            ENTITLEMENT_EXPIRES_AT,
            ENTITLEMENT_PROVIDER,
            ENTITLEMENT_SOURCE,
            ENTITLEMENT_STATUS
        )
        private val AUTH_ACCESS_TOKEN = stringPreferencesKey("auth_access_token")
        private val AUTH_REFRESH_TOKEN = stringPreferencesKey("auth_refresh_token")
        private val AUTH_USER_ID = stringPreferencesKey("auth_user_id")
        private val AUTH_EMAIL = stringPreferencesKey("auth_email")
        private val AUTH_EXPIRES_AT = longPreferencesKey("auth_expires_at")
        private val AUTH_KEYS = listOf(
            AUTH_ACCESS_TOKEN,
            AUTH_REFRESH_TOKEN,
            AUTH_USER_ID,
            AUTH_EMAIL,
            AUTH_EXPIRES_AT
        )
    }

    val currencySymbol: Flow<String> = context.dataStore.data.map { it[CURRENCY_SYMBOL] ?: "₱" }
    val currencyCode: Flow<String> = context.dataStore.data.map { it[CURRENCY_CODE] ?: "PHP" }
    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { it[FIRST_LAUNCH] ?: true }

    val themeId: Flow<ThemeId> = context.dataStore.data.map {
        ThemeId.fromStorage(it[THEME_ID])
    }
    val fontScaleStep: Flow<FontScaleStep> = context.dataStore.data.map {
        FontScaleStep.fromStorage(it[FONT_SCALE_STEP])
    }

    /** null means budget is off (missing or ≤ 0). */
    val dailyBudgetCents: Flow<Long?> = context.dataStore.data.map { prefs ->
        val v = prefs[DAILY_BUDGET_CENTS] ?: return@map null
        if (v <= 0L) null else v
    }

    suspend fun setDailyBudgetCents(cents: Long) {
        require(cents > 0L) { "daily budget must be positive cents" }
        context.dataStore.edit { it[DAILY_BUDGET_CENTS] = cents }
    }

    suspend fun clearDailyBudget() {
        context.dataStore.edit { it.remove(DAILY_BUDGET_CENTS) }
    }

    suspend fun setThemeId(id: ThemeId) {
        context.dataStore.edit { it[THEME_ID] = id.storageKey }
    }

    suspend fun setFontScaleStep(step: FontScaleStep) {
        context.dataStore.edit { it[FONT_SCALE_STEP] = step.storageKey }
    }

    /** Locally persisted entitlement snapshot. Absent entry resolves to FREE. */
    override val entitlement: Flow<Entitlement> = context.dataStore.data.map { prefs ->
        val lookup = EntitlementSnapshot(
            tier = prefs[ENTITLEMENT_TIER]
                ?.let { runCatching { com.needsvswants.app.domain.EntitlementTier.valueOf(it) }.getOrDefault(com.needsvswants.app.domain.EntitlementTier.FREE) }
                ?: com.needsvswants.app.domain.EntitlementTier.FREE,
            type = prefs[ENTITLEMENT_TYPE]
                ?.let { runCatching { EntitlementType.valueOf(it) }.getOrDefault(EntitlementType.FREE) }
                ?: EntitlementType.FREE,
            expiresAtEpochMillis = prefs[ENTITLEMENT_EXPIRES_AT],
            provider = prefs[ENTITLEMENT_PROVIDER],
            source = prefs[ENTITLEMENT_SOURCE],
            status = prefs[ENTITLEMENT_STATUS]
        )
        EntitlementSnapshot.orFree(lookup)
    }

    override suspend fun setEntitlement(entitlement: Entitlement) {
        val snapshot = EntitlementSnapshot.fromEntitlement(entitlement)
        context.dataStore.edit {
            it[ENTITLEMENT_TIER] = snapshot.tier.name
            it[ENTITLEMENT_TYPE] = snapshot.type.name
            if (snapshot.expiresAtEpochMillis != null) {
                it[ENTITLEMENT_EXPIRES_AT] = snapshot.expiresAtEpochMillis
            } else {
                it.remove(ENTITLEMENT_EXPIRES_AT)
            }
            snapshot.provider?.let { p -> it[ENTITLEMENT_PROVIDER] = p } ?: it.remove(ENTITLEMENT_PROVIDER)
            snapshot.source?.let { s -> it[ENTITLEMENT_SOURCE] = s } ?: it.remove(ENTITLEMENT_SOURCE)
            snapshot.status?.let { s -> it[ENTITLEMENT_STATUS] = s } ?: it.remove(ENTITLEMENT_STATUS)
        }
    }

    override suspend fun clearEntitlement() {
        context.dataStore.edit { prefs -> ENTITLEMENT_KEYS.forEach { prefs.remove(it) } }
    }

    // --- Auth session (Google / Supabase) ------------------------------------

    override val session: Flow<AuthSession?> = context.dataStore.data.map { prefs ->
        val access = prefs[AUTH_ACCESS_TOKEN]?.takeIf { it.isNotBlank() } ?: return@map null
        AuthSession(
            accessToken = access,
            refreshToken = prefs[AUTH_REFRESH_TOKEN],
            userId = prefs[AUTH_USER_ID],
            email = prefs[AUTH_EMAIL],
            expiresAtEpochMillis = prefs[AUTH_EXPIRES_AT]
        )
    }

    override suspend fun save(session: AuthSession) {
        context.dataStore.edit {
            it[AUTH_ACCESS_TOKEN] = session.accessToken
            session.refreshToken?.let { rt -> it[AUTH_REFRESH_TOKEN] = rt }
                ?: it.remove(AUTH_REFRESH_TOKEN)
            session.userId?.let { id -> it[AUTH_USER_ID] = id } ?: it.remove(AUTH_USER_ID)
            session.email?.let { e -> it[AUTH_EMAIL] = e } ?: it.remove(AUTH_EMAIL)
            session.expiresAtEpochMillis?.let { exp -> it[AUTH_EXPIRES_AT] = exp }
                ?: it.remove(AUTH_EXPIRES_AT)
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { prefs -> AUTH_KEYS.forEach { prefs.remove(it) } }
    }

    suspend fun setCurrency(symbol: String, code: String) {
        context.dataStore.edit {
            it[CURRENCY_SYMBOL] = symbol
            it[CURRENCY_CODE] = code
        }
    }

    suspend fun setFirstLaunchComplete() {
        context.dataStore.edit { it[FIRST_LAUNCH] = false }
    }

    suspend fun wipeAll() {
        context.dataStore.edit { it.clear() }
    }
}
