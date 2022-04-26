package com.example.mystoryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.mystoryapp.data.local.SessionManager
import com.example.mystoryapp.data.local.room.StoryDatabase
import com.example.mystoryapp.data.remote.retrofit.ApiServices
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.ui.activity.MainActivity

class StoryRepository (private val storyDatabase: StoryDatabase, private val apiService: ApiServices) {
    private lateinit var sessionManager: SessionManager

    @OptIn(ExperimentalPagingApi::class)
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        sessionManager = SessionManager(MainActivity.context)
        val token = sessionManager.fetchAuthToken().toString()
        Log.e(this.toString(), "sampai storyrepo")
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
//                StoryPagingSource(apiService, token)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}