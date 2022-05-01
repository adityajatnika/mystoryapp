package com.example.mystoryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapp.data.remote.retrofit.ApiServices
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.data.remote.response.StoryResponse

class StoryRepository (private val quoteDatabase: StoryDatabase, private val apiService: ApiServices) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        Log.e(this.toString(), "sampai storyrepo")
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }
}