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
 * - Parses Gemini response into three distinct sections for tabbed display
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
     * 5. Parses the response into three sections (Safe, Best, Full)
     * 6. Updates state with parsed results or error message
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
                    safeMenuContent = null,
                    bestMenuContent = null,
                    fullMenuContent = null,
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

                    // Parse the response into three sections
                    val analysisText = result.data
                    val (safeMenu, bestMenu, fullMenu) = parseAnalysisResponse(analysisText)

                    _uiState.update {
                        it.copy(
                            isAnalyzing = false,
                            safeMenuContent = safeMenu,
                            bestMenuContent = bestMenu,
                            fullMenuContent = fullMenu,
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
     * Removes the displayed analysis results from all three tabs, allowing the user
     * to request a new analysis or start over.
     */
    fun onClearResult() {
        _uiState.update {
            it.copy(
                safeMenuContent = null,
                bestMenuContent = null,
                fullMenuContent = null,
            )
        }
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

    /**
     * Parses the Gemini AI response into three distinct sections.
     *
     * This method extracts content between the structured markers that were
     * requested in the prompt (=== SECTION NAME START/END ===).
     * If parsing fails, it returns appropriate fallback content.
     *
     * @param response The complete response from Gemini AI
     * @return Triple of (safeMenu, bestMenu, fullMenu) strings
     */
    private fun parseAnalysisResponse(response: String): Triple<String, String, String> {
        return try {
            val safeMenuStart = response.indexOf("=== SAFE MENU START ===")
            val safeMenuEnd = response.indexOf("=== SAFE MENU END ===")
            val bestMenuStart = response.indexOf("=== BEST MENU START ===")
            val bestMenuEnd = response.indexOf("=== BEST MENU END ===")
            val fullMenuStart = response.indexOf("=== FULL MENU START ===")
            val fullMenuEnd = response.indexOf("=== FULL MENU END ===")

            val safeMenu =
                if (safeMenuStart != -1 && safeMenuEnd != -1) {
                    response.substring(safeMenuStart + 24, safeMenuEnd).trim()
                } else {
                    "Unable to parse safe menu items. Please try again."
                }

            val bestMenu =
                if (bestMenuStart != -1 && bestMenuEnd != -1) {
                    response.substring(bestMenuStart + 24, bestMenuEnd).trim()
                } else {
                    "Unable to parse recommendations. Please try again."
                }

            val fullMenu =
                if (fullMenuStart != -1 && fullMenuEnd != -1) {
                    response.substring(fullMenuStart + 24, fullMenuEnd).trim()
                } else {
                    response // Fallback to full response if parsing fails
                }

            Triple(safeMenu, bestMenu, fullMenu)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing analysis response", e)
            // Fallback: return full response in all sections
            Triple(response, response, response)
        }
    }
}