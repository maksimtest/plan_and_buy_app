package com.shoppingplanning.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoppingplanning.CreateButton
import com.shoppingplanning.HeaderLine
import com.shoppingplanning.IntegerTextField
import com.shoppingplanning.NeumorphicBox
import com.shoppingplanning.R
import com.shoppingplanning.data.MainViewModel
import com.shoppingplanning.info.ActiveItemInfo
import com.shoppingplanning.navigation.NavigationSelectedItem
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.shoppingplanning.util.StringUtil
import com.shoppingplanning.TitleLine
import com.shoppingplanning.entity.ItemEntity

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ShoppingScreen(
    mainViewModel: MainViewModel,
    navSelectedItem: MutableState<NavigationSelectedItem>
) {
    val packages by mainViewModel.packages.collectAsState()
    val activeItems by mainViewModel.activeItems.collectAsState()
    var packageId = navSelectedItem.value.packageId
    val packageMatch = packages.find { it.id == packageId }
    val readOnlyState = remember { mutableStateOf(true) }
    var isNewState by remember { mutableStateOf(true) }
    var titleState by remember { mutableStateOf("") }
    var checkboxVisible = true

    if (packageMatch != null) {
        isNewState = packageMatch.isNew
        checkboxVisible = packageMatch.isNew
        titleState = "${packageMatch.percent}%"
    }

    val filteredActives = activeItems.filter { it.packageId == packageId }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.main_background))
            .padding(all = 2.dp),
    ) {

        HeaderLine("Shopping")

        TitleLine("package: ${navSelectedItem.value.packageName}")

        if (!filteredActives.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 10.dp, top = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (isNewState) {
                    if (readOnlyState.value) {
                        CreateButton("start") {
                            readOnlyState.value = false
                        }
                    } else {
                        CreateButton("finish") {
                            readOnlyState.value = true
                            isNewState = false
                            mainViewModel.finishShopping(packageId)

                        }
                    }
                } else {
                    Text(titleState)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(6.dp)
            ) {
                items(filteredActives) { item ->
                    ShoppingItem(mainViewModel, checkboxVisible, item, readOnlyState)
                }
            }
        }

        if (filteredActives.isEmpty()) {
            Row(modifier = Modifier
                .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
                ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 30.dp, horizontal = 10.dp),
                    text = "Empty shopping list should filled on Planning Tab!",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun ShoppingItem(
    mainViewModel: MainViewModel,
    checkboxVisible: Boolean,
    item: ActiveItemInfo,
    readOnlyState: MutableState<Boolean>
) {
    var countState = remember { mutableIntStateOf(item.planCount) }
    var editModeState = remember { mutableStateOf(false) }
    var checkboxSelectedState = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        NeumorphicBox(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            editModeState.value = true
                        }
                    )
                },
            backgroundColor = colorResource(id = R.color.main_background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (!editModeState.value && checkboxVisible) {
                    Checkbox(
                        checked = checkboxSelectedState.value,
                        enabled = !readOnlyState.value,
                        onCheckedChange = {
                            checkboxSelectedState.value = !checkboxSelectedState.value
                            editModeState.value = checkboxSelectedState.value
                        }
                    )
                }
                if (editModeState.value) {
                    Column(
                        modifier = Modifier
                            .width(70.dp)
                            .padding(start = 2.dp, end = 4.dp)
                            .border(1.dp, Color.DarkGray, RoundedCornerShape(2.dp))
                    ) {
                        IntegerTextField(countState)
                    }

                    Text(text = item.unitCode)

                    CreateButton("Save") {
                        editModeState.value = false
                        val factCount = countState.intValue
                        val newItem = ItemEntity(
                            item.id,
                            item.productId,
                            item.packageId,
                            item.unitId,
                            item.planCount,
                            factCount,
                            item.num,
                            true,// ??
                            StringUtil.getYear(),
                            StringUtil.getMonth(),
                            StringUtil.getShortDate(),0,""
                        )
                        mainViewModel.update(newItem)
                    }
                } else {
                    Text("${item.factCount} ${item.unitCode}")
                }
                Text(
                    text = "Need:\n ${item.planCount} ${item.unitCode}",
                    modifier = Modifier
                        .width(120.dp)
                        .align(Alignment.CenterVertically)
                        //.border(1.dp, Color.DarkGray, RoundedCornerShape(2.dp))
                        .padding(end = 10.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    style = TextStyle(
                        textAlign = TextAlign.Center
                    ),
                    color = colorResource(id = R.color.second_text_color)
                )
                Text(
                    text = item.productName,
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.CenterVertically)
                        .padding(start = 10.dp)
                    ,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    color = colorResource(id = R.color.main_text_color)
                )

            }
        }
    }
}