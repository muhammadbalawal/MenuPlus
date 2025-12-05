package com.example.emptyactivity.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

/**
 * Top app bar composable displayed on main app screens.
 *
 * This component provides a consistent header across main app screens with:
 * - "MenuPlus" branding with gradient gold text and shadow effects
 * - Settings icon button for quick access to settings
 * - Premium black background with subtle gold gradient border
 *
 * The top bar is conditionally displayed on main screens (SavedMenu, Ocr, Profile) but
 * hidden on secondary screens like Settings to provide a cleaner, focused experience.
 *
 * @param onSettingsClick Callback function invoked when the settings icon is clicked.
 *                        Typically navigates to the Settings screen.
 *
 * Mostly created by: Malik
 */
@Composable
fun TopBar(
    onSettingsClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(PrestigeBlack),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Gradient MenuPlus Title
            Text(
                text = "MenuPlus",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
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
                                offset = Offset(0.5f, 0.5f),
                                blurRadius = 3f,
                            ),
                        letterSpacing = 0.5.sp,
                    ),
                color = Color.Unspecified,
            )

            // Settings Icon
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = RoyalGold,
                )
            }
        }

        // Subtle bottom border with gradient
        Canvas(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(1.dp),
        ) {
            drawLine(
                brush =
                    Brush.horizontalGradient(
                        colors =
                            listOf(
                                Color.Transparent,
                                RoyalGold.copy(alpha = 0.3f),
                                RoyalGold.copy(alpha = 0.5f),
                                RoyalGold.copy(alpha = 0.3f),
                                Color.Transparent,
                            ),
                    ),
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 2f,
            )
        }
    }
}
