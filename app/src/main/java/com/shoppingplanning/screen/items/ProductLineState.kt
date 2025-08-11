package com.shoppingplanning.screen.items

data class ProductLineState(
    val productName: String = "",
    val selectedCategory: String = "",
    val isDropdownExpanded: Boolean = false
)