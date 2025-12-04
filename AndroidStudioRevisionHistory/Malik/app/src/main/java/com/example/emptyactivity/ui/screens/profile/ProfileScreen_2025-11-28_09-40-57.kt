package com.example.emptyactivity.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.components.ProfileFormScreen
import com.example.emptyactivity.ui.screens.onboarding.OnboardingViewModel

@Composable
fun ProfileScreen(
    user: User,
    onNavigateBack: () -> Unit = {},
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    // Load existing profile when screen opens
    LaunchedEffect(user.id) {
        viewModel.loadUserProfile(user.id)
    }

    ProfileFormScreen(
        user = user,
        viewModel = viewModel,
        title = "Edit Profile",
        subtitle = "Update your dietary preferences",
        buttonText = "Save Changes",
        onSaveComplete = onNavigateBack,
    )
}
