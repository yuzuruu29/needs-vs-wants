package com.needsvswants.app.domain

enum class ThemeId(val storageKey: String) {
    MARKET_LIGHT("market_light"),
    MARKET_DARK("market_dark"),
    SYSTEM("system"),
    HIGH_CONTRAST("high_contrast");

    fun resolveIsDark(systemDark: Boolean): Boolean = when (this) {
        MARKET_DARK -> true
        MARKET_LIGHT, HIGH_CONTRAST -> false
        SYSTEM -> systemDark
    }

    companion object {
        fun fromStorage(raw: String?): ThemeId =
            entries.firstOrNull { it.storageKey == raw } ?: MARKET_LIGHT
    }
}

enum class FontScaleStep(val storageKey: String, val multiplier: Float) {
    // Softened so Extra large stays readable without crushing fixed layouts.
    DEFAULT("default", 1.00f),
    LARGE("large", 1.12f),
    EXTRA_LARGE("extra_large", 1.22f);

    companion object {
        fun fromStorage(raw: String?): FontScaleStep =
            entries.firstOrNull { it.storageKey == raw } ?: DEFAULT
    }
}
