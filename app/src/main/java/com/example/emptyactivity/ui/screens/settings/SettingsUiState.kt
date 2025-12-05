package com.example.emptyactivity.ui.screens.settings

/**
 * UI state for the SettingsScreen.
 *
 * This data class represents all the observable state needed by the SettingsScreen composable.
 * It follows the unidirectional data flow pattern where the ViewModel updates this state and
 * the UI reacts to changes.
 *
 * @param isLoading Indicates whether a logout operation is currently in progress. When true,
 *                  the logout button should show a loading indicator.
 * @param isLoggedOut Indicates whether the user has been successfully logged out. When true,
 *                    the authentication state should be updated, triggering navigation to
 *                    the unauthenticated screens.
 * @param errorMessage Optional error message to display to the user. When not null, an error
 *                     dialog should be shown with this message. Set to null when the error
 *                     is dismissed or when a new operation starts.
 *
 * Mostly created by: Muhammad
 */
data class SettingsUiState(
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false,
    val errorMessage: String? = null,
)
