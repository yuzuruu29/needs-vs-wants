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

/** A newer release advertised by the site's version.json (see UpdateChecker). */
data class AvailableUpdate(
    val versionName: String,
    val versionCode: Int,
    val apkUrl: String
)

class AppPreferences internal constructor(private val dataStore: DataStore<Preferences>) :
    EntitlementLocalStore, AuthSessionStore, PayPalReturnStore {

    /** Production path: the app-wide singleton DataStore behind [context]. */
    constructor(context: Context) : this(context.dataStore)
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
        private val ENTITLEMENT_SYNCED_AT = longPreferencesKey("entitlement_synced_at")
        private val ENTITLEMENT_KEYS = listOf(
            ENTITLEMENT_TIER,
            ENTITLEMENT_TYPE,
            ENTITLEMENT_EXPIRES_AT,
            ENTITLEMENT_PROVIDER,
            ENTITLEMENT_SOURCE,
            ENTITLEMENT_STATUS,
            ENTITLEMENT_SYNCED_AT
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
        private val QUOTA_CARRIED_LOGS = intPreferencesKey("quota_carried_logs")
        private val PAYPAL_RETURN_PENDING_AT = longPreferencesKey("paypal_return_pending_at")
        private val SPENDING_GOAL = stringPreferencesKey("spending_goal")
        private val BUDGET_NUDGE_PENDING = booleanPreferencesKey("budget_nudge_pending")
        private val CRASH_REPORTS_ENABLED = booleanPreferencesKey("crash_reports_enabled")
        private val BACKUP_FOLDER_URI = stringPreferencesKey("backup_folder_uri")
        private val AUTO_BACKUP_ENABLED = booleanPreferencesKey("auto_backup_enabled")
        private val LAST_BACKUP_AT = longPreferencesKey("last_backup_at")
        private val LAST_UPDATE_CHECK_AT = longPreferencesKey("last_update_check_at")
        private val UPDATE_AVAILABLE_NAME = stringPreferencesKey("update_available_name")
        private val UPDATE_AVAILABLE_CODE = intPreferencesKey("update_available_code")
        private val UPDATE_AVAILABLE_URL = stringPreferencesKey("update_available_url")
    }

    val currencySymbol: Flow<String> = dataStore.data.map { it[CURRENCY_SYMBOL] ?: "₱" }
    val currencyCode: Flow<String> = dataStore.data.map { it[CURRENCY_CODE] ?: "PHP" }
    val isFirstLaunch: Flow<Boolean> = dataStore.data.map { it[FIRST_LAUNCH] ?: true }

    val bestStreakEver: Flow<Int> = dataStore.data.map { it[BEST_STREAK_EVER] ?: 0 }
    val lastMilestoneShown: Flow<Int> = dataStore.data.map { it[LAST_MILESTONE_SHOWN] ?: 0 }
    val reminderEnabled: Flow<Boolean> = dataStore.data.map { it[REMINDER_ENABLED] ?: false }
    val reminderHour: Flow<Int> = dataStore.data.map { it[REMINDER_HOUR] ?: 20 }
    /** Interaction sounds (tap / long-press / orb). Default on. */
    val sfxEnabled: Flow<Boolean> = dataStore.data.map { it[SFX_ENABLED] ?: true }
    /** Vibration / haptic feedback. Default on. */
    val hapticsEnabled: Flow<Boolean> = dataStore.data.map { it[HAPTICS_ENABLED] ?: true }
    /** In-app reduced motion (also collapses when system animator scale is 0). Default off. */
    val reducedMotion: Flow<Boolean> = dataStore.data.map { it[REDUCED_MOTION] ?: false }

    suspend fun setReminderEnabled(enabled: Boolean) {
        dataStore.edit { it[REMINDER_ENABLED] = enabled }
    }

    suspend fun setReminderHour(hour: Int) {
        dataStore.edit { it[REMINDER_HOUR] = hour }
    }

    suspend fun setSfxEnabled(enabled: Boolean) {
        dataStore.edit { it[SFX_ENABLED] = enabled }
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        dataStore.edit { it[HAPTICS_ENABLED] = enabled }
    }

    suspend fun setReducedMotion(enabled: Boolean) {
        dataStore.edit { it[REDUCED_MOTION] = enabled }
    }

    val themeId: Flow<ThemeId> = dataStore.data.map {
        ThemeId.fromStorage(it[THEME_ID])
    }
    val fontScaleStep: Flow<FontScaleStep> = dataStore.data.map {
        FontScaleStep.fromStorage(it[FONT_SCALE_STEP])
    }

    /** null means budget is off (missing or ≤ 0). */
    val dailyBudgetCents: Flow<Long?> = dataStore.data.map { prefs ->
        val v = prefs[DAILY_BUDGET_CENTS] ?: return@map null
        if (v <= 0L) null else v
    }

    suspend fun setDailyBudgetCents(cents: Long) {
        require(cents > 0L) { "daily budget must be positive cents" }
        dataStore.edit { it[DAILY_BUDGET_CENTS] = cents }
    }

    suspend fun clearDailyBudget() {
        dataStore.edit { it.remove(DAILY_BUDGET_CENTS) }
    }

    suspend fun setThemeId(id: ThemeId) {
        dataStore.edit { it[THEME_ID] = id.storageKey }
    }

    suspend fun setFontScaleStep(step: FontScaleStep) {
        dataStore.edit { it[FONT_SCALE_STEP] = step.storageKey }
    }

    /** Locally persisted entitlement snapshot. Absent entry resolves to FREE. */
    override val entitlement: Flow<Entitlement> = dataStore.data.map { prefs ->
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

    override val entitlementSyncedAtMillis: Flow<Long> =
        dataStore.data.map { it[ENTITLEMENT_SYNCED_AT] ?: 0L }

    override suspend fun setEntitlement(entitlement: Entitlement) {
        val snapshot = EntitlementSnapshot.fromEntitlement(entitlement)
        dataStore.edit {
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

    override suspend fun markEntitlementSynced(atMillis: Long) {
        dataStore.edit { it[ENTITLEMENT_SYNCED_AT] = atMillis }
    }

    override suspend fun clearEntitlement() {
        dataStore.edit { prefs -> ENTITLEMENT_KEYS.forEach { prefs.remove(it) } }
    }

    // --- Auth session (Google / Supabase) ------------------------------------

    override val session: Flow<AuthSession?> = dataStore.data.map { prefs ->
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
        dataStore.edit {
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
        dataStore.edit { prefs -> AUTH_KEYS.forEach { prefs.remove(it) } }
    }

    // --- PayPal checkout return (durable across process death) --------------

    /**
     * True while a PayPal checkout return is pending but not yet confirmed.
     * A flag older than the 24h TTL is treated as stale (webhook never landed,
     * return missed) and reports inactive, so a cold start never retries forever.
     */
    override val paypalReturnPending: Flow<Boolean> = dataStore.data.map {
        paypalReturnPendingActive(it[PAYPAL_RETURN_PENDING_AT] ?: 0L, System.currentTimeMillis())
    }

    override suspend fun setPaypalReturnPending(pending: Boolean) {
        dataStore.edit {
            it[PAYPAL_RETURN_PENDING_AT] = if (pending) System.currentTimeMillis() else 0L
        }
    }

    override suspend fun clearPaypalReturnPending() {
        dataStore.edit { it[PAYPAL_RETURN_PENDING_AT] = 0L }
    }

    suspend fun setCurrency(symbol: String, code: String) {
        dataStore.edit {
            it[CURRENCY_SYMBOL] = symbol
            it[CURRENCY_CODE] = code
        }
    }

    suspend fun setFirstLaunchComplete() {
        dataStore.edit { it[FIRST_LAUNCH] = false }
    }

    /**
     * Spending-goal selection from the progressive onboarding (design audit #9).
     * Values: "track" (neutral default), "budget", "analyze". Stored for future
     * personalization hooks; cleared on wipe like every other key.
     */
    val spendingGoal: Flow<String> = dataStore.data.map { it[SPENDING_GOAL] ?: "track" }

    suspend fun setSpendingGoal(goal: String) {
        dataStore.edit { it[SPENDING_GOAL] = goal }
    }

    /**
     * One-shot nudge (design audit #9 follow-up): when onboarding's "Stay under a
     * daily budget" goal is chosen, this flag makes the Log screen pre-open its
     * set-budget form on the next visit. Consumed (cleared) by InputViewModel.
     */
    val budgetNudgePending: Flow<Boolean> = dataStore.data.map { it[BUDGET_NUDGE_PENDING] ?: false }

    suspend fun setBudgetNudgePending(pending: Boolean) {
        dataStore.edit { it[BUDGET_NUDGE_PENDING] = pending }
    }

    // --- Crash reporting (Sentry opt-out toggle; default ON, release-only) ---

    /** "Send crash reports" toggle in Settings. Default on; fully anonymous. */
    val crashReportsEnabled: Flow<Boolean> = dataStore.data.map { it[CRASH_REPORTS_ENABLED] ?: true }

    suspend fun setCrashReportsEnabled(enabled: Boolean) {
        dataStore.edit { it[CRASH_REPORTS_ENABLED] = enabled }
    }

    // --- Local backup (SAF folder + auto-backup worker) ----------------------

    /** Persisted SAF tree URI of the user-chosen backup folder; null = not set. */
    val backupFolderUri: Flow<String?> = dataStore.data.map {
        it[BACKUP_FOLDER_URI]?.takeIf { uri -> uri.isNotBlank() }
    }

    suspend fun setBackupFolderUri(uri: String?) {
        dataStore.edit {
            if (uri.isNullOrBlank()) it.remove(BACKUP_FOLDER_URI) else it[BACKUP_FOLDER_URI] = uri
        }
    }

    /** Daily auto-backup toggle (requires a backup folder). Default off. */
    val autoBackupEnabled: Flow<Boolean> = dataStore.data.map { it[AUTO_BACKUP_ENABLED] ?: false }

    suspend fun setAutoBackupEnabled(enabled: Boolean) {
        dataStore.edit { it[AUTO_BACKUP_ENABLED] = enabled }
    }

    /** Epoch millis of the last successful backup write (0 = never). */
    val lastBackupAt: Flow<Long> = dataStore.data.map { it[LAST_BACKUP_AT] ?: 0L }

    suspend fun setLastBackupAt(atMillis: Long) {
        dataStore.edit { it[LAST_BACKUP_AT] = atMillis }
    }

    // --- In-app update check (site version.json) -----------------------------

    /** Epoch millis of the last update-check attempt (throttling). */
    val lastUpdateCheckAt: Flow<Long> = dataStore.data.map { it[LAST_UPDATE_CHECK_AT] ?: 0L }

    suspend fun setLastUpdateCheckAt(atMillis: Long) {
        dataStore.edit { it[LAST_UPDATE_CHECK_AT] = atMillis }
    }

    /** Latest known newer release, or null when up to date / never checked. */
    val updateAvailable: Flow<AvailableUpdate?> = dataStore.data.map { prefs ->
        val name = prefs[UPDATE_AVAILABLE_NAME]?.takeIf { it.isNotBlank() } ?: return@map null
        AvailableUpdate(
            versionName = name,
            versionCode = prefs[UPDATE_AVAILABLE_CODE] ?: 0,
            apkUrl = prefs[UPDATE_AVAILABLE_URL] ?: ""
        )
    }

    suspend fun setUpdateAvailable(update: AvailableUpdate?) {
        dataStore.edit {
            if (update == null) {
                it.remove(UPDATE_AVAILABLE_NAME)
                it.remove(UPDATE_AVAILABLE_CODE)
                it.remove(UPDATE_AVAILABLE_URL)
            } else {
                it[UPDATE_AVAILABLE_NAME] = update.versionName
                it[UPDATE_AVAILABLE_CODE] = update.versionCode
                it[UPDATE_AVAILABLE_URL] = update.apkUrl
            }
        }
    }

    suspend fun updateBestStreak(streak: Int) {
        dataStore.edit { prefs ->
            val currentBest = prefs[BEST_STREAK_EVER] ?: 0
            if (streak > currentBest) {
                prefs[BEST_STREAK_EVER] = streak
            }
        }
    }

    suspend fun setLastMilestoneShown(milestoneDays: Int) {
        dataStore.edit { it[LAST_MILESTONE_SHOWN] = milestoneDays }
    }

    val quotaState: Flow<QuotaState> = dataStore.data.map { prefs ->
        QuotaState(
            day = prefs[QUOTA_DAY] ?: "",
            logsCreated = prefs[QUOTA_LOGS_CREATED] ?: 0,
            carriedLogs = prefs[QUOTA_CARRIED_LOGS] ?: 0
        )
    }

    suspend fun setQuotaState(state: QuotaState) {
        dataStore.edit {
            it[QUOTA_DAY] = state.day
            it[QUOTA_LOGS_CREATED] = state.logsCreated
            it[QUOTA_CARRIED_LOGS] = state.carriedLogs
        }
    }

    suspend fun resetQuotaForDay(today: String) {
        setQuotaState(QuotaState(day = today, logsCreated = 0, carriedLogs = 0))
    }

    suspend fun wipeAll() {
        dataStore.edit { it.clear() }
    }
}
