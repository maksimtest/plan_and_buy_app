package com.shoppingplanning.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.shoppingplanning.entity.ItemEntity
import com.shoppingplanning.info.ActiveItemInfo
import com.shoppingplanning.info.StatItemInfo
import com.shoppingplanning.info.StatPeriodInfo

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity)

    @Update
    suspend fun update(item: ItemEntity)

    @Query(
        """
        SELECT items.id as id, items.packageId as packageId, 
        products.id as productId, products.name as productName,
        units.id as unitId,units.code as unitCode,
         items.planCount as planCount, items.factCount as factCount,
         items.num as num
        FROM items
        JOIN products, units WHERE
            items.isCurrentMonth == 1 AND
            items.productId == products.id AND
            items.unitId == units.id
        """
    )
    fun getActiveItems(): Flow<List<ActiveItemInfo>>

    @Query("""
        SELECT items.*
        FROM items
        JOIN packages WHERE
            items.isCurrentMonth == 1 AND
            items.packageId == packages.id AND
            packages.isNew = 0 
        """)
    fun getActiveItemEntities(): List<ItemEntity>

    @Query("""
        SELECT items.id as id, items.packageId as packageId, 
        products.id as productId, products.name as productName,
        units.id as unitId,units.code as unitCode,
         items.planCount as planCount, items.factCount as factCount,
         items.num as num
        FROM items
        JOIN products, units WHERE
            items.year == :year AND
            items.month == :month AND
            items.productId == products.id AND
            items.unitId == units.id
        """)
    fun getAllByMonthYear(year: Int, month:Int): List<ActiveItemInfo>

    @Query("SELECT DISTINCT year FROM items ORDER BY year DESC")
    fun getMinimumYear(): List<Int>

    @Query("SELECT DISTINCT month FROM items WHERE year=:year ORDER BY month DESC")
    fun getMinimumYearMonth(year:Int): List<Int>

    @Query("""
        SELECT 
            products.name as product,
            sum(items.statCount) as count,
            items.statUnit as unit,
            items.year as year,
            items.month as month
        FROM items
        JOIN products WHERE
            items.year == :year AND
            items.month == :month AND
            items.productId == products.id
        GROUP BY year, month, product, unit
        ORDER BY (year*100+month)
        """)
    fun getStatItemByMonthYear(year: Int, month:Int): Flow<List<StatItemInfo>>

    @Query("""
        SELECT DISTINCT (year), month
        FROM items
        ORDER BY year, month DESC
        """)
    fun getPeriodsOfItems(): Flow<List<StatPeriodInfo>>

    @Query("SELECT * FROM items ORDER BY id")
    fun getAllItemEntities(): List<ItemEntity>

}