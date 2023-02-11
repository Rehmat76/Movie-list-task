package com.shoppingfoodcart.firstassigment.utils.room

import androidx.room.Dao
import androidx.room.Query
import com.shoppingfoodcart.firstassigment.models.generalModels.Results

@Dao
interface MoviesListDao : BaseDao<Results> {

    @Query("SELECT * from movies_table ORDER BY id ASC")
    suspend fun getAllMovies(): List<Results>

}