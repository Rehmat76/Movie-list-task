package com.shoppingfoodcart.firstassigment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoppingfoodcart.firstassigment.models.networkModels.BaseResponse
import com.shoppingfoodcart.firstassigment.models.networkModels.ErrorResponse
import com.shoppingfoodcart.firstassigment.utils.general.OneShotEvent
import com.shoppingfoodcart.firstassigment.utils.network.ResultWrapper


open class BaseAndroidViewModel() : ViewModel() {


    val snackbarMessage = MutableLiveData<OneShotEvent<String>>()
    val errorResponse = MutableLiveData<OneShotEvent<ErrorResponse>>()
    val progressBar = MutableLiveData<OneShotEvent<Boolean>>()
    val loader = MutableLiveData<OneShotEvent<Boolean>>()


    protected fun showSnackbarMessage(message: String) {
        snackbarMessage.value = OneShotEvent(message)
    }

    protected fun showNetworkError() {
        snackbarMessage.value = OneShotEvent("Something went wrong \n Please try again later")
    }

    protected fun handleErrorType(error: ResultWrapper<Any>) {
        when (error) {
            is ResultWrapper.NetworkError ->
                showNetworkError()
            is ResultWrapper.GenericError ->
                showSnackbarMessage("" + error.error?.message)
        }
    }

    protected fun isSuccess(result: BaseResponse): Boolean {
        if (result.code == 200) {
            return true
        } else {
            showSnackbarMessage(result.message)
            return false
        }
    }


    protected fun showProgressBar(show: Boolean) {
        progressBar.value = OneShotEvent(show)
    }


}