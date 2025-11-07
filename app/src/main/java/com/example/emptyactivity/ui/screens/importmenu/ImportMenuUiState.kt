package com.example.emptyactivity.ui.screens.importmenu

/**
 * UI state model for the ImportMenu screen.
 *
 * This data class holds all the state information needed to render the menu analysis screen.
 * The screen displays OCR-extracted menu text and allows users to analyze it with Gemini AI
 * to get personalized recommendations based on their dietary profile.
 *
 * @param menuText The menu text extracted from OCR. Should be pre-populated when navigating
 *                 from the OCR screen. Empty string if no text is available.
 * @param isAnalyzing True when Gemini AI is analyzing the menu, false otherwise.
 * @param analysisResult The personalized menu analysis result from Gemini AI. Contains safety
 *                       ratings, allergy warnings, and recommendations. Null until analysis completes.
 * @param errorMessage Error message string if menu analysis fails. Null if no error has occurred.
 */
data class ImportMenuUiState(
    val menuText: String = "",
    val isAnalyzing: Boolean = false,
    val analysisResult: String? = null,
    val errorMessage: String? = null,
)

