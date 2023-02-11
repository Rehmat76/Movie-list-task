package com.shoppingfoodcart.firstassigment.utils.network

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.shoppingfoodcart.firstassigment.datasource.network.RetrofitClientInstance
import com.shoppingfoodcart.firstassigment.utils.general.AppConstants.Companion.FCM_TOKEN
import com.shoppingfoodcart.firstassigment.utils.general.AppConstants.Companion.IS_LOGGED_IN
import com.shoppingfoodcart.firstassigment.utils.general.AppConstants.Companion.KEY_AUTH
import com.shoppingfoodcart.firstassigment.utils.general.AppConstants.Companion.PREF_NAME


class SessionManager {
    var context: Context? = null
    var pref: SharedPreferences

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGGED_IN, false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        with(pref.edit()) {
            putBoolean(IS_LOGGED_IN, isLoggedIn)
            apply()
        }
    }





    fun setFCMToken(token:String) {
        with(pref.edit()) {
            putString(FCM_TOKEN, token)
            apply()
        }
    }


    fun getFCMToken(): String {
        return pref.getString(FCM_TOKEN, "").toString()
    }




    fun setAuthToken(email: String?) {
        with(pref.edit()) {
            putString(KEY_AUTH, email)
            apply()
        }
        RetrofitClientInstance.getInstance(context!!)?.initRetrofit()
    }

    fun getAuthToken(): String? {
        return pref.getString(KEY_AUTH, "")
    }



    fun logout() {
        setLoggedIn(false)
        clearAppPreferences()
    }


    private fun clearAppPreferences() {
        with(pref.edit()) {
            clear().apply()
        }
    }

    fun getLocale(): String {
        return "en"
    }

}