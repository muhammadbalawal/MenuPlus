package com.example.emptyactivity.ui.screens.importmenu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.domain.usecase.menu.AnalyzeMenuUseCase
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the ImportMenu screen.
 *
 * This ViewModel manages the menu analysis workflow using Gemini AI. It coordinates between
 * the UI layer and the menu analysis use case to provide personalized menu recommendations
 * based on the user's dietary profile (allergies, preferences, restrictions).
 *
 * The ViewModel:
 * - Initializes menu text from OCR extraction (via navigation parameter)
 * - Triggers Gemini AI analysis when user requests it
 * - Manages analysis state (loading, success, error)
 * - Exposes reactive StateFlows for UI observation
 *
 * Note: Menu text can only be set via initializeMenuText() - manual text entry is not supported.
 * All menu text must come from OCR extraction.
 *
 * @param analyzeMenuUseCase The use case that handles menu analysis with Gemini AI.
 *                           Injected via Hilt.
 */
@HiltViewModel
class ImportMenuViewModel
@Inject
constructor(
    private val analyzeMenuUseCase: AnalyzeMenuUseCase,
) : ViewModel() {
    companion object {
        private const val TAG = "ImportMenuViewModel"
    }

    private val _uiState = MutableStateFlow(ImportMenuUiState())
    val uiState: StateFlow<ImportMenuUiState> = _uiState.asStateFlow()

    /**
     * Initiates Gemini AI analysis of the menu text.
     *
     * This method triggers the complete menu analysis workflow:
     * 1. Sets loading state to true
     * 2. Retrieves the current menu text from state
     * 3. Calls the analyze menu use case with user ID and menu text
     * 4. The use case fetches user profile (allergies, preferences, etc.) and sends to Gemini
     * 5. Updates state with analysis result or error message
     *
     * The analysis provides personalized recommendations including:
     * - Safety ratings (RED/YELLOW/GREEN) for each menu item
     * - Allergy warnings and ingredient concerns
     * - Dietary restriction violations
     * - Personalized recommendations based on preferences
     * - Menu translation to user's preferred language
     *
     * @param user The current authenticated user. Used to fetch their dietary profile
     *             and pass user ID to the analysis use case.
     */
    fun onAnalyzeMenu(user: User) {
        viewModelScope.launch {
            Log.d(TAG, "Starting menu analysis for user: ${user.id}")
            _uiState.update {
                it.copy(
                    isAnalyzing = true,
                    errorMessage = null,
                    analysisResult = null,
                )
            }

            val menuText = uiState.value.menuText

            when (
                val result =
                    analyzeMenuUseCase(
                        userId = user.id,
                        menuText = menuText,
                    )
            ) {
                is Result.Success -> {
                    Log.d(TAG, "Menu analysis successful")
                    _uiState.update {
                        it.copy(
                            isAnalyzing = false,
                            analysisResult = result.data,
                        )
                    }
                }
                is Result.Error -> {
                    Log.e(TAG, "Menu analysis failed: ${result.message}")
                    _uiState.update {
                        it.copy(
                            isAnalyzing = false,
                            errorMessage = result.message,
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isAnalyzing = true) }
                }
            }
        }
    }

    /**
     * Dismisses the current error message.
     *
     * Clears the error state so the error dialog is hidden. This is called when the user
     * acknowledges the error by clicking "OK" in the error dialog.
     */
    fun onErrorDismissed() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Clears the analysis result from the UI.
     *
     * Removes the displayed analysis result, allowing the user to request a new analysis
     * or start over. This is called when the user clicks the close button on the result card.
     */
    fun onClearResult() {
        _uiState.update { it.copy(analysisResult = null) }
    }

    /**
     * Initializes the menu text from OCR extraction.
     *
     * This method is called when navigating from the OCR screen with extracted text.
     * It sets the menu text in the state, but only if the current state is blank.
     * This prevents overwriting text if the user navigates back and forth.
     *
     * @param text The extracted menu text from OCR, typically a multi-line string
     *             containing all the menu items and descriptions.
     */
    fun initializeMenuText(text: String) {
        if (uiState.value.menuText.isBlank()) {
            _uiState.update { it.copy(menuText = text) }
        }
    }
}