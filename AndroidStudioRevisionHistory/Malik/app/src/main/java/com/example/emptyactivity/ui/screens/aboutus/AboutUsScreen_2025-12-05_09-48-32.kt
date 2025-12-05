package com.example.emptyactivity.ui.screens.aboutus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

/**
 * Screen that displays information about the team and app motivation.
 *
 * This screen provides users with information about:
 * - The development team (Malik Al-Shourbaji and Muhammad Balawal Safdar)
 * - The motivation and problem MenuPlus solves
 * - The mission and vision of the app
 * - Key features and benefits
 *
 * Features:
 * - Team member information display
 * - App motivation and mission statement
 * - Premium black and gold theme consistent with the rest of the app
 * - Navigation back to previous screen
 *
 * @param onNavigateBack Callback function to navigate back to the previous screen.
 * @param viewModel The ViewModel managing the screen's state. Injected via Hilt by default.
 *
 * Mostly created by: Muhammad
 */
@Composable
fun AboutUsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AboutUsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrestigeBlack),
    ) {
        // Back button positioned at top start
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // App Icon and Title
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .shadow(12.dp, CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF7A5A00),
                                RoyalGold,
                            ),
                        ),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = "MenuPlus",
                    tint = PrestigeBlack,
                    modifier = Modifier.size(40.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App Title
            Text(
                text = "MenuPlus",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF7A5A00),
                            RoyalGold,
                            Color(0xFFFFF4C8),
                            Color(0xFFD4AF37),
                        ),
                    ),
                    shadow = Shadow(
                        color = Color(0xAA8B7500),
                        offset = Offset(1f, 1f),
                        blurRadius = 4f,
                    ),
                ),
                color = Color.Unspecified,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Personal Menu Safety Guide",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Our Mission Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A),
                ),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                ) {
                    Text(
                        text = "Our Mission",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalGold,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Food allergies and dietary restrictions affect millions of people worldwide. Dining out should be an enjoyable experience, not a source of anxiety. MenuPlus empowers people to make informed dining decisions by instantly analyzing menus for allergens, dietary restrictions, and personal preferences.",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 20.sp,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "With AI-powered menu analysis and color-coded safety ratings, MenuPlus transforms the dining experience for anyone with food allergies, dietary restrictions, or specific nutritional goals.",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 20.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // The Team Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A),
                ),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                ) {
                    Text(
                        text = "The Team",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalGold,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Team Member 1
                    TeamMemberSection(
                        name = "Malik Al-Shourbaji",
                        role = "Co-Founder & Lead Developer",
                        description = "Passionate about building accessible technology that solves real-world problems. Focused on creating intuitive user experiences and robust architecture.",
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Divider(
                        color = Color.White.copy(alpha = 0.1f),
                        thickness = 1.dp,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Team Member 2
                    TeamMemberSection(
                        name = "Muhammad Balawal Safdar",
                        role = "Co-Founder & Lead Developer",
                        description = "Dedicated to leveraging AI and modern mobile technologies to create solutions that make a meaningful impact on people's daily lives.",
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // What We Offer Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A),
                ),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                ) {
                    Text(
                        text = "What We Offer",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalGold,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    FeatureBullet(
                        icon = "📸",
                        text = "Instant menu scanning using advanced OCR technology",
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FeatureBullet(
                        icon = "🤖",
                        text = "AI-powered analysis with personalized recommendations",
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FeatureBullet(
                        icon = "🎨",
                        text = "Color-coded safety ratings for quick decision making",
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FeatureBullet(
                        icon = "💾",
                        text = "Save and revisit your favorite menus anytime",
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FeatureBullet(
                        icon = "🌍",
                        text = "Multi-language support for global dining",
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FeatureBullet(
                        icon = "👤",
                        text = "Personalized profiles for your unique dietary needs",
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Text(
                text = "Built with ❤️ for food lovers everywhere",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "© 2024 MenuPlus",
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.3f),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TeamMemberSection(
    name: String,
    role: String,
    description: String,
) {
    Column {
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = role,
            fontSize = 13.sp,
            color = RoyalGold.copy(alpha = 0.9f),
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.8f),
            lineHeight = 18.sp,
        )
    }
}

@Composable
private fun FeatureBullet(
    icon: String,
    text: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = icon,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 12.dp),
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.9f),
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f),
        )
    }
}

