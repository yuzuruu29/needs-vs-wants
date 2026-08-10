package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdvisorChatSessionTest {

    private fun todayEntry(item: String, costCents: Long, type: EntryType): Entry {
        val now = System.currentTimeMillis()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(now))
        return Entry(id = 1, dateUtc = now, date = date, time = "10:00", item = item, costCents = costCents, type = type)
    }

    @Test
    fun initialSnapshot_hasWelcomeMessage() {
        val session = AdvisorChatSession()
        val snap = session.snapshot()
        assertEquals(1, snap.size)
        assertEquals(ChatSender.ADVISOR, snap[0].sender)
    }

    @Test
    fun sendUserQuery_blank_returnsNull() {
        val session = AdvisorChatSession()
        assertNull(session.sendUserQuery("   ", emptyList(), "₱", null))
        assertEquals(1, session.snapshot().size)
    }

    @Test
    fun sendUserQuery_appendsUserAndAdvisor() {
        val session = AdvisorChatSession()
        val entries = listOf(
            Entry(1, 1L, "2026-08-03", "10:00", "Coffee", 15000, EntryType.WANT)
        )
        val updated = session.sendUserQuery(
            queryText = "Can I buy a Want today?",
            entries = entries,
            currencySymbol = "₱",
            dailyBudgetCents = null
        )!!

        assertEquals(3, updated.size)
        assertEquals(ChatSender.USER, updated[1].sender)
        assertEquals(ChatSender.ADVISOR, updated[2].sender)
        assertTrue(updated[2].text.isNotBlank())
    }

    @Test
    fun sendUserQuery_overspendQuery_warnsWhenOverBudget() {
        val session = AdvisorChatSession()
        val entries = listOf(
            todayEntry("Dinner", 600_00, EntryType.WANT)
        )
        val updated = session.sendUserQuery(
            queryText = "What is my overspend status?",
            entries = entries,
            currencySymbol = "₱",
            dailyBudgetCents = 100_00
        )!!
        val advisor = updated.last()
        assertTrue(advisor.isWarning)
        assertTrue(advisor.text.contains("over budget", ignoreCase = true))
        assertTrue(advisor.citation!!.section.contains("Section"))
    }

    @Test
    fun sendUserQuery_spendingGoal_passedToEngine() {
        val session = AdvisorChatSession()
        val entries = listOf(
            todayEntry("Groceries", 100_00, EntryType.NEED)
        )
        val updated = session.sendUserQuery(
            queryText = "Analyze my week",
            entries = entries,
            currencySymbol = "₱",
            dailyBudgetCents = null,
            spendingGoal = "analyze"
        )!!
        val advisor = updated.last()
        assertTrue(advisor.text.contains("analyze", ignoreCase = true))
        assertTrue(advisor.citation!!.section.contains("Section"))
    }

    @Test
    fun sendUserQuery_defaultSpendingGoal_isTrack() {
        val session = AdvisorChatSession()
        val entries = listOf(
            todayEntry("Groceries", 100_00, EntryType.NEED)
        )
        val updated = session.sendUserQuery(
            queryText = "Analyze my week",
            entries = entries,
            currencySymbol = "₱",
            dailyBudgetCents = null
        )!!
        val advisor = updated.last()
        assertTrue(advisor.text.contains("track", ignoreCase = true))
    }
}
