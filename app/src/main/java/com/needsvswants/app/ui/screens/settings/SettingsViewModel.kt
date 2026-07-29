package com.needsvswants.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.prefs.AppPreferences
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

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: AppPreferences,
    private val dao: EntryDao
) : ViewModel() {
    val currentSymbol: StateFlow<String> = preferences.currencySymbol
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₱")
    val currentCode: StateFlow<String> = preferences.currencyCode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "PHP")

    fun setCurrency(symbol: String, code: String) {
        viewModelScope.launch { preferences.setCurrency(symbol, code) }
    }

    fun wipeData() {
        viewModelScope.launch {
            dao.deleteAll()
            preferences.wipeAll()
        }
    }
}
