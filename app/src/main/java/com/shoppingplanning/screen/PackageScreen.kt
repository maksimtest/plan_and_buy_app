package com.shoppingplanning.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoppingplanning.CreateButton
import com.shoppingplanning.HeaderLine
import com.shoppingplanning.NeumorphicBox
import com.shoppingplanning.R
import com.shoppingplanning.util.StringUtil
import com.shoppingplanning.data.MainViewModel
import com.shoppingplanning.entity.PackageEntity
import com.shoppingplanning.navigation.BottomNavItem
import com.shoppingplanning.navigation.NavigationSelectedItem
import com.shoppingplanning.navigation.ScreenNavItem

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PackageScreen(
    mainViewModel: MainViewModel,
    navSelectedItem: MutableState<NavigationSelectedItem>
) {
    val shoppingMode = navSelectedItem.value.selectedTabItem == BottomNavItem.Shopping
    val packages = mainViewModel.packages.collectAsState()
    val activeItems = mainViewModel.activeItems.collectAsState()
    val productSize = mainViewModel.productsWithUnits.value.size

    val countOfNewP = packages.value.filter { it.isNew }.size
    var count = remember { mutableStateOf(countOfNewP) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.main_background))
            .padding(all = 2.dp),
    ) {
        if (shoppingMode) {
            HeaderLine("Choose packages")
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            HeaderLine("Packages")
            CreateButton("+") {
                count.value++
                val name = if (count.value <= 1) "NEW" else "NEW ${count.value}"
                mainViewModel.update(PackageEntity(0, name, StringUtil.getYear(),name, "empty", true, 0))
            }
        }

        LazyColumn {
            items(packages.value) { item ->
                val count = activeItems.value.filter { it.packageId == item.id }.size
                item.description = "$count positions"
                if(shoppingMode || item.isNew)
                PackageItem(item, shoppingMode) {

                    navSelectedItem.value = if (shoppingMode)
                        NavigationSelectedItem(
                            BottomNavItem.Shopping,
                            ScreenNavItem.ShoppingScreen,
                            item.id, item.name
                        ) else NavigationSelectedItem(
                        BottomNavItem.Planning,
                        ScreenNavItem.PlanningScreen,
                        item.id, item.name
                    )
                }
            }
        }
        val titleText = if(productSize == 0) "Empty catalogs, need to create some products!" else "Need to create new package!"
        if (packages.value.none { shoppingMode || it.isNew }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 30.dp, horizontal = 10.dp),
                    text = "Need to create new package!",
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun PackageItem(packageItem: PackageEntity, shoppingMode: Boolean, onClick: () -> Unit) {
    var bcGrColor = colorResource(id = R.color.main_background)
    if (packageItem.isNew) bcGrColor = colorResource(id = R.color.main_background1)

    var destination = "Planning"
    var description = packageItem.description
    if(shoppingMode){
        if(packageItem.isNew){
            destination = "Shopping"
        } else {
            description = "${packageItem.percent}%"
            destination = "Details"
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            //.height(40.dp)
            .padding(vertical = 8.dp, horizontal = 4.dp)
            // .background(bcGrColor)
            .clickable { onClick() },

        ) {
        NeumorphicBox(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = bcGrColor
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 10.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = packageItem.name,
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.CenterVertically)
                        .padding(start = 10.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    color = colorResource(id = R.color.main_text_color)
                )
                Text(
                    text = description,
                    modifier = Modifier
                    ,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    color = colorResource(id = R.color.second_text_color)
                )
                Column(
                    modifier = Modifier
                        .padding(all=2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "go to..."
                    )
                    Text(destination)
                }
            }
        }
    }
}


