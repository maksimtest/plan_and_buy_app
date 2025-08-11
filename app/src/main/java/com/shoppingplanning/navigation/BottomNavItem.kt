package com.shoppingplanning.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector) {
    object Catalogs : BottomNavItem("Catalogs", Icons.Default.Create)
    object Planning : BottomNavItem("Planning", Icons.Default.Menu)
    object Shopping : BottomNavItem("Shopping", Icons.Default.ShoppingCart)
    object Statistic : BottomNavItem("Statistics", Icons.Default.Info)
}