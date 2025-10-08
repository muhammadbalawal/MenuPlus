package com.example.emptyactivity.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.emptyactivity.navigation.LocalNavController
import com.example.emptyactivity.view.screens.AboutScreen
import com.example.emptyactivity.view.screens.LandingScreen

@Composable
fun Router() {
    val navController = LocalNavController.current

    NavHost(
        navController = navController,
        startDestination = "landing",
    ) {
        composable("landing") {
            LandingScreen()
        }

        composable("about") {
            AboutScreen()
        }
    }
}
