package com.example.emptyactivity.ui

import com.example.emptyactivity.domain.model.User

/**
 * Sealed interface representing the application-level UI state.
 *
 * This state determines which navigation graph and screens should be displayed
 * based on the user's authentication and onboarding status. The state is managed
 * by MenuPlusAppViewModel and observed by the MenuPlusApp composable.
 */
sealed interface MenuPlusAppUiState {
    /**
     * Represents the initial loading state while checking authentication status.
     */
    data object Loading : MenuPlusAppUiState

    /**
     * Represents the state when no user is authenticated.
     * The unauthenticated navigation graph (Landing, Login, Register) is shown.
     */
    data object NotAuthenticated : MenuPlusAppUiState

    /**
     * Represents the state when a user is authenticated but hasn't completed onboarding.
     *
     * @param user The authenticated user who needs to complete onboarding.
     */
    data class NeedsOnboarding(
        val user: User,
    ) : MenuPlusAppUiState

    /**
     * Represents the state when a user is authenticated and has completed onboarding.
     * The authenticated navigation graph (SavedMenu, ImportMenu, Profile) is shown.
     *
     * @param user The authenticated user.
     */
    data class Authenticated(
        val user: User,
    ) : MenuPlusAppUiState

    data class DeepLinkOnboarding(
        val language: String?,
    ) : MenuPlusAppUiState

    data class DeepLinkSignup(
        val email: String?,
    ) : MenuPlusAppUiState
}
