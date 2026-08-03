package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType

data class AdvisorCitation(
    val title: String,
    val section: String,
    val notebookUrl: String = "https://notebook.google.com/"
)

data class AdvisorInsight(
    val headline: String,
    val advice: String,
    val citation: AdvisorCitation,
    val isWarning: Boolean = false
)

data class ChatMessage(
    val id: String,
    val sender: ChatSender,
    val text: String,
    val citation: AdvisorCitation? = null,
    val isWarning: Boolean = false,
    val timestampMs: Long = System.currentTimeMillis()
)

enum class ChatSender {
    USER,
    ADVISOR
}

object FinancialAdvisorEngine {

    const val SOURCE_OF_TRUTH_TITLE = "Google NotebookLM — Economic Studies"
    const val DEFAULT_NOTEBOOK_URL = "https://notebook.google.com/"

    fun generateInsight(
        entries: List<Entry>,
        currencySymbol: String,
        dailyBudgetCents: Long? = null
    ): AdvisorInsight {
        val totalNeedCents = entries.filter { it.type == EntryType.NEED }.sumOf { it.costCents }
        val totalWantCents = entries.filter { it.type == EntryType.WANT }.sumOf { it.costCents }
        val totalSpentCents = totalNeedCents + totalWantCents

        // Rule 1: Daily budget overspend check (NotebookLM Section 4.5)
        if (dailyBudgetCents != null && dailyBudgetCents > 0 && totalSpentCents > dailyBudgetCents) {
            val overAmountCents = totalSpentCents - dailyBudgetCents
            val formattedOver = overAmountCents.toMoney(currencySymbol)
            return AdvisorInsight(
                headline = "Compensatory Budget Recovery Active",
                advice = "According to your Economic Study Notebook #3 (Impulse Recovery), you have exceeded today's budget limit by $formattedOver. Reduce discretionary Want spending by 33% over the next 3 days to restore liquidity equilibrium.",
                citation = AdvisorCitation(
                    title = "Notebook #3: Impulse Recovery",
                    section = "NotebookLM Section 4.5 — Compensatory Sinking Protocol"
                ),
                isWarning = true
            )
        }

        // Rule 2: Want vs Need Equilibrium Check (NotebookLM Section 1.2)
        if (totalWantCents > totalNeedCents && totalSpentCents > 0) {
            return AdvisorInsight(
                headline = "Discretionary Spending Exceeds Baseline Needs",
                advice = "Your Economic Study Notebook #1 (Budgetary Equilibrium) principles indicate that discretionary Wants currently surpass essential Needs. Introduce a 24-hour delay before logging additional Want items.",
                citation = AdvisorCitation(
                    title = "Notebook #1: Budgetary Equilibrium",
                    section = "NotebookLM Section 1.2 — Binary Classification Dynamics"
                ),
                isWarning = true
            )
        }

        // Rule 3: Balanced Baseline (NotebookLM Section 3.1)
        return AdvisorInsight(
            headline = "Spending Within Economic Study Targets",
            advice = "Based on your Google NotebookLM economic study notebooks, your spending velocity is balanced. Essential Needs form the anchor of your daily ledger.",
            citation = AdvisorCitation(
                title = "Notebook #2: Behavioral Friction",
                section = "NotebookLM Section 3.1 — Real-Time Behavioral Control"
            ),
            isWarning = false
        )
    }

    fun evaluateConversationalQuery(
        query: String,
        entries: List<Entry>,
        currencySymbol: String,
        dailyBudgetCents: Long? = null
    ): ChatMessage {
        val lower = query.lowercase()
        val totalNeedCents = entries.filter { it.type == EntryType.NEED }.sumOf { it.costCents }
        val totalWantCents = entries.filter { it.type == EntryType.WANT }.sumOf { it.costCents }
        val totalSpentCents = totalNeedCents + totalWantCents

        val (text, citation, isWarning) = when {
            lower.contains("over") || lower.contains("overspend") || lower.contains("exceed") || lower.contains("limit") || lower.contains("budget") -> {
                if (dailyBudgetCents != null && dailyBudgetCents > 0 && totalSpentCents > dailyBudgetCents) {
                    val over = (totalSpentCents - dailyBudgetCents).toMoney(currencySymbol)
                    Triple(
                        "You are currently over budget by $over today. Per Notebook #3 (Impulse Recovery), absorb this overspend by lowering your Want spending allowance over the next 3 days.",
                        AdvisorCitation("Notebook #3: Impulse Recovery", "NotebookLM Section 4.5 — Compensatory Sinking Protocol"),
                        true
                    )
                } else {
                    Triple(
                        "Your daily spending is within your budget. Keep maintaining real-time log friction for non-essential Want items.",
                        AdvisorCitation("Notebook #2: Behavioral Friction", "NotebookLM Section 3.1 — Micro-Transaction Friction"),
                        false
                    )
                }
            }

            lower.contains("buy") || lower.contains("afford") || lower.contains("want") -> {
                if (totalWantCents >= totalNeedCents && totalSpentCents > 0) {
                    Triple(
                        "Discretionary Want spend currently equals or exceeds essential Needs (${totalWantCents.toMoney(currencySymbol)} Wants vs ${totalNeedCents.toMoney(currencySymbol)} Needs). Notebook #1 advises waiting 24 hours before buying another Want.",
                        AdvisorCitation("Notebook #1: Budgetary Equilibrium", "NotebookLM Section 1.2 — Binary Classification"),
                        true
                    )
                } else {
                    Triple(
                        "Your Needs currently form the majority of your ledger (${totalNeedCents.toMoney(currencySymbol)} Needs). You have capacity for a deliberate Want if it stays within your budget.",
                        AdvisorCitation("Notebook #1: Budgetary Equilibrium", "NotebookLM Section 1.2 — Binary Classification"),
                        false
                    )
                }
            }

            else -> {
                Triple(
                    "Grounded in your Google NotebookLM economic studies: Always classify entries at point-of-sale to preserve behavioral friction. Needs should remain your core financial anchor.",
                    AdvisorCitation("Notebook #2: Behavioral Control", "NotebookLM Section 3.1 — Real-Time Control"),
                    false
                )
            }
        }

        return ChatMessage(
            id = "advisor_${System.currentTimeMillis()}",
            sender = ChatSender.ADVISOR,
            text = text,
            citation = citation,
            isWarning = isWarning
        )
    }
}
