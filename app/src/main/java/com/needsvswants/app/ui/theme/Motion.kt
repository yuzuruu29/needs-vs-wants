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
    const val SealHoldMs = 1200
    const val OdometerRollMs = 500
    /** Idle breath period — slow ambient pulse for hero rings. */
    const val IdleMs = 2400
    /** Specular shine full rotation. */
    const val OrbShineMs = 9000
    /**
     * Main-tab paper page-turn duration. Snappier than the website notepad
     * (700ms) so five-tab navigation stays fluid while still reading as paper.
     */
    /**
     * Main-tab paper page-turn. Kept short so horizontal swipes feel snappy
     * against vertical scroll (nested scroll still owns the other axis).
     */
    const val PageFlipMs = 300
    /** Max rotateY — lighter tilt than full edge-on so flings stay responsive. */
    const val PageFlipMaxDegrees = 42f
    /** Depth scale drop at full offset (paper receding in the stack). */
    const val PageFlipDepthScale = 0.03f
    /** Camera distance multiplier for 3D page perspective (× density). */
    const val PageFlipCameraDistance = 14f
    /** Card/content ink-settle (fade + short drop). */
    const val InkSettleMs = 280
    /** Need/Want arc or stroke draw-in when data changes. */
    const val InkDrawMs = 480
    /** Paywall / receipt slip settle. */
    const val ReceiptPrintMs = 360
    /** Orb enlarges before the black-hole suck. */
    const val PortalPulseMs = 180
    /** Content collapse into the portal (suck). */
    const val PortalSuckMs = 320
    /** Percentage sheet expands after the void. */
    const val PortalRevealMs = 380

    /** Stamp landing scale — seal stamp grows in from here. */
    const val StampLandingScale = 0.8f
    /** Stamp leaving scale — seal stamp shrinks toward here on exit. */
    const val StampLeavingScale = 0.92f
    /** Rise scale for panels/meters/dialogs entering or exiting. */
    const val RiseScale = 0.96f

    val EaseEmphasizedDecelerate: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
    val EaseOutQuint: Easing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)
    val EaseStandard: Easing = FastOutSlowInEasing
    /** Paper land — soft ease-out-cubic matching notepad settle (D32 family). */
    val EasePaperFlip: Easing = CubicBezierEasing(0.32f, 0.72f, 0.25f, 1f)
    /** Odometer vertical roll deceleration curve. */
    val EaseOdometerRoll: Easing = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)

    /** Whether custom motion should run. False when system scale is 0 or user reduced-motion is on. */
    var enabled: Boolean = true
        private set

    /**
     * System animator scale + optional user reduced-motion preference (D103).
     * User reduced motion forces off even when the system scale is normal.
     */
    fun updateEnabled(animatorDurationScale: Float, userReducedMotion: Boolean = false) {
        enabled = resolveMotionEnabled(animatorDurationScale, userReducedMotion)
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

    /**
     * Entrance with stagger delay (see [staggerDelay]).
     */
    fun <T> entranceStagger(delayMs: Int): TweenSpec<T> = tween(
        durationMillis = if (enabled) EntranceMs else 1,
        delayMillis = if (enabled) delayMs else 0,
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

    /**
     * Paper page-turn for main-tab swipe settle and pill [animateScrollToPage].
     * Physics-free tween so the leaf lands with a predictable paper ease.
     */
    fun <T> pageFlip(): TweenSpec<T> = tween(
        durationMillis = if (enabled) PageFlipMs else 1,
        easing = EasePaperFlip
    )

    /** Ink settle — cards/sheets appear as ink dries on paper. */
    fun <T> inkSettle(): TweenSpec<T> = tween(
        durationMillis = if (enabled) InkSettleMs else 1,
        easing = EaseOutQuint
    )

    /** Ink draw-in — data arcs / progress strokes. */
    fun <T> inkDraw(): TweenSpec<T> = tween(
        durationMillis = if (enabled) InkDrawMs else 1,
        easing = EasePaperFlip
    )

    /** Receipt slip land (paywall). */
    fun <T> receiptPrint(): TweenSpec<T> = tween(
        durationMillis = if (enabled) ReceiptPrintMs else 1,
        easing = EasePaperFlip
    )

    fun <T> portalPulse(): TweenSpec<T> = tween(
        durationMillis = if (enabled) PortalPulseMs else 1,
        easing = EaseOutQuint
    )

    fun <T> portalSuck(): TweenSpec<T> = tween(
        durationMillis = if (enabled) PortalSuckMs else 1,
        easing = EaseEmphasizedDecelerate
    )

    fun <T> portalReveal(): TweenSpec<T> = tween(
        durationMillis = if (enabled) PortalRevealMs else 1,
        easing = EasePaperFlip
    )

    /** Spatial spring for page/slide moves. */
    fun spatialSpring(): FiniteAnimationSpec<Float> = if (enabled) {
        spring<Float>(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    } else {
        tween<Float>(1)
    }

    /** Selection (nav pill, chips) — crisp settle, no rubber bounce (D102). */
    fun selectionSpring(): FiniteAnimationSpec<Float> = if (enabled) {
        spring<Float>(
            dampingRatio = Spring.DampingRatioNoBouncy,
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

    /** Tactile recoil spring for NEED / WANT buttons on release. */
    fun recoilSpring(): FiniteAnimationSpec<Float> = if (enabled) {
        spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    } else {
        tween<Float>(1)
    }

    /** Elastic fluid glide for the floating navigation tab indicator. */
    fun tabGlideSpring(): FiniteAnimationSpec<Float> = if (enabled) {
        spring<Float>(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    } else {
        tween<Float>(1)
    }

    /** Vertical odometer digit roll deceleration spec. */
    fun <T> odometer(): TweenSpec<T> = tween(
        durationMillis = if (enabled) OdometerRollMs else 1,
        easing = EaseOdometerRoll
    )
}

/**
 * Returns the stagger delay (in ms) for [index] — each step is [Motion.StaggerStepMs].
 * Consumers pass this to `animationSpec`/`delayMillis`; it is not an animation spec itself.
 */
fun staggerDelay(index: Int): Int = index * Motion.StaggerStepMs

/**
 * Pure gate used by [Motion.updateEnabled] and unit tests.
 * Motion runs only when the system animator scale is on **and** the user has
 * not enabled reduced motion in Settings.
 */
fun resolveMotionEnabled(animatorDurationScale: Float, userReducedMotion: Boolean): Boolean =
    animatorDurationScale > 0.01f && !userReducedMotion

/**
 * Syncs [Motion.enabled] from system animator duration scale and the in-app
 * reduced-motion preference. Re-applies on every composition when the user
 * toggles Settings so the gate is live without restart.
 */
@Composable
fun RememberMotionGate(userReducedMotion: Boolean = false) {
    val context = LocalContext.current
    val scale = remember(context) {
        try {
            Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f
            )
        } catch (_: Exception) {
            1f
        }
    }
    // Side-effect each composition so toggling reduced motion applies immediately.
    Motion.updateEnabled(scale, userReducedMotion)
}
