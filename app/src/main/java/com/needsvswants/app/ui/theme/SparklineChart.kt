package com.needsvswants.app.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * Hand-rolled Compose Canvas sparkline (design audit #5) with touch scrubbing
 * (#6) and a haptic tick per data point (#14). Draws a smooth cubic Bézier path
 * through the daily totals and fills beneath it.
 *
 * D195 loupe scrubber: dragging raises a circular magnifier tethered to the
 * snapped point by an elastic Bézier (the loupe lags on [Motion.spatialSpring]),
 * with a crisp primitive tick as the snap lands on each ledger date. The flat
 * tooltip text lives inside the loupe now. Respects [Motion.enabled].
 *
 * @param labels per-point hover labels (e.g. "Mon" or "Jul 5"); empty to disable scrubbing UI.
 * @param tooltip provider for the loupe value text (date + amount).
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
    val density = LocalDensity.current
    val safety = data.ifEmpty { listOf(0L) }
    var scrubIndex by remember { mutableIntStateOf(-1) }
    var chartWidthPx by remember { mutableIntStateOf(0) }
    val interactive = labels.isNotEmpty()
    val loupeX = remember { Animatable(0f) }
    var loupeFresh by remember { mutableStateOf(false) }

    LaunchedEffect(scrubIndex, chartWidthPx) {
        if (scrubIndex in safety.indices && chartWidthPx > 0) {
            val target = xForIndex(scrubIndex, safety.size, chartWidthPx.toFloat())
            if (!loupeFresh) {
                loupeX.snapTo(target)
                loupeFresh = true
            } else if (Motion.enabled) {
                loupeX.animateTo(target, Motion.spatialSpring())
            } else {
                loupeX.snapTo(target)
            }
        } else {
            loupeFresh = false
        }
    }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .onSizeChanged { chartWidthPx = it.width }
                .pointerInput(safety, interactive) {
                    if (!interactive) return@pointerInput
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            loupeFresh = false
                            scrubIndex = nearestIndex(safety.size, offset.x, size.width.toFloat())
                            haptics?.primitiveTick(0.55f)
                        },
                        onHorizontalDrag = { change, _ ->
                            val newIndex = nearestIndex(safety.size, change.position.x, size.width.toFloat())
                            if (newIndex != scrubIndex && newIndex in safety.indices) {
                                scrubIndex = newIndex
                                haptics?.primitiveTick(0.55f)
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

        // Loupe + elastic tether while scrubbing (D195).
        if (scrubIndex in safety.indices && chartWidthPx > 0) {
            val loupeR = with(density) { 24.dp.toPx() }
            Canvas(Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val max = safety.maxOrNull()?.coerceAtLeast(1L) ?: 1L
                val n = safety.size
                val stepX = if (n > 1) w / (n - 1) else 0f
                val cx = if (n > 1) scrubIndex * stepX else w / 2f
                val py = h - (safety[scrubIndex].toFloat() / max) * (h - 10.dp.toPx()) - 4.dp.toPx()
                val lx = loupeX.value.coerceIn(loupeR, w - loupeR)
                val ly = h / 2f

                // Elastic Bézier tether from the loupe rim to the snapped point.
                val tether = Path().apply {
                    moveTo(lx + loupeR * 0.68f, ly + loupeR * 0.68f)
                    quadraticBezierTo(
                        (lx + cx) / 2f,
                        (ly + py) / 2f + 12f,
                        cx,
                        py
                    )
                }
                drawPath(tether, color = palette.gold.copy(alpha = 0.85f), style = Stroke(width = 1.5.dp.toPx()))

                // Loupe body: cast shadow, paper lens, gold ring.
                drawCircle(
                    Color(0xFF1A1A1A).copy(alpha = 0.10f),
                    radius = loupeR + 1.dp.toPx(),
                    center = Offset(lx, ly + 2.dp.toPx())
                )
                drawCircle(palette.surfaceCard.copy(alpha = 0.97f), radius = loupeR, center = Offset(lx, ly))
                drawCircle(
                    palette.gold.copy(alpha = 0.8f),
                    radius = loupeR,
                    center = Offset(lx, ly),
                    style = Stroke(width = 1.5.dp.toPx())
                )
                // Magnified point above the value text.
                drawCircle(accentColor, radius = 6.dp.toPx(), center = Offset(lx, ly - 6.dp.toPx()))
                drawCircle(palette.surfaceCard, radius = 2.dp.toPx(), center = Offset(lx, ly - 6.dp.toPx()))
            }
            Text(
                text = tooltip(scrubIndex),
                style = AppType.meta.copy(fontSize = 10.sp),
                color = palette.textPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .offset {
                        val ly = with(density) { 30.dp.toPx() }
                        IntOffset(
                            (loupeX.value - with(density) { 24.dp.toPx() }).roundToInt(),
                            (ly + with(density) { 2.dp.toPx() }).roundToInt()
                        )
                    }
                    .width(48.dp)
            )
        }
    }
}

/** X position for a data index (clamped 0..count-1). */
private fun xForIndex(i: Int, count: Int, width: Float): Float =
    if (count <= 1) width / 2f else (i.coerceIn(0, count - 1)) * (width / (count - 1))

/** Nearest data index to an x position (clamped 0..count-1). */
private fun nearestIndex(count: Int, x: Float, width: Float): Int {
    if (count <= 1) return 0
    val frac = (x / width).coerceIn(0f, 1f)
    return (frac * (count - 1)).roundToInt()
}
