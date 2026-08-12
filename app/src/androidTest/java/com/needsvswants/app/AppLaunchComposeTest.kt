package com.needsvswants.app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Compose smoke: the app composes a UI tree on cold start (first-run
 * onboarding, soft paywall, or Summary — whichever the state machine picks)
 * without throwing during composition.
 */
@RunWith(AndroidJUnit4::class)
class AppLaunchComposeTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun composition_succeeds_onColdStart() {
        composeRule.waitForIdle()
        composeRule.onRoot().assertExists()
    }
}
