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

/**
 * ViewModel for managing application-level UI state.
 *
 * This ViewModel observes authentication state changes and maps them to application UI states.
 * It determines which navigation graph should be displayed based on whether the user is
 * authenticated and whether they have completed onboarding.
 *
 * The ViewModel exposes a StateFlow that emits MenuPlusAppUiState, which is observed by
 * the MenuPlusApp composable to determine which screens to show.
 *
 * @param observeAuthStateUseCase The use case for observing authentication state changes.
 */
@HiltViewModel
class MenuPlusAppViewModel
    @Inject
    constructor(
        observeAuthStateUseCase: ObserveAuthStateUseCase,
    ) : ViewModel() {
        /**
         * The current application UI state based on authentication and onboarding status.
         *
         * This StateFlow emits:
         * - Loading: While checking authentication status
         * - NotAuthenticated: When no user is logged in
         * - NeedsOnboarding: When user is authenticated but hasn't completed onboarding
         * - Authenticated: When user is authenticated and has completed onboarding
         *
         * The state is cached and shared, stopping after 5 seconds of no subscribers.
         */
        val uiState: StateFlow<MenuPlusAppUiState> = 
            observeAuthStateUseCase()
                .map { user ->
                    when {
                        user == null -> MenuPlusAppUiState.NotAuthenticated
                        !user.hasCompletedOnboarding -> MenuPlusAppUiState.NeedsOnboarding(user)
                        else -> MenuPlusAppUiState.Authenticated(user)
                    }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = MenuPlusAppUiState.Loading,
                )
    }
