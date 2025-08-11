package com.shoppingplanning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.shoppingplanning.data.MainViewModel
import com.shoppingplanning.navigation.BottomNavItem
import com.shoppingplanning.navigation.NavigationSelectedItem
import com.shoppingplanning.navigation.ScreenNavItem
import com.shoppingplanning.screen.CatalogTab
import com.shoppingplanning.screen.PackageScreen
import com.shoppingplanning.screen.PlanningScreen
import com.shoppingplanning.screen.PlanningTab
import com.shoppingplanning.screen.ShoppingScreen
import com.shoppingplanning.screen.ShoppingTab
import com.shoppingplanning.screen.StatisticTab

class MainActivity : ComponentActivity() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainViewModel = (application as MyApp).mainViewModel
        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        val items = listOf(
            BottomNavItem.Catalogs,
            BottomNavItem.Planning,
            BottomNavItem.Shopping,
            BottomNavItem.Statistic
        )
        var navigationSelectedItem = remember {
            mutableStateOf<NavigationSelectedItem>(
                NavigationSelectedItem(BottomNavItem.Catalogs, ScreenNavItem.CatalogTab, 0,"")
            )
        }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = item == navigationSelectedItem.value.selectedTabItem,
                            onClick = {
                                when (item) {
                                    is BottomNavItem.Catalogs -> navigationSelectedItem.value =
                                        NavigationSelectedItem(item, ScreenNavItem.CatalogTab, 0,"")

                                    is BottomNavItem.Planning -> navigationSelectedItem.value =
                                        NavigationSelectedItem(item, ScreenNavItem.PlanningTab, 0,"")

                                    is BottomNavItem.Shopping -> navigationSelectedItem.value =
                                        NavigationSelectedItem(item, ScreenNavItem.ShoppingTab, 0,"")

                                    is BottomNavItem.Statistic -> navigationSelectedItem.value =
                                        NavigationSelectedItem(item, ScreenNavItem.StatisticTab, 0,"")

                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .background(colorResource(id = R.color.main_background))
                    .padding(innerPadding),
                contentAlignment = Alignment.Companion.Center
            ) {
                when (navigationSelectedItem.value.selectedScreen) {
                    is ScreenNavItem.PackagesScreen -> PackageScreen(
                        mainViewModel,
                        navigationSelectedItem
                    )

                    is ScreenNavItem.PlanningScreen -> PlanningScreen(
                        mainViewModel,
                        navigationSelectedItem
                    )

                    is ScreenNavItem.ShoppingScreen -> ShoppingScreen(
                        mainViewModel,
                        navigationSelectedItem
                    )

                    is ScreenNavItem.CatalogTab -> CatalogTab(mainViewModel)
                    is ScreenNavItem.PlanningTab -> PlanningTab(
                        mainViewModel,
                        navigationSelectedItem
                    )

                    is ScreenNavItem.ShoppingTab -> ShoppingTab(
                        mainViewModel,
                        navigationSelectedItem
                    )
                    is ScreenNavItem.StatisticTab -> StatisticTab(mainViewModel)
                }
            }
        }
    }
}