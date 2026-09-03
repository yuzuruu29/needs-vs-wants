package com.needsvswants.app.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Add
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.needsvswants.app.BuildConfig
import com.needsvswants.app.ui.theme.AppShapes
import com.needsvswants.app.ui.screens.advisor.FinancialAdvisorScreen
import com.needsvswants.app.ui.screens.history.HistoryScreen
import com.needsvswants.app.ui.screens.input.InputScreen
import com.needsvswants.app.ui.screens.paywall.PaywallScreen
import com.needsvswants.app.ui.screens.settings.SettingsScreen
import com.needsvswants.app.ui.screens.summary.SummaryScreen
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.AppType
import com.needsvswants.app.ui.theme.Motion
import com.needsvswants.app.ui.theme.PaperPagerPage
import com.needsvswants.app.ui.theme.pagerPageOffset
import com.needsvswants.app.ui.theme.rememberAppHaptics
import com.needsvswants.app.ui.theme.rememberAppSfx
import com.needsvswants.app.ui.theme.scaledSpacing
import com.needsvswants.app.ui.theme.themedInkWash
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
    val pagerState = rememberPagerState(initialPage = initialPage) { MainTab.visibleEntries().size }
    val scope = rememberCoroutineScope()
    val shouldOfferSoftPaywall by launchVm.shouldOfferSoftPaywall.collectAsStateWithLifecycle()
    var softPaywallLaunched by remember { mutableStateOf(false) }
    var paywallOpen by remember { mutableStateOf(false) }
    val haptics = rememberAppHaptics()
    val sfx = rememberAppSfx()
    val navEntitlement by launchVm.entitlement.collectAsStateWithLifecycle()
    // One-shot: after a fresh Pro/Max grant, dismiss the paywall into the Settings
    // Membership Desk once so the upgrade destination is obvious. Only fires when
    // the paywall actually closes (was open → now closed) while paid — a plain
    // cold start by an already-paid user must not re-seek to Settings.
    var grantJustClosedPaywall by remember { mutableStateOf(false) }
    var landedOnDesk by remember { mutableStateOf(false) }
    var lastTappedPage by remember { mutableIntStateOf(initialPage) }

    // Quick-log FAB scroll behavior (design audit #13): hide on scroll-down past a
    // threshold, show again on any scroll-up. Driven by nested scroll events from
    // the tab pages' vertical scrolls (propagate up to this root Box).
    var fabVisible by remember { mutableStateOf(true) }
    var fabScrollAccum by remember { mutableFloatStateOf(0f) }
    val fabThresholdPx = with(LocalDensity.current) { 48.dp.toPx() }
    val fabScrollConnection = remember(fabThresholdPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: androidx.compose.ui.geometry.Offset, source: NestedScrollSource): androidx.compose.ui.geometry.Offset {
                if (source != NestedScrollSource.UserInput) return androidx.compose.ui.geometry.Offset.Zero
                val dy = available.y
                if (dy > 0f) {
                    fabScrollAccum += dy
                    if (fabScrollAccum > fabThresholdPx && fabVisible) fabVisible = false
                } else if (dy < 0f) {
                    fabScrollAccum = 0f
                    if (!fabVisible) fabVisible = true
                }
                return androidx.compose.ui.geometry.Offset.Zero
            }
        }
    }

    // Soft paywall only on normal cold start — not when deep-linking to Log from a reminder.
    // The `plain` test flavor never offers it, regardless of entitlement state.
    LaunchedEffect(shouldOfferSoftPaywall, startDestination) {
        if (BuildConfig.PLAIN_FREE) return@LaunchedEffect
        if (startDestination != "summary") return@LaunchedEffect
        if (shouldOfferSoftPaywall && !softPaywallLaunched) {
            softPaywallLaunched = true
            paywallOpen = true
        }
    }

    // Post-checkout land-on-Desk: when the paywall closes (was open → now closed),
    // the user is now paid, and we have not yet landed, hop to Settings once so the
    // upgrade destination is obvious. `grantJustClosedPaywall` is set on the open→closed
    // edge and consumed on the first paid landing, so a plain cold start by an
    // already-paid user never re-seeks to Settings.
    LaunchedEffect(paywallOpen, navEntitlement) {
        val paid = navEntitlement.hasProAccessAt(System.currentTimeMillis())
        if (!paid) {
            // Access dropped — re-arm so a future genuine re-purchase re-lands on the Desk.
            landedOnDesk = false
        } else if (!paywallOpen && grantJustClosedPaywall && !landedOnDesk) {
            landedOnDesk = true
            grantJustClosedPaywall = false
            lastTappedPage = MainTab.visibleEntries().indexOf(MainTab.Settings)
            scope.launch {
                pagerState.animateScrollToPage(
                    page = MainTab.visibleEntries().indexOf(MainTab.Settings),
                    animationSpec = Motion.pageFlip()
                )
            }
        }
    }

    // Light tick when a swipe settles on a new page. Skip the settle that follows
    // a pill tap so we don't double-tick (the tap already ticks).
    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.settledPage }
            .collect { page ->
                if (page != lastTappedPage) {
                    haptics.tick()
                }
                lastTappedPage = -1
            }
    }

    // Desk under the ledger: warm paper wash. Individual tabs are opaque sheets
    // that paper-flip on swipe / pill tap (see [PaperPagerPage]). Edge-to-edge:
    // the wash bleeds under the status bar while content pads below it (D195).
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themedInkWash())
            .statusBarsPadding()
            .nestedScroll(fabScrollConnection)
    ) {
        // Hide the bar while the IME is up so Scaffold does not keep its height above the keyboard.
        val density = LocalDensity.current
        val imeInsets = WindowInsets.ime
        val imeVisible by remember(density, imeInsets) {
            derivedStateOf { imeInsets.getBottom(density) > 0 }
        }
        Scaffold(
            containerColor = AppTheme.colors.background,
            bottomBar = {
                if (!paywallOpen && !imeVisible) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(
                                horizontal = scaledSpacing(14f),
                                vertical = scaledSpacing(10f)
                            )
                    ) {
                        Surface(
                            shape = AppShapes.r28,
                            color = AppTheme.colors.surfaceCard.copy(alpha = 0.96f),
                            shadowElevation = 12.dp,
                            tonalElevation = 0.dp,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                AppTheme.colors.gold.copy(alpha = 0.45f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val tabs = MainTab.visibleEntries()
                            val selectedIndex = pagerState.currentPage
                            val indicatorOffsetFraction by animateFloatAsState(
                                targetValue = selectedIndex.toFloat(),
                                animationSpec = Motion.tabGlideSpring(),
                                label = "navIndicatorOffset"
                            )
                            val isMax = navEntitlement.hasMaxAccessAt(System.currentTimeMillis())
                            val isPaid = navEntitlement.hasProAccessAt(System.currentTimeMillis())
                            val activePillColor = when {
                                isMax -> AppTheme.colors.crimson
                                isPaid -> AppTheme.colors.gilt
                                else -> AppTheme.colors.crimson
                            }

                            // No IntrinsicSize.Min here: it forces an intrinsic query
                            // onto this Box's children, and BoxWithConstraints is a
                            // SubcomposeLayout whose intrinsics throw — that crashed
                            // every startup since 2.0.23. matchParentSize alone ties
                            // the pill layer to the tab Row's measured height.
                            Box(modifier = Modifier.fillMaxWidth()) {
                                BoxWithConstraints(modifier = Modifier.matchParentSize()) {
                                    val tabWidth = maxWidth / tabs.size
                                    Box(
                                        modifier = Modifier
                                            .offset {
                                                androidx.compose.ui.unit.IntOffset(
                                                    x = (indicatorOffsetFraction * tabWidth.toPx()).toInt(),
                                                    y = 0
                                                )
                                            }
                                            .width(tabWidth)
                                            .fillMaxHeight()
                                            .padding(horizontal = 3.dp, vertical = 4.dp)
                                            .clip(AppShapes.r20)
                                            .background(activePillColor.copy(alpha = 0.12f))
                                            .border(
                                                androidx.compose.foundation.BorderStroke(1.dp, activePillColor.copy(alpha = 0.35f)),
                                                AppShapes.r20
                                            )
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 2.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    tabs.forEachIndexed { index, tab ->
                                        val item = bottomNavItems.firstOrNull { it.route == tab.route } ?: return@forEachIndexed
                                        NavPill(
                                            item = item,
                                            selected = selectedIndex == index,
                                            entitlement = navEntitlement,
                                            onClick = {
                                                haptics.tick()
                                                sfx.tap()
                                                lastTappedPage = index
                                                scope.launch {
                                                    pagerState.animateScrollToPage(
                                                        page = index,
                                                        animationSpec = Motion.pageFlip()
                                                    )
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
            }
        ) { innerPadding ->
            val fling = PagerDefaults.flingBehavior(
                state = pagerState,
                snapAnimationSpec = Motion.pageFlip()
            )
            // Axis lock + mid-gesture pager disable so up/down never pages left/right.
            val scrollGate = rememberPagerScrollGate(pagerState)
            val allowPagerScroll = scrollGate.allowPagerUserScroll.value
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = !paywallOpen && allowPagerScroll,
                // 0 = less off-screen composition during fling (snappier on mid devices)
                beyondViewportPageCount = 0,
                flingBehavior = fling,
                pageNestedScrollConnection = scrollGate.connection,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    // Hard-clip the strip so turning neighbors never paint into chrome.
                    .clipToBounds()
            ) { page ->
                val pageOffset = pagerPageOffset(pagerState, page)
                PaperPagerPage(
                    pageOffset = pageOffset,
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds()
                        // Child-side axis lock (swallows residual X during vertical scroll).
                        .verticalScrollFirst()
                ) {
                    when (MainTab.visibleEntries()[page]) {
                        MainTab.Home -> SummaryScreen(
                            onNavigateToInput = {
                                lastTappedPage = MainTab.visibleEntries().indexOf(MainTab.Log)
                                scope.launch {
                                    pagerState.animateScrollToPage(
                                        page = MainTab.visibleEntries().indexOf(MainTab.Log),
                                        animationSpec = Motion.pageFlip()
                                    )
                                }
                            },
                            onOnboardingStartLogging = {
                                // The first-run narrative now ends in a real seal —
                                // hold the soft paywall for this session so the aha
                                // moment is not interrupted (D191). A later cold
                                // start still offers it.
                                launchVm.dismissSoftPaywallForSession()
                                lastTappedPage = MainTab.visibleEntries().indexOf(MainTab.Log)
                                scope.launch {
                                    pagerState.animateScrollToPage(
                                        page = MainTab.visibleEntries().indexOf(MainTab.Log),
                                        animationSpec = Motion.pageFlip()
                                    )
                                }
                            }
                        )
                        MainTab.Log -> InputScreen(
                            onOpenPaywall = {
                                paywallOpen = true
                            },
                            onOpenSettings = {
                                lastTappedPage = MainTab.visibleEntries().indexOf(MainTab.Settings)
                                scope.launch {
                                    pagerState.animateScrollToPage(
                                        page = MainTab.visibleEntries().indexOf(MainTab.Settings),
                                        animationSpec = Motion.pageFlip()
                                    )
                                }
                            }
                        )
                        MainTab.Advisor -> FinancialAdvisorScreen(onOpenPaywall = {
                            paywallOpen = true
                        })
                        MainTab.History -> HistoryScreen(onNavigateToInput = {
                            lastTappedPage = MainTab.visibleEntries().indexOf(MainTab.Log)
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = MainTab.visibleEntries().indexOf(MainTab.Log),
                                    animationSpec = Motion.pageFlip()
                                )
                            }
                        })
                        MainTab.Settings -> SettingsScreen(onOpenPaywall = {
                            paywallOpen = true
                        })
                    }
                }
            }
        }

        // Quick-log FAB (design audit #13): floats on every tab except Log itself.
        // Hidden while the paywall covers the desk and while the user scrolls down.
        AnimatedVisibility(
            visible = fabVisible && !paywallOpen && !imeVisible &&
                MainTab.visibleEntries().getOrNull(pagerState.currentPage) != MainTab.Log,
            enter = fadeIn(Motion.feedback()) + scaleIn(initialScale = 0.8f, animationSpec = Motion.feedback()),
            exit = fadeOut(Motion.feedback()) + scaleOut(targetScale = 0.8f, animationSpec = Motion.feedback())
        ) {
            // Inner Box restores BoxScope for the FAB's bottom-end alignment.
            Box(Modifier.fillMaxSize()) {
                FloatingActionButton(
                    onClick = {
                        haptics.tick()
                        sfx.tap()
                        lastTappedPage = MainTab.visibleEntries().indexOf(MainTab.Log)
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page = MainTab.visibleEntries().indexOf(MainTab.Log),
                                animationSpec = Motion.pageFlip()
                            )
                        }
                    },
                    shape = CircleShape,
                    containerColor = AppTheme.colors.crimson,
                    contentColor = AppTheme.colors.surfaceCard,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 20.dp, bottom = 96.dp)
                        .navigationBarsPadding()
                        .shadow(8.dp, CircleShape)
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = "Quick log")
                }
            }
        }

        // Paywall is a full-screen overlay — never a swipeable pager page.
        // The `plain` test flavor never composes it.
        if (paywallOpen && !BuildConfig.PLAIN_FREE) {
            PaywallScreen(onClose = {
                paywallOpen = false
                grantJustClosedPaywall = true
                launchVm.dismissSoftPaywallForSession()
            })
        }
    }
}

@Composable
private fun NavPill(
    item: BottomNavItem,
    selected: Boolean,
    entitlement: com.needsvswants.app.domain.Entitlement,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    val isMax = entitlement.hasMaxAccessAt(System.currentTimeMillis())
    val isPaid = entitlement.hasProAccessAt(System.currentTimeMillis())
    // Paid tiers tint the selected pill gold (Pro) / crimson+gold (Max).
    val selectedColor = when {
        isMax -> palette.crimson
        isPaid -> palette.gilt
        else -> palette.crimson
    }
    val tintProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = Motion.selectionSpring(),
        label = "navPill"
    )
    val tint = androidx.compose.ui.graphics.lerp(palette.textSecondary, selectedColor, tintProgress)
    val iconScale by animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = Motion.selectionSpring(),
        label = "navPillIconScale"
    )

    Column(
        modifier = modifier
            .clip(AppShapes.r16)
            .clickable(onClick = onClick, role = Role.Tab)
            // TalkBack: announce which tab is active, not just its name.
            .semantics { this.selected = selected }
            .padding(vertical = scaledSpacing(8f), horizontal = 2.dp)
            .heightIn(min = scaledSpacing(56f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.label,
            tint = tint,
            modifier = Modifier
                .size(scaledSpacing(22f))
                .graphicsLayer {
                    scaleX = iconScale
                    scaleY = iconScale
                }
        )
        Spacer(Modifier.height(scaledSpacing(3f)))
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
