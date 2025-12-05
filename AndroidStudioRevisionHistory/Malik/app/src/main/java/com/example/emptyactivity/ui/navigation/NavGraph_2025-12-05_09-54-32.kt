package com.example.emptyactivity.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.MenuPlusAppUiState
import com.example.emptyactivity.ui.MenuPlusAppViewModel
import com.example.emptyactivity.ui.components.TopBar
import com.example.emptyactivity.ui.screens.aboutus.AboutUsScreen
import com.example.emptyactivity.ui.screens.auth.login.LoginScreen
import com.example.emptyactivity.ui.screens.auth.register.RegisterScreen
import com.example.emptyactivity.ui.screens.importmenu.ImportMenuScreen
import com.example.emptyactivity.ui.screens.landing.LandingScreen
import com.example.emptyactivity.ui.screens.menuanalysis.MenuAnalysisScreen
import com.example.emptyactivity.ui.screens.ocr.OcrScreen
import com.example.emptyactivity.ui.screens.onboarding.OnboardingScreen
import com.example.emptyactivity.ui.screens.profile.ProfileScreen
import com.example.emptyactivity.ui.screens.savedmenu.SavedMenuDetailScreen
import com.example.emptyactivity.ui.screens.savedmenu.SavedMenuScreen
import com.example.emptyactivity.ui.screens.settings.SettingsScreen


/**
 * Root composable that manages the main navigation structure based on authentication state.
 *
 * This composable observes the app's authentication state and displays the appropriate
 * navigation graph:
 * - Loading: Shows loading indicator while checking auth state
 * - NotAuthenticated: Shows unauthenticated navigation (Landing, Login, Register)
 * - NeedsOnboarding: Shows onboarding flow for new users
 * - Authenticated: Shows main app navigation with bottom bar
 *
 * @param appViewModel The ViewModel managing app-level state (authentication, user data).
 *                     Injected via Hilt.
 *
 * Mostly created by: Muhammad
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
            OnboardingNavGraph(
                user = (appUiState as MenuPlusAppUiState.NeedsOnboarding).user,
            )
        }

        is MenuPlusAppUiState.Authenticated -> {
            AuthenticatedNavGraph(
                user = (appUiState as MenuPlusAppUiState.Authenticated).user,
            )
        }

        is MenuPlusAppUiState.DeepLinkOnboarding -> {
            val deepLinkState = appUiState as MenuPlusAppUiState.DeepLinkOnboarding
            OnboardingNavGraph(
                user =
                    User(
                        id = "3a85c4a9-7a9f-4612-8de6-f6f099b37ff2",
                        email = "deep_link@assignment.com",
                        name = "Assignment Demo User",
                        hasCompletedOnboarding = false,
                    ),
                deepLinkLanguage = deepLinkState.language,
            )
        }

        is MenuPlusAppUiState.DeepLinkSignup -> {
            val deepLinkState = appUiState as MenuPlusAppUiState.DeepLinkSignup
            RegisterNavGraph(initialEmail = deepLinkState.email)
        }
    }
}

/**
 * Loading screen shown while checking authentication state.
 *
 * Displays a centered circular progress indicator while the app determines
 * whether the user is authenticated and needs onboarding.
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
 * This graph handles the authentication flow:
 * - Landing screen (entry point)
 * - Login screen
 * - Register screen
 *
 * Users can navigate between login and register, but cannot access the main app
 * until they successfully authenticate.
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
 * Main navigation graph for authenticated users.
 *
 * This graph provides the core app functionality with a bottom navigation bar:
 * - SavedMenu: List of previously analyzed menus
 * - Ocr: Screen for extracting text from menu images
 * - ImportMenu: Screen for analyzing menu text with Gemini AI
 * - Profile: User profile and dietary preferences
 * - Settings: App settings and account management
 *
 * The graph includes a top bar (with settings button) and bottom navigation bar
 * that are conditionally shown based on the current route.
 *
 * @param user The authenticated user. Required for user-specific screens and operations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthenticatedNavGraph(user: User) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            if (shouldShowTopBar(navController)) {
                TopBar(
                    onSettingsClick = {
                        navController.navigate(Route.Settings)
                    },
                )
            }
        },
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
                SavedMenuScreen(
                    user = user,
                    navController = navController,
                )
            }

            composable<Route.Ocr> {
                OcrScreen(navController = navController)
            }

            composable<Route.ImportMenu> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.ImportMenu>()
                ImportMenuScreen(
                    user = user,
                    initialMenuText = route.menuText,
                    imageUriString = route.imageUriString,
                    navController = navController,
                )
            }

            composable<Route.Profile> {
                ProfileScreen(
                    user = user,
                    onNavigateBack = { navController.navigateUp() },
                )
            }

            composable<Route.Settings> {
                SettingsScreen(
                    user = user,
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToAboutUs = { navController.navigate(Route.AboutUs) },
                    onLogout = {},
                )
            }

            composable<Route.AboutUs> {
                AboutUsScreen(
                    onNavigateBack = { navController.navigateUp() },
                )
            }

            composable<Route.MenuAnalysis> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.MenuAnalysis>()
                
                // If menuId is provided, show SavedMenuDetailScreen
                if (route.menuId.isNotBlank()) {
                    SavedMenuDetailScreen(
                        menuId = route.menuId,
                        navController = navController,
                    )
                } else {
                    // Parse menuItems from JSON (direct list, not wrapped)
                    val menuItems =
                        remember(route.menuItemsJson) {
                            if (route.menuItemsJson.isNotBlank()) {
                                try {
                                    val json =
                                        kotlinx.serialization.json.Json { 
                                            ignoreUnknownKeys = true
                                            coerceInputValues = true
                                        }
                                    json.decodeFromString<List<com.example.emptyactivity.domain.model.MenuItem>>(route.menuItemsJson)
                                } catch (e: Exception) {
                                    emptyList<com.example.emptyactivity.domain.model.MenuItem>()
                                }
                            } else {
                                emptyList<com.example.emptyactivity.domain.model.MenuItem>()
                            }
                        }
                    
                    // Otherwise show regular MenuAnalysisScreen
                    MenuAnalysisScreen(
                        user = user,
                        menuText = route.menuText,
                        menuItems = menuItems,
                        imageUriString = route.imageUriString,
                        navController = navController,
                    )
                }
            }
        }
    }
}

/**
 * Navigation graph for the onboarding flow.
 *
 * This graph is shown to newly registered users who haven't completed their
 * dietary profile setup. The onboarding screen collects:
 * - Preferred language
 * - Allergies
 * - Dietary restrictions
 * - Food dislikes
 * - Food preferences
 *
 * Once completed, the user is automatically moved to the authenticated navigation graph.
 *
 * @param user The newly registered user who needs to complete onboarding.
 */
@Composable
fun OnboardingNavGraph(
    user: User,
    deepLinkLanguage: String? = null,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Onboarding,
    ) {
        composable<Route.Onboarding> {
            OnboardingScreen(
                user = user,
                onComplete = { },
                initialLanguage = deepLinkLanguage,
            )
        }
    }
}

@Composable
fun RegisterNavGraph(
    initialEmail: String? = null,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Register,
    ) {
        composable<Route.Register> {
            RegisterScreen(
                onNavigateToLogin = { navController.navigateUp() },
                onRegisterSuccess = { /* Auth state handles navigation */ },
                initialEmail = initialEmail,
            )
        }
    }
}

/**
 * Determines whether the bottom navigation bar should be displayed.
 *
 * The bottom bar is shown on main app screens (SavedMenu, Ocr, ImportMenu, Profile)
 * but hidden on secondary screens like Settings to provide a cleaner, focused experience.
 *
 * @param navController Navigation controller to access the current route.
 * @return True if the bottom bar should be shown, false otherwise.
 */
@Composable
private fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    return currentRoute in
        listOf(
            Route.SavedMenu::class.qualifiedName,
            Route.Ocr::class.qualifiedName,
            Route.Profile::class.qualifiedName,
        )
}

/**
 * Determines whether the top app bar should be displayed.
 *
 * The top bar (with settings button) is shown on main app screens to provide
 * quick access to settings. It's hidden on secondary screens for a cleaner UI.
 *
 * @param navController Navigation controller to access the current route.
 * @return True if the top bar should be shown, false otherwise.
 */
@Composable
private fun shouldShowTopBar(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    return currentRoute in
        listOf(
            Route.SavedMenu::class.qualifiedName,
            Route.Ocr::class.qualifiedName,
            Route.Profile::class.qualifiedName,
        )
}


