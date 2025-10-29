package com.example.emptyactivity.ui.navigation

import androidx.navigation.NavHostController

fun NavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


}
