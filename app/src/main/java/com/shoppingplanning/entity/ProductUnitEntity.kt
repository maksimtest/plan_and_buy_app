package com.shoppingplanning.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_units")
class ProductUnitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val productName: String,
    val unitId: Int,
    val unitCode: String,
    val num: Int
)