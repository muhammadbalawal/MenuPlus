package com.example.emptyactivity.ui.screens.settings

data class SettingsUiState(
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false,
    val errorMessage: String? = null,
)
