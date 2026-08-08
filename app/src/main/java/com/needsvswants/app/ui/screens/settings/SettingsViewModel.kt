package com.needsvswants.app.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.billing.BillingController
import com.needsvswants.app.data.billing.BillingResult
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.AdsConfig
import com.needsvswants.app.domain.DailyLogQuota
import com.needsvswants.app.domain.Entitlement
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

/** Read-only daily free quota snapshot for the Settings panel. Null = quota does not apply (Pro/Max or kill switch). */
data class DailyFreeLogsInfo(
    val allowancePerDay: Int,
    val remainingToday: Int,
    val bonusLogsToday: Int,
    val extraLogsPerReward: Int,
    val adsWatchedToday: Int,
    val maxAdsPerDay: Int
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: AppPreferences,
    private val entryRepository: EntryRepository,
    private val billingController: BillingController,
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

    val sfxEnabled: StateFlow<Boolean> = preferences.sfxEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val hapticsEnabled: StateFlow<Boolean> = preferences.hapticsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val reducedMotion: StateFlow<Boolean> = preferences.reducedMotion
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    /** Membership snapshot for the Settings panel (plan + expiry). */
    val membership: StateFlow<Entitlement> = preferences.entitlement
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Entitlement.Free)

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
                        if (membership.value.hasProAccessAt(System.currentTimeMillis()))
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
     * Null for Pro/Max (quota does not apply) or when the ads kill switch is off.
     * Uses the day-rolled quota flow — never the raw prefs read (see Phase 1 plan risk note).
     */
    val dailyFreeLogs: StateFlow<DailyFreeLogsInfo?> = combine(
        membership,
        preferences.quotaState.map { DailyLogQuota.rollDayIfNeeded(it, todayString()) }
    ) { ent, quota ->
        if (!AdsConfig.ENABLED || ent.hasProAccessAt(System.currentTimeMillis())) {
            null
        } else {
            DailyFreeLogsInfo(
                allowancePerDay = AdsConfig.FREE_DAILY_LOGS,
                remainingToday = DailyLogQuota.remaining(quota, todayString()),
                bonusLogsToday = quota.bonusLogs,
                extraLogsPerReward = AdsConfig.EXTRA_LOGS_PER_REWARD,
                adsWatchedToday = quota.adsWatched,
                maxAdsPerDay = AdsConfig.MAX_REWARDED_ADS_PER_DAY
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private fun todayString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun setReminderEnabled(enabled: Boolean, context: android.content.Context) {
        viewModelScope.launch {
            preferences.setReminderEnabled(enabled)
            if (enabled) {
                com.needsvswants.app.notification.ReminderScheduler.schedule(context, 20)
            } else {
                com.needsvswants.app.notification.ReminderScheduler.cancel(context)
            }
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
