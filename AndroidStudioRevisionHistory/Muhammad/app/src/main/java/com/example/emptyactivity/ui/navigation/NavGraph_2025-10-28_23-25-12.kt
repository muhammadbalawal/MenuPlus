package com.example.emptyactivity.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

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
            
        }
    }

}
        