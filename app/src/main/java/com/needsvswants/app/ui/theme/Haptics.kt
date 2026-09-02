package com.needsvswants.app.ui.theme

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import android.annotation.SuppressLint

/**
 * Physical-ledger haptic helpers. Mirrors iOS Haptics.seal / warn / success.
 * Fail-soft: older APIs fall back to CLOCK_TICK / LONG_PRESS where needed.
 *
 * D103: respects Settings "Vibration" via [enabled] / [LocalHapticsEnabled].
 * D195: frequency-tuned feedback through VibrationEffect.Composition primitives
 * (API 30+, TEXTURE_TICK 31+, LOW_TICK 33+) with constant fallbacks.
 */
class AppHaptics(
    private val view: View,
) {
    @Volatile
    var enabled: Boolean = true

    private val vibrator: Vibrator? by lazy { resolveVibrator(view.context) }

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

    /** Crisp mechanical-watch tick for rotary selection and dial drags. */
    fun primitiveTick(scale: Float = 0.7f) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            tick()
            return
        }
        composePrimitives(
            fallback = { tick() },
            supported = intArrayOf(VibrationEffect.Composition.PRIMITIVE_TICK),
        ) { c ->
            c.addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, scale.coerceIn(0f, 1f))
        }
    }

    /** Granular low-amplitude paper-friction grain (slips, flips, tear path). */
    fun textureTick(scale: Float = 0.5f) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            tick()
            return
        }
        composePrimitives(
            fallback = { tick() },
            supported = intArrayOf(VibrationEffect.Composition.PRIMITIVE_TICK),
        ) { c ->
            c.addPrimitive(
                VibrationEffect.Composition.PRIMITIVE_TICK,
                (scale * 0.5f).coerceIn(0.05f, 1f)
            )
        }
    }

    /** NEED / WANT seal hit — deep thud with a short resonant hum tail on T+. */
    fun sealThud() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            perform(
                modern = HapticFeedbackConstants.CONFIRM,
                legacy = HapticFeedbackConstants.CONFIRM
            )
            return
        }
        val wantHum = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        composePrimitives(
            fallback = {
                perform(
                    modern = HapticFeedbackConstants.CONFIRM,
                    legacy = HapticFeedbackConstants.CONFIRM
                )
            },
            supported = if (wantHum) {
                intArrayOf(
                    VibrationEffect.Composition.PRIMITIVE_THUD,
                    VibrationEffect.Composition.PRIMITIVE_LOW_TICK
                )
            } else {
                intArrayOf(VibrationEffect.Composition.PRIMITIVE_THUD)
            },
        ) { c ->
            c.addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, 1f)
            if (wantHum) {
                c.addPrimitive(VibrationEffect.Composition.PRIMITIVE_LOW_TICK, 0.35f, 40)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("WrongConstant")
    private fun composePrimitives(
        fallback: () -> Unit,
        supported: IntArray,
        block: (VibrationEffect.Composition) -> Unit,
    ) {
        if (!enabled) return
        val v = vibrator
        if (v == null ||
            !runCatching { v.areAllPrimitivesSupported(*supported) }.getOrDefault(false)
        ) {
            fallback()
            return
        }
        runCatching {
            val composition = VibrationEffect.startComposition()
            block(composition)
            v.vibrate(composition.compose())
        }.onFailure { fallback() }
    }

    private fun perform(modern: Int, legacy: Int) {
        if (!enabled) return
        val constant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) modern else legacy
        view.performHapticFeedback(constant)
    }

    private fun resolveVibrator(context: Context): Vibrator? = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
                as? android.os.VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }.getOrNull()
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
