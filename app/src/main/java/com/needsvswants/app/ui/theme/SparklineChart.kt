package com.needsvswants.app.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * Hand-rolled Compose Canvas sparkline (design audit #5) with touch scrubbing
 * (#6) and a haptic tick per data point (#14). Draws a smooth cubic Bézier path
 * through the daily totals, fills beneath it, and shows a crosshair + tooltip
 * while the user drags along the series. Respects [Motion.enabled].
 *
 * @param labels per-point hover labels (e.g. "Mon" or "Jul 5"); empty to disable scrubbing UI.
 * @param tooltip provider for the scrub tooltip text (date + amount).
 */
@Composable
fun SparklineChart(
    data: List<Long>,
    accentColor: Color,
    modifier: Modifier = Modifier,
    fillAlpha: Float = 0.12f,
    strokeWidth: Dp = 2.dp,
    labels: List<String> = emptyList(),
    tooltip: (Int) -> String = { "" },
    haptics: AppHaptics? = null
) {
    val palette = AppTheme.colors
    val safety = data.ifEmpty { listOf(0L) }
    var scrubIndex by remember { mutableIntStateOf(-1) }
    val interactive = labels.isNotEmpty()

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .pointerInput(safety, interactive) {
                    if (!interactive) return@pointerInput
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            scrubIndex = nearestIndex(safety.size, offset.x, size.width.toFloat())
                            haptics?.tick()
                        },
                        onHorizontalDrag = { change, _ ->
                            val newIndex = nearestIndex(safety.size, change.position.x, size.width.toFloat())
                            if (newIndex != scrubIndex && newIndex in safety.indices) {
                                scrubIndex = newIndex
                                haptics?.tick()
                            }
                        },
                        onDragEnd = { scrubIndex = -1 },
                        onDragCancel = { scrubIndex = -1 }
                    )
                }
        ) {
            val w = size.width
            val h = size.height
            val max = safety.maxOrNull()?.coerceAtLeast(1L) ?: 1L
            val n = safety.size
            val stepX = if (n > 1) w / (n - 1) else 0f

            fun xFor(i: Int): Float = if (n > 1) i * stepX else w / 2f
            fun yFor(value: Long): Float = h - (value.toFloat() / max) * (h - 10.dp.toPx()) - 4.dp.toPx()

            // Fill under the curve.
            val fillPath = Path().apply {
                moveTo(xFor(0), h)
                for (i in safety.indices) lineTo(xFor(i), yFor(safety[i]))
                lineTo(xFor(safety.lastIndex), h)
                close()
            }
            drawPath(fillPath, color = accentColor.copy(alpha = fillAlpha))

            // Smooth cubic Bézier stroke through the points.
            val strokePath = Path()
            for (i in safety.indices) {
                val x = xFor(i)
                val y = yFor(safety[i])
                if (i == 0) {
                    strokePath.moveTo(x, y)
                } else {
                    val prevX = xFor(i - 1)
                    val prevY = yFor(safety[i - 1])
                    val mid = prevX + (x - prevX) * 0.5f
                    strokePath.cubicTo(mid, prevY, mid, y, x, y)
                }
            }
            drawPath(strokePath, color = accentColor, style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round))

            // First + last point dots (skipped when reduced motion).
            if (Motion.enabled) {
                drawCircle(accentColor, radius = 4.dp.toPx(), center = Offset(xFor(0), yFor(safety[0])))
                drawCircle(accentColor, radius = 4.dp.toPx(), center = Offset(xFor(safety.lastIndex), yFor(safety.last())))
            }

            // Scrub crosshair + highlighted point.
            if (scrubIndex in safety.indices) {
                val cx = xFor(scrubIndex)
                drawLine(
                    color = palette.gold,
                    start = Offset(cx, 0f),
                    end = Offset(cx, h),
                    strokeWidth = 1.5.dp.toPx()
                )
                val py = yFor(safety[scrubIndex])
                drawCircle(color = palette.surfaceCard, radius = 7.dp.toPx(), center = Offset(cx, py))
                drawCircle(color = accentColor, radius = 4.dp.toPx(), center = Offset(cx, py))
            }
        }

        // Floating tooltip above the chart while scrubbing.
        if (scrubIndex in labels.indices) {
            androidx.compose.material3.Text(
                text = tooltip(scrubIndex),
                style = AppType.meta.copy(fontSize = 11.sp),
                color = palette.textPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

/** Nearest data index to an x position (clamped 0..count-1). */
private fun nearestIndex(count: Int, x: Float, width: Float): Int {
    if (count <= 1) return 0
    val frac = (x / width).coerceIn(0f, 1f)
    return (frac * (count - 1)).roundToInt()
}