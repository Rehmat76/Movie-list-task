package com.shoppingfoodcart.firstassigment.models.networkModels

import java.io.Serializable

class ErrorResponse(var message: String, var code: Int) : Serializable {

}