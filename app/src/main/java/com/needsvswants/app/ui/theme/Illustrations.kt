package com.needsvswants.app.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Spot illustrations — hand-drawn Compose Canvas art (design audit #3).
 *
 * Each illustration is theme-aware (reads [AppTheme.colors]), fits a 160×120dp
 * bounding box, and animates in with [Motion.entrance]. No raster assets, no APK
 * bloat — the art is drawn from brand palette paths. Introduces emotional depth to
 * empty states, onboarding, and locked gates without an "AI-generated" look.
 */

/** Shared entrance: scale 0.96→1 + fade via [Motion.entrance], collapses when motion is off. */
@Composable
private fun illustrationEntrance(visible: Boolean = true): Float {
    val progress by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = Motion.entrance(),
        label = "illustrationEntrance"
    )
    return if (Motion.enabled) progress else 1f
}

/**
 * Open ledger book with faint ruled lines and a pencil resting on the blank page.
 * Used for History empty and Summary first-launch.
 */
@Composable
fun EmptyDiaryIllustration(modifier: Modifier = Modifier) {
    val c = AppTheme.colors
    val t = illustrationEntrance()
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // Book boards (two covers) opening like a ledger.
        val spineX = w * 0.5f
        val topY = h * 0.22f
        val bottomY = h * 0.82f
        val coverInset = w * 0.06f
        val leaf = Color(0xFFF3F1EA)

        // Shadow under the book.
        drawOval(
            color = c.textMuted.copy(alpha = 0.12f * t),
            topLeft = Offset(w * 0.14f, h * 0.86f),
            size = Size(w * 0.72f, h * 0.07f)
        )

        // Left cover.
        drawRoundRect(
            color = c.marketGreen.copy(alpha = 0.16f * t),
            topLeft = Offset(coverInset, topY),
            size = Size(w * 0.5f - coverInset, bottomY - topY),
            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )
        // Right cover.
        drawRoundRect(
            color = c.crimson.copy(alpha = 0.14f * t),
            topLeft = Offset(w * 0.5f, topY),
            size = Size(w * 0.5f - coverInset, bottomY - topY),
            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )

        // Pages (open book) — warm paper.
        val pageTop = topY + h * 0.05f
        val pageBottom = bottomY - h * 0.05f
        drawRoundRect(
            color = leaf,
            topLeft = Offset(spineX - w * 0.34f, pageTop),
            size = Size(w * 0.68f, pageBottom - pageTop),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        // Spine groove.
        drawLine(
            color = c.divider.copy(alpha = 0.6f * t),
            start = Offset(spineX, pageTop),
            end = Offset(spineX, pageBottom),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Faint ruled lines on each page.
        val ruleColor = c.divider.copy(alpha = 0.5f * t)
        val lineY = pageTop + h * 0.16f
        val lineSpacing = h * 0.11f
        var li = 0
        while (lineY + li * lineSpacing < pageBottom - 4.dp.toPx()) {
            val y = lineY + li * lineSpacing
            drawLine(
                color = ruleColor,
                start = Offset(spineX - w * 0.28f, y),
                end = Offset(spineX - 4.dp.toPx(), y),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = ruleColor,
                start = Offset(spineX + 4.dp.toPx(), y),
                end = Offset(spineX + w * 0.28f, y),
                strokeWidth = 1.dp.toPx()
            )
            li++
        }

        // Pencil resting diagonally on the right page.
        val px = spineX + w * 0.16f
        val py = pageBottom - h * 0.06f
        val pencilLen = w * 0.30f
        val angle = -0.5f
        val dx = cos(angle) * pencilLen
        val dy = sin(angle) * pencilLen
        // Body.
        drawLine(
            color = c.gold.copy(alpha = 0.9f * t),
            start = Offset(px, py),
            end = Offset(px + dx, py + dy),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
        // Tip.
        drawLine(
            color = c.marketGreen.copy(alpha = 0.9f * t),
            start = Offset(px + dx, py + dy),
            end = Offset(px + dx + dx * 0.12f, py + dy + dy * 0.12f),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

/**
 * Closed book with a gold lock clasp — the Advisor Max gate.
 */
@Composable
fun LockedBookIllustration(modifier: Modifier = Modifier) {
    val c = AppTheme.colors
    val t = illustrationEntrance()
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.5f
        val topY = h * 0.24f
        val bottomY = h * 0.78f

        // Book body (closed) — crimson cover.
        val cover = c.crimson.copy(alpha = 0.16f * t)
        drawRoundRect(
            color = cover,
            topLeft = Offset(w * 0.18f, topY),
            size = Size(w * 0.64f, bottomY - topY),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )
        // Spine.
        drawRoundRect(
            color = c.crimson.copy(alpha = 0.85f * t),
            topLeft = Offset(w * 0.18f, topY),
            size = Size(w * 0.10f, bottomY - topY),
            cornerRadius = CornerRadius(4.dp.toPx(), 8.dp.toPx())
        )
        // Page edges (right side).
        drawRoundRect(
            color = c.surfaceCard,
            topLeft = Offset(w * 0.78f, topY + h * 0.02f),
            size = Size(w * 0.05f, (bottomY - topY) - h * 0.04f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )

        // Gold lock clasp over the cover's leading edge.
        val lockX = cx - w * 0.05f
        val lockY = cy
        drawRoundRect(
            color = c.gold.copy(alpha = 0.9f * t),
            topLeft = Offset(lockX, lockY - h * 0.05f),
            size = Size(w * 0.10f, h * 0.10f),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
        // Shackle arc.
        drawArc(
            color = c.gold.copy(alpha = 0.9f * t),
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(lockX + w * 0.012f, lockY - h * 0.10f),
            size = Size(w * 0.076f, w * 0.076f),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
        // Keyhole.
        drawCircle(
            color = c.crimsonDeep.copy(alpha = 0.9f * t),
            radius = 2.5.dp.toPx(),
            center = Offset(lockX + w * 0.05f, lockY)
        )
    }
}

/**
 * A hand holding a pencil writing in a ledger — onboarding step 1.
 */
@Composable
fun OnboardingHandIllustration(modifier: Modifier = Modifier) {
    val c = AppTheme.colors
    val t = illustrationEntrance()
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Ledger page (open notebook) lower-left.
        val pageTop = h * 0.52f
        val pageBottom = h * 0.88f
        drawRoundRect(
            color = c.surfaceCard,
            topLeft = Offset(w * 0.10f, pageTop),
            size = Size(w * 0.80f, pageBottom - pageTop),
            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )
        drawRoundRect(
            color = c.gold.copy(alpha = 0.3f * t),
            topLeft = Offset(w * 0.10f, pageTop),
            size = Size(w * 0.80f, pageBottom - pageTop),
            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
            style = Stroke(width = 1.5.dp.toPx())
        )
        // Ruled lines on the page.
        val ruleColor = c.divider.copy(alpha = 0.5f * t)
        var li = 0
        val lineStart = pageTop + h * 0.10f
        val lineSpacing = h * 0.08f
        while (lineStart + li * lineSpacing < pageBottom - 4.dp.toPx()) {
            val y = lineStart + li * lineSpacing
            drawLine(
                color = ruleColor,
                start = Offset(w * 0.16f, y),
                end = Offset(w * 0.84f, y),
                strokeWidth = 1.dp.toPx()
            )
            li++
        }

        // Pencil held diagonally — writing onto the page.
        val baseX = w * 0.58f
        val baseY = pageTop - h * 0.02f
        val angle = 0.6f
        val len = w * 0.34f
        val dx = cos(angle) * len
        val dy = sin(angle) * len
        drawLine(
            color = c.gold.copy(alpha = 0.9f * t),
            start = Offset(baseX, baseY),
            end = Offset(baseX + dx, baseY + dy),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = c.marketGreen.copy(alpha = 0.9f * t),
            start = Offset(baseX + dx, baseY + dy),
            end = Offset(baseX + dx + dx * 0.10f, baseY + dy + dy * 0.10f),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // A short in-progress line of "ink" on the page under the pencil tip.
        drawLine(
            color = c.textPrimary.copy(alpha = 0.7f * t),
            start = Offset(w * 0.30f, pageTop + h * 0.18f),
            end = Offset(w * 0.60f, pageTop + h * 0.18f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}