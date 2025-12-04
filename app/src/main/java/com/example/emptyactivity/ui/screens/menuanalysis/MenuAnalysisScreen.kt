package com.example.emptyactivity.ui.screens.menuanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.emptyactivity.domain.model.MenuItem
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.components.menu.MenuItemList
import com.example.emptyactivity.ui.navigation.Route
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAnalysisScreen(
    user: User,
    menuText: String,
    menuItems: List<MenuItem>,
    imageUriString: String = "",
    navController: NavController,
    viewModel: MenuAnalysisViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Error dialog
    if (uiState.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onErrorDismissed() },
            title = { Text("Error", color = RoyalGold, fontWeight = FontWeight.Bold) },
            text = { Text(uiState.errorMessage ?: "", color = Color.White.copy(alpha = 0.9f)) },
            confirmButton = {
                TextButton(onClick = { viewModel.onErrorDismissed() }) {
                    Text("OK", color = RoyalGold)
                }
            },
            containerColor = Color(0xFF1A1A1A),
            shape = RoundedCornerShape(20.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PrestigeBlack, Color(0xFF0A0A0A))
                )
            )
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ---------------------------
            // HEADER (BACK - TITLE - SAVE)
            // ---------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                // Back button
                IconButton(
                    onClick = {
                        navController.navigate(Route.Ocr) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .shadow(8.dp, CircleShape)
                        .background(Color(0xFF1A1A1A), CircleShape)
                        .size(44.dp)
                )
                {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = RoyalGold,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Title
                Text(
                    text = "Menu Analysis",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = RoyalGold,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // Save button (same style as delete button)
                IconButton(
                    onClick = {
                        viewModel.onSaveMenu(
                            user = user,
                            menuText = menuText,
                            menuItems = menuItems,
                            imageUriString = imageUriString
                        )
                    },
                    enabled = !uiState.isSaving && !uiState.isSaved && menuItems.isNotEmpty(),
                    modifier = Modifier
                        .shadow(8.dp, CircleShape)
                        .background(
                            if (uiState.isSaved) Color(0xFF1A1A1A) else Color(0xFF1A1A1A),
                            CircleShape
                        )
                        .size(44.dp)
                ) {
                    when {
                        uiState.isSaving -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = RoyalGold,
                                strokeWidth = 2.dp
                            )
                        }

                        uiState.isSaved -> {
                            Text(
                                "✓",
                                color = Color(0xFF43A047),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        else -> {
                            Text(
                                "Save",
                                color = RoyalGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // -------------
            // MENU ITEMS
            // -------------
            Text(
                text = "MENU ITEMS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalGold.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (menuItems.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Transparent)
                ) {
                    MenuItemList(menuItems = menuItems)
                }
            } else {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No menu items available",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
