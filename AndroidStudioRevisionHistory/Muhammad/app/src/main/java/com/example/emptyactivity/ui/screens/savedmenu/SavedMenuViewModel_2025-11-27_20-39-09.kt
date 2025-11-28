package com.example.emptyactivity.ui.screens.savedmenu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.domain.usecase.menu.GetMenusUseCase
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedMenuViewModel
    @Inject
    constructor(
        private val getMenusUseCase: GetMenusUseCase,
    ) : ViewModel() {
        companion object {
            private const val TAG = "SavedMenuViewModel"
        }

        private val _uiState = MutableStateFlow(SavedMenuUiState())
        val uiState: StateFlow<SavedMenuUiState> = _uiState.asStateFlow()

        fun loadMenus(user: User) {
            viewModelScope.launch {
                Log.d(TAG, "Loading menus for user: ${user.id}")
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                when (val result = getMenusUseCase(user.id)) {
                    is Result.Success -> {
                        Log.d(TAG, "Loaded ${result.data.size} menus")
                        _uiState.update {
                            it.copy(
                                menus = result.data,
                                isLoading = false,
                            )
                        }
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Failed to load menus: ${result.message}")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message,
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

