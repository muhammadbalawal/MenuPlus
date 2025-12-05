package com.example.emptyactivity.ui.screens.onboarding

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.components.ProfileFormScreen

/**
 * Screen that guides new users through setting up their dietary profile.
 *
 * This screen is shown to newly registered users who haven't completed their dietary profile
 * setup. It collects essential information needed for personalized menu analysis:
 * - Preferred language for menu translation
 * - Allergies (critical safety information)
 * - Dietary restrictions (vegan, halal, kosher, etc.)
 * - Food dislikes
 * - Food preferences
 *
 * The screen uses the ProfileFormScreen component, which is also used by the ProfileScreen
 * for editing existing profiles. After successful completion, the user is automatically
 * navigated to the main authenticated screens.
 *
 * Features:
 * - Language selection dropdown
 * - Tag-based input for dietary information
 * - Color-coded sections (red for allergies, orange for restrictions, yellow for dislikes, green for preferences)
 * - Loading states during language loading and profile saving
 * - Error handling
 * - Support for deep link language pre-selection
 *
 * @param user The newly registered user who needs to complete onboarding.
 * @param onComplete Callback function called when onboarding is successfully completed.
 *                   Currently unused as navigation is handled by authentication state.
 * @param viewModel The ViewModel managing the onboarding form state and profile saving logic.
 *                  Injected via Hilt by default.
 * @param initialLanguage Optional language name to pre-select. Useful for deep links that
 *                        specify a language preference.
 *
 * Mostly created by: Malik
 */
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

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            // Check if this was launched for result (from launcher app via deep link)
            val isLaunchedForResult = activity?.intent?.action == Intent.ACTION_VIEW
            
            if (isLaunchedForResult && activity != null) {
                // Return result and finish immediately
                val selectedLanguageName =
                    uiState.languages
                        .find { 
                            it.id == uiState.selectedLanguageId 
                        }?.name ?: "Unknown"
                
                val resultData =
                    """
                    Onboarding Completed Successfully!
                    Language: $selectedLanguageName
                    Allergies: ${uiState.allergies.joinToString(", ").ifEmpty { "None" }}
                    Dietary Restrictions: ${uiState.dietaryRestrictions.joinToString(", ").ifEmpty { "None" }}
                    Dislikes: ${uiState.dislikes.joinToString(", ").ifEmpty { "None" }}
                    Preferences: ${uiState.preferences.joinToString(", ").ifEmpty { "None" }}
                    """.trimIndent()

                val resultIntent =
                    activity.intent.apply {
                        putExtra("resultData", resultData)
                    }
                activity.setResult(Activity.RESULT_OK, resultIntent)
                activity.finish()
            }
            onComplete()
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
