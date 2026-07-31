package com.needsvswants.app.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppPreferences(private val context: Context) {
    companion object {
        private val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")
        private val CURRENCY_CODE = stringPreferencesKey("currency_code")
        private val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        private val DAILY_BUDGET_CENTS = longPreferencesKey("daily_budget_cents")
    }

    val currencySymbol: Flow<String> = context.dataStore.data.map { it[CURRENCY_SYMBOL] ?: "₱" }
    val currencyCode: Flow<String> = context.dataStore.data.map { it[CURRENCY_CODE] ?: "PHP" }
    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { it[FIRST_LAUNCH] ?: true }

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
