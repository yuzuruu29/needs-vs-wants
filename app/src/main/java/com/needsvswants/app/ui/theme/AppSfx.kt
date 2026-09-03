package com.needsvswants.app.ui.theme

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.needsvswants.app.R
import java.util.Random

/**
 * Organic playback rate for repeated UI sounds: ±2% pitch variance so a row of
 * identical taps never reads machine-stamped. Pure so tests can pin the bounds.
 */
internal fun organicRate(seed: Long = System.nanoTime()): Float =
    1f + ((Random(seed).nextDouble() - 0.5) * 0.04).toFloat()

/**
 * Short UI sonification for ledger taps, long-press, and the Summary orb.
 *
 * Assets: Kenney UI Audio (CC0) — see [SfxCredits].
 *
 * D103: play on the **media** stream at full gain so clips are audible even when
 * system touch-sounds / ASSISTANCE_SONIFICATION are muted. Gate only on the
 * Settings [enabled] flag (and a non-zero sample id), not a global "all three
 * loaded" latch that could stay false forever.
 */
interface AppSfx {
    fun tap()
    fun longPress()
    fun orb()

    /** When false, play calls no-op (Settings toggle). */
    var enabled: Boolean
}

/** Silent default for previews / tests. */
object SilentAppSfx : AppSfx {
    override var enabled: Boolean = false
    override fun tap() = Unit
    override fun longPress() = Unit
    override fun orb() = Unit
}

val LocalAppSfx = staticCompositionLocalOf<AppSfx> { SilentAppSfx }

/**
 * Credits for bundled interaction sounds (required by product; Kenney CC0
 * makes attribution optional but we always show authors).
 */
object SfxCredits {
    const val PACK_NAME = "UI Audio"
    const val AUTHOR = "Kenney Vleugels"
    const val SITE = "https://kenney.nl"
    const val LICENSE = "Creative Commons Zero (CC0 1.0)"
    const val LICENSE_URL = "https://creativecommons.org/publicdomain/zero/1.0/"
    const val PACK_URL = "https://kenney.nl/assets/ui-audio"

    const val ABOUT_LINE =
        "UI sounds: $PACK_NAME by $AUTHOR ($SITE) — $LICENSE"

    val ABOUT_LINES: List<String> = listOf(
        "UI sounds — $PACK_NAME",
        "by $AUTHOR · kenney.nl",
        LICENSE,
        "Files: click1, switch19, switch3 (CC0)",
    )

    val MAPPING: Map<String, String> = mapOf(
        "sfx_tap" to "click1.ogg — quick ledger tap",
        "sfx_long_press" to "switch19.ogg — long-press delete",
        "sfx_orb" to "switch3.ogg — Summary orb press",
    )
}

class SoundPoolAppSfx(context: Context) : AppSfx {
    private val appContext = context.applicationContext
    @Volatile
    override var enabled: Boolean = true

    private val pool: SoundPool = SoundPool.Builder()
        .setMaxStreams(6)
        .setAudioAttributes(
            AudioAttributes.Builder()
                // Media stream = phone volume slider users actually hear.
                // ASSISTANCE_SONIFICATION is often silent when touch sounds are off.
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private var tapId = 0
    private var longPressId = 0
    private var orbId = 0

    init {
        tapId = pool.load(appContext, R.raw.sfx_tap, 1)
        longPressId = pool.load(appContext, R.raw.sfx_long_press, 1)
        orbId = pool.load(appContext, R.raw.sfx_orb, 1)
    }

    override fun tap() = play(tapId, volume = 1f)
    override fun longPress() = play(longPressId, volume = 1f)
    override fun orb() = play(orbId, volume = 1f)

    private fun play(soundId: Int, volume: Float) {
        if (!enabled || soundId == 0) return
        // Try play even if load callback has not fired yet — SoundPool returns 0 if not ready.
        runCatching {
            val v = volume.coerceIn(0f, 1f)
            pool.play(soundId, v, v, /* priority */ 1, /* loop */ 0, /* rate */ organicRate())
        }
    }

    fun release() {
        runCatching { pool.release() }
    }

    companion object {
        @Volatile
        private var instance: SoundPoolAppSfx? = null

        fun get(context: Context): SoundPoolAppSfx {
            val existing = instance
            if (existing != null) return existing
            return synchronized(this) {
                instance ?: SoundPoolAppSfx(context.applicationContext).also { instance = it }
            }
        }

        /** Test / process-reset hook. */
        fun clearInstanceForTests() {
            synchronized(this) {
                instance?.release()
                instance = null
            }
        }
    }
}

@Composable
fun rememberAppSfx(): AppSfx = LocalAppSfx.current

/**
 * Creates (or reuses) the process-wide [SoundPoolAppSfx] and binds [enabled].
 * Call once near the activity root; children use [LocalAppSfx] / [rememberAppSfx].
 */
@Composable
fun rememberBoundAppSfx(enabled: Boolean): AppSfx {
    val context = LocalContext.current
    val sfx = remember(context) { SoundPoolAppSfx.get(context) }
    sfx.enabled = enabled
    return sfx
}
