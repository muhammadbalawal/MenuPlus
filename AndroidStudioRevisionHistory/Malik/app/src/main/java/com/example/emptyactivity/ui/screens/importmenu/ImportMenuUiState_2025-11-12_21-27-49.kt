package com.example.emptyactivity.ui.screens.importmenu

/**
 * UI state model for the ImportMenu screen.
 *
 * This data class holds all the state information needed to render the menu analysis screen.
 * The screen displays OCR-extracted menu text and allows users to analyze it with Gemini AI
 * to get personalized recommendations based on their dietary profile.
 *
 * The analysis result is split into three distinct sections for the tab interface:
 * - Safe Menu: Only items marked as GREEN (safe to eat)
 * - Best Menu: Personalized recommendations based on user preferences
 * - Full Menu: Complete annotated menu with all safety ratings
 *
 * @param menuText The menu text extracted from OCR. Should be pre-populated when navigating
 *                 from the OCR screen. Empty string if no text is available.
 * @param isAnalyzing True when Gemini AI is analyzing the menu, false otherwise.
 * @param safeMenuContent Content for the "Safe Menu" tab - only items marked as safe (GREEN).
 * @param bestMenuContent Content for the "Best Menu" tab - personalized recommendations.
 * @param fullMenuContent Content for the "Full Menu" tab - complete annotated menu.
 * @param errorMessage Error message string if menu analysis fails. Null if no error has occurred.
 */
data class ImportMenuUiState(
    val menuText: String = "",
    val isAnalyzing: Boolean = false,
    val safeMenuContent: String? = null,
    val bestMenuContent: String? = null,
    val fullMenuContent: String? = null,
    val errorMessage: String? = null,
)