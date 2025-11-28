package com.example.emptyactivity.ui.components.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emptyactivity.ui.theme.RoyalGold

@Composable
fun MenuDisplayContent(
    safeMenuContent: String?,
    bestMenuContent: String?,
    fullMenuContent: String?,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color(0xFF000000), // PrestigeBlack
        contentColor = RoyalGold,
    ) {
        Tab(
            selected = selectedTabIndex == 0,
            onClick = { selectedTabIndex = 0 },
            text = {
                Text(
                    "Safe Menu",
                    fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal,
                )
            },
            selectedContentColor = RoyalGold,
            unselectedContentColor = Color.White.copy(alpha = 0.6f),
        )
        Tab(
            selected = selectedTabIndex == 1,
            onClick = { selectedTabIndex = 1 },
            text = {
                Text(
                    "Best Menu",
                    fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal,
                )
            },
            selectedContentColor = RoyalGold,
            unselectedContentColor = Color.White.copy(alpha = 0.6f),
        )
        Tab(
            selected = selectedTabIndex == 2,
            onClick = { selectedTabIndex = 2 },
            text = {
                Text(
                    "Full Menu",
                    fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Normal,
                )
            },
            selectedContentColor = RoyalGold,
            unselectedContentColor = Color.White.copy(alpha = 0.6f),
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    when (selectedTabIndex) {
        0 -> SafeMenuContent(safeMenuContent ?: "No safe menu content available")
        1 -> BestMenuContent(bestMenuContent ?: "No best menu content available")
        2 -> FullMenuContent(fullMenuContent ?: "No full menu content available")
    }
}

@Composable
fun SafeMenuContent(analysisResult: String) {
    Card(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFF1B3A1B),
            ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "✅ Safe Choices",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF43A047),
            )
            Text(
                text = analysisResult,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
fun BestMenuContent(analysisResult: String) {
    Card(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFF3A2A1B),
            ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "⭐ Best Recommendations",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalGold,
            )
            Text(
                text = analysisResult,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
fun FullMenuContent(analysisResult: String) {
    Card(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A1A),
            ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "📋 Full Menu Analysis",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = analysisResult,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
            )
        }
    }
}

