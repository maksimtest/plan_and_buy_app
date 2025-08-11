package com.shoppingplanning.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shoppingplanning.entity.CategoryEntity
import com.shoppingplanning.entity.ProductUnitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductUnitDao {
    @Query("SELECT * FROM product_units")
    fun getAll(): Flow<List<ProductUnitEntity>>

    @Query("SELECT * FROM product_units")
    fun getAllProductEntity(): List<ProductUnitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ProductUnitEntity)

    @Update
    suspend fun update(item: ProductUnitEntity)

}