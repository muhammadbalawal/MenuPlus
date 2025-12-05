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

/**
 * ViewModel for the SavedMenuDetailScreen.
 *
 * This ViewModel manages the state and business logic for displaying and managing a single
 * saved menu. It handles loading menu details from the database, deleting menus, and
 * managing loading/error states.
 *
 * The ViewModel:
 * - Loads menu details by ID from the database
 * - Handles menu deletion with confirmation
 * - Manages loading states during operations
 * - Handles errors and exposes error messages to the UI
 * - Provides reactive state via StateFlow for UI observation
 *
 * @param getMenuByIdUseCase The use case responsible for fetching a menu by ID from the database.
 * @param deleteMenuUseCase The use case responsible for deleting a menu from the database.
 *                          Both injected via Hilt.
 *
 * Mostly created by: Malik
 */
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

        /**
         * Loads a menu from the database by its unique identifier.
         *
         * This method fetches menu details from the database via the GetMenuByIdUseCase and
         * updates the UI state with the results. The operation is performed asynchronously
         * and updates the loading state accordingly.
         *
         * @param menuId The unique identifier of the menu to load. Must not be blank.
         *               If blank, the method returns early with an error state.
         */
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

        /**
         * Deletes a menu from the database.
         *
         * This method removes the menu from the database via the DeleteMenuUseCase. The operation
         * is performed asynchronously and updates the deletion state accordingly. After successful
         * deletion, the UI should navigate back to the saved menus list.
         *
         * @param menuId The unique identifier of the menu to delete.
         */
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

