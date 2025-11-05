package com.example.emptyactivity.ui.screens.onboarding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emptyactivity.domain.model.User

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    user: User,
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Show error dialog
    if (uiState.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onErrorDismissed() },
            title = { Text("Error") },
            text = { Text(uiState.errorMessage ?: "") },
            confirmButton = {
                TextButton(onClick = { viewModel.onErrorDismissed() }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome to MenuPlus!") }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoadingLanguages) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Welcome Text
                Text(
                    text = "Let's personalize your experience",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Tell us about your dietary preferences so we can help you eat safely.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Language Selection
                Text(
                    text = "Preferred Language *",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                var expanded by remember { mutableStateOf(false) }
                val selectedLanguage = uiState.languages.find { it.id == uiState.selectedLanguageId }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedLanguage?.name ?: "Select Language",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        uiState.languages.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(language.name) },
                                onClick = {
                                    viewModel.onLanguageSelected(language.id)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Divider()

                // Allergies Section
                TagInputSection(
                    title = "Allergies",
                    subtitle = "Foods that cause allergic reactions",
                    tags = uiState.allergies,
                    currentInput = uiState.currentInput,
                    onInputChange = viewModel::onCurrentInputChange,
                    onAddTag = viewModel::onAddAllergy,
                    onRemoveTag = viewModel::onRemoveAllergy,
                    tagColor = Color(0xFFE53935) // Red
                )

                Divider()

                // Dietary Restrictions Section
                TagInputSection(
                    title = "Dietary Restrictions",
                    subtitle = "Foods you avoid (vegan, halal, kosher, etc.)",
                    tags = uiState.dietaryRestrictions,
                    currentInput = uiState.currentInput,
                    onInputChange = viewModel::onCurrentInputChange,
                    onAddTag = viewModel::onAddDietaryRestriction,
                    onRemoveTag = viewModel::onRemoveDietaryRestriction,
                    tagColor = Color(0xFFE53935) // Red
                )

                Divider()

                // Dislikes Section
                TagInputSection(
                    title = "Dislikes",
                    subtitle = "Foods you prefer not to eat",
                    tags = uiState.dislikes,
                    currentInput = uiState.currentInput,
                    onInputChange = viewModel::onCurrentInputChange,
                    onAddTag = viewModel::onAddDislike,
                    onRemoveTag = viewModel::onRemoveDislike,
                    tagColor = Color(0xFFFDD835) // Yellow
                )

                Divider()

                // Preferences Section
                TagInputSection(
                    title = "Preferences",
                    subtitle = "Foods you especially enjoy",
                    tags = uiState.preferences,
                    currentInput = uiState.currentInput,
                    onInputChange = viewModel::onCurrentInputChange,
                    onAddTag = viewModel::onAddPreference,
                    onRemoveTag = viewModel::onRemovePreference,
                    tagColor = Color(0xFF43A047) // Green
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = { viewModel.onSaveProfile(user) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !uiState.isSaving && uiState.selectedLanguageId.isNotBlank()
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Complete Setup")
                    }
                }
            }
        }
    }
}