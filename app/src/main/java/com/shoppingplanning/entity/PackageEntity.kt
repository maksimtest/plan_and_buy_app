package com.shoppingplanning.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packages")
class PackageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val year: Int,
    val date: String,
    var description: String,
    val isNew: Boolean,
    val percent: Int
)