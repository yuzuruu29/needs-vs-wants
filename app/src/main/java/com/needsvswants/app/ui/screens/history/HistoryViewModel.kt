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

    /** True until the first real entries emission lands (cold-start skeleton, design audit #2). */
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            entries.drop(1).first()
            _loading.value = false
        }
    }

    val isPro: StateFlow<Boolean> = entitlementRepository.isPro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun deleteEntry(entry: Entry): Entry {
        viewModelScope.launch {
            entryRepository.delete(entry)
            runCatching { NvwWidget.refreshAll(appContext) }
        }
        // Return the caller's copy so the UI can offer an undo that restores it by id.
        return entry
    }

    /** Undo a delete: re-inserts the entry, preserving its original id. */
    fun restoreEntry(entry: Entry) {
        viewModelScope.launch {
            entryRepository.restore(entry)
            runCatching { NvwWidget.refreshAll(appContext) }
        }
    }

    /** Persist edits to a sealed entry (same id, new fields). */
    fun updateEntry(entry: Entry) {
        viewModelScope.launch {
            entryRepository.update(entry)
            runCatching { NvwWidget.refreshAll(appContext) }
        }
    }

    /** Import parsed entries as fresh rows. Returns the count queued for insertion. */
    fun importEntries(entries: List<Entry>): Int {
        if (entries.isEmpty()) return 0
        viewModelScope.launch {
            for (entry in entries) {
                entryRepository.insert(entry)
            }
            runCatching { NvwWidget.refreshAll(appContext) }
        }
        return entries.size
    }

    fun exportCsvText(): String {
        return com.needsvswants.app.domain.ExportUseCase.exportCsv(entries.value)
    }
}
