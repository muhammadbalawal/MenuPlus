package com.example.emptyactivity.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPlusApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            if (showBottomBar(navController)){
                NavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Route.Landing,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable<Route.Landing>{
                LandingScreen(
                    onContinue = {
                        navController.navigate(Route.Login)
                    }
                )
            }

        }
    }
}



@Composable
private fun showBottomBar(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    return currentRoute in listOf(
        Route.SavedMenu::class.qualifiedName,
        Route.ImportMenu::class.qualifiedName,
        Route.Profile::class.qualifiedName
    )
}
        