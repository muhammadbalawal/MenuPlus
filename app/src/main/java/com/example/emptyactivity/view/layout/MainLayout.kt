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
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = { SharedTopBar(screenTitle, showBackButton) },
        bottomBar = { SharedBottomBar() },
    ) { paddingValues ->
        content(paddingValues)
    }
}

