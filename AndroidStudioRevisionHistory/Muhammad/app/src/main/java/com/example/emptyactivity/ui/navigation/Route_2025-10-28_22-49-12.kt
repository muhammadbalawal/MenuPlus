import kotlinx.serialization.Serializable

package com.example.emptyactivity.ui.navigation


@Serializable
sealed interface Route {
    @Serializable data object Landing : Route
    @Serializable data object Login : Route
    @Serializable data object Register : Route
}