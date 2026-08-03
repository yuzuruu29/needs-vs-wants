package com.needsvswants.app.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.prefs.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val dao: EntryDao,
    private val preferences: AppPreferences,
    private val entitlementRepository: EntitlementRepository
) : ViewModel() {
    val entitlement: StateFlow<com.needsvswants.app.domain.Entitlement> = entitlementRepository.entitlement
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), com.needsvswants.app.domain.Entitlement.Free)

    val entries: StateFlow<List<Entry>> = combine(
        dao.observeAll(),
        entitlement
    ) { allEntries, ent ->
        val now = System.currentTimeMillis()
        val cutoff = ent.retentionCutoffAt(now)
        if (cutoff == null) {
            allEntries
        } else {
            allEntries.filter { it.dateUtc >= cutoff }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currencySymbol: StateFlow<String> = preferences.currencySymbol
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₱")

    val isPro: StateFlow<Boolean> = entitlementRepository.isPro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch { dao.delete(entry) }
    }
}
