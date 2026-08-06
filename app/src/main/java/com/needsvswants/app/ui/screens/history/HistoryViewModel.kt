package com.needsvswants.app.ui.screens.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.widget.NvwWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val preferences: AppPreferences,
    private val entitlementRepository: EntitlementRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    val entitlement: StateFlow<com.needsvswants.app.domain.Entitlement> = entitlementRepository.entitlement
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), com.needsvswants.app.domain.Entitlement.Free)

    val entries: StateFlow<List<Entry>> = combine(
        entryRepository.observeAll(),
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
        viewModelScope.launch {
            entryRepository.delete(entry)
            runCatching { NvwWidget.refreshAll(appContext) }
        }
    }

    fun exportCsvText(): String {
        return com.needsvswants.app.domain.ExportUseCase.exportCsv(entries.value)
    }
}
