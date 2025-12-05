package com.example.emptyactivity.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emptyactivity.BuildConfig
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

/**
 * Screen that displays app settings and account management options.
 *
 * This screen provides users with access to app settings and account-related actions.
 * Currently, it primarily focuses on logout functionality, but can be extended with
 * additional settings in the future.
 *
 * Features:
 * - User information display (email, name)
 * - About Us navigation
 * - Logout functionality with confirmation dialog
 * - App version information
 * - Navigation back to previous screen
 *
 * The screen uses a premium black and gold theme consistent with the rest of the app.
 *
 * @param user The authenticated user whose settings are being displayed.
 * @param onNavigateBack Callback function to navigate back to the previous screen.
 * @param onNavigateToAboutUs Callback function to navigate to the About Us screen.
 * @param onLogout Callback function to handle logout. Currently unused as logout is
 *                 handled by the ViewModel and auth state management.
 * @param viewModel The ViewModel managing the screen's state and logout logic.
 *                  Injected via Hilt by default.
 *
 * Mostly created by: Malik
 */
@Composable
fun SettingsScreen(
    user: User,
    onNavigateBack: () -> Unit,
    onNavigateToAboutUs: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout", color = RoyalGold) },
            text = { Text("Are you sure you want to logout?", color = Color.White.copy(alpha = 0.9f)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.onLogout()
                    },
                ) {
                    Text("Logout", color = RoyalGold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = RoyalGold)
                }
            },
            containerColor = PrestigeBlack,
        )
    }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onLogout()
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = PrestigeBlack),
    ) {
        // Back button positioned at top start
        IconButton(
            onClick = onNavigateBack,
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
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Top section with title and user info
            Column {
                // Title with gradient
                Text(
                    text = "Settings",
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

                Spacer(modifier = Modifier.height(24.dp))

                // User Email
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = Color(0xFF1A1A1A),
                        ),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                    ) {
                        Text(
                            text = "Email",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.6f),
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // App Version
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = Color(0xFF1A1A1A),
                        ),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                    ) {
                        Text(
                            text = "App Version",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.6f),
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = BuildConfig.VERSION_NAME,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                        )
                    }
                }
            }

            // About Us and Sign Out Buttons at bottom
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // About Us Button
                Button(
                    onClick = onNavigateToAboutUs,
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A1A1A),
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About Us",
                        tint = RoyalGold,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "About Us",
                        color = RoyalGold,
                    )
                }

                // Sign Out Button
                Button(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F),
                        ),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color.White,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sign Out",
                        color = Color.White,
                    )
                }
            }
        }
    }
}
