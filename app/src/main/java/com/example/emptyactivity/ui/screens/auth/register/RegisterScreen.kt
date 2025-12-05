package com.example.emptyactivity.ui.screens.auth.register

import android.app.Activity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emptyactivity.ui.theme.PrestigeBlack
import com.example.emptyactivity.ui.theme.RoyalGold

/**
 * Screen that displays the user registration form.
 *
 * This screen provides a form for new users to create an account with their name, email,
 * and password. It features a premium black and gold theme consistent with the app's
 * design language.
 *
 * Features:
 * - Name, email, and password input fields
 * - Password confirmation field
 * - Password visibility toggle
 * - Loading state during registration
 * - Error handling with user-friendly messages
 * - Navigation to login screen
 * - Automatic navigation on successful registration
 * - Support for initial email pre-filling (e.g., from deep links)
 *
 * The screen uses the RegisterViewModel to manage form state and registration logic.
 * On successful registration, the authentication state change triggers automatic navigation
 * to the onboarding screens via the MenuPlusAppViewModel.
 *
 * @param onNavigateToLogin Callback function to navigate to the login screen.
 * @param onRegisterSuccess Callback function called when registration is successful. Currently
 *                          unused as navigation is handled by authentication state observation.
 * @param viewModel The ViewModel managing the registration form state and account creation logic.
 *                  Injected via Hilt by default.
 * @param initialEmail Optional email address to pre-fill in the email field. Useful for
 *                     deep links or when navigating from the login screen with an email.
 *
 * Mostly created by: Malik
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel(),
    initialEmail: String? = null,
) {
    val localContext = LocalContext.current
    val activity = localContext as? Activity

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(initialEmail) {
        if (!initialEmail.isNullOrBlank()) {
            viewModel.onEmailChange(initialEmail)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            if (activity?.intent?.action == android.content.Intent.ACTION_VIEW) {
                activity.let { safeActivity ->
                    val resultIntent = safeActivity.intent
                    resultIntent.putExtra("signupSuccess", true)
                    resultIntent.putExtra("userEmail", uiState.email)
                    safeActivity.setResult(Activity.RESULT_OK, resultIntent)
                    safeActivity.finish()
                }
            }
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
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = PrestigeBlack)
                .safeDrawingPadding(),
    ) {
        // Background glow effect
        Canvas(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-100).dp)
                    .size(400.dp),
        ) {
            drawCircle(
                brush =
                    Brush.radialGradient(
                        colors =
                            listOf(
                                RoyalGold.copy(alpha = 0.15f),
                                RoyalGold.copy(alpha = 0.08f),
                                Color.Transparent,
                            ),
                        radius = size.minDimension / 2f,
                    ),
                radius = size.minDimension / 2f,
                center = center,
            )
        }
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // Title with gradient
            Text(
                text =
                    if (activity?.intent?.action == android.content.Intent.ACTION_VIEW) {
                        "Sign Up for Cookie"
                    } else {
                        "Create Account"
                    },
                fontSize = 42.sp,
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
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = 
                    if (activity?.intent?.action == android.content.Intent.ACTION_VIEW) {
                        "Complete sign up to claim your cookie!"
                    } else {
                        "Join MenuPlus today"
                    },
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
                        tint = RoyalGold.copy(alpha = 0.7f),
                    )
                },
                singleLine = true,
                colors =
                    TextFieldDefaults.outlinedTextFieldColors(
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
                        tint = RoyalGold.copy(alpha = 0.7f),
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors =
                    TextFieldDefaults.outlinedTextFieldColors(
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
                        tint = RoyalGold.copy(alpha = 0.7f),
                    )
                },
                singleLine = true,
                visualTransformation =
                    if (uiState.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { viewModel.onTogglePasswordVisibility() }) {
                        Icon(
                            imageVector =
                                if (uiState.isPasswordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                            contentDescription = "Toggle password visibility",
                            tint = RoyalGold.copy(alpha = 0.7f),
                        )
                    }
                },
                colors =
                    TextFieldDefaults.outlinedTextFieldColors(
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
                        tint = RoyalGold.copy(alpha = 0.7f),
                    )
                },
                singleLine = true,
                visualTransformation =
                    if (uiState.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors =
                    TextFieldDefaults.outlinedTextFieldColors(
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
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = RoyalGold,
                        contentColor = PrestigeBlack,
                        disabledContainerColor = RoyalGold.copy(alpha = 0.5f),
                        disabledContentColor = PrestigeBlack.copy(alpha = 0.5f),
                    ),
                modifier =
                    Modifier
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
                        text =
                            if (activity?.intent?.action == android.content.Intent.ACTION_VIEW) {
                                "Claim Cookie! 🍪"
                            } else {
                                "Create Account"
                            },
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
