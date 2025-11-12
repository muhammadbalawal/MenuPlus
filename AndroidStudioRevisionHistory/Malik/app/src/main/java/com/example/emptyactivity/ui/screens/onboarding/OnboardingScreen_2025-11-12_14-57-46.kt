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
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigationEvent by viewModel.navigationEvent.collectAsStateWithLifecycle()

    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            OnboardingNavigationEvent.NavigateToMain -> {
                viewModel.onNavigationEventHandled()
                onComplete()
            }
            null -> { /* Do nothing */ }
        }
    }
        
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
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome to MenuPlus!") },
            )
        },
    ) { paddingValues ->
        if (uiState.isLoadingLanguages) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Text(
                    text = "Preferred Language *",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                var expanded by remember { mutableStateOf(false) }
                val selectedLanguage = uiState.languages.find { it.id == uiState.selectedLanguageId }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedLanguage?.name ?: "Select Language",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        uiState.languages.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(language.name) },
                                onClick = {
                                    viewModel.onLanguageSelected(language.id)
                                    expanded = false
                                },
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
                    currentInput = uiState.allergyInput,
                    onInputChange = viewModel::onAllergyInputChange,
                    onAddTag = viewModel::onAddAllergy,
                    onRemoveTag = viewModel::onRemoveAllergy,
                    tagColor = Color(0xFFE53935), // Red
                )

                Divider()

                // Dietary Restrictions Section
                TagInputSection(
                    title = "Dietary Restrictions",
                    subtitle = "Foods you avoid (vegan, halal, kosher, etc.)",
                    tags = uiState.dietaryRestrictions,
                    currentInput = uiState.dietaryRestrictionInput,
                    onInputChange = viewModel::onDietaryRestrictionInputChange,
                    onAddTag = viewModel::onAddDietaryRestriction,
                    onRemoveTag = viewModel::onRemoveDietaryRestriction,
                    tagColor = Color(0xFFE53935), // Red
                )

                Divider()

                // Dislikes Section
                TagInputSection(
                    title = "Dislikes",
                    subtitle = "Foods you prefer not to eat",
                    tags = uiState.dislikes,
                    currentInput = uiState.dislikeInput,
                    onInputChange = viewModel::onDislikeInputChange,
                    onAddTag = viewModel::onAddDislike,
                    onRemoveTag = viewModel::onRemoveDislike,
                    tagColor = Color(0xFFFDD835), // Yellow
                )

                Divider()

                // Preferences Section
                TagInputSection(
                    title = "Preferences",
                    subtitle = "Foods you especially enjoy",
                    tags = uiState.preferences,
                    currentInput = uiState.preferenceInput,
                    onInputChange = viewModel::onPreferenceInputChange,
                    onAddTag = viewModel::onAddPreference,
                    onRemoveTag = viewModel::onRemovePreference,
                    tagColor = Color(0xFF43A047), // Green
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = { viewModel.onSaveProfile(user) },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    enabled = !uiState.isSaving && uiState.selectedLanguageId.isNotBlank(),
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Text("Complete Setup")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun TagInputSection(
    title: String,
    subtitle: String,
    tags: List<String>,
    currentInput: String,
    onInputChange: (String) -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit,
    tagColor: Color,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        // Input Field
        OutlinedTextField(
            value = currentInput,
            onValueChange = onInputChange,
            label = { Text("Add $title") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions =
                KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
            keyboardActions =
                KeyboardActions(
                    onDone = {
                        onAddTag()
                    },
                ),
            trailingIcon = {
                if (currentInput.isNotBlank()) {
                    TextButton(onClick = onAddTag) {
                        Text("Add")
                    }
                }
            },
            singleLine = true,
        )

        if (tags.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                tags.forEach { tag ->
                    TagChip(
                        text = tag,
                        color = tagColor,
                        onRemove = { onRemoveTag(tag) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagChip(
    text: String,
    color: Color,
    onRemove: () -> Unit,
) {
    InputChip(
        selected = false,
        onClick = { },
        label = { Text(text) },
        trailingIcon = {
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(20.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove $text",
                    modifier = Modifier.size(16.dp),
                )
            }
        },
        colors =
            InputChipDefaults.inputChipColors(
                containerColor = color.copy(alpha = 0.2f),
                labelColor = color.copy(alpha = 0.8f),
            ),
        border =
            InputChipDefaults.inputChipBorder(
                borderColor = color,
                borderWidth = 1.dp,
            ),
    )
}
