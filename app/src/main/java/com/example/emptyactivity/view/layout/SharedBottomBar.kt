package com.example.emptyactivity.view.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.emptyactivity.navigation.LocalNavController
import com.example.emptyactivity.navigation.Routes

@Composable
fun SharedBottomBar() {
    val navController = LocalNavController.current

    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomBarButton(
                route = Routes.Landing.route,
                icon = Icons.Filled.Home,
                description = "Landing",
                onClick = { navController.navigate(Routes.Landing.route) },
            )
            BottomBarButton(
                route = Routes.About.route,
                icon = Icons.Filled.Info,
                description = "About",
                onClick = { navController.navigate(Routes.About.route) },
            )
        }
    }
}

@Composable
private fun BottomBarButton(
    route: String,
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(icon, contentDescription = description)
    }
}
