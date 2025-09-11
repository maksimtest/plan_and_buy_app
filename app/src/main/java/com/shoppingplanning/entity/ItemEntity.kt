package com.shoppingplanning.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PackageEntity::class,
            parentColumns = ["id"],
            childColumns = ["packageId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UnitEntity::class,
            parentColumns = ["id"],
            childColumns = ["unitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productId"), Index("packageId"), Index("unitId")]
)
class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val packageId: Int,
    val unitId: Int,
    val planCount: Int,
    val factCount:Int,
    val num: Int,
    val isCurrentMonth:Boolean,
    val year:Int,
    val month:Int,
    val date:String,
    val statCount:Int,
    val statUnit:String
)