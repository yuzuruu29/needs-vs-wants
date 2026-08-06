package com.needsvswants.app.ui.navigation

/**
 * Ordered set of swipeable main tabs hosted by the [HorizontalPager].
 *
 * Order matters — it is both the left-to-right swipe order and the bottom-nav
 * pill order: Home -> Log -> Advisor -> History -> Settings. The [route] values
 * mirror the legacy NavHost route strings so deep links and intent extras keep
 * working unchanged.
 *
 * Paywall is deliberately NOT a tab — it renders as a full-screen overlay.
 */
enum class MainTab(val route: String) {
    Home("summary"),
    Log("input"),
    Advisor("advisor"),
    History("history"),
    Settings("settings");

    companion object {
        /** Number of swipeable main tabs (excludes paywall). */
        const val COUNT = 5

        /** Resolve a [MainTab] from a route string, defaulting to [Home]. */
        fun fromRoute(route: String?): MainTab =
            entries.firstOrNull { it.route == route } ?: Home

        /** Index of the tab for a route, defaulting to the Home index (0). */
        fun indexOf(route: String?): Int = fromRoute(route).ordinal
    }
}
