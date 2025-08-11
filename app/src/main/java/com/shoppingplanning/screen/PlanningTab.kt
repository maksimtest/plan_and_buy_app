package com.shoppingplanning.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.shoppingplanning.data.MainViewModel
import com.shoppingplanning.navigation.BottomNavItem
import com.shoppingplanning.navigation.NavigationSelectedItem
import com.shoppingplanning.navigation.ScreenNavItem


@Composable
fun PlanningTab(
    mainViewModel: MainViewModel,
    navSelectedItem: MutableState<NavigationSelectedItem>
) {
    navSelectedItem.value = NavigationSelectedItem(
        BottomNavItem.Planning,
        ScreenNavItem.PackagesScreen,
        0,""
    )
}
