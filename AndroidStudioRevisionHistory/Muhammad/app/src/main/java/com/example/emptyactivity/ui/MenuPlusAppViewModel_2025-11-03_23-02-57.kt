package com.example.emptyactivity.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.usecase.auth.ObserveAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MenuPlusAppViewModel @Inject constructor(
    observeAuthStateUseCase: ObserveAuthStateUseCase
) : ViewModel() {
    
    val uiState: StateFlow<MenuPlusAppUiState> = 
        observeAuthStateUseCase()
            .map { user ->
                when {
                    user == null -> MenuPlusAppUiState.NotAuthenticated
                    !user.hasCompletedOnboarding -> MenuPlusAppUiState.NeedsOnboarding(user)
                    else -> MenuPlusAppUiState.Authenticated(user)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MenuPlusAppUiState.Loading
            )
}