package com.shoppingplanning.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
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