package com.example.emptyactivity.ui.screens.importmenu

import com.example.emptyactivity.domain.model.MenuItem

/**
 * UI state model for the ImportMenu screen.
 *
 * This data class holds all the state information needed to render the menu analysis screen.
 * The screen displays OCR-extracted menu text and allows users to analyze it with Gemini AI
 * to get personalized recommendations based on their dietary profile.
 *
 * The analysis result is a list of menu items ordered by what's best for the user.
 *
 * @param menuText The menu text extracted from OCR. Should be pre-populated when navigating
 *                 from the OCR screen. Empty string if no text is available.
 * @param isAnalyzing True when Gemini AI is analyzing the menu, false otherwise.
 * @param menuItems List of analyzed menu items, ordered by rank (best for user first).
 * @param errorMessage Error message string if menu analysis fails. Null if no error has occurred.
 * @param isSaving True when menu is being saved to database, false otherwise.
 * @param isSaved True when menu has been successfully saved, false otherwise.
 */
data class ImportMenuUiState(
    val menuText: String = "",
    val isAnalyzing: Boolean = false,
    val menuItems: List<MenuItem>? = null,
    val errorMessage: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
)
