package com.example.emptyactivity.ui.screens.savedmenu

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.emptyactivity.domain.model.Menu
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.navigation.Route
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SavedMenuScreen(
    user: User,
    navController: NavController,
    viewModel: SavedMenuViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMenus(user)
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
        } else if (uiState.menus.isEmpty()) {
            EmptyState()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.menus) { menu ->
                    MenuCard(
                        menu = menu,
                        onClick = {
                            navController.navigate(
                                Route.MenuAnalysis(
                                    menuId = menu.menuId,
                                    menuText = menu.menuText,
                                    menuItemsJson = menu.menuItemsJson ?: "",
                                    imageUriString = menu.imageUri ?: "",
                                ),
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun MenuCard(
    menu: Menu,
    onClick: () -> Unit,
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(menu.createdAt))

    val imageUri =
        remember(menu.imageUri) {
            if (!menu.imageUri.isNullOrBlank()) {
                try {
                    Uri.parse(menu.imageUri)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A1A),
            ),
    ) {
        Column {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(160.dp),
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Menu image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(Color(0xFF2A2A2A)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "📋",
                            fontSize = 48.sp,
                        )
                    }
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    color = RoyalGold.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "📋",
                fontSize = 64.sp,
            )
            Text(
                text = "No Saved Menus",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = "Analyze and save menus to see them here",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f),
            )
        }
    }
}
