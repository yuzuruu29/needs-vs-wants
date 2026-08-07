package com.needsvswants.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.absoluteValue

/**
 * Relative page offset for a pager page (classic foundation formula):
 * `0` = fully settled as current, positive = page sits to the left of the
 * viewport (leaving when going forward), negative = page sits to the right
 * (entering when going forward).
 */
fun pagerPageOffset(pagerState: PagerState, page: Int): Float {
    return (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
}

/**
 * Notebook-style paper page-turn for main-tab sheets (D102: fewer shade layers).
 *
 * While [Motion.enabled]:
 * - Leaves pivot on the **trailing** edge
 * - Soft depth scale + camera distance
 * - Single spine crease + light face shade + gold free-edge hairline
 *   (no cast/floor/shadowElevation soup — those lagged and muddied the leaf)
 *
 * Reduced motion: plain opaque sheet, no 3D.
 */
fun Modifier.paperPageFlip(
    pageOffset: Float,
    paperColor: Color,
    spineInk: Color = Color(0xFF1A1A1A),
    goldEdge: Color = Color(0xFFE8A92A),
): Modifier {
    if (!Motion.enabled) {
        return this
            .graphicsLayer {
                rotationY = 0f
                scaleX = 1f
                scaleY = 1f
                cameraDistance = 8f * density
                transformOrigin = TransformOrigin.Center
                alpha = 1f
                shadowElevation = 0f
            }
            .background(paperColor)
    }

    val clamped = pageOffset.coerceIn(-1.15f, 1.15f)
    val abs = clamped.absoluteValue.coerceAtMost(1f)
    val pivotX = when {
        clamped > 0.001f -> 1f
        clamped < -0.001f -> 0f
        else -> 0.5f
    }
    val rotation = clamped * Motion.PageFlipMaxDegrees
    val depth = 1f - abs * Motion.PageFlipDepthScale
    // Tiny peel lift — keep it under 3px so it never jitters vertical scroll.
    val liftY = -abs * 2.5f

    return this
        .graphicsLayer {
            cameraDistance = Motion.PageFlipCameraDistance * density
            transformOrigin = TransformOrigin(pivotX, 0.5f)
            rotationY = rotation
            scaleX = depth
            scaleY = depth
            translationY = liftY
            alpha = 1f
            clip = true
            // Elevation shadows lag flings — rely on drawn face shade instead.
            shadowElevation = 0f
        }
        .background(paperColor)
        .drawWithContent {
            drawContent()

            if (abs < 0.02f) return@drawWithContent

            val w = size.width
            val h = size.height
            val edge = (w * 0.11f).coerceAtLeast(14f)
            val t = abs // ease overlays with offset

            // Spine crease only (hinged edge).
            val hingeLeft = pivotX < 0.5f
            val creaseBrush = if (hingeLeft) {
                Brush.horizontalGradient(
                    colors = listOf(
                        spineInk.copy(alpha = 0.12f * t),
                        spineInk.copy(alpha = 0.04f * t),
                        Color.Transparent
                    ),
                    startX = 0f,
                    endX = edge
                )
            } else {
                Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        spineInk.copy(alpha = 0.04f * t),
                        spineInk.copy(alpha = 0.12f * t)
                    ),
                    startX = w - edge,
                    endX = w
                )
            }
            drawRect(brush = creaseBrush, size = size)

            // Soft face shade as the leaf turns (self-shadow, gentle).
            val faceShade = (t * t) * 0.08f
            if (faceShade > 0.008f) {
                drawRect(color = Color.Black.copy(alpha = faceShade), size = size)
            }

            // Gold hairline on the free edge.
            val freeLeft = !hingeLeft
            val hair = 1f.coerceAtLeast(density)
            val goldA = 0.28f * t
            if (freeLeft) {
                drawRect(
                    color = goldEdge.copy(alpha = goldA),
                    topLeft = Offset(w - hair, 0f),
                    size = Size(hair, h)
                )
            } else {
                drawRect(
                    color = goldEdge.copy(alpha = goldA),
                    topLeft = Offset(0f, 0f),
                    size = Size(hair, h)
                )
            }
        }
}

/**
 * Full-bleed paper sheet host for a pager page: flip transform + opaque wash.
 */
@Composable
fun PaperPagerPage(
    pageOffset: Float,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val c = AppTheme.colors
    Box(
        modifier = modifier
            .fillMaxSize()
            .paperPageFlip(
                pageOffset = pageOffset,
                paperColor = c.background,
                spineInk = c.textPrimary,
                goldEdge = c.gold
            ),
        content = content
    )
}
