package com.needsvswants.app.ui.screens.summary

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.DailySpend
import com.needsvswants.app.domain.Period
import com.needsvswants.app.domain.StreakMilestone
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.domain.toMoneyWhole
import com.needsvswants.app.ui.navigation.verticalScrollFirst
import com.needsvswants.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    onNavigateToInput: () -> Unit = {},
    viewModel: SummaryViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val trendPct by viewModel.trendPct.collectAsStateWithLifecycle()
    val period by viewModel.period.collectAsStateWithLifecycle()
    val symbol by viewModel.currencySymbol.collectAsStateWithLifecycle()
    val budgetStatus by viewModel.budgetStatus.collectAsStateWithLifecycle()
    val streakDays by viewModel.streakDays.collectAsStateWithLifecycle()
    val bestStreak by viewModel.bestStreak.collectAsStateWithLifecycle()
    val insights by viewModel.insights.collectAsStateWithLifecycle()
    val hasHistory by viewModel.hasHistory.collectAsStateWithLifecycle()
    val isFirstLaunch by viewModel.isFirstLaunch.collectAsStateWithLifecycle()
    val entitlement by viewModel.entitlement.collectAsStateWithLifecycle()
    val now = System.currentTimeMillis()
    val paid = entitlement.hasProAccessAt(now)
    val isMax = entitlement.hasMaxAccessAt(now)
    var showInstructions by remember { mutableStateOf(false) }
    var showSplitPortal by remember { mutableStateOf(false) }
    var currentMilestone by remember { mutableStateOf<StreakMilestone?>(null) }
    // Pull-to-refresh (design audit #11): auto-dismiss shortly after a refresh
    // so the gold spinner never sticks if the reactive flows don't re-emit.
    var refreshing by remember { mutableStateOf(false) }
    val palette = AppTheme.colors
    val haptics = rememberAppHaptics()
    val context = LocalContext.current

    // First cold start: How It Works before soft paywall (LaunchPaywall waits on !isFirstLaunch).
    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) showInstructions = true
    }

    LaunchedEffect(Unit) {
        viewModel.newMilestone.collect { ms ->
            currentMilestone = ms
        }
    }

    // Warn once per crossing into the over-budget state (haptics allowed under reduced motion).
    var wasOver by remember { mutableStateOf(false) }
    LaunchedEffect(budgetStatus) {
        val status = budgetStatus
        val isOver = status is BudgetStatus.On && status.remainingCents < 0
        if (isOver && !wasOver) haptics.warn()
        wasOver = isOver
    }

    if (showInstructions) {
        InstructionsOverlay(
            paid = paid,
            onSelectGoal = {
                viewModel.setSpendingGoal(it)
                if (it == "budget") viewModel.setBudgetNudgePending(true)
            },
            onDismiss = {
                showInstructions = false
                if (isFirstLaunch) viewModel.dismissFirstLaunch()
            }
        )
    }

    val pullState = rememberPullToRefreshState()
    // Auto-dismiss the gold spinner shortly after it starts — the stats/entitlement
    // flows re-emit reactively, but this guarantees the indicator retracts.
    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(900)
            refreshing = false
        }
    }
    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = {
            haptics.tick()
            refreshing = true
            viewModel.refresh()
        },
        state = pullState,
        indicator = {
            // Gold spinner on a paper chip (design audit #11). Positional args:
            // (state, isRefreshing, modifier, containerColor, indicatorColor).
            PullToRefreshDefaults.Indicator(
                pullState,
                refreshing,
                Modifier.align(Alignment.TopCenter),
                palette.surfaceCard,
                palette.gold
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(themedInkWash())
                .verticalScrollFirst()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 12.dp)
        ) {
            // Editorial header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Eyebrow(
                    when {
                        isMax -> "Max desk"
                        paid -> "Pro diary"
                        else -> "A 30-Day Trainer"
                    },
                    color = when {
                        isMax -> palette.gilt
                        paid -> palette.gold
                        else -> palette.crimson
                    },
                    maxLines = 1
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "NEEDS\nvs WANTS",
                    style = AppType.screenTitle,
                    color = palette.textPrimary
                )
                Spacer(Modifier.height(8.dp))
                GiltRule(width = 40.dp)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Seal every purchase. Learn the split.",
                    color = palette.textSecondary,
                    style = AppType.bodyMd,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HeaderIconWell(
                    onClick = { showInstructions = true },
                    contentDescription = "How it works"
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.HelpOutline,
                        contentDescription = null,
                        tint = palette.crimson,
                        modifier = Modifier.size(20.dp)
                    )
                }
                HeaderIconWell(
                    onClick = {
                        val send = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, viewModel.shareSummaryText())
                        }
                        context.startActivity(Intent.createChooser(send, "Share summary"))
                    },
                    contentDescription = "Share summary"
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = null,
                        tint = palette.crimson,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Period selector — gold-edge pill bar
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .paperSurface(
                    rememberPaperSpec(PaperKind.CHIP, goldEdge = true),
                    RoundedCornerShape(16.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val pillPeriods = if (paid) paidPeriods else freePeriods
                pillPeriods.forEach { p ->
                    val selected = p == period
                    val label = when (p) {
                        Period.DAY -> "Day"
                        Period.WEEK -> "Week"
                        Period.MONTH -> "Month"
                        Period.ALL -> if (paid) "Lifetime" else "All (30d)"
                    }
                    val pillColor by animateColorAsState(
                        targetValue = if (selected) palette.crimson else Color.Transparent,
                        animationSpec = Motion.state(),
                        label = "periodPill"
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (selected) palette.surfaceCard else palette.textSecondary,
                        animationSpec = Motion.state(),
                        label = "periodText"
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = scaledSpacing(40f))
                            .background(pillColor, RoundedCornerShape(12.dp))
                            .clickable {
                                haptics.tick()
                                viewModel.setPeriod(p)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            color = textColor,
                            style = AppType.meta.copy(
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Period body: budget + donut + stats crossfade when Day/Week/All changes
        AnimatedContent(
            targetState = period,
            transitionSpec = {
                val forward = targetState.ordinal > initialState.ordinal
                (
                    fadeIn(Motion.state()) +
                        slideInHorizontally(animationSpec = Motion.state()) {
                            (it * 0.04f).roundToInt() * (if (forward) 1 else -1)
                        }
                    ).togetherWith(
                    fadeOut(Motion.feedback()) +
                        slideOutHorizontally(animationSpec = Motion.feedback()) {
                            (it * 0.04f).roundToInt() * (if (forward) -1 else 1)
                        }
                )
            },
            label = "periodBody",
            modifier = Modifier.fillMaxWidth()
        ) { animatedPeriod ->
            Column(modifier = Modifier.fillMaxWidth()) {
                if (loading && !hasHistory) {
                    // Cold first render — shimmer matching the loaded layout (zero CLS).
                    SummarySkeletonDonut(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                    SummarySkeletonStatRow()
                } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        getPeriodLabel(animatedPeriod, paid),
                        color = palette.crimson,
                        style = AppType.meta.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1
                    )
                    // Trend pill vs the previous period (design audit #5). Hidden on ALL
                    // (baseline is not meaningful for lifetime) and when flat.
                    if (animatedPeriod != Period.ALL && trendPct != 0) {
                        Spacer(Modifier.width(8.dp))
                        TrendPill(
                            trendPct = trendPct,
                            comparedTo = "vs last ${animatedPeriod.name.lowercase()}"
                        )
                    }
                }
                Text(
                    getPeriodRange(animatedPeriod, paid),
                    color = palette.textMuted,
                    style = AppType.caption,
                    maxLines = 2
                )
                Spacer(Modifier.height(16.dp))

                // Daily budget meter — Day period only, when budget is on
                if (animatedPeriod == Period.DAY && budgetStatus is BudgetStatus.On) {
                    DailyBudgetMeter(status = budgetStatus as BudgetStatus.On, symbol = symbol)
                    Spacer(Modifier.height(20.dp))
                }

                // Hero ledger ring — tap opens Need/Want % portal
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 220.dp)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (stats.totalCents == 0L) {
                        if (!hasHistory) {
                            // First-launch: illustration + warm CTA invites the first log.
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                EmptyDiaryIllustration(modifier = Modifier.size(160.dp, 120.dp))
                                Spacer(Modifier.height(16.dp))
                                Eyebrow("EMPTY DIARY", color = palette.textMuted)
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    "Log your first purchase to start the diary.",
                                    style = AppType.bodyMd,
                                    color = palette.textSecondary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(16.dp))
                                GiltButton(
                                    onClick = onNavigateToInput,
                                    text = "Log a purchase",
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                )
                            }
                        } else {
                            // Period empty but history exists — keep the orb + contextual copy.
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                FloatingGeminiOrb(
                                    needsSweepDegrees = 0f,
                                    empty = true,
                                    orbSize = 200.dp
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Eyebrow("READY", color = palette.textMuted, size = 10)
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            "—",
                                            style = AppType.moneyLg,
                                            color = palette.textSecondary
                                        )
                                    }
                                }
                                Spacer(Modifier.height(16.dp))
                                val (emptyEyebrow, emptyBody) = emptyPeriodCopy(animatedPeriod, hasHistory, streakDays)
                                Eyebrow(emptyEyebrow, color = palette.textMuted)
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    emptyBody,
                                    style = AppType.bodyMd,
                                    color = palette.textSecondary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        val total = stats.totalCents.toFloat().coerceAtLeast(1f)
                        val targetSweep = (stats.needsTotalCents / total) * 360f
                        val needsSweep by animateFloatAsState(
                            targetValue = targetSweep,
                            animationSpec = Motion.entrance(),
                            label = "donutNeedsSweep"
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            FloatingGeminiOrb(
                                needsSweepDegrees = needsSweep,
                                empty = false,
                                orbSize = 210.dp,
                                onClick = {
                                    haptics.tick()
                                    showSplitPortal = true
                                },
                                onLongPress = {
                                    haptics.seal()
                                    showSplitPortal = true
                                }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp)
                                ) {
                                    Eyebrow("TOTAL", color = palette.textMuted, size = 10)
                                    Spacer(Modifier.height(4.dp))
                                    // Whole units only — ".00" cramps the dial and shifts alignment.
                                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                                        val totalText = stats.totalCents.toMoneyWhole(symbol)
                                        AnimatedMoney(
                                            cents = stats.totalCents,
                                            symbol = symbol,
                                            wholeOnly = true,
                                            style = AppType.moneyLg.copy(
                                                fontSize = fittingMoneySize(totalText, 22.sp, maxWidth),
                                                lineHeight = fittingMoneySize(totalText, 22.sp, maxWidth) * 1.15f,
                                                color = palette.textPrimary,
                                                textAlign = TextAlign.Center
                                            ),
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Clip,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "Tap the ring for the full split",
                                style = AppType.caption,
                                color = palette.textMuted
                            )
                            Spacer(Modifier.height(10.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LegendChip(palette.need, "Need", stats.needsPct)
                                Box(modifier = Modifier.width(1.dp).height(14.dp).background(palette.inkDivider))
                                LegendChip(palette.want, "Want", stats.wantsPct)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Daily-spend sparkline (design audit #5) — trend shape below the donut.
                if (stats.dailyTotals.size >= 2) {
                    val daily = stats.dailyTotals
                    SparklineChart(
                        data = daily.map { it.totalCents },
                        accentColor = palette.want,
                        labels = daily.map { it.date },
                        tooltip = { i ->
                            val d = daily[i]
                            "${d.date}  ${d.totalCents.toMoney(symbol)}"
                        },
                        haptics = haptics,
                        modifier = Modifier.fillMaxWidth().staggerIn(2)
                    )
                    Spacer(Modifier.height(8.dp))
                }

                // Money cards only — no redundant third NEED% box
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        label = "NEEDS",
                        cents = stats.needsTotalCents,
                        symbol = symbol,
                        accent = palette.need,
                        pct = stats.needsPct,
                        modifier = Modifier.weight(1f).staggerIn(0)
                    )
                    StatCard(
                        label = "WANTS",
                        cents = stats.wantsTotalCents,
                        symbol = symbol,
                        accent = palette.want,
                        pct = stats.wantsPct,
                        modifier = Modifier.weight(1f).staggerIn(1)
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Bento row (design audit #8): spend-split card + streak card 2-up.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SplitSummaryCard(
                        needsPct = stats.needsPct,
                        wantsPct = stats.wantsPct,
                        needsCents = stats.needsTotalCents,
                        wantsCents = stats.wantsTotalCents,
                        symbol = symbol,
                        onOpenPortal = {
                            if (stats.totalCents > 0L) {
                                haptics.tick()
                                showSplitPortal = true
                            }
                        },
                        modifier = Modifier.weight(1.25f).staggerIn(2)
                    )
                    if (streakDays > 0) {
                        StreakBentoCard(
                            currentStreak = streakDays,
                            bestStreak = bestStreak,
                            modifier = Modifier.weight(0.75f).staggerIn(3)
                        )
                    }
                }
                } // end else (loaded content)
            }
        }

        if (showSplitPortal) {
            SplitPercentagePortal(
                needsPct = stats.needsPct,
                wantsPct = stats.wantsPct,
                needsCents = stats.needsTotalCents,
                wantsCents = stats.wantsTotalCents,
                totalCents = stats.totalCents,
                symbol = symbol,
                dailyTotals = stats.dailyTotals,
                periodLabel = getPeriodLabel(period, paid),
                onDismiss = { showSplitPortal = false }
            )
        }

        val topInsight = insights.firstOrNull()
        if (topInsight != null) {
            Spacer(Modifier.height(12.dp))
            InsightStrip(insight = topInsight, modifier = Modifier.staggerIn(4))
        }

        Spacer(Modifier.height(20.dp))

        GiltButton(
            onClick = onNavigateToInput,
            text = "Log a purchase",
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (currentMilestone != null) {
        MilestoneMarkDialog(
            milestone = currentMilestone,
            onDismiss = {
                viewModel.acknowledgeMilestone(currentMilestone!!)
                currentMilestone = null
            }
        )
    }
    }
}

@Composable
private fun LegendChip(color: Color, label: String, pct: Int) {
    val palette = AppTheme.colors
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(Modifier.size(8.dp)) { drawCircle(color) }
        Spacer(Modifier.width(6.dp))
        Text(
            "$label $pct%",
            style = AppType.meta,
            color = palette.textSecondary
        )
    }
}

/**
 * Full-width ledger strip: Need % and Want % of this period's spend.
 * Replaces the old third "NEED %" money card (which only repeated needsPct).
 */
@Composable
private fun SplitSummaryCard(
    needsPct: Int,
    wantsPct: Int,
    needsCents: Long,
    wantsCents: Long,
    symbol: String,
    onOpenPortal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    Column(
        modifier = modifier
            .paperSurface(
                rememberPaperSpec(PaperKind.RAISED, goldEdge = true),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onOpenPortal)
            .padding(scaledSpacing(16f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Eyebrow("SPEND SPLIT", color = palette.crimson, size = 11)
            Text(
                "Open detail",
                style = AppType.caption,
                color = palette.gold.copy(alpha = 0.9f)
            )
        }
        Spacer(Modifier.height(scaledSpacing(12f)))
        // Dual percentage readout
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                Text("Need", style = AppType.meta, color = palette.need)
                Text(
                    "$needsPct%",
                    style = AppType.moneyLg.copy(fontSize = 28.sp, color = palette.need)
                )
                Text(
                    needsCents.toMoney(symbol),
                    style = AppType.caption,
                    color = palette.textSecondary
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(56.dp)
                    .background(palette.inkDivider)
            )
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text("Want", style = AppType.meta, color = palette.want)
                Text(
                    "$wantsPct%",
                    style = AppType.moneyLg.copy(fontSize = 28.sp, color = palette.want)
                )
                Text(
                    wantsCents.toMoney(symbol),
                    style = AppType.caption,
                    color = palette.textSecondary
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        // Single bar: green left / crimson right share of 100%
        val n = (needsPct.coerceIn(0, 100)) / 100f
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
        ) {
            drawRect(color = palette.want, size = size)
            drawRect(color = palette.need, size = Size(size.width * n, size.height))
        }
    }
}

/**
 * Split percentage sheet — quiet paper land (D102).
 * Desk dim + sheet rise/fade only — no black-hole suck/vignette.
 */
@Composable
private fun SplitPercentagePortal(
    needsPct: Int,
    wantsPct: Int,
    needsCents: Long,
    wantsCents: Long,
    totalCents: Long,
    symbol: String,
    dailyTotals: List<DailySpend>,
    periodLabel: String,
    onDismiss: () -> Unit
) {
    val palette = AppTheme.colors
    val reveal = remember { Animatable(0f) }
    val scrim = remember { Animatable(0f) }

    // Predictive back (design audit #10): scale the sheet down with the system
    // back gesture so the user sees the Summary beneath, then dismiss on commit.
    var backScale by remember { mutableStateOf(1f) }
    var backAlpha by remember { mutableStateOf(1f) }
    PredictiveBackHandler(enabled = true) {
        it.collect { event ->
            backScale = 1f - (event.progress * 0.08f)
            backAlpha = 1f - (event.progress * 0.3f)
        }
        onDismiss()
    }

    LaunchedEffect(Unit) {
        if (!Motion.enabled) {
            scrim.snapTo(1f)
            reveal.snapTo(1f)
            return@LaunchedEffect
        }
        scrim.animateTo(1f, Motion.portalPulse())
        reveal.animateTo(1f, Motion.portalReveal())
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.42f * scrim.value))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            // Paper sheet with percentages
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .graphicsLayer {
                        val t = reveal.value
                        // Subtle land from 0.96 — not a 0.72 zoom pop.
                        scaleX = (Motion.RiseScale + (1f - Motion.RiseScale) * t) * backScale
                        scaleY = (Motion.RiseScale + (1f - Motion.RiseScale) * t) * backScale
                        alpha = t * backAlpha
                        translationY = (1f - t) * 18f
                    }
                    .paperSurface(
                        rememberPaperSpec(PaperKind.RAISED, goldEdge = true),
                        RoundedCornerShape(22.dp)
                    )
                    .clickable(enabled = false, onClick = {})
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Eyebrow(periodLabel.uppercase(), color = palette.crimson, size = 10)
                    HeaderIconWell(
                        onClick = onDismiss,
                        contentDescription = "Close split"
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            tint = palette.textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Your log split",
                    style = AppType.sectionTitle,
                    color = palette.textPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Share of spend this period",
                    style = AppType.bodyMd,
                    color = palette.textSecondary
                )
                Spacer(Modifier.height(18.dp))
                Text(
                    totalCents.toMoney(symbol),
                    style = AppType.moneyLg.copy(fontSize = 26.sp),
                    color = palette.textPrimary
                )
                Eyebrow("TOTAL", color = palette.textMuted, size = 10)
                Spacer(Modifier.height(20.dp))
                GiltRule(width = 40.dp)
                Spacer(Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("NEED", style = AppType.eyebrowSm, color = palette.need)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "$needsPct%",
                            style = AppType.moneyLg.copy(fontSize = 36.sp, color = palette.need)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            needsCents.toMoney(symbol),
                            style = AppType.bodyMd,
                            color = palette.textSecondary
                        )
                    }
                    Box(
                        Modifier
                            .width(1.dp)
                            .height(88.dp)
                            .background(palette.inkDivider)
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("WANT", style = AppType.eyebrowSm, color = palette.want)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "$wantsPct%",
                            style = AppType.moneyLg.copy(fontSize = 36.sp, color = palette.want)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            wantsCents.toMoney(symbol),
                            style = AppType.bodyMd,
                            color = palette.textSecondary
                        )
                    }
                }
                Spacer(Modifier.height(18.dp))
                val n = needsPct.coerceIn(0, 100) / 100f
                Canvas(
                    Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(7.dp))
                ) {
                    drawRect(color = palette.want, size = size)
                    drawRect(color = palette.need, size = Size(size.width * n, size.height))
                }
                // Day-of-week breakdown (design audit #7) — inside the split portal.
                if (dailyTotals.size >= 7) {
                    Spacer(Modifier.height(20.dp))
                    PremiumSurface(
                        shape = RoundedCornerShape(16.dp),
                        goldEdge = true,
                        raised = true
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Eyebrow("SPENDING BY DAY", color = palette.crimson)
                            Spacer(Modifier.height(12.dp))
                            DayOfWeekChart(dailyTotals = dailyTotals, symbol = symbol)
                        }
                    }
                }
                Spacer(Modifier.height(18.dp))
                GiltButton(
                    onClick = onDismiss,
                    text = "Back to Home",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    cents: Long,
    symbol: String,
    accent: Color,
    pct: Int,
    modifier: Modifier
) {
    val palette = AppTheme.colors
    val dividerColor = palette.inkDivider
    Column(
        modifier = modifier
            .paperSurface(
                rememberPaperSpec(PaperKind.CHIP, goldEdge = true),
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = scaledSpacing(12f), vertical = scaledSpacing(14f))
    ) {
        Eyebrow(label, color = palette.textMuted, size = 11)
        Spacer(Modifier.height(scaledSpacing(6f)))
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val moneyText = cents.toMoney(symbol)
            val size = fittingMoneySize(moneyText, 17.sp, maxWidth)
            AnimatedMoney(
                cents = cents,
                symbol = symbol,
                style = AppType.moneyMd.copy(
                    fontSize = size,
                    color = accent
                ),
                maxLines = 1,
                softWrap = false,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "$pct% of spend",
            style = AppType.caption,
            color = palette.textSecondary
        )
        Spacer(Modifier.height(8.dp))
        Canvas(Modifier.fillMaxWidth().height(3.dp)) {
            drawRect(color = dividerColor, size = Size(size.width, size.height))
            drawRect(color = accent, size = Size(size.width * pct / 100f, size.height))
        }
    }
}

/** Compact streak card for the bento row (design audit #8) — chip geometry matching StatCard. */
@Composable
private fun StreakBentoCard(
    currentStreak: Int,
    bestStreak: Int,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    val next = StreakMilestone.nextAfter(currentStreak)
    val secondary = when {
        next != null -> {
            val left = next.days - currentStreak
            val unit = if (left == 1) "day" else "days"
            "$left $unit to ${next.label}"
        }
        bestStreak > currentStreak -> "Best $bestStreak"
        else -> "Full cycle"
    }
    Column(
        modifier = modifier
            .paperSurface(
                rememberPaperSpec(PaperKind.CHIP, goldEdge = true),
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = scaledSpacing(12f), vertical = scaledSpacing(14f))
    ) {
        Eyebrow("STREAK", color = palette.gilt, size = 11)
        Spacer(Modifier.height(scaledSpacing(6f)))
        Text(
            if (currentStreak == 1) "Day 1" else "Day $currentStreak",
            style = AppType.moneyMd.copy(fontSize = 20.sp, color = palette.textPrimary),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(4.dp))
        Text(
            secondary,
            style = AppType.caption,
            color = palette.textSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun InstructionsOverlay(
    onDismiss: () -> Unit,
    paid: Boolean = false,
    onSelectGoal: (String) -> Unit = {}
) {
    var currentPage by remember { mutableIntStateOf(0) }
    var goal by remember { mutableStateOf("track") }
    val palette = AppTheme.colors
    val totalPages = 5
    // Steps 1..4 are the existing info cards; step 0 is the new goal selector (#9).
    val titles = listOf(
        "",
        "Every purchase is a Need or a Want",
        if (paid) "Your diary keeps every day" else "Your diary keeps 30 days",
        "Rows seal themselves",
        "Optional daily budget on Log"
    )
    val bodies = listOf(
        "",
        "Each entry forces a binary choice. There is no middle ground. That is the lesson.",
        if (paid) "Your history stays for life. Pro keeps the whole diary — no auto-removal." else "Older entries are removed automatically. The window is always 30 days.",
        "When item, cost, and type are filled, the row seals. Delete any row you sealed by mistake.",
        "Set a limit on Log. Watch spent vs remaining. Sealing past the line asks \"Log anyway?\" first."
    )
    val lastPage = totalPages - 1

    // Predictive back (design audit #10): scale the instructions sheet down with
    // the system back gesture, then dismiss on commit.
    var backScale by remember { mutableStateOf(1f) }
    var backAlpha by remember { mutableStateOf(1f) }
    PredictiveBackHandler(enabled = true) {
        it.collect { event ->
            backScale = 1f - (event.progress * 0.08f)
            backAlpha = 1f - (event.progress * 0.3f)
        }
        onDismiss()
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent,
            modifier = Modifier
                .paperSurface(
                    rememberPaperSpec(PaperKind.RAISED, goldEdge = true),
                    RoundedCornerShape(20.dp)
                )
                .graphicsLayer {
                    scaleX = backScale
                    scaleY = backScale
                    alpha = backAlpha
                }
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress bar — (currentPage+1)/totalPages (design audit #9).
                LinearProgressIndicator(
                    progress = { (currentPage + 1f) / totalPages },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = palette.gold,
                    trackColor = palette.surfaceRaised,
                    strokeCap = StrokeCap.Round
                )
                Spacer(Modifier.height(14.dp))
                Eyebrow("HOW IT WORKS", color = palette.crimson)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Step ${currentPage + 1} of $totalPages",
                    style = AppType.meta,
                    color = palette.textMuted
                )
                Spacer(Modifier.height(8.dp))
                GiltRule(width = 32.dp)
                Spacer(Modifier.height(14.dp))

                if (currentPage == 0) {
                    // Goal selector — progressive profiling (design audit #9).
                    OnboardingHandIllustration(modifier = Modifier.size(160.dp, 104.dp))
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "What's your spending goal?",
                        style = AppType.dialogTitle,
                        color = palette.textPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(14.dp))
                    GoalCard(
                        icon = { Icon(Icons.Default.Edit, contentDescription = null, tint = palette.crimson, modifier = Modifier.size(20.dp)) },
                        text = "Track every purchase",
                        selected = goal == "track",
                        onClick = { goal = "track" }
                    )
                    Spacer(Modifier.height(8.dp))
                    GoalCard(
                        icon = { Icon(Icons.Default.Savings, contentDescription = null, tint = palette.marketGreen, modifier = Modifier.size(20.dp)) },
                        text = "Stay under a daily budget",
                        selected = goal == "budget",
                        onClick = { goal = "budget" }
                    )
                    Spacer(Modifier.height(8.dp))
                    GoalCard(
                        icon = { Icon(Icons.Default.BarChart, contentDescription = null, tint = palette.gilt, modifier = Modifier.size(20.dp)) },
                        text = "See where my money goes",
                        selected = goal == "analyze",
                        onClick = { goal = "analyze" }
                    )
                } else {
                    AnimatedContent(
                        targetState = currentPage,
                        transitionSpec = {
                            fadeIn(Motion.state()) togetherWith fadeOut(Motion.state())
                        },
                        label = "instructionsPage"
                    ) { page ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                titles[page],
                                style = AppType.dialogTitle,
                                color = palette.textPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(14.dp))
                            Text(
                                bodies[page],
                                style = AppType.body,
                                color = palette.textSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.Center) {
                    repeat(totalPages) { i ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(if (i == currentPage) 8.dp else 6.dp)
                                .background(
                                    if (i == currentPage) palette.crimson else palette.divider,
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GhostTextAction(text = "Skip", onClick = onDismiss)
                    if (currentPage < lastPage) {
                        GhostTextAction(
                            text = "Next",
                            onClick = {
                                currentPage++
                                // Persist the goal as soon as the user leaves the selector.
                                if (currentPage == 1) onSelectGoal(goal)
                            }
                        )
                    } else {
                        GiltButton(
                            onClick = {
                                onSelectGoal(goal)
                                onDismiss()
                            },
                            text = "Begin",
                            height = 46.dp,
                            modifier = Modifier.widthIn(min = 120.dp)
                        )
                    }
                }
            }
        }
    }
}

/** Selectable goal card for the onboarding step-0 selector (design audit #9). */
@Composable
private fun GoalCard(
    icon: @Composable () -> Unit,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val palette = AppTheme.colors
    val edge = if (selected) palette.crimson else palette.gold.copy(alpha = 0.28f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .paperSurface(rememberPaperSpec(PaperKind.CHIP, goldEdge = selected), RoundedCornerShape(14.dp))
            .border(BorderStroke(1.5.dp, edge), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(contentAlignment = Alignment.Center) { icon() }
        Text(
            text,
            style = AppType.bodyMd.copy(color = if (selected) palette.textPrimary else palette.textSecondary),
            modifier = Modifier.weight(1f)
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(palette.crimson, CircleShape)
                    .padding(4.dp)
            ) {
                Box(Modifier.fillMaxSize().background(palette.surfaceCard, CircleShape))
            }
        }
    }
}

/** Period pills shown to free users (30-day trainer scope). */
private val freePeriods = listOf(Period.DAY, Period.WEEK, Period.ALL)

/** Period pills shown to Pro/Max (adds Month + Lifetime). */
private val paidPeriods = listOf(Period.DAY, Period.WEEK, Period.MONTH, Period.ALL)

private fun getPeriodLabel(period: Period, paid: Boolean): String = when (period) {
    Period.DAY -> "TODAY"
    Period.WEEK -> "THIS WEEK"
    Period.MONTH -> "THIS MONTH"
    Period.ALL -> if (paid) "ALL TIME" else "ALL 30 DAYS"
}

private fun getPeriodRange(period: Period, paid: Boolean): String {
    val fmt = SimpleDateFormat("MMM d", Locale.getDefault())
    val today = Calendar.getInstance()
    return when (period) {
        Period.DAY -> fmt.format(today.time)
        Period.WEEK -> {
            // Inclusive 7 calendar days — matches PeriodWindow.WEEK (today − 6).
            val start = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -6) }
            "${fmt.format(start.time)} - ${fmt.format(today.time)}"
        }
        Period.MONTH -> {
            // Inclusive 30 calendar days — matches PeriodWindow.MONTH (today − 29).
            val start = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -29) }
            "${fmt.format(start.time)} - ${fmt.format(today.time)}"
        }
        Period.ALL -> if (paid) {
            "Since your first entry"
        } else {
            val start = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -34) }
            "${fmt.format(start.time)} - ${fmt.format(today.time)}"
        }
    }
}

/** Period-aware empty donut copy so streak + empty Day don't contradict. */
private fun emptyPeriodCopy(
    period: Period,
    hasHistory: Boolean,
    streakDays: Int
): Pair<String, String> {
    if (!hasHistory && streakDays <= 0) {
        return "EMPTY DIARY" to "Log your first expense\nto start the diary."
    }
    return when (period) {
        Period.DAY -> "NOTHING TODAY" to "Seal a purchase to fill\ntoday's Need / Want split."
        Period.WEEK -> "QUIET WEEK" to "No spend in this week window yet.\nLog a purchase to start the split."
        Period.MONTH -> "QUIET MONTH" to "No spend in this month window yet.\nLog a purchase to fill the chart."
        Period.ALL -> "NO ENTRIES" to "Nothing in the active window.\nLog a purchase to fill the chart."
    }
}
