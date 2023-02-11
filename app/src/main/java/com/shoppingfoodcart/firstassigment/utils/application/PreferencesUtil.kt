package com.shoppingfoodcart.firstassigment.utils.application

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shoppingfoodcart.firstassigment.models.generalModels.Results
import com.shoppingfoodcart.firstassigment.utils.general.AppConstants
import java.util.*
import kotlin.collections.ArrayList

class PreferencesUtil constructor(context: Context, prefFileName: String = AppConstants.PREF_FILE) {
    private val mPrefs: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    // Save Methods
    fun <S> saveObject(key: String, value: S) {
        val string = Gson().toJson(value)
        saveString(key, string)
    }

    fun <S> saveList(key: String, items: List<S>) {
        val json = Gson().toJson(items)
        mPrefs.edit().putString(key, json).apply()
    }
    fun saveString(key: String, value: String) {
        mPrefs.edit().putString(key, value).apply()
    }

    fun saveInt(key: String, value: Int) {
        mPrefs.edit().putInt(key, value).apply()
    }

    fun saveLong(key: String, value: Long) {
        mPrefs.edit().putLong(key, value).apply()
    }

    fun saveBoolean(key: String, value: Boolean) {
        mPrefs.edit().putBoolean(key, value).apply()
    }

    // Get Methods
    fun <S> getObject(key: String, type: Class<S>): S? {
        val json = getString(key) ?: return null
        return Gson().fromJson(json, type)
    }

    fun <S> getList(key: String): ArrayList<S> {
        val json = getString(key)
        val type = object : TypeToken<ArrayList<S>>() {

        }.type
        val list = Gson().fromJson<ArrayList<S>>(json, type)
        return list ?: ArrayList()
    }

    fun getString(key: String): String? {
        return mPrefs.getString(key, null)
    }

    fun getLong(key: String): Long {
        return mPrefs.getLong(key, Date().time)
    }

    fun getInt(key: String): Int {
        return mPrefs.getInt(key, 0)
    }

    fun getBoolean(key: String): Boolean {
        return mPrefs.getBoolean(key, false)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mPrefs.getBoolean(key, defaultValue)
    }

    fun remove(key: String) {
        mPrefs.edit().remove(key).apply()
    }

    fun clearAll() {
        mPrefs.edit().clear().apply()
    }

    fun loadData() : ArrayList<Results> {
        var movieListData = ArrayList<Results>()
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
//    val sharedPreferences: SharedPreferences =
//        requireContext().getSharedPreferences("shared preferences", Context.MODE_PRIVATE)

        // creating a variable for gson.
        val gson = Gson()

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        val json = mPrefs.getString("movieList", null)

        // below line is to get the type of our array list.
        val type = object : TypeToken<ArrayList<Results>>() {}.type

        // in below line we are getting data from gson
        // and saving it to our array list
        movieListData = gson.fromJson<Any>(json, type) as ArrayList<Results>

        // checking below if the array list is empty or not
        if (movieListData == null) {
            // if the array list is empty
            // creating a new array list.
            movieListData = ArrayList()
        }
        return movieListData
    }

    fun saveData(movieList : ArrayList<Results>) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
//    val sharedPreferences: SharedPreferences =
//        requireContext().getSharedPreferences("shared preferences", Context.MODE_PRIVATE)

        // creating a variable for editor to
        // store data in shared preferences.
        val editor = mPrefs.edit()

        // creating a new variable for gson.
        val gson = Gson()

        // getting data from gson and storing it in a string.
        val json = gson.toJson(movieList)

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("movieList", json)

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply()

        // after saving data we are displaying a toast message.
//        showToast("Saved Array List to Shared preferences. ")
    }

    @SuppressLint("SuspiciousIndentation")
    fun loadSearchData() : ArrayList<String> {

//        val gson = Gson()
//        val json = mPrefs.getString("searchedList", null)
//        if (json != null) {
//            val type = object : TypeToken<ArrayList<String>>() {}.type
//            searchMovieList = gson.fromJson<Any>(json, type) as ArrayList<String>

        var searchMovieList = ArrayList<String>()
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
//    val sharedPreferences: SharedPreferences =
//        requireContext().getSharedPreferences("shared preferences", Context.MODE_PRIVATE)

        // creating a variable for gson.
        val gson = Gson()

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        val json = mPrefs.getString("searchedList", null)

            if (json != null) {
                // below line is to get the type of our array list.
                val type = object : TypeToken<ArrayList<String>>() {}.type

                // in below line we are getting data from gson
                // and saving it to our array list
                searchMovieList = gson.fromJson<Any>(json, type) as ArrayList<String>
            }
        // checking below if the array list is empty or not
        if (searchMovieList == null) {
            // if the array list is empty
            // creating a new array list.
            searchMovieList = ArrayList()
        }
        return searchMovieList
    }
    fun saveSearchData(movieList : ArrayList<String>) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
//    val sharedPreferences: SharedPreferences =
//        requireContext().getSharedPreferences("shared preferences", Context.MODE_PRIVATE)

        // creating a variable for editor to
        // store data in shared preferences.
        val editor = mPrefs.edit()

        // creating a new variable for gson.
        val gson = Gson()

        // getting data from gson and storing it in a string.
        val json = gson.toJson(movieList)

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("searchedList", json)

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply()

        // after saving data we are displaying a toast message.
//        showToast("Saved Array List to Shared preferences. ")
    }
}

