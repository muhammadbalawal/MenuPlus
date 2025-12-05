package com.example.emptyactivity.ui.screens.savedmenu

import com.example.emptyactivity.domain.model.Menu

/**
 * UI state for the SavedMenuDetailScreen.
 *
 * This data class represents all the observable state needed by the SavedMenuDetailScreen composable.
 * It follows the unidirectional data flow pattern where the ViewModel updates this state and
 * the UI reacts to changes.
 *
 * @param menu The menu being displayed. Null when loading or if the menu doesn't exist.
 * @param isLoading Indicates whether the menu is currently being loaded from the database.
 *                  When true, a loading indicator should be displayed.
 * @param isDeleting Indicates whether a delete operation is currently in progress. When true,
 *                   the delete button should show a loading indicator.
 * @param isDeleted Indicates whether the menu was successfully deleted. When true, the UI
 *                  should navigate back to the saved menus list.
 * @param errorMessage Optional error message to display to the user. When not null, an error
 *                     dialog should be shown with this message. Set to null when the error
 *                     is dismissed or when a new operation starts.
 */
data class SavedMenuDetailUiState(
    val menu: Menu? = null,
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val isDeleted: Boolean = false,
    val errorMessage: String? = null,
)

