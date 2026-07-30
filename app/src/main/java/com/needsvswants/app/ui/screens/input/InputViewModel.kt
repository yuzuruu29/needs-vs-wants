package com.needsvswants.app.ui.screens.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.db.EntryDao
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.domain.parseCents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InputViewModel @Inject constructor(
    private val dao: EntryDao,
    private val preferences: AppPreferences
) : ViewModel() {
    val sheetEntries: StateFlow<List<Entry>> = dao.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currencySymbol: StateFlow<String> = preferences.currencySymbol
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₱")

    val isSheetFull: Boolean get() = sheetEntries.value.size >= 20

    var activeItem = MutableStateFlow("")
    var activeCost = MutableStateFlow("")
    var activeType = MutableStateFlow<EntryType?>(null)
    private var isSealing = false

    fun filterItem(input: String): String =
        input.filter { it.isLetterOrDigit() || it.isWhitespace() || it == '-' || it == '.' || it == '\'' || it == ',' }

    fun filterCost(input: String): String {
        val cleaned = input.filter { it.isDigit() || it == '.' }
        val parts = cleaned.split(".")
        return if (parts.size <= 1) cleaned else parts[0] + "." + parts[1].take(2)
    }

    fun trySeal() {
        if (isSealing) return
        val item = activeItem.value.trim()
        val costCents = parseCents(activeCost.value)
        val type = activeType.value
        if (item.isNotEmpty() && costCents != null && type != null && !isSheetFull) {
            isSealing = true
            val now = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            viewModelScope.launch {
                dao.insert(Entry(
                    dateUtc = now,
                    date = dateFormat.format(Date(now)),
                    time = timeFormat.format(Date(now)),
                    item = item,
                    costCents = costCents,
                    type = type
                ))
                activeItem.value = ""
                activeCost.value = ""
                activeType.value = null
                isSealing = false
            }
        }
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch { dao.delete(entry) }
    }

    /** D4: wipe the 20-row sheet so logging can continue. Matches iOS `startNewSheet()`. */
    fun startNewSheet() {
        viewModelScope.launch { dao.deleteAll() }
    }
}
