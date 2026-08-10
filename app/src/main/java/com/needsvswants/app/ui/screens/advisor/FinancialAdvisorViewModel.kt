package com.needsvswants.app.ui.screens.advisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.repository.EntryRepository
import com.needsvswants.app.domain.AdvisorChatSession
import com.needsvswants.app.domain.AdvisorContextPack
import com.needsvswants.app.domain.AdvisorInsight
import com.needsvswants.app.domain.ChatMessage
import com.needsvswants.app.domain.Entitlement
import com.needsvswants.app.domain.FinancialAdvisorEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdvisorUiState(
    val insight: AdvisorInsight? = null,
    val chatMessages: List<ChatMessage> = emptyList(),
    val currencySymbol: String = "₱",
    val isLoading: Boolean = false,
    val hasMaxAccess: Boolean = false,
    val sourceOfTruthTitle: String = FinancialAdvisorEngine.SOURCE_OF_TRUTH_TITLE,
    val notebookUrl: String = FinancialAdvisorEngine.DEFAULT_NOTEBOOK_URL
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FinancialAdvisorViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val preferences: AppPreferences
) : ViewModel() {

    private data class AdvisorInputs(
        val entries: List<Entry>,
        val currencySymbol: String,
        val dailyBudgetCents: Long?,
        val entitlement: Entitlement,
        val messages: List<ChatMessage>
    )

    private val chatSession = AdvisorChatSession()
    private val _chatMessages = MutableStateFlow(chatSession.snapshot())

    val uiState: StateFlow<AdvisorUiState> = combine(
        combine(
            entryRepository.observeAll(),
            preferences.currencySymbol,
            preferences.dailyBudgetCents,
            preferences.entitlement,
            _chatMessages
        ) { entries, currencySymbol, dailyBudgetCents, entitlement, messages ->
            AdvisorInputs(entries, currencySymbol, dailyBudgetCents, entitlement, messages)
        },
        preferences.spendingGoal
    ) { inputs, spendingGoal ->
        val now = System.currentTimeMillis()
        val hasMax = inputs.entitlement.hasMaxAccessAt(now)
        val insight = FinancialAdvisorEngine.generateInsight(
            AdvisorContextPack.build(
                entries = inputs.entries,
                dailyBudgetCents = inputs.dailyBudgetCents,
                spendingGoal = spendingGoal,
                nowEpochMs = now
            )
        )
        AdvisorUiState(
            insight = insight,
            chatMessages = inputs.messages,
            currencySymbol = inputs.currencySymbol,
            hasMaxAccess = hasMax,
            isLoading = false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AdvisorUiState(isLoading = true)
    )

    fun sendUserQuery(queryText: String) {
        if (queryText.isBlank()) return
        viewModelScope.launch {
            val entitlement = preferences.entitlement.first()
            if (!entitlement.hasMaxAccessAt(System.currentTimeMillis())) {
                return@launch
            }
            val entries = entryRepository.observeAll().first()
            val currency = preferences.currencySymbol.first()
            val dailyBudget = preferences.dailyBudgetCents.first()
            val spendingGoal = preferences.spendingGoal.first()
            val updated = chatSession.sendUserQuery(
                queryText = queryText,
                entries = entries,
                currencySymbol = currency,
                dailyBudgetCents = dailyBudget,
                spendingGoal = spendingGoal
            )
            if (updated != null) {
                _chatMessages.value = updated
            }
        }
    }
}
