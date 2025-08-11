package com.shoppingplanning.navigation

data class NavigationSelectedItem (
    var selectedTabItem: BottomNavItem,
    var selectedScreen: ScreenNavItem,
    var packageId:Int,
    var packageName:String
)