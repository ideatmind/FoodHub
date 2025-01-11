package com.foodhub.fooddelivery.data

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodHubSession @Inject constructor(val context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("foodhub", Context.MODE_PRIVATE)

    fun storeToken(token: String) {
        sharedPref.edit().putString("token", token).apply()
    }

    fun getToken(): String? {
        sharedPref.getString("token", null)?.let {
            return it
        }
        return null
    }
}