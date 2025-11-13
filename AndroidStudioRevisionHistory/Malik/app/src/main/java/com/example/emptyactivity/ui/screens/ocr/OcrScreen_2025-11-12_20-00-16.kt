package com.example.emptyactivity.ui.screens.ocr

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.emptyactivity.ui.navigation.Route
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

/**
 * OCR Screen - Image Picker with Premium Theme
 *
 * This screen allows users to:
 * 1. Pick an image from their device
 * 2. Preview the selected menu image
 * 3. Scan the image with AI (OCR + Gemini analysis)
 *
 * The OCR extraction happens behind the scenes - users don't see the raw text.
 */
@Composable
fun OcrScreen(
    navController: NavHostController,
    vm: OcrViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val imageUri by vm.imageUri.collectAsStateWithLifecycle()
    val loading by vm.loading.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    val pickImage =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
        ) { uri: Uri? ->
            vm.onImagePicked(uri, context)
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
                .offset(y = (-100).dp)
                .size(400.dp)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Title with gradient
            Text(
                text = "Scan Menu",
                fontSize = 42.sp,
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
                text = "Upload a photo of your menu",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Image Picker Box (Big centered block)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .then(
                        if (imageUri == null) {
                            Modifier
                                .border(
                                    width = 2.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            RoyalGold.copy(alpha = 0.5f),
                                            RoyalGold.copy(alpha = 0.2f),
                                        )
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    pickImage.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center,
            ){
                if (imageUri == null) {
                    // Empty state - Prompt to pick image
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = RoyalGold.copy(alpha = 0.7f),
                            modifier = Modifier.size(80.dp)
                        )

                        Text(
                            text = "Tap to Select Image",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )

                        Text(
                            text = "Choose a photo from your gallery",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    // Show selected image
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Selected menu image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Fit,
                    )
                }

                // Loading overlay
                if (loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                PrestigeBlack.copy(alpha = 0.7f)
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            CircularProgressIndicator(
                                color = RoyalGold,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "Extracting text...",
                                color = Color.White,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Error message
            if (error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE53935).copy(alpha = 0.15f),
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "⚠️ Error",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53935),
                        )
                        Text(
                            text = error ?: "Failed to process image",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Action Buttons
            if (imageUri != null && !loading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Clear/Pick New Button
                    OutlinedButton(
                        onClick = { vm.clearUi() },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = RoyalGold,
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.linearGradient(
                                colors = listOf(RoyalGold, RoyalGold.copy(alpha = 0.5f))
                            ),
                            width = 2.dp
                        ),
                    ) {
                        Text(
                            text = "Pick New",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }

                    // Scan with AI Button (Primary)
                    Button(
                        onClick = {
                            // Get the extracted text and navigate to analysis
                            val extractedText = vm.getExtractedText()
                            if (extractedText.isNotBlank()) {
                                navController.navigate(
                                    Route.ImportMenu(
                                        menuText = extractedText,
                                        imageUriString = imageUri.toString()
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoyalGold,
                            contentColor = PrestigeBlack,
                        ),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Scan with AI",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



