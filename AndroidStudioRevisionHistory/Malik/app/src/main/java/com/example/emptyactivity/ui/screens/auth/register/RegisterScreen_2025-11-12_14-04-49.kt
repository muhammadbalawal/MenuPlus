package com.example.emptyactivity.ui.screens.auth.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRegisterSuccess()
        }
    }

    if (uiState.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onErrorDismissed() },
            title = { Text("Error") },
            text = { Text(uiState.errorMessage ?: "") },
            confirmButton = {
                TextButton(onClick = { viewModel.onErrorDismissed() }) {
                    Text("OK", color = RoyalGold)
                }
            },
            containerColor = PrestigeBlack,
            titleContentColor = RoyalGold,
            textContentColor = Color.White.copy(alpha = 0.9f),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrestigeBlack)
            .safeDrawingPadding(),
    )
    {// Background glow effect
        Canvas(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-100).dp)
                .size(400.dp)
        ) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        RoyalGold.copy(alpha = 0.15f),
                        RoyalGold.copy(alpha = 0.08f),
                        Color.Transparent,
                    ),
                    radius = size.minDimension / 2f
                ),
                radius = size.minDimension / 2f,
                center = center
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // Title with gradient
            Text(
                text = "Create Account",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF7A5A00),
                            RoyalGold,
                            Color(0xFFFFF4C8),
                            Color(0xFFD4AF37),
                        )
                    ),
                    shadow = Shadow(
                        color = Color(0xAA8B7500),
                        offset = Offset(1f, 1f),
                        blurRadius = 4f
                    )
                ),
                color = Color.Unspecified,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Join MenuPlus today",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Name TextField
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Full Name", color = Color.White.copy(alpha = 0.6f)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = RoyalGold.copy(alpha = 0.7f)
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = RoyalGold,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    textColor = Color.White,
                    cursorColor = RoyalGold,
                    focusedLabelColor = Color.White.copy(alpha = 0.6f),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email TextField
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email", color = Color.White.copy(alpha = 0.6f)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = RoyalGold.copy(alpha = 0.7f)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = RoyalGold,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    textColor = Color.White,
                    cursorColor = RoyalGold,
                    focusedLabelColor = Color.White.copy(alpha = 0.6f),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Password", color = Color.White.copy(alpha = 0.6f)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = RoyalGold.copy(alpha = 0.7f)
                    )
                },
                singleLine = true,
                visualTransformation = if (uiState.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { viewModel.onTogglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (uiState.isPasswordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = "Toggle password visibility",
                            tint = RoyalGold.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = RoyalGold,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    textColor = Color.White,
                    cursorColor = RoyalGold,
                    focusedLabelColor = Color.White.copy(alpha = 0.6f),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password TextField
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) },
                label = { Text("Confirm Password", color = Color.White.copy(alpha = 0.6f)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = RoyalGold.copy(alpha = 0.7f)
                    )
                },
                singleLine = true,
                visualTransformation = if (uiState.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = RoyalGold,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    textColor = Color.White,
                    cursorColor = RoyalGold,
                    focusedLabelColor = Color.White.copy(alpha = 0.6f),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(32.dp))

            Spacer(modifier = Modifier.height(16.dp))

            // Register Button
            Button(
                onClick = { viewModel.onRegisterClick() },
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RoyalGold,
                    contentColor = PrestigeBlack,
                    disabledContainerColor = RoyalGold.copy(alpha = 0.5f),
                    disabledContentColor = PrestigeBlack.copy(alpha = 0.5f),
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = PrestigeBlack,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = "Create Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login prompt
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Already have an account? ",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                )
                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = "Sign In",
                        color = RoyalGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
