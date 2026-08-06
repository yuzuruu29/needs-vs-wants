package com.needsvswants.app.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pure-helper tests for [Motion] tokens and [staggerDelay].
 *
 * No Compose runtime needed — these exercise only the token constants,
 * duration-collapse logic, and the stagger helper.
 */
class MotionTokensTest {

    private fun enableMotion(enabled: Boolean) {
        Motion.updateEnabled(if (enabled) 1f else 0f)
    }

    @Test
    fun disabled_collapsesAllTweenDurationsToOneMs() {
        enableMotion(false)
        assertEquals(1, Motion.feedback<Float>().durationMillis)
        assertEquals(1, Motion.state<Float>().durationMillis)
        assertEquals(1, Motion.entrance<Float>().durationMillis)
        assertEquals(1, Motion.seal<Float>().durationMillis)
        assertEquals(1, Motion.stamp<Float>().durationMillis)
        assertEquals(1, Motion.budget<Float>().durationMillis)
        assertEquals(1, Motion.number<Float>().durationMillis)
    }

    @Test
    fun enabled_keepsDeclaredDurations() {
        enableMotion(true)
        assertEquals(Motion.FeedbackMs, Motion.feedback<Float>().durationMillis)
        assertEquals(Motion.StateMs, Motion.state<Float>().durationMillis)
        assertEquals(Motion.EntranceMs, Motion.entrance<Float>().durationMillis)
        assertEquals(Motion.SealMs, Motion.seal<Float>().durationMillis)
        assertEquals(Motion.StampMs, Motion.stamp<Float>().durationMillis)
        assertEquals(Motion.BudgetMs, Motion.budget<Float>().durationMillis)
        assertEquals(Motion.StampMs, Motion.number<Float>().durationMillis)
    }

    @Test
    fun disabled_springsCollapseToOneMsTween() {
        // Reduced motion: every spring collapses to a near-instant tween,
        // so no spring profile survives when disabled.
        enableMotion(false)
        assertEquals(1, (Motion.pagerSettle() as TweenSpec<Float>).durationMillis)
        assertEquals(1, (Motion.spatialSpring() as TweenSpec<Float>).durationMillis)
        assertEquals(1, (Motion.selectionSpring() as TweenSpec<Float>).durationMillis)
        assertEquals(1, (Motion.sealSpring() as TweenSpec<Float>).durationMillis)
        assertEquals(1, (Motion.pressSpring() as TweenSpec<Float>).durationMillis)
    }

    @Test
    fun enabled_pagerSettleSpringKeepsCrispProfile() {
        // Spring specs have no durationMillis, but the spring profile must
        // remain a crisp (no-bounce) snap when motion is enabled.
        enableMotion(true)
        val spring = Motion.pagerSettle() as SpringSpec<Float>
        assertEquals(Spring.DampingRatioNoBouncy, spring.dampingRatio)
        assertEquals(Spring.StiffnessMedium, spring.stiffness)
    }

    @Test
    fun spatialSpring_usesLowBouncyMediumStiffness() {
        enableMotion(true)
        val spring = Motion.spatialSpring() as SpringSpec<Float>
        assertEquals(Spring.DampingRatioLowBouncy, spring.dampingRatio)
        assertEquals(Spring.StiffnessMedium, spring.stiffness)
    }

    @Test
    fun staggerDelay_zeroIndexIsZero() {
        assertEquals(0, staggerDelay(0))
    }

    @Test
    fun staggerDelay_threeIndexIsThreeTimesStep() {
        assertEquals(3 * Motion.StaggerStepMs, staggerDelay(3))
    }

    @Test
    fun staggerDelay_matchesFormula() {
        assertEquals(180, staggerDelay(3))
    }
}
