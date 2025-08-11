package com.shoppingplanning.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.shoppingplanning.CreateButton
import com.shoppingplanning.HeaderLine
import com.shoppingplanning.IntegerTextField
import com.shoppingplanning.NeumorphicBox
import com.shoppingplanning.R
import com.shoppingplanning.TitleLine
import com.shoppingplanning.data.MainViewModel
import com.shoppingplanning.entity.ItemEntity
import com.shoppingplanning.entity.ProductUnitEntity
import com.shoppingplanning.info.ActiveItemInfo
import com.shoppingplanning.info.ProductDto
import com.shoppingplanning.navigation.NavigationSelectedItem
import com.shoppingplanning.screen.items.PlanningLineState
import com.shoppingplanning.util.StringUtil

@Composable
fun PlanningScreen(
    mainViewModel: MainViewModel,
    navSelectedItem: MutableState<NavigationSelectedItem>
) {
    val activeItems by mainViewModel.activeItems.collectAsState()
    val packageId = navSelectedItem.value.packageId
    val activeItemByCurrentPackage = activeItems.filter { it.packageId == packageId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.main_background))
            .padding(all = 2.dp),
    ) {

        HeaderLine("Planning")
        TitleLine("package: ${navSelectedItem.value.packageName}")
        PlanningNewLineContainer(mainViewModel, packageId)


        if (activeItemByCurrentPackage.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 30.dp),
                    text = "You may add new position!",
                    fontSize = 20.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(6.dp)
            ) {
                items(activeItemByCurrentPackage) { item ->
                    PlanningItem(item, packageId)
                }
            }

        }
    }
}

@Composable
fun PlanningNewLineContainer(mainViewModel: MainViewModel, packageId: Int) {
    val planningLines = remember { mutableStateListOf<PlanningLineState>() }
    val products by mainViewModel.productsWithUnits.collectAsState()

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.main_background))
            .padding(vertical = 2.dp, horizontal = 0.dp)
            //.border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
    ) {
        CreateButton("+") {
            planningLines.add(PlanningLineState())
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.padding(start = 4.dp, end = 8.dp)
        ) {
            itemsIndexed(planningLines) { index, lineState ->
                NewPlanningLine(
                    mainViewModel = mainViewModel,
                    state = lineState,
                    packageId = packageId,
                    products = products,

                    onUpdate = { updatedLine ->
                        planningLines[index] = updatedLine
                    },
                    onDelete = {
                        planningLines.removeAt(index)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun NewPlanningLine(
    mainViewModel: MainViewModel,
    state: PlanningLineState,
    packageId: Int,
    products: List<ProductDto>,
    onUpdate: (PlanningLineState) -> Unit,
    onDelete: () -> Unit
) {
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var countState = remember { mutableIntStateOf(0) }
    val selectedUnitState = remember { mutableStateOf("") }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
        ) {
            val productFieldWidthPx = with(LocalDensity.current) { 140.dp }

            // 1. Product name
            Box(
                modifier = Modifier
                    .width(productFieldWidthPx)

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        }
                ) {
                    OutlinedTextField(
                        value = state.selectedProductName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Product") },
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 0.dp)

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
                    products.forEach { product ->
                        DropdownMenuItem(
                            text = { Text(product.name) },
                            onClick = {
                                onUpdate(
                                    state.copy(
                                        selectedProductName = product.name,
                                        isDropdownExpanded = false,
                                        units = product.units ?: emptyList()
                                    )
                                )
                                if (product.units != null && product.units!!.size == 1) {
                                    selectedUnitState.value == product.units!!.get(0).unitCode
                                }
                            }
                        )
                    }
                }
            }
            // 2. Count blocks
            Column(
                modifier = Modifier
                    .width(70.dp)
                    .padding(start = 6.dp)
                    .border(1.dp, Color.DarkGray, RoundedCornerShape(2.dp))
            ) {
                IntegerTextField(countState)
            }
            // 3. Units blocks
            FlowRow(
                modifier = Modifier
                    .padding(vertical = 0.dp, horizontal = 6.dp)
                    .width(80.dp)
                    .border(1.dp, Color.DarkGray, RoundedCornerShape(2.dp))
                ,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
                    2.dp
                ),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(2.dp)
            ) {
                state.units.forEach { unit ->
                    val isSelected = selectedUnitState.value == unit.unitCode
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
                    Box() {
                        Text(
                            text = unit.unitCode,
                            color = textColor,
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .background(backgroundColor)
                                .border(borderWidth, borderColor, RoundedCornerShape(6.dp))
                                .padding(all = 4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable {
                                    Log.d("MyTag", "click ${unit.unitCode}")
                                    selectedUnitState.value = unit.unitCode
                                }
                        )
                    }
                }
            }

            // 4. Save button
            CreateButton("+") {
                val selectedProductName = state.selectedProductName
                val selectedUnitCode = selectedUnitState.value

                val matchedProduct = products.find { it.name == selectedProductName }

                var matchedUnit: ProductUnitEntity? = null
                if (matchedProduct != null) {
                    matchedUnit = matchedProduct.units?.find { it.unitCode == selectedUnitCode }

                    Log.d("MyTag", "new ItemEntity: selectedUnitCode=${selectedUnitCode}")
                    Log.d(
                        "MyTag",
                        "new ItemEntity: matchedUnit=${matchedUnit}, matchedProduct=${matchedProduct}"
                    )
                    if (matchedUnit != null) {

                        val newItem =
                            ItemEntity(
                                0,
                                matchedProduct.id,
                                packageId,
                                matchedUnit.unitId,
                                countState.value,
                                0,
                                1,
                                true,
                                StringUtil.getYear(),
                                StringUtil.getMonth(),
                                "",
                                0,
                                ""
                            )
                        Log.d("MyTag", "new ItemEntity, packageId=$packageId, ")
                        mainViewModel.update(newItem)
                        onDelete()
                    }
                }
            }
        }
    }
}


@Composable
fun PlanningItem(
    item: ActiveItemInfo, packageId: Int
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .fillMaxWidth(),
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
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                //    .height(60.dp)
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
                //.background(colorResource(id = R.color.teal_200))
                //              .background(bcGrColor))
            ) {
                Text(
                    text = item.productName,
                    modifier = Modifier
                        .width(120.dp)
                        .align(Alignment.CenterVertically),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    color = colorResource(id = R.color.main_text_color)
                )
                Text(
                    text = "${item.planCount} ${item.unitCode}",
                    modifier = Modifier
                        .width(120.dp)
                        .align(Alignment.CenterVertically)
                        .padding(end = 10.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    color = colorResource(id = R.color.second_text_color)
                )
            }
        }
    }
}


