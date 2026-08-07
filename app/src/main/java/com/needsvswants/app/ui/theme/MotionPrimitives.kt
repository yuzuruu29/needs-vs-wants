package com.needsvswants.app.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.needsvswants.app.domain.toMoney
import com.needsvswants.app.domain.toMoneyWhole
import kotlinx.coroutines.launch

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
    val density = LocalDensity.current
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
        .offset { IntOffset(0, with(density) { offset.dp.roundToPx() }) }
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
 * @param color optional text color
 * @param maxLines max lines for the money text
 * @param softWrap whether the money text may soft-wrap
 */
@Composable
fun AnimatedMoney(
    cents: Long,
    symbol: String,
    style: TextStyle = AppType.moneyLg,
    color: Color? = null,
    maxLines: Int = Int.MAX_VALUE,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    /** When true, formats via [toMoneyWhole] (no decimals) for hero/orb totals. */
    wholeOnly: Boolean = false,
    textAlign: TextAlign? = null,
    modifier: Modifier = Modifier
) {
    val target = cents.toFloat()
    val animated by animateFloatAsState(
        targetValue = target,
        animationSpec = Motion.number(),
        label = "animatedMoney"
    )
    val amount = animated.toLong()
    Text(
        text = if (wholeOnly) amount.toMoneyWhole(symbol) else amount.toMoney(symbol),
        modifier = modifier,
        style = style,
        color = color ?: Color.Unspecified,
        maxLines = maxLines,
        softWrap = softWrap,
        overflow = overflow,
        textAlign = textAlign
    )
}

/**
 * Entrance stagger — content fades in and rises from [offsetY] while later
 * siblings wait their [staggerDelay] turn. Transform-only (alpha + translationY).
 *
 * Replays when the host content re-enters composition (e.g. inside an
 * AnimatedContent period switch), which is intended.
 *
 * @param index stagger slot; delay = [staggerDelay](index)
 * @param offsetY starting vertical offset in dp (default 12.dp)
 */
@Composable
fun Modifier.staggerIn(index: Int, offsetY: Float = 12f): Modifier = composed {
    val offsetPx = with(LocalDensity.current) { offsetY.dp.toPx() }
    val fadeAlpha = remember { Animatable(if (Motion.enabled) 0f else 1f) }
    val slideY = remember { Animatable(if (Motion.enabled) offsetPx else 0f) }
    LaunchedEffect(Unit) {
        if (Motion.enabled) {
            val spec = Motion.entranceStagger<Float>(staggerDelay(index))
            launch { fadeAlpha.animateTo(1f, spec) }
            launch { slideY.animateTo(0f, spec) }
        }
    }
    this.graphicsLayer {
        alpha = fadeAlpha.value
        translationY = slideY.value
    }
}

/**
 * Idle breath alpha for hero rings — slow 0.7→1 pulse on [Motion.IdleMs].
 * Returns 1f when motion is disabled so reduced motion shows a steady ring.
 */
@Composable
fun rememberIdleBreathAlpha(): Float {
    // Gate the transition itself: when motion is disabled (or never observed),
    // skip creating the infinite animation entirely — no off-screen recompose.
    // Motion.enabled is set once at the theme root, so this conditional call
    // order is stable across recompositions.
    if (!Motion.enabled) return 1f
    val transition = rememberInfiniteTransition(label = "idleBreath")
    val alpha by transition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(Motion.IdleMs, easing = Motion.EaseStandard),
            repeatMode = RepeatMode.Reverse
        ),
        label = "idleBreathAlpha"
    )
    return alpha
}
