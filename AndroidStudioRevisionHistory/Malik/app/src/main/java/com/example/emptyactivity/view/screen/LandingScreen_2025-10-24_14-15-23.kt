// File: LandingScreen.kt
package com.example.emptyactivity.view.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emptyactivity.navigation.LocalNavController
import com.example.emptyactivity.ui.theme.FadedYellow
import com.example.emptyactivity.ui.theme.Ink
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold
import com.example.emptyactivity.view.layout.MainLayout

@Composable
fun LandingScreen() {
    MainLayout(screenTitle = "",
                showTopBar = false,
                showBottomBar = false

    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = PrestigeBlack)
                    .safeDrawingPadding(),
        ) {
            Text(
                text = "MenuPlus",
                fontSize = 65.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Cursive, // no custom font
                color = RoyalGold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp),

            )

            val navController = LocalNavController.current

            Button(
                onClick = {
                    navController.navigate("about")
                },
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RoyalGold,
                    contentColor = PrestigeBlack
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Default,
                )
            }
        }
    }
}


