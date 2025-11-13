package com.example.emptyactivity.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Sealed interface defining all navigation routes in the application.
 *
 * This uses Kotlin's type-safe navigation with Jetpack Compose Navigation. All routes are
 * marked with @Serializable to enable type-safe navigation arguments. Routes can be either
 * data objects (no parameters) or data classes (with parameters).
 *
 * The sealed interface ensures compile-time safety - you can't navigate to a route that
 * doesn't exist, and the compiler will catch missing route parameters.
 */
@Serializable
sealed interface Route {
    /** Landing screen shown to unauthenticated users. */
    @Serializable data object Landing : Route

    /** Login screen for user authentication. */
    @Serializable data object Login : Route

    /** Registration screen for creating new user accounts. */
    @Serializable data object Register : Route

    /** Onboarding screen for new users to set up their dietary profile. */
    @Serializable data object Onboarding : Route

    /** Saved menus screen showing user's previously analyzed menus. */
    @Serializable data object SavedMenu : Route

    /**
     * Menu analysis screen for analyzing OCR-extracted menu text with Gemini AI.
     *
     * @param menuText The extracted menu text from OCR. Passed when navigating from OCR screen.
     */
    @Serializable data class ImportMenu(
        val menuText: String = "",
    ) : Route

    /** User profile screen displaying user information and dietary preferences. */
    @Serializable data object Profile : Route

    /** Legacy scanning route (deprecated, use Ocr instead). */
    @Serializable data object Scanning : Route

    /** OCR screen for extracting text from menu images using Google Cloud Vision API. */
    @Serializable data object Ocr : Route

    /**
     * Detailed menu view screen for a specific saved menu.
     *
     * @param menuId The unique identifier of the menu to display.
     */
    @Serializable data class DetailedMenu(
        val menuId: String,
    ) : Route

    /** Settings screen for app configuration and account management. */
    @Serializable data object Settings : Route

    /** Deep link route for onboarding with parameters */
    @Serializable data class DeepLinkOnboarding(
        val language: String? = null,
    ) : Route
}
