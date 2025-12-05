package com.example.emptyactivity.ui.screens.onboarding

import com.example.emptyactivity.domain.model.Language

/**
 * UI state for the OnboardingScreen.
 *
 * This data class represents all the observable state needed by the OnboardingScreen composable.
 * It follows the unidirectional data flow pattern where the ViewModel updates this state and
 * the UI reacts to changes.
 *
 * @param languages List of available languages that can be selected. Loaded from the database
 *                  on ViewModel initialization.
 * @param selectedLanguageId The ID of the currently selected language. Must not be blank
 *                           before saving the profile.
 * @param allergies List of allergy tags entered by the user. These are critical safety
 *                  items that will cause menu items to be marked RED.
 * @param dietaryRestrictions List of dietary restriction tags (e.g., "vegan", "halal").
 *                            Items violating these will be marked RED.
 * @param dislikes List of disliked food tags. Items containing these will be marked YELLOW.
 * @param preferences List of preferred food tags. Items matching these will be highlighted
 *                    and recommended.
 * @param allergyInput Current text input for adding a new allergy tag.
 * @param dietaryRestrictionInput Current text input for adding a new dietary restriction tag.
 * @param dislikeInput Current text input for adding a new dislike tag.
 * @param preferenceInput Current text input for adding a new preference tag.
 * @param isLoading Indicates whether the profile is currently being saved. When true,
 *                  the save button should show a loading indicator.
 * @param isLoadingLanguages Indicates whether languages are currently being loaded. When true,
 *                           a loading indicator should be displayed.
 * @param isSaving Indicates whether a save operation is in progress. Used internally
 *                 for state management.
 * @param errorMessage Optional error message to display to the user. When not null, an error
 *                     dialog should be shown with this message. Set to null when the error
 *                     is dismissed or when a new operation starts.
 *
 * Mostly created by: Muhammad
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
