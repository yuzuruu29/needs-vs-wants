package com.needsvswants.app.ui.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.ui.screens.advisor.FinancialAdvisorScreen
import com.needsvswants.app.ui.screens.history.HistoryScreen
import com.needsvswants.app.ui.screens.input.InputScreen
import com.needsvswants.app.ui.screens.paywall.PaywallScreen
import com.needsvswants.app.ui.screens.settings.SettingsScreen
import com.needsvswants.app.ui.screens.summary.SummaryScreen
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.AppType
import com.needsvswants.app.ui.theme.Motion
import com.needsvswants.app.ui.theme.rememberAppHaptics
import kotlinx.coroutines.launch

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("summary", "Home", Icons.Filled.PieChart, Icons.Outlined.PieChart),
    BottomNavItem("input", "Log", Icons.Filled.EditNote, Icons.Outlined.EditNote),
    BottomNavItem("advisor", "Advisor", Icons.AutoMirrored.Filled.MenuBook, Icons.AutoMirrored.Outlined.MenuBook),
    BottomNavItem("history", "History", Icons.Filled.History, Icons.Outlined.History),
    BottomNavItem("settings", "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    startDestination: String = "summary",
    launchVm: LaunchPaywallViewModel = hiltViewModel()
) {
    val initialPage = MainTab.indexOf(startDestination)
    val pagerState = rememberPagerState(initialPage = initialPage) { MainTab.COUNT }
    val scope = rememberCoroutineScope()
    val shouldOfferSoftPaywall by launchVm.shouldOfferSoftPaywall.collectAsStateWithLifecycle()
    var softPaywallLaunched by remember { mutableStateOf(false) }
    var paywallOpen by remember { mutableStateOf(false) }
    val haptics = rememberAppHaptics()

    // Soft paywall only on normal cold start — not when deep-linking to Log from a reminder.
    LaunchedEffect(shouldOfferSoftPaywall, startDestination) {
        if (startDestination != "summary") return@LaunchedEffect
        if (shouldOfferSoftPaywall && !softPaywallLaunched) {
            softPaywallLaunched = true
            paywallOpen = true
        }
    }

    // Light tick when a swipe settles on a new page. Skip the settle that follows
    // a pill tap so we don't double-tick (the tap already ticks).
    var lastTappedPage by remember { mutableIntStateOf(initialPage) }
    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.settledPage }
            .collect { page ->
                if (page != lastTappedPage) {
                    haptics.tick()
                }
                lastTappedPage = -1
            }
    }

    Box(modifier = Modifier.fillMaxSize().background(AppTheme.colors.background)) {
        Scaffold(
            containerColor = AppTheme.colors.background,
            bottomBar = {
                if (!paywallOpen) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = AppTheme.colors.surfaceCard,
                            shadowElevation = 10.dp,
                            tonalElevation = 0.dp,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                AppTheme.colors.gold.copy(alpha = 0.38f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 2.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                bottomNavItems.forEachIndexed { index, item ->
                                    NavPill(
                                        item = item,
                                        selected = pagerState.currentPage == index,
                                        onClick = {
                                            haptics.tick()
                                            lastTappedPage = index
                                            scope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = !paywallOpen,
                beyondViewportPageCount = 1,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) { page ->
                when (MainTab.entries[page]) {
                    MainTab.Home -> SummaryScreen(onNavigateToInput = {
                        lastTappedPage = MainTab.Log.ordinal
                        scope.launch { pagerState.animateScrollToPage(MainTab.Log.ordinal) }
                    })
                    MainTab.Log -> InputScreen()
                    MainTab.Advisor -> FinancialAdvisorScreen(onOpenPaywall = {
                        paywallOpen = true
                    })
                    MainTab.History -> HistoryScreen(onNavigateToInput = {
                        lastTappedPage = MainTab.Log.ordinal
                        scope.launch { pagerState.animateScrollToPage(MainTab.Log.ordinal) }
                    })
                    MainTab.Settings -> SettingsScreen(onOpenPaywall = {
                        paywallOpen = true
                    })
                }
            }
        }

        // Paywall is a full-screen overlay — never a swipeable pager page.
        if (paywallOpen) {
            PaywallScreen(onClose = {
                paywallOpen = false
                launchVm.dismissSoftPaywallForSession()
            })
        }
    }
}

@Composable
private fun NavPill(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    val tintProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = Motion.selectionSpring(),
        label = "navPill"
    )
    val tint = androidx.compose.ui.graphics.lerp(palette.textSecondary, palette.crimson, tintProgress)
    val bg = palette.crimson.copy(alpha = 0.10f * tintProgress)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp, horizontal = 1.dp)
            .heightIn(min = 52.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.label,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = item.label,
            style = AppType.navLabel.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            ),
            color = tint,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            softWrap = false
        )
    }
}
