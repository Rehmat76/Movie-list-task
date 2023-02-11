package com.shoppingfoodcart.firstassigment.datasource.network


import com.shoppingfoodcart.firstassigment.models.generalModels.MoviesListModel
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiService {

    @GET("movie/popular?api_key=e5ea3092880f4f3c25fbc537e9b37dc1")
    suspend fun getPopMovies(@Query("page") pageNumber: Int): MoviesListModel

    @GET("search/movie?api_key=e5ea3092880f4f3c25fbc537e9b37dc1")
    suspend fun getSearchMovie(@Query("query") movie_name: String, @Query("page") pageNumber: Int): MoviesListModel

}