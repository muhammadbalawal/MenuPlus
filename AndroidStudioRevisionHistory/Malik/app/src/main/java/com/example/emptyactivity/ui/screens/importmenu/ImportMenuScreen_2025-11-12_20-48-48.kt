package com.example.emptyactivity.ui.screens.importmenu

import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

/**
 * Import Menu Screen - Loading & Results
 *
 * This screen has two states:
 * 1. LOADING: Shows the menu image with animated magnifying glass while Gemini analyzes
 * 2. RESULTS: Shows tabbed analysis (Safe Menu, Best Menu, Full Menu)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportMenuScreen(
    user: User,
    initialMenuText: String = "",
    imageUriString: String = "",
    viewModel: ImportMenuViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Parse image URI
    val imageUri = remember(imageUriString) {
        if (imageUriString.isNotBlank()) Uri.parse(imageUriString) else null
    }

    // Auto-trigger analysis when screen loads
    LaunchedEffect(initialMenuText, user.id) {
        if (initialMenuText.isNotBlank()) {
            viewModel.initializeMenuText(initialMenuText)
            viewModel.onAnalyzeMenu(user)
        }
    }
    // Tab state for results
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Error dialog
    if (uiState.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onErrorDismissed() },
            title = { Text("Error", color = RoyalGold) },
            text = { Text(uiState.errorMessage ?: "", color = Color.White.copy(alpha = 0.9f)) },
            confirmButton = {
                TextButton(onClick = { viewModel.onErrorDismissed() }) {
                    Text("OK", color = RoyalGold)
                }
            },
            containerColor = PrestigeBlack,
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

        // LOADING STATE (analyzing OR waiting for results)
        if (uiState.isAnalyzing || (uiState.analysisResult == null && uiState.menuText.isNotBlank())) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // Title
                Text(
                    text = "Analyzing Menu",
                    fontSize = 32.sp,
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

                Spacer(modifier = Modifier.height(24.dp))

                // Image with animated magnifying glass
                if (imageUri != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Menu being analyzed",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                        )

                        // Animated Magnifying Glass
                        AnimatedMagnifyingGlass()
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Text(
                    text = "AI is scanning your menu...",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(24.dp))

                CircularProgressIndicator(
                    color = RoyalGold,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // RESULTS STATE (analysis complete)
        if (uiState.analysisResult != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                // Title
                Text(
                    text = "Menu Analysis",
                    fontSize = 32.sp,
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
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tab Row
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = PrestigeBlack,
                    contentColor = RoyalGold,
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Text(
                                "Safe Menu",
                                fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = RoyalGold,
                        unselectedContentColor = Color.White.copy(alpha = 0.6f),
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Text(
                                "Best Menu",
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = RoyalGold,
                        unselectedContentColor = Color.White.copy(alpha = 0.6f),
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = {
 
