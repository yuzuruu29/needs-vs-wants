package com.needsvswants.app.ui.theme

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlin.math.hypot
import kotlinx.coroutines.awaitCancellation

/**
 * Gyroscopic specular foil — a metallic sheen whose highlight angle follows the
 * physical tilt of the device (D195). Direct-manipulation response, so it stays
 * live under reduced motion: nothing animates on its own.
 */

/** Normalized device tilt, clamped to -1..1 per axis, low-pass filtered. */
data class TiltFoilState(val roll: Float, val pitch: Float)

class TiltFoilController(context: Context) : SensorEventListener {
    val hasSensor: Boolean

    private val manager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val sensor: Sensor?
    private val smooth = FloatArray(2)

    /**
     * Held as [State] rather than a plain value on purpose: the sensor fires at
     * roughly 17Hz, and reading it during composition recomposed every foil host
     * on every event. Read it inside a draw or layer lambda instead.
     */
    val tilt: MutableState<TiltFoilState> = mutableStateOf(TiltFoilState(0f, 0f))

    init {
        sensor = manager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        hasSensor = sensor != null
    }

    fun start() {
        val s = sensor ?: return
        runCatching { manager?.registerListener(this, s, SensorManager.SENSOR_DELAY_UI) }
    }

    fun stop() {
        runCatching { manager?.unregisterListener(this) }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return
        val gx = (event.values[0] / SensorManager.GRAVITY_EARTH).coerceIn(-1.4f, 1.4f)
        val gy = (event.values[1] / SensorManager.GRAVITY_EARTH).coerceIn(-1.4f, 1.4f)
        smooth[0] += (gx - smooth[0]) * TILT_SMOOTHING
        smooth[1] += (gy - smooth[1]) * TILT_SMOOTHING
        tilt.value = TiltFoilState(
            roll = (smooth[0] / TILT_RANGE).coerceIn(-1f, 1f),
            pitch = (smooth[1] / TILT_RANGE).coerceIn(-1f, 1f),
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private companion object {
        const val TILT_SMOOTHING = 0.15f
        const val TILT_RANGE = 1.1f
    }
}

/**
 * Live device tilt, streaming only while the host is RESUMED. Without a sensor
 * this stays at rest (0, 0) forever, which is the correct fallback.
 */
@Composable
fun rememberTiltFoil(): State<TiltFoilState> {
    val context = LocalContext.current
    val controller = remember(context) { TiltFoilController(context) }
    // Android 9+ throttles sensors for background apps, but minSdk 24 devices
    // would keep the accelerometer streaming behind a backgrounded app.
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(controller, lifecycleOwner) {
        if (!controller.hasSensor) return@LaunchedEffect
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            controller.start()
            try {
                awaitCancellation()
            } finally {
                controller.stop()
            }
        }
    }
    return controller.tilt
}

/**
 * Foil sheen that tracks [tilt]: the gradient direction follows the device lean,
 * so gilded seals and badges catch light like real metal when the phone moves.
 * Without a sensor (or at rest tilt) it rests as a soft diagonal sheen.
 */
@Composable
fun Modifier.tiltFoilSpecular(
    tilt: State<TiltFoilState>,
    glowColor: Color = AppTheme.colors.gold,
    alpha: Float = 0.30f,
): Modifier = drawBehind {
    val lean = tilt.value
    val w = size.width
    val h = size.height
    var dx = lean.roll
    var dy = -lean.pitch
    val magnitude = hypot(dx, dy)
    if (magnitude < 0.08f) {
        dx = 0.35f
        dy = 1f
    } else {
        dx /= magnitude
        dy /= magnitude
    }
    val radius = maxOf(w, h) * 0.75f
    val center = Offset(w / 2f, h / 2f)
    val brush = Brush.linearGradient(
        colors = listOf(
            Color.Transparent,
            glowColor.copy(alpha = alpha),
            Color.Transparent,
        ),
        start = center - Offset(dx, dy) * radius,
        end = center + Offset(dx, dy) * radius,
    )
    drawRect(brush)
}
