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
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.MenuPlusAppUiState
import com.example.emptyactivity.ui.MenuPlusAppViewModel
import com.example.emptyactivity.ui.components.TopBar
import com.example.emptyactivity.ui.screens.auth.login.LoginScreen
import com.example.emptyactivity.ui.screens.auth.register.RegisterScreen
import com.example.emptyactivity.ui.screens.importmenu.ImportMenuScreen
import com.example.emptyactivity.ui.screens.landing.LandingScreen
import com.example.emptyactivity.ui.screens.onboarding.OnboardingScreen
import com.example.emptyactivity.ui.screens.profile.ProfileScreen
import com.example.emptyactivity.ui.screens.savedmenu.SavedMenuScreen


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
                user = (appUiState as MenuPlusAppUiState.NeedsOnboarding).user
            )
        }

        is MenuPlusAppUiState.Authenticated -> {
            AuthenticatedNavGraph(
                user = (appUiState as MenuPlusAppUiState.Authenticated).user
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthenticatedNavGraph(user: User) {
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
                ImportMenuScreen(user = user)
            }

            composable<Route.Profile> {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun OnboardingNavGraph(
    user: User,
) {
    val navController = rememberNavController()
    

    NavHost(
        navController = navController,
        startDestination = Route.Onboarding
    ) {
        composable<Route.Onboarding> {
            OnboardingScreen(
                user = user,
                onComplete = {
                }
            )
        }
    }
}
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

@Composable
private fun shouldShowTopBar(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    return currentRoute in
            listOf(
                Route.SavedMenu::class.qualifiedName,
                Route.ImportMenu::class.qualifiedName,
                Route.Profile::class.qualifiedName,
            )
}


