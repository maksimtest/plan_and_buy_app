package com.shoppingplanning.info

data class ActiveItemInfo(
    val id: Int,
    val packageId: Int,
    val productId:Int,
    val productName:String,
    val unitId: Int,
    val unitCode: String,
    val planCount:Int,
    val factCount:Int,
    val num:Int
)
