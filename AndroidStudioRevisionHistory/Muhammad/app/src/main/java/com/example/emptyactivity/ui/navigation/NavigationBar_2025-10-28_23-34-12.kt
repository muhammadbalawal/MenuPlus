package com.example.emptyactivity.ui.navigation

import androidx.navigation.NavHostController
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue



@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {

    }

}
