package com.example.emptyactivity.ui.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

/**
 * Bottom navigation bar composable for main app navigation.
 *
 * This navigation bar provides persistent access to the three main app sections:
 * - Menu: Saved menus screen
 * - Scan: OCR screen for extracting text from images
 * - Profile: User profile screen
 *
 * The bar uses Material 3 NavigationBar with premium black/gold theme.
 *
 * @param navController Navigation controller for handling route navigation.
 *
 * Mostly created by: Muhammad
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(PrestigeBlack),
    ) {
        // Subtle top border with gradient
        Canvas(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(1.dp),
        ) {
            drawLine(
                brush =
                    Brush.horizontalGradient(
                        colors =
                            listOf(
                                Color.Transparent,
                                RoyalGold.copy(alpha = 0.3f),
                                RoyalGold.copy(alpha = 0.5f),
                                RoyalGold.copy(alpha = 0.3f),
                                Color.Transparent,
                            ),
                    ),
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 2f,
            )
        }
        NavigationBar(
            containerColor = PrestigeBlack,
            contentColor = Color.White,
        ) {
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
                    colors =
                        NavigationBarItemDefaults.colors(
                            selectedIconColor = RoyalGold,
                            selectedTextColor = RoyalGold,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            unselectedTextColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = Color.Transparent,
                        ),
                )
            }
        }
    }
}

/**
 * Data class representing an item in the bottom navigation bar.
 *
 * @param route The route to navigate to when this item is clicked.
 * @param label The text label displayed for this navigation item.
 * @param icon The icon displayed for this navigation item.
 */
private data class BottomNavItem(
    val route: Route,
    val label: String,
    val icon: ImageVector,
)

/**
 * List of navigation items displayed in the bottom navigation bar.
 *
 * This list defines the main navigation destinations available to authenticated users.
 */
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

