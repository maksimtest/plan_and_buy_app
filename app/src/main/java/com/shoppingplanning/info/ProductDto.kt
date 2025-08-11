package com.shoppingplanning.info

import com.shoppingplanning.entity.ProductUnitEntity

data class ProductDto(
    val id: Int,
    val name: String,
    val categoryId: Int,
    val categoryName: String,
    val statistic: String,
    var units: List<ProductUnitEntity>? = null
)