package com.shoppingfoodcart.firstassigment

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class AssigmentApplication {

    companion object {
        var imagesDataSourceJob = Job()
        val coroutineScope = CoroutineScope(imagesDataSourceJob + Dispatchers.Main)
    }



}