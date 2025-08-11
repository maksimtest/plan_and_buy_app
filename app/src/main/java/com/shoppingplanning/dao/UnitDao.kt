package com.shoppingplanning.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shoppingplanning.entity.CategoryEntity
import com.shoppingplanning.entity.PackageEntity
import com.shoppingplanning.entity.ProductEntity
import com.shoppingplanning.entity.UnitEntity
import com.shoppingplanning.info.ProductInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: UnitEntity)

    @Update
    suspend fun update(item: UnitEntity)

    @Query("SELECT * FROM units ORDER BY id")
    suspend fun getAll(): List<UnitEntity>
}