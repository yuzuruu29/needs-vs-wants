package com.needsvswants.app.ui.theme

import android.provider.Settings
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Single motion-token source for the physical-ledger feel.
 * All custom animations should consume these — no magic numbers in screens.
 *
 * When the system animator duration scale is 0 (reduced motion / a11y),
 * [enabled] is false and every spec — tweens and springs — collapses to a
 * near-instant 1ms tween.
 */
object Motion {
    const val FeedbackMs = 120
    const val StateMs = 250
    const val EntranceMs = 450
    const val SealMs = 150
    const val StampMs = 450
    const val BudgetMs = 600
    const val StaggerStepMs = 60
    const val PagerSettleMs = 350

    val EaseEmphasizedDecelerate: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
    val EaseOutQuint: Easing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)
    val EaseStandard: Easing = FastOutSlowInEasing

    /** Whether custom motion should run. False when system animation scale is 0. */
    var enabled: Boolean = true
        private set

    fun updateEnabled(animatorDurationScale: Float) {
        enabled = animatorDurationScale > 0.01f
    }

    fun <T> feedback(): TweenSpec<T> = tween(
        durationMillis = if (enabled) FeedbackMs else 1,
        easing = EaseStandard
    )

    fun <T> state(): TweenSpec<T> = tween(
        durationMillis = if (enabled) StateMs else 1,
        easing = EaseOutQuint
    )

    fun <T> entrance(): TweenSpec<T> = tween(
        durationMillis = if (enabled) EntranceMs else 1,
        easing = EaseEmphasizedDecelerate
    )

    fun <T> seal(): TweenSpec<T> = tween(
        durationMillis = if (enabled) SealMs else 1,
        easing = EaseOutQuint
    )

    fun <T> stamp(): TweenSpec<T> = tween(
        durationMillis = if (enabled) StampMs else 1,
        easing = EaseEmphasizedDecelerate
    )

    fun <T> budget(): TweenSpec<T> = tween(
        durationMillis = if (enabled) BudgetMs else 1,
        easing = EaseStandard
    )

    fun <T> navEnter(): TweenSpec<T> = tween(
        durationMillis = if (enabled) StateMs else 1,
        easing = EaseOutQuint
    )

    fun <T> navExit(): TweenSpec<T> = tween(
        durationMillis = if (enabled) (StateMs * 0.75f).toInt() else 1,
        easing = EaseStandard
    )

    /** Count-up spec for [AnimatedMoney] and other numeric transitions. */
    fun <T> number(): TweenSpec<T> = tween(
        durationMillis = if (enabled) StampMs else 1,
        easing = EaseOutQuint
    )

    /** Pager settle spring — crisp page snap with no overshoot. */
    fun pagerSettle(): FiniteAnimationSpec<Float> = if (enabled) {
        spring<Float>(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    } else {
        tween<Float>(1)
    }

    /** Spatial spring for page/slide moves. */
    fun spatialSpring(): FiniteAnimationSpec<Float> = if (enabled) {
        spring<Float>(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    } else {
        tween<Float>(1)
    }

    fun selectionSpring(): FiniteAnimationSpec<Float> = if (enabled) {
        spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    } else {
        tween<Float>(1)
    }

    fun sealSpring(): FiniteAnimationSpec<Float> = if (enabled) {
        spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    } else {
        tween<Float>(1)
    }

    fun pressSpring(): FiniteAnimationSpec<Float> = if (enabled) {
        spring<Float>(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh
        )
    } else {
        tween<Float>(1)
    }
}

/**
 * Returns the stagger delay (in ms) for [index] — each step is [Motion.StaggerStepMs].
 * Consumers pass this to `animationSpec`/`delayMillis`; it is not an animation spec itself.
 */
fun staggerDelay(index: Int): Int = index * Motion.StaggerStepMs

/** Syncs [Motion.enabled] from the system animator duration scale. Call once at theme root. */
@Composable
fun RememberMotionGate() {
    val context = LocalContext.current
    remember(context) {
        val scale = try {
            Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f
            )
        } catch (_: Exception) {
            1f
        }
        Motion.updateEnabled(scale)
        true
    }
}
