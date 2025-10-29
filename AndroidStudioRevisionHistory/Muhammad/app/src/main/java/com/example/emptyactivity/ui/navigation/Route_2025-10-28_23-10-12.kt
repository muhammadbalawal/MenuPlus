package com.example.emptyactivity.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable data object Landing : Route
    @Serializable data object Login : Route
    @Serializable data object Register : Route
}