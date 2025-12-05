package com.example.emptyactivity.ui.screens.savedmenu

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.emptyactivity.ui.components.menu.MenuDisplayContent
import com.example.emptyactivity.ui.navigation.Route
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMenuDetailScreen(
    menuId: String,
    navController: NavController,
    viewModel: SavedMenuDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(menuId) {
        if (menuId.isNotBlank()) {
            viewModel.loadMenu(menuId)
        }
    }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            navController.navigate(Route.SavedMenu) {
                popUpTo(Route.SavedMenu) { inclusive = false }
            }
        }
    }

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
                .background(color = PrestigeBlack)
                .safeDrawingPadding(),
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = RoyalGold,
            )
        } else if (uiState.menu != null) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
            ) {
                // Back button
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.padding(bottom = 8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = RoyalGold,
                        modifier = Modifier.size(24.dp),
                    )
                }

                // Title
                Text(
                    text = "Saved Menu",
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

                // Delete button
                Button(
                    onClick = { viewModel.onDeleteMenu(menuId) },
                    enabled = !uiState.isDeleting && !uiState.isDeleted,
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                if (uiState.isDeleted) {
                                    Color(0xFF43A047)
                                } else {
                                    Color(0xFFD32F2F) // Red for delete
                                },
                        ),
                ) {
                    if (uiState.isDeleting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Deleting...", color = Color.White)
                    } else if (uiState.isDeleted) {
                        Text("✓ Deleted", color = Color.White)
                    } else {
                        Text("Delete Menu", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Shared menu display content
                MenuDisplayContent(
                    safeMenuContent = uiState.menu?.safeMenuContent,
                    bestMenuContent = uiState.menu?.bestMenuContent,
                    fullMenuContent = uiState.menu?.fullMenuContent,
                )
            }
        } else {
            // Menu not found or failed to load
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // Back button
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier =
                        Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = RoyalGold,
                        modifier = Modifier.size(24.dp),
                    )
                }

                Text(
                    text = "Menu Not Found",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Text(
                    text = "The menu you're looking for doesn't exist or has been deleted.",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp),
                )
                Button(
                    onClick = { navController.navigateUp() },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = RoyalGold,
                        ),
                ) {
                    Text("Go Back", color = Color.Black)
                }
            }
        }
    }
}

