package com.needsvswants.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.domain.FontScaleStep
import com.needsvswants.app.domain.ThemeId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: AppPreferences,
    private val dao: EntryDao
) : ViewModel() {
    val currentSymbol: StateFlow<String> = preferences.currencySymbol
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₱")
    val currentCode: StateFlow<String> = preferences.currencyCode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "PHP")

    val themeId: StateFlow<ThemeId> = preferences.themeId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeId.MARKET_LIGHT)

    val fontScaleStep: StateFlow<FontScaleStep> = preferences.fontScaleStep
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FontScaleStep.DEFAULT)

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
            dao.deleteAll()
            preferences.wipeAll()
        }
    }
}
