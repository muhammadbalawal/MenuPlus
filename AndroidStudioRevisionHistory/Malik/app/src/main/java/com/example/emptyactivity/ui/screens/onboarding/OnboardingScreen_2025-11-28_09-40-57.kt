package com.example.emptyactivity.ui.screens.onboarding

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.components.ProfileFormScreen

@Composable
fun OnboardingScreen(
    user: User,
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
    initialLanguage: String? = null,
) {
    val localContext = LocalContext.current
    val activity = localContext as? Activity

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigationEvent by viewModel.navigationEvent.collectAsStateWithLifecycle()


    LaunchedEffect(initialLanguage, uiState.languages) {
        if (!initialLanguage.isNullOrBlank()) {
            val language = uiState.languages.find { it.name.equals(initialLanguage, ignoreCase = true) }
            language?.let { viewModel.onLanguageSelected(it.id) }
        }
    }

    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            OnboardingNavigationEvent.NavigateToMain -> {
                viewModel.onNavigationEventHandled()
                val selectedLanguageName = uiState.languages.find { it.id == uiState.selectedLanguageId }?.name ?: "Unknown"
                val resultData =
                    """
                    Onboarding Completed Successfully!
                    Language: $selectedLanguageName
                    Allergies: ${uiState.allergies.joinToString(", ")}
                    Dietary Restrictions: ${uiState.dietaryRestrictions.joinToString(", ")}
                    Dislikes: ${uiState.dislikes.joinToString(", ")}
                    Preferences: ${uiState.preferences.joinToString(", ")}
                    """.trimIndent()

                activity?.let { safeActivity ->
                    val resultIntent = safeActivity.intent
                    resultIntent.putExtra("resultData", resultData)
                    safeActivity.setResult(Activity.RESULT_OK, resultIntent)
                    safeActivity.finish()
                }

                onComplete()
            }
            null -> { }
        }
    }

    ProfileFormScreen(
        user = user,
        viewModel = viewModel,
        title = "Welcome to MenuPlus!",
        subtitle = "Let's personalize your dining experience",
        buttonText = "Complete Setup",
        onSaveComplete = {
            // Navigation handled by navigationEvent
        },
    )
}
