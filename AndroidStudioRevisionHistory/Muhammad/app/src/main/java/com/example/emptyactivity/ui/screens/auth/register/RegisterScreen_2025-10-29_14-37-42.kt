package com.example.emptyactivity.ui.screens.auth.register

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    Text(text = "Register")
}
