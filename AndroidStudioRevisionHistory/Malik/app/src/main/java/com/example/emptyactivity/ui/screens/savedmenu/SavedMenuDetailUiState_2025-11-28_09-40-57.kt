package com.example.emptyactivity.ui.screens.savedmenu

import com.example.emptyactivity.domain.model.Menu

data class SavedMenuDetailUiState(
    val menu: Menu? = null,
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val isDeleted: Boolean = false,
    val errorMessage: String? = null,
)

