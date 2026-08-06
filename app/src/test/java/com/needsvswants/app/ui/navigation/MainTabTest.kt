package com.needsvswants.app.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pure-helper tests for [MainTab] route/index mapping.
 *
 * No Compose runtime needed — these exercise only the enum constants,
 * the route lookup, and the index resolution used by the pager.
 */
class MainTabTest {

    @Test
    fun fromRoute_knownRoutes_returnMatchingTab() {
        assertEquals(MainTab.Home, MainTab.fromRoute("summary"))
        assertEquals(MainTab.Log, MainTab.fromRoute("input"))
        assertEquals(MainTab.Advisor, MainTab.fromRoute("advisor"))
        assertEquals(MainTab.History, MainTab.fromRoute("history"))
        assertEquals(MainTab.Settings, MainTab.fromRoute("settings"))
    }

    @Test
    fun fromRoute_unknownRoute_defaultsToHome() {
        assertEquals(MainTab.Home, MainTab.fromRoute("paywall"))
        assertEquals(MainTab.Home, MainTab.fromRoute("nonsense"))
    }

    @Test
    fun fromRoute_nullRoute_defaultsToHome() {
        assertEquals(MainTab.Home, MainTab.fromRoute(null))
    }

    @Test
    fun indexOf_knownRoutes_returnOrdinal() {
        assertEquals(0, MainTab.indexOf("summary"))
        assertEquals(1, MainTab.indexOf("input"))
        assertEquals(2, MainTab.indexOf("advisor"))
        assertEquals(3, MainTab.indexOf("history"))
        assertEquals(4, MainTab.indexOf("settings"))
    }

    @Test
    fun indexOf_unknownRoute_returnsHomeIndex() {
        assertEquals(0, MainTab.indexOf("paywall"))
        assertEquals(0, MainTab.indexOf(null))
    }

    @Test
    fun order_matchesBottomNavSwipeOrder() {
        // Swipe order must be Home -> Log -> Advisor -> History -> Settings.
        val expected = listOf(MainTab.Home, MainTab.Log, MainTab.Advisor, MainTab.History, MainTab.Settings)
        assertEquals(expected, MainTab.entries.toList())
    }

    @Test
    fun count_matchesNumberOfTabs() {
        assertEquals(MainTab.COUNT, MainTab.entries.size)
        assertEquals(5, MainTab.COUNT)
    }

    @Test
    fun routes_matchLegacyNavHostStrings() {
        // Routes must stay aligned with the legacy NavHost route strings so
        // deep links and intent extras keep working.
        assertEquals("summary", MainTab.Home.route)
        assertEquals("input", MainTab.Log.route)
        assertEquals("advisor", MainTab.Advisor.route)
        assertEquals("history", MainTab.History.route)
        assertEquals("settings", MainTab.Settings.route)
    }
}
