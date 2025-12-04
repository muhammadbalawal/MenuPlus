package com.example.emptyactivity.ui.screens.menuanalysis

/**
 * UI state for the MenuAnalysisScreen.
 *
 * This data class represents all the observable state needed by the MenuAnalysisScreen composable.
 * It follows the unidirectional data flow pattern where the ViewModel updates this state and
 * the UI reacts to changes.
 *
 * @param isSaving Indicates whether a save operation is currently in progress. When true,
 *                 the save button should show a loading indicator.
 * @param isSaved Indicates whether the menu was successfully saved. When true, the save button
 *                should show a success checkmark instead of the "Save" text.
 * @param errorMessage Optional error message to display to the user. When not null, an error
 *                     dialog should be shown with this message. Set to null when the error
 *                     is dismissed or when a new operation starts.
 */
data class MenuAnalysisUiState(
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
)
