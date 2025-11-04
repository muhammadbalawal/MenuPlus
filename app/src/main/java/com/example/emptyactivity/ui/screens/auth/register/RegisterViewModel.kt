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

@HiltViewModel
class RegisterViewModel
    @Inject
    constructor(
        private val registerUseCase: RegisterUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(RegisterUiState())
        val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

        fun onNameChange(name: String) {
            _uiState.update { it.copy(name = name, errorMessage = null) }
        }

        fun onEmailChange(email: String) {
            _uiState.update { it.copy(email = email, errorMessage = null) }
        }

        fun onPasswordChange(password: String) {
            _uiState.update { it.copy(password = password, errorMessage = null) }
        }

        fun onConfirmPasswordChange(confirmPassword: String) {
            _uiState.update { it.copy(confirmPassword = confirmPassword, errorMessage = null) }
        }

        fun onTogglePasswordVisibility() {
            _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
        }

        fun onErrorDismissed() {
            _uiState.update { it.copy(errorMessage = null) }
        }

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
