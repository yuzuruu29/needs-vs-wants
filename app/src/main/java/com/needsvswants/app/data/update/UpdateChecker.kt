package com.needsvswants.app.data.update

import com.needsvswants.app.BuildConfig
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.prefs.AvailableUpdate
import com.needsvswants.app.data.remote.HttpJsonClient
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sideload update check (audit gap: users had no way to learn about new
 * APKs). Polls the site's static `version.json` at most once a day, stores an
 * [AvailableUpdate] in prefs when the advertised versionCode is newer, clears
 * it when up to date. Non-nagging: the UI just shows a row/banner; nothing
 * downloads automatically.
 */
@Singleton
class UpdateChecker @Inject constructor(
    private val preferences: AppPreferences
) {
    /** Throttled check; pass [force] for the Settings "Check now" row. */
    suspend fun checkOnce(force: Boolean = false) {
        val now = System.currentTimeMillis()
        if (!force && !shouldCheck(preferences.lastUpdateCheckAt.first(), now)) return
        preferences.setLastUpdateCheckAt(now)
        HttpJsonClient.request(VERSION_URL).onSuccess { body ->
            val remote = parseVersionJson(body)
            preferences.setUpdateAvailable(
                remote?.takeIf { it.versionCode > BuildConfig.VERSION_CODE }
            )
        }
        // Failures are silent by design (offline is normal); the throttle
        // stamp above stops hot retry loops either way.
    }

    companion object {
        const val VERSION_URL = "https://needs-vs-wants.vercel.app/version.json"
        const val CHECK_INTERVAL_MS: Long = 24 * 60 * 60 * 1000L

        /** Pure throttle gate (unit-tested). */
        fun shouldCheck(
            lastCheckAt: Long,
            nowMillis: Long,
            intervalMillis: Long = CHECK_INTERVAL_MS
        ): Boolean = nowMillis - lastCheckAt >= intervalMillis

        /**
         * Parses the site's version.json (`versionName`, `versionCode`,
         * `apkUrl`). Regex field readers, same trade-off as
         * PayMongoCheckoutJson (org.json is a stub in JVM tests). Returns null
         * when any field is missing or the URL is not https.
         */
        fun parseVersionJson(json: String): AvailableUpdate? {
            val name = readString(json, "versionName") ?: return null
            val code = readInt(json, "versionCode") ?: return null
            val url = readString(json, "apkUrl") ?: return null
            if (!url.startsWith("https://")) return null
            return AvailableUpdate(versionName = name, versionCode = code, apkUrl = url)
        }

        private fun readString(json: String, key: String): String? {
            val m = Regex(""""$key"\s*:\s*"((?:\\.|[^"\\])*)"""").find(json) ?: return null
            return m.groupValues[1].replace("\\/", "/").replace("\\\"", "\"")
        }

        private fun readInt(json: String, key: String): Int? {
            val m = Regex(""""$key"\s*:\s*(\d+)""").find(json) ?: return null
            return m.groupValues[1].toIntOrNull()
        }
    }
}
