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
                B
            }
        }
    ) {

    }
}