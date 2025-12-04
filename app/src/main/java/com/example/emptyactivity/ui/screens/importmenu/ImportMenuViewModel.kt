package com.example.emptyactivity.ui.screens.importmenu

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.model.MenuItem
import com.example.emptyactivity.domain.model.SafetyRating
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.domain.usecase.menu.AnalyzeMenuUseCase
import com.example.emptyactivity.domain.usecase.menu.SaveMenuUseCase
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
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
        private val saveMenuUseCase: SaveMenuUseCase,
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
                        menuItems = null,
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

                        // Parse the JSON response
                        val analysisText = result.data
                        val menuItems = parseAnalysisResponse(analysisText)

                        _uiState.update {
                            it.copy(
                                isAnalyzing = false,
                                menuItems = menuItems,
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
         * Saves the current menu analysis to the database.
         *
         * This method saves the menu text and all analysis results (safe, best, full menu)
         * to Supabase so the user can access it later from the Saved Menus screen.
         *
         * @param user The current authenticated user. Used to associate the menu with the user.
         * @param imageUriString Optional URI of the original menu image.
         */
        fun onSaveMenu(user: User, imageUriString: String = "", context: Context? = null) {
            viewModelScope.launch {
                Log.d(TAG, "Saving menu for user: ${user.id}")

                val state = uiState.value

                // Check if menuItems exist before attempting to save
                if (state.menuItems == null) {
                    Log.e(TAG, "Cannot save menu: no analysis results available")
                    _uiState.update {
                        it.copy(
                            errorMessage = "Please analyze the menu before saving"
                        )
                    }
                    return@launch
                }

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
                            menuText = state.menuText,
                            menuItems = state.menuItems, // Now guaranteed to be non-null
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
                    menuItems = null,
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
         * Parses the Gemini AI JSON response into a list of MenuItem objects.
         *
         * This method extracts JSON from the response (handling cases where Gemini
         * might wrap it in markdown or other text) and deserializes it into MenuItem objects.
         * Items are sorted by rank (best for user first).
         *
         * @param response The complete response from Gemini AI (should be JSON)
         * @return List of MenuItem objects sorted by rank
         */
        private fun parseAnalysisResponse(response: String): List<MenuItem> =
            try {
                // Try to extract JSON from response (in case Gemini wraps it in markdown)
                val jsonStart = response.indexOf("{")
                val jsonEnd = response.lastIndexOf("}") + 1
                val jsonString = if (jsonStart != -1 && jsonEnd > jsonStart) {
                    response.substring(jsonStart, jsonEnd)
                } else {
                    response
                }

                val json = Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                }

                @Serializable
                data class MenuItemDto(
                    val name: String,
                    val description: String,
                    val price: String? = null,
                    val safetyRating: String,
                    val allergies: List<String> = emptyList(),
                    val dietaryRestrictions: List<String> = emptyList(),
                    val dislikes: List<String> = emptyList(),
                    val preferences: List<String> = emptyList(),
                    val recommendation: String? = null,
                    val rank: Int? = null,
                )

                @Serializable
                data class MenuAnalysisResponseDto(
                    val menuItems: List<MenuItemDto>
                )

                val responseDto = json.decodeFromString<MenuAnalysisResponseDto>(jsonString)

                responseDto.menuItems.map { dto ->
                    MenuItem(
                        name = dto.name,
                        description = dto.description,
                        price = dto.price,
                        safetyRating = when (dto.safetyRating.uppercase()) {
                            "RED" -> SafetyRating.RED
                            "YELLOW" -> SafetyRating.YELLOW
                            "GREEN" -> SafetyRating.GREEN
                            else -> SafetyRating.GREEN
                        },
                        allergies = dto.allergies,
                        dietaryRestrictions = dto.dietaryRestrictions,
                        dislikes = dto.dislikes,
                        preferences = dto.preferences,
                        recommendation = dto.recommendation,
                        rank = dto.rank ?: Int.MAX_VALUE,
                    )
                }.sortedBy { it.rank } // Sort by rank (best first)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing JSON response", e)
                Log.e(TAG, "Response was: $response")
                emptyList()
            }
    }
