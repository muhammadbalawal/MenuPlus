package com.example.emptyactivity.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.usecase.auth.LogoutUseCase
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the SettingsScreen.
 *
 * This ViewModel manages the state and business logic for the settings screen, primarily
 * handling user logout functionality. It coordinates with the LogoutUseCase to sign out
 * the user and update the authentication state.
 *
 * The ViewModel:
 * - Handles logout operations
 * - Manages loading states during logout
 * - Handles errors and exposes error messages to the UI
 * - Provides reactive state via StateFlow for UI observation
 *
 * @param logoutUseCase The use case responsible for signing out the user. Injected via Hilt.
 *
 * Mostly created by: Malik
 */
@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val logoutUseCase: LogoutUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SettingsUiState())
        val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

        /**
         * Initiates the logout process for the current user.
         *
         * This method calls the LogoutUseCase to sign out the user from Supabase authentication.
         * After successful logout, the authentication state will be updated, which will trigger
         * navigation to the unauthenticated navigation graph (Landing/Login screens).
         *
         * The operation is performed asynchronously and updates the loading state accordingly.
         */
        fun onLogout() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                when (logoutUseCase()) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, isLoggedOut = true)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Failed to logout. Please try again.",
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }

        /**
         * Dismisses the current error message displayed to the user.
         *
         * This method clears the error state in the UI state, which hides any error dialogs
         * that may be displayed. Called when the user acknowledges an error by clicking "OK"
         * in an error dialog.
         */
        fun onErrorDismissed() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
