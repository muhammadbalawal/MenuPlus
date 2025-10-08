package com.example.emptyactivity.view.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.emptyactivity.view.layout.MainLayout

@Composable
fun AboutScreen() {
    MainLayout(screenTitle = "About Us") { paddingValues: PaddingValues ->
        Text(
            text = "This is the About Us screen",
            modifier = androidx.compose.ui.Modifier.padding(paddingValues)
        )
    }
}
