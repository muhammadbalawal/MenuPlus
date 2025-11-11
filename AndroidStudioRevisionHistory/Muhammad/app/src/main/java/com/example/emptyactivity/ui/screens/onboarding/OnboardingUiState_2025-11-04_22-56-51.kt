package com.example.emptyactivity.ui.screens.onboarding

import com.example.emptyactivity.domain.model.Language

data class OnboardingUiState(
    val languages: List<Language> = emptyList(),
    val selectedLanguageId: String = "",
    val allergies: List<String> = emptyList(),
    val dietaryRestrictions: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val preferences: List<String> = emptyList(),
    val allergyInput: String = "",
    val dietaryRestrictionInput: String = "",
    val dislikeInput: String = "",
    val preferenceInput: String = "",
    val isLoading: Boolean = false,
    val isLoadingLanguages: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)