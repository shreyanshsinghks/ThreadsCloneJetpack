package com.hello.threads.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.hello.threads.navigation.Routes

data class BottomNavItem (
    val title: String,
    val route: String,
    val icon: ImageVector
)