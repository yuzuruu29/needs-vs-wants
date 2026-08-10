package com.needsvswants.app.domain

import com.needsvswants.app.data.model.Entry
import com.needsvswants.app.data.model.EntryType
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

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

/**
 * Pure ledger context snapshot the coach rules reason over. No Android types.
 * Built from [Entry]s plus the optional daily budget and the user's spending
 * goal; "week" means the last 7 calendar days ending today (local time via
 * [Calendar]).
 */
data class AdvisorContextPack(
    val todayTotalCents: Long,
    val weekTotalCents: Long,
    val needsCents: Long,
    val wantsCents: Long,
    val wantsTodayCents: Long,
    val needsPct: Double,
    val wantsPct: Double,
    val budgetOn: Boolean,
    val remainingCents: Long,
    val streakDays: Int,
    val topWantItems: List<String>,
    val spendingGoal: String
) {
    companion object {
        const val DEFAULT_SPENDING_GOAL = "track"

        private val dayMs = TimeUnit.DAYS.toMillis(1)

        fun build(
            entries: List<Entry>,
            dailyBudgetCents: Long?,
            spendingGoal: String = DEFAULT_SPENDING_GOAL,
            nowEpochMs: Long = System.currentTimeMillis()
        ): AdvisorContextPack {
            val todayStart = startOfDayMs(nowEpochMs)
            val weekStart = todayStart - 6 * dayMs
            val todayEnd = todayStart + dayMs

            val todayEntries = entries.filter { it.dateUtc in todayStart until todayEnd }
            val weekEntries = entries.filter { it.dateUtc >= weekStart && it.dateUtc < todayEnd }

            val needsCents = weekEntries.filter { it.type == EntryType.NEED }.sumOf { it.costCents }
            val wantsCents = weekEntries.filter { it.type == EntryType.WANT }.sumOf { it.costCents }
            val wantsTodayCents = todayEntries.filter { it.type == EntryType.WANT }.sumOf { it.costCents }
            val weekTotalCents = needsCents + wantsCents
            val todayTotalCents = todayEntries.sumOf { it.costCents }

            val budgetOn = dailyBudgetCents != null && dailyBudgetCents > 0
            val remainingCents = if (budgetOn && dailyBudgetCents != null) {
                dailyBudgetCents - todayTotalCents
            } else {
                0L
            }

            val needsPct = if (weekTotalCents > 0) needsCents * 100.0 / weekTotalCents else 0.0
            val wantsPct = if (weekTotalCents > 0) wantsCents * 100.0 / weekTotalCents else 0.0

            val streakDays = StreakMath.currentStreak(entries.map { it.date }, nowEpochMs)

            val topWantItems = entries.asSequence()
                .filter { it.type == EntryType.WANT }
                .sortedByDescending { it.costCents }
                .take(3)
                .map { it.item }
                .toList()

            return AdvisorContextPack(
                todayTotalCents = todayTotalCents,
                weekTotalCents = weekTotalCents,
                needsCents = needsCents,
                wantsCents = wantsCents,
                wantsTodayCents = wantsTodayCents,
                needsPct = needsPct,
                wantsPct = wantsPct,
                budgetOn = budgetOn,
                remainingCents = remainingCents,
                streakDays = streakDays,
                topWantItems = topWantItems,
                spendingGoal = spendingGoal
            )
        }

        private fun startOfDayMs(epochMs: Long): Long {
            val cal = Calendar.getInstance()
            cal.timeInMillis = epochMs
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis
        }
    }
}

/**
 * 3-day compensatory recovery plan (study_03, NotebookLM Section 4.5).
 * Pure cents math, no Android types. Null when there is no budget or no
 * overspend to recover.
 */
data class RecoveryPlan(
    val overByCents: Long,
    val dayCaps: List<Long>,
    val needOnlyEvenings: Boolean,
    val citation: String
) {
    companion object {
        /** Existing asset citation (economic_studies_index.json study_03), verbatim. */
        const val CITATION = "NotebookLM Section 4.5 — Compensatory Sinking Protocol"
        const val DAY_COUNT = 3

        private const val WANT_RECOVERY_D1_PERCENT = 33
        private const val WANT_RECOVERY_D2_PERCENT = 20

        /**
         * Deterministic integer cents math:
         * - D+1 cap = max(0, budget - wantsToday * 33 / 100)  (recover 33% of today's Want spend)
         * - D+2 cap = max(0, budget - wantsToday * 20 / 100)
         * - D+3 cap = budget
         * [needOnlyEvenings] is true when the overspend exceeds 25% of the
         * daily budget (strictly greater).
         * @return null when the budget is off or today is not over budget.
         */
        fun build(
            dailyBudgetCents: Long?,
            todayTotalCents: Long,
            wantsTodayCents: Long
        ): RecoveryPlan? {
            if (dailyBudgetCents == null || dailyBudgetCents <= 0) return null
            val overByCents = todayTotalCents - dailyBudgetCents
            if (overByCents <= 0) return null
            val dayCaps = listOf(
                (dailyBudgetCents - wantsTodayCents * WANT_RECOVERY_D1_PERCENT / 100).coerceAtLeast(0L),
                (dailyBudgetCents - wantsTodayCents * WANT_RECOVERY_D2_PERCENT / 100).coerceAtLeast(0L),
                dailyBudgetCents
            )
            val needOnlyEvenings = overByCents * 4 > dailyBudgetCents
            return RecoveryPlan(
                overByCents = overByCents,
                dayCaps = dayCaps,
                needOnlyEvenings = needOnlyEvenings,
                citation = CITATION
            )
        }
    }
}

/**
 * Pre-seal Want consult verdict (study_04 Want Impulse Hold Protocol /
 * study_05 Daily Budget Health Check). Pure; no Android types. Null when the
 * daily budget is off: there is no guardrail to measure against.
 */
data class WantHold(
    val hold: Boolean,
    val reason: String,
    val citation: String
) {
    companion object {
        /** study_04 asset citation, verbatim. */
        const val CITATION_HOLD = "NotebookLM Section 1.2 — Binary Classification Dynamics"

        /** study_05 asset citation, verbatim (single source: [FinancialAdvisorEngine.CITATION_SECTION_31]). */
        const val CITATION_OK = FinancialAdvisorEngine.CITATION_SECTION_31
    }
}

/**
 * Quick-protocol chip labels for the Advisor dashboard (spec verbatim):
 * Overspend · Can I buy this? · Want share · Budget health · Weekend plan.
 * Tapping a chip sends [advisorProtocolQuery] of the label into chat.
 */
object AdvisorProtocols {
    const val OVERSPEND = "Overspend"
    const val CAN_I_BUY_THIS = "Can I buy this?"
    const val WANT_SHARE = "Want share"
    const val BUDGET_HEALTH = "Budget health"
    const val WEEKEND_PLAN = "Weekend plan"

    /** Dashboard chip row order, spec verbatim. */
    val ALL: List<String> = listOf(OVERSPEND, CAN_I_BUY_THIS, WANT_SHARE, BUDGET_HEALTH, WEEKEND_PLAN)
}

/**
 * Maps a quick-protocol chip [protocol] to the chat query sent to the coach.
 * Each query is worded so [FinancialAdvisorEngine.evaluateConversationalQuery]
 * routes it to the matching rule branch (overspend, buy, share, health,
 * weekend). Unknown labels pass through as-is. Pure and unit-tested.
 */
fun advisorProtocolQuery(protocol: String): String = when (protocol) {
    AdvisorProtocols.OVERSPEND -> "What is my overspend status?"
    AdvisorProtocols.CAN_I_BUY_THIS -> "Can I buy this Want item?"
    AdvisorProtocols.WANT_SHARE -> "What is my Want share this week?"
    AdvisorProtocols.BUDGET_HEALTH -> "How is my budget health today?"
    AdvisorProtocols.WEEKEND_PLAN -> "Give me a weekend plan"
    else -> protocol
}

object FinancialAdvisorEngine {

    const val SOURCE_OF_TRUTH_TITLE = "Google NotebookLM — Economic Studies"
    const val DEFAULT_NOTEBOOK_URL = "https://notebook.google.com/"

    /**
     * Section 3.1 citation, verbatim from economic_studies_index.json
     * (study_02 / study_05 / study_07 / study_08). Single source of truth so
     * the engine and the asset index cannot drift apart again.
     */
    const val CITATION_SECTION_31 = "NotebookLM Section 3.1 — Real-Time Friction Behavioral Control"

    private const val HOLD_SHARE_PERCENT = 15
    private const val WANTS_SHARE_HOLD_THRESHOLD = 55.0

    /**
     * Pre-seal consult for a draft Want. Hold when the item costs strictly
     * more than 15% of today's remaining budget, or Wants are strictly more
     * than 55% of this week's spending (integer cents math on the share
     * threshold). Returns null when the daily budget is off.
     */
    fun wantHoldSuggestion(costCents: Long, context: AdvisorContextPack): WantHold? {
        if (!context.budgetOn) return null
        val overRemainingShare = costCents > context.remainingCents * HOLD_SHARE_PERCENT / 100
        val wantsHeavy = context.wantsPct > WANTS_SHARE_HOLD_THRESHOLD
        return if (overRemainingShare || wantsHeavy) {
            WantHold(
                hold = true,
                reason = if (overRemainingShare) {
                    "This Want costs more than 15% of what remains for today."
                } else {
                    "Wants are more than 55% of this week's spending."
                },
                citation = WantHold.CITATION_HOLD
            )
        } else {
            WantHold(
                hold = false,
                reason = "This Want stays within today's remaining budget.",
                citation = WantHold.CITATION_OK
            )
        }
    }

    private fun pct(value: Double): Int = value.roundToInt()

    /**
     * Recovery plan for the live [AdvisorContextPack]: null when the budget is
     * off or today is within budget. The daily budget is recovered from the
     * pack (budget = todayTotal + remaining), so no extra inputs are needed.
     */
    fun buildRecoveryPlan(context: AdvisorContextPack): RecoveryPlan? {
        if (!context.budgetOn) return null
        return RecoveryPlan.build(
            dailyBudgetCents = context.todayTotalCents + context.remainingCents,
            todayTotalCents = context.todayTotalCents,
            wantsTodayCents = context.wantsTodayCents
        )
    }

    /**
     * Coach gate predicate (Task 3): intercept the Want seal only when the
     * user has Max access, the row is a Want, and the coach verdict is a
     * hold. Needs, Free/Pro, and all-clear verdicts fall through to the
     * untouched seal path.
     */
    fun shouldInterceptCoach(
        hasMaxAccess: Boolean,
        type: EntryType,
        suggestion: WantHold?
    ): Boolean = hasMaxAccess && type == EntryType.WANT && suggestion?.hold == true

    /**
     * Rule library for the Today's insight card. Evaluated top-down; every rule
     * returns advice plus a NotebookLM citation (rule 1-3 are the original
     * rules, 4-7 extend the library with streak, weekend, goal, and
     * budget-health rules).
     */
    fun generateInsight(context: AdvisorContextPack): AdvisorInsight {
        val topWantsText = context.topWantItems.takeIf { it.isNotEmpty() }
            ?.joinToString(", ")
            ?: "no big Want items yet"

        // Rule 1: Daily budget overspend recovery (NotebookLM Section 4.5)
        if (context.budgetOn && context.remainingCents < 0) {
            return AdvisorInsight(
                headline = "Compensatory Budget Recovery Active",
                advice = "You have exceeded today's budget limit. According to your Economic Study Notebook #3 (Impulse Recovery), reduce discretionary Want spending by 33% over the next 3 days to restore liquidity equilibrium.",
                citation = AdvisorCitation(
                    title = "Notebook #3: Impulse Recovery",
                    section = "NotebookLM Section 4.5 — Compensatory Sinking Protocol"
                ),
                isWarning = true
            )
        }

        // Rule 2: Want vs Need equilibrium (NotebookLM Section 1.2)
        if (context.weekTotalCents > 0 && context.wantsCents > context.needsCents) {
            return AdvisorInsight(
                headline = "Discretionary Spending Exceeds Baseline Needs",
                advice = "Your Economic Study Notebook #1 (Budgetary Equilibrium) principles indicate that discretionary Wants currently surpass essential Needs (${pct(context.wantsPct)}% of the week). Introduce a 24-hour delay before logging additional Want items.",
                citation = AdvisorCitation(
                    title = "Notebook #1: Budgetary Equilibrium",
                    section = "NotebookLM Section 1.2 — Binary Classification Dynamics"
                ),
                isWarning = true
            )
        }

        // Rule 3: Weekend streak plan (NotebookLM Section 3.1)
        if (context.streakDays >= 5) {
            return AdvisorInsight(
                headline = "Weekend Streak Plan",
                advice = "You are on a ${context.streakDays}-day logging streak. Protect it through the weekend: pre-plan Want purchases, keep Needs as your anchor, and log every entry at point of sale.",
                citation = AdvisorCitation(
                    title = "Notebook #2: Behavioral Friction",
                    section = CITATION_SECTION_31
                ),
                isWarning = false
            )
        }

        // Rule 4: Streak momentum (NotebookLM Section 3.1)
        if (context.streakDays >= 2) {
            return AdvisorInsight(
                headline = "Streak Momentum",
                advice = "Keep the ${context.streakDays}-day streak alive: one more day of logging strengthens the habit loop your study notebooks describe.",
                citation = AdvisorCitation(
                    title = "Notebook #2: Behavioral Friction",
                    section = CITATION_SECTION_31
                ),
                isWarning = false
            )
        }

        // Rule 5: Analyze-goal period comparison (NotebookLM Section 1.2)
        if (context.spendingGoal == "analyze" && context.weekTotalCents > 0) {
            return AdvisorInsight(
                headline = "Analyze Goal: Weekly Comparison",
                advice = "Your 7-day ledger is ${pct(context.needsPct)}% Needs and ${pct(context.wantsPct)}% Wants, with today at ${context.todayTotalCents} of ${context.weekTotalCents} total cents. Compare this week against the next to spot the shift.",
                citation = AdvisorCitation(
                    title = "Notebook #1: Budgetary Equilibrium",
                    section = "NotebookLM Section 1.2 — Binary Classification Dynamics"
                ),
                isWarning = false
            )
        }

        // Rule 6: Budget health OK (NotebookLM Section 3.1)
        if (context.budgetOn && context.remainingCents >= 0) {
            return AdvisorInsight(
                headline = "Budget Health: Within Daily Limit",
                advice = "Today's spending is within your daily budget with ${context.remainingCents} cents remaining. Keep logging every purchase to preserve the friction your study notebooks recommend.",
                citation = AdvisorCitation(
                    title = "Notebook #2: Behavioral Friction",
                    section = CITATION_SECTION_31
                ),
                isWarning = false
            )
        }

        // Rule 7: Balanced baseline (NotebookLM Section 3.1)
        return AdvisorInsight(
            headline = "Spending Within Economic Study Targets",
            advice = "Based on your Google NotebookLM economic study notebooks, your spending velocity is balanced. Essential Needs form the anchor of your daily ledger; top Wants so far: $topWantsText.",
            citation = AdvisorCitation(
                title = "Notebook #2: Behavioral Friction",
                section = CITATION_SECTION_31
            ),
            isWarning = false
        )
    }

    /**
     * Keyword router over [AdvisorContextPack]. Every branch returns advice
     * plus a citation; no branch answers without one.
     */
    fun evaluateConversationalQuery(
        query: String,
        context: AdvisorContextPack
    ): ChatMessage {
        val lower = query.lowercase()
        val topWantsText = context.topWantItems.takeIf { it.isNotEmpty() }
            ?.joinToString(", ")
            ?: "none yet"

        val (text, citation, isWarning) = when {
            // Branch 1: Want share / ratio (NotebookLM Section 1.2)
            lower.contains("ratio") || lower.contains("share") || lower.contains("split") ||
                lower.contains("percent") || lower.contains("percentage") -> {
                Triple(
                    "Over the last 7 days your ledger is ${pct(context.needsPct)}% Needs and ${pct(context.wantsPct)}% Wants. Notebook #1 keeps Needs above 50% as the core anchor. Your top Wants by cost: $topWantsText.",
                    AdvisorCitation("Notebook #1: Budgetary Equilibrium", "NotebookLM Section 1.2 — Binary Classification Dynamics"),
                    context.wantsPct > 50.0
                )
            }

            // Branch 2: Weekend plan (NotebookLM Section 3.1)
            lower.contains("weekend") || lower.contains("saturday") || lower.contains("sunday") -> {
                val streakNote = if (context.streakDays >= 5) {
                    " You are on a ${context.streakDays}-day streak; protect it by pre-planning weekend Wants."
                } else {
                    ""
                }
                Triple(
                    "Plan the weekend as a ledger, not a spree: set a Want cap, keep Needs first, and log at point of sale.$streakNote",
                    AdvisorCitation("Notebook #2: Behavioral Friction", CITATION_SECTION_31),
                    false
                )
            }

            // Branch 3: Streak (NotebookLM Section 3.1)
            lower.contains("streak") || lower.contains("consecutive") -> {
                if (context.streakDays > 0) {
                    Triple(
                        "You are on a ${context.streakDays}-day logging streak. Consistency is the strongest habit signal in your study notebooks; keep going.",
                        AdvisorCitation("Notebook #2: Behavioral Friction", CITATION_SECTION_31),
                        false
                    )
                } else {
                    Triple(
                        "You have no active streak yet. Log at least one purchase today to start the habit loop your study notebooks describe.",
                        AdvisorCitation("Notebook #2: Behavioral Friction", CITATION_SECTION_31),
                        false
                    )
                }
            }

            // Branch 4: Budget health (NotebookLM Section 3.1)
            lower.contains("health") || lower.contains("healthy") || lower.contains("situation") -> {
                if (context.budgetOn && context.remainingCents < 0) {
                    Triple(
                        "Your budget health is red: today's spending is over the daily limit. Notebook #3 recommends a 33% Want cut for the next 3 days.",
                        AdvisorCitation("Notebook #3: Impulse Recovery", "NotebookLM Section 4.5 — Compensatory Sinking Protocol"),
                        true
                    )
                } else if (context.budgetOn) {
                    Triple(
                        "Your budget health is green: today is within the daily limit with ${context.remainingCents} cents remaining. Keep real-time log friction on Want items.",
                        AdvisorCitation("Notebook #2: Behavioral Friction", CITATION_SECTION_31),
                        false
                    )
                } else {
                    Triple(
                        "No daily budget is set, so there is no guardrail to measure. Set one on the Log screen to give the coach a target.",
                        AdvisorCitation("Notebook #2: Behavioral Friction", CITATION_SECTION_31),
                        false
                    )
                }
            }

            // Branch 5: Goal / analyze (NotebookLM Section 1.2)
            lower.contains("goal") || lower.contains("analyze") || lower.contains("analysis") -> {
                Triple(
                    "Your spending goal is ${context.spendingGoal}. For an analyze-style review: your week is ${pct(context.needsPct)}% Needs and ${pct(context.wantsPct)}% Wants; keep comparing periods to spot the trend.",
                    AdvisorCitation("Notebook #1: Budgetary Equilibrium", "NotebookLM Section 1.2 — Binary Classification Dynamics"),
                    false
                )
            }

            // Branch 6: Overspend / budget (NotebookLM Section 4.5 or 3.1)
            lower.contains("over") || lower.contains("overspend") || lower.contains("exceed") ||
                lower.contains("limit") || lower.contains("budget") -> {
                if (context.budgetOn && context.remainingCents < 0) {
                    Triple(
                        "You are currently over budget today. Per Notebook #3 (Impulse Recovery), absorb this overspend by lowering your Want spending allowance over the next 3 days.",
                        AdvisorCitation("Notebook #3: Impulse Recovery", "NotebookLM Section 4.5 — Compensatory Sinking Protocol"),
                        true
                    )
                } else {
                    Triple(
                        "Your daily spending is within your budget. Keep maintaining real-time log friction for non-essential Want items.",
                        AdvisorCitation("Notebook #2: Behavioral Friction", CITATION_SECTION_31),
                        false
                    )
                }
            }

            // Branch 7: Buy / afford a Want (NotebookLM Section 1.2)
            lower.contains("buy") || lower.contains("afford") || lower.contains("purchase") ||
                lower.contains("want") -> {
                val wantsHeavy = context.weekTotalCents > 0 && context.wantsPct >= 50.0
                val overBudget = context.budgetOn && context.remainingCents < 0
                if (wantsHeavy || overBudget) {
                    Triple(
                        "Discretionary Want spend is ${pct(context.wantsPct)}% of your week and ${if (overBudget) "today is over budget" else "heavy"}. Notebook #1 advises waiting 24 hours before buying another Want.",
                        AdvisorCitation("Notebook #1: Budgetary Equilibrium", "NotebookLM Section 1.2 — Binary Classification"),
                        true
                    )
                } else {
                    Triple(
                        "Your Needs currently form the majority of your ledger (${pct(context.needsPct)}% this week). You have capacity for a deliberate Want if it stays within your budget.",
                        AdvisorCitation("Notebook #1: Budgetary Equilibrium", "NotebookLM Section 1.2 — Binary Classification"),
                        false
                    )
                }
            }

            // Branch 8: Default grounding (NotebookLM Section 3.1)
            else -> {
                Triple(
                    "Grounded in your Google NotebookLM economic studies: Always classify entries at point-of-sale to preserve behavioral friction. Needs should remain your core financial anchor.",
                    AdvisorCitation("Notebook #2: Behavioral Control", CITATION_SECTION_31),
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
