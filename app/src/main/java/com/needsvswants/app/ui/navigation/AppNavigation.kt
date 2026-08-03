package com.needsvswants.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.needsvswants.app.ui.screens.advisor.FinancialAdvisorScreen
import com.needsvswants.app.ui.screens.history.HistoryScreen
import com.needsvswants.app.ui.screens.input.InputScreen
import com.needsvswants.app.ui.screens.paywall.PaywallScreen
import com.needsvswants.app.ui.screens.settings.SettingsScreen
import com.needsvswants.app.ui.screens.summary.SummaryScreen
import com.needsvswants.app.ui.theme.AppTheme

private const val ROUTE_PAYWALL = "paywall"

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("summary", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("input", "Log", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
    BottomNavItem("advisor", "Advisor", Icons.Filled.Lightbulb, Icons.Outlined.Lightbulb),
    BottomNavItem("history", "History", Icons.Filled.History, Icons.Outlined.History),
    BottomNavItem("settings", "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    launchVm: LaunchPaywallViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route
    val shouldOfferSoftPaywall by launchVm.shouldOfferSoftPaywall.collectAsStateWithLifecycle()
    var softPaywallLaunched by remember { mutableStateOf(false) }

    // Soft-launch subscription prompt (Play/App Store style): free users see Pro/Max once per session.
    LaunchedEffect(shouldOfferSoftPaywall) {
        if (shouldOfferSoftPaywall && !softPaywallLaunched) {
            softPaywallLaunched = true
            navController.navigate(ROUTE_PAYWALL) {
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        containerColor = AppTheme.colors.background,
        bottomBar = {
            if (currentRoute != ROUTE_PAYWALL) {
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
                            bottomNavItems.forEach { item ->
                                val selected =
                                    currentDestination?.hierarchy?.any { it.route == item.route } == true
                                NavPill(
                                    item = item,
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
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
        NavHost(
            navController = navController,
            startDestination = "summary",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("summary") {
                SummaryScreen(onNavigateToInput = {
                    navController.navigate("input") {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable("input") { InputScreen() }
            composable("advisor") {
                FinancialAdvisorScreen(
                    onOpenPaywall = { navController.navigate(ROUTE_PAYWALL) }
                )
            }
            composable("history") {
                HistoryScreen(onNavigateToInput = {
                    navController.navigate("input") {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable("settings") {
                SettingsScreen(
                    onOpenPaywall = { navController.navigate(ROUTE_PAYWALL) }
                )
            }
            composable(ROUTE_PAYWALL) {
                PaywallScreen(
                    onClose = {
                        launchVm.dismissSoftPaywallForSession()
                        if (!navController.popBackStack()) {
                            navController.navigate("summary") {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
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
    val tint = if (selected) palette.crimson else palette.textSecondary
    val bg = if (selected) palette.crimson.copy(alpha = 0.10f) else androidx.compose.ui.graphics.Color.Transparent

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
            // Fixed-ish label size so Extra-large text scale doesn't crush the bar.
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 9.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                letterSpacing = 0.sp,
                lineHeight = 11.sp
            ),
            color = tint,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            softWrap = false
        )
    }
}
