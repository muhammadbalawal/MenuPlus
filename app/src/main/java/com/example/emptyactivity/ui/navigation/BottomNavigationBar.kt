package com.example.emptyactivity.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hasRoute(item.route::class) == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                    )
                },
                label = { Text(item.label) },
            )
        }
    }
}

private data class BottomNavItem(
    val route: Route,
    val label: String,
    val icon: ImageVector,
)

private val bottomNavItems =
    listOf(
        BottomNavItem(
            route = Route.SavedMenu,
            label = "Menu",
            icon = Icons.Default.Restaurant,
        ),
        BottomNavItem(
            route = Route.Ocr,
            label = "Scan",
            icon = Icons.Default.QrCodeScanner,
        ),
        BottomNavItem(
            route = Route.Profile,
            label = "Profile",
            icon = Icons.Default.Person,
        ),
    )


