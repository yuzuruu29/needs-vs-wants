package com.needsvswants.app.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

/**
 * Ruled costing-ledger paper craft — the surface system that replaces glass.
 *
 * Paper is near-opaque ink-on-sheet: fill, optional horizontal rules, biro border,
 * soft desk shadow, left spine crease. No specular cores, no gold glow halos,
 * no translucent plastic fills.
 *
 * Layer order (bottom → top):
 *   1. desk shadow (elevation only)
 *   2. clip to shape
 *   3. fill + rules + margin + crease + optional grain + optional receipt teeth
 *   4. ink / gold hairline border
 *   5. caller content
 */

/** Which paper treatment a surface wants. */
enum class PaperKind { RAISED, FLAT, CHIP, FIELD, RECEIPT }

/** Immutable recipe a [paperSurface] uses to composite its sheet. */
data class PaperSpec(
    val fill: Color,
    val ruleColor: Color,
    val ruleAlpha: Float,
    val ruleSpacingDp: Float,
    val marginRuleColor: Color?,
    val marginRuleXFrac: Float,
    val inkBorder: Color,
    val inkBorderWidth: Dp,
    val deskShadow: Color,
    val elevation: Dp,
    val creaseAlpha: Float,
    val grainAlpha: Float,
    val serration: Boolean,
)

/**
 * Composites a paper ledger surface.
 *
 * @param shape clip + border shape (rounded rects for cards; use sharp for receipt later)
 * @param showMarginRule when true and [PaperSpec.marginRuleColor] non-null, draws vertical margin
 */
fun Modifier.paperSurface(
    spec: PaperSpec,
    shape: Shape,
    showMarginRule: Boolean = false,
    clip: Boolean = true,
): Modifier {
    val base = if (spec.elevation > 0.dp) {
        this.shadow(
            elevation = spec.elevation,
            shape = shape,
            clip = clip,
            ambientColor = spec.deskShadow,
            spotColor = spec.deskShadow,
        )
    } else {
        this
    }
    return base
        .then(if (clip) Modifier.clip(shape) else Modifier)
        .drawBehind {
            // a) Opaque sheet fill
            drawRect(spec.fill, size = size)

            // b) Horizontal ledger rules
            if (spec.ruleAlpha > 0.001f && spec.ruleSpacingDp > 0f) {
                val step = spec.ruleSpacingDp * density
                if (step > 0.5f) {
                    val lineColor = spec.ruleColor.copy(alpha = spec.ruleAlpha)
                    val stroke = 1f.coerceAtLeast(density * 0.6f)
                    var y = step
                    while (y < size.height - step * 0.25f) {
                        drawLine(
                            color = lineColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = stroke,
                        )
                        y += step
                    }
                }
            }

            // c) Optional vertical margin rule (classic notebook)
            if (showMarginRule) {
                spec.marginRuleColor?.let { mc ->
                    val x = size.width * spec.marginRuleXFrac.coerceIn(0.02f, 0.25f)
                    drawLine(
                        color = mc,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 1f.coerceAtLeast(density * 0.7f),
                    )
                }
            }

            // d) Left spine crease (RAISED / RECEIPT)
            if (spec.creaseAlpha > 0.001f) {
                val edge = (size.width * 0.08f).coerceAtLeast(10f * density)
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = spec.creaseAlpha),
                            Color.Black.copy(alpha = spec.creaseAlpha * 0.25f),
                            Color.Transparent,
                        ),
                        startX = 0f,
                        endX = edge,
                    ),
                    size = size,
                )
            }

            // e) Static grain — few fixed dots, never animated
            if (spec.grainAlpha > 0.001f) {
                val g = Color.Black.copy(alpha = spec.grainAlpha)
                val seeds = listOf(
                    0.12f to 0.18f, 0.41f to 0.33f, 0.73f to 0.22f,
                    0.28f to 0.61f, 0.55f to 0.72f, 0.88f to 0.48f,
                    0.19f to 0.84f, 0.66f to 0.11f, 0.91f to 0.79f,
                    0.07f to 0.45f, 0.48f to 0.91f, 0.82f to 0.63f,
                )
                val r = 0.9f * density
                for ((fx, fy) in seeds) {
                    drawCircle(
                        color = g,
                        radius = r,
                        center = Offset(size.width * fx, size.height * fy),
                    )
                }
            }

            // f) Receipt serration — top edge teeth (visual only; clip is rounded)
            if (spec.serration) {
                val toothW = 8f * density
                val toothH = 4f * density
                val path = Path()
                path.moveTo(0f, toothH)
                var x = 0f
                val teeth = ceil(size.width / toothW).toInt().coerceAtLeast(1)
                for (i in 0 until teeth) {
                    val mid = x + toothW * 0.5f
                    val end = (x + toothW).coerceAtMost(size.width)
                    path.lineTo(mid.coerceAtMost(size.width), 0f)
                    path.lineTo(end, toothH)
                    x += toothW
                }
                path.lineTo(size.width, toothH)
                path.lineTo(size.width, 0f)
                path.lineTo(0f, 0f)
                path.close()
                // Cover top band with desk-matching cut: use fill as “torn off” by inverting
                // via semi-transparent notch — draw teeth as ink outline instead for clarity.
                drawPath(
                    path = path,
                    color = spec.fill,
                )
                // Subtle tooth outline
                var ox = 0f
                val ink = spec.inkBorder.copy(alpha = 0.35f)
                for (i in 0 until teeth) {
                    val mid = ox + toothW * 0.5f
                    val end = (ox + toothW).coerceAtMost(size.width)
                    drawLine(ink, Offset(ox, toothH), Offset(mid.coerceAtMost(size.width), 0f), 1f)
                    drawLine(ink, Offset(mid.coerceAtMost(size.width), 0f), Offset(end, toothH), 1f)
                    ox += toothW
                }
            }
        }
        .border(BorderStroke(spec.inkBorderWidth, spec.inkBorder), shape)
}

/**
 * Single authoritative per-theme paper recipe.
 * High-contrast: solid stock, strong rules/borders, no grain.
 */
@Composable
fun rememberPaperSpec(kind: PaperKind, goldEdge: Boolean = true): PaperSpec {
    val c = AppTheme.colors
    val light = c.isLightStatusBars
    val hc = c.glassFillAlpha > 0.95f // reuse HC pin until paperOpacity lands

    if (hc) {
        return PaperSpec(
            fill = c.surfaceCard,
            ruleColor = Color.Black,
            ruleAlpha = when (kind) {
                PaperKind.CHIP, PaperKind.RECEIPT -> 0f
                else -> 0.18f
            },
            ruleSpacingDp = 8f,
            marginRuleColor = null,
            marginRuleXFrac = 0.08f,
            inkBorder = if (goldEdge) c.gold.copy(alpha = 0.55f) else c.dividerStrong,
            inkBorderWidth = 1.dp,
            deskShadow = Color.Black.copy(alpha = 0.22f),
            elevation = when (kind) {
                PaperKind.RAISED, PaperKind.RECEIPT -> 4.dp
                PaperKind.CHIP -> 1.dp
                else -> 0.dp
            },
            creaseAlpha = if (kind == PaperKind.RAISED || kind == PaperKind.RECEIPT) 0.10f else 0f,
            grainAlpha = 0f,
            serration = kind == PaperKind.RECEIPT,
        )
    }

    val receiptFill = if (light) Color(0xFFF7F4EC) else Color(0xFF2A241C)
    val fill = when (kind) {
        PaperKind.FLAT -> c.surfaceSunken
        PaperKind.RECEIPT -> receiptFill
        else -> c.surfaceCard
    }

    return PaperSpec(
        fill = fill,
        ruleColor = c.textPrimary,
        ruleAlpha = when (kind) {
            PaperKind.RAISED -> if (light) 0.06f else 0.10f
            PaperKind.FLAT -> if (light) 0.05f else 0.08f
            PaperKind.FIELD -> if (light) 0.04f else 0.07f
            PaperKind.CHIP, PaperKind.RECEIPT -> 0f
        },
        ruleSpacingDp = 8f,
        marginRuleColor = c.crimson.copy(alpha = if (light) 0.22f else 0.30f),
        marginRuleXFrac = 0.08f,
        inkBorder = when {
            goldEdge -> c.gold.copy(alpha = if (kind == PaperKind.RAISED) {
                if (light) 0.38f else 0.42f
            } else {
                if (light) 0.28f else 0.32f
            })
            else -> c.divider.copy(alpha = 0.90f)
        },
        inkBorderWidth = 1.dp,
        deskShadow = Color.Black.copy(alpha = if (light) 0.14f else 0.32f),
        elevation = when (kind) {
            PaperKind.RAISED -> if (light) 4.dp else 3.dp
            PaperKind.RECEIPT -> 3.dp
            PaperKind.CHIP -> 1.dp
            PaperKind.FIELD -> 0.dp
            PaperKind.FLAT -> 0.dp
        },
        creaseAlpha = when (kind) {
            PaperKind.RAISED -> if (light) 0.07f else 0.10f
            PaperKind.RECEIPT -> 0.06f
            else -> 0f
        },
        grainAlpha = when (kind) {
            PaperKind.RAISED, PaperKind.RECEIPT -> if (light) 0.025f else 0.035f
            PaperKind.FIELD -> if (light) 0.015f else 0.02f
            else -> 0f
        },
        serration = kind == PaperKind.RECEIPT,
    )
}
