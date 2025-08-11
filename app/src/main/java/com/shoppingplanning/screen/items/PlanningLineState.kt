package com.shoppingplanning.screen.items

import com.shoppingplanning.entity.ProductUnitEntity

data class PlanningLineState(
    val selectedProductName: String = "",
    var units: List<ProductUnitEntity> = emptyList<ProductUnitEntity>(),
    val isDropdownExpanded: Boolean = false
)