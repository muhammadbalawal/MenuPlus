package com.example.emptyactivity.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.emptyactivity.ui.MenuPlusAppUiState
import com.example.emptyactivity.ui.MenuPlusAppViewModel
import com.example.emptyactivity.ui.screens.auth.login.LoginScreen
import com.example.emptyactivity.ui.screens.auth.register.RegisterScreen
import com.example.emptyactivity.ui.screens.importmenu.ImportMenuScreen
import com.example.emptyactivity.ui.screens.landing.LandingScreen
import com.example.emptyactivity.ui.screens.onboarding.OnboardingScreen
import com.example.emptyactivity.ui.screens.profile.ProfileScreen
import com.example.emptyactivity.ui.screens.savedmenu.SavedMenuScreen

/**
 * Root composable for the MenuPlus application navigation.
 *
 * This composable observes the application UI state and displays the appropriate
 * navigation graph based on authentication and onboarding status. It serves as the
 * entry point for all navigation in the app.
 *
 * @param appViewModel The ViewModel managing application-level UI state.
 */
@Composable
fun MenuPlusApp(
    appViewModel: MenuPlusAppViewModel = hiltViewModel(),
) {
    val appUiState by appViewModel.uiState.collectAsStateWithLifecycle()

    when (appUiState) {
        MenuPlusAppUiState.Loading -> {
            LoadingScreen()
        }

        MenuPlusAppUiState.NotAuthenticated -> {
            UnauthenticatedNavGraph()
        }

        is MenuPlusAppUiState.NeedsOnboarding -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                OnboardingScreen()
            }
        }

        is MenuPlusAppUiState.Authenticated -> {
            AuthenticatedNavGraph()
        }
    }
}

/**
 * Loading screen shown while checking authentication status.
 *
 * Displays a circular progress indicator centered on the screen.
 */
@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Navigation graph for unauthenticated users.
 *
 * This graph contains routes accessible to users who are not logged in:
 * - Landing: Initial welcome screen
 * - Login: User authentication screen
 * - Register: User registration screen
 */
@Composable
private fun UnauthenticatedNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Landing,
    ) {
        composable<Route.Landing> {
            LandingScreen(
                onContinue = { navController.navigate(Route.Login) },
            )
        }

        composable<Route.Login> {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Route.Register) },
                onLoginSuccess = { /* Auth state handles navigation */ },
            )
        }

        composable<Route.Register> {
            RegisterScreen(
                onNavigateToLogin = { navController.navigateUp() },
                onRegisterSuccess = { /* Auth state handles navigation */ },
            )
        }
    }
}

/**
 * Navigation graph for authenticated users.
 *
 * This graph contains routes accessible to users who are logged in and have completed onboarding:
 * - SavedMenu: List of saved restaurant menus
 * - ImportMenu: Screen for importing new menus
 * - Profile: User profile and settings
 *
 * The graph includes a bottom navigation bar for easy navigation between main screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthenticatedNavGraph() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(navController)) {
                BottomNavigationBar(navController)
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Route.SavedMenu,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable<Route.SavedMenu> {
                SavedMenuScreen()
            }

            composable<Route.ImportMenu> {
                ImportMenuScreen()
            }

            composable<Route.Profile> {
                ProfileScreen()
            }
        }
    }
}

/**
 * Determines whether the bottom navigation bar should be displayed.
 *
 * The bottom bar is only shown on main authenticated screens (SavedMenu, ImportMenu, Profile).
 * It is hidden on other screens like DetailedMenu or during navigation transitions.
 *
 * @param navController The navigation controller to check the current route.
 * @return true if the bottom bar should be shown, false otherwise.
 */
@Composable
private fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    return currentRoute in
        listOf(
            Route.SavedMenu::class.qualifiedName,
            Route.ImportMenu::class.qualifiedName,
            Route.Profile::class.qualifiedName,
        )
}
