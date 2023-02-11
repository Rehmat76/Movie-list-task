package com.shoppingfoodcart.firstassigment.datasource.reporsitory

import com.shoppingfoodcart.firstassigment.models.networkModels.ApiErrorResponse


interface BaseDataSource {
    fun onPayloadError(error: ApiErrorResponse)
}
