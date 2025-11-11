package com.example.emptyactivity.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Sealed interface defining all navigation routes in the application.
 *
 * This sealed interface uses Kotlinx Serialization to enable type-safe navigation with
 * Navigation Compose. Each route represents a screen or destination in the app.
 *
 * Routes are divided into:
 * - Unauthenticated routes: Landing, Login, Register
 * - Onboarding route: Onboarding
 * - Authenticated routes: SavedMenu, ImportMenu, Profile, Scanning, DetailedMenu
 */
@Serializable
sealed interface Route {
    /** Landing screen shown to unauthenticated users. */
    @Serializable data object Landing : Route

    /** Login screen for user authentication. */
    @Serializable data object Login : Route

    /** Registration screen for creating new user accounts. */
    @Serializable data object Register : Route

    /** Onboarding screen for new users to set up preferences. */
    @Serializable data object Onboarding : Route

    /** Saved menus screen showing user's saved restaurant menus. */
    @Serializable data object SavedMenu : Route

    /** Import menu screen for uploading and processing new menus. */
    @Serializable data object ImportMenu : Route

    /** Profile screen showing user information and settings. */
    @Serializable data object Profile : Route

    /** Scanning screen for capturing menu images (not yet implemented). */
    @Serializable data object Scanning : Route

    /**
     * Detailed menu view for a specific saved menu.
     *
     * @param menuId The unique identifier of the menu to display.
     */
    @Serializable data class DetailedMenu(
        val menuId: String,
    ) : Route
}
