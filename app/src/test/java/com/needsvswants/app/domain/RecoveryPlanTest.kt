package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class RecoveryPlanTest {

    private val dayMs = TimeUnit.DAYS.toMillis(1)

    /** Fixed reference "now"; day windows resolve in the local time zone. */
    private val NOW = 1786320000000L

    private val sectionCitation = Regex("Section \\d+\\.\\d+")

    private fun dayStart(epochMs: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = epochMs
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun entry(
        daysAgo: Int,
        item: String,
        costCents: Long,
        type: EntryType
    ): Entry {
        val dateUtc = dayStart(NOW) - daysAgo * dayMs
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(dateUtc))
        return Entry(
            id = daysAgo.toLong(),
            dateUtc = dateUtc,
            date = date,
            time = "10:00",
            item = item,
            costCents = costCents,
            type = type
        )
    }

    private fun pack(
        entries: List<Entry>,
        dailyBudgetCents: Long? = null,
        spendingGoal: String = "track"
    ): AdvisorContextPack =
        AdvisorContextPack.build(entries, dailyBudgetCents, spendingGoal, NOW)

    // --- Deterministic day-cap math (33% / 20% / 0) -----------------------------

    @Test
    fun build_recover33Then20PercentOfWantsToday_thenFullBudget() {
        // Budget ₱100, today total ₱120 (over by ₱20), today's Wants ₱30.
        val plan = RecoveryPlan.build(
            dailyBudgetCents = 100_00,
            todayTotalCents = 120_00,
            wantsTodayCents = 30_00
        )

        assertNotNull(plan)
        assertEquals(20_00L, plan!!.overByCents)
        assertEquals(3, plan.dayCaps.size)
        // D+1 = 10000 - 3000 * 33 / 100 = 9010
        assertEquals(90_10L, plan.dayCaps[0])
        // D+2 = 10000 - 3000 * 20 / 100 = 9400
        assertEquals(94_00L, plan.dayCaps[1])
        // D+3 = full budget
        assertEquals(100_00L, plan.dayCaps[2])
    }

    @Test
    fun build_wantsTodayZero_dayCapsEqualBudgetEveryDay() {
        val plan = RecoveryPlan.build(
            dailyBudgetCents = 100_00,
            todayTotalCents = 120_00,
            wantsTodayCents = 0
        )

        assertEquals(listOf(100_00L, 100_00L, 100_00L), plan!!.dayCaps)
    }

    @Test
    fun build_wantsTodayMuchLargerThanBudget_dayCapsFloorAtZero() {
        // D+1 = 10000 - 40000 * 33 / 100 = -3200 -> 0; D+2 = 2000.
        val plan = RecoveryPlan.build(
            dailyBudgetCents = 100_00,
            todayTotalCents = 120_00,
            wantsTodayCents = 400_00
        )

        assertEquals(0L, plan!!.dayCaps[0])
        assertEquals(20_00L, plan.dayCaps[1])
        assertEquals(100_00L, plan.dayCaps[2])
    }

    // --- needOnlyEvenings threshold (overBy > 25% of budget, strict) -------------

    @Test
    fun build_needOnlyEvenings_whenOverByExactlyQuarter_isFalse() {
        val plan = RecoveryPlan.build(
            dailyBudgetCents = 100_00,
            todayTotalCents = 125_00,
            wantsTodayCents = 0
        )

        assertEquals(25_00L, plan!!.overByCents)
        assertFalse(plan.needOnlyEvenings)
    }

    @Test
    fun build_needOnlyEvenings_whenOverByAboveQuarter_isTrue() {
        val plan = RecoveryPlan.build(
            dailyBudgetCents = 100_00,
            todayTotalCents = 125_01,
            wantsTodayCents = 0
        )

        assertTrue(plan!!.needOnlyEvenings)
    }

    @Test
    fun build_needOnlyEvenings_whenSmallOverspend_isFalse() {
        val plan = RecoveryPlan.build(
            dailyBudgetCents = 100_00,
            todayTotalCents = 110_00,
            wantsTodayCents = 50_00
        )

        assertFalse(plan!!.needOnlyEvenings)
    }

    // --- Null plan when there is nothing to recover ------------------------------

    @Test
    fun build_zeroOver_returnsNull() {
        assertNull(
            RecoveryPlan.build(
                dailyBudgetCents = 100_00,
                todayTotalCents = 100_00,
                wantsTodayCents = 30_00
            )
        )
    }

    @Test
    fun build_underBudget_returnsNull() {
        assertNull(
            RecoveryPlan.build(
                dailyBudgetCents = 100_00,
                todayTotalCents = 80_00,
                wantsTodayCents = 30_00
            )
        )
    }

    @Test
    fun build_budgetOff_returnsNull() {
        assertNull(RecoveryPlan.build(null, todayTotalCents = 120_00, wantsTodayCents = 30_00))
        assertNull(RecoveryPlan.build(0, todayTotalCents = 120_00, wantsTodayCents = 30_00))
    }

    // --- Citation presence --------------------------------------------------------

    @Test
    fun build_citationMatchesExistingAsset() {
        val plan = RecoveryPlan.build(
            dailyBudgetCents = 100_00,
            todayTotalCents = 120_00,
            wantsTodayCents = 30_00
        )

        assertEquals("NotebookLM Section 4.5 — Compensatory Sinking Protocol", plan!!.citation)
        assertTrue(sectionCitation.containsMatchIn(plan.citation))
        assertEquals(plan.citation, RecoveryPlan.CITATION)
    }

    // --- Engine adapter: built from the live AdvisorContextPack ------------------

    @Test
    fun engine_buildRecoveryPlan_fromOverBudgetContext() {
        val ctx = pack(
            entries = listOf(
                entry(daysAgo = 0, item = "Groceries", costCents = 50_00, type = EntryType.NEED),
                entry(daysAgo = 0, item = "Sneakers", costCents = 70_00, type = EntryType.WANT),
                // A week-old Want must NOT count toward wantsTodayCents.
                entry(daysAgo = 7, item = "Old Want", costCents = 999_00, type = EntryType.WANT)
            ),
            dailyBudgetCents = 100_00
        )

        val plan = FinancialAdvisorEngine.buildRecoveryPlan(ctx)

        assertNotNull(plan)
        assertEquals(20_00L, plan!!.overByCents)
        assertEquals(70_00L, ctx.wantsTodayCents)
        // D+1 = 10000 - 7000 * 33 / 100 = 7690
        assertEquals(76_90L, plan.dayCaps[0])
        // D+2 = 10000 - 7000 * 20 / 100 = 8600
        assertEquals(86_00L, plan.dayCaps[1])
        assertEquals(100_00L, plan.dayCaps[2])
    }

    @Test
    fun engine_buildRecoveryPlan_nullWhenWithinBudget() {
        val ctx = pack(
            entries = listOf(
                entry(daysAgo = 0, item = "Groceries", costCents = 80_00, type = EntryType.NEED)
            ),
            dailyBudgetCents = 100_00
        )

        assertNull(FinancialAdvisorEngine.buildRecoveryPlan(ctx))
    }

    @Test
    fun engine_buildRecoveryPlan_nullWhenBudgetOff() {
        val ctx = pack(
            entries = listOf(
                entry(daysAgo = 0, item = "Sneakers", costCents = 120_00, type = EntryType.WANT)
            ),
            dailyBudgetCents = null
        )

        assertNull(FinancialAdvisorEngine.buildRecoveryPlan(ctx))
    }
}
