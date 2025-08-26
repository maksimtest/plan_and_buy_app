package com.shoppingplanning.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.shoppingplanning.CreateButton
import com.shoppingplanning.CreateTextField
import com.shoppingplanning.NeumorphicBox
import com.shoppingplanning.R
import com.shoppingplanning.data.MainViewModel
import com.shoppingplanning.entity.CategoryEntity
import com.shoppingplanning.entity.ProductEntity
import com.shoppingplanning.entity.UnitEntity
import com.shoppingplanning.info.ProductDto
import com.shoppingplanning.screen.items.ProductLineState
import com.shoppingplanning.showMsg
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun CatalogProductScreen(mainViewModel: MainViewModel) {
    val categories by mainViewModel.categories.collectAsState()
    val products by mainViewModel.productsWithUnits.collectAsState()

    ProductNewLineContainer(mainViewModel, categories)

    GroupedProductList(products)

}

@Composable
fun ProductNewLineContainer(mainViewModel: MainViewModel, categories: List<CategoryEntity>) {
    val productLines = remember { mutableStateListOf<ProductLineState>() }
    val units = mainViewModel.units

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.main_background))
            .padding(vertical = 2.dp, horizontal = 2.dp)
    ) {
        CreateButton("+") {
            productLines.add(ProductLineState())
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.padding(start = 14.dp, end = 8.dp)
        ) {
            itemsIndexed(productLines) { index, lineState ->
                NewProductLine(
                    mainViewModel = mainViewModel,
                    state = lineState,
                    units,
                    categories = categories,
                    onUpdate = { updatedLine ->
                        productLines[index] = updatedLine
                    },
                    onDelete = {
                        productLines.removeAt(index)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun NewProductLine(
    mainViewModel: MainViewModel,
    state: ProductLineState,
    units: List<UnitEntity>,
    categories: List<CategoryEntity>,
    onUpdate: (ProductLineState) -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current

    val selectedUnitStates = remember { mutableStateMapOf<String, Boolean>() }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var productNameState = remember { mutableStateOf("") }

    Column {
        // First Line with 'product name' and 'select category' fields
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val nameFieldWidthPx = with(LocalDensity.current) { 100.dp }
            val categoryFieldWidthPx = with(LocalDensity.current) { 150.dp }

            // 1. Product name
            CreateTextField(productNameState, "Product", nameFieldWidthPx)


            // 2. Category selector with dropdown
            Box(
                modifier = Modifier
                    .width(categoryFieldWidthPx)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        }
                ) {
                    OutlinedTextField(
                        value = state.selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Transparent layout for click handler
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                onUpdate(state.copy(isDropdownExpanded = true))
                            }
                    )
                }
                // Dropdown for category list
                DropdownMenu(
                    expanded = state.isDropdownExpanded,
                    onDismissRequest = {
                        onUpdate(state.copy(isDropdownExpanded = false))
                    },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                onUpdate(
                                    state.copy(
                                        selectedCategory = category.name,
                                        isDropdownExpanded = false
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
        // Second line with units elements and 'save' button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BoxWithConstraints {
                val unitsWidthPx = with(LocalDensity.current) { maxWidth - 70.dp }

                // 3. Unit elements
                FlowRow(
                    modifier = Modifier
                        .padding(vertical = 6.dp, horizontal = 0.dp)
                        .width(unitsWidthPx)
                    ,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
                        2.dp
                    ),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(2.dp)
                ) {
                    units.forEach { unit ->
                        if (selectedUnitStates[unit.code] == null) selectedUnitStates[unit.code] =
                            false
                        val isSelected = selectedUnitStates[unit.code] == true
                        val borderColor =
                            if (isSelected) Color.DarkGray else colorResource(id = R.color.hidden_text_color)
                        val textColor =
                            if (isSelected) colorResource(id = R.color.main_text_color) else colorResource(
                                id = R.color.hidden_text_color
                            )
                        val backgroundColor =
                            if (isSelected) colorResource(id = R.color.main_background1) else colorResource(
                                id = R.color.main_background
                            )
                        val borderWidth = if (isSelected) 2.dp else 1.dp
                        val horizontalPadding = 6.dp

                        Box() {
                            Text(
                                text = unit.code,
                                color = textColor,
                                modifier = Modifier
                                    .padding(
                                        start = horizontalPadding,
                                        end = 6.dp,
                                        top = 6.dp,
                                        bottom = 6.dp
                                    )
                                    .background(backgroundColor)
                                    .border(borderWidth, borderColor, RoundedCornerShape(6.dp))
                                    .padding(all = 6.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable {
                                        val current = selectedUnitStates[unit.code] == true
                                        selectedUnitStates[unit.code] = !current
                                    }
                            )
                        }
                    }
                }
            }
            val context = LocalContext.current
            // 4. Save button
            CreateButton("Save") {
                val selectedCategoryName = state.selectedCategory
                val matchedCategory = categories.find { it.name == selectedCategoryName }

                val selectedUnitList = mutableListOf<UnitEntity>()
                units.forEach {
                    if (selectedUnitStates[it.code] == true) {
                        selectedUnitList.add(it)
                    }
                }

                val productName = productNameState.value
                Log.d("MyTag", "productName=$productName, isD=${mainViewModel.isDuplicateProductName(productName)}")
                if (productName.isEmpty()) {
                    showMsg(context, "Product name should be entered")
                } else if (mainViewModel.isDuplicateProductName(productName)) {
                    showMsg(context, "Such product name already exists!")
                } else if (matchedCategory == null) {
                    showMsg(context, "Category should be selected")
                } else if (selectedUnitList.isEmpty()) {
                    showMsg(context, "Any units should be selected")
                } else {
                    val newProduct = ProductEntity(0, productName, matchedCategory.id)
                    mainViewModel.insert(newProduct, selectedUnitList)
                    onDelete()
                }

            }
        }
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color.DarkGray)
    )

}

@Composable
fun GroupedProductList(products: List<ProductDto>) {
    val grouped = products.groupBy { it.categoryName }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp)
    ) {
        grouped.forEach { (category, items) ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp)
                    ,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.description_text_color),
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    )
                }
            }
            items(items) { product ->
                ProductItem(product)
            }
        }
    }
}

@Composable
fun ProductItem(product: ProductDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp)
        ,
        horizontalArrangement = Arrangement.Center
    ) {
        NeumorphicBox(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = colorResource(id = R.color.main_background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.name,
                    modifier = Modifier
                        .padding(start = 10.dp),
                    fontSize = 20.sp,
                    maxLines = 1,
                    fontFamily = FontFamily.Default,
                    color = colorResource(id = R.color.main_text_color)
                )
                product.units?.forEach {
                    Text(it.unitCode)
                }
            }
        }
    }
}

