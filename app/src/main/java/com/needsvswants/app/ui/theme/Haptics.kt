package com.needsvswants.app.ui.theme

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

/**
 * Physical-ledger haptic helpers. Mirrors iOS Haptics.seal / warn / success.
 * Fail-soft: older APIs fall back to CLOCK_TICK / LONG_PRESS where needed.
 */
class AppHaptics(private val view: View) {

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
        val constant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) modern else legacy
        view.performHapticFeedback(constant)
    }
}

@Composable
fun rememberAppHaptics(): AppHaptics {
    val view = LocalView.current
    return remember(view) { AppHaptics(view) }
}
