package com.example.emptyactivity.ui.screens.ocr

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.emptyactivity.ui.navigation.Route

/**
 * OCR screen composable for extracting text from menu images.
 *
 * This screen provides the user interface for the OCR workflow:
 * 1. User selects an image from their device (gallery or camera)
 * 2. Image is displayed and sent to Google Cloud Vision API for text extraction
 * 3. Extracted text lines are displayed to the user
 * 4. User can navigate to the analysis screen with the extracted text
 *
 * The screen handles various states:
 * - Initial state: Shows prompt to pick an image
 * - Loading state: Shows progress indicator while OCR is processing
 * - Success state: Displays extracted text lines and "Analyze with AI" button
 * - Error state: Shows error message with retry option
 * - Empty result: Shows message if no text is detected
 *
 * @param navController Navigation controller for navigating to the ImportMenu screen
 *                      with extracted text when user clicks "Analyze with AI".
 * @param vm The ViewModel managing OCR state and business logic. Injected via Hilt.
 */
@Composable
fun OcrScreen(
    navController: NavHostController,
    vm: OcrViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val imageUri by vm.imageUri.collectAsStateWithLifecycle()
    val lines by vm.lines.collectAsStateWithLifecycle()
    val loading by vm.loading.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    val pickImage =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
        ) { uri: Uri? ->
            vm.onImagePicked(uri, context)
        }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = {
                        pickImage.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                        )
                    },
                    modifier = Modifier.weight(1f),
                ) { Text("Pick Image") }

                if (imageUri != null) {
                    OutlinedButton(
                        onClick = { vm.clearUi() },
                        modifier = Modifier.weight(1f),
                    ) { Text("Clear") }
                }
            }

            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Selected image",
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                )
            } else {
                Text(
                    text = "Choose a menu photo or screenshot to extract text.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (loading) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) { CircularProgressIndicator() }
            }

            if (error != null) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                        ),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "OCR failed",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                        )
                        Text(
                            text = error ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = {
                                imageUri?.let { uri ->
                                    vm.onImagePicked(uri, context)
                                }
                            }) { Text("Retry") }
                            OutlinedButton(onClick = { vm.clearUi() }) { Text("Dismiss") }
                        }
                    }
                }
            }

            if (lines.isNotEmpty()) {
                Text(text = "Extracted lines", style = MaterialTheme.typography.titleMedium)
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    itemsIndexed(lines) { idx, line ->
                        Text(text = "${idx + 1}. $line")
                    }
                }
                
                // Button to analyze with AI
                Button(
                    onClick = {
                        val extractedText = vm.getExtractedText()
                        navController.navigate(Route.ImportMenu(menuText = extractedText))
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("🤖 Analyze with AI")
                }
            } else if (!loading && imageUri != null && error == null) {
                Text(
                    text = "No text detected in this image.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
