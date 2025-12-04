package com.example.emptyactivity.ui.screens.savedmenu

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.emptyactivity.ui.components.menu.MenuItemList
import com.example.emptyactivity.ui.navigation.Route
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen that displays detailed information about a saved menu.
 *
 * This screen shows the complete analysis of a previously saved menu, including all menu items
 * with their safety ratings, tags, and recommendations. Users can view the menu details and
 * delete the menu if they no longer need it.
 *
 * Features:
 * - Displays menu items with color-coded safety ratings
 * - Shows menu creation date
 * - Delete functionality with confirmation dialog
 * - Loading and error states
 * - Navigation back to saved menus list
 *
 * The screen automatically loads the menu when displayed and parses the stored menu items
 * from JSON format for display.
 *
 * @param menuId The unique identifier of the menu to display. Used to fetch the menu
 *               from the database.
 * @param navController Navigation controller for navigating back to the saved menus list
 *                     or other screens.
 * @param viewModel The ViewModel managing the screen's state and menu loading/deletion logic.
 *                  Injected via Hilt by default.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedMenuDetailScreen(
    menuId: String,
    navController: NavController,
    viewModel: SavedMenuDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

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

    // Error Dialog
    if (uiState.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onErrorDismissed() },
            title = {
                Text(
                    "Error",
                    color = RoyalGold,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Text(
                    uiState.errorMessage ?: "",
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 20.sp,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onErrorDismissed() },
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = RoyalGold,
                        ),
                ) {
                    Text("OK", fontWeight = FontWeight.SemiBold)
                }
            },
            containerColor = Color(0xFF1A1A1A),
            shape = RoundedCornerShape(20.dp),
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Delete Menu?",
                    color = RoyalGold,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Text(
                    "This action cannot be undone. Are you sure you want to delete this menu?",
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 20.sp,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.onDeleteMenu(menuId)
                    },
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFFEF5350),
                        ),
                ) {
                    Text("Delete", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = Color.White.copy(alpha = 0.7f),
                        ),
                ) {
                    Text("Cancel")
                }
            },
            containerColor = Color(0xFF1A1A1A),
            shape = RoundedCornerShape(20.dp),
        )
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    PrestigeBlack,
                                    Color(0xFF0A0A0A),
                                ),
                        ),
                ).safeDrawingPadding(),
    ) {
        if (uiState.isLoading) {
            // Enhanced loading state
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = RoyalGold,
                    strokeWidth = 4.dp,
                )
                Text(
                    text = "Loading menu...",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                )
            }
        } else if (uiState.menu != null) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Header with back button, title, and delete button in one row
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Back button with background
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier =
                            Modifier
                                .shadow(8.dp, CircleShape)
                                .background(
                                    color = Color(0xFF1A1A1A),
                                    shape = CircleShape,
                                ).size(44.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = RoyalGold,
                            modifier = Modifier.size(22.dp),
                        )
                    }

                    // Title in the middle
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = "Saved Menu",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalGold,
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // Date saved
                        val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
                        val formattedDate =
                            remember(uiState.menu?.createdAt) {
                                dateFormat.format(Date(uiState.menu?.createdAt ?: 0L))
                            }

                        Text(
                            text = formattedDate,
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center,
                        )
                    }

                    // Delete button with background
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        enabled = !uiState.isDeleting && !uiState.isDeleted,
                        modifier =
                            Modifier
                                .shadow(8.dp, CircleShape)
                                .background(
                                    color =
                                        if (uiState.isDeleting || uiState.isDeleted) {
                                            Color(0xFF2A2A2A)
                                        } else {
                                            Color(0xFF1A1A1A)
                                        },
                                    shape = CircleShape,
                                ).size(44.dp),
                    ) {
                        if (uiState.isDeleting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = RoyalGold,
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint =
                                    if (uiState.isDeleted) {
                                        Color(0xFF43A047)
                                    } else {
                                        Color(0xFFEF5350)
                                    },
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Menu items section with label
                Text(
                    text = "MENU ITEMS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalGold.copy(alpha = 0.7f),
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                )

                val menuItems =
                    remember(uiState.menu?.menuItemsJson) {
                        uiState.menu?.menuItemsJson?.takeIf { it.isNotBlank() }?.let { jsonString ->
                            try {
                                val json =
                                    Json {
                                        ignoreUnknownKeys = true
                                        coerceInputValues = true
                                    }
                                json.decodeFromString<List<MenuItem>>(jsonString)
                            } catch (e: Exception) {
                                emptyList<MenuItem>()
                            }
                        } ?: emptyList()
                    }

                if (menuItems.isNotEmpty()) {
                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                    ) {
                        MenuItemList(menuItems = menuItems)
                    }
                } else {
                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = Color(0xFF1A1A1A),
                            ),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Text(
                                    text = "🍽️",
                                    fontSize = 48.sp,
                                )
                                Text(
                                    text = "No menu items available",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                                Text(
                                    text = "This menu doesn't have any items",
                                    color = Color.White.copy(alpha = 0.4f),
                                    fontSize = 13.sp,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            // Enhanced "Menu Not Found" state
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // Back button at top
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier =
                        Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 32.dp)
                            .shadow(8.dp, CircleShape)
                            .background(
                                color = Color(0xFF1A1A1A),
                                shape = CircleShape,
                            ).size(44.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = RoyalGold,
                        modifier = Modifier.size(22.dp),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Error content card
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = Color(0xFF1A1A1A),
                        ),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(
                                        brush =
                                            Brush.radialGradient(
                                                colors =
                                                    listOf(
                                                        Color(0xFFEF5350).copy(alpha = 0.2f),
                                                        Color.Transparent,
                                                    ),
                                            ),
                                    ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "❌",
                                fontSize = 40.sp,
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Menu Not Found",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "The menu you're looking for doesn't exist or has been deleted.",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = RoyalGold,
                                ),
                            contentPadding = PaddingValues(vertical = 16.dp),
                        ) {
                            Text(
                                "Go Back",
                                color = PrestigeBlack,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
