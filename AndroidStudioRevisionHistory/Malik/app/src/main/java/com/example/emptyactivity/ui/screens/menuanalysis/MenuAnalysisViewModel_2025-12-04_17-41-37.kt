package com.example.emptyactivity.ui.screens.menuanalysis

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.model.MenuItem
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.domain.usecase.menu.SaveMenuUseCase
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the MenuAnalysisScreen.
 *
 * This ViewModel manages the state and business logic for the menu analysis screen, which displays
 * analyzed menu items with safety ratings and allows users to save menus for later access.
 *
 * The ViewModel handles:
 * - Saving analyzed menus to the database (Supabase)
 * - Managing save operation state (loading, success, error)
 * - Error handling and user feedback
 *
 * State is exposed via StateFlow to enable reactive UI updates following the MVVM pattern.
 *
 * @param saveMenuUseCase The use case responsible for persisting menus to the database.
 *                        Injected via Hilt.
 */
@HiltViewModel
class MenuAnalysisViewModel
    @Inject
    constructor(
        private val saveMenuUseCase: SaveMenuUseCase,
    ) : ViewModel() {
        companion object {
            private const val TAG = "MenuAnalysisViewModel"
        }

        private val _uiState = MutableStateFlow(MenuAnalysisUiState())
        val uiState: StateFlow<MenuAnalysisUiState> = _uiState.asStateFlow()

        /**
         * Saves the analyzed menu to the database for later access.
         *
         * This method persists the menu text, analyzed menu items, and optional image URI
         * to Supabase. The save operation is performed asynchronously and updates the UI state
         * to reflect loading, success, or error states.
         *
         * The saved menu can be accessed later from the SavedMenuScreen, allowing users to
         * review previously analyzed menus without re-scanning.
         *
         * @param user The authenticated user who is saving the menu. Used to associate
         *             the menu with the correct user account.
         * @param menuText The original OCR-extracted menu text that was analyzed.
         * @param menuItems The list of analyzed menu items with safety ratings and recommendations.
         * @param imageUriString Optional URI string of the original menu image. Can be empty
         *                       if no image was provided or if the menu was analyzed from text only.
         * @param context Optional Android context. Currently unused but reserved for future
         *                features like image storage or notifications.
         */
        fun onSaveMenu(
            user: User,
            menuText: String,
            menuItems: List<MenuItem>,
            imageUriString: String = "",
            context: Context? = null,
        ) {
            viewModelScope.launch {
                Log.d(TAG, "Saving menu for user: ${user.id}")
                _uiState.update {
                    it.copy(
                        isSaving = true,
                        errorMessage = null,
                        isSaved = false,
                    )
                }

                when (
                    val result =
                        saveMenuUseCase(
                            userId = user.id,
                            menuText = menuText,
                            menuItems = menuItems,
                            imageUri = if (imageUriString.isNotBlank()) imageUriString else null,
                        )
                ) {
                    is Result.Success -> {
                        Log.d(TAG, "Menu saved successfully")
                        _uiState.update {
                            it.copy(
                                isSaving = false,
                                isSaved = true,
                            )
                        }
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Menu save failed: ${result.message}")
                        _uiState.update {
                            it.copy(
                                isSaving = false,
                                errorMessage = result.message,
                                isSaved = false,
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isSaving = true) }
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
