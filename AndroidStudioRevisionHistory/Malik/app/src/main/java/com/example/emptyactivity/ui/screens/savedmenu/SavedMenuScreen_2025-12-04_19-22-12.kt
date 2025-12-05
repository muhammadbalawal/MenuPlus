package com.example.emptyactivity.ui.screens.savedmenu

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.emptyactivity.domain.model.Menu
import com.example.emptyactivity.domain.model.MenuItem
import com.example.emptyactivity.domain.model.SafetyRating
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.navigation.Route
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen that displays a grid of previously saved and analyzed menus.
 *
 * This screen shows all menus that the user has saved after analysis. Each menu is displayed
 * as a card in a grid layout, showing the menu image (if available), creation date, and
 * a preview of the menu text.
 *
 * Features:
 * - Grid layout with menu cards
 * - Click on a menu card to view detailed analysis
 * - Loading state while fetching menus
 * - Error handling with user-friendly messages
 * - Empty state when no menus are saved
 *
 * The screen automatically loads menus when it's first displayed and navigates to
 * SavedMenuDetailScreen when a menu card is clicked.
 *
 * @param user The authenticated user whose menus should be displayed.
 * @param navController Navigation controller for navigating to menu detail screen.
 * @param viewModel The ViewModel managing the screen's state and menu loading logic.
 *                  Injected via Hilt by default.
 */
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
            EmptyState(
                onScanClick = {
                    navController.navigate(Route.Ocr) {
                        // Use the same navigation pattern as bottom navigation bar
                        // to preserve navigation state and prevent stack issues
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        } else {
            // Sort menus by date (newest first)
            val sortedMenus = uiState.menus.sortedByDescending { it.createdAt }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(sortedMenus) { menu ->
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
    // Parse menu items to get stats
    val menuItems = remember(menu.menuItemsJson) {
        if (!menu.menuItemsJson.isNullOrBlank()) {
            try {
                Json.decodeFromString<List<MenuItem>>(menu.menuItemsJson)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    val itemCount = menuItems.size
    val safeCount = menuItems.count { it.safetyRating == SafetyRating.GREEN }
    val cautionCount = menuItems.count { it.safetyRating == SafetyRating.YELLOW }
    val avoidCount = menuItems.count { it.safetyRating == SafetyRating.RED }

    // Extract menu title from menu text (first line or first 30 chars)
    val menuTitle = remember(menu.menuText) {
        val lines = menu.menuText.lines().filter { it.isNotBlank() }
        if (lines.isNotEmpty()) {
            val firstLine = lines.first().trim()
            if (firstLine.length > 30) {
                firstLine.take(30) + "..."
            } else {
                firstLine
            }
        } else {
            "Menu"
        }
    }

    // Format date as relative time
    val formattedDate = remember(menu.createdAt) {
        formatRelativeDate(menu.createdAt)
    }

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
                .clickable(onClick = onClick)
                .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A1A),
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image section
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(140.dp),
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Menu image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        contentScale = ContentScale.Crop,
                    )
                    // Gradient overlay for better text readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.6f)
                                    )
                                )
                            )
                    )
                } else {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF2A2A2A),
                                            Color(0xFF1A1A1A)
                                        )
                                    )
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = "Menu",
                            tint = RoyalGold.copy(alpha = 0.5f),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Menu title overlay on image
                if (imageUri != null) {
                    Text(
                        text = menuTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Content section
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title (if no image)
                if (imageUri == null) {
                    Text(
                        text = menuTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Date
                Text(
                    text = formattedDate,
                    fontSize = 11.sp,
                    color = RoyalGold.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium,
                )

                // Stats row
                if (itemCount > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Item count
                        Text(
                            text = "$itemCount items",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.7f),
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Safety indicators
                        if (safeCount > 0) {
                            SafetyIndicator(
                                count = safeCount,
                                color = Color(0xFF43A047),
                                size = 6.dp
                            )
                        }
                        if (cautionCount > 0) {
                            SafetyIndicator(
                                count = cautionCount,
                                color = Color(0xFFFDD835),
                                size = 6.dp
                            )
                        }
                        if (avoidCount > 0) {
                            SafetyIndicator(
                                count = avoidCount,
                                color = Color(0xFFE53935),
                                size = 6.dp
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No items analyzed",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.5f),
                    )
                }
            }
        }
    }
}

@Composable
fun SafetyIndicator(
    count: Int,
    color: Color,
    size: androidx.compose.ui.unit.Dp = 6.dp
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(color, RoundedCornerShape(50))
        )
        if (count > 0) {
            Text(
                text = "$count",
                fontSize = 9.sp,
                color = Color.White.copy(alpha = 0.6f),
            )
        }
    }
}

fun formatRelativeDate(timestamp: Long): String {
    val now = System.currentTimeMillis()

    // Detect if timestamp is in seconds (Unix timestamp) vs milliseconds
    // Unix timestamps are typically 10 digits, millisecond timestamps are 13 digits
    val timestampInMillis = if (timestamp < 10000000000L) {
        // Timestamp is in seconds, convert to milliseconds
        timestamp * 1000
    } else {
        // Timestamp is already in milliseconds
        timestamp
    }

    val diff = now - timestampInMillis

    // Handle edge cases: negative diff (future) or very large diff (invalid timestamp)
    if (diff < 0) {
        // Timestamp is in the future, show absolute date
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        return dateFormat.format(Date(timestampInMillis))
    }

    // If diff is suspiciously small (less than 1 second), it might be a parsing issue
    // Check if timestamp is actually very old by comparing to a reasonable minimum
    val minValidTimestamp = 1000000000000L // Jan 1, 2001 in milliseconds
    if (timestampInMillis < minValidTimestamp) {
        // Invalid timestamp, show absolute date
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(Date(timestampInMillis))
    }

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 7 -> {
            val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
            dateFormat.format(Date(timestampInMillis))
        }
        days > 0 -> "${days}d ago"
        hours > 0 -> "${hours}h ago"
        minutes > 0 -> "${minutes}m ago"
        else -> "Just now"
    }
}

@Composable
fun EmptyState(
    onScanClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            // Icon with gradient background
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                RoyalGold.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = "No menus",
                    tint = RoyalGold.copy(alpha = 0.6f),
                    modifier = Modifier.size(64.dp)
                )
            }

            Text(
                text = "No Saved Menus",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Text(
                text = "Scan a menu to get personalized recommendations based on your dietary preferences",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CTA Button
            Button(
                onClick = onScanClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RoyalGold,
                    contentColor = PrestigeBlack
                )
            ) {
                Text(
                    text = "Scan Your First Menu",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
