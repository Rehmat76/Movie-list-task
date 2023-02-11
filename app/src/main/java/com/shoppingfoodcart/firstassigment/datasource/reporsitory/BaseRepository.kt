package com.shoppingfoodcart.firstassigment.datasource.reporsitory

import android.content.Context
import com.google.gson.Gson
import com.shoppingfoodcart.firstassigment.datasource.network.ApiService
import com.shoppingfoodcart.firstassigment.datasource.network.RetrofitClientInstance
import com.shoppingfoodcart.firstassigment.utils.network.ErrorUtils
import com.shoppingfoodcart.firstassigment.utils.network.SessionManager
import com.shoppingfoodcart.firstassigment.utils.network.isOnline

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


open class BaseRepository(ctx: Context,var retroInstance: RetrofitClientInstance) {

    var context: Context
    var sessionManager: SessionManager? = null
    val dispatcher: CoroutineDispatcher = Dispatchers.IO

    var gson = Gson()

    init {
        context = ctx
        sessionManager = SessionManager(context)

    }


    fun getApiService(): ApiService {
        return retroInstance.getService()
    }


    fun checkInternetConnection(callback: BaseDataSource): Boolean {
        if (!isOnline(context)) {
            callback.onPayloadError(
                ErrorUtils.parseError(
                    "{\"message\":\"Please check internet connection and try again\",\"code\":\"0\",\"name\":\"error\"}"
                )
            )
            return true
        }
        return false
    }


}