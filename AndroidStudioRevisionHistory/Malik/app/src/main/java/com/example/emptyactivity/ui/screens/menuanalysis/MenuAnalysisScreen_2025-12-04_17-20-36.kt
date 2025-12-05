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
import com.example.emptyactivity.domain.model.SafetyRating
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

    // Tab state for filtering by safety rating
    var selectedTabIndex by remember { mutableIntStateOf(0) }

// Calculate item counts for each category
    val allItemsCount = menuItems.size
    val safeItemsCount = menuItems.count { it.safetyRating == SafetyRating.GREEN }
    val cautionItemsCount = menuItems.count { it.safetyRating == SafetyRating.YELLOW }
    val avoidItemsCount = menuItems.count { it.safetyRating == SafetyRating.RED }

// Filter menu items based on selected tab
    val filteredMenuItems = remember(selectedTabIndex, menuItems) {
        when (selectedTabIndex) {
            0 -> menuItems // All items
            1 -> menuItems.filter { it.safetyRating == SafetyRating.GREEN } // Safe
            2 -> menuItems.filter { it.safetyRating == SafetyRating.YELLOW } // Caution
            3 -> menuItems.filter { it.safetyRating == SafetyRating.RED } // Avoid
            else -> menuItems
        }
    }

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
                            colors = listOf(PrestigeBlack, Color(0xFF0A0A0A)),
                        ),
                ).safeDrawingPadding(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ---------------------------
            // HEADER (BACK - TITLE - SAVE)
            // ---------------------------
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
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
                    modifier =
                        Modifier
                            .shadow(8.dp, CircleShape)
                            .background(Color(0xFF1A1A1A), CircleShape)
                            .size(44.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = RoyalGold,
                        modifier = Modifier.size(22.dp),
                    )
                }

                // Title
                Text(
                    text = "Menu Analysis",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = RoyalGold,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                // Save button (same style as delete button)
                IconButton(
                    onClick = {
                        viewModel.onSaveMenu(
                            user = user,
                            menuText = menuText,
                            menuItems = menuItems,
                            imageUriString = imageUriString,
                        )
                    },
                    enabled = !uiState.isSaving && !uiState.isSaved && menuItems.isNotEmpty(),
                    modifier =
                        Modifier
                            .shadow(8.dp, CircleShape)
                            .background(
                                if (uiState.isSaved) Color(0xFF1A1A1A) else Color(0xFF1A1A1A),
                                CircleShape,
                            ).size(44.dp),
                ) {
                    when {
                        uiState.isSaving -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = RoyalGold,
                                strokeWidth = 2.dp,
                            )
                        }

                        uiState.isSaved -> {
                            Text(
                                "✓",
                                color = Color(0xFF43A047),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        else -> {
                            Text(
                                "Save",
                                color = RoyalGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }

            // -------------
// FILTER TABS
// -------------
            if (menuItems.isNotEmpty()) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = RoyalGold,
                    divider = {
                        Divider(
                            color = Color.White.copy(alpha = 0.1f),
                            thickness = 1.dp
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "All",
                                    fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                )
                                Text(
                                    "($allItemsCount)",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.6f),
                                    maxLines = 1,
                                )
                            }
                        },
                        selectedContentColor = RoyalGold,
                        unselectedContentColor = Color.White.copy(alpha = 0.6f),
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Safe",
                                    fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                )
                                Text(
                                    "($safeItemsCount)",
                                    fontSize = 10.sp,
                                    color = Color(0xFF43A047).copy(alpha = 0.8f),
                                    maxLines = 1,
                                )
                            }
                        },
                        selectedContentColor = Color(0xFF43A047),
                        unselectedContentColor = Color.White.copy(alpha = 0.6f),
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Warn",
                                    fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                )
                                Text(
                                    "($cautionItemsCount)",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFDD835).copy(alpha = 0.8f),
                                    maxLines = 1,
                                )
                            }
                        },
                        selectedContentColor = Color(0xFFFDD835),
                        unselectedContentColor = Color.White.copy(alpha = 0.6f),
                    )
                    Tab(
                        selected = selectedTabIndex == 3,
                        onClick = { selectedTabIndex = 3 },
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Avoid",
                                    fontWeight = if (selectedTabIndex == 3) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                )
                                Text(
                                    "($avoidItemsCount)",
                                    fontSize = 10.sp,
                                    color = Color(0xFFE53935).copy(alpha = 0.8f),
                                    maxLines = 1,
                                )
                            }
                        },
                        selectedContentColor = Color(0xFFE53935),
                        unselectedContentColor = Color.White.copy(alpha = 0.6f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

// -------------
// MENU ITEMS LIST
// -------------
            if (menuItems.isNotEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .background(Color.Transparent),
                ) {
                    if (filteredMenuItems.isNotEmpty()) {
                        MenuItemList(menuItems = filteredMenuItems)
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "No items in this category",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No menu items available",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}
