package com.needsvswants.app.ui.screens.summary

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.Period
import com.needsvswants.app.domain.StreakMilestone
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@Composable
fun SummaryScreen(
    onNavigateToInput: () -> Unit = {},
    viewModel: SummaryViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val period by viewModel.period.collectAsStateWithLifecycle()
    val symbol by viewModel.currencySymbol.collectAsStateWithLifecycle()
    val budgetStatus by viewModel.budgetStatus.collectAsStateWithLifecycle()
    val streakDays by viewModel.streakDays.collectAsStateWithLifecycle()
    val bestStreak by viewModel.bestStreak.collectAsStateWithLifecycle()
    val insights by viewModel.insights.collectAsStateWithLifecycle()
    val hasHistory by viewModel.hasHistory.collectAsStateWithLifecycle()
    val isFirstLaunch by viewModel.isFirstLaunch.collectAsStateWithLifecycle()
    var showInstructions by remember { mutableStateOf(false) }
    var currentMilestone by remember { mutableStateOf<StreakMilestone?>(null) }
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
            onDismiss = {
                showInstructions = false
                if (isFirstLaunch) viewModel.dismissFirstLaunch()
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(themedInkWash())
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
                Eyebrow("A 35-Day Trainer", color = palette.crimson, maxLines = 1)
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
            color = palette.surfaceCard,
            border = BorderStroke(1.dp, palette.gold.copy(alpha = 0.28f)),
            shadowElevation = 2.dp,
            tonalElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Period.entries.forEach { p ->
                    val selected = p == period
                    val label = when (p) {
                        Period.DAY -> "Day"
                        Period.WEEK -> "Week"
                        Period.ALL -> "All (35d)"
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
                            .height(40.dp)
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
                Text(
                    getPeriodLabel(animatedPeriod),
                    color = palette.crimson,
                    style = AppType.meta.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1
                )
                Text(
                    getPeriodRange(animatedPeriod),
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

                // Hero donut with gilt glow backdrop
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (stats.totalCents == 0L) {
                        val ringBreath = rememberIdleBreathAlpha()
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(180.dp)
                                    .drawBehind {
                                        drawCircle(
                                            brush = Brush.radialGradient(
                                                colors = listOf(palette.gilt.copy(alpha = 0.18f), Color.Transparent),
                                                radius = size.minDimension
                                            )
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(
                                    modifier = Modifier
                                        .size(150.dp)
                                        .alpha(ringBreath)
                                ) {
                                    drawArc(
                                        color = palette.inkDivider,
                                        startAngle = 0f,
                                        sweepAngle = 360f,
                                        useCenter = false,
                                        style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round),
                                        size = Size(size.width, size.height)
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
                    } else {
                        val total = stats.totalCents.toFloat().coerceAtLeast(1f)
                        val targetSweep = (stats.needsTotalCents / total) * 360f
                        val needsSweep by animateFloatAsState(
                            targetValue = targetSweep,
                            animationSpec = Motion.entrance(),
                            label = "donutNeedsSweep"
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(200.dp)
                                    .drawBehind {
                                        drawCircle(
                                            brush = Brush.radialGradient(
                                                colors = listOf(palette.gilt.copy(alpha = 0.16f), Color.Transparent),
                                                radius = size.minDimension / 1.2f
                                            )
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(Modifier.size(180.dp)) {
                                    val ring = 22.dp.toPx()
                                    val stroke = Stroke(width = ring, cap = StrokeCap.Round)
                                    drawArc(
                                        color = palette.inkDivider,
                                        startAngle = 0f,
                                        sweepAngle = 360f,
                                        useCenter = false,
                                        style = Stroke(width = ring),
                                        size = Size(size.width, size.height)
                                    )
                                    drawArc(
                                        color = palette.need,
                                        startAngle = -90f,
                                        sweepAngle = needsSweep,
                                        useCenter = false,
                                        style = stroke,
                                        size = Size(size.width, size.height)
                                    )
                                    drawArc(
                                        color = palette.want,
                                        startAngle = -90f + needsSweep,
                                        sweepAngle = 360f - needsSweep,
                                        useCenter = false,
                                        style = stroke,
                                        size = Size(size.width, size.height)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Eyebrow("TOTAL", color = palette.textMuted, size = 10)
                                    Spacer(Modifier.height(2.dp))
                                    AnimatedMoney(
                                        cents = stats.totalCents,
                                        symbol = symbol,
                                        style = AppType.moneyLg.copy(
                                            fontSize = adaptiveMoneySize(
                                                stats.totalCents.toMoney(symbol),
                                                22.sp
                                            ),
                                            color = palette.textPrimary
                                        ),
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                            Spacer(Modifier.height(14.dp))
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

                // Stat cards
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
                    StatCard(
                        label = "NEED %",
                        value = "${stats.needsPct}%",
                        accent = palette.gilt,
                        pct = stats.needsPct,
                        modifier = Modifier.weight(1f).staggerIn(2)
                    )
                }
            }
        }

        // Quiet streak + one insight — never above the donut (outside period fade)
        if (streakDays > 0) {
            Spacer(Modifier.height(16.dp))
            StreakLine(
                currentStreak = streakDays,
                bestStreak = bestStreak,
                modifier = Modifier.fillMaxWidth().staggerIn(3)
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

@Composable
private fun StatCard(
    label: String,
    value: String? = null,
    cents: Long? = null,
    symbol: String = "",
    accent: Color,
    pct: Int,
    modifier: Modifier
) {
    val palette = AppTheme.colors
    val dividerColor = palette.inkDivider
    Column(
        modifier = modifier
            .background(palette.surfaceCard, RoundedCornerShape(16.dp))
            .border(1.dp, palette.gold.copy(alpha = 0.28f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Eyebrow(label, color = palette.textMuted, size = 9)
        Spacer(Modifier.height(6.dp))
        if (cents != null && symbol.isNotEmpty()) {
            AnimatedMoney(
                cents = cents,
                symbol = symbol,
                style = AppType.moneyMd.copy(
                    fontSize = adaptiveMoneySize(cents.toMoney(symbol), 15.sp),
                    color = accent
                ),
                maxLines = 1,
                softWrap = false
            )
        } else {
            Text(
                value ?: "",
                color = accent,
                style = AppType.moneyMd.copy(fontSize = adaptiveMoneySize(value ?: "", 15.sp)),
                maxLines = 1,
                softWrap = false
            )
        }
        Spacer(Modifier.height(8.dp))
        Canvas(Modifier.fillMaxWidth().height(3.dp)) {
            drawRect(color = dividerColor, size = Size(size.width, size.height))
            drawRect(color = accent, size = Size(size.width * pct / 100f, size.height))
        }
    }
}

@Composable
fun InstructionsOverlay(onDismiss: () -> Unit) {
    var currentPage by remember { mutableIntStateOf(0) }
    val palette = AppTheme.colors
    val titles = listOf(
        "Every purchase is a Need or a Want",
        "Your diary keeps 35 days",
        "Rows seal themselves",
        "Optional daily budget on Log"
    )
    val bodies = listOf(
        "Each entry forces a binary choice. There is no middle ground. That is the lesson.",
        "Older entries are removed automatically. The window is always 35 days.",
        "When item, cost, and type are filled, the row seals. Delete any row you sealed by mistake.",
        "Set a limit on Log. Watch spent vs remaining. Sealing past the line asks \"Log anyway?\" first."
    )
    val lastPage = titles.lastIndex

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = palette.surfaceCard,
            border = BorderStroke(1.dp, palette.gold.copy(alpha = 0.28f)),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Eyebrow("HOW IT WORKS", color = palette.crimson)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Step ${currentPage + 1} of ${titles.size}",
                    style = AppType.meta,
                    color = palette.textMuted
                )
                Spacer(Modifier.height(8.dp))
                GiltRule(width = 32.dp)
                Spacer(Modifier.height(14.dp))
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
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.Center) {
                    repeat(titles.size) { i ->
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
                            onClick = { currentPage++ }
                        )
                    } else {
                        GiltButton(
                            onClick = onDismiss,
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

private fun getPeriodLabel(period: Period): String = when (period) {
    Period.DAY -> "TODAY"
    Period.WEEK -> "THIS WEEK"
    Period.ALL -> "ALL 35 DAYS"
}

private fun getPeriodRange(period: Period): String {
    val fmt = SimpleDateFormat("MMM d", Locale.getDefault())
    val today = Calendar.getInstance()
    return when (period) {
        Period.DAY -> fmt.format(today.time)
        Period.WEEK -> {
            // Inclusive 7 calendar days — matches PeriodWindow.WEEK (today − 6).
            val start = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -6) }
            "${fmt.format(start.time)} - ${fmt.format(today.time)}"
        }
        Period.ALL -> {
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
        Period.ALL -> "NO ENTRIES" to "Nothing in the active window.\nLog a purchase to fill the chart."
    }
}
