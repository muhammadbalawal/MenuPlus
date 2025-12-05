package com.example.emptyactivity.ui.screens.aboutus

/**
 * UI state for the AboutUsScreen.
 *
 * This data class represents the observable state for the About Us screen.
 * Since this is primarily a static information screen, the state is minimal
 * and only includes basic tracking fields.
 *
 * @param isLoading Indicates whether data is being loaded. Currently unused but included
 *                  for consistency with other screens in case future enhancements require it.
 *
 * Mostly created by: Muhammad
 */
data class AboutUsUiState(
    val isLoading: Boolean = false,
)

