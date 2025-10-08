package com.example.emptyactivity.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.emptyactivity.navigation.LocalNavController
import com.example.emptyactivity.view.screens.LandingScreen
import com.example.emptyactivity.view.screens.AboutScreen

@Composable
fun Router() {
    val navController = LocalNavController.current

    NavHost(
        navController = navController,
        startDestination = "landing"
    ) {
        composable("landing") {
            LandingScreen()
        }

        composable("about") {
            AboutScreen()
        }
    }
}
