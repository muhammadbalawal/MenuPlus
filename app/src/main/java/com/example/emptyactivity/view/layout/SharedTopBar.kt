package com.example.emptyactivity.view.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.emptyactivity.navigation.LocalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTopBar(
    screenTitle: String,
    showBackButton: Boolean
) {
    val navController = LocalNavController.current

    CenterAlignedTopAppBar(
        title = { Text(screenTitle) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Go Back"
                    )
                }
            }
        }
    )
}
