package com.shoppingplanning.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoppingplanning.HeaderLine
import com.shoppingplanning.R
import com.shoppingplanning.data.MainViewModel

@Composable
fun CatalogTab(mainViewModel: MainViewModel) {
    var productModeState = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.main_background))
            .padding(all = 2.dp),
    ) {
        if (productModeState.value) {
            HeaderLine("Product")
        } else {
            HeaderLine("Category")
        }

        ChooseTypeOfScreen(productModeState)

        if (productModeState.value) {
            CatalogProductScreen(mainViewModel)
        } else {
            CatalogCategoryScreen(mainViewModel)
        }
    }
}

@Composable
fun ChooseTypeOfScreen(state: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = state.value,
            onClick = { state.value = true }
        )
        Text(
            "Product",
            modifier = Modifier
                .clickable { state.value = true },
            fontSize = 18.sp
        )

        RadioButton(
            modifier = Modifier.padding(start = 20.dp),
            selected = !state.value,
            onClick = { state.value = false }
        )
        Text(
            "Category",
            modifier = Modifier
                .clickable { state.value = false },
            fontSize = 18.sp
        )
    }
}
