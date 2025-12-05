package com.example.emptyactivity.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.domain.usecase.profile.GetAllLanguagesUseCase
import com.example.emptyactivity.domain.usecase.profile.GetUserProfileUseCase
import com.example.emptyactivity.domain.usecase.profile.SaveUserProfileUseCase
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the OnboardingScreen.
 *
 * This ViewModel manages the state and business logic for the onboarding flow, where new
 * users set up their dietary profile. It handles loading available languages, managing form
 * input, and saving the user profile to the database.
 *
 * The ViewModel:
 * - Loads available languages on initialization
 * - Manages form state for language selection and dietary tags
 * - Handles tag addition/removal for allergies, restrictions, dislikes, and preferences
 * - Saves the complete profile to the database
 * - Manages loading states during operations
 * - Handles errors and exposes error messages to the UI
 * - Emits navigation events when onboarding is complete
 *
 * @param getAllLanguagesUseCase The use case for fetching available languages from the database.
 * @param saveUserProfileUseCase The use case for saving the user's dietary profile.
 * @param getUserProfileUseCase The use case for loading an existing profile (used when editing).
 *                              All injected via Hilt.
 */
@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        private val getAllLanguagesUseCase: GetAllLanguagesUseCase,
        private val saveUserProfileUseCase: SaveUserProfileUseCase,
        private val getUserProfileUseCase: GetUserProfileUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(OnboardingUiState())
        val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

        private val _navigationEvent = MutableStateFlow<OnboardingNavigationEvent?>(null)
        val navigationEvent: StateFlow<OnboardingNavigationEvent?> = _navigationEvent.asStateFlow()


        init {
            loadLanguages()
        }

        private fun loadLanguages() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoadingLanguages = true) }

                when (val result = getAllLanguagesUseCase()) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                languages = result.data,
                                isLoadingLanguages = false,
                                selectedLanguageId = result.data.firstOrNull()?.id ?: "",
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingLanguages = false,
                                errorMessage = result.message,
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoadingLanguages = true) }
                    }
                }
            }
        }

        fun onLanguageSelected(languageId: String) {
            _uiState.update { it.copy(selectedLanguageId = languageId, errorMessage = null) }
        }

        // Separate input change functions for each section
        fun onAllergyInputChange(input: String) {
            _uiState.update { it.copy(allergyInput = input) }
        }

        fun onDietaryRestrictionInputChange(input: String) {
            _uiState.update { it.copy(dietaryRestrictionInput = input) }
        }

        fun onDislikeInputChange(input: String) {
            _uiState.update { it.copy(dislikeInput = input) }
        }

        fun onPreferenceInputChange(input: String) {
            _uiState.update { it.copy(preferenceInput = input) }
        }

        // Updated add functions to use their respective inputs
        fun onAddAllergy() {
            val input = uiState.value.allergyInput.trim()
            if (input.isNotBlank() && !uiState.value.allergies.contains(input)) {
                _uiState.update {
                    it.copy(
                        allergies = it.allergies + input,
                        allergyInput = "", 
                    )
                }
            }
        }

        fun onRemoveAllergy(allergy: String) {
            _uiState.update {
                it.copy(allergies = it.allergies - allergy)
            }
        }

        fun onAddDietaryRestriction() {
            val input = uiState.value.dietaryRestrictionInput.trim()
            if (input.isNotBlank() && !uiState.value.dietaryRestrictions.contains(input)) {
                _uiState.update {
                    it.copy(
                        dietaryRestrictions = it.dietaryRestrictions + input,
                        dietaryRestrictionInput = "",
                    )
                }
            }
        }

        fun onRemoveDietaryRestriction(restriction: String) {
            _uiState.update {
                it.copy(dietaryRestrictions = it.dietaryRestrictions - restriction)
            }
        }

        fun onAddDislike() {
            val input = uiState.value.dislikeInput.trim()
            if (input.isNotBlank() && !uiState.value.dislikes.contains(input)) {
                _uiState.update {
                    it.copy(
                        dislikes = it.dislikes + input,
                        dislikeInput = "", 
                    )
                }
            }
        }

        fun onRemoveDislike(dislike: String) {
            _uiState.update {
                it.copy(dislikes = it.dislikes - dislike)
            }
        }

        fun onAddPreference() {
            val input = uiState.value.preferenceInput.trim()
            if (input.isNotBlank() && !uiState.value.preferences.contains(input)) {
                _uiState.update {
                    it.copy(
                        preferences = it.preferences + input,
                        preferenceInput = "",
                    )
                }
            }
        }

        fun onRemovePreference(preference: String) {
            _uiState.update {
                it.copy(preferences = it.preferences - preference)
            }
        }

        fun onErrorDismissed() {
            _uiState.update { it.copy(errorMessage = null) }
        }

        fun loadUserProfile(userId: String) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoadingLanguages = true) }

                when (val result = getUserProfileUseCase(userId)) {
                    is Result.Success -> {
                        val profile = result.data
                        _uiState.update {
                            it.copy(
                                selectedLanguageId = profile?.preferredLanguageId ?: it.selectedLanguageId,
                                allergies = profile?.allergies ?: emptyList(),
                                dietaryRestrictions = profile?.dietaryRestrictions ?: emptyList(),
                                dislikes = profile?.dislikes ?: emptyList(),
                                preferences = profile?.preferences ?: emptyList(),
                                isLoadingLanguages = false,
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingLanguages = false,
                                errorMessage = result.message,
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoadingLanguages = true) }
                    }
                }
            }
        }

        fun onSaveProfile(user: User, onSaveComplete: () -> Unit = {}) {
            viewModelScope.launch {
                _uiState.update { it.copy(isSaving = true, errorMessage = null) }

                val state = uiState.value
                when (
                    val result =
                        saveUserProfileUseCase(
                            userId = user.id,
                            preferredLanguageId = state.selectedLanguageId,
                            allergies = state.allergies,
                            dietaryRestrictions = state.dietaryRestrictions,
                            dislikes = state.dislikes,
                            preferences = state.preferences,
                        )
                ) {
                    is Result.Success -> {
                        _uiState.update { it.copy(isSaving = false, isLoading = false) }
                        // Call callback if provided, otherwise use navigation event
                        if (onSaveComplete != {}) {
                            onSaveComplete()
                        } else {
                            _navigationEvent.value = OnboardingNavigationEvent.NavigateToMain
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isSaving = false,
                                errorMessage = result.message,
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isSaving = true) }
                    }
                }
            }
        }

        fun onNavigationEventHandled() {
            _navigationEvent.value = null
        }
    }

sealed interface OnboardingNavigationEvent {
    data object NavigateToMain : OnboardingNavigationEvent
}

