package com.shoppingplanning.data

import com.shoppingplanning.entity.CategoryEntity
import com.shoppingplanning.entity.ItemEntity
import com.shoppingplanning.entity.PackageEntity
import com.shoppingplanning.entity.ProductEntity
import com.shoppingplanning.entity.ProductUnitEntity
import com.shoppingplanning.entity.UnitEntity
import com.shoppingplanning.info.ActiveItemInfo
import com.shoppingplanning.info.ProductInfo
import com.shoppingplanning.info.StatItemInfo
import com.shoppingplanning.info.StatPeriodInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class AppRepository(private val db: AppDatabase) {
    var categories: Flow<List<CategoryEntity>> = db.categoryDao().getAll()

    fun getAllCategories(): List<CategoryEntity> {
        return db.categoryDao().getAllCategories()
    }
    fun getAllProducts(): List<ProductEntity> {
        return db.productDao().getAllProducts()
    }

    var packages: Flow<List<PackageEntity>> = db.packageDao().getAll()

    suspend fun getNewPackages() = db.packageDao().getNewPackages()

    var products: Flow<List<ProductEntity>> = db.productDao().getAll()
    var productsInfo: Flow<List<ProductInfo>> = db.productDao().getAllProductInfo()
    var productUnits: Flow<List<ProductUnitEntity>> = db.productUnitDao().getAll()
    var activeItems: Flow<List<ActiveItemInfo>> = db.itemDao().getActiveItems()

    fun getStatItemByMonthYear(year: StateFlow<Int>, month: StateFlow<Int>) =
        db.itemDao().getStatItemByMonthYear(year.value, month.value)

    fun getActiveItemEntities() = db.itemDao().getActiveItemEntities()

    var periodsOfItems: Flow<List<StatPeriodInfo>> = db.itemDao().getPeriodsOfItems()

    suspend fun getMinimumItemsYear(): Int {
        val list = db.itemDao().getMinimumYear()
        if (list.isEmpty()) return 0
        return list[0]
    }

    suspend fun getMinimumItemsYearMonth(year: Int): Int {
        val list = db.itemDao().getMinimumYearMonth(year)
        if (list.isEmpty()) return 0
        return list[0]
    }

    suspend fun getProductByName(name: String) = db.productDao().getProductByName(name)

    suspend fun getUnits() = db.unitDao().getAll()

    suspend fun updateCategory(item: CategoryEntity) {
        if (item.id == 0) {
            db.categoryDao().insert(item)
        } else {
            db.categoryDao().update(item)
        }
    }

    suspend fun updatePackage(item: PackageEntity) {
        if (item.id == 0) {
            db.packageDao().insert(item)
        } else {
            db.packageDao().update(item)
        }
    }

    suspend fun updateProduct(item: ProductEntity) {
        if (item.id == 0) {
            db.productDao().insert(item)
        } else {
            db.productDao().update(item)
        }
    }

    suspend fun updateProductUnit(item: ProductUnitEntity) {
        if (item.id == 0) {
            db.productUnitDao().insert(item)
        } else {
            db.productUnitDao().update(item)
        }
    }

    suspend fun updateItem(item: ItemEntity) {
        if (item.id == 0) {
            db.itemDao().insert(item)
        } else {
            db.itemDao().update(item)
        }
    }

    suspend fun updateUnit(unit: UnitEntity) {
        if (unit.id == 0) {
            db.unitDao().insert(unit)
        } else {
            db.unitDao().update(unit)
        }
    }
}