package com.example.emptyactivity.ui.screens.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset

@Composable
fun LandingScreen(
    onContinue: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = PrestigeBlack)
                .safeDrawingPadding(),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ){
            Canvas(
                modifier = Modifier
                    .size(300.dp)){
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            RoyalGold.copy(alpha = 0.35f),
                            Color.Transparent
                        ),
                        radius = size.minDimension / 2f
                    ),
                    radius = size.minDimension / 2f,
                    center = center,
                    style = Fill
                )
            }

            Text(
                text= "MenuPlus",
                fontSize = 65.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Cursive,
                textAlign = TextAlign.Center,

                style = TextStyle(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF7A5A00),
                            RoyalGold,
                            Color(0xFFFFF4C8),
                            Color(0xFFD4AF37)
                        )
                    ),
                    shadow = Shadow(
                        color = Color(0xAA8B7500),
                        offset = Offset(2f, 2f),
                        blurRadius = 6f
                    )
                ),

                color = Color.Unspecified

            )

        }
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onContinue,
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
