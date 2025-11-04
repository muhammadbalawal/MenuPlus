package com.example.emptyactivity.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.emptyactivity.ui.screens.auth.login.LoginScreen
import com.example.emptyactivity.ui.screens.auth.register.RegisterScreen
import com.example.emptyactivity.ui.screens.landing.LandingScreen


@Composable
fun MenuPlusApp(
    appViewModel: MenuPlusAppViewModel = hiltViewModel()
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
            OnboardingNavGraph()
        }

        is MenuPlusAppUiState.Authenticated -> {
            AuthenticatedNavGraph()
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticatedNavGraph() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            if (showBottomBar(navController)) {
                BottomNavigationBar(navController)
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Route.SavedMenu,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable<Route.Landing> {
                LandingScreen(
                    onContinue = {
                        navController.navigate(Route.Login)
                    },
                )
            }

            composable<Route.Login> {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(Route.Register)
                    },
                    onLoginSuccess = {
                        navController.navigate(Route.SavedMenu) {
                            popUpTo(Route.Landing) { inclusive = true }
                        }
                    },
                )
            }

            composable<Route.Login> {
                RegisterScreen(
                    onNavigateToLogin = {
                        navController.navigate(Route.Login)
                    },
                    onRegisterSuccess = {
                        navController.navigate(Route.SavedMenu) {
                            popUpTo(Route.Landing) { inclusive = true }
                        }
                    },
                )
            }
        }
    }
}


@Composable
private fun showBottomBar(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    return currentRoute in 
        listOf(
            Route.SavedMenu::class.qualifiedName,
            Route.ImportMenu::class.qualifiedName,
            Route.Profile::class.qualifiedName,
        )
}
