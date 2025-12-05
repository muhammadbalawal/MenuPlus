package com.example.emptyactivity.ui.screens.savedmenu

import com.example.emptyactivity.domain.model.Menu

/**
 * UI state for the SavedMenuScreen.
 *
 * This data class represents all the observable state needed by the SavedMenuScreen composable.
 * It follows the unidirectional data flow pattern where the ViewModel updates this state and
 * the UI reacts to changes.
 *
 * @param menus The list of saved menus to display. Empty list when no menus are available
 *              or when loading has not completed.
 * @param isLoading Indicates whether menus are currently being loaded from the database.
 *                  When true, a loading indicator should be displayed.
 * @param errorMessage Optional error message to display to the user. When not null, an error
 *                     dialog should be shown with this message. Set to null when the error
 *                     is dismissed or when a new operation starts.
 *
 * Mostly created by: Malik
 */
data class SavedMenuUiState(
    val menus: List<Menu> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

