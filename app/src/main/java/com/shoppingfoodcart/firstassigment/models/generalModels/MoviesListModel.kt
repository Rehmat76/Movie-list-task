package com.shoppingfoodcart.firstassigment.models.generalModels

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MoviesListModel (

    @SerializedName("page"          ) var page         : Int?               = null,
    @SerializedName("results"       ) var results      : ArrayList<Results> = arrayListOf(),
    @SerializedName("total_pages"   ) var totalPages   : Int?               = null,
    @SerializedName("total_results" ) var totalResults : Int?               = null

)