package com.needsvswants.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.needsvswants.app.ui.screens.history.HistoryScreen
import com.needsvswants.app.ui.screens.input.InputScreen
import com.needsvswants.app.ui.screens.settings.SettingsScreen
import com.needsvswants.app.ui.screens.summary.SummaryScreen
import com.needsvswants.app.ui.theme.*

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("summary", "Summary", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("input", "Log", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
    BottomNavItem("history", "History", Icons.Filled.History, Icons.Outlined.History),
    BottomNavItem("settings", "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = AppTheme.colors.background,
        bottomBar = {
            // Floating pill nav bar — sits above content with hairline border and subtle elevation.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppTheme.colors.surfaceCard)
                    .border(1.dp, AppTheme.colors.divider, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavPill(item, selected) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
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
            composable("history") {
                HistoryScreen(onNavigateToInput = {
                    navController.navigate("input") {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable("settings") { SettingsScreen() }
        }
    }
}

@Composable
private fun NavPill(item: BottomNavItem, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) AppTheme.colors.crimson.copy(alpha = 0.12f) else Color.Transparent
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.label,
                tint = if (selected) AppTheme.colors.crimson else AppTheme.colors.textSecondary,
                modifier = Modifier.size(22.dp)
            )
            if (selected) {
                Spacer(Modifier.width(8.dp))
                Text(
                    item.label,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, letterSpacing = 1.sp),
                    color = AppTheme.colors.crimson,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
