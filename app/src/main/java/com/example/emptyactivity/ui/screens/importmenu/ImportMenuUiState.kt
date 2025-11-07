package com.example.emptyactivity.ui.screens.importmenu

data class ImportMenuUiState(
    val menuText: String = "",
    val isAnalyzing: Boolean = false,
    val analysisResult: String? = null,
    val errorMessage: String? = null,
)

