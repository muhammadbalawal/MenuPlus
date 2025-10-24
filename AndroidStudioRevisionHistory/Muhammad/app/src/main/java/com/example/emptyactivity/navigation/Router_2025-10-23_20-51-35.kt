package com.example.emptyactivity.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.emptyactivity.navigation.LocalNavController
import com.example.emptyactivity.view.screen.LandingScreen

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
        
    }
}
