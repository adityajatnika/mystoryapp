package com.example.mystoryapp.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.mystoryapp.R

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_EMAIL = "user_email"
        const val USER_PASS = "user_pass"
        const val USER_NAME = "user_name"

    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String?) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun saveUserInfo(name : String, email: String, pass: String){
        val editor = prefs.edit()
        editor.putString(USER_NAME, name)
        editor.putString(USER_PASS, pass)
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }


    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}