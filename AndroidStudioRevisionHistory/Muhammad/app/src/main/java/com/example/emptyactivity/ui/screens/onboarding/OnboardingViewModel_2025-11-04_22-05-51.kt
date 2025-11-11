package com.example.emptyactivity.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.domain.usecase.profile.GetAllLanguagesUseCase
import com.example.emptyactivity.domain.usecase.profile.SaveUserProfileUseCase
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getAllLanguagesUseCase: GetAllLanguagesUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

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
                            selectedLanguageId = result.data.firstOrNull()?.id ?: ""
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingLanguages = false,
                            errorMessage = result.message
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

    fun onCurrentInputChange(input: String) {
        _uiState.update { it.copy(currentInput = input) }
    }

    fun onAddAllergy() {
        val input = uiState.value.currentInput.trim()
        if (input.isNotBlank() && !uiState.value.allergies.contains(input)) {
            _uiState.update {
                it.copy(
                    allergies = it.allergies + input,
                    currentInput = ""
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
        val input = uiState.value.currentInput.trim()
        if (input.isNotBlank() && !uiState.value.dietaryRestrictions.contains(input)) {
            _uiState.update {
                it.copy(
                    dietaryRestrictions = it.dietaryRestrictions + input,
                    currentInput = ""
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
        val input = uiState.value.currentInput.trim()
        if (input.isNotBlank() && !uiState.value.dislikes.contains(input)) {
            _uiState.update {
                it.copy(
                    dislikes = it.dislikes + input,
                    currentInput = ""
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
        val input = uiState.value.currentInput.trim()
        if (input.isNotBlank() && !uiState.value.preferences.contains(input)) {
            _uiState.update {
                it.copy(
                    preferences = it.preferences + input,
                    currentInput = ""
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

    fun onSaveProfile(user: User) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }

            val state = uiState.value
            when (val result = saveUserProfileUseCase(
                userId = user.id,
                preferredLanguageId = state.selectedLanguageId,
                allergies = state.allergies,
                dietaryRestrictions = state.dietaryRestrictions,
                dislikes = state.dislikes,
                preferences = state.preferences
            )) {
                is Result.Success -> {
                    _uiState.update { it.copy(isSaving = false, isLoading = false) }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = result.message
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isSaving = true) }
                }
            }
        }
    }
}

