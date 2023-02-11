package com.shoppingfoodcart.firstassigment.utils.application

import com.shoppingfoodcart.firstassigment.datasource.network.RetrofitClientInstance
import com.shoppingfoodcart.firstassigment.datasource.reporsitory.DataRepository
import com.shoppingfoodcart.firstassigment.viewmodel.MoviesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    factory { RetrofitClientInstance(get()) }
    factory { DataRepository(get(),get()) }
}

val viewModelModules = module {
    viewModel { MoviesViewModel(get()) }
}
