package com.needsvswants.app.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.needsvswants.app.domain.DailySpend
import com.needsvswants.app.domain.toMoney
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Horizontal bar chart of spending by day of week (design audit #7). Seven rows
 * (Mon-Sun), each bar split into Need (green) + Want (crimson) segments. Bars
 * draw in with [Motion.inkDraw]. Lives inside the SplitPercentagePortal.
 *
 * D195: vertical drag scrub highlights the row under the finger with a gold
 * underline and a crisp tick per row snap. Lives only while dragging.
 */
@Composable
fun DayOfWeekChart(
    dailyTotals: List<DailySpend>,
    symbol: String,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    val haptics = rememberAppHaptics()
    val rowPitch = with(LocalDensity.current) { 20.dp.toPx() }
    var scrubbedRow by remember { mutableIntStateOf(-1) }
    val fmt = SimpleDateFormat("EEE", Locale.getDefault())
    // Map each DailySpend's date -> weekday total, aggregated across the period.
    val byWeekday = mutableMapOf<Int, Pair<Long, Long>>() // dayOfWeek(1=Sun..7) -> (needs, wants)
    for (d in dailyTotals) {
        val cal = Calendar.getInstance().let {
            it.time = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(d.date) ?: java.util.Date()
            it
        }
        val dow = cal.get(Calendar.DAY_OF_WEEK)
        val (n, w) = byWeekday.getOrDefault(dow, 0L to 0L)
        byWeekday[dow] = (n + d.needsCents) to (w + d.wantsCents)
    }

    // Order Mon..Sun (Calendar.MONDAY=2 ... SUNDAY=1).
    val order = listOf(2, 3, 4, 5, 6, 7, 1) // Mon..Sun
    val maxTotal = order.mapNotNull { byWeekday[it] }.maxOfOrNull { it.first + it.second }?.coerceAtLeast(1L) ?: 1L

    Column(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(byWeekday.size) {
                detectVerticalDragGestures(
                    onDragStart = { off ->
                        scrubbedRow = (off.y / rowPitch).toInt().coerceIn(0, 6)
                        haptics.primitiveTick(0.5f)
                    },
                    onVerticalDrag = { change, _ ->
                        val newRow = (change.position.y / rowPitch).toInt().coerceIn(0, 6)
                        if (newRow != scrubbedRow) {
                            scrubbedRow = newRow
                            haptics.primitiveTick(0.5f)
                        }
                    },
                    onDragEnd = { scrubbedRow = -1 },
                    onDragCancel = { scrubbedRow = -1 }
                )
            },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val labelCal = Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) }
        order.forEachIndexed { rowIdx, dow ->
            val (needs, wants) = byWeekday[dow] ?: (0L to 0L)
            val total = needs + wants
            labelCal.set(Calendar.DAY_OF_WEEK, dow)
            val label = fmt.format(labelCal.time)
            val highlighted = scrubbedRow == rowIdx
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = AppType.caption,
                    color = if (highlighted) palette.textPrimary else palette.textMuted,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.width(40.dp)
                )
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .height(12.dp)
                ) {
                    val frac = total.toFloat() / maxTotal
                    val needFrac = if (total > 0) needs.toFloat() / total else 0f
                    val barW = size.width * frac
                    val barH = size.height
                    val radius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx())
                    drawRoundRect(
                        color = palette.surfaceRaised,
                        size = Size(size.width, barH),
                        cornerRadius = radius
                    )
                    if (barW > 0f) {
                        drawRoundRect(
                            color = palette.need,
                            size = Size(barW * needFrac, barH),
                            cornerRadius = radius
                        )
                        drawRoundRect(
                            color = palette.want,
                            size = Size(barW * (1f - needFrac), barH),
                            topLeft = androidx.compose.ui.geometry.Offset(barW * needFrac, 0f),
                            cornerRadius = radius
                        )
                    }
                    if (highlighted) {
                        drawRect(
                            color = palette.gold.copy(alpha = 0.9f),
                            topLeft = Offset(0f, barH - 1.5f.dp.toPx()),
                            size = Size(barW.coerceAtLeast(4.dp.toPx()), 1.5f.dp.toPx())
                        )
                    }
                }
                Text(
                    text = total.toMoney(symbol),
                    style = AppType.caption,
                    color = if (highlighted) palette.textPrimary else palette.textSecondary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(72.dp)
                )
            }
        }
    }
}