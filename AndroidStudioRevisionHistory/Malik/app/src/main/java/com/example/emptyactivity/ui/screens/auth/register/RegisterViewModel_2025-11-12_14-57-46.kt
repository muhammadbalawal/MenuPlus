package com.example.emptyactivity.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.usecase.auth.RegisterUseCase
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the registration screen.
 *
 * This ViewModel manages the registration form state and handles user account creation.
 * It processes user input, validates registration data, and manages loading/error states.
 * On successful registration, the authentication state change is automatically observed
 * by MenuPlusAppViewModel, which triggers navigation to the authenticated screens.
 *
 * @param registerUseCase The use case for performing user registration.
 */
@HiltViewModel
class RegisterViewModel
    @Inject
    constructor(
        private val registerUseCase: RegisterUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(RegisterUiState())

        /**
         * The current UI state for the registration screen.
         *
         * This StateFlow emits RegisterUiState updates whenever the form state changes,
         * allowing the UI to reactively update based on user input and registration results.
         */
        val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

        /**
         * Handles name input changes from the UI.
         *
         * Updates the name field and clears any existing error messages.
         *
         * @param name The new name value entered by the user.
         */
        fun onNameChange(name: String) {
            _uiState.update { it.copy(name = name, errorMessage = null) }
        }

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
         * Handles password confirmation input changes from the UI.
         *
         * Updates the confirmPassword field and clears any existing error messages.
         *
         * @param confirmPassword The new password confirmation value entered by the user.
         */
        fun onConfirmPasswordChange(confirmPassword: String) {
            _uiState.update { it.copy(confirmPassword = confirmPassword, errorMessage = null) }
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
         * Handles the register button click.
         *
         * This method initiates the registration process by calling the register use case with
         * the current form values. It updates the UI state to show loading during the request
         * and handles success/error results.
         */
        fun onRegisterClick() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val state = uiState.value
                when (
                    val result =
                        registerUseCase(
                            email = state.email,
                            password = state.password,
                            confirmPassword = state.confirmPassword,
                            name = state.name,
                        )
                ) {
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
