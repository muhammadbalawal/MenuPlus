package com.example.emptyactivity.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.emptyactivity.ui.navigation.MenuPlusApp
import com.example.emptyactivity.ui.theme.EmptyActivityTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point Activity for the MenuPlus application.
 *
 * This Activity sets up the Compose UI with edge-to-edge display and applies the app theme.
 * It uses Hilt for dependency injection and delegates navigation to the MenuPlusApp composable.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * Initializes the Activity and sets up the Compose UI.
     *
     * This method enables edge-to-edge display and sets the content to the MenuPlusApp
     * composable wrapped in the app theme.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmptyActivityTheme {
                MenuPlusApp()
            }
        }
    }
}
