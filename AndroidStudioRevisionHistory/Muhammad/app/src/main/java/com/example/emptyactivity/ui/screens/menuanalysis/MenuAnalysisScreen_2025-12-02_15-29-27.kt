package com.example.emptyactivity.ui.screens.menuanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.components.menu.MenuDisplayContent
import com.example.emptyactivity.ui.navigation.Route
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAnalysisScreen(
    user: User,
    menuText: String,
    safeMenuContent: String,
    bestMenuContent: String,
    fullMenuContent: String,
    imageUriString: String = "",
    navController: NavController,
    viewModel: MenuAnalysisViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = PrestigeBlack),
    ) {
        IconButton(
            onClick = { 
                navController.navigate(Route.SavedMenu) {
                    popUpTo(Route.SavedMenu) { inclusive = false }
                }
            },
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .zIndex(1f),
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = RoyalGold,
                modifier = Modifier.size(24.dp),
            )
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(24.dp),
        ) {
            Text(
                text = "Menu Analysis",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style =
                    TextStyle(
                        brush =
                            Brush.linearGradient(
                                colors =
                                    listOf(
                                        Color(0xFF7A5A00),
                                        RoyalGold,
                                        Color(0xFFFFF4C8),
                                        Color(0xFFD4AF37),
                                    ),
                            ),
                        shadow =
                            Shadow(
                                color = Color(0xAA8B7500),
                                offset = Offset(1f, 1f),
                                blurRadius = 4f,
                            ),
                    ),
                color = Color.Unspecified,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onSaveMenu(
                        user = user,
                        menuText = menuText,
                        safeMenuContent = safeMenuContent,
                        bestMenuContent = bestMenuContent,
                        fullMenuContent = fullMenuContent,
                        imageUriString = imageUriString,
                        context = context,
                    )
                },
                enabled = !uiState.isSaving && !uiState.isSaved,
                modifier = Modifier.fillMaxWidth(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            if (uiState.isSaved) {
                                Color(0xFF43A047)
                            } else {
                                RoyalGold
                            },
                    ),
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Saving...", color = Color.White)
                } else if (uiState.isSaved) {
                    Text("✓ Saved", color = Color.White)
                } else {
                    Text("Save Menu", color = PrestigeBlack)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Shared menu display content
            MenuDisplayContent(
                safeMenuContent = safeMenuContent,
                bestMenuContent = bestMenuContent,
                fullMenuContent = fullMenuContent,
            )
        }
    }
}
