package com.example.emptyactivity.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable data object Landing : Route

    @Serializable data object Login : Route

    @Serializable data object Register : Route

    @Serializable data object Onboarding : Route

    @Serializable data object SavedMenu : Route

    @Serializable data object ImportMenu : Route

    @Serializable data object Profile : Route

    @Serializable data class DetailedMenu(
        val menuId: String,
    ) : Route
}
