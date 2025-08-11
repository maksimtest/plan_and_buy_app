package com.shoppingplanning

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.shoppingplanning.data.AppDatabase
import com.shoppingplanning.data.AppRepository
import com.shoppingplanning.data.MainViewModel
import com.shoppingplanning.data.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MyApp : Application(), ViewModelStoreOwner {

    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    override val viewModelStore: ViewModelStore
        get() = appViewModelStore

    lateinit var mainViewModel: MainViewModel
        private set

    lateinit var repository: AppRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        val db = AppDatabase.getInstance(this)
        repository = AppRepository(db)

        val factory = MainViewModelFactory(repository)

        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        applicationScope.launch {
            mainViewModel.init()
        }
    }
}
