package com.example.emptyactivity.ui.screens.settings

/**
 * UI state for the settings screen.
 *
 * This data class holds all the state needed for the settings screen, including loading state,
 * logout success state, and error messages. It is managed by SettingsViewModel and observed
 * by SettingsScreen.
 *
 * @param isLoading True when a logout request is in progress.
 * @param isLoggedOut True when logout was successful, triggers navigation to landing screen.
 * @param errorMessage Error message to display if logout fails, null if no error.
 */
data class SettingsUiState(
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false,
    val errorMessage: String? = null,
)
