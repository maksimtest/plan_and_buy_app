package com.shoppingplanning.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "units")
class UnitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val code: String,
    val mainUnitName: String,
    val koef:Int,
)