package com.example.emptyactivity.ui.screens.aboutus

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel for the AboutUsScreen.
 *
 * This ViewModel manages the state for the About Us screen. Since this screen displays
 * static information about the team and app motivation, the ViewModel is minimal and
 * primarily serves as a state holder for consistency with other screens in the app.
 *
 * The ViewModel provides:
 * - Reactive state via StateFlow for UI observation
 * - Consistent architecture pattern across the app
 *
 * Mostly created by: Malik
 */
@HiltViewModel
class AboutUsViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(AboutUsUiState())
        val uiState: StateFlow<AboutUsUiState> = _uiState.asStateFlow()
    }

