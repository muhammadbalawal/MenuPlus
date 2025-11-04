package com.example.emptyactivity.ui

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