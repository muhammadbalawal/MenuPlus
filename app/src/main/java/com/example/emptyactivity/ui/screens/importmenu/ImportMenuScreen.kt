package com.example.emptyactivity.ui.screens.importmenu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emptyactivity.domain.model.User

/**
 * ImportMenu screen composable for analyzing menu text with Gemini AI.
 *
 * This screen displays OCR-extracted menu text and allows users to get personalized
 * menu analysis using Gemini AI. The analysis considers the user's dietary profile:
 * - Allergies (critical safety warnings)
 * - Dietary restrictions (vegan, halal, kosher, etc.)
 * - Dislikes (preferences to avoid)
 * - Preferences (foods they enjoy)
 * - Preferred language (for menu translation)
 *
 * The screen workflow:
 * 1. Receives menu text from OCR screen via navigation parameter
 * 2. Displays the extracted menu text (read-only)
 * 3. User clicks "Analyze Menu with AI" button
 * 4. Shows loading state while Gemini processes the menu
 * 5. Displays personalized analysis with safety ratings and recommendations
 *
 * The screen handles various states:
 * - No text: Shows error message directing user to OCR screen
 * - Has text: Displays menu text and analysis button
 * - Analyzing: Shows loading indicator
 * - Success: Displays detailed AI analysis result
 * - Error: Shows error dialog with message
 *
 * @param user The current authenticated user. Required to fetch their dietary profile
 *             for personalized analysis.
 * @param initialMenuText The menu text extracted from OCR, passed via navigation route.
 *                        Empty string if navigating directly (shouldn't happen in normal flow).
 * @param viewModel The ViewModel managing menu analysis state and business logic. Injected via Hilt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportMenuScreen(
    user: User,
    initialMenuText: String = "",
    viewModel: ImportMenuViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Initialize menu text immediately if provided (from OCR)
    // Use initialMenuText as key so it re-runs when text changes
    LaunchedEffect(initialMenuText) {
        if (initialMenuText.isNotBlank()) {
            viewModel.initializeMenuText(initialMenuText)
        } else if (uiState.menuText.isBlank()) {
            // If no text provided and state is blank, this shouldn't happen
            // User should come from OCR screen
        }
    }

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
                title = { Text("Analyze Menu") },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // 🧾 MENU TEXT DISPLAY (from OCR)
            if (uiState.menuText.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = "Extracted Menu Text:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.menuText,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.heightIn(max = 200.dp),
                        )
                    }
                }
            } else {
                // Show message if no text (shouldn't happen if coming from OCR)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                        ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = "No menu text available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Please scan a menu image from the OCR screen first.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    }
                }
            }

            // 🤖 ANALYZE BUTTON
            Button(
                onClick = { viewModel.onAnalyzeMenu(user) },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                enabled = !uiState.isAnalyzing && uiState.menuText.isNotBlank(),
            ) {
                if (uiState.isAnalyzing) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        Text("Analyzing with AI...")
                    }
                } else {
                    Text("🤖 Analyze Menu with AI")
                }
            }

            // 📊 ANALYSIS RESULT
            if (uiState.analysisResult != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "AI Analysis Result",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            IconButton(
                                onClick = { viewModel.onClearResult() },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = uiState.analysisResult ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}