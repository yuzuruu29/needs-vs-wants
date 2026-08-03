package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import org.junit.Assert.*
import org.junit.Test

class FinancialAdvisorTest {

    @Test
    fun generateInsight_whenOverDailyBudget_returnsRecoveryWarning() {
        val entries = listOf(
            Entry(id = 1, dateUtc = 1785736520000L, date = "2026-08-03", time = "10:00", item = "Dinner", costCents = 600000, type = EntryType.WANT)
        )

        val insight = FinancialAdvisorEngine.generateInsight(
            entries = entries,
            currencySymbol = "₱",
            dailyBudgetCents = 500000
        )

        assertTrue(insight.isWarning)
        assertEquals("Compensatory Budget Recovery Active", insight.headline)
        assertTrue(insight.advice.contains("exceeded today's budget limit"))
        assertEquals("Notebook #3: Impulse Recovery", insight.citation.title)
    }

    @Test
    fun generateInsight_whenWantsExceedNeeds_returnsEquilibriumWarning() {
        val entries = listOf(
            Entry(id = 1, dateUtc = 1785736520000L, date = "2026-08-03", time = "10:00", item = "Groceries", costCents = 100000, type = EntryType.NEED),
            Entry(id = 2, dateUtc = 1785736520000L, date = "2026-08-03", time = "11:00", item = "Gadget", costCents = 300000, type = EntryType.WANT)
        )

        val insight = FinancialAdvisorEngine.generateInsight(
            entries = entries,
            currencySymbol = "₱",
            dailyBudgetCents = 1000000
        )

        assertTrue(insight.isWarning)
        assertEquals("Discretionary Spending Exceeds Baseline Needs", insight.headline)
        assertEquals("Notebook #1: Budgetary Equilibrium", insight.citation.title)
    }

    @Test
    fun generateInsight_whenBalanced_returnsStandardTargetInsight() {
        val entries = listOf(
            Entry(id = 1, dateUtc = 1785736520000L, date = "2026-08-03", time = "10:00", item = "Rent", costCents = 500000, type = EntryType.NEED),
            Entry(id = 2, dateUtc = 1785736520000L, date = "2026-08-03", time = "11:00", item = "Coffee", costCents = 100000, type = EntryType.WANT)
        )

        val insight = FinancialAdvisorEngine.generateInsight(
            entries = entries,
            currencySymbol = "₱",
            dailyBudgetCents = 1000000
        )

        assertFalse(insight.isWarning)
        assertEquals("Spending Within Economic Study Targets", insight.headline)
        assertEquals("Notebook #2: Behavioral Friction", insight.citation.title)
    }

    @Test
    fun evaluateConversationalQuery_handlesOverspendQuestion() {
        val entries = listOf(
            Entry(id = 1, dateUtc = 1785736520000L, date = "2026-08-03", time = "10:00", item = "Shirt", costCents = 600000, type = EntryType.WANT)
        )

        val chatMsg = FinancialAdvisorEngine.evaluateConversationalQuery(
            query = "Am I over budget today?",
            entries = entries,
            currencySymbol = "₱",
            dailyBudgetCents = 500000
        )

        assertEquals(ChatSender.ADVISOR, chatMsg.sender)
        assertTrue(chatMsg.isWarning)
        assertTrue(chatMsg.text.contains("over budget"))
        assertNotNull(chatMsg.citation)
    }

    @Test
    fun evaluateConversationalQuery_handlesWantPurchaseQuestion() {
        val entries = listOf(
            Entry(id = 1, dateUtc = 1785736520000L, date = "2026-08-03", time = "10:00", item = "Food", costCents = 200000, type = EntryType.NEED)
        )

        val chatMsg = FinancialAdvisorEngine.evaluateConversationalQuery(
            query = "Can I buy a Want item?",
            entries = entries,
            currencySymbol = "₱",
            dailyBudgetCents = 1000000
        )

        assertEquals(ChatSender.ADVISOR, chatMsg.sender)
        assertFalse(chatMsg.isWarning)
        assertTrue(chatMsg.text.contains("capacity for a deliberate Want"))
    }
}
