package com.needsvswants.app.ui.theme

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 * Ink-dry stamp bleed (D195): an AGSL wash that simulates ink soaking into
 * parchment fiber — a brief high-contrast stamp, a ~280ms capillary bleed at a
 * noisy front, then a soft fade to matte. API 33+ (RuntimeShader); the modifier
 * is a no-op below T and under reduced motion so the base stamp still shows.
 */

/**
 * Pure bleed-front position: the radial distance the soaked ink has reached at
 * [progress] (0 stamp just landed, 1 fully settled matte). Exposed for tests.
 */
fun stampBleedFront(progress: Float): Float = 0.22f + 0.24f * progress.coerceIn(0f, 1f)

/** Pure matte fade factor: full wet contrast early, -15% by full settle. */
fun stampBleedMatte(progress: Float): Float =
    1f - 0.15f * ((progress - 0.6f) / 0.4f).coerceIn(0f, 1f)

private const val STAMP_BLEED_AGSL = """
    uniform float2 resolution;
    uniform float progress;
    uniform float4 ink;

    half4 main(in float2 fragCoord) {
        float2 c = fragCoord / resolution - 0.5;
        float d = length(c) * 1.25;
        float n = fract(sin(dot(floor(fragCoord * 0.55), vec2(127.1, 311.7))) * 43758.5453);
        float front = 0.22 + 0.24 * progress;
        float grain = n * 0.11;
        float body = 1.0 - smoothstep(front - 0.05, front, d + grain);
        float rim = smoothstep(front - 0.16, front - 0.06, d + grain)
                  * (1.0 - smoothstep(front - 0.06, front, d + grain));
        float matte = 1.0 - 0.15 * smoothstep(0.6, 1.0, progress);
        float a = (body + rim * 0.25) * matte;
        return half4(ink.rgb, ink.a * a);
    }
"""

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun DrawScope.drawBleed(progress: Float, ink: Color) {
    val shader = RuntimeShader(STAMP_BLEED_AGSL)
    shader.setFloatUniform("resolution", size.width, size.height)
    shader.setFloatUniform("progress", progress.coerceIn(0f, 1f))
    shader.setFloatUniform("ink", ink.red, ink.green, ink.blue, ink.alpha)
    drawRect(androidx.compose.ui.graphics.ShaderBrush(shader))
}

/**
 * Ink-bleed wash driven by [progress] (0..1). Drives its own progress when
 * [active] flips true, using [Motion.inkSettle]; when [progressOverride] is
 * non-null it renders that exact progress instead (tests/previews).
 */
@Composable
fun Modifier.inkBleedStamp(
    active: Boolean,
    ink: Color = AppTheme.colors.gold,
    progressOverride: Float? = null,
): Modifier = composed {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return@composed this
    if (progressOverride != null) {
        return@composed if (progressOverride > 0f) {
            drawBehind {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    drawBleed(progressOverride, ink)
                }
            }
        } else {
            this
        }
    }
    val progress = remember { Animatable(0f) }
    LaunchedEffect(active) {
        if (active && Motion.enabled) {
            progress.snapTo(0f)
            progress.animateTo(1f, Motion.inkSettle())
        }
    }
    val value = progress.value
    if (value <= 0f) return@composed this
    drawBehind {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            drawBleed(value, ink)
        }
    }
}
