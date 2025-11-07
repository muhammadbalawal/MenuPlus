package com.example.emptyactivity.ui.screens.onboarding

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getAllLanguagesUseCase: GetAllLanguagesUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        loadLanguages()
    }

    private fun loadLanguages(
    
}