package com.shoppingfoodcart.firstassigment.datasource.reporsitory

import android.content.Context
import androidx.lifecycle.LiveData
import com.shoppingfoodcart.firstassigment.datasource.network.RetrofitClientInstance
import com.shoppingfoodcart.firstassigment.models.generalModels.MoviesListModel
import com.shoppingfoodcart.firstassigment.models.generalModels.Results
import com.shoppingfoodcart.firstassigment.utils.network.ResultWrapper
import com.shoppingfoodcart.firstassigment.utils.network.safeApiCall
import com.shoppingfoodcart.firstassigment.utils.room.MoviesListDao
import com.shoppingfoodcart.firstassigment.utils.room.MyRoomDataBase


class DataRepository(ctx: Context, retroInstance: RetrofitClientInstance) :
    BaseRepository(ctx, retroInstance) {

    // ============================= Remote Data API Calls  ============================= //

    suspend fun getMoviesList(pageNumber: Int): ResultWrapper<MoviesListModel> {
        return safeApiCall(dispatcher) {
            getApiService().getPopMovies(pageNumber)
        }
    }


    suspend fun getSearchMovie(movieName: String, pageNumber: Int): ResultWrapper<MoviesListModel> {
        return safeApiCall(dispatcher) {
            getApiService().getSearchMovie(movieName, pageNumber)
        }
    }


    // ============================= Room database  ============================= //


    private val moviesListDao: MoviesListDao = MyRoomDataBase.getDatabase(context).moviesListDao()

//    private val userDetailsDao: MoviesDetailsDao = MyRoomDataBase.getDatabase(context).moviesDetailsDao()


    suspend fun insertMovie(movieModel: Results) {
        moviesListDao.insert(movieModel)
    }

    suspend fun getMovies(): List<Results> {
        return moviesListDao.getAllMovies()
    }

    suspend fun deleteMovies(movieModel: Results) {
        moviesListDao.delete(movieModel)
    }

}