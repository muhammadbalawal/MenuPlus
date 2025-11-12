package com.example.emptyactivity.ui.screens.auth.register

/**
 * UI state for the registration screen.
 *
 * This data class holds all the state needed for the registration form, including user input,
 * loading state, success state, and error messages. It is managed by RegisterViewModel
 * and observed by RegisterScreen.
 *
 * @param name The display name entered by the user.
 * @param email The email address entered by the user.
 * @param password The password entered by the user.
 * @param confirmPassword The password confirmation entered by the user.
 * @param isPasswordVisible Whether the password field should show the password text.
 * @param isLoading Whether a registration request is currently in progress.
 * @param isSuccess Whether the registration was successful.
 * @param errorMessage The error message to display if registration fails, null if no error.
 */
data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)
