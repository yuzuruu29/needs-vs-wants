package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry

/**
 * Pure chat session for the Financial Advisor. No Android types — unit-testable.
 * [FinancialAdvisorViewModel] owns an instance and maps it into UI state.
 */
class AdvisorChatSession(
    initialMessages: List<ChatMessage> = listOf(
        ChatMessage(
            id = "init_welcome",
            sender = ChatSender.ADVISOR,
            text = "Hello! I am your Financial Advisor, grounded in your Google NotebookLM economic studies. Ask me anything about your budget, spending ratios, or overspend recovery!"
        )
    )
) {
    private val messages = initialMessages.toMutableList()

    fun snapshot(): List<ChatMessage> = messages.toList()

    /**
     * Appends the user query and the engine response. No-op for blank input.
     * The engine receives a pure [AdvisorContextPack] built from the ledger,
     * the optional daily budget, and the user's spending goal.
     * [currencySymbol] is retained for API compatibility; the context pack
     * carries all numeric context the engine reasons over.
     * @return the new message list, or null if the query was blank.
     */
    @Suppress("UNUSED_PARAMETER") // currencySymbol retained for call-site compatibility
    fun sendUserQuery(
        queryText: String,
        entries: List<Entry>,
        currencySymbol: String,
        dailyBudgetCents: Long?,
        spendingGoal: String = AdvisorContextPack.DEFAULT_SPENDING_GOAL
    ): List<ChatMessage>? {
        if (queryText.isBlank()) return null
        messages.add(
            ChatMessage(
                id = "user_${System.currentTimeMillis()}",
                sender = ChatSender.USER,
                text = queryText
            )
        )
        val context = AdvisorContextPack.build(
            entries = entries,
            dailyBudgetCents = dailyBudgetCents,
            spendingGoal = spendingGoal
        )
        messages.add(
            FinancialAdvisorEngine.evaluateConversationalQuery(
                query = queryText,
                context = context
            )
        )
        return snapshot()
    }
}
