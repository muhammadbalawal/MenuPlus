package com.example.emptyactivity.ui.screens.savedmenu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.usecase.menu.DeleteMenuUseCase
import com.example.emptyactivity.domain.usecase.menu.GetMenuByIdUseCase
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedMenuDetailViewModel
    @Inject
    constructor(
        private val getMenuByIdUseCase: GetMenuByIdUseCase,
        private val deleteMenuUseCase: DeleteMenuUseCase,
    ) : ViewModel() {
        companion object {
            private const val TAG = "SavedMenuDetailViewModel"
        }

        private val _uiState = MutableStateFlow(SavedMenuDetailUiState())
        val uiState: StateFlow<SavedMenuDetailUiState> = _uiState.asStateFlow()

        fun loadMenu(menuId: String) {
            if (menuId.isBlank()) {
                Log.e(TAG, "Cannot load menu: menuId is blank")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Invalid menu ID",
                        menu = null,
                    )
                }
                return
            }

            viewModelScope.launch {
                Log.d(TAG, "Loading menu: $menuId")
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                try {
                    when (val result = getMenuByIdUseCase(menuId)) {
                        is Result.Success -> {
                            Log.d(TAG, "Menu loaded successfully")
                            _uiState.update {
                                it.copy(
                                    menu = result.data,
                                    isLoading = false,
                                )
                            }
                        }
                        is Result.Error -> {
                            Log.e(TAG, "Failed to load menu: ${result.message}")
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = result.message,
                                    menu = null,
                                )
                            }
                        }
                        is Result.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Exception while loading menu", e)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to load menu: ${e.message}",
                            menu = null,
                        )
                    }
                }
            }
        }

        fun onDeleteMenu(menuId: String) {
            viewModelScope.launch {
                Log.d(TAG, "Deleting menu: $menuId")
                _uiState.update {
                    it.copy(
                        isDeleting = true,
                        errorMessage = null,
                        isDeleted = false,
                    )
                }

                when (val result = deleteMenuUseCase(menuId)) {
                    is Result.Success -> {
                        Log.d(TAG, "Menu deleted successfully")
                        _uiState.update {
                            it.copy(
                                isDeleting = false,
                                isDeleted = true,
                            )
                        }
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Failed to delete menu: ${result.message}")
                        _uiState.update {
                            it.copy(
                                isDeleting = false,
                                errorMessage = result.message,
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isDeleting = true) }
                    }
                }
            }
        }

        fun onErrorDismissed() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }

