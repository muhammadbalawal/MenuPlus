package com.example.emptyactivity.ui

import com.example.emptyactivity.domain.model.User

sealed interface MenuPlusAppUiState {
    data object Loading : MenuPlusAppUiState
    data object NotAuthenticated : MenuPlusAppUiState
    data class NeedsOnboarding(val user: User) : MenuPlusAppUiState
    data class Authenticated(val user: User) : MenuPlusAppUiState
}