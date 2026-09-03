package com.needsvswants.app.ui.theme

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Paper-themed shimmer placeholder (design audit #2).
 *
 * Base fill is [AppPalette.surfaceRaised]; a [AppPalette.goldSoft] sweep travels
 * left→right on a 1200ms loop. Collapses to a static placeholder when
 * [Motion.enabled] is false (reduced motion).
 */
@Composable
fun PaperShimmer(
    modifier: Modifier = Modifier,
    shape: Shape = AppShapes.r16
) {
    val palette = AppTheme.colors
    if (!Motion.enabled) {
        Box(modifier = modifier.background(palette.surfaceRaised, shape))
        return
    }
    val transition = rememberInfiniteTransition(label = "paperShimmer")
    val offset by transition.animateFloat(
        initialValue = -0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(Motion.ShimmerMs, easing = Motion.EaseStandard),
            repeatMode = RepeatMode.Restart
        ),
        label = "paperShimmerOffset"
    )
    Box(
        modifier = modifier
            .background(palette.surfaceRaised, shape)
            .drawBehind {
                val w = size.width
                val sweep = w * 0.7f
                val centerX = w * offset
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            palette.goldSoft.copy(alpha = 0.25f),
                            Color.Transparent
                        ),
                        start = Offset(centerX - sweep / 2f, 0f),
                        end = Offset(centerX + sweep / 2f, 0f)
                    )
                )
            }
    )
}