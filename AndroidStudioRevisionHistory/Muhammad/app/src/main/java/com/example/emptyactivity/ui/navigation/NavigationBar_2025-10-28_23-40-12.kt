package com.example.emptyactivity.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.navigation.NavHostController
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Restaurant




@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {

    }

}


private data class BottomNavItem(
    val route: Route,
    val label: String,
    val icon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(
        route = Route.SavedMenu,
        label = "Menu",
        icon = Icons.Default.Restaurant
    ),
    BottomNavItem(
        route = Route.ImportMenu,
        label = "Scan",
        icon = Icons.Default.QrCodeScanner
    ),
    BottomNavItem(
        route = Route.Profile,
        label = "Profile",
        icon = Icons.Default.Person
    )
)