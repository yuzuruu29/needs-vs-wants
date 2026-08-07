package com.needsvswants.app.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

/**
 * Quiet paper-ink press indication (D102 polish).
 *
 * One soft radial wash + optional gold hairline — no lagged multi-crest
 * splash. Tuned for app-wide [LocalIndication] use on chips, pills, and rows
 * without reading as a Material bubble or game ripple.
 *
 * Reduced motion ([Motion.enabled] == false) collapses every step to ~1ms.
 */
class InkWaveIndication(
    private val ink: Color,
    private val inkAlpha: Float,
    private val ring: Color,
    private val ringAlpha: Float,
) : IndicationNodeFactory {

    override fun create(interactionSource: InteractionSource): DelegatableNode =
        InkWaveNode(interactionSource, ink, inkAlpha, ring, ringAlpha)

    override fun equals(other: Any?): Boolean =
        other is InkWaveIndication &&
            other.ink == ink && other.inkAlpha == inkAlpha &&
            other.ring == ring && other.ringAlpha == ringAlpha

    override fun hashCode(): Int = ink.hashCode() * 31 + ring.hashCode()
}

private class InkWaveNode(
    private val interactionSource: InteractionSource,
    private val ink: Color,
    private val inkAlpha: Float,
    private val ring: Color,
    private val ringAlpha: Float,
) : Modifier.Node(), DrawModifierNode {

    private val waves = mutableStateListOf<Wave>()

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        // One live wave at a time — stacked press ghosts look sloppy.
                        waves.toList().forEach { it.cancel() }
                        waves += Wave(interaction.pressPosition, interaction)
                    }
                    is PressInteraction.Release ->
                        waves.firstOrNull { it.press === interaction.press }?.release()
                    is PressInteraction.Cancel ->
                        waves.firstOrNull { it.press === interaction.press }?.cancel()
                    else -> Unit
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        if (waves.isEmpty()) return
        val minDim = size.minDimension
        // Tiny chrome (chips, close wells): wash only — gold ring reads as noise.
        val showRing = minDim >= WaveMinRingDp.toPx()
        waves.forEach { wave ->
            val a = wave.alpha.value
            if (a <= 0.001f) return@forEach
            val maxR = farthestCornerRadius(size, wave.origin)
            val r = maxR * wave.scale.value
            if (r <= 0.5f) return@forEach

            // Soft ink stain (radial), not a hard disk.
            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.0f to ink.copy(alpha = inkAlpha * a),
                        0.55f to ink.copy(alpha = inkAlpha * 0.45f * a),
                        1.0f to Color.Transparent
                    ),
                    center = wave.origin,
                    radius = r.coerceAtLeast(1f)
                ),
                radius = r,
                center = wave.origin
            )

            if (showRing) {
                val ringR = r * WaveRingInset
                if (ringR > 1f) {
                    drawCircle(
                        color = ring.copy(alpha = ringAlpha * a),
                        radius = ringR,
                        center = wave.origin,
                        style = Stroke(width = 1.1.dp.toPx())
                    )
                }
            }
        }
    }

    private inner class Wave(
        val origin: Offset,
        val press: PressInteraction.Press,
    ) {
        val scale = Animatable(0f)
        val alpha = Animatable(0f)
        private var finished = false

        init {
            coroutineScope.launch { scale.animateTo(1f, Motion.feedback()) }
            coroutineScope.launch { alpha.animateTo(1f, Motion.feedback()) }
        }

        fun release() {
            if (finished) return
            finished = true
            coroutineScope.launch {
                // Brief settle travel while fading — keep it short.
                val grow = launch { scale.animateTo(WaveTravelScale, Motion.seal()) }
                val fade = launch { alpha.animateTo(0f, Motion.feedback()) }
                joinAll(grow, fade)
                waves -= this@Wave
            }
        }

        fun cancel() {
            if (finished) return
            finished = true
            coroutineScope.launch {
                alpha.animateTo(0f, Motion.seal())
                waves -= this@Wave
            }
        }
    }
}

/**
 * Radius that reaches every corner of [size] from [origin] — the wash covers
 * the whole target wherever the finger lands.
 */
internal fun farthestCornerRadius(size: Size, origin: Offset): Float =
    listOf(
        Offset(0f, 0f),
        Offset(size.width, 0f),
        Offset(0f, size.height),
        Offset(size.width, size.height),
    ).maxOf { (it - origin).getDistance() }

/** Gold hairline sits slightly inside the wash edge. */
internal const val WaveRingInset = 0.88f
/** Modest travel after lift — never a splash. */
internal const val WaveTravelScale = 1.06f
/** Below this target size, skip the gold ring (chips / icon wells). */
internal val WaveMinRingDp = 52.dp

// Kept for test compatibility / docs; lags removed in D102 (single-layer wave).
internal const val WaveRingLagMs = 0L
internal const val WaveCrestLagMs = 0L
internal const val WaveCrestScale = 1.0f

/**
 * Theme-aware ink wave. High contrast gets a slightly stronger wash so
 * feedback stays visible; default stays quiet (~7%).
 */
@Composable
fun rememberInkWaveIndication(): IndicationNodeFactory {
    val c = AppTheme.colors
    return remember(c) {
        val hc = c.glassFillAlpha > 0.95f
        InkWaveIndication(
            ink = c.textPrimary,
            inkAlpha = if (hc) 0.12f else 0.07f,
            ring = c.gold,
            ringAlpha = if (hc) 0.40f else 0.28f,
        )
    }
}
