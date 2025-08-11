package com.shoppingplanning.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoppingplanning.CreateButton
import com.shoppingplanning.CreateTextField
import com.shoppingplanning.NeumorphicBox
import com.shoppingplanning.R
import com.shoppingplanning.data.MainViewModel
import com.shoppingplanning.entity.CategoryEntity
import com.shoppingplanning.showMsg


@Composable
fun CatalogCategoryScreen(mainViewModel: MainViewModel) {
    val categories by mainViewModel.categories.collectAsState()

    CategoryNewLineContainer(mainViewModel)

    LazyColumn {
        items(categories) { category ->
            CategoryItem(mainViewModel,category)
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CategoryNewLineContainer(mainViewModel: MainViewModel) {
    var categoryInputText = remember { mutableStateOf("") }
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(start = 10.dp, end=8.dp, top=10.dp, bottom = 10.dp)
            .fillMaxWidth()
            .background(colorResource(id = R.color.main_background))
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BoxWithConstraints {
            val widthPx = with(LocalDensity.current) { maxWidth - 120.dp }
            CreateTextField(categoryInputText, "new category name", widthPx)
        }

        CreateButton("Save") {
            val categoryName = categoryInputText.value
            if(categoryName.isEmpty()) {
                showMsg(context, "Category name should be entered!")
            } else if(mainViewModel.isDuplicateCategoryName(categoryName)){
                showMsg(context, "Such category name already exists!")
            } else {
                addCategory(mainViewModel, categoryName, context)
                categoryInputText.value = ""
            }
        }
    }
}

@Composable
fun CategoryItem(mainViewModel: MainViewModel, category: CategoryEntity) {
    val editingMode = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start=8.dp, end=12.dp, top=10.dp, bottom = 10.dp)

            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        editingMode.value = true
                    }
                )
            }
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
                    .padding(all = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if(editingMode.value){
                    val categoryNameEditingState = remember { mutableStateOf(category.name) }
                    CreateTextField(categoryNameEditingState, "new category name", 100.dp)
                    CreateButton("Cancel") {
                        editingMode.value = false
                    }
                    CreateButton("Save") {
                        val categoryName = categoryNameEditingState.value
                        if(categoryName.isEmpty()) {
                            showMsg(context, "Category name should be entered!")
                        } else if(categoryName == category.name){
                            editingMode.value = false
                        } else {
                           mainViewModel.update(CategoryEntity(category.id, categoryName, category.description))
                        }
                    }

                } else {
                    Text(
                        text = category.name,
                        modifier = Modifier
                            .padding(start = 10.dp),
                        fontSize = 20.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Default,
                        color = colorResource(id = R.color.main_text_color)
                    )
                    Text(
                        text = "",
                        modifier = Modifier
                            .width(120.dp)
                        ,
                        fontSize = 16.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Default,
                        color = colorResource(id = R.color.second_text_color)
                    )
                }
            }
        }
    }
}

fun addCategory(viewModel: MainViewModel, name: String,context:Context) {
        viewModel.update(CategoryEntity(0, name, "-"))
}
