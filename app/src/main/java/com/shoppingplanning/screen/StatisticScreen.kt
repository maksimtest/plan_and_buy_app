package com.shoppingplanning.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoppingplanning.HeaderLine
import com.shoppingplanning.NeumorphicBox
import com.shoppingplanning.R
import com.shoppingplanning.util.StringUtil
import com.shoppingplanning.data.MainViewModel
import com.shoppingplanning.info.StatItemInfo
import com.shoppingplanning.info.StatPeriodInfo


@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun StatisticScreen(
    mainViewModel: MainViewModel
) {
    val items = mainViewModel.statItems.collectAsState()
    val periods = mainViewModel.statPeriods.collectAsState()

    val currentIndexState = remember { mutableIntStateOf(0) }
    Log.d("MyTag", "StatisticScreen: init currentIndexState.intValue=${currentIndexState.intValue}")
    mainViewModel.updateData(currentIndexState)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.main_background))
            .padding(all = 2.dp),
    ) {

        HeaderLine("Statistic")

        PeriodSpinner(mainViewModel, periods.value, currentIndexState) {
            mainViewModel.updateData(currentIndexState)
        }


        if (items.value.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 30.dp),
                    text = "No statistic information!",
                    fontSize = 20.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(6.dp)
            ) {
                items(items.value) { item ->
                    StatisticItem(item)
                }
            }

        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PeriodSpinner(
    mainViewModel: MainViewModel,
    periods: List<StatPeriodInfo>,
    currentIndexState: MutableIntState,
    onChangePeriod: () -> Unit
) {
    val activeColor = colorResource(id = R.color.active_cursor)
    val noActiveColor = colorResource(id = R.color.no_active_cursor)

    val previousColorState = remember { mutableStateOf(noActiveColor) }
    val nextColorState = remember { mutableStateOf(noActiveColor) }

    Log.d(
        "MyTag",
        "try init previousColorState.value = activeColor, (periods.size > 1)=${periods.size > 1}," +
                "(currentIndexState.intValue==periods.size-1)=${currentIndexState.intValue == periods.size - 1}" +
                ",(periods.size)=${periods.size}, (currentIndexState.intValue)=${currentIndexState.intValue}"
    )
    if (periods.size > 1 && currentIndexState.intValue == 0) {
        previousColorState.value = activeColor
    }

    Log.d("MyTag", "PeriodSpinner_1, currentIndex=${currentIndexState.value}")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all=20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .width(54.dp)
                .height(54.dp)
                .border(1.dp, Color.DarkGray, RoundedCornerShape(2.dp))
                .background(previousColorState.value)
                .clickable(
                    onClick = {
                        Log.d(
                            "MyTag",
                            "clickPrev, currentIndexState.intValue=${currentIndexState.intValue}"
                        )
                        if (periods.size - 1 > currentIndexState.intValue) {
                            currentIndexState.intValue++
                            Log.d(
                                "MyTag",
                                "clickPrev,_1. become currentIndexState.intValue=${currentIndexState.intValue}"
                            )

                            onChangePeriod()

                            if (currentIndexState.intValue > 0 &&
                                periods.size > 1
                            ) {
                                Log.d(
                                    "MyTag",
                                    "clickPrev_2,previousColorState.value = noActiveColor"
                                )
                                previousColorState.value = noActiveColor
                            }
                            if (periods.size > 1) {
                                nextColorState.value = activeColor
                                Log.d("MyTag", "clickPrev_3, nextColorState.value = activeColor")
                            }
                        } else onChangePeriod()
                    }
                )
        )
        {
            Image(
                modifier = Modifier
                    .width(54.dp)
                    .height(54.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "go to..."
            )
        }

        Text(
            text = "${mainViewModel.selectedDateYear.value}\n${StringUtil.getNameMonth(mainViewModel.selectedDateMonth.value)}",
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier
                .width(54.dp)
                .height(54.dp)
                .border(1.dp, Color.DarkGray, RoundedCornerShape(2.dp))
                .background(nextColorState.value)
                .clickable(
                    onClick = {
                        Log.d(
                            "MyTag",
                            "clickNext, currentIndexState.intValue=${currentIndexState.intValue}"
                        )
                        if (currentIndexState.intValue > 0) {
                            currentIndexState.intValue--
                            Log.d(
                                "MyTag",
                                "clickNext,_1. become currentIndexState.intValue=${currentIndexState.intValue}"
                            )

                            onChangePeriod()

                            if (currentIndexState.intValue == 0) {
                                nextColorState.value = noActiveColor
                                Log.d("MyTag", "clickNext_2, nextColorState.value = noActiveColor")
                            }
                            if (periods.size > 1) {
                                previousColorState.value = activeColor
                                Log.d(
                                    "MyTag",
                                    "clickNext_3, previousColorState.value = activeColor"
                                )
                            }
                        }
                    }
                )
        ) {
            Image(
                modifier = Modifier
                    .width(54.dp)
                    .height(54.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "go to..."
            )
        }
    }
}

@Composable
fun StatisticItem(item: StatItemInfo) {

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
                        ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text=item.product,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(start = 10.dp, top=6.dp, bottom=6.dp)
                )
                Text(text="${item.count} ${item.unit}",
                    fontSize = 22.sp,
                    modifier = Modifier
                        .width(120.dp)
                        .padding(end = 10.dp)
                )

            }
        }
    }
}