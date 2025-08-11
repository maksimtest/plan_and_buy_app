package com.shoppingplanning.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shoppingplanning.entity.CategoryEntity
import com.shoppingplanning.entity.PackageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackageDao {
    @Query("SELECT * FROM packages ORDER BY isNew DESC, id DESC")
    fun getAll(): Flow<List<PackageEntity>>

    @Query("SELECT * FROM packages WHERE isNew=true")
    suspend fun getNewPackages(): List<PackageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PackageEntity)

    @Update
    suspend fun update(item: PackageEntity)

}