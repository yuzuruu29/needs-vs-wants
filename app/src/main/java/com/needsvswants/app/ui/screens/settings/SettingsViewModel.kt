package com.needsvswants.app.ui.screens.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.backup.BackupScheduler
import com.needsvswants.app.data.backup.BackupService
import com.needsvswants.app.data.billing.BillingController
import com.needsvswants.app.data.billing.BillingResult
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.prefs.AvailableUpdate
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.data.update.UpdateChecker
import com.needsvswants.app.diagnostics.CrashReporting
import com.needsvswants.app.domain.DailyLogQuota
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.AdsConfig
import com.needsvswants.app.domain.FontScaleStep
import com.needsvswants.app.domain.QuotaState
import com.needsvswants.app.domain.ThemeId
import com.needsvswants.app.widget.NvwWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class CurrencyOption(val symbol: String, val code: String, val label: String)

val currencies = listOf(
    CurrencyOption("₱", "PHP", "PHP"),
    CurrencyOption("$", "USD", "USD"),
    CurrencyOption("€", "EUR", "EUR"),
    CurrencyOption("¥", "JPY", "JPY"),
    CurrencyOption("S$", "SGD", "SGD")
)

data class FontScaleOption(val step: FontScaleStep, val label: String)

val fontScaleOptions = listOf(
    FontScaleOption(FontScaleStep.DEFAULT, "Default"),
    FontScaleOption(FontScaleStep.LARGE, "Large"),
    FontScaleOption(FontScaleStep.EXTRA_LARGE, "Extra large")
)

data class ThemeOption(val id: ThemeId, val label: String)

val themeOptions = listOf(
    ThemeOption(ThemeId.MARKET_LIGHT, "Market light"),
    ThemeOption(ThemeId.MARKET_DARK, "Market dark"),
    ThemeOption(ThemeId.SYSTEM, "System"),
    ThemeOption(ThemeId.HIGH_CONTRAST, "High contrast")
)

/** Read-only daily free quota snapshot for the Settings panel. Null = quota does not apply (Pro/Max). */
data class DailyFreeLogsInfo(
    val allowancePerDay: Int,
    val remainingToday: Int,
    val carriedLogs: Int,
    val bonusLogs: Int,
    val adsWatched: Int
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: AppPreferences,
    private val entryRepository: EntryRepository,
    private val billingController: BillingController,
    private val backupService: BackupService,
    private val updateChecker: UpdateChecker,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    val currentSymbol: StateFlow<String> = preferences.currencySymbol
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₱")
    val currentCode: StateFlow<String> = preferences.currencyCode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "PHP")

    val themeId: StateFlow<ThemeId> = preferences.themeId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeId.MARKET_LIGHT)

    val fontScaleStep: StateFlow<FontScaleStep> = preferences.fontScaleStep
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FontScaleStep.DEFAULT)

    val reminderEnabled: StateFlow<Boolean> = preferences.reminderEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val reminderHour: StateFlow<Int> = preferences.reminderHour
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 20)

    val sfxEnabled: StateFlow<Boolean> = preferences.sfxEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val hapticsEnabled: StateFlow<Boolean> = preferences.hapticsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val reducedMotion: StateFlow<Boolean> = preferences.reducedMotion
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    /** Membership snapshot for the Settings panel (plan + expiry). */
    val membership: StateFlow<Entitlement> = preferences.entitlement
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Entitlement.Free)

    /** Onboarding spending goal (track / budget / analyze). */
    val spendingGoal: StateFlow<String> = preferences.spendingGoal
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "track")

    fun setSpendingGoal(goal: String) {
        viewModelScope.launch { preferences.setSpendingGoal(goal) }
    }

    private val _refreshBusy = MutableStateFlow(false)
    val refreshBusy: StateFlow<Boolean> = _refreshBusy.asStateFlow()

    private val _refreshFeedback = MutableStateFlow<String?>(null)
    val refreshFeedback: StateFlow<String?> = _refreshFeedback.asStateFlow()

    /**
     * Best-effort "Refresh membership": re-pulls entitlement from the remote
     * via the billing restore path (webhook may have granted Pro/Max while the
     * local snapshot stayed FREE). Surfaces a lightweight status for the panel.
     */
    fun refreshMembership() {
        viewModelScope.launch {
            if (_refreshBusy.value) return@launch
            _refreshBusy.value = true
            _refreshFeedback.value = null
            try {
                when (val result = billingController.restorePurchases()) {
                    BillingResult.Success -> _refreshFeedback.value =
                        // Read the FRESH snapshot (the suspend DataStore read
                        // returns the just-written value); the stateIn-cached
                        // `membership` can lag the write and briefly report
                        // "No active membership found." right after a grant.
                        if (preferences.entitlement.first().hasProAccessAt(System.currentTimeMillis()))
                            "Membership refreshed."
                        else
                            "No active membership found."
                    BillingResult.Unavailable -> _refreshFeedback.value = "Membership sync is not configured."
                    is BillingResult.Failed -> _refreshFeedback.value =
                        result.reason ?: "Could not refresh membership."
                    else -> _refreshFeedback.value = null
                }
            } finally {
                _refreshBusy.value = false
            }
        }
    }

    /**
     * Read-only daily free logs state for Settings.
     * Null for Pro/Max (quota does not apply). Uses the day-rolled quota flow —
     * never the raw prefs read (see Phase 1 plan risk note).
     */
    val dailyFreeLogs: StateFlow<DailyFreeLogsInfo?> = combine(
        membership,
        preferences.quotaState.map { DailyLogQuota.rollDayIfNeeded(it, todayString()) }
    ) { ent, quota ->
        if (ent.hasProAccessAt(System.currentTimeMillis())) {
            null
        } else {
            DailyFreeLogsInfo(
                allowancePerDay = AdsConfig.FREE_DAILY_LOGS,
                remainingToday = DailyLogQuota.remaining(quota, todayString()),
                carriedLogs = quota.carriedLogs,
                bonusLogs = quota.bonusLogs,
                adsWatched = quota.adsWatched
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private fun todayString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun setReminderEnabled(enabled: Boolean, context: android.content.Context) {
        viewModelScope.launch {
            preferences.setReminderEnabled(enabled)
            if (enabled) {
                // Use the stored hour — it was hardcoded to 20 before (audit gap).
                val hour = preferences.reminderHour.first()
                com.needsvswants.app.notification.ReminderScheduler.schedule(context, hour)
            } else {
                com.needsvswants.app.notification.ReminderScheduler.cancel(context)
            }
        }
    }

    fun setReminderHour(hour: Int, context: android.content.Context) {
        viewModelScope.launch {
            preferences.setReminderHour(hour.coerceIn(0, 23))
            if (preferences.reminderEnabled.first()) {
                com.needsvswants.app.notification.ReminderScheduler.schedule(context, hour.coerceIn(0, 23))
            }
        }
    }

    // --- Privacy: crash reporting toggle -------------------------------------

    val crashReportsEnabled: StateFlow<Boolean> = preferences.crashReportsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setCrashReportsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setCrashReportsEnabled(enabled)
            CrashReporting.applyState(appContext, enabled)
        }
    }

    // --- Local backup / restore ----------------------------------------------

    val backupFolderUri: StateFlow<String?> = preferences.backupFolderUri
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val autoBackupEnabled: StateFlow<Boolean> = preferences.autoBackupEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val lastBackupAt: StateFlow<Long> = preferences.lastBackupAt
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    private val _backupBusy = MutableStateFlow(false)
    val backupBusy: StateFlow<Boolean> = _backupBusy.asStateFlow()

    private val _backupFeedback = MutableStateFlow<String?>(null)
    val backupFeedback: StateFlow<String?> = _backupFeedback.asStateFlow()

    fun setBackupFolder(uri: String?) {
        viewModelScope.launch {
            preferences.setBackupFolderUri(uri)
            _backupFeedback.value = null
            if (uri == null) {
                preferences.setAutoBackupEnabled(false)
                BackupScheduler.cancel(appContext)
            }
        }
    }

    fun setAutoBackupEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setAutoBackupEnabled(enabled)
            if (enabled) BackupScheduler.schedule(appContext) else BackupScheduler.cancel(appContext)
        }
    }

    fun backupNow() {
        viewModelScope.launch {
            if (_backupBusy.value) return@launch
            _backupBusy.value = true
            _backupFeedback.value = null
            _backupFeedback.value = when (val r = backupService.backupNow()) {
                is BackupService.BackupResult.Success ->
                    "Backed up ${r.entryCount} entries to ${r.fileName}."
                BackupService.BackupResult.NoFolder -> "Choose a backup folder first."
                is BackupService.BackupResult.Failed -> r.reason
            }
            _backupBusy.value = false
        }
    }

    fun restoreFrom(uri: Uri) {
        viewModelScope.launch {
            if (_backupBusy.value) return@launch
            _backupBusy.value = true
            _backupFeedback.value = null
            _backupFeedback.value = when (val r = backupService.restoreFrom(uri)) {
                is BackupService.RestoreResult.Success ->
                    "Restored ${r.imported} entries (${r.duplicatesSkipped} duplicates skipped)."
                is BackupService.RestoreResult.Failed -> r.reason
            }
            runCatching { NvwWidget.refreshAll(appContext) }
            _backupBusy.value = false
        }
    }

    // --- Sideload update check -------------------------------------------------

    val updateAvailable: StateFlow<AvailableUpdate?> = preferences.updateAvailable
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _updateCheckBusy = MutableStateFlow(false)
    val updateCheckBusy: StateFlow<Boolean> = _updateCheckBusy.asStateFlow()

    private val _updateFeedback = MutableStateFlow<String?>(null)
    val updateFeedback: StateFlow<String?> = _updateFeedback.asStateFlow()

    fun checkForUpdates() {
        viewModelScope.launch {
            if (_updateCheckBusy.value) return@launch
            _updateCheckBusy.value = true
            _updateFeedback.value = null
            runCatching { updateChecker.checkOnce(force = true) }
            _updateFeedback.value = if (preferences.updateAvailable.first() != null) {
                null // the "Update available" row itself is the feedback
            } else {
                "You're on the latest version."
            }
            _updateCheckBusy.value = false
        }
    }

    fun setSfxEnabled(enabled: Boolean) {
        viewModelScope.launch { preferences.setSfxEnabled(enabled) }
    }

    fun setHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch { preferences.setHapticsEnabled(enabled) }
    }

    fun setReducedMotion(enabled: Boolean) {
        viewModelScope.launch { preferences.setReducedMotion(enabled) }
    }

    fun setCurrency(symbol: String, code: String) {
        viewModelScope.launch { preferences.setCurrency(symbol, code) }
    }

    fun setThemeId(id: ThemeId) {
        viewModelScope.launch { preferences.setThemeId(id) }
    }

    fun setFontScaleStep(step: FontScaleStep) {
        viewModelScope.launch { preferences.setFontScaleStep(step) }
    }

    fun wipeData() {
        viewModelScope.launch {
            entryRepository.deleteAll()
            preferences.wipeAll()
            runCatching { NvwWidget.refreshAll(appContext) }
        }
    }
}
