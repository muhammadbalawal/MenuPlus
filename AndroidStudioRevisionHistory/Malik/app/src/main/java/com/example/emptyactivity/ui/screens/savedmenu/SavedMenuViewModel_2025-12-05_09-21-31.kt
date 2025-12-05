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

/**
 * ViewModel for the SavedMenuScreen.
 *
 * This ViewModel manages the state and business logic for displaying a list of previously
 * saved menus. It handles loading menus from the database, managing loading states, and
 * error handling.
 *
 * The ViewModel:
 * - Loads all menus for the authenticated user
 * - Manages loading state during data fetching
 * - Handles errors and exposes error messages to the UI
 * - Provides reactive state via StateFlow for UI observation
 *
 * @param getMenusUseCase The use case responsible for fetching menus from the database.
 *                        Injected via Hilt.
 *
 * Mostly created by: Muhammad
 */
@HiltViewModel
class SavedMenuViewModel
    @Inject
    constructor(
        private val getMenusUseCase: GetMenusUseCase,
    ) : ViewModel() {
        companion object {
            private const val TAG = "SavedMenuViewModel"
        }

        private val _uiState = MutableStateFlow(SavedMenuUiState(isLoading = true))
        val uiState: StateFlow<SavedMenuUiState> = _uiState.asStateFlow()

        /**
         * Loads all saved menus for the specified user.
         *
         * This method fetches menus from the database via the GetMenusUseCase and updates
         * the UI state with the results. The operation is performed asynchronously and
         * updates the loading state accordingly.
         *
         * @param user The authenticated user whose menus should be loaded. The user ID
         *             is used to filter menus in the database.
         */
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

