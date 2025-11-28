package com.example.emptyactivity.ui.screens.savedmenu

import com.example.emptyactivity.domain.model.Menu

data class SavedMenuUiState(
    val menus: List<Menu> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

