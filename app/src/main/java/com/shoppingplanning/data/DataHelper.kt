package com.shoppingplanning.data

import android.util.Log
import com.shoppingplanning.entity.CategoryEntity
import com.shoppingplanning.entity.ProductEntity
import com.shoppingplanning.entity.ProductUnitEntity
import com.shoppingplanning.entity.UnitEntity
import com.shoppingplanning.screen.ProductItem

class DataHelper(val mainViewModel: MainViewModel) {

    suspend fun init(repository: AppRepository) {
        // 1. Init UNITS
        val unit1Code = "kg"
        val unit2Code = "l"
        val unit3Code = "pcs"
        val unit4Code = "gr"
        val unit5Code = "ml"
        val unit1 = UnitEntity(0, unit1Code, unit4Code, 1000)
        val unit2 = UnitEntity(0, unit2Code, unit5Code, 1000)
        val unit3 = UnitEntity(0, unit3Code, unit3Code, 1)
        val unit4 = UnitEntity(0, unit4Code, unit4Code, 1)
        val unit5 = UnitEntity(0, unit5Code, unit5Code, 1)
        mainViewModel.insertAndReadUnits(listOf(unit1, unit2, unit3, unit4, unit5))
        var unit1Id = 0
        var unit2Id = 0
        var unit3Id = 0
        var unit4Id = 0
        var unit5Id = 0
        mainViewModel.units.forEach {
            if (it.code == unit1Code) unit1Id = it.id
            if (it.code == unit2Code) unit2Id = it.id
            if (it.code == unit3Code) unit3Id = it.id
            if (it.code == unit4Code) unit4Id = it.id
            if (it.code == unit5Code) unit5Id = it.id
        }
        Log.d("MyTag", "DataHelper.init: unit1Id")
        val ukg = UnitEntity(unit1Id, unit1Code, unit4Code, 1000)
        val ul = UnitEntity(unit2Id, unit2Code, unit5Code, 1000)
        val upcs = UnitEntity(unit3Id, unit3Code, unit3Code, 1)
        val ugr = UnitEntity(unit4Id, unit4Code, unit4Code, 1)
        val uml = UnitEntity(unit5Id, unit5Code, unit5Code, 1)


        // 2. Init CATEGORY
        val catName1 = "Dairy"
        val catName2 = "Fish"
        val catName3 = "Meat"
        val catName4 = "Fruit"
        val catName5 = "Vegetables"
        val catName6 = "Sweets"
        val catName7 = "Drink"
        val catName8 = "Others"

        val category1 = CategoryEntity(0, catName1, "-")
        val category2 = CategoryEntity(0, catName2, "-")
        val category3 = CategoryEntity(0, catName3, "-")
        val category4 = CategoryEntity(0, catName4, "-")
        val category5 = CategoryEntity(0, catName5, "-")
        val category6 = CategoryEntity(0, catName6, "-")
        val category7 = CategoryEntity(0, catName7, "-")
        val category8 = CategoryEntity(0, catName8, "-")
        mainViewModel.updateCategoryForInit(category1)
        mainViewModel.updateCategoryForInit(category2)
        mainViewModel.updateCategoryForInit(category3)
        mainViewModel.updateCategoryForInit(category4)
        mainViewModel.updateCategoryForInit(category5)
        mainViewModel.updateCategoryForInit(category6)
        mainViewModel.updateCategoryForInit(category7)
        mainViewModel.updateCategoryForInit(category8)
        var catId1 = 0;
        var catId2 = 0;
        var catId3 = 0;
        var catId4 = 0;
        var catId5 = 0;
        var catId6 = 0;
        var catId7 = 0;
        var catId8 = 0;
        mainViewModel.getAllCategories().forEach {
            when (it.name) {
                catName1 -> catId1 = it.id
                catName2 -> catId2 = it.id
                catName3 -> catId3 = it.id
                catName4 -> catId4 = it.id
                catName5 -> catId5 = it.id
                catName6 -> catId6 = it.id
                catName7 -> catId7 = it.id
                catName8 -> catId8 = it.id
            }
        }
        Log.d(
            "MyTag",
            "catId1=$catId1, catId2=$catId2, catId3=$catId3, catId7=$catId7, catId8=$catId8"
        )

        // 3. Init ProductUnitEntity instances
        val pu1 = ProductUnitEntity(0, 0, "", unit1Id, unit1.code, 1)
        val pu2 = ProductUnitEntity(0, 0, "", unit2Id, unit2.code, 2)
        val pu3 = ProductUnitEntity(0, 0, "", unit3Id, unit3.code, 3)

        val pu4 = ProductUnitEntity(0, 0, "", unit4Id, unit4.code, 4)
        val pu5 = ProductUnitEntity(0, 0, "", unit5Id, unit5.code, 5)
        mainViewModel.update(pu1)
        mainViewModel.update(pu2)
        mainViewModel.update(pu3)
        mainViewModel.update(pu4)
        mainViewModel.update(pu5)

        // 4. Init Products
        val prName1 = "Milk"
        val prName2 = "Chease"
        val prName3 = "Salmom"
        val prName4 = "Red fish"
        val prName5 = "Perch"
        val prName6 = "Beef"
        val prName7 = "Pork"
        val prName8 = "Chicken"
        val prName9 = "Banana"
        val prName10 = "Apple"
        val prName11 = "Tomato"
        val prName12 = "Cucumber"
        val prName13 = "Cabbage"
        val prName14 = "Orange"
        val prName15 = "Potato"
        val prName16 = "Carrot"
        val prName17 = "Bread"
        val prName18 = "Butter"
        val prName19 = "Sausage"
        val prName20 = "Pepsi"
        val prName21 = "Beer"
        val prName22 = "Honey"
        val prName23 = "Cake"
        val prName24 = "Sugar"

        createProduct(mainViewModel, prName1, catId1, listOf(ul, uml))
        createProduct(mainViewModel, prName2, catId1, listOf(ukg, ugr))
        createProduct(mainViewModel, prName3, catId2, listOf(ukg, ugr, upcs))
        createProduct(mainViewModel, prName4, catId2, listOf(ukg, ugr, upcs))
        createProduct(mainViewModel, prName5, catId2, listOf(ukg, ugr, upcs))
        createProduct(mainViewModel, prName6, catId3, listOf(ukg, ugr))
        createProduct(mainViewModel, prName7, catId3, listOf(ukg, ugr))
        createProduct(mainViewModel, prName8, catId3, listOf(ukg, ugr))
        createProduct(mainViewModel, prName9, catId4, listOf(ukg, ugr, upcs))
        createProduct(mainViewModel, prName10, catId4, listOf(ukg, ugr, upcs))

        createProduct(mainViewModel, prName11, catId5, listOf(ukg, ugr))
        createProduct(mainViewModel, prName12, catId5, listOf(ukg, ugr))
        createProduct(mainViewModel, prName13, catId5, listOf(ukg, ugr))
        createProduct(mainViewModel, prName14, catId4, listOf(ukg, ugr, upcs))
        createProduct(mainViewModel, prName15, catId5, listOf(ukg, ugr))
        createProduct(mainViewModel, prName16, catId5, listOf(ukg, ugr))
        createProduct(mainViewModel, prName17, catId8, listOf(upcs))
        createProduct(mainViewModel, prName18, catId8, listOf(ukg, ugr, upcs))
        createProduct(mainViewModel, prName19, catId3, listOf(ukg, ugr))
        createProduct(mainViewModel, prName20, catId7, listOf(ul, uml))

        createProduct(mainViewModel, prName21, catId7, listOf(ul, uml))
        createProduct(mainViewModel, prName22, catId6, listOf(ukg, ugr))
        createProduct(mainViewModel, prName23, catId6, listOf(upcs))
        createProduct(mainViewModel, prName24, catId6, listOf(ukg, ugr))
    }

    fun createProduct(
        mainViewModel: MainViewModel,
        name: String,
        catId: Int,
        units: List<UnitEntity>
    ) {
        val product = ProductEntity(0, name, catId)
        mainViewModel.insert(product, units)
    }
}