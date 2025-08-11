package com.shoppingplanning.navigation

sealed class ScreenNavItem() {
    object PackagesScreen : ScreenNavItem()
    object ShoppingScreen : ScreenNavItem()
    object PlanningScreen : ScreenNavItem()

    object CatalogTab : ScreenNavItem()
    object PlanningTab : ScreenNavItem()
    object ShoppingTab : ScreenNavItem()
    object StatisticTab : ScreenNavItem()
}