package com.needsvswants.app.ui.theme

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView

/**
 * Physical-ledger haptic helpers. Mirrors iOS Haptics.seal / warn / success.
 * Fail-soft: older APIs fall back to CLOCK_TICK / LONG_PRESS where needed.
 *
 * D103: respects Settings "Vibration" via [enabled] / [LocalHapticsEnabled].
 */
class AppHaptics(
    private val view: View,
) {
    @Volatile
    var enabled: Boolean = true

    /** Entry sealed — medium confirm stamp. */
    fun seal() {
        perform(
            modern = HapticFeedbackConstants.CONFIRM,
            legacy = HapticFeedbackConstants.CONTEXT_CLICK
        )
    }

    /** Delete / wipe / overspend warn. */
    fun warn() {
        perform(
            modern = HapticFeedbackConstants.REJECT,
            legacy = HapticFeedbackConstants.LONG_PRESS
        )
    }

    /** Period / preference / chip selection tick. */
    fun tick() {
        perform(
            modern = HapticFeedbackConstants.CLOCK_TICK,
            legacy = HapticFeedbackConstants.CLOCK_TICK
        )
    }

    /** Day-complete / sheet-complete success. */
    fun success() {
        perform(
            modern = HapticFeedbackConstants.CONFIRM,
            legacy = HapticFeedbackConstants.CONTEXT_CLICK
        )
    }

    private fun perform(modern: Int, legacy: Int) {
        if (!enabled) return
        val constant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) modern else legacy
        view.performHapticFeedback(constant)
    }
}

/** Default true so previews and tests still tick unless overridden. */
val LocalHapticsEnabled = staticCompositionLocalOf { true }

@Composable
fun rememberAppHaptics(): AppHaptics {
    val view = LocalView.current
    val enabled = LocalHapticsEnabled.current
    val haptics = remember(view) { AppHaptics(view) }
    haptics.enabled = enabled
    return haptics
}
