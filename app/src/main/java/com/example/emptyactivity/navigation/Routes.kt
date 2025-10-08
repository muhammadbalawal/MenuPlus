package com.example.emptyactivity.navigation

sealed class Routes(val route: String) {
    object Landing : Routes("landing")
    object About : Routes("about")
}
