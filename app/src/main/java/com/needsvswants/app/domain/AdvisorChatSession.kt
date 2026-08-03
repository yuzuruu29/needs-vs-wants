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
     * @return the new message list, or null if the query was blank.
     */
    fun sendUserQuery(
        queryText: String,
        entries: List<Entry>,
        currencySymbol: String,
        dailyBudgetCents: Long?
    ): List<ChatMessage>? {
        if (queryText.isBlank()) return null
        messages.add(
            ChatMessage(
                id = "user_${System.currentTimeMillis()}",
                sender = ChatSender.USER,
                text = queryText
            )
        )
        messages.add(
            FinancialAdvisorEngine.evaluateConversationalQuery(
                query = queryText,
                entries = entries,
                currencySymbol = currencySymbol,
                dailyBudgetCents = dailyBudgetCents
            )
        )
        return snapshot()
    }
}
