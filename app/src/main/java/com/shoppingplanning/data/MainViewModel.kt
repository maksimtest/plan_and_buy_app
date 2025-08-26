package com.shoppingplanning.data

import android.util.Log
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoppingplanning.util.StringUtil
import com.shoppingplanning.entity.CategoryEntity
import com.shoppingplanning.entity.ItemEntity
import com.shoppingplanning.entity.PackageEntity
import com.shoppingplanning.entity.ProductEntity
import com.shoppingplanning.entity.ProductUnitEntity
import com.shoppingplanning.entity.UnitEntity
import com.shoppingplanning.info.ProductDto
import com.shoppingplanning.info.StatItemInfo
import com.shoppingplanning.info.StatPeriodInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppRepository) : ViewModel() {
    val categories = repository.categories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val packages = repository.packages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeItems = repository.activeItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getAllCategories(): List<CategoryEntity> {
        return repository.getAllCategories()
    }
    suspend fun getAllProduct(): List<ProductEntity> {
        return repository.getAllProducts()
    }
    var minYear = 0
    var minMonth = 0
    suspend fun initMinYear() {
        minYear = repository.getMinimumItemsYear()
        if (minYear == StringUtil.getYear()) {
            minMonth = repository.getMinimumItemsYearMonth(minYear)
        }
    }

    suspend fun checkActiveItems() {
        val list: List<ItemEntity> = repository.getActiveItemEntities()
        val currentYear = StringUtil.getYear()
        val currentMonth = StringUtil.getMonth()
        var currentYear1 = currentYear
        var currentMonth1 = currentMonth - 1
        if (currentMonth1 == 0) {
            currentYear1--
            currentMonth1 = 12
        }

        for (item in list) {
            var isCurrentMonth = false;
            if (item.year == currentYear && item.month == currentMonth) isCurrentMonth = true;
            if (item.year == currentYear1 && item.month == currentMonth1) isCurrentMonth = true;
            val newEntity = ItemEntity(
                item.id, item.productId, item.packageId,
                item.unitId, item.planCount, item.factCount, item.num,
                isCurrentMonth,
                item.year, item.month, item.date,
                item.statCount, item.statUnit
            )
            repository.updateItem(newEntity)
        }
    }

    val productsWithUnits: StateFlow<List<ProductDto>> = combine(
        repository.productsInfo,
        repository.productUnits

    ) { productList, productUnitList ->
        productList.map {
            val productId = it.id
            var units = productUnitList.filter { it.productId == productId }.sortedBy { it.num }
            ProductDto(it.id, it.name, it.categoryId, it.categoryName, "", units)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun isDuplicateCategoryName(name: String): Boolean {
        return categories.value.find { it.name.equals(name, true) } != null
    }

    fun isDuplicateProductName(name: String): Boolean {
        return productsWithUnits.value.find { it.name.equals(name, true) } != null
    }


    var units: List<UnitEntity> = emptyList()
    var unitsMap: Map<Int, UnitEntity> = emptyMap()

    fun finishShopping(packageId: Int) {
        viewModelScope.launch {

            val packageMatch = packages.value.find { it.id == packageId }
            val itemsMatch = activeItems.value.filter { it.packageId == packageId }
            if (packageMatch != null && !itemsMatch.isEmpty()) {
                var planCount = 0
                var factCount = 0

                itemsMatch.forEach {
                    val unit = unitsMap[it.unitId]!!
                    val statCount = it.factCount * unit.koef
                    val statUnit = unit.mainUnitName

                    val newItem = ItemEntity(
                        it.id,
                        it.productId,
                        packageId,
                        it.unitId,
                        it.planCount,
                        it.factCount,
                        it.num,
                        true,
                        StringUtil.getYear(),
                        StringUtil.getMonth(),
                        StringUtil.getShortDate(),
                        statCount,
                        statUnit
                    )
                    planCount += it.planCount
                    factCount += it.factCount
                    repository.updateItem(newItem)
                }
                var percent: Int =
                    if (factCount >= planCount) 100 else (100 * factCount / planCount).toInt()
                val localDate = StringUtil.getShortDate()
                val year = StringUtil.getYear()
                val newPackage =
                    PackageEntity(packageId, localDate, year, localDate, "", false, percent)
                repository.updatePackage(newPackage)
            }
        }
    }
    suspend fun updateCategoryForInit(item: CategoryEntity) {
            repository.updateCategory(item)
    }
    fun update(item: CategoryEntity) {
        viewModelScope.launch {
            repository.updateCategory(item)
        }
    }

    fun update(item: PackageEntity) {
        viewModelScope.launch {
            repository.updatePackage(item)
        }
    }

    fun insert(product: ProductEntity, selectedUnitList: List<UnitEntity>) {
        viewModelScope.launch {
            repository.updateProduct(product)

            val matchProduct = repository.getProductByName(product.name)

            if (matchProduct != null) {
                selectedUnitList.forEach {
                    repository.updateProductUnit(
                        ProductUnitEntity(
                            0,
                            matchProduct.id,
                            product.name,
                            it.id,
                            it.code,
                            0
                        )
                    )
                }
            }
        }
    }

    fun update(item: ItemEntity) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }

    fun update(item: ProductUnitEntity) {
        viewModelScope.launch {
            repository.updateProductUnit(item)
        }
    }

    fun update(item: UnitEntity) {
        viewModelScope.launch {
            repository.updateUnit(item)
        }
    }

    suspend fun insertAndReadUnits(items: List<UnitEntity>) {
        items.forEach {
            repository.updateUnit(it)
        }
        units = repository.getUnits()
    }

    suspend fun init() {
        val currentObj = this
        Log.d("MyTag", "mvm: init()")

        units = repository.getUnits();

        if (units.isEmpty()) {
            DataHelper(currentObj).init(repository)
            units = repository.getUnits();
        }
        unitsMap = units.associateBy { it.id }
        initMinYear()
        checkActiveItems()
    }

    val statPeriods = repository.periodsOfItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedStateDateYear = MutableStateFlow(StringUtil.getYear())
    private val _selectedStateDateMonth = MutableStateFlow(StringUtil.getMonth())

    val selectedDateYear: StateFlow<Int> = _selectedStateDateYear
    val selectedDateMonth: StateFlow<Int> = _selectedStateDateMonth

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val statItems: StateFlow<List<StatItemInfo>> = _selectedStateDateMonth.flatMapLatest {
        repository.getStatItemByMonthYear(
            selectedDateYear,
            selectedDateMonth
        )
    }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateData(currentIndexState: MutableIntState) {
        if (currentIndexState.intValue < 0 ||
            statPeriods.value.isEmpty() ||
            statPeriods.value.size <= currentIndexState.intValue
        ) {
            Log.d(
                "MyTag",
                "updateData skipped: currentIndexState.intValue=${currentIndexState.intValue}, statPeriods.value.size=${statPeriods.value.size}"
            )
            return
        }
        Log.d(
            "MyTag",
            "updateData, currentIndexState.intValue=${currentIndexState.intValue}, statPeriods.value.size=${statPeriods.value.size}"
        )
        val period = statPeriods.value[currentIndexState.intValue]

        _selectedStateDateYear.value = period.year
        _selectedStateDateMonth.value = period.month
    }
}