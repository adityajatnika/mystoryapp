package com.example.mystoryapp.data.di

import android.content.Context
import com.example.mystoryapp.data.StoryDatabase
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}