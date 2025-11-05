package com.example.emptyactivity.ui.ocr

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@Composable
fun OcrScreen(
    vm: OcrViewModel = hiltViewModel(),
) {
    val imageUri by vm.imageUri.collectAsStateWithLifecycle()
    val lines by vm.lines.collectAsStateWithLifecycle()
    val loading by vm.loading.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        // ViewModel handles nulls
        vm.onImagePicked(uri, /* context */ androidx.compose.ui.platform.LocalContext.current)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Header actions (no top app bar, inline buttons)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = {
                        pickImage.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.weight(1f),
                ) { Text("Pick Image") }

                if (imageUri != null) {
                    OutlinedButton(
                        onClick = vm::clear,
                        modifier = Modifier.weight(1f),
                    ) { Text("Clear") }
                }
            }

            // Image preview (if any)
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                )
            } else {
                // Subtle hint when nothing is picked yet
                Text(
                    text = "Choose a menu photo or screenshot to extract text.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            // Loading
            if (loading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            // Error
            if (error != null) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "OCR failed",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = error ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = {
                                imageUri?.let { uri ->
                                    vm.onImagePicked(uri, androidx.compose.ui.platform.LocalContext.current)
                                }
                            }) { Text("Retry") }
                            OutlinedButton(onClick = vm::clear) { Text("Dismiss") }
                        }
                    }
                }
            }

            // Extracted lines
            if (lines.isNotEmpty()) {
                Text(
                    text = "Extracted lines",
                    style = MaterialTheme.typography.titleMedium,
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    itemsIndexed(lines) { idx, line ->
                        Text(text = "${idx + 1}. $line")
                    }
                }
            } else if (!loading && imageUri != null && error == null) {
                // Picked an image but got nothing back
                Text(
                    text = "No text detected in this image.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
