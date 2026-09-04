package com.needsvswants.app.ui.theme

import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
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
 * Tactile recoil modifier — drops to 0.94 on touch down, springs past 1.0 with slight
 * micro-overshoot on release before settling at 1.0 via [Motion.recoilSpring].
 */
fun Modifier.pressRecoil(
    interaction: MutableInteractionSource,
    enabled: Boolean = true
): Modifier = composed {
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.94f else 1f,
        animationSpec = if (pressed) Motion.pressSpring() else Motion.recoilSpring(),
        label = "pressRecoil"
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

/**
 * Splits formatted money text into its leading currency prefix and numeric part
 * for balanced optical centering on hero dials.
 */
fun splitMoneyPrefix(text: String, symbol: String): Pair<String, String>? {
    if (symbol.isEmpty() || !text.startsWith(symbol)) return null
    val spacePrefix = "$symbol "
    return if (text.startsWith(spacePrefix)) {
        spacePrefix to text.removePrefix(spacePrefix)
    } else {
        symbol to text.removePrefix(symbol)
    }
}

/**
 * Animated Odometer Money count-up with independent vertical digit roll animation.
 *
 * Digits roll vertically into place using [Motion.odometer], while formatting
 * through [toMoney] or [toMoneyWhole] with [symbol]. Each roll carries a brief
 * velocity motion blur (API 31+; a no-op below Android S and under reduced motion).
 */
@Composable
fun AnimatedOdometerMoney(
    cents: Long,
    symbol: String,
    style: TextStyle = AppType.moneyLg,
    color: Color? = null,
    wholeOnly: Boolean = false,
    opticalCentering: Boolean = false,
    modifier: Modifier = Modifier
) {
    val text = if (wholeOnly) cents.toMoneyWhole(symbol) else cents.toMoney(symbol)
    val resolvedColor = color ?: Color.Unspecified
    val split = if (opticalCentering) splitMoneyPrefix(text, symbol) else null
    val rowModifier = modifier.semantics(mergeDescendants = true) {
        contentDescription = text
    }
    if (!Motion.enabled) {
        if (split != null) {
            val (prefix, numberPart) = split
            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = prefix,
                    style = style,
                    color = resolvedColor
                )
                Text(
                    text = numberPart,
                    style = style,
                    color = resolvedColor
                )
                Text(
                    text = prefix,
                    style = style,
                    color = Color.Transparent,
                    modifier = Modifier.clearAndSetSemantics { }
                )
            }
        } else {
            Text(
                text = text,
                modifier = modifier,
                style = style,
                color = resolvedColor
            )
        }
        return
    }

    if (split != null) {
        val (prefix, numberPart) = split
        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = prefix,
                style = style,
                color = resolvedColor
            )
            numberPart.forEach { char ->
                if (char.isDigit()) {
                    RollingDigit(char = char, style = style, color = color)
                } else {
                    Text(
                        text = char.toString(),
                        style = style,
                        color = resolvedColor
                    )
                }
            }
            Text(
                text = prefix,
                style = style,
                color = Color.Transparent,
                modifier = Modifier.clearAndSetSemantics { }
            )
        }
    } else {
        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            text.forEach { char ->
                if (char.isDigit()) {
                    RollingDigit(char = char, style = style, color = color)
                } else {
                    Text(
                        text = char.toString(),
                        style = style,
                        color = resolvedColor
                    )
                }
            }
        }
    }
}

@Composable
private fun RollingDigit(
    char: Char,
    style: TextStyle,
    color: Color?,
) {
    AnimatedContent(
        targetState = char,
        contentAlignment = Alignment.Center,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInVertically(animationSpec = Motion.odometer()) { height -> height } + fadeIn(Motion.odometer()))
                    .togetherWith(slideOutVertically(animationSpec = Motion.odometer()) { height -> -height } + fadeOut(Motion.odometer()))
            } else {
                (slideInVertically(animationSpec = Motion.odometer()) { height -> -height } + fadeIn(Motion.odometer()))
                    .togetherWith(slideOutVertically(animationSpec = Motion.odometer()) { height -> height } + fadeOut(Motion.odometer()))
            }
        },
        label = "odometerDigit"
    ) { digit ->
        // AnimatedContent gives each target digit its own remember scope, so a
        // newly rolled-in digit starts blurred and settles on its own. The old
        // previous/SideEffect pair wrote state during composition, which reset
        // this Animatable a frame in and meant the blur never actually rendered.
        val blur = remember { Animatable(1f) }
        LaunchedEffect(Unit) { blur.animateTo(0f, Motion.odometer()) }
        Text(
            text = digit.toString(),
            style = style,
            color = color ?: Color.Unspecified,
            modifier = Modifier.graphicsLayer {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && blur.value > 0.05f) {
                    val radius = 3.dp.toPx() * blur.value
                    renderEffect = runCatching { BlurEffect(radius, radius * 0.6f) }.getOrNull()
                } else {
                    renderEffect = null
                }
            }
        )
    }
}

/**
 * Pure sheet-open phase of the two-stage origami unfold: rotateX in degrees for
 * a slip hinged at its top edge. Progress 0..[Motion.UnfoldSheetPortion] unfolds
 * the sheet; the remainder of the progress window belongs to the seal landing.
 */
fun unfoldSheetRotationX(progress: Float): Float =
    -(1f - (progress / Motion.UnfoldSheetPortion).coerceIn(0f, 1f)) * Motion.UnfoldMaxDegrees

/** Pure: the sheet-opening share of the unfold progress (0..1). */
fun unfoldSheetProgress(progress: Float): Float =
    (progress / Motion.UnfoldSheetPortion).coerceIn(0f, 1f)

/** Pure: the seal-landing share of the unfold progress (0..1, stays 0 while the sheet opens). */
fun unfoldSealProgress(progress: Float): Float =
    ((progress - Motion.UnfoldSheetPortion) / (1f - Motion.UnfoldSheetPortion)).coerceIn(0f, 1f)

/**
 * Origami unfold modifier for paper slips hinged at the top edge: the sheet
 * swings down from edge-on ([Motion.UnfoldMaxDegrees]) to flat while fading in,
 * then settles with a short drop in the final tenth of [progress].
 */
fun Modifier.origamiUnfold(progress: Float, offsetYDp: Float = 6f): Modifier =
    graphicsLayer {
        if (progress >= 1f) return@graphicsLayer
        rotationX = unfoldSheetRotationX(progress)
        alpha = (progress / 0.25f).coerceIn(0f, 1f)
        transformOrigin = TransformOrigin(0.5f, 0f)
        cameraDistance = Motion.PageFlipCameraDistance * density
        if (progress > 0.9f) {
            translationY = (1f - (progress - 0.9f) / 0.1f) * offsetYDp.dp.toPx()
        }
    }
