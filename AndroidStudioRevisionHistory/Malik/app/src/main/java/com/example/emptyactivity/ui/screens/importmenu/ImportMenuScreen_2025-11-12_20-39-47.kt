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
f
