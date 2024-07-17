package com.example.twittercounter.util

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()


    fun setAccessToken(accessToken: String) {
        editor.putString(Constants.ACCESS_TOKEN, accessToken)
        editor.commit()
    }

    fun setAccessTokenSecret(accessTokenSecret: String) {
        editor.putString(Constants.ACCESS_TOKEN_SECRET, accessTokenSecret)
        editor.commit()
    }

    fun getAccessTokenSecret(): String? {
        return sharedPreferences.getString(Constants.ACCESS_TOKEN_SECRET, null)
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(Constants.ACCESS_TOKEN, null)
    }

}