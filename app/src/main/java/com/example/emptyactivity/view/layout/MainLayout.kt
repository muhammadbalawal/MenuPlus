// File: MainLayout.kt
package com.example.emptyactivity.view.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    screenTitle: String,
    showBackButton: Boolean = false,
    showTopBar: Boolean = true,
    showBottomBar: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            if (showTopBar) {
                SharedTopBar(screenTitle, showBackButton)
            }
        },
        bottomBar = {
            if (showBottomBar) {
                SharedBottomBar()
            }
        },
    ) { paddingValues ->
        content(paddingValues)
    }
}
