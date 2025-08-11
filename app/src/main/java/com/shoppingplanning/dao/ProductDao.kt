package com.shoppingplanning.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shoppingplanning.entity.ProductEntity
import com.shoppingplanning.info.ProductInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ProductEntity)

    @Update
    suspend fun update(item: ProductEntity)

    @Query("SELECT * FROM products ORDER BY name")
    fun getAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE name=:name")
    suspend fun getProductByName(name:String): ProductEntity?

    @Query("""
        SELECT pr.id as id, 
                pr.name as name,
                pr.categoryId as categoryId, 
                cat.name as categoryName, 
                "" as statistic
        FROM products pr
        JOIN categories cat WHERE pr.categoryId = cat.id
        ORDER BY pr.name
    """)
    fun getAllProductInfo(): Flow<List<ProductInfo>>

    @Query("""
        SELECT *
        FROM products
    """)
    fun getAllProducts(): List<ProductEntity>
}