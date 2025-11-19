package com.example.emptyactivity.ui.screens.onboarding

import com.example.emptyactivity.domain.model.Language

/**
 * UI state for the onboarding screen.
 *
 * This data class holds all the state needed for the onboarding form, including language selection,
 * dietary preferences input, loading states, and error messages. It is managed by OnboardingViewModel
 * and observed by OnboardingScreen.
 *
 * @param languages List of available languages that the user can select from.
 * @param selectedLanguageId The ID of the currently selected language.
 * @param allergies List of allergies the user has added.
 * @param dietaryRestrictions List of dietary restrictions the user has added.
 * @param dislikes List of foods the user dislikes.
 * @param preferences List of foods the user prefers.
 * @param allergyInput Current text input for adding a new allergy.
 * @param dietaryRestrictionInput Current text input for adding a new dietary restriction.
 * @param dislikeInput Current text input for adding a new dislike.
 * @param preferenceInput Current text input for adding a new preference.
 * @param isLoading True when saving the profile is in progress.
 * @param isLoadingLanguages True when loading available languages is in progress.
 * @param isSaving True when the profile save operation is in progress.
 * @param errorMessage Error message to display if any operation fails, null if no error.
 */
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
    val errorMessage: String? = null,
)
