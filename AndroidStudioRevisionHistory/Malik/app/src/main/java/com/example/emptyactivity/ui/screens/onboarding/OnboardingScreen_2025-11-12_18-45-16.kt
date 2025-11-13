package com.example.emptyactivity.ui.screens.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

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
                    Text("OK", color = RoyalGold)
                }
            },
            containerColor = PrestigeBlack,
            titleContentColor = RoyalGold,
            textContentColor = Color.White.copy(alpha = 0.9f),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrestigeBlack)
            .safeDrawingPadding(),
    ) {
        // Background glow effect
        Canvas(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-50).dp)
                .size(350.dp)
        ) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        RoyalGold.copy(alpha = 0.15f),
                        RoyalGold.copy(alpha = 0.08f),
                        Color.Transparent,
                    ),
                    radius = size.minDimension / 2f
                ),
                radius = size.minDimension / 2f,
                center = center
            )
        }

        if (uiState.isLoadingLanguages) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = RoyalGold)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Title with gradient
                Text(
                    text = "Welcome to MenuPlus!",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF7A5A00),
                                RoyalGold,
                                Color(0xFFFFF4C8),
                                Color(0xFFD4AF37),
                            )
                        ),
                        shadow = Shadow(
                            color = Color(0xAA8B7500),
                            offset = Offset(1f, 1f),
                            blurRadius = 4f
                        )
                    ),
                    color = Color.Unspecified,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Let's personalize your dining experience",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Language Selection
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Preferred Language *",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = RoyalGold,
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
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = RoyalGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                textColor = Color.White,
                                cursorColor = RoyalGold,
                                focusedLabelColor = Color.White.copy(alpha = 0.6f),
                                unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(PrestigeBlack),
                        ) {
                            uiState.languages.forEach { language ->
                                DropdownMenuItem(
                                    text = { Text(language.name, color = Color.White) },
                                    onClick = {
                                        viewModel.onLanguageSelected(language.id)
                                        expanded = false
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = Color.White,
                                    ),
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                GoldDivider()
                Spacer(modifier = Modifier.height(8.dp))

                // Allergies Section (RED)
                TagInputSection(
                    title = "Allergies",
                    subtitle = "Foods that cause allergic reactions",
                    tags = uiState.allergies,
                    currentInput = uiState.allergyInput,
                    onInputChange = viewModel::onAllergyInputChange,
                    onAddTag = viewModel::onAddAllergy,
                    onRemoveTag = viewModel::onRemoveAllergy,
                    tagColor = Color(0xFFE53935), // Red for danger
                )

                Spacer(modifier = Modifier.height(8.dp))
                GoldDivider()
                Spacer(modifier = Modifier.height(8.dp))

                // Dietary Restrictions Section (ORANGE)
                TagInputSection(
                    title = "Dietary Restrictions",
                    subtitle = "Foods you avoid (vegan, halal, kosher, etc.)",
                    tags = uiState.dietaryRestrictions,
                    currentInput = uiState.dietaryRestrictionInput,
                    onInputChange = viewModel::onDietaryRestrictionInputChange,
                    onAddTag = viewModel::onAddDietaryRestriction,
                    onRemoveTag = viewModel::onRemoveDietaryRestriction,
                    tagColor = Color(0xFFFF6F00), // Orange for warning
                )

                Spacer(modifier = Modifier.height(8.dp))
                GoldDivider()
                Spacer(modifier = Modifier.height(8.dp))

                // Dislikes Section (YELLOW)
                TagInputSection(
                    title = "Dislikes",
                    subtitle = "Foods you prefer not to eat",
                    tags = uiState.dislikes,
                    currentInput = uiState.dislikeInput,
                    onInputChange = viewModel::onDislikeInputChange,
                    onAddTag = viewModel::onAddDislike,
                    onRemoveTag = viewModel::onRemoveDislike,
                    tagColor = Color(0xFFFDD835), // Yellow for caution
                )

                Spacer(modifier = Modifier.height(8.dp))
                GoldDivider()
                Spacer(modifier = Modifier.height(8.dp))

                // Preferences Section (GREEN)
                TagInputSection(
                    title = "Preferences",
                    subtitle = "Foods you especially enjoy",
                    tags = uiState.preferences,
                    currentInput = uiState.preferenceInput,
                    onInputChange = viewModel::onPreferenceInputChange,
                    onAddTag = viewModel::onAddPreference,
                    onRemoveTag = viewModel::onRemovePreference,
                    tagColor = Color(0xFF43A047), // Green for positive
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Save Button
                Button(
                    onClick = { viewModel.onSaveProfile(user) },
                    enabled = !uiState.isSaving && uiState.selectedLanguageId.isNotBlank(),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RoyalGold,
                        contentColor = PrestigeBlack,
                        disabledContainerColor = RoyalGold.copy(alpha = 0.5f),
                        disabledContentColor = PrestigeBlack.copy(alpha = 0.5f),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = PrestigeBlack,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(
                            text = "Complete Setup",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun GoldDivider() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    RoyalGold.copy(alpha = 0.3f),
                    RoyalGold.copy(alpha = 0.6f),
                    RoyalGold.copy(alpha = 0.3f),
                    Color.Transparent,
                )
            ),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 2f
        )
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
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = RoyalGold,
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f),
        )

        // Input Field
        OutlinedTextField(
            value = currentInput,
            onValueChange = onInputChange,
            label = { Text("Add $title", color = Color.White.copy(alpha = 0.6f)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = RoyalGold,
                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                textColor = Color.White,
                cursorColor = RoyalGold,
                focusedLabelColor = Color.White.copy(alpha = 0.6f),
                unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
            ),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAddTag()
                },
            ),
            trailingIcon = {
                if (currentInput.isNotBlank()) {
                    TextButton(onClick = onAddTag) {
                        Text("Add", color = RoyalGold, fontWeight = FontWeight.Bold)
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
        label = { Text(text, fontWeight = FontWeight.Medium) },
        trailingIcon = {
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(20.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove $text",
                    modifier = Modifier.size(16.dp),
                    tint = color
                )
            }
        },
        colors = InputChipDefaults.inputChipColors(
            containerColor = color.copy(alpha = 0.15f),
            labelColor = color,
        ),
        border = InputChipDefaults.inputChipBorder(
            borderColor = color.copy(alpha = 0.5f),
            borderWidth = 1.5.dp,
        ),
        shape = RoundedCornerShape(20.dp),
    )
}
