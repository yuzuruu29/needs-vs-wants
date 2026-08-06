package com.needsvswants.app.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.offset
import com.needsvswants.app.domain.toMoney

/**
 * Reusable motion primitives built on [Motion] tokens.
 *
 * Each primitive consumes only Motion specs — no magic numbers.
 * Used across screens for press feedback, entrance transitions, and
 * numeric count-ups ([AnimatedMoney]).
 */

/**
 * Press-scale modifier — wraps the GiltButton-style press feedback pattern.
 * Scales content to 0.97 while pressed, springs back via [Motion.pressSpring].
 *
 * Usage: `Modifier.pressScale(interactionSource, enabled)`
 */
fun Modifier.pressScale(
    interaction: MutableInteractionSource,
    enabled: Boolean = true
): Modifier = composed {
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.97f else 1f,
        animationSpec = Motion.pressSpring(),
        label = "pressScale"
    )
    this.scale(scale)
}

/**
 * Fade-slide entrance primitive.
 * Fades content in (alpha 0→1) while sliding it up from [offsetY] to 0,
 * driven by [Motion.entrance].
 *
 * Best used for period/stats transitions where content should rise into place.
 *
 * @param visible when false, content is fully faded and offset; when true, fully shown
 * @param offsetY starting vertical offset in dp (default 12.dp)
 */
fun Modifier.fadeSlideEnter(
    visible: Boolean,
    offsetY: Float = 12f
): Modifier = composed {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = Motion.entrance(),
        label = "fadeSlideEnterAlpha"
    )
    val offset by animateFloatAsState(
        targetValue = if (visible) 0f else offsetY,
        animationSpec = Motion.entrance(),
        label = "fadeSlideEnterOffset"
    )
    this
        .alpha(alpha)
        .offset(y = offset.dp)
}

/**
 * Animated money count-up.
 *
 * Animates the displayed amount from 0 to [cents] using [Motion.number],
 * formatting the raw value through [toMoney] with [symbol].
 *
 * Durations collapse to ~1ms when [Motion] is disabled (reduced motion).
 *
 * @param cents target amount in cents
 * @param symbol currency symbol (e.g. "₱")
 * @param style text style — defaults to [AppType.moneyLg]
 */
@Composable
fun AnimatedMoney(
    cents: Long,
    symbol: String,
    style: TextStyle = AppType.moneyLg
) {
    val target = cents.toFloat()
    val animated by animateFloatAsState(
        targetValue = target,
        animationSpec = Motion.number(),
        label = "animatedMoney"
    )
    Text(
        text = animated.toLong().toMoney(symbol),
        style = style
    )
}
