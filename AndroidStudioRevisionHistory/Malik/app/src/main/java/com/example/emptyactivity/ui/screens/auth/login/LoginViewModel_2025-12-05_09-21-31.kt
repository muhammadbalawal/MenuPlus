package com.example.emptyactivity.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.usecase.auth.LoginUseCase
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the login screen.
 *
 * This ViewModel manages the login form state and handles user authentication.
 * It processes user input, validates credentials, and manages loading/error states.
 * On successful login, the authentication state change is automatically observed
 * by MenuPlusAppViewModel, which triggers navigation to the authenticated screens.
 *
 * @param loginUseCase The use case for performing user login.
 *
 * Mostly created by: Malik
 */
@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val loginUseCase: LoginUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(LoginUiState())

        /**
         * The current UI state for the login screen.
         *
         * This StateFlow emits LoginUiState updates whenever the form state changes,
         * allowing the UI to reactively update based on user input and login results.
         */
        val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

        /**
         * Handles email input changes from the UI.
         *
         * Updates the email field and clears any existing error messages.
         *
         * @param email The new email value entered by the user.
         */
        fun onEmailChange(email: String) {
            _uiState.update { it.copy(email = email, errorMessage = null) }
        }

        /**
         * Handles password input changes from the UI.
         *
         * Updates the password field and clears any existing error messages.
         *
         * @param password The new password value entered by the user.
         */
        fun onPasswordChange(password: String) {
            _uiState.update { it.copy(password = password, errorMessage = null) }
        }

        /**
         * Toggles the password visibility state.
         *
         * This allows users to show or hide the password text in the password field.
         */
        fun onTogglePasswordVisibility() {
            _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
        }

        /**
         * Dismisses the current error message.
         *
         * Clears the error message from the UI state when the user dismisses the error.
         */
        fun onErrorDismissed() {
            _uiState.update { it.copy(errorMessage = null) }
        }

        /**
         * Handles the login button click.
         *
         * This method initiates the login process by calling the login use case with
         * the current email and password. It updates the UI state to show loading
         * during the request and handles success/error results.
         */
        fun onLoginClick() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                when (val result = loginUseCase(uiState.value.email, uiState.value.password)) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, isSuccess = true)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = result.message)
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }
