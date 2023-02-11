package com.shoppingfoodcart.firstassigment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shoppingfoodcart.firstassigment.datasource.reporsitory.DataRepository
import com.shoppingfoodcart.firstassigment.models.generalModels.MoviesListModel
import com.shoppingfoodcart.firstassigment.models.generalModels.Results
import com.shoppingfoodcart.firstassigment.utils.network.ResultWrapper
import kotlinx.coroutines.launch

class MoviesViewModel(private val dataRepository: DataRepository) : BaseAndroidViewModel() {
    private val _moviesList = MutableLiveData<MoviesListModel>()
    private val _searchMoviesList = MutableLiveData<MoviesListModel>()

    private val _moviesListFav = MutableLiveData<List<Results>>()

    val movieList: LiveData<MoviesListModel>
        get() = _moviesList

    val searchMoviesList: LiveData<MoviesListModel>
        get() = _searchMoviesList

    val movieListFav: LiveData<List<Results>>
        get() = _moviesListFav

    fun getMoviesList(pageNumber: Int) {
        viewModelScope.launch {
            dataRepository.getMoviesList(
                pageNumber
            ).let { response ->
                showProgressBar(false)
                when (response) {
                    is ResultWrapper.Success -> {
                        _moviesList.value = response.value
                        //sendDataToDB(_userList.value!!)
                    }
                    else -> handleErrorType(response)
                }
            }
        }
    }

    fun getSearchMoviesList(searchName : String, pageNumber: Int) {
        viewModelScope.launch {
            dataRepository.getSearchMovie(searchName,
                pageNumber
            ).let { response ->
                showProgressBar(false)
                when (response) {
                    is ResultWrapper.Success -> {
                        _searchMoviesList.value = response.value
                    }
                    else -> handleErrorType(response)
                }
            }
        }
    }

    fun getFavroiteList(){
        viewModelScope.launch {
            dataRepository.getMovies().let {
                showProgressBar(false)
                _moviesListFav.value = it
            }
        }
    }

    fun deleteData(item : Results){
        viewModelScope.launch {
            dataRepository.deleteMovies(item)
        }
    }

    fun insertData(item : Results){
        viewModelScope.launch {
            dataRepository.insertMovie(item)
        }
    }

}