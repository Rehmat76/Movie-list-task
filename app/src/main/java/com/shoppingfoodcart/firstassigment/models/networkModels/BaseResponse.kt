package com.shoppingfoodcart.firstassigment.models.networkModels

import java.io.Serializable

open class BaseResponse(
    val code: Int = 0,
    var message: String = ""
) : Serializable