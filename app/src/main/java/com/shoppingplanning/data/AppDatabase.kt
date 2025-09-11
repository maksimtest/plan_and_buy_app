package com.shoppingplanning.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shoppingplanning.dao.CategoryDao
import com.shoppingplanning.dao.ItemDao
import com.shoppingplanning.dao.PackageDao
import com.shoppingplanning.dao.ProductDao
import com.shoppingplanning.dao.ProductUnitDao
import com.shoppingplanning.dao.UnitDao
import com.shoppingplanning.entity.CategoryEntity
import com.shoppingplanning.entity.ItemEntity
import com.shoppingplanning.entity.PackageEntity
import com.shoppingplanning.entity.ProductEntity
import com.shoppingplanning.entity.ProductUnitEntity
import com.shoppingplanning.entity.UnitEntity

@Database(entities = [
    CategoryEntity::class,
    PackageEntity::class,
    ProductEntity::class,
    ProductUnitEntity::class,
    UnitEntity::class,
    ItemEntity::class],
    version = 6,
    exportSchema = true)

abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun packageDao(): PackageDao
    abstract fun productDao(): ProductDao
    abstract fun unitDao(): UnitDao
    abstract fun itemDao(): ItemDao
    abstract fun productUnitDao(): ProductUnitDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                                            context.applicationContext,
                                            AppDatabase::class.java,
                                            "shoppingPlanner_db"
                                        ).fallbackToDestructiveMigration(true)
                    .build().also { INSTANCE = it }
            }

    }
}
