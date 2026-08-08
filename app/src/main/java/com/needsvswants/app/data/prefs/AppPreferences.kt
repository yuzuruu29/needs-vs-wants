package com.needsvswants.app.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.needsvswants.app.data.auth.AuthSessionStore
import com.needsvswants.app.data.entitlement.EntitlementLocalStore
import com.needsvswants.app.data.entitlement.EntitlementSnapshot
import com.needsvswants.app.data.entitlement.PayPalReturnStore
import com.needsvswants.app.data.remote.AuthSession
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.EntitlementType
import com.needsvswants.app.domain.DailyLogQuota
import com.needsvswants.app.domain.QuotaState
import com.needsvswants.app.domain.FontScaleStep
import com.needsvswants.app.domain.ThemeId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Pure TTL gate for the durable PayPal-return pending flag: a stored timestamp
 * reports active only while it is recent (strictly within [ttlMillis] of the
 * current time). 0 (no flag) and expired timestamps report inactive, so a stale
 * return — webhook never landed, return missed — stops retrying after the TTL.
 * Extracted as a pure function so the boundary behavior is unit-testable.
 */
fun paypalReturnPendingActive(
    storedAt: Long,
    nowMillis: Long,
    ttlMillis: Long = 24 * 60 * 60 * 1000L
): Boolean = storedAt > 0L && nowMillis - storedAt < ttlMillis

class AppPreferences(private val context: Context) : EntitlementLocalStore, AuthSessionStore, PayPalReturnStore {
    companion object {
        private val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")
        private val CURRENCY_CODE = stringPreferencesKey("currency_code")
        private val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        private val DAILY_BUDGET_CENTS = longPreferencesKey("daily_budget_cents")
        private val BEST_STREAK_EVER = intPreferencesKey("best_streak_ever")
        private val LAST_MILESTONE_SHOWN = intPreferencesKey("last_milestone_shown")
        private val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        private val REMINDER_HOUR = intPreferencesKey("reminder_hour")
        private val SFX_ENABLED = booleanPreferencesKey("sfx_enabled")
        private val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        private val REDUCED_MOTION = booleanPreferencesKey("reduced_motion")
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
        private val QUOTA_DAY = stringPreferencesKey("quota_day")
        private val QUOTA_LOGS_CREATED = intPreferencesKey("quota_logs_created")
        private val QUOTA_BONUS_LOGS = intPreferencesKey("quota_bonus_logs")
        private val QUOTA_ADS_WATCHED = intPreferencesKey("quota_ads_watched")
        private val PAYPAL_RETURN_PENDING_AT = longPreferencesKey("paypal_return_pending_at")
    }

    val currencySymbol: Flow<String> = context.dataStore.data.map { it[CURRENCY_SYMBOL] ?: "₱" }
    val currencyCode: Flow<String> = context.dataStore.data.map { it[CURRENCY_CODE] ?: "PHP" }
    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { it[FIRST_LAUNCH] ?: true }

    val bestStreakEver: Flow<Int> = context.dataStore.data.map { it[BEST_STREAK_EVER] ?: 0 }
    val lastMilestoneShown: Flow<Int> = context.dataStore.data.map { it[LAST_MILESTONE_SHOWN] ?: 0 }
    val reminderEnabled: Flow<Boolean> = context.dataStore.data.map { it[REMINDER_ENABLED] ?: false }
    val reminderHour: Flow<Int> = context.dataStore.data.map { it[REMINDER_HOUR] ?: 20 }
    /** Interaction sounds (tap / long-press / orb). Default on. */
    val sfxEnabled: Flow<Boolean> = context.dataStore.data.map { it[SFX_ENABLED] ?: true }
    /** Vibration / haptic feedback. Default on. */
    val hapticsEnabled: Flow<Boolean> = context.dataStore.data.map { it[HAPTICS_ENABLED] ?: true }
    /** In-app reduced motion (also collapses when system animator scale is 0). Default off. */
    val reducedMotion: Flow<Boolean> = context.dataStore.data.map { it[REDUCED_MOTION] ?: false }

    suspend fun setReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { it[REMINDER_ENABLED] = enabled }
    }

    suspend fun setReminderHour(hour: Int) {
        context.dataStore.edit { it[REMINDER_HOUR] = hour }
    }

    suspend fun setSfxEnabled(enabled: Boolean) {
        context.dataStore.edit { it[SFX_ENABLED] = enabled }
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[HAPTICS_ENABLED] = enabled }
    }

    suspend fun setReducedMotion(enabled: Boolean) {
        context.dataStore.edit { it[REDUCED_MOTION] = enabled }
    }

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

    // --- PayPal checkout return (durable across process death) --------------

    /**
     * True while a PayPal checkout return is pending but not yet confirmed.
     * A flag older than the 24h TTL is treated as stale (webhook never landed,
     * return missed) and reports inactive, so a cold start never retries forever.
     */
    override val paypalReturnPending: Flow<Boolean> = context.dataStore.data.map {
        paypalReturnPendingActive(it[PAYPAL_RETURN_PENDING_AT] ?: 0L, System.currentTimeMillis())
    }

    override suspend fun setPaypalReturnPending(pending: Boolean) {
        context.dataStore.edit {
            it[PAYPAL_RETURN_PENDING_AT] = if (pending) System.currentTimeMillis() else 0L
        }
    }

    override suspend fun clearPaypalReturnPending() {
        context.dataStore.edit { it[PAYPAL_RETURN_PENDING_AT] = 0L }
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

    suspend fun updateBestStreak(streak: Int) {
        context.dataStore.edit { prefs ->
            val currentBest = prefs[BEST_STREAK_EVER] ?: 0
            if (streak > currentBest) {
                prefs[BEST_STREAK_EVER] = streak
            }
        }
    }

    suspend fun setLastMilestoneShown(milestoneDays: Int) {
        context.dataStore.edit { it[LAST_MILESTONE_SHOWN] = milestoneDays }
    }

    val quotaState: Flow<QuotaState> = context.dataStore.data.map { prefs ->
        QuotaState(
            day = prefs[QUOTA_DAY] ?: "",
            logsCreated = prefs[QUOTA_LOGS_CREATED] ?: 0,
            bonusLogs = prefs[QUOTA_BONUS_LOGS] ?: 0,
            adsWatched = prefs[QUOTA_ADS_WATCHED] ?: 0
        )
    }

    suspend fun setQuotaState(state: QuotaState) {
        context.dataStore.edit {
            it[QUOTA_DAY] = state.day
            it[QUOTA_LOGS_CREATED] = state.logsCreated
            it[QUOTA_BONUS_LOGS] = state.bonusLogs
            it[QUOTA_ADS_WATCHED] = state.adsWatched
        }
    }

    suspend fun resetQuotaForDay(today: String) {
        setQuotaState(QuotaState(day = today, logsCreated = 0, bonusLogs = 0, adsWatched = 0))
    }

    suspend fun wipeAll() {
        context.dataStore.edit { it.clear() }
    }
}
