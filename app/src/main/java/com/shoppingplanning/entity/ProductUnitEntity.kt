package com.shoppingplanning.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "product_units")
//@Entity(
//    tableName = "product_units",
//    foreignKeys = [
//        ForeignKey(
//            entity = ProductEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["productId"],
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = UnitEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["unitId"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ],
//    indices = [Index("productId"), Index("unitId")]
//)
class ProductUnitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val productName: String,
    val unitId: Int,
    val unitCode: String,
    val num: Int
)