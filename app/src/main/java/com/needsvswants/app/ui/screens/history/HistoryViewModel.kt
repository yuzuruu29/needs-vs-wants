package com.needsvswants.app.ui.screens.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.entitlement.EntitlementRepository
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.Period
import com.needsvswants.app.domain.PeriodWindow
import com.needsvswants.app.widget.NvwWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Pure history filter (audit gap: no search/filter): case-insensitive item
 * match, or a date match against the entry's `date` string; optional
 * Need/Want narrowing. Blank query + null type returns the list unchanged.
 */
fun filterHistoryEntries(entries: List<Entry>, query: String, type: EntryType?): List<Entry> {
    val q = query.trim()
    if (q.isEmpty() && type == null) return entries
    return entries.filter { e ->
        (type == null || e.type == type) &&
            (q.isEmpty() || e.item.contains(q, ignoreCase = true) || e.date.contains(q))
    }
}

/**
 * Pure period window filter (Day / Week / Month / All), same cutoffs as the
 * Summary selector via [PeriodWindow]. ALL passes through unchanged — the
 * Free 30-day retention clamp already happened upstream in [entries].
 */
fun withinPeriod(entries: List<Entry>, period: Period, nowMs: Long): List<Entry> {
    val since = PeriodWindow.sinceEpochMs(period, nowMs)
    if (since <= 0L) return entries
    return entries.filter { it.dateUtc >= since }
}

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

    // --- Search + type filter (audit gap) ------------------------------------

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _typeFilter = MutableStateFlow<EntryType?>(null)
    val typeFilter: StateFlow<EntryType?> = _typeFilter.asStateFlow()

    private val _periodFilter = MutableStateFlow(Period.ALL)
    val periodFilter: StateFlow<Period> = _periodFilter.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setTypeFilter(type: EntryType?) {
        _typeFilter.value = type
    }

    fun setPeriodFilter(period: Period) {
        _periodFilter.value = period
    }

    /** What the ledger list actually renders; export/date-range still use [entries]. */
    val filteredEntries: StateFlow<List<Entry>> = combine(
        entries, _searchQuery, _typeFilter, _periodFilter
    ) { list, query, type, period ->
        filterHistoryEntries(
            withinPeriod(list, period, System.currentTimeMillis()),
            query,
            type
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
