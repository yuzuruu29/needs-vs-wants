package com.needsvswants.app.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Home ledger spend ring — matte paper dial with Need/Want ink arcs.
 *
 * D102: clean butt-capped junctions (no round-cap blobs), theme-aware edge
 * hairline (no hard white), matched track/arc weight. Motion is caller-driven
 * arc sweep only — no idle float/shine (paper desk, not a toy orb).
 *
 * Optional [onClick] opens the percentage portal (Summary).
 */
@Composable
fun FloatingGeminiOrb(
    needsSweepDegrees: Float,
    empty: Boolean,
    modifier: Modifier = Modifier,
    orbSize: Dp = 200.dp,
    onClick: (() -> Unit)? = null,
    center: @Composable BoxScope.() -> Unit
) {
    val palette = AppTheme.colors
    val sfx = rememberAppSfx()

    Box(
        modifier = modifier
            .size(orbSize)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        role = Role.Button,
                        onClick = {
                            sfx.orb()
                            onClick()
                        },
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val min = size.minDimension
            val c = Offset(size.width / 2f, size.height / 2f)
            val outerR = min / 2f
            val coreR = outerR * 0.74f
            val ringW = outerR * 0.11f
            val hair = 1.1f.dp.toPx().coerceAtLeast(1f)

            // 1) Soft desk contact shadow (static, tight)
            val shadowCenter = Offset(c.x, c.y + coreR * 0.48f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1A1A1A).copy(alpha = 0.10f),
                        Color.Transparent
                    ),
                    center = shadowCenter,
                    radius = coreR * 0.88f
                ),
                radius = coreR * 0.82f,
                center = shadowCenter
            )

            // 2) Matte paper disk — theme stock
            val paperTop = palette.surfaceCard
            val paperMid = palette.surfaceRaised
            val paperEdge = palette.surfaceSunken
            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.0f to paperTop,
                        0.62f to paperMid,
                        1.0f to paperEdge
                    ),
                    center = Offset(c.x - coreR * 0.10f, c.y - coreR * 0.16f),
                    radius = coreR * 1.12f
                ),
                radius = coreR,
                center = c
            )

            // Quiet ink rim (paper edge, not chrome)
            drawCircle(
                color = palette.textPrimary.copy(alpha = 0.08f),
                radius = coreR,
                center = c,
                style = Stroke(width = hair)
            )

            // 3) Track + Need/Want ink arcs (same stroke weight, butt caps)
            val trackR = (outerR + coreR) / 2f
            val ringInset = outerR - trackR - ringW / 2f
            val ringTopLeft = Offset(ringInset, ringInset)
            val ringSize = Size(min - ringInset * 2f, min - ringInset * 2f)

            drawArc(
                color = palette.inkDivider.copy(alpha = 0.90f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = ringW, cap = StrokeCap.Butt),
                topLeft = ringTopLeft,
                size = ringSize
            )

            if (!empty) {
                val needSweep = needsSweepDegrees.coerceIn(0f, 360f)
                val wantSweep = (360f - needSweep).coerceAtLeast(0f)

                // Draw Want first so Need sits cleanly on the 12-o'clock joint.
                if (wantSweep > 0.4f) {
                    drawInkArc(
                        color = palette.want,
                        highlight = paperTop,
                        startAngle = -90f + needSweep,
                        sweep = wantSweep,
                        ringW = ringW,
                        topLeft = ringTopLeft,
                        arcSize = ringSize
                    )
                }
                if (needSweep > 0.4f) {
                    drawInkArc(
                        color = palette.need,
                        highlight = paperTop,
                        startAngle = -90f,
                        sweep = needSweep,
                        ringW = ringW,
                        topLeft = ringTopLeft,
                        arcSize = ringSize
                    )
                }
            } else {
                // Resting gold tick — short, dim, static
                drawInkArc(
                    color = palette.gold.copy(alpha = 0.85f),
                    highlight = paperTop,
                    startAngle = -90f,
                    sweep = 36f,
                    ringW = ringW * 0.72f,
                    topLeft = ringTopLeft,
                    arcSize = ringSize
                )
            }

            // 4) Outer gold ledger trim (hairline only)
            drawCircle(
                color = palette.gold.copy(alpha = 0.32f),
                radius = coreR + hair * 0.5f,
                center = c,
                style = Stroke(width = hair)
            )
        }

        // Center hole for TOTAL + amount — slightly roomier so whole-unit
        // money can sit on the optical center of the paper disk.
        Box(
            modifier = Modifier.size(orbSize * 0.56f),
            contentAlignment = Alignment.Center,
            content = center
        )
    }
}

/**
 * Solid classification arc. Butt caps avoid bright blobs where Need meets Want.
 * Optional inner hairline uses paper tone (theme-aware), never pure white.
 */
private fun DrawScope.drawInkArc(
    color: Color,
    highlight: Color,
    startAngle: Float,
    sweep: Float,
    ringW: Float,
    topLeft: Offset,
    arcSize: Size
) {
    if (sweep <= 0.4f) return
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        style = Stroke(width = ringW, cap = StrokeCap.Butt),
        topLeft = topLeft,
        size = arcSize
    )
    // Soft paper-edge sheen down the arc centerline — quiet, no glass blob.
    val sheenW = (ringW * 0.18f).coerceAtMost(2.2f.dp.toPx())
    if (sheenW > 0.6f) {
        drawArc(
            color = highlight.copy(alpha = 0.28f),
            startAngle = startAngle,
            sweepAngle = sweep,
            useCenter = false,
            style = Stroke(width = sheenW, cap = StrokeCap.Butt),
            topLeft = topLeft,
            size = arcSize
        )
    }
}
