package com.example.mystoryapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.StringBuilder

class AccountPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val LOGIN_KEY = booleanPreferencesKey("login_key")
    private val NAME = stringPreferencesKey("name")
    private val TOKEN = stringPreferencesKey("token")
    private val EMAIL = stringPreferencesKey("email")
    private val PASS = stringPreferencesKey("password")


    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME] ?: ""
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun saveAccountLogin(name : String, email : String, pass : String) {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = true
            preferences[NAME] = name
            preferences[PASS] = pass
            preferences[EMAIL] = email
        }
    }

    suspend fun setToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AccountPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): AccountPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AccountPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}