package com.example.emptyactivity.ui.screens.auth.login

/**
 * UI state for the login screen.
 *
 * This data class holds all the state needed for the login form, including user input,
 * loading state, success state, and error messages. It is managed by LoginViewModel
 * and observed by LoginScreen.
 *
 * @param email The email address entered by the user.
 * @param password The password entered by the user.
 * @param isPasswordVisible Whether the password field should show the password text.
 * @param isLoading Whether a login request is currently in progress.
 * @param isSuccess Whether the login was successful.
 * @param errorMessage The error message to display if login fails, null if no error.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)
