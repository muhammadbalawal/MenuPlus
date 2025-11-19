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
 * ViewModel for the settings screen.
 *
 * This ViewModel manages the settings screen functionality, primarily handling user logout.
 * It processes logout requests, manages loading/error states, and triggers navigation
 * back to the landing screen on successful logout.
 *
 * The ViewModel exposes reactive StateFlows that the UI can observe to update automatically
 * when the settings state changes (loading, logout success, errors).
 *
 * @param logoutUseCase The use case for performing user logout.
 */
@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val logoutUseCase: LogoutUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SettingsUiState())
        val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

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

        fun onErrorDismissed() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
