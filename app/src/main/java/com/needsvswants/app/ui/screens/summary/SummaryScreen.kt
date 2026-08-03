package com.needsvswants.app.ui.screens.summary

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.domain.BudgetStatus
import com.needsvswants.app.domain.Period
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SummaryScreen(
    onNavigateToInput: () -> Unit = {},
    viewModel: SummaryViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val period by viewModel.period.collectAsStateWithLifecycle()
    val symbol by viewModel.currencySymbol.collectAsStateWithLifecycle()
    val budgetStatus by viewModel.budgetStatus.collectAsStateWithLifecycle()
    var showInstructions by remember { mutableStateOf(false) }
    val palette = AppTheme.colors

    if (showInstructions) {
        InstructionsOverlay(onDismiss = { showInstructions = false })
    }

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
            Column {
                Eyebrow("A 35-Day Trainer", color = palette.crimson)
                Spacer(Modifier.height(6.dp))
                Text(
                    "NEEDS\nvs WANTS",
                    style = MaterialTheme.typography.displayLarge.copy(lineHeight = 36.sp),
                    color = palette.textPrimary
                )
                Spacer(Modifier.height(8.dp))
                GiltRule(width = 40.dp)
                Spacer(Modifier.height(6.dp))
                Text("Expense Tracker", color = palette.crimson, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Text(getPeriodLabel(period), color = palette.crimson, style = MaterialTheme.typography.labelMedium)
                Text(getPeriodRange(period), color = palette.textMuted, style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, letterSpacing = 0.6.sp))
            }
            IconButton(onClick = { showInstructions = true }, modifier = Modifier.size(36.dp)) {
                Icon(Icons.AutoMirrored.Filled.HelpOutline, "Help", tint = palette.crimson, modifier = Modifier.size(20.dp))
            }
        }

        Spacer(Modifier.height(28.dp))

        // Period selector — pill bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(palette.surfaceCard, RoundedCornerShape(12.dp))
                .border(1.dp, palette.divider, RoundedCornerShape(12.dp))
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
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .then(
                            if (selected) Modifier
                                .background(Brush.horizontalGradient(listOf(palette.crimson, palette.crimsonDeep)), RoundedCornerShape(8.dp))
                            else Modifier
                                .clickable { viewModel.setPeriod(p) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (!selected) Box(modifier = Modifier.matchParentSize().clickable { viewModel.setPeriod(p) })
                    Text(
                        label,
                        color = if (selected) palette.surfaceCard else palette.textSecondary,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // Daily budget meter — Day period only, when budget is on
        if (period == Period.DAY && budgetStatus is BudgetStatus.On) {
            DailyBudgetMeter(status = budgetStatus as BudgetStatus.On, symbol = symbol)
            Spacer(Modifier.height(20.dp))
        }

        // Hero donut with gilt glow backdrop
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (stats.totalCents == 0L) {
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
                        Canvas(Modifier.size(150.dp)) {
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
                    Eyebrow("EMPTY DIARY", color = palette.textMuted)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Log your first expense\nto start the diary.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
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
                            val total = stats.totalCents.toFloat().coerceAtLeast(1f)
                            val needsSweep = (stats.needsTotalCents / total) * 360f
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
                            Text(
                                stats.totalCents.toMoney(symbol),
                                style = MaterialTheme.typography.headlineMedium,
                                color = palette.textPrimary,
                                fontWeight = FontWeight.Bold,
                                softWrap = false,
                                maxLines = 1,
                                fontSize = adaptiveMoneySize(stats.totalCents.toMoney(symbol), 22.sp)
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

        Spacer(Modifier.height(28.dp))

        // Stat cards
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("NEEDS", stats.needsTotalCents.toMoney(symbol), palette.need, stats.needsPct, Modifier.weight(1f))
            StatCard("WANTS", stats.wantsTotalCents.toMoney(symbol), palette.want, stats.wantsPct, Modifier.weight(1f))
            StatCard("NEED %", "${stats.needsPct}%", palette.gilt, stats.needsPct, Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        GiltButton(
            onClick = onNavigateToInput,
            text = "Log an expense",
            modifier = Modifier.fillMaxWidth()
        )
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
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, letterSpacing = 0.5.sp),
            color = palette.textSecondary
        )
    }
}

@Composable
private fun StatCard(label: String, value: String, accent: Color, pct: Int, modifier: Modifier) {
    val palette = AppTheme.colors
    val dividerColor = palette.inkDivider
    Column(
        modifier = modifier
            .background(palette.inkElevated, RoundedCornerShape(16.dp))
            .border(1.dp, palette.inkDivider, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Eyebrow(label, color = palette.textMuted, size = 9)
        Spacer(Modifier.height(6.dp))
        Text(
            value,
            color = accent,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            softWrap = false,
            fontSize = adaptiveMoneySize(value, 15.sp)
        )
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
        "Every expense is a Need or a Want",
        "Your diary keeps 35 days",
        "Rows seal themselves",
        "Optional daily budget on Log"
    )
    val bodies = listOf(
        "Each entry forces a binary choice. There is no middle ground. This is the lesson.",
        "Older entries are automatically removed. The window is always 35 days.",
        "When you fill in item, cost, and type, the row saves instantly. Hold a sealed row to unseal it.",
        "Set a limit on the Log screen — watch spent vs remaining, change or turn it off anytime. Sealing past the line asks “Log anyway?” first."
    )
    val lastPage = titles.lastIndex

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = palette.inkElevated,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(20.dp),
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Eyebrow("WELCOME", color = palette.crimson)
                Spacer(Modifier.height(8.dp))
                GiltRule(width = 32.dp)
                Spacer(Modifier.height(12.dp))
                Text(titles[currentPage], style = MaterialTheme.typography.headlineMedium, color = palette.textPrimary, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(bodies[currentPage], style = MaterialTheme.typography.bodyLarge, color = palette.textSecondary, textAlign = TextAlign.Center)
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.Center) {
                    repeat(titles.size) { i ->
                        Box(modifier = Modifier.padding(4.dp).size(6.dp).background(if (i == currentPage) palette.crimson else palette.divider, RoundedCornerShape(3.dp)))
                    }
                }
            }
        },
        confirmButton = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onDismiss) { Text("Skip", color = palette.textMuted) }
                if (currentPage < lastPage) {
                    TextButton(onClick = { currentPage++ }) { Text("Next", color = palette.crimson, fontWeight = FontWeight.SemiBold) }
                } else {
                    TextButton(onClick = onDismiss) { Text("Begin", color = palette.crimson, fontWeight = FontWeight.Bold) }
                }
            }
        }
    )
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
            val start = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -6) }
            "${fmt.format(start.time)} — ${fmt.format(today.time)}"
        }
        Period.ALL -> {
            val start = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -34) }
            "${fmt.format(start.time)} — ${fmt.format(today.time)}"
        }
    }
}
