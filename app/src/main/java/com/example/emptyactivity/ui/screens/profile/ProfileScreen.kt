package com.example.emptyactivity.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.components.ProfileFormScreen
import com.example.emptyactivity.ui.screens.onboarding.OnboardingViewModel

/**
 * Screen that allows users to view and edit their dietary profile.
 *
 * This screen provides access to the user's dietary preferences, allergies, restrictions,
 * dislikes, and language settings. It reuses the ProfileFormScreen component, which is also
 * used by the OnboardingScreen, ensuring a consistent editing experience.
 *
 * Features:
 * - Loads existing profile data when the screen opens
 * - Allows editing of all dietary information
 * - Saves changes to the database
 * - Navigation back to previous screen after saving
 *
 * The screen uses the OnboardingViewModel for state management, which handles both creating
 * new profiles (during onboarding) and updating existing profiles (from this screen).
 *
 * @param user The authenticated user whose profile is being edited.
 * @param onNavigateBack Callback function to navigate back to the previous screen. Called
 *                       after successfully saving profile changes.
 * @param viewModel The OnboardingViewModel managing the profile form state and save operations.
 *                  Injected via Hilt by default. Note: Despite the name, this ViewModel is
 *                  used for both onboarding and profile editing.
 */
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
